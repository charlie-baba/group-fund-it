IF EXISTS(SELECT a.id FROM bank a WHERE a.id = 1)
	UPDATE bank SET id = 1 WHERE id = 1;
ELSE 
INSERT INTO bank VALUES (1, true, '2016-08-05 20:26:06.047', 'System', false, '2016-08-05 20:26:06.047', 'System', '044', 'Access Bank PLC.');
INSERT INTO bank VALUES (1, true, '2016-08-05 20:26:06.047', 'System', false, '2016-08-05 20:26:06.047', 'System', '044', 'Access Bank PLC.');
INSERT INTO bank VALUES (2, true, '2016-08-05 20:26:06.047', 'System', false, '2016-08-05 20:26:06.047', 'System', '023', 'City Bank PLC.');
INSERT INTO bank VALUES (3, true, '2016-08-05 20:26:06.047', 'System', false, '2016-08-05 20:26:06.047', 'System', '063', 'Diamond Bank PLC.');
INSERT INTO bank VALUES (4, true, '2016-08-05 20:26:06.047', 'System', false, '2016-08-05 20:26:06.047', 'System', '050', 'Ecobank PLC.');
INSERT INTO bank VALUES (5, true, '2016-08-05 20:26:06.047', 'System', false, '2016-08-05 20:26:06.047', 'System', '070', 'Fidelity Bank PLC.');
INSERT INTO bank VALUES (6, true, '2016-08-05 20:26:06.047', 'System', false, '2016-08-05 20:26:06.047', 'System', '011', 'First Bank of Nigeria PLC.');
INSERT INTO bank VALUES (7, true, '2016-08-05 20:26:06.047', 'System', false, '2016-08-05 20:26:06.047', 'System', '214', 'First City Monument Bank PLC.');
INSERT INTO bank VALUES (8, true, '2016-08-05 20:26:06.047', 'System', false, '2016-08-05 20:26:06.047', 'System', '058', 'Guarantee Trust Bank PLC.');
INSERT INTO bank VALUES (9, true, '2016-08-05 20:26:06.047', 'System', false, '2016-08-05 20:26:06.047', 'System', '030', 'Heritage Bank');
INSERT INTO bank VALUES (10, true, '2016-08-05 20:26:06.047', 'System', false, '2016-08-05 20:26:06.047', 'System', '082', 'Keystone Bank');
INSERT INTO bank VALUES (11, true, '2016-08-05 20:26:06.047', 'System', false, '2016-08-05 20:26:06.047', 'System', '076', 'Skye Bank PLC.');
INSERT INTO bank VALUES (12, true, '2016-08-05 20:26:06.047', 'System', false, '2016-08-05 20:26:06.047', 'System', '221', 'Stanbic-IBTC Bank PLC.');
INSERT INTO bank VALUES (13, true, '2016-08-05 20:26:06.047', 'System', false, '2016-08-05 20:26:06.047', 'System', '068', 'Standard Chartered Bank');
INSERT INTO bank VALUES (14, true, '2016-08-05 20:26:06.047', 'System', false, '2016-08-05 20:26:06.047', 'System', '232', 'Sterling Bank PLC.');
INSERT INTO bank VALUES (15, true, '2016-08-05 20:26:06.047', 'System', false, '2016-08-05 20:26:06.047', 'System', '032', 'Union Bank of Nigeria PLC.');
INSERT INTO bank VALUES (16, true, '2016-08-05 20:26:06.047', 'System', false, '2016-08-05 20:26:06.047', 'System', '033', 'United Bank For Africa PLC.');
INSERT INTO bank VALUES (17, true, '2016-08-05 20:26:06.047', 'System', false, '2016-08-05 20:26:06.047', 'System', '215', 'Unity Bank PLC.');
INSERT INTO bank VALUES (18, true, '2016-08-05 20:26:06.047', 'System', false, '2016-08-05 20:26:06.047', 'System', '035', 'Wema Bank PLC.');
INSERT INTO bank VALUES (19, true, '2016-08-05 20:26:06.047', 'System', false, '2016-08-05 20:26:06.047', 'System', '057', 'Zenith Bank PLC.');


IF EXISTS(SELECT p.id FROM project_category p WHERE p.id = 1)
	UPDATE project_category SET id = 1 WHERE id = 1;
ELSE 
INSERT INTO project_category VALUES (1, true, '2017-12-29 08:29:06.047', 'System', false, '2017-12-29 08:29:06.047', 'System', 'Weddings', 'WEDDING');
INSERT INTO project_category VALUES (1, true, '2017-12-29 08:29:06.047', 'System', false, '2017-12-29 08:29:06.047', 'System', 'Weddings', 'WEDDING');
INSERT INTO project_category VALUES (2, true, '2017-12-29 08:29:06.047', 'System', false, '2017-12-29 08:29:06.047', 'System', 'Birthdays', 'BIRTHDAY');
INSERT INTO project_category VALUES (3, true, '2017-12-29 08:29:06.047', 'System', false, '2017-12-29 08:29:06.047', 'System', 'Burial/Condolence', 'BURIAL');
INSERT INTO project_category VALUES (4, true, '2017-12-29 08:29:06.047', 'System', false, '2017-12-29 08:29:06.047', 'System', 'Fund-Raiser', 'FUND-RAISER');
INSERT INTO project_category VALUES (5, true, '2017-12-29 08:29:06.047', 'System', false, '2017-12-29 08:29:06.047', 'System', 'Excursion/Vacation', 'EXCURSION');
INSERT INTO project_category VALUES (6, true, '2017-12-29 08:29:06.047', 'System', false, '2017-12-29 08:29:06.047', 'System', 'Others', 'OTHERS');


IF EXISTS(SELECT r.id FROM role r WHERE r.id = 1)
	UPDATE role SET id = 1 WHERE id = 1;
ELSE
INSERT INTO role VALUES ('1', true, '2018-01-23 20:09:55.988', 'System', false, '2018-01-23 20:09:55.988', 'System', 'ADMIN', 'ADMIN');
INSERT INTO role VALUES ('1', true, '2018-01-23 20:09:55.988', 'System', false, '2018-01-23 20:09:55.988', 'System', 'ADMIN', 'ADMIN');


IF EXISTS(SELECT u.id FROM user_profile u WHERE u.id = 1)
	UPDATE user_profile SET id = 1 WHERE id = 1;
ELSE
INSERT INTO user_profile VALUES ('1',true,'2018-01-23 20:09:55.856','System',false,'2018-01-23 20:09:55.941','System',66,null,'admin@groupwallet.com','Admin','MALE','Admin',null,'$2a$10$8iH442UwT1rVH91Ld04PZe4m8iFBm4yFVstdqslNBh6n1EgWCv9bG',false,'08012345678',null);
INSERT INTO user_profile VALUES ('1',true,'2018-01-23 20:09:55.856','System',false,'2018-01-23 20:09:55.941','System',66,null,'admin@groupwallet.com','Admin','MALE','Admin',null,'$2a$10$8iH442UwT1rVH91Ld04PZe4m8iFBm4yFVstdqslNBh6n1EgWCv9bG',false,'08012345678',null);

IF EXISTS(SELECT r.id FROM user_role r WHERE r.id = 1)
	UPDATE user_role SET id = 1 WHERE id = 1;
ELSE
INSERT INTO user_role VALUES ('1',true,'2018-01-23 20:09:55.963','System',false,'2018-01-23 20:09:55.996','System','1','1');
INSERT INTO user_role VALUES ('1',true,'2018-01-23 20:09:55.963','System',false,'2018-01-23 20:09:55.996','System','1','1');


