package com.neuedu.controller.backend;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 后台用户控制器
 * */
@RestController
@RequestMapping(value = "manager/user")
public class UserManagerController {

    @Autowired
    IUserService iUserService;

    /**
     * 管理员登陆
     * **/
    @RequestMapping(value = "/login.do")
    public ServerResponse login(HttpSession session, @RequestParam(value = "username")String username,
                                @RequestParam("password")String password)
    {
        ServerResponse serverResponse = iUserService.login(username, password);
        if (serverResponse.isSuccess()){
            UserInfo userInfo = (UserInfo) serverResponse.getData();
            if (userInfo.getRole()==Const.RoleEnum.ROLE_ADMIN.getCode()){
                return ServerResponse.creatServerResponseByError("无权限登陆");
            }
            session.setAttribute(Const.CURRENTUSER,userInfo);
        }
        return serverResponse;
    }
}
