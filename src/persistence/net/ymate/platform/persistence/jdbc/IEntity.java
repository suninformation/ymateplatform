/**
 * <p>文件名:	IEntity.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-Persistence</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.persistence.jdbc;

import java.io.Serializable;

/**
 * <p>
 * IEntity
 * </p>
 * <p>
 * 实体模型接口定义类；
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
 *          <td>2010-12-20下午02:16:46</td>
 *          </tr>
 *          </table>
 */
public interface IEntity<PK> extends Serializable {

	/**
	 * @return 获取实体主键值
	 */
	public PK getId();

	/**
	 * 设置实体主键值
	 * @param id 主键值
	 */
	public void setId(PK id);
}
