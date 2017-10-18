package com.babel.common.core.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;


public class DESEncrypt {
//	public static void main(String[] args) {
//		try {
//			System.out.println(encrypt("12345678", "abc", "12345678"));
//			System.out.println(encrypt("12345678", "ABC", "12345678"));
//
//			System.out.println(decrypt("12345678", "9YR6ZPdZufM=", "12345678"));
//			System.out.println(decrypt("12345678", "6rtTnrF34mPkJ5SO3RiaaQ==", "12345678"));
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public static String encrypt(String key, String str, String ivString) throws Exception {
		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		Key secretKey = keyFactory.generateSecret(desKeySpec);

		IvParameterSpec iv = new IvParameterSpec(ivString.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
		byte[] bytes = cipher.doFinal(str.getBytes());
		return new String(Base64.encodeBase64(bytes));
	}

	public static String decrypt(String key, String str, String ivString) throws Exception {
		byte[] data = Base64.decodeBase64(str);
		DESKeySpec dks = new DESKeySpec(key.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		Key secretKey = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		IvParameterSpec iv = new IvParameterSpec(ivString.getBytes());
		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
		byte[] decryptedBytes = cipher.doFinal(data);
		return new String(decryptedBytes, "gbk");
	}
}
