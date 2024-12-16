package org.homework.api;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface CommandService {
    SendMessage getWeather(String chatId, float latitude, float longitude);

    SendMessage getHelp(String chatId);

    SendMessage startMessage(String chatId);
}
