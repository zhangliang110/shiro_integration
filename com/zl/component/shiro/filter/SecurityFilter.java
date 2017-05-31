package com.meixun.b2b.mall.component.shiro.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.meixun.b2b.mall.common.constant.Constant;
import com.meixun.b2b.mall.common.constant.ErrorCode;
import com.meixun.b2b.mall.util.ResultUtil;
import com.meixun.b2b.mall.util.WebHelperUtil;
import com.meixun.b2b.mall.web.dao.IOssUserDao;
import com.meixun.b2b.mall.web.model.OssUserModel;

public class SecurityFilter extends FormAuthenticationFilter{
	
	@Autowired
	IOssUserDao ossUserDao;
	
	
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		
		HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        
        if(this.isLoginRequest(request, response)) {
			if (this.isLoginSubmission(request, response)) {
				// form表单提交
				ResultUtil loginInfo = login(req); // 登录
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/json; charset=utf-8");
				ServletOutputStream out = response.getOutputStream();
				out.write(JSON.toJSONString(loginInfo).getBytes());
				out.close();
				return false;
			}
			if ("XMLHttpRequest".equalsIgnoreCase(req.getHeader("x-requested-with"))) {
				// get ajax转发请求
				response.setContentType("application/json;charset=" + Constant.DEFAULT_CHARSET);
				ServletOutputStream out = response.getOutputStream();
				out.write(JSON.toJSONString(ErrorCode.LOGIN_TIME_OUT.createResponse())
						.getBytes(Constant.DEFAULT_CHARSET));
				out.close();
				return false;
			}
			
			//对请求做跳转操作
			return requestForward(req, resp);
			
        } else {//保存当前地址并重定向到登录界面
            this.saveRequestAndRedirectToLogin(req, resp);
            return false;
        }
	}

	private boolean requestForward(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, UnsupportedEncodingException {
		SavedRequest savedRequest = WebUtils.getAndClearSavedRequest(req);
		if (savedRequest == null || savedRequest.getMethod().equalsIgnoreCase("post")) {
			return true;	//保存请求为空，或者为post不做处理  请求直接跳转到登陆页面
		} else {
			String referer = savedRequest.getRequestUrl();
			resp.sendRedirect(req.getContextPath() + "/common/login?url=" + URLEncoder.encode(referer, Constant.DEFAULT_CHARSET));
			return false;
		}
	}

	private ResultUtil login(HttpServletRequest req) {
		// 表单提交登录
		String username = req.getParameter("userInfo");
		String password = req.getParameter("password");
		String rella = req.getParameter("rella");

		String sessionPicCode = String.valueOf(req.getSession().getAttribute(Constant.SESSION_VERIFY_PIC_KEY));
		if (StringUtils.isEmpty(sessionPicCode) || !sessionPicCode.equalsIgnoreCase(rella)) {
			return ErrorCode.INVALID_VERIFY_CODE.createResponse();
		}
		try {
			SecurityUtils.getSubject()
					.login(new UsernamePasswordToken(username, WebHelperUtil.encryptPassword(password)));
		} catch (DisabledAccountException e) {
			return ErrorCode.USER_NOT_ACTIVE.createResponse();
		} catch (UnknownAccountException e) {
			return ErrorCode.USER_NOT_EXISTS.createResponse();
		} catch (IncorrectCredentialsException e) {
			return ErrorCode.WRONG_NAME_OR_PSW.createResponse();
		}
		OssUserModel userModel = ossUserDao.getUserByName(username);
		req.getSession().setAttribute(Constant.SESSION_USER_KEY, userModel.getUserId());

		OssUserModel model = new OssUserModel();
		model.setUserId(userModel.getUserId());
		model.setLastLoginDate(new Date());
		model.setLastLoginIp(WebHelperUtil.getRemoteHost(req));
		ossUserDao.updateUserById(model);
		return ErrorCode.SUCCESS.createResponse();
	}
}
