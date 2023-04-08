drop table if exists gift_certificates_tags;
drop table if exists gift_certificates;
drop table if exists tags;

create table if not exists gift_certificates (
    id bigserial not null,
    name varchar(255) not null,
    description varchar(255) not null,
    price numeric(38,2) not null,
    duration numeric(21,0) not null,
    create_date timestamp(6) not null,
    last_update_date timestamp(6) not null,
    primary key (id)
);

create table if not exists tags (
    id bigserial not null,
    name varchar(255) not null,
    primary key (id)
);

create table if not exists gift_certificates_tags (
    gift_certificate_id bigint not null,
    tag_id bigint not null
);

alter table if exists gift_certificates_tags
    add constraint fk_gift_certificates_tags_tags
        foreign key (tag_id) references tags;

alter table if exists gift_certificates_tags
    add constraint fk_gift_certificates_tags_gift_certificates
        foreign key (gift_certificate_id) references gift_certificates;
