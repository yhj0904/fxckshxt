package kr.co.nanwe.rest.service;

import java.util.HashMap;

public class ErrorMessage {
	
	@SuppressWarnings("serial")
	private final HashMap<String, String> errorMsg = new HashMap<String, String>() {
        {
        	put("-1", "요청 처리중 오류가 발생하였습니다.");
        	put("-2", "요청하신 페이지가 존재하지 않습니다.");
        	put("-3", "권한이 없습니다.");        	
        	put("-98", "유효하지 않은 키입니다.");
            put("-99", "등록된 도메인과 요청 도메인이 다릅니다.");
        }
    };

	private String code;
	private String msg;

	public ErrorMessage(String errorCode) {
		this.code = errorCode;
		this.msg = getErrorMsg(errorCode);
	}

	public ErrorMessage(int statusCode) {
		String errorCode = "400";
		switch (statusCode) {
			case 400:
				errorCode = "-1";
				break;
			case 401:
				errorCode = "-3";
				break;
			case 403:
				errorCode = "-1";
				break;
			case 404:
				errorCode = "-2";
				break;
			case 500:
				errorCode = "-1";
				break;
			default:
				errorCode = "-1";
		}
		this.code = errorCode;
		this.msg = getErrorMsg(errorCode);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	private String getErrorMsg(String code) {
		return errorMsg.get(code);
	}
	
}
