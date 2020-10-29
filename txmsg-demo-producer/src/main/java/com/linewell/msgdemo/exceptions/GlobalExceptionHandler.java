package com.linewell.msgdemo.exceptions;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linewell.msgdemo.common.Result;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Set;


@ControllerAdvice
public class GlobalExceptionHandler {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	/**
	 * 处理bean校验异常
	 *
	 * @param e
	 * @return
	 */
	@ExceptionHandler(BindException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Result handleBusinessException(BindException e) {
		Result resultData = new Result();
		BindingResult bindingResult = e.getBindingResult();
		if (bindingResult.hasErrors()) {
			resultData.setSuccess(Result.FAILURE);
			resultData.setMessage("参数校验失败");

			JSONArray errorArr = new JSONArray();
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				JSONObject errorJSON = new JSONObject();
				errorJSON.put("field", fieldError.getField());
				errorJSON.put("message", fieldError.getDefaultMessage());
				errorJSON.put("code", fieldError.getCode());
				errorArr.add(errorJSON);
			}
			resultData.put("errors", errorArr);
		}
		return resultData;
	}

	/**
	 * 处理@RequestParam参数校验异常
	 *
	 * @param e
	 * @return
	 */
	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Result handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
		Result resultData = new Result();
		resultData.setSuccess(Result.FAILURE);
		resultData.setMessage("请求参数：" + e.getParameterName() + "不能为空");
		return resultData;
	}

	@ExceptionHandler(DuplicateKeyException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Result handleDuplicateKeyException(DuplicateKeyException e) {
		Result resultData = new Result();
		resultData.setSuccess(Result.FAILURE);
		resultData.setMessage("数据库插入异常");
		return resultData;
	}

	/**
	 * 处理方法参数检验异常
	 * 
	 * @param exception
	 * @return
	 */
	@ExceptionHandler
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Result handle(ValidationException exception) {
		Result resultData = new Result();
		resultData.setSuccess(Result.FAILURE);

		if (exception instanceof ConstraintViolationException) {
			JSONArray errorArr = new JSONArray();
			ConstraintViolationException exs = (ConstraintViolationException) exception;
			Set<ConstraintViolation<?>> violations = exs.getConstraintViolations();

			for (ConstraintViolation<?> item : violations) {
				String field = StringUtils.substringAfterLast(item.getPropertyPath().toString(), ".");
				JSONObject errorJSON = new JSONObject();
				errorJSON.put("field", field);
				errorJSON.put("message", item.getMessage());
				errorJSON.put("code", item.getMessageTemplate());
				errorArr.add(errorJSON);
			}
			resultData.put("errors", errorArr);
		}
		return resultData;
	}

	/**
	 * 处理所有不可知异常
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Result handleException(Exception e) {
		e.printStackTrace();
		logger.error(e.getMessage());
		Result resultData = new Result();
		resultData.setSuccess(Result.FAILURE);
		resultData.setMessage(e.getMessage());
		return resultData;
	}
}