// Estresse: aumenta a carga em degraus para descobrir onde a API começa a degradar.
// Os thresholds são mais folgados e servem de alerta, não de meta.
// Ajuste o pico com -e VUS_MAX=<n>.

import { garantirAlvoLocal, OPCOES_BASE, THRESHOLDS_PADRAO } from '../lib/config.js';
import { fluxoCadastro, fluxoConsulta, criarMassa, excluirMassa } from '../lib/fluxos.js';

const VUS_MAX = Number(__ENV.VUS_MAX || 100);

function degraus(proporcao) {
    const alvo = (fracao) => Math.max(1, Math.round(VUS_MAX * fracao * proporcao));
    return [
        { duration: '1m', target: alvo(0.25) },
        { duration: '2m', target: alvo(0.25) },
        { duration: '1m', target: alvo(0.5) },
        { duration: '2m', target: alvo(0.5) },
        { duration: '1m', target: alvo(1) },
        { duration: '2m', target: alvo(1) },
        { duration: '1m', target: 0 }, // recuperação
    ];
}

export const options = {
    ...OPCOES_BASE,
    scenarios: {
        consulta: { executor: 'ramping-vus', startVUs: 0, stages: degraus(0.7), exec: 'consulta' },
        cadastro: { executor: 'ramping-vus', startVUs: 0, stages: degraus(0.3), exec: 'cadastro' },
    },
    thresholds: {
        ...THRESHOLDS_PADRAO,
        http_req_failed: ['rate<0.05'],
        checks: ['rate>0.95'],
        http_req_duration: ['p(95)<2000'],
    },
};

export function setup() {
    garantirAlvoLocal();
    return criarMassa(20);
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
