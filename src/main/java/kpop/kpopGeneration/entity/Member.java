package kpop.kpopGeneration.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    int id;

    String username;
    String password;
    String nickName;
    int postCnt;
    int commentCnt;

    String profileImage;

    public Member(String username, String password, String nickname){
        this.username = username;
        this.password = password;
        this.nickName = nickname;
    }

}
