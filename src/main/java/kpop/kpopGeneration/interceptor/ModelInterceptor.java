package kpop.kpopGeneration.interceptor;

import kpop.kpopGeneration.dto.MemberViewDto;
import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.security.entity.MemberRole;
import kpop.kpopGeneration.security.entity.repository.MemberRoleRepository;
import kpop.kpopGeneration.security.oauth2.Oauth2Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ModelInterceptor implements HandlerInterceptor {

    private final MemberRoleRepository memberRoleRepository;

    /**
     * 현재 접근한 사용자의 정보들을 템플릿 엔진에게 일괄되게 전달하는 역할을 한다
     */
    @Override

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        if (modelAndView != null) {
            Map<String, Object> model = modelAndView.getModel();

            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            MemberViewDto memberViewDto = new MemberViewDto(null, null, false);

            //현재 로그인한 상태라면 새로운 MemberViewDto를 생성한다
            if (principal instanceof Member || principal instanceof Oauth2Member) {
                Member member = null;

                if (principal instanceof Member) {
                    member = (Member) principal;
                } else if (principal instanceof Oauth2Member) {
                    Oauth2Member principal1 = (Oauth2Member) principal;
                    member = principal1.getMember();
                }
                memberViewDto = new MemberViewDto(member.getId(), member.getNickName(), true);

                List<MemberRole> memberRoles = memberRoleRepository.findAllByMemberFetch(member).get();
                for (MemberRole memberRole : memberRoles) {
                    if (memberRole.getRole().getName().equals("ROLE_MANAGER")) {
                        memberViewDto.updateManagerRole(true);
                    }
                }

            }
            model.put("member", memberViewDto);
        }

        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);

    }
}
