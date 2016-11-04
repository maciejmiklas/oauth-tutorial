package org.ot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.stream.Collectors;


@Configuration
@EnableOAuth2Client
class OAuthClientConfiguration {

    @Autowired
    private Environment env;

    @Resource
    @Qualifier("accessTokenRequest")
    private AccessTokenRequest accessTokenRequest;

    @Bean
    @Scope("session")
    public OAuth2ProtectedResourceDetails googleResource() {
	AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();
	details.setId("google-oauth-client");
	details.setClientId(env.getProperty("google.client.id"));
	details.setClientSecret(env.getProperty("google.client.secret"));
	details.setAccessTokenUri(env.getProperty("google.accessTokenUri"));
	details.setUserAuthorizationUri(env.getProperty("google.userAuthorizationUri"));
	details.setTokenName(env.getProperty("google.authorization.code"));
	String commaSeparatedScopes = env.getProperty("google.auth.scope");
	details.setScope(Arrays.stream(commaSeparatedScopes.split(",")).collect(Collectors.toList()));
	details.setPreEstablishedRedirectUri(env.getProperty("google.preestablished.redirect.url"));
	details.setUseCurrentUri(false);
	details.setAuthenticationScheme(AuthenticationScheme.query);
	details.setClientAuthenticationScheme(AuthenticationScheme.form);
	return details;
    }

    @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
    public OAuth2RestTemplate googleRestTemplate() {
	return new OAuth2RestTemplate(googleResource(), new DefaultOAuth2ClientContext(accessTokenRequest));
    }
}
