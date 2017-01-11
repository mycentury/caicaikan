package win.caicaikan.task;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import win.caicaikan.constant.ExecuteStatus;
import win.caicaikan.repository.mongodb.entity.TaskEntity;
import win.caicaikan.service.internal.DaoService;

/**
 * 任务调度 耗时统计 模板方法模式
 * 
 * @author yanwenge
 */
public abstract class TaskTemplete {
	private static final Logger logger = Logger.getLogger(TaskTemplete.class);
	@Autowired
	protected DaoService daoService;

	private TaskEntity task;

	public void execute() {
		logger.info("任务【" + getTaskName() + "】START ");
		Date startDate = new Date();
		try {
			doBeforeTask();
			doInTask();
			doAfterTask();
		} catch (Exception e) {
			logger.error(getTaskName() + " execute error", e);
			task.setInfo(e.getMessage());
			task.setExecuteStatus(ExecuteStatus.FAILED.name());
			task.setUpdateTime(new Date());
			daoService.save(task);
		} finally {
			long second = (new Date().getTime() - startDate.getTime()) / 1000;
			logger.info("任务【" + getTaskName() + "】 END,耗时:" + second / 3600 + "h" + (second / 60) % 60 + "m" + second % 60 + "s");
		}
	}

	public String getTaskName() {
		return this.getClass().getSimpleName();
	}

	protected void doBeforeTask() {
		// 状态检查
		task = daoService.queryById(getTaskName(), TaskEntity.class);
		if (!task.getExecuteStatus().equals(ExecuteStatus.SUCCESS.name())) {
			String message = "任务【" + getTaskName() + "】状态不正确!";
			logger.error(message);
			throw new IllegalStateException(message);
		}
		// 状态更新为RUNNING，类似于加锁
		task.setExecuteStatus(ExecuteStatus.RUNNING.name());
		task.setUpdateTime(new Date());
		daoService.save(task);
	}

	protected void doAfterTask() {
		if (task != null) {
			// 状态更新为SUCCESS，任务成功完成
			task.setExecuteStatus(ExecuteStatus.SUCCESS.name());
			task.setUpdateTime(new Date());
			daoService.save(task);
		}
	}

	protected abstract void doInTask();

}
