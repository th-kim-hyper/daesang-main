package com.daesang.rpa.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.daesang.rpa.service.CommonService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class MemberTask {

	private final CommonService commonService;

	@Scheduled(cron = "${rpa.scheduled.sync-member}")
	public void removeExpiredFavorite() {

		commonService.processSyncMembers(commonService.getValidTenantId(null), null);
	}
}
