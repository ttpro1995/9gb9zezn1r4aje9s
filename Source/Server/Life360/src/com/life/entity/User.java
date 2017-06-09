package com.life.entity;

import com.life.common.JsonUtils;
import java.io.Serializable;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class User implements Serializable {

    private static final long serialVersionUID = -7174751641295169852L;

    public int id;

    public String firstName;
    public String lastName;
    public String email;
    public String phoneNumber;
    public String password;
    public String avatar;
    public String token;
    public long createdTime;
    public long updatedTime;

    public User() {
        createdTime = System.currentTimeMillis();
        updatedTime = createdTime;
    }

    public User(int id, String firstName, String lastName, String email, String phoneNumber, String password, String avatar, String token) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.avatar = avatar;
        this.token = token;
        createdTime = System.currentTimeMillis();
        updatedTime = createdTime;
    }

    public User(String firstName, String lastName, String email, String phoneNumber, String password, String avatar) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.avatar = avatar;

        createdTime = System.currentTimeMillis();
        updatedTime = createdTime;
    }

    public User(String firstName, String lastName, String email, String phoneNumber, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;

        createdTime = System.currentTimeMillis();
        updatedTime = createdTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(long updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }

}
