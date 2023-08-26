package kpop.kpopGeneration.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kpop.kpopGeneration.dto.Category;
import kpop.kpopGeneration.entity.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static kpop.kpopGeneration.entity.QMember.member;
import static kpop.kpopGeneration.entity.QPost.post;

@Slf4j
@Repository
public class SearchingRepositoryImpl {
    private final JPAQueryFactory queryFactory;

    public SearchingRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Page<Post> searchByContent(Category category, String content, Pageable pageable) {

        List<Post> list = queryFactory
                .select(post)
                .from(post)
                .join(post.member, member).fetchJoin()
                .where(checkContent(content), checkCategory(category), post.deletedTrue.eq(false))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.id.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post)
                .where(checkContent(content), checkCategory(category), post.deletedTrue.eq(false));

        return PageableExecutionUtils.getPage(list, pageable, countQuery::fetchOne);
    }
    private BooleanExpression checkContent(String content) {
        String[] words = content.split("[`~!@#$%^&*()_|+\\-=?;:'\",.<>\\{\\}\\[\\]\\\\\\/ ]");
        BooleanExpression contains = post.title.concat(post.body).contains(words[0]);

        if (words.length == 1) { // 길이가 1 => 검색하는 단어가 한 개
            return contains;
        } else { // 길이가 2 이상 => 검색하는 단어가 여러 개
            for (int i = 1; i<words.length ; i++) {
                contains = contains.and(post.title.concat(post.body).contains(words[i]));
            }
        }
        return contains;
    }

    private BooleanExpression checkCategory(Category category) {
        if(category == Category.ALL){
            return  post.category.ne(Category.NEWS);
        }else{
            return  post.category.eq(category).and(post.category.ne(Category.NEWS));
        }
    }


    public Page<Post> searchByNickname(Category category,  String content, Pageable pageable) {

        List<Post> list = queryFactory
                .select(post)
                .from(post)
                .select(post)
                .from(post)
                .join(post.member, member).fetchJoin()
                .where(checkNickname(content), checkCategory(category), post.deletedTrue.eq(false))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.id.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post)
                .where(checkNickname(content), checkCategory(category), post.deletedTrue.eq(false));

        return PageableExecutionUtils.getPage(list, pageable, countQuery::fetchOne);
    }

    private BooleanExpression checkNickname(String content) {

//        String[] words = content.split(" ");
        String[] words = content.split("[`~!@#$%^&*()_|+\\-=?;:'\",.<>\\{\\}\\[\\]\\\\\\/ ]");

        BooleanExpression contains = member.nickName.contains(words[0]);

        if (words.length == 1) { // 길이가 1 => 검색하는 단어가 한 개
            return contains;
        } else { // 길이가 2 이상 => 검색하는 단어가 여러 개
            for (int i = 1; i<words.length ; i++) {
                contains = contains.and(member.nickName.contains(words[i]));
            }
        }
        return contains;
    }
}
