import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.passport.PassportData;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {
    private FirebaseService firebaseService = new FirebaseService();

    public static void main(String[] args) {

        try {
            FileInputStream serviceAccount =
                    new FileInputStream("src/my-tbots-base-firebase-adminsdk-pdz0b-dc3f910af1.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://my-tbots-base.firebaseio.com")
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendMsg(Message message, String messageText) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId());
//        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(messageText);
        try {
            setButtons(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow firstKeyboardRow = new KeyboardRow();

        firstKeyboardRow.add(new KeyboardButton("show list"));
//        firstKeyboardRow.add(new KeyboardButton("2"));
//        firstKeyboardRow.add(new KeyboardButton("3"));

        keyboardRowList.add(firstKeyboardRow);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();

        if (message != null && message.hasText()) {

            ItemsList itemsList = null;
            User user = message.getFrom();
            try {
                itemsList = firebaseService.getItemsList(user.getUserName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (itemsList == null) {
                itemsList = new ItemsList();
                itemsList.setUser(user.getUserName());
            }

            switch (message.getText().substring(0, 1)) {
                case "s":
                    try {
                        sendMsg(message, itemsList.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

//                case "/" :
//
//                    itemsList
//                            .getItems()
//                            .remove(Integer.parseInt(message.getText().substring(1).strip()) - 1);
//                    try {
//                        sendMsg(message, firebaseService.updateItemsList(itemsList).toString());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    break;

//                case "3" : sendMsg(message, "III");
//                    break;

                case "+":
                    try {
                        itemsList.addItem(message.getText().substring(1).strip());
                        sendMsg(message, firebaseService.saveItemsList(itemsList).toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case "-":
                    itemsList
                            .getItems()
                            .remove(Integer.parseInt(message.getText().substring(1).strip()) - 1);
                    try {
                        sendMsg(message, firebaseService.updateItemsList(itemsList).toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    sendMsg(message, "Wrong command!");
            }
        }
    }

    public String getBotUsername() {
        return "notesListBot";
    }

    public String getBotToken() {
        return "999539329:AAEePneZ1xsEYEuehkBBR84AXcs5DCZTPz4";
    }
}
