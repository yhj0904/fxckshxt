package kr.co.nanwe.cmmn.bean;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.nanwe.cmmn.annotation.Program;
import kr.co.nanwe.cmmn.annotation.ProgramInfo;
import kr.co.nanwe.cmmn.util.StringUtil;

/**
 * @Class Name 		: ProgramControllerScan
 * @Description 	: 프로그램 컨트롤러 Method를 스캔
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.01.06		임문환			최초생성
 */

public class ProgramControllerScan {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProgramControllerScan.class);
	
	public static List<Map<String, Object>> getProgramList(){		
		//Return 할 리스트
		List<Map<String, Object>> programList = new ArrayList<Map<String, Object>>();
		
		//패키지의 Controller 클래스를 스캔
		List<ClassMetadata> classMetaScans = classMetaScan("classpath*:kr/co/nanwe/**/*Controller.class", Program.class);
		for (ClassMetadata classMetadata : classMetaScans) {
			String className = classMetadata.getClassName();
			try {
				Class<?> c = Class.forName(className);
				Program program = c.getAnnotation(Program.class);
				if(program != null) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("code", program.code());
					map.put("name", program.name());
					RequestMapping requestMapping = c.getAnnotation(RequestMapping.class);
					if(requestMapping != null) {
						map.put("uri", requestMapping.value()[0] + ".do");
					}
					programList.add(map);
				}
			} catch (ClassNotFoundException e) {
				LOGGER.error(e.getMessage());
			}
			
		}
		return programList;
	}
	
	
	public static List<Map<String, Object>> getProgramListByMenuCd(String menuCd){		
		//Return 할 리스트
		List<Map<String, Object>> programList = new ArrayList<Map<String, Object>>();
		
		//패키지의 Controller 클래스를 스캔
		List<ClassMetadata> classMetaScans = classMetaScan("classpath*:kr/co/nanwe/**/*Controller.class", Program.class);
		for (ClassMetadata classMetadata : classMetaScans) {
			String className = classMetadata.getClassName();
			try {
				Class<?> c = Class.forName(className);
				Program program = c.getAnnotation(Program.class);
				if(program != null) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("code", program.code());
					map.put("name", program.name());
					RequestMapping requestMapping = c.getAnnotation(RequestMapping.class);
					if(requestMapping != null) {
						map.put("uri", requestMapping.value()[0] + ".do");
					}
					if(requestMapping.value()[0].indexOf("/sys/") == -1){
						programList.add(map);
					}
				}
			} catch (ClassNotFoundException e) {
				LOGGER.error(e.getMessage());
			}
			
		}
		return programList;
	}
	
	public static List<Map<String, Object>> getProgramInfoList(String programCode){
		
		if(StringUtil.isNull(programCode)) {
			return null;
		}
		
		//Return 할 리스트
		List<Map<String, Object>> programInfoList = new ArrayList<Map<String, Object>>();
		
		//패키지의 Controller 클래스를 스캔
		List<ClassMetadata> classMetaScans = classMetaScan("classpath*:kr/co/nanwe/**/*Controller.class", Program.class);
		for (ClassMetadata classMetadata : classMetaScans) {
			String className = classMetadata.getClassName();
			try {
				Class<?> c = Class.forName(className);
				Program program = c.getAnnotation(Program.class);
				if(program != null) {
					String code = program.code();
					if(programCode.equals(code)) {
						String prefixUri = "";
						RequestMapping requestMapping = c.getAnnotation(RequestMapping.class);
						if(requestMapping != null) {
							String[] prefixUriArr = requestMapping.value();
							if(prefixUriArr.length > 0){
								prefixUri = prefixUriArr[0];
							}
						}
						
						Method[] methods = c.getMethods();
						for (Method method : methods) {
							Map<String, Object> map = new HashMap<String, Object>();
							String uri = "";						
							RequestMapping requestMapping2 = method.getAnnotation(RequestMapping.class);
							if(requestMapping2 != null) {							
								String[] uriArr = requestMapping2.value();
								if(uriArr.length > 0){
									uri = prefixUri + uriArr[0];
								}
							}
							
							ProgramInfo progrmaInfo = method.getAnnotation(ProgramInfo.class);
							if(progrmaInfo != null) {
								map.put("code", progrmaInfo.code());
								map.put("name", progrmaInfo.name());
								map.put("uri", uri);
								programInfoList.add(map);
							}
						}
					}
				}
			} catch (ClassNotFoundException e) {
				LOGGER.error(e.getMessage());
			}
			
		}
		return programInfoList;
	}

	private static List<ClassMetadata> classMetaScan(String classAntPattern, Class<? extends Annotation> annotationType) {
		List<ClassMetadata> classeMetas = null;
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] rs;
		try {
			rs = resolver.getResources(classAntPattern);
			classeMetas = new ArrayList<ClassMetadata>();
			for (Resource r : rs) {
				MetadataReader mr = new SimpleMetadataReaderFactory().getMetadataReader(r);
				boolean hasAnnotation = mr.getAnnotationMetadata().hasAnnotation(annotationType.getName());
				if (hasAnnotation) {
					classeMetas.add(mr.getClassMetadata());
				}
			}
		} catch (IOException e) {
			LOGGER.debug(e.getMessage());
		}
		
		return classeMetas;
	}
}