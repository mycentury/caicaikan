package win.caicaikan.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

public class BaseController {
	protected boolean hasLogin(HttpServletRequest request) {
		return !StringUtils.isEmpty(request.getSession().getAttribute("username"));
	}
}
