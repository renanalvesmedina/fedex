
(function(lmsDirectiveModule){

	lmsDirectiveModule.directive("lmsFilialPerfilEmpresaUsuario", [
		"SuggestFactory",
		function(SuggestFactory) {
			return SuggestFactory.createTemplate({
					suggest : "data as data.sgFilial + ' - ' + data.nmFilial + ' - ' + data.nmEmpresa for data",
					inputLabel : "sgFilial",
					minLength : 3,
					url : contextPath + "rest/municipios/filialPerfilSuggest/findFilialPerfilSuggest"
			});
		}
	]);

})(lmsDirectiveModule);
