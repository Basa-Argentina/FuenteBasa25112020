/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/06/2011
 */
package com.security.accesoDatos.configuraciongeneral.hibernate;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.LeeCodigoBarraService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.configuraciongeneral.LeeCodigoBarra;

/**
 * @author X
 *
 */
@Component
public class LeeCodigoBarraServiceImp extends GestorHibernate<LeeCodigoBarra> implements LeeCodigoBarraService{
	private static Logger logger=Logger.getLogger(LeeCodigoBarraServiceImp.class);
	
	@Autowired
	public LeeCodigoBarraServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<LeeCodigoBarra> getClaseModelo() {
		return LeeCodigoBarra.class;
	}

}
