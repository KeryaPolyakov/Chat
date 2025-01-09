package com.kirillpolyakov.service;


import com.kirillpolyakov.model.User;

public interface UserService {

    void add(User user);

    User get(long id);



}
