var parametrosClienteListController = [
    "$rootScope",
    "$scope",
    "editTabState",
    function ($rootScope, $scope, editTabState) {

        function initializeAbaList() {
            $scope.setEditTabState(editTabState);
        }

        initializeAbaList();

        $scope.onChangeTipoIdentificacao = function () {
            if (!$scope.filter.tipoIdentificacao) {
                delete $scope.filter.identificacao;
            }
        };

        $scope.consultar = function () {
            if ($scope.consultaEhValida()) {
                $scope.find();
            } else {
                $scope.addAlerts([{msg: $scope.getMensagem("LMS-10047")}]);
            }
        };

        $scope.consultaEhValida = function () {
            if ($scope.filter.identificacao
                    || $scope.filter.nomeRazaoSocial
                    || $scope.filter.nomeFantasia
                    || $scope.filter.numeroConta) {
                return true;
            }

            return false;
        };

        $scope.find = function () {
            $rootScope.showLoading = true;
            $scope.listTableParams.clear();
            
            var filter = angular.copy($scope.filter);
            
            if (angular.isDefined($scope.filter.tipoPessoa)) {
                filter.tipoPessoa = $scope.filter.tipoPessoa.value;
            }
            
            if (angular.isDefined($scope.filter.tipoIdentificacao)) {
                filter.tipoIdentificacao = $scope.filter.tipoIdentificacao.value;
            }
            
            if (angular.isDefined($scope.filter.tipoCliente)) {
                filter.tipoCliente = $scope.filter.tipoCliente.value;
            }
            
            if (angular.isDefined($scope.filter.situacao)) {
                filter.situacao = $scope.filter.situacao.value;
            }
            
            $scope.listTableParams.load(filter).then(function () {
                $rootScope.showLoading = false;
            }, function () {
                $rootScope.showLoading = false;
            });
        };

    }
];

