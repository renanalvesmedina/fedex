var parametrosClienteController = [
    "$rootScope",
    "$scope",
    function ($rootScope, $scope) {
        $scope.setConstructor({
            rest: "/vendas/parametrosCliente/"
        });

        $rootScope.showLoading = false;
    }
];