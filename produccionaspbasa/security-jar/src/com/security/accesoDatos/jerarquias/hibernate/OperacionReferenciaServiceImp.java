/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 11/07/2011
 */
package com.security.accesoDatos.jerarquias.hibernate;


import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.accesoDatos.jerarquias.interfaz.OperacionReferenciaService;
import com.security.modelo.jerarquias.Operacion;
import com.security.modelo.jerarquias.OperacionElemento;

/**
 * @author Gabriel Mainero
 *
 */
@Component
public class OperacionReferenciaServiceImp extends GestorHibernate<OperacionElemento> implements OperacionReferenciaService{
	private static Logger logger=Logger.getLogger(OperacionReferenciaServiceImp.class);
	
	@Autowired
	public OperacionReferenciaServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	

	@Override
	public Class<OperacionElemento> getClaseModelo() {
		return OperacionElemento.class;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<OperacionElemento> listarOperacionReferenciaFiltradas(OperacionElemento operacionReferencia, Operacion operacion){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	if(operacionReferencia!=null){
        		if(operacionReferencia.getEstado()!=null && !operacionReferencia.getEstado().equals("Todos"))
        			crit.add(Restrictions.eq("estado", operacionReferencia.getEstado())); 			
        	}
        	if(operacion != null)
        		crit.add(Restrictions.eq("operacion", operacion));
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            return crit.list();
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo listar ", hibernateException);
	        return null;
        }finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
    }
	
}
