create DATABASE spring6;
create user IF not exists `restadmin`@`%` IDENTIFIED with mysql_native_password by 'password';
grant all on *.* to 'mysql-guru'@'localhost' with grant option;

