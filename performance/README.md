# Testes de performance (k6)

Scripts [k6](https://grafana.com/docs/k6/latest/) para medir a API localmente. Eles autenticam com sessão e CSRF, exercitam ONGs, animais, adotantes, adoções e acompanhamentos, e excluem os dados que criam ao final.

> **Nunca rode contra produção.** O serviço no Northflank tem 256MB e cairia sob carga. Por isso os scripts recusam qualquer `BASE_URL` que não seja local, a não ser que você passe `-e ALLOW_REMOTE=true`.

## Estrutura

```
performance/
├── cenarios/
│   ├── smoke.js     # 1 usuário, poucas iterações: valida que tudo funciona
│   ├── carga.js     # uso normal sustentado (padrão: 10 VUs por 3 min)
│   └── estresse.js  # degraus até o pico (padrão: 100 VUs) para achar o limite
└── lib/
    ├── config.js    # BASE_URL, credenciais, trava de alvo local, thresholds
    ├── api.js       # login + CSRF e chamadas por recurso
    ├── dados.js     # payloads com nomes/CPF/e-mail únicos
    └── fluxos.js    # fluxos de cadastro e de consulta
```

Nos cenários de carga e estresse, 70% dos usuários fazem o fluxo de **consulta** (listagens e detalhes sobre uma massa criada no `setup`) e 30% fazem o fluxo de **cadastro** (cria a cadeia completa, consulta, atualiza e exclui).

## Pré-requisitos

1. Banco local e API rodando, conforme o [README principal](../README.md):

   ```powershell
   docker compose up -d db
   mvn spring-boot:run
   ```

2. Confirme que a API está usando o **banco local** (porta 5433), não o de produção. Os testes gravam e excluem dados.
3. Um usuário para login. O padrão é `admin`/`admin`.

## Como rodar

### Com Docker (não precisa instalar o k6)

```powershell
docker compose run --rm k6 run cenarios/smoke.js
docker compose run --rm k6 run cenarios/carga.js
docker compose run --rm k6 run cenarios/estresse.js
```

O container acessa a API em `http://host.docker.internal:8080`. Para outro endereço, defina `K6_BASE_URL`.

### Com o k6 instalado

```powershell
k6 run performance/cenarios/smoke.js
```

Nesse caso o padrão é `http://localhost:8080`.

## Parâmetros

Passe com `-e NOME=valor`, por exemplo `docker compose run --rm k6 run -e VUS=20 -e DURACAO=5m cenarios/carga.js`.

| Variável        | Padrão                  | Uso                                         |
|-----------------|-------------------------|---------------------------------------------|
| `BASE_URL`      | `http://localhost:8080` | Endereço da API                             |
| `K6_USERNAME`   | `admin`                 | Usuário do login                            |
| `K6_PASSWORD`   | `admin`                 | Senha do login                              |
| `VUS`           | `10`                    | Usuários simultâneos no cenário de carga    |
| `DURACAO`       | `3m`                    | Tempo de patamar no cenário de carga        |
| `VUS_MAX`       | `100`                   | Pico do cenário de estresse                 |
| `ALLOW_REMOTE`  | —                       | `true` libera um `BASE_URL` não local       |
| `DEBUG`         | —                       | `true` mostra status e corpo de cada check que falhar |

## Critérios (thresholds)

O k6 termina com código de saída diferente de zero se algum critério falhar.

| Cenário  | Critérios                                                                 |
|----------|---------------------------------------------------------------------------|
| smoke    | nenhuma requisição com erro e 100% dos checks                             |
| carga    | erros < 1%, checks > 99%, p95 < 500ms nas consultas e < 800ms nos cadastros |
| estresse | erros < 5%, checks > 95%, p95 < 2s                                        |

As métricas saem agrupadas por endpoint (por exemplo, `GET /ongs/{id}`), então dá para ver qual rota está lenta.

## Observações

- As listagens (`GET /ongs`, `/animais`, etc.) não são paginadas e devolvem todos os registros. Quanto mais dados no banco, mais lentas elas ficam, e isso aparece nos resultados.
- Se um teste for interrompido no meio (Ctrl+C), registros com nome iniciado por `k6 ` podem ficar no banco. Exclua pela API ou recrie o banco local.
