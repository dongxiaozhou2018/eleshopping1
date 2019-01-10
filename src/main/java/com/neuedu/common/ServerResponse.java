package com.neuedu.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> {


    private Integer status;//状态码为0时：成功
    private T data;//当status= 0时，data对应的接口相应的数据
    private String meg;//提示信息

    public ServerResponse() {
    }

    public ServerResponse(Integer status) {
        this.status = status;
    }
    public ServerResponse(Integer status,  String meg) {
        this.status = status;
        this.meg = meg;
    }
    public ServerResponse(Integer status, String meg, T data) {
        this.status = status;
        this.meg = meg;
        this.data = data;
    }
/*
* {"status":0}调用成功
* */
    public static ServerResponse creatServerResponseBySuccess()
    {
        return new ServerResponse(ResponseCode.SUCCESS);
    }
    /*
     * {"status":0，"meg":"***"}调用成功
     * */
    public static ServerResponse creatServerResponseBySuccess(String meg)
    {
        return new ServerResponse(ResponseCode.SUCCESS,meg);
    }
    /*
     * {"status":0，"meg":"***","data":{}}调用成功
     * */
    public static <T> ServerResponse creatServerResponseBySuccess(String meg,T data)
    {
        return new ServerResponse(ResponseCode.SUCCESS,meg,data);
    }
    /*
     * {"status":1}调用失败
     * */
    public static ServerResponse creatServerResponseByError()
    {
        return new ServerResponse(ResponseCode.ERROR);
    }
    /*
     * {"status":custom}自定义调用失败
     * */
    public static ServerResponse creatServerResponseByError(Integer status)
    {
        return new ServerResponse(status);
    }
    /*
     * {"status":custom}自定义调用失败
     * */
    public static ServerResponse creatServerResponseByError(String meg)
    {
        return new ServerResponse(ResponseCode.ERROR,meg);
    }
    /*
     * {"status":custom}自定义调用失败
     * */
    public static ServerResponse creatServerResponseByError(Integer status,String meg)
    {
        return new ServerResponse(ResponseCode.ERROR,meg);
    }
/*
*
* 判断接口是否访问成功
* */
    @JsonIgnore
    public  boolean isSuccess()
    {
        return this.status==ResponseCode.SUCCESS;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMeg() {
        return meg;
    }

    public void setMeg(String meg) {
        this.meg = meg;
    }
}
