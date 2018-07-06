package com.jberrio.studiomanager.models;

import com.jberrio.studiomanager.models.Calendar.Calendar;
import com.jberrio.studiomanager.models.User.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Studio {
    @NotNull
    @Size(min=3)
    private String name;

    @NotNull
    @Size(min=3)
    private String address;

    @NotNull
    @Size(min=9,max =11)
    private String phoneNumber;

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min=3)
    private String type;

    @OneToMany
    @JoinColumn(name = "studio_id")
    private List<User> userList = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "studio_id")
    private List<Room> roomList = new ArrayList<>();

    private Calendar calendar = new Calendar();

    public Studio() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<User> getUserList() {
        return userList;
    }


    public List<Room> getRoomList() {
        return roomList;
    }

}
