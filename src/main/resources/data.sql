insert into college(college_name, latitude, longitude)
	values('서울시립대학교', 37.584828300773886, 127.05773316246166);

insert into college_mail_domain(COLLEGE_id, college_mail_domain)
	values(
		(select college_id from college where college_name = '서울시립대학교'),
        'uos.ac.kr'
	);


insert into user( user_id, password, nickname, college_email_address, COLLEGE_id)
	values(
		'wonbinkim', 'password', '김원빈', 'example@uos.ac.kr',
		(select COLLEGE_id from college_mail_domain where college_mail_domain = 'uos.ac.kr')
	);
    
insert into place( place_name, place_address, latitude, longitude)
	values(
		'테스트용 가게 기꾸초밥',
        '경기 구리시 안골로 91 기꾸초밥',
        38,
        128
	);
    
insert into place_list_at_college(COLLEGE_id, PLACE_id)
	values (1, 1);

insert into kakao_place(PLACE_id, kakao_place_id, category)
	values (1, '16955698', '초밥');

insert into review(PLACE_id, USER_id, post_date, post_text, rating)
	values(1, 1, '2022-05-31', '테스트용 리뷰', 4);

        