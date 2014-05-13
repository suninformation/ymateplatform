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
package net.ymate.platform.persistence.jdbc.base.dialect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * AbstractDialect
 * </p>
 * <p>
 * 数据库方言接口抽象实现类；
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
 *          <td>2011-8-30下午01:55:13</td>
 *          </tr>
 *          </table>
 */
public abstract class AbstractDialect implements IDialect {

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.base.dialect.IDialect#wapperQuotedIdent(java.lang.String)
	 */
	public String wapperQuotedIdent(String source) {
		return source;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.base.IDialect#getGeneratedKey(java.sql.Statement)
	 */
	public Object[] getGeneratedKey(Statement statement) throws SQLException {
		Object[] _returnValue = null;
		// 检索由于执行此 Statement 对象而创建的所有自动生成的键
		ResultSet _keyRSet = null;
		try {
			List<Long> _ids = new ArrayList<Long>();
			_keyRSet = statement.getGeneratedKeys();
			while (_keyRSet.next()) {
				// _keyRSet.getMetaData().getColumnClassName(0).............;
				_ids.add(_keyRSet.getLong(1));
			}
			_returnValue = _ids.toArray();
		} finally {
			_keyRSet = null;
		}
		return _returnValue;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.base.dialect.IDialect#getSequenceNextValSql(java.lang.String)
	 */
	public String getSequenceNextValSql(String sequenceName) {
		throw new UnsupportedOperationException();
	}

}
