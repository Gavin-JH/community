package com.gavin.community.controller;

import com.gavin.community.dto.AccesssTokenDTO;
import com.gavin.community.dto.GithubUser;
import com.gavin.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

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
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request) {

        AccesssTokenDTO accesssTokenDTO = new AccesssTokenDTO();
        accesssTokenDTO.setClient_id(clientId);
        accesssTokenDTO.setClient_secret(clientSecret);
        accesssTokenDTO.setCode(code);
        accesssTokenDTO.setRedirect_uri(redirectUri);
        accesssTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accesssTokenDTO);
        GithubUser user = githubProvider.getUser(accessToken);
        if (user != null) {
            //登录成功
            request.getSession().setAttribute("user", user);//user对象放到session中
            //加了redirect会将地址改变，重定向到主页，http://localhost:3595
            // 如果不加，则地址不变，只渲染页面http://localhost:3595/callback?code=e9ab9399e30ab2dcd01b&state=1#
            return "redirect:/";//回到index.xml
        } else {
            //登录失败，重新登录
            return "redirect:/";
        }
    }
}
