package com.invengo.basetmp;

import android.util.Log;

import com.atid.lib.system.ModuleControl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android_serialport_api.SerialPort;

/**
 * 基础的串口通信模板
 * Created by 李泽荣 on 2018/11/7.
 */

public class Rd {
	private String drv = "/dev/ttyS4";		// 串口驱动名
	private int br = 115200;	// 波特率

	private SerialPort sp = null;	// 串口对象
	private ReadThread rt = null;	// 读取线程
	private boolean lock = false;	// 指令锁

	public Rd (String d, int b) {
		drv = d;
		br = b;
	}

	// 开启电源
	private void powerOn () {
		ModuleControl.powerRfidEx(true, 2, 4);
	}

	// 关闭电源
	private void powerOff () {
		ModuleControl.powerRfidEx(false, 2, 4);
	}

	// 读数据线程
	private class ReadThread extends Thread {
		private static final String TAG = "-------- 串口读数据";
		private InputStream is = null;	// 串口的输入流

		ReadThread (InputStream input) {
			this.is = input;
		}

		@Override
		public void run() {
			super.run();
			if (this.is != null) {
				int n = 0;
				int j = 0;
				byte[] r = null;
				while(!isInterrupted()) {
					int size;
					try {
						byte[] buf = new byte[32];
						Log.i(TAG, "挂机 ...");
						size = this.is.read(buf);
						if (size > 0) {
							Log.i(TAG, "------------ s : " + size);
							for (int i = 0; i < size; i++) {
								Log.i(TAG, Integer.toHexString(buf[i]));
							}
							Log.i(TAG, "------------ e");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					lock = false;
				}
				Log.i(TAG, "Over");
			}
		}
	}

	// 发送数据
	public void send (byte[] command) {
		if (!lock) {
			lock = true;
			try {
				this.sp.getOutputStream().write(command);
			} catch (IOException e) {
				lock = false;
				e.printStackTrace();
			}
		}
	}

	public void open() {
		// 开启电源
		powerOn();

		// 开启串口
		if (this.sp == null) {
			try {
				File f = new File(drv);
//Log.i("------", f.canRead() + " , " + f.canWrite());
				this.sp = new SerialPort(new File(drv), br, 0);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}

			// 启动读数据线程
			this.rt = new ReadThread(this.sp.getInputStream());
			this.rt.start();
		}
	}

	public void close() {
		// 关闭串口
		if (this.sp != null) {
			// 关闭读数据线程
			this.rt.interrupt();
			this.rt = null;
			this.sp.close();
			this.sp = null;
		}

		// 关闭电源
		powerOff ();
	}

}
