package kpop.kpopGeneration.security.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class ResourceRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "url")
    private Resource resource;

    @ManyToOne(fetch = FetchType.LAZY)
    private  Role role;

    private Integer orderPriority;

    public ResourceRole(Resource resource, Role role) {
        this.resource = resource;
        this.role = role;
        this.orderPriority = 100;
    }
}
