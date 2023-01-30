package com.project.viewe.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Slf4j
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @NotBlank(message = "Username is required")
    private String username;
    private String firstName;
    private String lastName;
    private String status;
    @ElementCollection
    private List<String> profilePhotoUrl = new ArrayList<>();
    private String userDescription;
    @NotBlank(message = "Password is required")
    private String password;
    @Email
    @NotBlank(message = "Email is required")
    private String email;
    private boolean enable;
    private Instant created;
    @OneToMany
    private List<Comment> comments;
    @ElementCollection
    private List<Long> subscribedToUsers = new ArrayList<>();
    @ElementCollection
    private List<Long> subscribers = new ArrayList<>();
    @ElementCollection
    private List<Long> viewHistory = new ArrayList<>();
    @ElementCollection
    private List<Long> myLikedPosts = new ArrayList<>();
    @ElementCollection
    private List<Long> myDisLikedPosts = new ArrayList<>();
    @ElementCollection
    private List<Long> myLikedComments = new ArrayList<>();
    @ElementCollection
    private List<Long> myDisLikedComments = new ArrayList<>();
    @OneToMany(fetch = LAZY)
    private List<Post> posts = new ArrayList<>();

    public void addToViewHistory(Long postId){
        log.info("I am in user.addToViewHistory");
            viewHistory.add(postId);
    }
    public void addToLikePosts(Long postId){ myLikedPosts.add(postId);}
    public void addToDisLikePosts(Long postId){ myDisLikedPosts.add(postId);}
    public void removeFromLikePosts(Long postId){ myLikedPosts.remove(postId);}
    public void removeFromDisLikedPosts(Long postId){ myDisLikedPosts.remove(postId);}
    public void addToLikeComment(Long commentId){ myLikedComments.add(commentId);}
    public void addToDisLikeComment(Long commentId){ myDisLikedComments.add(commentId);}
    public void removeFromLikeComment(Long commentId){ myLikedComments.remove(commentId);}
    public void removeFromDisLikedComment(Long commentId){ myDisLikedComments.remove(commentId);}
    public void addToSubscribers(Long userId){ subscribers.add(userId);}
    public void addToSubscribeToUsers(Long userId){ subscribedToUsers.add(userId);}
    public void removeFromSubscribers(Long userId){ subscribers.remove(userId);}
    public void removeFromSubscribeToUsers(Long userId){ subscribedToUsers.remove(userId);}

}
