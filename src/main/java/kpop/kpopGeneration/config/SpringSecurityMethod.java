package kpop.kpopGeneration.config;

import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.security.oauth2.Oauth2Member;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * SpringSecurity를 사용할 때 기본적으로 필요한 메서드들을 등록
 */
public class SpringSecurityMethod {
    /**
     * 현재 Request가 로그인한 상태인지 아닌지 여부를 return
     */
    static public boolean checkLogin(){
        boolean result = false;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof Member || principal instanceof Oauth2Member){
            result = true;
        }
        return result;
    }

    /**
     * 현재 Request가 해당 object의 소유주인지 아닌지 판단
     */
    static public boolean checkAuthority(String username){
        boolean result = false;
        if(checkLogin() == false){
            return false;
        }
        Member member = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof Member){
            member = (Member) principal;
        }else if(principal instanceof Oauth2Member){
            member = ((Oauth2Member) principal).getMember();
        }

        if(member.getUsername().equals(username)){
            result = true;
        }
        return result;
    }

    /**
     * 현재 사용자의 username을 return
     */
    static public  String getUsername(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = null;
        if (principal instanceof Member) {
            member = (Member) principal;
        } else if (principal instanceof Oauth2Member) {
            Oauth2Member oauth2Member = (Oauth2Member) principal;
            member = oauth2Member.getMember();
        }
        return member.getUsername();
    }
}
