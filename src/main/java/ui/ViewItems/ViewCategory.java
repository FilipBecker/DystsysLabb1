package ui.ViewItems;

import bo.Model.Category;

/**
 * A Category Object represented in the UI as a ViewCategory Object instead of the model version
 */
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
