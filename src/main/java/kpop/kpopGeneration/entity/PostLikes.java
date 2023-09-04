package kpop.kpopGeneration.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class PostLikes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    Post post;

    public PostLikes(Member member, Post post){
        this.member = member;
        this.post = post;
    }


}
