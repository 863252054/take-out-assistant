package cn.edu.zucc.personplan.itf;

import cn.edu.zucc.personplan.model.BeanMerchant;
import cn.edu.zucc.personplan.model.BeanProductDetails;
import cn.edu.zucc.personplan.model.BeanProductEvaluate;
import cn.edu.zucc.personplan.model.BeanProductType;
import cn.edu.zucc.personplan.util.BaseException;

import java.util.List;

public interface IProductDetailsManager {
    public void addProduct(BeanProductType productType, String product_name,
                           float product_price, float product_discountPrice, int count) throws BaseException;

    public void deleteProduct(BeanProductDetails productDetails) throws BaseException;

    public void modifyProductDetails(BeanProductDetails productDetails, String product_name,
                       float product_price, float product_discountPrice,int count) throws BaseException;

    public void evaluateProduct(BeanProductEvaluate productEvaluate,String evaluateContent, int evaluateStarRated) throws  BaseException;

    public List<BeanProductDetails> loadProductDetails(BeanProductType productType) throws BaseException;

    public List<BeanProductEvaluate> loadProductEvaluate() throws BaseException;
}
