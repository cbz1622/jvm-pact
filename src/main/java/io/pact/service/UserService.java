package io.pact.service;

import io.pact.models.User;

public interface UserService {
    User getUserById(int id);
    User setUser(int id, String name);
}
