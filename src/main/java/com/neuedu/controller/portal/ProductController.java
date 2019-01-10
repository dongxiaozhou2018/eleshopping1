package com.neuedu.controller.portal;

import com.neuedu.common.ServerResponse;
import com.neuedu.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/product")
public class ProductController {

    @Autowired
    IProductService iProductService;
    /**
     * 商品详情
     * */
    @RequestMapping(value = "/detailportal.do")
    public ServerResponse detailportal(Integer productId){
        return iProductService.detailportal(productId);
    }
    /**
     * 前台-搜索商品并排序
     * */
    @RequestMapping(value = "listportail.do")
    public  ServerResponse list(@RequestParam(required = false)Integer categoryId,
                                @RequestParam(required = false)String keyword,
                                @RequestParam(required = false,defaultValue ="1")Integer pageNum,
                                @RequestParam(required = false,defaultValue = "10")Integer pageSize,
                                @RequestParam(required = false,defaultValue = "")String orderBy){

        return iProductService.list_portal(categoryId,keyword,pageNum,pageSize,orderBy);
    }

}
