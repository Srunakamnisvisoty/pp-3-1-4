package web.service;

import web.model.User;

import java.util.List;

public interface UserService {
    List<User> userList();
    User add(User user);
    void delete(long id);
    User edit(User user, Boolean encryptPassword);
    User getById(long id);
    User getByLogin(String login);
}
