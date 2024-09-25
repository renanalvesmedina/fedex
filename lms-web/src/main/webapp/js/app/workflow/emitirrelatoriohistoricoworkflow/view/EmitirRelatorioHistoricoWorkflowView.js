var EmitirRelatorioHistoricoWorkflowView = {
	name : "emitirRelatorioHistoricoWorkflow",
	title : "emitirRelatorioHistoricoWK",
	controller : EmitirRelatorioHistoricoWorkflowController,
	tabs : [ {
		name : "pesq",
		title : "relatorio",
		url : "/",
		controller: EmitirRelatorioHistoricoWorkflowPesqController
	}]
};

WorkflowRotas.views.push(EmitirRelatorioHistoricoWorkflowView);

