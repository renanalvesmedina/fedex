
(function(lmsDirectiveModule){
	
	lmsDirectiveModule.directive("adsmPerfil", [
		"SuggestFactory",
		function(SuggestFactory) {
			return SuggestFactory.createTemplate({
					suggest : "data as data.dsPerfil for data",
					inputLabel : "dsPerfil",
					minLength : 4,
					url : contextPath + "rest/perfil/perfilAdsm/findPerfilAdsmSuggest"
			});
		}
	]);
	
})(lmsDirectiveModule);
