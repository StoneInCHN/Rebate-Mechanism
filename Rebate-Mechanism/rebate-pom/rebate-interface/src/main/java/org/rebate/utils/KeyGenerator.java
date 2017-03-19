/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rebate.utils;

import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

/**
 * 
 * @author tallauze
 */
public class KeyGenerator {

	public static final String ALGORITHM = "RSA";
	
	public static final String CIPHER_DECRYPT_ALGORYTHM = "RSA/ECB/PKCS1Padding";
	public static final String CIPHER_ENCRYPT_ALGORYTHM = "RSA/ECB/PKCS1Padding";

	public static KeyPair generateKeys() {
		KeyPairGenerator kpg = null;
		try {
			kpg = KeyPairGenerator.getInstance(ALGORITHM);
			kpg.initialize(1024);
		} catch (NoSuchAlgorithmException ex) {
			Logger.getLogger(KeyGenerator.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		KeyPair kp = kpg.generateKeyPair();
		return kp;
	}

	public static String encrypt(String text, PublicKey key) {
		Cipher cipher = null;
		byte[] cipherText = null;
		try {
			cipher = Cipher.getInstance(CIPHER_ENCRYPT_ALGORYTHM);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			cipherText = cipher.doFinal(text.getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		String temp = null;
		try {
			temp = new String(Base64.encodeBase64(cipherText), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return temp;
	}

	public static String decrypt(String text, PrivateKey key) {
		byte[] cryptedText = Base64.decodeBase64(text);
		byte[] decryptedText = null;
		Cipher cipher = null;
		String temp = null;
		try {
			cipher = Cipher.getInstance(CIPHER_DECRYPT_ALGORYTHM);
			cipher.init(Cipher.DECRYPT_MODE, key);
			decryptedText = cipher.doFinal(cryptedText);
			temp = new String(decryptedText);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return temp;
	}
}
