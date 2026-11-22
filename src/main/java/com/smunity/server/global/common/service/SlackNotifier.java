package com.smunity.server.global.common.service;

public interface SlackNotifier {

    void sendMessage(Exception ex);
}
