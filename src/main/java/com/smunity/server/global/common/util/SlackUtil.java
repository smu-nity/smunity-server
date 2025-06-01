package com.smunity.server.global.common.util;

import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.model.Field;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookPayloads;
import com.smunity.server.global.exception.GeneralException;
import com.smunity.server.global.exception.code.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class SlackUtil {

    private final Slack slack = Slack.getInstance();

    @Value("${slack.webhook.url}")
    private String SLACK_WEBHOOK_URL;

    public void sendMessage(Exception ex) {
        try {
            slack.send(SLACK_WEBHOOK_URL, payload(ex.getMessage()));
        } catch (IOException e) {
            throw new GeneralException(ErrorCode.SLACK_SERVER_ERROR);
        }
    }

    private Payload payload(String message) {
        return WebhookPayloads.payload(p -> p.attachments(List.of(attachment(message))));
    }

    private Attachment attachment(String message) {
        return Attachment.builder()
                .color("#FF0000")
                .fields(List.of(field(message)))
                .build();
    }

    private Field field(String message) {
        return Field.builder()
                .title("에러 로그")
                .value(message)
                .valueShortEnough(false)
                .build();
    }
}
