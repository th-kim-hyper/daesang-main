CREATE OR REPLACE DATABASE `portal`;

CREATE OR REPLACE TABLE `portal`.`tb_api_request` (
  `REQ_ID` varchar(50) NOT NULL COMMENT 'API요청이력ID',
  `REQ_IP` varchar(50) NOT NULL COMMENT '요청IP',
  `REQ_URL` varchar(200) NOT NULL COMMENT '요청URL',
  `REQ_METHOD` varchar(8) NOT NULL COMMENT '요청 METHOD',
  `REQ_JSON` longtext COMMENT '요청파라메터',
  `RES_JSON` longtext COMMENT '응답파라메터',
  `REG_DT` timestamp NOT NULL COMMENT '등록시간'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='API요청이력';

CREATE OR REPLACE TABLE `portal`.`tb_group_change` (
  `APV_NO` varchar(50) NOT NULL COMMENT '승인번호',
  `APV_USER_ID` varchar(50) COMMENT '승인자ID',
  `APV_DT` timestamp COMMENT '승인시간',
  `REQ_USER_ID` varchar(50) COMMENT '요청자ID',
  `REQ_DT` timestamp COMMENT '요청시간',
  `REG_DT` timestamp NOT NULL COMMENT '등록시간',
  PRIMARY KEY (`APV_NO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='그룹변경요청이력';

CREATE OR REPLACE TABLE `portal`.`tb_group_change_detail` (
  `APV_NO` varchar(50) NOT NULL COMMENT '승인번호',
  `USER_ID` varchar(50) NOT NULL COMMENT '대상자ID',
  `GROUP_ID` varchar(50) NOT NULL COMMENT '대상그룹',
  `ADD_YN` tinyint(1) NOT NULL COMMENT '그룹추가여부',
  `SUCCESS_YN` tinyint(1) NOT NULL COMMENT '성공여부',
  `BEFORE_STATUS` varchar(200) COMMENT '변경전상태',
  `REQ_ID` varchar(50) NOT NULL COMMENT 'API요청이력ID',
  `REG_DT` timestamp NOT NULL COMMENT '등록시간'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='그룹변경요청이력(상세)';

CREATE OR REPLACE TABLE `portal`.`tb_member_required` (
  `TENANT_ID` varchar(40) NOT NULL COMMENT '테넌트ID',
  `USER_ID` varchar(50) NOT NULL COMMENT '사용자ID',
  `REG_DT` timestamp NOT NULL COMMENT '등록시간'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='작업이력';

INSERT INTO portal.tb_member_required (TENANT_ID, USER_ID, REG_DT) VALUES ('tenant_1', 'admin', SYSDATE());

CREATE OR REPLACE TABLE `portal`.`tb_job_info` (
  `TARGET_ID` varchar(40) NOT NULL COMMENT '작업ID',
  `TARGET_NAME` varchar(200) NOT NULL COMMENT '작업명',
  `DIV_NAME` varchar(200) NOT NULL COMMENT '본부명',
  `TEAM_NAME` varchar(200) NOT NULL COMMENT '팀명',
  `REG_DT` timestamp NOT NULL COMMENT '등록시간'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='작업정보';

INSERT INTO portal.tb_job_info (TARGET_ID,TARGET_NAME,DIV_NAME,TEAM_NAME,REG_DT) VALUES
	 ('b31ec0c2-2b75-4e6c-a1ad-911bf861bb59','RPA_0000_공통TASK','대상주식회사','혁신기획팀',NULL),
	 ('5a05e9d9-c550-4c9b-b201-4a7b02b7f990','RPA_0000_표준스크립트','대상주식회사','혁신기획팀',NULL),
	 ('2abca419-07fe-4ad3-b5be-5410c51fcb60','RPA_0040_VOC실적레포트','CS본부','CCM팀',NULL),
	 ('d571af21-539b-4ca6-9e2c-1ba5dbabbce6','RPA_0060_매출거래처_반출입처리','영업본부','영업관리1팀',NULL),
	 ('abbfb3e9-9e67-4e94-90f3-fa2fb44cc6e5','RPA_0070_일일실적레포트','영업본부','영업관리1팀',NULL),
	 ('1b088794-6aff-4bc0-98fd-d20d85c8d201','RPA_0080_사사자료실업로드','홍보실','홍보팀',NULL),
	 ('655e9fc6-639c-4b6d-8df4-ffe0445c166a','RPA_0100_온라인매출마감','온라인사업부','온라인지원팀',NULL),
	 ('af4830c8-d053-4177-9b66-74f62dcf7e28','RPA_0110_CVS주문생성','영업본부','KAM3팀',NULL),
	 ('554f921e-d651-4830-8142-fba98e476eaa','RPA_0120_CH센터WMS할당_2차','물류본부','CH센터',NULL),
	 ('f35f31b7-4847-4727-90d3-9aaa924b0d60','RPA_0130_유통사입고예약','물류본부','칠곡센터/용인신선센터/양지센터',NULL);
INSERT INTO portal.tb_job_info (TARGET_ID,TARGET_NAME,DIV_NAME,TEAM_NAME,REG_DT) VALUES
	 ('f02703fb-b942-4f66-8b68-4ecb4ee59d2d','RPA_0160_행사가_매출조정','영업본부','영업관리1팀',NULL),
	 ('1a1c0ea1-dd2c-4ae6-88cc-a8886ab3e66d','RPA_0190_온라인쇼핑몰가격조사','온라인사업부','온라인지원팀',NULL),
	 ('3cc5c0b5-0ace-4d04-8410-6f8630bcec00','RPA_0200_선사TRACING','물류본부','국제물류팀',NULL),
	 ('fadbd260-62d2-4f23-8c31-237ee531a718','RPA_0210_크레탑조기경보_SAP업데이트','영업본부','여신관리팀',NULL),
	 ('acc39a2b-c53b-4bfd-af80-4aebf07b6991','RPA_0220_유통ERP구매계약','영업본부','가공1팀',NULL),
	 ('1ebc6e89-9540-4aa6-96f0-bba68cf69cca','RPA_0230_실수요웹주문','영업본부','실수요영업2팀',NULL),
	 ('8616a100-fbb1-4992-acfe-200a9fe83324','RPA_0240_대리점재고회전일공유','영업본부','영업관리1팀',NULL),
	 ('4366f5c1-6708-461b-989a-9a0b6cadf536','RPA_0250_KAM2팀주문생성','영업본부','KAM2팀',NULL),
	 ('e53f2ce2-65da-4b2f-89f7-90c0400c2af4','RPA_0260_에브리데이실적이관','영업본부','KAM2팀',NULL),
	 ('d6809620-8e7f-4555-b400-67c706f3a1b7','RPA_0270_총괄판촉리포트','영업본부','소매전략팀',NULL);
INSERT INTO portal.tb_job_info (TARGET_ID,TARGET_NAME,DIV_NAME,TEAM_NAME,REG_DT) VALUES
	 ('e278334d-0670-45e2-9640-05fd6dd02adb','RPA_0280_인프라2고용확인레포트','영업본부','영업관리1팀',NULL),
	 ('8c8a4946-6aa4-4976-9bbf-4a996a6dd483','RPA_1010_법인카드_무증빙_결재','재경본부','FS센터',NULL),
	 ('959893b1-065f-44f7-b939-90e9514af820','RPA_1010_법인카드_무증빙_결재v2','재경본부','FS센터',NULL),
	 ('448eff2d-b1d9-4b5c-85ac-83c3d7d8efee','RPA_1010_법인카드_무증빙_결재_테스트','재경본부','FS센터',NULL),
	 ('91ab3fa5-9024-49e6-8b53-fdb8065eb2b3','RPA_1010_법인카드_무증빙_결재_테스트_원본','재경본부','FS센터',NULL),
	 ('00051d2d-7745-4de7-b8d6-d183cd40c9cd','RPA_1030_현금무증빙결재','재경본부','FS센터',NULL),
	 ('d1ba4400-5ba4-47c5-b50c-819405fa87ec','RPA_1050_매출세금계산서발행','재경본부','FS센터',NULL),
	 ('b35dca8d-4886-4559-8944-be69e856fe9f','RPA_1060_수입부가세정산','재경본부','회계팀',NULL),
	 ('1507255d-9e6a-4448-a5c5-ddef73ee03be','RPA_1070_미결계정관리','재경본부','FS센터',NULL),
	 ('c43a1214-99ff-44ab-ace4-9fcb4866b787','RPA_1090_비구매거래처승인','재경본부','FS센터',NULL);
INSERT INTO portal.tb_job_info (TARGET_ID,TARGET_NAME,DIV_NAME,TEAM_NAME,REG_DT) VALUES
	 ('86982b86-757d-4c70-ba27-2e233a38f7bd','RPA_1100_비영업고객승인','재경본부','FS센터',NULL),
	 ('3f3acafd-0b09-47fc-9e12-03e7e251627f','RPA_1110_증빙대사리스트출력','재경본부','FS센터',NULL),
	 ('25044e0a-c80b-4350-bd38-21772d1fc2fa','RPA_1120_미제출증빙관리','재경본부','FS센터',NULL),
	 ('481d22fe-c394-4948-b22f-fdaa975c2f02','RPA_1140_의제매입세액공제신고서작성','재경본부','FS센터',NULL),
	 ('4df08a14-e474-4bfc-ae84-a9b8c94bd856','RPA_1150_미결전표메일링','재경본부','FS센터',NULL),
	 ('24d82764-397f-47ae-a220-d83c9a336610','RPA_1160_현업마감확인','재경본부','회계팀',NULL),
	 ('13cd964c-efa0-49e9-9dc3-91d9e66f0eef','RPA_1170_대손충담금','재경본부','회계팀',NULL),
	 ('df4c3c62-691d-4cd3-9a5d-c177866c7425','RPA_1180_상품매입마감','재경본부','회계팀',NULL),
	 ('8aed301a-9fd0-4d37-b29d-d126b0360ac2','RPA_1190_유급휴가기표','재경본부','회계팀',NULL),
	 ('90c313c8-f6ce-44df-bdc9-6b2af3a7c20f','RPA_1200_외화평가수행','재경본부','회계팀',NULL);
INSERT INTO portal.tb_job_info (TARGET_ID,TARGET_NAME,DIV_NAME,TEAM_NAME,REG_DT) VALUES
	 ('4b75c5d6-7342-4ca0-b955-4bcdc3fe05d8','RPA_1210_감가상각','재경본부','회계팀',NULL),
	 ('b55713eb-0801-4f01-bbbc-5e52495160b1','RPA_1220_선급비용정산','재경본부','회계팀',NULL),
	 ('c8e8c938-6ca6-4a95-a003-7a68560a5405','RPA_1230_퇴직금충담금설정','재경본부','회계팀',NULL),
	 ('f2280485-e3e1-4199-908b-529f1adc463c','RPA_1240_GRIR의제매입Clearing','재경본부','회계팀',NULL),
	 ('8e144395-2422-4075-a4ba-6c6e1e99a694','RPA_1260_부가가치세매입마감','재경본부','회계팀',NULL),
	 ('edc23a98-881f-4784-a9f8-463cdbe6c6aa','RPA_1270_자가소비부가세기표','재경본부','회계팀',NULL),
	 ('88fb039a-9dd8-4741-bcfc-afa54f79c0d2','RPA_1280_부가세공통매입안분','재경본부','회계팀',NULL),
	 ('05fd81f2-67bb-4383-9e6d-4fd259e1bdc7','RPA_1290_부가세간주임대료기표','재경본부','회계팀',NULL),
	 ('2dcae1e5-2dc2-4832-9311-d77d3d64ce00','RPA_1300_부가세매출마감','재경본부','회계팀',NULL),
	 ('18534246-a7e5-4972-a708-3066eacfd89f','RPA_1310_재고평가충당금설정','재경본부','회계팀',NULL);
INSERT INTO portal.tb_job_info (TARGET_ID,TARGET_NAME,DIV_NAME,TEAM_NAME,REG_DT) VALUES
	 ('f2060d23-1610-4767-a432-a420a61b6ee7','RPA_1320_재고자산조정','재경본부','회계팀',NULL),
	 ('6d33fdef-1196-4ad5-993d-415a036658c9','RPA_1330_IFRS15대체전기수행','재경본부','회계팀',NULL),
	 ('9fe1e48e-5d6a-4fa6-849a-9dff2b4aa029','RPA_1340_명세서작성','재경본부','회계팀',NULL),
	 ('b94628d9-1739-42ce-83d9-ec20f463a411','RPA_1350_주석작성','재경본부','회계팀',NULL),
	 ('a20be53c-acb7-4ce2-9478-a9c7d8d36f6e','RPA_1360_매출TOD작성','재경본부','회계팀',NULL),
	 ('066a5754-9807-4350-af0d-898c6a030541','RPA_1370_유형자산TOD작성','재경본부','회계팀',NULL),
	 ('3a51af41-fd8e-4943-8627-73e8dadeae8f','RPA_1390_자재수불부분석','재경본부','회계팀',NULL),
	 ('0ccc9271-3ae5-4974-9747-d86253b175f3','RPA_1400_POS입금미결관리','재경본부','회계팀',NULL),
	 ('dcd9a9d2-cb08-4ac5-8927-047bead7c552','RPA_1410_미착미확정매입채무대체','재경본부','회계팀',NULL),
	 ('09c1a226-2e4f-4bb8-b424-f8a877c95a47','RPA_1420_신용카드대사및입금등록','재경본부','회계팀',NULL);
INSERT INTO portal.tb_job_info (TARGET_ID,TARGET_NAME,DIV_NAME,TEAM_NAME,REG_DT) VALUES
	 ('7fe349f5-6fc2-4bbf-95e4-c23ae225b991','RPA_1430_부가세신고','재경본부','회계팀',NULL),
	 ('b07ba31d-d471-4152-b2a8-6ed2874c9e03','RPA_1440_부가세직수출대사','재경본부','회계팀',NULL),
	 ('76b73dde-8f80-4b07-8fe8-4e26951dfbda','RPA_1440_부가세직수출대사','재경본부','회계팀',NULL),
	 ('47c653b8-bbb7-44a0-a7a8-fea119b91ad8','RPA_1460_의제매입세액한도계산','재경본부','회계팀',NULL),
	 ('ff129921-149d-4a64-9984-56eafdac3a59','RPA_1470_원천세신고','재경본부','회계팀',NULL),
	 ('0ae43d0e-9285-4868-9802-4d12a10d6160','RPA_1480_내부거래대사','재경본부','회계팀',NULL),
	 ('0f3beb90-6878-4ecb-8578-53b7d55c5bc6','RPA_1490_특관자시가모니터링','재경본부','회계팀',NULL),
	 ('af105bce-c92e-4122-8c1a-48818c611ac2','RPA_1490_특관자시가모니터링','재경본부','회계팀',NULL),
	 ('06c0e147-669c-4f58-8edd-16b72c851d92','RPA_1510_MDM영업고객승인','재경본부','회계팀',NULL),
	 ('5ab4709c-52c8-47ca-bb60-0d0b23c3c689','RPA_1530_금융거래처생성및관리','재경본부','재무팀',NULL);
INSERT INTO portal.tb_job_info (TARGET_ID,TARGET_NAME,DIV_NAME,TEAM_NAME,REG_DT) VALUES
	 ('d0c18a04-1455-40ad-8c11-0dc8a3a026ce','RPA_1540_구매처지급조건관리','재경본부','재무팀',NULL),
	 ('649444cd-159c-434f-9837-cfab77a6131c','RPA_1550_환율등록관리','재경본부','재무팀',NULL),
	 ('a9b96730-df31-45e3-9466-191c163ae9a8','RPA_1560_원화출금','재경본부','재무팀',NULL),
	 ('e9f39f2e-38af-4ff8-bb2b-b94694700acf','RPA_1570_외화출금','재경본부','재무팀',NULL),
	 ('cd2e3df0-95e0-46ac-8ad4-53d4de6815e5','RPA_1580_원화입금','재경본부','재무팀',NULL),
	 ('42549810-bf85-4549-98c9-631caca3610f','RPA_1590_외화입금','재경본부','재무팀',NULL),
	 ('eebc1021-5fae-4385-ad8e-56f6277286e4','RPA_1600_자금조달기표','재경본부','재무팀',NULL),
	 ('12ff4c44-238d-4aca-aa44-c97e08a4f64f','RPA_1610_현물환거래생성','재경본부','재무팀',NULL),
	 ('89199ddb-6f4b-44b1-a972-02355b9a6983','RPA_1610_현물환거래생성','재경본부','재무팀',NULL),
	 ('bca64d02-681d-44f0-94de-ffcb1d3d9d50','RPA_1620_명세서관리','재경본부','재무팀',NULL);
INSERT INTO portal.tb_job_info (TARGET_ID,TARGET_NAME,DIV_NAME,TEAM_NAME,REG_DT) VALUES
	 ('962ad842-496d-4252-bbe9-96d2289076f3','RPA_1621_명세서결산','재경본부','재무팀',NULL),
	 ('60469ea5-2a49-4f1a-895f-95bec98ad150','RPA_1621_명세서결산','재경본부','재무팀',NULL),
	 ('0715b95e-dbc6-4c7b-846f-fdbae16bdce4','RPA_1621_명세서결산','재경본부','재무팀',NULL),
	 ('52bdfbf7-fef7-43ad-9e71-851928780142','RPA_1621_명세서결산_테스트','재경본부','재무팀',NULL),
	 ('bb3a6a5c-99ce-4d58-affa-1435fcbcc1ab','RPA_1630_재무보고업무','재경본부','재무팀',NULL),
	 ('d56c695e-8957-4963-b2c2-3fb7a90d43d7','RPA_1640_법인카드관리','재경본부','재무팀',NULL),
	 ('f3101494-1e06-4c25-b6b9-a153ff6a046c','RPA_1640_법인카드관리','재경본부','재무팀',NULL),
	 ('89b1572a-c172-46b9-89cb-a7f8aacd5b79','RPA_1650_자금수지실적및계획분석','재경본부','재무팀',NULL),
	 ('44fb140c-7edb-4bf1-8580-859ec21dc0da','RPA_1660_공시','재경본부','재무팀',NULL),
	 ('b5d3958a-d725-403a-a7d9-b4617ec639d7','RPA_1670_손익자료추출','재경본부','재무팀',NULL);
INSERT INTO portal.tb_job_info (TARGET_ID,TARGET_NAME,DIV_NAME,TEAM_NAME,REG_DT) VALUES
	 ('242d5269-9434-47af-934f-8f2e221bcd6b','RPA_1680_IR실행','재경본부','재무팀',NULL),
	 ('8aa304bd-4317-4175-acdf-fe487e965b42','RPA_1700_금융시장동향분석','재경본부','재무팀',NULL),
	 ('ffbcda68-0a70-4da7-af01-e2eaa0bbae17','RPA_1700_금융시장동향분석','재경본부','재무팀',NULL),
	 ('2ffcf022-4475-4530-af2b-0031892ae968','RPA_1710_본부주간업무취합','재경본부','재무팀',NULL),
	 ('2019f7ba-1ad3-480c-8dea-15aaeb414636','RPA_1720_모집단조회','재경본부','IC센터',NULL),
	 ('f57db790-d827-48ef-b897-f07e74b71927','RPA_1730_모집단업로드','재경본부','IC센터',NULL),
	 ('f75cf7f9-b333-4f27-a64c-7a6f457decd1','RPA_1740_증빙요청및수취','재경본부','IC센터',NULL),
	 ('eb51407a-ef1c-458a-866d-28e71aca55cb','RPA_1750_증빙적격성검증','재경본부','IC센터',NULL),
	 ('275fcc74-937f-4552-99e4-6ae23b11b626','RPA_1760_중요성판단','재경본부','IC센터',NULL),
	 ('7f292f7d-03a0-4ff7-8fef-b479f996da5d','RPA_1770_변화관리','재경본부','IC센터',NULL);
INSERT INTO portal.tb_job_info (TARGET_ID,TARGET_NAME,DIV_NAME,TEAM_NAME,REG_DT) VALUES
	 ('8a42bc5c-0407-4ed6-af16-863d496e1a29','RPA_9900_운영관리','대상주식회사','혁신기획팀',NULL),
	 ('16136370-9384-4c89-bfc6-8a1bc2e75fc3','RPA_9900_패스워드변경','대상주식회사','혁신기획팀',NULL);
