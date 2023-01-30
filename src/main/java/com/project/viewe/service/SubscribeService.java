package com.project.viewe.service;

import com.project.viewe.exception.ProjectException;
import com.project.viewe.model.User;
import com.project.viewe.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscribeService {

    private final UserRepo userRepo;
    private final UserService userService;

    public String subscribeToUser(String username) {
        var user = userService.findByUsername(username);
        var currentUser = userService.getCurrentUser();
        var result ="";
        if(userService.ifSubscribeToUser(user, currentUser)){
            user.removeFromSubscribers(currentUser.getId());
            currentUser.removeFromSubscribeToUsers(user.getId());
            result = "Unfollowed by Akram " + username;
        }else {
            if(Objects.equals(username, currentUser.getUsername())){
                result = "Unable to subscribe to user " + username;
            }else {
                user.addToSubscribers(currentUser.getId());
                currentUser.addToSubscribeToUsers(user.getId());
                result = "Subscribe to user " + username + " successful";
            }

        }
        userRepo.save(user);
        userRepo.save(currentUser);
        return result;
    }

    public List<String> getAllSubscribers() {
        var user = userService.getCurrentUser().getSubscribers();
        return user.stream().map(userId -> getUsername(findUserById(userId))).toList();
    }

    public List<String> getAllSubscribedToUsers() {
        var user  = userService.getCurrentUser().getSubscribedToUsers();
        return user.stream().map(userId -> getUsername(findUserById(userId))).toList();
    }

    public List<String> getAllSubscribersToUser(String username) {
        var user = userService.findByUsername(username).getSubscribers();
        return user.stream().map(userId -> getUsername(findUserById(userId))).toList();
    }

    public List<String> getAllUserSubscribedToUser(String username) {
        var user = userService.findByUsername(username).getSubscribedToUsers();
        return user.stream().map(userId -> getUsername(findUserById(userId))).toList();
    }

    private User findUserById(Long userId) {
        return userRepo.findById(userId).orElseThrow(() -> new ProjectException("Cannot find user by id " + userId));
    }

    public String getUsername(User user){
        return user.getUsername();
    }

}
