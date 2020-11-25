<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>

<% 	
	String key = request.getParameter("mapa");
	String clase = request.getParameter("clase");
	if(key==null){
		key = (String)request.getAttribute("mapa");
		clase = (String)request.getAttribute("clase");
	}
	java.util.Map<String,Object> mapaPopup = (java.util.Map<String,Object>) request.getAttribute(key);
	request.setAttribute("tituloPopup",mapaPopup.get("tituloPopup"));
	request.setAttribute("textoBusqueda",mapaPopup.get("textoBusqueda")); 
	request.setAttribute("urlRequest",(String)mapaPopup.get("urlRequest")); 
	request.setAttribute("coleccionPopup",mapaPopup.get("coleccionPopup"));
	request.setAttribute("campos",mapaPopup.get("campos"));
	request.setAttribute("referenciaHtml",mapaPopup.get("referenciaHtml"));
	request.setAttribute("referenciaPopup",mapaPopup.get("referenciaPopup"));
	request.setAttribute("referenciaPopup2",mapaPopup.get("referenciaPopup2"));
	request.setAttribute("filterPopUp",mapaPopup.get("filterPopUp"));
	request.setAttribute("pageSize",mapaPopup.get("pageSize"));
	request.setAttribute("size",mapaPopup.get("size"));
%>

<script type="text/javascript" language="JavaScript">

var popupClaseNombre;
$(document).ready(function() {	
	popupClaseNombre = $(".clase").val();
	$("#buscarPorDescripcion").click(function(){
		var url = $(".urlBusqueda").val();
		var filterPopUp = $(".filterPopUp").val();
		buscarPopup(url, popupClaseNombre, filterPopUp);
	});
	initDisplayTable(popupClaseNombre,$(".filterPopUp").val());
});
function initDisplayTable(popupClaseNombre,filterPopUp){
	$(".selectorDiv").find('div.infoGrilla a, div.gotoPage a').click(function(){
		var url = $(this).attr('href');
		buscarPopupAjax(url,popupClaseNombre,filterPopUp);
		return false;
	});
}
function submitLink(link) {
	var href=link.href; 
	var beforePage=href.substring(0, href.lastIndexOf("-p=")+3 ); 
	var afterPage=href.substring(href.lastIndexOf("-p=")+3,link.href.length); 
	var indexOfCuestion= afterPage.lastIndexOf("?"); 
	if(indexOfCuestion==-1) 
		afterPage=""; 
	else 
		afterPage=afterPage.substring(afterPage.lastIndexOf("?"),afterPage.length);
	link.href=beforePage+document.getElementById("gotoPage").value+afterPage;
	return false;
}
function buscarPopup(url, clase, filterPopUp) {
	var texto = $(".inputSearch."+clase).val();
	//agrego el valor de busqueda a la url	
	if(url.indexOf("?") != -1)
		url = url+'&val='+texto;
	else
		url = url+'?val='+texto;

	if(filterPopUp != null && filterPopUp != "")
		url = url+'&codigo='+filterPopUp;
		
	if(! $(".selectorDiv").length){
		var selectorDiv = $("<div></div>");
		selectorDiv.addClass("selectorDiv");
		$(document.body).append(selectorDiv);
		$(".darkMiddleClass."+clase).remove();
	} 
	buscarPopupAjax(url,clase,filterPopUp);
}
function buscarPopupAjax(url,clase,filterPopUp){
	//realizo el request
	jQuery.ajax({
        url: url,
        success: function(data){
			$(this).html(data);
			$(this).html($(this).find(".darkMiddleClass."+clase));
			$(this).find(".displayTagDiv").displayTagAjax();
			var left=($(window).width()-$(this).find(".darkMiddleClass."+clase).width())/2;
			$(this).find(".darkMiddleClass."+clase).css("left",left);
			$('#darkLayer').show();
			$(this).find(".darkMiddleClass."+clase).show();
			$(this).find("#buscarPorDescripcion").click(function(){
				var url = $(".darkMiddleClass."+clase+" .urlBusqueda").val();
				var filterPopUp = $(".darkMiddleClass."+clase+" .filterPopUp").val();
				buscarPopup(url, popupClaseNombre, filterPopUp);
			});
			initDisplayTable(clase,$(".darkMiddleClass."+clase+" .filterPopUp").val());
        },
        data: ({"time":new Date().getTime()}),
        context: $(".selectorDiv")
    });
}
function cancelarPopup() {
	$(".urlBusqueda ."+popupClaseNombre).val("");
	popupOffDiv($('.'+popupClaseNombre),'darkLayer');
	var idCompHtml = $('#hdn_referenceHtml').val();
	$("#"+idCompHtml).focus();
}
function abrirPopup(claseNom){
	popupClaseNombre = claseNom;
	$(".displayTagDiv").displayTagAjax();
	var div = $('.'+claseNom);
	popupOnDiv(div,'darkLayer');
}
</script>

<div class="darkMiddleClass <%=clase%>">
	<fieldset>
		<table width="100%">
			<thead>
				<tr>
					<th align="left" id="loginImg"><font style="color: #003090"><spring:message
								code="${tituloPopup}" htmlEscape="true" /></font></th>
				</tr>
			</thead>
		</table>
		<div class="displayTagDiv <%=clase%>" style="text-align: center;">
			<spring:message code="textos.buscar" htmlEscape="true" />
			<input type="hidden" class="clase" value="<%=clase%>" /> <input
				type="hidden" class="urlBusqueda <%=clase%>" value="${urlRequest}" />
			<input type="hidden" class="filterPopUp <%=clase%>"
				value="${filterPopUp}" /> <input type="text"
				class="inputSearch <%=clase%>" style="width: 300px;"
				value="${textoBusqueda}" /> &nbsp;&nbsp;
			<button name="buscar" type="button" id="buscarPorDescripcion"
				class="botonCentrado">
				<img src="<%=request.getContextPath()%>/images/buscar.png">
				<spring:message code="textos.buscar" htmlEscape="true" />
			</button>
			<br style="font-size: xx-small;" /> <br style="font-size: xx-small;" />
			<div style="overflow: scroll; height: 200px;">
				<display:table name="${coleccionPopup}" id="objetoColeccion"
					requestURI="${urlRequest}" pagesize="${size}"
					<%-- partialList="true" --%> size="${size}">
					<display:setProperty name="paging.banner.some_items_found"
						value="<div class='infoGrilla'>{0} {1} encontrados, mostrando de {2} a {3}.</div>" />
					<display:column class="hidden" headerClass="hidden">
						<input type="hidden" id="hdn_referenceHtml"
							value="${referenciaHtml}" />
					</display:column>
					<c:forEach items="${campos}" var="campoDisplayTag">
						<c:if test="${campoDisplayTag.property == referenciaPopup}">
							<display:column property="${campoDisplayTag.property}"
								class="hidden hdn_reference" headerClass="hidden" />
						</c:if>
						<c:if test="${campoDisplayTag.property == referenciaPopup2}">
							<display:column property="${campoDisplayTag.property}"
								class="hidden hdn_reference2" headerClass="hidden" />
						</c:if>
						<c:if test="${campoDisplayTag.hidden == false}">
							<display:column property="${campoDisplayTag.property}"
								titleKey="${campoDisplayTag.titleKey}" />
						</c:if>
						<c:if test="${campoDisplayTag.hidden == true}">
							<display:column property="${campoDisplayTag.property}"
								sortable="true" headerClass="hidden" class="hidden" />
						</c:if>
					</c:forEach>
				</display:table>
			</div>
			<br style="font-size: xx-small;" />
			<button name="cancelar" type="button" onclick="cancelarPopup();"
				class="botonCentrado">
				<img src="<%=request.getContextPath()%>/images/cancelar.png">
				<spring:message code="botones.cancelar" htmlEscape="true" />
			</button>
		</div>
	</fieldset>
</div>