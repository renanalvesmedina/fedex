<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.fretecarreteiroviagem.manterTabelasReferenciasFreteKmAction">
	<adsm:form action="entrega/fecharManifestoEntregas" idProperty="idReferenciaTipoVeiculo" service="lms.fretecarreteiroviagem.manterTabelasReferenciasFreteKmAction.findByIdReferenciaTipoVeiculo" onDataLoadCallBack="desabilitaCampos">
		<adsm:masterLink idProperty="idReferenciaFreteCarreteiro" showSaveAll="true">
			<adsm:masterLinkItem property="unidadeFederativaByIdUnidadeFederativaOrigem.nmUnidadeFederativa" label="ufOrigem2" itemWidth="50"/>
			<adsm:masterLinkItem property="unidadeFederativaByIdUnidadeDestino.nmUnidadeFederativa" label="ufDestino2" itemWidth="50"/>
			<adsm:masterLinkItem property="filialByIdFilialOrigem.pessoa.nmFantasia" label="filialOrigem" itemWidth="50"/>
			<adsm:masterLinkItem property="filialByIdFilialDestino.pessoa.nmFantasia" label="filialDestino" itemWidth="50"/>
			<adsm:hidden property="idFilialOrigem"/>
			<adsm:hidden property="idFilialDestino"/>
			<adsm:hidden property="idUfOrigem"/>
			<adsm:hidden property="idUfDestino"/>
			<adsm:hidden property="dtVigenciaInicial"/>
			<adsm:hidden property="dtVigenciaFinal"/>
		</adsm:masterLink>
		<adsm:hidden property="referenciaFreteCarreteiro.idReferenciaFreteCarreteiro"/>
		  
		
		<adsm:combobox autoLoad="false" property="tipoMeioTransporte.idTipoMeioTransporte"  boxWidth="180" label="tipoMeioTransporte" service="lms.fretecarreteiroviagem.manterTabelasReferenciasFreteKmAction.findAtivos" optionLabelProperty="dsTipoMeioTransporte"  optionProperty="idTipoMeioTransporte"  labelWidth="19%" width="31%" required="true" onlyActiveValues="true">
			<adsm:propertyMapping relatedProperty="tipoMeioTransporte.dsTipoMeioTransporte" modelProperty="dsTipoMeioTransporte"/>
		</adsm:combobox>
		
		<adsm:hidden property="tipoMeioTransporte.dsTipoMeioTransporte"/>
		 
		
		<adsm:range label="faixaKm" width="31%" labelWidth="19%">
             <adsm:textbox dataType="integer" property="qtKmInicial" size="6" maxLength="6"/>
             <adsm:textbox dataType="integer" property="qtKmFinal" size="6" maxLength="6"/>
        </adsm:range>
        
        <adsm:combobox autoLoad="false" property="moedaPais.idMoedaPais" optionProperty="idMoedaPais"  optionLabelProperty="moeda.siglaSimbolo" service="lms.fretecarreteiroviagem.manterTabelasReferenciasFreteKmAction.findMoedaByPais" label="moeda" width="31%" labelWidth="19%" required="true" disabled="true"> 
			<adsm:propertyMapping relatedProperty="moedaPais.moeda.siglaSimbolo" modelProperty="moeda.siglaSimbolo"/>
		</adsm:combobox>
		<adsm:hidden property="moedaPais.moeda.siglaSimbolo"/>
		
        
        <adsm:textbox dataType="decimal" property="vlFreteReferencia" mask="#,###,###,###,###,##0.000" minValue="0.01" size="17" maxLength="50" width="25%" label="valorPorKM" labelWidth="19%" required="true"/>
       
      
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvarValor" id="storeButton" callbackProperty="storeCallBack" service="lms.fretecarreteiroviagem.manterTabelasReferenciasFreteKmAction.saveReferenciaTipoVeiculo"/>
			<adsm:newButton id="removeButton" onclick="limpaCampos()"/>
		</adsm:buttonBar>
	</adsm:form>
		<adsm:grid detailFrameName="cheq" autoSearch="false" property="referenciaTipoVeiculo" idProperty="idReferenciaTipoVeiculo" unique="true" rows="9" service="lms.fretecarreteiroviagem.manterTabelasReferenciasFreteKmAction.findPaginatedReferenciaTipoVeiculo" rowCountService="lms.fretecarreteiroviagem.manterTabelasReferenciasFreteKmAction.getRowCountReferenciaTipoVeiculo">
			<adsm:gridColumn width="50%" title="tipoMeioTransporte" property="tipoMeioTransporte.dsTipoMeioTransporte"/>
			<adsm:gridColumn width="15%" title="kmInicial" property="qtKmInicial" dataType="integer"/>
			<adsm:gridColumn width="15%" title="kmFinal" property="qtKmFinal" dataType="integer"/>
			<adsm:gridColumn width="20%" title="valorPorKM" property="vlFreteReferencia" mask="#,###,###,###,###,##0.000" dataType="decimal"/>
		<adsm:buttonBar>
			<adsm:removeButton service="lms.fretecarreteiroviagem.manterTabelasReferenciasFreteKmAction.removeByIdsReferenciaTipoVeiculo" caption="excluirValor" /> 
		</adsm:buttonBar>
		</adsm:grid>
</adsm:window>

<script>

function buscaValoresPai(){
	
	var tabGroup = getTabGroup(this.document);
	var tabCad = tabGroup.getTab("cad");
	
	//seta os valores do pai no filho
	setElementValue("idUfOrigem",tabCad.getElementById("unidadeFederativaByIdUnidadeFederativaOrigem.idUnidadeFederativa"));
	setElementValue("idUfDestino",tabCad.getElementById("unidadeFederativaByIdUnidadeDestino.idUnidadeFederativa"));
	setElementValue("dtVigenciaInicial",tabCad.getElementById("dtVigenciaInicial"));
	setElementValue("dtVigenciaFinal",tabCad.getElementById("dtVigenciaFinal"));
	
	
}

//função que carrega a combo de tipo de meio de transporte e moeda do pais chamada no onShow do main.
function findData() {
		var remoteCall = {serviceDataObjects:new Array()};
		remoteCall.serviceDataObjects.push(createServiceDataObject("lms.contratacaoveiculos.tipoMeioTransporteService.findAtivos", "tipoMeioTransporte.idTipoMeioTransporte", { tpMeioTransporte:"R" }));
		remoteCall.serviceDataObjects.push(createServiceDataObject("lms.fretecarreteiroviagem.manterTabelasReferenciasFreteKmAction.findMoedaByPais", "verificaMoedaPais", new Array()));
		xmit(remoteCall);
		buscaValoresPai();
		limpaCampos();
		desabilitaKm();
}

//funcao chamada no callback do form
function desabilitaCampos_cb(data,exception){
	onDataLoad_cb(data,exception);
	setaMoedaPai();
	setDisabled("__buttonBar:1.removeButton",false);
	verificaAcaoVigenciaAtual();
}

function verificaAcaoVigenciaAtual(){
	var tabGroup = getTabGroup(this.document);
	var tabCad = tabGroup.getTab("cad");
	//alert(tabCad.getFormProperty("acaoVigenciaAtual"));
	if(tabCad.getFormProperty("acaoVigenciaAtual") == 2 || tabCad.getFormProperty("acaoVigenciaAtual") == 1){
		setDisabled(document,true);
		setDisabled("storeButton",true);
		setDisabled("removeButton",true);
		setDisabled("__buttonBar:1.removeButton",true);
	}else if(tabCad.getFormProperty("acaoVigenciaAtual") == 0 || tabCad.getFormProperty("acaoVigenciaAtual")== null){
		setDisabled(document,false);
		desabilitaKm();
	}
	setFocusOnFirstFocusableField(document); 
	
}

function initWindow(eventObj) {
	if (eventObj.name == "tab_click")  {
		verificaAcaoVigenciaAtual();
		setaMoedaPai();
	}
	if(eventObj.name == "removeButton_grid"){
		setDisabled("removeButton", false);
	}
	
	
}

//funcao chamada no call back do store
function storeCallBack_cb(data,exception,key) {
	if (exception != undefined) {
		alert(exception);
		setFocusOnFirstFocusableField(document);
		return;
	}
	storeItem_cb(data,exception,key);
	setDisabled("removeButton", false);
}



function desabilitaKm(){
	var tabGroup = getTabGroup(this.document);
	var tabCad = tabGroup.getTab("cad");

	setElementValue("idFilialDestino","");
	setElementValue("idFilialOrigem","");

	var filialByIdFilialOrigem = tabCad.getFormProperty("filialByIdFilialOrigem.idFilial");
	if(filialByIdFilialOrigem != ''){
		setElementValue("idFilialOrigem",filialByIdFilialOrigem);
		document.getElementById("idFilialOrigem").masterLink="true";
	}
	var filialByIdFilialDestino = 	tabCad.getFormProperty("filialByIdFilialDestino.idFilial");
	if(filialByIdFilialDestino != ''){
		setElementValue("idFilialDestino",filialByIdFilialDestino);
		document.getElementById("idFilialDestino").masterLink="true";
	}	
	
	if(getElementValue("idFilialOrigem")!= '' && getElementValue("idFilialDestino")!= ''){
			setDisabled("qtKmInicial", true);
			setDisabled("qtKmFinal", true);
			document.getElementById("qtKmInicial").required = "false";
			document.getElementById("qtKmFinal").required = "false";
			
	}else{
		setDisabled("qtKmInicial", false);
		setDisabled("qtKmFinal", false);
		document.getElementById("qtKmInicial").required = "true";
		document.getElementById("qtKmFinal").required = "true";
	}
}

function limpaCampos(){
	//setDisabled(document,false);
	setDisabled("tipoMeioTransporte.idTipoMeioTransporte", false);
	setDisabled("moedaPais.idMoedaPais", false);
	setDisabled("vlFreteReferencia", false);
	
	
	setFocus(document.getElementById("tipoMeioTransporte.idTipoMeioTransporte"));
}


//**********************************funcoes relativas a combo moeda pais ********************************
function verificaMoedaPais_cb(dados) {
   	moedaPais_idMoedaPais_cb(dados);
	moedas = dados;
	if(dados) {
		setaMoedaPai();
	}
}

function setaMoedaPai() {
	  var tabGroup = getTabGroup(this.document);
	  var tabCad = tabGroup.getTab("cad");	
	  setElementValue("moedaPais.idMoedaPais",tabCad.getElementById("moedaPais.idMoedaPais"));
	  document.getElementById("moedaPais.idMoedaPais").masterLink = "true";	
	  setDisabled("moedaPais.idMoedaPais", true);			
	  return;
			
}
	
	
</script>