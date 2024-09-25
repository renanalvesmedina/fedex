
(function(lmsDirectiveModule){
	
	lmsDirectiveModule.directive("lmsUsuarioLms", [
		"SuggestFactory",
		function(SuggestFactory) {
			return SuggestFactory.createTemplate({
					suggest : "data as data.nrMatricula + ' - ' + data.nmUsuario for data",
					inputLabel : "nrMatricula",
					minLength : 4,
					url : contextPath + "rest/configuracoes/usuarioLms/findUsuarioLmsSuggest"
			});
		}
	]);
	
})(lmsDirectiveModule);



