package com.project.viewe.service;

import com.project.viewe.dto.PostDtoResponse;
import com.project.viewe.exception.ProjectException;
import com.project.viewe.model.*;
import com.project.viewe.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final PostService postService;
    private final PostRepo postRepo;
    private final UserService userService;
    private final UserRepo userRepo;
    private final CommentRepo commentRepo;

    public List<PostDtoResponse> getAllLikedPosts() {
        var user = userService.getCurrentUser();
        var postId = user.getMyLikedPosts();
        return postId.stream().map(id -> postService.mapToPostDtoResponse(postService.findPostById(id))).toList();
    }

    public void addLikeToPost(Long postId) {
        var post = postService.findPostById(postId);
        var user = userService.getCurrentUser();
        var username = user.getUsername();

        if(userService.ifDisLikedPost(postId, user)){
            addLikeAndRemoveDisLike(post, postId, username, user);
        }else if (userService.ifLikedPost(postId, user)){
            justRemoveLike(post, postId, username, user);
        }else{
            justAddLike(post, postId, username, user);
        }
        saveUserAndPostRepos(user, post);
    }

    private void addLikeAndRemoveDisLike(Post post, Long postId, String username, User user) {
        post.increaseLikeCount();
        post.decreaseDisLikeCount();
        post.addUsernameToLikePosts(username);
        post.removeUsernameFromDisLikedPosts(username);
        user.addToLikePosts(postId);
        user.removeFromDisLikedPosts(postId);
    }

    private void justRemoveLike(Post post, Long postId, String username, User user) {
        post.decreaseLikeCount();
        post.removeUsernameFromLikePosts(username);
        user.removeFromLikePosts(postId);
    }

    private void justAddLike(Post post, Long postId, String username, User user) {
        post.increaseLikeCount();
        post.addUsernameToLikePosts(username);
        user.addToLikePosts(postId);
    }

    public void addDisLikeToPost(Long postId){
        var post = postService.findPostById(postId);
        var user = userService.getCurrentUser();
        var username = user.getUsername();
        if(userService.ifLikedPost(postId, user)){
            addDisLikeAndRemoveLike(post, postId, username, user);
        }else if (userService.ifDisLikedPost(postId, user)){
            justRemoveDisLike(post, postId, username, user);
        }else{
            justAddDisLike(post, postId, username, user);
        }
        saveUserAndPostRepos(user, post);
    }

    private void addDisLikeAndRemoveLike(Post post, Long postId, String username, User user) {
        post.increaseDisLikeCount();
        post.decreaseLikeCount();
        post.removeUsernameFromLikePosts(username);
        post.addUsernameToDisLikePosts(username);
        user.addToDisLikePosts(postId);
        user.removeFromLikePosts(postId);
    }

    private void justAddDisLike(Post post, Long postId, String username, User user) {
        post.increaseDisLikeCount();
        post.addUsernameToDisLikePosts(username);
        user.addToDisLikePosts(postId);
    }

    private void justRemoveDisLike(Post post, Long postId, String username, User user) {
        post.decreaseDisLikeCount();
        post.removeUsernameFromDisLikedPosts(username);
        user.removeFromDisLikedPosts(postId);
    }

    public void addLikeToComment(Long postId, Long commentId) {
        var post = postService.findPostById(postId);
        var user = userService.getCurrentUser();
        var username = user.getUsername();
        var comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ProjectException("Cannot find comment by id: " + commentId));
        if(userService.ifLikedComment(user, comment)){
            justRemoveLikeToComment(comment, commentId, username, user);
        }else if(userService.ifDisLikedComment(user, comment)){
            addLikeAndRemoveDisLikeToComment(comment, commentId, username, user);
        }else{
            justAddLikeToComment(comment, commentId, username, user);
        }
        postRepo.save(post);
        saveUserAndCommentRepos(user, comment);
    }

    private void justAddLikeToComment(Comment comment, Long commentId, String username, User user) {
        comment.increaseLikeCount();
        comment.addUsernameToLikeComments(username);
        user.addToLikeComment(commentId);
    }

    private void addLikeAndRemoveDisLikeToComment(Comment comment, Long commentId, String username, User user) {
        comment.increaseLikeCount();
        comment.addUsernameToLikeComments(username);
        comment.decreaseDisLikeCount();
        comment.removeUsernameFromDisLikedComments(username);
        user.addToLikeComment(commentId);
        user.removeFromDisLikedComment(commentId);
    }

    private void justRemoveLikeToComment(Comment comment, Long commentId, String username, User user) {
        comment.decreaseLikeCount();
        comment.removeUsernameFromLikeComments(username);
        user.removeFromLikeComment(commentId);
    }

    public void addDisLikeToComment(Long postId, Long commentId) {
        var post = postService.findPostById(postId);
        var user = userService.getCurrentUser();
        var username = user.getUsername();
        var comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ProjectException("Cannot find comment by id: " + commentId));
        if(userService.ifLikedComment(user, comment)){
            addDisLikeAndRemoveLikeToComment(comment, commentId, username, user);
        }else if(userService.ifDisLikedComment(user, comment)){
            justRemoveDisLikeToComment(comment, commentId, username, user);
        }else{
            justAddDisLikeToComment(comment, commentId, username, user);
        }
        postRepo.save(post);
        saveUserAndCommentRepos(user, comment);
    }

    private void justAddDisLikeToComment(Comment comment, Long commentId, String username, User user) {
        comment.increaseDisLikeCount();
        comment.addUsernameToDisLikeComments(username);
        user.addToDisLikeComment(commentId);
    }

    private void justRemoveDisLikeToComment(Comment comment, Long commentId, String username, User user) {
        comment.decreaseDisLikeCount();
        comment.removeUsernameFromDisLikedComments(username);
        user.removeFromDisLikedComment(commentId);
    }

    private void addDisLikeAndRemoveLikeToComment(Comment comment, Long commentId, String username, User user) {
        comment.decreaseLikeCount();
        comment.removeUsernameFromLikeComments(username);
        comment.increaseDisLikeCount();
        comment.addUsernameToDisLikeComments(username);
        user.removeFromLikeComment(commentId);
        user.addToDisLikeComment(commentId);
    }

    public void saveUserAndPostRepos(User user, Post post){
        userRepo.save(user);
        postRepo.save(post);
    }

    public void saveUserAndCommentRepos(User user, Comment comment){
        userRepo.save(user);
        commentRepo.save(comment);
    }
}
