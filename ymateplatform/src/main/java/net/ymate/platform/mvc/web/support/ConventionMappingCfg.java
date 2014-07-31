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

/**
 * 当WebMVC开启Convention模式时，若存在此配置文件，则只有配置文件内的mapping地址才能正常访问
 *
 * @author 刘镇 (suninformation@163.com) on 14-7-31
 * @version 1.0
 */
@Configuration("cfgs/convention_mapping.cfg.xml")
public class ConventionMappingCfg extends AbstractConfiguration {
}
