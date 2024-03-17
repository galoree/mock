package org.lanit.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.*;
import org.lanit.models.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.tinylog.Logger;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class JSONController {
    @PostMapping(value = "json")
    public Object getResponse(@RequestParam(value = "action") String action, @RequestBody String requestBody) throws IOException {
        // Если action = add
        if (action.equals("add")) {
            // Конвертируем JSON в объект
            ObjectMapper objectMapper = new ObjectMapper();
            RequestJson requestJson = objectMapper.readValue(requestBody, RequestJson.class);
            // Создаем объект, который будем использовать как JSON ответ
            ResponseJson responseJson = new ResponseJson();
            Info info = requestJson.getInfo();
            // Получаем add объект и у него берем название тикера, timeFrame и percent
            Add add = requestJson.getAdd();
            String tickerName = add.getName();
            int timeFrame = add.getTimeFrame();
            int percent = add.getPercent();
            // Получаем время для lastUpdate, устанавливаем тот же UUID
            LocalDateTime dt = LocalDateTime.now();
            DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            String lastUpdate = dtFormatter.format(dt);
            String uuid = requestJson.getUuid();
            List<TickersItem> tickers = info.getTickers();
            responseJson.setUuid(uuid);
            responseJson.setLastUpdate(lastUpdate);
            // Создаем оповещение, которое потом добавим
            AlertsItem alertsItem = new AlertsItem();
            alertsItem.setTimeFrame(timeFrame);
            alertsItem.setPercent(percent);
            // Если имеется тикер с запрошенным именем, добавляем оповещение в него
            for (int i = 0; i < tickers.size(); i++) {
                if (tickerName.equals(tickers.get(i).getTicker())) {
                    tickers.get(i).getAlerts().add(alertsItem);
                    info.setTickers(tickers);
                    responseJson.setInfo(info);
                    // Конвертируем объект в JSON и возвращаем ответ
                    String responseBody = objectMapper.writeValueAsString(responseJson);
                    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
                    return ResponseEntity.status(200).header("content-type", "application/json").body(responseBody);
                }
            }
            // Если тикера с указанным именем нет, создаем такой тикер с указанным оповещением
            TickersItem tickersItem = new TickersItem();
            List<AlertsItem> tickersItemAlerts = new ArrayList<>();
            // Добавляем оповещение в новый массив оповещений
            tickersItemAlerts.add(alertsItem);
            // Добавляем массив оповещений и имя к созданному тикеру
            tickersItem.setTicker(tickerName);
            tickersItem.setAlerts(tickersItemAlerts);
            // Добавляем тикер в тикеры, которые уже были в запросе
            info.getTickers().add(tickersItem);
            responseJson.setInfo(info);
            // Конвертируем объект в JSON и возвращаем ответ
            String responseBody = objectMapper.writeValueAsString(responseJson);
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            return ResponseEntity.status(200).header("content-type", "application/json").body(responseBody);

            // Если action = delete
        } else if (action.equals("delete")) {
            // Конвертируем JSON в объект
            ObjectMapper objectMapper = new ObjectMapper();
            // Создаем объект, который будем использовать как JSON ответ
            DeleteRequestJson deleteRequestJson = objectMapper.readValue(requestBody, DeleteRequestJson.class);
            ResponseJson responseJson = new ResponseJson();
            Info info = deleteRequestJson.getInfo();
            // Получаем delete объект и у него берем название тикера, индкес оповещения
            Delete delete = deleteRequestJson.getDelete();
            String deleteName = delete.getTickerName();
            int deleteIndex = delete.getAlertIndex();
            // Получаем тикеры, устанавливаем тот же UUID, время для lastUpdate
            List<TickersItem> tickers = info.getTickers();
            String uuid = deleteRequestJson.getUuid();
            LocalDateTime dt = LocalDateTime.now();
            DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            String lastUpdate = dtFormatter.format(dt);
            responseJson.setUuid(uuid);
            responseJson.setLastUpdate(lastUpdate);
            // Ищем тикер с названием в запросе, удаляем оповещение по индексу, который был передан в запросе
            for (int i = 0; i < info.getTickers().size(); i++) {
                if (deleteName.equals(tickers.get(i).getTicker())) {
                    try {
                        tickers.get(i).getAlerts().remove(deleteIndex);
                        info.setTickers(tickers);
                        responseJson.setInfo(info);
                        // Подготовив объект responseJson конвертируем его в JSON и возвращаем как ответ
                        String responseBody = objectMapper.writeValueAsString(responseJson);
                        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
                        return ResponseEntity.status(200).header("content-type", "application/json").body(responseBody);
                    } catch (Exception e) {
                        // Если в запросе на удаление несуществующий индекс в тикере, возвращаем ошибку
                        Logger.error(requestBody);
                        return ResponseEntity.status(404).header("content-type", "application/json").body("{\n   \"status\": \"404\"\n   \"message\": \"Передан некорректный индекс\"\n}");
                    }
                }
            }
            // Если в запросе на удаление несуществующий тикер, возвращаем ошибку
            Logger.error(requestBody);
            return ResponseEntity.status(404).header("content-type", "application/json").body("{\n   \"status\": \"404\"\n   \"message\": \"Передан некорректный тикер\"\n}");
        }
        // При некорректном action параметре возвращаем ошибку
        Logger.error(requestBody);
        return ResponseEntity.status(400).header("content-type", "application/json").body(String.format("{\n   \"status\": \"400\"\n   \"message\": \"Передан некорректный action - %s\"\n}", action));
    }
}
