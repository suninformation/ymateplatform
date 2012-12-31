/**
 * <p>文件名:	SqlBatchParameter.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-Persistence</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.persistence.jdbc.base;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * SqlBatchParameter
 * </p>
 * <p>
 * 基于批量处理的SQL参数对象类；
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
 *          <td>2011-8-28上午01:52:39</td>
 *          </tr>
 *          </table>
 */
public class SqlBatchParameter {

	/**
	 * 存放SQL参数的集合
	 */
	private List<SqlParameter> __parameters = new ArrayList<SqlParameter>();

	/**
	 * 构造器
	 */
	public SqlBatchParameter() {
	}

	/**
	 * 添加批参数
	 * 
	 * @param paramter SQL 参数对象
	 * @return 批参数对象
	 */
	public SqlBatchParameter addParameter(SqlParameter paramter) {
		if (paramter != null) {
			this.__parameters.add(paramter);
		}
		return this;
	}

	/**
	 * 添加批参数值对象
	 * 
	 * @param paramter 参数值
	 * @return 批参数对象
	 */
	public SqlBatchParameter addParameter(Object paramter) {
		if (paramter == null) {
			this.__parameters.add(new SqlParameter(Types.VARCHAR, null));
		} else {
			this.__parameters.add(new SqlParameter(paramter));
		}
		return this;
	}

	/**
	 * @return 获取批参数集合
	 */
	public List<SqlParameter> getSqlParameterSet() {
		return this.__parameters;
	}

}
