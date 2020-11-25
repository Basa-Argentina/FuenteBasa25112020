/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 17/06/2011
 */
package com.security.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.context.SecurityContextHolder;

import com.security.accesoDatos.configuraciongeneral.hibernate.LecturaServiceImp;
import com.security.accesoDatos.configuraciongeneral.interfaz.LecturaService;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Lectura;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.seguridad.User;

/**
 * @author Luciano de Asteinza
 * 
 */
public class LecturasServlet extends HttpServlet{
	private static final long serialVersionUID = 2918192312249275039L;
	private static Logger logger = Logger.getLogger(LecturasServlet.class);
	
	@Override
	public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml"); 
		response.setCharacterEncoding("ISO-8859-1"); //seteo el encoding de los caracteres 
		response.setHeader("Cache-Control", "no-cache");
		
		String codigoStr = request.getParameter("codigo");
		Boolean utilizada = null;
		
		if(request.getParameter("utilizada")!=null)
			utilizada = Boolean.valueOf(request.getParameter("utilizada"));
		
		if(utilizada != null && Boolean.TRUE.equals(utilizada)){
			utilizada = false;
		}	
		
		Long codigo = null;
		if(codigoStr!= null && !"".equals(codigoStr) && !"undefined".equals(codigoStr))
		{
			codigo = Long.valueOf(codigoStr);
		}
		
		Lectura lectura = getObject(codigo, utilizada);
		String respuesta = lectura != null && lectura.getDescripcion()!=null ? lectura.getDescripcion() : "";
		try {
			response.getWriter().write(respuesta);
		} catch (IOException e) {
			logger.error("No se pudo recuperar el elemento", e);
			e.printStackTrace();
		}		
	}
	
	private Lectura getObject(Long codigo, Boolean utilizada){
		Lectura lectura = null;
		LecturaService service = new LecturaServiceImp(HibernateControl.getInstance());
		lectura = service.obtenerPorCodigo(codigo, utilizada, obtenerEmpresaDefault(), obtenerClienteAsp());
		return lectura;
	}
	
	private User obtenerUser(){
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	private ClienteAsp obtenerClienteAsp(){
		return obtenerUser().getCliente();
	}
	private Empresa obtenerEmpresaDefault(){
		return ((PersonaFisica)obtenerClienteAsp().getContacto()).getEmpresaDefecto();
	}
}
