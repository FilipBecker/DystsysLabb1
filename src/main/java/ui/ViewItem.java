package ui;

import bo.TestItem;

public class ViewItem {
    private final String name;
    private final int value;

    public ViewItem(TestItem testItem) {
        this.name = testItem.getName();
        this.value = testItem.getValue();
    }

    @Override
    public String toString() {
        return "ViewItem{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
