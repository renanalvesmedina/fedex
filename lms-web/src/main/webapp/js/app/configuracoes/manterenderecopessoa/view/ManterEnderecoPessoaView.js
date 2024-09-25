
var ManterEnderecoPessoaView = {
	name : "manterEnderecoPessoa",
	title : "manterEnderecoPessoa",
	ignoreModuleName : true,
	controller : ManterEnderecoPessoaController,
	tabs : [  {
		name : "cad",
		title : "detalhamento",
		url : "/detalhe/:id"
	}]
};

ConfiguracoesRotas.views.push(ManterEnderecoPessoaView);