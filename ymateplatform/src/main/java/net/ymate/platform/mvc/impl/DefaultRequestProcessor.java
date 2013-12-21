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
package net.ymate.platform.mvc.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.ymate.platform.base.YMP;
import net.ymate.platform.commons.beans.IBeanMeta;
import net.ymate.platform.commons.i18n.I18N;
import net.ymate.platform.commons.lang.PairObject;
import net.ymate.platform.mvc.IRequestProcessor;
import net.ymate.platform.mvc.MVC;
import net.ymate.platform.mvc.context.IRequestContext;
import net.ymate.platform.mvc.filter.IFilter;
import net.ymate.platform.mvc.filter.IFilterChain;
import net.ymate.platform.mvc.filter.impl.DefaultFilterChain;
import net.ymate.platform.mvc.support.IControllerBeanFactory;
import net.ymate.platform.mvc.support.IControllerBeanMeta;
import net.ymate.platform.mvc.support.RequestExecutor;
import net.ymate.platform.mvc.support.RequestMeta;
import net.ymate.platform.mvc.support.impl.ControllerBeanFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <p>
 * DefaultRequestProcessor
 * </p>
 * <p>
 * MVC控制器请求处理器接口默认实现类；
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
 *          <td>2012-12-10下午11:19:49</td>
 *          </tr>
 *          </table>
 */
public class DefaultRequestProcessor implements IRequestProcessor {

	private static final Log _LOG = LogFactory.getLog(DefaultRequestProcessor.class);

	protected final static Map<String, RequestExecutor> __REQUEST_EXECUTOR_CACHES = new ConcurrentHashMap<String, RequestExecutor>();

	protected final Map<String, RequestMeta> __CONSTANT_REQUEST_MAPPING_MAP;

	protected IControllerBeanFactory __CONTROLLER_BEAN_FACTORY;

	/**
	 * 构造器
	 */
	public DefaultRequestProcessor() {
		__CONSTANT_REQUEST_MAPPING_MAP = new HashMap<String, RequestMeta>();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.IRequestProcessor#initialize()
	 */
	public void initialize() {
//		_LOG.info("初始化默认控制器请求处理器...");
        for (IBeanMeta iBeanMeta : new ArrayList<IBeanMeta>(this.getControllerBeanFactory().getBeanMetas())) {
            IControllerBeanMeta _controllerMeta = (IControllerBeanMeta) iBeanMeta;
            this.addControllerMetaToMap(_controllerMeta);
        }
//        _LOG.info("默认控制器请求处理器初始化完毕");
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.IRequestProcessor#getControllerBeanFactory()
	 */
	public IControllerBeanFactory getControllerBeanFactory() {
		if (__CONTROLLER_BEAN_FACTORY == null) {
//			_LOG.info("创建控制器类工厂对象");
			__CONTROLLER_BEAN_FACTORY = new ControllerBeanFactory(MVC.getConfig().getControllerPackages());
		}
		return __CONTROLLER_BEAN_FACTORY;
	}

	/**
	 * @param context 请求上下文对象
	 * @return 根据请求上下文提取请求映射字符串，并返回与之匹配的请求元数据描述对象，若不存在则返回NULL
	 */
	protected RequestMeta matchRequestMapping(IRequestContext context) {
		return __CONSTANT_REQUEST_MAPPING_MAP.get(context.getRequestMapping());
	}

	/**
	 * 根据控制器对象元描述添加类对象到对象工厂
	 * 
	 * @param beanMeta 控制器对象元描述
	 */
	protected void addControllerMetaToMap(IControllerBeanMeta beanMeta) {
		for (RequestMeta _meta : beanMeta.getRequestMetas()) {
			_LOG.info(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.mvc.register_controller", _meta.getRequestMapping(), _meta.getTarget().getClass().getName() + "#" + _meta.getMethod().getName()));
			__CONSTANT_REQUEST_MAPPING_MAP.put(_meta.getRequestMapping(), _meta);
			// 注册拦截器
			for (PairObject<Class<IFilter>, String> _c : _meta.getFilters()) {
				_LOG.info(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.mvc.register_filter", _c.getKey(), _c.getKey().getName()));
				this.getControllerBeanFactory().add(_c.getKey());
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.IRequestProcessor#parserController(java.lang.Class)
	 */
	public void addController(Class<?> controller) {
		IBeanMeta _beanMeta = this.getControllerBeanFactory().add(controller);
		if (_beanMeta != null && _beanMeta instanceof IControllerBeanMeta) {
			this.addControllerMetaToMap((IControllerBeanMeta) _beanMeta);
		}
	}

	/**
	 * @param meta 请求元数据描述对象
	 * @param chain 拦截器执行链对象
	 * @return 返回根据所提供参数构建的请求执行器实例
	 */
	protected RequestExecutor getRequestExecutor(RequestMeta meta, IFilterChain chain) {
		return new RequestExecutor(meta, chain);
	}

	/**
	 * @param cacheKey 缓存KEY
	 * @return 尝试从执行器缓存中提取已存在的请求执行器对象
	 */
	protected RequestExecutor getRequestExecutorFormCache(String cacheKey) {
		return __REQUEST_EXECUTOR_CACHES.get(cacheKey);
	}

	/**
	 * 缓存请求执行器对象
	 * 
	 * @param cacheKey 缓存KEY
	 * @param executor 请求执行器对象
	 */
	protected void putRequestExecutorToCache(String cacheKey, RequestExecutor executor) {
		__REQUEST_EXECUTOR_CACHES.put(cacheKey, executor);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.IRequestProcessor#bindRequestExecutor(net.ymate.platform.mvc.context.IRequestContext)
	 */
	public final RequestExecutor bindRequestExecutor(IRequestContext context) {
		RequestMeta _meta = this.matchRequestMapping(context);
		if (_meta != null) {
			RequestExecutor _exec = this.getRequestExecutorFormCache(_meta.getRequestMapping());
			if (_exec == null) {
				IFilterChain _chain = new DefaultFilterChain();
	            for (PairObject<Class<IFilter>, String> _filter : _meta.getFilters()) {
	            	_chain.add(new PairObject<IFilter, String>(this.getControllerBeanFactory().get(_filter.getKey()), _filter.getValue()));
	            }
				_exec = this.getRequestExecutor(_meta, _chain);
				_LOG.info(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.mvc.create_request_executor", _meta.getRequestMapping()));
				this.putRequestExecutorToCache(_meta.getRequestMapping(), _exec);
			} else {
				_LOG.info(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.mvc.match_request_executor", _meta.getRequestMapping()));
			}
			return _exec;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.IRequestProcessor#destroy()
	 */
	public void destroy() {
		__REQUEST_EXECUTOR_CACHES.clear();
		__CONSTANT_REQUEST_MAPPING_MAP.clear();
		__CONTROLLER_BEAN_FACTORY = null;
	}

}
