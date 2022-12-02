package com.sidroded.ukrtomatbot.service;

import com.sidroded.ukrtomatbot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    static private final long OWNER_ID = 5061545325L;

    /*----------------------------POSITION------------------------------*/
    static private final String SHOP_POSITION = "SHOP";
    static private final String TOMATOES_POSITION = "TOMATOES";
    static private final String TOMATOES_CHOICE_POSITION = "TOMATOES_CHOICE";

    /*-----------------------------PRODUCTS----------------------------*/

    static private final String TOMATOES_CLASSIC_PRODUCT = "TOMATOES_CLASSIC";
    static private final String TOMATOES_PIQUANT_PRODUCT = "TOMATOES_PIQUANT";
    static private final String TOMATOES_SPICY_PRODUCT = "TOMATOES_SPICY";
    static private final String TOMATOES_ORIGINAL_PRODUCT = "TOMATOES_ORIGINAL";
    static private final String TOMATOES_EXTRA_PRODUCT = "TOMATOES_EXTRA";

    static private final String TOMATOES_CLASSIC_PRODUCT_MIXED_OIL = "TOMATOES_CLASSIC_MIXED_OIL";
    static private final String TOMATOES_PIQUANT_PRODUCT_MIXED_OIL = "TOMATOES_PIQUANT_MIXED_OIL";
    static private final String TOMATOES_SPICY_PRODUCT_MIXED_OIL = "TOMATOES_SPICY_MIXED_OIL";

    static private final String TOMATOES_CLASSIC_PRODUCT_OLIVE_OIL = "TOMATOES_CLASSIC_OLIVE_OIL";
    static private final String TOMATOES_PIQUANT_PRODUCT_OLIVE_OIL = "TOMATOES_PIQUANT_OLIVE_OIL";
    static private final String TOMATOES_SPICY_PRODUCT_OLIVE_OIL = "TOMATOES_SPICY_OLIVE_OIL";
    static private final String TOMATOES_EXTRA_PRODUCT_OLIVE_OIL = "TOMATOES_EXTRA_OLIVE_OIL";

    static private final String TOMATOES_ORIGINAL_PRODUCT_REFINED_OIL = "TOMATOES_ORIGINAL_MIXED_OIL";

    /*---------------------------PRODUCT PRICE-------------------------*/

    static private final int TOMATOES_SPICY_PRODUCT_MIXED_OIL_PRICE = 150;
    static private final int TOMATOES_CLASSIC_PRODUCT_MIXED_OIL_PRICE = 150;
    static private final int TOMATOES_PIQUANT_PRODUCT_MIXED_OIL_PRICE = 150;

    static private final int TOMATOES_CLASSIC_PRODUCT_OLIVE_OIL_PRICE = 200;
    static private final int TOMATOES_PIQUANT_PRODUCT_OLIVE_OIL_PRICE = 200;
    static private final int TOMATOES_SPICY_PRODUCT_OLIVE_OIL_PRICE = 20;
    static private final int TOMATOES_EXTRA_PRODUCT_OLIVE_OIL_PRICE = 150;

    static private final int TOMATOES_ORIGINAL_PRODUCT_REFINED_OIL_PRICE = 150;

    /*------------------------PRODUCT DESCRIPTION----------------------*/

    static private final String TOMATOES_CLASSIC_DESCRIPTION = "TOMATOES_CLASSIC";
    static private final String TOMATOES_PIQUANT_DESCRIPTION = "TOMATOES_PIQUANT";
    static private final String TOMATOES_SPICY_DESCRIPTION = "TOMATOES_SPICY";
    static private final String TOMATOES_ORIGINAL_DESCRIPTION = "TOMATOES_ORIGINAL";
    static private final String TOMATOES_EXTRA_DESCRIPTION = "TOMATOES_EXTRA";

    static private final String TOMATOES_CLASSIC_DESCRIPTION_MIXED_OIL = "TOMATOES_CLASSIC_MIXED_OIL_DESCRIPTION";
    static private final String TOMATOES_PIQUANT_DESCRIPTION_MIXED_OIL = "TOMATOES_PIQUANT_MIXED_OIL_DESCRIPTION";
    static private final String TOMATOES_SPICY_DESCRIPTION_MIXED_OIL = "TOMATOES_SPICY_MIXED_OIL_DESCRIPTION";

    static private final String TOMATOES_CLASSIC_DESCRIPTION_OLIVE_OIL = "TOMATOES_CLASSIC_OLIVE_OIL_DESCRIPTION";
    static private final String TOMATOES_PIQUANT_DESCRIPTION_OLIVE_OIL = "TOMATOES_PIQUANT_OLIVE_OIL_DESCRIPTION";
    static private final String TOMATOES_SPICY_DESCRIPTION_OLIVE_OIL = "TOMATOES_SPICY_OLIVE_OIL_DESCRIPTION";
    static private final String TOMATOES_EXTRA_DESCRIPTION_OLIVE_OIL = "TOMATOES_EXTRA_OLIVE_OIL_DESCRIPTION";

    /*---------------------------INLINE BUTTONS-----------------------*/

    static private final String MIXED_OIL_BUTTON = "MIXED_OIL_BUTTON";
    static private final String OLIVE_OIL_BUTTON = "OLIVE_OIL_BUTTON";
    static private final String REFINED_OIL_BUTTON = "REFINED_OIL_BUTTON";
    static private final String CLEAR_BASKET = "CLEAR_BASKET";
    static private final String CREATE_AN_ORDER = "CREATE_AN_ORDER";

    /*------------------------------INFO------------------------------*/
    static private final String ERROR = EmojiParser.parseToUnicode("Сталася помилка " + ":shrug:");
    static private final String IN_DEVELOP = EmojiParser.parseToUnicode("Ця частина боту знаходиться в розробці " + ":shrug:");

    /*----------------------------HASH MAPS---------------------------*/

    private HashMap<Long, String> returnMap = new HashMap<>();
    private HashMap<Long, String> productsMap = new HashMap<>();
    private HashMap<Long, ArrayList<String>> basketMap = new HashMap<>();
    private HashMap<Long, Integer> sumMap = new HashMap<>();
    private HashMap<String, Integer> priceMap = new HashMap<>();


    /*----------------------------CONFIG------------------------------*/

    private BotConfig config;
    public TelegramBot(BotConfig botConfig) {
        this.config = botConfig;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String regex = "\\d+";

            if (messageText.matches(regex)) {
                int count = Integer.parseInt(messageText);
                basketMap.computeIfAbsent(chatId, k -> new ArrayList<>()).add(productsMap.get(chatId) +
                        " x" + count + "шт - " + priceMap.get(productsMap.get(chatId)) * count + "грн.");

                sumMap.put(chatId, sumMap.get(chatId) + priceMap.get(productsMap.get(chatId)) * count);
                sendMassage(chatId, "Додано!");
            }

            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "Магазин":
                    shopCommandReceived(chatId);
                    break;
                case "Доставка та оплата":
                    deliveryCommandReceived(chatId);
                    break;
                case "Зв'язатись":
                    contactCommandReceived(chatId);
                    break;
                case "Кошик":
                    basketCommandReceived(chatId);
                    break;
                case "Повернутись":
                    if (!returnMap.containsKey(chatId)) returnMap.put(chatId, SHOP_POSITION);

                    switch (returnMap.get(chatId)) {
                        case SHOP_POSITION:
                            startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                            break;
                        case TOMATOES_POSITION:
                            shopCommandReceived(chatId);
                            break;
                        case TOMATOES_CHOICE_POSITION:
                            tomatoesCommandReceived(chatId);
                            break;
                        default:
                            sendMassage(chatId, ERROR);
                    }
                    break;
                case "В'ялені томати":
                    tomatoesCommandReceived(chatId);
                    break;
                case "Класичні" :
                    productsMap.put(chatId, TOMATOES_CLASSIC_PRODUCT);
                    setTomatoesProduct(chatId);
                    break;
                case "Пікантні":
                    productsMap.put(chatId, TOMATOES_PIQUANT_PRODUCT);
                    setTomatoesProduct(chatId);
                    break;
                case "Оригінальні":
                    productsMap.put(chatId, TOMATOES_ORIGINAL_PRODUCT);
                    setTomatoesProduct(chatId);
                    break;
                case "Пекучі":
                    productsMap.put(chatId, TOMATOES_SPICY_PRODUCT);
                    setTomatoesProduct(chatId);
                    break;
                case "Екстра":
                    productsMap.put(chatId, TOMATOES_EXTRA_PRODUCT);
                    setTomatoesProduct(chatId);
                    break;
                default:
                    sendMassage(chatId, EmojiParser.parseToUnicode("Невідома команда " + ":shrug:"));
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            int massageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            switch (callbackData) {
                case MIXED_OIL_BUTTON :
                    setTomatoesCountMixedOil(chatId, massageId);
                    break;
                case OLIVE_OIL_BUTTON:
                    setTomatoesCountOliveOil(chatId, massageId);
                    break;
                case REFINED_OIL_BUTTON:
                    setTomatoesCountRefinedOil(chatId);
                    break;
                case CLEAR_BASKET:
                    sumMap.put(chatId, 0);
                    basketMap.put(chatId, new ArrayList<>());
                    basketCommandReceived(chatId);
                    break;
            }
        }
    }

    /*---------------------RECEIVED COMMANDS--------------------*/

    public void startCommandReceived(long chatId, String name) {
        String startMessage = name + ", доброго дня! Вітаємо вас у нашому магазині крафтових смаколиків.";
        createAllPrices();
        sumMap.put(chatId, 0);

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(startMessage);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Магазин");
        row.add("Доставка та оплата");
        row.add("Зв'язатись.");
        keyboardRows.add(row);
        row = new KeyboardRow();
        row.add("Кошик");
        row.add("Сформувати рахунок");
        keyboardRows.add(row);
        keyboardMarkup.setKeyboard(keyboardRows);
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred" + e.getMessage());
        }
    }

    public void shopCommandReceived(long chatId) {
        String shopMassage = "Що бажаєте скуштувати?";
        returnMap.put(chatId, SHOP_POSITION);

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(shopMassage);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("В'ялені томати");
        row.add("Ковбаса");
        row.add("Паштети");
        keyboardRows.add(row);
        row = new KeyboardRow();
        row.add("Повернутись");
        keyboardRows.add(row);
        keyboardMarkup.setKeyboard(keyboardRows);
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred" + e.getMessage());
        }
    }

    public void deliveryCommandReceived(long chatId) {
        String deliveryMassage = "Доставка за тарифами Нової пошти.\n\n" +
                "Оплата:\n" +
                "1. 100% передоплата.\n" +
                "2. Наложенний платіж.";

        sendMassage(chatId, deliveryMassage);
    }

    public void contactCommandReceived(long chatId) {
        String contactText = "";

        sendMassage(chatId, IN_DEVELOP);
    } //soon

    public void basketCommandReceived(long chatId) {
        String basketMessage;
        String basketTitle = "Ваше замовлення:";

        if ((!basketMap.containsKey(chatId)) || basketMap.get(chatId).isEmpty()) {
            basketMessage = "Ваш кошик порожній";
        } else {
            StringBuilder basketBuilder = new StringBuilder();

            basketBuilder.append(basketTitle + "\n\n");

            for (String str : basketMap.get(chatId)) {
                basketBuilder.append(str + "\n\n");
            }

            basketBuilder.append("Разом до сплати:" + sumMap.get(chatId) + "грн.");

            basketMessage = basketBuilder.toString();
        }

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(basketMessage);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        var clearBasketButton = new InlineKeyboardButton();
        clearBasketButton.setText("Очистити");
        clearBasketButton.setCallbackData(CLEAR_BASKET);

        rowInLine.add(clearBasketButton);
        rowsInLine.add(rowInLine);

        rowInLine = new ArrayList<>();

        var createAnOrderButton = new InlineKeyboardButton();
        createAnOrderButton.setText("Сформувати рахунок");
        createAnOrderButton.setCallbackData(CREATE_AN_ORDER);

        rowInLine.add(createAnOrderButton);
        rowsInLine.add(rowInLine);

        inlineKeyboardMarkup.setKeyboard(rowsInLine);
        message.setReplyMarkup(inlineKeyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException ignored) {}


    }

    public void tomatoesCommandReceived(long chatId) {
        String shopMassage = "Маємо 5 неперевершених смаків!";
        returnMap.put(chatId, TOMATOES_POSITION);

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(shopMassage);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Класичні");
        row.add("Пікантні");
        row.add("Оригінальні");
        keyboardRows.add(row);
        row = new KeyboardRow();
        row.add("Пекучі");
        row.add("Екстра");
        keyboardRows.add(row);
        row = new KeyboardRow();
        row.add("Повернутись");
        row.add("Кошик");
        keyboardRows.add(row);
        keyboardMarkup.setKeyboard(keyboardRows);
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred" + e.getMessage());
        }
    }


    /*----------------------------MANAGER COMMAND----------------------------*/

    public void sendMassage(long chatId, String massageText) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(massageText);
        sendMessage.setChatId(String.valueOf(chatId));
        try {
            execute(sendMessage);
        } catch (TelegramApiException ignored) {}
    }

    public void createAnOrderCommandReceived(long chatId, ArrayList<String> orderArray) {

    } //test code

    public void getOwnerId(long chatId) {
        String path = "d:/Test/getId.txt";

        try (FileWriter writer = new FileWriter(path)){
            writer.write(String.valueOf(chatId));
        } catch (Exception ignored) {}
    }

    public void sendOrderToOwner(ArrayList<String> orderArray) {
        StringBuilder order = new StringBuilder();

        for (String str : orderArray) {
            order.append(str);
        }

        sendMassage(OWNER_ID, order.toString());
    }

    public void createAllPrices() {
        priceMap.put(TOMATOES_CLASSIC_PRODUCT_MIXED_OIL, TOMATOES_CLASSIC_PRODUCT_MIXED_OIL_PRICE);
        priceMap.put(TOMATOES_CLASSIC_PRODUCT_OLIVE_OIL, TOMATOES_CLASSIC_PRODUCT_OLIVE_OIL_PRICE);

        priceMap.put(TOMATOES_PIQUANT_PRODUCT_MIXED_OIL, TOMATOES_PIQUANT_PRODUCT_MIXED_OIL_PRICE);
        priceMap.put(TOMATOES_PIQUANT_PRODUCT_OLIVE_OIL, TOMATOES_PIQUANT_PRODUCT_OLIVE_OIL_PRICE);

        priceMap.put(TOMATOES_SPICY_PRODUCT_MIXED_OIL, TOMATOES_SPICY_PRODUCT_MIXED_OIL_PRICE);
        priceMap.put(TOMATOES_SPICY_PRODUCT_OLIVE_OIL, TOMATOES_SPICY_PRODUCT_OLIVE_OIL_PRICE);

        priceMap.put(TOMATOES_ORIGINAL_PRODUCT_REFINED_OIL, TOMATOES_ORIGINAL_PRODUCT_REFINED_OIL_PRICE);

        priceMap.put(TOMATOES_EXTRA_PRODUCT_OLIVE_OIL, TOMATOES_EXTRA_PRODUCT_OLIVE_OIL_PRICE);
    }

    /*-------------------------------PRODUCTS----------------------------------*/

    public void setTomatoesProduct(long chatId) {
        returnMap.put(chatId, TOMATOES_CHOICE_POSITION);

        String description = "Описание без масла";

        switch (productsMap.get(chatId)) {
            case TOMATOES_CLASSIC_PRODUCT:
                description = TOMATOES_CLASSIC_DESCRIPTION;
                break;
            case TOMATOES_PIQUANT_PRODUCT:
                description = TOMATOES_PIQUANT_DESCRIPTION;
                break;
            case TOMATOES_ORIGINAL_PRODUCT:
                description = TOMATOES_ORIGINAL_DESCRIPTION;
                break;
            case TOMATOES_SPICY_PRODUCT:
                description = TOMATOES_SPICY_DESCRIPTION;
                break;
            case TOMATOES_EXTRA_PRODUCT:
                description = TOMATOES_EXTRA_DESCRIPTION;
        }

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(description);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        if (!productsMap.get(chatId).equals(TOMATOES_ORIGINAL_PRODUCT)) {
        var oliveOilButton = new InlineKeyboardButton();
        oliveOilButton.setText("Оливкова олія");
        oliveOilButton.setCallbackData(OLIVE_OIL_BUTTON);

            if (!productsMap.get(chatId).equals(TOMATOES_EXTRA_PRODUCT)) {
                var mixedOilButton = new InlineKeyboardButton();
                mixedOilButton.setText("Суміш олій");
                mixedOilButton.setCallbackData(MIXED_OIL_BUTTON);

                rowInLine.add(mixedOilButton);
            }

        rowInLine.add(oliveOilButton);

        } else {
            var oliveOilButton = new InlineKeyboardButton();
            oliveOilButton.setText("Рафінована олія");
            oliveOilButton.setCallbackData(REFINED_OIL_BUTTON);

            rowInLine.add(oliveOilButton);
        }
        rowsInLine.add(rowInLine);

        inlineKeyboardMarkup.setKeyboard(rowsInLine);
        message.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(message);
        } catch (TelegramApiException ignored) {}
    }

    public void setTomatoesCountMixedOil(long chatId, int massageId) {
        String countMessage = "Скільки баночок бажаєте?";
        String description = "Oписание";

        switch (productsMap.get(chatId)) {
            case TOMATOES_CLASSIC_PRODUCT :
                productsMap.put(chatId, TOMATOES_CLASSIC_PRODUCT_MIXED_OIL);
                description = TOMATOES_CLASSIC_DESCRIPTION_MIXED_OIL;
                break;
            case TOMATOES_PIQUANT_PRODUCT:
                productsMap.put(chatId, TOMATOES_PIQUANT_PRODUCT_MIXED_OIL);
                description = TOMATOES_PIQUANT_DESCRIPTION_MIXED_OIL;
                break;
            case TOMATOES_SPICY_PRODUCT:
                productsMap.put(chatId, TOMATOES_SPICY_PRODUCT_MIXED_OIL);
                description = TOMATOES_SPICY_DESCRIPTION_MIXED_OIL;
                break;
        }

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(countMessage);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("3");
        row.add("2");
        row.add("1");
        keyboardRows.add(row);
        row = new KeyboardRow();
        row.add("Інша кількість");
        keyboardRows.add(row);
        row = new KeyboardRow();
        row.add("Повернутись");
        keyboardRows.add(row);
        keyboardMarkup.setKeyboard(keyboardRows);
        message.setReplyMarkup(keyboardMarkup);

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(String.valueOf(chatId));
        editMessageText.setText(description);
        editMessageText.setMessageId(massageId);

        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            log.error("Error occurred" + e.getMessage());
        }

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred" + e.getMessage());
        }
    }

    public void setTomatoesCountOliveOil(long chatId, int massageId) {
        String countMessage = "Скільки баночок бажаєте?";
        String description = "Oписание";

        switch (productsMap.get(chatId)) {
            case TOMATOES_CLASSIC_PRODUCT :
                description = TOMATOES_CLASSIC_DESCRIPTION_OLIVE_OIL;
                productsMap.put(chatId, TOMATOES_CLASSIC_PRODUCT_OLIVE_OIL);
                break;
            case TOMATOES_PIQUANT_PRODUCT:
                description = TOMATOES_PIQUANT_DESCRIPTION_OLIVE_OIL;
                productsMap.put(chatId, TOMATOES_PIQUANT_PRODUCT_OLIVE_OIL);
                break;
            case TOMATOES_SPICY_PRODUCT:
                description = TOMATOES_SPICY_DESCRIPTION_OLIVE_OIL;
                productsMap.put(chatId, TOMATOES_SPICY_PRODUCT_OLIVE_OIL);
                break;
            case TOMATOES_EXTRA_PRODUCT:
                productsMap.put(chatId, TOMATOES_EXTRA_PRODUCT_OLIVE_OIL);
                break;
        }

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(countMessage);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("3");
        row.add("2");
        row.add("1");
        keyboardRows.add(row);
        row = new KeyboardRow();
        row.add("Інша кількість");
        keyboardRows.add(row);
        row = new KeyboardRow();
        row.add("Повернутись");
        keyboardRows.add(row);
        keyboardMarkup.setKeyboard(keyboardRows);
        message.setReplyMarkup(keyboardMarkup);

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(String.valueOf(chatId));
        editMessageText.setText(description);
        editMessageText.setMessageId(massageId);

        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            log.error("Error occurred" + e.getMessage());
        }

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred" + e.getMessage());
        }
    }

    public void setTomatoesCountRefinedOil(long chatId) {
        String countMessage = "Скільки баночок бажаєте?";

        productsMap.put(chatId, TOMATOES_ORIGINAL_PRODUCT_REFINED_OIL);

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(countMessage);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("3");
        row.add("2");
        row.add("1");
        keyboardRows.add(row);
        row = new KeyboardRow();
        row.add("Інша кількість");
        keyboardRows.add(row);
        row = new KeyboardRow();
        row.add("Повернутись");
        keyboardRows.add(row);
        keyboardMarkup.setKeyboard(keyboardRows);
        message.setReplyMarkup(keyboardMarkup);


        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred" + e.getMessage());
        }
    }


}
