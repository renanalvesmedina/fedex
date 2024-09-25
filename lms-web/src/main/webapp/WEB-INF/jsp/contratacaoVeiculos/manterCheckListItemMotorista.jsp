<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<script>
function realizaPaginacao(){
	onPageLoad();
	
	setMasterLink(this.document, true);
	
		
	var idChecklistMeioTransporte = getElementValue("idChecklistMeioTransporte");
	
	var idTipoMeioTransporte = getElementValue("idTipoMeioTransporte");
	
	var tpIdentificacaoMot1 = getElementValue("tpIdentificacaoMot1");
	var nrIdentificacaoFormatadoMot1 = getElementValue("nrIdentificacaoFormatadoMot1");
	var nmPessoaMot1 = getElementValue("nmPessoaMot1");
	var idPessoaMot1 = getElementValue("idPessoaMot1");
	
	var tpIdentificacaoMot2 = getElementValue("tpIdentificacaoMot2");
	var nrIdentificacaoFormatadoMot2 = getElementValue("nrIdentificacaoFormatadoMot2");
	var nmPessoaMot2= getElementValue("nmPessoaMot2");
	var idPessoaMot2 = getElementValue("idPessoaMot2");
	
	var tpSituacaoChecklist = getElementValue("tpSituacaoChecklist");
	if (idTipoMeioTransporte != undefined && idTipoMeioTransporte != ''){
		var dataId = new Array();
		setNestedBeanPropertyValue(dataId, "idTipoMeioTransporte", idTipoMeioTransporte);
		setNestedBeanPropertyValue(dataId, "idChecklistMeioTransporte", idChecklistMeioTransporte);
		
		setNestedBeanPropertyValue(dataId, "tpIdentificacaoMot1", tpIdentificacaoMot1);
		setNestedBeanPropertyValue(dataId, "nrIdentificacaoFormatadoMot1", nrIdentificacaoFormatadoMot1);
		setNestedBeanPropertyValue(dataId, "nmPessoaMot1", nmPessoaMot1);
		setNestedBeanPropertyValue(dataId, "idPessoaMot1", idPessoaMot1);
		
		setNestedBeanPropertyValue(dataId, "tpIdentificacaoMot2", tpIdentificacaoMot2);
		setNestedBeanPropertyValue(dataId, "nrIdentificacaoFormatadoMot2", nrIdentificacaoFormatadoMot2);
		setNestedBeanPropertyValue(dataId, "nmPessoaMot2", nmPessoaMot2);
		setNestedBeanPropertyValue(dataId, "idPessoaMot2", idPessoaMot2);
		
		itChecklistTpMeioTranspGridDef.executeSearch(dataId);
		
	} else
		itChecklistTpMeioTranspGridDef.resetGrid();
	
	if(tpSituacaoChecklist == 'Aprovado' || tpSituacaoChecklist == 'Reprovado'){
		
		verifyDataRealizacao();
	}else
		setDisabled("botaoSalvar",false);
		
}
</script>
<adsm:window service="lms.contratacaoveiculos.manterCheckListAction" onPageLoad="realizaPaginacao">
  
  <adsm:form action="/contratacaoVeiculos/manterCheckList" idProperty="idItChecklistTpMeioTransp">
  
  	<adsm:hidden property="idTipoMeioTransporte"/>
  	<adsm:hidden property="isDataAtual" />
  	
	<adsm:hidden property="tpIdentificacaoMot1"/>
	<adsm:hidden property="nrIdentificacaoFormatadoMot1"/>
	<adsm:hidden property="nmPessoaMot1"/>
	<adsm:hidden property="idPessoaMot1"/>
	
	<adsm:hidden property="tpIdentificacaoMot2"/>
	<adsm:hidden property="nrIdentificacaoFormatadoMot2"/>
	<adsm:hidden property="nmPessoaMot2"/>
	<adsm:hidden property="idPessoaMot2"/>
	
	<adsm:hidden property="tpSituacaoChecklist"/>
	
	<adsm:hidden property="idChecklistMeioTransporte"/>
	
	<adsm:hidden property="isItemMotorista" value="M"/>
	
	
	<adsm:grid property="itChecklistTpMeioTransp" idProperty="idItChecklistTpMeioTransp" title="checklistItemMotorista" service="lms.contratacaoveiculos.manterCheckListAction.findPaginatedItChecklistMotByIdTipoMeioTransporteSolicitacao" onRowClick="returnFalse();"  scrollBars="vertical" selectionMode="none" showPagging="false" gridHeight="380" unique="true" >
		<adsm:gridColumn title="identificacao" property="tipoIdentificacao" width="50"/>
		<adsm:gridColumn title="" property="nrIdentificacaoFormatado" width="150" align="right"/>
		<adsm:gridColumn title="nome" property="nmPessoa" width="150"/>
		<adsm:gridColumn title="item" property="dsItemCheckList" width="150"/>
		<adsm:gridColumn title="obrigatorio" property="blObrigatorioAprovacao" width="80" renderMode="image-check"/>
		<adsm:editColumn field="ComboBox" title="aprovado" domain="DM_SIM_NAO" width="100" property="apr"/>
		<adsm:editColumn field="textbox" title="" width="1" property="idMotorista" />
		<adsm:editColumn field="textbox" title="" width="1" property="idRespostaChecklist" />
	</adsm:grid>
	
	<adsm:buttonBar freeLayout="false" >
			<adsm:button id="botaoSalvar" caption="salvar" onclick="if(itChecklistTpMeioTranspGridDef.currentRowCount>0)storeButtonScript('lms.contratacaoveiculos.manterCheckListAction.storeChecklistTipoMeioTransporte', 'myStore', this.form);"/>	
			<adsm:button caption="fechar" onclick="self.close()" disabled="false"/>		
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

	function returnFalse(){
		return false;
	}
	function verifyDataRealizacao(){
		
		var isDataAtual = getElementValue("isDataAtual");
		if(isDataAtual != "")
			setDisabled("botaoSalvar", false);
		else
			setDisabled("botaoSalvar", true);	
	}
	
	function myStore_cb(data,exception){
		store_cb(data,exception);
		if(exception == undefined){
			setElementValue("tpSituacaoChecklist",getNestedBeanPropertyValue(data,"situacaoChecklist"));
			verifyDataRealizacao();
			updateStatus();
			itChecklistTpMeioTranspGridDef.executeLastSearch();
			
		}	
	}
	
	function updateStatus(){
		if(getElementValue("tpSituacaoChecklist") == 'A')
			setElementValue("tpSituacaoChecklist", "Aprovado");
		else if (getElementValue("tpSituacaoChecklist") == 'R')
			setElementValue("tpSituacaoChecklist", "Reprovado");
		else if (getElementValue("tpSituacaoChecklist") == 'I')
			setElementValue("tpSituacaoChecklist", "Incompleto");		
		dialogArguments.updateStatus(getElementValue("tpSituacaoChecklist"));
	}
	
	
	//tipo de meio de transporte	
	if (dialogArguments.document.forms[0].elements["tipoMeioTransporte.idTipoMeioTransporte"])
		document.forms[0].elements["idTipoMeioTransporte"].value = dialogArguments.document.forms[0].elements["tipoMeioTransporte.idTipoMeioTransporte"].value;
	
	// 1º motorista	
	if (dialogArguments.document.forms[0].elements["pessoaByIdPrimeiroMotorista.tpIdentificacao"])
		document.forms[0].elements["tpIdentificacaoMot1"].value = dialogArguments.document.forms[0].elements["pessoaByIdPrimeiroMotorista.tpIdentificacao"].value;	
		
	if (dialogArguments.document.forms[0].elements["pessoaByIdPrimeiroMotorista.nrIdentificacao"])
		document.forms[0].elements["nrIdentificacaoFormatadoMot1"].value = dialogArguments.document.forms[0].elements["pessoaByIdPrimeiroMotorista.nrIdentificacao"].value;		
		
	if (dialogArguments.document.forms[0].elements["pessoaByIdPrimeiroMotorista.nmPessoa"])
		document.forms[0].elements["nmPessoaMot1"].value = dialogArguments.document.forms[0].elements["pessoaByIdPrimeiroMotorista.nmPessoa"].value;			
		
	if (dialogArguments.document.forms[0].elements["pessoaByIdPrimeiroMotorista.idPessoa"])
		document.forms[0].elements["idPessoaMot1"].value = dialogArguments.document.forms[0].elements["pessoaByIdPrimeiroMotorista.idPessoa"].value;				
	
	//2º motorista
	
	if (dialogArguments.document.forms[0].elements["pessoaByIdSegundoMotorista.tpIdentificacao"])
		document.forms[0].elements["tpIdentificacaoMot2"].value = dialogArguments.document.forms[0].elements["pessoaByIdSegundoMotorista.tpIdentificacao"].value;	
		
	if (dialogArguments.document.forms[0].elements["pessoaByIdSegundoMotorista.nrIdentificacao"])
		document.forms[0].elements["nrIdentificacaoFormatadoMot2"].value = dialogArguments.document.forms[0].elements["pessoaByIdSegundoMotorista.nrIdentificacao"].value;		
		
	if (dialogArguments.document.forms[0].elements["pessoaByIdSegundoMotorista.nmPessoa"])
		document.forms[0].elements["nmPessoaMot2"].value = dialogArguments.document.forms[0].elements["pessoaByIdSegundoMotorista.nmPessoa"].value;			
		
	if (dialogArguments.document.forms[0].elements["pessoaByIdSegundoMotorista.idPessoa"])
		document.forms[0].elements["idPessoaMot2"].value = dialogArguments.document.forms[0].elements["pessoaByIdSegundoMotorista.idPessoa"].value;					
	
	//situacao checklist	
	if (dialogArguments.document.forms[0].elements["tpSituacao.description"])
		document.forms[0].elements["tpSituacaoChecklist"].value = dialogArguments.document.forms[0].elements["tpSituacao.description"].value;			
		
	if (dialogArguments.document.forms[0].elements["idChecklistMeioTransporte"])
		document.forms[0].elements["idChecklistMeioTransporte"].value = dialogArguments.document.forms[0].elements["idChecklistMeioTransporte"].value;				

	//data de realizacao
	
	if (dialogArguments.document.forms[0].elements["isDataAtual"]){
		document.forms[0].elements["isDataAtual"].value = dialogArguments.document.forms[0].elements["isDataAtual"].value;				
		
	}		
</script>

