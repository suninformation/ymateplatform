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

import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.ymate.platform.mvc.filter.IFilter;
import net.ymate.platform.mvc.impl.DefaultMvcConfig;
import net.ymate.platform.mvc.web.IWebErrorHandler;
import net.ymate.platform.mvc.web.IWebEventHandler;
import net.ymate.platform.mvc.web.IWebMultipartHandler;
import net.ymate.platform.mvc.web.IWebMvcConfig;
import net.ymate.platform.plugin.IPluginExtraParser;


/**
 * <p>
 * WebMvcConfig
 * </p>
 * <p>
 * 基于Web应用的MVC框架初始化配置接口实现类；
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
 *          <td>2012-12-7下午9:21:28</td>
 *          </tr>
 *          </table>
 */
public class WebMvcConfig extends DefaultMvcConfig implements IWebMvcConfig {

	private IWebMultipartHandler __multipartHandler;
	private List<Class<IFilter>> __extraFilters;
	private boolean __restfulModel;
    private boolean __conventionUrlrewrite;
	private boolean __conventionModel;
	private String __urlSuffix;
	private String __viewPath;
	private String __uploadTempDir;
	private int __fileSizeMax;
	private int __totalSizeMax;
	private int __sizeThreshold;
	private String __cookiePrefix;
	private String __cookieDomain;
	private String __cookiePath;
	private String __cookieAuthKey;

	/**
	 * 构造器
	 * 
	 * @param handler
	 * @param extraParser
	 * @param errorHandler
	 * @param locale
	 * @param i18n
	 * @param charsetEncoding
	 * @param pluginHome
	 * @param extendParams
	 * @param controllerPackages
	 */
	public WebMvcConfig(IWebEventHandler handler,
			IPluginExtraParser extraParser, IWebErrorHandler errorHandler,
			Locale locale, boolean i18n, String charsetEncoding, String pluginHome, Map<String, String> extendParams, String[] controllerPackages) {
		super(handler, extraParser, errorHandler, locale, i18n, charsetEncoding, pluginHome, extendParams, controllerPackages);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.DefaultMvcConfig#getEventHandlerClassImpl()
	 */
	@Override
	public IWebEventHandler getEventHandlerClassImpl() {
		return (IWebEventHandler) super.getEventHandlerClassImpl();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.DefaultMvcConfig#getErrorHandlerClassImpl()
	 */
	@Override
	public IWebErrorHandler getErrorHandlerClassImpl() {
		return (IWebErrorHandler) super.getErrorHandlerClassImpl();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.web.IWebMvcConfig#getMultipartHandlerClassImpl()
	 */
	public IWebMultipartHandler getMultipartHandlerClassImpl() {
		return __multipartHandler;
	}

	/**
	 * 设置处理类型为"multipart/form-data"表单请求的接口实现对象
	 * @param handler 接口实现类
	 */
	public void setMultipartHandlerClassImpl(IWebMultipartHandler handler) {
		this.__multipartHandler = handler;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.web.IWebMvcConfig#getExtraFilters()
	 */
	public List<Class<IFilter>> getExtraFilters() {
		return __extraFilters;
	}

	/**
	 * 设置扩展拦截器集合（用于二次开发扩展MVC框架与其它框架的整合）
	 * 
	 * @param extraFilters 扩展拦截器集合
	 */
	public void setExtraFilters(List<Class<IFilter>> extraFilters) {
		this.__extraFilters = extraFilters;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.web.IWebMvcConfig#isRestfulModel()
	 */
	public boolean isRestfulModel() {
		return __restfulModel;
	}

	/**
	 * 设置是否采用严格的RESTFul模式
	 * 
	 * @param restfulModel true/false
	 */
	public void setRestfulModel(boolean restfulModel) {
		this.__restfulModel = restfulModel;
	}

    public boolean isConventionUrlrewrite() {
        return __conventionUrlrewrite;
    }

    public void setConventionUrlrewrite(boolean conventionUrlrewrite) {
        this.__conventionUrlrewrite = conventionUrlrewrite;
    }

    /* (non-Javadoc)
         * @see net.ymate.platform.mvc.web.IWebMvcConfig#isConventionModel()
         */
	public boolean isConventionModel() {
		return __conventionModel;
	}

	/**
	 * 设置是否采用约定优于配置模式
	 * 
	 * @param conventionModel true/false
	 */
	public void setConventionModel(boolean conventionModel) {
		this.__conventionModel = conventionModel;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.web.IWebMvcConfig#getUrlSuffix()
	 */
	public String getUrlSuffix() {
		return __urlSuffix;
	}

	/**
	 * 设置请求的URL后缀名，如：.do
	 * 
	 * @param urlSuffix
	 */
	public void setUrlSuffix(String urlSuffix) {
		this.__urlSuffix = urlSuffix;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.web.IWebMvcConfig#getViewPath()
	 */
	public String getViewPath() {
		return __viewPath;
	}

	/**
	 * 设置控制器视图文件基础路径（必须是以 '/' 开始和结尾，默认值：/WEB-INF/templates/）
	 * 
	 * @param viewPath
	 */
	public void setViewPath(String viewPath) {
		this.__viewPath = viewPath;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.web.IWebMvcConfig#getUploadTempDir()
	 */
	public String getUploadTempDir() {
		return __uploadTempDir;
	}

	/**
	 * 设置文件上传临时目录，为空则默认使用：System.getProperty("java.io.tmpdir")
	 * 
	 * @param tempDir
	 */
	public void setUploadTempDir(String tempDir) {
		this.__uploadTempDir = tempDir;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.web.IWebMvcConfig#getFileSizeMax()
	 */
	public int getUploadFileSizeMax() {
		return __fileSizeMax;
	}

	/**
	 * 设置上传文件大小最大值（字节），默认值：-1（注：10485760 = 10M）
	 * 
	 * @param fileSizeMax
	 */
	public void setUploadFileSizeMax(int fileSizeMax) {
		this.__fileSizeMax = fileSizeMax;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.web.IWebMvcConfig#getTotalSizeMax()
	 */
	public int getUploadTotalSizeMax() {
		return __totalSizeMax;
	}

	/**
	 * 设置上传文件总量大小最大值（字节）, 默认值：-1（注：10485760 = 10M）
	 * 
	 * @param totalSizeMax
	 */
	public void setUploadTotalSizeMax(int totalSizeMax) {
		this.__totalSizeMax = totalSizeMax;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.web.IWebMvcConfig#getSizeThreshold()
	 */
	public int getUploadSizeThreshold() {
		return __sizeThreshold;
	}

	/**
	 * 设置内存缓冲区的大小，默认值： 10240字节（=10K），即如果文件大于10K，将使用临时文件缓存上传文件
	 * 
	 * @param sizeThreshold
	 */
	public void setUploadSizeThreshold(int sizeThreshold) {
		this.__sizeThreshold = sizeThreshold;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.web.IWebMvcConfig#getCookiePrefix()
	 */
	public String getCookiePrefix() {
		return __cookiePrefix;
	}

	/**
	 * 设置Cookie键前缀
	 * 
	 * @param cookiePrefix
	 */
	public void setCookiePrefix(String cookiePrefix) {
		this.__cookiePrefix = cookiePrefix;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.web.IWebMvcConfig#getCookieDomain()
	 */
	public String getCookieDomain() {
		return __cookieDomain;
	}

	public void setCookieDomain(String cookieDomain) {
		this.__cookieDomain = cookieDomain;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.web.IWebMvcConfig#getCookiePath()
	 */
	public String getCookiePath() {
		return __cookiePath;
	}

	/**
	 * 设置Cookie作用路径
	 * 
	 * @param cookiePath
	 */
	public void setCookiePath(String cookiePath) {
		this.__cookiePath = cookiePath;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.web.IWebMvcConfig#getCookieAuthKey()
	 */
	public String getCookieAuthKey() {
		return __cookieAuthKey;
	}

	/**
	 * 设置Cookie密钥
	 * 
	 * @param cookieAuthKey
	 */
	public void setCookieAuthKey(String cookieAuthKey) {
		this.__cookieAuthKey = cookieAuthKey;
	}

}
