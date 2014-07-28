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
package net.ymate.platform.plugin.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.ymate.platform.base.YMP;
import net.ymate.platform.commons.i18n.I18N;
import net.ymate.platform.commons.lang.BlurObject;
import net.ymate.platform.commons.util.FileUtils;
import net.ymate.platform.commons.util.ResourceUtils;
import net.ymate.platform.commons.util.RuntimeUtils;

import net.ymate.platform.plugin.IPluginFactory;
import net.ymate.platform.plugin.IPluginParser;
import net.ymate.platform.plugin.PluginClassLoader;
import net.ymate.platform.plugin.PluginMeta;
import net.ymate.platform.plugin.PluginParserException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

	private static final Log _LOG = LogFactory.getLog(DefaultPluginParser.class);

	private static final String PLUGIN_TAG = "plugin";
	
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
            List<String> _excludePluginIds = new ArrayList<String>();
            Iterator<URL> _configURLs = ResourceUtils.getResources(__pluginFactory.getPluginConfig().getPluginManifestFile(), this.getClass(), true);
            while (_configURLs.hasNext()) {
                URL _configURL = _configURLs.next();
                List<PluginMeta> _metas = __doManifestFileProcess(__pluginFactory.getPluginClassLoader(), null, _configURL);
                // 若设置为包含CLASSPATH内插件则加载，否则添加至排除集合中
                for (PluginMeta _meta : _metas) {
                    if (__pluginFactory.getPluginConfig().isIncludeClassPath()) {
                        _returnValue.put(_meta.getId(), _meta);
                    }
                    _excludePluginIds.add(_meta.getId());
                }
            }
			// 然后尝试加载由PLUGIN_HOME指定的插件目录
			if (StringUtils.isNotBlank(__pluginFactory.getPluginConfig().getPluginHomePath())) {
				File _pluginDirFile = new File(__pluginFactory.getPluginConfig().getPluginHomePath());
				if (_pluginDirFile.exists() && _pluginDirFile.isDirectory()) {
                    File[] _subDirFiles = _pluginDirFile.listFiles();
					for (File _subDirFile : _subDirFiles != null ? _subDirFiles : new File[0]) {
						if (_subDirFile.isDirectory()) {
                            ClassLoader _currentLoader = __doCreatePluginClassLoader(_subDirFile.getPath());
                            // 如果插件目录中存在主配置文件，则优先处理
							File _manifestFile = new File(_subDirFile, __pluginFactory.getPluginConfig().getPluginManifestFile());
							if (_manifestFile.exists() && _manifestFile.isFile()) {
								List<PluginMeta> _metas = __doManifestFileProcess(_currentLoader, _subDirFile.getPath(), _manifestFile.toURI().toURL());
								for (PluginMeta _meta : _metas) {
									_returnValue.put(_meta.getId(), _meta);
								}
							} else {
                                // 否则扫描类路径和JAR包中资源
                                Enumeration<URL> _pluginConfigURLs = _currentLoader.getResources(__pluginFactory.getPluginConfig().getPluginManifestFile());
                                while (_pluginConfigURLs.hasMoreElements()) {
                                    List<PluginMeta> _metas = __doManifestFileProcess(_currentLoader, _subDirFile.getPath(), _pluginConfigURLs.nextElement());
                                    for (PluginMeta _meta : _metas) {
                                        if (_excludePluginIds.contains(_meta.getId())) {
                                            // CLASSPATH中的插件Jar包会被重复加载，在这里判断当前插件是否在排除列表中
                                            break;
                                        }
                                        _returnValue.put(_meta.getId(), _meta);
                                    }
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
     * 创建基于插件HOME的插件类加载器
     *
     * @param path
     * @return
     * @throws MalformedURLException
     */
    private ClassLoader __doCreatePluginClassLoader(String path) throws MalformedURLException {
        _LOG.info(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.plugin.create_plugin_loader", path));
        ArrayList<URL> _libList = new ArrayList<URL>();
        // 设置JAR包路径
        File _pluginLibDir = new File(FileUtils.fixSeparator(path) + "lib");
        if (_pluginLibDir.exists() && _pluginLibDir.isDirectory()) {
            File[] _libFiles = _pluginLibDir.listFiles();
            for (File _libFile : _libFiles != null ? _libFiles : new File[0]) {
                if (_libFile.isFile() && _libFile.getAbsolutePath().endsWith("jar")) {
                    _libList.add(_libFile.toURI().toURL());
                    _LOG.info(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.plugin.plugin_load_jar_file", path, _libFile.getPath()));
                }
            }
        }
        // 设置类文件路径
        _pluginLibDir = new File(FileUtils.fixSeparator(path) + "classes");
        if (_pluginLibDir.exists() && _pluginLibDir.isDirectory()) {
            _libList.add(_pluginLibDir.toURI().toURL());
            _LOG.info(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.plugin.plugin_load_classpath", path, _pluginLibDir.getPath()));
        }
        _LOG.info(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.plugin.create_plugin_loader_final", path));
        return new PluginClassLoader(_libList.toArray(new URL[0]), __pluginFactory.getPluginClassLoader());
    }

	/**
	 * 分析插件主配置文件
	 *
     * @param classLoader
	 * @param pluginPath
	 * @param configFileUrl
	 * @return
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	private List<PluginMeta> __doManifestFileProcess(ClassLoader classLoader, String pluginPath, URL configFileUrl) throws IOException, SAXException, ParserConfigurationException {
		_LOG.info(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.plugin.parse_plugin_file", configFileUrl.getFile()));
		List<PluginMeta> _returnValue = new ArrayList<PluginMeta>();
		//
		List<Element> _pluginElements = new ArrayList<Element>();
		//
		InputStream _in = configFileUrl.openStream();
		Document _document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(_in);
		Element _rootElement = _document.getDocumentElement();
        // 插件组
        boolean _pluginGroupFlag = false;
        String _pluginAuthor = _rootElement.getAttribute(ATTR_AUTHOR);
        String _pluginEmail = _rootElement.getAttribute(ATTR_EMAIL);
        String _pluginVersion = _rootElement.getAttribute(ATTR_VERSION);
        boolean _authorNotNull = StringUtils.isNotBlank(_pluginAuthor);
        boolean _emailNotNull = StringUtils.isNotBlank(_pluginEmail);
        boolean _versionNotNull = StringUtils.isNotBlank(_pluginVersion);
        //
		if (_rootElement.getNodeName().equals(PLUGIN_TAG)) {
			_pluginElements.add(_rootElement);
		} else {
            // 当前插件主配置文件包含多个插件配置，即插件组
            _pluginGroupFlag = true;
            _pluginAuthor = _rootElement.getAttribute(ATTR_AUTHOR);
            _pluginEmail = _rootElement.getAttribute(ATTR_EMAIL);
            _pluginVersion = _rootElement.getAttribute(ATTR_VERSION);
            //
			NodeList _pluginNodes = _rootElement.getElementsByTagName(PLUGIN_TAG);
			for (int _idx = 0; _idx < _pluginNodes.getLength(); _idx++) {
				_pluginElements.add((Element) _pluginNodes.item(_idx));
			}
		}
		for (Element _pluginElement : _pluginElements) {
			PluginMeta _pluginMeta = __doPluginElementProcess(classLoader, _pluginElement, pluginPath, configFileUrl);
			if (_pluginMeta != null) {
                // 若为插件组，则尝试完善插件描述对象属性信息
                if (_pluginGroupFlag) {
                    if (_authorNotNull && StringUtils.isBlank(_pluginMeta.getAuthor())) {
                        _pluginMeta.setAuthor(_pluginAuthor);
                    }
                    if (_emailNotNull && StringUtils.isBlank(_pluginMeta.getEmail())) {
                        _pluginMeta.setEmail(_pluginEmail);
                    }
                    if (_versionNotNull && StringUtils.isBlank(_pluginMeta.getVersion())) {
                        _pluginMeta.setVersion(_pluginVersion);
                    }
                }
				_returnValue.add(_pluginMeta);
			}
		}
		return _returnValue;
	}

	private PluginMeta __doPluginElementProcess(ClassLoader classLoader, Element pluginElement, String pluginPath, URL configFileUrl) {
		String id = pluginElement.getAttribute(ATTR_ID);
		String name = pluginElement.getAttribute(ATTR_NAME);
		String alias = pluginElement.getAttribute(ATTR_ALIAS);
		String version = pluginElement.getAttribute(ATTR_VERSION);
        String initClass = pluginElement.getAttribute(ATTR_CLASS);
		if ((StringUtils.isBlank(alias) || StringUtils.isBlank(name)) && StringUtils.isBlank(initClass)) {
			_LOG.warn(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.plugin.plugin_will_be_ignored", configFileUrl.getFile()));
			return null;
		}
        if (StringUtils.isBlank(id)) {
            // 若插件ID为填写则使用初始化类名称进行MD5加密后的值做为ID
            id = DigestUtils.md5Hex(initClass);
        }
		//
		boolean disabled = false;
		NodeList _disabledNodes = pluginElement.getElementsByTagName(ATTR_DISABLED);
		if (_disabledNodes.getLength() > 0) {
			Node _node = _disabledNodes.item(0);
			disabled = new BlurObject(_node.getTextContent()).toBooleanValue();
		}
		if (disabled) {
			_LOG.warn(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.plugin.plugin_will_be_disabled", configFileUrl.getFile()));
			return null;
		}
		String author = pluginElement.getAttribute(ATTR_AUTHOR);
		String email = pluginElement.getAttribute(ATTR_EMAIL);
		String description = "";
		NodeList _descriptionNodes  =pluginElement.getElementsByTagName(ATTR_DESCRIPTION);
		if (_descriptionNodes.getLength() > 0) {
			Node _node = _descriptionNodes.item(0);
			description = _node.getTextContent();
		}
		boolean automatic = false;
		NodeList _automacticNodes = pluginElement.getElementsByTagName(ATTR_AUTOMATIC);
		if (_automacticNodes.getLength() > 0) {
			Node _node = _automacticNodes.item(0);
			automatic = new BlurObject(_node.getTextContent()).toBooleanValue();
		}
		Object _extraObj = null;
		if (__pluginFactory.getPluginConfig().getPluginExtraParserClassImpl() != null) {
			NodeList _extraNodes = pluginElement.getElementsByTagName(ATTR_EXTRA_PART);
			if (_extraNodes.getLength() > 0) {
				_extraObj = __pluginFactory.getPluginConfig().getPluginExtraParserClassImpl().doExtarParser(_extraNodes.item(0));
			}
		}
		PluginMeta _pluginMeta = new PluginMeta(classLoader, id, name, alias, initClass, version, pluginPath, author, email, _extraObj, description);
		_pluginMeta.setAutomatic(automatic);
		_pluginMeta.setDisabled(disabled);
        if (configFileUrl.getProtocol().equals("jar")) {
            _pluginMeta.setPluginFile(new File(StringUtils.substringBetween(configFileUrl.getFile(), "file:", "!")));
        }
		return _pluginMeta;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.plugin.IPluginParser#setPluginFactory(net.ymate.platform.plugin.IPluginFactory)
	 */
	public void setPluginFactory(IPluginFactory factory) {
		__pluginFactory = factory;
	}

}
