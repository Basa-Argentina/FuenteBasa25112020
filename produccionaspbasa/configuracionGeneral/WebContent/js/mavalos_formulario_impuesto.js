$(document).ready(function() {			
	//Tooltips
	$("img[title]").tooltip();
	
	//Slide 1
	$('#busquedaDiv').attr({'style':'display:block'});
	$('#busquedaImg').click(function() {
		$('#busquedaDiv').slideToggle('slideUp');
		$('#busquedaImgSrcDown').slideToggle('slideUp');
		$('#busquedaImgSrc').slideToggle('slideUp');
	});	

	//bindings keyup caracteres alfanumericos con espacios
	$('.alphaNumericSpace').keyup(function() {
		if (this.value.match(/[^a-zA-Z0-9 ]/g)) {
            this.value = this.value.replace(/[^a-zA-Z0-9 ]/g, '');
        }
	});
	//bindings keyup caracteres alfanumericos sin espacios
	$('.alphaNumeric').keyup(function() {
		if (this.value.match(/[^a-zA-Z0-9]/g)) {
			this.value = this.value.replace(/[^a-zA-Z0-9]/g, '');
		}
	});
	//bindings keyup caracteres numericos
	$('.numeric').keyup(function() {
		if (this.value.match(/[^0-9 ]/g)) {
            this.value = this.value.replace(/[^0-9]/g, '');
        }
	});
	//bindings keyup caracteres alfabeticos
	$('.alphabetic').keyup(function() {
		if (this.value.match(/[^a-zA-Z ]/g)) {
            this.value = this.value.replace(/[^a-zA-Z ]/g, '');
        }
	});

});
//Metodo que se ejecuta al terminar de cargar todos los componentes de la pagina
function volver(){
	history.back();
}
function volverCancelar(){
	document.location="mostrarImpuesto.html";
}
function guardarYSalir(){
 	document.forms[0].submit();
}