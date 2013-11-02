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

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;

import net.ymate.platform.commons.lang.BlurObject;
import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.commons.util.ClassUtils.ClassBeanWrapper;
import net.ymate.platform.mvc.filter.IFilterChain;
import net.ymate.platform.mvc.support.RequestExecutor;
import net.ymate.platform.mvc.view.IView;
import net.ymate.platform.mvc.web.IUploadFileWrapper;
import net.ymate.platform.mvc.web.annotation.CookieValue;
import net.ymate.platform.mvc.web.annotation.ModelBind;
import net.ymate.platform.mvc.web.annotation.PathVariable;
import net.ymate.platform.mvc.web.annotation.RequestHeader;
import net.ymate.platform.mvc.web.annotation.RequestParam;
import net.ymate.platform.mvc.web.context.WebContext;
import net.ymate.platform.mvc.web.view.IWebView;
import net.ymate.platform.mvc.web.view.impl.BinaryView;
import net.ymate.platform.mvc.web.view.impl.ForwardView;
import net.ymate.platform.mvc.web.view.impl.FreeMarkerView;
import net.ymate.platform.mvc.web.view.impl.HttpStatusView;
import net.ymate.platform.mvc.web.view.impl.JsonView;
import net.ymate.platform.mvc.web.view.impl.JspView;
import net.ymate.platform.mvc.web.view.impl.RedirectView;
import net.ymate.platform.mvc.web.view.impl.TextView;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * HttpRequestExecutor
 * </p>
 * <p>
 * 基于Web应用的MVC请求执行器；
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
 *          <td>2012-12-20下午6:26:12</td>
 *          </tr>
 *          </table>
 */
public class HttpRequestExecutor extends RequestExecutor {

	private static final Log _LOG = LogFactory.getLog(HttpRequestExecutor.class);

	/**
	 * 构造器
	 * 
	 * @param meta 请求元数据描述对象
	 */
	public HttpRequestExecutor(HttpRequestMeta meta) {
		super(meta);
	}

	/**
	 * 构造器
	 * 
	 * @param meta 请求元数据描述对象
	 * @param chain 拦截器执行链对象
	 */
	public HttpRequestExecutor(HttpRequestMeta meta, IFilterChain chain) {
		super(meta, chain);
	}

	protected Object parseCookieValueAnnotation(String paramName, String defaultValue, boolean required, Class<?> type, String defaultParamName) {
		String _paramName = StringUtils.defaultIfEmpty(paramName, defaultParamName);
		String _value = StringUtils.defaultIfEmpty(CookieHelper.create().getCookie(_paramName).toStringValue(), defaultValue);
		_LOG.info("分析请求参数[name=" + _paramName + ", value=" + _value + ", type=Cookies]");
		// 若方法存在参数验证注解则放弃此项
		if (!hasValidation() && required && StringUtils.isBlank(_value)) {
			throw new NullPointerException("方法参数" + _paramName + "值为空");
		}
		validateFieldValues.put(_paramName, _value);
		return new BlurObject(_value).toObjectValue(type);
	}

	protected Object parsePathVariableAnnotation(String paramName, String defaultValue, boolean required, Class<?> type, String defaultParamName) {
		String _paramName = StringUtils.defaultIfEmpty(paramName, defaultParamName);
		String _value = StringUtils.defaultIfEmpty((String) WebContext.getContext().get(_paramName), defaultValue);
		_LOG.info("分析请求参数[name=" + _paramName + ", value=" + _value + ", type=PathVariable]");
		if (!hasValidation() && required && StringUtils.isBlank(_value)) {
			throw new NullPointerException("方法参数" + _paramName + "值为空");
		}
		validateFieldValues.put(_paramName, _value);
		return new BlurObject(_value).toObjectValue(type);
	}

	protected Object parseRequestHeaderAnnotation(String paramName, String defaultValue, boolean required, Class<?> type, String defaultParamName) {
		String _paramName = StringUtils.defaultIfEmpty(paramName, defaultParamName);
		String _value = StringUtils.defaultIfEmpty(WebContext.getRequest().getHeader(_paramName), defaultValue);
		_LOG.info("分析请求参数[name=" + _paramName + ", value=" + _value + ", type=Header]");
		if (!hasValidation() && required && StringUtils.isBlank(_value)) {
			throw new NullPointerException("方法参数" + _paramName + "值为空");
		}
		validateFieldValues.put(_paramName, _value);
		return new BlurObject(_value).toObjectValue(type);
	}

	protected Object parseRequestParamAnnotation(String prefix, String paramName, String defaultValue, boolean required, Class<?> type, String defaultParamName) {
		String _paramName = prefix + StringUtils.defaultIfEmpty(paramName, defaultParamName);
		if (type.isArray()) {
			if (type.equals(IUploadFileWrapper[].class)) {
				if (WebContext.getRequest() instanceof MultipartRequestWrapper) {
					IUploadFileWrapper[] _value = ((MultipartRequestWrapper) WebContext.getRequest()).getFiles(_paramName);
					_LOG.info("分析请求参数[name=" + _paramName + ", value=" + (_value != null ? _value.toString() : "") + ", type=RequestParameter]");
					validateFieldValues.put(_paramName, _value);
					return ((MultipartRequestWrapper) WebContext.getRequest()).getFiles(_paramName);
				}
				validateFieldValues.put(_paramName, null);
				return null;
			}
			String[] _values = (String[]) WebContext.getRequest().getParameterMap().get(_paramName);
			if (_values == null || _values.length == 0) {
				_values = StringUtils.split(defaultValue, ",");
			}
			if (_values != null && _values.length > 0) {
				Class<?> _arrayClassType = ClassUtils.getArrayClassType(type);
				Object[] _tempParams = (Object[]) Array.newInstance(_arrayClassType, _values.length);
				for (int _tempIdx = 0; _tempIdx < _values.length; _tempIdx++) {
					_tempParams[_tempIdx] = new BlurObject(_values[_tempIdx]).toObjectValue(_arrayClassType);
				}
				_LOG.info("分析请求参数[name=" + _paramName + ", value=" + (_tempParams != null ? _tempParams.toString() : "") + ", type=RequestParameter]");
				validateFieldValues.put(_paramName, _tempParams);
				return _tempParams;
			} else if (!hasValidation() && required) {
				throw new NullPointerException("方法参数" + _paramName + "值为空");
			}
			validateFieldValues.put(_paramName, null);
			return null;
		} else if (type.equals(IUploadFileWrapper.class)) {
			if (WebContext.getRequest() instanceof MultipartRequestWrapper) {
				IUploadFileWrapper _value = ((MultipartRequestWrapper) WebContext.getRequest()).getFile(_paramName);
				_LOG.info("分析请求参数[name=" + _paramName + ", value=" + (_value != null ? _value.getName() : "") + ", type=RequestParameter]");
				validateFieldValues.put(_paramName, _value);
				return _value;
			}
			validateFieldValues.put(_paramName, null);
			return null;
		}
		String _value = StringUtils.defaultIfEmpty(WebContext.getRequest().getParameter(_paramName), defaultValue);
		_LOG.info("分析请求参数[name=" + _paramName + ", value=" + _value + ", type=RequestParameter]");
		if (!hasValidation() && required && StringUtils.isBlank(_value)) {
			throw new NullPointerException("方法参数" + _paramName + "值为空");
		}
		validateFieldValues.put(_paramName, _value);
		return new BlurObject(_value).toObjectValue(type);
	}

	protected Object parseModelBindAnnotation(Class<?> type) {
		ClassBeanWrapper<?> _wrapper = ClassUtils.wrapper(type);
		for (String _fName : _wrapper.getFieldNames()) {
			Annotation[] _fieldAnnotations = _wrapper.getFieldAnnotations(_fName);
			for (Annotation _annotation : _fieldAnnotations) {
				if (_annotation instanceof CookieValue) {
					CookieValue _anno = (CookieValue) _annotation;
					Object _value = this.parseCookieValueAnnotation(_anno.value(), _anno.defaultValue(), _anno.required(), _wrapper.getFieldType(_fName), _fName);
					_wrapper.setValue(_fName, _value);
					break;
				} else if (_annotation instanceof PathVariable) {
					PathVariable _anno = (PathVariable) _annotation;
					Object _value = this.parsePathVariableAnnotation(_anno.value(), _anno.defaultValue(), _anno.required(), _wrapper.getFieldType(_fName), _fName);
					_wrapper.setValue(_fName, _value);
					break;
				} else if (_annotation instanceof RequestHeader) {
					RequestHeader _anno = (RequestHeader) _annotation;
					Object _value = this.parseRequestHeaderAnnotation(_anno.value(), _anno.defaultValue(), _anno.required(), _wrapper.getFieldType(_fName), _fName);
					_wrapper.setValue(_fName, _value);
					break;
				} else if (_annotation instanceof RequestParam) {
					RequestParam _anno = (RequestParam) _annotation;
					Object _value = this.parseRequestParamAnnotation(_anno.prefix(), _anno.value(), _anno.defaultValue(), _anno.required(), _wrapper.getFieldType(_fName), _fName);
					_wrapper.setValue(_fName, _value);
					break;
				} else if (_annotation instanceof ModelBind) {
					_wrapper.setValue(_fName, this.parseModelBindAnnotation(_wrapper.getFieldType(_fName)));
					break;
				}
			}
		}
		return _wrapper.getTarget();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.support.RequestExecutor#getMethodParams()
	 */
	protected Object[] getMethodParams() {
		Class<?>[] _paramTypes = this.requestMeta.getParameterTypes();
		Object[] _params = new Object[_paramTypes.length];
		if (_params.length > 0) {
			Annotation[][] _paramAnnotations = this.requestMeta.getMethod().getParameterAnnotations();
			for (int _idx = 0; _idx < _params.length; _idx++) {
				Annotation[] _annotations = _paramAnnotations[_idx];
				for (Annotation _annotation : _annotations) {
					if (_annotation instanceof CookieValue) {
						CookieValue _anno = (CookieValue) _annotation;
						_params[_idx] = this.parseCookieValueAnnotation(_anno.value(), _anno.defaultValue(), _anno.required(), _paramTypes[_idx], this.requestMeta.getMethodParamNames()[_idx]);
						break;
					} else if (_annotation instanceof PathVariable) {
						PathVariable _anno = (PathVariable) _annotation;
						_params[_idx] = this.parsePathVariableAnnotation(_anno.value(), _anno.defaultValue(), _anno.required(), _paramTypes[_idx], this.requestMeta.getMethodParamNames()[_idx]);
						break;
					} else if (_annotation instanceof RequestHeader) {
						RequestHeader _anno = (RequestHeader) _annotation;
						_params[_idx] = this.parseRequestHeaderAnnotation(_anno.value(), _anno.defaultValue(), _anno.required(), _paramTypes[_idx], this.requestMeta.getMethodParamNames()[_idx]);
						break;
					} else if (_annotation instanceof RequestParam) {
						RequestParam _anno = (RequestParam) _annotation;
						_params[_idx] = this.parseRequestParamAnnotation(_anno.prefix(), _anno.value(), _anno.defaultValue(), _anno.required(), _paramTypes[_idx], this.requestMeta.getMethodParamNames()[_idx]);
						break;
					} else if (_annotation instanceof ModelBind) {
						_params[_idx] = this.parseModelBindAnnotation(_paramTypes[_idx]);
						break;
					}
				}
			}
		}
		return _params;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.support.RequestExecutor#processMethodResultToView(java.lang.Object)
	 */
	protected IView processMethodResultToView(Object result) throws Exception {
		IView _view = null;
		if (result == null) {
			_view = new JspView();
		} else if (result instanceof String) {
			String _viewStr = StringUtils.trimToEmpty((String) result).toLowerCase();
			if (StringUtils.startsWith(_viewStr, IWebView.VIEW_REDIRECT)) {
				_view = new RedirectView(StringUtils.substringAfter(_viewStr, IWebView.VIEW_REDIRECT));
			} else if (StringUtils.startsWith(_viewStr, IWebView.VIEW_FORWARD)) {
				_view = new ForwardView(StringUtils.substringAfter(_viewStr, IWebView.VIEW_FORWARD));
			} else if (StringUtils.startsWith(_viewStr, IWebView.VIEW_HTTP_STATUS)) {
				String[] _statusContent = StringUtils.split(StringUtils.substringAfter(_viewStr, IWebView.VIEW_HTTP_STATUS), ",");
				_view = new HttpStatusView(Integer.parseInt(_statusContent[0]), _statusContent.length >= 2 ? _statusContent[1] : null);
			} else if (StringUtils.startsWith(_viewStr, IWebView.VIEW_JSON)) {
				_view = new JsonView(StringUtils.substringAfter(_viewStr, IWebView.VIEW_JSON));
			} else if (StringUtils.startsWith(_viewStr, IWebView.VIEW_JSP)) {
				_view = new JspView(StringUtils.substringAfter(_viewStr, IWebView.VIEW_JSP));
			} else if (StringUtils.startsWith(_viewStr, IWebView.VIEW_FTL)) {
				_view = new FreeMarkerView(StringUtils.substringAfter(_viewStr, IWebView.VIEW_FTL));
			} else if (StringUtils.startsWith(_viewStr, IWebView.VIEW_INLINE_FILE)) {
				_view = BinaryView.loadFromFile(StringUtils.substringAfter(_viewStr, IWebView.VIEW_INLINE_FILE), false);
			} else if (StringUtils.startsWith(_viewStr, IWebView.VIEW_FILE)) {
				_view = BinaryView.loadFromFile(StringUtils.substringAfter(_viewStr, IWebView.VIEW_FILE), true);
			} else {
				_view = new TextView(_viewStr);
			}
		} else if (result instanceof IView) {
			_view = (IView) result;
		}
		return _view;
	}

}
