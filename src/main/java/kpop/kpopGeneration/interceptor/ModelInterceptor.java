package kpop.kpopGeneration.interceptor;

import kpop.kpopGeneration.dto.MemberViewDto;
import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.security.oauth2.Oauth2Member;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class ModelInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

            if(modelAndView != null ){
                Map<String, Object> model = modelAndView.getModel();

                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                MemberViewDto memberViewDto = new MemberViewDto(null , null, false);
                if(principal instanceof Member || principal instanceof Oauth2Member){
                    Member member = null;

                    if(principal instanceof  Member){
                        member = (Member) principal;
                    }else if(principal instanceof  Oauth2Member){
                        Oauth2Member principal1 = (Oauth2Member) principal;
                        member = principal1.getMember();
                    }
                    memberViewDto = new MemberViewDto(member.getId(), member.getNickName(), true);
                }
                model.put("member" , memberViewDto);
            }

            HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);

    }
}
