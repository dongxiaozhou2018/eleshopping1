package com.neuedu.controller.portal;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    IUserService iUserService;

    /*
    * 登录
    * */
    @RequestMapping(value = "/login.do")
    public ServerResponse login(HttpSession session,@RequestParam(value = "username")String username,
                                @RequestParam("password")String password)
    {
        ServerResponse serverResponse = iUserService.login(username, password);
        if (serverResponse.isSuccess()){
            UserInfo userInfo = (UserInfo) serverResponse.getData();
            session.setAttribute(Const.CURRENTUSER,userInfo);
        }
        return serverResponse;
    }

    /**
     * 注册
     * */
    @RequestMapping(value = "/register.do")
    public ServerResponse register(HttpSession session,UserInfo userInfo)
    {
        ServerResponse serverResponse = iUserService.register(userInfo);
        return serverResponse;
    }

    /**
     * 根据用户名查询密保问题
     * */
    @RequestMapping(value = "/forget_get_question.do")
    public ServerResponse forget_get_question(String username)
    {
        ServerResponse serverResponse = iUserService.forget_get_question(username);
        return serverResponse;
    }

    /**
     * 提交问题答案
     * */
    @RequestMapping(value = "/forget_get_answer.do")
    public ServerResponse forget_get_answer(String username,String question,String answer )
    {
        ServerResponse serverResponse = iUserService.forget_get_answer(username,question,answer);
        return serverResponse;
    }

    /**
     * 忘记密码的重置密码
     * */
    @RequestMapping(value = "/forget_reset_password.do")
    public ServerResponse forget_reset_password(String username,String passwordNew,String forgetIp )
    {
        ServerResponse serverResponse = iUserService.forget_reset_password(username,passwordNew,forgetIp);
        return serverResponse;
    }

    /**
     * 检测用户名或邮箱是否有效
     * */
    @RequestMapping(value = "/check_valid.do")
    public ServerResponse check_valid(String str,String type)
    {
        ServerResponse serverResponse = iUserService.check_valid(str,type);
        return serverResponse;
    }

    /**
     * 获取登陆用户信息
     * */
    @RequestMapping(value = "/get_user_info.do")
    public ServerResponse get_user_info(HttpSession session)
    {
        UserInfo userInfo =(UserInfo)session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.creatServerResponseByError("用户未登录");
        }
        userInfo.setPassword("");
        userInfo.setQuestion("");
        userInfo.setAnswer("");
        userInfo.setRole(null);
        return ServerResponse.creatServerResponseBySuccess(null,userInfo);
    }

    /**
     * 登录状态中重置密码
     * */
    @RequestMapping(value = "/reset_password.do")
    public ServerResponse reset_password(HttpSession session,String passwordOld,String passwordNew)
    {
        UserInfo userInfo =(UserInfo)session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.creatServerResponseByError("用户未登录");
        }
        return iUserService.reset_password(userInfo.getUsername(),passwordOld,passwordNew);
    }

    /**
     * 登录状态中更新个人信息
     * */
    @RequestMapping(value = "/update_information.do")
    public ServerResponse update_information(HttpSession session,UserInfo user)
    {
        UserInfo userInfo =(UserInfo)session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.creatServerResponseByError("用户未登录");
        }
        user.setId(userInfo.getId());
        ServerResponse serverResponse = iUserService.update_information(user);
        if (serverResponse.isSuccess()){
            //更新session中用户信息
            UserInfo userInfoByUserid = iUserService.findUserInfoByUserid(userInfo.getId());
            session.setAttribute(Const.CURRENTUSER,userInfoByUserid);
        }
        return serverResponse;
    }
    /**
     * 获取登陆用户详细信息
     * */
    @RequestMapping(value = "/get_information.do")
    public ServerResponse get_information(HttpSession session)
    {
        UserInfo userInfo =(UserInfo)session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.creatServerResponseByError("用户未登录");
        }
        userInfo.setPassword("");
        return ServerResponse.creatServerResponseBySuccess(null,userInfo);
    }

    /**
     * 退出登录
     * */
    @RequestMapping(value = "/logout.do")
    public ServerResponse logout(HttpSession session)
    {
        session.removeAttribute(Const.CURRENTUSER);
        return ServerResponse.creatServerResponseBySuccess();
    }
}
