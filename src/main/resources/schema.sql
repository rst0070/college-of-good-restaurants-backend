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
    password varchar(40) not null,
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
    longitude double not null
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
