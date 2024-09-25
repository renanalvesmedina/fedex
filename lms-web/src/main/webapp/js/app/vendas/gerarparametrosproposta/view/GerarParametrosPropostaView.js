var GerarParametrosPropostaView = {
    name: "gerarParametrosProposta",
    title: "gerarParametrosProposta",
    url: "/gerarParametrosProposta",
    controller: GerarParametrosPropostaController,
    tabs: [
          
           {
        	   name: "destConv",
        	   title: "destinos",
        	   url: "/destConv/:id",
        	   disabled : "disableAbaDestConv",
        	   controller: GerarParametrosPropostaDestConvController
           }
        ]
};

VendasRotas.views.push(GerarParametrosPropostaView);

