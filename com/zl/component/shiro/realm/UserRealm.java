package com.meixun.b2b.mall.component.shiro.realm;

import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;
import com.meixun.b2b.mall.web.dao.IOssUserDao;
import com.meixun.b2b.mall.web.model.OssRole;
import com.meixun.b2b.mall.web.model.OssUserModel;
import com.meixun.b2b.mall.web.service.IAuthorityService;

/**
 * <p>Date: 14-1-28
 * <p>Version: 1.0
 */
@Component
public class UserRealm extends AuthorizingRealm {

	@Autowired
    private IOssUserDao userDao ;
	@Autowired
	private IAuthorityService authorityService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String)principals.getPrimaryPrincipal();
        OssUserModel userModel = userDao.getUserByName(username);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        List<OssRole> roles = authorityService.getRolesByUserId(userModel.getUserId());
        Set<String> roleNames = Sets.newHashSet();
        for (OssRole role : roles) {
        	roleNames.add(role.getName());
		}
        authorizationInfo.setRoles(roleNames);
        authorizationInfo.setStringPermissions(authorityService.findPermissions(userModel.getUserId()));

        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        String username = (String)token.getPrincipal();

        OssUserModel userModel = userDao.getUserByName(username);

        if(userModel == null) {
            throw new UnknownAccountException();//没找到帐号
        }
        
        if (userModel.getUserStatus()  == 0 ) {
        	throw new DisabledAccountException();	//帐号未激活
        }
        

        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
        		userModel.getUserName(), //用户名
        		userModel.getPassword(), //密码
                getName()  //realm name
        );
        return authenticationInfo;
    }
}
