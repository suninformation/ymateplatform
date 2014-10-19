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
package net.ymate.platform.mvc.web.view.impl;

import net.ymate.platform.commons.lang.PairObject;
import net.ymate.platform.commons.util.FileUtils;
import net.ymate.platform.mvc.web.context.WebContext;
import net.ymate.platform.mvc.web.view.AbstractWebView;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.net.URLEncoder;

/**
 * <p>
 * BinaryView
 * </p>
 * <p>
 * 二进制数据流视图实现类；
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
 *          <td>2011-10-23上午11:32:55</td>
 *          </tr>
 *          </table>
 */
public class BinaryView extends AbstractWebView {

    protected String fileName;
    protected Object data;

    private long maxLength = -1;

    /**
     * 构造器
     */
    public BinaryView() {
    }

    /**
     * 构造器
     *
     * @param data 数据对象
     */
    public BinaryView(Object data) {
        this.data = data;
    }

    /**
     * 构造器
     *
     * @param inputStream
     * @param maxLength   输入流数据长度
     */
    public BinaryView(InputStream inputStream, long maxLength) {
        this.data = inputStream;
        if (maxLength > 0) {
            this.maxLength = maxLength;
        }
    }

    /**
     * @param fileName   文件路径名称
     * @param attachment 是否采用档案下载的方式
     * @return 加载文件并转换成二进制视图类对象，若fileName文件不存在则返回NULL
     * @throws Exception 任何可能发生的异常
     */
    public static BinaryView loadFromFile(String fileName, boolean attachment) throws Exception {
        File _file = new File(fileName);
        if (_file.exists() && _file.isFile() && _file.canRead()) {
            BinaryView _view = new BinaryView(new FileInputStream(_file));
            _view.setContentType(FileUtils.MIME_TYPE_MAPS.get(FileUtils.getExtName(_file.getPath())));
            if (attachment) {
                _view.setFileName(_file.getName());
            }
            return _view;
        }
        return null;
    }

    /* (non-Javadoc)
     * @see net.ymate.platform.mvc.web.view.AbstractWebView#renderView()
     */
    protected void renderView() throws Exception {
        HttpServletResponse response = WebContext.getResponse();
        HttpServletRequest request = WebContext.getRequest();
        //
        response.setContentType(StringUtils.defaultIfBlank(getContentType(), "application/octet-stream"));
        //
        if (StringUtils.isNotBlank(fileName)) {
            StringBuilder _dispositionSB = new StringBuilder("attachment;filename=");
            if (request.getHeader("User-Agent").toLowerCase().contains("firefox")) {
                _dispositionSB.append(new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
            } else {
                _dispositionSB.append(URLEncoder.encode(fileName, "UTF-8"));
            }
            response.setHeader("Content-Disposition", _dispositionSB.toString());
        }
        //
        if (this.data == null) {
            return;
        }
        // 文件
        if (this.data instanceof File) {
            // 读取文件数据长度
            maxLength = ((File) this.data).length();
            // 尝试计算Range以配合断点续传
            PairObject<Long, Long> _rangePO = __doParseRange(maxLength);
            // 若为断点续传
            if (_rangePO != null) {
                __doSetRangeHeader(request, response, _rangePO);
                // 开始续传文件流
                IOUtils.copyLarge(new FileInputStream((File) this.data), response.getOutputStream(), _rangePO.getKey(), _rangePO.getValue());
            } else {
                // 正常下载
                response.setContentLength((int) IOUtils.copyLarge(new FileInputStream((File) this.data), response.getOutputStream()));
            }
        }
        // 字节数组
        else if (this.data instanceof byte[]) {
            byte[] _datas = (byte[]) this.data;
            IOUtils.write(_datas, response.getOutputStream());
            response.setContentLength(_datas.length);
        }
        // 字符数组
        else if (this.data instanceof char[]) {
            char[] _datas = (char[]) this.data;
            IOUtils.write(_datas, response.getOutputStream());
            response.setContentLength(_datas.length);
        }
        // 文本流
        else if (this.data instanceof Reader) {
            Reader r = (Reader) this.data;
            IOUtils.copy(r, response.getOutputStream());
        }
        // 二进制流
        else if (this.data instanceof InputStream) {
            PairObject<Long, Long> _rangePO = __doParseRange(maxLength);
            if (_rangePO != null) {
                __doSetRangeHeader(request, response, _rangePO);
                IOUtils.copyLarge((InputStream) this.data, response.getOutputStream(), _rangePO.getKey(), _rangePO.getValue());
            } else {
                response.setContentLength((int) IOUtils.copyLarge((InputStream) this.data, response.getOutputStream()));
            }
        }
        // 普通对象
        else {
            IOUtils.write(String.valueOf(data), response.getOutputStream());
        }
    }

    private void __doSetRangeHeader(HttpServletRequest request, HttpServletResponse response, PairObject<Long, Long> range) {
        // 表示使用了断点续传（默认是“none”，可以不指定）
        response.setHeader("Accept-Ranges", "bytes");
        // Content-Length: [文件的总大小] - [客户端请求的下载的文件块的开始字节]
        long _totalLength = range.getValue() - range.getKey();
        response.setHeader("Content-Length", _totalLength + "");
        // Content-Range: bytes [文件块的开始字节]-[文件的总大小 - 1]/[文件的总大小]
        response.setHeader("Content-Range", "bytes " + range.getKey() + "-" + (range.getValue() - 1) + "/" + maxLength);
        // response.setHeader("Connection", "Close"); //如果有此句话不能用IE直接下载
        // Status: 206
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
    }

    /**
     * 分析请求头中的Range参数，计算并返回本次数据的开始和结束位置
     *
     * @param maxLength 数据大小
     * @return 若非断点续传则返回null
     */
    private PairObject<Long, Long> __doParseRange(long maxLength) {
        PairObject<Long, Long> _returnValue = null;
        // 通过请求头Range参数判断是否采用断点续传
        String _rangeStr = WebContext.getRequest().getHeader("Range");
        if (_rangeStr != null && _rangeStr.startsWith("bytes=") && _rangeStr.length() >= 7) {
            _rangeStr = StringUtils.substringAfter(_rangeStr, "bytes=");
            String[] _ranges = StringUtils.split(_rangeStr, ",");
            // 可能存在多个Range，目前仅处理第一个...
            for (String _range : _ranges) {
                if (StringUtils.isBlank(_range)) {
                    return null;
                }
                try {
                    // bytes=-100
                    if (_range.startsWith("-")) {
                        long _end = Long.parseLong(_range);
                        long _start = maxLength + _end;
                        if (_start < 0) {
                            return null;
                        }
                        _returnValue = new PairObject<Long, Long>(_start, maxLength);
                        break;
                    }
                    // bytes=1024-
                    if (_range.endsWith("-")) {
                        long _start = Long.parseLong(StringUtils.substringBefore(_range, "-"));
                        if (_start < 0) {
                            return null;
                        }
                        _returnValue = new PairObject<Long, Long>(_start, maxLength);
                        break;
                    }
                    // bytes=10-1024
                    if (_range.contains("-")) {
                        String[] _tmp = _range.split("-");
                        long _start = Long.parseLong(_tmp[0]);
                        long _end = Long.parseLong(_tmp[1]);
                        if (_start > _end) {
                            return null;
                        }
                        _returnValue = new PairObject<Long, Long>(_start, _end + 1);
                    }
                } catch (Throwable e) {
                    return null;
                }
            }
        }
        return _returnValue;
    }

    /**
     * @param data the data to set
     */
    public void setData(Object data) {
        this.data = data;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}