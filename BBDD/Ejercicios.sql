use football_manager;

/*
1 Consulta
Control entrenador únic --> Crea un trigger que impedeixi que un equip tingui més d’un entrenador actiu alhora, 
és a dir, només hi pot haver un entrenador sense data_baixa registrada a la taula 'entrenar_equips'. 
Mostra un missatge d'error amb la comanda SIGNAL.
*/

select * from entrenar_equips where data_baixa is null;

delimiter //

create trigger impedirMasEntrenadores
before insert on entrenar_equips
for each row
begin
	declare equipoAsignado integer;
	select equips_id into equipoAsignado from entrenar_equips where data_baixa is null;
    
	if equipoAsignado = new.equips_id then
		signal sqlstate '45000'
		set message_text = 'Error, ya hay un entrenador en este equipo';
    end if;
    
end //

delimiter ;

/*
2 Consulta
Control de capacitat dels estadis --> Crea un trigger que, en afegir o modificar un estadi, 
verifiqui que la capacitat (num_espectadors) sigui major de 5000 i menor de 100.000. 
En cas contrari, establir el valor mínim o màxim, segons correspongui.
*/

/*
3 Consulta
Control entrenador amb contracte vigent --> Crea un trigger que impedeixi que un entrenador que està actualment assignat a un equip (data_baixa nul·la) 
pugui ser assignat de nou sense tancar l’anterior relació. 
Mostra un missatge d'error amb la comanda SIGNAL.

És possible modificar el trigger per fer que si això es produeix, es finalitzi l'assignació actual (establint la data_baixa) abans de procedir amb la nova?
*/

/*
4 Consulta
Fes que cada vegada que s'afegeixen o es modifiquin jugadors i entrenadors, 
ens assegurem que el nom i cognoms s’emmagatzemen amb la primera lletra en majúscula i la resta en minúscula (ex: jOaN viDal → Joan Vidal)
*/

/*
5 Consulta
Crea un trigger que, abans d'assignar un nou jugador/a a un equip, 
comprovi que aquell equip no superi els 25 jugadors actius (sense data_baixa). 
En cas contrari, impedeix la inserció i mostra un missatge d’error amb SIGNAL.
*/

select count(*) from jugadors_equips where equips_id = 4;

delimiter //

create trigger asignarJugador
before insert on jugadors_equips
for each row
begin
	declare cantidadJugadores integer;
	select count(*) into cantidadJugadores from jugadors_equips where equips_id = new.equips_id;
    if cantidadJugadores > 25 and data_baixa is null then
    
    end if;
end //

delimiter ;

/*
6 Consulta
Crea un trigger que impedeixi programar dos equips per jugar entre ells més d’un cop en una mateixa jornada i lliga, 
independentment de qui és local o visitant. 
Si això passa, mostra un missatge d'error amb la comanda SIGNAL.
*/

/*
7 Consulta
De manera similar, crea un trigger que impedeixi inserir un nou partit si algun dels equips (local o visitant)
 ja té programat un altre partit en la mateixa jornada i lliga. 
 El trigger ha de validar tant el local com el visitant.
*/

/*
8 Consulta
Crea un trigger que, si detecta que s'ha incrementat el sou d'un jugador/a, 
registri el canvi a una taula canvis_sou_jugadors (que hauràs de crear) incloent: 
la id del jugador/a, el sou antic, el sou nou i la data del canvi.
*/

delimiter //

create trigger cambioSalarioJugador
after update on persones
for each row
begin
	
end //

delimiter ;

/*
9 Consulta
Crea una taula log_equips_modificats que enregistri qualsevol canvi de president d’un equip. 
Implementa un trigger que inserti automàticament un registre cada vegada que hi hagi una actualització. 
Cal enregistrar el nom de l'equip, l'anterior president, el nou president i la data en què s'ha produït el canvi.
*/

/*
10 Consulta
Control d'errors en inscripció de jornades --> 
Crea una taula log_errors_jornades per registrar intents fallits d'inserció a la taula jornades (p. ex. si es repeteix el número de jornada per una mateixa lliga). 
Afegeix un trigger per controlar-ho.
*/

/*
11 Consulta
Registre de jugadors eliminats --> Crea un trigger que, abans d’eliminar un jugador/a, 
insereixi les seves dades en una taula jugadors_eliminats, juntament amb la marca temporal del moment de l'esborrat.

Funciona el trigger en tots els casos? En quins casos no funciona i per què? 
*/

/*
12 Consulta
Volem garantir que la supressió d’un/a entrenador/a es faci de forma segura i auditada. 
Crea un procedure que, donat l’id d’un entrenador/a, faci les accions següents:

Si l’entrenador/a està actualment assignat/da a un equip, tanca la relació establint la data_baixa amb la data actual.

Registra les dades de l’entrenador/a a la taula entrenadors_eliminats (que haureu de crear), incloent: 
nom, cognoms, persones_id  i el nom de l’equip amb el qual estava vinculat, si n’hi havia. 
Si no en tenia cap, s’ha d’indicar "Sense equip vigent". També cal desar la marca temporal del moment de l’eliminació.

Elimina la fila corresponent de la taula entrenadors.

❗Aquest procedure ha de ser transaccional: si qualsevol pas falla, cap canvi no ha de quedar aplicat a la base de dades.
*/

/*
13 Consulta
Estadístiques d'un jugador/a --> Crea una procedure que mostri el nombre total de gols per un jugador/a donat (proporcioneu la id del jugador/a).
*/

/*
14 Consulta
Reassignar entrenador/a -->  Crea un procedure que, donat un identificador d’equip i un d’entrenador/a, 
finalitzi automàticament el contracte vigent (afegint la data_baixa) i en creï una nova relació amb l'equip indicat. 
El procedure ha de ser transaccional.
*/

/*
15 Consulta
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

/*
16 Consulta
Transferir jugador/a --> Crea un procedure que permeti transferir un jugador/a d’un equip a un altre. 
El sistema ha de verificar que el jugador/a i els dos equips existeixen i que el jugador/a està actualment vinculat al primer equip proporcionat per paràmetre. 
El procediment ha d'actualitzar la data de baixa a l'antic equip i inserir una nova alta al nou equip. 
Tot el procés ha de realitzar-se dins d’una transacció.
*/