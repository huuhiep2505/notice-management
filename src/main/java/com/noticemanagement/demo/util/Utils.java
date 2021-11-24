package com.noticemanagement.demo.util;

import com.noticemanagement.demo.constant.Constants;
import com.noticemanagement.demo.model.JwtRequest;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

/**
 * Helper class, can be reused across the application
 * @Author thanhbc1@fsoft.com.vn
 */
@Service
public class Utils {

  public static JwtRequest sessionFromReq(HttpServletRequest request) throws Exception {
    if (isNotLogin(request)) {
      throw new Exception("User is not logined");
    }
    if (request.getAttribute(Constants.CURRENT_SESISSION) == null) {
      return null;
    }
    return (JwtRequest) request.getAttribute(Constants.CURRENT_SESISSION);
  }

  public static boolean isNotLogin(HttpServletRequest request) {
    if (request.getAttribute(Constants.CHECK_AUTH) == null) {
      return false;
    }
    return !(Boolean) request.getAttribute(Constants.CHECK_AUTH);
  }

}
