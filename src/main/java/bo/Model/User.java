package bo.Model;

import Enums.Privilege;

/**
 * Represents a user in the model
 */
public class User {
    private int id;
    private String username;
    private String password;
    private Privilege role; // ADMIN, WAREHOUSE, CUSTOMER
    private String email;

    protected User(int id, String username, Privilege role, String email ){
        this.id = id;
        this.username = username;
        this.role = role;
        this.email = email;
    }

    protected User(int id, String username, String password, Privilege role, String email ){
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Privilege getRole() {
        return role;
    }
    public void setRole(Privilege role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }


}
