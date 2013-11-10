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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.ymate.platform.commons.lang.PairObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.thoughtworks.paranamer.AdaptiveParanamer;

/**
 * <p>
 * ClassUtils
 * </p>
 * <p>
 * 类操作相关工具；
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
 *          <td>2012-12-5下午6:41:23</td>
 *          </tr>
 *          </table>
 */
public class ClassUtils {

	private static final Log _LOG = LogFactory.getLog(ClassUtils.class);

	private static InnerClassLoader _INNER_CLASS_LOADER = new InnerClassLoader(new URL[] {}, ClassUtils.class.getClassLoader());

	/**
	 * @return 返回默认类加载器对象
	 */
	public static ClassLoader getDefaultClassLoader() {
		return _INNER_CLASS_LOADER;
	}

	/**
	 * 在参数packageNames指定的包路径内，查找实现了由clazz指定的接口、注解或抽象类的类对象集合
	 * 
	 * @param clazz
	 * @param packageNames
	 * @param callingClass
	 * @return
	 */
	public static <T> Collection<Class<T>> findClassByClazz(Class<T> clazz, Collection<String> packageNames, Class<?> callingClass) {
		List<Class<T>> _returnValue = new ArrayList<Class<T>>();
		try {
			for (String _packageName : packageNames) {
				Iterator<URL> _urls = ResourceUtils.getResources(_packageName.replaceAll("\\.", "/"), callingClass, true);
				while (_urls.hasNext()) {
					URL _url = _urls.next();
					__doProcessURL(_url, _returnValue, clazz, _packageName, callingClass);
				}
			}
		} catch (Exception e) {
			_LOG.warn("", RuntimeUtils.unwrapThrow(e));
		}
		return _returnValue;
	}

	private static <T> void __doProcessURL(URL _url, Collection<Class<T>> collections, Class<T> clazz, String _packageName, Class<?> callingClass) throws URISyntaxException, IOException {
		if (_url.getProtocol().equalsIgnoreCase("file") || _url.getProtocol().equalsIgnoreCase("vfsfile")) {
			File[] _files = new File(_url.toURI()).listFiles();
			for (File _file : _files != null ? _files : new File[0]) {
				__doFindClassByClazz(collections, clazz, _packageName, _file, callingClass);
			}
		} else if (_url.getProtocol().equalsIgnoreCase("jar") || _url.getProtocol().equalsIgnoreCase("wsjar")) {
			JarFile _jarFileObj = ((JarURLConnection) _url.openConnection()).getJarFile();
			__doFindClassByJar(collections, clazz, _packageName, _jarFileObj, callingClass);
		} else if (_url.getProtocol().equalsIgnoreCase("zip")) {
			__doFindClassByZip(collections, clazz, _packageName, _url, callingClass);
		}
	}

	@SuppressWarnings("unchecked")
	protected static <T> void __doFindClassByZip(Collection<Class<T>> collections, Class<T> clazz, String packageName, URL zipUrl, Class<?> callingClass) {
		ZipInputStream _zipStream = null;
		try {
			String _zipFilePath = zipUrl.toString();
			if (_zipFilePath.indexOf('!') > 0) {
				_zipFilePath = StringUtils.substringBetween(zipUrl.toString(), "zip:", "!");
			} else {
				_zipFilePath = StringUtils.substringAfter(zipUrl.toString(), "zip:");
			}
			_zipStream = new ZipInputStream(new FileInputStream(new File(_zipFilePath)));
			ZipEntry _zipEntry = null;
			while (null != (_zipEntry = _zipStream.getNextEntry())) {
				if (!_zipEntry.isDirectory()) {
					if (_zipEntry.getName().endsWith(".class") && _zipEntry.getName().indexOf('$') < 0) {
						Class<?> _class = __doProcessEntry(zipUrl, _zipEntry);
						if (_class != null) {
							if (clazz.isAnnotation()) {
								if (isAnnotationOf(_class, (Class<Annotation>) clazz)) {
									collections.add((Class<T>) _class);
								}
							} else if (clazz.isInterface()) {
								if (isInterfaceOf(_class, clazz)) {
									collections.add((Class<T>) _class);
								}
							} else if (isSubclassOf(_class, clazz)) {
								collections.add((Class<T>) _class);
							}
						}
					}
				}
				_zipStream.closeEntry();
            }
		} catch (Exception e) {
			_LOG.warn("", RuntimeUtils.unwrapThrow(e));
		} finally {
			if (_zipStream != null) {
				try {
					_zipStream.close();
				} catch (IOException e) {
					_LOG.warn("", RuntimeUtils.unwrapThrow(e));
				}
			}
		}
	}

	private static Class<?> __doProcessEntry(URL zipUrl, ZipEntry entry) {  
        if (entry.getName().endsWith(".class")) {
			try {
				_INNER_CLASS_LOADER.addURL(zipUrl); // ~~是否需要进行去得判断呢？
				String _className = entry.getName().replace("/", ".");
				_className = _className.substring(0, _className.length() - 6);
				return Class.forName(_className, true, _INNER_CLASS_LOADER);
			} catch (Throwable e) {
				_LOG.warn("", RuntimeUtils.unwrapThrow(e));
			}
        }
        return null;
	}

	/**
	 * 获取文件路径中所有的类（所有以.class结尾，且不包含'$'内部类的）文件并将文件路径转换成Java类引用路径，如："com/ymatesoft/common/A.class"~"com.ymatesoft.common.A"
	 * 
	 * @param collections
	 * @param clazz
	 * @param packageName
	 * @param packageFile
	 * @param callingClass
	 */
	@SuppressWarnings("unchecked")
	protected static <T> void __doFindClassByClazz(Collection<Class<T>> collections, Class<T> clazz, String packageName, File packageFile, Class<?> callingClass) {
		if (packageFile.isFile()) {
			try {
				if (packageFile.getName().endsWith(".class") && packageFile.getName().indexOf('$') < 0) {
					Class<?> _class = ResourceUtils.loadClass(packageName + "." + packageFile.getName().replace(".class", ""), callingClass);
					if (_class != null) {
						if (clazz.isAnnotation()) {
							if (isAnnotationOf(_class, (Class<Annotation>) clazz)) {
								collections.add((Class<T>) _class);
							}
						} else if (clazz.isInterface()) {
							if (isInterfaceOf(_class, clazz)) {
								collections.add((Class<T>) _class);
							}
						} else if (isSubclassOf(_class, clazz)) {
							collections.add((Class<T>) _class);
						}
					}
				}
			} catch (NoClassDefFoundError e) {
				_LOG.warn("", RuntimeUtils.unwrapThrow(e));
			} catch (ClassNotFoundException e) {
				_LOG.warn("", RuntimeUtils.unwrapThrow(e));
			}
		} else {
			File[] _tmpfiles = packageFile.listFiles();
			for (File _tmpFile : _tmpfiles != null ? _tmpfiles : new File[0]) {
				__doFindClassByClazz(collections, clazz, packageName + "." + packageFile.getName(), _tmpFile, callingClass);
			}
		}
	}

	/**
	 * 获取 Jar 包中所有的类（所有以.class结尾，且不包含'$'内部类的）文件并将文件路径转换成Java类引用路径，如："com/ymatesoft/common/A.class"~"com.ymatesoft.common.A"
	 * 
	 * @param collections
	 * @param clazz
	 * @param packageName
	 * @param jarFile
	 * @param callingClass
	 */
	@SuppressWarnings("unchecked")
	protected static <T> void __doFindClassByJar(Collection<Class<T>> collections, Class<T> clazz, String packageName, JarFile jarFile, Class<?> callingClass) {
		Enumeration<JarEntry> _entriesEnum = jarFile.entries();
		for (; _entriesEnum.hasMoreElements();) {
			JarEntry _entry = _entriesEnum.nextElement();
			// 替换文件名中所有的 '/' 为 '.'，并且只存放.class结尾的类名称，剔除所有包含'$'的内部类名称
			String _className = _entry.getName().replaceAll("/", ".");
			if (_className.endsWith(".class") && _className.indexOf('$') < 0) {
				if (_className.startsWith(packageName)) {
					Class<?> _class = null;
					try {
						_class = ResourceUtils.loadClass(_className.substring(0, _className.lastIndexOf('.')), callingClass);
						if (_class != null) {
							if (clazz.isAnnotation()) {
								if (isAnnotationOf(_class, (Class<Annotation>) clazz)) {
									collections.add((Class<T>) _class);
								}
							} else if (clazz.isInterface()) {
								if (isInterfaceOf(_class, clazz)) {
									collections.add((Class<T>) _class);
								}
							} else if (isSubclassOf(_class, clazz)) {
								collections.add((Class<T>) _class);
							}
						}
					} catch (NoClassDefFoundError e) {
						_LOG.warn("", RuntimeUtils.unwrapThrow(e));
					} catch (ClassNotFoundException e) {
						_LOG.warn("", RuntimeUtils.unwrapThrow(e));
					}
				}
			}
		}
	}

	static class InnerClassLoader extends URLClassLoader {

		public InnerClassLoader(URL[] urls, ClassLoader parent) {
			super(urls, parent);
		}

		@Override
		public void addURL(URL url) {
			super.addURL(url);
		}

	}

	/**
	 * 获得指定名称、限定接口的实现类
	 * 
	 * @param <T>
	 * @param className 实现类名
	 * @param interfaceClass 限制接口名
	 * @param callingClass
	 * @return 如果可以得到并且限定于指定实现，那么返回实例，否则为空
	 */
	@SuppressWarnings("unchecked")
	public static <T> T impl(String className, Class<T> interfaceClass, Class<?> callingClass) {
		if (StringUtils.isNotBlank(className)) {
			try {
				Class<?> implClass = ResourceUtils.loadClass(className, callingClass);
				if (interfaceClass == null || interfaceClass.isAssignableFrom(implClass)) {
					try {
						return (T) implClass.newInstance();
					} catch (InstantiationException e) {
						_LOG.warn("", RuntimeUtils.unwrapThrow(e));
					} catch (IllegalAccessException e) {
						_LOG.warn("", RuntimeUtils.unwrapThrow(e));
					}
				}
			} catch (ClassNotFoundException e) {
				_LOG.warn("", RuntimeUtils.unwrapThrow(e));
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T impl(Class<?> implClass, Class<T> interfaceClass) {
		if (implClass != null) {
			if (interfaceClass == null || interfaceClass.isAssignableFrom(implClass)) {
				try {
					return (T) implClass.newInstance();
				} catch (InstantiationException e) {
					_LOG.warn("", RuntimeUtils.unwrapThrow(e));
				} catch (IllegalAccessException e) {
					_LOG.warn("", RuntimeUtils.unwrapThrow(e));
				}
			}
		}
		return null;
	}

	/**
	 * 判断类clazz是否是superClass类的子类对象
	 * 
	 * @param clazz
	 * @param superClass
	 * @return 
	 */
	public static boolean isSubclassOf(Class<?> clazz, Class<?> superClass) {
		boolean _flag = false;
		do {
			Class<?> cc = clazz.getSuperclass();
			if (cc != null) {
				if (cc.equals(superClass)) {
					_flag = true;
					break;
				} else {
					clazz = clazz.getSuperclass();
				}
			} else {
				break;
			}
		} while ((clazz != null && clazz != Object.class));
		return _flag;
	}

	/**
	 * @param clazz 目标对象
	 * @param interfaceClass 接口类型
	 * @return  判断clazz类中是否实现了interfaceClass接口
	 */
	public static boolean isInterfaceOf(Class<?> clazz, Class<?> interfaceClass) {
		boolean _flag = false;
		do {
            for (Class<?> cc : clazz.getInterfaces()) {
                if (cc.equals(interfaceClass)) {
                    _flag = true;
                }
            }
			clazz = clazz.getSuperclass();
		} while (!_flag && (clazz != null && clazz != Object.class));
		return _flag;
	}

	/**
	 * @param target 目标对象，即可以是Field对象、Method对象或是Class对象
	 * @param annotationClass 注解类对象
	 * @return  判断target对象是否存在annotationClass注解
	 */
	public static boolean isAnnotationOf(Object target, Class<? extends Annotation> annotationClass) {
		if (target instanceof Field) {
			if (((Field) target).isAnnotationPresent(annotationClass)) {
				return true;
			}
		} else if (target instanceof Method) {
			if (((Method) target).isAnnotationPresent(annotationClass)) {
				return true;
			}
		} else if (target instanceof Class) {
			if (((Class<?>) target).isAnnotationPresent(annotationClass)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param clazz 类型
	 * @return 返回类中实现的接口名称集合
	 */
	public static String[] getInterfaceNames(Class<?> clazz) {
		Class<?>[] interfaces = clazz.getInterfaces();
		List<String> names = new ArrayList<String>();
		for (Class<?> i : interfaces) {
			names.add(i.getName());
		}
		return names.toArray(new String[names.size()]);
	}

	/**
	 * @param clazz 类对象
	 * @return 获取泛型的数据类型集合，注：不适用于泛型嵌套, 即泛型里若包含泛型则返回此泛型的RawType类型
	 */
	public static List<Class<?>> getParameterizedTypes(Class<?> clazz) {
		List<Class<?>> _clazzs = new ArrayList<Class<?>>();
		Type _types = clazz.getGenericSuperclass();
		if (ParameterizedType.class.isAssignableFrom(_types.getClass())) {
			for (Type _type : ((ParameterizedType) _types).getActualTypeArguments()) {
				if (ParameterizedType.class.isAssignableFrom(_type.getClass())) {
					_clazzs.add((Class<?>) ((ParameterizedType) _type).getRawType());
				} else {
					_clazzs.add((Class<?>) _type);
				}
			}
		} else {
			_clazzs.add((Class<?>) _types);
		}
		return _clazzs;
	}

	/**
	 * 获取clazz指定的类对象所有的Field对象（若包含其父类对象，直至其父类为空）
	 * 
	 * @param clazz 目标类
	 * @param parent 是否包含其父类对象
	 * @return Field对象集合
	 */
	public static List<Field> getFields(Class<?> clazz, boolean parent) {
		List<Field> fieldList = new ArrayList<Field>();
		Class<?> clazzin = clazz;
		do {
			if (clazzin == null) {
				break;
			}
			fieldList.addAll(Arrays.asList(clazzin.getDeclaredFields()));
			if (parent) {
				clazzin = clazzin.getSuperclass();
			} else {
				clazzin = null;
			}
		} while (true);
		return fieldList;
	}

	/**
	 * @param <A>
	 * @param clazz
	 * @param annotationClazz
	 * @param onlyFirst
	 * @return 获取clazz类中成员声明的annotationClazz注解
	 */
	public static <A extends Annotation> List<PairObject<Field, A>> getFieldAnnotations(Class<?> clazz, Class<A> annotationClazz, boolean onlyFirst) {
		List<PairObject<Field, A>> _annotations = new ArrayList<PairObject<Field, A>>();
		for (Field _field : ClassUtils.getFields(clazz, true)) {
			A _annotation = _field.getAnnotation(annotationClazz);
			if (_annotation != null) {
				_annotations.add(new PairObject<Field, A>(_field, _annotation));
				if (onlyFirst) {
					break;
				}
			}
		}
		return _annotations;
	}

	/**
	 * @param method
	 * @return 获取方法的参数名
	 */
	public static String[] getMethodParamNames(final Method method) {
		return new AdaptiveParanamer().lookupParameterNames(method);
	}

	/**
	 * @param clazz 数组类型
	 * @return 返回数组元素类型
	 */
	public static Class<?> getArrayClassType(Class<?> clazz) {
		try {
			return Class.forName(StringUtils.substringBetween(clazz.getName(), "[L", ";"));
		} catch (ClassNotFoundException e) {
			_LOG.warn("", RuntimeUtils.unwrapThrow(e));
		}
		return null;
	}

	/**
	 * @param clazz 目标类型
	 * @return 创建一个类对象实例，包裹它并赋予其简单对象属性操作能力，可能返回空
	 */
	public static <T> ClassBeanWrapper<T> wrapper(Class<T> clazz) {
		try {
			return wrapper(clazz.newInstance());
		} catch (Exception e) {
			_LOG.warn("", RuntimeUtils.unwrapThrow(e));
		}
		return null;
	}

	/**
	 * @param target 目标类对象
	 * @return 包裹它并赋予其简单对象属性操作能力，可能返回空
	 */
	public static <T> ClassBeanWrapper<T> wrapper(T target) {
		return new ClassBeanWrapper<T>(target);
	}

	/**
	 * <p>
	 * ClassBeanWrapper
	 * </p>
	 * <p>
	 * 类对象包裹器，赋予对象简单的属性操作能力；
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
	 *          <td>2012-12-23上午12:46:50</td>
	 *          </tr>
	 *          </table>
	 */
	public static class ClassBeanWrapper<T> {

		protected static Map<Class<?>, MethodAccess> __methodCache = new WeakHashMap<Class<?>, MethodAccess>();

		private T target;
	
		private Map<String, Field> _fields;

		private MethodAccess methodAccess;

		/**
		 * 构造器
		 * @param target
		 */
		protected ClassBeanWrapper(T target) {
			this.target = target;
			this._fields = new HashMap<String, Field>();
			for (Field _field : target.getClass().getDeclaredFields()) {
				if (Modifier.isStatic(_field.getModifiers())) {
					continue;
				}
				this._fields.put(_field.getName(), _field);
			}
			//
			this.methodAccess = __methodCache.get(target.getClass());
			if (this.methodAccess == null) {
				this.methodAccess = MethodAccess.get(target.getClass());
				__methodCache.put(target.getClass(), this.methodAccess);
			}
		}

		public T getTarget() {
			return target;
		}

		public Set<String> getFieldNames() {
			return _fields.keySet();
		}

		public Annotation[] getFieldAnnotations(String fieldName) {
			return getField(fieldName).getAnnotations();
		}

		public Field getField(String fieldName) {
			return _fields.get(StringUtils.uncapitalize(fieldName));
		}

		public Class<?> getFieldType(String fieldName) {
			return getField(fieldName).getType();
		}

		public ClassBeanWrapper<T> setValue(String fieldName, Object value) {
			methodAccess.invoke(this.target, "set" + StringUtils.capitalize(fieldName), value);
			return this;
		}

		public Object getValue(String fieldName) {
			return methodAccess.invoke(this.target, "get" + StringUtils.capitalize(fieldName));
		}

	}

}