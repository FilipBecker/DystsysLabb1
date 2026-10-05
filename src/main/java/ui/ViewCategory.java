package ui;

import Model.Category;

public class ViewCategory {
    private int id;
    private String name;

    public ViewCategory(Category category) {
        name = category.getName();
        id = category.getId();
    }

    public ViewCategory() {
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
