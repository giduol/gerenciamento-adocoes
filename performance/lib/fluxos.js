// Fluxos de negócio reaproveitados pelos cenários.

import { group, sleep } from 'k6';
import { garantirLogin, ongs, animais, adotantes, adocoes, acompanhamentos } from './api.js';
import { ongForm, animalForm, adotanteForm, adocaoForm, acompanhamentoForm } from './dados.js';

function pausa() {
    sleep(0.5 + Math.random()); // simula o tempo de "pensar" do usuário
}

// Cria a cadeia completa (ONG, animal, adotante, adoção e acompanhamento).
// Devolve o que conseguiu criar, mesmo se alguma etapa falhar, para permitir a limpeza.
export function criarCadeia() {
    const cadeia = {};
    cadeia.ong = ongs.criar(ongForm());
    cadeia.animal = animais.criar(animalForm());
    cadeia.adotante = adotantes.criar(adotanteForm());
    if (cadeia.ong && cadeia.animal && cadeia.adotante) {
        cadeia.adocao = adocoes.criar(adocaoForm(cadeia.ong.id, cadeia.animal.id, cadeia.adotante.id));
    }
    if (cadeia.adocao) {
        cadeia.acompanhamento = acompanhamentos.criar(cadeia.adocao.id, acompanhamentoForm());
    }
    return cadeia;
}

// Remove na ordem exigida pela API: a adoção primeiro (leva junto os acompanhamentos),
// depois os cadastros que ela referencia.
export function excluirCadeia(cadeia) {
    if (cadeia.adocao) adocoes.excluir(cadeia.adocao.id);
    if (cadeia.animal) animais.excluir(cadeia.animal.id);
    if (cadeia.adotante) adotantes.excluir(cadeia.adotante.id);
    if (cadeia.ong) ongs.excluir(cadeia.ong.id);
}

// Ciclo de escrita: cria, consulta, atualiza e exclui tudo o que criou.
export function fluxoCadastro() {
    garantirLogin();
    let cadeia = {};
    try {
        group('cadastro', () => {
            cadeia = criarCadeia();
        });
        pausa();

        group('consulta do cadastro', () => {
            if (cadeia.adocao) adocoes.obter(cadeia.adocao.id);
            if (cadeia.adocao) acompanhamentos.listarPorAdocao(cadeia.adocao.id);
            if (cadeia.acompanhamento) acompanhamentos.obter(cadeia.acompanhamento.id);
        });
        pausa();

        group('atualizacao', () => {
            if (cadeia.animal) {
                animais.atualizar(cadeia.animal.id, { ...animalForm(), name: cadeia.animal.name, breed: 'Vira-lata' });
            }
            if (cadeia.adocao) {
                adocoes.atualizar(
                    cadeia.adocao.id,
                    adocaoForm(cadeia.ong.id, cadeia.animal.id, cadeia.adotante.id, 'APROVADA')
                );
            }
        });
        pausa();
    } finally {
        group('exclusao', () => excluirCadeia(cadeia));
    }
}

// Ciclo de leitura sobre a massa criada no setup().
export function fluxoConsulta(massa) {
    garantirLogin();
    const item = massa[Math.floor(Math.random() * massa.length)];

    // Listas completas: usadas nos selects do cadastro de adoção.
    group('listagens', () => {
        ongs.listar();
        animais.listar();
        adotantes.listar();
        adocoes.listar();
    });
    pausa();

    // Primeira página de cada relatório.
    group('relatorios paginados', () => {
        ongs.listarPagina();
        animais.listarPagina();
        adotantes.listarPagina();
        adocoes.listarPagina();
    });
    pausa();

    group('detalhes', () => {
        ongs.obter(item.ong.id);
        animais.obter(item.animal.id);
        adotantes.obter(item.adotante.id);
        adocoes.obter(item.adocao.id);
        acompanhamentos.listarPorAdocao(item.adocao.id);
        acompanhamentos.listarPorCpf(item.adotante.cpf);
    });
    pausa();
}

// Massa de dados para os cenários de leitura.
export function criarMassa(quantidade) {
    garantirLogin();
    const massa = [];
    for (let i = 0; i < quantidade; i++) {
        const cadeia = criarCadeia();
        if (cadeia.adocao) {
            massa.push(cadeia);
        } else {
            excluirCadeia(cadeia);
        }
    }
    if (massa.length === 0) {
        throw new Error('Não foi possível criar a massa de dados. Veja os checks acima.');
    }
    return massa;
}

export function excluirMassa(massa) {
    garantirLogin();
    massa.forEach(excluirCadeia);
}
