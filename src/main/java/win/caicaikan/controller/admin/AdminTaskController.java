package win.caicaikan.controller.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import win.caicaikan.annotation.Role;
import win.caicaikan.api.res.Result;
import win.caicaikan.constant.OperType;
import win.caicaikan.constant.ResultType;
import win.caicaikan.constant.RoleType;
import win.caicaikan.repository.mongodb.entity.RecordEntity;
import win.caicaikan.repository.mongodb.entity.TaskEntity;
import win.caicaikan.service.external.AliApiSecurityService;
import win.caicaikan.service.external.ApiConfig;
import win.caicaikan.service.internal.DaoService;
import win.caicaikan.service.internal.InitService;
import win.caicaikan.service.internal.RecordService;
import win.caicaikan.service.internal.SessionService;
import win.caicaikan.task.TaskTemplete;
import win.caicaikan.util.SpringContextUtil;

@Controller
@RequestMapping("admin")
public class AdminTaskController {
	private final static Logger logger = Logger.getLogger(AdminTaskController.class);

	@Autowired
	private RecordService recordService;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private InitService initService;
	@Autowired
	private AliApiSecurityService aliApiSecurityService;
	@Autowired
	private ApiConfig apiConfig;
	@Autowired
	private DaoService daoService;

	@RequestMapping("task")
	@Role({ RoleType.ADMIN })
	public String index(HttpServletRequest request, ModelMap map) {
		if (!sessionService.hasLogin(request)) {
			return "redirect:/admin/login";
		}
		if (!sessionService.isAdmin(request)) {
			return "redirect:/user/login";
		}
		List<TaskEntity> tasks = daoService.query(null, TaskEntity.class);
		map.put("tasks", tasks);
		return "admin/task";
	}

	@RequestMapping("task/execute")
	@Role({ RoleType.ADMIN })
	public @ResponseBody Result<Boolean> execute(HttpServletRequest request, String id, ModelMap map) {
		RecordEntity recordEntity = recordService.assembleRocordEntity(request);
		recordEntity.setOpertype(OperType.EXECUTE_TASK.name());
		Result<Boolean> result = new Result<Boolean>();
		if (StringUtils.isEmpty(id)) {
			result.setResultStatusAndMsg(ResultType.PARAMETER_ERROR, "id不能为空");
			recordEntity.setAfter(result.getMessage());
			recordService.insert(recordEntity);
			return result;
		}
		if (!sessionService.hasLogin(request)) {
			result.setResultStatusAndMsg(ResultType.NOT_LOGIN, null);
			recordEntity.setAfter(result.getMessage());
			recordService.insert(recordEntity);
			return result;
		}
		if (!sessionService.isAdmin(request)) {
			result.setResultStatusAndMsg(ResultType.NO_AUTHORITY, null);
			recordEntity.setAfter(result.getMessage());
			recordService.insert(recordEntity);
			return result;
		}

		TaskTemplete executor = null;
		Map<String, TaskTemplete> beans = SpringContextUtil.getBeans(TaskTemplete.class);
		for (TaskTemplete templete : beans.values()) {
			if (id.equals(templete.getTaskName())) {
				executor = templete;
				break;
			}
		}

		if (executor == null) {
			result.setResultStatusAndMsg(ResultType.PARAMETER_ERROR, "id无法匹配");
			recordEntity.setAfter(result.getMessage());
			recordService.insert(recordEntity);
			return result;
		}
		try {
			final TaskTemplete taskTemplete = executor;
			new Thread(new Runnable() {
				@Override
				public void run() {
					taskTemplete.execute();
				}
			}).start();
			result.setResultStatusAndMsg(ResultType.SUCCESS, "已经开始执行！");
			recordEntity.setAfter(result.getMessage());
			recordService.insert(recordEntity);
		} catch (Exception e) {
			result.setResultStatusAndMsg(ResultType.SERVICE_ERROR, null);
			recordEntity.setAfter(result.getMessage());
			recordService.insert(recordEntity);
		}
		logger.info("请求：" + recordEntity.getParam() + ",响应：" + result.getMessage());
		return result;
	}
}
