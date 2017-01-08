package win.caicaikan.controller.admin;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import win.caicaikan.annotation.Role;
import win.caicaikan.api.res.Result;
import win.caicaikan.constant.ResultType;
import win.caicaikan.constant.RoleType;
import win.caicaikan.service.internal.InitService;
import win.caicaikan.service.internal.RecordService;
import win.caicaikan.service.internal.SessionService;

@Controller
@RequestMapping("admin")
public class AdminInitController {
	private final static Logger logger = Logger.getLogger(AdminInitController.class);

	@Autowired
	private RecordService recordService;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private InitService initService;

	@RequestMapping("init")
	@Role({ RoleType.ADMIN })
	public String index(HttpServletRequest request, ModelMap map) {
		return "admin/init";
	}

	@RequestMapping("predicts")
	@Role({ RoleType.ADMIN })
	public @ResponseBody Result<Boolean> initSsqPredicts(HttpServletRequest request, Integer terms) {
		Result<Boolean> result = new Result<Boolean>();
		if (!sessionService.hasLogin(request)) {
			result.setResultStatusAndMsg(ResultType.NOT_LOGIN, null);
			return result;
		}
		if (!sessionService.isAdmin(request)) {
			result.setResultStatusAndMsg(ResultType.NO_AUTHORITY, null);
			return result;
		}
		initService.initSsqPredicts(terms);
		result.setResultStatusAndMsg(ResultType.SUCCESS, "执行成功！");
		return result;
	}

	@RequestMapping("positions")
	@Role({ RoleType.ADMIN })
	public @ResponseBody Result<Boolean> initPredictRightPositions(HttpServletRequest request) {
		Result<Boolean> result = new Result<Boolean>();
		if (!sessionService.hasLogin(request)) {
			result.setResultStatusAndMsg(ResultType.NOT_LOGIN, null);
			return result;
		}
		if (!sessionService.isAdmin(request)) {
			result.setResultStatusAndMsg(ResultType.NO_AUTHORITY, null);
			return result;
		}
		initService.initPredictRightPositions();
		result.setResultStatusAndMsg(ResultType.SUCCESS, "保存成功！");
		return result;
	}
}
