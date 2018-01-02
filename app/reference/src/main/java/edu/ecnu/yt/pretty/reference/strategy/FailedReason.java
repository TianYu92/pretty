/**
 * Alipay.com Inc. Copyright (c) 2004-2017 All Rights Reserved.
 */
package edu.ecnu.yt.pretty.reference.strategy;

/**
 *
 * @author yt
 * @version $Id: FailedReason.java, v 0.1 2017年12月15日 下午1:43 yt Exp $
 */
public class FailedReason {

    private final String message;
    private final Exception exception;

    public FailedReason(String message, Exception exception) {
        this.message = message;
        this.exception = exception;
    }

    /**
     * Getter method for property <tt>message</tt>.
     *
     * @return property value of message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Getter method for property <tt>exception</tt>.
     *
     * @return property value of exception
     */
    public Exception getException() {
        return exception;
    }
}