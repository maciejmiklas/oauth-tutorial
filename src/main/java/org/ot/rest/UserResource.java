package org.ot.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.ot.domain.GoogleUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserResource {

    @Autowired()
    @Qualifier("googleRestTemplate")
    private OAuth2RestTemplate oauthRest;

    @RequestMapping(method = RequestMethod.GET, path="/rest/user/info")
    public GoogleUserInfo getInfo() throws JsonProcessingException {
	String url = "https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + oauthRest.getAccessToken();
	ResponseEntity<GoogleUserInfo> forEntity = oauthRest.getForEntity(url, GoogleUserInfo.class);
	return forEntity.getBody();
    }

    @RequestMapping("/rest/user/principal")
    public Principal getPrincipal(Principal principal) {
	return principal;
    }

}
