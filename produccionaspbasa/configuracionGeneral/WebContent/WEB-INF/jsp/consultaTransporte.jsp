<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page buffer = "32kb" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">	
		<jsp:include page="styles.jsp"/>
		<title>
			<spring:message code="formularioTransporte.titulo" htmlEscape="true"/>
		</title>
		<script type="text/javascript" src="js/jquery-1.5.js"></script>
		<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
		<script type="text/javascript" src="js/mavalos_jquery.tools.min.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/mavalos_consulta_transporte.js"></script>
		<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/calendar_us.js" language="JavaScript"></script>
		<script type="text/javascript" src="js/jquery.alerts.js"></script>		
		<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
		<script type="text/javascript" src="js/httprequest.js"></script>
	</head>
	<body onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
		<div class="contextMenu" id="myMenu1">
		    <ul>	 
		      <li id="consultar" value=""><img src="images/consultar.png" /><font size="2"><spring:message code="botones.consultar" htmlEscape="true"/></font></li>	 
		      <li id="modificar"><img src="images/modificar.png" /> <font size="2"><spring:message code="botones.modificar" htmlEscape="true"/></font></li> 
		      <li id="eliminar"><img src="images/eliminar.png" /> <font size="2"><spring:message code="botones.eliminar" htmlEscape="true"/></font></li>
		    </ul> 	 
	  	</div> 
	  	<div id="contenedorGeneral">
			<jsp:include page="innerMenu.jsp"/>
			<div class="contenido" align="left">
			<br style="font-size: medium;"/>
			<fieldset style="border: none; text-align: left; width: 97%;">
					<legend>
			        	<font class="tituloForm" size="5">
			        		<spring:message code="formularioTransporte.titulo" htmlEscape="true"/>		        		
		        		</font>		  
					</legend>
					<br/>
					<form:form action="filtrarTransporte.html" commandName="transporteBusqueda" method="post">
					<input type="hidden" id="clienteId" name="clienteId" value="<c:out value="${clienteId}" default="" />"/>
						<fieldset>
							<table width="100%">
				            	<thead>
					            	<tr>
					              		<th align="left" id="busquedaImg" >						  
							        		<font style="color:#003090">
							        			<spring:message code="textos.buscar" htmlEscape="true"/>
							        		</font>
							        		<img id="busquedaImgSrcDown" src="images/skip_down.png" title="<spring:message code="textos.cerrarPanel" htmlEscape="true"/>">
							        		<img id="busquedaImgSrc" src="images/skip.png"  style="DISPLAY: none" title="<spring:message code="textos.abrirPanel" htmlEscape="true"/>">					 
					              		</th>
								 	</tr>
								</thead>
							</table>
							<div style="background-color: #f1e87d; WIDTH: auto; DISPLAY: none"  id="busquedaDiv" align="center">
								<table class="busqueda" style="width: 100%; background-color: white;">
									<tr>
										<td class="texto_ti">
											<spring:message code="formularioTransporte.buscar.empresa" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<spring:message code="formularioTransporte.buscar.codigoMin" htmlEscape="true"/>
										</td>
										<td class="texto_ti">
											<spring:message code="formularioTransporte.buscar.codigoMax" htmlEscape="true"/>
										</td>
										<td rowspan="2" style="vertical-align: bottom;">
											<button name="buscar" class="botonCentrado" type="submit">
												<img src="<%=request.getContextPath()%>/images/buscar.png"> 
												<spring:message code="textos.buscar" htmlEscape="true"/>								
											</button>
										</td>
									</tr>
									<tr>
										<td class="texto_ti">
													<input type="text" id="codigoEmpresa"
														name="codigoEmpresa" maxlength="6" style="width: 50px;"
														value="<c:out value="${transporteBusqueda.codigoEmpresa}" default="" />"
														<c:if test="${accion == 'CONSULTA'}">
																		readonly="readonly"
																	</c:if> />
														&nbsp;&nbsp;
													<button type="button"
														onclick="abrirPopup('empresasPopupMap');"
														title="<spring:message code="textos.buscar" htmlEscape="true"/>"
														<c:if test="${accion == 'CONSULTA'}">
																disabled="disabled"
															</c:if>>
														<img src="<%=request.getContextPath()%>/images/buscar.png">
													</button>&nbsp;&nbsp; 
													<label id="codigoEmpresaLabel"
														for="codigoEmpresa"> <c:out
																value="${transporteBusqueda.empresa.descripcion}"
																default="" /> 
													</label>	
												</td>		
										<td class="texto_ti">
											<input type="text" id="codigoMin" maxlength="3" name="codigoMin" style="width: 200px;"
												value="<c:out value="${transporteBusqueda.codigoMin}" default="" />"/>
										</td>										
										<td class="texto_ti">
											<input type="text" id="codigoMax" maxlength="3" name="codigoMax" style="width: 200px;" 
												value="<c:out value="${transporteBusqueda.codigoMax}" default="" />"/>
										</td>					
																																		
									</tr>
								</table>
							</div>	
						</fieldset>
					</form:form>	
					<br style="font-size: xx-small;"/>
					<fieldset>
						<display:table name="transportes" id="transporte" requestURI="mostrarTransporte.html" pagesize="20" sort="list" keepStatus="true">
							<display:column class="hidden" headerClass="hidden">
							    <input type="hidden" id="hdn_id" value="${transporte.id}"/>
			              	</display:column>		
			              	<display:column class="hidden" headerClass="hidden">
						    	<input type="hidden" id="hdn_eliminar" value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>"/>
		           		   	</display:column>
		           		   	<display:column property="empresa.descripcion" titleKey="formularioTransporte.datosTransporte.empresa" sortable="true" class="celdaAlineadoIzquierda"/>
							<display:column property="codigo" titleKey="formularioTransporte.datosTransporte.codigo" sortable="true" class="celdaAlineadoIzquierda"/>
						  	<display:column property="descripcion" maxLength="30" titleKey="formularioTransporte.datosTransporte.descripcion" sortable="true" class="celdaAlineadoIzquierda"/>
						  	<display:column  titleKey="formularioTransporte.datosTransporte.habilitado" sortable="true" class="celdaAlineadoIzquierda">
						  		<c:if test="${transporte.habilitado == true }">
						  			<spring:message code="formularioTransporte.habilitado" htmlEscape="true" ></spring:message>
						  		</c:if>
						  		<c:if test="${transporte.habilitado == false }">
						  			<spring:message code="formularioTransporte.deshabilitado" htmlEscape="true" ></spring:message>
						  		</c:if>
						  	</display:column>						  							  	
						  	<display:column class="celdaAlineadoCentrado">
					  	 		<img id="information" src="<%=request.getContextPath()%>/images/information.png" title="<spring:message code="textos.menuDesplegable" htmlEscape="true"/>">
							</display:column>	
						</display:table> 
						<div style="width: 100%" align ="right">
							<button name="agregar" type="button" onclick="agregar();" class="botonCentrado">
								<img src="<%=request.getContextPath()%>/images/add.png" > 
								<spring:message code="botones.agregar" htmlEscape="true"/>  
							</button>
						</div>
					</fieldset>
					<br style="font-size: xx-small;"/>
					<div align="center">
						<button name="volver_atras" type="button"  onclick="volver();" class="botonCentrado">
							<img src="<%=request.getContextPath()%>/images/volver4.png"/> 
							<spring:message code="botones.cerrar" htmlEscape="true"/>  
						</button>						
					</div>	
					<br style="font-size: xx-small;"/>
				</fieldset>	
			</div>
			<jsp:include page="footer.jsp"/>			
		</div>
		<div id="darkLayer" class="darkClass">&nbsp;</div>		
		<jsp:include page="fieldErrors.jsp"/>
		<jsp:include page="fieldAvisos.jsp"/>
		<jsp:include page="popupBusqueda.jsp">
			<jsp:param name="mapa" value="empresasPopupMap" /> 
			<jsp:param name="clase" value="empresasPopupMap" /> 
		</jsp:include>
	</body>
</html>