<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.manterRotasViagemAction" >
	<adsm:form action="/municipios/manterTrechosRotaViagem" idProperty="idTrechoRotaIdaVolta" 
			service="lms.municipios.manterRotasViagemAction.findByIdTrechoRotaEvent" onDataLoadCallBack="trechoLoad" >
		<adsm:masterLink idProperty="idRotaViagem" showSaveAll="true">
			<adsm:masterLinkItem label="rota" property="rotaEvent.dsRota" itemWidth="100"/>
			<adsm:hidden property="idRotaIdaVolta" />
			<adsm:hidden property="tipoRotaIdaVolta" value="E" />
		</adsm:masterLink>
		<adsm:hidden property="dsRotaToLink" />
		<adsm:hidden property="dsTrechoRotaIdaVolta" />
		
        <adsm:hidden property="versao" />
        
        <adsm:combobox property="filialRotaByIdFilialRotaOrigem.filial.idFilial" onchange="filiaisChange('origemSelecionado');"
        		service="" optionLabelProperty="idFilial" optionProperty="sgFilial" autoLoad="false"
        		label="filialOrigem" labelWidth="17%" width="33%" boxWidth="200" required="true" />
        <adsm:hidden property="origemSelecionado" value="0" />
        <adsm:hidden property="filialRotaByIdFilialRotaOrigem.nrOrdem" />
        <adsm:hidden property="filialRotaByIdFilialRotaOrigem.filial.sgFilial" />
        <adsm:hidden property="filialRotaByIdFilialRotaOrigem.filial.pessoa.nmFantasia" />
        
        <adsm:combobox property="filialRotaByIdFilialRotaDestino.filial.idFilial" autoLoad="false"
        		service="" optionLabelProperty="idFilial" optionProperty="sgFilial" onchange="filiaisChange('destinoSelecionado');"
        		label="filialDestino" labelWidth="17%" width="33%" boxWidth="200" required="true" />
		<adsm:hidden property="destinoSelecionado" value="0" />
		<adsm:hidden property="filialRotaByIdFilialRotaDestino.nrOrdem" />
		<adsm:hidden property="filialRotaByIdFilialRotaDestino.filial.sgFilial" />
        <adsm:hidden property="filialRotaByIdFilialRotaDestino.filial.pessoa.nmFantasia" />
        
		<adsm:textbox dataType="integer" property="nrTempoViagemView"
				label="tempoViagem" size="6" maxLength="3" required="true" labelWidth="17%" unit="h" width="83%"/>
        
        <adsm:hidden property="blDomingo" value="false" />
        <adsm:hidden property="blSegunda" value="false" />
        <adsm:hidden property="blTerca" value="false" />
        <adsm:hidden property="blQuarta" value="false" />
        <adsm:hidden property="blQuinta" value="false" />
        <adsm:hidden property="blSexta" value="false" />
        <adsm:hidden property="blSabado" value="false" />
        <adsm:hidden property="blDomingo" value="false" />
        
        <adsm:buttonBar freeLayout="true" >
        	<adsm:button id="pptButton" caption="pontosParadaTrecho" action="/municipios/manterPontosParadaTrecho" cmd="main" >
				<adsm:linkProperty src="idTrechoRotaIdaVolta" target="trechoRotaIdaVolta.idTrechoRotaIdaVolta" disabled="true" />
				<adsm:linkProperty src="versao" target="trechoRotaIdaVolta.versao" disabled="true" />
				<adsm:linkProperty src="dsRotaToLink" target="rota.dsRota" disabled="true" />
				<adsm:linkProperty src="dsTrechoRotaIdaVolta" target="trechoRotaIdaVolta.dsTrechoRotaIdaVolta" disabled="true" />
			</adsm:button>
			<adsm:storeButton id="storeButtonRota" caption="incluirTrecho" callbackProperty="afterStore"
					service="lms.municipios.manterRotasViagemAction.saveTrechoRotaEvent" />
			<adsm:newButton id="newButtonRota" />
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid idProperty="idTrechoRotaIdaVolta" property="trechoRotaIdaVolta" detailFrameName="rota"
				rows="10" unique="true" autoSearch="false" gridHeight="180" 
				service="lms.municipios.manterRotasViagemAction.findPaginatedTrechoRotaEvent"
				rowCountService="lms.municipios.manterRotasViagemAction.getRowCountTrechoRotaEvent" >
		<adsm:gridColumnGroup separatorType="FILIAL" >
			<adsm:gridColumn title="filialOrigem" property="filialRotaByIdFilialRotaOrigem.filial.sgFilial" width="90" />
			<adsm:gridColumn title="" property="filialRotaByIdFilialRotaOrigem.filial.pessoa.nmFantasia" width="90" />
		</adsm:gridColumnGroup>
		
		<adsm:gridColumnGroup separatorType="FILIAL" >
			<adsm:gridColumn title="filialDestino" property="filialRotaByIdFilialRotaDestino.filial.sgFilial" width="90" />
			<adsm:gridColumn title="" property="filialRotaByIdFilialRotaDestino.filial.pessoa.nmFantasia" width="90" />
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn title="tempoViagem" property="nrTempoViagemView" unit="h" dataType="integer" />
		
		<adsm:buttonBar>
			<adsm:removeButton id="removeButtonRota" caption="excluirTrecho" 
					service="lms.municipios.manterRotasViagemAction.removeByIdsTrechoRotaEvent" /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>
	
	document.getElementById("dsRotaToLink").masterLink = true;
	
	function trechoLoad_cb(data,error) {
		onDataLoad_cb(data,error);
				
		verificaAcaoVigenciaAtual();
		
		manipulaPPTButton(data);
				
		var dsTrecho = getNestedBeanPropertyValue(data,"filialRotaByIdFilialRotaOrigem.filial.sgFilial") +
				" - " + getNestedBeanPropertyValue(data,"filialRotaByIdFilialRotaDestino.filial.sgFilial");
		setElementValue("dsTrechoRotaIdaVolta",dsTrecho);
	}
	
	function initWindow(eventObj) {
		if ((eventObj.name == "newButton_click") || (eventObj.name == "tab_click") ) {
			verificaAcaoVigenciaAtual();
			setDisabled("pptButton",true);
		}		
	}
	
	function verificaAcaoVigenciaAtual(){
		var tabGroup = getTabGroup(this.document);
		var tabCad = tabGroup.getTab("cad");
		
		if (tabCad.getFormProperty("acaoVigenciaAtual") == "2") {
		    setDisabled(document,true);
		} else if(tabCad.getFormProperty("acaoVigenciaAtual") == "1") {
			setDisabled("filialRotaByIdFilialRotaOrigem.filial.idFilial",true);
			setDisabled("filialRotaByIdFilialRotaDestino.filial.idFilial",true);
		} else {
			setDisabled(document,false);
		}
	}

	function tabShowRotas() {
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("cad");
		var list = tabDet.tabOwnerFrame.document.getElementById("rotaEvent.filiaisRota");
		
		newButtonScript(this.document, true, {name:'newItemButton_click'});
		
		copiaCombo(list,document.getElementById("filialRotaByIdFilialRotaOrigem.filial.idFilial"));
		copiaCombo(list,document.getElementById("filialRotaByIdFilialRotaDestino.filial.idFilial"));
		
		setElementValue("idRotaIdaVolta",tabDet.tabOwnerFrame.document.getElementById("rotaEvent.idRotaIdaVolta"));
		setElementValue("dsRotaToLink",tabDet.tabOwnerFrame.document.getElementById("rotaEvent.dsRota"));
		
		verificaAcaoVigenciaAtual();
		setDisabled("pptButton",true);
	}
	
	/*
	 * Copia dados da listBox rota Ida para Volta após questionar cópia.
	 */
	function copiaCombo(elemSrc,elemTrg) {
		while (elemTrg.length > 1) {
			elemTrg.options[elemTrg.length-1] = null;
		}
		elemTrg.data = new Array();
				
		var i = 0;
		for (i = 0 ; i < elemSrc.length ; i++) {
			elemTrg.options[i+1] = new Option(
								elemSrc.options[i].text,
								elemSrc.options[i].data.filial.idFilial);
			elemTrg.data[i+1] = elemSrc.options[i].data;
		}
	}
	
	function filiaisChange(filialHidden) {
		var combo;
		var elementOrdem;
		var elementSigla;
		var elementNome;
		if (filialHidden == 'origemSelecionado') {
			combo = document.getElementById("filialRotaByIdFilialRotaOrigem.filial.idFilial");
			elementOrdem = document.getElementById("filialRotaByIdFilialRotaOrigem.nrOrdem");
			elementSigla = document.getElementById("filialRotaByIdFilialRotaOrigem.filial.sgFilial");
			elementNome = document.getElementById("filialRotaByIdFilialRotaOrigem.filial.pessoa.nmFanstasia");
		} else {
			combo = document.getElementById("filialRotaByIdFilialRotaDestino.filial.idFilial");
			elementOrdem = document.getElementById("filialRotaByIdFilialRotaDestino.nrOrdem");
			elementSigla = document.getElementById("filialRotaByIdFilialRotaDestino.filial.sgFilial");
			elementNome = document.getElementById("filialRotaByIdFilialRotaDestino.filial.pessoa.nmFantasia");			
		}
		
		if (combo.selectedIndex != 0) {
			setElementValue(filialHidden,combo.selectedIndex);
			setElementValue(elementOrdem,combo.selectedIndex);
			setElementValue(elementSigla,combo.data[combo.selectedIndex].filial.sgFilial);
			setElementValue(elementNome,combo.data[combo.selectedIndex].nmFilial);
		}
	}
	
	function afterStore_cb(data,error,key) {	
		var retorno = store_cb(data,error,key);
		if (error == undefined)
			manipulaPPTButton(data);
		return retorno;
	}
	
	function manipulaPPTButton(data) {
		var tabGroup = getTabGroup(this.document);
		var tabCad = tabGroup.getTab("cad");
		
		var nrOrdemOrigem = getNestedBeanPropertyValue(data,"filialRotaByIdFilialRotaOrigem.nrOrdem");
		var nrOrdemDestino = getNestedBeanPropertyValue(data,"filialRotaByIdFilialRotaDestino.nrOrdem");
		// Ao salvar, data é undefined.
		if (nrOrdemOrigem == undefined || nrOrdemDestino == undefined) {
			nrOrdemOrigem = getElementValue("filialRotaByIdFilialRotaOrigem.nrOrdem");
			nrOrdemDestino = getElementValue("filialRotaByIdFilialRotaDestino.nrOrdem");
		}
		if (nrOrdemOrigem != undefined && nrOrdemDestino != undefined) {
			var hasIdRotaViagem = tabCad.getFormProperty("idRotaViagem") != "";
			var hasOrdem = (nrOrdemDestino - nrOrdemOrigem == 1) ||
					(nrOrdemOrigem == 1 && nrOrdemDestino == document.getElementById("filialRotaByIdFilialRotaDestino.filial.idFilial").length -1);
			
			setDisabled("pptButton",(!hasIdRotaViagem || !hasOrdem));
		}
	}
	
</script>