
var ManterMarkupView = { 
    name: "manterMarkup", 
    title: "manterMarkup", 
    url: "/markup", 
    controller: ManterMarkupController, 
    tabs: [ 
           { 
        	   name: "cad", 
        	   title: "vigentes", 
        	   url: "/:id", 
        	   disabled: false, 
        	   editTab: false 
           },
           {
        	   name: "hist",
        	   title: "naoVigentes",
        	   url: "/hist/:id",
        	   disabled : false,
        	   controller: ManterMarkupHistController
           }
    ] 
};

TabelaDePrecosRotas.views.push(ManterMarkupView);

