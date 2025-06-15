package kr.co.nanwe.bbs.service;

import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import kr.co.nanwe.bbs.web.EmpNoticeController;
import kr.co.nanwe.cmmn.config.WebConfig;
import kr.co.nanwe.cmmn.service.Paging;
import kr.co.nanwe.cmmn.util.HttpURLConnectionUtil;
import kr.co.nanwe.cmmn.util.PagingUtil;
import kr.co.nanwe.cmmn.util.StringUtil;

/**
 * @Class Name 		: JobApiService
 * @Description 	: 워크넷, 잡코리아, 잡알리오, 사람인 관련 클래스
 * @Modification Information
 * @
 * @ 수정일			수정자			수정내용
 * @ --------------------------------------------------------------------------
 * @ 2023.10.30		신한나			최초생성
 */

@Service
public class JobApiService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JobApiService.class);
	
	/** Web Config*/
	@Resource(name = "web")
	private WebConfig web;
	
	/** worknet */
	public Map<String, Object> worknet(BbsMgtVO bbsMgtVO, EmpSearchVO search, String category){
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		if(StringUtil.isNull(search.getCp())) {
			search.setCp("1");
		}

		int pageNum = Integer.valueOf(search.getCp());
		if(pageNum < 1) {
			pageNum = 1;
		} else if(pageNum > 1000) {
			pageNum = 1000;
		}
		//시작페이지
		search.setStartPage(pageNum);
		
		int perPage = bbsMgtVO.getRowCnt();
		if(perPage < 1) {
			perPage = 100;
		} else if(perPage > 100) {
			perPage = 100;
		}
        //한페이지 당 보여줄 갯수
		search.setDisplay(perPage);
		
		int rTotal = 0;
		int rStartPage = 0;
		int rDisplay = 0;
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();	
		
		StringReader sr = null;
		InputSource is = null;
		HttpURLConnection conn = null;
		try {
			//채용공고 api
			String apiUrl = web.getApiProp("worknet.apiUrl");
						
			String url = apiUrl + "/opi/opi/opia/wantedApi.do";
			 
			//인증키
			String authKey = web.getApiProp("worknet.authKey"); 
			int startPage = search.getStartPage();			
			int display = search.getDisplay();
			url += "?authKey=" + authKey;
			url += "&callTp=L";
			url += "&returnType=XML";
			url += "&startPage=" + startPage;
			url += "&display=" + display;
			
			
			/* 선택 항목 */
			
			//근무지역코드  (다중검색 가능)
			String region = search.getRegion();
			if(region != null && !"".equals(region)) {
				url += "&region=" + region.replaceAll("\\,", "\\|");			
			}
			
			//직종코드 (다중검색 가능) * 다수의 직종코드를 한번에 입력할 경우 API 이용에 제한이 있을 수 있습니다.(예: 100개 이상)
			String occupation = search.getOccupation();
			if(occupation != null && !"".equals(occupation)) {
				url += "&occupation=" + occupation;	
			}
			
			//임금형태  * 검색조건 미입력시 관계없음으로 조회됩니다.
			String salTp = search.getSalTp();
			int minPay = search.getMinPay();
			int maxPay = search.getMaxPay();
			if(salTp != null && !"".equals(salTp) && maxPay != 0) {
				url += "&salTp=" + salTp;	//임금형태 (D 일급, H 시급, M 월급, Y 연봉)
				url += "&minPay=" + minPay;	//최소급여 (~ 만원(원)이상 : 연봉,월급(일급,시급) 검색값 입력 시 천단위구분을 위한 콤마(,)는 입력하지 않습니다.) *임금형태 입력시 필수입력	
				url += "&maxPay=" + maxPay;	//최대급여 (~ 만원(원)이상 : 연봉,월급(일급,시급) 검색값 입력 시 천단위구분을 위한 콤마(,)는 입력하지 않습니다.) *임금형태 입력시 필수입력	
			}
			
			//학력코드
			String education = search.getEducation();
			if(education != null && !"".equals(education)) {
				url += "&education=" + education;
			}
			
			//경력코드
			String career = search.getCareer();
			int minCareerM = search.getMinCareerM();
			int maxCareerM = search.getMaxCareerM();
			if(career != null && !"".equals(career)) {
				url += "&career=" + career;
				url += "&minCareerM=" + minCareerM;	//경력 최소개월 수 - *경력코드 입력시 필수입력	
				url += "&maxCareerM=" + maxCareerM;	//경력 최대개월 수 - *경력코드 입력시 필수입력	
			}
			
			//우대조건 (다중검색 가능)
			String pref = search.getPref();
			if(pref != null && !"".equals(pref)) {
				url += "&pref=" + pref;
			}
			
			//역세권코드 (다중검색 가능) * 지역-호선-역 코드 의 형식으로 입력하십시오.
			String subway = search.getSubway();
			if(subway != null && !"".equals(subway)) {
				url += "&subway=" + subway;
			}
			
			//고용형태 (다중검색 가능)
			String empTp = search.getEmpTp();
			String termContractMmcnt = search.getTermContractMmcnt();
			if(empTp != null && !"".equals(empTp)) {
				url += "&empTp=" + empTp;				
				//근무기간 (다중검색 가능) - * 고용형태가 시간선택제(11,21)인 경우에만 적용됨.
				if(("11".equals(empTp) || "21".equals(empTp)) && termContractMmcnt != null && !"".equals(termContractMmcnt)) {
					url += "&termContractMmcnt=" + termContractMmcnt;
				}
			}
			
			//근무형태 (다중검색 가능)
			String holidayTp = search.getHolidayTp();
			if(holidayTp != null && !"".equals(holidayTp)) {
				url += "&holidayTp=" + holidayTp;		 	
			}
			
			//기업형태 택일	(다중검색 가능)
			String coTp = search.getCoTp();
			if(coTp != null && !"".equals(coTp)) {
				url += "&coTp=" + coTp;			
			}
			
			//사업자등록번호
			String busino = search.getBusino();
			if(busino != null && !"".equals(busino)) {
				url += "&busino=" + busino;
			}
			
			//강소기업 여부 (Y)
			String dtlSmlgntYn = search.getDtlSmlgntYn();
			if(dtlSmlgntYn != null && "Y".equals(dtlSmlgntYn)) {
				url += "&dtlSmlgntYn=" + dtlSmlgntYn;
			}
			
			//일학습병행기업 여부(Y)
			String workStudyJoinYn = search.getWorkStudyJoinYn();
			if(workStudyJoinYn != null && "Y".equals(workStudyJoinYn)) {
				url += "&workStudyJoinYn=" + workStudyJoinYn;
			}
			
			//강소기업 분류코드
			String smlgntCoClcd = search.getSmlgntCoClcd();
			if(smlgntCoClcd != null && !"".equals(smlgntCoClcd)) {
				url += "&smlgntCoClcd=" + smlgntCoClcd;
			}
			
			//사원수 (다중검색 가능)
			String workerCnt = search.getWorkerCnt();
			if(workerCnt != null && !"".equals(workerCnt)) {
				url += "&workerCnt=" + workerCnt;			
			}
			
			//근무편의 (다중검색 가능)
			String welfare = search.getWelfare();
			if(welfare != null && !"".equals(welfare)) {
				url += "&welfare=" + welfare;		 	
			}
			
			//자격면허코드 (다중검색 가능)
			String certLic = search.getCertLic();
			if(certLic != null && !"".equals(certLic)) {
				url += "&certLic=" + certLic;
			}
			
			//등록일
			String regDate = search.getRegDate();
			if(regDate != null && !"".equals(regDate)) {
				url += "&regDate=" + regDate;
			}
			
			//키워드검색 (다중검색 가능) * UTF-8 인코딩입니다.
			String keyword = search.getKeyword();
			if(keyword != null && !"".equals(keyword)) {
				url += "&keyword=" + URLEncoder.encode(keyword, "UTF-8");		 	
			}
			
			//채용시까지 구인여부 : Y/N
			String untilEmpWantedYn = search.getUntilEmpWantedYn();
			if(untilEmpWantedYn != null && !"".equals(untilEmpWantedYn)) {
				url += "&untilEmpWantedYn=" + untilEmpWantedYn;		 	
			}
			
			//최소 구인인증일자
			String minWantedAuthDt = search.getMinWantedAuthDt();
			if(minWantedAuthDt != null && !"".equals(minWantedAuthDt)) {
				url += "&minWantedAuthDt=" + minWantedAuthDt;			
			}
			
			//최대 구인인증일자
			String maxWantedAuthDt = search.getMaxWantedAuthDt();
			if(maxWantedAuthDt != null && !"".equals(maxWantedAuthDt)) {
				url += "&maxWantedAuthDt=" + maxWantedAuthDt;			
			}
			
			//채용구분 - * 해당 검색조건 미입력 시 자동으로 상용직 검색
			String empTpGb = search.getEmpTpGb();
			if(empTpGb != null && !"".equals(empTpGb)) {	
				url += "&empTpGb=" + empTpGb;	
			}
			
			//등록일 기준 정렬방식 - * 해당 검색조건 미입력 시 자동으로 등록일 상향정렬
			String sortOrderBy = search.getSortOrderBy();
			if(sortOrderBy != null && !"".equals(sortOrderBy)) {
				url += "&sortOrderBy=" + sortOrderBy;
			}
			
			//전공코드(다중검색 가능) - 3차 계열만 입력하세요
			String major = search.getMajor();
			if(major != null && !"".equals(major)) {
				url += "&major=" + major;			
			}
			
			//외국어코드(다중검색 가능)
			String foreignLanguage = search.getForeignLanguage();
			if(foreignLanguage != null && !"".equals(foreignLanguage)) {
				url += "&foreignLanguage=" + foreignLanguage;			
			}
			
			//기타 우대조건(컴퓨터 활용) (다중검색 가능)
			String comPreferential = search.getComPreferential();
			if(comPreferential != null && !"".equals(comPreferential)) {
				url += "&comPreferential=" + comPreferential;			
			}
			
			//기타 우대조건 (일반) (다중검색 가능)
			String pfPreferential = search.getPfPreferential();
			if(pfPreferential != null && !"".equals(pfPreferential)) {
				url += "&pfPreferential=" + pfPreferential;			
			}
			
			//근무시간 (다중검색 가능)
			String workHrCd = search.getWorkHrCd();
			if(workHrCd != null && !"".equals(workHrCd)) {
				url += "&workHrCd=" + workHrCd;
			}
			
			LOGGER.debug("============ main url >>>>>>>>>>>>>>>> " + url);

			conn = HttpURLConnectionUtil.openHttpsUrlConnection(url);
			conn.setRequestMethod("GET");
			
			if(conn.getResponseCode() == HttpStatus.OK.value()) {
				String responseStr = HttpURLConnectionUtil.getResponseString(conn);
								
				
				//XML 파싱
				sr = new StringReader(responseStr);
				is = new InputSource(sr);			
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(is);
				Element root = document.getDocumentElement();
				
				//총건수, 페이징번호, 출력건수
				rTotal = Integer.parseInt(root.getElementsByTagName("total").item(0).getTextContent());
				rStartPage = Integer.parseInt(root.getElementsByTagName("startPage").item(0).getTextContent());
				rDisplay = Integer.parseInt(root.getElementsByTagName("display").item(0).getTextContent());
				
				//채용공고 목록
				NodeList wantedList = root.getElementsByTagName("wanted");
				
				if (wantedList.getLength() > 0) {
					for (int i = 0; i < wantedList.getLength(); i++) {					
						NodeList nodeList = wantedList.item(i).getChildNodes();
						//System.out.println("nodeList : "+ nodeList);
						
						if(nodeList.getLength() > 0) {
							Map<String, Object> wantedMap = new HashMap<String, Object>();
							for (int j = 0; j < nodeList.getLength(); j++) {
								
								//System.out.println("nodeList.item(j).getNodeName() : "+
								//nodeList.item(j).getNodeName());
								//System.out.println("nodeList.item(j).getTextContent() : "+
								//nodeList.item(j).getTextContent());
								
								
								wantedMap.put(nodeList.item(j).getNodeName(), nodeList.item(j).getTextContent());
							}
							list.add(wantedMap);
						}
					}
				}
			}
		} catch(Exception e) {
			LOGGER.error(e.getLocalizedMessage());
		} finally {
			if (sr != null) {
				sr.close();
			}
			HttpURLConnectionUtil.disconnect(conn);
		}
		
		//페이징 처리 ("현재페이지" , 페이지당 로우개수, 페이징 개수, 전체 ROW COUNT)
		Paging paging = PagingUtil.getPaging(search.getCp(), bbsMgtVO.getRowCnt(), bbsMgtVO.getPageCnt(), rTotal);
		
		//총건수 조절 (워크넷 페이징은 1000건이 last이므로)
		if(rTotal > (perPage * 1000)) {
			//endPage.set(0, "lastPage", 1000);
			paging.setLastPage(1000);
			rTotal = (perPage * 1000);
			//dsPaging.set(0, "totalCount", rTotal);
			paging.setTotalCount(rTotal);
			//int endPage = dsPaging.getInt(0, "endPage");
			int endPage = paging.getEndPage();
			if(endPage >= 1000) {
				paging.setEndPage(1000);
			}
		}
		
		//LOGGER.debug("paging" + paging);
		//LOGGER.debug("============ LIST >>>>>>>>>>>>>>>> " + list);
		result.put("paging", paging);
		result.put("search", search);
		result.put("list", list);
		
		return result;
	}
	
	/** worknet - main */
	public List<Map<String, Object>> mainWorknet(Map<String, Object> param){
		
		//SEARCH
		EmpSearchVO search = new EmpSearchVO();
		
		int pageNum = 1;
		//시작페이지
		search.setStartPage(pageNum);
		
		int perPage = (int) param.get("pagePerRow");
		if(perPage < 1) {
			perPage = 10;
		} else if(perPage > 100) {
			perPage = 100;
		}
        //한페이지 당 보여줄 갯수
		search.setDisplay(perPage);
		
		int rTotal = 0;
		int rStartPage = 0;
		int rDisplay = 0;
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();	
		
		StringReader sr = null;
		InputSource is = null;
		HttpURLConnection conn = null;
		try {
			//채용공고 api
			String apiUrl = web.getApiProp("worknet.apiUrl");
						
			String url = apiUrl + "/opi/opi/opia/wantedApi.do";
			 
			//인증키
			String authKey = web.getApiProp("worknet.authKey"); 
			int startPage = search.getStartPage();			
			int display = search.getDisplay();
			url += "?authKey=" + authKey;
			url += "&callTp=L";
			url += "&returnType=XML";
			url += "&startPage=" + startPage;
			url += "&display=" + display;
			
			
			/* 선택 항목 */
			
			//근무지역코드  (다중검색 가능)
			//String region = search.getRegion();
			//if(region != null && !"".equals(region)) {
			//	url += "&region=" + region.replaceAll("\\,", "\\|");			
			//}
			
			////직종코드 (다중검색 가능) * 다수의 직종코드를 한번에 입력할 경우 API 이용에 제한이 있을 수 있습니다.(예: 100개 이상)
			//String occupation = search.getOccupation();
			//if(occupation != null && !"".equals(occupation)) {
			//	url += "&occupation=" + occupation;	
			//}
			
			////임금형태  * 검색조건 미입력시 관계없음으로 조회됩니다.
			//String salTp = search.getSalTp();
			//int minPay = search.getMinPay();
			//int maxPay = search.getMaxPay();
			//if(salTp != null && !"".equals(salTp) && maxPay != 0) {
			//	url += "&salTp=" + salTp;	//임금형태 (D 일급, H 시급, M 월급, Y 연봉)
			//	url += "&minPay=" + minPay;	//최소급여 (~ 만원(원)이상 : 연봉,월급(일급,시급) 검색값 입력 시 천단위구분을 위한 콤마(,)는 입력하지 않습니다.) *임금형태 입력시 필수입력	
			//	url += "&maxPay=" + maxPay;	//최대급여 (~ 만원(원)이상 : 연봉,월급(일급,시급) 검색값 입력 시 천단위구분을 위한 콤마(,)는 입력하지 않습니다.) *임금형태 입력시 필수입력	
			//}
			
			////학력코드
			//String education = search.getEducation();
			//if(education != null && !"".equals(education)) {
			//	url += "&education=" + education;
			//}
			
			////경력코드
			//String career = search.getCareer();
			//int minCareerM = search.getMinCareerM();
			//int maxCareerM = search.getMaxCareerM();
			//if(career != null && !"".equals(career)) {
			//	url += "&career=" + career;
			//	url += "&minCareerM=" + minCareerM;	//경력 최소개월 수 - *경력코드 입력시 필수입력	
			//	url += "&maxCareerM=" + maxCareerM;	//경력 최대개월 수 - *경력코드 입력시 필수입력	
			//}
			
			////우대조건 (다중검색 가능)
			//String pref = search.getPref();
			//if(pref != null && !"".equals(pref)) {
			//	url += "&pref=" + pref;
			//}
			
			////역세권코드 (다중검색 가능) * 지역-호선-역 코드 의 형식으로 입력하십시오.
			//String subway = search.getSubway();
			//if(subway != null && !"".equals(subway)) {
			//	url += "&subway=" + subway;
			//}
			
			////고용형태 (다중검색 가능)
			//String empTp = search.getEmpTp();
			//String termContractMmcnt = search.getTermContractMmcnt();
			//if(empTp != null && !"".equals(empTp)) {
			//	url += "&empTp=" + empTp;				
			//	//근무기간 (다중검색 가능) - * 고용형태가 시간선택제(11,21)인 경우에만 적용됨.
			//	if(("11".equals(empTp) || "21".equals(empTp)) && termContractMmcnt != null && !"".equals(termContractMmcnt)) {
			//		url += "&termContractMmcnt=" + termContractMmcnt;
			//	}
			//}
			
			////근무형태 (다중검색 가능)
			//String holidayTp = search.getHolidayTp();
			//if(holidayTp != null && !"".equals(holidayTp)) {
			//	url += "&holidayTp=" + holidayTp;		 	
			//}
			
			////기업형태 택일	(다중검색 가능)
			//String coTp = search.getCoTp();
			//if(coTp != null && !"".equals(coTp)) {
			//	url += "&coTp=" + coTp;			
			//}
			
			////사업자등록번호
			//String busino = search.getBusino();
			//if(busino != null && !"".equals(busino)) {
			//	url += "&busino=" + busino;
			//}
			
			////강소기업 여부 (Y)
			//String dtlSmlgntYn = search.getDtlSmlgntYn();
			//if(dtlSmlgntYn != null && "Y".equals(dtlSmlgntYn)) {
			//	url += "&dtlSmlgntYn=" + dtlSmlgntYn;
			//}
			
			////일학습병행기업 여부(Y)
			//String workStudyJoinYn = search.getWorkStudyJoinYn();
			//if(workStudyJoinYn != null && "Y".equals(workStudyJoinYn)) {
			//	url += "&workStudyJoinYn=" + workStudyJoinYn;
			//}
			
			////강소기업 분류코드
			//String smlgntCoClcd = search.getSmlgntCoClcd();
			//if(smlgntCoClcd != null && !"".equals(smlgntCoClcd)) {
			//	url += "&smlgntCoClcd=" + smlgntCoClcd;
			//}
			
			////사원수 (다중검색 가능)
			//String workerCnt = search.getWorkerCnt();
			//if(workerCnt != null && !"".equals(workerCnt)) {
			//	url += "&workerCnt=" + workerCnt;			
			//}
			
			////근무편의 (다중검색 가능)
			//String welfare = search.getWelfare();
			//if(welfare != null && !"".equals(welfare)) {
			//	url += "&welfare=" + welfare;		 	
			//}
			
			////자격면허코드 (다중검색 가능)
			//String certLic = search.getCertLic();
			//if(certLic != null && !"".equals(certLic)) {
			//	url += "&certLic=" + certLic;
			//}
			
			////등록일
			//String regDate = search.getRegDate();
			//if(regDate != null && !"".equals(regDate)) {
			//	url += "&regDate=" + regDate;
			//}
			
			////키워드검색 (다중검색 가능) * UTF-8 인코딩입니다.
			//String keyword = search.getKeyword();
			//if(keyword != null && !"".equals(keyword)) {
			//	url += "&keyword=" + URLEncoder.encode(keyword, "UTF-8");		 	
			//}
			
			////채용시까지 구인여부 : Y/N
			//String untilEmpWantedYn = search.getUntilEmpWantedYn();
			//if(untilEmpWantedYn != null && !"".equals(untilEmpWantedYn)) {
			//	url += "&untilEmpWantedYn=" + untilEmpWantedYn;		 	
			//}
			
			////최소 구인인증일자
			//String minWantedAuthDt = search.getMinWantedAuthDt();
			//if(minWantedAuthDt != null && !"".equals(minWantedAuthDt)) {
			//	url += "&minWantedAuthDt=" + minWantedAuthDt;			
			//}
			
			////최대 구인인증일자
			//String maxWantedAuthDt = search.getMaxWantedAuthDt();
			//if(maxWantedAuthDt != null && !"".equals(maxWantedAuthDt)) {
			//	url += "&maxWantedAuthDt=" + maxWantedAuthDt;			
			//}
			
			////채용구분 - * 해당 검색조건 미입력 시 자동으로 상용직 검색
			//String empTpGb = search.getEmpTpGb();
			//if(empTpGb != null && !"".equals(empTpGb)) {	
			//	url += "&empTpGb=" + empTpGb;	
			//}
			
			////등록일 기준 정렬방식 - * 해당 검색조건 미입력 시 자동으로 등록일 상향정렬
			//String sortOrderBy = search.getSortOrderBy();
			//if(sortOrderBy != null && !"".equals(sortOrderBy)) {
			//	url += "&sortOrderBy=" + sortOrderBy;
			//}
			
			////전공코드(다중검색 가능) - 3차 계열만 입력하세요
			//String major = search.getMajor();
			//if(major != null && !"".equals(major)) {
			//	url += "&major=" + major;			
			//}
			
			////외국어코드(다중검색 가능)
			//String foreignLanguage = search.getForeignLanguage();
			//if(foreignLanguage != null && !"".equals(foreignLanguage)) {
			//	url += "&foreignLanguage=" + foreignLanguage;			
			//}
			
			////기타 우대조건(컴퓨터 활용) (다중검색 가능)
			//String comPreferential = search.getComPreferential();
			//if(comPreferential != null && !"".equals(comPreferential)) {
			//	url += "&comPreferential=" + comPreferential;			
			//}
			
			////기타 우대조건 (일반) (다중검색 가능)
			//String pfPreferential = search.getPfPreferential();
			//if(pfPreferential != null && !"".equals(pfPreferential)) {
			//	url += "&pfPreferential=" + pfPreferential;			
			//}
			
			////근무시간 (다중검색 가능)
			//String workHrCd = search.getWorkHrCd();
			//if(workHrCd != null && !"".equals(workHrCd)) {
			//	url += "&workHrCd=" + workHrCd;
			//}
			
			LOGGER.debug("============ url >>>>>>>>>>>>>>>> " + url);

			conn = HttpURLConnectionUtil.openHttpsUrlConnection(url);
			conn.setRequestMethod("GET");
			
			if(conn.getResponseCode() == HttpStatus.OK.value()) {
				String responseStr = HttpURLConnectionUtil.getResponseString(conn);
								
				
				//XML 파싱
				sr = new StringReader(responseStr);
				is = new InputSource(sr);			
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(is);
				Element root = document.getDocumentElement();
				
				//총건수, 페이징번호, 출력건수
				rTotal = Integer.parseInt(root.getElementsByTagName("total").item(0).getTextContent());
				rStartPage = Integer.parseInt(root.getElementsByTagName("startPage").item(0).getTextContent());
				rDisplay = Integer.parseInt(root.getElementsByTagName("display").item(0).getTextContent());
				
				//채용공고 목록
				NodeList wantedList = root.getElementsByTagName("wanted");
				
				if (wantedList.getLength() > 0) {
					for (int i = 0; i < wantedList.getLength(); i++) {					
						NodeList nodeList = wantedList.item(i).getChildNodes();
						//System.out.println("nodeList : "+ nodeList);
						
						if(nodeList.getLength() > 0) {
							Map<String, Object> wantedMap = new HashMap<String, Object>();
							for (int j = 0; j < nodeList.getLength(); j++) {
								
								//System.out.println("nodeList.item(j).getNodeName() : "+
								//nodeList.item(j).getNodeName());
								//System.out.println("nodeList.item(j).getTextContent() : "+
								//nodeList.item(j).getTextContent());
								
								
								wantedMap.put(nodeList.item(j).getNodeName(), nodeList.item(j).getTextContent());
							}
							list.add(wantedMap);
						}
					}
				}
			}
		} catch(Exception e) {
			LOGGER.error(e.getLocalizedMessage());
		} finally {
			if (sr != null) {
				sr.close();
			}
			HttpURLConnectionUtil.disconnect(conn);
		}

		return list;
	}
}
