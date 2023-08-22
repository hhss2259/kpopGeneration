package kpop.kpopGeneration.entity;


import com.sun.istack.NotNull;
import kpop.kpopGeneration.dto.Category;
import kpop.kpopGeneration.dto.PostSaveDto;
import kpop.kpopGeneration.dto.PostSaveViewDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Post extends JpaBaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    Long id;

    @Column(length = 2000)
    String title;


    @Column(length = 8000)
    String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    Member member;

    LocalDateTime deletedTime;
    Boolean deletedTrue;
    Long views;

    Long likes;

    Long commentCnt;

    @Enumerated(EnumType.STRING)
    Category category;


    public Post(String title, String body, Member member, Category category) {
        this.title = title;
        this.body = body;
        this.member = member;
        this.views = 0L;
        this.likes = 0L;
        this.deletedTime  = null;
        this.category = category;
        this.deletedTrue = false;
        this.commentCnt = 0L;
    }

    public void increaseCommentCnt(){
        this.commentCnt++;
    }

    public void updatePost(PostSaveDto postSaveDto) {
        this.title = postSaveDto.getTitle();
        this.body = postSaveDto.getBody();
        this.category = postSaveDto.getCategory();
    }

    public void deletePost() {
        this.deletedTime = LocalDateTime.now();
        this.deletedTrue = true;
    }

    public void increaseViews() {
        this.views++;
    }

    public void increaseLikes() {
        this.likes++;
    }

    public void decreaseLikes() {
        this.likes--;
    }
}
