
var ManterBlacklistClientesController = [
 		"$scope",
		"$rootScope",
		"$stateParams",
		"TableFactory",
		"modalService",
		function($scope, $rootScope, $stateParams, TableFactory, modalService) {
 			$scope.idFornecedorEscolta = $stateParams.id;
 			$scope.find = find;
			$scope.detail = detail;
			$scope.removeByIds = removeByIds;

			function init() {
				$scope.blacklistTableParams = new TableFactory({
					service : $scope.rest.doPost,
					method : "findBlacklist"
				});
				$scope.find();
			}

			function find() {
				$scope.blacklistTableParams.load({ id : $scope.idFornecedorEscolta });
			}

			function detail(row) {
				$rootScope.isPopup = true;
				var rest = $scope.rest;
				var controller = [
						"$scope",
						"$modalInstance",
						function($scope, $modalInstance) {
							$scope.title = "blacklistClientes";
							$scope.innerTemplate = contextPath + "js/app/sgr/manterfornecedorescolta/view/modalBlacklistClientes.html";

							$scope.store = store;
							$scope.close = close;

							$scope.data = row || { idFornecedorEscolta : $scope.idFornecedorEscolta };

							function store() {
								$rootScope.showLoading = true;
								rest.doPost("storeBlacklist", $scope.data).then(function(idFornecedorEscoltaImpedido) {
									if (!$scope.data.id) {
										$scope.data.id = idFornecedorEscoltaImpedido;
									}
									$modalInstance.close();
								})['finally'](function() {
									$rootScope.showLoading = false;
								});
							}

							function close() {
								$modalInstance.dismiss("cancel");
							}
						} ];
				modalService.open({
					controller : controller,
					windowClass : "modal-blacklist"
				})["finally"](function() {
					$rootScope.isPopup = false;
					$scope.find();
				});
			}

			function removeByIds() {
				var ids = [];
				$.each($scope.blacklistTableParams.selected, function() {
					ids.push(this[$scope.getIdProperty()]);
				});
				if (ids.length === 0) {
					$scope.addAlerts([ { msg : $scope.getMensagem("erSemRegistro"), type : "warning" } ]);
				} else {
					$scope.confirm($scope.getMensagem("erExcluir")).then(function() {
						$rootScope.showLoading = true;
						$scope.rest.doPost("removeBlacklistByIds", ids).then(function(data) {
							$scope.showSuccessMessage();
							$scope.find();
						})['finally'](function() {
							$rootScope.showLoading = false;
						});
					});
				}
			}

			init();
		}
];
