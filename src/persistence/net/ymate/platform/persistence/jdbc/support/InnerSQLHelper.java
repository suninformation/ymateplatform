/**
 * <p>文件名:	InnerSQLHelper.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-Persistence</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.persistence.jdbc.support;

import net.ymate.platform.commons.util.ExpressionUtils;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * InnerSQLHelper
 * </p>
 * <p>
 * 内置SQL语句拼装工具类，其与SQLHelper对象不同之处是其自身仅仅处理SQL字符串，不对Operator操作者对象进行管理；
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
 *          <td>2010-12-27上午10:58:43</td>
 *          </tr>
 *          </table>
 */
public class InnerSQLHelper {

	private String __sqlStr;

	/**
	 * 创建InnerSQLHelper（内置SQL语句拼装器）对象实例
	 * @param innerSql
	 * @return
	 */
	public static InnerSQLHelper create(String innerSql) {
		return new InnerSQLHelper(innerSql);
	}

	/**
	 * 构造器
	 * @param sql
	 */
	private InnerSQLHelper(String sql) {
		this.__sqlStr = sql;
	}
	/**
	 * 替换SQL串中占位符
	 * @param varName
	 * @param sqlStr
	 * @return
	 */
	public InnerSQLHelper replace(String varName, String sqlStr) {
		this.__sqlStr = ExpressionUtils.bind(__sqlStr).set(varName, StringUtils.isNotBlank(sqlStr) ? sqlStr : " ").getResult();
		return this;
	}

	/**
	 * 清除SQL串中占位符（即使用" "替换占位符）
	 * @param varName 占位符名称
	 * @return
	 */
	public InnerSQLHelper remove(String varName) {
		this.__sqlStr = ExpressionUtils.bind(__sqlStr).set(varName, " ").getResult();
		return this;
	}

	/**
	 * 获取生成的SQL串，若为null则返回为" "
	 * @return
	 */
	public String getSQL() {
		return StringUtils.isNotBlank(this.__sqlStr)? this.__sqlStr : " ";
	}

}
