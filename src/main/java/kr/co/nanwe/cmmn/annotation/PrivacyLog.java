package kr.co.nanwe.cmmn.annotation;

import java.lang.annotation.Documented;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Class Name 		: PrivacyLog
 * @Description 	: 개인정보 접근기록인 경우 애노테이션
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Documented
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PrivacyLog {
	String value() default "Y";
}