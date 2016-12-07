package win.caicaikan.task;

import java.util.Date;

import org.apache.log4j.Logger;

/**
 * 任务调度 耗时统计 模板方法模式
 * 
 * @author yanwenge
 */
public abstract class TaskTemplete {
	private static final Logger logger = Logger.getLogger(TaskTemplete.class);

	protected void execute() {
		Date startDate = new Date();
		String taskName = null;
		try {
			taskName = getTaskName();
			logger.info("任务【" + taskName + "】START ");
			run();
			Thread.sleep(1000 * 5);// 休眠5s
		} catch (Throwable e) {
			logger.error(taskName + " execute error", e);
		} finally {
			long second = (new Date().getTime() - startDate.getTime()) / 1000;
			logger.info("任务【" + taskName + "】 END,耗时:" + second / 3600 + "h" + (second / 60) % 60
					+ "m" + second % 60 + "s");
		}
	}

	/**
	 * 初始化<br>
	 * 设置任务名称<br>
	 */
	protected String getTaskName() {
		return this.getClass().getSimpleName();
	}

	/**
	 * 执行任务
	 * 
	 * @throws Throwable
	 */
	public abstract void run() throws Throwable;

}
