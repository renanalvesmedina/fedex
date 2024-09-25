var ParametrizacaoEnvioCTeClienteController = [
    "$scope",
    "$state",
    "$rootScope",
    function($scope, $state, $rootScope) {
        $scope.initializeAba = function () {
            $scope.data = {};
            $scope.page = {};
            $scope.row = {};
        };

        $scope.initializeAba();

        $scope.changeNrIdentificacao = function (tpCnpj, cnpj) {
            if(tpCnpj && cnpj) {
                $scope.rest.doGet("findNomeCliente?nrIdentificacao=" + cnpj+ "&tpCnpj=" + tpCnpj).then(function (response) {
                    response != null ? $scope.data.nome = response : $scope.data.nome = '';
                });

            } else {
                $scope.showMessage("TIPO CNPJ INVALIDO", MESSAGE_SEVERITY.WARNING);
            }
        };

        $scope.clear = function () {
            $scope.setData({});
            $rootScope.row = {};
            $scope.data = {};
            $scope.data.tpCnpj = {};
            $scope.data.tpCnpj.description = {};
            $state.transitionTo($state.current, {}, {
                reload : true,
                inherit : false,
                notify : true
            });
        };
    }
];