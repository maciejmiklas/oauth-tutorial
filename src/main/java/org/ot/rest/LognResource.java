package org.ot.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LognResource {

    @Autowired()
    @Qualifier("googleRestTemplate")
    private OAuth2RestTemplate oauthRest;

    @RequestMapping(method = RequestMethod.GET, path="/googleLogin")
    public String googleLogin() {
	return oauthRest.getAccessToken().getValue();
    }

}
