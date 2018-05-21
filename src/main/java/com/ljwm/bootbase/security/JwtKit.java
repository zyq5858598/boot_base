package com.ljwm.bootbase.security;

import cn.hutool.core.date.DateUtil;
import com.ljwm.bootbase.dto.Kv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * JKhaled created by yunqisong@foxmail.com 2017/11/21
 * FOR : JWT Token构造工具
 */
@SuppressWarnings("unchecked")
@Slf4j
public class JwtKit {
  /**
   * 构造函数私有化
   */
  private static final Map<String, Claims> tokenStore = new HashMap<>();

  private static final String CLAIM_KEY_USERNAME = "sub";

  private static final String CLAIM_KEY_CREATED = "created";

  private static final String LOGIN_TYPE = "loginType";

  /**
   * 加密令牌的私钥，默认值由配置文件给出
   */
  private static String secret;
  /**
   * 令牌Token的有效时长，单位秒，默认值由配置文件给出
   */

  private static Long expiration;
  /**
   * Token前缀,由系统配置文件给出
   */

  private static String tokenPre;


  public static void init(String secret, Long expiration, String tokenPre) {
    JwtKit.secret = secret;
    JwtKit.expiration = expiration;
    JwtKit.tokenPre = tokenPre;
  }

  /**
   * 对外暴露的核心方法，获取token中的account信息
   *
   * @param authToken
   * @return
   */
  public static String getUsernameFormToken(String authToken) {

    String account;

    try {
      Claims claims = getClaimsFromToken(authToken);
      account = claims.getSubject();
    } catch (Exception e) {
      account = null;
    }
    return account;
  }

  /**
   * 对外暴露的核心方法，获取token中的account信息
   *
   * @param authToken
   * @return
   */
  public static String getLoginTypeFormToken(String authToken) {

    String loginType;

    try {
      Claims claims = getClaimsFromToken(authToken);
      loginType = (String) claims.get(LOGIN_TYPE);
    } catch (Exception e) {
      loginType = null;
    }
    return loginType;
  }

  /**
   * 将token信息解析出来对应的 Claims
   *
   * @param token
   * @return
   */
  private static Claims getClaimsFromToken(String token) {

    Claims claims;
    try {
      claims = tokenStore.get(token);
      if (claims == null) {
        Claims temp = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        tokenStore.put(token, temp);
        claims = temp;
      }
    } catch (Exception e) {
      claims = null;
    }
    return claims;
  }

  /**
   * 获取Token的过期日期
   *
   * @param token
   * @return
   */
  public static Date getExpirationDateFromToken(String token) {

    Date expiration;
    try {
      Claims claims = getClaimsFromToken(token);
      expiration = claims.getExpiration();
    } catch (Exception e) {
      expiration = null;
    }
    return expiration;
  }

  public static Map<String, String> getExtInfoFormToken(String token) {

    Map<String, String> expiration = Kv.create();
    try {
      Claims claims = getClaimsFromToken(token);
      for (String k : claims.keySet()) {
        if (!k.equals(CLAIM_KEY_USERNAME) && !k.equals(LOGIN_TYPE) && !k.equals(CLAIM_KEY_CREATED) &&  claims.get(k) != null) {
          log.info("变量 :{}  值:{}",k,String.valueOf(claims.get(k)));
          expiration.put(k, String.valueOf(claims.get(k)));
        }
      }
    } catch (Exception e) {
      log.info("Token转化错误!\n {}",e);
      expiration = Kv.create();
    }
    return expiration;
  }

  /**
   * 获取Token的创建时间
   *
   * @param token
   * @return
   */
  private static Date getCreatedFormToken(String token) {
    Date created;
    try {
      Claims claims = getClaimsFromToken(token);
      created = DateUtil.date((Long) claims.get(CLAIM_KEY_CREATED));
    } catch (Exception e) {
      created = null;
    }
    return created;
  }

  /**
   * 判断Token是否过期
   *
   * @param token
   * @return
   */
  private static Boolean isTokenExpired(String token) {
    Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  /**
   * 验证一个Token是否合法
   *
   * @param token
   * @param iJwtAndSecurityAble
   * @return
   */
  public static Boolean validateToken(String token, IJwtAndSecurityAble iJwtAndSecurityAble) {
    Date created = getCreatedFormToken(token);

    return !isTokenExpired(token) &&    // 确保 Token没有过期  如果   userDetails.getLastModifyPassTime() == null  为当前时间
      (iJwtAndSecurityAble.getLastModifyPasswordTime() != null && iJwtAndSecurityAble.getLastModifyPasswordTime().before(created)); // 确保创建修改密码时间在这token创建之前
  }


  /**
   * 根据用户信息生成token
   *
   * @param iJwtAndSecurityAble 根据详情创建Token
   * @return 根据详情创建Token
   */
  public static String generateToken(IJwtAndSecurityAble iJwtAndSecurityAble) {

    return tokenPre + Jwts.builder()
      .setClaims(
        Kv.by(CLAIM_KEY_USERNAME, iJwtAndSecurityAble.getUsername())
          .set(CLAIM_KEY_CREATED, DateUtil.date())
          .set(LOGIN_TYPE, iJwtAndSecurityAble.getLoginType())
          .set(iJwtAndSecurityAble.extInfo())   // 填充额外信息
      )
      .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
      .signWith(SignatureAlgorithm.HS512, secret)
      .compact();
  }


}
