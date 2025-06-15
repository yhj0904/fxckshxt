package kr.co.nanwe.cmmn.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Class Name 		: EncryptUtil
 * @Description 	: SHA 256 암호화 클래스
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public class EncryptUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EncryptUtil.class);

	public static String encryptMsg(String msg) {
		
		try {
			MessageDigest md;
			
			md = MessageDigest.getInstance("SHA-256");
			md.update(msg.getBytes());

			String hashMsg = bytesToHex(md.digest());
			
			return hashMsg;
			
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error(e.getMessage());
		}
		return null;
		
	}

	private static String bytesToHex(byte[] bytes) {
		
		StringBuilder builder = new StringBuilder();
		
		for (byte b : bytes) {
			builder.append(String.format("%02x", b));
		}
		
		return builder.toString();
	}
	
	public static String encryptMsgWithSalt(String msg, String salt) {
		
		String result = "";
        
        byte[] a = msg.getBytes();
        byte[] b = salt.getBytes();
        byte[] bytes = new byte[a.length + b.length];
         
        System.arraycopy(a, 0, bytes, 0, a.length);
        System.arraycopy(b, 0, bytes, a.length, b.length);
			
		try {
			
			MessageDigest md;
			
			md = MessageDigest.getInstance("SHA-256");
			md.update(bytes);

			byte[] byteData = md.digest();

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xFF) + 256, 16).substring(1));
			}

			result = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error(e.getMessage());
		}

		return result;
		
	}
	
	 /**
     * SALT 생성
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String getSalt() {
        String value = "";
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[16];
            secureRandom.nextBytes(salt);
            value = salt.toString();
        } catch(NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage());
        }
        return value;
    }
}
