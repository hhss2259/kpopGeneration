package kpop.kpopGeneration.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Member extends JpaBaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    Long id;

    String username;
    String password;
    String nickName;
    Long postCnt;
    Long commentCnt;

    String profileImage;

    String email;

    public Member(String username, String password, String nickname){
        this.username = username;
        this.password = password;
        this.nickName = nickname;
        this.email = null;
        this.postCnt = 0L;
        this.commentCnt = 0L;
        this.profileImage = null;
    }
    public Member(String username, String password, String nickname, String email){
        this.username = username;
        this.password = password;
        this.nickName = nickname;
        this.email = email;
        this.postCnt = 0L;
        this.commentCnt = 0L;
        this.profileImage = null;
    }


    public void increasePostCnt() {
        this.postCnt++;
    }
    public void increaseCommentCnt(){ this.commentCnt++;}

    public void updateNickname(String nickname) {
        this.nickName = nickname;
    }

    public void decreasePostCnt() {
        this.postCnt--;
    }

    public void decreaseCommentCnt() { this.commentCnt--;
    }
}
