
var ExemploTelaComponentesCustomTableController = [
	"$rootScope",
	"$scope",
	"TableFactory",
	"modalService",
 	function($rootScope, $scope, TableFactory, modalService) {

		function initTable() {
			$scope.customTableParams = new TableFactory({
				service: $scope.rest.doPost,
				method: "findDataForCustomTable"
			});

			$rootScope.showLoading = true;
			$scope.customTableParams.clear();
			$scope.customTableParams.load({}).then(function() {
				$rootScope.showLoading = false;
			},function() {
				$rootScope.showLoading = false;
			});

		}

		initTable();

		$scope.selectRow = function(row) {
			modalService.open({title: "Seleção de linha", message: "Empresa selecionada: " + row.nmPessoa});
		};

		$scope.detail = function(row, event) {

			var modalController = ["$scope", "$modalInstance", function($scope, $modalInstance) {
				$scope.empresa = row;
				$scope.title = "Detalhamento";
				$scope.innerTemplate = contextPath+"js/app/recepcaodescarga/exemplotelacomponentes/view/exemploTelaComponentesModal.html";
				$scope.close = function() {
					$modalInstance.dismiss("cancel");
				};
			}];

			modalService.open({
				controller: modalController,
				windowClass: 'customTableModal'
			});
			event.stopPropagation();

		};

	}
];
