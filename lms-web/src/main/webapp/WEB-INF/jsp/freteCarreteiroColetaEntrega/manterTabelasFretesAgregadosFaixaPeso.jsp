<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.fretecarreteirocoletaentrega.manterTabelasFretesAgregadosAction" >

	<adsm:form idProperty="idFaixaPesoParcelaTabelaCE"  
				action="/freteCarreteiroColetaEntrega/manterTabelasFretesAgregados"  
				service="lms.fretecarreteirocoletaentrega.manterTabelasFretesAgregadosAction.findFaixaPeso" 
				onDataLoadCallBack="faixaPesoLoad" >
				
		<adsm:masterLink idProperty="idTabelaColetaEntrega" showSaveAll="true">
		</adsm:masterLink>

		<adsm:textbox 
					property="psInicial" 
					label="faixaInicial"
					dataType="weight" 
					unit="kg" 
					size="15" 
					maxLength="18"
					labelWidth="18%" 
					width="82%"
					required="true"/>
					
		<adsm:textbox 
					property="psFinal" 
					label="faixaFinal"
					dataType="weight" 
					unit="kg" 
					size="15" 
					maxLength="18"
					labelWidth="18%" 
					width="82%"
					required="true"/>

		<adsm:textbox 
					property="vlValor"
					label="valor" 
					dataType="currency" 
					labelWidth="18%" 
					width="82%" 
					size="21"
					maxLength="18"
					required="true"/>
					
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvarItem" service="lms.fretecarreteirocoletaentrega.manterTabelasFretesAgregadosAction.storeFaixaPeso" callbackProperty="callbackProperty" />
			<adsm:button caption="limpar" id="newButton" onclick="limpar();" />
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid property="faixaPesoParcelaTabelaCE" idProperty="idFaixaPesoParcelaTabelaCE" selectionMode="check" gridHeight="82" 
			   unique="true" rows="8" detailFrameName="faixaPeso"
			   service="lms.fretecarreteirocoletaentrega.manterTabelasFretesAgregadosAction.findPaginatedFaixaPeso" autoSearch="false"
			   rowCountService="lms.fretecarreteirocoletaentrega.manterTabelasFretesAgregadosAction.getRowCountFaixaPeso"
			   onDataLoadCallBack="gridFaixaPesoSearch" >
			   <adsm:gridColumn property="psInicial"	dataType="weight"	title="faixaInicial" width="25%" />
			   <adsm:gridColumn property="psFinal"		dataType="weight"	title="faixaFinal"	width="25%" />
			   <adsm:gridColumn property="vlValor"		dataType="currency" title="valor"		width="25%" />
			   <adsm:gridColumn property="tpFator"		dataType="text"		title="fatorCalculo" width="25%" />

		<adsm:buttonBar>
			<adsm:button caption="excluirItem" buttonType="removeButton" onclick="removeId();"/>
		</adsm:buttonBar>
	</adsm:grid>	
	
</adsm:window>



<script>
var inicial;
var psFinal;
var anterior;
function removeId(){
	faixaPesoParcelaTabelaCEGridDef.removeByIds("lms.fretecarreteirocoletaentrega.manterTabelasFretesAgregadosAction.removeByIdsFaixaPeso", "removeId");
}

function removeId_cb(data, error){
	removeById_cb(data);
	if(error != undefined) {
		setElementValue("psInicial",inicial);
		alert(error);
	}else{
		store_cb(data,error);
		limpar();
	}
}

function callbackProperty_cb(data, error) {
	if(error != undefined) {
		setElementValue("psInicial",inicial);
		alert(error);
	} else {
		store_cb(data,error);
		limpar();
	}
}

function loadPesoInicial() {
	var tabGroup = getTabGroup(this.document);
	var tabCad = tabGroup.getTab("cad");
	var telaCad = tabCad.tabOwnerFrame;

	var acaoVigenciaAtual = telaCad.getElementValue("acaoVigenciaAtual");
	var sdo = createServiceDataObject("lms.fretecarreteirocoletaentrega.manterTabelasFretesAgregadosAction.loadPesoInicial","loadPesoInicial",{idTabelaColetaEntrega: telaCad.getElementValue("idTabelaColetaEntrega")});
	xmit({serviceDataObjects:[sdo]});
}

function loadPesoInicial_cb(data, error) {
	setElementValue("psInicial",getNestedBeanPropertyValue(data,"psInicial"));
	getTabGroup(this.document).selectedTab.setChanged(false);
	getTabGroup(this.document).selectedTab.itemTabChanged = false;
}

function gridFaixaPesoSearch_cb(data, error){
	controlaPsInicial();
}


function storeItem_cb(data,error){
	if(error != undefined) {
		setElementValue("psInicial",inicial);
		alert(error);
		
	}
	else{
		inicial = data.inicial;
		psFinal = data.psFinal;
		
		store_cb(data,error);
		
		resetValue(this.document);
		
		setElementValue("psInicial",inicial);
		setElementValue("psFinal","");
		setElementValue("vlValor","");

		getTabGroup(this.document).selectedTab.setChanged(false);
		getTabGroup(this.document).selectedTab.itemTabChanged = false;

	}
}

function limpar(){
	resetValue(this.document);
	setDisabled("psFinal", false);
	setDisabled("vlValor", false);
	loadPesoInicial();
}

function faixaPesoLoad_cb(data,error){
	onDataLoad_cb(data,error);
	controlaPsInicial();
	
	if(psFinal != data.psFinal){
		setDisabled("psInicial", true);
		setDisabled("psFinal", true);
	}
}

function controlaPsInicial(isPrimeiraFaixa){
		
	var tabGroup = getTabGroup(this.document);
	var tabCad = tabGroup.getTab("cad");
	var telaCad = tabCad.tabOwnerFrame;

	var acaoVigenciaAtual = telaCad.getElementValue("acaoVigenciaAtual");
	if(acaoVigenciaAtual != ""){
		if (acaoVigenciaAtual == 0) {
			setDisabled(this.document,false);
		} else if (acaoVigenciaAtual == 1) {
			setDisabled(this.document,true);
		} else if (acaoVigenciaAtual == 2) {
			setDisabled(this.document,true);
		}	
	} else {
		setDisabled(this.document,false);
		// tenho que repetir o teste pois esta habilitou o form inteiro
		setDisabled("psInicial", true);
	}
	
	var size = faixaPesoParcelaTabelaCEGridDef.gridState.data.length;
		
	if(size > 0 && (acaoVigenciaAtual == "" || acaoVigenciaAtual == "0")){
		var i = faixaPesoParcelaTabelaCEGridDef.currentRowCount;
		
		var x = 0; 
		for(x; x < size; x++){
			setDisabled("faixaPesoParcelaTabelaCE:"+x+".idFaixaPesoParcelaTabelaCE",true);
		}
		i -= 1;
		
		setDisabled("faixaPesoParcelaTabelaCE:"+i+".idFaixaPesoParcelaTabelaCE",false);
	}

	loadPesoInicial();
}

</script>