var parametrosClienteCadController = [
    "$scope",
    "$rootScope",
    "$stateParams",
    "$state",
    function ($scope, $rootScope, $stateParams, $state) {

        function initializeAbaCad(params) {
            if (params.id) {
                $rootScope.showLoading = true;
                $scope.findById(params.id).then(function (data) {
                    $scope.setData(data);
                    $rootScope.showLoading = false;
                }, function () {
                    $scope.setData({});
                    $rootScope.showLoading = false;
                });
            } else {
                $scope.setData({});
            }
        }

        initializeAbaCad($stateParams);

        $scope.store = function () {
            $rootScope.showLoading = true;

            if($scope.validaCheckboxCobranTdeDiferenciada()) {

                $rootScope.showLoading = false;
                $scope.addAlerts([{msg: $scope.getMensagem("LMS-04580")}]);

            } else {

                $scope.rest.doPost("", $scope.data).then(function (response) {
                    $rootScope.showLoading = false;
                    $state.transitionTo($state.current, {id: response[$scope.getIdProperty()]}, {
                        reload: false,
                        inherit: false,
                        notify: true
                    });
                    initializeAbaCad($stateParams);
                    $scope.showSuccessMessage();
                }, function () {
                    $rootScope.showLoading = false;
                });
            }

        };

        $scope.clear = function () {
            $scope.data.nfeConjulgada = false;
            $scope.data.obrigaRG = false;
            $scope.data.permiteBaixaPorVolume = false;
            $scope.data.exigeComprovanteEntrega = false;
            $scope.data.obrigaQuiz = false;
            $scope.data.pemiteBaixasParciais = false;
            $scope.data.permiteProdutoPerigoso = false;
            $scope.data.permiteProdutoControladoPoliciaCivil = false;
            $scope.data.permiteProdutoControladoPoliciaFederal = false;
            $scope.data.permiteProdutoControladoExercito = false;
            $scope.data.obrigaParentesco = false;
            $scope.data.naoPermiteSubcontratacao = false;
            $scope.data.enviaDocsFaturamentoNas=false;
            $scope.data.validaCobrancDifTdeDest=false;
            $scope.data.cobrancaTdeDiferenciada=false;
        };

        $scope.validaCheckboxCobranTdeDiferenciada = function () {

            return $scope.data.cobrancaTdeDiferenciada && !($scope.data.dificuldadeEntrega != null && $scope.data.dificuldadeEntrega === true)

        }

    }
];

