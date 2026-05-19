drop database if exists tetris;
create database tetris;

use tetris;

create table usuario(
	id int auto_increment,
    nombre varchar(100) not null,
    contrasenya varchar(100) not null,
    
    constraint pk_usuario primary key(id)
);

create table partida (
	id_partida int auto_increment,
    puntos int not null,
    nivel int not null,
    horas time not null,
    id_usuario int not null,
    
    constraint pk_partida primary key(id_partida),
    constraint fk_partida_usuario foreign key (id_usuario) references usuario(id)
);

select * from partida;

select usuario.nombre, max(p.puntos), sec_to_time(sum(p.horas)) from partida p join usuario on p.id_usuario = usuario.id where p.id_usuario = 1 group by usuario.nombre;