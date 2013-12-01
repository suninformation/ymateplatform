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
package net.ymate.platform.mvc.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import net.ymate.platform.base.YMP;

/**
 * <p>
 * WebMvcEventListener
 * </p>
 * <p>
 * 基于Web应用的MVC框架初始化及上下文事件监听器；
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
 *          <td>2012-12-7下午8:33:43</td>
 *          </tr>
 *          </table>
 */
public class WebMvcEventListener implements ServletContextListener,
		HttpSessionListener, ServletRequestListener {

	private static IWebEventHandler __WEB_EVENT_HANDLER;

	static {
		YMP.initialize();
		__WEB_EVENT_HANDLER = WebMVC.getConfig().getEventHandlerClassImpl();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent sce) {
		if (__WEB_EVENT_HANDLER != null) {
			__WEB_EVENT_HANDLER.onStartup(sce);
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent sce) {
		if (__WEB_EVENT_HANDLER != null) {
			__WEB_EVENT_HANDLER.onShutdown(sce);
		}
		YMP.destroy();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
	 */
	public void sessionCreated(HttpSessionEvent se) {
		if (__WEB_EVENT_HANDLER != null) {
			__WEB_EVENT_HANDLER.onSessionCreated(se);
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
	 */
	public void sessionDestroyed(HttpSessionEvent se) {
		if (__WEB_EVENT_HANDLER != null) {
			__WEB_EVENT_HANDLER.onSessionDestroyed(se);
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequestListener#requestInitialized(javax.servlet.ServletRequestEvent)
	 */
	public void requestInitialized(ServletRequestEvent sre) {
		if (__WEB_EVENT_HANDLER != null) {
			__WEB_EVENT_HANDLER.onRequestInitialized(sre);
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequestListener#requestDestroyed(javax.servlet.ServletRequestEvent)
	 */
	public void requestDestroyed(ServletRequestEvent sre) {
		if (__WEB_EVENT_HANDLER != null) {
			__WEB_EVENT_HANDLER.onRequestDestroyed(sre);
		}
	}

}
