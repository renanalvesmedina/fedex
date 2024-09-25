
(function(freteCarreteiroColetaEntregaModule){
	
	freteCarreteiroColetaEntregaModule.factory("ManterReciboFactory", [
		"$http", 
		function($http) {
        	return {
        		store: function(bean) {
        			return $http.post(contextPath+'rest/fretecarreteirocoletaentrega/manterRecibo/store', bean)
        				.then(function(response){
        					return response.data;
        				});
        		},
        		find: function(filter) {
        			return $http.post(contextPath+'rest/fretecarreteirocoletaentrega/manterRecibo/find', filter)
        				.then(function(response){
        					return response.data;
        				});
        		},
        		findAnexos: function(filter) {
        			return $http.post(contextPath+'rest/fretecarreteirocoletaentrega/manterRecibo/findAnexos', filter)
        				.then(function(response){
        					return response.data;
        				});
        		},
        		findAnexoById: function(id) {
        			return $http.get(contextPath+'rest/fretecarreteirocoletaentrega/manterRecibo/findAnexoById?id='+id)
        			.then(function(response){
        				return response.data;
        			});
        		},
        		removeAnexoReciboByIds: function(ids) {
        			return $http.post(contextPath+'rest/fretecarreteirocoletaentrega/manterRecibo/removeAnexoReciboByIds', ids)
        				.then(function(response){
        					return response.data;
        				});
        		},
        		findById: function(id) {
        			return $http.get(contextPath+'rest/fretecarreteirocoletaentrega/manterRecibo/findById?id=' + id)
        				.then(function(response){
        					return response.data;
        				});
        		},
        		findRim: function(filter) {
        			return $http.post(contextPath+'rest/fretecarreteirocoletaentrega/manterRecibo/findRim',filter)
        				.then(function(response){
        					return response.data;
        				});
        		},
        		findRimDados: function(filter) {
        			return $http.post(contextPath+'rest/fretecarreteirocoletaentrega/manterRecibo/findRimDados',filter)
        				.then(function(response){
        					return response.data;
        				});
        		},
        		findByIdProcesso: function(id) {
        			return $http.get(contextPath+'rest/fretecarreteirocoletaentrega/manterRecibo/findByIdProcesso?id='+id)
        				.then(function(response){
        					return response.data;
        				});
        		},
        		cancelarRecibo: function(id) {
        			return $http.get(contextPath+'rest/fretecarreteirocoletaentrega/manterRecibo/cancelarRecibo?id='+id)
        			.then(function(response) {
        				return response.data;
        			});
        		},
        		cancelarReciboComplementar: function(id) {
        			return $http.get(contextPath+'rest/fretecarreteirocoletaentrega/manterRecibo/cancelarReciboComplementar?id='+id)
        			.then(function(response) {
        				return response.data;
        			});
        		},
        		emitirRecibo: function(id) {
        			return $http.get(contextPath+'rest/fretecarreteirocoletaentrega/manterRecibo/emitirRecibo?id='+id)
        			.then(function(response) {
        				return response.data;
        			});
        		},
				findReciboSuggest : function(value, filter) {
					var url = contextPath + "rest/fretecarreteirocoletaentrega/manterRecibo/findReciboSuggest";
					var data = angular.extend({ value : value }, filter);
					return $http.post(url, data)
							.then(function(response) {
								return response.data;
							});
				},
          		report: function(filter) {
        			return $http.post(contextPath+'rest/fretecarreteirocoletaentrega/manterRecibo/report', filter)
        				.then(function(response){
        					return response.data;
        				});
        		},
        		populateMoedas: function() {
        			return $http.get(contextPath+'rest/fretecarreteirocoletaentrega/manterRecibo/populateMoedas')
        			.then(function(response) {
        				return response.data;
        			});
        		}
        	};
        }
	]);
	
})(freteCarreteiroColetaEntregaModule);

