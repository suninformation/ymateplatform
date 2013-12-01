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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.ymate.platform.commons.lang.PairObject;
import net.ymate.platform.mvc.context.IRequestContext;
import net.ymate.platform.mvc.web.context.WebContext;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * RequestMappingParser
 * </p>
 * <p>
 * WebMVC请求映射处理分析器；
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
 *          <td>2011-7-26上午11:11:45</td>
 *          </tr>
 *          </table>
 */
public class RequestMappingParser {

	/**
	 * @param partStr 参数段
	 * @return 返回去掉首尾'/'字符的串
	 */
	protected String fixMappingPart(String partStr) {
		partStr = StringUtils.trimToEmpty(partStr);
		if (StringUtils.startsWith(partStr, "/")) {
			partStr = StringUtils.substringAfter(partStr, "/");
		}
		if (StringUtils.endsWith(partStr, "/")) {
			partStr = StringUtils.substringBeforeLast(partStr, "/");
		}
		return partStr;
	}

	/**
	 * @param mappingParamPart 请求映射参数段
	 * @param mappingMatcher 请求映射参数匹配规则
	 * @return 返回根据mappingMatcher提取请求映射中的参数值
	 */
	protected Map<String, String> parserMappingParams(String mappingParamPart, String mappingMatcher) {
		Map<String, String> _params = new HashMap<String, String>();
		String[] _paramNames = StringUtils.split(mappingMatcher, "/");
		String[] _parts = StringUtils.split(mappingParamPart, "/");
		if (_parts.length >= _paramNames.length) {
			for (int _index = 0; _index < _paramNames.length; _index++) {
				String _pName = StringUtils.substringBetween(_paramNames[_index], "{", "}");
				if (_pName != null) {
					_params.put(_pName, _parts[_index]);
				}
			}
		}
		return _params;
	}

	/**
	 * @param context 请求上下文对象
	 * @param mappingSet 请求映射键值集合（注：集合中的映射串必须是包含'$'字符的参数定义）
	 * @return 分析请求映射串，匹配成功则返回对应映射集合的键值，同时处理请求串中的参数变量存入WebContext容器中的PathVariable参数池
	 */
	public String doParser(IRequestContext context, Set<String> mappingSet) {
		String _requestMapping = this.fixMappingPart(context.getRequestMapping());
		// 过滤出匹配程度较高的映射键值集合：PairObject<Mapping分解数组[原始，前缀段，参数段], 参数段数量>
		Set<PairObject<String[], Integer>> _filteredMapping = new HashSet<PairObject<String[], Integer>>();
		for (String _key : mappingSet) {
			if (_key.contains("{")) {
				String _mappingKey = StringUtils.substringBefore(_key, "{");
				String _fixedMappingKey = this.fixMappingPart(_mappingKey);
				if (StringUtils.startsWithIgnoreCase(_requestMapping, _fixedMappingKey)) {
					// 截取参数段，并分析参数段数量
					String _paramKey = _key.substring(_key.indexOf("{"));
					_filteredMapping.add(new PairObject<String[], Integer>(new String[] { _key, _mappingKey, _fixedMappingKey, _paramKey }, StringUtils.split(_paramKey, "/").length));
				}
			}
		}
		// 遍历已过滤映射集合并通过与请求映射串比较参数数量，找出最接近的一个
		PairObject<String[], Integer> _result = null;
		String _mappingParamPart = null;
		for (PairObject<String[], Integer> _item : _filteredMapping) {
			// 提取当前请求映射的参数段
			_mappingParamPart = StringUtils.substringAfter(_requestMapping, _item.getKey()[2]);
			// 计算当前请求映射的参数段数量
			int _paramPartCount = StringUtils.split(_mappingParamPart, "/").length;
			// 匹配参数段数量，若数量相同则直接返回
			if (_paramPartCount == _item.getValue()) {
				_result = _item;
				break;
			}
		}
		if (_result != null) {
			Map<String, String> _params = this.parserMappingParams(_mappingParamPart, _result.getKey()[3]);
			if (WebContext.getContext() != null) {
				// 参数变量存入WebContext容器中的PathVariable参数池
				for (String _key : _params.keySet()) {
					WebContext.getContext().put(_key, _params.get(_key));
				}
			}
			return _result.getKey()[0];
		}
		return null;
	}

}
