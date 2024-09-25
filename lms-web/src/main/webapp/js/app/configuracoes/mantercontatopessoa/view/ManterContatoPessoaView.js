(function(define) {
	"use strict";

	define(
			[ "app/configuracoes/mantercontatopessoa/controller/ManterContatoPessoaController" ],
			function(ManterContatoPessoaController) {

				return {
					name : "manterContatoPessoa",
					title : "manterContatoPessoa",
					ignoreModuleName : true,
					controller : ManterContatoPessoaController,
					tabs : [  {
						name : "cad",
						title : "detalhamento",
						url : "/detalhe/:id"
					}]
				};
			});

}(define));
