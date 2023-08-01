package kpop.kpopGeneration.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Parent;

import javax.persistence.*;

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
    Post post;

    boolean isCommentForComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_comment_id")
    Comment comment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    Member member;

    Long likes;

    public Comment(Post post, Comment comment,Member member) {
        this.post = post;
        this.comment = comment;
        if(comment == null){
            this.isCommentForComment = false;
        }else{
            this.isCommentForComment = true;
        }
        this.member = member;
    }

}
