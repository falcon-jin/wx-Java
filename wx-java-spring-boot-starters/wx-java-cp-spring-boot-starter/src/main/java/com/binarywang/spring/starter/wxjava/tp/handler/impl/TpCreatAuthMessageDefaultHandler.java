package com.binarywang.spring.starter.wxjava.tp.handler.impl;

import com.binarywang.spring.starter.wxjava.tp.handler.TpCreatAuthMessageHandler;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.cp.bean.message.WxCpTpXmlMessage;
import me.chanjar.weixin.cp.bean.message.WxCpXmlOutMessage;
import me.chanjar.weixin.cp.tp.service.WxCpTpService;

import java.util.Map;

/**
 * 推送suite_ticket事件默认实现
 *
 * @author falcon
 */
public class TpCreatAuthMessageDefaultHandler implements TpCreatAuthMessageHandler {

    @Override
    public WxCpXmlOutMessage handle(WxCpTpXmlMessage wxMessage, Map<String, Object> context, WxCpTpService wxCpService, WxSessionManager sessionManager) throws WxErrorException {
        return null;
    }
}
