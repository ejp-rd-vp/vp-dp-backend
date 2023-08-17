package org.ejprarediseases.vpdpbackend.config;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    interface Jwt2AuthoritiesConverter extends Converter<Jwt, Collection<? extends GrantedAuthority>> {
    }


    @SuppressWarnings("unchecked")
    @Bean
    Jwt2AuthoritiesConverter authoritiesConverter() {
        // This is a converter for roles as embedded in the JWT by a Keycloak server
        // Roles are taken from both realm_access.roles & resource_access.{client}.roles
        return jwt -> {
            final var realmAccess = (Map<String, Object>) jwt.getClaims().getOrDefault("realm_access", Map.of());
            final var realmRoles = (Collection<String>) realmAccess.getOrDefault("roles", List.of());

            final var resourceAccess = (Map<String, Object>) jwt.getClaims().getOrDefault("resource_access", Map.of());
            // We assume here you have "spring-addons-confidential" and
            // "spring-addons-public" clients configured with "client roles" mapper in
            // Keycloak
            final var confidentialClientAccess = (Map<String, Object>) resourceAccess
                    .getOrDefault("spring-addons-confidential", Map.of());
            final var confidentialClientRoles = (Collection<String>) confidentialClientAccess.getOrDefault("roles",
                    List.of());
            final var publicClientAccess = (Map<String, Object>) resourceAccess.getOrDefault("spring-addons-public",
                    Map.of());
            final var publicClientRoles = (Collection<String>) publicClientAccess.getOrDefault("roles", List.of());

            return Stream
                    .concat(realmRoles.stream(),
                            Stream.concat(confidentialClientRoles.stream(), publicClientRoles.stream()))
                    .map(SimpleGrantedAuthority::new).toList();
        };
    }

    interface Jwt2AuthenticationConverter extends Converter<Jwt, AbstractAuthenticationToken> {
    }

    @Bean
    Jwt2AuthenticationConverter authenticationConverter(
            Converter<Jwt, Collection<? extends GrantedAuthority>> authoritiesConverter) {
        return jwt -> new JwtAuthenticationToken(jwt, authoritiesConverter.convert(jwt));
    }

    @Bean
    public SecurityFilterChain apiFilterChain(
            HttpSecurity http,
            ServerProperties serverProperties,
            Converter<Jwt, ? extends AbstractAuthenticationToken> authenticationConverter) throws Exception {

        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(authenticationConverter)
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