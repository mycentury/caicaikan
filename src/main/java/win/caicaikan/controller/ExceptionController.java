/**
 * 
 */
package win.caicaikan.controller;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年12月22日
 * @ClassName ExceptionController
 */
@ControllerAdvice(basePackages = { "win.caicaikan.controller" })
public class ExceptionController {
	private static final Logger logger = Logger.getLogger(ExceptionController.class);

	@ExceptionHandler(value = { IOException.class, RuntimeException.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String exception(Exception exception, WebRequest request) {
		logger.info("Catch an exception", exception);
		return "error/errorPage";
	}

	@ExceptionHandler(value = { NoHandlerFoundException.class })
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String noMapping(Exception exception, WebRequest request) {
		logger.info("No mapping exception", exception);
		return "error/notFound";
	}

}
