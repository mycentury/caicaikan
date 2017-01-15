/**
 * 
 */
package win.caicaikan.controller;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年12月22日
 * @ClassName ExceptionController
 */
@ControllerAdvice(basePackages = { "win.caicaikan.controller" })
public class ExceptionController {
	private static final Logger logger = Logger.getLogger(ExceptionController.class);

	@ExceptionHandler(value = { Exception.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String serverError(Exception exception, WebRequest request) {
		logger.info("BAD_GATEWAY", exception);
		return "error/500";
	}

	@ResponseStatus(HttpStatus.BAD_GATEWAY)
	public String badGateway(Exception exception, WebRequest request) {
		logger.info("BAD_GATEWAY", exception);
		return "error/502";
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String badRequest(Exception exception, WebRequest request) {
		logger.info("BAD_REQUEST", exception);
		return "error/400";
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	public String forbidden(Exception exception, WebRequest request) {
		logger.info("FORBIDDEN", exception);
		return "error/403";
	}

	// @ExceptionHandler(value = { Exception.class })
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String notFound(Exception exception, WebRequest request) {
		logger.info("No mapping exception", exception);
		return "error/404";
	}
}
