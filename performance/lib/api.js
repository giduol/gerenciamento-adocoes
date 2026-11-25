// Cliente HTTP da API: autenticação por sessão + CSRF e chamadas por recurso.
// O k6 mantém um cookie jar por VU. Como os cenários usam noCookiesReset (OPCOES_BASE),
// JSESSIONID e XSRF-TOKEN sobrevivem entre iterações e cada VU faz login uma única vez.

import http from 'k6/http';
import { check, fail } from 'k6';
import { BASE_URL, USERNAME, PASSWORD } from './config.js';

let csrf = null; // { headerName, token } do VU atual

function obterCsrf() {
    const res = http.get(`${BASE_URL}/auth/csrf`, { tags: { name: 'GET /auth/csrf' } });
    if (!check(res, { 'csrf 200': (r) => r.status === 200 })) {
        fail(`Não foi possível obter o token CSRF (status ${res.status}). A API está rodando em ${BASE_URL}?`);
    }
    csrf = res.json();
}

function headers(extra = {}) {
    return {
        'Content-Type': 'application/json',
        [csrf.headerName]: csrf.token,
        ...extra,
    };
}

export function login() {
    obterCsrf();
    const res = http.post(
        `${BASE_URL}/auth/login`,
        JSON.stringify({ username: USERNAME, password: PASSWORD }),
        { headers: headers(), tags: { name: 'POST /auth/login' } }
    );
    if (!check(res, { 'login 200': (r) => r.status === 200 })) {
        fail(`Login falhou (status ${res.status}). Confira K6_USERNAME/K6_PASSWORD.`);
    }
    // O id da sessão muda no login; renova o token para garantir que está associado à nova sessão.
    obterCsrf();
}

// Faz login uma única vez por VU (o estado do módulo é isolado por VU).
export function garantirLogin() {
    if (csrf === null) {
        login();
    }
}

function esperar(res, status, nome) {
    const ok = check(res, { [`${nome} ${status}`]: (r) => r.status === status });
    if (!ok && __ENV.DEBUG === 'true') {
        console.warn(`${nome}: esperado ${status}, recebido ${res.status} ${String(res.body).slice(0, 300)}`);
    }
    return res;
}

function criar(caminho, corpo, nome) {
    const res = esperar(
        http.post(`${BASE_URL}${caminho}`, JSON.stringify(corpo), { headers: headers(), tags: { name: nome } }),
        201,
        nome
    );
    return res.status === 201 ? res.json() : null;
}

function listar(caminho, nome) {
    return esperar(http.get(`${BASE_URL}${caminho}`, { tags: { name: nome } }), 200, nome);
}

// Listagem paginada (?page&size): além do status, confere o formato da página.
function listarPagina(caminho, page, size, nome) {
    const res = http.get(`${BASE_URL}${caminho}?page=${page}&size=${size}`, { tags: { name: nome } });
    const ok = check(res, {
        [`${nome} 200`]: (r) => r.status === 200,
        [`${nome} formato`]: (r) => {
            const corpo = r.json();
            return Array.isArray(corpo.content) && corpo.content.length <= size && corpo.totalElements >= corpo.content.length;
        },
    });
    if (!ok && __ENV.DEBUG === 'true') {
        console.warn(`${nome}: status ${res.status} ${String(res.body).slice(0, 300)}`);
    }
    return res;
}

function obter(caminho, nome) {
    return esperar(http.get(`${BASE_URL}${caminho}`, { tags: { name: nome } }), 200, nome);
}

function atualizar(caminho, corpo, nome) {
    return esperar(
        http.put(`${BASE_URL}${caminho}`, JSON.stringify(corpo), { headers: headers(), tags: { name: nome } }),
        200,
        nome
    );
}

function excluir(caminho, nome) {
    return esperar(http.del(`${BASE_URL}${caminho}`, null, { headers: headers(), tags: { name: nome } }), 204, nome);
}

export const ongs = {
    criar: (form) => criar('/ongs', form, 'POST /ongs'),
    listar: () => listar('/ongs', 'GET /ongs'),
    listarPagina: (page = 0, size = 20) => listarPagina('/ongs', page, size, 'GET /ongs?page'),
    obter: (id) => obter(`/ongs/${id}`, 'GET /ongs/{id}'),
    atualizar: (id, form) => atualizar(`/ongs/${id}`, form, 'PUT /ongs/{id}'),
    excluir: (id) => excluir(`/ongs/${id}`, 'DELETE /ongs/{id}'),
};

export const animais = {
    criar: (form) => criar('/animais', form, 'POST /animais'),
    listar: () => listar('/animais', 'GET /animais'),
    listarPagina: (page = 0, size = 20) => listarPagina('/animais', page, size, 'GET /animais?page'),
    obter: (id) => obter(`/animais/${id}`, 'GET /animais/{id}'),
    atualizar: (id, form) => atualizar(`/animais/${id}`, form, 'PUT /animais/{id}'),
    excluir: (id) => excluir(`/animais/${id}`, 'DELETE /animais/{id}'),
};

export const adotantes = {
    criar: (form) => criar('/adotantes', form, 'POST /adotantes'),
    listar: () => listar('/adotantes', 'GET /adotantes'),
    listarPagina: (page = 0, size = 20) => listarPagina('/adotantes', page, size, 'GET /adotantes?page'),
    obter: (id) => obter(`/adotantes/${id}`, 'GET /adotantes/{id}'),
    atualizar: (id, form) => atualizar(`/adotantes/${id}`, form, 'PUT /adotantes/{id}'),
    excluir: (id) => excluir(`/adotantes/${id}`, 'DELETE /adotantes/{id}'),
};

export const adocoes = {
    criar: (form) => criar('/adocoes', form, 'POST /adocoes'),
    listar: () => listar('/adocoes', 'GET /adocoes'),
    listarPagina: (page = 0, size = 20) => listarPagina('/adocoes', page, size, 'GET /adocoes?page'),
    obter: (id) => obter(`/adocoes/${id}`, 'GET /adocoes/{id}'),
    atualizar: (id, form) => atualizar(`/adocoes/${id}`, form, 'PUT /adocoes/{id}'),
    // Também remove os acompanhamentos da adoção.
    excluir: (id) => excluir(`/adocoes/${id}`, 'DELETE /adocoes/{id}'),
};

export const acompanhamentos = {
    criar: (adocaoId, form) =>
        criar(`/adocoes/${adocaoId}/acompanhamentos`, form, 'POST /adocoes/{id}/acompanhamentos'),
    listarPorAdocao: (adocaoId) =>
        listar(`/adocoes/${adocaoId}/acompanhamentos`, 'GET /adocoes/{id}/acompanhamentos'),
    listarPorCpf: (cpf) => listar(`/acompanhamentos?cpf=${encodeURIComponent(cpf)}`, 'GET /acompanhamentos?cpf'),
    obter: (id) => obter(`/acompanhamentos/${id}`, 'GET /acompanhamentos/{id}'),
    excluir: (id) => excluir(`/acompanhamentos/${id}`, 'DELETE /acompanhamentos/{id}'),
};
