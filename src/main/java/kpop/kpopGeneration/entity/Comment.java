package kpop.kpopGeneration.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Parent;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment extends JpaBaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    Long id;

    @Column(length = 1000)
    String textBody;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_post_id")
    Post parentPost;

    Boolean isCommentForComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_comment_id")
    Comment parentComment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    Member member;

    Long likes;

    LocalDateTime deletedTime;
    Boolean deletedTrue;
    Integer depth;
    Long orderNumber;

    public Comment(String textBody, Post parentPost, Comment parentComment , Member member) {
        this.textBody = textBody;
        this.parentPost = parentPost;
        this.parentComment = parentComment;
        if(parentComment == null){
            this.isCommentForComment = false;
            this.depth = 0 ;
            this.orderNumber = parentPost.getCommentCnt();
        }else{
            this.isCommentForComment = true;
            this.depth = parentComment.depth+1;
            this.orderNumber = parentComment.getOrderNumber();
        }
        this.member = member;
        this.deletedTime = null;
        this.deletedTrue = false;
        this.likes = 0L;
    }

    public void updateTextBody(String textBody) {
        this.textBody = textBody;
    }

    public void deleteComment() {
        this.deletedTrue = true;
        this.deletedTime = LocalDateTime.now();
    }

    public void increaseLikes() {
        this.likes++;
    }

    public void decreaseLikes() {
        this.likes--;
    }
}
