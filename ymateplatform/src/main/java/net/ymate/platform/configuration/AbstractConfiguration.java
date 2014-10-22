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
package net.ymate.platform.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ymate.platform.configuration.provider.IConfigurationProvider;


/**
 * <p>
 * AbstractConfiguration
 * </p>
 * <p>
 * 配置信息对象基础抽象实现类，方便扩展实现；
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
 *          <td>2011-8-27上午01:57:05</td>
 *          </tr>
 *          </table>
 */
public abstract class AbstractConfiguration implements IConfiguration {

	private IConfigurationProvider __cfgProvider;
	private boolean __isInited;

	/* (non-Javadoc)
	 * @see net.ymate.platform.configuration.IConfiguration#contains(java.lang.String)
	 */
	public boolean contains(String key) {
		if (this.__cfgProvider != null) {
			return this.__cfgProvider.contains(key);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.configuration.IConfiguration#getArray(java.lang.String)
	 */
	public String[] getArray(String key) {
		return getArray(key, true);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.configuration.IConfiguration#getArray(java.lang.String, boolean)
	 */
	public String[] getArray(String key, boolean zeroSize) {
		if (this.__cfgProvider != null) {
			return this.__cfgProvider.getArray(key, zeroSize);
		}
		return zeroSize ? new String[] {} : null;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.configuration.IConfiguration#getBoolean(java.lang.String)
	 */
	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.configuration.IConfiguration#getBoolean(java.lang.String, boolean)
	 */
	public boolean getBoolean(String key, boolean defaultValue) {
		if (this.__cfgProvider != null) {
			return this.__cfgProvider.getBoolean(key, defaultValue);
		}
		return defaultValue;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.configuration.IConfiguration#getCfgTagName()
	 */
	public String getCfgTagName() {
		return ".cfg";
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.configuration.IConfiguration#getCfgsMap()
	 */
	public Map<String, String> getCfgsMap() {
		if (this.__cfgProvider != null) {
			return this.__cfgProvider.getCfgsMap();
		}
		return new HashMap<String, String>();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.configuration.IConfiguration#getDouble(java.lang.String)
	 */
	public double getDouble(String key) {
		return getDouble(key, 0.0F);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.configuration.IConfiguration#getDouble(java.lang.String, double)
	 */
	public double getDouble(String key, double defaultValue) {
		if (this.__cfgProvider != null) {
			return this.__cfgProvider.getDouble(key, defaultValue);
		}
		return defaultValue;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.configuration.IConfiguration#getFloat(java.lang.String)
	 */
	public float getFloat(String key) {
		return getFloat(key, 0.0F);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.configuration.IConfiguration#getFloat(java.lang.String, float)
	 */
	public float getFloat(String key, float defaultValue) {
		if (this.__cfgProvider != null) {
			return this.__cfgProvider.getFloat(key, defaultValue);
		}
		return defaultValue;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.configuration.IConfiguration#getInt(java.lang.String)
	 */
	public int getInt(String key) {
		return getInt(key, 0);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.configuration.IConfiguration#getInt(java.lang.String, int)
	 */
	public int getInt(String key, int defaultValue) {
		if (this.__cfgProvider != null) {
			return this.__cfgProvider.getInt(key, defaultValue);
		}
		return defaultValue;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.configuration.IConfiguration#getList(java.lang.String)
	 */
	public List<String> getList(String key) {
		if (this.__cfgProvider != null) {
			return this.__cfgProvider.getList(key);
		}
		return new ArrayList<String>();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.configuration.IConfiguration#getLong(java.lang.String)
	 */
	public long getLong(String key) {
		return getLong(key, 0L);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.configuration.IConfiguration#getLong(java.lang.String, long)
	 */
	public long getLong(String key, long defaultValue) {
		if (this.__cfgProvider != null) {
			return this.__cfgProvider.getLong(key, defaultValue);
		}
		return defaultValue;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.configuration.IConfiguration#getMap(java.lang.String)
	 */
	public Map<String, String> getMap(String keyHead) {
		if (this.__cfgProvider != null) {
			return this.__cfgProvider.getMap(keyHead);
		}
		return new HashMap<String, String>();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.configuration.IConfiguration#getString(java.lang.String)
	 */
	public String getString(String key) {
		return getString(key, "");
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.configuration.IConfiguration#getString(java.lang.String, java.lang.String)
	 */
	public String getString(String key, String defaultValue) {
		if (this.__cfgProvider != null) {
			return this.__cfgProvider.getString(key, defaultValue);
		}
		return defaultValue;
	}

    public List<String> getCategoryNames() {
        return __cfgProvider.getCategoryNames();
    }

	/* (non-Javadoc)
	 * @see net.ymate.platform.configuration.IConfiguration#initialize(net.ymate.platform.configuration.provider.IConfigurationProvider)
	 */
	public void initialize(IConfigurationProvider provider) {
		this.__cfgProvider = provider;
		this.__isInited = true;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.configuration.IConfiguration#isInited()
	 */
	public boolean isInited() {
		return this.__isInited;
	}

}
