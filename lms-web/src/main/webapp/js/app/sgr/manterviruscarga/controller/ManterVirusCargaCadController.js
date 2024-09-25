
var ManterVirusCargaCadController = [
		"$rootScope",
		"$scope",
		"$stateParams",
		"$state",
		function($rootScope, $scope, $stateParams, $state) {
			$scope.setConstructor({
				rest : "/sgr/virusCarga"
			});

			$scope.initializeAbaCad = initializeAbaCad;
			$scope.findChave = findChave;
			$scope.clearData = clearData;
			$scope.store = store;

			$scope.initializeAbaCad($stateParams);

			function initializeAbaCad(params) {
				if ($stateParams.id) {
					$scope.populateDataById($stateParams.id);
				} else {
					$scope.setData({});
					$scope.data.dhAtivacao = Utils.Date.formatMomentAsISO8601(moment());
				}
			}

			function findChave(modelValue) {
				if (!$scope.data.nrChave) {
					return;
				}
				$rootScope.showLoading = true;
				$scope.rest.doGet("findChave/" + $scope.data.nrChave).then(function(response) {
					$scope.data.dsSerie = response.dsSerie;
					$scope.data.nrNotaFiscal = response.nrNotaFiscal;
					$scope.data.cliente = response.cliente;
					$scope.data.cliente.nmPessoa = response.cliente.nmPessoa;
					$rootScope.showLoading = false;
					$rootScope.clearAlerts();
					$('#volume').focus();
				}, function(error) {
					//delete $scope.data.nrChave;
					$rootScope.showLoading = false;
				});
			}

			function clearData() {
				$scope.setData({});
				$state.transitionTo($state.current, {}, {
					reload : false,
					inherit : false,
					notify : true
				});
			}

			function store() {
				$rootScope.showLoading = true;
				$scope.rest.doPost("", $scope.data).then(function(response) {
					$rootScope.showLoading = false;
					$state.transitionTo($state.current, {
						reload : false,
						inherit : false,
						notify : true
					});
					$scope.showSuccessMessage();
				}, function() {
					$rootScope.showLoading = false;
				});
			}
		}

];
