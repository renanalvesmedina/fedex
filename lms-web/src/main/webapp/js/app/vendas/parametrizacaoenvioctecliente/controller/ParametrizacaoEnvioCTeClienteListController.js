var ParametrizacaoEnvioCTeClienteListController = [
    "$scope",
    "$state",
    "$rootScope",
    "editTabState",
    function($scope, $state, $rootScope, $editTabState) {

        $scope.setConstructor({
            rest: "/vendas/parametrizacaoEnvioCTeCliente"
        });

        $scope.initializeAbaList = function () {
            $scope.setEditTabState($editTabState);
            $scope.data = {};
            $scope.page = {};
            $rootScope.row = {};
            $rootScope.data = {};
            $rootScope.data.tpCnpj = {};
            $scope.data.blAtivo = true;
        };

        $scope.initializeAbaList();

        $scope.consultar = function () {
            $scope.page = {};
            let dsDiretorioArmazenagem = ($scope.data.dsDiretorioArmazenagem === undefined ? '' : $scope.data.dsDiretorioArmazenagem);
            let tpPesquisa = (($scope.data.tpPesquisa === undefined || $scope.data.tpPesquisa == null) ? '' : $scope.data.tpPesquisa.value);
            let tpCnpj = (($scope.data.tpCnpj === undefined || $scope.data.tpCnpj == null) ? '' : $scope.data.tpCnpj.description);
            let nrIdentificacao = (($scope.data.nrIdentificacao === undefined || $scope.data.nrIdentificacao == null) ? '' : $scope.data.nrIdentificacao);

            if (tpCnpj && nrIdentificacao) {
                $scope.changeNrIdentificacao(tpCnpj, nrIdentificacao);
            }

            $scope.find(tpCnpj, nrIdentificacao, dsDiretorioArmazenagem, tpPesquisa, $scope.data.blAtivo);
        };

        $scope.changeNrIdentificacao = function (tpCnpj, cnpj) {
            if(tpCnpj) {
                $scope.rest.doGet("findNomeCliente?nrIdentificacao=" + cnpj+ "&tpCnpj=" + tpCnpj).then(function (response) {
                    response != null ? $scope.data.nome = response : $scope.data.nome = '';
                });
            } else {
                $scope.showMessage("TIPO CNPJ INVALIDO", MESSAGE_SEVERITY.WARNING);
            }
        };

        $scope.detail = function(row) {
            $rootScope.data = $scope.data;
            $rootScope.row = row;

            $state.transitionTo($scope.editTabState, row, {
                reload: false,
                inherit: false,
                notify: true
            });
        };

        $scope.find = function (tpCnpj, nrIdentificacao, dsDiretorioArmazenagem, tpPesquisa, blAtivo){
            if(!tpCnpj && nrIdentificacao) {
                $scope.showMessage("TIPO CNPJ INVALIDO", MESSAGE_SEVERITY.WARNING);
            } else {
                $scope.rest.doGet("findParametrizacaoEnvioCTeCliente?&tpCnpj=" + tpCnpj +
                    "&nrIdentificacao=" + nrIdentificacao +
                    "&dsDiretorioArmazenagem=" + dsDiretorioArmazenagem +
                    "&tpPesquisa=" + tpPesquisa +
                    "&blAtivo=" + blAtivo).then(function (response) {

                    if (response != null) {
                        $scope.page = response;
                    } else {
                        $scope.clear();
                        $scope.addAlerts([{msg: $scope.getMensagem("LMS-23030")}]);
                    }
                });
            }
        }
    }
];