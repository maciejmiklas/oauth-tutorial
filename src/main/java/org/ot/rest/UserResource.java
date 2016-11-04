package org.ot.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.ot.domain.GoogleUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserResource {

    @Autowired
    private OAuth2RestOperations oauthRest;

    @RequestMapping(method = RequestMethod.GET, path="/user/info")
    public GoogleUserInfo findProfile() throws JsonProcessingException {
	String url = "https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + oauthRest.getAccessToken();
	ResponseEntity<GoogleUserInfo> forEntity = oauthRest.getForEntity(url, GoogleUserInfo.class);
	return forEntity.getBody();
    }

}
