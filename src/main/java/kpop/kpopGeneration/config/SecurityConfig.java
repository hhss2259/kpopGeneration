package kpop.kpopGeneration.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //csrf 공격 방지
        http.csrf().disable();

        // JWT를 사용하므로 Session을 사용하지 않는다
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //모든 요청에 대해 허용 중
        http.authorizeRequests()
                .anyRequest().permitAll();
        return http.build();
    }

}
