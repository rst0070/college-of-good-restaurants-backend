create table COLLEGE(
	college_id int primary key auto_increment,
    college_name varchar(50) not null,
    distance_limit_km double not null default 5,
    latitude double not null,
    longitude double not null
);

create table COLLEGE_MAIL_DOMAIN(
	COLLEGE_id int primary key,
    college_mail_domain varchar(320) not null
);

alter table college_mail_domain
	add constraint link_to_college_table
    foreign key (COLLEGE_id) references COLLEGE (college_id);
    
create table USER(
	user_id varchar(20) primary key,
    password varchar(60) not null,
    nickname varchar(20) not null,
    college_email_address varchar(320) not null unique,
    COLLEGE_id int not null,
    constraint link_to_college_entity foreign key (COLLEGE_id) references COLLEGE (college_id),
    constraint unique_nickname_by_college_id unique(COLLEGE_id, nickname)
);

create table PLACE(
	place_id int primary key auto_increment,
    place_name varchar(50) not null,
    place_address varchar(100) not null,
    latitude double not null,
    longitude double not null,
    phone varchar(20),
    constraint place_unique_by_address_and_name unique (place_address, place_name)
);

create table PLACE_REGISTRANT(
    PLACE_id int primary key,
    USER_id varchar(20),
    foreign key (PLACE_id) references PLACE(place_id),
    foreign key (USER_id) references USER(user_id)
);

create table PLACE_LIST_AT_COLLEGE(
	COLLEGE_id int,
    PLACE_id int,
    primary key (COLLEGE_id, PLACE_id)
);

alter table place_list_at_college
	add constraint link_to_college_from_place_list
    foreign key (COLLEGE_id) references COLLEGE (college_id);
    
alter table place_list_at_college
	add constraint link_to_place_from_place_list
    foreign key (PLACE_id) references PLACE (place_id);
    
create table KAKAO_PLACE(
	PLACE_id int primary key,
    kakao_place_id tinytext not null,
    category tinytext not null,
    place_image_url varchar(2000),
    foreign key (PLACE_id) references PLACE (place_id)
);

create table EMAIL_AUTH_CODE(
	email_addr varchar(320) primary key,
    auth_code varchar(8) not null,
    exp_date datetime not null
);

create table REVIEW(
    review_id bigint primary key,
	PLACE_id int,
    USER_id varchar(20),
    post_date date not null,
    post_text text not null,
    rating int,
    constraint review_rating_check check( rating in (1, 2, 3, 4, 5)),
    foreign key (PLACE_id) references PLACE(place_id),
    foreign key (USER_id) references USER(user_id)
);

create table REVIEW_IMAGE_LIST(
    REVIEW_id bigint,
    image_url varchar(1000),
    foreign key (REVIEW_id) references REVIEW(review_id),
    primary key (REVIEW_id, image_url)
);

create table COMMENT(
    comment_id bigint primary key,
    REVIEW_id bigint,
    USER_id varchar(20),
    comment_text text not null,
    comment_date datetime,
    primary key (comment_id),
    foreign key (REVIEW_id) references REVIEW(review_id),
    foreign key (USER_id) references USER(user_id)
);

create table PLACE_LIKE(
    PLACE_id int,
    USER_id varchar(20),
    like_date date not null,
    primary key (PLACE_id, USER_id),
    foreign key (PLACE_id) references PLACE(place_id),
    foreign key (USER_id) references USER(user_id)
);

create table JWT(
    jwt_id long primary key,
    user_id varchar(20),
    jwt text
);


