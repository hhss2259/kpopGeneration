package kpop.kpopGeneration.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Member extends JpaBaseTimeEntity {

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

    public Member(String username, String password, String nickname){
        this.username = username;
        this.password = password;
        this.nickName = nickname;
        this.postCnt = 0L;
        this.commentCnt = 0L;
        this.profileImage = null;
    }

    public void increasePostCnt() {
        this.postCnt++;
    }
}
