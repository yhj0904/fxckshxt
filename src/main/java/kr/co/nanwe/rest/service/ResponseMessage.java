package kr.co.nanwe.rest.service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseMessage {
	
    private Object data;
    private String code;
    private String msg;
    
    public void setSuccess(Object result) {
    	this.code = "0";
    	this.data = result;
    }
    
    public void setError(String errorCode) {
    	ErrorMessage error = new ErrorMessage(errorCode);
    	this.code = error.getCode();
    	this.msg= error.getMsg();
	}
    
    public void setError(int statusCode) {
    	ErrorMessage error = new ErrorMessage(statusCode);
    	this.code = error.getCode();
    	this.msg= error.getMsg();
	}
}
