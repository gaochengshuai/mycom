package com.gaocs.common.aop;

import com.gaocs.base.BaseOutputDTO;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ：gaocs
 * @date ：Created in 2020/4/24 14:34
 * @description：过滤拦截无用的token
 * @modified By：
 * @version: $
 */
@Component
@Aspect
public class TokenCheckAspect {

	/* 日志对象初始化 */
	private static Logger logger = LogManager.getLogger(TokenCheckAspect.class);

	@Pointcut("execution(public Object com.gaocs.controller.*.*(..))")
	public void TokenCheckAspect(){}

	@Around("TokenCheckAspect() &&( @annotation(org.springframework.web.bind.annotation.RequestMapping) || @annotation(org.springframework.web.bind.annotation.PostMapping))")
	public Object aroud(ProceedingJoinPoint joinPoint){
		BaseOutputDTO outDTO = new BaseOutputDTO();
		logger.info("AOP：校验tokenCode");
		try {
			//1.获取到所有的参数值的数组
			Object[] args = joinPoint.getArgs();
			Signature signature = joinPoint.getSignature();
			MethodSignature methodSignature = (MethodSignature) signature;
			//2.通过这获取到方法的所有参数名称的字符串数组
			String[] parameterNames = methodSignature.getParameterNames();
			//3.获取的参数名称的下标获取到对应的值
			int tokenCodeIndex = ArrayUtils.indexOf(parameterNames, "tokenCode");
			int requestIndex = ArrayUtils.indexOf(parameterNames, "request");
			logger.info("tokenCodeIndex:" + tokenCodeIndex + ",requestIndex:" + requestIndex);
			String tokenCodeValue = null;
			if (tokenCodeIndex !=-1){
				tokenCodeValue = (String) args[tokenCodeIndex];
			}else if (requestIndex !=-1){
				HttpServletRequest request = (HttpServletRequest) args[requestIndex];
				tokenCodeValue = request.getParameter("tokenCode");
			}
			if (tokenCodeValue !=null){
				logger.info("获取参数tokenCode:" + tokenCodeValue);
				//如果token为空字符串，返回
				if ("".equals(tokenCodeValue.trim())){
					outDTO.setCode("nullToken");
					outDTO.setMsg("tokenCode为空字符串");
					return outDTO;
				}
				String jsons = tokenCodeValue;
				logger.info("tokenCode获取结果:" + jsons);
				if(jsons == null) {
					outDTO.setCode("999999");
					outDTO.setMsg("参数为空");
					return outDTO;
				}
			}
			return joinPoint.proceed();
		}catch (Throwable throwable) {
			logger.error("tokenCode校验异常,{},{}" ,throwable.getMessage(),throwable);
			outDTO.setCode("error");
			outDTO.setMsg("系统异常");
			return outDTO;
		}
	}
}
