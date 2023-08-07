package kpop.kpopGeneration.security.entity.repository;

import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.security.entity.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRoleRepositoryCustom {
    Optional<List<MemberRole>> findAllByMemberFetch(Member member);

    void deleteAllByMember(Member member);
}
