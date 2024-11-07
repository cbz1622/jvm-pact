package io.pact.impl;

import io.pact.models.User;
import io.pact.service.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public User getUserById(int id) {
        if (id == 1) {
            return new User(1,"John Doe");
        }
        return null;
    }

    @Override
    public User setUser(int id, String name) {
        return new User(id, name);
    }
}
