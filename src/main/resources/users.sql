insert into user( user_id, password, nickname, college_email_address, COLLEGE_id)
    values('wonbinkim', '$2a$16$nPFM5duMaxPRxB3jjh8PGez3hiHOfHE33xwaXkYX8gm76v1b42eJO', '김원빈', 'example@uos.ac.kr',
          (select COLLEGE_id from college_mail_domain where college_mail_domain = 'uos.ac.kr')
      );

insert into user( user_id, password, nickname, college_email_address, COLLEGE_id)
values('user1', '$2a$16$nPFM5duMaxPRxB3jjh8PGez3hiHOfHE33xwaXkYX8gm76v1b42eJO', '채민관', 'example2@uos.ac.kr',
       (select COLLEGE_id from college_mail_domain where college_mail_domain = 'uos.ac.kr')
      );