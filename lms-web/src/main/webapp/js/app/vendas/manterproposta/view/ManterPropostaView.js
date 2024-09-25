var ManterPropostaView = {
    name: "manterProposta",
    title: "gerarPropostasCliente",
    url: "/manterproposta",
    controller: ManterPropostaController,
    tabs: [
           {
        	   name: "list",
        	   title: "Listagem",
        	   url: "/",
        	   listTab: true,
        	   controller: ManterPropostaListController
           },
           {
        	   name: "cad",
        	   title: "Detalhamento",
        	   url: "/cad/:id",
        	   editTab: true,
        	   controller: ManterPropostaCadController
           },
           {
        	   name: "frm",
        	   title: "Formalidades",
        	   url: "/frm/:id",
        	   disabled: "!$stateParams.id",
        	   controller: ManterPropostaFrmController
           },
           {
        	   name: "serv",
        	   title: "servicosAdicionais",
        	   url: "/serv/:id",
        	   disabled : "disableAbaServ",
        	   controller: ManterPropostaServController
           },
           {
        	   name: "fluxo",
        	   title: "fluxoAprovacao",
        	   url: "/fluxo/:id",
        	   disabled : "disableAbaFlux",
        	   controller: ManterPropostaFluxoController
           },
           {
        	   name: "anx",
        	   title: "Anexos",
        	   url: "/anx/:id",
        	   disabled: "!$stateParams.id",
        	   controller: ManterPropostaAnexoController
           },
           {
        	   name: "hist",
        	   title: "historico",
        	   url: "/hist/:id",
        	   disabled: "disableAbaHist",
        	   controller: ManterPropostaHistoricoController
           }
           ]
};

VendasRotas.views.push(ManterPropostaView);

