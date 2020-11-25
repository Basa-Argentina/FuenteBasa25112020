/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 17/06/2011
 */
package com.security.accesoDatos.facturacion.interfaz;

import java.util.List;


/**
 * @author Gonzalo Noriega
 *
 */
public interface RemitosMovimientosService {
	public List<?> listarRemitosMovimientosFiltrados(int mesBusqueda, int anoBusqueda, String codigoEmpresa, String codigoCliente, String tipoRequerimientoCod);
	public List<String> obtenerTipoRequerimientoDetalle(String tipoRequerimientoCod);
}
