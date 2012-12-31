/**
 * <p>文件名:	DefaultModuleLoader.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.module.base.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.ymate.platform.module.base.IModule;
import net.ymate.platform.module.base.IModuleLoader;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * DefaultModuleLoader
 * </p>
 * <p>
 * 模块加载器接口默认实现类；
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
 *          <td>2012-12-23下午6:08:03</td>
 *          </tr>
 *          </table>
 */
public class DefaultModuleLoader implements IModuleLoader {

	private List<IModule> __loadedModules = new ArrayList<IModule>();

	/* (non-Javadoc)
	 * @see net.ymate.platform.module.base.IModuleLoader#initialize(java.util.Properties)
	 */
	public void initialize(Properties configs) throws Exception {
		String[] _moduleList = StringUtils.split(configs.getProperty("ymp.module_list"), '|');
		if (_moduleList != null && _moduleList.length > 0) {
			for(String _moduleName : _moduleList) {
				String _moduleClassName = configs.getProperty("ymp.modules." + _moduleName);
				if (StringUtils.isNotBlank(_moduleClassName)) {
					IModule _module = (IModule) Class.forName(_moduleClassName).newInstance();
					_module.initialize(__parseModuleCfg(_moduleName, configs));
					__loadedModules.add(_module);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.module.base.IModuleLoader#destroy()
	 */
	public void destroy() throws Exception {
		for (IModule _module : __loadedModules) {
			_module.destroy();
		}
		__loadedModules.clear();
	}

	/**
	 * @param moduleName 模块名称
	 * @param configs 配置参数集合
	 * @return 分析提取模块配置映射
	 */
	private Map<String, String> __parseModuleCfg(String moduleName, Properties configs) {
		Map<String, String> _returnValue = new HashMap<String, String>();
		// 提取模块配置
		for(Object _key : configs.keySet()) {
			String _prefix = "ymp.configs." + moduleName + ".";
			if (StringUtils.startsWith((String) _key, _prefix)) {
				String _cfgKey = StringUtils.substring((String) _key, _prefix.length());
				String _cfgValue = configs.getProperty((String) _key);
				_returnValue.put(_cfgKey, _cfgValue);
			}
		}
		return _returnValue;
	}

}
