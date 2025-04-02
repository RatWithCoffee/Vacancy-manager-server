package vacancy_manager.models;


import java.io.Serial;
import java.io.Serializable;

public class User  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private int id;
    private String login;
    private String password;
    private Role role;

    public User(String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }


    public boolean isAdmin() {
        return role == Role.ADMIN;
    }


    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
