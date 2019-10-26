package com.pier.result;

import java.util.HashMap;

public class Result extends HashMap<String, Object> {

	private static final long serialVersionUID = -846833313112782555L;

	public Result() {
		setCode("0");
		setMessage("success");
	}

	public void setCode(String errorCode) {
		put("code", errorCode);
	}

	public void setMessage(String message) {
		put("message", message);
	}

	public void setError(String code, String message) {
		setCode(code);
		setMessage(message);
	}

	public String getCode() {
		return (String)get("code");
	}

	public String getMessage() {
		return (String)get("message");
	}
}
