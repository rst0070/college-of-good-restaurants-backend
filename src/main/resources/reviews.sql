insert into review(review_id, PLACE_id, USER_id, post_date, post_text, rating) values(16610622302321234,1, 'wonbinkim', '2022-05-31', '테스트용 리뷰', 4);
    insert into REVIEW_IMAGE_LIST(REVIEW_id, image_url) values (16610622302321234, 'https://test.image.1');
    insert into REVIEW_IMAGE_LIST(REVIEW_id, image_url) values (16610622302321234, 'https://test.image.2');
    insert into REVIEW_IMAGE_LIST(REVIEW_id, image_url) values (16610622302321234, 'https://test.image.3');

insert into review(review_id, PLACE_id, USER_id, post_date, post_text, rating) values(16618655171448795, 2, 'wonbinkim', '2022-08-30', '리뷰 123', 4);
insert into REVIEW_IMAGE_LIST(REVIEW_id, image_url) values (16618655171448795, '	https://mblogthumb-phinf.pstatic.net/MjAxOTA4MjdfMTQ3/MDAxNTY2OTEwNzcxODYz.zY4lJ7sbxCkOa8mE4ZSVRUpUbD4a6U-wTjkIX603fYcg.qDdV01hHy8EU1nZvaG_WPR4lHd_XIQurZvA0jm7pDVkg.JPEG.ynk_1204/1566909783835.jpg?type=w800');
insert into REVIEW_IMAGE_LIST(REVIEW_id, image_url) values (16618655171448795, 'https://mblogthumb-phinf.pstatic.net/MjAxOTA4MDJfMTQg/MDAxNTY0NzM0MDk1NjM3.NbQrTpzyrSLYj-8NxZsVK8concii0ShYe491wLv_Bjkg.BAkLQ1PmkSXscwGfzK2UIl2nEZf7yCmxOwsVR841twkg.JPEG.ynk_1204/2019-08-02-16-50-34.jpg?type=w800');

insert into review(review_id, PLACE_id, USER_id, post_date, post_text, rating) values(16618655171448796, 3, 'wonbinkim', '2022-08-30', '리뷰 123', 4);


INSERT INTO COMMENT(comment_id, REVIEW_id, USER_id, comment_text, comment_date) values(16618658070739908, 16610622302321234, 'wonbinkim', 'comment qqwrqwr', '2022-08-30 22:23:27.071');
INSERT INTO COMMENT(comment_id, REVIEW_id, USER_id, comment_text, comment_date) values(16618658070739909, 16610622302321234, 'wonbinkim', 'comment qqw12323r', '2022-08-30 22:35:27.071');

INSERT INTO COMMENT(comment_id, REVIEW_id, USER_id, comment_text, comment_date) values(16618655171448795, 16618655171448795, 'wonbinkim', 'comment 123132qqwrqwr', '2022-08-30 22:30:27.071');
INSERT INTO COMMENT(comment_id, REVIEW_id, USER_id, comment_text, comment_date) values(16618655171448796, 16618655171448795, 'wonbinkim', '123132qqwrqwr', '2022-08-30 22:30:27.071');

INSERT INTO COMMENT(comment_id, REVIEW_id, USER_id, comment_text, comment_date) values(16618655171448797, 16618655171448796, 'wonbinkim', '댓글댓글', '2022-08-30 22:30:27.071');
INSERT INTO COMMENT(comment_id, REVIEW_id, USER_id, comment_text, comment_date) values(16618655171448798, 16618655171448796, 'wonbinkim', '댓으르르 123132qqwrqwr', '2022-08-30 22:30:27.071');
INSERT INTO COMMENT(comment_id, REVIEW_id, USER_id, comment_text, comment_date) values(16618655171448799, 16618655171448796, 'wonbinkim', 'ㅇ눔너아ㅓ무', '2022-08-30 22:30:27.071');
