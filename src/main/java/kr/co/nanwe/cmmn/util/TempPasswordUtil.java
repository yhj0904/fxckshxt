package kr.co.nanwe.cmmn.util;

import java.util.Random;

/**
 * @Class Name 		: TempPasswordUtil
 * @Description 	: 임시 비밀번호 생성
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public class TempPasswordUtil {
	
	private Random randomAlpha = null;
	private Random randomSpec = null;
	private Random randomNum = null;
	
	private int alphaCnt = 0;
	private int specCnt = 0;
	private int numCnt = 0;

	private final int MAX_ALPHA_COUNT = 8;
	private final int MAX_SPEC_COUNT = 2;
	private final int MAX_NUM_COUNT = 2;
	
	private final char[] PASSWORD_ALPHA_TABLE =  { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 
										             'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
										             'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
										             'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 
										             'w', 'x', 'y', 'z'};
	
	private final char[] PASSWORD_SPEC_TABLE =  {'!', '@', '#', '$', '%', '^', '&', '*'};

	private final char[] PASSWORD_NUM_TABLE =  {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
	
	public String getTempPassword() {
		
		Random random = new Random(System.currentTimeMillis());
		randomAlpha = new Random(System.currentTimeMillis());
		randomSpec = new Random(System.currentTimeMillis());
		randomNum = new Random(System.currentTimeMillis());
		
		int passwordLength = MAX_ALPHA_COUNT + MAX_SPEC_COUNT + MAX_NUM_COUNT;
		
		StringBuffer buf = new StringBuffer();
		for(int i = 0; i < passwordLength; i++) {
			String str = "";
			int idx = 0;
			if (i == 0) {
				str = getRandomAlpha();
			} else if(alphaCnt < MAX_ALPHA_COUNT && specCnt < MAX_SPEC_COUNT && numCnt < MAX_NUM_COUNT) {
				idx = random.nextInt(3);
				switch (idx) {
					case 0:
						str = getRandomAlpha();
						break;
					case 1:
						str = getRandomSpec();
						break;
					case 2:
						str = getRandomNum();
						break;
					default:
						str = getRandomAlpha();
						break;
				}
			} else if (alphaCnt < MAX_ALPHA_COUNT && specCnt < MAX_SPEC_COUNT) {
				idx = random.nextInt(2);
				switch (idx) {
					case 0:
						str = getRandomAlpha();
						break;
					case 1:
						str = getRandomSpec();
						break;
					default:
						str = getRandomAlpha();
						break;
				}
			} else if (alphaCnt < MAX_ALPHA_COUNT && numCnt < MAX_NUM_COUNT) {
				idx = random.nextInt(2);
				switch (idx) {
					case 0:
						str = getRandomAlpha();
						break;
					case 1:
						str = getRandomNum();
						break;
					default:
						str = getRandomAlpha();
						break;
				}
			} else if (specCnt < MAX_SPEC_COUNT && numCnt < MAX_NUM_COUNT) {
				idx = random.nextInt(2);
				switch (idx) {
					case 0:
						str = getRandomSpec();
						break;
					case 1:
						str = getRandomNum();
						break;
					default:
						str = getRandomSpec();
						break;
				}
			} else if (alphaCnt < MAX_ALPHA_COUNT) {
				str = getRandomAlpha();
			} else if (specCnt < MAX_SPEC_COUNT) {
				str = getRandomSpec();
			} else if (numCnt < MAX_NUM_COUNT) {
				str = getRandomNum();
			}
			buf.append(str);
        }
		return buf.toString();
	}
	
	private String getRandomAlpha() {
		alphaCnt++;
		return Character.toString(PASSWORD_ALPHA_TABLE[randomAlpha.nextInt(PASSWORD_ALPHA_TABLE.length)]);
	}
	
	private String getRandomSpec() {
		specCnt++;
		return Character.toString(PASSWORD_SPEC_TABLE[randomSpec.nextInt(PASSWORD_SPEC_TABLE.length)]);
	}
	
	private String getRandomNum() {
		numCnt++;
		return Character.toString(PASSWORD_NUM_TABLE[randomNum.nextInt(PASSWORD_NUM_TABLE.length)]);
	}
	
}