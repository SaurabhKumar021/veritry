package com.mmadapps.verisupplier.customs;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

public class Encrypt {
	/*private static final String KEY="1429111390460322";
	static String IV="90460322";*/
	 
	public String encryptText(String plainText, String key, String IV)throws Exception {
		byte[] plaintext = plainText.trim().getBytes("UTF-16LE");
		byte[] tdesKeyData = key.getBytes();
		byte[] myIV = IV.getBytes();

		Cipher c3des = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		SecretKeySpec myKey = new SecretKeySpec(tdesKeyData, "DESede");
		IvParameterSpec ivspec = new IvParameterSpec(myIV);

		c3des.init(Cipher.ENCRYPT_MODE, myKey, ivspec);
		byte[] cipherText = c3des.doFinal(plaintext);
		String encryptedString = Base64.encodeToString(cipherText,Base64.DEFAULT);
		return encryptedString.trim();

	}
}
