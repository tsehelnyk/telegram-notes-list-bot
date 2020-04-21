import java.util.ArrayList;
import java.util.List;

public class ItemsList {
    private String user;
    private List<String> items;

    public ItemsList() {
        user = "John";
        items = new ArrayList<>();
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }
    public List<String> getItems() {
        return items;
    }

    public void addItem(String item) {
        this.items.add(item);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        items.forEach(item -> stringBuilder.append(items.indexOf(item) + 1 + ". " + item + "\n") );
        return stringBuilder.toString();
    }
}
