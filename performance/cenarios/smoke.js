// Smoke: 1 usuário, poucas iterações. Confirma que os fluxos funcionam
// antes de rodar carga ou estresse.

import { garantirAlvoLocal, OPCOES_BASE, THRESHOLDS_PADRAO } from '../lib/config.js';
import { fluxoCadastro, fluxoConsulta, criarMassa, excluirMassa } from '../lib/fluxos.js';

export const options = {
    ...OPCOES_BASE,
    scenarios: {
        cadastro: { executor: 'per-vu-iterations', vus: 1, iterations: 2, exec: 'cadastro' },
        consulta: { executor: 'per-vu-iterations', vus: 1, iterations: 3, exec: 'consulta', startTime: '1s' },
    },
    thresholds: {
        ...THRESHOLDS_PADRAO,
        http_req_failed: ['rate==0'],
        checks: ['rate==1'],
    },
};

export function setup() {
    garantirAlvoLocal();
    return criarMassa(2);
}

export function cadastro() {
    fluxoCadastro();
}

export function consulta(massa) {
    fluxoConsulta(massa);
}

export function teardown(massa) {
    excluirMassa(massa);
}
