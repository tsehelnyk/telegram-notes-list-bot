import java.util.ArrayList;
import java.util.List;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {
    private ItemsList itemsList = new ItemsList();

    public static void main(String[] args) {
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

        firstKeyboardRow.add(new KeyboardButton("1"));
//        firstKeyboardRow.add(new KeyboardButton("2"));
//        firstKeyboardRow.add(new KeyboardButton("3"));

        keyboardRowList.add(firstKeyboardRow);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            switch (message.getText().substring(0, 1)) {
                case "1" : sendMsg(message, itemsList.toString());
                    break;
//                case "2" : sendMsg(message, "This is two");
//                    break;
//                case "3" : sendMsg(message, "III");
//                    break;
                case "+" : itemsList.addItem(message.getText().substring(1).strip());
                    sendMsg(message, itemsList.toString());
                    break;
                case "-" : itemsList
                        .getItems()
                        .remove(Integer.parseInt(message.getText().substring(1).strip()) - 1);
                    sendMsg(message, itemsList.toString());
                    break;
                default : sendMsg(message, "Wrong command!");
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
