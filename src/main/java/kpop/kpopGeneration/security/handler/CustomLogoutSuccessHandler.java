package kpop.kpopGeneration.security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        RequestCache requestCache = new HttpSessionRequestCache();
        RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            String redirectUrl = savedRequest.getRedirectUrl();
            redirectStrategy.sendRedirect(request, response, redirectUrl);
            return;
        }else{
            redirectStrategy.sendRedirect(request, response, request.getHeader("Referer"));
            return;
        }
    }
}
