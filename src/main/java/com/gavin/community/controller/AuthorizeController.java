package com.gavin.community.controller;

import com.gavin.community.dto.AccesssTokenDTO;
import com.gavin.community.dto.GithubUser;
import com.gavin.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Gavin on 2019/12/8.
 */
@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;
    @Value("${github.setClient_id}")
    private String clientId;

    @Value("${github.setClient_secret}")
    private String clientSecret;

    @Value("${github.redirect_uri}")
    private String redirectUri;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state) {
        AccesssTokenDTO accesssTokenDTO = new AccesssTokenDTO();
        accesssTokenDTO.setClient_id(clientId);
        accesssTokenDTO.setClient_secret(clientSecret);
        accesssTokenDTO.setCode(code);
        accesssTokenDTO.setRedirect_uri(redirectUri);
        accesssTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accesssTokenDTO);
        GithubUser user = githubProvider.getUser(accessToken);
        System.out.println(user.getName());
        return "index";
    }
}
