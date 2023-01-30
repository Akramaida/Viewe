package com.project.viewe.model;

import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String postName;
    private String title;
    @ElementCollection
    private List<String> tags = new ArrayList<>();
    private String description;
    private String username;
    private String videoUrl;
    private String videoThumbnailUrl;
    private String photoUrl;
    private PostStatus postStatus;
    @ElementCollection
    private List<String> likeUsername = new ArrayList<>();
    private Long likeCount = 0L;
    @ElementCollection
    private List<String> disLikeUsername = new ArrayList<>();
    private Long disLikeCount = 0L;
    private Long viewCount = 0L;
    @OneToMany
    private List<Comment> commentList= new ArrayList<>();
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    public void increaseViewCount(){
        viewCount++;
    }
    public void increaseLikeCount(){ likeCount++; }
    public void decreaseLikeCount(){ likeCount--; }
    public void increaseDisLikeCount(){ disLikeCount++; }
    public void decreaseDisLikeCount(){ disLikeCount--; }
    public void addUsernameToLikePosts(String username){ likeUsername.add(username);}
    public void addUsernameToDisLikePosts(String username){ disLikeUsername.add(username);}
    public void removeUsernameFromLikePosts(String username){ likeUsername.remove(username);}
    public void removeUsernameFromDisLikedPosts(String username){ disLikeUsername.remove(username);}

}
