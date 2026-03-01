# BuildPipeline Refactoring Kata - Java

## Sobre o Projeto

Este projeto documenta a refatoração de um sistema legado de pipeline de CI/CD. O trabalho envolveu identificar problemas no código original e aplicar técnicas de refatoração para melhorar clareza, testabilidade e manutenibilidade.

### Análise do Código Original

O código inicial apresentava problemas comuns em sistemas legados:
- Método `run()` com múltiplas responsabilidades e lógica aninhada
- Uso repetido de strings literais "success" e "failure" 
- Classe única responsável por testes, deployment e notificações
- Ausência de testes automatizados (apenas placeholder com TODO)
- Dificuldade para entender e estender o comportamento

## Objetivos da Refatoração

1. Extrair métodos e usar variáveis com nomes mais claros
2. Eliminar duplicação de código e strings mágicas
3. Separar responsabilidades em classes distintas
4. Implementar suite de testes automatizados
5. Documentar o código adequadamente

## Melhorias Implementadas

### 1. Extração de Enum para Resultados (PipelineResult)

**Problema**: Strings mágicas "success" e "failure" espalhadas pelo código.

**Solução**: Criação do enum `PipelineResult` com métodos auxiliares:
```java
public enum PipelineResult {
    SUCCESS, FAILURE;
    
    public static PipelineResult fromString(String value);
    public boolean isSuccess();
    public boolean isFailure();
}
```

**Vantagens dessa abordagem**:
- Segurança de tipos em tempo de compilação
- Código mais legível com métodos como `isSuccess()`
- Mais fácil adicionar novos estados no futuro

### 2. Encapsulamento de Resultados (PipelineExecutionResult)

**Problema**: Status de testes e deployment espalhados em variáveis boolean.

**Solução**: Criação da classe `PipelineExecutionResult` que encapsula os resultados da execução e centraliza a lógica para gerar mensagens de email baseadas no contexto.

**Vantagens dessa abordagem**:
- Dados relacionados agrupados em um único objeto
- Lógica de decisão de mensagens em local único
- Facilita testes isolados dessa funcionalidade

### 3. Separação de Responsabilidades (TestRunner, Deployer, EmailNotifier)

**Problema**: Classe `Pipeline` com múltiplas responsabilidades.

**Solução**: Criação de três classes especializadas:

**TestRunner**: Responsável por executar testes e logar resultados. Trata o caso especial de projetos sem testes.

**Deployer**: Gerencia o processo de deployment. Implementa a regra de negócio que impede deployment quando os testes falham.

**EmailNotifier**: Lida com notificações por email. Verifica a configuração antes de enviar.

**Vantagens dessa separação**:
- Cada classe tem responsabilidade única e bem definida
- Testes podem ser feitos isoladamente
- Mais fácil adicionar novos tipos de notificação no futuro
- Código mais modular e reutilizável

### 4. Simplificação da Classe Pipeline

Com a extração das classes especializadas, o método `run()` foi reduzido de aproximadamente 50 linhas para apenas 4 linhas:

```java
public void run(Project project) {
    boolean testsPassed = testRunner.executeTests(project);
    boolean deploySuccessful = deployer.deploy(project, testsPassed);
    
    PipelineExecutionResult executionResult = 
        new PipelineExecutionResult(testsPassed, deploySuccessful);
    emailNotifier.notifyIfEnabled(executionResult);
}
```

O código agora funciona como orquestrador, delegando cada responsabilidade para a classe apropriada. O fluxo do pipeline ficou explícito e fácil de entender.

### 5. Suite Completa de Testes

O projeto original tinha apenas um teste vazio (TODO). Foram implementados 24 testes cobrindo diferentes cenários:

**Testes de Integração (PipelineTest - 9 testes)**:
- Execução completa com testes passando e deployment bem-sucedido
- Falha nos testes (deployment não deve acontecer)
- Deployment falhando após testes passarem
- Projeto sem testes
- Diferentes configurações de email (habilitado/desabilitado)

**Testes Unitários**:
- TestRunnerTest (3 testes) - execução de testes
- DeployerTest (3 testes) - lógica de deployment
- PipelineExecutionResultTest (5 testes) - geração de mensagens
- PipelineResultTest (4 testes) - conversão de strings e casos extremos

Esses testes foram fundamentais para garantir que o comportamento original foi preservado durante toda a refatoração.

### 6. Documentação com Javadoc

Todas as classes públicas e métodos foram documentados com Javadoc, incluindo propósito, parâmetros, valores de retorno e comportamentos especiais. Isso facilita o entendimento do código e permite que IDEs exibam a documentação automaticamente.

## Comparação: Antes vs Depois

| Métrica | Antes | Depois |
|---------|-------|--------|
| Número de classes | 1 | 5 |
| Linhas no método run() | ~50 | 4 |
| Testes automatizados | 1 (vazio) | 24 |
| Strings mágicas | 6+ | 0 |
| Responsabilidades por classe | 4 | 1 |
| Javadoc | 0 linhas | ~80 linhas |

## Arquitetura Resultante

```
Pipeline (Orchestrator)
  ├── TestRunner (execução de testes)
  ├── Deployer (lógica de deployment)
  └── EmailNotifier (notificações)

Classes de Suporte:
  ├── PipelineResult (enum para resultados)
  └── PipelineExecutionResult (encapsulamento de resultados)
```

## Executando os Testes

```bash
# Compilar e executar todos os testes
mvn test

# Limpar e testar
mvn clean test

# Executar um teste específico
mvn test -Dtest=PipelineTest
```

## Tecnologias Utilizadas

- Java 21
- JUnit 5 (framework de testes)
- AssertJ (assertions fluentes)
- Mockito (mocks para testes)
- Maven (gerenciamento de dependências e build)

## Princípios de Design Aplicados

**SOLID**:
- Single Responsibility: Cada classe tem uma responsabilidade específica
- Open/Closed: Estrutura permite extensão sem modificar código existente
- Liskov Substitution: Interfaces bem definidas
- Interface Segregation: Interfaces coesas sem métodos desnecessários
- Dependency Inversion: Dependências via interfaces (Logger, Emailer, Config)

**Clean Code**:
- Nomes descritivos que expressam intenção
- Métodos pequenos focados em uma tarefa
- Eliminação de duplicação
- Javadoc para documentação
- Código autoexplicativo

## Principais Aprendizados

Durante este trabalho de refatoração, alguns pontos se mostraram particularmente importantes:

A refatoração incremental (pequenas mudanças validadas por testes) é mais segura que grandes reescritas. Escrever testes antes de refatorar foi essencial para preservar o comportamento original.

A extração de classes especializadas resolveu o problema de múltiplas responsabilidades de forma elegante. O uso de enums no lugar de strings literais eliminou uma categoria inteira de possíveis erros.

O encapsulamento de dados relacionados em value objects (como PipelineExecutionResult) melhorou significativamente a coesão do código.

## Autor

Trabalho desenvolvido para a disciplina de Engenharia de Software.

## Referências

Este projeto é baseado no [BuildPipeline-Refactoring-Kata](https://github.com/emilybache/BuildPipeline-Refactoring-Kata) de Emily Bache.
