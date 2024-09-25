<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window>
	<adsm:form action="/tabelaPrecos/emitirTabelasMercurio">
		<adsm:i18nLabels>
			<adsm:include key="LMS-01040"/>
		</adsm:i18nLabels>	

		<adsm:hidden property="tabelaPreco.idTabelaPreco" serializable="true"/>
		<adsm:hidden property="tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco"/>
		<adsm:lookup
			label="tabela" 
			property="tipoTabelaPreco" 
			idProperty="idTabelaPreco"
			criteriaProperty="tabelaPrecoString"
			action="tabelaPrecos/manterTabelasPreco" 
			service="lms.tabelaprecos.emitirTabelasMercurioAction.findLookup" 
			onchange="return changeTabelaPrecoCustom();"
			onDataLoadCallBack="tabelaPrecoCustom"
			onPopupSetValue="changeTabelaPrecoPopup"
			dataType="text" size="11" maxLength="15" labelWidth="15%" width="40%" 
			required="true">
			<adsm:propertyMapping
				modelProperty="dsDescricao"
				relatedProperty="dsDescricao" />
			<adsm:propertyMapping
				modelProperty="idTabelaPreco"
				relatedProperty="tabelaPreco.idTabelaPreco" />
			<adsm:propertyMapping
				modelProperty="tipoTabelaPreco.tpTipoTabelaPreco"
				relatedProperty="tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco"/>	

			<adsm:textbox
			 	property="dsDescricao"
			 	dataType="text"
			 	size="35"
			 	maxLength="60"/>
		</adsm:lookup>
 
		<adsm:checkbox property="emitirDensidades" label="emitirDensidades" labelWidth="12%" width="30%"/>
		<adsm:checkbox property="emitirTonelada" label="pagaFreteTonelada" labelWidth="15%" width="12%"/>

		<%-- TABELA FOB --%>
		<adsm:section caption="tabelaFob"/>
		<%-- UFs DE DESTINO --%>
		<adsm:combobox
			label="ufDestino"
			service="lms.vendas.gerarParametrosPropostaAction.findUnidadeFederativaFromBrasil"
			property="idUnidadeFederativaDestino"
			optionLabelProperty="siglaDescricao"
			optionProperty="idUnidadeFederativa"
			onlyActiveValues="true"
			labelWidth="15%"
			width="35%"
			disabled="true"
			boxWidth="150"/>

		<%-- LOCALIZACAO DE DESTINO --%>
		<adsm:combobox
			label="tipoLocalizacao"
			service="lms.vendas.gerarParametrosPropostaAction.findTipoLocalizacaoComercial"
			property="idTipoLocalizacaoMunicipioDestino"
			optionLabelProperty="dsTipoLocalizacaoMunicipio"
			optionProperty="idTipoLocalizacaoMunicipio"
			onlyActiveValues="true"
			labelWidth="15%"
			width="35%"
			disabled="true"
			boxWidth="150"/>

		<%-- TABELA PACOTINHO --%>
		<adsm:section caption="tabelaPacotinho"/>
		<%-- UFs DE ORIGEM --%>
		<adsm:combobox
			label="ufOrigem"
			service="lms.vendas.gerarParametrosPropostaAction.findUnidadeFederativaFromBrasil"
			property="idUnidadeFederativaOrigem"
			optionLabelProperty="siglaDescricao"
			optionProperty="idUnidadeFederativa"
			onlyActiveValues="true"
			labelWidth="15%"
			width="35%"
			disabled="true"
			boxWidth="150"/>

		<%-- LOCALIZACAO DE ORIGEM --%>
		<adsm:checkbox property="isCapital" label="capital" labelWidth="15%" width="12%" />

		<adsm:section  caption="tabelaDoAereo"/>
		<adsm:lookup property="aeroportoOrigem" idProperty="idAeroporto" criteriaProperty="sgAeroporto"
			service="lms.tabelaprecos.emitirTabelasMercurioAction.findLookupAeroporto"
			action="municipios/manterAeroportos" exactMatch="true" dataType="text"
			label="aeroportoOrigem" 
			labelWidth="15%" width="35%" 
			size="3" maxLength="3"
			disabled="true">
			<adsm:textbox property="nmAeroportoOrigem" disabled="true" dataType="text"/>
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="nmAeroportoOrigem"/>
		</adsm:lookup>

		<adsm:lookup property="aeroportoDestino" idProperty="idAeroporto" criteriaProperty="sgAeroporto"
			service="lms.tabelaprecos.emitirTabelasMercurioAction.findLookupAeroporto"
			action="municipios/manterAeroportos" exactMatch="true" dataType="text"
			label="aeroportoDestino" 
			labelWidth="15%" width="35%" 
			size="3" maxLength="3"
			disabled="true">
			<adsm:textbox property="nmAeroportoDestino" disabled="true" dataType="text"/>
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="nmAeroportoDestino"/>
		</adsm:lookup>

		<adsm:buttonBar>
			<adsm:button caption="visualizar" id="btnVisualizar" onclick="emitirPdf()" disabled="false"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
<script>

document.getElementById("isCapital").checked = true;

function initWindow(){
	document.getElementById("btnVisualizar").disabled = false;
}

function tabelaPrecoCustom_cb(data) {
	
	verifySubTipoTabelaPreco(data[0]);
	lookupExactMatch({e:getElement("tipoTabelaPreco.idTabelaPreco"), data:data});
	
	if(data[0] == undefined){
		return false;
	}
	
	var tipoTabelaPreco = getNestedBeanPropertyValue( data[0],"tipoTabelaPreco.tpTipoTabelaPreco.value");
	if (tipoTabelaPreco != undefined && tipoTabelaPreco == "A"){
		setDisabled("aeroportoOrigem.idAeroporto",false);
		setDisabled("aeroportoDestino.idAeroporto",false);
	}else {
		resetDisableTabelaAereo();
	} 
}
function changeTabelaPrecoPopup(data){
	verifySubTipoTabelaPreco(data);
}

function resetDisableTabelaAereo(){
	setDisabled("aeroportoOrigem.idAeroporto",true);
	setDisabled("aeroportoDestino.idAeroporto",true);
	setElementValue("aeroportoOrigem.idAeroporto","");
	setElementValue("aeroportoOrigem.sgAeroporto","");
	setElementValue("nmAeroportoOrigem","");
	setElementValue("aeroportoDestino.idAeroporto","");
	setElementValue("aeroportoDestino.sgAeroporto","");
	setElementValue("nmAeroportoDestino","");
}

function emitirPdf(){
	//somente aeroporto origem ou destino deve estar preenchido
	if (getElementValue("aeroportoOrigem.idAeroporto")!="" && getElementValue("aeroportoDestino.idAeroporto")!=""){
		var msg = i18NLabel.getLabel("LMS-01040");
		alert(msg.replace(/[{]0[}]/,getElement("aeroportoDestino.sgAeroporto").label));
		setFocus("aeroportoDestino.sgAeroporto");
		return;
	} 
	
	// se tudo estiver certo, emite o relatório
	reportButtonScript('lms.tabelaprecos.emitirTabelasMercurioAction.execute', 'openPdf', document.forms[0]);
}

function changeTabelaPrecoCustom() {
	var ret = tipoTabelaPreco_tabelaPrecoStringOnChangeHandler();
	
	if(getElementValue("tipoTabelaPreco.tabelaPrecoString") == "") {
		resetAndDisableFields();
	}

	if( getElementValue("tipoTabelaPreco.idTabelaPreco") == ""){
		resetDisableTabelaAereo();
	}
	
	return ret;
}

function verifySubTipoTabelaPreco(data){
	resetAndDisableFields();

	if(data != undefined) {
		var tpSubtipoTabelaPreco = getNestedBeanPropertyValue(data, "subtipoTabelaPreco.tpSubtipoTabelaPreco");
		if(tpSubtipoTabelaPreco == "F" || tpSubtipoTabelaPreco == "P") {
			if(tpSubtipoTabelaPreco == "F") {
				setDisabled("idUnidadeFederativaDestino", false);
				setDisabled("idTipoLocalizacaoMunicipioDestino", false);
				getElement("idUnidadeFederativaDestino").required = true;
				getElement("idTipoLocalizacaoMunicipioDestino").required = true;
			} else if(tpSubtipoTabelaPreco == "P") {
				setDisabled("idUnidadeFederativaOrigem", false);
				getElement("idUnidadeFederativaOrigem").required = true;
			}
		}
	}
}

function resetAndDisableFields() {
	setElementValue("idUnidadeFederativaDestino", "");
	setElementValue("idTipoLocalizacaoMunicipioDestino", "");
	setElementValue("idUnidadeFederativaOrigem", "");
	
	setDisabled("idUnidadeFederativaDestino", true);
	setDisabled("idTipoLocalizacaoMunicipioDestino", true);
	setDisabled("idUnidadeFederativaOrigem", true);

	getElement("idUnidadeFederativaDestino").required = false;
	getElement("idTipoLocalizacaoMunicipioDestino").required = false;
	getElement("idUnidadeFederativaOrigem").required = false;
	
}
</script>
</adsm:window> 