var ParametrizacaoEnvioCTeClienteView = {
    name: "parametrizacaoEnvioCTeCliente",
    title: "Manter Parametrizacao Envio CTe",
    controller: ParametrizacaoEnvioCTeClienteController,
    tabs: [
        {
            name: "list",
            title: "consulta",
            url: "/",
            disabled: false,
            listTab: true,
            controller: ParametrizacaoEnvioCTeClienteListController,
        },
        {
            name: "cad",
            title: "detalhamento",
            url: "/cad/",
            disabled : false,
            editTab: true,
            controller: ParametrizacaoEnvioCTeClienteCadController,
        }
    ]
};

VendasRotas.views.push(ParametrizacaoEnvioCTeClienteView);