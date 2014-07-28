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
package net.ymate.platform.extra.scaffold;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * Tomcat独立JVM服务配置及Nigix和Apache服务负载均衡配置生成器
 *
 * @author 刘镇 (suninformation@163.com) on 14-7-25
 * @version 1.0
 */
public class TomcatServScaffold {

    private String __TEMPLATE_ROOT_PATH = TomcatServScaffold.class.getPackage().getName().replace(".", "/");
    private Configuration __FREEMARKER_CFG;

    /**
     * 需要创建的目录列表
     */
    private static String[] __NEED_MK_DIRS = new String[] {
            "bin",
            "conf",
            "logs",
            "temp",
            "webapps",
            "webapps/ROOT",
            "work"
    };

    /**
     * 需要复制的配置文件列表
     */
    private static String[] __NEED_COPY_FILES = new String[] {
            "conf/catalina.policy",
            "conf/catalina.properties",
            "conf/logging.properties",
            "conf/context.xml",
            "conf/tomcat-users.xml",
            "conf/web.xml"
    };

    private static String __PARAM_CATALINA_HOME = "catalina.home";
    private static String __PARAM_CATALINA_BASE = "catalina.base";
    private static String __PARAM_TOMCAT_VERSION = "tomcat.version";

    private static String __PARAM_HOST_NAME = "host.name";
    private static String __PARAM_HOST_ALIAS = "host.alias";

    private static String __PARAM_SERVICE_NAME = "service.name";
    private static String __PARAM_SERVER_PORT = "server.port";
    private static String __PARAM_CONNECTOR_PORT = "connector.port";
    private static String __PARAM_REDIRECT_PORT = "redirect.port";

    private static String __PARAM_AJP_HOST = "ajp.host";
    private static String __PARAM_AJP_PORT = "ajp.port";

    private Map<String, Object> __properties = new HashMap<String, Object>();

    private String __catalinaHome;
    private String __catalinaBase;
    private String __tomcatVersion;

    /**
     * 构造器，初始化命今参数并检查参数有效性
     */
    public TomcatServScaffold() {
        // 初始化模板引擎配置
        __FREEMARKER_CFG = new Configuration();
        __FREEMARKER_CFG.setClassForTemplateLoading(this.getClass(), "/");
        __FREEMARKER_CFG.setObjectWrapper(new DefaultObjectWrapper());
        __FREEMARKER_CFG.setDefaultEncoding("UTF-8");
        //
        __catalinaHome = System.getProperty(__PARAM_CATALINA_HOME);
        if (StringUtils.isBlank(__catalinaHome)) {
            throw new NullArgumentException(__PARAM_CATALINA_HOME);
        }
        __properties.put("catalina_home", __catalinaHome);
        //
        __catalinaBase = System.getProperty(__PARAM_CATALINA_BASE);
        if (StringUtils.isBlank(__catalinaBase)) {
            throw new NullArgumentException(__PARAM_CATALINA_BASE);
        }
        __properties.put("catalina_base", __catalinaBase);
        //
        __tomcatVersion = System.getProperty(__PARAM_TOMCAT_VERSION);
        if (StringUtils.isBlank(__tomcatVersion) || !(__tomcatVersion.equals("6") || __tomcatVersion.equals("7"))) {
            throw new IllegalArgumentException(__PARAM_TOMCAT_VERSION);
        }
        __properties.put("tomcat_version", __tomcatVersion);
        //
        String _hostName = System.getProperty(__PARAM_HOST_NAME);
        if (StringUtils.isBlank(_hostName)) {
            throw new NullArgumentException(__PARAM_HOST_NAME);
        }
        __properties.put("host_name", _hostName);
        __properties.put("host_alias", StringUtils.defaultIfEmpty(System.getProperty(__PARAM_HOST_ALIAS), ""));

        __properties.put("website_root_path", new File(__catalinaBase, "webapps/ROOT").getPath());
        //
        String _serviceName = System.getProperty(__PARAM_SERVICE_NAME);
        if (StringUtils.isBlank(_serviceName)) {
            throw new NullArgumentException(__PARAM_SERVICE_NAME);
        }
        __properties.put("service_name", _serviceName);
        //
        String _serverPort = System.getProperty(__PARAM_SERVER_PORT);
        if (StringUtils.isBlank(_serverPort)) {
            throw new NullArgumentException(__PARAM_SERVER_PORT);
        }
        if (!StringUtils.isNumeric(_serverPort)) {
            throw new IllegalArgumentException(__PARAM_SERVER_PORT);
        }
        __properties.put("server_port", _serverPort);
        //
        String _connectorPort = System.getProperty(__PARAM_CONNECTOR_PORT);
        if (StringUtils.isBlank(_connectorPort)) {
            throw new NullArgumentException(__PARAM_CONNECTOR_PORT);
        }
        if (!StringUtils.isNumeric(_connectorPort)) {
            throw new IllegalArgumentException(__PARAM_CONNECTOR_PORT);
        }
        __properties.put("connector_port", _connectorPort);
        //
        String _redirectPort = System.getProperty(__PARAM_REDIRECT_PORT);
        if (StringUtils.isBlank(_redirectPort)) {
            throw new NullArgumentException(__PARAM_REDIRECT_PORT);
        }
        if (!StringUtils.isNumeric(_redirectPort)) {
            throw new IllegalArgumentException(__PARAM_REDIRECT_PORT);
        }
        __properties.put("redirect_port", _redirectPort);
        //
        __properties.put("ajp_host", StringUtils.defaultIfEmpty(System.getProperty(__PARAM_AJP_HOST), "localhost"));
        //
        String _ajpPort = System.getProperty(__PARAM_AJP_PORT);
        if (StringUtils.isBlank(_ajpPort)) {
            throw new NullArgumentException(__PARAM_AJP_PORT);
        }
        if (!StringUtils.isNumeric(_ajpPort)) {
            throw new IllegalArgumentException(__PARAM_AJP_PORT);
        }
        __properties.put("ajp_port", _ajpPort);
    }

    /**
     * 创建目录结构
     *
     * @throws Exception
     */
    public void makeTomcatDirs() throws Exception {
        File _catalinaBaseDir = new File(this.__catalinaBase);
        if (!_catalinaBaseDir.isAbsolute()) {
            throw new IllegalArgumentException(this.__catalinaBase);
        } else if (!_catalinaBaseDir.exists()) {
            if (_catalinaBaseDir.mkdir()) {
                for (String _dirName : __NEED_MK_DIRS) {
                    new File(_catalinaBaseDir, _dirName).mkdir();
                }
            } else {
                throw new IOException(this.__catalinaBase);
            }
        }
    }

    /**
     * 复制配置文件
     *
     * @throws Exception
     */
    public void copyConfFiles() throws Exception {
        File _catalinaHomeDir = new File(this.__catalinaHome);
        if (!_catalinaHomeDir.isAbsolute() || !_catalinaHomeDir.exists() || !_catalinaHomeDir.isDirectory()) {
            throw new FileNotFoundException(this.__catalinaHome);
        }
        //
        for (String _fileName : __NEED_COPY_FILES) {
            FileUtils.copyFile(new File(_catalinaHomeDir, _fileName), new File(this.__catalinaBase, _fileName));
        }
    }

    /**
     * 生成server.xml等相关配置文件
     *
     * @throws Exception
     */
    public void buildConfigFile() {
        __doWriterTargetFile("conf/server.xml", "/tomcatserv/tmpl/v" + this.__tomcatVersion + "/server-xml.ftl", __properties);
        //
        __doWriterTargetFile("vhost.conf", "/tomcatserv/tmpl/vhost-conf.ftl", __properties);
    }

    public void buildCommandlineFiles() {
        __doWriterTargetFile("bin/install.bat", "/tomcatserv/tmpl/install-cmd.ftl", __properties);
        __doWriterTargetFile("bin/manager.bat", "/tomcatserv/tmpl/manager-cmd.ftl", __properties);
//        __doWriterTargetFile("bin/setenv.bat", "/tomcatserv/tmpl/setenv-cmd.ftl", __properties);
        __doWriterTargetFile("bin/shutdown.bat", "/tomcatserv/tmpl/shutdown-cmd.ftl", __properties);
        __doWriterTargetFile("bin/startup.bat", "/tomcatserv/tmpl/startup-cmd.ftl", __properties);
        __doWriterTargetFile("bin/uninstall.bat", "/tomcatserv/tmpl/uninstall-cmd.ftl", __properties);
        //
        __doWriterTargetFile("bin/manager.sh", "/tomcatserv/tmpl/manager-sh.ftl", __properties);
    }

    private void __doWriterTargetFile(String targetFileName, String tmplFile, Map<String, Object> properties) {
        Writer _outWriter = null;
        try {
            File _outputFile = new File(this.__catalinaBase, targetFileName);
            _outWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(_outputFile), StringUtils.defaultIfEmpty(__FREEMARKER_CFG.getOutputEncoding(), __FREEMARKER_CFG.getDefaultEncoding())));
            Template _template = __FREEMARKER_CFG.getTemplate(__TEMPLATE_ROOT_PATH + tmplFile);
            _template.process(properties, _outWriter);
            System.out.println("Output file: " + _outputFile);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            if (_outWriter != null) {
                try {
                    _outWriter.flush();
                    _outWriter.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * 主程序
     * <p>
     * <code>
     * 命令行: mvn compile exec:java -Dexec.mainClass="net.ymate.platform.extra.scaffold.TomcatServScaffold" -Dcatalina.home="/Users/suninformation/Java/apache-tomcat-6.0.39" -Dcatalina.base="/Users/suninformation/Temp/demoServ" -Dtomcat.version="6" -Dhost.name="www.ymate.net" -Dhost.alias="ymate.net" -Dservice.name="demoServ" -Dserver.port="8005" -Dconnector.port="8080" -Dredirect.port="8443" -Dajp.port="8009"
     * </code>
     * </p>
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        TomcatServScaffold _scaffold = new TomcatServScaffold();
        _scaffold.makeTomcatDirs();
        _scaffold.copyConfFiles();
        _scaffold.buildConfigFile();
        _scaffold.buildCommandlineFiles();
    }
}
