package kpop.kpopGeneration.security.entity.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kpop.kpopGeneration.security.entity.QRole;
import kpop.kpopGeneration.security.entity.Role;

import javax.persistence.EntityManager;
import java.awt.geom.QuadCurve2D;
import java.util.List;
import java.util.Optional;

import static kpop.kpopGeneration.security.entity.QRole.*;

public class RoleRepositoryImpl implements RoleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public RoleRepositoryImpl(EntityManager entityManager){
        this.queryFactory = new JPAQueryFactory(entityManager);
    }


    /**
     * 해당 name을 가진 모든 role 들을 반환한다
     */
    @Override
    public Optional<List<Role>> findAllByName(List<String> names) {
        List<Role> list = queryFactory
                .selectFrom(role)
                .where(getRolesByName(names))
                .fetch();
        return Optional.ofNullable(list);
    }

   private BooleanExpression getRolesByName(List<String> names){

       BooleanExpression expression = role.name.eq(names.get(0));

       for(int i = 1; i< names.size() ; i++){
           expression = expression.or(role.name.eq(names.get(i)));
       }
       return expression;
   }
}
