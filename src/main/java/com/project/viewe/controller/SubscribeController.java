package com.project.viewe.controller;

import com.project.viewe.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/subscribe")
public class SubscribeController {

    private final SubscribeService subscribeService;

    @PostMapping("/{username}")
    @ResponseStatus(HttpStatus.CREATED)
    public String subscribeToUser(@PathVariable String username){
        return subscribeService.subscribeToUser(username);
    }

    @GetMapping("/subscribers")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getAllSubscribers(){
        return subscribeService.getAllSubscribers();
    }

    @GetMapping("/subscribedToUsers")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getAllUsersISubscribed(){
        return subscribeService.getAllSubscribedToUsers();
    }

    @GetMapping("/subscribers/{username}")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getAllSubscribersToUser(@PathVariable String username){
        return subscribeService.getAllSubscribersToUser(username);
    }

    @GetMapping("/subscribedToUsers/{username}")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getAllUsersTheUserIsFollowing(@PathVariable String username){
        return subscribeService.getAllUserSubscribedToUser(username);
    }

}
