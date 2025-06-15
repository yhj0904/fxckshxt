package kr.co.nanwe.cmmn.validator;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.ValidatorAction;
import org.springframework.validation.Errors;
import org.springmodules.validation.commons.FieldChecks;

/**
 * @Class Name 		: ComFieldChecks
 * @Description 	: 커스텀 validator를 위한 필드 체크 클래스
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

@SuppressWarnings("serial")
public class ComFieldChecks extends FieldChecks {
	
	/** 아이디 유효성 검사 */
	public static boolean validateIsId(Object bean, ValidatorAction va, Field field, Errors errors) {
		String value = FieldChecks.extractValue(bean, field);
		if (!GenericValidator.isBlankOrNull(value)) {
            try {
            	if (!ComGenericValidator.isValidId(value)) {
        			FieldChecks.rejectValue(errors, field, va);
        			return false;
        		}
            } catch (Exception e) {
                FieldChecks.rejectValue(errors, field, va);
                return false;
            }
        }
        return true;		
	}
	
	/** 비밀번호 유효성 검사 */
	public static boolean validateIsPw(Object bean, ValidatorAction va, Field field, Errors errors) {
		String value = FieldChecks.extractValue(bean, field);
		if (!GenericValidator.isBlankOrNull(value)) {
            try {
            	if (!ComGenericValidator.isValidPw(value)) {
        			FieldChecks.rejectValue(errors, field, va);
        			return false;
        		}
            } catch (Exception e) {
                FieldChecks.rejectValue(errors, field, va);
                return false;
            }
        }
        return true;		
	}
	
	/** 날짜 유효성 검사 */
	public static boolean validateIsDate(Object bean, ValidatorAction va, Field field, Errors errors) {
		String value = FieldChecks.extractValue(bean, field);
		if (!GenericValidator.isBlankOrNull(value)) {
            try {
            	if (!ComGenericValidator.isValidDate(value)) {
        			FieldChecks.rejectValue(errors, field, va);
        			return false;
        		}
            } catch (Exception e) {
                FieldChecks.rejectValue(errors, field, va);
                return false;
            }
        }
        return true;
	}
	
	/** 글자수 유효성 검사 */
	public static boolean validateIsLength(Object bean, ValidatorAction va, Field field, Errors errors) {
		String value = FieldChecks.extractValue(bean, field);
		if (!GenericValidator.isBlankOrNull(value)) {
            try {
            	int min = Integer.parseInt(field.getVarValue("min"));
        		int max = Integer.parseInt(field.getVarValue("max"));
        		if (!ComGenericValidator.isValidLength(value, min, max)) {
        			FieldChecks.rejectValue(errors, field, va);
        			return false;
        		}
            } catch (Exception e) {
                FieldChecks.rejectValue(errors, field, va);
                return false;
            }
        }
        return true;
	}
	
	/** 숫자인지 체크 */
	public static boolean validateIsNum(Object bean, ValidatorAction va, Field field, Errors errors) {
		String value = FieldChecks.extractValue(bean, field);
		if (!GenericValidator.isBlankOrNull(value)) {
            try {
            	if (!ComGenericValidator.isValidNum(value)) {
        			FieldChecks.rejectValue(errors, field, va);
        			return false;
        		}
            } catch (Exception e) {
                FieldChecks.rejectValue(errors, field, va);
                return false;
            }
        }
        return true;
	}
	
	/** 최소숫자 체크 */
	public static boolean validateIsMinNum(Object bean, ValidatorAction va, Field field, Errors errors) {
		String value = FieldChecks.extractValue(bean, field);
		if (!GenericValidator.isBlankOrNull(value)) {
            try {
            	int intValue = Integer.parseInt(value);
            	int min = Integer.parseInt(field.getVarValue("min"));
            	if (!ComGenericValidator.isValidMinNum(intValue, min)) {
        			FieldChecks.rejectValue(errors, field, va);
        			return false;
        		}
            } catch (Exception e) {
                FieldChecks.rejectValue(errors, field, va);
                return false;
            }
        }
        return true;
	}
	
	/** 최대숫자 체크 */
	public static boolean validateIsMaxNum(Object bean, ValidatorAction va, Field field, Errors errors) {
		String value = FieldChecks.extractValue(bean, field);
		if (!GenericValidator.isBlankOrNull(value)) {
            try {
            	int intValue = Integer.parseInt(value);
            	int max = Integer.parseInt(field.getVarValue("max"));
            	if (!ComGenericValidator.isValidMaxNum(intValue, max)) {
        			FieldChecks.rejectValue(errors, field, va);
        			return false;
        		}
            } catch (Exception e) {
                FieldChecks.rejectValue(errors, field, va);
                return false;
            }
        }
        return true;
	}
	
	/** 아이피 유효성 검사 */
	public static boolean validateIsIp(Object bean, ValidatorAction va, Field field, Errors errors) {
		String value = FieldChecks.extractValue(bean, field);
		if (!GenericValidator.isBlankOrNull(value)) {
            try {
            	if (!ComGenericValidator.isValidIp(value)) {
        			FieldChecks.rejectValue(errors, field, va);
        			return false;
        		}
            } catch (Exception e) {
                FieldChecks.rejectValue(errors, field, va);
                return false;
            }
        }
        return true;		
	}
}
