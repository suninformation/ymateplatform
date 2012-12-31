/**
 * <p>文件名:	MySqlDialect.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-Persistence</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.persistence.jdbc.base.dialect.impl;

import net.ymate.platform.persistence.jdbc.base.dialect.AbstractDialect;


/**
 * <p>
 * MySqlDialect
 * </p>
 * <p>
 * MySql 数据库方言接口实现类；
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
public class MySqlDialect extends AbstractDialect {

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.base.IDialect#getDialectName()
	 */
	public String getDialectName() {
		return "MySql";
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.base.IDialect#getPaginationSql(java.lang.String, int, int)
	 */
	public String getPaginationSql(String sql, int limit, int offset) {
		if (offset == 0) {
			return sql + " limit " + Integer.toString(limit);
		} else {
			return sql + " limit " + Integer.toString(limit) + "," + Integer.toString(offset);
		}
	}

}
