package com.project.viewe.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @NotEmpty
    private String text;
    private String username;
    @OneToMany
    private List<Comment> subComment = new ArrayList<>();
    @ManyToOne
    private User user;
    @ManyToOne
    private Post post;
    @ElementCollection
    private List<String> visualComment = new ArrayList<>();
    @ElementCollection
    private List<String> likeUsername = new ArrayList<>();
    private Long likeCount = 0L;
    @ElementCollection
    private List<String> disLikeUsername = new ArrayList<>();
    private Long disLikeCount = 0L;

    public void increaseLikeCount() {
        likeCount++;
    }

    public void decreaseLikeCount() {
        likeCount--;
    }

    public void increaseDisLikeCount() {
        disLikeCount++;
    }

    public void decreaseDisLikeCount() {
        disLikeCount--;
    }

    public void addUsernameToLikeComments(String username) {
        likeUsername.add(username);
    }

    public void addUsernameToDisLikeComments(String username) {
        disLikeUsername.add(username);
    }

    public void removeUsernameFromLikeComments(String username) {
        likeUsername.remove(username);
    }

    public void removeUsernameFromDisLikedComments(String username) {
        disLikeUsername.remove(username);
    }
}
