package com.project.viewe.controller;

import com.project.viewe.dto.PostDtoResponse;
import com.project.viewe.dto.UserProfileRequestDto;
import com.project.viewe.dto.UserProfileResponseDto;
import com.project.viewe.service.LikeService;
import com.project.viewe.service.PostService;
import com.project.viewe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/user/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final LikeService likeService;
    private final PostService postService;

    @PostMapping("photo-profile")
    @ResponseStatus(HttpStatus.CREATED)
    public void editPhotoProfile(@RequestParam("file") MultipartFile file) {
        userService.editPhotoProfile(file);
    }

    @PostMapping("edit-profile")
    @ResponseStatus(HttpStatus.CREATED)
    public void editProfile(@RequestBody UserProfileRequestDto userProfileRequestDto) {
        userService.editProfile(userProfileRequestDto);
    }

    @GetMapping("profile")
    @ResponseStatus(HttpStatus.OK)
    public UserProfileResponseDto getMyProfile() {
        return userService.getMyProfile();
    }

    @GetMapping("{username}/profile")
    @ResponseStatus(HttpStatus.OK)
    public UserProfileResponseDto getUserProfile(@PathVariable String username) {
        return userService.getUserProfile(username);
    }

    @GetMapping("history")
    @ResponseStatus(OK)
    public List<PostDtoResponse> getAllViewHistory() {
        return postService.getAllViewHistory();
    }

    @GetMapping("like")
    @ResponseStatus(HttpStatus.OK)
    public List<PostDtoResponse> getAllLikedPosts() {
        return likeService.getAllLikedPosts();
    }

    @GetMapping("comment/{commentId}/like")
    @ResponseStatus(OK)
    public List<String> getAllUsersToLikeComment(@PathVariable Long commentId) {
        return userService.getAllUsersToLikeComment(commentId);
    }

    @GetMapping("post/{postId}/like")
    @ResponseStatus(OK)
    public List<String> getAllUsersToLikePost(@PathVariable Long postId) {
        return userService.getAllUsersToLikePost(postId);
    }
}
