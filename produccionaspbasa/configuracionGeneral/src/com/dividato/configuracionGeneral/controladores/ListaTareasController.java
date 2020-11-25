/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/05/2011
 */
package com.dividato.configuracionGeneral.controladores;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ReferenciaService;
import com.security.modelo.configuraciongeneral.Referencia;
import com.security.modelo.seguridad.User;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;

/**
 * Controlador que se utiliza para los servicios asociados a la
 * lista de transportes.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Victor Kenis *
 */

@Controller
@RequestMapping(
		value=
			{	
				"/iniciarTarea.html",
				"/mostrarTarea.html",
				"/eliminarTarea.html"
			}
		)
public class ListaTareasController {
	private ReferenciaService referenciaService;
	private EmpresaService empresaService;
	
	@Autowired
	public void setReferenciaService(ReferenciaService referenciaService) {
		this.referenciaService = referenciaService;
	}
	
	@Autowired
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	@RequestMapping(value="/iniciarTarea.html", method = RequestMethod.GET)
	public String iniciarTarea(HttpSession session, Map<String,Object> atributos){
		
		return "redirect:mostrarTarea.html";
	}
	
	@RequestMapping(value="/mostrarTarea.html", method = RequestMethod.GET)
	public String mostrarTarea(HttpSession session, 
			Map<String,Object> atributos,
			@RequestParam(value="val", required=false) String valEmpresa){
		
		User user = obtenerUser();
		
		List<Referencia> referencias = referenciaService.listarByCodigoUsuario(user.getId());
								
		
		atributos.put("tareas", referencias);

		
		return "consultaTarea";
	}
	

	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de eliminar Tarea.
	 * 
	 * @param idDireccion el id de Tarea a eliminar.
	 * (Observar la anotación @RequestParam)
	 * @param atributos son los atributos del request
	 * @return ejecuta el método de consulta de inscost y retorna su resultado.
	 */
	@RequestMapping(
			value="/eliminarTarea.html",
			method = RequestMethod.GET
		)
	public String eliminarTarea(HttpSession session,
			@RequestParam("id") String id,
			Map<String,Object> atributos) {
		
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		//Obtenemos el transporte para eliminar luego
		Referencia tarea = referenciaService.obtenerPorId(Long.valueOf(id));
		tarea.setEstadoTarea("Finalizada");
		
		//Eliminamos el transporte
		referenciaService.actualizar(tarea);
		
		ScreenMessage mensaje;
		//Controlamos su eliminacion.

			mensaje = new ScreenMessageImp("notif.transporte.tareaFinalizada", null);
			hayAvisos = true;

		avisos.add(mensaje);
		
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		return mostrarTarea(session, atributos, null);
	}
	
	private User obtenerUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
}
