-- BEGIN SECTION 1 --------------------------------------
create table EMPLOYEE
(
  ID        NUMBER not null,
  EMPLOYEE_NUMBER VARCHAR2(100),
  NAME      VARCHAR2(200),
  SURNAME   VARCHAR2(200),
  AGE	NUMBER not null,
  PRIMARY KEY (ID)
);

create table TEMP_TABLE 
(
  ID        NUMBER not null,
  NAME      VARCHAR2(200)
);

-- END SECTION 1 ----------------------------------------


-- BEGIN SECTION 2 --------------------------------------
-- Create table
create table PEOPLE
(
  id          number not null,
  firstname        varchar2(100),
  lastname     varchar2(100),
  birthdate timestamp(6),
  deathdate date,
  firstblob	BLOB ,
  secondblob BLOB, 
  firstclob CLOB
);

alter table PEOPLE  add constraint IDX_PEOPLE primary key (ID);

create sequence SEQ_PEOPLE start with 1 increment by 1;

-- Create table
create table BLOBCLOB
(
  id          number not null,
  blob	BLOB ,
  clob CLOB
);

alter table BLOBCLOB add constraint IDX_BLOBCLOB primary key (ID);

create sequence SEQ_BLOBCLOB start with 1 increment by 1;
-- END SECTION 2 ----------------------------------------


-- BEGIN SECTION 4 --------------------------------------
-- Create schema 
create schema ZOO;

-- Create table
create table ZOO.PEOPLE
(
  id          number not null,
  firstname        varchar2(100),
  lastname     varchar2(100),
  birthdate timestamp(6),
  deathdate date,
  firstblob	BLOB ,
  secondblob BLOB, 
  firstclob CLOB
);

alter table ZOO.PEOPLE  add constraint ZOO_IDX_PEOPLE primary key (ID);

create sequence ZOO_SEQ_PEOPLE start with 1 increment by 1;

-- END SECTION 4 ----------------------------------------


-- BEGIN SECTION 5 --------------------------------------

-- Create table with autogenerated pk
create table AUTO_ID
(
  ID INTEGER GENERATED BY DEFAULT AS IDENTITY (start with 100),
  VALUE varchar2(100),
  PRIMARY KEY (ID)
);

-- END SECTION 5 ----------------------------------------


-- BEGIN SECTION 8 --------------------------------------
-- aggregated beans

create table USERS
(
  ID INTEGER GENERATED BY DEFAULT AS IDENTITY,
  FIRSTNAME      VARCHAR2(200) not null,
  LASTNAME   VARCHAR2(200) not null,
  VERSION NUMBER not null
);

create table USER_COUNTRY
(
  ID INTEGER GENERATED BY DEFAULT AS IDENTITY,
  NAME VARCHAR2(200) not null
);

create table USER_ADDRESS
(
  USER_ID INTEGER not null,
  COUNTRY_ID INTEGER not null,
  FOREIGN KEY (USER_ID) REFERENCES USERS (ID),
  FOREIGN KEY (COUNTRY_ID) REFERENCES USER_COUNTRY (ID)
);

create table USER_JOB
(
  ID INTEGER GENERATED BY DEFAULT AS IDENTITY,
  USER_ID INTEGER not null,
  NAME VARCHAR2(200),
  FOREIGN KEY (USER_ID) REFERENCES USERS (ID)
);

create table USER_JOB_TASK
(
	ID INTEGER GENERATED BY DEFAULT AS IDENTITY,
	USER_JOB_ID INTEGER not null,
	NAME VARCHAR2(200),
	FOREIGN KEY (USER_JOB_ID) REFERENCES USER_JOB (ID)
);

-- BEGIN SECTION 8 --------------------------------------
commit;
