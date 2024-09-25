
var ReajusteTabelaDePrecoAbaReajusteController = [
	"$scope", 
	"$http",
	"ManterPaisFactory", 
	function($scope, $http, ManterPaisFactory) {

		ManterPaisFactory.findPaisUsuarioLogado().then(function(data) {
			$scope.data.idPais = data.idPais;
		});
	}
];