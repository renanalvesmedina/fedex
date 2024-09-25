
var ExemploTelaComponentesController = [
	"$scope",
 	function($scope) {

		$scope.setConstructor({
			rest: "/recepcaodescarga/telaComponentes"
		});

		$scope.rest.doGet("getOpcoesChosen").then(function(response) {
			$scope.listaEmpresas = response;
		});

		$scope.listOptionsVeiculos = [
			{veiculo:'carro (mocked)'},
			{veiculo:'caminhão (mocked)'},
			{veiculo:'ônibus (mocked)'}
		];


		$scope.telefones = [
           {
        	   idTelefoneEndereco: 1,
        	   numero:'1111-1111'
           },
           {
        	   idTelefoneEndereco: 2,
        	   numero:'2222-2222'
           }
		];
		$scope.listOptionsTelefones = [];

		$scope.changePais = function() {
			$scope.data.unidadeFederativa = null;
		};


		$scope.validateCnpj = function(modelValue, viewValue) {
			var mockCnpjValidos = ["11111111111111",
			                       "22222222222222",
			                       "33333333333333",
			                       "44444444444444",
			                       "55555555555555",
			                       "66666666666666",
			                       "77777777777777",
			                       "88888888888888",
			                       "99999999999999"];

			/*regra qualquer, somente para exemplo.*/
			return {
				isValid: $.inArray(modelValue, mockCnpjValidos) >= 0,
				messageKey: 'LMS-04470',
				messageParams: [viewValue]
			};
		};
	}
];
