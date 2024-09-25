var ReajusteTabelaClienteAbaReajusteController = [
	"$scope", 
	"$http",
	"$rootScope",
	function($scope, $http, $rootScope) {

		$scope.tiposGeneralidadeObrig = [ "Pedagio","Gris","Minimo Gris","TDE","Minimo TDE","TRT","Minimo TRT"  ];
		$scope.tiposFretePeso = [ "Advalorem", "Advalorem2", "Tonelada Frete", "Frete Peso", "Frete Volume", "Tarifa Minima", "Tarifa Especifica", "Referencia", "Minimo Frete", "Minimo Frete Quilo", "Minimo Frete Peso", "Minimo Progressivo" ];
		
		function calculateValue(value, orginalValue) {
			if(value === null){
				value = orginalValue;
			}
			return ((value * 100)/ orginalValue) - 100 ;
		}
		
		function calculatePercent(percentValue, orginalValue) {
			if(percentValue === null){
				percentValue = 0;
			}
   			return orginalValue + ((orginalValue * percentValue) / 100);
   	    }
		
		$scope.calcValue = function(list, index, fieldOriginal, fieldCalculado, fieldPercentual){
			var fieldCalculadoComPrecisao = fieldCalculado + "COM_PRECISAO";
			angular.forEach(list, function(value, key) {
   				var json = angular.fromJson(value);
   				if( parseInt(index) === parseInt(key) && json[fieldCalculado] !== json[fieldOriginal] ){
   					if(!(round_number(json[fieldCalculadoComPrecisao],2) === json[fieldCalculado])){
   						json[fieldCalculadoComPrecisao] = json[fieldCalculado];
   					}
   					json[fieldPercentual] = calculateValue(json[fieldCalculadoComPrecisao] , json[fieldOriginal]);
   				}
   			});
		};
		
		function round_number(num, dec) {
		    return Math.round(num * Math.pow(10, dec)) / Math.pow(10, dec);
		}
		
		$scope.calcPercentual = function(list, index, fieldOriginal, fieldCalculado, fieldPercentual){
			angular.forEach(list, function(value, key) {
   				var json = angular.fromJson(value);
   				if( parseInt(index) === key ){
					json[fieldCalculado] = calculatePercent(json[fieldPercentual] , json[fieldOriginal]);
   				}
   			});
		};
		
		$scope.calcHeaderPercentual = function(list, fieldHeader, fieldOriginal, fieldCalculado, fieldPercentual){
			if( fieldHeader !== null ){
				angular.forEach(list, function(value, key) {
	   				var json = angular.fromJson(value);
	   				if(json[fieldPercentual] !== null && json[fieldOriginal] !== 0 ){
	   					json[fieldPercentual] = fieldHeader;
	   					json[fieldCalculado]  = calculatePercent(fieldHeader , json[fieldOriginal]);
	   				}
	   			});
			}
		};
		
		$scope.calcHeaderValue = function(list, fieldHeader, fieldOriginal, fieldCalculado, fieldPercentual){
			if( fieldHeader !== null ){
				angular.forEach(list, function(value, key) {
	   				var json = angular.fromJson(value);
	   				if(json[fieldCalculado] !== null && json[fieldOriginal] !== 0){
	   					json[fieldCalculado]  = fieldHeader;
	   					json[fieldPercentual] = calculateValue(fieldHeader , json[fieldOriginal]);
	   				}
	   			});
			}
		};
		
		$scope.loadGenaralidadeObrig = function(){
			$rootScope.showLoading = true; 
			$scope.rest.doPost("listGeneralidadesObrig", $scope.data).then(function(response) {

				mantemValorComPrecisao(response, "PEDAGIO_CALCULADO");
				mantemValorComPrecisao(response, "GRIS_CALCULADO");
				mantemValorComPrecisao(response, "GRIS_MIN_CALCULADO");
				mantemValorComPrecisao(response, "TRT_MIN_CALCULADO");
				mantemValorComPrecisao(response, "TDE_MIN_CALCULADO");
				mantemValorComPrecisao(response, "TRT_CALCULADO");
				mantemValorComPrecisao(response, "TDE_CALCULADO");
        		$scope.listGenaralidadeObrig = response;
        		
        		$rootScope.showLoading = false;
        		
			}, function() {
  				$rootScope.showLoading = false; 
			});
		};

   	    function mantemValorComPrecisao(list, fieldCalculado, fieldCalculadoComPrecisao){
   	    	var fieldCalculadoComPrecisao = fieldCalculado + "COM_PRECISAO";
			angular.forEach(list, function(value, key) {
   				var json = angular.fromJson(value);
   				json[fieldCalculadoComPrecisao] = json[fieldCalculado];
   			});
   	    };

		
		$scope.salvarGeneralidadeObrig = function(){
			$rootScope.showLoading = true; 
			$scope.rest.doPost("saveGeneralidadesObrig", $scope.listGenaralidadeObrig).then(function(response) {
				toastr.success("Sucesso");
        		$rootScope.showLoading = false;
        		
			}, function() {
  				$rootScope.showLoading = false; 
			});
		};
		
		$scope.loadFretePeso = function(){
			$rootScope.showLoading = true; 
			$scope.rest.doPost("listFretePeso", $scope.data).then(function(response) {

				mantemValorComPrecisao(response, "ADVALOREN_CALCULADO");
				mantemValorComPrecisao(response, "ADVALOREN2_CALCULADO");
				mantemValorComPrecisao(response, "TONELADA_FRETE_CALCULADO");
				mantemValorComPrecisao(response, "FRETE_PESO_CALCULADO");
				mantemValorComPrecisao(response, "FRETE_VOLUME_CALCULADO");
				mantemValorComPrecisao(response, "TARIFA_MINIMA_CALCULADO");
				mantemValorComPrecisao(response, "TARIFA_ESPECIFICA_CALCULADO");
				mantemValorComPrecisao(response, "REFERENCIA_CALCULADO");
				mantemValorComPrecisao(response, "MINIMO_FRETE_CALCULADO");
				mantemValorComPrecisao(response, "MINIMO_FRETE_QUILO_CALCULADO");
				mantemValorComPrecisao(response, "MINIMO_FRETE_PESO_CALCULADO");
				mantemValorComPrecisao(response, "MINIMO_PROGRAMADO_CALCULADO");
				$scope.listFretePeso = response;
        		$rootScope.showLoading = false;
        		
			}, function() {
  				$rootScope.showLoading = false; 
			});
		};
		
		$scope.salvarFretePeso = function(){
			$rootScope.showLoading = true; 
			$scope.rest.doPost("saveFretePeso", $scope.listFretePeso).then(function(response) {
				toastr.success("Sucesso");
        		$rootScope.showLoading = false;
        		
			}, function() {
  				$rootScope.showLoading = false; 
			});
		};
		
		$scope.loadGenaralidades = function(){
			$rootScope.showLoading = true; 
			$scope.rest.doPost("listGeneralidade", $scope.data).then(function(response) {

				mantemValorComPrecisao(response, "CALCULADO");
				mantemValorComPrecisao(response, "MINIMO_CALCULADO");
				
				$scope.listGeneralidades = response;
        		$rootScope.showLoading = false;
        		
			}, function() {
  				$rootScope.showLoading = false; 
			});
		};
		
		$scope.salvarGeneralidades = function(){
			$rootScope.showLoading = true; 
			$scope.rest.doPost("saveGeneralidade", $scope.listGeneralidades).then(function(response) {
				toastr.success("Sucesso");
        		$rootScope.showLoading = false;
        		
			}, function() {
  				$rootScope.showLoading = false; 
			});
		}
		
		$scope.loadTaxa = function(){
			$rootScope.showLoading = true; 
			$scope.rest.doPost("listTaxaCliente", $scope.data).then(function(response) {
				mantemValorComPrecisao(response, "TAXA_CALCULADO");
				mantemValorComPrecisao(response, "EXCEDENTE_CALCULADO");
				$scope.listTaxa = response;
        		$rootScope.showLoading = false;
        		
			}, function() {
  				$rootScope.showLoading = false; 
			});
		};
		
		$scope.salvarTaxa = function(){
			$rootScope.showLoading = true; 
			$scope.rest.doPost("saveTaxaCliente", $scope.listTaxa).then(function(response) {
				toastr.success("Sucesso");
        		$rootScope.showLoading = false;
        		
			}, function() {
  				$rootScope.showLoading = false; 
			});
		};
		
		
		$scope.loadServAdicional = function(){
			$rootScope.showLoading = true; 
			$scope.rest.doPost("listServAdicionalCliente", $scope.data).then(function(response) {
				mantemValorComPrecisao(response, "CALCULADO");
				mantemValorComPrecisao(response, "CALCULADO_MINIMO");
				$scope.listServAdicional = response;
        		$rootScope.showLoading = false;
        		
			}, function() {
  				$rootScope.showLoading = false; 
			});
		};
		
		$scope.salvarServAdicional = function(){
			$rootScope.showLoading = true; 
			$scope.rest.doPost("saveServAdicionalCliente", $scope.listServAdicional).then(function(response) {
				toastr.success("Sucesso");
        		$rootScope.showLoading = false;
        		
			}, function() {
  				$rootScope.showLoading = false; 
			});
		};
		
		
		$scope.loadGenaralidadeObrig();
	}
];