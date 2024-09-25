<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tabelaprecos.simularNovosPrecosCiasAereasAction">
	<adsm:form action="/tabelaPrecos/simularNovosPrecosCiasAereas">
	
		<adsm:i18nLabels>
			<adsm:include key="LMS-00055" />
		</adsm:i18nLabels>
	
		<adsm:combobox 
			label="tipoTabela"
			property="tipoTabelaPreco.idTipoTabelaPreco"
			optionProperty="idTipoTabelaPreco"
			optionLabelProperty="dsDescricao"
			service="lms.tabelaprecos.simularNovosPrecosCiasAereasAction.findTipoTabelaPreco" />
			
		<adsm:combobox 
			label="subtipoTabela"
			property="subtipoTabelaPreco.idSubtipoTabelaPreco"
			optionProperty="idSubtipoTabelaPreco"
			optionLabelProperty="tpSubtipoTabelaPreco"
			service="lms.tabelaprecos.simularNovosPrecosCiasAereasAction.findSubtipoTabelaPreco" />
			
		<adsm:combobox 
			label="ciaAerea" 
			property="empresaByIdEmpresaCadastrada.idEmpresa" 
			service="lms.tabelaprecos.simularNovosPrecosCiasAereasAction.findCiasAereas" 
			optionLabelProperty="pessoa.nmPessoa" 
			optionProperty="idEmpresa" 
			onlyActiveValues="false"
			boxWidth="250"/>
		
		<adsm:textbox 
			label="dataReferencia"
			property="dtReferencia"
			dataType="JTDate" />

		<adsm:buttonBar freeLayout="true">
			<adsm:button 
				id="btnConsultar"
				caption="consultar" 
				onclick="return onClickConsultar();"
				disabled="false"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid 
		idProperty="idTabelaPreco" 
		property="tabelaPrecoList" 
		detailFrameName="reaj"
		gridHeight="200" 
		unique="true"
		rows="12">
		
		<adsm:gridColumn 
			title="tabela" 
			property="tabelaPrecoString" 
			width="20%" />
		
		<adsm:gridColumn 
			title="ciaAerea" 
			property="empresaByIdEmpresaCadastrada.pessoa.nmPessoa" 
			width="16%" />
		
		<adsm:gridColumn 
			dataType="percent"
			title="reajuste" 
			property="pcReajuste" 
			width="16%" 
			unit="percent" />
		
		<adsm:gridColumn 
			dataType="JTDate"
			title="vigenciaInicial" 
			property="dtVigenciaInicial" 
			width="16%" />
			
		<adsm:gridColumn 
			dataType="JTDate"
			title="vigenciaFinal" 
			property="dtVigenciaFinal" 
			width="16%" />
		
		<adsm:gridColumn 
			title="efetivada" 
			property="blEfetivada" 
			renderMode="image-check"
			width="16%" />
		
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
		
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
<!--
function initWindow(eventObj) {
	changeConsultarButtonStatus(false);
	changeAbasStatus(true);
	cleanAbas();
	reconfiguraSessao();
}

function onClickConsultar() {
	if (!validateSearch()) {
		alert(i18NLabel.getLabel("LMS-00055"));
		return false;
	} else {
		findButtonScript("tabelaPrecoList", document.forms[0]);
	}
}

function changeConsultarButtonStatus(status) {
	setDisabled("btnConsultar", status);
}

function changeAbasStatus(status) {
	var tabGroup = getTabGroup(this.document);
	if(tabGroup != undefined) {
		tabGroup.setDisabledTab("reajEsp", status);
		tabGroup.setDisabledTab("reajExce", status);
	}
}

function validateSearch() {
	var search = true;
	if (getElementValue("tipoTabelaPreco.idTipoTabelaPreco")       == "" &&
		getElementValue("subtipoTabelaPreco.idSubtipoTabelaPreco") == "" &&
		getElementValue("empresaByIdEmpresaCadastrada.idEmpresa")  == "" &&
		getElementValue("dtReferencia")                            == "") {
		search = false;
	}
	return search;
}

function reconfiguraSessao() {
	var service = "lms.tabelaprecos.simularNovosPrecosCiasAereasAction.reconfiguraSessao";
	var sdo = createServiceDataObject(service);
	xmit({serviceDataObjects:[sdo]});
}

function cleanAbas() {
	var tabGroup = getTabGroup(this.document);
	var tabReaj = tabGroup.getTab("reaj");
	cleanButtonScript(tabReaj.getDocument(), false);
	var tabExce = tabGroup.getTab("reajExce");
	cleanButtonScript(tabExce.getDocument(), false);
	var tabEsp = tabGroup.getTab("reajEsp");
	cleanButtonScript(tabEsp.getDocument(), false);
}
//-->
</script>