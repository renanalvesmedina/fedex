
var openMacroZona = function(modalService, $event, idMacroZona) {

	$event.preventDefault();
	
	/* Configuracao da modal macro zona */
	var MacroZonaInstanceCtrl = ["$scope",
	                             "$modalInstance",
	                             "TableFactory",
	                             "MacroZonaFactory",
	                             function ($scope, $modalInstance, TableFactory, MacroZonaFactory) {    			
		
		$scope.consultando = true;
		
		$scope.title = $scope.$eval("'macroZona' | translate");
		$scope.innerTemplate = contextPath+"js/app/portaria/popupmacrozona/view/popupMacroZona.html";

		MacroZonaFactory.findById(idMacroZona).then(function(data) {
			$scope.dados = data;
			$scope.consultando = false;
		}, function() {
			$scope.consultando = false;
		});
		$scope.volumesTableParams = new TableFactory({
			service: MacroZonaFactory.findVolumes,
			remotePagination: true
		});
		$scope.dispositivosTableParams = new TableFactory({
			service: MacroZonaFactory.findDispositivos,
			remotePagination: true
		});
		
		$scope.loadVolumes = function() {
			if (!$scope.isVolumesLoaded) {
				$scope.volumesTableParams.load({macroZona:{id: $scope.dados.idMacroZona}});
				$scope.isVolumesLoaded = true;
			}
		};
		
		$scope.loadDispositivos = function() {
			if (!$scope.isDispositivosLoaded) {
				$scope.dispositivosTableParams.load({macroZona:{id: $scope.dados.idMacroZona}});
				$scope.isDispositivosLoaded = true;
			}
		};

		$scope.close = function () {
			$modalInstance.close();
		};
	}];
	
	modalService.open({windowClass: "clmListaPopup", controller: MacroZonaInstanceCtrl});

};