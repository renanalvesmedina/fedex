var parametrosClienteView = {
    name: "parametrosCliente",
    title: "parametrosCliente",
    url: "/parametrosCliente",
    controller: parametrosClienteController,
    tabs: [{
            name: "list",
            title: "listagem",
            url: "/",
            listTab: true,
            controller: parametrosClienteListController
        },
        {
            name: "cad",
            title: "Detalhamento",
            url: "/cad/:id",
            editTab: true,
            disabled: "!$stateParams.id",
            controller: parametrosClienteCadController
        }
    ]
};

VendasRotas.views.push(parametrosClienteView);