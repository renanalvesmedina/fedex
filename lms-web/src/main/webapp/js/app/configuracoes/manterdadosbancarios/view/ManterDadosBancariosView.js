(function(define) {
	"use strict";

	define(
			[ "app/configuracoes/manterdadosbancarios/controller/ManterDadosBancariosController" ],
			function(ManterDadosBancariosController) {

				return {
					name : "manterDadosBancarios",
					title : "manterDadosBancarios",
					ignoreModuleName : true,
					controller : ManterDadosBancariosController,
					tabs : [  {
						name : "cad",
						title : "detalhamento",
						url : "/detalhe/:id"
					}]
				};
			});

}(define));
