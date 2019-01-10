package com.neuedu.service.impl;

import com.neuedu.Utils.IPCache;
import com.neuedu.Utils.MD5Utils;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.UserInfoMapper;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IUserServiceImpl implements IUserService {

    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public ServerResponse login(String username, String password) {

        //step1:参数的非空校验
        if(username==null||username.equals(""))
        {
            return ServerResponse.creatServerResponseByError("用户名不能为空");
        }
        if(password==null||password.equals(""))
        {
            return ServerResponse.creatServerResponseByError("密码不能为空");
        }

        //step2:检查用户名是否存在
        int i = userInfoMapper.checkUsername(username);
        if (i==0)
        {
            return ServerResponse.creatServerResponseByError("用户名不存在");
        }

        //step3：根据用户名密码查找用户信息
        UserInfo userInfo = userInfoMapper.selectUserInfoByUsernameAndPassword(username,MD5Utils.getMD5Code(password));
        if (userInfo==null){
            return ServerResponse.creatServerResponseByError("密码错误");
        }
        //step4：返回结果
        userInfo.setPassword("");
        return ServerResponse.creatServerResponseBySuccess(null,userInfo);
    }

    @Override
    public ServerResponse register(UserInfo userInfo) {

        //step1:参数非空校验
        if (userInfo==null)
        {
            return ServerResponse.creatServerResponseByError("参数必须");
        }
        //step2:校验用户名
        int i = userInfoMapper.checkUsername(userInfo.getUsername());
        if (i>0)
        {
            return ServerResponse.creatServerResponseByError("用户名已存在");
        }

        //step3:校验邮箱是否存在
        int result_email = userInfoMapper.checkEmail(userInfo.getEmail());
        if (result_email>0)
        {
            return ServerResponse.creatServerResponseByError("该邮箱已注册");
        }

        //step4:注册
        userInfo.setRole(Const.RoleEnum.ROLE_CUSTOMER.getCode());
        userInfo.setPassword(MD5Utils.getMD5Code(userInfo.getPassword()));
        int insert = userInfoMapper.insert(userInfo);
        if (insert>0){
            return ServerResponse.creatServerResponseBySuccess("注册成功");
        }
        //step5:返回结果

        return ServerResponse.creatServerResponseByError("注册失败");
    }

    @Override
    public ServerResponse forget_get_question(String username) {

        //step1:参数校验
        if (username==null||username.equals("")){
            return ServerResponse.creatServerResponseByError("用户名不能为空");
        }
        //step2:校验username
        int i = userInfoMapper.checkUsername(username);
        if (i==0)
        {//用户名不存在
            return ServerResponse.creatServerResponseByError("用户名不存在，请重新输入");
        }
        //step3:查找密保问题
        String question = userInfoMapper.selectQuestionByUsername(username);
        if (question==null||question.equals("")){
            return ServerResponse.creatServerResponseByError("密保问题未设置");
        }
        return ServerResponse.creatServerResponseBySuccess(question);
    }

    @Override
    public ServerResponse forget_get_answer(String username, String question, String answer) {

        //step1:参数校验
        if (username==null||username.equals("")){
            return ServerResponse.creatServerResponseByError("用户名不能为空");
        }
        if (question==null||question.equals("")){
            return ServerResponse.creatServerResponseByError("问题不能为空");
        }
        if (answer==null||answer.equals("")){
            return ServerResponse.creatServerResponseByError("答案不能为空");
        }
        //step2:根据username，question，answer查询
        int i = userInfoMapper.selectByUsernameAndQuestionAndAnswer(username, question, answer);
        if (i==0){//答案错误
            return ServerResponse.creatServerResponseByError("答案有误");
        }
        //step3:服务端生成一个ip保存并将ip返回给客户端
        String forgetIp = UUID.randomUUID().toString();
        IPCache.set(username,forgetIp);

        return ServerResponse.creatServerResponseBySuccess(null,forgetIp);
    }

    @Override
    public ServerResponse forget_reset_password(String username, String passwordNew, String forgetIp) {

        //step1:参数校验
        if (username==null||username.equals("")){
            return ServerResponse.creatServerResponseByError("用户名不能为空");
        }
        if (passwordNew==null||passwordNew.equals("")){
            return ServerResponse.creatServerResponseByError("密码不能为空");
        }
        if (forgetIp==null||forgetIp.equals("")){
            return ServerResponse.creatServerResponseByError("ip不能为空");
        }
        //step2：ip校验
        String ip = IPCache.get(username);
        if (ip==null){
            return ServerResponse.creatServerResponseByError("ip过期");
        }
        if (!ip.equals(forgetIp)){
            return ServerResponse.creatServerResponseByError("无效的ip");
        }
        //step3：修改密码
        int i = userInfoMapper.updateUserPassword(username,MD5Utils.getMD5Code(passwordNew));
        if (i>0){
            return ServerResponse.creatServerResponseBySuccess();
        }
        return ServerResponse.creatServerResponseByError("密码修改失败");
    }

    @Override
    public ServerResponse check_valid(String str, String type) {

        //step1:参数非空校验
        if (str==null||str.equals("")){
            return ServerResponse.creatServerResponseByError("用户名或邮箱不能为空");
        }
        if (type==null||type.equals("")){
            return ServerResponse.creatServerResponseByError("校验的类型参数不能为空");
        }
        //step2：type：username-》校验用户名str
        //           ：email--》校验邮箱str
        if (type.equals("username")){
            int i = userInfoMapper.checkUsername(str);
            if (i>0){
                //用户名已存在
                return ServerResponse.creatServerResponseByError("用户名已存在");
            }else {
                return ServerResponse.creatServerResponseBySuccess();
            }
        }else if (type.equals("email")){
            int i = userInfoMapper.checkEmail(str);
            if (i>0){
                //邮箱已存在
                return ServerResponse.creatServerResponseByError("邮箱已存在");
            }else {
                return ServerResponse.creatServerResponseBySuccess();
            }
        }else{
            return ServerResponse.creatServerResponseByError("参数类型错误");
        }
        }

    @Override
    public ServerResponse reset_password(String username,String passwordOld, String passwordNew) {

        //step1:参数非空校验
        if (passwordOld==null||passwordOld.equals("")){
            return ServerResponse.creatServerResponseByError("用户旧密码不能为空");
        }
        if (passwordNew==null||passwordNew.equals("")){
            return ServerResponse.creatServerResponseByError("用户新密码不能为空");
        }
        //step2：根据用户名和passwordOld
        UserInfo userInfo = userInfoMapper.selectUserInfoByUsernameAndPassword(username, MD5Utils.getMD5Code(passwordOld));
        if(userInfo==null){
            return ServerResponse.creatServerResponseByError("旧密码错误");
        }
        //step修改密码
        userInfo.setPassword(MD5Utils.getMD5Code(passwordNew));
        int i = userInfoMapper.updateByPrimaryKey(userInfo);
        if (i>0){
            return ServerResponse.creatServerResponseBySuccess();
        }
        return ServerResponse.creatServerResponseByError("密码修改失败");
    }

    @Override
    public ServerResponse update_information(UserInfo user) {

        //step1:参数校验
        if (user==null){
            return ServerResponse.creatServerResponseByError("参数不能为空");
        }
        //step2：更新用户信息
        int i = userInfoMapper.updateUserBySelectActive(user);
        if (i>0){
            return ServerResponse.creatServerResponseBySuccess();
        }
        return ServerResponse.creatServerResponseByError("更新个人信息失败");
    }

    @Override
    public UserInfo findUserInfoByUserid(Integer userId) {

       return userInfoMapper.selectByPrimaryKey(userId);
    }
    //step3：返回结果
}
