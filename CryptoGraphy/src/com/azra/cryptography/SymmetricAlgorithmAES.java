package com.azra.cryptography;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

public class SymmetricAlgorithmAES {
	static final String TAG = "SymmetricAlgorithmAES";

	static SecretKeySpec secretKeySpec = null;

	public static void SetSecretKeySpecs() {
		secretKeySpec = null;
	}

	public static SecretKeySpec GenerateSecretKeySpecs() {
		// Set up secret key spec for 128-bit AES encryption and decryption
		try {
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
			sr.setSeed("any data used as random seed".getBytes());
			KeyGenerator kg = KeyGenerator.getInstance("AES");
			kg.init(128, sr);
			return new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static String encryptString(String dataToEncrypt) {

		try {

			if (Prefs.getKey(Prefs.SECRET_KEY) == "") {
				secretKeySpec = GenerateSecretKeySpecs();
				String stringSecretKey = Base64.encodeToString(
						secretKeySpec.getEncoded(), Base64.DEFAULT);
				Prefs.addKey(Prefs.SECRET_KEY, stringSecretKey);
			}
			if (Prefs.getKey(Prefs.SECRET_KEY) != "") {
				byte[] encodedBytes = null;

				Cipher c = Cipher.getInstance("AES");
				String key = Prefs.getKey(Prefs.SECRET_KEY);

				byte[] encodedKey = Base64.decode(key, Base64.DEFAULT);
				SecretKey originalKey = new SecretKeySpec(encodedKey, 0,
						encodedKey.length, "AES");
				c.init(Cipher.ENCRYPT_MODE, originalKey);
				encodedBytes = c.doFinal(dataToEncrypt.getBytes());

				return Base64.encodeToString(encodedBytes, Base64.DEFAULT);
			} else {
				return null;
			}
		} catch (Exception e) {
			// Log.e(TAG, "AES encryption error");
			return null;
		}
	}

	public static String decryptString(String dataToDecrypt) {

		if (Prefs.getKey(Prefs.SECRET_KEY) != "") {
			byte[] decodedBytes = null;
			try {
				Cipher c = Cipher.getInstance("AES");

				String key = Prefs.getKey(Prefs.SECRET_KEY);
				byte[] encodedKey = Base64.decode(key, Base64.DEFAULT);
				SecretKey originalKey = new SecretKeySpec(encodedKey, 0,
						encodedKey.length, "AES");
				c.init(Cipher.DECRYPT_MODE, originalKey);

				byte[] dataInBytes = Base64.decode(dataToDecrypt,
						Base64.DEFAULT);

				decodedBytes = c.doFinal(dataInBytes);
				return new String(decodedBytes);
			} catch (Exception e) {
				// Log.e(TAG, "AES decryption error");
				e.printStackTrace();
				return null;
			}

		} else
			return null;

	}

}
