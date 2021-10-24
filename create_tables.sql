create table coordinates(
                            id serial primary key,
                            x int,
                            y real
);

create table human(
                      id serial primary key,
                      name text not null,
                      age bigint not null,
                      height bigint not null,
                      birthday date
);


create table city(
                     id bigserial primary key,
                     name text not null,
                     coordinates_id int references coordinates(id),
                     creation_date date not null,
                     area bigint,
                     population int not null,
                     meters_above_sea bigint not null,
                     establishment_date date,
                     agglomeration real not null,
                     climate text,
                     governor_id int references human(id),
                     owner_id int references users(id),
                     owner_name text
);

create table users(name text,
                      hash text,
                      salt text
);
