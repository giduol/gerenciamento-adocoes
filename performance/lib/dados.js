// Geração de payloads. Nome, CPF, CNPJ e e-mail precisam ser únicos
// (validação de nome duplicado e colunas unique no banco), por isso cada
// chamada combina o prefixo "k6", o VU, um contador do VU e o horário.
// Os nomes de ONG e adotante têm no máximo 30 caracteres no banco,
// então o sufixo usa base 36 para ficar curto (ex.: "k6 Adotante 1a-3f-x8k2qp").

let contador = 0;

function sufixo() {
    contador += 1;
    const horario = Date.now().toString(36).slice(-6);
    return `${__VU.toString(36)}-${contador.toString(36)}-${horario}`;
}

function digitos(tamanho) {
    let s = '';
    for (let i = 0; i < tamanho; i++) {
        s += Math.floor(Math.random() * 10);
    }
    return s;
}

export function ongForm() {
    const id = sufixo();
    return {
        name: `k6 ONG ${id}`,
        cnpj: digitos(14),
        email: `k6-ong-${id}@exemplo.test`,
        contact: 'Contato k6',
        phone: `11${digitos(9)}`,
    };
}

export function animalForm() {
    return {
        name: `k6 Animal ${sufixo()}`,
        species: 'Cachorro',
        birthDate: '2022-03-15',
        breed: 'SRD',
        specialCharacteristics: 'Gerado pelo teste de performance',
        knownDiseases: null,
    };
}

export function adotanteForm() {
    const id = sufixo();
    return {
        name: `k6 Adotante ${id}`,
        cpf: digitos(11),
        address: 'Rua do Teste, 123',
        email: `k6-adotante-${id}@exemplo.test`,
        phone: `11${digitos(9)}`,
    };
}

export function adocaoForm(ongId, animalId, adotanteId, status = 'PENDENTE') {
    return { ongId, animalId, adotanteId, status };
}

export function acompanhamentoForm() {
    return {
        periodDays: 30,
        animalWithAdopter: true,
        animalHealthy: true,
        animalReturned: false,
        animalNotFound: false,
        methods: ['IN_PERSON', 'PHOTOS'],
        notes: 'Acompanhamento gerado pelo teste de performance.',
    };
}
