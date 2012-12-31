/**
 * <p>文件名:	LogModule.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.module;

import java.util.Map;

import net.ymate.platform.commons.lang.BlurObject;
import net.ymate.platform.commons.logger.ILogConfig;
import net.ymate.platform.commons.logger.ILogger;
import net.ymate.platform.commons.logger.ILogger.LogLevel;
import net.ymate.platform.commons.logger.Logs;
import net.ymate.platform.commons.logger.impl.DefaultLog4JLogger;
import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.commons.util.ExpressionUtils;
import net.ymate.platform.commons.util.RuntimeUtils;
import net.ymate.platform.configuration.Cfgs;
import net.ymate.platform.module.base.IModule;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * LogModule
 * </p>
 * <p>
 * 日志记录器模块加载器接口实现类；
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
 *          <td>2012-12-23下午6:26:42</td>
 *          </tr>
 *          </table>
 */
public class LogModule implements IModule {

	/* (non-Javadoc)
	 * @see net.ymate.platform.module.base.IModule#initialize(java.util.Map)
	 */
	public void initialize(final Map<String, String> moduleCfgs) throws Exception {
		Logs.initialize(new ILogConfig() {

			public String getLogCfgFile() {
				String _logCfgF = moduleCfgs.get("xml_cfg_file");
				if (StringUtils.isBlank(_logCfgF)) {
					if (Cfgs.isInited()) {
						_logCfgF = Cfgs.getUserDir() + "cfgs/log4j.xml";
					} else {
						_logCfgF = RuntimeUtils.getRootPath() + "cfgs/log4j.xml";
					}
				} else if (_logCfgF.contains("${user.dir}")) {
					if (Cfgs.isInited()) {
						_logCfgF = ExpressionUtils.bind(_logCfgF).set("user.dir", Cfgs.getUserDir()).getResult();
					} else {
						_logCfgF = ExpressionUtils.bind(_logCfgF).set("user.dir", RuntimeUtils.getRootPath()).getResult();
					}
				}
				return _logCfgF;
			}

			public String getLogOutputDir() {
				String _logOutputF = moduleCfgs.get("output_path");
				if (StringUtils.isBlank(_logOutputF)) {
					if (Cfgs.isInited()) {
						_logOutputF = Cfgs.getUserDir() + "logs";
					} else {
						_logOutputF = RuntimeUtils.getRootPath() + "logs";
					}
				} else if (_logOutputF.contains("${user.dir}")) {
					if (Cfgs.isInited()) {
						_logOutputF = ExpressionUtils.bind(_logOutputF).set("user.dir", Cfgs.getUserDir()).getResult();
					} else {
						_logOutputF = ExpressionUtils.bind(_logOutputF).set("user.dir", RuntimeUtils.getRootPath()).getResult();
					}
				}
				return _logOutputF;
			}

			public String getLoggerName() {
				return StringUtils.defaultIfEmpty(moduleCfgs.get("name"), DEFAULT_LOGGER_NAME);
			}

			public LogLevel getLogLevel() {
				return LogLevel.parse(moduleCfgs.get("level"));
			}

			public boolean allowPrintConsole() {
				return new BlurObject(moduleCfgs.get("allow_print_console")).toBooleanValue();
			}

			public ILogger getLoggerClassImpl() {
				return ClassUtils.impl(StringUtils.defaultIfEmpty(moduleCfgs.get("impl_class"), DefaultLog4JLogger.class.getName()), ILogger.class, LogModule.class);
			}
			
		});
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.module.base.IModule#destroy()
	 */
	public void destroy() throws Exception {
		Logs.clear();
	}

}
