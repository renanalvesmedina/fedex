<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.rnc.emitirRelatoriosRNCAction" onPageLoadCallBack="carregaMoedaSessao">
	<adsm:form action="/rnc/emitirRelatoriosRNC">
        <adsm:hidden property="statusNaoConformidadeHidden"/>
		<adsm:combobox label="statusNaoConformidade" labelWidth="23%" width="75%" serializable="true"
						property="statusNaoConformidade" 
						domain="DM_STATUS_NAO_CONFORMIDADE"
						onchange="onChangeStatusNaoConformidade()"
						renderOptions="true"
						/>

		<adsm:lookup label="filialEmitente" dataType="text" size="5" maxLength="3" labelWidth="23%" width="75%" serializable="true"
					 property="filialEmitente"
					 service="lms.municipios.filialService.findLookup" 
                     action="/municipios/manterFiliais"
                     idProperty="idFilial"
                     criteriaProperty="sgFilial">
        	<adsm:propertyMapping relatedProperty="filialEmitente.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" property="filialEmitente.pessoa.nmFantasia" serializable="true" size="35" maxLength="30" disabled="true"/>
        </adsm:lookup>

		<adsm:lookup label="filialResponsavel" dataType="text" size="5" maxLength="3" labelWidth="23%" width="75%" serializable="true"
					 property="filialResponsavel"
					 service="lms.municipios.filialService.findLookup" 
                     action="/municipios/manterFiliais"
                     idProperty="idFilial"
                     criteriaProperty="sgFilial">
        	<adsm:propertyMapping relatedProperty="filialResponsavel.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" property="filialResponsavel.pessoa.nmFantasia" serializable="true" size="35" maxLength="30" disabled="true"/>
        </adsm:lookup>
        
        <adsm:lookup label="filialDeDestino" dataType="text" size="5" maxLength="3" labelWidth="23%" width="75%" serializable="true"
					 property="filialDestino"
					 service="lms.municipios.filialService.findLookup" 
                     action="/municipios/manterFiliais"
                     idProperty="idFilial"
                     criteriaProperty="sgFilial">
        	<adsm:propertyMapping relatedProperty="filialDestino.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" property="filialDestino.pessoa.nmFantasia" serializable="true" size="35" maxLength="30" disabled="true"/>
        </adsm:lookup>

		<adsm:range label="periodo" labelWidth="23%" width="75%" required="true" >
			<adsm:textbox dataType="JTDate" property="dataInicial" picker="true" />
			<adsm:textbox dataType="JTDate" property="dataFinal" picker="true"/>
		</adsm:range>

        <adsm:hidden property="tipoDocumentoHidden"/>
		<adsm:combobox	label="tipoDocumento" labelWidth="23%" width="75%" serializable="true"
						property="tipoDocumento"
					   	service="lms.rnc.emitirRelatoriosRNCAction.findTipoDocumentoServico"
					   	optionProperty="value" 
						optionLabelProperty="description"						
						onchange="onChangeTipoDocumento()"
						/>
		
		<adsm:hidden property="motivoNaoConformidadeHidden"/>
		<adsm:combobox  label="motivoNaoConformidade" labelWidth="23%" width="75%" serializable="true"
						property="motivoNaoConformidade"
						service="lms.rnc.motivoAberturaNcService.findOrderByDsMotivoAbertura"
						optionLabelProperty="dsMotivoAbertura"
						optionProperty="idMotivoAberturaNc" 
						onchange="onChangeMotivoNaoConformidade()"
						/>

        <adsm:hidden property="causaNaoConformidadeHidden"/>
		<adsm:combobox	label="causaNaoConformidade" labelWidth="23%" width="75%" serializable="true"
					    property="causaNaoConformidade"
					    service="lms.rnc.emitirRelatoriosRNCAction.findCausaNaoConformidadeOrdenado"
					    optionLabelProperty="dsCausaNaoConformidade"
					    optionProperty="idCausaNaoConformidade"
					    onchange="onChangeCausaNaoConformidade()"
					    />

        <adsm:hidden property="statusOcorrenciaHidden" serializable="true"/>
		<adsm:combobox	label="statusOcorrencia" labelWidth="23%" width="75%" serializable="true"
		                onchange="onChangeStatusOcorrencia()"  
						property="statusOcorrencia" 
						domain="DM_STATUS_OCORRENCIA_NC" 
						renderOptions="true" />

		<adsm:lookup label="remetente" labelWidth="23%" width="75%" size="20" maxLength="20"  serializable="true"
					 service="lms.vendas.clienteService.findLookup" 
					 action="/vendas/manterDadosIdentificacao"
					 dataType="text" 
					 property="remetente" 
					 idProperty="idCliente" 
					 criteriaProperty="pessoa.nrIdentificacao" 
					 exactMatch="false"
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado">
			<adsm:propertyMapping relatedProperty="remetente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="remetente.pessoa.nmPessoa" size="50" maxLength="50" disabled="true" serializable="true"/>
		</adsm:lookup>

		<adsm:lookup label="destinatario" labelWidth="23%" width="75%" size="20" maxLength="20"  serializable="true"
					 service="lms.vendas.clienteService.findLookup" 
					 action="/vendas/manterDadosIdentificacao"
					 dataType="text" 
					 property="destinatario" 
					 idProperty="idCliente" 
					 criteriaProperty="pessoa.nrIdentificacao" 
					 exactMatch="false"
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado">
			<adsm:propertyMapping relatedProperty="destinatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="destinatario.pessoa.nmPessoa" size="50" maxLength="50" disabled="true" serializable="true"/>
		</adsm:lookup>

		<adsm:range label="diasPendentes" labelWidth="23%" width="75%" >
			<adsm:textbox dataType="integer" property="diaInicial" size="4" maxLength="2" />
			<adsm:textbox dataType="integer" property="diaFinal" size="4" maxLength="2" />
		</adsm:range>
		
		<adsm:combobox	label="relatorio" labelWidth="23%" width="75%" serializable="true"
		                onchange="onChangeRelatorio()"  
						property="tpRelatorioRnc" 
						domain="DM_TP_RELATORIO_RNC" 
						renderOptions="true" />

		<adsm:combobox label="moeda" labelWidth="23%" width="75%"
					   property="moeda.idMoeda" 
					   service="lms.configuracoes.moedaService.find" 
					   optionProperty="idMoeda" 
					   optionLabelProperty="siglaSimbolo" 
					   required="true"
					   onchange="onChangeComboMoeda()"
					   onlyActiveValues="true"
					   />
		<adsm:hidden property="dsSimboloMoedaHidden"/>
		
		<adsm:combobox label="formatoRelatorio" labelWidth="23%" width="75%"
		               domain="DM_FORMATO_RELATORIO"
		               property="tpFormatoRelatorio"
		               defaultValue="pdf"
		               required="true"
		               renderOptions="true"
		               />
		
		<adsm:buttonBar>
			
			<adsm:button buttonType="reportViewerButton" id="btnVisualizar" caption="visualizar" onclick="imprimeRelatorio()"  />
			
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

function imprimeRelatorio() {
	
	if(getElementValue("tpRelatorioRnc") == "IT"){
		reportButtonScript('lms.rnc.emitirRelatoriosRNCAction.executeCsvReport', 'openPdf', document.forms[0]);
	}else{
		executeReportWithCallback('lms.rnc.emitirRelatoriosRNCAction.execute', 'verificaEmissao', document.forms[0]);
	}

}

/**
 * Função para verificar se o relatorio foi impresso, caso seja
 * executar o registro da emissão do manifesto de coleta
 */
function verificaEmissao_cb(strFile, error) {
    openReportWithLocator(strFile, error, null, false);
    
    if (error!=undefined) {
        setFocusOnFirstFocusableField(document);
        return false;
    }
}


document.getElementById("filialEmitente.sgFilial").serializable = true;
document.getElementById("filialResponsavel.sgFilial").serializable = true;
document.getElementById("filialDestino.sgFilial").serializable = true;

function initWindow(eventObj) {
    if(eventObj.name=="cleanButton_click") {
        carregaMoedaSessao_cb(undefined, undefined);
    }
    buscarDataAtual_cb();
}

/**
 * Busca os Data Atual.
 */
function buscarDataAtual_cb() {
    var sdo = createServiceDataObject("lms.rnc.emitirRelatoriosRNCAction.getDataAtual", "retornoBuscarDataAtual");
	xmit({serviceDataObjects:[sdo]});
}	

		
/**
 * Retorno da pesquisa de dados do usuário da sessão em getDadosSessao().
 */
function retornoBuscarDataAtual_cb(data, error) {
	setElementValue("dataFinal", setFormat(document.getElementById("dataFinal"), data.dataAtual));
}

/**
 * Carrega a moeda da sessão do usuario
 */
 function carregaMoedaSessao_cb(data, error) {
	var criteria = new Array();
	
    var sdo = createServiceDataObject("lms.rnc.emitirRelatoriosRNCAction.findMoedaSessao", "resultadoBusca", criteria);
    xmit({serviceDataObjects:[sdo]});
}

function resultadoBusca_cb(data, error) {
	if(data.idMoeda==undefined || data.idMoeda=="") {
		return false;
	} else {
		setElementValue('moeda.idMoeda', data.idMoeda);
		setElementValue('dsSimboloMoedaHidden', data.siglaSimbolo);
	}
}

function onChangeComboMoeda() {
	var comboMoeda= document.getElementById("moeda.idMoeda");
	setElementValue('dsSimboloMoedaHidden', comboMoeda.options[comboMoeda.selectedIndex].text);
}

function onChangeStatusOcorrencia() {
	var comboStatusOcorrencia = document.getElementById("statusOcorrencia");
	setElementValue('statusOcorrenciaHidden', comboStatusOcorrencia.options[comboStatusOcorrencia.selectedIndex].text);
}

function onChangeStatusNaoConformidade() {
    var comboStatusNaoConformidade = document.getElementById("statusNaoConformidade");
    setElementValue('statusNaoConformidadeHidden', comboStatusNaoConformidade.options[comboStatusNaoConformidade.selectedIndex].text);
}

function onChangeTipoDocumento() {
    var comboTipoDocumento = document.getElementById("tipoDocumento");
    setElementValue('tipoDocumentoHidden', comboTipoDocumento.options[comboTipoDocumento.selectedIndex].text);
}

function onChangeMotivoNaoConformidade() {
    var comboMotivoNaoConformidade = document.getElementById("motivoNaoConformidade");
    setElementValue('motivoNaoConformidadeHidden', comboMotivoNaoConformidade.options[comboMotivoNaoConformidade.selectedIndex].text);
}

function onChangeCausaNaoConformidade() {
    var comboCausaNaoConformidade = document.getElementById("causaNaoConformidade");
    setElementValue('causaNaoConformidadeHidden', comboCausaNaoConformidade.options[comboCausaNaoConformidade.selectedIndex].text);
}

function onChangeRelatorio(){
	if (getElementValue("tpRelatorioRnc") == "IT") {
		setElementValue("tpFormatoRelatorio", "csv");
		setDisabled("tpFormatoRelatorio", true);
	}else{
		setDisabled("tpFormatoRelatorio", false);
		setElementValue("tpFormatoRelatorio", "");
	}
}

</script>