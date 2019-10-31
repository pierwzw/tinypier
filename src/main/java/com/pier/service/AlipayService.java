package com.pier.service;
import com.alipay.api.domain.*;

import java.util.ArrayList;
import java.util.Map;

import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayOpenPublicTemplateMessageIndustryModifyRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayUserUserinfoShareRequest;
import com.alipay.api.response.AlipayOpenPublicTemplateMessageIndustryModifyResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayUserUserinfoShareResponse;
import com.pier.bean.Book;
import com.pier.bean.Order;
import com.pier.config.Constant;
import com.pier.utils.JsonUtils;
import com.pier.utils.OrderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author zhongweiwu
 * @date 2019/10/25 15:51
 */
@Slf4j
@Service
public class AlipayService {

    private static final String CHARSET = "UTF-8";
    private static String ALIPAY_URL = Constant.ALIPAY_URL;
    private static String APP_ID = Constant.APP_ID;
    private static String APP_PRIVATE_KEY  = Constant.APP_PRIVATE_KEY;
    private static String ALIPAY_PUBLIC_KEY  =Constant.ALIPAY_PUBLIC_KEY;

    private static final String NOTIFY_URL = Constant.NOTIFY_URL;

    private DefaultAlipayClient alipayClient;

    @PostConstruct
    public void init(){
        alipayClient = new DefaultAlipayClient(ALIPAY_URL, APP_ID, APP_PRIVATE_KEY, "json", CHARSET,
                ALIPAY_PUBLIC_KEY, "RSA2");
    }

    /**
     * 支付宝预下单
     * @param order
     * @return
     */
    public AlipayTradePrecreateResponse precreateOrder(Order order, String title){
        try{
            AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
            request.setNotifyUrl(NOTIFY_URL);
            AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
            model.setOutTradeNo(order.getOrderId());
            model.setTotalAmount(order.getPrice().toString());
            model.setSubject(title);
            request.setBizContent(JsonUtils.toUnderlineJSONString(model));
            return alipayClient.execute(request);
        }catch (Exception e){
            log.error("系统异常", e);
        }
        return null;
    }

    /**
     * 订单查询，需要支付完成后等待5秒再查询
     * @param outTradeNo
     * @return
     */
     public AlipayTradeQueryResponse queryOrder(String outTradeNo){
        try{
            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
            AlipayTradeQueryModel alipayTradeQueryModel = new AlipayTradeQueryModel();
            alipayTradeQueryModel.setOutTradeNo(outTradeNo);
            request.setBizContent(JsonUtils.toUnderlineJSONString(alipayTradeQueryModel));
            return alipayClient.execute(request);
        }catch (Exception e){
            log.error("系统异常", e);
        }
        return null;
     }

    public void callDemo() {
        try{
            //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.open.public.template.message.industry.modify
            AlipayOpenPublicTemplateMessageIndustryModifyRequest request = new AlipayOpenPublicTemplateMessageIndustryModifyRequest();
            //SDK已经封装掉了公共参数，这里只需要传入业务参数
            //此次只是参数展示，未进行字符串转义，实际情况下请转义
            request.setBizContent("  {" +
                    "    \"primary_industry_name\":\"IT科技/IT软件与服务\"," +
                    "    \"primary_industry_code\":\"10001/20102\"," +
                    "    \"secondary_industry_code\":\"10001/20102\"," +
                    "    \"secondary_industry_name\":\"IT科技/IT软件与服务\"" +
                    " }");
            AlipayOpenPublicTemplateMessageIndustryModifyResponse response = alipayClient.execute(request);
            //调用成功，则处理业务逻辑
            if (response.isSuccess()) {
                //.....
            }
        }catch (Exception e){
        }
    }

    public void userAuthorize(){
        try{
            //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.user.userinfo.share
            AlipayUserUserinfoShareRequest request = new AlipayUserUserinfoShareRequest();
            //授权类接口执行API调用时需要带上accessToken
            AlipayUserUserinfoShareResponse response= alipayClient.execute(request,"accessToken");
            //业务处理
        }catch (Exception e){

        }
    }

    public void AppAuthorize(){
        try{
            //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.open.public.template.message.industry.modify
            AlipayOpenPublicTemplateMessageIndustryModifyRequest request = new AlipayOpenPublicTemplateMessageIndustryModifyRequest();
            //SDK已经封装掉了公共参数，这里只需要传入业务参数
            //此次只是参数展示，未进行字符串转义，实际情况下请转义
            request.setBizContent("  { " +
                    "    \"primary_industry_name\":\"IT科技/IT软件与服务\"," +
                    "    \"primary_industry_code\":\"10001/20102\"," +
                    "    \"secondary_industry_code\":\"10001/20102\"," +
                    "    \"secondary_industry_name\":\"IT科技/IT软件与服务\"" +
                    "  }");
            //ISV代理商户调用需要传入app_auth_token
            request.putOtherTextParam("app_auth_token", "201511BBaaa6464f271f49e482f2e9fe63ca5F05");
            AlipayOpenPublicTemplateMessageIndustryModifyResponse response = alipayClient.execute(request);
            //调用成功，则处理业务逻辑
            if(response.isSuccess()){
                //.....
            }
        }catch (Exception e){

        }
    }

    /**
     * 异步通知验签
     */
    public boolean verifySign(Map<String, String> paramsMap){
        try{
            //调用SDK验证签名
            boolean signVerified = AlipaySignature.rsaCheckV1(paramsMap, ALIPAY_PUBLIC_KEY, CHARSET, "RSA2");
            if(signVerified){
                // TODO 验签成功后
                // 需要做以下的检验：1. out_trade_no 2. total_amount 3. seller_id 4. app_id 5. 订单状态
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            log.error("verify sign error");
            return false;
        }
    }
}
