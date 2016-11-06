package org.ot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.annotation.Resource;
import javax.servlet.Filter;


@Configuration
@EnableOAuth2Client
class OAuthConfiguration extends WebSecurityConfigurerAdapter {


    @Resource
    @Qualifier("accessTokenRequest")
    private AccessTokenRequest accessTokenRequest;

    @Autowired
    private OAuth2ClientContext oauth2ClientContext;


    @Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
	FilterRegistrationBean registration = new FilterRegistrationBean();
	registration.setFilter(filter);
	registration.setOrder(-100);
	return registration;
    }

    private Filter ssoFilter() {
	OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(
		"/*");
	OAuth2RestTemplate facebookTemplate = new OAuth2RestTemplate(oauth2GoogleClient(), oauth2ClientContext);
	filter.setRestTemplate(facebookTemplate);
	filter.setTokenServices(
		new UserInfoTokenServices(oauth2GoogleResource().getUserInfoUri(), oauth2GoogleClient().getClientId()));
	return filter;
    }

    @Bean
    @ConfigurationProperties("oauth2.google.client")
    public AuthorizationCodeResourceDetails oauth2GoogleClient() {
	return new AuthorizationCodeResourceDetails();
    }

    @Bean
    @ConfigurationProperties("oauth2.google.resource")
    public ResourceServerProperties oauth2GoogleResource() {
	return new ResourceServerProperties();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
	http.antMatcher("/**").authorizeRequests().antMatchers("/", "/login**", "/webjars/**").permitAll().anyRequest()
		.authenticated().and().exceptionHandling()
		.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/")).and().logout()
		.logoutSuccessUrl("/").permitAll().and().csrf()
		.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
		.addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
    }

    @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public OAuth2RestTemplate googleRestTemplate() {
	return new OAuth2RestTemplate(oauth2GoogleClient(), new DefaultOAuth2ClientContext(accessTokenRequest));
    }
}
