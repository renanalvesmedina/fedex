
var ManterProprietarioView = {
	name : "manterProprietario",
	title : "manterProprietarios",
	ignoreModuleName : true,
	controller : ManterProprietarioController,
	tabs : [ {
		name : "list",
		title : "listagem",
		url : "/",
		disabled: "data.workflow",
		controller: ManterProprietarioListController
	},
	{
		name : "cad",
		title : "detalhamento",
		url : "/detalhe/:id",
		controller: ManterProprietarioCadController
	},
	{
		name : "inscricaoEstadual",
		title : "inscricoesEstaduais",
		url : "/inscricaoEstadual/:id",
		disabled: "!$stateParams.id",
		controller: ManterProprietarioInscricaoEstadualController
	},
	{
		name : "endereco",
		title : "endereco",
		url : "/endereco/:id",
		disabled: "!$stateParams.id",
		controller: ManterProprietarioEnderecoController
	},
	{
		name : "dadosBancarios",
		title : "dadosBancarios",
		url : "/dadosBancarios/:id",
		disabled: "!$stateParams.id",
		controller: ManterProprietarioDadosBancariosController
	},
	{
		name : "contato",
		title : "contato",
		url : "/contato/:id",
		disabled: "!$stateParams.id",
		controller: ManterProprietarioContatoController
	},
	{
		name : "telefone",
		title : "telefone",
		url : "/telefone/:id",
		disabled: "!$stateParams.id",
		controller: ManterProprietarioTelefoneController
	},
	{
		name : "desconto",
		title : "desconto",
		url : "/detalhe/:id",					
		disabled : "!$stateParams.id",
		controller : ManterProprietarioDescontoController
	},
	{
		name : "workflow",
		title : "historicoAprovacao",
		url : "/detalhe/:id/workflow",					
		disabled : "!$stateParams.id",
		controller : ManterProprietarioWorkflowController
	} 
	]
};

ContratacaoVeiculosRotas.views.push(ManterProprietarioView);
