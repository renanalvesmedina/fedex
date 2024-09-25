var ManterTerritorioCadController = [
	"$rootScope",
	"$scope",
	"$stateParams",
	"typeTab",
	"editTabState",
	"TableFactory",
	"$filter",
	function($rootScope, $scope, $stateParams, typeTab, editTabState, TableFactory, $filter) {
		$scope.initAbas($stateParams, typeTab, editTabState);

		$scope.listDetailTableParams = new TableFactory({
			service: $scope.rest.doPost,
			method: "find"
		});

		function atualizarTable() {
			if ($scope.data.filial || $scope.data.regional) {
				$rootScope.showLoading = true;
				$scope.listDetailTableParams.load({filial:$scope.data.filial,regional: $scope.data.regional})
					.then(function() {})
					['finally'](function() {
						$rootScope.showLoading = false;
					});
			} else {
				$scope.listDetailTableParams.clear();
			}
		}

		$scope.salvar = function () {

			if ((!$scope.data.filial) && (!$scope.data.regional)) {
				$scope.showMessage("LMS-01274", MESSAGE_SEVERITY.WARNING);
				return;
			}

			$rootScope.showLoading = true;

			$scope.rest.doPost("", $scope.data)
				.then(function(response) {
					atualizarTable();
					$scope.showSuccessMessage();

					if (!$scope.data.id) {
						$scope.data.nmTerritorio = null;
						$('form').removeClass('submitted');
					}
				})
				['finally'](function() {
					$rootScope.showLoading = false;
				});
		};

		$scope.selectRegional = function() {
			$scope.data.filial = null;
		};

		$scope.$watch('data.regional.idRegional', function() {
			atualizarTable();
		});

		$scope.$watch('data.filial.idFilial', function() {

			if ($scope.data.filial && $scope.data.filial.idFilial) {
				$scope.rest.doPost("findRegionalByFilial", $scope.data.filial.idFilial)
					.then(function(response) {
						$scope.data.regional = response;
					})
					['finally'](function() {
						$rootScope.showLoading = false;
						atualizarTable();
					});

			} else {
				atualizarTable();
			}
		});

		$scope.$watch('data.nmTerritorio', function (value) {
			$scope.data.nmTerritorio = $filter('uppercase')(value);
		});

		atualizarTable();
	}
];
