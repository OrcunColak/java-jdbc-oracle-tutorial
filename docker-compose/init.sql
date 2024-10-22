CREATE USER sa IDENTIFIED BY my1passw;

GRANT connect, resource, dba to sa;

ALTER SESSION SET current_schema = sa;

CREATE TABLE all_char_types_table (
    char_column char(5),
    varchar2_column varchar2(20),
    varchar_column varchar(15),
    nchar_column nchar(3),
    nvarchar2_column nvarchar2(10)
);

CREATE TABLE all_clob_types_table (
    clob_column clob,
    nclob_column nclob
);

-- binary blobs
CREATE TABLE all_blob_types_table (
    raw_column raw(100),
    blob_column blob,
    bfile_column bfile
);

CREATE TABLE bit_types_table (
    bit_column number(1)
);

CREATE TABLE boolean_types_table (
    id  NUMBER NOT NULL ENABLE,
    active BOOLEAN DEFAULT NULL
);

-- BOOLEAN = TRUE OR FALSE
INSERT INTO boolean_types_table (id, active) VALUES (1, TRUE);
INSERT INTO boolean_types_table (id, active) VALUES (2, FALSE);
-- BOOLEAN = 1 OR 0
INSERT INTO boolean_types_table (id, active) VALUES (3, 1);
INSERT INTO boolean_types_table (id, active) VALUES (4, 0);
-- BOOLEAN = 'Y' OR 'N'
INSERT INTO boolean_types_table (id, active) VALUES (5, 'Y');
INSERT INTO boolean_types_table (id, active) VALUES (6, 'N');


CREATE TABLE integer_types_table (
    tinyint_column number(3),
    smallint_column smallint,
    integer_column integer,
	int_column int,
    bigint_column number(19)
);

CREATE TABLE all_numeric_types_table (
   binaryfloat_column binary_float,
   binarydouble_column binary_double,
   double_column float,
   real_column real,
   decimal_column decimal
);

CREATE TABLE datetime_types_table (
    date_column DATE,

    -- does not store any time zone information. it stores the exact date and time you provide without any reference to a time zone
    timestamp_column TIMESTAMP(6),
    -- time zone or offset is stored, displayed with original time zone
    timestamp_with_time_zone_column TIMESTAMP WITH TIME ZONE,

    -- converted to database time zone, displayed in the session's local time zone
    -- Use when users in different zones need local time display
    timestamp_with_local_time_zone_column TIMESTAMP WITH LOCAL TIME ZONE,
    interval_column INTERVAL DAY TO SECOND
);