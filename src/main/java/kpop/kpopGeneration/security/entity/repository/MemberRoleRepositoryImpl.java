package kpop.kpopGeneration.security.entity.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.entity.QMember;
import kpop.kpopGeneration.security.entity.MemberRole;
import kpop.kpopGeneration.security.entity.QMemberRole;
import kpop.kpopGeneration.security.entity.QRole;
import kpop.kpopGeneration.security.entity.ResourceRole;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static kpop.kpopGeneration.entity.QMember.*;
import static kpop.kpopGeneration.security.entity.QMemberRole.*;
import static kpop.kpopGeneration.security.entity.QRole.role;

public class MemberRoleRepositoryImpl implements MemberRoleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MemberRoleRepositoryImpl(EntityManager entityManager){
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * 한 Member의 모든 MemberRole을 가지고 온다
     */
    @Override
    public Optional<List<MemberRole>> findAllByMemberFetch(Member member) {
        List<MemberRole> list = queryFactory
                .select(memberRole)
                .from(memberRole)
                .join(memberRole.member, QMember.member).fetchJoin()
                .join(memberRole.role, role).fetchJoin()
                .where(memberRole.member.eq(member))
                .fetch();
        return Optional.ofNullable(list);
    }

    @Override
    public void deleteAllByMember(Member member) {
        long execute = queryFactory
                .delete(memberRole)
                .where(memberRole.member.eq(member))
                .execute();
    }
}
