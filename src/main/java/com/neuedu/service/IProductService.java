package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Product;
import org.springframework.web.multipart.MultipartFile;


public interface IProductService {

    /**
     * 商品添加或修改
     * */
    ServerResponse saveOrUpdate(Product product);

    /**
     * 商品上下架
     * */
    ServerResponse set_sale_status(Integer productId,Integer status);

    /**
     * 后台查看商品详情
     * */
    ServerResponse detail( Integer productId);

    /**
     * 后台-商品列表 分页
     * */
    ServerResponse list(Integer pageNum,Integer pageSize);
    /**
     * 商品搜索
     * */
    ServerResponse search(Integer productId,String productName,Integer pageNum,Integer pageSize);
    /**
     *图片上传
     **/
    ServerResponse upload(MultipartFile file,String path);
    /**
     * 前台商品详情
     * */
    ServerResponse detailportal(Integer productId);
    /**
     * 前台商品搜索
     * */
    ServerResponse list_portal(Integer categoryId,String keyword,Integer pageNum,Integer pageSize,String orderBy);
}
