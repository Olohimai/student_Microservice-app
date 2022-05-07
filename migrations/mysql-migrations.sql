SET GLOBAL general_log_file = '/var/log/mysql/mariadb.log';
SET GLOBAL general_log = 1;

drop schema if exists student;
create schema student;
use student;

create table course
(
    id          bigint auto_increment primary key,
    description longtext     null,
    fee         double       null,
    title       varchar(255) null
);

create table portal_user
(
    id        bigint auto_increment primary key,
    email     varchar(45) not null,
    password  varchar(64) not null,
    role      int         null,
    user_name varchar(20) not null,
    constraint UK_9eg0sqxgl1lcy1pmnu6mta5vw unique (user_name),
    constraint UK_npmhp9oynwc4rdjxg20jl0nt3 unique (email)
);

create table student
(
    id         bigint auto_increment primary key,
    forename   varchar(255) null,
    student_id varchar(255) null,
    surname    varchar(255) null,
    constraint UK_lh7am6sc9pv0nhyg7qkj7w5d3 unique (student_id)
);

create table enrolment
(
    course_id  bigint not null,
    student_id bigint not null,
    primary key (course_id, student_id),
    constraint FKqnrv6xltxnx61nfjoe2sngny4 foreign key (course_id) references course (id),
    constraint FKquem30hnspsnegde2q2bhvou foreign key (student_id) references student (id)
);

create table portal_user_student
(
    student_id bigint null,
    user_id    bigint not null primary key,
    constraint FKdw32ami43fm7ttefy8bw0nsb4 foreign key (student_id) references student (id),
    constraint FKrp3bw0f99i3nor9f1qkcyq77t foreign key (user_id) references portal_user (id)
);

INSERT INTO student.student (id, forename, student_id, surname)
VALUES (1, 'Olo', 'c3781247', 'Akalumhe');
INSERT INTO student.student (id, forename, student_id, surname)
VALUES (2, 'Glory', 'c3922382', 'Williams');

INSERT INTO student.portal_user (id, email, password, role, user_name)
VALUES (1, 'oloakalumhe@gmail.com', '$2a$10$VAe0TshcDRpQONvzXn8.le/7e1UktcoUtnSfBNaicitxSBcvMybD.', 0, 'oloakalumhe');

INSERT INTO student.portal_user (id, email, password, role, user_name)
VALUES (2, 'glorywilliams@gmail.com', '$2a$10$K4V3sCLlwg89MeXhqnqxAeVUECnOJHV.Vr6n09UCg03gEed/Q6f06', 0, 'glorywilliams');

INSERT INTO student.portal_user_student (student_id, user_id)
VALUES (1, 1);

INSERT INTO student.portal_user_student (student_id, user_id)
VALUES (2, 2);

INSERT INTO student.course (id, description, fee, title)
VALUES (1, 'The module provides an understanding of Cloud Computing technologies and software processes and methods for Cloud Computing ', 1400,
        'Cloud Computing Development ');
INSERT INTO student.course (id, description, fee, title)
VALUES (2, 'This module is to provide the student with the necessary skills to be a practising software developer.', 1300,
        'Advanced Software Engineering ');
INSERT INTO student.course (id, description, fee, title)
VALUES (3, 'The Research Practice (RP) module is the foundation for the whole of your Master’s Degree, covering the core skills of research and critical analysis..', 1200,
        'Research Practice');
INSERT INTO student.course (id, description, fee, title)
VALUES (4, 'This module content aims to equip students with an insight into the challenges of managing projects in practice',4000,
        'Project Management ');
INSERT INTO student.course (id, description, fee, title)
VALUES (5, 'This module provides an in-depth look at the Service-Oriented Architecture paradigm and, more specifically, at its recent development: Microservices. ',5000,
        'Software Engineering forService Computing');
INSERT INTO student.course (id, description, fee, title)
VALUES (6, 'The Dissertation forms an important element of the MSc courses, and must be passed to obtain the MSc degree. ', 7000,
        'Software Engineering forService Computing');
INSERT INTO student.course (id, description, fee, title)
VALUES (7, 'Learn about the fundamental principles and approaches for Intelligent Systems, autonomous behaviour, sensing and control, through the practical example of a simple robotic device (Delta Robot). You''ll have opportunity to work practically with the robot and develop software for simple behavioural and reaction patterns of robotic devices.', 725,
        'Dissertation');


INSERT INTO student.enrolment (course_id, student_id)
VALUES (1, 2);
INSERT INTO student.enrolment (course_id, student_id)
VALUES (3, 1);
INSERT INTO student.enrolment (course_id, student_id)
VALUES (4, 1);

DROP USER IF EXISTS 'student-spring-user'@'%';
CREATE USER 'student-spring-user'@'%' IDENTIFIED BY 'student-secret';
GRANT ALL PRIVILEGES on student.* to `student-spring-user`;
FLUSH PRIVILEGES;
