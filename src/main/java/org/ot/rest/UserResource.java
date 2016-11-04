package org.ot.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.ot.domain.GoogleProfile;
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

    @RequestMapping(method = RequestMethod.GET, path="/user/profile")
    public GoogleProfile findProfile() throws JsonProcessingException {
	String url = "https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + oauthRest.getAccessToken();
	ResponseEntity<GoogleProfile> forEntity = oauthRest.getForEntity(url, GoogleProfile.class);
	return forEntity.getBody();
    }

}
