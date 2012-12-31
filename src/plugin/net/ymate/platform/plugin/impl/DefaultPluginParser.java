/**
 * <p>文件名:	DefaultPluginParser.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.plugin.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.ymate.platform.commons.lang.BlurObject;
import net.ymate.platform.commons.logger.Logs;
import net.ymate.platform.commons.util.FileUtils;
import net.ymate.platform.commons.util.ResourceUtils;
import net.ymate.platform.commons.util.RuntimeUtils;
import net.ymate.platform.plugin.IPluginFactory;
import net.ymate.platform.plugin.IPluginParser;
import net.ymate.platform.plugin.PluginMeta;
import net.ymate.platform.plugin.PluginParserException;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * <p>
 * DefaultPluginParser
 * </p>
 * <p>
 * 默认插件主配置文件分析器接口实现类；
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
 *          <td>2012-12-2下午4:53:52</td>
 *          </tr>
 *          </table>
 */
public class DefaultPluginParser implements IPluginParser {

	private static final String ATTR_NAME = "name";
	private static final String ATTR_ALIAS = "alias";
	private static final String ATTR_CLASS = "class";
	private static final String ATTR_ID = "id";
	private static final String ATTR_VERSION = "version";
	private static final String ATTR_AUTHOR = "author";
	private static final String ATTR_EMAIL = "email";
	private static final String ATTR_AUTOMATIC = "automatic";
	private static final String ATTR_DISABLED = "disabled";
	private static final String ATTR_EXTRA_PART = "extra-part";
	private static final String ATTR_DESCRIPTION = "description";

	private IPluginFactory __pluginFactory;

	/* (non-Javadoc)
	 * @see net.ymate.platform.plugin.IPluginParser#doParser()
	 */
	public Map<String, PluginMeta> doParser() throws PluginParserException {
		Map<String, PluginMeta> _returnValue = new HashMap<String, PluginMeta>();
		try {
			// 首先加载当前CLASSPATH内的所有包含插件主配置文件的Jar包
			Iterator<URL> _configURLs = ResourceUtils.getResources(PLUGIN_XML_MAIN_CONFIG_FILE, this.getClass(), true);
			while (_configURLs.hasNext()) {
				PluginMeta _meta = __doManifestFileProcess(null, _configURLs.next());
				if (_meta != null) {
					_returnValue.put(_meta.getId(), _meta);
				}
			}
			// 然后尝试加载由PLUGIN_HOME指定的插件目录
			if (StringUtils.isNotBlank(__pluginFactory.getPluginConfig().getPluginHomePath())) {
				File _pluginDirFile = new File(__pluginFactory.getPluginConfig().getPluginHomePath());
				if (_pluginDirFile.exists() && _pluginDirFile.isDirectory()) {
                    File[] _subDirFiles = _pluginDirFile.listFiles();
					for (File _subDirFile : _subDirFiles != null ? _subDirFiles : new File[0]) {
						if (_subDirFile.isDirectory()) {
							File _manifestFile = new File(_subDirFile, PLUGIN_XML_MAIN_CONFIG_FILE);
							if (_manifestFile.exists() && _manifestFile.isFile()) {
								PluginMeta _meta = __doManifestFileProcess(__pluginFactory.getPluginConfig().getPluginHomePath(), _manifestFile.toURL());
								if (_meta != null) {
									_returnValue.put(_meta.getId(), _meta);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			throw new PluginParserException(RuntimeUtils.unwrapThrow(e));
		}
		return _returnValue;
	}

	/**
	 * 分析插件主配置文件
	 * 
	 * @param pluginHomePath
	 * @param configFileUrl
	 * @return
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	private PluginMeta __doManifestFileProcess(String pluginHomePath, URL configFileUrl) throws IOException, SAXException, ParserConfigurationException {
		Logs.debug("分析插件配置文件: " + configFileUrl.getFile());
		InputStream _in = configFileUrl.openStream();
		Document _document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(_in);
		Element _rootElement = _document.getDocumentElement();
		//
		String id = _rootElement.getAttribute(ATTR_ID);
		String name = _rootElement.getAttribute(ATTR_NAME);
		String alias = _rootElement.getAttribute(ATTR_ALIAS);
		String version = _rootElement.getAttribute(ATTR_VERSION);
		if (StringUtils.isBlank(id) || StringUtils.isBlank(name)) {
			Logs.warn("插件配置文件 " + configFileUrl.getFile() + " 中的 id 或 name 属性未设置, 此插件将被忽略.");
			return null;
		}
		//
		boolean disabled = false;
		NodeList _disabledNodes = _rootElement.getElementsByTagName(ATTR_DISABLED);
		if (_disabledNodes.getLength() > 0) {
			Node _node = _disabledNodes.item(0);
			disabled = new BlurObject(_node.getTextContent()).toBooleanValue();
		}
		if (disabled) {
			Logs.warn("插件配置文件 " + configFileUrl.getFile() + " 中的 disabled 属性已开启, 此插件将被忽略.");
			return null;
		}
		String initClass = _rootElement.getAttribute(ATTR_CLASS);
		String author = _rootElement.getAttribute(ATTR_AUTHOR);
		String email = _rootElement.getAttribute(ATTR_EMAIL);
		String description = "";
		NodeList _descriptionNodes  =_rootElement.getElementsByTagName(ATTR_DESCRIPTION);
		if (_descriptionNodes.getLength() > 0) {
			Node _node = _descriptionNodes.item(0);
			description = _node.getTextContent();
		}
		boolean automatic = false;
		NodeList _automacticNodes = _rootElement.getElementsByTagName(ATTR_AUTOMATIC);
		if (_automacticNodes.getLength() > 0) {
			Node _node = _automacticNodes.item(0);
			automatic = new BlurObject(_node.getTextContent()).toBooleanValue();
		}
		Object _extraObj = null;
		if (__pluginFactory.getPluginConfig().getPluginExtraParserClassImpl() != null) {
			NodeList _extraNodes = _rootElement.getElementsByTagName(ATTR_EXTRA_PART);
			if (_extraNodes.getLength() > 0) {
				_extraObj = __pluginFactory.getPluginConfig().getPluginExtraParserClassImpl().doExtarParser(_extraNodes.item(0));
			}
		}
		String _path = (StringUtils.isBlank(pluginHomePath) || FileUtils.toFile(configFileUrl) == null) ? "" : FileUtils.toFile(configFileUrl).getParent();
		PluginMeta _pluginMeta = new PluginMeta(this.__pluginFactory, id, name, alias, initClass, version, _path, author, email, _extraObj, description);
		_pluginMeta.setAutomatic(automatic);
		_pluginMeta.setDisabled(disabled);
		return _pluginMeta;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.plugin.IPluginParser#setPluginFactory(net.ymate.platform.plugin.IPluginFactory)
	 */
	public void setPluginFactory(IPluginFactory factory) {
		__pluginFactory = factory;
	}

}
