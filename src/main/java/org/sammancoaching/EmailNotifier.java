package org.sammancoaching;

import org.sammancoaching.dependencies.Config;
import org.sammancoaching.dependencies.Emailer;
import org.sammancoaching.dependencies.Logger;

/**
 * Gerencia notificações por email ao final da execução do pipeline.
 * Só envia emails se estiver habilitado na configuração.
 */
public class EmailNotifier {
    private final Config config;
    private final Emailer emailer;
    private final Logger logger;

    /**
     * Cria um novo EmailNotifier com as dependências especificadas.
     *
     * @param config  configuração que determina se emails devem ser enviados
     * @param emailer serviço para envio de emails
     * @param logger  serviço para registro de eventos de notificação
     */
    public EmailNotifier(Config config, Emailer emailer, Logger logger) {
        this.config = config;
        this.emailer = emailer;
        this.logger = logger;
    }

    /**
     * Envia uma notificação por email sobre os resultados do pipeline se habilitado.
     * Se os emails estiverem desabilitados, registra essa informação.
     *
     * @param executionResult os resultados da execução do pipeline
     */
    public void notifyIfEnabled(PipelineExecutionResult executionResult) {
        if (!isEmailEnabled()) {
            logEmailDisabled();
            return;
        }

        sendEmail(executionResult);
    }

    private boolean isEmailEnabled() {
        return config.sendEmailSummary();
    }

    private void logEmailDisabled() {
        logger.info("Email disabled");
    }

    private void sendEmail(PipelineExecutionResult executionResult) {
        logger.info("Sending email");
        String emailMessage = executionResult.buildEmailMessage();
        emailer.send(emailMessage);
    }
}
