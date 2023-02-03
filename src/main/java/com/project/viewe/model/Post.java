package com.project.viewe.model;

import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;
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
    private AtomicInteger likeCount = new AtomicInteger(0);
    @ElementCollection
    private List<String> disLikeUsername = new ArrayList<>();
    private AtomicInteger disLikeCount = new AtomicInteger(0);
    private AtomicInteger viewCount = new AtomicInteger(0);
    @OneToMany
    private List<Comment> commentList= new ArrayList<>();
    @ManyToOne
    private User user;
    public void increaseViewCount(){
        viewCount.incrementAndGet();
    }
    public void increaseLikeCount(){ likeCount.incrementAndGet(); }
    public void decreaseLikeCount(){ likeCount.decrementAndGet(); }
    public void increaseDisLikeCount(){ disLikeCount.incrementAndGet(); }
    public void decreaseDisLikeCount(){ disLikeCount.decrementAndGet(); }
    public void addUsernameToLikePosts(String username){ likeUsername.add(username);}
    public void addUsernameToDisLikePosts(String username){ disLikeUsername.add(username);}
    public void removeUsernameFromLikePosts(String username){ likeUsername.remove(username);}
    public void removeUsernameFromDisLikedPosts(String username){ disLikeUsername.remove(username);}

}
