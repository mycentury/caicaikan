/**
 * 
 */
package win.caicaikan.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import win.caicaikan.annotation.Role;
import win.caicaikan.api.res.Result;
import win.caicaikan.constant.ResultType;
import win.caicaikan.constant.RoleType;
import win.caicaikan.service.internal.SessionService;

import com.google.gson.Gson;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年12月8日
 * @ClassName MenuInterceptor
 */
public class RoleInterceptor implements HandlerInterceptor {
	private final static Logger logger = Logger.getLogger(RoleInterceptor.class);
	@Autowired
	private SessionService sessionService;

	/**
	 * preHandle方法是进行处理器拦截用的，顾名思义，该方法将在Controller处理之前进行调用， SpringMVC中的Interceptor拦截器是链式的，可以同时存在
	 * 多个Interceptor，然后SpringMVC会根据声明的前后顺序一个接一个的执行 ，而且所有的Interceptor中的preHandle方法都会在
	 * Controller方法调用之前调用。SpringMVC的这种Interceptor链式结构也是可以进行中断的 ，这种中断方式是令preHandle的返
	 * 回值为false，当preHandle的返回值为false的时候整个请求就结束了。
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			Role role = method.getAnnotation(Role.class);
			boolean hasAuth = true;
			RoleType lowestRoleType = null;
			if (role != null) {
				hasAuth = false;
				RoleType[] roleTypes = role.value();
				for (RoleType roleType : roleTypes) {
					if (roleType.getCode().equals(sessionService.getUsertype(request))) {
						hasAuth = true;
						break;
					}

					if (lowestRoleType == null || roleType.getPriority() < lowestRoleType.getPriority()) {
						lowestRoleType = roleType;
					}
				}
			}
			if (!hasAuth) {
				if ("XMLHttpRequest".equalsIgnoreCase(request.getHeader("x-requested-with"))) {
					Result<Boolean> result = new Result<Boolean>();
					result.setResultStatusAndMsg(ResultType.NO_AUTHORITY, null);
					response.setCharacterEncoding("UTF-8");
					response.setContentType("application/json; charset=utf-8");
					response.getWriter().write(new Gson().toJson(result));
				} else {
					String priUrl = request.getHeader("Referer");
					String url = lowestRoleType == RoleType.ADMIN ? "/admin/login" : "/user/login";
					response.sendRedirect(url);
				}
				return false;
			}
		}
		return true;

	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	}

	/**
	 * 该方法也是需要当前对应的Interceptor的preHandle方法的返回值为true时才会执行。该方法将在整个请求完成之后， 也就是DispatcherServlet渲染了视图执行，
	 * 这个方法的主要作用是用于清理资源的，当然这个方法也只能在当前这个Interceptor的preHandle方法的返回值为true时才会执行。
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

	}
}