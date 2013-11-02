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
package net.ymate.platform.mvc.web.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.ymate.platform.commons.lang.PairObject;
import net.ymate.platform.mvc.MVC;
import net.ymate.platform.mvc.context.IRequestContext;
import net.ymate.platform.mvc.filter.IFilter;
import net.ymate.platform.mvc.filter.IFilterChain;
import net.ymate.platform.mvc.impl.DefaultRequestProcessor;
import net.ymate.platform.mvc.support.IControllerBeanFactory;
import net.ymate.platform.mvc.support.IControllerBeanMeta;
import net.ymate.platform.mvc.support.RequestExecutor;
import net.ymate.platform.mvc.support.RequestMeta;
import net.ymate.platform.mvc.web.WebMVC;
import net.ymate.platform.mvc.web.context.WebContext;
import net.ymate.platform.mvc.web.filter.FileUploadFilter;
import net.ymate.platform.mvc.web.filter.RequestMethodFilter;
import net.ymate.platform.mvc.web.support.HttpMethod;
import net.ymate.platform.mvc.web.support.HttpRequestExecutor;
import net.ymate.platform.mvc.web.support.HttpRequestMeta;
import net.ymate.platform.mvc.web.support.RequestMappingParser;
import net.ymate.platform.mvc.web.support.WebControllerBeanFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * WebRequestProcessor
 * </p>
 * <p>
 * 基于Web应用的MVC控制器请求处理器接口实现类；
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
 *          <td>2012-12-16下午3:40:20</td>
 *          </tr>
 *          </table>
 */
public class WebRequestProcessor extends DefaultRequestProcessor {

	private static final Log _LOG = LogFactory.getLog(WebRequestProcessor.class);

	protected final Map<String, RequestMeta> __REQUEST_MAPPING_MAP;

	protected final Map<HttpMethod, Map<String, RequestMeta>> __RESTFUL_MAPPING_MAP;

	protected final RequestMappingParser __REQUEST_MAPPING_PARSER;

	/**
	 * 构造器
	 */
	public WebRequestProcessor() {
		super();
		__REQUEST_MAPPING_MAP = new HashMap<String, RequestMeta>();
		__RESTFUL_MAPPING_MAP = new HashMap<HttpMethod, Map<String, RequestMeta>>();
		for (HttpMethod _method : HttpMethod.values()) {
			__RESTFUL_MAPPING_MAP.put(_method, new HashMap<String, RequestMeta>());
		}
		__REQUEST_MAPPING_PARSER = new RequestMappingParser();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.impl.DefaultRequestProcessor#initialize()
	 */
	public void initialize() {
        super.initialize();
        // 注册框架支持的默认拦截器
        this.getControllerBeanFactory().add(RequestMethodFilter.class);
        this.getControllerBeanFactory().add(FileUploadFilter.class);
        // 注册扩展拦截器
        for (Class<IFilter> _extraFilter : WebMVC.getConfig().getExtraFilters()) {
        	this.getControllerBeanFactory().add(_extraFilter);
        }
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.impl.DefaultRequestProcessor#getControllerBeanFactory()
	 */
	public IControllerBeanFactory getControllerBeanFactory() {
		if (__CONTROLLER_BEAN_FACTORY == null) {
			__CONTROLLER_BEAN_FACTORY = new WebControllerBeanFactory(MVC.getConfig().getControllerPackages());
		}
		return __CONTROLLER_BEAN_FACTORY;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.impl.DefaultRequestProcessor#addControllerMetaToMap(net.ymate.platform.mvc.support.IControllerBeanMeta)
	 */
	protected void addControllerMetaToMap(IControllerBeanMeta beanMeta) {
		for (RequestMeta _meta : beanMeta.getRequestMetas()) {
			if (WebMVC.getConfig().isRestfulModel()) {
				Set<HttpMethod> _allowMethods = ((HttpRequestMeta) _meta).getAllowHttpMethods();
				if (_allowMethods.isEmpty()) {
					__RESTFUL_MAPPING_MAP.get(HttpMethod.GET).put(_meta.getRequestMapping(), _meta);
				} else {
					for (HttpMethod _method : _allowMethods) {
						__RESTFUL_MAPPING_MAP.get(_method).put(_meta.getRequestMapping(), _meta);
					}
				}
			} else {
				if (_meta.getRequestMapping().contains("{")) {
					__REQUEST_MAPPING_MAP.put(_meta.getRequestMapping(), _meta);
				} else {
					__CONSTANT_REQUEST_MAPPING_MAP.put(_meta.getRequestMapping(), _meta);
				}
			}
			_LOG.info("注册控制器请求映射 [" + _meta.getRequestMapping() + ", " + _meta.getClass().getName() + "]");
			// 注册拦截器
			for (PairObject<Class<IFilter>, String> _c : _meta.getFilters()) {
				_LOG.info("注册拦截器 [" + _c.getKey() + ", " + _c.getKey().getName() + "]");
				this.getControllerBeanFactory().add(_c.getKey());
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.impl.DefaultRequestProcessor#matchRequestMapping(net.ymate.platform.mvc.context.IRequestContext)
	 */
	protected RequestMeta matchRequestMapping(IRequestContext context) {
		RequestMeta _meta = null;
		if (WebMVC.getConfig().isRestfulModel()) {
			HttpMethod _method = HttpMethod.valueOf(WebContext.getRequest().getMethod());
			_meta = __RESTFUL_MAPPING_MAP.get(_method).get(context.getRequestMapping());
			if (_meta == null) {
				String _key = __REQUEST_MAPPING_PARSER.doParser(context, Collections.unmodifiableSet(__RESTFUL_MAPPING_MAP.get(_method).keySet()));
				if (_key != null) {
					_meta = __RESTFUL_MAPPING_MAP.get(_method).get(_key);
				}
			}
		} else {
			_meta = __CONSTANT_REQUEST_MAPPING_MAP.get(context.getRequestMapping());
			if (_meta == null) {
				String _key = __REQUEST_MAPPING_PARSER.doParser(context, Collections.unmodifiableSet(__REQUEST_MAPPING_MAP.keySet()));
				if (_key != null) {
					_meta = __REQUEST_MAPPING_MAP.get(_key);
				}
			}
		}
		return _meta;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.impl.DefaultRequestProcessor#getRequestExecutorFormCache(java.lang.String)
	 */
	protected RequestExecutor getRequestExecutorFormCache(String cacheKey) {
		if (WebMVC.getConfig().isRestfulModel()) {
			cacheKey += ("|" + WebContext.getRequest().getMethod());
		}
		return __REQUEST_EXECUTOR_CACHES.get(cacheKey);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.impl.DefaultRequestProcessor#putRequestExecutorToCache(java.lang.String, net.ymate.platform.mvc.support.RequestExecutor)
	 */
	protected void putRequestExecutorToCache(String cacheKey, RequestExecutor executor) {
		if (WebMVC.getConfig().isRestfulModel()) {
			cacheKey += ("|" + WebContext.getRequest().getMethod());
		}
		__REQUEST_EXECUTOR_CACHES.put(cacheKey, executor);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.impl.DefaultRequestProcessor#getRequestExecutor(net.ymate.platform.mvc.support.RequestMeta, net.ymate.platform.mvc.filter.IFilterChain)
	 */
	protected RequestExecutor getRequestExecutor(RequestMeta meta, IFilterChain chain) {
		// 添加扩展拦截器
		for (Class<IFilter> _extraFilter : WebMVC.getConfig().getExtraFilters()) {
			chain.add(0, new PairObject<IFilter, String>(this.getControllerBeanFactory().get(_extraFilter)));
		}
		// 添加请求方法拦截器
		chain.add(0, new PairObject<IFilter, String>(this.getControllerBeanFactory().get(FileUploadFilter.class)));
		chain.add(0, new PairObject<IFilter, String>(this.getControllerBeanFactory().get(RequestMethodFilter.class)));
		return new HttpRequestExecutor((HttpRequestMeta) meta, chain);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.impl.DefaultRequestProcessor#destroy()
	 */
	@Override
	public void destroy() {
		super.destroy();
		__REQUEST_MAPPING_MAP.clear();
	}

}
