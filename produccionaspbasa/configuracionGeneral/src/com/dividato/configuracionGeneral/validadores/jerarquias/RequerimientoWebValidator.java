package com.dividato.configuracionGeneral.validadores.jerarquias;

import static com.security.recursos.Configuracion.formatoFechaFormularios;
import java.util.Date;
import java.util.Iterator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.configuraciongeneral.interfaz.ElementoService;
import com.security.accesoDatos.jerarquias.interfaz.TipoOperacionService;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.jerarquias.Requerimiento;
import com.security.modelo.jerarquias.RequerimientoReferencia;
import com.security.modelo.seguridad.User;
import com.security.utils.Constantes;


@Component
public class RequerimientoWebValidator implements Validator{
	private TipoOperacionService tipoOperacionService;
	private ElementoService elementoService;
	int i = 0 ;
	private String tipo = "Nulo";
	
	
	@Autowired
	public void setTipoOperacionService(TipoOperacionService tipoOperacionService) {
		this.tipoOperacionService = tipoOperacionService;
	}
	
	@Autowired
	public void setElementoService(ElementoService elementoService) {
		this.elementoService = elementoService;
	}
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean supports(Class type) {
		return Requerimiento.class.isAssignableFrom(type);
	}

	
	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, "fechaAlta",
				new CustomDateEditor(formatoFechaFormularios, true));
		binder.registerCustomEditor(Date.class, "fechaEntrega",
			new CustomDateEditor(formatoFechaFormularios, true));
}

	@Override
	public void validate(Object obj, Errors errors) {	
		Requerimiento requerimiento = (Requerimiento) obj;
		if(!requerimiento.isBuscarElemento() && !requerimiento.isBuscarElementoSinReferencia() 
				&& !requerimiento.isInsertarElementoDirecto()){
			
			
			boolean banderaRequeridos = false;
			
			if(requerimiento.getClienteCodigo() == null || "".equals(requerimiento.getClienteCodigo())){
				errors.rejectValue("clienteCodigo", "required");
				banderaRequeridos = true;
			}
			if(requerimiento.getCodigoDireccion() == null || "".equals(requerimiento.getCodigoDireccion())){
				errors.rejectValue("codigoDireccion", "required");
				banderaRequeridos = true;
			}

			if(requerimiento.getTipoRequerimientoCod() == null || "".equals(requerimiento.getTipoRequerimientoCod())){
				errors.rejectValue("tipoRequerimientoCod", "required");
				banderaRequeridos = true;
			}
			if(requerimiento.getCodigoPersonal() == null || "".equals(requerimiento.getCodigoPersonal())){
				errors.rejectValue("codigoPersonal", "required");
				banderaRequeridos = true;
			}
			if(requerimiento.getCodigoPersonalAutorizante() == null || "".equals(requerimiento.getCodigoPersonalAutorizante())){
				errors.rejectValue("codigoPersonalAutorizante", "required");
				banderaRequeridos = true;
			}
			if(requerimiento.getFechaAlta() == null){
				errors.rejectValue("fechaAlta", "required");
				banderaRequeridos = true;
			}
			
			if(requerimiento.getFechaEntrega() == null){
				errors.rejectValue("fechaEntrega", "required");
				banderaRequeridos = true;
			}
			
			if(requerimiento.getTipoRequerimiento().getCargaPorCantidad()== Boolean.TRUE) {
				if(requerimiento.getCantidad() == null ) {
				errors.rejectValue("cantidad", "required");
			   return;
			
				}
				if(requerimiento.getCantidad() == 0 ) {
					errors.rejectValue("cantidad", "required");
					
					return;
					}
		}

			if(banderaRequeridos)
				return;
			//Valido los formatos de hora
			
			if(validarHoraIncorrecta(requerimiento.getHoraAlta()))
				errors.rejectValue("horaAlta", "formularioRequerimiento.errorHora");
			if(validarHoraIncorrecta(requerimiento.getHoraEntrega()))
				errors.rejectValue("horaEntrega", "formularioRequerimiento.errorHora");
			
			if(requerimiento.getFechaAlta() != null && requerimiento.getFechaEntrega() != null)
				if(requerimiento.getFechaAlta().after(requerimiento.getFechaEntrega()))
					errors.rejectValue("fechaAlta", "formularioRequerimiento.errorFechaAltaEntrega");
			
			if(requerimiento.getTipoRequerimiento().getCargaElementoSimple())
				if(requerimiento.getListaElementos()==null || requerimiento.getListaElementos().isEmpty())
					errors.rejectValue("codigoElemento", "formularioRequerimiento.errorCantidadVacia");
			

		}
		else{
			if(requerimiento.getClienteCodigo() == null || "".equals(requerimiento.getClienteCodigo())){
				errors.rejectValue("clienteCodigo", "required");
				return;
			}
			
			if(requerimiento.isInsertarElementoDirecto())
				
			{
				if(requerimiento.getTipoRequerimientoCod() == null || "".equals(requerimiento.getClienteCodigo())){
					errors.rejectValue("clienteCodigo", "required");
					return;
				}
								Elemento elemento = elementoService.buscarElementosParaRequerimientosPorSQL(requerimiento.getCodigoElemento(), 
						requerimiento.getClienteCodigo(), requerimiento.getTipoRequerimientoCod());
				
				
			if(elemento==null){
					errors.rejectValue("codigoElemento", "formularioRequerimiento.errorInsertarElemento");
					return;
				}
 			try{
 				String elemento1 = elementoService.revisarNumReqSQL(elemento.getId().longValue());
				if (elemento1!=null){
					errors.rejectValue("codigoElemento", "formularioRequerimiento.errorInsertarElementoPendiente" ,new String[]{elemento1},"");
					return;
				}
			} catch(Exception e) {

                 if (!elemento.getClienteEmp().getCodigo().equals(requerimiento.getClienteCodigo())){
					errors.rejectValue("codigoElemento", "formularioRequerimiento.errorInsertarElementoCliente" );
					return;
					
				}
                 
				 if ((requerimiento.getTipoRequerimientoCod().equals("14") || requerimiento.getTipoRequerimientoCod().equals("22")) && requerimiento.getClienteEmp().getControlaReferencias()==1){
					Boolean elemento2 = elementoService.revisarReferencias(elemento.getId().longValue());
					
					if (elemento2==Boolean.FALSE) {
						
						errors.rejectValue("codigoElemento", "formularioRequerimiento.errorInsertarElementoReferencia");
						return;
					
					}
				}

				 if(requerimiento.getTipoRequerimiento().getTipoMovimiento().equals("ingreso") && !elemento.getEstado().equalsIgnoreCase(Constantes.ELEMENTO_ESTADO_EN_EL_CLIENTE) &&
						!elemento.getEstado().equalsIgnoreCase("En Consulta")){
					errors.rejectValue("codigoElemento","formularioRequerimiento.errorInsertarElementoEstado",new String[]{elemento.getCodigo(),elemento.getEstado()},"");
					return;
				}
				 if(requerimiento.getTipoRequerimiento().getTipoMovimiento().equals("egreso") && !elemento.getEstado().equalsIgnoreCase(Constantes.ELEMENTO_ESTADO_EN_GUARDA) &&
						!elemento.getEstado().equalsIgnoreCase("En Planta")){
					errors.rejectValue("codigoElemento","formularioRequerimiento.errorInsertarElementoEstado",new String[]{elemento.getCodigo(),elemento.getEstado()},"");
					return;
				}}
 		
			i++;
		
			if (requerimiento.getListaElementos()==null) {
			
				requerimiento.setElemento(elemento);
				
			} 
			else { 
				
				String elementoActual = "1";
				Iterator<RequerimientoReferencia> itr = requerimiento.getListaElementos().iterator();
				
				while(itr.hasNext()) {
					RequerimientoReferencia elementoRef = itr.next();
					elementoActual = elementoRef.getElemento().getTipoElemento().getId().toString();
					break;
			    }	
				
				if (elemento.getTipoElemento().getId().toString().equals(elementoActual)) {
					requerimiento.setElemento(elemento);
				}
				else {
					errors.rejectValue("codigoElemento", "formularioRequerimiento.errorInsertarDistinto");
					return;
				}
			}
								
							}
				
			}
		}
	
	
	private boolean validarHoraIncorrecta(String hora){
		String [] split = hora.split(":");
		try{
			Integer h = Integer.parseInt(split[0]);
			Integer m = Integer.parseInt(split[1]);
		}
		catch (NumberFormatException e) {
			return true;
		}
		return false;
	}

}

