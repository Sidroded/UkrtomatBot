package com.sidroded.ukrtomatbot.service;

import com.sidroded.ukrtomatbot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
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

    static private final String TOMATOES_CLASSIC_PRODUCT_MIXED_OIL = Products.TOMATOES_CLASSIC_PRODUCT_MIXED_OIL;
    static private final String TOMATOES_PIQUANT_PRODUCT_MIXED_OIL = Products.TOMATOES_PIQUANT_PRODUCT_MIXED_OIL;
    static private final String TOMATOES_SPICY_PRODUCT_MIXED_OIL = Products.TOMATOES_SPICY_PRODUCT_MIXED_OIL;

    static private final String TOMATOES_CLASSIC_PRODUCT_OLIVE_OIL = Products.TOMATOES_CLASSIC_PRODUCT_OLIVE_OIL;
    static private final String TOMATOES_PIQUANT_PRODUCT_OLIVE_OIL = Products.TOMATOES_PIQUANT_PRODUCT_OLIVE_OIL;
    static private final String TOMATOES_SPICY_PRODUCT_OLIVE_OIL = Products.TOMATOES_SPICY_PRODUCT_OLIVE_OIL;
    static private final String TOMATOES_EXTRA_PRODUCT_OLIVE_OIL = Products.TOMATOES_EXTRA_PRODUCT_OLIVE_OIL;

    static private final String TOMATOES_ORIGINAL_PRODUCT_REFINED_OIL = Products.TOMATOES_ORIGINAL_PRODUCT_REFINED_OIL;

    /*---------------------------PRODUCT PRICE-------------------------*/

    static private final int TOMATOES_SPICY_PRODUCT_MIXED_OIL_PRICE = 150;
    static private final int TOMATOES_CLASSIC_PRODUCT_MIXED_OIL_PRICE = 150;
    static private final int TOMATOES_PIQUANT_PRODUCT_MIXED_OIL_PRICE = 150;

    static private final int TOMATOES_CLASSIC_PRODUCT_OLIVE_OIL_PRICE = 200;
    static private final int TOMATOES_PIQUANT_PRODUCT_OLIVE_OIL_PRICE = 200;
    static private final int TOMATOES_SPICY_PRODUCT_OLIVE_OIL_PRICE = 200;
    static private final int TOMATOES_EXTRA_PRODUCT_OLIVE_OIL_PRICE = 150;

    static private final int TOMATOES_ORIGINAL_PRODUCT_REFINED_OIL_PRICE = 150;

    /*------------------------PRODUCT DESCRIPTION----------------------*/

    static private final String TOMATOES_CLASSIC_DESCRIPTION = Description.TOMATOES_CLASSIC_DESCRIPTION;
    static private final String TOMATOES_PIQUANT_DESCRIPTION = Description.TOMATOES_PIQUANT_DESCRIPTION;
    static private final String TOMATOES_SPICY_DESCRIPTION = Description.TOMATOES_SPICY_DESCRIPTION;
    static private final String TOMATOES_ORIGINAL_DESCRIPTION = Description.TOMATOES_ORIGINAL_DESCRIPTION;
    static private final String TOMATOES_EXTRA_DESCRIPTION = Description.TOMATOES_EXTRA_DESCRIPTION;

    static private final String TOMATOES_CLASSIC_DESCRIPTION_MIXED_OIL = Description.TOMATOES_CLASSIC_DESCRIPTION_MIXED_OIL;
    static private final String TOMATOES_PIQUANT_DESCRIPTION_MIXED_OIL = Description.TOMATOES_PIQUANT_DESCRIPTION_MIXED_OIL;
    static private final String TOMATOES_SPICY_DESCRIPTION_MIXED_OIL = Description.TOMATOES_SPICY_DESCRIPTION_MIXED_OIL;

    static private final String TOMATOES_CLASSIC_DESCRIPTION_OLIVE_OIL = Description.TOMATOES_CLASSIC_DESCRIPTION_OLIVE_OIL;
    static private final String TOMATOES_PIQUANT_DESCRIPTION_OLIVE_OIL = Description.TOMATOES_PIQUANT_DESCRIPTION_OLIVE_OIL;
    static private final String TOMATOES_SPICY_DESCRIPTION_OLIVE_OIL = Description.TOMATOES_SPICY_DESCRIPTION_OLIVE_OIL;

    /*---------------------------INLINE BUTTONS-----------------------*/

    static private final String MIXED_OIL_BUTTON = "MIXED_OIL_BUTTON";
    static private final String OLIVE_OIL_BUTTON = "OLIVE_OIL_BUTTON";
    static private final String REFINED_OIL_BUTTON = "REFINED_OIL_BUTTON";
    static private final String CLEAR_BASKET_BUTTON = "CLEAR_BASKET";
    static private final String CREATE_AN_ORDER_BUTTON = "CREATE_AN_ORDER";
    static private final String CONFIRM_GUEST_INFO_BUTTON = "CONFIRM_GUEST_INFO_BUTTON";
    static private final String CHANGE_GUEST_INFO_BUTTON = "CHANGE_GUEST_INFO_BUTTON";

    /*-----------------------------IMG URL-----------------------------*/

    static private final String TOMATOES_CLASSIC_URL = "https://ibb.co/dk0vqZS";
    static private final String TOMATOES_PIQUANT_URL = "https://ibb.co/8b7BkPG";
    static private final String TOMATOES_SPICY_URL = "https://ibb.co/DVxdZp2";
    static private final String TOMATOES_ORIGINAL_URL = "https://ibb.co/B2BggLY";
    static private final String TOMATOES_EXTRA_URL = "https://ibb.co/gwqdbqk";

    /*------------------------------INFO------------------------------*/
    static private final String ERROR = EmojiParser.parseToUnicode("?????????????? ?????????????? " + ":shrug:");
    static private final String IN_DEVELOP = EmojiParser.parseToUnicode("???? ?????????????? ???????? ?????????????????????? ?? ???????????????? " + ":shrug:");

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
                            " x" + count + "???? - " + priceMap.get(productsMap.get(chatId)) * count + "??????.");

                    sumMap.put(chatId, (int) (sumMap.get(chatId) + priceMap.get(productsMap.get(chatId)) * count));
                    choiceCommandReceived(chatId);
                }
            }

            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "??????????????":
                    shopCommandReceived(chatId);
                    break;
                case "???????????????? ???? ????????????":
                    deliveryCommandReceived(chatId);
                    break;
                case "????'??????????????":
                    contactCommandReceived(chatId);
                    break;
                case "??????????":
                    basketCommandReceived(chatId);
                    break;
                case "??????????????????????":
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
                case "???????????? ????":
                    if (!returnMap.containsKey(chatId)) returnMap.put(chatId, SHOP_POSITION);

                    switch (returnMap.get(chatId)) {
                        case TOMATOES_CHOICE_POSITION:
                            tomatoesCommandReceived(chatId);
                            break;
                        default:
                            sendMassage(chatId, ERROR);
                    }
                    break;
                case "??????":
                    basketCommandReceived(chatId);
                    receiptCommandReceived(chatId);
                    break;
                case "??'?????????? ????????????":
                    tomatoesCommandReceived(chatId);
                    break;
                case "????????????????" :
                    productsMap.put(chatId, TOMATOES_CLASSIC_PRODUCT);
                    setTomatoesProduct(chatId);
                    break;
                case "????????????????":
                    productsMap.put(chatId, TOMATOES_PIQUANT_PRODUCT);
                    setTomatoesProduct(chatId);
                    break;
                case "??????????????????????":
                    productsMap.put(chatId, TOMATOES_ORIGINAL_PRODUCT);
                    setTomatoesProduct(chatId);
                    break;
                case "????????????":
                    productsMap.put(chatId, TOMATOES_SPICY_PRODUCT);
                    setTomatoesProduct(chatId);
                    break;
                case "????????????":
                    productsMap.put(chatId, TOMATOES_EXTRA_PRODUCT);
                    setTomatoesProduct(chatId);
                    break;
                case "???????????????????? ??????????????":
                    createAnOrderCommandReceived(chatId);
                    break;
                case "???? ??????????????":
                    beginCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
            }


            if (returnMap.get(chatId).equals(CREATE_AN_ORDER_POSITION)) {
                if (guestInfoMap.computeIfAbsent(chatId, k -> new ArrayList<>()).size() == 2) {
                    guestInfoMap.computeIfAbsent(chatId, k -> new ArrayList<>()).add(messageText);
                    sendMassage(chatId, "?????????????? ?????????? ???????????????? ???????????????????? ?????????????????? ?? (+380).");
                } else if (guestInfoMap.computeIfAbsent(chatId, k -> new ArrayList<>()).size() == 3) {
                    sendMassage(chatId, "?????????????? ?????????? ???? ?????????? ???????????????????? ?????????? ??????????.");
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
                    guestInfoMap.computeIfAbsent(chatId, k -> new ArrayList<>()).add("????????-?????????? ?????????????????? ?????????????? ??????????:");
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
        guestInfoMap.computeIfAbsent(chatId, k -> new ArrayList<>()).add("????????-?????????? ?????????????????? ?????????????? ??????????:");
        guestInfoMap.computeIfAbsent(chatId, k -> new ArrayList<>()).add("");

        String startMessage = name + ", ?????????????? ??????! ?????????????? ?????? ?? ???????????? ???????????????? ?????????????????? ????????????????????.";

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(startMessage);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("??????????????");
        row.add("???????????????? ???? ????????????");
        row.add("????'??????????????.");
        keyboardRows.add(row);
        row = new KeyboardRow();
        row.add("??????????");
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
        String shopMassage = "???? ?????????????? ?????????????????????";
        returnMap.put(chatId, SHOP_POSITION);

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(shopMassage);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("??'?????????? ????????????");
        /*row.add("??????????????");
        row.add("??????????????");*/
        keyboardRows.add(row);
        row = new KeyboardRow();
        row.add("??????????????????????");
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
        String deliveryMassage = "???????????????? ???? ???????????????? ?????????? ??????????.\n\n" +
                "????????????:\n" +
                "1. 100% ??????????????????????.\n" +
                "2. ???????????????????? ????????????.";

        sendMassage(chatId, deliveryMassage);
    }

    public void contactCommandReceived(long chatId) {
        String contactText = "";

        sendMassage(chatId, IN_DEVELOP);
    }        //soon

    public void basketCommandReceived(long chatId) {
        String basketMessage;
        String basketTitle = "???????? ????????????????????:";

        if ((!basketMap.containsKey(chatId)) || basketMap.get(chatId).isEmpty()) {
            basketMessage = "?????? ?????????? ????????????????. \n\n" +
                    "?????????? ???? ????????????: " + sumMap.get(chatId) + "??????.";
        } else {
            StringBuilder basketBuilder = new StringBuilder();

            basketBuilder.append(basketTitle + "\n\n");

            for (String str : basketMap.get(chatId)) {
                basketBuilder.append(str + "\n\n");
            }

            basketBuilder.append("?????????? ???? ????????????: " + sumMap.get(chatId) + "??????.");

            basketMessage = basketBuilder.toString();
        }

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(basketMessage);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        var clearBasketButton = new InlineKeyboardButton();
        clearBasketButton.setText("????????????????");
        clearBasketButton.setCallbackData(CLEAR_BASKET_BUTTON);

        rowInLine.add(clearBasketButton);
        rowsInLine.add(rowInLine);

        rowInLine = new ArrayList<>();

        var createAnOrderButton = new InlineKeyboardButton();
        createAnOrderButton.setText("???????????????????? ??????????????");
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
        String shopMassage = "?????????? 5 ???????????????????????????? ????????????!";
        returnMap.put(chatId, TOMATOES_POSITION);

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(shopMassage);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("????????????????");
        row.add("????????????????");
        row.add("??????????????????????");
        keyboardRows.add(row);
        row = new KeyboardRow();
        row.add("????????????");
        row.add("????????????");
        keyboardRows.add(row);
        row = new KeyboardRow();
        row.add("??????????????????????");
        row.add("??????????");
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
        String added = "????????????!";

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(added);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("???????????? ????");
        row.add("??????");
        keyboardRows.add(row);
        row = new KeyboardRow();
        row.add("??????????????????????");
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
        String added = "???????????? ???????????? ????, ?????? ?????????????????? ??????????????!";

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(added);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("???????????? ????");
        row.add("???????????????????? ??????????????");
        keyboardRows.add(row);
        row = new KeyboardRow();
        row.add("??????????????????????");
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
        String str1 = "?????????????????? ?????????? ?????????? ??????????.";
        String str2 = "?????????????? ???????? ???????????????? ???? ????'??.";

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
        clearBasketButton.setText("??????????????????????");
        clearBasketButton.setCallbackData(CONFIRM_GUEST_INFO_BUTTON);

        rowInLine.add(clearBasketButton);
        rowsInLine.add(rowInLine);

        rowInLine = new ArrayList<>();

        var createAnOrderButton = new InlineKeyboardButton();
        createAnOrderButton.setText("??????????????");
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

        String str1 = "???????????? ?????? ???????????? ???????????????????? (???????????????? ????????????)";
        String str2 = "5168 7422 3406 4297";
        String str3 = "?????????????? ???? ???????????? ??????!";
        StringBuilder builder = new StringBuilder();
        builder.append("???????? ???????????????????? ??????????: " + orderNum + "\n\n");

        for (String str : basketMap.get(chatId)) {
            builder.append(str + "\n\n");
        }

        builder.append("???? ????????????: " + sumMap.get(chatId) + "??????");

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
        row.add("???? ??????????????");
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
        String startMessage = name + ", ?????????????? ??????! ?????????????? ?????? ?? ???????????? ???????????????? ?????????????????? ????????????????????.";

        basketMap.put(chatId, new ArrayList<>());
        sumMap.put(chatId, 0);

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(startMessage);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("??????????????");
        row.add("???????????????? ???? ????????????");
        row.add("????'??????????????.");
        keyboardRows.add(row);
        row = new KeyboardRow();
        row.add("??????????");
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

    public void sendImageFromUrl(Long chatId, String URL) {
        SendPhoto sendPhotoRequest = new SendPhoto();
        sendPhotoRequest.setChatId(String.valueOf(chatId));
        sendPhotoRequest.setPhoto(new InputFile(URL));
        try {
            execute(sendPhotoRequest);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void getOwnerId(long chatId) {
        String path = "d:/Test/getId.txt";

        try (FileWriter writer = new FileWriter(path)){
            writer.write(String.valueOf(chatId));
        } catch (Exception ignored) {}
    }

    public void sendOrderToOwner(long chatId) {
        StringBuilder order = new StringBuilder();
        order.append("???????????????????? ??????????: " + orderNum + "\n\n");

        for (String str : basketMap.get(chatId)) {
            order.append(str + "\n\n");
        }

        order.append("C???????? ????????????????????: " + sumMap.get(chatId) + "??????");

        guestInfoMap.computeIfAbsent(chatId, k -> new ArrayList<>()).remove(0);

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
        String description = "?????? ?????????? ????????????????. \n\n" +
                "?????????? ???? ????????????: " + sumMap.get(chatId) + "??????.";

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(String.valueOf(chatId));
        editMessageText.setText(description);
        editMessageText.setMessageId(messageId);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        var clearBasketButton = new InlineKeyboardButton();
        clearBasketButton.setText("????????????????");
        clearBasketButton.setCallbackData(CLEAR_BASKET_BUTTON);

        rowInLine.add(clearBasketButton);
        rowsInLine.add(rowInLine);

        rowInLine = new ArrayList<>();

        var createAnOrderButton = new InlineKeyboardButton();
        createAnOrderButton.setText("???????????????????? ??????????????");
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

        String description = "???????????????? ?????? ??????????";
        String url = "";

        switch (productsMap.get(chatId)) {
            case TOMATOES_CLASSIC_PRODUCT:
                description = TOMATOES_CLASSIC_DESCRIPTION;
                url = TOMATOES_CLASSIC_URL;
                break;
            case TOMATOES_PIQUANT_PRODUCT:
                description = TOMATOES_PIQUANT_DESCRIPTION;
                url = TOMATOES_PIQUANT_URL;
                break;
            case TOMATOES_ORIGINAL_PRODUCT:
                description = TOMATOES_ORIGINAL_DESCRIPTION;
                url = TOMATOES_ORIGINAL_URL;
                break;
            case TOMATOES_SPICY_PRODUCT:
                description = TOMATOES_SPICY_DESCRIPTION;
                url = TOMATOES_SPICY_URL;
                break;
            case TOMATOES_EXTRA_PRODUCT:
                url = TOMATOES_EXTRA_URL;
                description = TOMATOES_EXTRA_DESCRIPTION;
        }

        sendImageFromUrl(chatId, url);

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(description);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        if (!productsMap.get(chatId).equals(TOMATOES_ORIGINAL_PRODUCT)) {
        var oliveOilButton = new InlineKeyboardButton();
        oliveOilButton.setText("???????????????? ????????");
        oliveOilButton.setCallbackData(OLIVE_OIL_BUTTON);

            if (!productsMap.get(chatId).equals(TOMATOES_EXTRA_PRODUCT)) {
                var mixedOilButton = new InlineKeyboardButton();
                mixedOilButton.setText("?????????? ????????");
                mixedOilButton.setCallbackData(MIXED_OIL_BUTTON);

                rowInLine.add(mixedOilButton);
            }

        rowInLine.add(oliveOilButton);

        } else {
            var oliveOilButton = new InlineKeyboardButton();
            oliveOilButton.setText("???????????????????? ????????");
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
        String countMessage = "?????????????? ?????????????? ???????????????";
        String description = "O??????????????";

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
        row.add("???????? ??????????????????");
        keyboardRows.add(row);
        row = new KeyboardRow();
        row.add("??????????????????????");
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
        String countMessage = "?????????????? ?????????????? ???????????????";
        String description = "O??????????????";

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
        row.add("???????? ??????????????????");
        keyboardRows.add(row);
        row = new KeyboardRow();
        row.add("??????????????????????");
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
        String countMessage = "?????????????? ?????????????? ???????????????";

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
        row.add("???????? ??????????????????");
        keyboardRows.add(row);
        row = new KeyboardRow();
        row.add("??????????????????????");
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
