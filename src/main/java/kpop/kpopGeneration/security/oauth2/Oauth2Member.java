package kpop.kpopGeneration.security.oauth2;

import kpop.kpopGeneration.entity.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class Oauth2Member extends DefaultOAuth2User {

    private final Member member;

    public Oauth2Member(Member member, Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey) {
        super(authorities, attributes, nameAttributeKey);
        this.member = member;
    }
}
