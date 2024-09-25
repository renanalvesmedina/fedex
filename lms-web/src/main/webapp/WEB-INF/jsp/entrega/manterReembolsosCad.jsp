<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.entrega.manterReembolsosAction" >
	<adsm:form action="/entrega/manterReembolsos" idProperty="idDoctoServico" onDataLoadCallBack="verificaTpSituacao" >
		<adsm:hidden property="documentoMir"/>
		<adsm:hidden property="whichReport" />

		<adsm:i18nLabels>
			<adsm:include key="LMS-09006"/>
		</adsm:i18nLabels>

		<adsm:hidden property="filial.idFilial" serializable="false"/>
		<adsm:textbox dataType="text" property="filial.sgFilial" label="filialOrigem" labelWidth="19%" width="31%" disabled="true" size="3">
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" />
		</adsm:textbox>

		<adsm:textbox dataType="integer" property="nrReciboReembolso" label="numeroReembolso" maxLength="8" size="16" labelWidth="20%" width="30%" disabled="true" mask="00000000"/>
		<adsm:textbox dataType="JTDate" property="dhEmissaoReciboReembolso" label="dataEmissao" maxLength="8" size="16" labelWidth="19%" width="31%" disabled="true"/>

		<adsm:section caption="documentoReembolsado"/>

		<adsm:textbox disabled="true" property="tpDocumentoServico" label="documentoServico" labelWidth="19%" width="31%" size="6" dataType="text">
			<adsm:textbox disabled="true" size="3" dataType="text" property="sgFilialDoc"/>
			<adsm:textbox disabled="true" property="nrDoctoServico" dataType="integer" size="10" />
		</adsm:textbox>

		<adsm:textbox disabled="true" property="sgFilialOper" label="filialDestino" labelWidth="19%" width="31%" size="3" dataType="text">
			<adsm:textbox disabled="true" size="30" dataType="text" property="nmFantasiaOper"/>
		</adsm:textbox>
			
		<adsm:textbox dataType="JTDate" property="dhEmissaoDoc" label="dataEmissao" labelWidth="19%" width="31%" disabled="true" />

		<adsm:textbox label="remetente" labelWidth="19%" property="nrIdentificacaoRemetente" disabled="true" width="81%" size="20" maxLength="20" dataType="text">
			<adsm:textbox size="30" dataType="text" property="doctoServicoByIdDoctoServReembolsado.clienteByIdClienteRemetente.pessoa.nmPessoa" disabled="true"/>
		</adsm:textbox>

		<adsm:textbox label="destinatario" labelWidth="19%" property="nrIdentificacaoDestinatario" disabled="true" width="81%" size="20" maxLength="20" dataType="text">
			<adsm:textbox size="30" dataType="text" property="doctoServicoByIdDoctoServReembolsado.clienteByIdClienteDestinatario.pessoa.nmPessoa" disabled="true"/>
		</adsm:textbox>

		<adsm:listbox labelWidth="19%" width="81%" label="notasFiscais" style="width='110px'" property="notasFiscais" optionLabelProperty="nrNotaFiscal" optionProperty="teste" size="5" boxWidth="150"/>

		<adsm:textarea disabled="true" maxLength="500" property="obRecolhimento" label="observacaoRecolhimento" labelWidth="19%" width="81%" columns="80" rows="2"/>

		<adsm:section caption="valorReembolso"/>
		<adsm:textbox property="moeda.sgMoeda" dataType="text" label="moeda" labelWidth="19%" width="81%" disabled="true" />

		<adsm:textbox dataType="currency" property="vlReembolso" label="valorReembolso" size="16" labelWidth="19%" width="81%" disabled="true"/>	

		<adsm:combobox label="situacaoRecibo" property="tpSituacaoRecibo" domain="DM_STATUS_RECIBO_REEMBOLSO" labelWidth="19%" width="31%" disabled="true"/>

		<adsm:textbox property="dhDigitacaoCheque" label="dataHoraDigitacaoCheque" dataType="JTDateTimeZone" labelWidth="19%" width="31%" disabled="true"/>

		<adsm:hidden property="tpDocumentoMir" value="R"/>
		<adsm:buttonBar>
			<adsm:button caption="mir" id="botaoMir" action="/entrega/manterMemorandosInternosResposta" cmd="main">
				<adsm:linkProperty src="tpDocumentoMir" target="tpDocumentoMir"/>
				<adsm:linkProperty src="filial.idFilial" target="reciboReembolso.filial.idFilial"/>
				<adsm:linkProperty src="filial.sgFilial" target="reciboReembolso.filial.sgFilial"/>
				<adsm:linkProperty src="filial.pessoa.nmFantasia" target="reciboReembolso.filial.pessoa.nmFantasia"/>
				<adsm:linkProperty src="idDoctoServico" target="reciboReembolso.idDoctoServico"/>
				<adsm:linkProperty src="nrReciboReembolso" target="reciboReembolso.nrReciboReembolso"/>
			</adsm:button>
			<adsm:button caption="emitir" id="botaoEmitir" onclick="emitirReport();" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

function emitirReport() {
	setElementValue("whichReport", "reemissaoReembolso");
	reportButtonScript("lms.entrega.manterReembolsosAction", 'openPdf', document.Lazy);
}

function initWindow(eventObj){
	if (eventObj.name == "tab_click") {
		setDisabled("botaoEmitir", false);

		var tabGroup = getTabGroup(this.document);	
		tabGroup.getTab("cheq").getElementById("chequeReembolso" + ".form").disabled = false;
		tabGroup.getTab("cheq").getElementById("chequeReembolso" + ".chkSelectAll").disabled = false;

		verificaSituacao(getElementValue("tpSituacaoRecibo"));
	}	
}

function verificaTpSituacao_cb(data, exception){
	onDataLoad_cb(data, exception);
	var tabGroup = getTabGroup(this.document);
	setElementValue(tabGroup.getTab('cheq').getElementById("pesquisa"), "true");
	var tpSituacao = getNestedBeanPropertyValue(data, "tpSituacaoRecibo.value");
	verificaSituacao(tpSituacao);
}

function verificaSituacao(tpSituacao){
	var tabGroup = getTabGroup(this.document);
	tabGroup.setDisabledTab("cheq", false);

	if(tpSituacao == "CD") {
		setDisabled("botaoEmitir", true);
	} else {
		setDisabled("botaoEmitir", false);
	}
}

function onStoreClick() {
	var strService = "lms.entrega.manterReembolsosAction.storeCheques";
	var strCallBack = "storeCustom";
	var tabGroup = getTabGroup(this.document);
	var tabCheq = tabGroup.getTab("cheq");

	storeEditGridScript(strService, strCallBack, document.forms[0], tabCheq.getDocument().forms[1]);
}

function storeCustom_cb(data,exception,key){
	resetValue("dhDigitacaoCheque");
	store_cb(data, exception, key);

	if(exception == undefined) {
		var dataId = new Array();
		setNestedBeanPropertyValue(dataId, "idDoctoServico", getElementValue("idDoctoServico"));
		parent.frames[2].chequeReembolsoGridDef.executeSearch(dataId);				

		var tpSituacao = getNestedBeanPropertyValue(data, "tpSituacaoRecibo");
		verificaSituacao(tpSituacao);
	}	
}

</script>