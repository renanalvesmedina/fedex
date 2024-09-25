<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterNivelWorkflowPropostaAction">
	<adsm:i18nLabels>
		<adsm:include key="LMS-01240"/>
	</adsm:i18nLabels>
	<adsm:form action="/vendas/manterNivelWorkflowProposta" onDataLoadCallBack="preparaTela" idProperty="idComiteNivelMarkup">
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
			required="true"
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
		
		<%-------------------------%>
		<%-- PARCELA PRECO COMBO --%>
		<%-------------------------%>
		<adsm:combobox boxWidth="200" label="parcela" onlyActiveValues="true"
			optionLabelProperty="nmParcelaPreco" optionProperty="idParcelaPreco"
			property="parcelaPreco.idParcelaPreco"
			service="" width="30%"
			labelWidth="20%" autoLoad="false"
			disabled="false"
			required="true">
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
			property="eventoWorkflow.idEventoWorkflow"
			service="lms.vendas.manterNivelWorkflowPropostaAction.findTipoEventoCombo" 
			width="70%"
			labelWidth="20%"
			boxWidth="250"
			required="true">
		</adsm:combobox>
		
		<adsm:checkbox 
			label="lbIsento" 
			property="blIsento" 
			labelWidth="20%" 
			width="80%" 
			onclick="onClickCheckedIsento(this);"
		/>
		
		<%--------------------%>
		<%-- PERC. VARIACAO --%>
		<%--------------------%>
		<adsm:textbox dataType="decimal" minValue="0.00" maxValue="100.00" property="pcVariacao" mask="##0.00" label="pcVariacao"
			maxLength="5" size="10" labelWidth="20%" width="60%" required="true"/>
		
		
		<%--------------------%>
		<%----- VIGÊNCIA -----%>
		<%--------------------%>
		<adsm:range label="vigencia" width="50%" labelWidth="20%" required="true">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>	
			
		
		<adsm:buttonBar>
			<adsm:button id="idAplicarTodasParcelas" caption="aplicarTodasParcelas" onclick="storeParaTodasParcelas()" disabled="true"/>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

function storeParaTodasParcelas() {
	if(confirm(i18NLabel.getLabel("LMS-01240"))) {
		storeButtonScript('lms.vendas.manterNivelWorkflowPropostaAction.storeParaTodasParcelas', 'storeParaTodasParcelas', document.forms[0]);
	}
}

function storeParaTodasParcelas_cb(data, error) {
	store_cb(data, error);
	if (error!=undefined) {
		alert(error);
	} else {
		showSuccessMessage();
		newButtonScript(this.document, true, {name:'newButton_click'});
	}
}

/***************************************/
/* ON CLICK PICKER TABELA PRECO LOOKUP */
/***************************************/
function onclickPickerLookupTabelaPreco() {
	if(!getElementValue("idComiteNivelMarkup")){
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

function preparaTela_cb(dados, error) {
	if (error != undefined) {
		alert(error);
		return;
	}
	onDataLoad_cb(dados, error);
	if (dados != undefined) {
		setElementValue("tpParcelaPreco", getNestedBeanPropertyValue(dados, "parcelaPreco.tpParcelaPreco.description"));
	}
	desabilitaCampos(dados != undefined);
}

function initWindow(eventObj) {
	var disableFields = (eventObj.name == "gridRow_click" || eventObj.name == "storeButton");
	desabilitaCampos(disableFields);
}

function desabilitaCampos(disableFields) {
	setDisabled("parcelaPreco.idParcelaPreco", disableFields);
	setDisabled("pcVariacao", disableFields);
	setDisabled("dtVigenciaInicial", disableFields);
	setDisabled("dtVigenciaFinal", false);
	setDisabled("idAplicarTodasParcelas", disableFields);
	desabilitaTabelaPreco(disableFields);
}

function desabilitaTabelaPreco(disableFields) {
	setDisabled("tabelaPreco.idTabelaPreco", disableFields);
	setDisabled("tabelaPreco.tabelaPrecoString", disableFields);
	setDisabled("tabelaPreco.dsDescricao", true);
}

function onClickCheckedIsento(element){
	if(element.checked){
		setDisabled("eventoWorkflow.idEventoWorkflow", true);
		resetValue("eventoWorkflow.idEventoWorkflow");
		getElement("eventoWorkflow.idEventoWorkflow").required = false;
	} else {
		setDisabled("eventoWorkflow.idEventoWorkflow", false);
		getElement("eventoWorkflow.idEventoWorkflow").required = true;
	}
}
</script>