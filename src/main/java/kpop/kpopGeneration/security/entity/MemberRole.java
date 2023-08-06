package kpop.kpopGeneration.security.entity;

import kpop.kpopGeneration.dto.RoleEnum;
import kpop.kpopGeneration.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class MemberRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_role")
    private  Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    public MemberRole(Member member) {
        this.member = member;
        this.role = new Role("ROLE_USER");
    }

    public void updateRole(String name) {
        this.role = new Role(name);
    }
}
