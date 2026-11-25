// Configuração compartilhada pelos cenários. Tudo pode ser sobrescrito com -e VAR=valor.

export const BASE_URL = (__ENV.BASE_URL || 'http://localhost:8080').replace(/\/$/, '');
export const USERNAME = __ENV.K6_USERNAME || 'admin';
export const PASSWORD = __ENV.K6_PASSWORD || 'admin';

const HOSTS_LOCAIS = ['localhost', '127.0.0.1', 'host.docker.internal'];

// Trava de segurança: os cenários criam e excluem dados e geram carga.
// Produção roda no plano gratuito do Northflank (256MB) e não deve ser alvo.
export function garantirAlvoLocal() {
    const host = BASE_URL.replace(/^https?:\/\//, '').split(/[:/]/)[0];
    if (!HOSTS_LOCAIS.includes(host) && __ENV.ALLOW_REMOTE !== 'true') {
        throw new Error(
            `BASE_URL aponta para "${host}", que não é local. ` +
            'Os testes de performance só devem rodar contra a API local. ' +
            'Se tiver certeza, use -e ALLOW_REMOTE=true.'
        );
    }
}

// Opções comuns a todos os cenários.
// Por padrão o k6 limpa os cookies a cada iteração, o que derrubaria a sessão
// e o token CSRF. Mantê-los simula um usuário que entra uma vez e segue navegando.
export const OPCOES_BASE = {
    noCookiesReset: true,
};

// Limites aplicados a todos os cenários. Cada cenário pode acrescentar os seus.
export const THRESHOLDS_PADRAO = {
    http_req_failed: ['rate<0.01'],
    checks: ['rate>0.99'],
};
