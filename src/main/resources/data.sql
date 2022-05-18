insert into college(college_name, latitude, longitude)
	values('서울시립대학교', 37.584828300773886, 127.05773316246166);

insert into college_mail_domain(COLLEGE_id, college_mail_domain)
	values(
		(select college_id from college where college_name = '서울시립대학교'),
        'uos.ac.kr'
	);

/*
insert into user( user_id, password, nickname, college_email_address, COLLEGE_id)
	values(
		'wonbinkim', 'password', '김원빈', 'rst0070@uos.ac.kr',
		(select COLLEGE_id from college_mail_domain where college_mail_domain = 'uos.ac.kr')
	);
 */
        