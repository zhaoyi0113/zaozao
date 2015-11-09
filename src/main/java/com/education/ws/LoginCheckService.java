package com.education.ws;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by yzzhao on 11/9/15.
 */
@Service
public class LoginCheckService {

    public boolean whetherLogin(HttpServletRequest request){
        HttpSession session = request.getSession();
        return session.getAttribute("user_name") != null;
    }
}
