package com.meixun.b2b.mall.component.shiro.dao;

import java.io.Serializable;
import java.util.Date;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;

import com.meixun.b2b.mall.component.shiro.util.SerializableUtils;
import com.whalin.MemCached.MemCachedClient;

public class ShiroSessionDao extends CachingSessionDAO{

	@Autowired
	private MemCachedClient client;
	
	@Override
	protected void doUpdate(Session session) {
		if(session instanceof ValidatingSession && !((ValidatingSession)session).isValid()) {
		    return; //如果会话过期/停止 没必要再更新了
		}
		client.replace(session.getId().toString(), SerializableUtils.serialize(session), new Date(1000 * 60 * 60));
	}

	@Override
	protected void doDelete(Session session) {
//		SecurityUtils.getSubject().logout();		//登出
		client.delete(session.getId().toString());
	}

	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = generateSessionId(session);
		assignSessionId(session, sessionId);
		client.add(sessionId.toString(), SerializableUtils.serialize(session), new Date(1000 * 60 * 60));
		return session.getId();
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		Object value = client.get(sessionId.toString());
		if (value == null) {
			return null;
		}
		return SerializableUtils.deserialize((String)value);
	}

}
