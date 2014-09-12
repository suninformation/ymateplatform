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
package net.ymate.platform.mvc.web.support;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import net.ymate.platform.commons.util.FileUtils;
import net.ymate.platform.mvc.web.IUploadFileWrapper;
import net.ymate.platform.mvc.web.WebMVC;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;

/**
 * <p>
 * FileUploadHelper
 * </p>
 * <p>
 * 文件上传处理助手类；注：文件上传页面Form表单必须采用POST方式提交并设置属性：enctype="multipart/form-data"，否则将无法处理
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
 *          <td>2011-6-5下午02:50:07</td>
 *          </tr>
 *          </table>
 */
public class FileUploadHelper {

	private HttpServletRequest __request;

	/**
	 * 监听器
	 */
	private ProgressListener __listener;

	/**
	 * 上传文件临时目录（不支持自定义文件流处理）
	 */
	private File __uploadTempDir;

	/**
	 * 上传文件最大值
	 */
	private long __fileSizeMax = -1; // 10485760 = 10M

	/**
	 * 上传文件总量的最大值
	 */
	private long __sizeMax = -1;

	/**
	 * 内存缓冲区的大小,默认值为10K,如果文件大于10K,将使用临时文件缓存上传文件
	 */
	private int __sizeThreshold = 10240; // 4096 = 4K

	/**
	 * 构造器
	 * 
	 * @param request
	 */
	private FileUploadHelper(HttpServletRequest request) {
		this.__request = request;
	}

	/**
	 * 基于磁盘文件存储方式构建
	 * 
	 * @param request
	 * @return
	 */
	public static FileUploadHelper bind(HttpServletRequest request) {
		return new FileUploadHelper(request);
	}

	/**
	 * 处理表单提交，使用提供的文件上传处理器处理文件流
	 * 
	 * @param processer
	 * @throws FileUploadException 
	 * @throws IOException 
	 */
	public UploadFormWrapper processUpload(IUploadFileItemProcesser processer) throws FileUploadException, IOException {
		UploadFormWrapper _form = null;
		boolean _isMultipart = ServletFileUpload.isMultipartContent(this.__request);
		if (_isMultipart) {
			if (null != processer) {
				_form = this.__doUploadFileAsStream(processer);
			} else {
				_form = this.UploadFileAsDiskBased();
			}
		} else {
			_form = new UploadFormWrapper();
		}
		return _form;
	}

	/**
	 * 处理表单提交
	 * 
	 * @return
	 * @throws FileUploadException 
	 * @throws IOException 
	 */
	public UploadFormWrapper processUpload() throws FileUploadException, IOException {
		return this.processUpload(null);
	}

	/**
	 * 采用文件流的方式处理上传文件（即将上传文件流对象交给用户做进一步处理）
	 * 
	 * @param processer
	 * @throws IOException 
	 * @throws FileUploadException 
	 */
	private UploadFormWrapper __doUploadFileAsStream(IUploadFileItemProcesser processer) throws FileUploadException, IOException {
		ServletFileUpload _upload = new ServletFileUpload();
		if (this.__listener != null) {
			_upload.setProgressListener(this.__listener);
		}
		_upload.setFileSizeMax(this.__fileSizeMax);
		_upload.setSizeMax(this.__sizeMax);
		UploadFormWrapper _form = new UploadFormWrapper();
		Map<String,List<String>> tmpParams = new HashMap<String,List<String>>();
        Map<String,List<UploadFileWrapper>> tmpFiles = new HashMap<String,List<UploadFileWrapper>>();
        //
		FileItemIterator _iter = _upload.getItemIterator(this.__request);
		while (_iter.hasNext()) {
			FileItemStream _item = _iter.next();
			if (_item.isFormField()) {
				List<String> _valueList = tmpParams.get(_item.getFieldName());
				if (_valueList == null) {
					_valueList = new ArrayList<String>();
                    tmpParams.put(_item.getFieldName(), _valueList);
				}
				_valueList.add(Streams.asString(_item.openStream(), WebMVC.getConfig().getCharsetEncoding()));
			} else {
				List<UploadFileWrapper> _valueList2 = tmpFiles.get(_item.getFieldName());
                if (_valueList2 == null){
                	_valueList2 = new ArrayList<UploadFileWrapper>();
                    tmpFiles.put(_item.getFieldName(), _valueList2);
                }
                // 交给用户接口处理
                _valueList2.add(processer.process(_item));
			}
		}
		//
		for (Entry<String, List<String>> entry : tmpParams.entrySet()) {
			String key = entry.getKey();
			List<String> value = entry.getValue();
			_form.getFieldMap().put(key, value.toArray(new String[value.size()]));
		}
		for (Entry<String, List<UploadFileWrapper>> entry : tmpFiles.entrySet()) {
			String key = entry.getKey();
			_form.getFileMap().put(key, entry.getValue().toArray(new UploadFileWrapper[entry.getValue().size()]));
		}
		return _form;
	}

	/**
	 * 采用文件方式处理上传文件（即先将文件上传后，再交给用户已上传文件对象集合）
	 * 
	 * @throws FileUploadException
	 */
	private UploadFormWrapper UploadFileAsDiskBased() throws FileUploadException {
		DiskFileItemFactory _factory = new DiskFileItemFactory();
		_factory.setRepository(this.__uploadTempDir);
		_factory.setSizeThreshold(this.__sizeThreshold);
		ServletFileUpload _upload = new ServletFileUpload(_factory);
		_upload.setFileSizeMax(this.__fileSizeMax);
		_upload.setSizeMax(this.__sizeMax);
		if (this.__listener != null) {
			_upload.setProgressListener(this.__listener);
		}
		UploadFormWrapper _form = new UploadFormWrapper();
			Map<String,List<String>> tmpParams = new HashMap<String,List<String>>();
	        Map<String,List<UploadFileWrapper>> tmpFiles = new HashMap<String,List<UploadFileWrapper>>();
	        //
			List<FileItem> _items = _upload.parseRequest(this.__request);
			for (FileItem _item : _items) {
				if (_item.isFormField()) {
					List<String> _valueList = tmpParams.get(_item.getFieldName());
					if (_valueList == null) {
						_valueList = new ArrayList<String>();
	                    tmpParams.put(_item.getFieldName(), _valueList);
					}
					try {
						_valueList.add(_item.getString(WebMVC.getConfig().getCharsetEncoding()));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				} else {
					List<UploadFileWrapper> _valueList2 = tmpFiles.get(_item.getFieldName());
	                if (_valueList2 == null){
	                	_valueList2 = new ArrayList<UploadFileWrapper>();
	                    tmpFiles.put(_item.getFieldName(), _valueList2);
	                }
	                _valueList2.add(new UploadFileWrapper(_item));
				}
			}
			//
			for (Entry<String, List<String>> entry : tmpParams.entrySet()) {
				String key = entry.getKey();
				List<String> value = entry.getValue();
				_form.getFieldMap().put(key, value.toArray(new String[value.size()]));
			}
			for (Entry<String, List<UploadFileWrapper>> entry : tmpFiles.entrySet()) {
				String key = entry.getKey();
				_form.getFileMap().put(key, entry.getValue().toArray(new UploadFileWrapper[entry.getValue().size()]));
			}
		return _form;
	}

	/**
	 * 监听器
	 * @return
	 */
	public ProgressListener getFileUploadListener() {
		return __listener;
	}

	/**
	 * 监听器
	 * @param listener
	 * @return
	 */
	public FileUploadHelper setFileUploadListener(ProgressListener listener) {
		this.__listener = listener;
		return this;
	}

	/**
	 * 上传文件临时目录（不支持自定义文件流处理）
	 * @return
	 */
	public File getUploadTempDir() {
		return __uploadTempDir;
	}

	/**
	 * 上传文件临时目录（不支持自定义文件流处理），默认使用：System.getProperty("java.io.tmpdir")
	 * @param uploadDir
	 * @return
	 */
	public FileUploadHelper setUploadTempDir(File uploadDir) {
		__uploadTempDir = uploadDir;
		return this;
	}

	/**
	 * 上传文件最大值
	 * @return
	 */
	public long getFileSizeMax() {
		return __fileSizeMax;
	}

	/**
	 * 上传文件最大值
	 * @param fileSize
	 * @return
	 */
	public FileUploadHelper setFileSizeMax(long fileSize) {
		__fileSizeMax = fileSize;
		return this;
	}

	/**
	 * 内存缓冲区的大小,默认值为10K,如果文件大于10K,将使用临时文件缓存上传文件
	 * @return
	 */
	public int getSizeThreshold() {
		return __sizeThreshold;
	}

	/**
	 * 内存缓冲区的大小,默认值为10K,如果文件大于10K,将使用临时文件缓存上传文件
	 * @param threshold
	 * @return
	 */
	public FileUploadHelper setSizeThreshold(int threshold) {
		__sizeThreshold = threshold;
		return this;
	}

	/**
	 * 上传文件总量的最大值
	 * @return
	 */
	public long getSizeMax() {
		return __sizeMax;
	}

	/**
	 * 上传文件总量的最大值
	 * @param size
	 * @return
	 */
	public FileUploadHelper setSizeMax(long size) {
		__sizeMax = size;
		return this;
	}

	/**
	 * <p>
	 * IUploadFileItemProcesser
	 * </p>
	 * <p>
	 * 文件上传处理回调接口定义，用于将每个文件交给开发者自行处理；
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
	 *          <td>2011-6-5下午03:47:49</td>
	 *          </tr>
	 *          </table>
	 */
	public interface IUploadFileItemProcesser {

		/**
		 * 处理文件或文件流
		 * @param item
		 * @return
		 * @throws IOException
		 * @throws FileUploadException
		 */
		public UploadFileWrapper process(FileItemStream item) throws IOException, FileUploadException;

	}

	/**
	 * <p>
	 * UploadFormWrapper
	 * </p>
	 * <p>
	 * 文件上传表单包装器；
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
	 *          <td>2011-6-7上午09:50:56</td>
	 *          </tr>
	 *          </table>
	 */
	public class UploadFormWrapper {

		private Map<String, String[]> __fieldMap = new HashMap<String, String[]>();

		private Map<String, IUploadFileWrapper[]> __fileMap = new HashMap<String, IUploadFileWrapper[]>();

		/**
		 * 构造器
		 */
		public UploadFormWrapper() {
		}

		/**
		 * 构造器
		 * @param fieldMap
		 * @param fileMap
		 */
		public UploadFormWrapper(Map<String, String[]> fieldMap, Map<String, UploadFileWrapper[]> fileMap) {
			this.__fieldMap.putAll(fieldMap);
			this.__fileMap.putAll(fileMap);
		}

		public Map<String, String[]> getFieldMap() {
			return this.__fieldMap;
		}

		public Map<String, IUploadFileWrapper[]> getFileMap() {
			return this.__fileMap;
		}

		public String[] getField(String fieldName) {
			return this.__fieldMap.get(fieldName);
		}

		public IUploadFileWrapper[] getFile(String fieldName) {
			return this.__fileMap.get(fieldName);
		}

	}

	/**
	 * <p>
	 * UploadFileWrapper
	 * </p>
	 * <p>
	 * 上传文件对象包装器；
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
	 *          <td>2011-6-6上午01:16:45</td>
	 *          </tr>
	 *          </table>
	 */
	public static class UploadFileWrapper implements IUploadFileWrapper {

		private FileItem __fileItemObj;
		private File __fileObj;
		private boolean __isFileObj;

		/**
		 * 构造器
		 * @param fileItem
		 */
		public UploadFileWrapper(FileItem fileItem) {
			this.__fileItemObj = fileItem;
		}

		/**
		 * 构造器
		 * @param file
		 */
		public UploadFileWrapper(File file) {
			this.__fileObj = file;
			this.__isFileObj = true;
		}

		/**
		 * 获取文件名（含路径）
		 * @return String
		 */
		public String getPath() {
			if (this.__isFileObj) {
				return this.__fileObj == null ? "" : this.__fileObj.getAbsolutePath();
			}
			return __fileItemObj.getName();
		}

		/**
		 * @return 获取文件名（不含路径）
		 */
		public String getName() {
			String _filePath = null;
			if (this.__isFileObj) {
				_filePath = this.__fileObj == null ? "" :this.__fileObj.getAbsolutePath();
			} else {
				_filePath = this.__fileItemObj.getName();
			}
			if (_filePath != null) {
				int t = _filePath.lastIndexOf("\\");
				if (t == -1) {
					t = _filePath.lastIndexOf("/");
				}
				return _filePath.substring(t + 1);
			}
			return null;
		}

		/**
		 * 获取文件内容，将其存储在字节数组中（不适合对大文件操作）
		 * 
		 * @return byte[]
		 */
		public byte[] get() {
			if (this.__isFileObj) {
				if (this.__fileObj == null) {
					return null;
				}
				byte[] _fileData = new byte[(int) this.__fileObj.length()];
		        FileInputStream _fis = null;
		        try {
		            _fis = new FileInputStream(this.__fileObj);
		            _fis.read(_fileData);
		        } catch (IOException e) {
		            _fileData = null;
		        } finally {
		            if (_fis != null) {
		                try {
		                    _fis.close();
		                } catch (IOException e) {
		                    // ignore
		                }
		            }
		        }
		        return _fileData;
			}
			return this.__fileItemObj.get();
		}

		/**
		 * 删除文件
		 */
		public void delete() {
			if (this.__isFileObj) {
				if (this.__fileObj != null && this.__fileObj.exists()) {
					this.__fileObj.delete();
		        }
			} else {
				this.__fileItemObj.delete();
			}
		}

		/**
		 * 转移文件
		 * 
		 * @param dest 目标
		 * @throws Exception
		 */
		public void transferTo(File dest) throws Exception{
	        this.__fileItemObj.write(dest);
	    }

		/**
		 * 写文件
		 * @param file
		 * @throws Exception
		 */
		public void writeTo(File file) throws Exception {
			if (this.__isFileObj) {
				if (this.__fileObj == null) {
					throw new IOException("Cannot write, file object was null!");
				}
				 if (!this.__fileObj.renameTo(file)) {
                    BufferedInputStream in = null;
                    BufferedOutputStream out = null;
                    try {
                        in = new BufferedInputStream(new FileInputStream(this.__fileObj));
                        out = new BufferedOutputStream(new FileOutputStream(file));
                        IOUtils.copy(in, out);
                    } finally {
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException e) {
                                // ignore
                            }
                        }
                        if (out != null) {
                            try {
                                out.close();
                            } catch (IOException e) {
                                // ignore
                            }
                        }
                    }
	            } else {
	                throw new IOException("Cannot write file to disk!");
	            }
			} else {
				this.__fileItemObj.write(file);
			}
		}

		/**
		 * @return 获取文件输入流对象
		 * @throws IOException
		 */
		public InputStream getInputStream() throws IOException {
			if (this.__isFileObj) {
				if (this.__fileObj == null) {
					throw new IOException("Cannot get input stream, file object was null!");
				}
				return new FileInputStream(this.__fileObj);
			}
			return this.__fileItemObj.getInputStream();
		}

		/**
		 * @return 获取文件大小
		 */
		public long getSize() {
			if (this.__isFileObj) {
				return this.__fileObj == null ? 0 : this.__fileObj.length();
			}
			return this.__fileItemObj.getSize();
		}

		/**
		 * @return 获取文件输出流对象
		 * @throws IOException
		 */
		public OutputStream getOutputStream() throws IOException {
			if (this.__isFileObj) {
				if (this.__fileObj == null) {
					throw new IOException("Cannot get output stream, file object was null!");
				}
				return new FileOutputStream(this.__fileObj);
			}
			return __fileItemObj.getOutputStream();
		}

		/* (non-Javadoc)
		 * @see net.ymate.platform.mvc.web.IUploadFileWrapper#getContentType()
		 */
		public String getContentType() {
			if (this.__isFileObj) {
				if (this.__fileObj != null) {
					return FileUtils.MIME_TYPE_MAPS.get(FileUtils.getExtName(__fileObj.getAbsolutePath()));
				}
			} else if (__fileItemObj != null) {
				return __fileItemObj.getContentType();
			}
			return null;
		}
	}

}
