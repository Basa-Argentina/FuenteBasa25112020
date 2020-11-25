/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/06/2011
 */
package com.security.accesoDatos.configuraciongeneral.hibernate;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.CambioEtiquetaService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.configuraciongeneral.CambioEtiqueta;


/**
 * @author X
 *
 */
@Component
public class CambioEtiquetaServiceImp extends GestorHibernate<CambioEtiqueta> implements CambioEtiquetaService{
	private static Logger logger=Logger.getLogger(CambioEtiquetaServiceImp.class);
	
	@Autowired
	public CambioEtiquetaServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<CambioEtiqueta> getClaseModelo() {
		return CambioEtiqueta.class;
	}

	
	@Override
	public List<CambioEtiqueta> listarCambioEtiquetaPorCodigoLectura(Long codigoLectura){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	if(codigoLectura!=null){
        		crit.add(Restrictions.eq("idLectura", codigoLectura));
        	}
        	
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
