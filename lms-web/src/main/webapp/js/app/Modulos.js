var appModuleDependencies = [];

//---------------------------------------------------------------

var consultarlocalizacoesmercadoriasModuleName = "app.sim.consultarlocalizacoesmercadorias";
var consultarlocalizacoesmercadoriasModule = angular.module(consultarlocalizacoesmercadoriasModuleName, []);
appModuleDependencies.push(consultarlocalizacoesmercadoriasModuleName);

//---------------------------------------------------------------

var simModuleName = "app.sim";
var simModule = angular.module(simModuleName, [consultarlocalizacoesmercadoriasModuleName]);
appModuleDependencies.push(simModuleName);

//---------------------------------------------------------------

var carregamentoModuleName = "app.carregamento";
var carregamentoModule = angular.module(carregamentoModuleName, []);
appModuleDependencies.push(carregamentoModuleName);

//---------------------------------------------------------------

var expedicaoModuleName = "app.expedicao";
var expedicaoModule = angular.module(expedicaoModuleName, []);
appModuleDependencies.push(expedicaoModuleName);

//---------------------------------------------------------------

var municipiosModuleName = "app.municipios";
var municipiosModule = angular.module(municipiosModuleName, []);
appModuleDependencies.push(municipiosModuleName);

//---------------------------------------------------------------

var portariaModuleName = "app.portaria";
var portariaModule = angular.module(portariaModuleName, []);
appModuleDependencies.push(portariaModuleName);

//---------------------------------------------------------------

var freteCarreteiroColetaEntregaModuleName = "app.fretecarreteirocoletaentrega";
var freteCarreteiroColetaEntregaModule = angular.module(freteCarreteiroColetaEntregaModuleName, []);
appModuleDependencies.push(freteCarreteiroColetaEntregaModuleName);

//---------------------------------------------------------------

var configuracoesModuleName = "app.configuracoes";
var configuracoesModule = angular.module(configuracoesModuleName, []);
appModuleDependencies.push(configuracoesModuleName);

//---------------------------------------------------------------

var freteCarreteiroViagemModuleName = "app.fretecarreteiroviagem";
var freteCarreteiroViagemModule = angular.module(freteCarreteiroViagemModuleName, [configuracoesModuleName]);
appModuleDependencies.push(freteCarreteiroViagemModuleName);

//---------------------------------------------------------------

var contratacaoVeiculosModuleName = "app.contratacaoveiculos";
var contratacaoVeiculosModule = angular.module(contratacaoVeiculosModuleName, []);
appModuleDependencies.push(contratacaoVeiculosModuleName);

//---------------------------------------------------------------

var vendasModuleName = "app.vendas";
var vendasModule = angular.module(vendasModuleName, []);
appModuleDependencies.push(vendasModuleName);

//---------------------------------------------------------------

var tabelaDePrecosModuleName = "app.tabeladeprecos";
var tabelaDePrecosModule = angular.module(tabelaDePrecosModuleName, []);
appModuleDependencies.push(tabelaDePrecosModuleName);

//---------------------------------------------------------------

var workflowModuleName = "app.workflow";
var workflowModule = angular.module(workflowModuleName, []);
appModuleDependencies.push(workflowModuleName);

//---------------------------------------------------------------

var rncModuleName = "app.rnc";
var rncModule = angular.module(rncModuleName, []);
appModuleDependencies.push(rncModuleName);

//---------------------------------------------------------------

var pendenciaModuleName = "app.pendencia";
var pendenciaModule = angular.module(pendenciaModuleName, []);
appModuleDependencies.push(pendenciaModuleName);

//---------------------------------------------------------------

var indenizacoesModuleName ="app.indenizacoes";
var indenizacoesModule = angular.module(indenizacoesModuleName, []);
appModuleDependencies.push(indenizacoesModuleName);

//---------------------------------------------------------------

var segurosModuleName = "app.seguros";
var segurosModule = angular.module(segurosModuleName, []);
appModuleDependencies.push(segurosModuleName);

//---------------------------------------------------------------

var tributosModuleName = "app.tributos";
var tributosModule = angular.module(tributosModuleName, []);
appModuleDependencies.push(tributosModuleName);

//---------------------------------------------------------------

var ediModuleName = "app.edi";
var ediModule = angular.module(ediModuleName, []);
appModuleDependencies.push(ediModuleName);

//---------------------------------------------------------------

var contasareceberModuleName = "app.contasareceber";
var contasareceberModule = angular.module(contasareceberModuleName, []);
appModuleDependencies.push(contasareceberModuleName);

//---------------------------------------------------------------

var prestacaocontasciasaereasModuleName = "app.prestacaocontasciasaereas";
var prestacaocontasciasaereasModule = angular.module(prestacaocontasciasaereasModuleName, []);
appModuleDependencies.push(prestacaocontasciasaereasModuleName);

//---------------------------------------------------------------

var integracaoModuleName = "app.integracao";
var integracaoModule = angular.module(integracaoModuleName, []);
appModuleDependencies.push(integracaoModuleName);

//---------------------------------------------------------------

var volModuleName = "app.vol";
var volModule = angular.module(volModuleName, []);
appModuleDependencies.push(volModuleName);

//---------------------------------------------------------------

var franqueadoModuleName = "app.franqueado";
var franqueadoModule = angular.module(franqueadoModuleName, []);
appModuleDependencies.push(franqueadoModuleName);

//---------------------------------------------------------------

var coletaModuleName = "app.coleta";
var coletaModule = angular.module(coletaModuleName, []);
appModuleDependencies.push(coletaModuleName);

//---------------------------------------------------------------

var recepcaoDescargaModuleName = "app.recepcaodescarga";
var recepcaoDescargaModule = angular.module(recepcaoDescargaModuleName, []);
appModuleDependencies.push(recepcaoDescargaModuleName);

//---------------------------------------------------------------

var entregaModuleName = "app.entrega";
var entregaModule = angular.module(entregaModuleName, []);
appModuleDependencies.push(entregaModuleName);

//---------------------------------------------------------------

var sgrModuleName = "app.sgr";
var sgrModule = angular.module(sgrModuleName, []);
appModuleDependencies.push(sgrModuleName);

//---------------------------------------------------------------

var veiculoOnlineModuleName = "app.veiculoonline";
var veiculoOnlineModule = angular.module(veiculoOnlineModuleName, []);
appModuleDependencies.push(veiculoOnlineModuleName);

//---------------------------------------------------------------

/**
 * IMPORTANTE:
 * 		Se o módulo que estava procurando não for encontrado e houver
 * 		a necessidade de criar um novo, levar essa situação para a
 * 		equipe de arquitetura.
 *
 **/