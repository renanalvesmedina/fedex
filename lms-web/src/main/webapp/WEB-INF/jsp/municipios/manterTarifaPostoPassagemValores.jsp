<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/municipios/manterTarifaPostoPassagem" idProperty="idValorTarifaPostoPassagem" service="lms.municipios.manterTarifaPostoPassagemAction.findByIdValorTarifaPostoPassagem" onDataLoadCallBack="desabilitaBotoes">
		<adsm:masterLink idProperty="idTarifaPostoPassagem" showSaveAll="true">
			<adsm:masterLinkItem label="tipo" property="tpPosto" itemWidth="50"/>
			<adsm:masterLinkItem label="localizacaoMunicipio" property="localizacao" itemWidth="50"/>
			<adsm:masterLinkItem label="rodovia" property="rodovia" itemWidth="50"/>
			<adsm:masterLinkItem label="sentidoCobranca" property="sentido" itemWidth="50" />
		</adsm:masterLink> 
		<adsm:hidden property="postoPassagem.idPostoPassagem"/>
		<adsm:hidden property="tarifaPostoPassagem.idtarifapostopassagem"/>
		 
		<adsm:hidden property="acaoVigenciaAtual"/> 
		
		<adsm:combobox autoLoad="false" property="tipoMeioTransporte.idTipoMeioTransporte" boxWidth="180" label="tipoMeioTransporte" service="lms.municipios.manterTarifaPostoPassagemAction.findComboTipoMeioTransporte" optionLabelProperty="dsTipoMeioTransporte"
				optionProperty="idTipoMeioTransporte"  labelWidth="17%" width="33%" required="true" onchange="tpMeioTranpChange(this);">
			<adsm:propertyMapping relatedProperty="tipoMeioTransporte.dsTipoMeioTransporte" modelProperty="dsTipoMeioTransporte"/>
			<adsm:propertyMapping relatedProperty="tipoMeioTransporte.tipoMeioTransporte.dsTipoMeioTransporte" modelProperty="tipoMeioTransporte.dsTipoMeioTransporte"/>
		</adsm:combobox>
  
		<adsm:combobox property="qtEixos" boxWidth="180" label="quantidadeEixos" service="lms.municipios.manterTarifaPostoPassagemAction.findComboEixosTipoMeioTransporte"
				optionLabelProperty="qtEixos"  optionProperty="qtEixos" labelWidth="17%" width="33%" required="true" onDataLoadCallBack="comboQtEixos">
			<adsm:propertyMapping criteriaProperty="tipoMeioTransporte.idTipoMeioTransporte" modelProperty="idTipoMeioTransporte"/>
		</adsm:combobox>

		
		<adsm:textbox property="tipoMeioTransporte.tipoMeioTransporte.dsTipoMeioTransporte" disabled="true" label="compostoPor" labelWidth="17%" width="33%" dataType="text"/>
		
		<adsm:hidden property="tipoMeioTransporte.dsTipoMeioTransporte"/>
		<adsm:combobox autoLoad="false" property="moedaPais.idMoedaPais" optionProperty="idMoedaPais"  optionLabelProperty="moeda.siglaSimbolo" service="lms.configuracoes.moedaPaisService.findMoedaByPais" label="moeda" width="33%" labelWidth="17%" required="true" onDataLoadCallBack="verificaMoedaPais"> 
			<adsm:propertyMapping relatedProperty="moedaPais.moeda.sgMoeda" modelProperty="moeda.sgMoeda"/>
			<adsm:propertyMapping relatedProperty="moedaPais.moeda.dsSimbolo" modelProperty="moeda.dsSimbolo"/>
		</adsm:combobox>
		<adsm:hidden property="moedaPais.moeda.sgMoeda"/>
		<adsm:hidden property="moedaPais.moeda.dsSimbolo"/>
		<adsm:textbox dataType="decimal" property="vlTarifa" mask="#,###,###,###,###,##0.00" label="valorFixo" labelWidth="17%" width="33%" required="true" minValue="0.01"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="incluirValor" id="storeButton" callbackProperty="storeCallBack" service="lms.municipios.manterTarifaPostoPassagemAction.saveValorTarifaPostoPassagem"/>
			<adsm:newButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:form>  
	<adsm:grid idProperty="idValorTarifaPostoPassagem" property="ValorTarifaPostoPassagem" detailFrameName="val"
				selectionMode="check" rows="10" unique="true" autoSearch="false" service="lms.municipios.manterTarifaPostoPassagemAction.findPaginatedValorTarifaPostoPassagem"
				rowCountService="lms.municipios.manterTarifaPostoPassagemAction.getRowCountValorTarifaPostoPassagem">
		<adsm:gridColumn title="tipoMeioTransporte" property="tipoMeioTransporte.dsTipoMeioTransporte" width=""/>
		<adsm:gridColumn title="eixos" property="qtEixos" width="50" dataType="integer"/>
		<%-- 
		<adsm:gridColumn title="compostoPor" property="tipoMeioTransporte.tipoMeioTransporte.dsTipoMeioTransporte" width="220"/>
		<adsm:gridColumn title="eixos" property="eixosTipoMeioTransporteComposto.qtEixos" width="50" dataType="integer"/>
		--%>
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="tarifa" property="moedaPais.moeda.sgMoeda" width="40" />		
			<adsm:gridColumn title="" property="moedaPais.moeda.dsSimbolo" width="35" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="" property="vlTarifa" width="60" align="right" dataType="currency"/>
		<adsm:buttonBar>
			<adsm:removeButton service="lms.municipios.manterTarifaPostoPassagemAction.removeByIdsValorTarifaPostoPassagem" caption="excluirValor"/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script type="text/javascript">
<!--
    function desabilitaBotoes_cb(data,exception){
    	onDataLoad_cb(data,exception);
    	verificaAcaoVigenciaAtual();
    }
    
    function comboQtEixos_cb(data) {
	    qtEixos_cb(data);
    	if (data != undefined && data.length == 1)
    		document.getElementById("qtEixos").selectedIndex = 1;
    }
    
	function findData() {
		var tabGroup = getTabGroup(this.document);
		var tabCad = tabGroup.getTab("cad");
		var remoteCall = {serviceDataObjects:new Array()};
		remoteCall.serviceDataObjects.push(createServiceDataObject("lms.municipios.manterTarifaPostoPassagemAction.findComboTipoMeioTransporte", "tipoMeioTransporte.idTipoMeioTransporte", { tpMeioTransporte:"R" }));
		remoteCall.serviceDataObjects.push(createServiceDataObject("lms.municipios.manterTarifaPostoPassagemAction.findComboMoedaPais", "verificaMoedaPais", { moedaPais:{pais:{idPais:tabCad.getFormProperty("pais.idPais") }}}));
		var acaoVigenciaAtual = tabCad.getFormProperty("acaoVigenciaAtual");
		setElementValue("acaoVigenciaAtual",acaoVigenciaAtual);
		xmit(remoteCall);
		setDisabled("__buttonBar:1.removeButton",true);
	}


function storeCallBack_cb(data,exception,key) {
	if (exception != undefined) {
		alert(exception);
		setFocusOnFirstFocusableField(document);
		return;
	}
	storeItem_cb(data,exception,key);
	findData();
}
function initWindow(eventObj) {
    if ((eventObj.name == "newButton_click") || (eventObj.name == "tab_click") ) {
		verificaAcaoVigenciaAtual();
	}
	if (eventObj.name == "newItemButton_click" || eventObj.name == "storeItemButton" || eventObj.name == "removeButton_grid") {
	    if (document.getElementById("moedaPais.idMoedaPais").data != undefined)
			setaPais(document.getElementById("moedaPais.idMoedaPais").data);
		verificaAcaoVigenciaAtual();
	}
	
	 
}
function tpMeioTranpChange(field) {
	if (field.selectedIndex == 0) {
		resetValue("tipoMeioTransporte.tipoMeioTransporte.dsTipoMeioTransporte");
	}
	comboboxChange({e:field});
}
function verificaAcaoVigenciaAtual() {
	var tabGroup = getTabGroup(this.document);
	var tabCad = tabGroup.getTab("cad");
	if (tabCad.getFormProperty("acaoVigenciaAtual") == "2") {
	    setDisabled(document,true);
		setDisabled("storeButton",true);
		setDisabled("removeButton",true);
	}else{
		setDisabled(document,false);
		setDisabled("tipoMeioTransporte.tipoMeioTransporte.dsTipoMeioTransporte",true);
		setDisabled("storeButton",false);
		if (getElementValue("tipoMeioTransporte.idTipoMeioTransporte") != "")
			setDisabled("removeButton",false);
	}	
	setDisabled("__buttonBar:1.removeButton",true);
}
 
function verificaMoedaPais_cb(dados) {
   	moedaPais_idMoedaPais_cb(dados);
	moedas = dados;
	if(dados) {
		setaPais(dados);
	}
}

	function setaPais(dados) {
	  for(var i = 0; i < dados.length; i++) {
			var indUtil = getNestedBeanPropertyValue(dados[i], "blIndicadorMaisUtilizada");
			if(indUtil == true || indUtil == 'true'){
				moedaDefault = getNestedBeanPropertyValue(dados[i], "idMoedaPais");
				setElementValue("moedaPais.idMoedaPais", moedaDefault);
				setElementValue("moedaPais.moeda.dsSimbolo",getNestedBeanPropertyValue(dados[i], "moeda.dsSimbolo"));
				setElementValue("moedaPais.moeda.sgMoeda",getNestedBeanPropertyValue(dados[i], "moeda.sgMoeda"));
				return;
			}
		}
	}
//-->
</script>
