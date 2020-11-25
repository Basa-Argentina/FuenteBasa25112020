package com.security.accesoDatos.configuraciongeneral.interfaz;

import java.util.Date;
import java.util.List;

import com.security.accesoDatos.interfaz.GeneralServiceInterface;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.LoteExportacionRearchivo;

/**
 * 
 * @author Federico Mz
 *
 */
public interface LoteExportacionRearchivoService extends GeneralServiceInterface<LoteExportacionRearchivo>{

	List<LoteExportacionRearchivo> obtenerPor(ClienteAsp clienteAsp, String codigoEmpresa, String codigoSucursal,
			String codigoCliente, Integer codigoClasificacionDocumental,
			Long codigoDesde, Long codigoHasta, Date fechaDesde, Date fechaHasta);

	LoteExportacionRearchivo obtenerPorIdConLoteReferencias(Long idLoteExportacionRearchivo);

}
