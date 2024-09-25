(function(define) {
	"use strict";

	define(
			[ "app/configuracoes/mantercontatopessoa/controller/ManterTelefonePessoaController" ],
			function(ManterTelefonePessoaController) {

				return {
					name : "manterTelefonePessoa",
					title : "manterTelefonePessoa",
					ignoreModuleName : true,
					controller : ManterTelefonePessoaController,
					tabs : [  {
						name : "cad",
						title : "detalhamento",
						url : "/detalhe/:id"
					}]
				};
			});

}(define));
