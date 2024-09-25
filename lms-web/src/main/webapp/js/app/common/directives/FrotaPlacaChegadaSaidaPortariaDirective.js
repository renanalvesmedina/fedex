goog.require("app.angular.directives.lms");

goog.provide("app.angular.directives.lms.lmsPlacaFrota");

(function(lmsDirectiveModule){
	
	lmsDirectiveModule.directive("lmsFrotaPlacaEntradaSaidaPortaria", [
		"SuggestFactory",
		function(SuggestFactory) {
			return SuggestFactory.createTemplate({
					suggest : "data as data.nrFrota + ' - ' + data.nrIdentificador + ' - ' + data.tipoLabel + ' - ' + data.subTipoLabel + data.dsControleCarga for data",
					inputLabel : "nrIdentificador",
					minLength : 3,
					url : contextPath + "rest/portaria/frotaPlacaChegadaSaida/findSuggest"
			});
		}]);
	
})(lmsDirectiveModule);