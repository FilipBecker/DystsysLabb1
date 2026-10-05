package ui.ViewItems;

import Enums.Privilege;
import bo.Model.User;
/**
 * A User Object represented in the UI as a ViewUser Object instead of the model version
 */
public class ViewUser {
    private final int id;
    private final String username;
    private final Privilege role; // ADMIN, WAREHOUSE, CUSTOMER
    private final String email;

    public ViewUser(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole();
        this.email = user.getEmail();
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Privilege getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }
}
