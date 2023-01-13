package com.daesang.rpa.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.daesang.rpa.service.CommonService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/proxy")
public class ProxyController {

	final private CommonService commonService;

	@GetMapping("/tenants")
	public List<Map<String, Object>> tenants(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) {

		return commonService.retrieveTenantsProxy(request);
	}

	@GetMapping("{tenantId}/members")
	public List<Map<String, Object>> tenantMembers(HttpServletRequest request,
			@PathVariable("tenantId") String tenantId, @RequestParam Map<String, Object> paramMap) {

		return commonService.retrieveTenantMembersProxy(commonService.getValidTenantId(tenantId), paramMap);
	}

	@GetMapping("/{tenantId}/members/{userId}")
	public Map<String, Object> tenantMembers(@PathVariable("tenantId") String tenantId,
			@PathVariable("userId") String userId) {

		return commonService.retrieveTenantMember(commonService.getValidTenantId(tenantId), userId);
	}

	@GetMapping("{tenantId}/groups")
	public List<Map<String, Object>> tenantGroups(HttpServletRequest request, @PathVariable("tenantId") String tenantId,
			@RequestParam Map<String, Object> paramMap) {

		return commonService.retrieveTenantGroupsProxy(request, commonService.getValidTenantId(tenantId));
	}

	@PostMapping("/{tenantId}/group-changes")
	public Map<String, Object> tenantGroupChanges(HttpServletRequest request, @PathVariable("tenantId") String tenantId,
			@RequestBody Map<String, Object> paramMap) {

		return commonService.processTenantGroupChangesProxy(request, commonService.getValidTenantId(tenantId),
				paramMap);
	}
}
