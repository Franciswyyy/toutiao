package com.nowcoder.model;

import org.springframework.stereotype.Component;

@Component
public class HostHolder {
    //用来存用户的，每个线程存自己的， 线程本地变量。
    private static ThreadLocal<User> users = new ThreadLocal<User>();

    public User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }

    public void clear() {
        users.remove();;
    }
}