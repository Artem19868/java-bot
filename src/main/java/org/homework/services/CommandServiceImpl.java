package org.homework.services;

import org.homework.api.CommandService;
import org.homework.api.DataService;
import org.homework.di.annotations.Register;
import org.homework.di.annotations.Resolve;
import org.homework.logger.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

@Register
public class CommandServiceImpl implements CommandService {

    @Resolve
    JsonParserServiceImpl jsonParserService;
    @Resolve
    private DataService dataService;

    @Resolve
    private Logger logger;

    @Override
    public SendMessage getHelp(String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Команды этого бота : /help,");
        return sendMessage;
    }

    @Override
    public SendMessage getWeather(String chatId, float latitude, float longitude) {
        String url = "http://api.weatherapi.com/v1/current.json?key=&q=" + latitude + "," + longitude + "&lang=ru&aqi=no";
        String weatherJson;

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        BasicHttpService basicHttpService = new BasicHttpService();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        try {
            weatherJson = basicHttpService.sendGetRequest(url, headers);
            System.out.println(weatherJson);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при получении данных о погоде: " + e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Некорректный URL:  " + e.getMessage());
        }


        sendMessage.setText(jsonParserService.WeatherParser(weatherJson));
        return sendMessage;
    }

    @Override
    public SendMessage startMessage(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Привет! Чтобы получить прогноз погоды, нажмите на кнопку ниже или отправьте свою геопозицию.");

        KeyboardButton locationButton = new KeyboardButton();
        locationButton.setText("Отправить геолокацию");
        locationButton.setRequestLocation(true);

        // Добавляем кнопку в строку
        KeyboardRow row = new KeyboardRow();
        row.add(locationButton);

        // Добавляем строку в клавиатуру
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(Collections.singletonList(row));
        keyboardMarkup.setResizeKeyboard(true); // Уменьшаем клавиатуру
        keyboardMarkup.setOneTimeKeyboard(true); // Показываем клавиатуру один раз

        message.setReplyMarkup(keyboardMarkup);

        return message;
    }
}
