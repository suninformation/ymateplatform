/**
 * <p>文件名:	ICompositeQuery.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMateSmartPlatform</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.persistence.jdbc.query;


/**
 * <p>
 * ICompositeQuery
 * </p>
 * <p>
 * 组合子查询接口定义类，仅用于SQL生成；
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
 *          <td>2011-6-23上午09:01:46</td>
 *          </tr>
 *          </table>
 */
public interface ICompositeSubQuery {

	/**
	 * @return 获取WHERE条件查询时与已存在的条件关系连接运算符号，如：AND或OR等，若为NULL则默认采用 "AND"
	 */
	public String getQueryWhereConditionType();

	/**
	 * 设置WHERE条件查询时与已存在的条件关系连接运算符号，如：AND或OR等，若为NULL则默认采用 "AND"
	 * 
	 * @param conditionType
	 * @return
	 */
	public ICompositeSubQuery setQueryWhereConditionType(String conditionType);

	/**
	 * @return 生成查询SQL语句
	 */
	public String buildSql();

}
