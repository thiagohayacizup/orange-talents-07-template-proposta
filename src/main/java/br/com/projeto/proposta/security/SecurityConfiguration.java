package br.com.projeto.proposta.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;

@EnableWebSecurity
@Configuration
@Profile("!test")
class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests(expressionInterceptUrlRegistry ->
                expressionInterceptUrlRegistry
                        .antMatchers(HttpMethod.GET, "/proposta/**")
                        .hasAuthority("SCOPE_proposta-escopo")
                        .antMatchers(HttpMethod.POST, "/proposta/**")
                        .hasAuthority("SCOPE_proposta-escopo")
                        .antMatchers(HttpMethod.POST, "/biometria/cartao/**")
                        .hasAuthority("SCOPE_proposta-escopo")
                        .anyRequest()
                        .authenticated()
        ).oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    }

}
