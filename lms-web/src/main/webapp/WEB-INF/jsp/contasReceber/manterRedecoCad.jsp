<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<script>

function myOnPageLoad(){
	var remoteCall = {serviceDataObjects:new Array()};
    var tpFinalidadeMatriz = createServiceDataObject("lms.contasreceber.manterRedecoAction.findTpFinalidadeMatriz", "mountTpFinalidadeMatriz");
    var tpFinalidadeEmpresaCobranca = createServiceDataObject("lms.contasreceber.manterRedecoAction.findTpFinalidadeEmpresaCobranca", "mountTpFinalidadeEmpresaCobranca");    
    var tpFinalidade = createServiceDataObject("lms.contasreceber.manterRedecoAction.findTpFinalidade", "mountTpFinalidade");
    var dadosInicial = createServiceDataObject("lms.contasreceber.manterRedecoAction.findDadosInicial", "findDadosIniciais"); 
    remoteCall.serviceDataObjects.push(tpFinalidadeMatriz);
    remoteCall.serviceDataObjects.push(tpFinalidadeEmpresaCobranca);    
    remoteCall.serviceDataObjects.push(tpFinalidade);
    remoteCall.serviceDataObjects.push(dadosInicial);
	xmit(remoteCall);
	
	onPageLoad();
}

</script>
<adsm:window service="lms.contasreceber.manterRedecoAction" onPageLoad="myOnPageLoad" onPageLoadCallBack="myPageLoadCallBack">
	<adsm:form action="/contasReceber/manterRedeco" idProperty="idRedeco"
		newService="lms.contasreceber.manterRedecoAction.newMaster" onDataLoadCallBack="myOnDataLoad">

		<adsm:hidden property="idProcessoWorkflow" value=""/>	
		<adsm:hidden property="blShowBotaoEmitirRecibo"/>
		<adsm:hidden property="filial.idFilial"/>
		<adsm:hidden property="pendenciaDesconto.idPendencia"/>
		<adsm:hidden property="pendenciaLucrosPerdas.idPendencia"/>
		<adsm:hidden property="pendenciaRecebimento.idPendencia"/>		
		
		<adsm:hidden property="blMatriz" serializable="false"/>

		<adsm:hidden property="tpSituacaoRedecoValue"/>		
		
		<adsm:textbox label="filialCobranca" dataType="text" property="filial.sgFilial" size="3" maxLength="3" width="31%"
					 disabled="true"
					 labelWidth="19%">			 

			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" maxLength="30" disabled="true"/>
    	
    	</adsm:textbox>

        <adsm:textbox label="numeroRedeco" property="nrRedeco" dataType="integer" size="10" labelWidth="19%" width="31%" disabled="true" mask="0000000000"/>

        <adsm:textbox dataType="text" label="situacao" property="tpSituacaoRedeco" size="37" labelWidth="19%" width="31%" disabled="true"/>
		
		<adsm:textbox dataType="JTDate" property="dtEmissao" label="dataEmissao" required="true" labelWidth="19%" width="31%" disabled="true"/>
		
        <adsm:textbox dataType="text" property="nmResponsavelCobranca" size="30" label="cobrador" required="true"  labelWidth="19%" 
        				width="31%" maxLength="60"/>
        
        <adsm:combobox service="lms.contasreceber.manterRedecoAction.findEmpresaCobranca" optionProperty="idEmpresaCobranca" optionLabelProperty="pessoa.nmPessoa" property="empresaCobranca.idEmpresaCobranca" 
        				onlyActiveValues="true" boxWidth="210"
        				label="empresaCobranca" labelWidth="19%" width="31%" />
        				
		<adsm:combobox property="tpFinalidade" label="finalidade" service="lms.contasreceber.manterRedecoAction.findTpFinalidade" onchange="return myFinalidadeOnChange(this)"
						optionProperty="value" optionLabelProperty="description" required="true" defaultValue="CC"
						labelWidth="19%" width="31%" autoLoad="false"/>        				

		<adsm:textbox dataType="JTDate" property="dtRecebimento" label="dataRecebimento" labelWidth="19%" width="31%" required="true"/>
		
		<adsm:textbox dataType="JTDate" property="dtLiquidacao" label="dataLiquidacao" labelWidth="19%" width="31%" disabled="true"/>
		
		<adsm:textbox dataType="integer" property="qtTotalDocumentos" label="quantidadeDocumentos" disabled="true" labelWidth="19%" width="31%"/>
		
		<adsm:textbox dataType="currency" property="vlTotalPago" label="totalRecebido" disabled="true" labelWidth="19%" width="31%" serializable="true"/>
		
		<adsm:textbox dataType="currency" property="vlTotalJuros" label="totalJurosRecebidos" disabled="true" labelWidth="19%" width="31%"/>
		
		<adsm:textbox dataType="currency" property="vlTotalDesconto" label="totalDescontos" disabled="true" labelWidth="19%" width="31%"/>
		
		<adsm:textbox dataType="currency" property="vlTotalTarifas" label="totalTarifas" disabled="true" labelWidth="19%" width="31%"/>
		
		<adsm:textbox dataType="currency" property="vlDiferencaCambialCotacao" label="diferencaCambialCotacao" disabled="true" labelWidth="19%" width="31%"/>
		
		<adsm:textbox dataType="text" property="moeda.siglaSimbolo" label="moeda" disabled="true" labelWidth="19%" width="31%"/>		
		
		<adsm:combobox property="tpAbrangencia" label="abrangencia" domain="DM_ABRANGENCIA" labelWidth="19%" width="31%" required="true" defaultValue="N"/>	
		
		<adsm:combobox property="blDigitacaoConcluida" label="digitacaoConcluida" domain="DM_SIM_NAO" labelWidth="19%" width="31%" disabled="true" defaultValue="N"/>	
		
		<adsm:textarea property="obRedeco" label="observacao"  maxLength="60" columns="100"  rows="3" labelWidth="19%" width="81%"/>

	    <adsm:i18nLabels>
			<adsm:include key="LMS-36191"/>
	    </adsm:i18nLabels>		


        <adsm:section caption="relacoesCobranca" />

		<adsm:buttonBar lines="1">

            <adsm:button caption="composicaoPagamento" id="composicaoPagamento" onclick="compPagamento();"/>
            <adsm:button caption="concluirDigitacao" id="concluirDigitacao" onclick="concluirDig();"/>
            <adsm:button caption="emissao" id="emissaoButton" onclick="emitirRedeco();"/>
            <adsm:button caption="cancelar" id="cancelar" onclick="cancelRedeco();"/>
            <adsm:button caption="baixar" id="baixar" onclick="baixarRedeco();"/>
			<adsm:button id="storeButton" caption="salvar" onclick="myStore(this)" disabled="false" buttonType="storeButton"/>
			<adsm:button buttonType="newButton" id="limparButton" caption="limpar" onclick="newButtonScript(document, true, {name:'newButton_click'}); relacaoCobrancaGridDef.resetGrid();"/>
		</adsm:buttonBar>
	</adsm:form>
	        <adsm:grid idProperty="idRelacaoCobranca" property="relacaoCobranca" selectionMode="none" gridHeight="50" 
        	showGotoBox="false" showPagging="false" showRowIndex="false" showTotalPageCount="false" rows="4"
        	scrollBars="vertical" autoSearch="false" autoAddNew="false" onRowClick="fazNada();">	
		    <adsm:gridColumn width="15%" title="numeroRelacao" property="nrRelacaoCobrancaFilial"/>
		    <adsm:gridColumn width="15%" title="valorTotal" property="vlFrete" dataType="currency"/>
		    <adsm:gridColumn width="15%" title="valorJuros" property="vlJuros" dataType="currency"/>
		    <adsm:gridColumn width="10%" title="valorDesconto" property="vlDesconto" dataType="currency"/>
		    <adsm:gridColumn width="15%" title="valorTarifa" property="vlTarifa" dataType="currency"/>
		    <adsm:gridColumn width="15%" title="valorImpostos" property="valorImpostos" dataType="currency"/>
		    <adsm:gridColumn width="15%" title="valorLiquido" property="valorLiquido" dataType="currency"/>
	    </adsm:grid>
</adsm:window>
<script>

document.getElementById("blMatriz").masterLink = "true";
/*
 *
 *
 *
 * FUNCTIONS QUE CARREGA A TELA
 *
 *
 *
 *
 */
var objTpFinalidadeMatriz;
var objTpFinalidadeEmpresaCobranca;
var objTpFinalidade;
var objFilialSessao;
var objDadosIniciais;

function initWindow(eventObj){
	if (eventObj.name == "newButton_click" || eventObj.name == "removeButton"){
		processoPadraoDadosIniciais();
		resetValue('moeda.siglaSimbolo');
	}

	if (eventObj.name == "tab_click"){	
		if (eventObj.src.tabGroup.oldSelectedTab.properties.id == "pesq"){
			processoPadraoDadosIniciais();
			if( getElementValue("idRedeco") == '' ){
				relacaoCobrancaGridDef.resetGrid();
			}
		}
	}
	enableButtons();
	
	var tabGroup = getTabGroup(this.document);	
				
	var tabDocumentos = tabGroup.getTab("itens");
	var telaDocumentos = tabDocumentos.tabOwnerFrame;
	
	var grid = telaDocumentos.itemRedecoGridDef;
	
	if (grid != undefined && (grid.currentRowCount > 0 && (eventObj.name != "newButton_click" && eventObj.name != "removeButton"))){
		var siglaSimbolo = grid.gridState.data[0].moeda.siglaSimbolo;
		setElementValue('moeda.siglaSimbolo',siglaSimbolo);
	} else {
		resetValue('moeda.siglaSimbolo');
	}
	
	var idProcessoWorkflow = getElementValue('idProcessoWorkflow');
		
	/** Caso exista idProcessoWorkflow, seleciona a tab cad, e desabilita todo documento */
	if (idProcessoWorkflow != ""){
		setDisabled(document, true);
	}

	
}

function processoPadraoDadosIniciais(){
	
	fillTpFinalidadeMatriz();
	fillFilialSessao();
	fillDadosIniciais();
	
	setDisabled("tpFinalidade", false);
}

var tpFinalidadeForLazyLoad;

function myOnDataLoad_cb(d,e,c,x){ 
	onDataLoad_cb(d,e,c,x);
	tpFinalidadeForLazyLoad = d.tpFinalidade; 
	
	setDisabled("tpFinalidade", true);
	
	enableButtons();
	
	if (getElementValue("tpFinalidade") == "EC"){
		setDisabled("tpFinalidade", false);
		fillTpFinalidadeEmpresaCobranca();
		setElementValue("tpFinalidade", "EC");
	}

	//POPULAR A GRID DE RELAÇÃO DE COBRANÇA
	var remoteCall = {serviceDataObjects:new Array()};
    var dataCall = createServiceDataObject("lms.contasreceber.manterRedecoAction.findRelacaoCobranca", "myOnDataLoadGrid", {idRedeco:getElementValue("idRedeco")});
    remoteCall.serviceDataObjects.push(dataCall);
	xmit(remoteCall);	

	fillTpFinalidade();
	
	var idProcessoWorkflow = getElementValue('idProcessoWorkflow');
		
	/** Caso exista idProcessoWorkflow, seleciona a tab cad, e desabilita todo documento */
	if (idProcessoWorkflow != ""){
		showSuccessMessage();
		setDisabled(document, true);
	}

}

/**Chama a tela Manter Composicao Pagamento Redeco */
function compPagamento(){
	if (getTabGroup(this.document).getTab("cad").hasChanged()){
		var LMS_36370 = 'Ação não pode ser realizada pois existem alterações pendentes a serem salvas.'; 
		alert(LMS_36370);
		return;
	}
	
	informarComposicao();
}

/**Abre a tela de composições passando o id do redeco*/
function informarComposicao(){

	var url = "/contasReceber/manterCompPagamentoRedeco.do?cmd=main";
	var idRedeco = getElementValue("idRedeco");
	if(idRedeco != "") {
		url += "&idRedeco=" + idRedeco;
	}
	parent.parent.redirectPage(contextRoot + url);
}

/**Conclui a digitação do Redeco*/
function concluirDig(){
	if (getTabGroup(this.document).getTab("cad").hasChanged()){
		var LMS_36370 = 'Ação não pode ser realizada pois existem alterações pendentes a serem salvas.'; 
		alert(LMS_36370);
		return;
	}

	var idRedeco = getElementValue("idRedeco");

	var data = new Array();	
	setNestedBeanPropertyValue(data, "idRedeco", idRedeco);		
	
	var sdo = createServiceDataObject("lms.contasreceber.manterRedecoAction.validateBeforeConcluirDigitacao", "validateBeforeConcluirDigitacao", data);
	xmit({serviceDataObjects:[sdo]});
}

/**Esta função também existe em ManterCompPagamentoRedecoCad.jsp pela mesma razão. Provavelmente o ajuste deva ser em ambos. */
function validateBeforeConcluirDigitacao_cb(data, error) {
	if (data.travaJuros == "true") {
		alert(data.errorMessage);
		
	} else if(data.encContasFilDif === "true" ) {
		alert(data.encContasFilDifMessage);
		
	} else if(data.redecoSemCompPagto == "true") {
		alert(data.errorMessage2);
		
	} else if (data.vlLiquidoLtVlTotalReceb == "true") {
		var LMS_36343 = 'Total da composição de pagamentos é inferior à soma dos saldos dos documentos do redeco. Deseja realizar uma baixa parcial dos documentos?';
		if (confirm(LMS_36343)) {
			if (data.nrItensRedecoMaiorQueUm == "true") {
				alert(data.nrItensRedecoMaiorQueUmMessage);
				return;
				
			}

			concluirDigitacao2();
		} else {
			alert(data.errorMessage);
			
		}
		
	} else if (data.vlLiquidoEqualsVlTotalReceb == "false") {
		alert(data.errorMessage);
	
	} else {
		concluirDigitacao2();
		
	}
	
}

function validateBeforeBaixar_cb(data, error) {
	if (data.travaJuros == "true") {
		alert(data.errorMessage);
	} else if (data.vlLiquidoLtVlTotalReceb == "true") {
		var LMS_36343 = 'Total da composição de pagamentos é inferior à soma dos saldos dos documentos do redeco. Deseja realizar uma baixa parcial dos documentos?';
		if (confirm(LMS_36343)) {
			showModalDialog('contasReceber/manterRedeco.do?cmd=baixar',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:240px;dialogHeight:100px;');
		} 
	} else {
		showModalDialog('contasReceber/manterRedeco.do?cmd=baixar',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:240px;dialogHeight:100px;');
	}
}

function concluirDigitacao2() {
	var idRedeco = getElementValue("idRedeco");

	var data = new Array();	
	setNestedBeanPropertyValue(data, "idRedeco", idRedeco);		
	
 	var sdo = createServiceDataObject("lms.contasreceber.manterRedecoAction.concluirDigitacao", "concluirDigitacao", data);
 	xmit({serviceDataObjects:[sdo]});
 }


function concluirDigitacao_cb(data,error){
	if(error != undefined){
		alert(error);
	}
	onDataLoad_cb(data,error);
	enableButtons();
}

function myStore_cb(d,e,c,x){
	store_cb(d,e,c,x);
	
	if (e == undefined){
		if(d.blComposicao == "true"){
			informarComposicao();
		}else{
			if(confirm("Deseja concluír a digitação")){
				concluirDig();
			}else{
				informarComposicao();
		}
		}
		
		if (getElementValue("tpFinalidade") != "EC"){			
			setDisabled("tpFinalidade", true);
		} else {
			setDisabled("tpFinalidade", false);
			fillTpFinalidadeEmpresaCobranca();
			setElementValue("tpFinalidade", "EC");
		}
	}
	
	var tabGroup = getTabGroup(this.document);	
				
	var tabDocumentos = tabGroup.getTab("itens");
	var telaDocumentos = tabDocumentos.tabOwnerFrame.document;
	
	if( d != undefined ){
		setElementValue(telaDocumentos.getElementById("_nrRedeco"), 
		                setFormat("nrRedeco",getElementValue("nrRedeco")));
	}
}

/*
 * HABILITAR/DESHABILITAR BOTOES
 * 
 * chamado por: initWindow, myOnDataLoad_cb, emitirRedeco_cb
 *
 */
function enableButtons(){

	/**Regras para os botões Composicao Pagamento e Concluir Digitação*/
	var tipoFinalidade   = getElementValue("tpFinalidade"); 
	var totalRecebido    = getElementValue("vlTotalPago");
	var tpSituacaoRedeco = getElementValue("tpSituacaoRedecoValue");	
	var blDigitacaoConcluida = getElementValue("blDigitacaoConcluida");

	setDisabled("composicaoPagamento", false);
	
	if( (tpSituacaoRedeco == "DI" || tpSituacaoRedeco == "EM") && blDigitacaoConcluida == "N" ){
		setDisabled("concluirDigitacao", false);
	}else{
		setDisabled("concluirDigitacao", true);
	}

	
	if (getElementValue("tpSituacaoRedecoValue") == "LI"){
		setDisabled("cancelar", true);
		setDisabled("baixar", true);
	}
	
	if (getElementValue("tpSituacaoRedecoValue") == "CA"){
		setDisabled('limparButton',false);
		setDisabled('emissaoButton',true);		
		setDisabled('cancelar',true);
		setDisabled('baixar',true);
		setDisabled('storeButton',true);
	}
	
	if (getElementValue('blDigitacaoConcluida') == 'N'){
		setDisabled('baixar',true);
}
}

/*
 * MONTA O OBJETO QUE TEM A LISTA DE tpFinalidade QUANDO É UM NOVO REDECO
 *
 * chamado por: myOnPageLoad 
 */
function mountTpFinalidadeMatriz_cb(d,e){
	if (e == undefined){
		objTpFinalidadeMatriz = d;
	}
}

/*
 * MONTA O OBJETO QUE TEM A LISTA COMPLETA DE tpFinalidadeEmpresaCobranca
 *
 * chamado por: myOnPageLoad
 */
function mountTpFinalidadeEmpresaCobranca_cb(d,e){
	if (e == undefined){
		objTpFinalidadeEmpresaCobranca = d;
	}
}

/*
 * MONTA O OBJETO QUE TEM A LISTA COMPLETA DE tpFinalidade
 *
 * chamado por: myOnPageLoad
 */
function mountTpFinalidade_cb(d,e){
	if (e == undefined){
		objTpFinalidade = d;
	}
}

/*
 * ENCHE A COMBO tpFinalidade COM O OBJETO objTpFinalidade
 * 
 * chamado por: myOnDataLoad_cb
 */
function fillTpFinalidade(){
	var idRedeco = getElementValue("idRedeco");
	var sgFilial = getElementValue("filial.sgFilial");
	var data = new Array();	
	
	setNestedBeanPropertyValue(data, "idRedeco", idRedeco);
	setNestedBeanPropertyValue(data, "sgFilial", sgFilial);

	var sdo = createServiceDataObject("lms.contasreceber.manterRedecoAction.findTpFinalidadeForEdition", "fillTpFinalidadeForEdition", data);
	xmit({serviceDataObjects:[sdo]});
}

function fillTpFinalidadeForEdition_cb(data, error) {
	if (error == undefined){
		objTpFinalidade = data;
	}
	
	comboboxLoadOptions({e:document.getElementById("tpFinalidade"), data:objTpFinalidade});
	
	setElementValue('tpFinalidade',tpFinalidadeForLazyLoad);
}


/*
 * ENCHE A COMBO tpFinalidade COM O OBJETO objTpFinalidadeEmpresaCobranca
 * 
 * chamado por: myOnDataLoad_cb
 */
function fillTpFinalidadeEmpresaCobranca(){
	if (objTpFinalidadeEmpresaCobranca != undefined){
		comboboxLoadOptions({e:document.getElementById("tpFinalidade"), data:objTpFinalidadeEmpresaCobranca});
	} else {
		setTimeout("fillTpFinalidadeEmpresaCobranca()",1000);
	}
}

/*
 * ENCHE A COMBO tpFinalidade COM O OBJETO objTpFinalidadeMatriz se a filial da sessão
 * é matriz, senão coloca todas as finalidades do dominio
 * 
 * chamado por: initWindow
 */
function fillTpFinalidadeMatriz(){
	if (objTpFinalidade != undefined){
		if (getElementValue("blMatriz") == "true"){		
			fillTpFinalidade();
		} else {
			comboboxLoadOptions({e:document.getElementById("tpFinalidade"), data:objTpFinalidadeMatriz});
		}
	} else {
		setTimeout("fillTpFinalidadeMatriz()",1000);
	}
}


/*
 * Seta os objetos de inicialização de tela
 *
 * chamado por: myOnPageLoad
 */
function findDadosIniciais_cb(d,e){
	if (e == undefined){
		setElementValue("blMatriz", d.blMatriz);
		
		objFilialSessao = new Object();
		objDadosIniciais = new Object();
		
		objFilialSessao.idFilial = d.idFilial;
		objFilialSessao.sgFilial = d.sgFilial;
		objFilialSessao.nmFantasia = d.nmFantasia;
		objDadosIniciais.tpSituacaoRedeco = d.tpSituacaoRedeco;
		objDadosIniciais.dtEmissao = d.dtEmissao;
		objDadosIniciais.dtRecebimento = d.dtRecebimento;
	}
}

/*
 * Seta a filial da sessão na lookup da tela
 *
 * chamado por: initWindow
 */
function fillFilialSessao(){
	if (objFilialSessao != undefined){
		setElementValue("filial.idFilial", objFilialSessao.idFilial);
		setElementValue("filial.sgFilial", objFilialSessao.sgFilial);
		setElementValue("filial.pessoa.nmFantasia", objFilialSessao.nmFantasia);
	} else {
		setTimeout("fillFilialSessao()",1000);
	}
}

/*
 * Seta a filial da sessão na lookup da tela
 *
 * chamado por: initWindow
 */
function fillDadosIniciais(){
	if (objDadosIniciais != undefined){
		setElementValue("tpSituacaoRedeco", objDadosIniciais.tpSituacaoRedeco);
		setElementValue("dtEmissao", setFormat("dtEmissao", objDadosIniciais.dtEmissao));
		setElementValue("dtRecebimento", setFormat("dtRecebimento", objDadosIniciais.dtRecebimento));
	} else {
		setTimeout("fillDadosIniciais()",1000);
	}
}


/*
 *
 * POPULA A GRID
 *
 * chamado por: myOnDataLoad_cb
*/
function myOnDataLoadGrid_cb(d,e,c,x){
	if (e == undefined){
		relacaoCobrancaGridDef.resetGrid();
		relacaoCobrancaGridDef.populateGrid(d);
	}
}

/*
 *
 *
 *
 *
 * CÓDIGO DOS BOTÕES
 *
 *
 *
 *
*/
function cancelRedeco(){
	if (!confirm("Confirma Cancelamento?")) {
		return;
	}
	var remoteCall = {serviceDataObjects:new Array(), handleCallbackUserMessages:true, showSuccessMessage:true};
    var dataCall = createServiceDataObject("lms.contasreceber.manterRedecoAction.cancelRedeco", "cancelRedeco", {idRedeco:getElementValue("idRedeco")});
    remoteCall.serviceDataObjects.push(dataCall);
	xmit(remoteCall);	
}

function cancelRedeco_cb(d,e,c,x){
	onDataLoad_cb(d,e,c,x);
	enableButtons();
}

function emitirRedeco(){
	if (getTabGroup(this.document).getTab("cad").hasChanged()){
		if (!confirm(i18NLabel.getLabel("LMS-36191"))) {		
			return;
		}
	}

	var remoteCall = {serviceDataObjects:new Array(), handleCallbackUserMessages:false, showSuccessMessage:false};
    var dataCall = createServiceDataObject("lms.contasreceber.manterRedecoAction.emitirRedeco", "emitirRedeco", {redeco:{idRedeco:getElementValue("idRedeco")}, tpAbrangencia:getElementValue("tpAbrangencia")});
    remoteCall.serviceDataObjects.push(dataCall);
	xmit(remoteCall);
}

function emitirRedeco_cb(d,e,c,x){
	var strFile = d;

	if (e == undefined) {
		onDataLoad_cb(d,e);
		enableButtons();
		if (d.relatorio != undefined && d.relatorio != ""){
			openReportWithLocator(d.relatorio,e);		
		}		
		//setElementValue("tpSituacaoRedeco", d.tpSituacaoRedeco);
	} else {
		alert(e);
		enableButtons();
	}
	
	
}

function baixarRedeco(){
	if (getTabGroup(this.document).getTab("cad").hasChanged()){
		if (!confirm(i18NLabel.getLabel("LMS-36191"))) {		
			return;
		}
	}

	var idRedeco = getElementValue("idRedeco");

	var data = new Array();	
	setNestedBeanPropertyValue(data, "idRedeco", idRedeco);		

	var sdo = createServiceDataObject("lms.contasreceber.manterRedecoAction.validateBeforeConcluirDigitacao", "validateBeforeBaixar", data);
	xmit({serviceDataObjects:[sdo]});
}

function baixarRedeco_cb(d,e,c,x){
	var strFile = d;
	if (e == undefined){
		onDataLoad(getElementValue("idRedeco"));
		if (d.relatorio != undefined && d.relatorio != ""){
			strFile = new Object();
			strFile._value = d.relatorio;
			openPdf_cb(strFile,e,c,x);		
		}		
	} else {
		alert(e);
	}
}

function fazNada(){
	return false;
}

	function myFinalidadeOnChange(elem){
		
		var retorno = comboboxChange({e:elem});
		
		var tpFinalidade = elem.value;
		var cmbEmpresaCobranca = getElement('empresaCobranca.idEmpresaCobranca');
		
		if( tpFinalidade == 'EC' ){
			cmbEmpresaCobranca.required = 'true';
		} else {
			cmbEmpresaCobranca.required = 'false';
		}
		
		return retorno;
		
	}
	
	function myStore(elem){
		var retorno = storeButtonScript('lms.contasreceber.manterRedecoAction.store', 'myStore', document.forms[0]);		
		return retorno;
	}

	/** 
	  * CallBack da página 
	  */
	function myPageLoadCallBack_cb(data, error){
		var url = new URL(parent.location.href);
		
		if (url.parameters != undefined && url.parameters.idProcessoWorkflow != undefined && url.parameters.idProcessoWorkflow != ''){   
			onDataLoad(url.parameters.idProcessoWorkflow);		
		}
		onPageLoad_cb(data, error);
	}
</script>