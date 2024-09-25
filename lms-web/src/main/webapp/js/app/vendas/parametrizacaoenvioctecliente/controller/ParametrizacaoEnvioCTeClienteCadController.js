var ParametrizacaoEnvioCTeClienteCadController = [
    "$scope",
    "$rootScope",
    "$state",
    function($scope, $rootScope, $state) {

        $scope.setConstructor({
            rest: "/vendas/parametrizacaoEnvioCTeCliente"
        });

        $scope.changeNrIdentificacao = function (tpCnpj, cnpj) {
            if (tpCnpj && cnpj) {
               $scope.rest.doGet("findNomeCliente?nrIdentificacao=" + cnpj + "&tpCnpj=" + tpCnpj).then(function (response) {
                    response != null ? $scope.data.nome = response : $scope.data.nome = '';
                });
            } else {
                $scope.showMessage("CNPJ OU TIPO CNPJ INVALIDO", MESSAGE_SEVERITY.WARNING);
            }
        };

        $scope.defineTpCnpj = function (tpCnpj) {
            if (tpCnpj !== "") {
                if (tpCnpj.length === 14) {
                    $scope.data.tpCnpj = {"description": "Completo", "id": 6339, "value": "C"};
                } else {
                    $scope.data.tpCnpj = {"description": "Parcial", "id": 6338, "value": "P"};
                }
            }
        };

        $scope.initializeAbaCad = function () {
            $scope.data = {};
            $scope.data.tpCnpj = {};
            $scope.data.tpCnpj.description = {};
            $scope.row = $rootScope.row !== undefined ? $rootScope.row : {};
            $scope.row.blAtivo = $scope.row.blAtivo !== undefined ? $scope.row.blAtivo : true;
            $scope.row.nrIdentificacao = $scope.row.nrIdentificacao !== undefined ? $scope.row.nrIdentificacao : "";
            $scope.defineTpCnpj($scope.row.nrIdentificacao);
            if ($scope.data.tpCnpj.description && $scope.row.nrIdentificacao) {
                $scope.changeNrIdentificacao($scope.data.tpCnpj.description, $scope.row.nrIdentificacao);
            }
        };

        $scope.initializeAbaCad();

        $scope.salvar = function () {
            var regexp = /^[A-Za-z0-9_/-]{4,}$/;
            var regexm = /(\/\/)/;

            if ($scope.data.tpCnpj.description && $scope.row.nrIdentificacao) {
                $scope.changeNrIdentificacao($scope.data.tpCnpj.description, $scope.row.nrIdentificacao);
            }

            if ($scope.data.nome !== "" && $scope.data.nome !== undefined) {
                if ($scope.row.tpPesquisa !== undefined) {
                    if (regexp.test($scope.row.dsDiretorioArmazenagem) &&
                        !regexm.test($scope.row.dsDiretorioArmazenagem) &&
                        $scope.row.dsDiretorioArmazenagem !== 'undefined' &&
                        $scope.row.dsDiretorioArmazenagem !== null) {
                        $scope.rest.doPost("store", $scope.row).then(function (response) {
                            $rootScope.currentTab.form.$dirty = false;
                            $scope.showSuccessMessage();
                        })
                    } else {
                        $scope.showMessage("FORMATO DE DIRETORIO INVALIDO", MESSAGE_SEVERITY.WARNING);
                    }
                } else {
                    $scope.showMessage("TIPO PESQUISA INVALIDO", MESSAGE_SEVERITY.WARNING);
                }
            } else {
                $scope.addAlerts([{msg: $scope.getMensagem("LMS-23030")}]);
            }
        };
    }
];