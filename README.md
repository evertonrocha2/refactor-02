# BuildPipeline Refactoring Kata - Java

## Sobre o Projeto

Este projeto documenta a refatoração de um sistema legado de pipeline de CI/CD escrito em Java. O código original apresentava problemas de clareza, duplicação e baixa coesão, dificultando manutenção e evolução.

### Problemas Identificados

- Método run() complexo com ~50 linhas e lógica aninhada
- Strings literais "success" e "failure" repetidas em vários pontos
- Classe Pipeline acumulando múltiplas responsabilidades
- Ausência de testes automatizados
- Falta de documentação

### Objetivos

Aplicar técnicas de refatoração para melhorar qualidade, testabilidade e manutenibilidade do código, utilizando princípios SOLID e Clean Code.

## Refatorações Realizadas

### Eliminação de Magic Strings

Criado o enum PipelineResult para substituir strings literais "success" e "failure", trazendo type safety e eliminando possíveis erros de digitação.

### Encapsulamento de Resultados

A classe PipelineExecutionResult foi criada para agrupar os resultados da execução (testes e deployment) e centralizar a lógica de geração de mensagens de email.

### Separação de Responsabilidades

A classe Pipeline foi dividida em três classes especializadas:
- TestRunner: executa testes e registra resultados
- Deployer: gerencia o deployment (só executa se testes passarem)
- EmailNotifier: cuida das notificações por email

### Simplificação do Código

O método run() foi reduzido de ~50 linhas para 4 linhas, funcionando apenas como orquestrador que delega responsabilidades. O fluxo do pipeline ficou claro e autoexplicativo.

### Testes Automatizados

Foram implementados 24 testes (9 de integração e 15 unitários) cobrindo todos os cenários possíveis. Os testes garantiram que o comportamento original foi preservado durante toda a refatoração.

### Documentação

Todas as classes e métodos públicos foram documentados com Javadoc em português.

## Resultados

O código foi reorganizado em 5 classes: Pipeline (orquestrador), TestRunner, Deployer e EmailNotifier (classes especializadas), além de PipelineResult (enum) e PipelineExecutionResult (value object).

A complexidade do método principal foi reduzida de ~50 linhas para 4 linhas. Foram criados 24 testes automatizados garantindo cobertura completa. Todas as strings mágicas foram eliminadas e o código foi totalmente documentado.

## Executando os Testes

Para executar todos os testes:
```bash
mvn test
```

## Tecnologias

- Java 21
- JUnit 5
- AssertJ
- Mockito
- Maven

## Princípios Aplicados

Foram aplicados os princípios SOLID (Single Responsibility, Open/Closed, Dependency Inversion) e práticas de Clean Code (nomes descritivos, métodos pequenos, eliminação de duplicação).

A refatoração incremental com validação constante por testes mostrou-se mais segura que grandes reescritas. A extração de classes especializadas e o uso de enums eliminaram categorias inteiras de possíveis erros.

## Referências

Baseado no [BuildPipeline-Refactoring-Kata](https://github.com/emilybache/BuildPipeline-Refactoring-Kata) de Emily Bache.
