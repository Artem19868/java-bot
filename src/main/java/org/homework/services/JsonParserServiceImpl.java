package org.homework.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.homework.api.JsonParserService;
import org.homework.di.annotations.Register;

@Register
public class JsonParserServiceImpl  implements JsonParserService {
    @Override
    public String WeatherParser(String weatherJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(weatherJson);

            String locationName = rootNode.path("location").path("name").asText();
            double tempC = rootNode.path("current").path("temp_c").asDouble();
            String conditionText = rootNode.path("current").path("condition").path("text").asText();
            double windKph = rootNode.path("current").path("wind_kph").asDouble();
            String windDir = rootNode.path("current").path("wind_dir").asText();
            double humidity = rootNode.path("current").path("humidity").asDouble();
            double feelsLikeC = rootNode.path("current").path("feelslike_c").asDouble();

            String formatWeatherString = String.format("Погода в %s:\n" +
                    "Температура: %.1f °C\n" +
                    "Состояние: %s\n" +
                    "Скорость ветра: %.1f км/ч\n" +
                    "Направление ветра: %s\n" +
                    "Влажность: %.0f%%\n" +
                    "Ощущается как: %.1f°C\n", locationName,tempC,conditionText,windKph,windDir,humidity,feelsLikeC);

            return  formatWeatherString;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
