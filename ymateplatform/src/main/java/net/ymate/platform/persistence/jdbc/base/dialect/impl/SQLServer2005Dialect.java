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
package net.ymate.platform.persistence.jdbc.base.dialect.impl;

import net.ymate.platform.persistence.jdbc.base.dialect.AbstractDialect;

/**
 * <p>
 * SQLServer2005Dialect
 * </p>
 * <p>
 * SQLServer 2005 及以上数据库方言接口实现类；
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
 *          <td>2012-4-19下午3:38:40</td>
 *          </tr>
 *          </table>
 */
public class SQLServer2005Dialect extends AbstractDialect {

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.base.IDialect#getDialectName()
	 */
	public String getDialectName() {
		return "SQL Server";
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.base.dialect.AbstractDialect#wapperQuotedIdent(java.lang.String)
	 */
	@Override
	public String wapperQuotedIdent(String source) {
		return "[" + source + "]";
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.base.IDialect#getPaginationSql(java.lang.String, int, int)
	 */
	public String getPaginationSql(String sql, int limit, int offset) {
		StringBuilder _returnValue = new StringBuilder(sql.length()+100);
        if (offset == 0){
        	_returnValue.insert(getSqlAfterSelectInsertPoint(sql), " top " + limit);
        } else{
        	int orderByIndex = sql.toLowerCase().lastIndexOf("order by");
            if (orderByIndex <= 0) {
                throw new UnsupportedOperationException("must specify 'order by' statement to support limit operation with offset in sql server 2005");
            }
            String sqlOrderBy = sql.substring(orderByIndex + 8);
            String sqlRemoveOrderBy = sql.substring(0, orderByIndex);
     
            int insertPoint = getSqlAfterSelectInsertPoint(sql);
            _returnValue.append("with tempPagination as(")
                    .append(sqlRemoveOrderBy)
                    .insert(insertPoint + 23, " ROW_NUMBER() OVER(ORDER BY " + sqlOrderBy + ") as RowNumber,").append(") select * from tempPagination where RowNumber > ").append(limit).append(" and RowNumber <= ").append(limit + offset);
        }
        return _returnValue.toString();
	}

	/**
	 * @param sql SQL语句
	 * @return 获取 SQL 中 select 子句位置
	 */
	protected static int getSqlAfterSelectInsertPoint(String sql) { 
        int selectIndex = sql.toLowerCase().indexOf("select"); 
        int selectDistinctIndex = sql.toLowerCase().indexOf("select distinct"); 
        return selectIndex + ((selectDistinctIndex == selectIndex) ? 15 : 6); 
    }

}
