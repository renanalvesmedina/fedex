<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<script>
function realizaPaginacao(){
	onPageLoad();
	
	setMasterLink(this.document, true);
	
	var idTipoMeioTransporte = getElementValue("idTipoMeioTransporte");
	var nrIdentificacaoMeioTransp = getElementValue("nrIdentificacaoMeioTransp");
	var nrFrotaMT = getElementValue("nrFrotaMT");
	var nrFrotaSR = getElementValue("nrFrotaSR");
	var nrIdentificacaoSemiReboque = getElementValue("nrIdentificacaoSemiReboque");
	var idTipoMeioTransporteSemiReboque = getElementValue("idTipoMeioTransporteSemiReboque");
	var idChecklistMeioTransporte = getElementValue("idChecklistMeioTransporte");
	var tpSituacaoChecklist = getElementValue("tpSituacaoChecklist");
	
	if (idTipoMeioTransporte != undefined && idTipoMeioTransporte != ''){
		var dataId = new Array();
		setNestedBeanPropertyValue(dataId, "idTipoMeioTransporte", idTipoMeioTransporte);
		setNestedBeanPropertyValue(dataId, "nrIdentificacaoMeioTransp", nrIdentificacaoMeioTransp);
		setNestedBeanPropertyValue(dataId, "nrFrotaMT", nrFrotaMT);
		setNestedBeanPropertyValue(dataId, "nrIdentificacaoSemiReboque", nrIdentificacaoSemiReboque);
		setNestedBeanPropertyValue(dataId, "idTipoMeioTransporteSemiReboque", idTipoMeioTransporteSemiReboque);
		setNestedBeanPropertyValue(dataId, "idChecklistMeioTransporte", idChecklistMeioTransporte);
		itChecklistTpMeioTranspGridDef.executeSearch(dataId);
	} else
		itChecklistTpMeioTranspGridDef.resetGrid();
	
	if(tpSituacaoChecklist == 'Aprovado' || tpSituacaoChecklist == 'Reprovado'){
		verifyDataRealizacao();
	}else
		setDisabled("botaoSalvar",false);
		
}



</script>
<adsm:window service="lms.contratacaoveiculos.manterCheckListAction" onPageLoad="realizaPaginacao" >
	<adsm:form action="/contratacaoVeiculos/manterCheckList" idProperty="idItChecklistTpMeioTransp">
	<adsm:hidden property="isDataAtual" />	
	<adsm:hidden property="nrFrotaMT" />	
	<adsm:hidden property="nrFrotaSR" />
	<adsm:hidden property="idTipoMeioTransporte"/>
	<adsm:hidden property="nrIdentificacaoMeioTransp"/>
	<adsm:hidden property="nrIdentificacaoSemiReboque"/>
	<adsm:hidden property="idTipoMeioTransporteSemiReboque"/>
	
	<adsm:hidden property="idChecklistMeioTransporte"/>
	<adsm:hidden property="tpSituacaoChecklist"/>
		
	<adsm:grid property="itChecklistTpMeioTransp" idProperty="idItChecklistTpMeioTransp" onRowClick="returnFalse();" title="checklistItemMeioTransporte"
			service="lms.contratacaoveiculos.manterCheckListAction.findPaginatedItChecklistByIdTipoMeioTransporteSolicitacao" 
			scrollBars="vertical" selectionMode="none" showPagging="false" gridHeight="380" unique="true" >
		<adsm:gridColumn title="modalidade" property="tpMeioTransporte" isDomain="true" width="100"/>
		<adsm:gridColumn title="tipoMeioTransporte" property="dsTipoMeioTransporte" width="100"/>
		
		<adsm:gridColumn title="meioTransporte" property="nrFrota" width="50"/>
		<adsm:gridColumn title="" property="nrIdentificacao" width="80"/>
		
		<adsm:gridColumn title="item" property="dsItemCheckList" width="150"/>
		<adsm:gridColumn title="obrigatorio" property="blObrigatorioAprovacao" width="100" renderMode="image-check"/>
		<adsm:editColumn field="ComboBox" title="aprovado" domain="DM_SIM_NAO" width="100" property="apr"/>
		<adsm:editColumn field="textbox" title="" width="1" property="idRespostaChecklist" />
		
		<adsm:buttonBar freeLayout="false" >
			<adsm:button id="botaoSalvar" caption="salvar" onclick="if(itChecklistTpMeioTranspGridDef.currentRowCount>0)storeButtonScript('lms.contratacaoveiculos.manterCheckListAction.storeChecklistTipoMeioTransporte', 'myStore', this.form);"/>	
			<adsm:button caption="fechar" onclick="self.close()" disabled="false"/>	
		</adsm:buttonBar>
	</adsm:grid>
</adsm:form>
</adsm:window>
<script>

	function verifyDataRealizacao(){
		var isDataAtual = getElementValue("isDataAtual");
		if(isDataAtual != "")
			setDisabled("botaoSalvar", false);
		else
			setDisabled("botaoSalvar", true);	
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
	
	function returnFalse(){
		return false;
	}
	
	function myStore_cb(data,exception){
		store_cb(data,exception);
		if(exception == undefined){
			setElementValue("tpSituacaoChecklist",getNestedBeanPropertyValue(data,"situacaoChecklist"));
			updateStatus();
			verifyDataRealizacao();
			itChecklistTpMeioTranspGridDef.executeLastSearch();
		}	
	}
	
	if (dialogArguments.document.forms[0].elements["tipoMeioTransporte.idTipoMeioTransporte"])
		document.forms[0].elements["idTipoMeioTransporte"].value = dialogArguments.document.forms[0].elements["tipoMeioTransporte.idTipoMeioTransporte"].value;
		
	if (dialogArguments.document.forms[0].elements["solicitacaoContratacao.nrIdentificacaoMeioTransp"])
		document.forms[0].elements["nrIdentificacaoMeioTransp"].value = dialogArguments.document.forms[0].elements["solicitacaoContratacao.nrIdentificacaoMeioTransp"].value;	
	
	if (dialogArguments.document.forms[0].elements["nrFrotaMT"])
		document.forms[0].elements["nrFrotaMT"].value = dialogArguments.document.forms[0].elements["nrFrotaMT"].value;	
		
	if (dialogArguments.document.forms[0].elements["solicitacaoContratacao.nrIdentificacaoSemiReboque"])
		document.forms[0].elements["nrIdentificacaoSemiReboque"].value = dialogArguments.document.forms[0].elements["solicitacaoContratacao.nrIdentificacaoSemiReboque"].value;		
	
	if (dialogArguments.document.forms[0].elements["nrFrotaSR"])
		document.forms[0].elements["nrFrotaSR"].value = dialogArguments.document.forms[0].elements["nrFrotaSR"].value;	
		
	if (dialogArguments.document.forms[0].elements["idChecklistMeioTransporte"])
		document.forms[0].elements["idChecklistMeioTransporte"].value = dialogArguments.document.forms[0].elements["idChecklistMeioTransporte"].value;			
	
	if (dialogArguments.document.forms[0].elements["idTipoMeioTransporteSemiReboque"])
		document.forms[0].elements["idTipoMeioTransporteSemiReboque"].value = dialogArguments.document.forms[0].elements["idTipoMeioTransporteSemiReboque"].value;			
		
	if (dialogArguments.document.forms[0].elements["tpSituacao.description"])
		document.forms[0].elements["tpSituacaoChecklist"].value = dialogArguments.document.forms[0].elements["tpSituacao.description"].value;			
			
	//data de realizacao
	if (dialogArguments.document.forms[0].elements["isDataAtual"]){
		document.forms[0].elements["isDataAtual"].value = dialogArguments.document.forms[0].elements["isDataAtual"].value;				
		
	}		
		
</script>
