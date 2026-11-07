package capstone_feast.model;

public class ComboItem {
    private int id;
    private String value;

    public ComboItem(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public int getId() { return id; }
    public String getValue() { return value; }

    @Override
    public String toString() {
        return value; 
    }
}