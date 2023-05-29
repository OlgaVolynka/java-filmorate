package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserDbStorage userDbStorage;
    private final JdbcTemplate jdbcTemplate;
    private long id = 0;

    @Autowired
    public UserService(UserDbStorage userDbStorage, JdbcTemplate jdbcTemplate) {
        this.userDbStorage = userDbStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addFriends(long id, long friendId) {

        userDbStorage.getUserById(id);
        userDbStorage.getUserById(friendId);

        String sqlQuery = "insert into users_friend(user_id, friend_id) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery,
                id, friendId
        );


    }

    public void deleteFriends(Long id, Long friendId) {

        userDbStorage.getUserById(id);
        userDbStorage.getUserById(friendId);

        String sqlQuery = "delete from users_friend where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlQuery, id, friendId);

    }

    public List<User> getCommonFriends(Long id, Long friendId) {

        userDbStorage.getUserById(id);
        userDbStorage.getUserById(friendId);

        Set<Long> friendsList = getSetIdFriends(id);  //получили список друзей
        Set<Long> secondFriendsList = getSetIdFriends(friendId);  //получили список друзей

        List<User> listUser = new ArrayList<>();
        if (friendsList.isEmpty() || secondFriendsList.isEmpty()) {
            return listUser;
        }

        listUser = secondFriendsList.stream()
                .filter(friendsList::contains)
                .map(userId -> getUserById(userId))
                .collect(Collectors.toList());

        return listUser;
    }

    public List<User> getSetFriends(Long userId) {

        userDbStorage.getUserById(userId);

        Set<Long> listId = getSetIdFriends(userId);
        List<User> listUser;

        listUser = listId.stream()
                .map(user -> getUserById(user))
                .collect(Collectors.toList());

        return listUser;

    }

    public User getUserById(Long userId) {

        return userDbStorage.getUserById(userId);
    }

    public User create(User user) {

        String name = user.getName();

        if (name == null || name.isBlank()) {
            user.setName(user.getLogin());
        }
        return userDbStorage.create(user);
    }

    public User updateUser(User user) {

        userDbStorage.getUserById(user.getId());
        if (user.getName().isBlank() || user.getName() == null) {
            user.setName(user.getLogin());
        }
        userDbStorage.updateUser(user);
        return user;
    }

    public Set<Long> getSetIdFriends(Long userId) {

        userDbStorage.getUserById(userId);
        Set<Long> listIdUser = new HashSet<>();

        SqlRowSet rs = jdbcTemplate.queryForRowSet("select * from users_friend where user_id = ?", userId);

        while (rs.next()) {

            listIdUser.add((long) rs.getInt("friend_id"));
        }
        return listIdUser;
    }
}
