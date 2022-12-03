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
    static private int orderNum = 30_000;

    /*----------------------------POSITION------------------------------*/
    static private final String SHOP_POSITION = "SHOP";
    static private final String TOMATOES_POSITION = "TOMATOES";
    static private final String TOMATOES_CHOICE_POSITION = "TOMATOES_CHOICE";
    static private final String CREATE_AN_ORDER_POSITION = "CREATE_AN_ORDER_POSITION";

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
    static private final String CLEAR_BASKET_BUTTON = "CLEAR_BASKET";
    static private final String CREATE_AN_ORDER_BUTTON = "CREATE_AN_ORDER";
    static private final String CONFIRM_GUEST_INFO_BUTTON = "CONFIRM_GUEST_INFO_BUTTON";
    static private final String CHANGE_GUEST_INFO_BUTTON = "CHANGE_GUEST_INFO_BUTTON";

    /*------------------------------INFO------------------------------*/
    static private final String ERROR = EmojiParser.parseToUnicode("Сталася помилка " + ":shrug:");
    static private final String IN_DEVELOP = EmojiParser.parseToUnicode("Ця частина боту знаходиться в розробці " + ":shrug:");

    /*----------------------------HASH MAPS---------------------------*/

    private HashMap<Long, String> returnMap = new HashMap<>();
    private HashMap<Long, String> productsMap = new HashMap<>();
    private HashMap<Long, ArrayList<String>> basketMap = new HashMap<>();
    private HashMap<Long, Integer> sumMap = new HashMap<>();
    private HashMap<String, Integer> priceMap = new HashMap<>();
    private HashMap<Long, ArrayList<String>> guestInfoMap = new HashMap<>();

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
                long count = Integer.parseInt(messageText);
                if (count < 100) {
                    basketMap.computeIfAbsent(chatId, k -> new ArrayList<>()).add(productsMap.get(chatId) +
                            " x" + count + "шт - " + priceMap.get(productsMap.get(chatId)) * count + "грн.");

                    sumMap.put(chatId, (int) (sumMap.get(chatId) + priceMap.get(productsMap.get(chatId)) * count));
                    choiceCommandReceived(chatId);
                }
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
                case "Додати ще":
                    if (!returnMap.containsKey(chatId)) returnMap.put(chatId, SHOP_POSITION);

                    switch (returnMap.get(chatId)) {
                        case TOMATOES_CHOICE_POSITION:
                            tomatoesCommandReceived(chatId);
                            break;
                        default:
                            sendMassage(chatId, ERROR);
                    }
                    break;
                case "Чек":
                    basketCommandReceived(chatId);
                    receiptCommandReceived(chatId);
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
                case "Сформувати рахунок":
                    createAnOrderCommandReceived(chatId);
                    break;
                case "На початок":
                    beginCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
            }


            if (returnMap.get(chatId).equals(CREATE_AN_ORDER_POSITION)) {
                if (guestInfoMap.computeIfAbsent(chatId, k -> new ArrayList<>()).size() == 2) {
                    guestInfoMap.computeIfAbsent(chatId, k -> new ArrayList<>()).add(messageText);
                    sendMassage(chatId, "Введіть номер телефону отримувача починаючи з (+380).");
                } else if (guestInfoMap.computeIfAbsent(chatId, k -> new ArrayList<>()).size() == 3) {
                    sendMassage(chatId, "Введіть місто та номер відділення Нової Пошти.");
                    guestInfoMap.computeIfAbsent(chatId, k -> new ArrayList<>()).add(messageText);
                } else if (guestInfoMap.computeIfAbsent(chatId, k -> new ArrayList<>()).size() == 4) {
                    guestInfoMap.computeIfAbsent(chatId, k -> new ArrayList<>()).add(messageText);
                    checkGuestInfoCommandReceived(chatId);
                    returnMap.put(chatId, SHOP_POSITION);
                }
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
                case CLEAR_BASKET_BUTTON:
                    sumMap.put(chatId, 0);
                    basketMap.put(chatId, new ArrayList<>());
                    updateClearBasket(chatId, massageId);
                    break;
                case CREATE_AN_ORDER_BUTTON:
                    createAnOrderCommandReceived(chatId);
                    break;
                case CHANGE_GUEST_INFO_BUTTON:
                    guestInfoMap.put(chatId, new ArrayList<>());
                    guestInfoMap.computeIfAbsent(chatId, k -> new ArrayList<>()).add("Будь-ласка перевірте введені данні:");
                    guestInfoMap.computeIfAbsent(chatId, k -> new ArrayList<>()).add("");
                    createAnOrderCommandReceived(chatId);
                    break;
                case CONFIRM_GUEST_INFO_BUTTON:
                    finishOrderCommandReceived(chatId);
                    sendOrderToOwner(chatId);
                    break;
            }
        }
    }

    /*---------------------RECEIVED COMMANDS--------------------*/

    public void startCommandReceived(long chatId, String name) {
        createAllPrices();
        sumMap.put(chatId, 0);
        guestInfoMap.put(chatId, new ArrayList<>());
        guestInfoMap.computeIfAbsent(chatId, k -> new ArrayList<>()).add("Будь-ласка перевірте введені данні:");
        guestInfoMap.computeIfAbsent(chatId, k -> new ArrayList<>()).add("");

        String startMessage = name + ", доброго дня! Вітаємо вас у нашому магазині крафтових смаколиків.";

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
    }        //soon

    public void basketCommandReceived(long chatId) {
        String basketMessage;
        String basketTitle = "Ваше замовлення:";

        if ((!basketMap.containsKey(chatId)) || basketMap.get(chatId).isEmpty()) {
            basketMessage = "Ваш кошик порожній. \n\n" +
                    "Разом до сплати: " + sumMap.get(chatId) + "грн.";
        } else {
            StringBuilder basketBuilder = new StringBuilder();

            basketBuilder.append(basketTitle + "\n\n");

            for (String str : basketMap.get(chatId)) {
                basketBuilder.append(str + "\n\n");
            }

            basketBuilder.append("Разом до сплати: " + sumMap.get(chatId) + "грн.");

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
        clearBasketButton.setCallbackData(CLEAR_BASKET_BUTTON);

        rowInLine.add(clearBasketButton);
        rowsInLine.add(rowInLine);

        rowInLine = new ArrayList<>();

        var createAnOrderButton = new InlineKeyboardButton();
        createAnOrderButton.setText("Сформувати рахунок");
        createAnOrderButton.setCallbackData(CREATE_AN_ORDER_BUTTON);

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

    public void choiceCommandReceived(long chatId) {
        String added = "Додано!";

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(added);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Додати ще");
        row.add("Чек");
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

    public void receiptCommandReceived(long chatId) {
        String added = "Можете додати ще, або сформвати рахунок!";

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(added);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Додати ще");
        row.add("Сформувати рахунок");
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

    public void createAnOrderCommandReceived(long chatId) {
        returnMap.put(chatId, CREATE_AN_ORDER_POSITION);
        String str1 = "Заповніть данні Нової Пошти.";
        String str2 = "Вкажіть ваші Прізвище та Ім'я.";

        sendMassage(chatId, str1);
        sendMassage(chatId, str2);
    }

    public void checkGuestInfoCommandReceived(long chatId) {
        StringBuilder builder = new StringBuilder();
        for (String str : guestInfoMap.get(chatId)) {
            builder.append(str + "\n");
        }

        SendMessage message = new SendMessage();
        message.setText(builder.toString());
        message.setChatId(String.valueOf(chatId));

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        var clearBasketButton = new InlineKeyboardButton();
        clearBasketButton.setText("Підтвердити");
        clearBasketButton.setCallbackData(CONFIRM_GUEST_INFO_BUTTON);

        rowInLine.add(clearBasketButton);
        rowsInLine.add(rowInLine);

        rowInLine = new ArrayList<>();

        var createAnOrderButton = new InlineKeyboardButton();
        createAnOrderButton.setText("Змінити");
        createAnOrderButton.setCallbackData(CHANGE_GUEST_INFO_BUTTON);

        rowInLine.add(createAnOrderButton);
        rowsInLine.add(rowInLine);

        inlineKeyboardMarkup.setKeyboard(rowsInLine);
        message.setReplyMarkup(inlineKeyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred" + e.getMessage());
        }

        returnMap.put(chatId, SHOP_POSITION);
    }

    public void finishOrderCommandReceived(long chatId) {
        orderNum++;

        String str1 = "Картка для оплати замовлення (Бігленко Андрій)";
        String str2 = "5168 7422 3406 4297";
        String str3 = "Дякуємо що обрали нас!";
        StringBuilder builder = new StringBuilder();
        builder.append("Ваше замовлення номер: " + orderNum + "\n\n");

        for (String str : basketMap.get(chatId)) {
            builder.append(str + "\n\n");
        }

        builder.append("До сплати: " + sumMap.get(chatId) + "грн");

        sendMassage(chatId, builder.toString());
        sendMassage(chatId, str1);
        sendMassage(chatId, str2);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(str3);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("На початок");
        keyboardRows.add(row);
        keyboardMarkup.setKeyboard(keyboardRows);
        sendMessage.setReplyMarkup(keyboardMarkup);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error occurred" + e.getMessage());
        }
    }

    private void beginCommandReceived(long chatId, String name) {
        String startMessage = name + ", доброго дня! Вітаємо вас у нашому магазині крафтових смаколиків.";

        basketMap.put(chatId, new ArrayList<>());

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


    public void getOwnerId(long chatId) {
        String path = "d:/Test/getId.txt";

        try (FileWriter writer = new FileWriter(path)){
            writer.write(String.valueOf(chatId));
        } catch (Exception ignored) {}
    }

    public void sendOrderToOwner(long chatId) {
        StringBuilder order = new StringBuilder();


        for (String str : basketMap.get(chatId)) {
            order.append(str + "\n\n");
        }

        for (String str : guestInfoMap.get(chatId)) {
            order.append(str + "\n");
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

    public void updateClearBasket(long chatId, int messageId) {
        String description = "Ваш кошик порожній. \n\n" +
                "Разом до сплати: " + sumMap.get(chatId) + "грн.";

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(String.valueOf(chatId));
        editMessageText.setText(description);
        editMessageText.setMessageId(messageId);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        var clearBasketButton = new InlineKeyboardButton();
        clearBasketButton.setText("Очистити");
        clearBasketButton.setCallbackData(CLEAR_BASKET_BUTTON);

        rowInLine.add(clearBasketButton);
        rowsInLine.add(rowInLine);

        rowInLine = new ArrayList<>();

        var createAnOrderButton = new InlineKeyboardButton();
        createAnOrderButton.setText("Сформувати рахунок");
        createAnOrderButton.setCallbackData(CREATE_AN_ORDER_BUTTON);

        rowInLine.add(createAnOrderButton);
        rowsInLine.add(rowInLine);

        inlineKeyboardMarkup.setKeyboard(rowsInLine);
        editMessageText.setReplyMarkup(inlineKeyboardMarkup);

        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            log.error("Error occurred" + e.getMessage());
        }
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
