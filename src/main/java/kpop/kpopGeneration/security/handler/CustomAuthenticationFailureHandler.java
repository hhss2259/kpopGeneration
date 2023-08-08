package kpop.kpopGeneration.security.handler;

import kpop.kpopGeneration.config.LoginInfo;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String errorMessage = LoginInfo.INVALID_USERNAME;
        if (exception instanceof BadCredentialsException) {
           errorMessage = LoginInfo.INVALID_PASSWORD;
        } else if (exception instanceof InsufficientAuthenticationException) {
            errorMessage = LoginInfo.INVALID_SECRET_KEY;
        }
        setDefaultFailureUrl("/loginError?error=true&errorMessage=" + errorMessage);
        super.onAuthenticationFailure(request, response, exception);
    }
}
