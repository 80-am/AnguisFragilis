package com.example.anguisfragilis.Repository;

import com.example.anguisfragilis.Domain.User;

public interface Projekt4Repository {
    void addUser(User user);
    User checkUser(User user);
}
