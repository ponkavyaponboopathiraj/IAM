package IAM.IAM.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class AuthorizationServerSecurityConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .oauth2AuthorizationServer(authorizationServer ->
                authorizationServer
                    .oidc(Customizer.withDefaults())
            )
            .authorizeHttpRequests(authorize ->
                authorize
                    .anyRequest()
                    .authenticated()
            )
            .exceptionHandling(exceptions ->
                exceptions
                    .authenticationEntryPoint(
                        new org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint(
                            "/login"
                        )
                    )
            );

        return http.build();
    }
}