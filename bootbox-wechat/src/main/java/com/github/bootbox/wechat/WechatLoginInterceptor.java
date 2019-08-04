package com.github.bootbox.wechat;

import com.github.bootbox.web.api.ApiHttpRequest;
import com.github.bootbox.web.api.ApiRequestHolder;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhanguiqi
 */
@Aspect
public class WechatLoginInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(WechatLoginInterceptor.class);

    private final WechatLoginHandler wechatLoginHandler;

    public WechatLoginInterceptor(WechatLoginHandler wechatLoginHandler) {
        this.wechatLoginHandler = wechatLoginHandler;
    }

    // 010100
    @Around("@annotation(com.github.bootbox.wechat.WechatLogin)")
    public Object wechatLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        final ApiHttpRequest currentApiRequest = ApiRequestHolder.getCurrentApiRequest();
        LOGGER.info("Trying to wechat login, request:{}",
                currentApiRequest != null ? currentApiRequest.getApiRequest() : null);
        final Object baseRequest = currentApiRequest.getApiRequest();

        if (baseRequest instanceof WechatApiRequest) {
            WechatApiRequest request = (WechatApiRequest) baseRequest;
            if (StringUtils.isBlank(request.getOpenid())
                    || StringUtils.isBlank(request.getToken())) {
                throw new WechatException("缺少微信登录参数");
            }

            final Object handle = this.wechatLoginHandler.handle(request);
            if (handle != null) {
                return handle;
            }

//            final TokenInfo tokenInfo = redisClient.getJsonObject(WechatRedisKeys.WECHAT_OPENID_TOKEN_PREFIX
//                            + baseRequest.getOpenid(),
//                    TokenInfo.class
//            );
//
//        if (tokenInfo.getUid() != 0) {
//            currentApiRequest.getBaseRequest().setUid(tokenInfo.getUid());
//        }
//            if (tokenInfo == null) {
//                throw new CommonBizException("微信登录已过期");
//            }

        } else {
            throw new WechatException("Unable to login wechat, because not a wechat request!");
        }
        return joinPoint.proceed();
    }
}
