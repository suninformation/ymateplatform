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
package net.ymate.platform.persistence.support;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * PageResultSet
 * </p>
 * <p>
 * 分页查询结果集；
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
 *          <td>2011-9-24下午08:32:02</td>
 *          </tr>
 *          </table>
 */
public class PageResultSet<T> {

	/**
	 * 当前页号
	 */
	private int __pageNumber;

	/**
	 * 总页数
	 */
	private int __pageCount;

	/**
	 * 当前页面大小
	 */
	private int __pageSize;

	/**
	 * 总记录数
	 */
	private int __recordCount;

	/**
	 * 结果集列表
	 */
	private List<T> __resultSet;

	/**
	 * 构造器
	 * 
	 * @param resultSet 结果数据集合
	 * @param pageNumber 页号
	 * @param pageSize 页记录数
	 * @param recordCount 总计录数
	 */
	public PageResultSet(List<T> resultSet, int pageNumber, int pageSize, int recordCount) {
		this.__resultSet = new ArrayList<T>(resultSet); // 注：此处若不采用创建新集合对象方式保储数据，则会出现JSTL迭代时首先为空，然后出现java.util.ConcurrentModificationException异常的现象！！！
		this.__pageNumber = pageNumber;
		this.__pageSize = pageSize;
		this.__recordCount = recordCount;
		//
		if (recordCount <= 0) {
			__pageCount = -1;
		} else {
			if (recordCount % pageSize > 0) {
				__pageCount = recordCount / pageSize + 1;
			} else {
				__pageCount =  recordCount / pageSize;
			}
		}
	}

	/**
	 * @return the pageNumber
	 */
	public int getPageNumber() {
		return __pageNumber;
	}

	/**
	 * @return the pageCount
	 */
	public int getPageCount() {
		return __pageCount;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return __pageSize;
	}

	/**
	 * @return the recordCount
	 */
	public int getRecordCount() {
		return __recordCount;
	}

	/**
	 * @return the resultSet
	 */
	public List<T> getResultSet() {
		return __resultSet;
	}

}
