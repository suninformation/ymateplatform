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
package net.ymate.platform.persistence.jdbc.transaction;

import net.ymate.platform.base.YMP;
import net.ymate.platform.commons.i18n.I18N;
import net.ymate.platform.commons.util.RuntimeUtils;
import net.ymate.platform.persistence.jdbc.transaction.ITransaction.TransactionLevel;
import net.ymate.platform.persistence.jdbc.transaction.impl.DefaultTransaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <p>
 * Trans
 * </p>
 * <p>
 * 事务管理器类，用于执行基于JDBC事务的相关操作；<br/>
 * 注：支持事务模板的无限层级嵌套，如果每一层嵌套，指定的事务级别有所不同，不同的数据库，可能引发不可预知的错误。
 * 所以嵌套的事务将以最顶层的事务级别为标准，就是说，如果最顶层的事务级别为'TRANSACTION_READ_COMMITTED'，
 * 那么下面所包含的所有事务，无论你指定什么样的事务级别，都是'TRANSACTION_READ_COMMITTED'，
 * 这一点由ITransaction接口实现类来保证，其setLevel当被设置了一个大于0的整数以后，将不再接受任何其他的值。
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
 *          <td>2011-9-6下午04:36:53</td>
 *          </tr>
 *          </table>
 */
public class Trans {

	private static final Log _LOG = LogFactory.getLog(Trans.class);

	private static Class<ITransaction> __implClass;

	private static ThreadLocal<ITransaction> __trans = new ThreadLocal<ITransaction>();

	private static ThreadLocal<Integer> __count = new ThreadLocal<Integer>();

	/**
	 * @return 返回当前线程的事务对象，如果不存在事务则返回 null
	 */
	public static ITransaction get() {
		return __trans.get();
	}

	/**
	 * 扩展默认的 ITransaction 事务实现方式
	 * 
	 * @param transClass 自定义的事务实现类
	 */
	public static void registerTransactionClass(Class<ITransaction> transClass) {
		__implClass = transClass;
	}

	/**
	 * 开始一个事务
	 * 
	 * @param level 事务级别
	 * @throws Exception
	 */
	static void __begin(TransactionLevel level) throws Exception {
		if (null == __trans.get()) {
			ITransaction tn = null == __implClass ? new DefaultTransaction() : __implClass.newInstance();
			tn.setLevel(level);
			__trans.set(tn);
			__count.set(0);
			//
			_LOG.info(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.jdbc.trans_begin"));
		}
		__count.set(__count.get() + 1);
	}

	/**
	 * 提交事务
	 * 
	 * @throws Exception
	 */
	static void __commit() throws Exception {
		if (__count.get() > 0) {
			__count.set(__count.get() - 1);
		}
		if (__count.get() == 0) {
			__trans.get().commit();
			_LOG.info(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.jdbc.trans_commit"));
		}
	}

	/**
	 * 回滚事务
	 * 
	 * @param num 事务计数
	 */
	static void __rollback(int num) throws Exception {
		__count.set(num);
		if (__count.get() == 0) {
			__trans.get().rollback();
			_LOG.info(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.jdbc.trans_rollback"));
		} else {
			__count.set(__count.get() - 1);
		}
	}

	/**
	 * 关闭事务
	 */
	static void __close() throws Exception {
		if (__count.get() == 0) {
			try {
				__trans.get().close();
				_LOG.info(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.jdbc.trans_close"));
			} finally {
				__trans.set(null);
			}
		}
	}

	/**
	 * 执行一组事务任务操作，默认的事务级别为：ITransaction.TransactionLevel.TRANSACTION_READ_COMMITTED
	 * 
	 * @param tasks 事务任务操作对象
	 */
	public static void exec(ITransTask... tasks) throws TransactionException {
		exec(TransactionLevel.TRANSACTION_READ_COMMITTED, tasks);
	}

	/**
	 * 执行一组事务任务操作，并指定事务级别
	 * 
	 * @param level 事务级别
	 * @param tasks 事务任务操作对象
	 */
	public static void exec(TransactionLevel level, ITransTask... tasks) throws TransactionException {
		if (null == tasks) {
			return;
		}
		int num = __count.get() == null ? 0 : __count.get();
		try {
			__begin(level);
			for (ITransTask task : tasks) {
				task.doTask();
			}
			__commit();
		} catch (Throwable e) {
			try {
				__rollback(num);
			} catch (Exception ignored) {}
			throw new TransactionException(RuntimeUtils.unwrapThrow(e));
		} finally {
			try {
				__close();
			} catch (Exception ignored) {}
		}
	}

	/**
	 * 执行一个具有返回值的事务操作，采用默认的事务级别
	 * 
	 * @param task 事务任务操作
	 * @return 事务执行结果
	 */
	public static <T> T exec(TransResultTask<T> task) throws TransactionException {
		Trans.exec((ITransTask) task);
		return task.getResult();
	}

	/**
	 * 执行一个具有返回值的事务操作，并指定事务级别
	 * 
	 * @param level 事务级别
	 * @param task 事务任务操作
	 * @return 事务执行结果
	 */
	public static <T> T exec(TransactionLevel level, TransResultTask<T> task) throws TransactionException {
		Trans.exec(level,(ITransTask) task);
		return task.getResult();
	}

	// ------------------------------------------------------------------
	//  以下方法适用于习惯手工用 try...catch...finally 处理数据库事务的开发人员
	// ------------------------------------------------------------------

	/**
	 * 开始一个事务，默认的事务级别为：ITransaction.TransactionLevel.TRANSACTION_READ_COMMITTED
	 * <p>需要手工用 try...catch...finally 来保证你提交和关闭这个事务</p>
	 */
	public static void begin() throws Exception {
		Trans.__begin(ITransaction.TransactionLevel.TRANSACTION_READ_COMMITTED);
	}

	/**
	 * 开始一个指定级别的事务
	 * <p>需要手工用 try...catch...finally 来保证你提交和关闭这个事务</p>
	 * 
	 * @param level 事务级别
	 * @throws Exception
	 */
	public static void begin(TransactionLevel level) throws Exception {
		Trans.__begin(level);
	}

	/**
	 * 提交事务，执行前必需保证已经手工开始了一个事务
	 * 
	 * @throws Exception
	 */
	public static void commit() throws Exception {
		Trans.__commit();
	}

	/**
	 * 回滚事务，执行前必需保证已经手工开始了一个事务
	 * 
	 * @throws Exception
	 */
	public static void rollback() throws Exception {
		Integer c = Trans.__count.get();
		if (c == null) {
			c = 0;
		}
		Trans.__rollback(c);
	}

	/**
	 * 关闭事务，执行前必需保证已经手工开始了一个事务
	 * 
	 * @throws Exception
	 */
	public static void close() throws Exception {
		Trans.__close();
	}

}
