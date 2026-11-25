// Carga: uso normal esperado, mantido por alguns minutos.
// 70% dos usuários consultam e 30% cadastram, com tempo de "pensar" entre ações.
// Ajuste com -e VUS=<n> -e DURACAO=<tempo>.

import { garantirAlvoLocal, OPCOES_BASE, THRESHOLDS_PADRAO } from '../lib/config.js';
import { fluxoCadastro, fluxoConsulta, criarMassa, excluirMassa } from '../lib/fluxos.js';

const VUS = Number(__ENV.VUS || 10);
const DURACAO = __ENV.DURACAO || '3m';

const vusConsulta = Math.max(1, Math.round(VUS * 0.7));
const vusCadastro = Math.max(1, VUS - vusConsulta);

function estagios(alvo) {
    return [
        { duration: '30s', target: alvo }, // aquecimento
        { duration: DURACAO, target: alvo }, // patamar
        { duration: '30s', target: 0 }, // desaceleração
    ];
}

export const options = {
    ...OPCOES_BASE,
    scenarios: {
        consulta: { executor: 'ramping-vus', startVUs: 0, stages: estagios(vusConsulta), exec: 'consulta' },
        cadastro: { executor: 'ramping-vus', startVUs: 0, stages: estagios(vusCadastro), exec: 'cadastro' },
    },
    thresholds: {
        ...THRESHOLDS_PADRAO,
        'http_req_duration{scenario:consulta}': ['p(95)<500'],
        'http_req_duration{scenario:cadastro}': ['p(95)<800'],
    },
};

export function setup() {
    garantirAlvoLocal();
    return criarMassa(10);
}

export function consulta(massa) {
    fluxoConsulta(massa);
}

export function cadastro() {
    fluxoCadastro();
}

export function teardown(massa) {
    excluirMassa(massa);
}
