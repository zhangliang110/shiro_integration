package com.meixun.b2b.mall.component.shiro.listener;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义session失效
 * @author zl
 * @className: ShiroSessionListener
 * @description:
 * @createDate: 2017年3月8日
 * @company:　美讯在线
 * @version: 1.0
 */
public class ShiroSessionListener extends SessionListenerAdapter{
	private static final Logger logger = LoggerFactory.getLogger(ShiroSessionListener.class);

	@Override
	public void onExpiration(Session session) {
		logger.debug("shiro session 以失效");
		/*SecurityUtils.getSubject().logout();	//shiro认证登出
*/		super.onExpiration(session);
	}
}
