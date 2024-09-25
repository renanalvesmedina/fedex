
var ManterKmFranquiaController = [
		"$scope",
		"$rootScope",
		"$stateParams",
		"TableFactory",
		"modalService",
		function($scope, $rootScope, $stateParams, TableFactory, modalService) {
 			$scope.idFornecedorEscolta = $stateParams.id;
 			$scope.find = find;
			$scope.detail = detail;
			$scope.popup = popup;
			$scope.removeByIds = removeByIds;

			function init() {
				$scope.franquiaTableParams = new TableFactory({
					service : $scope.rest.doPost,
					method : "findFranquia"
				});
				$scope.find();
			}

			function find() {
				$scope.franquiaTableParams.load({ id : $scope.idFornecedorEscolta });
			}

			function detail(row) {
				if (row) {
					$rootScope.showLoading = true;
					$scope.rest.doGet("findFranquia/" + row.id).then(function(data) {
						$scope.popup(data);
					})['finally'](function() {
						$rootScope.showLoading = false;
					});
				} else {
					$scope.popup({ idFornecedorEscolta : $scope.idFornecedorEscolta });
				}
			}

			function popup(data) {
				$rootScope.isPopup = true;
				var rest = $scope.rest;
				var controller = [
						"$scope",
						"$modalInstance",
						function($scope, $modalInstance) {
							$scope.title = "franquia";
							$scope.innerTemplate = contextPath + "js/app/sgr/manterfornecedorescolta/view/modalKmFranquia.html";

							$scope.store = store;
							$scope.close = close;

							$scope.data = data;
							if ($scope.data.filiaisAtendimento) {
								$.each($scope.data.filiaisAtendimento, function(i, e) {
									$scope.data.filiaisAtendimento[i] = { data : { filialAtendimento : e }};
								});
							}

							function store() {
								var data = angular.copy($scope.data);
								var list = data.filiaisAtendimento;
								data.filiaisAtendimento = [];
								if (list) {
									$.each(list.untouched.concat(list.added.concat(list.updated)), function(i, e) {
										data.filiaisAtendimento.push(e.data.filialAtendimento);
									});
								}
								delete data.filialAtendimento;
								$rootScope.showLoading = true;
								rest.doPost("storeFranquia", data).then(function(idFranquiaFornecedorEscolta) {
									if (!$scope.data.id) {
										$scope.data.id = idFranquiaFornecedorEscolta;
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
					windowClass : "modal-kmfranquia"
				})['finally'](function() {
					$rootScope.isPopup = false;
					$scope.find();
				});
			}

			function removeByIds() {
				var ids = [];
				$.each($scope.franquiaTableParams.selected, function() {
					ids.push(this[$scope.getIdProperty()]);
				});
				if (ids.length === 0) {
					$scope.addAlerts([ { msg : $scope.getMensagem("erSemRegistro"), type : "warning" } ]);
				} else {
					$scope.confirm($scope.getMensagem("erExcluir")).then(function() {
						$rootScope.showLoading = true;
						$scope.rest.doPost("removeFranquiaByIds", ids).then(function(data) {
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
