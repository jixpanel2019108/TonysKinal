drop database if exists DBTonysKinal2019108;
Create database DBTonysKinal2019108;
use DBTonysKinal2019108;

########################################## CREACION DE ENTIDADES ##########################################
create table Empresas(
	codigoEmpresa int not null auto_increment,
	nombreEmpresa Varchar(150) not null,
	direccion varchar(150) not null,
	telefono varchar(10) not null,
	primary key PK_codigoEmpresa (codigoEmpresa)
);

create table TipoEmpleado(
	codigoTipoEmpleado int not null auto_increment,
	descripcion varchar(100) not null,
	primary key PK_codigoTipoEmpleado (codigoTipoEmpleado)
);

create table TipoPlato(
	codigoTipoPlato int not null auto_increment,
	descripcionTipoPlato varchar(100) not null,
	primary key PK_codigoTipoPlato (codigoTipoPlato)
);

create table Productos(
	codigoProducto int not null auto_increment,
	nombreProducto varchar(150) not null,
	cantidad int not null,
	primary key PK_codigoProducto (codigoProducto)
);

create table Presupuesto(
	codigoPresupuesto int not null auto_increment,
	fechaSolicitud Date not null,
	cantidadPresupuesto decimal(10,2) not null,
	codigoEmpresa int null,
	primary key PK_codigoPresupuesto (codigoPresupuesto),
	constraint FK_Presupuesto_Empresas foreign key (codigoEmpresa) references Empresas(codigoEmpresa) on delete cascade
);

create table Servicios(
	codigoServicio int not null auto_increment,
	fechaServicio date not null,
	tipoServicio varchar(100) not null,
	horaServicio time not null,
	lugarServicio Varchar(100) not null,
	telefonoContacto varchar(10) not null,
	codigoEmpresa int null,
	primary key PK_codigoServicio (codigoServicio),
	constraint FK_Servicios_Empresas foreign key (codigoEmpresa) references Empresas(codigoEmpresa) on delete cascade
);
#1#drop table Empleados;
create table Empleados(
	codigoEmpleado int not null auto_increment,
	numeroEmpleado int not null,
	apellidosEmpleado varchar(150) not null,
	nombresEmpleado varchar(150) not null,
	direccionEmpleado varchar(150) not null,
	telefonoContacto varchar(10) not null,
	gradoCocinero varchar(50),
	codigoTipoEmpleado int not null,
	primary key PK_codigoEmpleado (codigoEmpleado),
	constraint FK_Empleados_TipoEmpleado foreign key (codigoTipoEmpleado) REFERENCES TipoEmpleado(codigoTipoEmpleado) on delete cascade
);

create table Platos(
	codigoPlato int not null auto_increment,
	cantidad int not null,
	nombrePlato varchar(50) not null,
	descripcionPlato varchar(150) not null,
	precioPlato decimal(10,2) not null,
	codigoTipoPlato int null,
	primary key PK_codigoPlato (codigoPlato),
	constraint FK_Platos_TipoPlato foreign key (codigoTipoPlato) references TipoPlato(codigoTipoPlato) on delete cascade
);

create table Servicios_has_Platos(
	Servicios_codigoServicio int,
	Platos_codigoPlato int,
	constraint FK_Servicios_has_Platos_Servicios foreign key (Servicios_codigoServicio) references Servicios(codigoServicio) on delete cascade,
	constraint FK_Servicios_has_Platos_Platos foreign key (Platos_codigoPlato) references Platos(codigoPlato) on delete cascade
);

create table Productos_has_Platos(
	Productos_codigoProducto int,
	Platos_codigoPlato int,
	constraint FK_Productos_has_Platos_Productos foreign key (Productos_codigoProducto) references Productos(codigoProducto) on delete cascade,
	constraint FK_Productos_has_Platos_Platos foreign key (Platos_codigoPlato) references Platos(codigoPlato) on delete cascade
);

create table Servicios_has_Empleados(
	codigoServiciosHasEmpleados int not null auto_increment,
	codigoServicio int,
	codigoEmpleado int,
	fechaEvento date not null,
	horaEvento time not null,
	lugarEvento Varchar(150) not null,
    primary key PK_codigoServiciosHasEmpleados(codigoServiciosHasEmpleados),
	constraint FK_Servicios_has_Empleados_Servicios foreign key (codigoServicio) references Servicios(codigoServicio) on delete cascade,
	constraint FK_Servicios_has_Empleados_Empleados foreign key (codigoEmpleado) references Empleados(codigoEmpleado) on delete cascade
);



#----------------------------------Procedimientos de Empresas------------------------------------------#
delimiter $$
create procedure sp_ListarEmpresas ()
	begin
		Select Empresas.codigoEmpresa, 
				Empresas.nombreEmpresa, 
				Empresas.direccion, 
				Empresas.telefono
		from Empresas;
	end $$
delimiter ;

delimiter $$
create procedure sp_AgregarEmpresa(in nombreEmpresa varchar(150), in direccion varchar(150), in telefono varchar(10))
	begin
			insert into Empresas (nombreEmpresa, direccion, telefono) 
			values (nombreEmpresa, direccion, telefono);
	end $$
delimiter ;

delimiter $$
create procedure sp_EditarEmpresa(in codEmpresa int, in nomEmpresa varchar(150), in Ddireccion varchar(150), in tel varchar(10))
	begin
		Update Empresas
		set
			nombreEmpresa = nomEmpresa, 
			direccion = Ddireccion, 
			telefono = tel
			where codigoEmpresa=codEmpresa;
	end $$
delimiter ;

delimiter $$
create procedure sp_EliminarEmpresa(in idBuscar int)
	begin
		delete from Empresas
		where idBuscar = codigoEmpresa;
	end $$
delimiter ;

delimiter $$
create procedure sp_BuscarEmpresa(in idEmp int)
	begin
		select Empresas.codigoEmpresa,Empresas.nombreEmpresa, Empresas.direccion, Empresas.telefono  
		from Empresas 
		where idEmp = codigoEmpresa;
	end $$
delimiter ;

#----------------------------------Procedimientos de TipoEmpleado------------------------------------------#

delimiter $$
create procedure sp_ListarTipoEmpleados ()
	begin
		Select 	TipoEmpleado.codigoTipoEmpleado, 
				TipoEmpleado.descripcion
		from TipoEmpleado;
	end $$
delimiter ;

delimiter $$
create procedure sp_AgregarTipoEmpleado(in descripcionx varchar(100))
	begin
		insert into TipoEmpleado (descripcion) 
		values (descripcionx);
	end $$
delimiter ;

delimiter $$
create procedure sp_EditarTipoEmpleado(in codTipoEmpleado int,in Ddescripcion varchar(100))
	begin
		Update TipoEmpleado
		set
			descripcion = Ddescripcion
		where codigoTipoEmpleado=codTipoEmpleado;
	end $$
delimiter ;

delimiter $$
create procedure sp_EliminarTipoEmpleado(in idBuscar int)
begin
	delete from TipoEmpleado
    where codigoTipoEmpleado=idBuscar;
end $$
delimiter ;

delimiter $$
create procedure sp_BuscarTipoEmpleado(in idTipEmp int)
	begin
		select TipoEmpleado.codigoTipoEmpleado, 
		TipoEmpleado.descripcion 
		from TipoEmpleado 
		where idTipEmp = codigoTipoEmpleado;
	end $$
delimiter ;
#----------------------------------Procedimientos de TipoPlato------------------------------------------#
delimiter $$
create procedure sp_ListarTipoPlato ()
	begin
		Select 	TipoPlato.codigoTipoPlato, 
				TipoPlato.descripcionTipoPlato
		from TipoPlato;
	end $$
delimiter ;

delimiter $$
create procedure sp_AgregarTipoPlato(in descripcionTipo varchar(100))
	begin
		insert into TipoPlato(descripcionTipoPlato) 
		values (descripcionTipo);
	end $$
delimiter ;

delimiter $$
create procedure sp_EditarTipoPlato(in codTipoPlato int, in descripcionTipo varchar(100))
	begin
		Update TipoPlato
		set
			descripcionTipoPlato = descripcionTipo
		where codigoTipoPlato=codTipoPlato;
	end $$
delimiter ;

delimiter $$
create procedure sp_EliminarTipoPlato(in idBuscar int)
	begin
		delete from TipoPlato
		where codigoTipoPlato=idBuscar;
	end $$
delimiter ;

delimiter $$
create procedure sp_BuscarTipoPlato(in idTipPl int)
	begin
		select 	TipoPlato.codigoTipoPlato, 
				TipoPlato.descripcionTipoPlato 
			from TipoPlato 
			where idTipPl = codigoTipoPlato;
	end $$
delimiter ;
#----------------------------------Procedimientos de Productos------------------------------------------#
delimiter $$
create procedure sp_ListarProductos ()
	begin
		Select 	Productos.codigoProducto, 
				Productos.nombreProducto, 
				Productos.cantidad
		from Productos;
	end $$
delimiter ;

delimiter $$
create procedure sp_AgregarProducto(in nombreProductox varchar(150), in cantidadx int)
	begin
		insert into Productos(nombreProducto, cantidad) 
		values (nombreProductox, cantidadx);
	end $$
delimiter ;

delimiter $$
create procedure sp_EditarProducto(in codProducto int, in nombreProdu varchar(150), in canti int)
	begin
		Update Productos
		set
			nombreProducto = nombreProdu,
			cantidad = canti
		where codigoProducto=codProducto;
	end $$
delimiter ;

delimiter $$
create procedure sp_EliminarProducto(in idBuscar int)
	begin
		delete from Productos
		where codigoProducto=idBuscar;
	end $$
delimiter ;

delimiter $$
create procedure sp_BuscarProducto(in idProdu int)
	begin
		select 	Productos.codigoProducto, 
				Productos.nombreProducto, 
                Productos.cantidad 
		from Productos 
		where idProdu = codigoProducto;
	end $$
delimiter ;
#----------------------------------Procedimientos de Presupuesto------------------------------------------#
#drop procedure sp_ListarPresupuestos;
delimiter $$
	create procedure sp_ListarPresupuestos ()
		begin
			Select 
				Presupuesto.codigoPresupuesto, 
                Presupuesto.fechaSolicitud, 
                Presupuesto.cantidadPresupuesto, 
                Presupuesto.codigoEmpresa
					from Presupuesto;
		end $$
delimiter ;

delimiter $$
	create procedure sp_AgregarPresupuesto(in fechaSolicitad date, in cantidadPresupuesto decimal(10,2), in codigoEmpresa int)
		begin
			insert into Presupuesto(codigoPresupuesto, fechaSolicitud, cantidadPresupuesto, codigoEmpresa)
			values(codigoPresupuesto, fechaSolicitad, cantidadPresupuesto, codigoEmpresa);
		end $$
delimiter ;

delimiter $$
	create procedure sp_EditarPresupuesto(in codPresupuesto int, in fechaSoli date, in cantidadPresu decimal(10,2), in codigEmprea int)
		begin
			Update Presupuesto
			set
				fechaSolicitud = fechaSoli,
				cantidadPresupuesto = cantidadPresu,
                codigoEmpresa = codigEmprea
			where codigoPresupuesto=codPresupuesto;
		end $$
delimiter ;

delimiter $$
	create procedure sp_EliminarPresupuesto(in idBuscar int)
		begin
			delete 
            from Presupuesto
			where codigoPresupuesto=idBuscar;
		end $$
delimiter ;

delimiter $$
	create procedure sp_BuscarPresupuesto(in idPresu int)
		begin
			select 
            Presupuesto.fechaSolicitud, 
            Presupuesto.cantidadPresupuesto, 
            Presupuesto.codigoEmpresa 
				from Presupuesto
			where idPresu = codigoPresupuesto;
		end $$
delimiter ;

#----------------------------------Procedimientos de Servicios------------------------------------------#

delimiter $$
	create procedure sp_ListarServicios ()
		begin
			Select 	Servicios.codigoServicio, 
					Servicios.fechaServicio, 
                    Servicios.tipoServicio, 
                    Servicios.horaServicio, 
                    Servicios.lugarServicio, 
                    Servicios.telefonoContacto,
					Servicios.codigoEmpresa
					from Servicios;
		end $$
delimiter ;
 

delimiter $$
	create procedure sp_AgregarServicio(in fechaServiciox date, in tipoServiciox varchar(100), in horaServiciox time,
						in lugarServiciox varchar(100), in telefonoContactox varchar(10), in codigoEmpresax int)
		begin
			insert into Servicios(fechaServicio, tipoServicio, horaServicio, lugarServicio, telefonoContacto, codigoEmpresa)
						values(fechaServiciox, tipoServiciox, horaServiciox, lugarServiciox, telefonoContactox, codigoEmpresax);
		end $$
delimiter ;

delimiter $$
create procedure sp_EditarServicio(in codServicio int, in fechaServi date, in tipoServi varchar(100), in horaServi time,
	in lugarServi varchar(100), in teleContac varchar(10), in coioEmpres int)
begin
	Update Servicios
    set 
        fechaServicio = fechaServi,
        tipoServicio = tipoServi,
        horaServicio = horaServi,
        lugarServicio = lugarServi,
        telefonoContacto = teleContac,
        codigoEmpresa = coioEmpres
	where codigoServicio=codServicio;
end $$
delimiter ;

delimiter $$
create procedure sp_EliminarServicio(in idBuscar int)
begin
	delete from Servicios
    where codigoServicio=idBuscar;
end $$
delimiter ;

delimiter $$
	create procedure sp_BuscarServicio(in idServi int)
		begin
			select 	Servicios.codigoServicio, 
					Servicios.fechaServicio, 
                    Servicios.tipoServicio, 
                    Servicios.horaServicio, 
                    Servicios.lugarServicio, 
                    Servicios.telefonoContacto,
					Servicios.codigoEmpresa
						from Servicios 
							where idServi = codigoServicio;
		end $$
delimiter ;
#----------------------------------Procedimientos de Empleados------------------------------------------#
delimiter $$
create procedure sp_ListarEmpleados ()
	begin
		Select 	Empleados.codigoEmpleado, 
				Empleados.numeroEmpleado, 
				Empleados.apellidosEmpleado, 
				Empleados.nombresEmpleado, 
				Empleados.direccionEmpleado, 
				Empleados.telefonoContacto, 
				Empleados.gradoCocinero, 
				Empleados.codigoTipoEmpleado
		from Empleados;
	end $$
delimiter ;


delimiter $$
create procedure sp_AgregarEmpleado(in numeroEmpleadox int, in apellidosEmpleado varchar(150), in nombresEmpleado varchar(150),
	in direccionEmpleado varchar(150), in telefonoContacto varchar(10), in gradoCocinero varchar(50), in codigoTipoEmpleado int)
	begin
		insert into Empleados(numeroEmpleado, apellidosEmpleado, nombresEmpleado, direccionEmpleado, telefonoContacto, gradoCocinero, codigoTipoEmpleado)
			values(numeroEmpleadox, apellidosEmpleado, nombresEmpleado, direccionEmpleado, telefonoContacto, gradoCocinero, codigoTipoEmpleado);
	end $$
delimiter;


delimiter $$
create procedure sp_EditarEmpleado(in codEmpleado int, in numEmpleado int, in apelEmp varchar(150), in nomEmp varchar(150),
	in direccionEmp varchar(150), in teleContac varchar(10), in gradoCoci varchar(50), in codTipEmpleado int)
	begin
		Update Empleados
		set
			numeroEmpleado = numEmpleado,
			apellidosEmpleado = apelEmp,
			nombresEmpleado = nomEmp,
			direccionEmpleado = direccionEmp,
			telefonoContacto = teleContac,
			gradoCocinero = gradoCoci,
			codigoTipoEmpleado = codTipEmpleado
		where codigoEmpleado=codEmpleado;
	end $$
delimiter ;

delimiter $$
create procedure sp_EliminarEmpleado(in idBuscar int)
begin
	delete from Empleados
    where codigoEmpleado=idBuscar;
end $$
delimiter ;

delimiter $$
create procedure sp_BuscarEmpleado(in idEmpl int)
	begin
		select 	Empleados.codigoEmpleado, 
				Empleados.numeroEmpleado, 
                Empleados.apellidosEmpleado, 
                Empleados.nombresEmpleado, 
                Empleados.direccionEmpleado, 
				Empleados.telefonoContacto, 
                Empleados.gradoCocinero, 
                Empleados.codigoTipoEmpleado, 
                Empleados.TipoEmpleado_codigoTipoEmpleado 
					from Empleados where idEmpl = codigoEmpleado;
	end $$
delimiter ;
#----------------------------------Procedimientos de Platos------------------------------------------#
delimiter $$
create procedure sp_ListarPlatos ()
	begin
		Select 	Platos.codigoPlato, 
				Platos.cantidad, 
                Platos.nombrePlato,
                Platos.descripcionPlato,
                Platos.precioPlato,
                Platos.codigoTipoPlato
		from Platos;
	end $$
delimiter ;

delimiter $$
create procedure sp_AgregarPlato(in cantidadx int, in nombrePlatox varchar(50), in descripcionPlatox varchar(150), 
								in precioPlatox decimal(10,2), in codigoTipoPlatox int)
	begin
		insert into Platos(cantidad, nombrePlato, descripcionPlato, precioPlato, codigoTipoPlato)
			values(cantidadx, nombrePlatox, descripcionPlatox, precioPlatox, codigoTipoPlatox);
	end $$
delimiter ;

delimiter $$
create procedure sp_EditarPlato(in codPlato int, in canti int, in nomPlato varchar(50), in descriPlato varchar(150), in precioPl decimal(10,2), 
	in codTipPl int)
begin
	Update Platos
    set
        cantidad = canti,
        nombrePlato = nomPlato,
        descripcionPlato = descriPlato,
        precioPlato = precioPl,
        codigoTipoPlato = codTipPl
	where codigoPlato=codPlato;
end $$
delimiter ;

delimiter $$
create procedure sp_EliminarPlato(in idBuscar int)
begin
	delete from Platos
    where codigoPlato=idBuscar;
end $$
delimiter ;

delimiter $$
create procedure sp_BuscarPlato(in idPla int)
	begin
		select 	Platos.codigoPlato, 
				Platos.cantidad, 
                Platos.nombrePlato, 
                Platos.descripcionPlato, 
				Platos.precioPlato, 
                Platos.codigoTipoPlato
					from Platos 
					where idPla = codigoPlato;
	end $$
delimiter ;
#----------------------------------Procedimientos de Servicios_has_Platos------------------------------------------#
delimiter $$
create procedure sp_ListarServicios_has_Platos ()
	begin
		Select s.codigoServicio,
				p.codigoPlato
		from servicios s, platos p;
	end $$
delimiter ;

delimiter $$
create procedure sp_AgregarServicios_has_Platos(in Servicios_codigoServicio int, in Platos_codigoPlato int)
	begin
		insert into Servicios_has_Platos(Servicios_codigoServicio, Platos_codigoPlato) values(Servicios_codigoServicio, Platos_codigoPlato);
	end $$
delimiter ;

delimiter $$
create procedure sp_EditarServicios_has_Platos(in Servi_codServi int, in Platos_codPl int, in fila int)
	begin
		Update Servicios_has_Platos
		set
			Servicios_codigoServicio = Servi_codServi, 
			Platos_codigoPlato = Platos_codPl;
	end $$
delimiter ;

delimiter $$
create procedure sp_EliminarServicios_has_Platos(in idBuscar int)
	begin
		delete From Servicios_has_Platos;
	end $$
delimiter ;

delimiter $$
create procedure sp_BuscarServicios_has_Platos()
	begin
		select	 Servicios_has_Platos.Servicios_codigoServicio, 
				Servicios_has_Platos.Platos_codigoPlato 
					from Servicios_has_Platos;
	end $$
delimiter ;
#----------------------------------Procedimientos de Productos_has_Platos------------------------------------------#
delimiter $$
create procedure sp_ListarProductos_has_Platos ()
	begin
		Select 	pr.codigoProducto, 
				pl.codigoPlato
		from productos pr, platos pl ;
	end $$
delimiter ;


delimiter $$
create procedure sp_AgregarProductos_has_Platos(in codigoProductox int, in codigoPlatox int)
	begin
		insert into Productos_has_Platos(codigoProducto, codigoPlato) values(codigoProductox, codigoPlatox);
	end $$
delimiter ;

delimiter $$
create procedure sp_EditarProductos_has_Platos(in Produc_codProdu int, in Plat_codPl int, in fila int)
	begin
		Update Productos_has_Platos
		set
			Productos_codigoProducto = Produc_codProdu, 
			Platos_codigoPlato = Plat_codPl;
	end $$
delimiter ;

delimiter $$
create procedure sp_EliminarProductos_has_Platos(in idBuscar int)
	begin
		delete from Platos;
	end $$
delimiter ;

delimiter $$
create procedure sp_BuscarProductos_has_Platos()
	begin
		select 	Productos_has_Platos.Productos_codigoProducto, 
				Productos_has_Platos.Platos_codigoPlato 
					from Productos_has_Platos;
	end $$
delimiter ;


#----------------------------------Procedimientos de Servicios_has_Empleados------------------------------------------#
delimiter $$
create procedure sp_ListarServicios_has_Empleados ()
	begin
		Select 	
				s.codigoServicio,
				e.codigoEmpleado,
                she.codigoServiciosHasEmpleados,
                she.fechaEvento,
				she.horaEvento, 
                she.lugarEvento
					from Servicios_has_Empleados she, Servicios s, Empleados e
						where she.codigoServicio = s.codigoServicio and she.codigoEmpleado = e.codigoEmpleado;
	end $$
delimiter ;

delimiter $$
create procedure sp_AgregarServicios_has_Empleados(in codigoServiciox int, in codigoEmpleadox int, in fechaEventox date,
	in horaEventox time, in lugarEventox varchar(150))
	begin
		insert into Servicios_has_Empleados(codigoServicio, codigoEmpleado, fechaEvento, horaEvento, lugarEvento)
			values(codigoServiciox, codigoEmpleadox, fechaEventox, horaEventox, lugarEventox);
	end $$
delimiter ;

delimiter $$
create procedure sp_EditarServicios_has_Empleados(in Servicios_codServ int, in Empl_codEmpleado int, in fecEvent date,
	in horaEven time, in lugEve varchar(150), in buscar int)
	begin
		Update Servicios_has_Empleados
		set
			codigoServicio = Servicios_codServ, 
			codigoEmpleado = Empl_codEmpleado,
			fechaEvento = fecEvent,
			horaEvento = horaEven,
			lugarEvento = lugEve
            where buscar = codigoServiciosHasEmpleados;
	end $$
delimiter ;

delimiter $$
create procedure sp_EliminarServicios_has_Empleados(in idBuscar int)
	begin
		delete from Servicios_has_Empleados where idBuscar = codigoServiciosHasEmpleados;
	end $$
delimiter ;

delimiter $$
create procedure sp_BuscarServicios_has_Empleados()
	begin
		select	Servicios_has_Empleados.codigoServicio, 
				Servicios_has_Empleados.codigoEmpleado, 
                Servicios_has_Empleados.fechaEvento,
				Servicios_has_Empleados.horaEvento, 
                Servicios_has_Empleados.lugarEvento 
					from Servicios_has_Empleados;
	end $$
delimiter ;


#/////////////////////////////////PROCEDURES REPORTES//////////////////////////////


Delimiter $$
Create procedure sp_ListarReporte(in codEmpresa int)
begin
	select * from Empresas e inner join Presupuesto P 
		on E.codigoEmpresa = P.codigoEmpresa
		inner join Servicios S on 
			E.codigoEmpresa = S.codigoEmpresa
			where E.codigoEmpresa = codEmpresa group by S.tipoServicio HAVING COUNT(*) > 1;
end$$
Delimiter ;

Delimiter $$
	create procedure sp_subReportePresupuesto(in codEmpresa int)
		begin
			select * from Empresas E inner join presupuesto P on
            E.codigoEmpresa = P.codigoEmpresa where E.codigoEmpresa = codEmpresa 
            group by P.cantidadPresupuesto;
		end $$
Delimiter ;

	
Delimiter $$
	create procedure sp_ReportePresupuesto(in codEmpresa int)
		begin
			select * from Empresas e inner join servicios s 
			on e.codigoEmpresa = s.codigoEmpresa where e.codigoEmpresa = codEmpresa;
		end $$
Delimiter ;

Delimiter $$
	create procedure sp_ReporteServicios(in codServicio int)
		begin
			SELECT * FROM  servicios S
					INNER JOIN platos p ON s.codigoServicio = p.codigoPlato
					CROSS JOIN(SELECT TP.codigoTipoPlato,TP.descripcionTipoPlato FROM tipoPlato TP, platos PL 
						WHERE TP.codigoTipoPlato = PL.codigoTipoPlato GROUP BY TP.codigoTipoPlato ) C 
                    CROSS JOIN(SELECT em.codigoEmpresa, em.nombreEmpresa FROM empresas em, servicios se 
						WHERE em.codigoEmpresa = se.codigoEmpresa  ) CE 
					INNER JOIN productos R ON s.codigoServicio = R.codigoProducto
						WHERE s.codigoServicio = codServicio AND c.codigoTipoPlato = p.codigoTipoPlato 
                        AND s.codigoEmpresa = CE.codigoEmpresa ORDER BY s.tipoServicio;
		end $$
Delimiter ;


#------------------	INGRESO DE DATOS 	------------------------------

call sp_AgregarEmpresa("Suministros & Alimentos","Mixco", "36957851");
call sp_AgregarEmpresa("Dominos","Guatemala", "55445212");
call sp_AgregarEmpresa("La Torre","Guatemala", "355377664");
call sp_AgregarEmpresa("La Barata","Guatemala", "78564652");
call sp_AgregarEmpresa("La Chonita","Guatemala", "78557887");
call sp_AgregarEmpresa("Sony","Mixco", "11123487");
call sp_AgregarEmpresa("Adidas","Guatemala", "45678554");
call sp_AgregarEmpresa("Nike","Mixco", "78985456");
call sp_AgregarEmpresa("Pro-Look","Guatemala", "45545687");
call sp_AgregarEmpresa("Kinal","Guatemala", "12354565");
call sp_AgregarEmpresa("Play Station","Guatemala", "11123487");
call sp_AgregarEmpresa("Nintendo","Guatemala", "45678554");
call sp_AgregarEmpresa("Olimpo","Carretera al Salvador", "78985456");
call sp_AgregarEmpresa("Fab","Villa Nueva", "45545687");


call sp_AgregarTipoEmpleado("Cocinero");
call sp_AgregarTipoEmpleado("Camarero");
call sp_AgregarTipoEmpleado("Ayudante de cocina");
call sp_AgregarTipoEmpleado("Conserje");
call sp_AgregarTipoEmpleado("Gerente");

call sp_AgregarTipoPlato("Desayuno");	
call sp_AgregarTipoPlato("Almuerzo");
call sp_AgregarTipoPlato("Cena");
call sp_AgregarTipoPlato("Refaccion");
call sp_AgregarTipoPlato("Postre");

call sp_AgregarProducto("Huevo, Tomate, Cafe",40);
call sp_AgregarProducto("Huevo, Tomate, Cafe, Platano",20);
call sp_AgregarProducto("Tortilla y chorizo",80);
call sp_AgregarProducto("Fideos, chile pimiento, zanahoria",20);
call sp_AgregarProducto("Carne de res, perulero, papa, elote",10);
call sp_AgregarProducto("Pollo, Tomate, cebolla, ajo, repollo",40);
call sp_AgregarProducto("Sardina, arroz, pimienta",20);
call sp_AgregarProducto("Fideo, tomate, cebolla",80);
call sp_AgregarProducto("Frijol, Huevo, crema",20);
call sp_AgregarProducto("Fresa, chocolate, limon, uva,crema",10);


call sp_AgregarPresupuesto("19-10-01",2512,1);
call sp_AgregarPresupuesto("19-10-02",1555,2);
call sp_AgregarPresupuesto("19-10-03",123,3);
call sp_AgregarPresupuesto("19-10-20",2525,4);
call sp_AgregarPresupuesto("19-10-21",4556,5);
call sp_AgregarPresupuesto("19-11-10",13123,6);
call sp_AgregarPresupuesto("19-12-20",1155,7);
call sp_AgregarPresupuesto("19-12-18",1000,8);
call sp_AgregarPresupuesto("19-12-18",1555,9);	
call sp_AgregarPresupuesto("19-12-19",1000,10);


call sp_AgregarServicio("20-10-01","Buffe",now(),"Hotel Irtra","11111111",1);
call sp_AgregarServicio("20-10-02","Evento",now(),"Eventos Lulu","12544565",2);
call sp_AgregarServicio("20-10-03","Boda",now(),"Hotel Conquistador","56456555",3);
call sp_AgregarServicio("20-10-20","Cumpleaños",now(),"Salon Quinto","555565212",4);
call sp_AgregarServicio("20-11-21","Buffe",now(),"Salon Luisito","55652132",5);
call sp_AgregarServicio("20-11-10","Refaccion",now(),"Los cedros","96565854",6);
call sp_AgregarServicio("20-11-20","Fiesta",now(),"Cerro Gordo","1778",7);
call sp_AgregarServicio("20-12-18","Aniversario",now(),"Eventos z.10","52221225",8);
call sp_AgregarServicio("20-12-18","15 Años",now(),"Salon Pro","23323222",9);
call sp_AgregarServicio("20-12-19","Fiesta",now(),"Salon 1","22252125",10);

call sp_AgregarEmpleado(1001,"Ramirez Perez","Juan Pablo","Mixco","52212565","Profesional",1);
call sp_AgregarEmpleado(1002,"Garcia Mejia","Allan Rodrigo","Santa Rosa","56598542",null,5);
call sp_AgregarEmpleado(1003,"Ramirez Lopez","Kenet Omar","Peten","97895456",null,2);
call sp_AgregarEmpleado(1004,"Cabrera Perez","Elliot David","El Progreso","3124545",null,3);
call sp_AgregarEmpleado(1005,"Hernandez Perez","Jordi Mateo","Guatemala","54525444",null,1);
call sp_AgregarEmpleado(1006,"Ramirez Lopez","Nelson Aldair","Guatemala","64585645","Profesional",1);
call sp_AgregarEmpleado(1007,"Alvarado Aguilar","Angel Fernando","Guatemala","45645658","Profesional",1);
call sp_AgregarEmpleado(1008,"Alcantara Perez","Aruto Jose","Villa Nueva","12345678","Intermedio",1);
call sp_AgregarEmpleado(1009,"Ramirez Alverez","Jose Jose","Izabal","13467985",null,3);
call sp_AgregarEmpleado(1010,"Rodriguez Perez","Juan Carlos","Mixco","31649725",null,2);

call sp_AgregarPlato(20,"Tipico","Huevo, Salsa de tomate y cafe",35,1);
call sp_AgregarPlato(100,"Super Tipico","Desayuno guatemalteco con platano huevo y salsa de tomate",45,1);
call sp_AgregarPlato(100,"Burrito de Chorizo","Burrito con choirzo adentro",36,1);
call sp_AgregarPlato(100,"Chomin","Comida China de fideos",40,2);
call sp_AgregarPlato(100,"Caldo de Res","Comida De Res",40,2);
call sp_AgregarPlato(100,"Caldo de Pollo","Comida de Pollo con verduras",40,2);
call sp_AgregarPlato(100,"Sushi","Comida China con arroz",50,2);
call sp_AgregarPlato(100,"Fideos","Fideos con salsa de tomate",30,2);
call sp_AgregarPlato(100,"Huevos y frijol","Cena tipica con frijol y huevos",25,3);
call sp_AgregarPlato(100,"Helado","Helado de diferentes sabores",10,4);


call sp_AgregarServicios_has_Empleados(1,1,"20-01-01",now(),"Hotel Irta");
call sp_AgregarServicios_has_Empleados(2,2,"20-02-02",now(),"Eventos Lulu");
call sp_AgregarServicios_has_Empleados(3,3,"20-03-03",now(),"Hotel Conquistador");
call sp_AgregarServicios_has_Empleados(4,4,"20-04-04",now(),"Salon Quinto");
call sp_AgregarServicios_has_Empleados(5,5,"20-05-05",now(),"Salon Luisito");
call sp_AgregarServicios_has_Empleados(6,6,"20-06-06",now(),"Los cedros");
call sp_AgregarServicios_has_Empleados(7,7,"20-07-07",now(),"Cerro Gordo");
call sp_AgregarServicios_has_Empleados(8,8,"20-08-08",now(),"Eventos z.10");
call sp_AgregarServicios_has_Empleados(9,9,"20-09-09",now(),"Salon Pro");
call sp_AgregarServicios_has_Empleados(10,10,"20-10-10",now(),"Salon 1");



#ALTER USER 'root'@'localhost' identified with mysql_native_password by 'admin';
#?characterEncoding=utf8