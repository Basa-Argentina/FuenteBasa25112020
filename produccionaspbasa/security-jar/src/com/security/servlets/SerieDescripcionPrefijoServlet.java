/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 28/06/2011
 */
package com.security.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.context.SecurityContextHolder;

import com.security.accesoDatos.configuraciongeneral.hibernate.SerieServiceImp;
import com.security.accesoDatos.configuraciongeneral.interfaz.SerieService;
import com.security.accesoDatos.hibernate.ClienteAspServiceImp;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.accesoDatos.interfaz.ClienteAspService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Serie;
import com.security.modelo.seguridad.User;

/**
 * @author Victor Kenis
 *
 */
public class SerieDescripcionPrefijoServlet extends HttpServlet{
	private static final long serialVersionUID = -2135973356955496716L;
	private static Logger logger = Logger.getLogger(SerieDescripcionPrefijoServlet.class);

	@Override
	public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml"); 
		response.setCharacterEncoding("ISO-8859-1"); //seteo el encoding de los caracteres 
		response.setHeader("Cache-Control", "no-cache");
		
		String codigo = request.getParameter("codigo");
		String codigoEmpresa = request.getParameter("codigoEmpresa");
		String tipoSerie = request.getParameter("tipoSerie");
		String habilitadoStr = request.getParameter("habilitado");
		Boolean habilitado = null;
		if(habilitadoStr != null && habilitadoStr.length() > 0)
		{
			if("true".equals(habilitadoStr))
			{
				habilitado = true;
			}
			if("false".equals(habilitadoStr))
			{
				habilitado = false;
			}
		}
		String clienteIdStr = request.getParameter("clienteId");
		if(clienteIdStr == null || clienteIdStr.length()==0){
			clienteIdStr=obtenerClienteAspUser().getId().toString();
		}
		
		String respuesta = "";
		String proximoNumero = null;
		Serie lista = null;
		if(!"".equals(codigo))
			lista = getObject(codigo, tipoSerie, codigoEmpresa, habilitado,Long.valueOf(clienteIdStr));	
		if(lista != null){
			
			if(lista.getUltNroImpreso() != null && lista.getUltNroImpreso().length()>0 && Long.valueOf(lista.getUltNroImpreso()) != 0){
        		Long proximoCodigoLong = Long.valueOf(lista.getUltNroImpreso()) + 1L;
	    		StringBuffer aux = new StringBuffer("");
	    		proximoNumero = String.valueOf(proximoCodigoLong);
	    		for (int i = 0; i<(lista.getUltNroImpreso().length() - proximoNumero.length()) ; i++){
	    			aux.append("0");
	    		}
	    		aux.append(proximoNumero);
	    		proximoNumero = aux.toString();
			}
	    		else{
	    			proximoNumero = "00000001";
		    	}
			
			respuesta = lista.getDescripcion()+"-"+lista.getPrefijo()+"-"+proximoNumero;
		}else{
			respuesta = "";
		}		
		try {
			response.getWriter().write(respuesta);
		} catch (IOException e) {
			logger.error("No se pudo listar los conceptos facturables", e);
			e.printStackTrace();
		}		
	}
	
	private Serie getObject(String codigo, String tipoSerie, String codigoEmpresa, Boolean habilitado, Long clienteId){
		SerieService service = new SerieServiceImp(HibernateControl.getInstance());
		Serie tipo = service.obtenerPorCodigo(codigo, tipoSerie, codigoEmpresa, habilitado,getObject(clienteId));
		if(tipo != null)
			return tipo;
		else
			return null;
	}
	
	private ClienteAsp getObject(Long id){
		ClienteAspService service = new ClienteAspServiceImp(HibernateControl.getInstance());
		return service.obtenerPorId(id);
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}
