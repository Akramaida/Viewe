package com.project.viewe.service;

import com.project.viewe.dto.UserProfileRequestDto;
import com.project.viewe.dto.UserProfileResponseDto;
import com.project.viewe.exception.ProjectException;
import com.project.viewe.model.Comment;
import com.project.viewe.model.Post;
import com.project.viewe.model.User;
import com.project.viewe.repo.CommentRepo;
import com.project.viewe.repo.PostRepo;
import com.project.viewe.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepo userRepo;
    private final UploadService uploadService;
    private final PostRepo postRepo;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final CommentRepo commentRepo;
    private final ModelMapper modelMapper;

    public void addToViewHistory(Long postId) {
        var user = getCurrentUser();
        user.addToViewHistory(postId);
        userRepo.save(user);
    }

    public User getCurrentUser() {
        Jwt principal = (Jwt) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        log.info("Username is: " + principal.getSubject());
        return findByUsername(principal.getSubject());
    }

    public User findByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new ProjectException("Cannot find user by username: " + username));
    }

    public boolean ifSubscribeToUser(User user, User currentUser) {
        var getUsers = user.getSubscribers();
        return getUsers.stream().anyMatch(findUser -> findUser.equals(currentUser.getId()));
    }

    public boolean ifLikedPost(Long postId, User user) {
        return user.getMyLikedPosts().stream().anyMatch(likedPost -> likedPost.equals(postId));
    }

    public boolean ifDisLikedPost(Long postId, User user) {
        return user.getMyDisLikedPosts().stream().anyMatch(dislikedPost -> dislikedPost.equals(postId));
    }

    public boolean ifLikedComment(User user, Comment comment) {
        return user.getMyLikedComments().stream().anyMatch(likeComment -> likeComment.equals(comment.getId()));
    }

    public boolean ifDisLikedComment(User user, Comment comment) {
        return user.getMyDisLikedComments().stream().anyMatch(disLikeComment -> disLikeComment.equals(comment.getId()));
    }

    public void editPhotoProfile(MultipartFile multipartFile) {
        var user = getCurrentUser();
        String fileName = multipartFile.getOriginalFilename();
        String photoUrl = uploadService.uploadProfileFile(fileName, multipartFile);
        user.getProfilePhotoUrl().add(photoUrl);
        userRepo.save(user);
    }

    public void editProfile(UserProfileRequestDto userProfileRequestDto) {
        var user = getCurrentUser();
        if(userProfileRequestDto.getNewPassword() != null && userProfileRequestDto.getOldPass() != null) {
            var isAuthenticated = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),
                            userProfileRequestDto.getOldPass())).isAuthenticated();
            if (isAuthenticated && userProfileRequestDto.getNewPassword() != null) {
                var password = passwordEncoder.encode(userProfileRequestDto.getNewPassword());
                user.setPassword(password);
            } else {
                throw new ProjectException("Password is incorrect");
            }
        }
        mapToUserProfileWithoutFile(user, userProfileRequestDto);
    }


    private void mapToUserProfileWithoutFile(User user, UserProfileRequestDto userProfileRequestDto) {
        if(userRepo.existsByUsername(userProfileRequestDto.getUsername())) {
            log.error("User with username - {} has existed", userProfileRequestDto.getUsername());
            throw new ProjectException("User has existed already");
        }else{
            user.setUsername(userProfileRequestDto.getUsername());
        }
        user.setEmail(userProfileRequestDto.getEmail());
        user.setUserDescription(userProfileRequestDto.getUserDescription());
        user.setFirstName(userProfileRequestDto.getFirstName());
        user.setLastName(userProfileRequestDto.getLastName());
        user.setStatus(userProfileRequestDto.getStatus());
        userRepo.save(user);
    }

    public UserProfileResponseDto getMyProfile() {
        var user = getCurrentUser();
        return mapToUserGetProfile(user);
    }

    public UserProfileResponseDto getUserProfile(String username) {
        var user = findByUsername(username);
        return mapToUserGetProfile(user);
    }

    public List<String> getAllUsersToLikeComment(Long commentId) {
        var comment = commentRepo.findById(commentId)
                .orElseThrow(()-> new ProjectException("Cannot find comment by id " + commentId));
        List<String> username = new ArrayList<>();
        for(int i = 0; i < comment.getLikeUsername().size(); i++){
            username.add(i, comment.getLikeUsername().get(i));
        }
        return username;
    }

    private UserProfileResponseDto mapToUserGetProfile(User user) {
        return modelMapper.map(user, UserProfileResponseDto.class);
    }

    public List<String> getAllUsersToLikePost(Long postId) {
        var post = findPostById(postId);
        List<String> username = new ArrayList<>();
        for(int i = 0; i < post.getLikeUsername().size(); i++){
            username.add(i, post.getLikeUsername().get(i));
        }
        return username;
    }

    public Post findPostById(Long postId) {
        return postRepo.findById(postId).orElseThrow(() -> new ProjectException("cannot find post by id:" + postId));
    }
}
