package IAM.IAM.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.util.UUID;

@Configuration
public class OAuth2ClientConfig {

    @Bean
    public RegisteredClient employeePortalClient(
            RegisteredClientRepository registeredClientRepository,
            PasswordEncoder passwordEncoder) {

        String clientId = "employee-portal";

        RegisteredClient existingClient =
                registeredClientRepository.findByClientId(clientId);

        if (existingClient != null) {
            return existingClient;
        }

        RegisteredClient client = RegisteredClient.withId(
                    UUID.randomUUID().toString()
                )
                .clientId(clientId)
                .clientSecret(
                    passwordEncoder.encode("EmployeePortal@123")
                )
                .clientName("Employee Management Portal")

                .clientAuthenticationMethod(
                    ClientAuthenticationMethod.CLIENT_SECRET_BASIC
                )

                .authorizationGrantType(
                    AuthorizationGrantType.AUTHORIZATION_CODE
                )

                .authorizationGrantType(
                    AuthorizationGrantType.REFRESH_TOKEN
                )

                .redirectUri(
                    "http://localhost:3000/login/oauth2/code/iam"
                )

                .postLogoutRedirectUri(
                    "http://localhost:3000/"
                )

                .scope("openid")
                .scope("profile")
                .scope("email")

                .build();

        registeredClientRepository.save(client);

        return client;
    }
}