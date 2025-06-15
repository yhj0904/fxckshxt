package kr.co.nanwe.cmmn.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * @Class Name 		: MessageUtil
 * @Description 	: 다국어 지원을 위한 메세지 클래스
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Component("messageUtil")
public class MessageUtil {
	
	@Autowired
	private MessageSource messageSource;
	
	
	/**
	 * 해당 메시지 코드의 값을 가져온다
	 *
	 * @param 
	 * @return 
	 */
	public String getMessage(String code) {
		return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
	}
	
	/**
	 * 해당 메시지 코드의 값을 가져온다
	 *
	 * @param 
	 * @return 
	 */
	public String getMessage(String code, Object[] args) {
		return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
	}
		
}