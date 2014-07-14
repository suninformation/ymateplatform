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
package net.ymate.platform.plugin;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import net.ymate.platform.base.YMP;
import net.ymate.platform.commons.i18n.I18N;
import net.ymate.platform.commons.util.FileUtils;
import net.ymate.platform.commons.util.RuntimeUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <p>
 * PluginMeta
 * </p>
 * <p>
 * 插件配置信息元数据描述类；
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
 *          <td>2011-10-17下午04:52:05</td>
 *          </tr>
 *          </table>
 */
public class PluginMeta {

	private static final Log _LOG = LogFactory.getLog(PluginMeta.class);

	/** 插件唯一ID */
	private String id;

	/** 插件名称 */
	private String name;

	/** 插件别名 */
	private String alias;

	/** 插件初始启动类 */
	private String initClass;

	/** 插件版本 */
	private String version;

	/** 插件文件存放路径 */
	private String path;

	/** 插件作者 */
	private String author;

	/** 作者联系邮箱 */
	private String email;

	/** 当前插件类加载器 */
	private ClassLoader classLoader;

	/** 插件主配置文件附加内容 **/
	private Object extraObj;

	/** 是否加载时自动启动运行 */
	private boolean automatic;

	/** 是否禁用当前插件 */
	private boolean disabled;

	/** 插件描述 **/
	private String description;

    private File pluginFile;

	/**
	 * 构造器
     * @param classLoader
	 */
	public PluginMeta(ClassLoader classLoader) {
        this.classLoader = classLoader;
	}

	/**
	 * 构造器
     * @param classLoader
	 * @param id
	 * @param name
	 * @param alias
	 * @param initClass
	 * @param version
	 * @param path
	 * @param author
	 * @param email
	 * @param extraObj
	 * @param description
	 */
	public PluginMeta(ClassLoader classLoader, String id, String name, String alias, String initClass, String version, String path, String author, String email, Object extraObj, String description) {
		this.classLoader = classLoader;
		this.id = id;
		this.name = name;
		this.alias = alias;
		this.initClass = initClass;
		this.version = version;
		this.path = path;
		this.author = author;
		this.email = email;
		this.extraObj = extraObj;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getInitClass() {
		return initClass;
	}

	public void setInitClass(String initClass) {
		this.initClass = initClass;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Object getExtraObj() {
		return extraObj;
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	public boolean isAutomatic() {
		return automatic;
	}

	public void setAutomatic(boolean automatic) {
		this.automatic = automatic;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getDescription() {
		return description;
	}

    public File getPluginFile() {
        return pluginFile;
    }

    public void setPluginFile(File pluginFile) {
        this.pluginFile = pluginFile;
    }
}
