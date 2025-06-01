package com.smunity.server.global.common.util;

import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.model.Field;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookPayloads;
import com.smunity.server.global.exception.GeneralException;
import com.smunity.server.global.exception.code.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class SlackUtil {

    private final Slack slack = Slack.getInstance();

    @Value("${slack.webhook.url}")
    private String SLACK_WEBHOOK_URL;

    public void sendMessage(Exception ex) {
        sendMessage("에러 로그", ex.getMessage(), "#F44336");
    }

    private void sendMessage(String title, String message) {
        sendMessage(title, message, "#2196F3");
    }

    private void sendMessage(String title, String message, String color) {
        try {
            slack.send(SLACK_WEBHOOK_URL, payload(title, message, color));
        } catch (IOException e) {
            throw new GeneralException(ErrorCode.SLACK_SERVER_ERROR);
        }
    }

    private Payload payload(String title, String message, String color) {
        return WebhookPayloads.payload(p -> p.attachments(List.of(attachment(title, message, color))));
    }

    private Attachment attachment(String title, String message, String color) {
        return Attachment.builder()
                .color(color)
                .fields(List.of(field(title, message)))
                .build();
    }

    private Field field(String title, String message) {
        return Field.builder()
                .title(title)
                .value(message)
                .valueShortEnough(false)
                .build();
    }
}
