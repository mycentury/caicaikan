/**
 * 
 */
package win.caicaikan.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import win.caicaikan.constant.Constant;
import win.caicaikan.service.internal.SessionService;
import win.caicaikan.util.CheckCodeUtil;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年11月25日
 * @ClassName LotterySsqController
 */
/**
 * @author yanwenge
 */
@Controller
public class CheckCodeController extends SessionService {
	@RequestMapping("/checkcode")
	public void checkCode(HttpServletRequest request, HttpServletResponse response) {
		String checkcode = CheckCodeUtil.getRandomCode(Constant.CHECK_CODE, 4);
		super.setCheckcode(request, checkcode);
		BufferedImage image = CheckCodeUtil.getCheckCodeImg(checkcode, 60, 22);
		try {
			ImageIO.write(image, Constant.IMAGE_TYPE, response.getOutputStream());
			response.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
