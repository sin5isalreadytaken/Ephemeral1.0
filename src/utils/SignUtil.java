package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class SignUtil {
	private static String token = "wechatechostr";
	
	public static boolean checkSignature(String signature, String timestamp, String nonce){
		String[] paramArr = new String[]{token, timestamp, nonce};
		Arrays.sort(paramArr);//sort token,timestamp,nonce by dictionary
		String content = paramArr[0].concat(paramArr[1]).concat(paramArr[2]);//spliced it together into a string
		String ciphertext = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] digest = md.digest(content.toString().getBytes());//encryption the after-stitching string
			ciphertext = byteToStr(digest);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ciphertext != null ? ciphertext.equals(signature.toUpperCase()) : false;//compare encrypted string with signature
	}
	
	public static String byteToStr(byte[] byteArray){
		String strDigest = "";
		for (int i = 0; i < byteArray.length; i++){
			strDigest += byteToHexStr(byteArray[i]);
		}
		return strDigest;
	}
	
	public static String byteToHexStr(byte mByte){
		char[] Digit = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
		char[] tempArr = new char[2];
		tempArr[0] = Digit[(mByte >>> 4) & 0x0F];
		tempArr[1] = Digit[mByte & 0x0F];
		String s  = new String(tempArr);
		return s;
	}
}
