# CryptoHub

Aplicativo Android desenvolvido como desafio técnico para a vaga de Android Sênior na **Mercado Bitcoin**.

O app consome a API da [CoinMarketCap](https://coinmarketcap.com/api/documentation/v1/) para listar exchanges de criptomoedas e exibir seus detalhes, incluindo as moedas negociadas em cada uma.

## Funcionalidades

- **Listagem de exchanges**: exibe logo, nome, volume spot (USD) e data de lançamento.
- **Detalhe da exchange**: exibe logo, nome, id, descrição, website, taxas (maker/taker), data de lançamento e a lista de moedas negociadas com preço em USD.

## Arquitetura

O projeto segue **Clean Architecture** com **MVVM** na camada de apresentação, dividido em três módulos lógicos:

```
com.example.cryptohub
├── core/            # Classes utilitárias compartilhadas (Result/Resource, extensões, etc.)
├── data/
│   ├── remote/      # Retrofit service + DTOs (kotlinx.serialization)
│   ├── mapper/      # DTO -> Domain
│   └── repository/  # Implementação das interfaces de repositório
├── domain/
│   ├── model/       # Modelos de domínio (Exchange, ExchangeDetail, Coin)
│   ├── repository/  # Contratos (interfaces) de repositório
│   └── usecase/     # Casos de uso (regras de negócio isoladas)
├── di/              # Módulos Hilt (Network, Repository)
└── presentation/
    ├── list/        # Tela de listagem (ViewModel + Composables)
    ├── detail/       # Tela de detalhe (ViewModel + Composables)
    └── navigation/    # Navigation Compose
```

**Por quê essa separação?** A camada `domain` não depende de Android nem de bibliotecas de rede — é Kotlin puro, o que facilita testes unitários rápidos e desacopla regra de negócio de detalhes de implementação (Retrofit, Compose, etc). A UI só conhece a camada de apresentação, que por sua vez só conhece os casos de uso — nunca a API diretamente.

## Stack técnica

| Categoria | Tecnologia |
|---|---|
| Linguagem | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Navegação | Navigation Compose |
| Injeção de dependência | Hilt |
| Rede | Retrofit + OkHttp |
| Serialização | Kotlinx Serialization |
| Assincronismo | Coroutines + Flow |
| Imagens | Coil |
| Testes unitários | JUnit, MockK, Turbine |
| Testes de UI | Compose UI Test |

## Configuração da API Key

O projeto requer uma chave de API da CoinMarketCap. Para rodar localmente:

1. Crie uma conta e obtenha sua chave em [pro.coinmarketcap.com/api/v1](https://pro.coinmarketcap.com/api/v1).
2. Abra (ou crie) o arquivo `local.properties` na raiz do projeto — **esse arquivo não é versionado**.
3. Adicione a linha:

```properties
CMC_API_KEY=sua_chave_aqui
```

4. Sincronize o projeto no Android Studio (File > Sync Project with Gradle Files). A chave é injetada em tempo de build via `BuildConfig.CMC_API_KEY` e enviada como header `X-CMC_PRO_API_KEY` nas requisições.

 **Observação**: o endpoint de exchanges (`/v1/exchange/info`) pode exigir um plano pago da CoinMarketCap. Caso sua chave não tenha acesso, isso está documentado na seção [Limitações conhecidas](#-limitações-conhecidas).

## ▶️ Como rodar

```bash
git clone <url-do-repositorio>
cd CryptoHub
# configure local.properties conforme instruções acima
./gradlew assembleDebug
```

Ou abra o projeto diretamente no Android Studio e rode a configuração padrão `app`.

**Requisitos mínimos**: Android Studio (versão atual estável), JDK 17.
**minSdk**: 24 (Android 7.0) · **targetSdk**: 36 (Android 16)

## Testes

```bash
# Testes unitários
./gradlew testDebugUnitTest

# Testes instrumentados / UI (requer emulador ou dispositivo conectado)
./gradlew connectedDebugAndroidTest
```

## Decisões técnicas

- **Kotlinx Serialization** em vez de Gson: melhor integração com data/sealed classes do Kotlin e null-safety em tempo de compilação.
- **MockK** em vez de Mockito: lida nativamente com `final classes`, `object` e `suspend functions`, comuns em um projeto 100% Kotlin.
- **Turbine** para testar `Flow` de forma legível e determinística.
- **Result/Resource sealed class** na camada de domínio para representar explicitamente estados de `Loading`, `Success` e `Error`, evitando exceptions não tratadas subindo até a UI.

## Limitações conhecidas

- _(preencher conforme o desenvolvimento avançar — ex: paginação da API, comportamento offline, etc.)_

## Sobre o desafio

Este repositório foi criado exclusivamente para fins de avaliação técnica, conforme especificado no [desafio da Mercado Bitcoin](https://github.com/mb-desafio/querosermb).
