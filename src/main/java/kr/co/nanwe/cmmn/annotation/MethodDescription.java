package kr.co.nanwe.cmmn.annotation;

import java.lang.annotation.Documented;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Class Name 		: MethodDescription
 * @Description 	: 로그 기록을 위한 메소드 설명 애노테이션
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@Documented
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodDescription {
	String value() default "";
}