package com.project.viewe.controller;

import com.project.viewe.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
@Slf4j
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{postId}/like")
    @ResponseStatus(HttpStatus.CREATED)
    public void addLikeToPost(@PathVariable Long postId){
        log.info("I am in like controller");
        likeService.addLikeToPost(postId);
    }

    @PostMapping("/{postId}/disLike")
    @ResponseStatus(HttpStatus.CREATED)
    public void addDisLikeToPost(@PathVariable Long postId){
        likeService.addDisLikeToPost(postId);
    }


    @PostMapping("/{postId}/comment/{commentId}/like")
    @ResponseStatus(HttpStatus.CREATED)
    public void addLikeToComment(@PathVariable Long postId, @PathVariable Long commentId){
        likeService.addLikeToComment(postId, commentId);
    }

    @PostMapping("/{postId}/comment/{commentId}/disLike")
    @ResponseStatus(HttpStatus.CREATED)
    public void addDisLikeToComment(@PathVariable Long postId, @PathVariable Long commentId){
        likeService.addDisLikeToComment(postId, commentId);
    }

}
