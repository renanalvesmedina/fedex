<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.manterRotasViagemAction" >
	<adsm:i18nLabels>
		<adsm:include key="LMS-29193"/>
	</adsm:i18nLabels>
	<adsm:form
		action="/municipios/manterTrechosRotaViagem"
		idProperty="idTrechoRotaIdaVolta"
		service="lms.municipios.manterRotasViagemAction.findByIdTrechoRotaIda"
		onDataLoadCallBack="trechoLoad"
	>
		<adsm:masterLink idProperty="idRotaViagem" showSaveAll="true">
			<adsm:masterLinkItem label="rota" property="rotaIda.dsRota" itemWidth="100"/>
			<adsm:hidden property="idRotaIdaVolta"/>
			<adsm:hidden property="tipoRotaIdaVolta" value="I"/>
		</adsm:masterLink>
		<adsm:hidden property="dsRotaToLink"/>
		<adsm:hidden property="nrRotaToLink"/>
		<adsm:hidden property="dsTrechoRotaIdaVolta"/>
		<adsm:hidden property="versao"/>
		<adsm:hidden property="rotaIdaVolta.nrDistancia"/>

		<adsm:combobox
			label="filialOrigem"
			property="filialRotaByIdFilialRotaOrigem.filial.idFilial"
			optionProperty="sgFilial"
			optionLabelProperty="idFilial"
			service=""
			onchange="filiaisChange('origemSelecionado');"
			autoLoad="false"
			labelWidth="17%"
			width="33%"
			boxWidth="200"
			required="true"
		/>
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

		<adsm:textbox
			dataType="JTTime"
			property="hrSaida"
			label="horarioSaida"
			size="6"
			labelWidth="17%"
			width="33%"
		/>

		<adsm:textbox dataType="integer" property="nrDistancia" required="true"
				label="distancia" maxLength="6" size="6" labelWidth="17%" unit="km2" width="33%"/>		

		<adsm:textbox
			label="tempoViagem"
			property="nrTempoViagem"
			dataType="JTTime"
			mask="hhh:mm"
			size="6"
			maxLength="6"
			labelWidth="17%"
			unit="h"
			width="33%"
			required="true"
		/>
		<adsm:textbox property="nrTempoOperacao"
				label="tempoOperacao" dataType="JTTime" mask="hhh:mm" size="6" required="true" maxLength="6" labelWidth="17%" unit="h" width="33%"/>

		<adsm:multicheckbox property="blDomingo|blSegunda|blTerca|blQuarta|blQuinta|blSexta|blSabado|"
				label="frequencia" texts="dom|seg|ter|qua|qui|sex|sab|" align="top" labelWidth="17%" width="33%" />
			
		<adsm:textbox property="vlRateio" labelStyle="vertical-align:top" mask="###,###,###,###,##0.00" onchange="onChangeVlRateio()"
				label="valorRateio" dataType="currency" size="18" required="true" labelWidth="17%" width="33%" style="text-align:right;"/>

		<adsm:buttonBar freeLayout="true" >
			<adsm:button id="pptButton" caption="pontosParadaTrecho" action="/municipios/manterPontosParadaTrecho" cmd="main" >
				<adsm:linkProperty src="idTrechoRotaIdaVolta" target="trechoRotaIdaVolta.idTrechoRotaIdaVolta" disabled="true" />
				<adsm:linkProperty src="versao" target="trechoRotaIdaVolta.versao" disabled="true" />
				<adsm:linkProperty src="dsRotaToLink" target="rota.dsRota" disabled="true" />
				<adsm:linkProperty src="nrRotaToLink" target="rota.nrRota" disabled="true" />
				<adsm:linkProperty src="dsTrechoRotaIdaVolta" target="trechoRotaIdaVolta.dsTrechoRotaIdaVolta" disabled="true" />
			</adsm:button>
			<adsm:storeButton caption="incluirTrecho" callbackProperty="afterStore"
					service="lms.municipios.manterRotasViagemAction.saveTrechoRotaIda" />
			<adsm:newButton />
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid
		idProperty="idTrechoRotaIdaVolta"
		property="trechoRotaIdaVolta"
		detailFrameName="ida"
		rows="6"
		unique="true"
		autoSearch="false"
		scrollBars="horizontal"
		gridHeight="120"
		service="lms.municipios.manterRotasViagemAction.findPaginatedTrechoRotaIda"
		rowCountService="lms.municipios.manterRotasViagemAction.getRowCountTrechoRotaIda"
		onDataLoadCallBack="updateVlFreteCarreteiro"
	>
		<adsm:gridColumnGroup separatorType="FILIAL" >
			<adsm:gridColumn title="filialOrigem" property="filialRotaByIdFilialRotaOrigem.filial.sgFilial" width="90" />
			<adsm:gridColumn title="" property="filialRotaByIdFilialRotaOrigem.filial.pessoa.nmFantasia" width="90" />
		</adsm:gridColumnGroup>

		<adsm:gridColumnGroup separatorType="FILIAL" >
			<adsm:gridColumn title="filialDestino" property="filialRotaByIdFilialRotaDestino.filial.sgFilial" width="90" />
			<adsm:gridColumn title="" property="filialRotaByIdFilialRotaDestino.filial.pessoa.nmFantasia" width="90" />
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn title="horarioSaida" property="hrSaida" width="80" dataType="JTTime" />		
		<adsm:gridColumn title="distancia" property="nrDistancia" unit="km2" dataType="integer" width="70" />
		<adsm:gridColumn title="tempoViagem" property="nrTempoViagem" dataType="text" width="90" align="center"/>
		<adsm:gridColumn title="tempoOperacao" property="nrTempoOperacao" dataType="text" width="90" align="center"/>
		<adsm:gridColumn title="dom" property="blDomingo" width="35" renderMode="image-check" />
		<adsm:gridColumn title="seg" property="blSegunda" width="35" renderMode="image-check" />
		<adsm:gridColumn title="ter" property="blTerca" width="35" renderMode="image-check" />
		<adsm:gridColumn title="qua" property="blQuarta" width="35" renderMode="image-check" />
		<adsm:gridColumn title="qui" property="blQuinta" width="35" renderMode="image-check" />
		<adsm:gridColumn title="sex" property="blSexta" width="35" renderMode="image-check" />
		<adsm:gridColumn title="sab" property="blSabado" width="35" renderMode="image-check" />
		<adsm:gridColumn title="valorRateio" property="vlRateio" dataType="currency" align="right" width="90"/>
	<adsm:buttonBar>
		<adsm:removeButton caption="excluirTrecho" service="lms.municipios.manterRotasViagemAction.removeByIdsTrechoRotaIda" /> 
	</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script language="javascript" type="text/javascript">
	document.getElementById("rotaIdaVolta.nrDistancia").masterLink = true;
	document.getElementById("dsRotaToLink").masterLink = true;
	document.getElementById("nrRotaToLink").masterLink = true;

	function trechoLoad_cb(data,error) {
		onDataLoad_cb(data,error);

		verificaAcaoVigenciaAtual();

		manipulaPPTButton();

		var dsTrecho = getNestedBeanPropertyValue(data,"filialRotaByIdFilialRotaOrigem.filial.sgFilial") +
				" - " + getNestedBeanPropertyValue(data,"filialRotaByIdFilialRotaDestino.filial.sgFilial");
		setElementValue("dsTrechoRotaIdaVolta",dsTrecho);

		var comboOrigem = document.getElementById("filialRotaByIdFilialRotaOrigem.filial.idFilial");
		comboOrigem.selectedIndex = getElementValue("filialRotaByIdFilialRotaOrigem.nrOrdem");
		var comboDestino = document.getElementById("filialRotaByIdFilialRotaDestino.filial.idFilial");
		comboDestino.selectedIndex = getElementValue("filialRotaByIdFilialRotaDestino.nrOrdem");
	}

	function initWindow(eventObj) {
		if ((eventObj.name == "newButton_click") || (eventObj.name == "tab_click") ) {
			var tabGroup = getTabGroup(this.document);
			var tabCad = tabGroup.getTab("cad");

			verificaAcaoVigenciaAtual();
			setDisabled("pptButton", true);

			var tpRotaViagem = tabCad.getFormProperty("tpRota");
			if("EC" == tpRotaViagem) {
				getElement("hrSaida").required = "false";
			} else {
				getElement("hrSaida").required = "true";
			}
		} else if (eventObj.name == "storeButton") {
			newButtonScript(document, true, {name:'newItemButton_click'});
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
			setDisabled("hrSaida",true);
			setDisabled("blDomingo",true);
			setDisabled("blSegunda",true);
			setDisabled("blTerca",true);
			setDisabled("blQuarta",true);
			setDisabled("blQuinta",true);
			setDisabled("blSexta",true);
			setDisabled("blSabado",true);
			setDisabled("nrTempoViagem",true);
			setDisabled("nrTempoOperacao",true);
			setDisabled("nrDistancia",true);
			setDisabled("vlRateio",true);
		} else {
			setDisabled(document,false);
			manipulaNrDistancia();
		}
	}

	function tabShowRotas() {
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("cad");
		var list = tabDet.tabOwnerFrame.document.getElementById("rotaIda.filiaisRota");		

		newButtonScript(this.document, true, {name:'newItemButton_click'});

		copiaCombo(list,document.getElementById("filialRotaByIdFilialRotaOrigem.filial.idFilial"));
		copiaCombo(list,document.getElementById("filialRotaByIdFilialRotaDestino.filial.idFilial"));

		setElementValue("rotaIdaVolta.nrDistancia",tabDet.tabOwnerFrame.document.getElementById("rotaIda.nrDistancia"));
		setElementValue("idRotaIdaVolta",tabDet.tabOwnerFrame.document.getElementById("rotaIda.idRotaIdaVolta"));
		setElementValue("dsRotaToLink",tabDet.tabOwnerFrame.document.getElementById("rotaIda.dsRota"));
		setElementValue("nrRotaToLink",tabDet.tabOwnerFrame.document.getElementById("rotaIda.nrRota"));
	}

	/*
	 * Copia dados da listBox rota Ida para combo.
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

	function findDistanciaByFiliais() {
		var data = new Array();
		data.idFilialOrigem = getElementValue("filialRotaByIdFilialRotaOrigem.filial.idFilial");
		data.idFilialDestino = getElementValue("filialRotaByIdFilialRotaDestino.filial.idFilial");
		var sdo = createServiceDataObject("lms.municipios.manterRotasViagemAction.findDistanciaByFiliais",
					"findDistanciaByFiliais",data);
		xmit({serviceDataObjects:[sdo]});
	}

	function findDistanciaByFiliais_cb(data,error) {
		if (error != undefined) {
			alert(error);
			return false;
		}

		if (data != undefined) {
			setElementValue("nrDistancia",getNestedBeanPropertyValue(data,"nrDistancia"));
		} else {
			resetValue("nrDistancia");
		}
	}

	function filiaisChange(filialHidden) {
		var origem = document.getElementById("filialRotaByIdFilialRotaOrigem.filial.idFilial");
		var destino = document.getElementById("filialRotaByIdFilialRotaDestino.filial.idFilial");
		
		if (origem.selectedIndex == 1 && destino.selectedIndex == destino.length-1) {
			var tabGroup = getTabGroup(this.document);
			var tabDet = tabGroup.getTab("cad");
			var distancia = tabDet.tabOwnerFrame.document.getElementById("rotaIda.nrDistancia");
			setElementValue("nrDistancia",distancia);
		} else if (origem.selectedIndex != 0 && destino.selectedIndex != 0 &&
				origem.selectedIndex < destino.selectedIndex) {
			findDistanciaByFiliais();
		} else {
			resetValue("nrDistancia");
		}
		manipulaNrDistancia();

		var combo;
		var elementOrdem;
		var elementSigla;
		var elementNome;

		if (filialHidden == 'origemSelecionado') {
			combo = origem;
			elementOrdem = document.getElementById("filialRotaByIdFilialRotaOrigem.nrOrdem");
			elementSigla = document.getElementById("filialRotaByIdFilialRotaOrigem.filial.sgFilial");
			elementNome = document.getElementById("filialRotaByIdFilialRotaOrigem.filial.pessoa.nmFantasia");
		} else {
			combo = destino;
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
		if (key != "LMS-29061") {
			var retorno = store_cb(data,error,key);
			if (error == undefined)
				manipulaPPTButton();
			return retorno;
		}
		
		var resultConfirm = confirm(error);
		if (resultConfirm)
			storeButtonScript('lms.municipios.manterRotasViagemAction.saveTrechoAfterValidationConfirmedIda',
					'storeItem', document.getElementById('form_idTrechoRotaIdaVolta'));
		else if (!resultConfirm)
			storeButtonScript('lms.municipios.manterRotasViagemAction.saveTrechoAfterValidationRefusedIda',
					'storeItem', document.getElementById('form_idTrechoRotaIdaVolta'));
	}

	function manipulaNrDistancia() {
		var origem = document.getElementById("filialRotaByIdFilialRotaOrigem.filial.idFilial");
		var destino = document.getElementById("filialRotaByIdFilialRotaDestino.filial.idFilial");
		
		setDisabled("nrDistancia", origem.selectedIndex == 1 && destino.selectedIndex == destino.length-1);
	}

	function manipulaPPTButton() {
		var tabGroup = getTabGroup(this.document);
		var tabCad = tabGroup.getTab("cad");

		nrOrdemOrigem = getElementValue("filialRotaByIdFilialRotaOrigem.nrOrdem");
		nrOrdemDestino = getElementValue("filialRotaByIdFilialRotaDestino.nrOrdem");
		if (nrOrdemOrigem != undefined && nrOrdemDestino != undefined) {
			var hasIdRotaViagem = !isBlank(tabCad.getFormProperty("idRotaViagem"));
			var hasIdTrechoRotaIdaVolta = getElementValue("idTrechoRotaIdaVolta") > 0;
			var hasOrdem = (nrOrdemDestino - nrOrdemOrigem == 1) ||
					(nrOrdemOrigem == 1 && nrOrdemDestino == document.getElementById("filialRotaByIdFilialRotaDestino.filial.idFilial").length -1);
			setDisabled("pptButton", !(hasIdRotaViagem && hasIdTrechoRotaIdaVolta && hasOrdem));
		}
	}
	
	function onChangeVlRateio() {
		var vlRateio = getElementValue("vlRateio");
		var data = new Array();
		setNestedBeanPropertyValue(data,"vlRateio",vlRateio);

		var sdo = createServiceDataObject("lms.municipios.manterRotasViagemAction.validaValorMaximoRateio",
				"validaValorMaximoRateio",data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function validaValorMaximoRateio_cb(data,error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		if (data != undefined) {
			var blValorRateioValido = getNestedBeanPropertyValue(data,"blValorRateioValido");
			var valorMaxVr = getNestedBeanPropertyValue(data,"valorMaxVr");
			if(blValorRateioValido === 'false'){
				alertI18nMessage("LMS-29193", valorMaxVr, false);
				resetValue("vlRateio");
			}
		}
	}
	
	function updateVlFreteCarreteiro_cb(data,error,key) {
		atualizaVlFreteCarreteiro();
	}
	
	function atualizaVlFreteCarreteiro() {
		var data = new Array();
		var sdo = createServiceDataObject("lms.municipios.manterRotasViagemAction.calculaTotalRateioIda","atualizaVlFreteCarreteiro",data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function atualizaVlFreteCarreteiro_cb(data,error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		if (data != undefined) {
			var tabGroup = getTabGroup(this.document);
			var tabCad = tabGroup.getTab("cad");
			var telaCad = tabCad.tabOwnerFrame;	
			
			var vlFreteCarreteiro = getNestedBeanPropertyValue(data,"vlTotalRateio");
			if (vlFreteCarreteiro != undefined && vlFreteCarreteiro != ""){
				tabCad.getElementById("rotaIda.vlFreteCarreteiro").value = vlFreteCarreteiro;
			} else {
				tabCad.getElementById("rotaIda.vlFreteCarreteiro").value = "";
			}
			telaCad.calcFreteKm("Ida");
		}
	}
	
</script>