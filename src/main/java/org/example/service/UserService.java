package org.example.service;

import org.example.pojo.User;

public interface UserService {
    //根据用户吗查询用户
    User findByUserName(String username);

    //注册
    void register(String username, String password);
    //更新信息
    void update(User user);

    void updateAvatar(String avatarUrl);

    void updatePwd(String newPwd);
}
