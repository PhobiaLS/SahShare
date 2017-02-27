/*Kreiranje baze*/

create schema chessbase;
/*Kreiranje tabele korisnika*/
CREATE TABLE `chessbase`.`user` (
  `idUser` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(45) NOT NULL,
  `userPassword` varchar(45) NOT NULL,
  `userRating` int(11) NOT NULL DEFAULT '1000',
  `userEmail` varchar(45) NOT NULL,
  `userAvatar` varchar(100) NOT NULL DEFAULT 'default',
  PRIMARY KEY (`idUser`),
  UNIQUE KEY `userName_UNIQUE` (`userName`),
  UNIQUE KEY `userEmail_UNIQUE` (`userEmail`)
);
/*Kreiranje tabele za prijatelje*/
CREATE TABLE `chessbase`.`friends` (
  `idFriend1` INT NOT NULL,
  `idFriend2` INT NOT NULL,
  PRIMARY KEY (`idFriend1`, `idFriend2`)
 );
 
/*Kreiranje korisnika*/ 
create user 'chessUsername'@'localhost' identified by 'chessPassword';
 
grant select, insert, update, delete, create, create view, drop,execute, references on *.* to 'chessUsername'@'localhost';

grant all privileges on *.* to 'chessUsername'@'%' identified by 'chessPassword';