package com.eversec.lte.exception;

/**
 * 用户标记解析错误
 * 
 */
public class UnkownProcessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UnkownProcessException() {
		super();
	}

	public UnkownProcessException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UnkownProcessException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnkownProcessException(String message) {
		super(message);
	}

	public UnkownProcessException(Throwable cause) {
		super(cause);
	}

}
