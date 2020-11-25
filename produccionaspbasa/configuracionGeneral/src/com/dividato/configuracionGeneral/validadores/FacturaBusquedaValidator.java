package com.dividato.configuracionGeneral.validadores;

import static com.security.recursos.Configuracion.formatoFechaFormularios;

import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import com.security.modelo.configuraciongeneral.Factura;
/**
 * 
 * @author Victor Kenis
 *
 */
@Component
public class FacturaBusquedaValidator implements Validator {
	
	/**
	 * Indica cual es la clase que este validador puede validar.
	 */
	@Override
	public boolean supports(Class type) {
		return Factura.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, "fechaDesde",
				new CustomDateEditor(formatoFechaFormularios, true));
		binder.registerCustomEditor(Date.class, "fechaHasta",
				new CustomDateEditor(formatoFechaFormularios, true));
	}
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		Factura factura = (Factura) command;
		
		if (factura.getFechaDesde() != null	&& factura.getFechaHasta() != null) {
			if (factura.getFechaDesde().after(factura.getFechaHasta())) {
				errors.rejectValue("fechaDesde","formularioFactura.error.fechaDesdeMayorFechaHasta0");

			}
		}
	}	
	
	
}