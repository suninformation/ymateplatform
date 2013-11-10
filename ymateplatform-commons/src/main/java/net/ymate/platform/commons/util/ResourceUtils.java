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
package net.ymate.platform.commons.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * ResourceUtils
 * </p>
 * <p>
 * 资源加载工具类
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
 *          <td>2010-5-16下午04:12:19</td>
 *          </tr>
 *          </table>
 */
public class ResourceUtils {

	private static final Log _LOG = LogFactory.getLog(ResourceUtils.class);

	/**
	 * 
	 * @param resourceName
	 * @param callingClass
	 * @param aggregate
	 * @return
	 * @throws IOException
	 */
	public static Iterator<URL> getResources(String resourceName, Class<?> callingClass, boolean aggregate) throws IOException {
		AggregateIterator<URL> iterator = new AggregateIterator<URL>();
		iterator.addEnumeration(Thread.currentThread().getContextClassLoader().getResources(resourceName));
		if ((!iterator.hasNext()) || (aggregate)) {
			iterator.addEnumeration(ClassUtils.getDefaultClassLoader().getResources(resourceName));
		}
		if ((!iterator.hasNext()) || (aggregate)) {
			ClassLoader cl = callingClass.getClassLoader();
			if (cl != null) {
				iterator.addEnumeration(cl.getResources(resourceName));
			}
		}
		if ((!iterator.hasNext()) && (resourceName != null) && (((resourceName.length() == 0) || (resourceName.charAt(0) != '/')))) {
			return getResources('/' + resourceName, callingClass, aggregate);
		}
		return iterator;
	}

	/**
	 * 
	 * @param resourceName
	 * @param callingClass
	 * @return
	 */
	public static URL getResource(String resourceName, Class<?> callingClass) {
		URL url = Thread.currentThread().getContextClassLoader().getResource(resourceName);
		if (url == null) {
			url = ClassUtils.getDefaultClassLoader().getResource(resourceName);
		}
		if (url == null) {
			url = callingClass.getResource(resourceName);
			if (url == null) {
				ClassLoader cl = callingClass.getClassLoader();
				if (cl != null) {
					url = cl.getResource(resourceName);
				}
			}
		}
		if ((url == null) && (resourceName != null) && (((resourceName.length() == 0) || (resourceName.charAt(0) != '/')))) {
			return getResource('/' + resourceName, callingClass);
		}
		return url;
	}

	/**
	 * 
	 * @param resourceName
	 * @param callingClass
	 * @return
	 */
	public static InputStream getResourceAsStream(String resourceName, Class<?> callingClass) {
		URL url = getResource(resourceName, callingClass);
		try {
			return (url != null) ? url.openStream() : null;
		} catch (IOException e) {
			_LOG.warn("", RuntimeUtils.unwrapThrow(e));
		}
		return null;
	}

	/**
	 * 
	 * @param className
	 * @param callingClass
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Class<?> loadClass(String className, Class<?> callingClass) throws ClassNotFoundException {
		try {
			return Thread.currentThread().getContextClassLoader().loadClass(className);
		} catch (ClassNotFoundException e) {
			try {
				return Class.forName(className);
			} catch (ClassNotFoundException ex) {
				try {
					return ClassUtils.getDefaultClassLoader().loadClass(className);
				} catch (ClassNotFoundException exc) {
					 _LOG.warn("", RuntimeUtils.unwrapThrow(exc));
				}
			}
		}
		return callingClass.getClassLoader().loadClass(className);
	}

	protected static class AggregateIterator<E> implements Iterator<E> {
		LinkedList<Enumeration<E>> enums;
		Enumeration<E> cur;
		E next;
		Set<E> loaded;

		protected AggregateIterator() {
			this.enums = new LinkedList<Enumeration<E>>();
			this.cur = null;
			this.next = null;
			this.loaded = new HashSet<E>();
		}

		public AggregateIterator<E> addEnumeration(Enumeration<E> e) {
			if (e.hasMoreElements()) {
				if (this.cur == null) {
					this.cur = e;
					this.next = e.nextElement();
					this.loaded.add(this.next);
				} else {
					this.enums.add(e);
				}
			}
			return this;
		}

		public boolean hasNext() {
			return this.next != null;
		}

		public E next() {
			if (this.next != null) {
				E prev = this.next;
				this.next = loadNext();
				return prev;
			}
			throw new NoSuchElementException();
		}

		private Enumeration<E> determineCurrentEnumeration() {
			if ((this.cur != null) && (!this.cur.hasMoreElements())) {
				if (this.enums.size() > 0)
					this.cur = this.enums.removeLast();
				else {
					this.cur = null;
				}
			}
			return this.cur;
		}

		private E loadNext() {
			if (determineCurrentEnumeration() != null) {
				E tmp = this.cur.nextElement();
				do {
					if (!this.loaded.contains(tmp))
						break;
					tmp = loadNext();
				} while (tmp != null);

				if (tmp != null) {
					this.loaded.add(tmp);
				}
				return tmp;
			}
			return null;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

}
