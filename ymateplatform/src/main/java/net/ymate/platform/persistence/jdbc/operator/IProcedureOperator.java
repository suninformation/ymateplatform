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
package net.ymate.platform.persistence.jdbc.operator;

import java.util.List;

import net.ymate.platform.persistence.jdbc.base.SqlParameter;

/**
 * <p>
 * IProcedureOperator
 * </p>
 * <p>
 * 数据库存储过程操作者接口定义类；
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
 *          <td>2010-12-25下午02:40:48</td>
 *          </tr>
 *          </table>
 * @deprecated 尚未完成
 */
public interface IProcedureOperator extends IOperator {

	/**
	 * 存储过程执行成功标记
	 */
	public final static int PROC_SUCCESS_FLAG = 0; // 存储过程执行成功标记

	/**
	 * 存储过程拥有返回结果集标记
	 */
	public final static int PROC_HAVE_RESULT = 1; // 存储过程拥有返回结果集标记

	/**
	 * @return 获取返回的OUT参数集合对象
	 */
	public List<Object> getResultOutParameters();

	/**
	 * @param index 参数位置（从第一个OUT参数开始计数）
	 * @return 获取返回的OUT参数值
	 */
	public Object getResultOutParameter(int index);

	/**
	 * @return 获取当前设定的存储过程OUT参数集合
	 */
	public List<SqlParameter> getOutParameters();

	/**
	 * 添加存储过程OUT参数(当SqlParameter对象作为设置存储过程的OUT参数时，其Value属性无任务意义，可将其置为NULL)
	 * 
	 * @param sqlParameter
	 */
	public void addOutParameter(SqlParameter sqlParameter);

	/**
	 * @return 获取所有结果集合对象
	 */
	public List<List<Object[]>> getResultSetAll();

	/**
	 * @return 按顺序获取结果集对象集合
	 */
	public List<Object[]> getResultSet(int index);

	/**
	 * @return 获取执行结果返回标记；>0 表示执行成功； ==1 表示存在返回结果
	 */
	public int getReturnFlag();

	/**
	 * @return 获取执行结果返回消息
	 */
	public String getReturnMsg();

	/**
	 * @return 是否为多返回结果集方式
	 */
	public boolean isMultipleResultSet();

}
