package com.daesang.rpa.repository.mariadb;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommonMapper {

	List<Map<String, Object>> selectStatisticsDivisions();

	List<Map<String, Object>> selectStatisticsDivisionTeams(String divName);

	List<Map<String, Object>> selectStatisticsTimeSummaries(Map<String, Object> paramMap);

	List<Map<String, Object>> selectStatisticsTargetCounts(Map<String, Object> paramMap);

	List<Map<String, Object>> selectStatisticsObjectCounts(Map<String, Object> paramMap);

	List<Map<String, Object>> selectStatisticsResults(Map<String, Object> paramMap);

	List<Map<String, Object>> selectStatisticsHourResults(Map<String, Object> paramMap);

	List<Map<String, Object>> selectTenants();

	Map<String, Object> selectTenant(String tenantId);

	List<Map<String, Object>> selectTenantGroups(String preName);

	List<Map<String, Object>> selectTenantWorkgroups(String preName);

	List<Map<String, Object>> selectMembers(Map<String, Object> paramMap);

	List<Map<String, Object>> selectTenantMembers(Map<String, Object> paramMap);

	Map<String, Object> selectTenantMember(Map<String, Object> paramMap);

	@Deprecated
	List<Map<String, Object>> selectTenantMemberGroups(String preName, String userId);

	List<Map<String, Object>> selectHistoryLogins(Map<String, Object> paramMap);

	List<Map<String, Object>> selectHistoryGroupChanges(Map<String, Object> paramMap);

	void insertHistoryGroupChange(Map<String, Object> paramMap);

	void insertHistoryGroupChangeDetail(Map<String, Object> paramMap);

	void insertHistoryApi(Map<String, Object> paramMap);

	List<Map<String, Object>> selectMemberRequired(String tenantId);
}