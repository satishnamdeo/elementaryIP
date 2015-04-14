INSERT INTO `elemntryip_master`.`url_master` (`url`, `url_name`) VALUES ('/elementryIp/manageproject.html', 'Manage Project');
INSERT INTO `elemntryip_master`.`role_x_url_master` (`role_id`, `url_id`) VALUES ('1', (select  url_id from elemntryip_master.url_master where url like '/elementryIp/manageproject.html'));
INSERT INTO `elemntryip_master`.`role_x_url_master` (`role_id`, `url_id`) VALUES ('2', (select  url_id from elemntryip_master.url_master where url like '/elementryIp/manageproject.html'));

INSERT INTO `elemntryip_master`.`url_master` (`url`, `url_name`) VALUES ('/elementryIp/createproject.html', 'Create Project');
INSERT INTO `elemntryip_master`.`role_x_url_master` (`role_id`, `url_id`) VALUES ('1', (select  url_id from elemntryip_master.url_master where url like '/elementryIp/createproject.html'));
INSERT INTO `elemntryip_master`.`role_x_url_master` (`role_id`, `url_id`) VALUES ('2', (select  url_id from elemntryip_master.url_master where url like '/elementryIp/createproject.html'));

INSERT INTO `elemntryip_master`.`url_master` (`url`, `url_name`) VALUES ('/elementryIp/deleteproject.html', 'Delete Project');
INSERT INTO `elemntryip_master`.`role_x_url_master` (`role_id`, `url_id`) VALUES ('1', (select  url_id from elemntryip_master.url_master where url like '/elementryIp/deleteproject.html'));
INSERT INTO `elemntryip_master`.`role_x_url_master` (`role_id`, `url_id`) VALUES ('2', (select  url_id from elemntryip_master.url_master where url like '/elementryIp/deleteproject.html'));

INSERT INTO `elemntryip_master`.`url_master` (`url`, `url_name`) VALUES ('/elementryIp/projectdetail.html', 'Project Detail');
INSERT INTO `elemntryip_master`.`role_x_url_master` (`role_id`, `url_id`) VALUES ('1', (select  url_id from elemntryip_master.url_master where url like '/elementryIp/projectdetail.html'));
INSERT INTO `elemntryip_master`.`role_x_url_master` (`role_id`, `url_id`) VALUES ('2', (select  url_id from elemntryip_master.url_master where url like '/elementryIp/projectdetail.html'));

INSERT INTO `elemntryip_master`.`url_master` (`url`, `url_name`) VALUES ('/elementryIp/searchProjectPatent.html', 'Search Project Patent');
INSERT INTO `elemntryip_master`.`role_x_url_master` (`role_id`, `url_id`) VALUES ('1', (select  url_id from elemntryip_master.url_master where url like '/elementryIp/searchProjectPatent.html'));
INSERT INTO `elemntryip_master`.`role_x_url_master` (`role_id`, `url_id`) VALUES ('2', (select  url_id from elemntryip_master.url_master where url like '/elementryIp/searchProjectPatent.html'));

<!-- On date 3/21/15 -->
INSERT INTO `elemntryip_master`.`url_master` (`url`, `url_name`) VALUES ('/elementryIp/externalsearch.html', 'External Search');
INSERT INTO `elemntryip_master`.`role_x_url_master` (`role_id`, `url_id`) VALUES ('1', (select  url_id from elemntryip_master.url_master where url like '/elementryIp/externalsearch.html'));
INSERT INTO `elemntryip_master`.`role_x_url_master` (`role_id`, `url_id`) VALUES ('2', (select  url_id from elemntryip_master.url_master where url like '/elementryIp/externalsearch.html'));

<!-- On date 3/23/15 -->
INSERT INTO `elemntryip_master`.`url_master` (`url`, `url_name`) VALUES ('/elementryIp/importpatent.html', 'Import Patent');
INSERT INTO `elemntryip_master`.`role_x_url_master` (`role_id`, `url_id`) VALUES ('1', (select  url_id from elemntryip_master.url_master where url like '/elementryIp/importpatent.html'));
INSERT INTO `elemntryip_master`.`role_x_url_master` (`role_id`, `url_id`) VALUES ('2', (select  url_id from elemntryip_master.url_master where url like '/elementryIp/importpatent.html'));

<!-- On date 4/14/15 -->
ALTER TABLE `elemntryip_master`.`project_manage` 
ADD COLUMN `project_notes` VARCHAR(45) NULL AFTER `last_modified`;

