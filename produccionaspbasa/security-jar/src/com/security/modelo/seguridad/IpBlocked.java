package com.security.modelo.seguridad;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
/**
 * 
 * @author Gabriel Mainero
 * @modified Ezequiel Beccaria
 *
 */
@Entity(name="ipsblocked")
public class IpBlocked{
	@Transient
	private Long id;
	@Transient
	private Date timeBlocked;
	@Transient
	private String ip;

	@Id @GeneratedValue(strategy=GenerationType.AUTO)        
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTimeBlocked() {
		return timeBlocked;
	}

	public void setTimeBlocked(Date timeBlocked) {
		this.timeBlocked = timeBlocked;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}
