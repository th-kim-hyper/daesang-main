package com.daesang.rpa.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.daesang.rpa.common.ErrorInfo;
import com.daesang.rpa.repository.mariadb.CommonMapper;
import com.daesang.rpa.repository.oracle.GroupwareMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saerom.onepass.client.OnepassClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommonService {

	@Value("${rpa.base.tenant}")
	private String BASE_TENANT;

	@Value("${rpa.proxy.url}")
	private String PROXY_URL;

	@Value("${rpa.proxy.token}")
	private String PROXY_TOKEN;

	@Value("${rpa.proxy.api.catalog}")
	private String PROXY_API_CATALOG;

	@Value("${rpa.proxy.api.user}")
	private String PROXY_API_USER;

	@Value("${rpa.proxy.api.group}")
	private String PROXY_API_GROUP;

	private final CommonMapper commonMapper;

	private final GroupwareMapper groupwareMapper;

	private final RestTemplate restTemplate;

	private final ObjectMapper objectMapper;

	private SimpleDateFormat simpleDateFormat;

	private HttpHeaders httpHeaders;

	private HttpEntity<Object> httpEntity;

	@PostConstruct
	private void postConstruct() {

		this.simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

		this.httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		// API ????????? ?????? ????????????
		httpHeaders.setBearerAuth(PROXY_TOKEN);

		this.httpEntity = new HttpEntity<Object>(httpHeaders);
	}

	public String getValidTenantId(String tenantId) {

		if (!StringUtils.hasText(tenantId) || "BASE".equals(tenantId.toUpperCase())) {

			return BASE_TENANT;

		} else {

			return tenantId;
		}
	}

	public List<Map<String, Object>> retrieveStatisticsDivisions() {

		return commonMapper.selectStatisticsDivisions();
	}

	public List<Map<String, Object>> retrieveStatisticsDivisionTeams(String divName) {

		return commonMapper.selectStatisticsDivisionTeams(divName);
	}

	public Map<String, Object> retrieveStatistics(Map<String, Object> paramMap) {

		String regDtFrom = (String) paramMap.get("regDtFrom");
		String regDtTo = (String) paramMap.get("regDtTo");

		paramMap.put("regDtFrom", StringUtils.hasText(regDtFrom) ? regDtFrom.replaceAll("-", "") : regDtFrom);
		paramMap.put("regDtTo", StringUtils.hasText(regDtTo) ? regDtTo.replaceAll("-", "") : regDtTo);

		Long workHour = 0L;
		Long targetcount = 0L;
		Long objectCount = 0L;

		// ????????????, ??????, chart1(?????? ????????????)
		List<Map<String, Object>> statisticsTimeSummaries = commonMapper.selectStatisticsTimeSummaries(paramMap);

		// chart2(?????? ????????????)
		List<Map<String, Object>> statisticsTargetCounts = commonMapper.selectStatisticsTargetCounts(paramMap);

		// chart3(?????? ????????????)
		List<Map<String, Object>> statisticsObjectCounts = commonMapper.selectStatisticsObjectCounts(paramMap);

		// chart4(????????????)
		List<Map<String, Object>> statisticsResults = commonMapper.selectStatisticsResults(paramMap);

		// chart5(????????? ????????????)
		List<Map<String, Object>> statisticsHourResults = commonMapper.selectStatisticsHourResults(paramMap);

		List<String> chart1Header = new ArrayList<String>();
		List<Long> chart1Data = new ArrayList<Long>();

		List<String> chart2Header = new ArrayList<String>();
		List<Long> chart2Data = new ArrayList<Long>();

		List<String> chart3Header = new ArrayList<String>();
		List<Long> chart3Data = new ArrayList<Long>();

		List<String> chart4Header = new ArrayList<String>();
		List<Long> chart4Data = new ArrayList<Long>();

		List<String> chart5header = Arrays.asList("??????", "??????", "??????", "????????????");
		List<List<Long>> chart5Data = new ArrayList<List<Long>>();

		// ????????????
		for (Map<String, Object> info : statisticsTimeSummaries) {

			workHour = workHour + Long.parseLong(String.valueOf(info.get("workHour")));
		}

		// ????????????
		for (Map<String, Object> info : statisticsTargetCounts) {

			targetcount = targetcount + (Long) info.get("targetCnt");
		}

		// ????????????
		for (Map<String, Object> info : statisticsObjectCounts) {

			objectCount = objectCount + (Long) info.get("objectCnt");
		}

		Map<String, Object> temp = null;

		int length = statisticsTimeSummaries.size();
		if (!"2".equals(paramMap.get("type")) && length > 5) {
			length = 5;
		}

		// chart1(?????? ????????????)
		for (int i = 0; i < length; i++) {

			temp = statisticsTimeSummaries.get(i);

			chart1Header.add((String) temp.get("teamName"));
			chart1Data.add(Long.parseLong(String.valueOf(temp.get("workHour")))); // ROUND ????????? ??? ?????? BigInteger ??????
		}

		length = statisticsTargetCounts.size();
		if (!"2".equals(paramMap.get("type")) && length > 5) {
			length = 5;
		}

		// chart2(?????? ????????????)
		for (int i = 0; i < length; i++) {

			temp = statisticsTargetCounts.get(i);

			chart2Header.add((String) temp.get("teamName"));
			chart2Data.add((Long) temp.get("targetCnt"));
		}

		length = statisticsObjectCounts.size();
		if (!"2".equals(paramMap.get("type")) && length > 5) {
			length = 5;
		}

		// chart3(?????? ????????????)
		for (int i = 0; i < length; i++) {

			temp = statisticsObjectCounts.get(i);

			chart3Header.add((String) temp.get("teamName"));
			chart3Data.add((Long) temp.get("objectCnt"));
		}

		// chart4(????????????)
		for (Map<String, Object> info : statisticsResults) {

			chart4Header.add((String) info.get("resultName"));
			chart4Data.add((Long) info.get("resultCnt"));
		}

		List<Long> result1List = new ArrayList<Long>();
		List<Long> result2List = new ArrayList<Long>();
		List<Long> result3List = new ArrayList<Long>();
		List<Long> result4List = new ArrayList<Long>();

		for (int i = 0; i < 24; i++) {
			result1List.add(0L);
			result2List.add(0L);
			result3List.add(0L);
			result4List.add(0L);
		}

		chart5Data.add(result1List);
		chart5Data.add(result2List);
		chart5Data.add(result3List);
		chart5Data.add(result4List);

		List<Long> tempList = null;

		for (Map<String, Object> statisticsHourResult : statisticsHourResults) {

			if (chart5header.get(0).equals(statisticsHourResult.get("resultName"))) {
				tempList = result1List;
			}
			if (chart5header.get(1).equals(statisticsHourResult.get("resultName"))) {
				tempList = result2List;
			}
			if (chart5header.get(2).equals(statisticsHourResult.get("resultName"))) {
				tempList = result3List;
			}
			if (chart5header.get(3).equals(statisticsHourResult.get("resultName"))) {
				tempList = result4List;
			}

			tempList.set(Integer.parseInt((String) statisticsHourResult.get("startHour")),
					(Long) statisticsHourResult.get("resultCnt"));
		}

		Map<String, Object> chart1 = new HashMap<String, Object>();
		chart1.put("labels", chart1Header);
		chart1.put("data", chart1Data);

		Map<String, Object> chart2 = new HashMap<String, Object>();
		chart2.put("labels", chart2Header);
		chart2.put("data", chart2Data);

		Map<String, Object> chart3 = new HashMap<String, Object>();
		chart3.put("labels", chart3Header);
		chart3.put("data", chart3Data);

		Map<String, Object> chart4 = new HashMap<String, Object>();
		chart4.put("labels", chart4Header);
		chart4.put("data", chart4Data);

		Map<String, Object> chart5 = new HashMap<String, Object>();
		chart5.put("labels", chart5header);
		chart5.put("data", chart5Data);

		Map<String, Object> summary = new HashMap<String, Object>();
		summary.put("workHour", workHour);
		summary.put("workAmount", workHour * 20000 / 1000000);
		summary.put("targetCount", targetcount);
		summary.put("objectCount", objectCount);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("summary", summary);
		resultMap.put("chart1", chart1);
		resultMap.put("chart2", chart2);
		resultMap.put("chart3", chart3);
		resultMap.put("chart4", chart4);
		resultMap.put("chart5", chart5);

		return resultMap;
	}

	public List<Map<String, Object>> retrieveTenants() {

		return commonMapper.selectTenants();
	}

	private Map<String, Object> retrieveTenant(String tenantId) {

		return commonMapper.selectTenant(tenantId);
	}

	public List<Map<String, Object>> retrieveTenantGroups(String tenantId) {

		String preName = (String) retrieveTenant(tenantId).get("preName");

		return commonMapper.selectTenantGroups(preName);
	}

	public List<Map<String, Object>> retrieveTenantWorkgroups(String tenantId) {

		String preName = (String) retrieveTenant(tenantId).get("preName");

		return commonMapper.selectTenantWorkgroups(preName);
	}

	public List<Map<String, Object>> retrieveMembers(Map<String, Object> paramMap) {

		paramMap.put("preName", "catalog");

		return commonMapper.selectMembers(paramMap);
	}

	public List<Map<String, Object>> retrieveTenantMembers(String tenantId, Map<String, Object> paramMap) {

		String userIds = (String) paramMap.get("userIds");
		if (StringUtils.hasText(userIds)) {
			paramMap.put("userIds", userIds.split(","));
		}

		String regDtFrom = (String) paramMap.get("regDtFrom");
		String regDtTo = (String) paramMap.get("regDtTo");

		paramMap.put("regDtFrom", StringUtils.hasText(regDtFrom) ? regDtFrom.replaceAll("-", "") : regDtFrom);
		paramMap.put("regDtTo", StringUtils.hasText(regDtTo) ? regDtTo.replaceAll("-", "") : regDtTo);

		paramMap.put("tenantId", tenantId);

		String preName = (String) retrieveTenant(tenantId).get("preName");
		paramMap.put("preName", preName);

		return commonMapper.selectTenantMembers(paramMap);
	}

	public Map<String, Object> retrieveTenantMember(String tenantId, String userId) {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", userId);

		paramMap.put("tenantId", tenantId);

		String preName = (String) retrieveTenant(tenantId).get("preName");
		paramMap.put("preName", preName);

		return commonMapper.selectTenantMember(paramMap);
	}

	@Deprecated
	public List<Map<String, Object>> retrieveTenantMemberGroups(String tenantId, String userId) {

		String preName = (String) retrieveTenant(tenantId).get("preName");

		return commonMapper.selectTenantMemberGroups(preName, userId);
	}

	public List<Map<String, Object>> retrieveHistoryLogins(String tenantId, Map<String, Object> paramMap) {

		String regDtFrom = (String) paramMap.get("regDtFrom");
		String regDtTo = (String) paramMap.get("regDtTo");

		paramMap.put("regDtFrom", StringUtils.hasText(regDtFrom) ? regDtFrom.replaceAll("-", "") : regDtFrom);
		paramMap.put("regDtTo", StringUtils.hasText(regDtTo) ? regDtTo.replaceAll("-", "") : regDtTo);

		String preName = (String) retrieveTenant(tenantId).get("preName");
		paramMap.put("preName", preName);

		return commonMapper.selectHistoryLogins(paramMap);
	}

	public List<Map<String, Object>> retrieveHistoryGroupChanges(String tenantId, Map<String, Object> paramMap) {

		String regDtFrom = (String) paramMap.get("regDtFrom");
		String regDtTo = (String) paramMap.get("regDtTo");

		paramMap.put("regDtFrom", StringUtils.hasText(regDtFrom) ? regDtFrom.replaceAll("-", "") : regDtFrom);
		paramMap.put("regDtTo", StringUtils.hasText(regDtTo) ? regDtTo.replaceAll("-", "") : regDtTo);

		String preName = (String) retrieveTenant(tenantId).get("preName");
		paramMap.put("preName", preName);

		return commonMapper.selectHistoryGroupChanges(paramMap);
	}

	public List<Map<String, Object>> retrieveTenantsProxy(HttpServletRequest request) {

		return retrieveTenants();
	}

	public List<Map<String, Object>> retrieveTenantMembersProxy(String tenantId, Map<String, Object> paramMap) {

		return retrieveTenantMembers(tenantId, paramMap);
	}

	public List<Map<String, Object>> retrieveTenantGroupsProxy(HttpServletRequest request, String tenantId) {

		return retrieveTenantGroups(tenantId);
	}

	public void export(HttpServletResponse response, List<Map<String, Object>> mapList, String fileName)
			throws IOException {

		int currentRow = 0;

		String sheetName = "Sheet1";
		if (StringUtils.hasText(fileName)) {
			sheetName = fileName;
		}

		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet(sheetName);

		if (mapList != null && mapList.size() > 0) {

			CellStyle csHeader = wb.createCellStyle();
			csHeader.setAlignment(HorizontalAlignment.CENTER);
			csHeader.setFillForegroundColor(IndexedColors.AQUA.getIndex());
			csHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			csHeader.setBorderTop(BorderStyle.THICK);
			csHeader.setBorderBottom(BorderStyle.THICK);
			csHeader.setBorderLeft(BorderStyle.THICK);
			csHeader.setBorderRight(BorderStyle.THICK);

			CellStyle csBody = wb.createCellStyle();
			csBody.setBorderTop(BorderStyle.THICK);
			csBody.setBorderBottom(BorderStyle.THICK);
			csBody.setBorderLeft(BorderStyle.THICK);
			csBody.setBorderRight(BorderStyle.THICK);

			List<String> keys = new ArrayList<String>(mapList.get(0).keySet());

			Cell cell = null;

			// Row Header
			Row row = sheet.createRow(currentRow++);
			for (int i = 0; i < keys.size(); i++) {

				cell = row.createCell(i);
				cell.setCellValue(keys.get(i));
				cell.setCellStyle(csHeader);
			}

			// Row Body
			for (Map<String, Object> map : mapList) {

				row = sheet.createRow(currentRow++);
				for (int i = 0; i < keys.size(); i++) {

					cell = row.createCell(i);
					cell.setCellValue((String) map.get(keys.get(i)));
					cell.setCellStyle(csBody);
				}
			}

			for (int i = 0; i < keys.size(); i++) {

				sheet.autoSizeColumn(i);
				sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 128);
			}
		}

		String exportName = "export_" + (fileName == null ? "" : fileName + "_") + simpleDateFormat.format(new Date())
				+ ".xlsx";

		// Response Header
		response.setContentType("ms-vnd/excel");
		response.setHeader("Content-Disposition",
				"attachment;filename=" + URLEncoder.encode(exportName, "UTF-8").replaceAll("\\+", "%20"));

		// Export
		wb.write(response.getOutputStream());
		wb.close();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, Object> processTenantGroupChangesProxy(HttpServletRequest request, String tenantId,
			Map<String, Object> paramMap) {

		// ?????? ???????????? ??????
		Map<String, Object> resultMap = null;

		// ???????????? ?????? ??????
		List<Map<String, Object>> groupChanges = (List<Map<String, Object>>) paramMap.get("groupChanges");
		if (groupChanges == null || groupChanges.size() <= 0) {

			resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", false);
			resultMap.put("resultDetail", "??????????????? ????????????.");

			return resultMap;
		}

		// ???????????? ???????????? ??????(????????????) ??????
		commonMapper.insertHistoryGroupChange(paramMap);

		String url = null;

		HttpMethod httpMethod = null;
		ResponseEntity<Map> responseEntity = null;

		List<Map<String, Object>> errInfos = new ArrayList<Map<String, Object>>();

		Map<String, Object> userInfo = null;
		String beforeGroupIds = null;

		// ?????? ?????? ????????? ????????? ???????????? ??????/?????? ?????? ??????
		for (Map<String, Object> groupChangeMap : groupChanges) {

			resultMap = new HashMap<String, Object>();
			resultMap.put("resultCode", false);

			// ?????? ????????? ???????????? ???????????? ?????? ??????
			userInfo = retrieveTenantMember(tenantId, (String) groupChangeMap.get("userId"));
			if (userInfo != null) {

				beforeGroupIds = (String) userInfo.get("groupIds");
			}

			if ((boolean) groupChangeMap.get("addYn")) {

				httpMethod = HttpMethod.POST;

			} else {

				httpMethod = HttpMethod.DELETE;
			}

			try {

				url = PROXY_URL + PROXY_API_GROUP + "/" + tenantId + "/" + groupChangeMap.get("groupId") + "/"
						+ groupChangeMap.get("userId");

				// API ??????
				responseEntity = restTemplate.exchange(url, httpMethod, httpEntity, Map.class);

				log.info(resultMap.toString());

				if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {

					errInfos.add(groupChangeMap);

					resultMap.put("resultDetail", "Proxy ?????? ??? ????????? ??????");

				} else {

					// ?????? ??????
					resultMap = responseEntity.getBody();

					if (resultMap == null || resultMap.get("resultCode") == null) {

						String message = "???????????? ?????? ?????? ??????";
						log.error(message);

						errInfos.add(groupChangeMap);

						resultMap = new HashMap<String, Object>();
						resultMap.put("resultCode", false);
						resultMap.put("resultDetail", message);
					}
				}

			} catch (ResourceAccessException | HttpClientErrorException | HttpServerErrorException e) {

				log.error(e.getMessage());

				errInfos.add(groupChangeMap);

				resultMap.put("resultDetail", "Proxy ?????? ??????");
			}

			groupChangeMap.put("apvNo", paramMap.get("apvNo"));
			groupChangeMap.put("reqId", UUID.randomUUID().toString()); // ????????? ????????? ??? ?????? ??? ?????? ??????
			groupChangeMap.put("successYn", resultMap.get("resultCode"));
			groupChangeMap.put("beforeStatus", beforeGroupIds);

			// ???????????? ???????????? ????????????(???????????? ???????????? ??????/??????) ??????
			commonMapper.insertHistoryGroupChangeDetail(groupChangeMap);

			groupChangeMap.put("reqIp", getRemoteAddr(request));
			groupChangeMap.put("reqUrl", url);
			groupChangeMap.put("reqMethod", httpMethod.name());

			try {

				groupChangeMap.put("reqJson", objectMapper.writeValueAsString(groupChangeMap));

			} catch (JsonProcessingException e) {

				log.error("?????? ???????????? ?????? ??????");
			}

			try {

				groupChangeMap.put("resJson", objectMapper.writeValueAsString(resultMap));

			} catch (JsonProcessingException e) {

				log.error("?????? ???????????? ?????? ??????");
			}

			// API ?????? ?????? ??????
			commonMapper.insertHistoryApi(groupChangeMap);
		}

		resultMap = new HashMap<String, Object>();

		if (errInfos.size() > 0) {

			resultMap.put("resultCode", false);
			resultMap.put("resultDetail", groupChanges.size() + "??? ??? " + errInfos.size() + "?????? ????????? ??????????????????.");
			resultMap.put("errorInfos", errInfos);

		} else {

			resultMap.put("resultCode", true);
			resultMap.put("resultDetail", groupChanges.size() + "?????? ????????? ?????????????????????.");
		}

		return resultMap;
	}

	public void processSyncMembers(String tenantId, String baseDate) {

		Map<String, Object> paramMap = null;

		String url = PROXY_URL + PROXY_API_USER;

		// ????????? ????????? ??????, ??????????????? ?????? ?????? ??????
		if (baseDate == null) {
			baseDate = simpleDateFormat.format(new Date()).substring(0, 8);
		}

		// ???????????? ????????? ??????
		List<Map<String, Object>> groupwareMembers = groupwareMapper.selectMembers(baseDate);

		// RPA ????????? ??????
		List<String> memberIds = retrieveTenantMembers(tenantId, new HashMap<String, Object>()).stream()
				.map(e -> (String) e.get("userId")).collect(Collectors.toList());

		log.info("*** RPA User Info - Count :" + memberIds.size());
		log.debug(" - Ids :" + memberIds.toString());

		// ???????????? ????????? ID ????????? ??????
		List<String> delIds = groupwareMembers.stream().map(e -> (String) e.get("shortname"))
				.collect(Collectors.toList());

		log.info("*** Groupware User Info - Count :" + delIds.size());
		log.debug(" - Ids :" + delIds.toString());

		// RPA ?????? ????????? ID ????????? ??????
		List<String> requiredId = commonMapper.selectMemberRequired(tenantId).stream()
				.map(e -> (String) e.get("userId")).collect(Collectors.toList());

		log.info("*** Required User Info - Count :" + requiredId.size());
		log.warn(" - Ids :" + requiredId.toString());

		// ???????????? ????????? ??????????????? ?????? ?????? ???????????? ??????
		delIds.addAll(requiredId);

		log.info("*** Active User Info - Count :" + delIds.size());
		log.debug(" - Ids :" + delIds.toString());

		// RPA ?????? ??? ??????????????? ???????????? ?????? ????????? ??????
		memberIds.removeAll(delIds);

		log.warn("*** Delete User Info - Count :" + memberIds.size());
		log.warn(" - Ids :" + memberIds.toString());

		// ?????? ??????
		for (String memberId : memberIds) {

			paramMap = new HashMap<String, Object>();
			paramMap.put("tenantId", tenantId);
			paramMap.put("userId", memberId);
			paramMap.put("syncType", "D");

			syncMember(url + "/" + memberId + "/" + tenantId, HttpMethod.DELETE, paramMap);
		}

		// ?????? ??? ?????? ??????
		for (Map<String, Object> member : groupwareMembers) {

			if (!"X".equals(member.get("syncType"))) {

				paramMap = new HashMap<String, Object>();
				paramMap.put("tenantId", tenantId);
				paramMap.put("userId", member.get("shortname"));
				paramMap.put("userNm", member.get("name"));
				paramMap.put("email", member.get("email"));
				paramMap.put("cmpnyNm", member.get("companyname") + "[" + member.get("companycode") + "]");
				paramMap.put("deptNm", member.get("deptname") + "[" + member.get("deptcode") + "]");
				paramMap.put("syncType", member.get("syncType"));

				syncMember(url, HttpMethod.POST, paramMap);
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void syncMember(String url, HttpMethod httpMethod, Map<String, Object> paramMap) {

		// ?????? ???????????? ??????
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", false);

		String reqJson = null;

		ResponseEntity<Map> responseEntity = null;

		try {

			reqJson = objectMapper.writeValueAsString(paramMap);

			// API ??????
			if (httpMethod == HttpMethod.POST) {

				responseEntity = restTemplate.exchange(url, httpMethod, new HttpEntity(reqJson, httpHeaders),
						Map.class);

			} else {

				responseEntity = restTemplate.exchange(url, httpMethod, httpEntity, Map.class);
			}

			log.info(resultMap.toString());

			if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {

				resultMap.put("resultDetail", "Proxy ?????? ??? ????????? ??????");

			} else {

				// ?????? ??????
				resultMap = responseEntity.getBody();
			}

		} catch (ResourceAccessException | HttpClientErrorException | HttpServerErrorException e) {

			log.error(e.getMessage());

			resultMap.put("resultDetail", "Proxy ?????? ??????");

		} catch (JsonProcessingException e) {

			resultMap.put("resultDetail", "?????? ???????????? ?????? ??????");
		}

		Map<String, Object> historyMap = new HashMap<String, Object>();
		historyMap.put("reqId", UUID.randomUUID().toString()); // ????????? ????????? ??? ?????? ??? ?????? ??????
		historyMap.put("reqIp", "localhost");
		historyMap.put("reqUrl", url);
		historyMap.put("reqMethod", httpMethod.name());
		historyMap.put("reqJson", reqJson);

		try {

			historyMap.put("resJson", objectMapper.writeValueAsString(resultMap));

		} catch (JsonProcessingException e) {

			log.error("?????? ???????????? ?????? ??????");
		}

		// API ?????? ?????? ??????
		commonMapper.insertHistoryApi(historyMap);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getUserInfo(HttpServletRequest request) {

		Map<String, Object> userInfo = null;

		HttpSession session = request.getSession(false);
		if (session != null) {

			userInfo = (Map<String, Object>) session.getAttribute("userInfo");
		}

		return userInfo;
	}

	public void authentication(HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = null;
		ErrorInfo errorInfo = null;
		String userId = null;

		try {

			// ???????????? ??????
			OnepassClient onepassClinet = new OnepassClient(request, response);
			if (onepassClinet.ssoValidate()) {

				// ???????????? ?????????ID ??????
				session = request.getSession(false);
				if (session != null) {

					log.warn("Ticket Info. **************************************************");

					// ??????????????? [IMUSERID] ????????? ??????(SSO KEY, ???????????? + ?????? 6????????? ??????)
					// ??????????????? [SHORTNAME]?????? ?????????(2022.07.04)
					log.warn("onepassid - SSO ?????????	: " + session.getAttribute("onepassid"));

					// ??????????????? [ORGNUMBER] ????????? ??????
					log.warn("empnumber - ??????		: " + session.getAttribute("empnumber"));

					// ??????????????? [COMPANYCODE] ????????? ??????
					log.warn("userinfo1 - ????????????	: " + session.getAttribute("userinfo1"));

					userId = (String) session.getAttribute("onepassid");
				}

				if (!StringUtils.hasText(userId)) {

					errorInfo = ErrorInfo.NO_ONPASSID_INFORMATION;
				}

			} else {

				errorInfo = ErrorInfo.UNAUTHENTICATED_USER;
			}

		} catch (Exception e) {

			errorInfo = ErrorInfo.AUTHENTICATION_FAILED;
		}

		if (errorInfo != null) {

			log.warn(errorInfo.message());
			return;
		}

		// ?????????????????? RPA DB?????? ??????
		Map<String, Object> userInfo = retrieveTenantMember(getValidTenantId(request.getParameter("tenantId")), userId);
		if (userInfo == null || userInfo.isEmpty()) {

			log.warn(ErrorInfo.NO_USER_INFORMATION.message());
			return;
		}

		// ?????????????????? ????????? ??????(????????? ??????)
		session.setAttribute("userInfo", userInfo);
	}

	public String getRemoteAddr(HttpServletRequest request) {

		String ip = request.getHeader("X-Forwarded-For");

		if (ip == null) {
			ip = request.getHeader("Proxy-Client-IP");
		}

		if (ip == null) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}

		if (ip == null) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}

		if (ip == null) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}

		if (ip == null) {
			ip = request.getRemoteAddr();
		}

		return ip;
	}
}