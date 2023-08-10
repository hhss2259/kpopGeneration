package kpop.kpopGeneration.security.oauth2;

import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.exception.DuplicateException;
import kpop.kpopGeneration.repository.MemberRepository;
import kpop.kpopGeneration.security.entity.MemberRole;
import kpop.kpopGeneration.security.entity.Role;
import kpop.kpopGeneration.security.entity.repository.MemberRoleRepository;
import kpop.kpopGeneration.security.entity.repository.RoleRepository;
import kpop.kpopGeneration.security.entity.repository.RoleRepositoryCustom;
import kpop.kpopGeneration.security.exception.NotExistedRoleException;
import kpop.kpopGeneration.security.oauth2.provider.NaverUserInfo;
import kpop.kpopGeneration.security.oauth2.provider.Oauth2UserInfo;
import kpop.kpopGeneration.security.service.MemberContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Transactional
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberRoleRepository memberRoleRepository;

    @Autowired
    private RoleRepository roleRepository;

    /**
    *  SNS 서버에 접근해 해당 유저의 정보를 받아와주는 역할을 한다
    */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Oauth2UserInfo oAuth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
//            oAuth2UserInfo = new GoogleUseInfo((Map)oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            oAuth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));
        } else{
            log.info("error");
        }

        // SNS 서버로부터 가지고 온 유저의 정보
        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        String username = provider+"_"+ providerId;
        String password = passwordEncoder.encode(provider);


        Member member = null;
        List<GrantedAuthority> roles = new ArrayList<>();
        Optional<Member> byUsername = memberRepository.findByUsername(username);
        if(byUsername.isPresent()){ //이미 회원가입된 user입니다.
            member = byUsername.get();
            List<MemberRole> memberRoles = memberRoleRepository.findAllByMemberFetch(member).get();
            memberRoles.forEach( memberRole -> {
                roles.add(new SimpleGrantedAuthority(memberRole.getRole().getName()));
            });
        }else{
            Member newMember = new Member(username, password, null);
            member = memberRepository.save(newMember);
            Role basicRole = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new NotExistedRoleException());
            memberRoleRepository.save(new MemberRole(member, basicRole));

            roles.add(new SimpleGrantedAuthority(basicRole.getName()));
        }

        return new Oauth2Member(member,
                roles,
                oAuth2User.getAttributes(),
                userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName() );
    }
}
