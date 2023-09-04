package kpop.kpopGeneration.security.entity.repository;

import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.security.entity.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRoleRepository extends JpaRepository<MemberRole, Long>, MemberRoleRepositoryCustom {

    //    void  deleteAllByMember(Member member);
}
