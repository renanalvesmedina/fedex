
var MonitoramentoAverbacoesView =  {
	name : "monitoramentoAverbacoes",
	title : "monitoramentoAverbacoes",
	controller : MonitoramentoAverbacoesController,
	tabs : [
	    {
            name : "list",
            title : "listagem",
            url : "/",
            listTab: true
	    }
	]
};

SegurosRotas.views.push(MonitoramentoAverbacoesView);
