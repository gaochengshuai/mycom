package com.gaocs.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author gaocs
 * @PackageName:com.crt.loan.app.kzx.domain
 * @Description:
 * @time:2020/11/3 18:22
 */
public class JwtToken {
	/** 公共加密变量，防止被攻击 */
	private static final String secert = "gaocs123456";
	public static String createToken(){
		//签发时间
		Date initDate = new Date();
		Calendar nowTime = Calendar.getInstance();
		nowTime.add(Calendar.MINUTE,1);
		Date expiresDate = nowTime.getTime();

		Map<String,Object> map = new HashMap <>();
		map.put("typ","JWT");
		map.put("alg","HS256");
		String token = JWT.create()
				.withHeader(map) // header
				.withClaim("name","测试") //payload
				.withClaim("id","admin")
				.withClaim("org","mr.gao")
				.withExpiresAt(expiresDate) //设置过期时间 过期时间大于签发时间
				.withIssuedAt(initDate) //签发时间
				.sign(Algorithm.HMAC256(secert));//加密
		return token;
	}
	/**
	 * 解密 token
	 * @param token
	 * */
	public static Map<String, Claim> verifyToken(String token){
		JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secert)).build();
		DecodedJWT jwt = null;
		try {
			jwt = jwtVerifier.verify(token);
		} catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException("令牌未校验通过，请重新登录");
		}
		return jwt.getClaims();
	}

	public static void main(String[] args) {
		String token = JwtToken.createToken();
		System.out.println("token:"+token);
		Map<String,Claim> claim = JwtToken.verifyToken(token);
		System.out.println(claim.get("name").asString());
		System.out.println(claim.get("id").asString());
		System.out.println(claim.get("org").asString() == null ? null : claim.get("org").asString());

		Map<String,Claim> claimExpire = JwtToken.verifyToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJvcmciOiJtci5nYW8iLCJuYW1lIjoi5rWL6K-VIiwiaWQiOiJhZG1pbiIsImV4cCI6MTYwNDQ1NTAyOSwiaWF0IjoxNjA0NDU0OTY5fQ.IBOlatBuEZHOaKHB-U4b3xZynrJCHtGkyc3f-2a4Pto");
		System.out.println(claimExpire.get("name").asString());
		System.out.println(claimExpire.get("id").asString());
		System.out.println(claimExpire.get("org").asString() == null ? null : claim.get("org").asString());
	}
}
