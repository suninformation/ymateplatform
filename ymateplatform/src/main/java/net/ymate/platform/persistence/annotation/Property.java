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
package net.ymate.platform.persistence.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Column
 * </p>
 * <p>
 * 声明 Model 类中的一个方法或成员所对应的数据表字段，若参数 name 为空，则默认采用对应方法或成员的名称；
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
 *          <td>2011-6-15下午10:39:19</td>
 *          </tr>
 *          </table>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.FIELD })
@Documented
public @interface Property {

	/**
	 * @return 字段名称，若为空，则默认采用对应方法或成员的名称
	 */
	String name() default "";

	/**
	 * @return 字段值是否自增长
	 */
	boolean isAutoIncrement() default false;

	/**
	 * @return  序列名称（只适用类似 Oracle 等数据库，配合 isAutoIncrement 参数一同使用）
	 */
	String sequenceName() default "";

}
