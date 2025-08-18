package com.bbs.njtech.config;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

//	 注册 Sa-Token 拦截器，打开注解式鉴权功能
//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**");
//	}



	/**
	 * 注册 Sa-Token 拦截器，打开注解式鉴权功能
	 * 如果在高版本 SpringBoot (≥2.6.x) 下注册拦截器失效，则需要额外添加 @EnableWebMvc 注解才可以使用
	 * @param registry
	 */
	@Override

	public void addInterceptors(InterceptorRegistry registry) {
		// 注册路由拦截器，自定义认证规则
		registry.addInterceptor(new SaInterceptor(handler -> {
					// 登录认证 -- 拦截所有路由，并排除/user/doLogin 用于开放登录
					SaRouter.match("/**", "/user/doLogin", r -> StpUtil.checkLogin());
					// 角色认证 -- 拦截以 admin 开头的路由，必须具备 admin 角色或者 super-admin 角色才可以通过认证
//					SaRouter.match("/admin/**", r -> StpUtil.checkRoleOr("admin", "super-admin"));
					// 权限认证 -- 不同模块认证不同权限
//					SaRouter.match("/user/**", r -> StpUtil.checkRole("user"));
//					SaRouter.match("/admin/**", r -> StpUtil.checkPermission("admin"));
					// 甚至你可以随意的写一个打印语句
//					SaRouter.match("/user/**", r -> System.out.println("--------权限认证成功-------"));

                    System.out.println("-------- 前端访问path：" + SaHolder.getRequest().getRequestPath());

				}).isAnnotation(true))
				//拦截所有以api开头的接口
				.addPathPatterns("/**")
				//不拦截/user/doLogin登录接口
				.excludePathPatterns("/user/doLogin",
						"/postMessage/findLatestPostMessageByPage",
						"/postMessage/findHotPostMessageByPage",
						"/postMessage/findActivityPostMessageByPage",
						"/postMessage/findServicePostMessageByPage",


						"/postMessage/findCategory",
						"/postMessage/findPostMessageBySubcategoryOrBySearchTextByPage",
						"/postMessage/postMessageDetectEffective",
						"/postMessage/findPostMessageById",
						"/postMessage/findCommentByPage",
						"/postMessage/findCommentById",
						"/postMessage/findAllSubCommentByCommentId",
						"/postMessage/findAllMyPostMessage",
						"/postMessage/findAllOtherPostMessage",
						"/postMessage/findAllMyCommentByPage",

						"/user/isLogin",
						"/user/findOtherUserById",
						"/user/getOpenidAndSessionKey",
						"/user/findUserById",
						"/wx/getMessageDeliveryToken",
						"/systemSetting/**"
				).excludePathPatterns(
						"/message/**"
				);

	}
	@Bean
	public SaServletFilter getSaServletFilter() {
		return new SaServletFilter()

				// 指定 [拦截路由] 与 [放行路由]
				.addInclude("/**").addExclude("/favicon.ico")

				// 认证函数: 每次请求执行
				.setAuth(obj -> {
					SaManager.getLog().debug("----- 请求path={}  提交token={}", SaHolder.getRequest().getRequestPath(), StpUtil.getTokenValue());
					// ...
				})

				// 异常处理函数：每次认证函数发生异常时执行此函数
				.setError(e -> {
					return SaResult.error(e.getMessage());
				})

				// 前置函数：在每次认证函数之前执行
				.setBeforeAuth(obj -> {
					SaHolder.getResponse()

							// ---------- 设置跨域响应头 ----------
							// 允许指定域访问跨域资源
							.setHeader("Access-Control-Allow-Origin", "*")
							// 允许所有请求方式
							.setHeader("Access-Control-Allow-Methods", "*")
							// 允许的header参数
							.setHeader("Access-Control-Allow-Headers", "*")
							// 有效时间
							.setHeader("Access-Control-Max-Age", "3600")
					;

					// 如果是预检请求，则立即返回到前端
					SaRouter.match(SaHttpMethod.OPTIONS)
							.free(r -> System.out.println("--------OPTIONS预检请求，不做处理"))
							.back();
				})
				;
	}

}
