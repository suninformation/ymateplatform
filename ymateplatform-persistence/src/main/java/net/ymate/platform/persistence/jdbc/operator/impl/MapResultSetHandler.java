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
package net.ymate.platform.persistence.jdbc.operator.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.ymate.platform.persistence.jdbc.operator.AbstractResultSetHandler;

/**
 * <p>
 * MapResultSetHandler
 * </p>
 * <p>
 * 采用 Map&lt;String, Object&gt; 存储数据的结果集数据处理接口实现类；
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
 *          <td>2011-10-25下午11:51:38</td>
 *          </tr>
 *          </table>
 */
public class MapResultSetHandler extends AbstractResultSetHandler<Map<String, Object>> {

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.AbstractResultSetHandler#processRowData(java.sql.ResultSet, java.util.List)
	 */
	public void processRowData(ResultSet rs, List<Map<String, Object>> result) throws SQLException {
		Map<String, Object> _rowMap = new LinkedHashMap<String, Object>(getColumnCount()); // 要保持字段的顺序!!
		for (int i = 0; i < getColumnCount(); i++) {
			String key = getColumnNames()[i].trim();
			Object value = rs.getObject(i + 1);
			_rowMap.put(key, value);
		}
		result.add(_rowMap);
	}

}
