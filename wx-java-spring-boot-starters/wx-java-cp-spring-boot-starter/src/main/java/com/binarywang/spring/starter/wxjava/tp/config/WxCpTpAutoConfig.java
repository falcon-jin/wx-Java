package com.binarywang.spring.starter.wxjava.tp.config;

import com.binarywang.spring.starter.wxjava.tp.handler.*;
import com.binarywang.spring.starter.wxjava.tp.handler.impl.*;
import com.binarywang.spring.starter.wxjava.tp.properties.WxCropTpProps;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.redis.RedissonWxRedisOps;
import me.chanjar.weixin.cp.config.impl.WxCpTpRedissonConfigImpl;
import me.chanjar.weixin.cp.constant.WxCpTpConsts;
import me.chanjar.weixin.cp.tp.message.WxCpTpMessageRouter;
import me.chanjar.weixin.cp.tp.service.WxCpTpService;
import me.chanjar.weixin.cp.tp.service.impl.WxCpTpServiceImpl;

import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author falcon
 */
@AutoConfiguration
@EnableConfigurationProperties(WxCropTpProps.class)
public class WxCpTpAutoConfig {

    @Bean
    public WxCpTpService wxCpTpService(RedissonClient redissonClient, WxCropTpProps cropTpProps) {
        WxCpTpService wxCpTpService = new WxCpTpServiceImpl();
        WxCpTpRedissonConfigImpl tpConfig = WxCpTpRedissonConfigImpl.builder()
                .wxRedisOps(new RedissonWxRedisOps(redissonClient))
                .keyPrefix("workTpRedis:")
                .corpId(cropTpProps.getCorpId())
                .corpSecret(cropTpProps.getCorpSecret())
                .token(cropTpProps.getToken())
                .aesKey(cropTpProps.getAesKey())
                .suiteId(cropTpProps.getSuiteId())
                .suiteSecret(cropTpProps.getSuiteSecret())
                .build();
        wxCpTpService.setWxCpTpConfigStorage(tpConfig);
        return wxCpTpService;
    }

    @Bean
    @ConditionalOnMissingBean
    public TpSubscribeMessageHandler tpSubscribeMessageHandler() {
        return new TpSubscribeMessageDefaultHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public TpCreatAuthMessageHandler tpCreatAuthMessageHandler() {
        return new TpCreatAuthMessageDefaultHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public TpSuiteTicketMessageHandler tpSuiteTicketMessageHandler() {
        return new TpSuiteTicketMessageDefaultHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public TpChangeContactMessageHandler tpChangeContactMessageHandler() {
        return new TpChangeContactMessageDefaultHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public TpAddExternalContactMessageHandler tpAddExternalContactMessageHandler() {
        return new TpAddExternalContactMessageDefaultHandler();
    }


    @Bean
    public WxCpTpMessageRouter wxCpTpMessageRouter(WxCpTpService wxCpTpService,
                                                   TpSubscribeMessageHandler subscribeMessageHandler,
                                                   TpCreatAuthMessageHandler tpCreatAuthMessageHandler,
                                                   TpSuiteTicketMessageHandler tpSuiteTicketMessageHandler,
                                                   TpChangeContactMessageHandler tpChangeContactMessageHandler,
                                                   TpAddExternalContactMessageHandler tpAddExternalContactMessageHandler) {
        WxCpTpMessageRouter router = new WxCpTpMessageRouter(wxCpTpService);
        //订阅事件
        router.rule()
                .msgType(WxConsts.XmlMsgType.EVENT)
                .event(WxConsts.EventType.SUBSCRIBE)
                .handler(subscribeMessageHandler).end();

        //授权成功通知:临时授权码
        router.rule()
                .infoType(WxCpTpConsts.InfoType.CREATE_AUTH)
                .handler(tpCreatAuthMessageHandler).end();

        //推送suite_ticket
        router.rule()
                .infoType(WxCpTpConsts.InfoType.SUITE_TICKET)
                .handler(tpSuiteTicketMessageHandler).end();

        //成员通知事件
        router.rule()
                .infoType(WxCpTpConsts.InfoType.CHANGE_CONTACT)
                .handler(tpChangeContactMessageHandler).end();

        //添加企业客户事件
        router.rule()
                .infoType(WxCpTpConsts.InfoType.CHANGE_EXTERNAL_CONTACT)
                .changeType("add_external_contact")
                .handler(tpAddExternalContactMessageHandler).end();
        return router;
    }
}
