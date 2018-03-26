package com.gigabyte.tugasakhir.vertx.packet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gigabyte.tugasakhir.vertx.model.User;
import io.vertx.core.shareddata.LocalMap;

public class UserList extends Message {
    private final LocalMap<String, User> userList;
    @JsonCreator
    public UserList(@JsonProperty("user") LocalMap<String, User> userList){
        this.userList = userList;
    }

    public LocalMap<String, User> getUserList() {
        return userList;
    }

    @Override
    public String toString() {
        return "UserList{" +
                "userList=" + userList +
                '}';
    }
}
