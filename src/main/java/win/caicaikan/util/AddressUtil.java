package win.caicaikan.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

public class AddressUtil {
	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (StringUtils.isEmpty(ip) || ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(ip) || ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(ip) || ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (StringUtils.isEmpty(ip) || ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (StringUtils.isEmpty(ip) || ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getHeader("X_Real_IP");
		}
		if (StringUtils.isEmpty(ip) || ("unknown".equalsIgnoreCase(ip))) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}
