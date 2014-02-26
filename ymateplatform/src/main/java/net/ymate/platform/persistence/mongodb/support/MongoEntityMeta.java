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
/**
 * <p>文件名:	MongoEntityMeta.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	ymate-platform</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.persistence.mongodb.support;

import net.ymate.platform.persistence.mongodb.MongoDB;
import net.ymate.platform.persistence.support.EntityMeta;

/**
 * <p>
 * MongoEntityMeta
 * </p>
 * <p>
 * 
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
 *          <td>2014年2月16日下午2:34:16</td>
 *          </tr>
 *          </table>
 */
public class MongoEntityMeta extends EntityMeta {

	public MongoEntityMeta(Class<?> entityClass) {
		super(entityClass, MongoDB.COLLECTION_PREFIX);
	}

	public MongoEntityMeta(Class<?> entityClass, boolean simple) {
		super(entityClass, MongoDB.COLLECTION_PREFIX, simple);
	}

}
