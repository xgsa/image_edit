package org.coolshop.domain;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class User {

    private Long id;
    private String login;
    private byte[] password;
    private String fullName;


    public User() {
    }

    public User(Long id, String login, byte[] password, String fullName) {
        this.id = id;
        this.login = login;
        this.fullName = fullName;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    private byte[] hashPassword(String rawPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] rawBytes = rawPassword.getBytes("UTF-8");
            return md.digest(rawBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("The required hash algorithm is not available");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("The required encoding is not supported");
        }
    }

    public boolean checkPassword(String password) {
        return Arrays.equals(this.password, hashPassword(password));
    }
}
