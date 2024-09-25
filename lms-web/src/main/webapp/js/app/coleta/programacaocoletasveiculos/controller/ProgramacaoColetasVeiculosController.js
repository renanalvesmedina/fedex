var ProgramacaoColetasVeiculosController = [
		"$scope",
		"$rootScope",
		"modalService",
		"TableFactory",
		"FilialFactory",
		"$modal",
		"$filter",
		function($scope, $rootScope, modalService, TableFactory, FilialFactory, $modal,$filter) {
			$scope.setConstructor({
				rest : "/coleta/programacaoColetasVeiculos"
			});
			

			/** Carrega dados da filial do usuario logado */
			$scope.loadCurrentFilial = function(object){
				$rootScope.showLoading = true;
				FilialFactory.findFilialUsuarioLogado().then(function(data) {
					$rootScope.showLoading = true;
    				object.filial = data;
    				$rootScope.showLoading = false;
				}, function() {
					$rootScope.showLoading = false;
				});
			};
			
			$scope.loadDefaultValues = function(){
				$scope.loadCurrentFilial($scope.filter);		
			};
			
			$scope.loadDefaultValues();
			
			function getScope() {
				return $scope;
			}

			$scope.listTableProgramacaoColetas = new TableFactory({
				service : $scope.rest.doPost,
				method : "findProgamacaoColetasNaoProgramadas",
				remotePagination : false
			});

			$scope.listTableProgramacaoColetasVeiculos = new TableFactory({
				service : $scope.rest.doPost,
				method : "findProgamacaoColetasVeiculos",
				remotePagination : false
			});

			$scope.limpar = function() {
				$scope.filter = {};
				$scope.loadDefaultValues();
			};
		
			$scope.listTableProgramacaoColetas.load({});
			$scope.listTableProgramacaoColetasVeiculos.load({});

			$scope.openModalVeiculo = function(idControleCarga) {

				var myController = [
						"$scope",
						"$modalInstance",
						"$rootScope",
						function($scope, $modalInstance, $rootScope) {

							$rootScope.isPopup = true;
							$scope.title = "dadosVeiculo";
							$scope.innerTemplate = contextPath
									+ "js/app/coleta/programacaocoletasveiculos/view/modalProgramacaoColetasVeiculosDadosVeiculo.html";

							$rootScope.showLoading = true;
							getScope().findById(idControleCarga).then(
									function(data) {
										$scope.data = data;
										$rootScope.showLoading = false;
									}, function() {
										$rootScope.showLoading = false;
									});

							$scope.close = function() {
								$modalInstance.dismiss("cancel");
							};

						} ];
				modalService.open({
					controller : myController,
					windowClass : "my-modal"
				});

			};
		
			$scope.openModalDadosColeta = function(idPedidoColeta) {
				var rest = $scope.rest;
				var myController = [
						"$scope",
						"$modalInstance",
						"$rootScope",
						function($scope, $modalInstance, $rootScope) {

							$rootScope.isPopup = true;
							$scope.title = "dadosColeta";
							$scope.innerTemplate = contextPath
									+ "js/app/coleta/programacaocoletasveiculos/view/consultarColetasDadosColeta.html";

							rest.doPost("consultarColetasDadosColeta",
									idPedidoColeta).then(function(data) {
								var hrLimiteColeta =  $filter('date')(new Date(data.hrLimiteColeta),'hh:mm');
								data.hrLimiteColeta = hrLimiteColeta;
								$scope.data = data;

							});

							$scope.listTableParamsDetalhesColeta = new TableFactory(
									{
										service : rest.doPost,
										method : "findDetalhesColeta",
										remotePagination : true
									});

							$scope.listTableParamsDetalhesColeta.load({
								idPedidoColeta : idPedidoColeta
							});

							$scope.visualizaRelatorioPedidoColeta = function() {
								$rootScope.showLoading = true;
								rest.doPost("visualizaRelatorioPedidoColeta",
										idPedidoColeta).then(
										function(data) {
											$rootScope.showLoading = false;
											if (data.reportLocator) {
												location.href = contextPath
														+ "/viewBatchReport?"
														+ data.reportLocator;
											}
										}, function() {
											$rootScope.showLoading = false;
										});
							};

							$scope.openModalEventosColeta = function() {

								modalService.open({
									controller : EventosColetaController,
									windowClass : 'my-modal',
									data:{idPedidoColeta : idPedidoColeta}
								});

								
							};
							$scope.close = function() {
								$modalInstance.dismiss("cancel");
							};

						} ];
				modalService.open({
					controller : myController,
					windowClass : "my-modal"
				});

			};

			$scope.checkRadioClick = function(row){
				$scope.data.rowRadioSelected = row;
			};
			
			$scope.consultar = function(){
				$scope.listTableProgramacaoColetas.load($scope.filter);
				$scope.listTableProgramacaoColetasVeiculos.load($scope.filter);
			};
						
			$scope.validaSelecaoGrid = function(){
				
				var itensColetasSelecionadas = $scope.listTableProgramacaoColetas.selected;
				var itemVeiculoSelecionado = $scope.data.rowRadioSelected;
				
				if (itemVeiculoSelecionado==null || itensColetasSelecionadas.length==0 || (itemVeiculoSelecionado==null || itemVeiculoSelecionado == undefined)) {
					$scope.addAlerts([{msg: $scope.getMensagem("LMS-00053"), type: "warning"}]);
					$rootScope.showLoading = false;
					return false;
				}else{
					return true;
				}
			};
						
			$scope.transmitir = function(){
				
				var existemRegistrosSelecionados = $scope.validaSelecaoGrid();
				if (existemRegistrosSelecionados){
					modalService.open({confirm: true, title: $scope.getMensagem("confirmacao"), message: $scope.getMensagem("LMS-02129"), windowClass: 'modal-confirm'})
					.then(function() {

						$rootScope.showLoading = true;
						var itensColetasSelecionadas = $scope.listTableProgramacaoColetas.selected;
						var itemVeiculoSelecionado = $scope.data.rowRadioSelected;
						var coletaDTO = {};	
						var idsPedidoColeta = [];

						$.each(itensColetasSelecionadas, function() {
							idsPedidoColeta.push(this.idPedidoColeta);
						});
						
						coletaDTO.id = itemVeiculoSelecionado.id;
						coletaDTO.idMeioTransporte = itemVeiculoSelecionado.idMeioTransporte;
						coletaDTO.idsPedido = idsPedidoColeta;
						
						
						$scope.rest.doPost("transmitirColetas", coletaDTO).then(function (response) {
								if (response != null){
									var msg = '';
									$.each(response, function() {
										msg += " Coleta: " + this.sgFilial + " - " + this.nrColeta + ": " + this.message;
									});
									
									if (msg != ''){
										$scope.addAlerts([{msg: msg, type: 'danger'}]);	
									}else { $scope.showSuccessMessage(); }
								}
			                 },
			                 function (erro) {
			                	 
			                 }
			             )['finally'](function(){
			            	 $rootScope.showLoading = false;
			            	 $scope.data.rowRadioSelected = null;
			            	 $scope.consultar();
			             });
					});	
				}
				 
			};

		} ];
