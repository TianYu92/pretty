/**
 * Alipay.com Inc. Copyright (c) 2004-2017 All Rights Reserved.
 */
package edu.ecnu.yt.pretty.common.service;

/**
 *
 * @author yt
 * @version $Id: Callback.java, v 0.1 2017年12月16日 下午4:54 yt Exp $
 */
public interface Callback<T> {
    void accept(T result);
}