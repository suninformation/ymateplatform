/**
 * <p>文件名:	IUpdateBatchOperator.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-Persistence</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.persistence.jdbc.operator;

import java.util.List;

import net.ymate.platform.persistence.jdbc.base.SqlBatchParameter;
import net.ymate.platform.persistence.jdbc.base.SqlParameter;


/**
 * <p>
 * IUpdateBatchOperator
 * </p>
 * <p>
 * 数据库批量更新操作者接口定义类；
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
 *          <td>2010-12-27下午12:02:56</td>
 *          </tr>
 *          </table>
 */
public interface IUpdateBatchOperator extends IOperator {

	/**
	 * 添加 SQL 语句的参数（适用于批处理）
	 * 
	 * @param parameter SQL 语句参数对象集合
	 */
	public void addBatchParameter(SqlBatchParameter parameter);

	/**
	 * @return 获取批处理 SQL 参数集合
	 */
	public List<List<SqlParameter>> getBatchParameters();

	/**
	 * @return 获取批更新操作后每条SQL语句影响的记录行数
	 */
	public int[] getBatchEffectCounts();

}
