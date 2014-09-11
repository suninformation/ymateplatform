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
package net.ymate.platform.mvc.web.support;

import net.ymate.platform.configuration.AbstractConfiguration;
import net.ymate.platform.configuration.annotation.Configuration;
import net.ymate.platform.configuration.provider.IConfigurationProvider;
import net.ymate.platform.mvc.filter.IFilter;
import net.ymate.platform.mvc.view.IView;
import net.ymate.platform.mvc.web.WebMVC;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 当WebMVC开启Convention模式时，若存在此配置文件，则只有配置文件内的mapping地址才能正常访问
 *
 * @author 刘镇 (suninformation@163.com) on 14-7-31
 * @version 1.0
 */
@Configuration("cfgs/convention_mapping.cfg.xml")
public class ConventionMappingCfg extends AbstractConfiguration {

    private Map<String, Class<IFilter>> __filterNameMapping = new HashMap<String, Class<IFilter>>();

    @Override
    public void initialize(IConfigurationProvider provider) {
        super.initialize(provider);
        // 尝试注册自定义的过滤器
        Map<String, String> _filters = getMap("filters|_");
        Iterator<String> _nameIt = _filters.keySet().iterator();
        while (_nameIt.hasNext()) {
            String _name = _nameIt.next();
            if (StringUtils.isNotBlank(_name)) {
                String _value = _filters.get(_name);
                if (StringUtils.isNotBlank(_value)) {
                    try {
                        Class<IFilter> _filterClass = ClassUtils.getClass(_filters.get(_name));
                        WebMVC.registerController(_filterClass);
                        __filterNameMapping.put(_name, _filterClass);
                    } catch (ClassNotFoundException e) {
                        // 忽略不能正确分析的拦截器类
                    }
                }
            }
        }
    }

    /**
     * @param mapping 请求URL路径
     * @return 返回是否授权访问，true/false
     */
    public boolean matchRequestMapping(String mapping) {
        if (isInited()) {
            return getBoolean(mapping);
        }
        return false;
    }

    public IView doFilterChain(String mapping) throws Exception {
        Map<String, String> _filterNames = getMap(mapping + "|_");
        Iterator<String> _nameIt = _filterNames.keySet().iterator();
        IView _returnView = null;
        while (_nameIt.hasNext()) {
            String _name = _nameIt.next();
            if (StringUtils.isNotBlank(_name)) {
                String _value = _filterNames.get(_name);
                IFilter _filter = WebMVC.getControllerBeanFactory().get(__filterNameMapping.get(_name));
                if (_filter != null) {
                    _returnView = _filter.doFilter(null, _value);
                    if (_returnView != null) {
                        break;
                    }
                }
            }
        }
        return _returnView;
    }
}
