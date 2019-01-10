package com.neuedu.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.neuedu.Utils.DateUtils;
import com.neuedu.Utils.PropertiesUtils;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CategoryMapper;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Category;
import com.neuedu.pojo.Product;
import com.neuedu.service.ICategoryService;
import com.neuedu.service.IProductService;
import com.neuedu.vo.ProductDetailVO;
import com.neuedu.vo.ProductListVO;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class IProductServiceImpl implements IProductService {

    @Autowired
    ProductMapper productMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    ICategoryService iCategoryService;

    @Override
    public ServerResponse saveOrUpdate(Product product) {

        //step1：参数非空校验
        if (product==null){
            return ServerResponse.creatServerResponseByError("参数为空");
        }
        //step2：设置商品主图
        String subImages = product.getSubImages();
        if (subImages !=null&&!subImages.equals("")){
            String[] subImagesArr = subImages.split(",");
            if (subImagesArr.length>0){
                //设置商品主图
                product.setMainImage(subImagesArr[0]);
            }
        }
        //step3：商品save or update
        if (product.getId()==null){
            //添加
            int insert = productMapper.insert(product);
            if (insert>0){
                return ServerResponse.creatServerResponseBySuccess();
            }else {
                return ServerResponse.creatServerResponseByError("添加失败");
            }
        }else{
            //更新
            int insert = productMapper.updateByPrimaryKey(product);
            if (insert>0){
                return ServerResponse.creatServerResponseBySuccess();
            }else {
                return ServerResponse.creatServerResponseByError("更新失败");
            }
        }
    }

    /**
     * 商品上下架
     * */
    @Override
    public ServerResponse set_sale_status(Integer productId, Integer status) {

        //step1：商品参数非空校验
        if (productId==null){
            return ServerResponse.creatServerResponseByError("productId不能为空");
        }
        if (status==null){
            return ServerResponse.creatServerResponseByError("status不能为空");
        }
        //step2：更新商品状态
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int i = productMapper.updateProductKeySelectiv(product);
        //step3：返回结果
        if (i>0){
            return ServerResponse.creatServerResponseBySuccess();
        }else {
            return ServerResponse.creatServerResponseByError("更新失败");
        }
    }

    @Override
    public ServerResponse detail(Integer productId) {

        //step1:参数非空校验
        if (productId==null){
            return ServerResponse.creatServerResponseByError("productId不能为空");
        }
        //step2：根据商品ID查询商品信息
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product==null){
            return ServerResponse.creatServerResponseByError("商品不存在");
        }
        //step3：将product转换成productDetailVO
        ProductDetailVO productDetailVO = assembeProductDetailVO(product);
        //step4：返回结果
        return ServerResponse.creatServerResponseBySuccess(null,productDetailVO);
    }
    private ProductDetailVO assembeProductDetailVO(Product product){

        ProductDetailVO productDetailVO = new ProductDetailVO();
        productDetailVO.setCategoryId(product.getCategoryId());
        productDetailVO.setCreateTime(DateUtils.dateToStr(product.getCreateTime()));
        productDetailVO.setDetail(product.getDetail());
        productDetailVO.setImageHost(PropertiesUtils.readByKey("imageHost"));
        productDetailVO.setName(product.getName());
        productDetailVO.setMainImage(product.getMainImage());
        productDetailVO.setId(product.getId());
        productDetailVO.setPrice(product.getPrice());
        productDetailVO.setStatus(product.getStatus());
        productDetailVO.setStock(product.getStock());
        productDetailVO.setSubImages(product.getSubImages());
        productDetailVO.setSubtitle(product.getSubtitle());
        productDetailVO.setUpdateTime(DateUtils.dateToStr(product.getUpdateTime()));
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category!=null){
            productDetailVO.setParentCategoryId(category.getParentId());
        }else {
            productDetailVO.setParentCategoryId(0);
        }
        return productDetailVO;
    }
/**
 * 商品列表 分页
 * */
    @Override
    public ServerResponse list(Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum,pageSize);
        //查询商品数据
        List<Product>productList = productMapper.selectAll();
        PageInfo pageInfo = new PageInfo(productList);
        return ServerResponse.creatServerResponseBySuccess(null,pageInfo);
    }

    /**
     * 产品搜索
     * */
    @Override
    public ServerResponse search(Integer productId, String productName, Integer pageNum, Integer pageSize) {
        //必需放在查询语句前（实现分页）
        PageHelper.startPage(pageNum,pageSize);
        if (productName != null && !productName.equals("")){
            productName = "%"+productName+"%";
        }else {
            productName = null;
        }
        List<Product> products = productMapper.findProductByProductIdAndProductName(productId, productName);
        List<ProductDetailVO> ProductListVO = Lists.newArrayList();
        if(products != null && products.size()>0){
            for (Product p:products) {
                ProductDetailVO productListVO = assembeProductDetailVO(p);
                ProductListVO.add(productListVO);
            }
        }
        PageInfo pageInfo = new PageInfo(ProductListVO);
        return ServerResponse.creatServerResponseBySuccess(null,pageInfo);
    }

    /**
     * 图片上传
     * */
    @Override
    public ServerResponse upload(MultipartFile file, String path) {

        //step1:非空校验
        if (file==null){
            return ServerResponse.creatServerResponseByError("图片为空");
        }
        //step2：获取图片名称
        String originalFilename = file.getOriginalFilename();
        //step3:获取图片扩展名
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        //step4：生成新的图片名
        String newFileName=UUID.randomUUID().toString()+substring;

        File pathFile = new File(path);
        if (!pathFile.exists()){
            pathFile.setWritable(true);
            pathFile.mkdir();
        }

        File file1 = new File(path,newFileName);
        try {
            file.transferTo(file1);
            //图片上传到图片服务器上
            //...未完待续
            Map<String,String>map = Maps.newHashMap();
            map.put("uri",newFileName);
            map.put("url",PropertiesUtils.readByKey("imageHost")+"/"+newFileName);
            return ServerResponse.creatServerResponseBySuccess(null,map);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 前台接口-商品详情
     * */
    @Override
    public ServerResponse detailportal(Integer productId) {

        //step1:校验参数
        if (productId == null) {
            return ServerResponse.creatServerResponseByError("productId不能为空");
        }
        //step2：根据商品ID查询商品信息
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.creatServerResponseByError("商品不存在");
        }
            //step3：校验商品状态
            if (product.getStatus() != Const.ProductStatusEnum.PRODUCT_ONLINE.getCode()) {
                return ServerResponse.creatServerResponseByError();
            }
            //step4：获取productdetailVO
            ProductDetailVO productDetailVO = assembeProductDetailVO(product);
            //step5：返回结果
            return ServerResponse.creatServerResponseBySuccess(null, productDetailVO);


    }

    /**
     * 前台商品搜索及排序
     * */
    @Override
    public ServerResponse list_portal(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy) {

        Set<Integer> integerSet = Sets.newHashSet();
        //step1:参数校验：categoryId和keyword不能同时为空
        if (categoryId==null&&(keyword==null||keyword.equals(""))){
            return ServerResponse.creatServerResponseByError("参数错误");
        }
        //step2：categoryId
        if (categoryId!=null){
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category==null&&(keyword==null||keyword.equals(""))){
                //说明没有商品数据
                PageHelper.startPage(pageNum,pageSize);
                List<ProductDetailVO>productDetailVOS = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo();
                return  ServerResponse.creatServerResponseBySuccess(null,pageInfo);
            }

            ServerResponse deep_categoryList = iCategoryService.get_deep_category(categoryId);
            if (deep_categoryList.isSuccess()){
                integerSet = (Set<Integer>) deep_categoryList.getData();
            }
        }
        //step3：keyword
        if (keyword!=null&&!keyword.equals("")){
            keyword="%"+keyword+"%";
        }

        if (orderBy.equals("")){
            PageHelper.startPage(pageNum,pageSize);
        }else {
            String[] s = orderBy.split("_");
            if (s.length>1){
                PageHelper.startPage(pageNum,pageSize,s[0]+" "+s[1]);
            }else{
                PageHelper.startPage(pageNum,pageSize);
            }
        }
        //step4:List<product>-->List<ProductDetailVO>
        List<Product> productList = productMapper.searchProduct(integerSet,keyword);
        List<ProductDetailVO> productDetailVOList = Lists.newArrayList();
        if (productList!=null&productList.size()>0){
            for (Product product:productList){
                ProductDetailVO productDetailVO = assembeProductDetailVO(product);
                productDetailVOList.add(productDetailVO);
            }
        }
        //step5:分页
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(productDetailVOList);
        //step6:返回结果
        return ServerResponse.creatServerResponseBySuccess(null,pageInfo);
    }
}
