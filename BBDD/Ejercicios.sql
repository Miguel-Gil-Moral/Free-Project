use football_manager;

/*
✅ 1 Consulta
Control entrenador únic --> Crea un trigger que impedeixi que un equip tingui més d’un entrenador actiu alhora, 
és a dir, només hi pot haver un entrenador sense data_baixa registrada a la taula 'entrenar_equips'. 
Mostra un missatge d'error amb la comanda SIGNAL.
*/

select * from entrenar_equips where data_baixa is null;
drop trigger impedirMasEntrenadores;

delimiter //

create trigger impedirMasEntrenadores
before insert on entrenar_equips
for each row
begin    
	if exists (select * from entrenar_equips where equips_id = new.equips_id and data_baixa is null) = new.equips_id then
		signal sqlstate '45000'
		set message_text = 'Error, ya hay un entrenador en este equipo';
    end if;
end //

delimiter ;

select * from entrenar_equips;
select * from persones;
select * from entrenadors;

insert into persones values (201, 'saludos', 'mundo', '2026-05-19', 0, 1, 'entrenador');
insert into entrenadors values (201, 0, 0);
insert into entrenar_equips values('2015-06-18', 201, 1, null);

/*
✅ 2 Consulta
Control de capacitat dels estadis --> Crea un trigger que, en afegir o modificar un estadi, 
verifiqui que la capacitat (num_espectadors) sigui major de 5000 i menor de 100.000. 
En cas contrari, establir el valor mínim o màxim, segons correspongui.
*/

delimiter //

create trigger capacidadEstadio
before insert on estadis
for each row
begin
	if new.num_espectadors < 5000 then
		set new.num_espectadors = 5000;
	elseif new.num_espectadors > 100000 then
		set new.num_espectadors = 100000;
    end if;
end //

delimiter ;

select * from estadis;

insert into estadis values (null, 'estadio de los atontaos', 1);

/*
✅ 3 Consulta
Control entrenador amb contracte vigent --> Crea un trigger que impedeixi que un entrenador que està actualment assignat a un equip (data_baixa nul·la) 
pugui ser assignat de nou sense tancar l’anterior relació. 
Mostra un missatge d'error amb la comanda SIGNAL.

És possible modificar el trigger per fer que si això es produeix, es finalitzi l'assignació actual (establint la data_baixa) abans de procedir amb la nova?
*/

drop trigger impedirAsignacion;

delimiter //

create trigger impedirAsignacion
before insert on entrenar_equips
for each row
begin
	if (select count(*) from entrenar_equips where entrenadors_id = new.entrenadors_id and data_baixa is null) > 0 then 
		signal sqlstate '45000'
        set message_text = 'Error, el entrenador esta ya en otro equipo';
    end if;
end //

delimiter ;

select * from persones;
select * from equips;
select * from entrenar_equips;

insert into entrenar_equips values (current_date(), 201, 1, null);

/*
✅ 4 Consulta
Fes que cada vegada que s'afegeixen o es modifiquin jugadors i entrenadors, 
ens assegurem que el nom i cognoms s’emmagatzemen amb la primera lletra en majúscula i la resta en minúscula (ex: jOaN viDal → Joan Vidal)
*/

select * from persones;

drop trigger modificarNombre;

delimiter //

create trigger modificarNombre
before insert on persones
for each row
begin
	set new.nom = concat(upper(left(new.nom, 1)), lower(substring(new.nom, 2)));
    set new.cognoms = concat(upper(left(new.cognoms, 1)), lower(substring(new.cognoms, 2)));
end //

delimiter ;

select * from persones;

insert into persones values(null, 'buenAS', 'dIAS', '2026-05-12', 100, 1, 'jugador');

/*
✅ 5 Consulta
Crea un trigger que, abans d'assignar un nou jugador/a a un equip, 
comprovi que aquell equip no superi els 25 jugadors actius (sense data_baixa). 
En cas contrari, impedeix la inserció i mostra un missatge d’error amb SIGNAL.
*/

select count(*) from jugadors_equips where equips_id = 1;

delimiter //

create trigger asignarJugador
before insert on jugadors_equips
for each row
begin
	declare cantidadJugadores int;
	select count(*) into cantidadJugadores from jugadors_equips where equips_id = new.equips_id and data_baixa is null;
    if cantidadJugadores >= 25 then
		signal sqlstate '45000'
        set message_text = 'Ya tienen 25 jugadores activos este equipo';
    end if;
end //

delimiter ;

/*
✅ 6 Consulta
Crea un trigger que impedeixi programar dos equips per jugar entre ells més d’un cop en una mateixa jornada i lliga, 
independentment de qui és local o visitant. 
Si això passa, mostra un missatge d'error amb la comanda SIGNAL.
*/

drop trigger impedirDosMismosPartidos;

delimiter //

create trigger impedirDosMismosPartidos
before insert on partits
for each row
begin
	declare partidoExiste int;
    select count(*) into partidoExiste from partits 
    where jornades_id = new.jornades_id 
    and (equips_id_local = new.equips_id_local and equips_id_visitant = new.equips_id_visitant) 
    or (equips_id_local = new.equips_id_visitant and equips_id_visitant = new.equips_id_local);
    
	if partidoExiste > 0 then
		signal sqlstate '45000'
        set message_text = 'No se puede hacer dos veces el mismo enfrentamiento';
    end if;
end //

delimiter ;

select * from partits;
insert into partits values(null, 3, 0, 3, 0, 1, 1, 38);
insert into partits values(null, 0, 0, 1, 1, 1, 38, 1);

/*
✅ 7 Consulta
De manera similar, crea un trigger que impedeixi inserir un nou partit si algun dels equips (local o visitant)
 ja té programat un altre partit en la mateixa jornada i lliga. 
 El trigger ha de validar tant el local com el visitant.
*/

drop trigger impedirInsertarPartidosIguales;

delimiter //

create trigger impedirInsertarPartidosIguales
before insert on partits
for each row
begin
	declare partidoExiste int;
    select count(*) into partidoExiste from partits 
    where jornades_id = new.jornades_id 
    and (equips_id_local = new.equips_id_local and equips_id_visitant = new.equips_id_visitant) 
    or (equips_id_local = new.equips_id_visitant and equips_id_visitant = new.equips_id_local);
    
	if partidoExiste > 0 then
		signal sqlstate '45000'
        set message_text = 'Uno de los equipos ya tiene un enfrentamiento en la misma jornada';
    end if;
end //

delimiter ;

/*
✅ 8 Consulta
Crea un trigger que, si detecta que s'ha incrementat el sou d'un jugador/a, 
registri el canvi a una taula canvis_sou_jugadors (que hauràs de crear) incloent: 
la id del jugador/a, el sou antic, el sou nou i la data del canvi.
*/

drop trigger cambioSalarioJugador;

delimiter //

create trigger cambioSalarioJugador
after update on persones
for each row
begin
	if old.sou != new.sou then
		insert into canvis_sou_jugadors values(new.id, old.sou, new.sou, current_date());
    end if;
end //

delimiter ;

select * from canvis_sou_jugadors;

update persones set nom = 'buenos' where nom = 'buenas';

/*
✅ 9 Consulta
Crea una taula log_equips_modificats que enregistri qualsevol canvi de president d’un equip. 
Implementa un trigger que inserti automàticament un registre cada vegada que hi hagi una actualització. 
Cal enregistrar el nom de l'equip, l'anterior president, el nou president i la data en què s'ha produït el canvi.
*/

drop trigger insertarRegistro;

select * from equips;

delimiter //

create trigger insertarRegistro
before update on equips
for each row
begin
	if old.nom_president != new.nom_president then
		insert into log_equips_modificats values(new.id, old.nom_president, new.nom_president, current_date());
	end if;
end //

delimiter ;

select * from log_equips_modificats;

insert into equips values(null, 'equipo de los atontaos', 2026, 'Pedro Sanchez', 1, 1, null);

update equips set nom_president = 'Pedro Sanchez' where nom = 'equipo de los atontaos';
update equips set nom_president = 'nulo' where nom = 'equipo de los atontaos';

/*
✅ 10 Consulta
Control d'errors en inscripció de jornades --> 
Crea una taula log_errors_jornades per registrar intents fallits d'inserció a la taula jornades (p. ex. si es repeteix el número de jornada per una mateixa lliga). 
Afegeix un trigger per controlar-ho.
*/

drop trigger registrarErroresJornadas;

delimiter //

create trigger registrarErroresJornadas
before insert on jornades
for each row
begin
	if exists (select * from jornades where jornada = new.jornada and lligues_id = new.lligues_id) then
		insert into log_errors_jornades values(null, 'Num jornada repetida', current_date());
	elseif new.data > current_date() then
		insert into log_errors_jornades values(null, 'Fecha adelantada', current_date());
	end if;
end //

delimiter ;

select * from jornades;
select * from log_errors_jornades;

insert into jornades values(null, 1, current_date(), 1);
insert into jornades values(null, 40, '2026-06-20', 1);

/*
✅ 11 Consulta
Registre de jugadors eliminats --> Crea un trigger que, abans d’eliminar un jugador/a, 
insereixi les seves dades en una taula jugadors_eliminats, juntament amb la marca temporal del moment de l'esborrat.

Funciona el trigger en tots els casos? En quins casos no funciona i per què? 
*/

drop trigger insertarJugadorEliminado;

delimiter //

create trigger insertarJugadorEliminado
after delete on persones
for each row
begin
	if old.tipus_persona = 'jugador' then
		insert into jugadors_eliminats values(old.id, old.nom, old.cognoms, old.data_naixement, old.nivell_motivacio, old.sou, current_date());
    end if;
end //

delimiter ;

select * from jugadors_eliminats;
select * from persones;

delete from persones where nom = 'buenas';

-- Procedures

/*
✅ 12 Consulta
Volem garantir que la supressió d’un/a entrenador/a es faci de forma segura i auditada. 
Crea un procedure que, donat l’id d’un entrenador/a, faci les accions següents:

Si l’entrenador/a està actualment assignat/da a un equip, tanca la relació establint la data_baixa amb la data actual.

Registra les dades de l’entrenador/a a la taula entrenadors_eliminats (que haureu de crear), incloent: 
nom, cognoms, persones_id  i el nom de l’equip amb el qual estava vinculat, si n’hi havia. 
Si no en tenia cap, s’ha d’indicar "Sense equip vigent". També cal desar la marca temporal del moment de l’eliminació.

Elimina la fila corresponent de la taula entrenadors.

❗Aquest procedure ha de ser transaccional: si qualsevol pas falla, cap canvi no ha de quedar aplicat a la base de dades.
*/

drop procedure supresionEntrenador;

delimiter //

create procedure supresionEntrenador(in entrenador_id int)
	begin
		declare nombre_entrenador varchar(45);
        declare apellido_entrenador varchar(45);
        declare nombre_equipo varchar(45);
        
		start transaction;
			if exists (select * from entrenar_equips where entrenadors_id = entrenador_id and data_baixa is null) then
		        select persones.nom, persones.cognoms, equips.nom 
				into nombre_entrenador, apellido_entrenador, nombre_equipo 
				from persones
				join entrenadors on persones.id = entrenadors.persones_id
				join entrenar_equips on entrenadors.persones_id = entrenar_equips.entrenadors_id
				join equips on entrenar_equips.equips_id = equips.id
                where persones.id = entrenador_id
                and data_baixa is null;
                
                if nombre_equipo is null then
					set nombre_equipo = 'Sin equipo vigente';
                end if;
                
                insert into entrenadors_eliminats values (entrenador_id, nombre_entrenador, apellido_entrenador, nombre_equipo, current_date());
                while exists (select 1 from entrenar_equips where entrenadors_id = entrenador_id) do 
					delete from entrenar_equips	where entrenadors_id = entrenador_id;
                end while;
                delete from entrenadors where persones_id = entrenador_id;
			end if;
        commit;
        
    end //

delimiter ;

call supresionEntrenador(201);

select * from entrenadors_eliminats;
select * from entrenar_equips where entrenadors_id = 201;
select * from entrenadors;

/*
✅ 13 Consulta
Estadístiques d'un jugador/a --> Crea una procedure que mostri el nombre total de gols per un jugador/a donat (proporcioneu la id del jugador/a).
*/

drop procedure mostrarNumeroGolesJugador;

delimiter //

create procedure mostrarNumeroGolesJugador (in jugador_id int, out numeroGoles int)
	begin
		select count(jugadors_id) into numeroGoles from partits_gols where jugadors_id = jugador_id;
    end //

delimiter ;

select * from partits_gols where jugadors_id = 110;

CALL mostrarNumeroGolesJugador(110, @goles);

SELECT @goles;

/*
✅ 14 Consulta
Reassignar entrenador/a -->  Crea un procedure que, donat un identificador d’equip i un d’entrenador/a, 
finalitzi automàticament el contracte vigent (afegint la data_baixa) i en creï una nova relació amb l'equip indicat. 
El procedure ha de ser transaccional.
*/

drop procedure reasignarEntrenador;

delimiter //

create procedure reasignarEntrenador (in equip_id int, in entrenador_id int)
	begin
		start transaction;
			update entrenar_equips set data_baixa = current_date() where entrenadors_id = entrenador_id;
            
            insert into entrenar_equips values(current_date(), entrenador_id, equip_id, null);        
        commit;
    end//

delimiter ;

select * from equips;
select * from entrenar_equips;

call reasignarEntrenador(39, 201);

/*
✅ 15 Consulta
Estadística de gols per jugador/a --> Fes un procedure que rebi el nom d'una lliga, 
calculi quants jugadors han fet més de 10, 20, i 30 gols, 
i insereixi la informació en una nova taula golejadors, amb la següent estructura:

CREATE TABLE golejadors (

  id INT AUTO_INCREMENT PRIMARY KEY,

  categoria VARCHAR(20), -- Ex: '+10 gols'

  total_jugadors INT,

  lliga VARCHAR(100),

  data_calcul TIMESTAMP

);
*/

drop procedure estadisticasGolsJugador;

delimiter //

create procedure estadisticasGolsJugador(in nombre_liga varchar(45))
	begin
		declare numeroGoles int;
        
		select count(*)
        into numeroGoles
		from (
			select pg.jugadors_id
			from partits_gols pg
			join partits p
				on pg.partits_id = p.id
			join jornades j
				on p.jornades_id = j.id
			join lligues l
				on j.lligues_id = l.id
			where l.nom = nombre_liga
			group by pg.jugadors_id
			having count(*) >= 10
		) t;
        insert into golejadors values(null, '+10 gols', numeroGoles, nombre_liga, current_timestamp());
        
        select count(*)
        into numeroGoles
		from (
			select pg.jugadors_id
			from partits_gols pg
			join partits p
				on pg.partits_id = p.id
			join jornades j
				on p.jornades_id = j.id
			join lligues l
				on j.lligues_id = l.id
			where l.nom = nombre_liga
			group by pg.jugadors_id
			having count(*) >= 20
		) t;
        insert into golejadors values(null, '+20 gols', numeroGoles, nombre_liga, current_timestamp());
        
        select count(*)
        into numeroGoles
		from (
			select pg.jugadors_id
			from partits_gols pg
			join partits p
				on pg.partits_id = p.id
			join jornades j
				on p.jornades_id = j.id
			join lligues l
				on j.lligues_id = l.id
			where l.nom = nombre_liga
			group by pg.jugadors_id
			having count(*) >= 30
		) t;
        insert into golejadors values(null, '+30 gols', numeroGoles, nombre_liga, current_timestamp());
		
    end //

delimiter ;

call estadisticasGolsJugador('La Liga EA Sports');

select * from golejadors;

select count(*) from partits_gols
join partits on partits_gols.partits_id = partits.id
join jornades on partits.jornades_id = jornades.id
join lligues on jornades.lligues_id = lligues.id
where partits_id = (
	select count(*) from partits_gols
);

select count(*), count(partits_id) 'golesMarcados' from partits_gols
join partits on partits_gols.partits_id = partits.id
join jornades on partits.jornades_id = jornades.id
join lligues on jornades.lligues_id = lligues.id
group by partits_id
having golesMarcados >= 20;

select jugadors_id, count(partits_id) 'numeroGoles' from partits_gols 
join partits on partits_gols.partits_id = partits.id
join jornades on partits.jornades_id = jornades.id
join lligues on jornades.lligues_id = lligues.id
group by jugadors_id
having numeroGoles >= 20
and jugadors_id = (
	select count(*) from partits_gols
);

select * from partits_gols where jugadors_id = 22;

select count(jugadors_id) from partits_gols;

select jugadors_id, count(*) 'Numero_goles'from partits_gols group by jugadors_id having Numero_goles > 20;

/*
✅ 16 Consulta
Transferir jugador/a --> Crea un procedure que permeti transferir un jugador/a d’un equip a un altre. 
El sistema ha de verificar que el jugador/a i els dos equips existeixen i que el jugador/a està actualment vinculat al primer equip proporcionat per paràmetre. 
El procediment ha d'actualitzar la data de baixa a l'antic equip i inserir una nova alta al nou equip. 
Tot el procés ha de realitzar-se dins d’una transacció.
*/

drop procedure transferirJugador;

delimiter //

create procedure transferirJugador (in jugador_id int, in equip1_id int, in equip2_id int)
	begin
		declare equipo1Existe int;
		declare equipo2Existe int;
		start transaction;
        
			select count(*) into equipo1Existe from equips where id = equip1_id;
            select count(*) into equipo2Existe from equips where id = equip2_id;
        
			if equipo1Existe > 0 and equipo2Existe > 0 then
				update jugadors_equips set data_baixa = current_date() where jugadors_id = jugador_id and equips_id = equip1_id;
				insert into jugadors_equips values(current_date(), jugador_id, equip2_id, null);
                else rollback;
			end if;
    end //

delimiter ;

select * from jugadors_equips;

call transferirJugador (145, 34, 30);