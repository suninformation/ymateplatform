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
package net.ymate.platform.persistence.jdbc.transaction.support;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import net.ymate.platform.persistence.jdbc.transaction.Trans;
import net.ymate.platform.persistence.jdbc.transaction.annotation.Transaction;

/**
 * <p>
 * DefaultTransactionProxyHandler
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
 *          <td>2013年9月18日下午8:06:12</td>
 *          </tr>
 *          </table>
 */
public class DefaultTransactionProxyHandler implements InvocationHandler {

	private Object targetObj;

	@SuppressWarnings("unchecked")
	public <T> T bind(T targetObj) {
		this.targetObj = targetObj;
		return (T) Proxy.newProxyInstance(
				targetObj.getClass().getClassLoader(),
				targetObj.getClass().getInterfaces(), this);
	}

	/* (non-Javadoc)
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object _resultObj = null;
		Transaction _transAnno = method.getAnnotation(Transaction.class);
		if (_transAnno != null) {
			Trans.begin(_transAnno.value());
			try {
				_resultObj = method.invoke(this.targetObj, args);
				Trans.commit();
			} catch (Throwable e) {
				Trans.rollback();
				throw e;
			} finally {
				Trans.close();
			}
		} else {
			_resultObj = method.invoke(this.targetObj, args);
		}
		return _resultObj;
	}

}
