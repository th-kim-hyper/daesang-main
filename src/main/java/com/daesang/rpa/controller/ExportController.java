package com.daesang.rpa.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.daesang.rpa.service.CommonService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/export")
public class ExportController {

	final private CommonService commonService;

	@GetMapping("/{tenantId}/history/logins")
	public void histroyLogins(HttpServletResponse response, @PathVariable("tenantId") String tenantId,
			@RequestParam Map<String, Object> paramMap) throws IOException {

		commonService.export(response,
				commonService.retrieveHistoryLogins(commonService.getValidTenantId(tenantId), paramMap), "로그인이력");
	}

	@GetMapping("/{tenantId}/history/group-changes")
	public void histroyGroupChanges(HttpServletResponse response, @PathVariable("tenantId") String tenantId,
			@RequestParam Map<String, Object> paramMap) throws IOException {

		commonService.export(response,
				commonService.retrieveHistoryGroupChanges(commonService.getValidTenantId(tenantId), paramMap),
				"그룹(권한)변경이력");
	}
}
