package kpop.kpopGeneration.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class PostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    Post post;

    String src;
    Boolean thumbnail;


    public PostImage(Post post, String src){
        this.post = post;
        this.src = src;
        this.thumbnail = false;
    }

    public void changeThumbnail(Boolean thumbnail) {
        this.thumbnail = thumbnail;
    }
}
