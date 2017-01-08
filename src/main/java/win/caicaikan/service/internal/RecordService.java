package win.caicaikan.service.internal;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import win.caicaikan.constant.OperType;
import win.caicaikan.repository.mongodb.entity.RecordEntity;
import win.caicaikan.util.AddressUtil;

@Service
public class RecordService {
	@Autowired
	private DaoService daoService;

	@Value("${log.record.open}")
	private boolean openRecord;

	public void insert(RecordEntity record) {
		if (!openRecord) {
			return;
		}
		Date createTime = new Date();
		record.setCreateTime(createTime);
		record.setUpdateTime(createTime);
		daoService.insert(record);
	}

	/**
	 * @param request
	 * @return
	 */
	public RecordEntity assembleRocordEntity(HttpServletRequest request) {
		RecordEntity record = new RecordEntity();
		if (!openRecord) {
			return record;
		}
		record.setOpertype(OperType.REQUEST.name());
		record.setUsername(String.valueOf(request.getSession().getAttribute("username")));
		record.setUsertype(String.valueOf(request.getSession().getAttribute("usertype")));
		if (StringUtils.isEmpty(record.getUsername())) {
			record.setUsername(String.valueOf(request.getAttribute("username")));
			record.setUsertype(String.valueOf(request.getAttribute("usertype")));
		}
		record.setBefore(String.valueOf(request.getHeader("Referer")));
		record.setAfter(String.valueOf(request.getRequestURL()));
		record.setIp(AddressUtil.getIpAddress(request));
		return record;
	}
}
