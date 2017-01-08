package win.caicaikan.controller.admin;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import win.caicaikan.service.external.AliApiSecurityService;
import win.caicaikan.service.external.ApiConfig;
import win.caicaikan.service.internal.DaoService;
import win.caicaikan.service.internal.InitService;
import win.caicaikan.service.internal.RecordService;
import win.caicaikan.service.internal.SessionService;

@Controller
@RequestMapping("admin")
public class AdminMenuController {
	private final static Logger logger = Logger.getLogger(AdminMenuController.class);

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

}
