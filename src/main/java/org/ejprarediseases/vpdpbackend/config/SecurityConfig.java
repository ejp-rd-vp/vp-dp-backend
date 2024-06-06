package org.ejprarediseases.vpdpbackend.config;

import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.proc.DefaultJOSEObjectTypeVerifier;
import io.micrometer.common.lang.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;


@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    @Value("${JWK_SET_URI}")
    private String jwtSetUri;

    @Bean
    @NonNull
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(jwtSetUri)
                .jwtProcessorCustomizer(
                        customizer -> {
                            customizer.setJWSTypeVerifier(
                                    new DefaultJOSEObjectTypeVerifier<>(new JOSEObjectType("at+jwt")));
                        })
                .build();
    }

    @Bean
    public SecurityFilterChain apiFilterChain(
            HttpSecurity http,
            ServerProperties serverProperties) throws Exception {

        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(jwtDecoder())
                        )
                )
                .cors(Customizer.withDefaults())
                .anonymous(Customizer.withDefaults())
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .requiresChannel(channel -> {
                    if (serverProperties.getSsl() != null && serverProperties.getSsl().isEnabled()) {
                        channel.anyRequest().requiresSecure();
                    }
                });
        return http.build();
    }

}