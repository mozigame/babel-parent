package com.babel.common.core.tools.qr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.google.zxing.WriterException;

public class QrTest {
	public static void main(String[] args) {
		int iWidth = 400;
		int iHeight = 400;
		File a = new File("D:/qr.png");
		try {
			OutputStream out = new FileOutputStream(a);
			MatrixToImageWriter.createRqCode("http://weixin.qq.com/q/02C5cv1ne490_100000075","D:/logo.png", iWidth, iHeight, out);
			System.out.println("生成完毕！！");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
