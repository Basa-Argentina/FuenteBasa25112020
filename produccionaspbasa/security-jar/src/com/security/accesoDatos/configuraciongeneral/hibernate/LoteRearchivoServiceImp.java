/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/06/2011
 */
package com.security.accesoDatos.configuraciongeneral.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.LoteRearchivoService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.ElementoHistorico;
import com.security.modelo.configuraciongeneral.LoteRearchivo;
import com.security.modelo.configuraciongeneral.LoteReferencia;
import com.security.modelo.configuraciongeneral.Rearchivo;
import com.security.modelo.configuraciongeneral.Referencia;
import com.security.modelo.configuraciongeneral.ReferenciaHistorico;
import com.security.modelo.seguridad.User;
import com.security.utils.DateUtil;

/**
 * 
 * @author Gabriel Mainero
 *
 */
@Component
public class LoteRearchivoServiceImp extends GestorHibernate<LoteRearchivo> implements LoteRearchivoService {
	private static Logger logger=Logger.getLogger(LoteRearchivoServiceImp.class);
	
	@Autowired
	public LoteRearchivoServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<LoteRearchivo> getClaseModelo() {
		return LoteRearchivo.class;
	}

	private List<Long> obtenerIDsLoteRarchivoFiltradas(ClienteAsp cliente,
			String codigoEmpresa, String codigoSucursal, String codigoCliente,
			Long codigoDesde, Long codigoHasta, Date fechaDesde, Date fechaHasta, String tipo, Integer codigoClasificacionDocumental, String codigoContenedor,
			String fieldOrder, String sortOrder,Integer numeroPagina,Integer tama�oPagina) {
		
		Session session = null;
		Integer result = null;
		
        try {
        	//obtenemos una sesi�n
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.setProjection(Projections.id());
	       
        	 crit.add(Restrictions.eq("clienteAsp", cliente));
 	        
         	if(codigoEmpresa!=null && !codigoEmpresa.isEmpty()){
         		crit.createCriteria("empresa", "emp");
         		crit.add(Restrictions.eq("emp.codigo", codigoEmpresa));
         	}
         	
         	if(codigoSucursal!=null && !codigoSucursal.isEmpty()){
         		crit.createCriteria("sucursal", "suc");
         		crit.add(Restrictions.eq("suc.codigo", codigoSucursal));
         	}
         	
         	if(codigoCliente!=null && !codigoCliente.isEmpty()){
         		crit.createCriteria("clienteEmp", "cli");
         		crit.add(Restrictions.eq("cli.codigo", codigoCliente));
         	}
         	
         	if(codigoDesde!=null)
         		crit.add(Restrictions.ge("id", codigoDesde));
         	if(codigoHasta!=null && codigoHasta.intValue()!=0)
         		crit.add(Restrictions.le("id", codigoHasta));
         	if(fechaDesde!=null){
         		crit.add(Restrictions.ge("fechaRegistro", DateUtil.getDateFrom(fechaDesde)));
         	}
         	if(fechaHasta!=null){
         		crit.add(Restrictions.le("fechaRegistro", DateUtil.getDateTo(fechaHasta)));
         	}
         	if(codigoClasificacionDocumental!=null){
         		crit.createCriteria("clasificacionDocumental", "cla");
         		crit.add(Restrictions.eq("cla.codigo", codigoClasificacionDocumental));
         	}
         	if(codigoContenedor!=null && !codigoContenedor.isEmpty()){
         		crit.createCriteria("contenedor", "con");
         		crit.add(Restrictions.eq("con.codigo", codigoContenedor));
         	}
         	if(tipo!=null && !tipo.isEmpty()  && !"Todos".equals(tipo)){
         		crit.add(Restrictions.eq("tipo", tipo));
         	}
         	
         	//Ordenamos
        	if(sortOrder!=null && sortOrder.length()>0 &&
            	fieldOrder!=null && fieldOrder.length()>0){
            		
        			String fieldOrdenar = "";
            		String fieldOrdenar2 = "";
            		
            		if("id".equals(fieldOrder))
            		{
            			fieldOrdenar = "id";
            		}
            		if("tipo".equals(fieldOrder))
            		{
            			fieldOrdenar = "tipo";
            		}
            		if("fechaRegistroStr".equals(fieldOrder)){
    					fieldOrdenar = "fechaRegistro";
    				}
            		if("indiceIndividualStr".equals(fieldOrder)){
    					fieldOrdenar = "indiceIndividual";
    				}
            		if("cantidad".equals(fieldOrder)){
    					fieldOrdenar = "cantidad";
    				}
            		
            		if("1".equals(sortOrder)){
            			if(!"".equals(fieldOrdenar))
        					crit.addOrder(Order.asc(fieldOrdenar));
            			if(!"".equals(fieldOrdenar2))
        					crit.addOrder(Order.asc(fieldOrdenar2));
        			}else if("2".equals(sortOrder)){
        				if(!"".equals(fieldOrdenar))
        					crit.addOrder(Order.desc(fieldOrdenar));
            			if(!"".equals(fieldOrdenar2))
        					crit.addOrder(Order.desc(fieldOrdenar2));
        			}
            	
            	}

        	//Paginamos
        	if(numeroPagina!=null && numeroPagina.longValue()>0 
    				&& tama�oPagina!=null && tama�oPagina.longValue()>0){
    			Integer paginaInicial = (numeroPagina - 1);
    			Integer filaDesde = tama�oPagina * paginaInicial;
    			crit.setFirstResult(filaDesde);
    			
    			crit.setMaxResults(tama�oPagina);
    		}
        	
        	return crit.list();
        	
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo listar ", hibernateException);
	        return null;
        }finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesi�n", e);
        	}
        }
    }
	
	@Override
	public Integer contarObtenerPor(ClienteAsp cliente,
			String codigoEmpresa, String codigoSucursal, String codigoCliente,
			Long codigoDesde, Long codigoHasta, Date fechaDesde, Date fechaHasta, String tipo, Integer codigoClasificacionDocumental, String codigoContenedor) {
		
		Session session = null;
		Integer result = null;
		
	    try {
	    	//obtenemos una sesi�n
	    	session = getSession();
	        Criteria crit = session.createCriteria(getClaseModelo());
	        crit.setProjection(Projections.rowCount());
	        
	        crit.add(Restrictions.eq("clienteAsp", cliente));
	        
        	if(codigoEmpresa!=null && !codigoEmpresa.isEmpty()){
        		crit.createCriteria("empresa", "emp");
        		crit.add(Restrictions.eq("emp.codigo", codigoEmpresa));
        	}
        	if(codigoSucursal!=null && !codigoSucursal.isEmpty()){
        		crit.createCriteria("sucursal", "suc");
        		crit.add(Restrictions.eq("suc.codigo", codigoSucursal));
        	}
        	if(codigoCliente!=null && !codigoCliente.isEmpty()){
        		crit.createCriteria("clienteEmp", "cli");
        		crit.add(Restrictions.eq("cli.codigo", codigoCliente));
        	}
        	if(codigoDesde!=null)
        		crit.add(Restrictions.ge("id", codigoDesde));
        	if(codigoHasta!=null && codigoHasta.intValue()!=0)
        		crit.add(Restrictions.le("id", codigoHasta));
        	if(fechaDesde!=null){
        		crit.add(Restrictions.ge("fechaRegistro", DateUtil.getDateFrom(fechaDesde)));
        	}
        	if(fechaHasta!=null){
        		crit.add(Restrictions.le("fechaRegistro", DateUtil.getDateTo(fechaHasta)));
        	}
        	if(codigoClasificacionDocumental!=null){
        		crit.createCriteria("clasificacionDocumental", "cla");
        		crit.add(Restrictions.eq("cla.codigo", codigoClasificacionDocumental));
        	}
        	if(codigoContenedor!=null && !codigoContenedor.isEmpty()){
        		crit.createCriteria("contenedor", "con");
        		crit.add(Restrictions.eq("con.codigo", codigoContenedor));
        	}
        	if(tipo!=null && !tipo.isEmpty()  && !"Todos".equals(tipo)){
        		crit.add(Restrictions.eq("tipo", tipo));
        	}
        	
	        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	        result = ((Integer)crit.list().get(0));
	        return result;
	    } catch (HibernateException e1) {
	    	logger.error("no se pudo listar todos ", e1);
	        return null;
	        
	    }finally{
		
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesi�n", e);
        	}
        }
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LoteRearchivo> obtenerPor(ClienteAsp cliente,
			String codigoEmpresa, String codigoSucursal, String codigoCliente,
			Long codigoDesde, Long codigoHasta, Date fechaDesde, Date fechaHasta, String tipo, Integer codigoClasificacionDocumental, String codigoContenedor, 
			String fieldOrder, String sortOrder,Integer numeroPagina,Integer tama�oPagina) {
		Session session = null;
	    try {
	    	List<Long> ids = obtenerIDsLoteRarchivoFiltradas(cliente, codigoEmpresa, codigoSucursal, codigoCliente, codigoDesde, codigoHasta, fechaDesde, fechaHasta, tipo, codigoClasificacionDocumental, codigoContenedor, fieldOrder, sortOrder, numeroPagina, tama�oPagina);
	    	//obtenemos una sesi�n
			session = getSession();
			
			//Si es null retornamos en cero, se puso dentro de session debido a que debe cerrar el finally
			if(ids==null || ids.size()==0)
        		return new ArrayList<LoteRearchivo>();
			
	        Criteria crit = session.createCriteria(getClaseModelo());
	        crit.add(Restrictions.in("id", ids));
	        
//        	//Ordenamos
        	if(sortOrder!=null && sortOrder.length()>0 &&
            	fieldOrder!=null && fieldOrder.length()>0){
            		
        		String fieldOrdenar = "";
            		String fieldOrdenar2 = "";
            		
            		if("id".equals(fieldOrder))
            		{
            			fieldOrdenar = "id";
            		}
            		if("tipo".equals(fieldOrder))
            		{
            			fieldOrdenar = "tipo";
            		}
            		if("fechaRegistroStr".equals(fieldOrder)){
    					fieldOrdenar = "fechaRegistro";
    				}
            		if("indiceIndividualStr".equals(fieldOrder)){
    					fieldOrdenar = "indiceIndividual";
    				}
            		if("cantidad".equals(fieldOrder)){
    					fieldOrdenar = "cantidad";
    				}
            		
					/*
					 * if("1".equals(sortOrder)){ if(!"".equals(fieldOrdenar))
					 * crit.addOrder(Order.desc(fieldOrdenar)); if(!"".equals(fieldOrdenar2))
					 * crit.addOrder(Order.desc(fieldOrdenar2)); }else if("2".equals(sortOrder)){
					 * if(!"".equals(fieldOrdenar)) crit.addOrder(Order.desc(fieldOrdenar));
					 * if(!"".equals(fieldOrdenar2)) crit.addOrder(Order.desc(fieldOrdenar2)); }
					 */		
            	}
        	
        	crit.addOrder(Order.desc("id"));
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	        return crit.list();
	    } catch (HibernateException e1) {
	    	logger.error("no se pudo listar todos ", e1);
	        return null;
	    }finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesi�n", e);
        	}
        }
	}
	
	/**
	 * Guarda o actualiza un LoteRearchivo en la base de datos y todas sus relaciones
	 * @param objeto
	 */
	
	@SuppressWarnings("unchecked")
	public synchronized void guardarActualizar(LoteRearchivo loteRearchivo, LoteReferencia loteReferencia){
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesi�n
			session = getSession();
			//creamos la transacci�n
			tx = session.getTransaction();
			tx.begin();
			
			//rearchivos a mantener
			
			Collection<Rearchivo> rearchivos=new ArrayList<Rearchivo>(loteRearchivo.getRearchivos());
			if(loteRearchivo.getId()==null || loteRearchivo.getId()==0){
				
				loteRearchivo.setId(null);
				loteRearchivo.getRearchivos().clear();
				session.save(loteRearchivo);
				
			}else{
				
				session.update(loteRearchivo);
				
				//borramos las rearchivos eliminadas
				
				loteRearchivo = (LoteRearchivo) session.get(LoteRearchivo.class, loteRearchivo.getId());
				Collection<Rearchivo> remove = CollectionUtils.subtract(loteRearchivo.getRearchivos(),rearchivos);
				for(Rearchivo ref:remove){
					if(ref.getId()!=null){
						
						session.delete(ref);
						loteRearchivo.getRearchivos().remove(ref);
						
					}
				}
				//rearchivos agregadas
				rearchivos = CollectionUtils.subtract(rearchivos,loteRearchivo.getRearchivos());
			}
			
			//guardamos las nuevas rearchivos
			for(Rearchivo ref : rearchivos)
			{
				ref.setLoteRearchivo(loteRearchivo);
			
				session.saveOrUpdate(ref);
				loteRearchivo.getRearchivos().add(ref);
			}
			//Reactualizo
			rearchivos=new ArrayList<Rearchivo>(loteRearchivo.getRearchivos());
			User user =  ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
			for(Rearchivo ref : rearchivos) {
		
				if (ref.getTexto1()!=null ||ref.getTexto2()!=null||ref.getNumero1()!=null||ref.getNumero2()!=null)ref.setUserCarga_id(user.getId());

				
				session.update(ref);
			}
			session.update(loteRearchivo);
			

			if(loteReferencia!=null){
				//referencias a mantener
				Collection<Referencia> referencias=new ArrayList<Referencia>(loteReferencia.getReferencias());
				
				if(loteReferencia.getId()==null || loteReferencia.getId()==0){
					Long id = null;
					loteReferencia.setId(id);
					//Agregado para el codigo///////////////
					LoteReferenciaServiceImp service = new LoteReferenciaServiceImp(HibernateControl.getInstance());
					loteReferencia.setCodigo(service.traerUltCodigoPorClienteAsp(loteRearchivo.getClienteAsp())+1L);
					//
					loteReferencia.getReferencias().clear();
					session.save(loteReferencia);
			
				}else{
					
					//actualizamos las modificadas
					
					for(Referencia ref : loteReferencia.getModificadas())
					{	
					    
						session.update(ref);
					}
										
					//borramos las referencias eliminadas
					
					
					loteReferencia = (LoteReferencia) session.get(LoteReferencia.class, loteReferencia.getId());
					Collection<Referencia> remove = CollectionUtils.subtract(loteReferencia.getReferencias(),referencias);
					for(Referencia ref:remove){
						if(ref.getId()!=null){
							ref.getElemento().setContenedor(null);
							session.update(ref.getElemento());
							//Lineas agregadas para el historico de elementos
							
							registrarHistoricoElementos("MS009ELE", ref.getElemento(),session,loteRearchivo.getUsuario_resp1());		
							////////////////////////////////////
							session.delete(ref);
							//Linea agregada para el historico de referencias
							registrarHistoricoReferencias("MS009REF", ref, session,loteReferencia,loteRearchivo.getUsuario_resp1());
							
							/////////////////////////////////////////////////
							
							loteReferencia.getReferencias().remove(ref);
						}
					}
					//referencias agregadas
					referencias = CollectionUtils.subtract(referencias,loteReferencia.getReferencias());
				}
				//guardamos las nuevas referencias
				for(Referencia ref : referencias)
				{
					Elemento contenedor = ref.getContenedor();
					if(contenedor!=null){
						contenedor = (Elemento)session.get(Elemento.class, contenedor.getId()); //refrescamos de la base
					}
					
					ref.setElemento((Elemento)session.get(Elemento.class, ref.getElemento().getId()));//refrescamos de la base
					
					ref.getElemento().setClienteEmp(loteReferencia.getClienteEmp());
					
					ref.setLoteReferencia(loteReferencia);
					
					
					ref.setClasificacionDocumental(ref.getClasificacionDocumental());
					
					
					if(contenedor!=null && ref.getElemento()!=contenedor){
						contenedor.setClienteEmp(loteReferencia.getClienteEmp());
						session.update(contenedor);
						//Lineas agregadas para el historico
						registrarHistoricoElementos("MS008ELE", contenedor,session,loteRearchivo.getUsuario_resp1());
						////////////////////////////////////
						ref.getElemento().setContenedor(contenedor);
					}
					session.update(ref.getElemento());
					//Lineas agregadas para el historico
					registrarHistoricoElementos("MS010ELE", ref.getElemento(),session,loteRearchivo.getUsuario_resp1());
					////////////////////////////////////
					//Lineas agregadas para el historico de referencias
					String mensaje = "";
					if(ref.getId()!=null)
						mensaje = "MS007REF";
					else
						mensaje = "MS008REF";
					
					registrarHistoricoReferencias(mensaje, ref, session, loteReferencia,loteRearchivo.getUsuario_resp1());
					/////////////////////////////////////////////////
					session.saveOrUpdate(ref);
					
					loteReferencia.getReferencias().add(ref);
				}
				session.update(loteReferencia);
			}
			//hacemos commit a la transacci�n para que 
			//se refresque la base de datos.
			tx.commit();
		} 
		catch (RuntimeException e) {
			//si ocurre alg�n error intentamos hacer rollback
		    
			if (tx != null && tx.isActive()) {
				try {
					tx.rollback();
		        } catch (HibernateException e1) {
		        	logger.error("no se pudo hacer rollback", e1);
		        }
		        logger.error("no se pudo guardar", e);
			}
		}finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesi�n", e);
        	}
        }
    }
	
	@Override
	public void eliminar(long idLoteRearchivo){
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesi�n
			session = getSession();
			//creamos una nueva transacci�n
			tx = session.getTransaction();
			tx.begin();
			LoteRearchivo loteRearchivo=(LoteRearchivo) session.get(LoteRearchivo.class, idLoteRearchivo);
			for(Rearchivo ref:loteRearchivo.getRearchivos()){
				session.delete(ref);
			}
			
			session.delete(loteRearchivo);
			//hacemos commit a los cambios para que se refresque la base de datos.
			tx.commit();
		} 
		catch (RuntimeException e) {
			//si ocurre alg�n error, se hace rollback a los cambios.
			if (tx != null && tx.isActive()) {
				try {
					tx.rollback();
		        } catch (HibernateException e1) {
		        	logger.error("no se pudo hacer rollback", e1);
		        }
				
		        logger.error("no se pudo guardar", e);
			}
		}finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesi�n", e);
        	}
        }
		
	}

	@Override
	public List<LoteRearchivo> obtenerPor(ClienteAsp clienteAsp,
			String codigoCliente, Integer codigoClasificacionDocumental,
			String descripcion,String tipo) {
		
		Session session = null;
		
	    try {
	    	//obtenemos una sesi�n
	    	session = getSession();
	        Criteria crit = session.createCriteria(getClaseModelo());
	        
	        crit.add(Restrictions.eq("clienteAsp", clienteAsp));
	        
        	if(codigoCliente!=null && !codigoCliente.isEmpty()){
        		crit.createCriteria("clienteEmp", "cli");
        		crit.add(Restrictions.eq("cli.codigo", codigoCliente));
        	}
        	
        	if(codigoClasificacionDocumental!=null){
        		crit.createCriteria("clasificacionDocumental", "cla");
        		crit.add(Restrictions.eq("cla.codigo", codigoClasificacionDocumental));
        	}
        	
        	if(tipo!=null && !tipo.isEmpty()  && !"Todos".equals(tipo)){
        		crit.add(Restrictions.eq("tipo", tipo));
        	}
        	
        	if(descripcion!=null && !descripcion.isEmpty()){
        		crit.add(Restrictions.ilike("descripcion", descripcion+"%"));
        	}
        	
	        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	        
	        return crit.list();
	    } catch (HibernateException e1) {
	    	logger.error("no se pudo listar todos ", e1);
	        return null;
	    }finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesi�n", e);
        	}
        }
	}
		
	private User obtenerUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
	private void registrarHistoricoReferencias(String mensaje, Referencia referencia,Session session,LoteReferencia lote){
		registrarHistoricoReferencias(mensaje, referencia,session,lote, null);
	}
	private void registrarHistoricoReferencias(String mensaje, Referencia referencia,Session session,LoteReferencia lote, User usuario){
	
		ReferenciaHistorico referenciaHis = new ReferenciaHistorico();
		referenciaHis.setIdReferencia(referencia.getId());
		//referenciaHis.setIdLoteReferencia(lote.getId());
		referenciaHis.setIdLoteReferencia(lote.getCodigo());
		if(referencia.getElemento()!=null)
		referenciaHis.setCodigoElemento(referencia.getElemento().getCodigo());
		if(referencia.getElemento().getContenedor()!=null)
		referenciaHis.setCodigoContenedor(referencia.getElemento().getContenedor().getCodigo());
		referenciaHis.setAccion(mensaje);
		referenciaHis.setFechaHora(new Date());
		if(usuario==null)
		referenciaHis.setUsuario(obtenerUser());
		else
		referenciaHis.setUsuario(usuario);
		referenciaHis.setClienteAsp(referencia.getElemento().getClienteAsp());
		referenciaHis.setCodigoCliente(lote.getClienteEmp().getCodigo());
		referenciaHis.setNombreCliente(lote.getClienteEmp().getRazonSocialONombreYApellido());
		session.save(referenciaHis);
	}
	
	private void registrarHistoricoElementos(String mensaje, Elemento elemento,Session session){
		registrarHistoricoElementos(mensaje, elemento, session, null);
	}
	
	private void registrarHistoricoElementos(String mensaje, Elemento elemento,Session session,User usuario){
		ElementoHistorico elementoHis = new ElementoHistorico();
		elementoHis.setCodigoElemento(elemento.getCodigo());
		elementoHis.setAccion(mensaje);
		elementoHis.setFechaHora(new Date());
		if(usuario==null)
			elementoHis.setUsuario(obtenerUser());
		else
			elementoHis.setUsuario(usuario);
		elementoHis.setClienteAsp(elemento.getClienteAsp());
		if(elemento.getClienteEmp()!=null){
			elementoHis.setCodigoCliente(elemento.getClienteEmp().getCodigo());
			elementoHis.setNombreCliente(elemento.getClienteEmp().getRazonSocialONombreYApellido());
		}
		elementoHis.setCodigoTipoElemento(elemento.getTipoElemento().getCodigo());
		elementoHis.setNombreTipoElemento(elemento.getTipoElemento().getDescripcion());
		session.save(elementoHis);
	}
}
