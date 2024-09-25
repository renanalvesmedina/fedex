<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.vendas.manterNivelWorkflowPropostaAction">
	<adsm:form action="/vendas/manterNivelWorkflowProposta">
	
		<adsm:hidden property="idSubtipoTabelaPreco" serializable="false"/>
		<adsm:hidden property="idTipoTabelaPreco" serializable="false"/>
		<adsm:hidden property="parcelaPreco.nmParcelaPreco" />
		
		<%-------------------%>
		<%-- TABELA LOOKUP --%>
		<%-------------------%>
		<adsm:lookup 
			action="/tabelaPrecos/manterTabelasPreco" 
			criteriaProperty="tabelaPrecoString" 
			dataType="text" 
			idProperty="idTabelaPreco"
			labelWidth="20%"
			label="tabela" 
			onclickPicker="onclickPickerLookupTabelaPreco()"
			property="tabelaPreco" 
			service="lms.vendas.manterNivelWorkflowPropostaAction.findLookupTabelaPreco" 
			size="8" 
			maxLength="7"
			width="45%"
			onDataLoadCallBack="dataLoadTabelaPreco"
			>

		   	<adsm:propertyMapping 
		   		modelProperty="dsDescricao" 
		   		relatedProperty="tabelaPreco.dsDescricao"
		   	/>
		   	<adsm:propertyMapping criteriaProperty="idTipoTabelaPreco" modelProperty="tipoTabelaPreco.idTipoTabelaPreco"/>
			<adsm:propertyMapping criteriaProperty="idSubtipoTabelaPreco" modelProperty="subtipoTabelaPreco.idSubtipoTabelaPreco"/>
            <adsm:textbox dataType="text" property="tabelaPreco.dsDescricao" 
            	size="30" maxLength="30" disabled="true"/>
        </adsm:lookup>
		
		<%-------------------%>
		<%-- SERVICO COMBO --%>
		<%-------------------%>
		<adsm:combobox boxWidth="200" label="parcela" onlyActiveValues="true"
			optionLabelProperty="nmParcelaPreco" optionProperty="idParcelaPreco"
			property="parcelaPreco.idParcelaPreco"
			service="" width="30%"
			labelWidth="20%" autoLoad="false"
			disabled="false">
			<adsm:propertyMapping relatedProperty="parcelaPreco.nmParcelaPreco" modelProperty="nmParcelaPreco" />
			<adsm:propertyMapping relatedProperty="tpParcelaPreco" modelProperty="tpParcelaPreco.description"/>
		</adsm:combobox>
		<adsm:textbox dataType="text" property="tpParcelaPreco" label="tipoParcela" size="30" maxLength="30" disabled="true"/>
		
		<%-------------------------%>
		<%-- EVENTO COMBO 		 --%>
		<%-------------------------%>		
		<adsm:combobox 
			label="evento" 
			optionLabelProperty="dsTipoEvento" 
			optionProperty="idTipoEvento"
			property="eventoWorkflow.tipoEvento.idTipoEvento"
			service="lms.vendas.manterNivelWorkflowPropostaAction.findTipoEventoCombo" 
			width="70%"
			labelWidth="20%"
			boxWidth="250"
			>
		</adsm:combobox>
			
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="comiteNivelMarkup"/>
			<adsm:resetButton/>
		</adsm:buttonBar>		
	</adsm:form>
	<adsm:grid idProperty="idComiteNivelMarkup" property="comiteNivelMarkup" defaultOrder="parcelaPreco_.nmParcelaPreco">
		<adsm:gridColumn width="25%" title="parcela" 	property="parcelaPreco.nmParcelaPreco"/>
		<adsm:gridColumn width="25%" title="descricao"  property="parcelaPreco.dsParcelaPreco"/>
		<adsm:gridColumn width="25%" title="evento" 	property="eventoWorkflow.dsTipoEvento"/>
		<adsm:gridColumn width="15%" title="pcVariacao" property="pcVariacao" dataType="integer"/>
		<adsm:gridColumn width="10%" title="situacao"   property="tpSituacao" isDomain="true" />
		<adsm:buttonBar> 
			<adsm:removeButton/>
		</adsm:buttonBar>			
	</adsm:grid>	
</adsm:window>
<script>
/***************************************/
/* ON CLICK PICKER TABELA PRECO LOOKUP */
/***************************************/
function onclickPickerLookupTabelaPreco() {
	var tabelaPrecoString = getElementValue("tabelaPreco.tabelaPrecoString");
	if(tabelaPrecoString != "")	{
		setElementValue("tabelaPreco.tabelaPrecoString","");
	}
	lookupClickPicker({e:document.forms[0].elements['tabelaPreco.idTabelaPreco']});
	if(getElementValue("tabelaPreco.tabelaPrecoString")=='' && tabelaPrecoString != "")	{
		setElementValue("tabelaPreco.tabelaPrecoString",tabelaPrecoString);
	}
	if(getElementValue("tabelaPreco.idTabelaPreco")){
		loadComboBoxParcela(getElementValue("tabelaPreco.idTabelaPreco"));
	}
}

function dataLoadTabelaPreco_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return;
	}
	if (data != undefined && data.length == 1) {
		if(data[0].idTabelaPreco){
			loadComboBoxParcela(data[0].idTabelaPreco);
		}
	}
	return tabelaPreco_tabelaPrecoString_exactMatch_cb(data);
}

function loadComboBoxParcela(idTabelaPreco) {
	var sdo = createServiceDataObject("lms.vendas.manterNivelWorkflowPropostaAction.findParcelaCombo", "loadComboBoxParcela", {idTabelaPreco:idTabelaPreco});
	xmit({serviceDataObjects:[sdo]});
}

function loadComboBoxParcela_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return;
	}
	comboboxLoadOptions({data:data, e:getElement("parcelaPreco.idParcelaPreco")});
}

</script>