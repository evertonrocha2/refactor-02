package org.sammancoaching;

import org.sammancoaching.dependencies.Config;
import org.sammancoaching.dependencies.Emailer;
import org.sammancoaching.dependencies.Logger;

/**
 * Gerencia notificações por email ao final da execução do pipeline.
 */
public class EmailNotifier {
    private final Config config;
    private final Emailer emailer;
    private final Logger logger;

    public EmailNotifier(Config config, Emailer emailer, Logger logger) {
        this.config = config;
        this.emailer = emailer;
        this.logger = logger;
    }

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
