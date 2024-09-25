var ManterParametrosPropostaView = {
    name: "manterParametrosProposta",
    title: "parametrosProposta",
    url: "/manterParametrosProposta",
    controller: ManterParametrosPropostaController,
    tabs: [
           {
        	   name: "list",
        	   title: "Listagem",
        	   url: "/",
        	   listTab: true,
        	   controller: ManterParametrosPropostaListController
           },
           {
        	   name: "rota",
        	   title: "rota",
        	   url: "/rota/:id",
        	   editTab: true,
        	   disabled : "disableAbaRota",
        	   controller: ManterParametrosPropostaRotaController
           },
           {
        	   name: "param",
        	   title: "parametros",
        	   url: "/param/:id",
        	   disabled : "disableAbaParam",
        	   controller: ManterParametrosPropostaParamController
           },
           {
        	   name: "taxas",
        	   title: "taxas",
        	   url: "/taxas/:id",
        	   disabled : "disableAbaTaxas",
        	   controller: ManterParametrosPropostaTaxasController
           },
		   {
        	   name: "generalidades",
        	   title: "generalidades",
        	   url: "/generalidades/:id",
        	   disabled : "disableAbaGeneralidades",
        	   controller: ManterParametrosPropostaGeneralidadesController
		   }
        ]
};

VendasRotas.views.push(ManterParametrosPropostaView);

