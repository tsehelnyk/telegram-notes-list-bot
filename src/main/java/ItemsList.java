import java.util.ArrayList;
import java.util.List;

public class ItemsList {
    private List<String> items;

    public ItemsList() {
        items = new ArrayList<>();
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
