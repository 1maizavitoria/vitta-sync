# Arquitetura da customização do Dashboard

## Objetivo

Permitir que cada usuário escolha quais widgets do Dashboard deseja visualizar, altere a ordem das seções e mantenha a preferência salva sem acoplar a interface ao mecanismo de persistência.

## Organização

```text
src/features/dashboard/
├── domain/dashboardLayout.js
├── application/useDashboardPreferences.js
├── infrastructure/localDashboardPreferencesRepository.js
└── presentation/DashboardCustomizationDialog.jsx
```

## Responsabilidades

### Domínio

`dashboardLayout.js` contém os widgets disponíveis, a configuração padrão e as regras puras de normalização, visibilidade e ordenação. Essa camada não depende de React, Material UI ou `localStorage`.

### Aplicação

`useDashboardPreferences.js` coordena o caso de uso de personalização. O hook mantém a configuração aplicada e um rascunho separado, além de expor as operações de abrir, cancelar, ordenar, alterar visibilidade, restaurar e salvar.

### Infraestrutura

`localDashboardPreferencesRepository.js` implementa a persistência no navegador. Sua interface assíncrona (`load`, `save` e `reset`) permite substituir o armazenamento local por uma API sem alterar o domínio ou a apresentação.

### Apresentação

`DashboardCustomizationDialog.jsx` apresenta os controles acessíveis. O componente recebe dados e ações por propriedades e não conhece o mecanismo de persistência.

### Composição

`pages/Dashboard/index.jsx` funciona como ponto de composição: cria o repositório, conecta o hook, associa identificadores aos componentes no registro de widgets e renderiza somente as seções visíveis na ordem salva.

## Padrões e princípios aplicados

- **Repository:** isola o mecanismo de persistência.
- **Facade:** o hook oferece uma interface única para o caso de uso.
- **Registry/Strategy:** o Dashboard associa o identificador salvo ao widget correspondente.
- **Single Responsibility Principle:** domínio, aplicação, infraestrutura e apresentação possuem motivos diferentes para mudar.
- **Dependency Inversion:** a lógica de aplicação recebe um repositório e não acessa diretamente o `localStorage`.
- **Open/Closed Principle:** widgets novos podem ser acrescentados ao catálogo e ao registro sem modificar as regras existentes.

## Persistência e evolução

A chave local utiliza o CPF do usuário autenticado:

```text
vitta-dashboard-layout:{cpf-do-usuario}
```

Configurações antigas são normalizadas ao carregar. Identificadores desconhecidos são descartados e widgets adicionados em versões futuras entram automaticamente como visíveis.

Para migrar ao banco de dados, deve-se criar um repositório remoto com as mesmas operações assíncronas e utilizá-lo no ponto de composição. O diálogo, as regras do domínio e a maior parte do hook permanecem inalterados.

## Decisões de acessibilidade

- A ordenação utiliza botões explícitos, sem exigir arrastar e soltar.
- Todos os botões possuem nomes acessíveis.
- O diálogo funciona por teclado.
- No modo acessível, controles e áreas clicáveis são ampliados.
- Pelo menos um widget permanece visível para evitar um Dashboard vazio.
- O layout do diálogo se adapta a dispositivos móveis.
