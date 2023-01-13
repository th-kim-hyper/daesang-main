package com.daesang.rpa.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.daesang.rpa.service.CommonService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class PageController {

	final private CommonService commonService;

	@PostMapping(value = { "/ext/sso", "/ext/sso/" })
	public String index(HttpServletRequest request, HttpServletResponse response) {

		commonService.authentication(request, response);

		return "redirect:/page/statistics";
	}

	@GetMapping("/page/statistics")
	public ModelAndView statistics() {

		ModelAndView modelAndView = new ModelAndView("html/statistics");
		modelAndView.addObject("divisions", commonService.retrieveStatisticsDivisions()); // 검색조건

		return modelAndView;
	}

	@GetMapping("/page/{tenantId}/members")
	public ModelAndView tenantMembers(@PathVariable("tenantId") String tenantId) {

		ModelAndView modelAndView = new ModelAndView("html/members");

		String validTenantId = commonService.getValidTenantId(tenantId);

		modelAndView.addObject("tenantId", validTenantId);
		modelAndView.addObject("tenantGroups", commonService.retrieveTenantGroups(validTenantId)); // 검색조건
		modelAndView.addObject("tenantWorkgroups", commonService.retrieveTenantWorkgroups(validTenantId)); // 검색조건

		return modelAndView;
	}

	@GetMapping("/page/{tenantId}/history/logins")
	public ModelAndView tenantHistoryLogins(@PathVariable("tenantId") String tenantId) {

		ModelAndView modelAndView = new ModelAndView("html/logins");

		String validTenantId = commonService.getValidTenantId(tenantId);

		modelAndView.addObject("tenantId", validTenantId);

		return modelAndView;
	}

	@GetMapping("/page/{tenantId}/history/group-changes")
	public ModelAndView tenantHistoryGroupChanges(@PathVariable("tenantId") String tenantId) {

		ModelAndView modelAndView = new ModelAndView("html/group-changes");

		String validTenantId = commonService.getValidTenantId(tenantId);

		modelAndView.addObject("tenantId", validTenantId);
		modelAndView.addObject("tenantGroups", commonService.retrieveTenantGroups(validTenantId)); // 검색조건

		return modelAndView;
	}
}