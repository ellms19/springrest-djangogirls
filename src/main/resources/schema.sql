drop table if exists comment;
drop table if exists post;
drop table if exists "user";

create table "user"(
    id serial primary key,
    firstname varchar(50) not null,
    lastname varchar(50) not null,
    email varchar(50) not null,
    password varchar(200) not null
);

create table post(
    id serial primary key,
    author integer references "user"(id),
    title varchar(200) not null,
    text text not null,
    created_date timestamp not null,
    published_date timestamp
);

create table comment(
    id serial primary key,
    post integer references post(id),
    author varchar(100) not null,
    text text not null,
    created_date timestamp not null,
    approved_comment boolean not null
);

--select * from inc inner join emp on inc.empid=emp.id
-- inner join (select empid, max(amount) as maxinc, count(amount) as totalinc from inc group by empid)
-- as t on inc.empid=t.empid and inc.amount = t.maxinc;