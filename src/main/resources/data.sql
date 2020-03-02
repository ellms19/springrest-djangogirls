delete from post;
delete from "user";
insert into "user"(firstname, lastname, email, password)
                    values('Name', 'Surname', 'name.surname@mail.domain', 'hashedpassword');
