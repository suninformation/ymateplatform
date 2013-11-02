/*
 * Copyright 2007-2107 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ymate.platform.mvc.web.support;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import net.ymate.platform.commons.codec.Base64Codec;
import net.ymate.platform.commons.codec.DESCodec;
import net.ymate.platform.commons.codec.MD5Codec;
import net.ymate.platform.commons.lang.BlurObject;
import net.ymate.platform.mvc.web.WebMVC;
import net.ymate.platform.mvc.web.context.WebContext;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * CookieHelper
 * </p>
 * <p>
 * Cookies操作助手，可以有效避免Cookie取值问题，同时支持Cookie加密；
 * </p>
 * 
 * @author 刘镇(suninformation@163.com)
 * @version 0.0.0
 *          <table style="border:1px solid gray;">
 *          <tr>
 *          <th width="100px">版本号</th><th width="100px">动作</th><th
 *          width="100px">修改人</th><th width="100px">修改时间</th>
 *          </tr>
 *          <!-- 以 Table 方式书写修改历史 -->
 *          <tr>
 *          <td>0.0.0</td>
 *          <td>创建类</td>
 *          <td>刘镇</td>
 *          <td>2011-6-10下午03:50:53</td>
 *          </tr>
 *          </table>
 */
public class CookieHelper {

	private HttpServletRequest __request;

	/**
	 * 是否使用密钥加密
	 */
	private boolean __useAuthKey;

	/**
	 * 是否使用Base64编码
	 */
	private boolean __useBase64;

	private boolean __useEncodeUrl;

	/**
	 * 加密密钥
	 */
	private String __cookieKey = null;
	private DESCodec __codecHelper = null;

	/**
	 * 构造器
	 */
	private CookieHelper() {
		this.__request = WebContext.getRequest();
	}

	/**
	 * @return 构建Cookies操作助手类实例
	 */
	public static CookieHelper create() {
		return new CookieHelper();
	}

	private Cookie __doGetCookie(String cookieName) {
		Cookie _cookies[] = this.__request.getCookies();
		if (_cookies == null) {
			return null;
		} else {
			for (int i = 0; i < _cookies.length; i++) {
				Cookie _cookie = _cookies[i];
				String name = _cookie.getName();
				if (name.equals(cookieName)) {
					return _cookie;
				}
			}
			return null;
		}
	}

	/**
	 * @param key 键
	 * @return 获取Cookie
	 */
	public BlurObject getCookie(String key) {
		Cookie _c = __doGetCookie(WebMVC.getConfig().getCookiePrefix() + key);
		if (_c != null) {
			String _v = __doDecodeValue(_c.getValue());
			if (this.__useEncodeUrl) {
				try {
					_v = URLEncoder.encode(_v, WebMVC.getConfig().getCharsetEncoding());
				} catch (UnsupportedEncodingException e) {
					System.err.println("[警告：" + e.getMessage() + "]");
				}
			}
			return new BlurObject(_v);
		}
		return null;
	}

	/**
	 * @return 获取全部Cookie
	 */
	public Map<String, BlurObject> getCookies() {
		Map<String, BlurObject> _returnValue = new HashMap<String, BlurObject>();
		Cookie[] _cookies = __request.getCookies();
		if (_cookies != null) {
			String _cookiePre = WebMVC.getConfig().getCookiePrefix();
			int _preLength = StringUtils.length(_cookiePre);
			for (Cookie _cookie : _cookies) {
				String _name = _cookie.getName();
				if (_name.startsWith(_cookiePre)) {
					String _v = __doDecodeValue(_cookie.getValue());
					if (this.__useEncodeUrl) {
						try {
							_v = URLDecoder.decode(_v, WebMVC.getConfig().getCharsetEncoding());
						} catch (UnsupportedEncodingException e) {
							System.err.println("[警告：" + e.getMessage() + "]");
						}
					}
					_returnValue.put(_name.substring(_preLength), new BlurObject(_v));
				}
			}
		}
		return _returnValue;
	}

	/**
	 * @param key 键
	 * @return 移除Cookie
	 */
	public CookieHelper removeCookie(String key) {
		return this.setCookie(key, "", 0);
	}

	/**
	 * @param key 键
	 * @param value 值
	 * @return 添加或重设Cookie，过期时间基于Session时效
	 */
	public CookieHelper setCookie(String key, String value) {
		return this.setCookie(key, value, -1);
	}

	/**
	 * @param key 键
	 * @param value 值
	 * @param maxAge 过期时间
	 * @return 添加或重设Cookie
	 */
	public CookieHelper setCookie(String key, String value, int maxAge) {
		if (this.__useEncodeUrl) {
			try {
				value = URLEncoder.encode(value, WebMVC.getConfig().getCharsetEncoding());
			} catch (UnsupportedEncodingException e) {
				System.err.println("[警告：" + e.getMessage() + "]");
			}
		}
		Cookie _cookie = new Cookie(WebMVC.getConfig().getCookiePrefix() + key, StringUtils.isBlank(value) ? "" : __doEncodeValue(value));
		_cookie.setMaxAge(maxAge);
		_cookie.setPath(WebMVC.getConfig().getCookiePath());
		if (StringUtils.isNotBlank(WebMVC.getConfig().getCookieDomain())) {
			_cookie.setDomain(WebMVC.getConfig().getCookieDomain());
		}
		_cookie.setSecure(__request.getServerPort() == 443 ? true : false);
		WebContext.getResponse().addCookie(_cookie);
		return this;
	}

	/**
	 * @return 设置开启采用密钥加密
	 */
	public CookieHelper allowUseAuthKey() {
		this.__useAuthKey = true;
		return this;
	}

	/**
	 * @return 设置开启采用Base64编码
	 */
	public CookieHelper allowUseBase64() {
		this.__useBase64 = true;
		return this;
	}

	/**
	 * @return 设置开启采用UrlEncode编码
	 */
	public CookieHelper allowUseEncodeUrl() {
		this.__useEncodeUrl = true;
		return this;
	}

	/**
	 * @return 清理所有的Cookie
	 */
	public CookieHelper clearCookies() {
		Map<String,BlurObject> _cookies = this.getCookies();
		for (String _name : _cookies.keySet()) {
			this.removeCookie(_name);
		}
		return this;
	}

	/**
	 * @return 获取经过MD5加密的Cookie密钥（注：需先开启采用密钥加密，否则返回“”）
	 */
	private String __getEncodedAuthKeyStr() {
		if (this.__useAuthKey) {
			String _key = WebMVC.getConfig().getCookieAuthKey();
			if (StringUtils.isNotBlank(_key)) {
				return MD5Codec.encode(_key + this.__request.getHeader(HttpHeaders.USER_AGENT));
			}
		}
		return "";
	}

	private String __doEncodeValue(String value) {
		String _value = null;
		if (this.__useAuthKey) {
			if (__cookieKey == null) {
				__cookieKey = __getEncodedAuthKeyStr();
			}
			if (StringUtils.isNotBlank(__cookieKey)) {
				try {
					if (__codecHelper == null) {
						__codecHelper = DESCodec.create(__cookieKey);
					}
					_value = __codecHelper.encrypt(value);
				} catch (Exception e) {
					// ~~~
				}
			} else {
				_value = value;
			}
		} else {
			_value = value;
		}
		if (StringUtils.isNotBlank(_value) && this.__useBase64) {
			_value = Base64Codec.encode(_value);
		}
		return _value;
	}

	private String __doDecodeValue(String value) {
		String _value = null;
		if (this.__useAuthKey) {
			if (__cookieKey == null) {
				__cookieKey = __getEncodedAuthKeyStr();
			}
			if (StringUtils.isNotBlank(__cookieKey)) {
				try {
					if (__codecHelper == null) {
						__codecHelper = DESCodec.create(__cookieKey);
					}
					if (this.__useBase64) {
						value = Base64Codec.decode(_value);
					}
					_value = __codecHelper.decrypt(value);
				} catch (Exception e) {
					// ~~~
				}
			} else {
				if (this.__useBase64) {
					value = Base64Codec.decode(value);
				}
				_value = value;
			}
		} else {
			if (this.__useBase64) {
				value = Base64Codec.decode(value);
			}
			_value = value;
		}
		return _value;
	}

}
