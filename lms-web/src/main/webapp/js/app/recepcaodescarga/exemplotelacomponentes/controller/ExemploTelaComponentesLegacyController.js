
var ExemploTelaComponentesLegacyController = [
	"$scope",
 	function($scope) {

		$scope.openSR = function($event) {
			var parans = "&isPopup=true";
			parans += "&idDoctoServico=" + 123;
			$scope.openLms(contextPath+'sim/registrarSolicitacoesRetirada.do?cmd=main'+parans);    			
		};
		
		$scope.openSPE = function($event) {
			var parans = "&isPopup=true";
			parans += "&idDoctoServico=" + 123;

			$scope.openLms(contextPath+'sim/registrarSolicitacoesEmbarque.do?cmd=main'+parans);    			
		};
	}
];
