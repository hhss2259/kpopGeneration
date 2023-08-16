package kpop.kpopGeneration.config;

import kpop.kpopGeneration.interceptor.ModelInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig  implements WebMvcConfigurer {
    private final ModelInterceptor modelInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(modelInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/**");
    }
}
