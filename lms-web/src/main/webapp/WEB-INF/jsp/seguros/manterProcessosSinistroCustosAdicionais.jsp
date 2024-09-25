<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.seguros.manterProcessosSinistroAction">
	<adsm:form action="/seguros/manterProcessosSinistro" height="103" idProperty="idCustoAdicionalSinistro" service="lms.seguros.manterProcessosSinistroAction.findCustoAdicionalById" onDataLoadCallBack="dataLoad">
	
		<adsm:hidden property="idProcessoSinistro"/>	

		<adsm:textbox label="numeroProcesso" property="nrProcessoSinistro" dataType="text" labelWidth="15%" width="85%" disabled="true" />

		<adsm:textbox dataType="text" property="dsCustoAdicional" label="descricao" size="50"  maxLength="60" width="40%" required="true" />
		<adsm:textbox dataType="JTDate" property="dtCustoAdicional" label="dataCustoAdicional" labelWidth="15%" width="30%" required="true" />

		<adsm:complement label="valor" labelWidth="15%" width="40%" separator="branco" required="true">
		<adsm:combobox property="moeda.idMoeda" 
					   boxWidth="85"
					   service="lms.seguros.manterProcessosSinistroAction.findComboMoeda" 
					   optionProperty="idMoeda" 
					   optionLabelProperty="siglaSimbolo" 
					   onlyActiveValues="true"
		/>
			<adsm:textbox dataType="currency" property="vlCustoAdicional" size="25" maxLength="19"/>
		</adsm:complement>
		
		<adsm:textbox label="valorReembolsado" property="vlReembolsado" dataType="currency" size="25"  maxLength="50" onchange="onVlReembolsadoChange();" labelWidth="15%" width="30%"/>					   

		<adsm:textbox dataType="JTDate" property="dtReembolsado" label="dataReembolso" width="45%" disabled="true" />
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton id="storeButton" service="lms.seguros.manterProcessosSinistroAction.storeCustosAdicionais" callbackProperty="storeCallback"/>
			<adsm:newButton id="newButton" />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="custoAdicionalSinistro" 
				idProperty="idCustoAdicionalSinistro" 
				selectionMode="check" 
				service="lms.seguros.manterProcessosSinistroAction.findPaginatedCustoAdicional" 
				rowCountService="lms.seguros.manterProcessosSinistroAction.getRowCountCustoAdicional" 
				detailFrameName="custosAdicionais"
				scrollBars="horizontal"
				rows="9"
				>
		<adsm:gridColumn title="descricao"          property="dsCustoAdicional" width="245" />
		<adsm:gridColumn title="dataCustoAdicional" property="dtCustoAdicional" width="120" align="center" dataType="JTDate" />

		<adsm:gridColumnGroup customSeparator=" " >
			<adsm:gridColumn title="valor" 				property="sgMoeda01" width="30"/>
			<adsm:gridColumn title=""                   property="dsSimbolo01" width="30"/>
		</adsm:gridColumnGroup>
			<adsm:gridColumn title=""                   property="vlCustoAdicional" width="120" align="right" dataType="currency"/>

		<adsm:gridColumnGroup customSeparator=" " >
			<adsm:gridColumn title="valorReembolsado"	property="sgMoeda02" width="30"/>
			<adsm:gridColumn title=""                   property="dsSimbolo02" width="30"/>
		</adsm:gridColumnGroup>
			<adsm:gridColumn title=""                   property="vlReembolsado" width="120" align="right" dataType="currency"/>

		<adsm:gridColumn title="dataReembolso"      property="dtReembolsado"    width="120" align="center" dataType="JTDate" />
		
		<adsm:buttonBar>
			<adsm:button id="removeButton" caption="excluir" buttonType="removeButton" onclick="onRemoveButtonClick();"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>

	function dataLoad_cb(data, error) {
		onDataLoad_cb(data, error);
		onVlReembolsadoChange();
	}

	function initWindow(evento) {

		if (evento.name=='tab_click')	 {
			resetValue(document);
			setMoedaPadrao();
			document.getElementById('dtReembolsado').required='false';
			setDisabled('dtReembolsado', true);
		} else if (evento.name=='newButton_click') {
			setMoedaPadrao();
			document.getElementById('dtReembolsado').required='false';
			setDisabled('dtReembolsado', true);
		}		
	}
	
	function onVlReembolsadoChange() {	
		var e = document.getElementById('vlReembolsado');
		if (e.value != '') {
			document.getElementById('dtReembolsado').required='true';
			setDisabled('dtReembolsado', false);
		} else {
			document.getElementById('dtReembolsado').required='false';
			setDisabled('dtReembolsado', true);
		} 			
	}

	function onRemoveButtonClick() {
		custoAdicionalSinistroGridDef.removeByIds('lms.seguros.manterProcessosSinistroAction.removeCustosAdicionaisByIds', 'onRemoveCallback');
	}
	
	function onRemoveCallback_cb(data, error) {
		if (error!=undefined)
			alert(error);
		executeSearch();
		newButtonScript(document, true);
	}


	function onCustosAdicionaisShow() {
	
		var tabGroup = getTabGroup(document);

		setElementValue("idProcessoSinistro", tabGroup.getTab("cad").getFormProperty("idProcessoSinistro"));
		setElementValue("nrProcessoSinistro", tabGroup.getTab("cad").getFormProperty("nrProcessoSinistro"));		
		document.getElementById("idProcessoSinistro").masterLink="true";
		document.getElementById("nrProcessoSinistro").masterLink="true";
		
		executeSearch();
		
		if (tabGroup.getTab("cad").getFormProperty("situacaoHidden")=='F' ) {
			setDisabled('removeButton', true);
			setDisabled('storeButton', true);
		}
		
	}
	
	// executa a consulta da grid
	function executeSearch() {
		var data = new Array();
		data.idProcessoSinistro = getElementValue('idProcessoSinistro');
		custoAdicionalSinistroGridDef.executeSearch(data);
	}
	
	function storeCallback_cb(data, error) {
		store_cb(data, error);
		if (error==undefined) 
			executeSearch();
	}

   	var tabGroup = getTabGroup(this.document);
	var abaDetalhamento = tabGroup.getTab("cad");

	function setMoedaPadrao() {
		setElementValue("moeda.idMoeda", abaDetalhamento.getFormProperty("idMoedaHidden"));
	}
	


		
	
	
	
</script>