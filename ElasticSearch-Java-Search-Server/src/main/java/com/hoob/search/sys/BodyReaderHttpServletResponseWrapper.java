package com.hoob.search.sys;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * http响应包装类
 */
public class BodyReaderHttpServletResponseWrapper extends HttpServletResponseWrapper {
	private ByteArrayOutputStream buffer = null;
	private ServletOutputStream out = null;
	private PrintWriter writer = null;
//
	public BodyReaderHttpServletResponseWrapper(HttpServletResponse resp) throws IOException {
		super(resp);
		buffer = new ByteArrayOutputStream();// 真正存储数据的流

		/**
		 * response输出数据时是调用getOutputStream()和getWriter()方法获取输出流，
		 * 再将数据输出到输出流对应的输出端的。
		 * 此处指定getOutputStream()和getWriter()返回的输出流的输出端为buffer，即将数据保存到buffer中。
		 */

		final ServletOutputStream bos = resp.getOutputStream();
		out = new ServletOutputStream() {
			@Override
			public void write(int b) throws IOException {
				buffer.write(b);
				bos.write(b);
			}

			@Override
			public void write(byte[] b) throws IOException {
				buffer.write(b);
				bos.write(b, 0, b.length);
			}

			@Override
			public boolean isReady() {
				return bos.isReady();
			}

			@Override
			public void setWriteListener(WriteListener writeListener) {

			}
		};
		writer = new PrintWriter(new OutputStreamWriter(buffer, this.getCharacterEncoding()));
	}

	/** 重载父类获取outputstream的方法 */
	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return out;
	}

	/** 重载父类获取writer的方法 */
	@Override
	public PrintWriter getWriter() throws UnsupportedEncodingException {
		return writer;
	}

	/** 重载父类获取flushBuffer的方法 */
	@Override
	public void flushBuffer() throws IOException {
		if (out != null) {
			out.flush();
		}
		if (writer != null) {
			writer.flush();
		}
	}

	@Override
	public void reset() {
		buffer.reset();
	}

	@Override
    public void finalize() throws Throwable {
		super.finalize();
		out.close();
		writer.close();
	}

	/** 将out、writer中的数据强制输出到WapperedResponse的buffer里面，否则取不到数据 */
	public byte[] getResponseData() throws IOException {
		flushBuffer();
		return buffer.toByteArray();
	}

	public String getContent() throws IOException {
		return new String(getResponseData(), "UTF-8");
	}
//
	public void close() throws IOException {
		out.close();
		buffer.close();
		writer.close();
	}

	// 内部类，对ServletOutputStream进行包装，指定输出流的输出端
	@SuppressWarnings("unused")
	private class WapperedOutputStream extends ServletOutputStream {

		private ByteArrayOutputStream bos = null;
//
		public WapperedOutputStream(ByteArrayOutputStream stream) throws IOException {
			bos = stream;
		}

		// 将指定字节写入输出流bos
		@Override
		public void write(int b) throws IOException {
			bos.write(b);
		}

		@Override
		public boolean isReady() {
			return false;
		}

		@Override
		public void setWriteListener(WriteListener writeListener) {
		}
	}
}