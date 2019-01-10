package com.neuedu.controller.backend;

import com.neuedu.common.ServerResponse;
import com.neuedu.service.IProductService;
import io.lettuce.core.output.ValueListOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import sun.awt.SunHints;

@Controller
@RequestMapping(value = "/manager/product")
public class UploadController {

    @Autowired
    IProductService iProductService;

@RequestMapping(value ="/upload",method = RequestMethod.GET)
    public String upload(){
        return "upload";
    }
    @RequestMapping(value ="/upload",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse upload2(@RequestParam(value = "upload_file",required = false)MultipartFile file){

    String path="D:\\图片";
    return iProductService.upload(file,path);
    }

}
