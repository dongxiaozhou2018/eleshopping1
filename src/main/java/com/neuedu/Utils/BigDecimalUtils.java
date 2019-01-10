package com.neuedu.Utils;

import java.math.BigDecimal;

/**
 * 计算价格工具类
 * */
public class BigDecimalUtils {

/**
 *加法计算
 */
    public static BigDecimal add(Double NO1,Double NO2){
        BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(NO1));
        BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(NO2));
        return bigDecimal1.add(bigDecimal2);
    }

    /**
     *减法计算
     */
    public static BigDecimal sub(Double NO1,Double NO2){
        BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(NO1));
        BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(NO2));
        return bigDecimal1.subtract(bigDecimal2);
    }

    /**
     *乘法计算
     */
    public static BigDecimal mul(Double NO1,Double NO2){
        BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(NO1));
        BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(NO2));
        return bigDecimal1.multiply(bigDecimal2);
    }

    /**
     *除法计算:保留两位小数，四舍五入
     */
    public static BigDecimal div(Double NO1,Double NO2){
        BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(NO1));
        BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(NO2));
        return bigDecimal1.divide(bigDecimal2,2,BigDecimal.ROUND_HALF_UP);
    }


}
