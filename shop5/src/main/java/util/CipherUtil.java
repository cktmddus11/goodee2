package util;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CipherUtil {
	private static byte[] randomKey;
	private final static byte[] iv = new byte[] {
			(byte) 0x8E, 0x12, 0x39, (byte) 0x9C,
						0x07, 0x72, 0x6F, (byte) 0x5A,
			(byte) 0x8E, 0x12, 0x39, (byte) 0x9C,
						0x07, 0x72, 0x6F, (byte) 0x5A };
	static Cipher cipher;
	static {
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static byte[] getRandomKey(String algo) throws NoSuchAlgorithmException{
		// keyGen : AES 용 키를 생성할 수 있는 객체
		KeyGenerator keyGen = KeyGenerator.getInstance(algo);
		// AES 알고리즘 : 128 bit ~ 192(?) bit 키로 설정이 가능
		keyGen.init(128); // 128비트짜리 키로 생성 
		SecretKey key = keyGen.generateKey(); 
		return key.getEncoded();
	}
	public static String encrypt(String plain) {
		byte[] cipherMsg = new byte[1024];
		try {
			// 암호화에 필요한 키를 생성
			randomKey = getRandomKey("AES");
			// key : 암호화에 필요한 키 객체 
			Key key = new SecretKeySpec(randomKey, "AES");
			// 초기화 벡터 객체 생성
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
			// Cipher.ENCRYPT_MODE : 암호화
			cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
			// 암호화 실행
			cipherMsg = cipher.doFinal(plain.getBytes());
		}catch(Exception e) {
			e.printStackTrace();
		}
		return byteToHex(cipherMsg).trim();
	}
	// 바이트값을 문자열 형태로 리턴해줌
	private static String byteToHex(byte[] cipherMsg) {
		if(cipherMsg == null) 
			return null;
		String str = "";
		for(byte b : cipherMsg) {
			str += String.format("%02X",  b);
		}
		return str;
	}
	public static String decrypt(String cipherMsg) {
		byte[] plainMsg = new byte[1024];
		try {                                        // 랜덤키여서 결과값이 실행할 때마다 다름
			Key key = new SecretKeySpec(randomKey, "AES");
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
			//Cipher.DECRYPT_MODE : 복호화 모드
			cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
			plainMsg = cipher.doFinal(hexToByte(cipherMsg.trim()));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new String(plainMsg).trim();
	}
	/* 
	 * 한바이트당 2개 문자 B0

		암호화
		byte[] 
		10110000 	
		ㅡㅡㅡ  ㅡㅡㅡ  ㅡㅡㅡ ㅡㅡㅡ
		한개당 4바이트?
		HEX -> B0 F0 00 FF 이자체 문자열 
		haxtobyte 를 호출해서 바이트형 배열로 변환?  
	 * 
	 * */
	private static byte[] hexToByte(String str) {
		// db에 있는거에 1/2? byte여서?
		if(str == null || str.length() < 2) 
			return null;
		int len = str.length() / 2; // B0 -> 2이하 01 B0
		byte[] buf = new byte[len];
		for(int i = 0;i<len;i++) { // 
			buf[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
		}
		return buf;
	}
	public static String encrypt(String plain, String key) {
		byte[] cipherMsg = new byte[1024];
		try {                                        // 사용자가 만든 키여서 결과값이 동일함
			Key genKey = new SecretKeySpec(makeKey(key), "AES");
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
			// 암호화 객체
			cipher.init(Cipher.ENCRYPT_MODE, genKey, paramSpec);
			cipherMsg = cipher.doFinal(plain.getBytes());
		}catch(Exception e) {
			e.printStackTrace();
		}
		return byteToHex(cipherMsg); // 바이트형 문자열로변환하는 함수호출?
	}
	// AES 용 키의 크기 : 128비트 => 16바이트 
	private static byte[] makeKey (String key) {
		int len = key.length(); // 10
		char ch = 'A'; // B
		// key = abc1234567ABCDEF -> 16개
		for(int i = len;i<16;i++) key += ch++; 
		return key.substring(0, 16).getBytes(); // 16 hex? 
	}
	public static String decrypt(String cipher1, String key) {
		byte[] plainMsg = new byte[1024];
		try {
			Key genkey = new SecretKeySpec(makeKey(key), "AES");
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
			//Cipher.DECRYPT_MODE : 복호화 모드
			cipher.init(Cipher.DECRYPT_MODE, genkey, paramSpec);
			plainMsg = cipher.doFinal(hexToByte(cipher1.trim()));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new String(plainMsg).trim();
	}
	
	//////////
//	
//	private static byte[] makeKey2 (String key) throws NoSuchAlgorithmException {
//		MessageDigest md = MessageDigest.getInstance("SHA-256");
//		String hashid = "";
//		byte[] plain = key.getBytes();
//		byte[] hash = md.digest(plain);
//		for(byte b : hash) {
//			hashid += String.format("%02X", b);
//		}
//		System.out.println(key);
//		return hashid.substring(0, 16).getBytes(); // 16 hex? 
//	}
//	public static String decrypt2(String cipher1, String key) {
//		byte[] plainMsg = new byte[1024];
//		try {
//			Key genkey = new SecretKeySpec(makeKey2(key), "AES");
//			AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
//			//Cipher.DECRYPT_MODE : 복호화 모드
//			cipher.init(Cipher.DECRYPT_MODE, genkey, paramSpec);
//			plainMsg = cipher.doFinal(hexToByte(cipher1.trim()));
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//		return new String(plainMsg).trim();
//	}
//	
//	public static String encrypt2(String plain, String key) {
//		byte[] cipherMsg = new byte[1024];
//		try {
//			// key : 암호화에 필요한 키 객체 
//			Key genkey = new SecretKeySpec(makeKey2(key), "AES");
//			// 초기화 벡터 객체 생성
//			AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
//			// Cipher.ENCRYPT_MODE : 암호화
//			cipher.init(Cipher.ENCRYPT_MODE, genkey, paramSpec);
//			// 암호화 실행
//			cipherMsg = cipher.doFinal(plain.getBytes());
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//		return byteToHex(cipherMsg).trim();
//	}
	public static String makehash (String key) throws Exception {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] plain = key.getBytes();
		byte[] hash = md.digest(plain);
		return byteToHex(hash); //이걸로 리턴해서 함수 한번더 호출해서 바이트형 배열로 변형 
	}
	
	
		
}
