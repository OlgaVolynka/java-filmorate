insert into mpa  (mpa_id, mpa_name) values (1,'G');
insert into mpa (mpa_id, mpa_name) values (2,'PG');
insert into mpa (mpa_id, mpa_name) values (3,'PG-13');
insert into mpa (mpa_id, mpa_name) values (4,'R');
insert into mpa (mpa_id, mpa_name) values ( 5,'NC-17');
merge into  genres key ( id)  values (1,'Комедия' );
merge into genres key ( id) values (2, 'Драма' );
merge into genres key ( id) values ( 3,'Мультфильм' );
merge into genres key ( id) values (4,'Триллер' );
merge into genres key ( id) values (5,'Документальный' );
merge into genres key ( id) values (6, 'Боевик' );

--insert into mpa (id, mpa_name) values (1, 'G');
--insert into mpa (id, mpa_name) values (2, 'PG');
--insert into mpa (id, mpa_name) values ( 3, 'PG-13' );
--insert into mpa (id, mpa_name) values ( 4, 'R' );
--insert into mpa (id, mpa_name) values ( 5, 'NC-17' );
