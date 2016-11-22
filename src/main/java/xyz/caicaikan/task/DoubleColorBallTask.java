/**
 * 
 */
package xyz.caicaikan.task;

import org.springframework.beans.factory.annotation.Autowired;

import xyz.caicaikan.service.DoubleColorBallService;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年11月21日
 * @ClassName DoubleColorBallTask
 */
public class DoubleColorBallTask extends TaskTemplete {

	@Autowired
	private DoubleColorBallService doubleColorBallService;

	@Override
	public void run() throws Throwable {
	}
}
