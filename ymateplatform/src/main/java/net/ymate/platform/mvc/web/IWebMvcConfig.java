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

import java.util.List;

import net.ymate.platform.mvc.IMvcConfig;
import net.ymate.platform.mvc.filter.IFilter;

/**
 * <p>
 * IWebMvcConfig
 * </p>
 * <p>
 * 基于Web应用的MVC框架初始化配置接口；
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
 *          <td>2012-12-6下午8:41:33</td>
 *          </tr>
 *          </table>
 */
public interface IWebMvcConfig extends IMvcConfig {

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.config.IMvcConfig#getEventHandlerClassImpl()
	 */
	public IWebEventHandler getEventHandlerClassImpl();

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.IMvcConfig#getErrorHandlerClassImpl()
	 */
	public IWebErrorHandler getErrorHandlerClassImpl();

	/**
	 * @return 返回处理类型为"multipart/form-data"表单请求的接口实现对象
	 */
	public IWebMultipartHandler getMultipartHandlerClassImpl();

	/**
	 * @return 返回扩展拦截器集合（用于二次开发扩展MVC框架与其它框架的整合）
	 */
	public List<Class<IFilter>> getExtraFilters();

	/**
	 * @return 是否采用严格的RESTFul模式，默认false
	 */
	public boolean isRestfulModel();

	/**
	 * @return 是否采用约定优于配置模式，默认true
	 */
	public boolean isConventionModel();

    /**
     * @return Convention模式开启时是否采用URL伪静态
     */
    public boolean isConventionUrlrewrite();

	/**
	 * @return 返回请求的URL后缀名，如：.do
	 */
	public String getUrlSuffix();

	/**
	 * @return 返回控制器视图文件基础路径（必须是以 '/' 开始和结尾，默认值：/WEB-INF/templates/）
	 */
	public String getViewPath();

	/**
	 * @return 返回文件上传临时目录，为空则默认使用：System.getProperty("java.io.tmpdir")
	 */
	public String getUploadTempDir();

	/**
	 * @return 返回上传文件大小最大值（字节），默认值：-1（注：10485760 = 10M）
	 */
	public int getUploadFileSizeMax();

	/**
	 * @return 返回上传文件总量大小最大值（字节）, 默认值：-1（注：10485760 = 10M）
	 */
	public int getUploadTotalSizeMax();

	/**
	 * @return 返回内存缓冲区的大小，默认值： 10240字节（=10K），即如果文件大于10K，将使用临时文件缓存上传文件
	 */
	public int getUploadSizeThreshold();

	/**
	 * @return 返回Cookie键前缀
	 */
	public String getCookiePrefix();

	/**
	 * @return 返回Cookie作用域
	 */
	public String getCookieDomain();

	/**
	 * @return 返回Cookie作用路径
	 */
	public String getCookiePath();

	/**
	 * @return 返回Cookie密钥
	 */
	public String getCookieAuthKey();

}
