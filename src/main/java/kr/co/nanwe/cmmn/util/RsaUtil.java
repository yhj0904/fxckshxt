package kr.co.nanwe.cmmn.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Class Name 		: RsaUtil
 * @Description 	: 
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Component("rsaUtil")
public class RsaUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RsaUtil.class);

	public String[] createRsaSession(HttpServletRequest request) {
		
		String[] arr = new String[2];
		
		removeRsaSession(request);

		try {
			SecureRandom secureRandom = new SecureRandom();
			
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(1024, secureRandom);
			KeyPair keyPair = generator.genKeyPair();
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey publicKey = keyPair.getPublic();
			PrivateKey privateKey = keyPair.getPrivate();
	 
			request.getSession().setAttribute("_RSA_WEB_Key_", privateKey);
			RSAPublicKeySpec publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
			String publicKeyModulus = publicSpec.getModulus().toString(16);
			String publicKeyExponent = publicSpec.getPublicExponent().toString(16);
	 
//			request.setAttribute("RSAModulus", publicKeyModulus);
//			request.setAttribute("RSAExponent", publicKeyExponent);
			
			arr[0] = publicKeyModulus;
			arr[1] = publicKeyExponent;
			
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			LOGGER.error(e.getMessage());
		}
		
		return arr;
	}
	
	public void removeRsaSession(HttpServletRequest request) {
		if(request.getSession().getAttribute("_RSA_WEB_Key_") != null) {
			request.getSession().removeAttribute("_RSA_WEB_Key_");
		}
	}
	
	public PrivateKey getPrivateKey(HttpServletRequest request) {
		PrivateKey privateKey = (PrivateKey) request.getSession().getAttribute("_RSA_WEB_Key_");
		if (privateKey == null) { 
			return null;
		}
		return privateKey;
	}
	
	public String decryptRsa(PrivateKey privateKey, String securedValue) {
		
		if(securedValue == null) {
			return null;
		}
		
		String decryptedValue = "";
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			byte[] encryptedBytes = hexToByteArray(securedValue);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
			decryptedValue = new String(decryptedBytes, "utf-8");
		} catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException e) {
			LOGGER.error(e.getMessage());
		}
		return decryptedValue;
	}

	/**
	 * 16진 문자열을 byte 배열로 변환한다.
	 */
	public strictfp static byte[] hexToByteArray(String hex) {
		if (hex == null || hex.length() % 2 != 0) {
			return new byte[] {};
		}

		byte[] bytes = new byte[hex.length() / 2];
		for (int i = 0; i < hex.length(); i += 2) {
			byte value = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
			bytes[(int) Math.floor(i / 2)] = value;
		}
		return bytes;
	}
	
}
