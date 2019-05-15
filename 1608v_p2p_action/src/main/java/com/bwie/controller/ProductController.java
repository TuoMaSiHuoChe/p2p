package com.bwie.controller;

import com.bwie.pojo.Product;
import com.bwie.pojo.ProductEarningRate;
import com.bwie.service.ProductService;
import com.bwie.until.FrontStatusConstants;
import com.bwie.until.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author: admin
 * @Date: 2019/5/14 08:48
 * @Description:
 */
@RestController
@RequestMapping("product")
public class ProductController extends BaseController {
    @Autowired
    private ProductService productService;

    @RequestMapping("findAllProduct")
    public Response findAllProduct(){
        List<Product> list=productService.findAllProduct();
        return Response.build().setStatus(FrontStatusConstants.SUCCESS).setData(list).setMessage("查询成功");
    }
    @RequestMapping("findProductById")
    public Response findProductById(@RequestParam("proId")Long proId){
        Product product= productService.findProductById(proId);
        return Response.build().setStatus(FrontStatusConstants.SUCCESS).setData(product).setMessage("查询成功");
    }
    @RequestMapping("findRates")
    public Response findRates(@RequestParam("proId")Integer proId){
        List<ProductEarningRate> list = productService.findRates(proId);

        return Response.build().setStatus(FrontStatusConstants.SUCCESS).setData(list).setMessage("查询成功");
    }
    @RequestMapping("modifyProduct")
    public Response modifyProduct(@Valid @RequestBody Product product, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return Response.build().setMessage(bindingResult.getFieldError().getDefaultMessage()).setStatus(FrontStatusConstants.EROO);
        }
        productService.update(product);
        return Response.build().setMessage("修改成功").setStatus(FrontStatusConstants.SUCCESS);
    }
    @RequestMapping("addProduct")
    public Response addProduct(@Valid @RequestBody Product product, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return Response.build().setMessage(bindingResult.getFieldError().getDefaultMessage()).setStatus(FrontStatusConstants.EROO);
        }
        productService.save(product);
        return Response.build().setMessage("添加成功").setStatus(FrontStatusConstants.SUCCESS);
    }
    @RequestMapping("delProduct")
    public Response addProduct(@RequestParam("proId")Long proId){

        productService.delete(proId);
        return Response.build().setMessage("删除成功").setStatus(FrontStatusConstants.SUCCESS);
    }

}
