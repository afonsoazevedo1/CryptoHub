# CryptoHub

Aplicativo Android desenvolvido como desafio técnico para a vaga de Android Sênior na **Mercado Bitcoin**.

O app consome a API da [CoinMarketCap](https://coinmarketcap.com/api/documentation/v1/) para listar exchanges de criptomoedas e exibir seus detalhes, incluindo as moedas negociadas em cada uma.

## Funcionalidades

- **Listagem de exchanges**: exibe logo, nome, **Spot Volume (USD)** e data de lançamento com suporte a **Paginação com Shimmer**.
- **Detalhe da exchange**: exibe logo, nome, id, descrição, website clicável, taxas (maker/taker), **Spot Volume** e a lista de moedas negociadas com preço em USD.
- **Tema Dinâmico**: suporte automático a Dark e Light mode seguindo o sistema operacional.
- **UX Instantânea**: passagem de dados via rota para renderização imediata do cabeçalho de detalhes.

## Arquitetura

O projeto segue **Clean Architecture** com **MVVM** na camada de apresentação, focado em **Kotlin Multiplatform (KMP)** readiness:

```
com.example.cryptohub
├── core/            # Utilitários (Extensions, Result, Configurações)
├── data/
│   ├── remote/      # Ktor Resources + DTOs (api, dto)
│   ├── mapper/      # DTO -> Domain
│   └── repository/  # Implementação dos repositórios com estratégia de Retry
├── domain/
│   ├── model/       # Modelos de domínio puros
│   ├── repository/  # Contratos (interfaces) de repositório
│   └── usecase/     # Regras de negócio isoladas
├── di/              # Módulos Koin (App, Network, Repo, UseCase, ViewModel)
└── presentation/
    ├── screens/
    │   ├── list/    # Tela de listagem (ViewModel + Composables)
    │   └── detail/  # Tela de detalhe (ViewModel + Composables)
    ├── navigation/  # Navigation Compose Type-safe
    ├── components/  # Componentes de UI reutilizáveis (Shimmer, ErrorView, MonoText)
    └── theme/       # Design System (Colors, Typography, Theme)
```

## Stack técnica

| Categoria | Tecnologia |
|---|---|
| Linguagem | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Navegação | Navigation Compose (Type-safe) |
| Injeção de dependência | **Koin** (KMP ready) |
| Rede | **Ktor Client** + Resources (KMP ready) |
| Serialização | Kotlinx Serialization |
| Qualidade | **Detekt** (Análise estática) |
| Testes unitários | JUnit, MockK, Turbine, Ktor MockEngine |
| Testes de UI | Compose UI Test |

## Configuração da API Key

O projeto requer uma chave de API da CoinMarketCap. Para rodar localmente:

1. Crie uma conta e obtenha sua chave em [pro.coinmarketcap.com/api/v1](https://pro.coinmarketcap.com/api/v1).
2. Abra (ou crie) o arquivo `local.properties` na raiz do projeto.
3. Adicione a linha: `CMC_API_KEY=sua_chave_aqui`
4. Sincronize o projeto. A chave é injetada via `BuildConfig`.

## Como rodar

```bash
git clone <url-do-repositorio>
cd CryptoHub
./gradlew assembleDebug
```

**Requisitos mínimos**: Android Studio Ladybug+, **JDK 21**.
**minSdk**: 24 · **targetSdk**: 36

## Testes

```bash
# Testes unitários e lógica de negócio
./gradlew test

# Testes instrumentados (UI e Navegação)
./gradlew connectedAndroidTest

# Análise estática de código
./gradlew detekt
```

## Decisões Técnicas

- **Ktor + Resources**: Escolha estratégica visando migração para Multiplatform. O uso de `@Resource` garante URLs type-safe.
- **Koin DSL**: Uso de `viewModelOf`, `singleOf` e `factoryOf` para reduzir boilerplate e facilitar a injeção.
- **Resiliência Parcial**: A tela de detalhes carrega metadados mesmo se o endpoint de ativos falhar (comum em exchanges menores no CMC).
- **Paralelismo**: Uso de `async/await` no repositório para carregar múltiplos endpoints simultaneamente.
- **Polimento visual**: Customização de componentes para estética "Fintech/Crypto" com tipografia mono-espaçada para dados financeiros e **inversão de separadores decimais/milhar** para o padrão brasileiro ($1.234,56).
- **UX de Paginação**: Substituição de indicadores de progresso genéricos por **Shimmers**, garantindo uma transição visual suave durante o carregamento de novos dados.

## Sobre o desafio

Repositório criado para avaliação técnica da Mercado Bitcoin.
