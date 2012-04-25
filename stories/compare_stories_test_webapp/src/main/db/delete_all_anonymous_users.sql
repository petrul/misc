delete from User where `login` like 'anonymous%';
delete from InsertionResult where `user` like 'anonymous%';
delete from Event where `user` like 'anonymous%';

