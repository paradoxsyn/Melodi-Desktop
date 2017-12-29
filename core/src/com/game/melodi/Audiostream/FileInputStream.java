package com.game.melodi.Audiostream;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

import sun.nio.ch.FileChannelImpl;

public class FileInputStream extends InputStream {

	InputStream in;
	FileChannel channel = null;

	public FileInputStream(File file) {
		in = file.fh.read();
	}

	@Override
	public int read() throws IOException {
		return in.read();
	}

	@Override
	public int available() throws IOException {
		return in.available();
	}

	@Override
	public void close() throws IOException {
		in.close();
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return in.read(b, off, len);
	}

	@Override
	public int read(byte[] b) throws IOException {
		return in.read(b);
	}

	@Override
	public long skip(long n) throws IOException {
		return in.skip(n);
	}

}
