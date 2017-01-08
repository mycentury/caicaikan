package win.caicaikan.service.internal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import win.caicaikan.constant.RoleType;

@Service
public class SessionService {
	public boolean hasLogin(HttpServletRequest request) {
		return !StringUtils.isEmpty(request.getSession().getAttribute("username"));
	}

	public String getUsertype(HttpServletRequest request) {
		Object usertype = request.getSession().getAttribute("usertype");
		if (usertype == null) {
			return RoleType.VISITOR.getCode();
		}
		return usertype.toString();
	}

	public boolean isAdmin(HttpServletRequest request) {
		return "A".equals(request.getSession().getAttribute("usertype"));
	}

	public void setUsername(HttpServletRequest request, String username) {
		request.getSession().setAttribute("username", username);
	}

	public void setUsertype(HttpServletRequest request, String usertype) {
		request.getSession().setAttribute("usertype", usertype);
	}

	public void removeUsername(HttpServletRequest request) {
		request.getSession().removeAttribute("username");
	}

	public void removeUsertype(HttpServletRequest request) {
		request.getSession().removeAttribute("usertype");
	}

	public void setCheckcode(HttpServletRequest request, String checkcode) {
		request.getSession().setAttribute("checkcode", checkcode);
	}

	public String getCheckcode(HttpServletRequest request) {
		Object checkcode = request.getSession().getAttribute("checkcode");
		return checkcode == null ? null : checkcode.toString();
	}
}
