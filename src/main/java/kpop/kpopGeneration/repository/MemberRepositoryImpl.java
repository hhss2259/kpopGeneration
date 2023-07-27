package kpop.kpopGeneration.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kpop.kpopGeneration.entity.QMember;

import javax.persistence.EntityManager;

import static kpop.kpopGeneration.entity.QMember.*;

public class MemberRepositoryImpl implements MemberRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    MemberRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public int findCntByUsername(String username) {
        int size = queryFactory
                .selectFrom(member)
                .where(member.username.eq(username))
                .fetch().size();

        return size;
    }

    @Override
    public int findCntByNickname(String nickname) {
        int size = queryFactory
                .selectFrom(member)
                .where(member.nickName.eq(nickname))
                .fetch().size();

        return size;
    }

    @Override
    public void deleteAllMember() {
        queryFactory
                .delete(member)
                .execute();
    }
}
