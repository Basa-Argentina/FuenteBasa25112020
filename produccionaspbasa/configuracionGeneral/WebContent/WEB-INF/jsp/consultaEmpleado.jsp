<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<jsp:include page="styles.jsp" />
<title><spring:message code="formularioEmpleado.tituloConsulta"
		htmlEscape="true" /> - <spring:message code="general.empresa"
		htmlEscape="true" /> <spring:message code="general.ambiente"
		htmlEscape="true" /></title>
<script type="text/javascript" src="js/jquery-1.5.js"></script>
<script type="text/javascript" src="js/jquery.contextmenu.js"></script>
<script type="text/javascript" src="js/mavalos_jquery.tools.min.js"
	language="JavaScript"></script>
<script type="text/javascript" src="js/mavalos_consulta_empleado.js"></script>
<script type="text/javascript" src="js/ini.js" language="JavaScript"></script>
<script type="text/javascript" src="js/calendar_us.js"
	language="JavaScript"></script>
<script type="text/javascript" src="js/jquery.alerts.js"></script>
<script type="text/javascript" src="js/jquery.displaytag-ajax-1.2.js"></script>
<script type="text/javascript" src="js/httprequest.js"></script>
</head>
<body
	onload="mostrarErrores(${errores}); mostrarAvisos(${hayAvisos || hayAvisosNeg})">
	<div class="contextMenu" id="myMenu1">
		<ul>
			<li id="consultar" value=""><img src="images/consultar.png" /><font
				size="2"><spring:message code="botones.consultar"
						htmlEscape="true" /></font></li>
			<li id="modificar"><img src="images/modificar.png" /> <font
				size="2"><spring:message code="botones.modificar"
						htmlEscape="true" /></font></li>
			<li id="eliminar"><img src="images/eliminar.png" /> <font
				size="2"><spring:message code="botones.eliminar"
						htmlEscape="true" /></font></li>
		</ul>
	</div>
	<div id="contenedorGeneral">
		<jsp:include page="innerMenu.jsp" />
		<div class="contenido" align="left">
			<br style="font-size: medium;" />
			<fieldset style="border: none; text-align: left; width: 97%;">
				<legend>
					<font class="tituloForm" size="5"> <spring:message
							code="formularioEmpleado.tituloConsulta" htmlEscape="true" />
					</font>
				</legend>
				<br />
				<form:form action="filtrarEmpleado.html"
					commandName="empleadoBusqueda" method="get">
					<fieldset>
						<table width="100%">
							<thead>
								<tr>
									<th align="left" id="busquedaImg"><font
										style="color: #003090"> <spring:message
												code="textos.buscar" htmlEscape="true" />
									</font> <img id="busquedaImgSrcDown" src="images/skip_down.png"
										title="<spring:message code="textos.cerrarPanel" htmlEscape="true"/>">
										<img id="busquedaImgSrc" src="images/skip.png"
										style="DISPLAY: none"
										title="<spring:message code="textos.abrirPanel" htmlEscape="true"/>">
									</th>
								</tr>
							</thead>
						</table>
						<div style="background-color: #f1e87d; WIDTH: auto; DISPLAY: none"
							id="busquedaDiv" align="center">
							<table class="busqueda"
								style="width: 100%; background-color: white;">
								<tr>
									<td class="texto_ti"><spring:message
											code="formularioEmpleado.cliente" htmlEscape="true" /></td>
									<td class="texto_ti"><spring:message
											code="formularioEmpleado.username" htmlEscape="true" /></td>
									<td class="texto_ti"><spring:message
											code="formularioEmpleado.firstName" htmlEscape="true" /></td>
									<td class="texto_ti"><spring:message
											code="formularioEmpleado.surName" htmlEscape="true" /></td>
									<td rowspan="2" style="vertical-align: bottom;">
										<button name="buscar" class="botonCentrado" type="submit">
											<img src="<%=request.getContextPath()%>/images/buscar.png">
											<spring:message code="textos.buscar" htmlEscape="true" />
										</button>
									</td>
								</tr>
								<tr>
									<!-- Inicio	Filtro Nuevo -->
									<td class="texto_ti"><input type="hidden" id="clienteId"
										name="clienteId" value="" /> <input type="hidden"
										id="codigoEmpresa" name="codigoEmpresa"
										value="<c:out value="${codigoEmpresa}" default="" />" /> <input
										type="text" id="codigoCliente" name="codigoCliente"
										maxlength="6" style="width: 50px;"
										value="<c:out value="${empleadoBusqueda.codigoCliente}" default="" />"
										<c:if test="${accion == 'CONSULTA'}">
															readonly="readonly"
														</c:if> />
										&nbsp;&nbsp;
										<button type="button" id="botonPopupCliente"
											title="<spring:message code="textos.buscar" htmlEscape="true"/>"
											<c:if test="${accion == 'CONSULTA'}">
														disabled="disabled"
													</c:if>>
											<img src="<%=request.getContextPath()%>/images/buscar.png">
										</button>&nbsp;&nbsp; <label id="codigoClienteLabel"
										for="codigoCliente"> <c:out value="" default="" />
									</label></td>
									<td class="texto_ti"><input type="text" id="username"
										name="username" style="width: 150px;"
										value="<c:out value="${empleadoBusqueda.username}" default="" />" />
									</td>
									<td class="texto_ti"><input type="text"
										id="persona.nombre" name="persona.nombre"
										style="width: 150px;"
										value="<c:out value="${empleadoBusqueda.persona.nombre}" default="" />" />
									</td>
									<td class="texto_ti"><input type="text"
										id="persona.apellido" name="persona.apellido"
										style="width: 150px;"
										value="<c:out value="${empleadoBusqueda.persona.apellido}" default="" />" />
									</td>
								</tr>
							</table>
						</div>
					</fieldset>
				</form:form>
				<br style="font-size: xx-small;" />
				<fieldset>
					<display:table name="empleados" id="empleado" requestURI=""
						pagesize="7" sort="list" keepStatus="true">
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_id" value="${empleado.id}" />
						</display:column>
						<display:column class="hidden" headerClass="hidden">
							<input type="hidden" id="hdn_eliminar"
								value="<spring:message code="mensaje.eliminar" htmlEscape="true"/>" />
						</display:column>
						<display:column maxLength="10" style="width:40px;"
							property="codigo"
							titleKey="formularioEmpleado.datosEmpleado.codigo"
							sortable="true" class="celdaAlineadoIzquierda" />
						<display:column maxLength="60" style="width:200px;"
							property="usernameSinCliente"
							titleKey="formularioEmpleado.username" sortable="true"
							class="celdaAlineadoIzquierda" />
						<display:column property="persona.nombre" style="width:300px;"
							titleKey="formularioEmpleado.firstName" sortable="true"
							class="celdaAlineadoIzquierda" />
						<display:column property="persona.apellido" style="width:300px;"
							titleKey="formularioEmpleado.surName" sortable="true"
							class="celdaAlineadoIzquierda" />
						<display:column titleKey="formularioEmpleado.enabled"
							sortable="true" class="celdaAlineadoCentrado">
							<c:if test="${empleado.admin == true}">
								<img src="<%=request.getContextPath()%>/images/habilitado.png"
									onclick="javascript:jAlert('<spring:message code="notif.empleado.disabledAdminEmpleado" htmlEscape="true"/>')"
									title=<spring:message code="formularioEmpleado.adminEmpleado" htmlEscape="true"/>>
							</c:if>
							<c:if test="${empleado.admin != true}">
								<c:if
									test="${empleado.habilitado != null && empleado.habilitado == true}">
									<img src="<%=request.getContextPath()%>/images/habilitado.png"
										onclick="desHabilitarEmpleado('<c:out value="${empleado.id}"/>');"
										title=<spring:message code="formularioEmpleado.deshabilitar" htmlEscape="true"/>>
								</c:if>
								<c:if
									test="${empleado.habilitado == null || empleado.habilitado == false}">
									<img
										src="<%=request.getContextPath()%>/images/deshabilitado.png"
										onclick="habilitarEmpleado('<c:out value="${empleado.id}"/>');"
										title=<spring:message code="formularioEmpleado.habilitar" htmlEscape="true"/>>
								</c:if>
							</c:if>
						</display:column>
						<display:column class="celdaAlineadoCentrado">
							<img id="information"
								src="<%=request.getContextPath()%>/images/information.png"
								title="<spring:message code="textos.menuDesplegable" htmlEscape="true"/>">
						</display:column>
					</display:table>
					<div style="width: 100%" align="right">
						<button name="agregar" type="button" onclick="agregarEmpleado();"
							class="botonCentrado">
							<img src="<%=request.getContextPath()%>/images/add.png">
							<spring:message code="botones.agregar" htmlEscape="true" />
						</button>
					</div>
				</fieldset>
				<br style="font-size: xx-small;" />
				<div align="center">
					<button name="volver_atras" type="button" onclick="volver();"
						class="botonCentrado">
						<img src="<%=request.getContextPath()%>/images/volver4.png" />
						<spring:message code="botones.cerrar" htmlEscape="true" />
					</button>
				</div>
				<br style="font-size: xx-small;" />
			</fieldset>
		</div>
		<jsp:include page="footer.jsp" />
	</div>
	<div id="darkLayer" class="darkClass">&nbsp;</div>
	<div class="selectorDiv"></div>
	<jsp:include page="fieldErrors.jsp" />
	<jsp:include page="fieldAvisos.jsp" />
</body>
</html>