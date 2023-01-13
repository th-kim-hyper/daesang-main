package com.daesang.rpa.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.daesang.rpa.service.CommonService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class ApiController {

	final private CommonService commonService;

	@GetMapping("/tenants")
	public List<Map<String, Object>> tenants() {

		return commonService.retrieveTenants();
	}

	@GetMapping("/statistics")
	public Map<String, Object> statistics(@RequestParam Map<String, Object> paramMap) {

		return commonService.retrieveStatistics(paramMap);
	}

	@GetMapping("/statistics/{divName}/teams")
	public List<Map<String, Object>> statisticsDivisionTeams(@PathVariable("divName") String divName) {

		return commonService.retrieveStatisticsDivisionTeams(divName);
	}

	@GetMapping("{tenantId}/groups")
	public List<Map<String, Object>> tenantGroups(@PathVariable("tenantId") String tenantId) {

		return commonService.retrieveTenantGroups(commonService.getValidTenantId(tenantId));
	}

	@Deprecated
	@GetMapping("/members")
	public List<Map<String, Object>> members(@RequestParam Map<String, Object> paramMap) {

		return commonService.retrieveMembers(paramMap);
	}

	@GetMapping("/{tenantId}/members")
	public List<Map<String, Object>> tenantMembers(@PathVariable("tenantId") String tenantId,
			@RequestParam Map<String, Object> paramMap, HttpServletRequest request) {

		StringBuffer sb = new StringBuffer("");

		if (request.getQueryString() != null) {

			// QueryString 형태로 전달된 groupId를 Mysql의 RegExp에 맞춰 가공
			String query[] = request.getQueryString().split("&");
			if (query.length > 1) {

				for (int i = 0; i < query.length; i++) {

					String[] temp = query[i].split("=");
					if (temp.length == 2 && "tenantGroups".equals(temp[0]) && StringUtils.hasText(temp[1])) {
						sb.append(temp[1] + "|");
					}
				}

				sb.append("X");
			}
		}

		paramMap.put("tenantGroups", sb.toString());

		return commonService.retrieveTenantMembers(commonService.getValidTenantId(tenantId), paramMap);
	}

	@GetMapping("/{tenantId}/members/{userId}")
	public Map<String, Object> tenantMembers(@PathVariable("tenantId") String tenantId,
			@PathVariable("userId") String userId) {

		return commonService.retrieveTenantMember(commonService.getValidTenantId(tenantId), userId);
	}

	@Deprecated
	@GetMapping("/{tenantId}/members/{userId}/groups")
	public List<Map<String, Object>> tenantMemberGroups(@PathVariable("tenantId") String tenantId,
			@PathVariable("userId") String userId) {

		return commonService.retrieveTenantMemberGroups(commonService.getValidTenantId(tenantId), userId);
	}

	@GetMapping("/{tenantId}/history/logins")
	public List<Map<String, Object>> histroyLogins(@PathVariable("tenantId") String tenantId,
			@RequestParam Map<String, Object> paramMap) {

		return commonService.retrieveHistoryLogins(commonService.getValidTenantId(tenantId), paramMap);
	}

	@GetMapping("/{tenantId}/history/group-changes")
	public List<Map<String, Object>> histroyGroupChanges(@PathVariable("tenantId") String tenantId,
			@RequestParam Map<String, Object> paramMap) {

		return commonService.retrieveHistoryGroupChanges(commonService.getValidTenantId(tenantId), paramMap);
	}

	@GetMapping("/{tenantId}/sync-members/{baseDate}")
	public void syncMembers(@PathVariable("tenantId") String tenantId, @PathVariable("baseDate") String baseDate,
			@RequestParam Map<String, Object> paramMap) {

		commonService.processSyncMembers(commonService.getValidTenantId(tenantId), baseDate);
	}
}
