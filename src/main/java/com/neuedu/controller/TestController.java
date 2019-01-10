package com.neuedu.controller;

import com.neuedu.common.ServerResponse;
import com.neuedu.dao.UserInfoMapper;
import com.neuedu.pojo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    UserInfoMapper userInfoMapper;

    @RequestMapping(value ="/user/{userid}")
    public ServerResponse<UserInfo> getUser(@PathVariable Integer userid)
    {
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userid);

        if (userInfo !=null)
        {
            return ServerResponse.creatServerResponseBySuccess(null,userInfo);
        }else{
            return ServerResponse.creatServerResponseByError("fail");
        }

    }
}
