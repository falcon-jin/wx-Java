package com.binarywang.spring.starter.wxjava.tp.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author falcon
 */
@Data
@ConfigurationProperties(prefix = "wx.cp.tp")
public class WxCropTpProps {

    private String suiteId;
    private String suiteSecret;
    /**
     * 第三方应用的token，用来检查应用的签名
     */
    private String token;
    /**
     * 第三方应用的EncodingAESKey，用来检查签名
     */
    private String aesKey;
    /**
     * 企微服务商企业ID & 企业secret，来自于企微配置
     */
    private String corpId;
    private String corpSecret;
    /**
     * 服务商secret
     */
    private String providerSecret;

}
