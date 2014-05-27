package org.coolshop.domain;

import javax.persistence.*;


// NOTE: "user" is a keyword in some of DB engines.
@Entity(name = "XUser")
public class User implements BaseEntity {

    public enum Role {
        Customer,
        Manager,
        Admin,
        User,
    }

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String login;

    @Column(length = 32, nullable = false)
    private String password;

    private String fullName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;


    // For hibernate, not intended to be used by the clients.
    protected User() {}

    public User(String login, String password, String fullName, Role role) {
        this.login = login;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (fullName != null ? !fullName.equals(user.fullName) : user.fullName != null) return false;
        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (login != null ? !login.equals(user.login) : user.login != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (role != user.role) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", fullName='" + fullName + '\'' +
                ", role=" + role +
                '}';
    }
}
