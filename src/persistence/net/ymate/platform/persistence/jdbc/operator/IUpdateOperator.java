/**
 * <p>文件名:	IUpdateOperator.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-Persistence</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.persistence.jdbc.operator;

/**
 * <p>
 * IUpdateOperator
 * </p>
 * <p>
 * 数据库更新操作者接口定义类；
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
 *          <td>2010-12-25下午02:39:48</td>
 *          </tr>
 *          </table>
 */
public interface IUpdateOperator extends IOperator {

	/**
	 * 获取更新操作执行后影响的记录行数，不支持批量更新语句；
	 * 对于 INSERT、UPDATE 或 DELETE 语句，返回行数; 对于无返回结果的SQL语句，返回 0
	 * 
	 * @return int
	 */
	public int getEffectCounts();

}
