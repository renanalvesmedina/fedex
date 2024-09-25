<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.fretecarreteiroviagem.manterTabelasReferenciasFreteKmAction">
	<adsm:form action="entrega/fecharManifestoEntregas" height="100" idProperty="idReferenciaFreteCarreteiro" onDataLoadCallBack="verifica" service="lms.fretecarreteiroviagem.manterTabelasReferenciasFreteKmAction.findByIdEValidaDtVigencia" newService="lms.fretecarreteiroviagem.manterTabelasReferenciasFreteKmAction.newMaster">
	<adsm:hidden property="acaoVigenciaAtual" serializable="false"/>
	<adsm:hidden property="tpSituacaoUf" value="A"/>
	<adsm:lookup 
		dataType="text" 
		property="unidadeFederativaByIdUnidadeFederativaOrigem" 
		idProperty="idUnidadeFederativa" 
		criteriaProperty="sgUnidadeFederativa" 
		service="lms.fretecarreteiroviagem.manterTabelasReferenciasFreteKmAction.findLookupUnidadeFederativa" 
		label="ufOrigem" width="35%" size="3" action="/municipios/manterUnidadesFederativas" maxLength="3">
		 	<adsm:propertyMapping relatedProperty="unidadeFederativaByIdUnidadeFederativaOrigem.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa"/>
		    <adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.idFilial" modelProperty=""/>
		    <adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.sgFilial" modelProperty=""/>
		    <adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty=""/>
		    <adsm:propertyMapping criteriaProperty="tpSituacaoUf" modelProperty="tpSituacao"/>
		 	
		 	<adsm:textbox dataType="text" property="unidadeFederativaByIdUnidadeFederativaOrigem.nmUnidadeFederativa" disabled="true" size="30"/>
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="unidadeFederativaByIdUnidadeDestino" idProperty="idUnidadeFederativa" criteriaProperty="sgUnidadeFederativa" service="lms.fretecarreteiroviagem.manterTabelasReferenciasFreteKmAction.findLookupUnidadeFederativa" label="ufDestino" width="35%" size="3" action="/municipios/manterUnidadesFederativas" maxLength="3">
			<adsm:propertyMapping relatedProperty="unidadeFederativaByIdUnidadeDestino.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa"/>
			<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.idFilial" modelProperty=""/>
		    <adsm:propertyMapping relatedProperty="filialByIdFilialDestino.sgFilial" modelProperty=""/>
		    <adsm:propertyMapping relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia" modelProperty=""/>
		    
		    <adsm:propertyMapping criteriaProperty="tpSituacaoUf" modelProperty="tpSituacao"/>
			
		 	<adsm:textbox dataType="text" property="unidadeFederativaByIdUnidadeDestino.nmUnidadeFederativa" disabled="true" size="30"/>
		</adsm:lookup>
		
		
		<adsm:lookup dataType="text" property="filialByIdFilialOrigem" idProperty="idFilial" criteriaProperty="sgFilial" service="lms.fretecarreteiroviagem.manterTabelasReferenciasFreteKmAction.findLookupFilial" label="filialOrigem" width="35%" size="3" exactMatch="false" minLengthForAutoPopUpSearch="3" action="/municipios/manterFiliais" maxLength="3">
			<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:propertyMapping relatedProperty="unidadeFederativaByIdUnidadeFederativaOrigem.nmUnidadeFederativa" modelProperty=""/>
			<adsm:propertyMapping relatedProperty="unidadeFederativaByIdUnidadeFederativaOrigem.idUnidadeFederativa" modelProperty=""/>
			<adsm:propertyMapping relatedProperty="unidadeFederativaByIdUnidadeFederativaOrigem.sgUnidadeFederativa" modelProperty=""/>
			<adsm:textbox dataType="text" property="filialByIdFilialOrigem.pessoa.nmFantasia" disabled="true" size="30"/>
		</adsm:lookup>
		
		<adsm:lookup 
			dataType="text" 
			property="filialByIdFilialDestino" 
			idProperty="idFilial" 
			criteriaProperty="sgFilial" 
			service="lms.fretecarreteiroviagem.manterTabelasReferenciasFreteKmAction.findLookupFilial" 
			label="filialDestino" width="35%" size="3" exactMatch="false" 
			minLengthForAutoPopUpSearch="3" action="/municipios/manterFiliais" maxLength="3">
			<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:propertyMapping relatedProperty="unidadeFederativaByIdUnidadeDestino.nmUnidadeFederativa" modelProperty=""/>
			<adsm:propertyMapping relatedProperty="unidadeFederativaByIdUnidadeDestino.idUnidadeFederativa" modelProperty=""/>
			<adsm:propertyMapping relatedProperty="unidadeFederativaByIdUnidadeDestino.sgUnidadeFederativa" modelProperty=""/>
			<adsm:textbox dataType="text" property="filialByIdFilialDestino.pessoa.nmFantasia" disabled="true" size="30"/>
		 	
		</adsm:lookup>
		
		<adsm:combobox 
		property="moedaPais.idMoedaPais" 
		optionLabelProperty="moeda.siglaSimbolo" 
		optionProperty="idMoedaPais" 
		label="moeda" service="lms.fretecarreteiroviagem.manterTabelasReferenciasFreteKmAction.findMoedaByPais" onDataLoadCallBack="verificaMoedaPais" required="true">
			<adsm:propertyMapping relatedProperty="moedaPais.moeda.siglaSimbolo" modelProperty="moeda.siglaSimbolo"/>
		</adsm:combobox>
		<adsm:hidden property="moedaPais.moeda.siglaSimbolo"/>
		<adsm:hidden property="moedaFoiSelecionada"/>
		
		
		
		<adsm:range label="vigencia" width="85%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"/>
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>
        
        
		<adsm:buttonBar>
			<adsm:storeButton caption="salvar" id="storeButton" service="lms.fretecarreteiroviagem.manterTabelasReferenciasFreteKmAction.store" callbackProperty="storeRef"/> 
			<adsm:newButton id="botaoLimpar"/>
			<adsm:button id="botaoExcluir" caption="excluir" buttonType="removeButton" onclick="removeButtonScript('lms.fretecarreteiroviagem.manterTabelasReferenciasFreteKmAction.removeById', 'desabilitaAbaValores', 'idReferenciaFreteCarreteiro', this.document)"/> 
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>  
<script>

//funcao chamada no callback do form
function verifica_cb(data,exception){
	
	onDataLoad_cb(data,exception);
	var tabGroup = getTabGroup(this.document);	
	tabGroup.setDisabledTab('cheq',false);
	
	var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
	setElementValue("acaoVigenciaAtual",acaoVigenciaAtual);
	verificaPadraoVigencia(null);
}
 
//verifica as vigencias
function verificaPadraoVigencia(tipoEvento){
	if(getElementValue("acaoVigenciaAtual") == 0){
		setDisabled("unidadeFederativaByIdUnidadeFederativaOrigem.idUnidadeFederativa", true);
		setDisabled("unidadeFederativaByIdUnidadeDestino.idUnidadeFederativa", true);
		setDisabled("filialByIdFilialOrigem.idFilial", true);
		setDisabled("filialByIdFilialDestino.idFilial", true);
		setDisabled("moedaPais.idMoedaPais", true);
		setDisabled("dtVigenciaInicial", false);
		setDisabled("dtVigenciaFinal", false);
		if(tipoEvento == "" ||  tipoEvento == null)
     		setFocusOnFirstFocusableField(document);
     	else
       		setFocusOnNewButton(document);
	}else if(getElementValue("acaoVigenciaAtual") == 1) {
      setDisabled(document,true);
	  setDisabled("botaoLimpar",false);
      setDisabled("storeButton",false);
      setDisabled("dtVigenciaFinal", false);
      if(tipoEvento == "" ||  tipoEvento == null)
     	setFocusOnFirstFocusableField(document);
     else
     	setFocusOnNewButton(document);
   }else if(getElementValue("acaoVigenciaAtual") == 2) {
  	 setDisabled(document,true);
     setDisabled("botaoLimpar",false);
     setFocusOnNewButton(document);
   }
     
} 
//*********initWindow****************************
function initWindow(eventObj){

	if(eventObj.name == 'gridRow_click'){
		if(getElementValue("idReferenciaFreteCarreteiro") != '' && getElementValue("idReferenciaFreteCarreteiro") != null)
		    verificaPadraoVigencia(null);
	}	
		
	if(getElementValue("moedaFoiSelecionada") == null || getElementValue("moedaFoiSelecionada") == '')
		populaComboMoedaPais();
	
	if(eventObj.name == 'newButton_click'){
		setDisabled("unidadeFederativaByIdUnidadeFederativaOrigem.idUnidadeFederativa", false);
		setDisabled("unidadeFederativaByIdUnidadeDestino.idUnidadeFederativa", false);
		setDisabled("filialByIdFilialOrigem.idFilial", false);
		setDisabled("filialByIdFilialDestino.idFilial", false);
		setDisabled("moedaPais.idMoedaPais", false);
		setDisabled("dtVigenciaInicial", false);
		setDisabled("dtVigenciaFinal", false);
		setFocus(document.getElementById("unidadeFederativaByIdUnidadeFederativaOrigem.sgUnidadeFederativa"));
		desabilitaAbaValores();
		
	}

	if(eventObj.name == 'tab_click'){
		var tabGroup = getTabGroup(this.document);	
		var tabCheq = tabGroup.getTab('cheq');	
		var numFilhos = tabCheq.tabOwnerFrame.referenciaTipoVeiculoGridDef.gridState.rowCount;
		if(numFilhos > 0){
			setDisabled("unidadeFederativaByIdUnidadeFederativaOrigem.idUnidadeFederativa", true);
			setDisabled("unidadeFederativaByIdUnidadeDestino.idUnidadeFederativa", true);
			setDisabled("filialByIdFilialOrigem.idFilial", true);
			setDisabled("filialByIdFilialDestino.idFilial", true);
			setDisabled("moedaPais.idMoedaPais", true);
		}else{
			setDisabled("unidadeFederativaByIdUnidadeFederativaOrigem.idUnidadeFederativa", false);
			setDisabled("unidadeFederativaByIdUnidadeDestino.idUnidadeFederativa", false);
			setDisabled("filialByIdFilialOrigem.idFilial", false);
			setDisabled("filialByIdFilialDestino.idFilial", false);
			setDisabled("moedaPais.idMoedaPais", false);
			setDisabled("dtVigenciaInicial", false);
			setDisabled("dtVigenciaFinal", false);
			setFocus(document.getElementById("unidadeFederativaByIdUnidadeFederativaOrigem.sgUnidadeFederativa"));
		}
		if(getElementValue("acaoVigenciaAtual")==1 || getElementValue("acaoVigenciaAtual")==2){	
			setDisabled("botaoExcluir",true);
		}
	}
}

function desabilitaAbaValores(){
	var tabGroup = getTabGroup(this.document);	
	tabGroup.setDisabledTab('cheq',true);
	
}

//funcao chamada no callback do store
function storeRef_cb(data,errorMsg, errorKey) {
		store_cb(data, errorMsg, errorKey);
		
		if(errorKey == undefined){
			var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
			setElementValue("acaoVigenciaAtual",acaoVigenciaAtual);
			var store = "true";
			verificaPadraoVigencia(store);
    	}else{
			if (errorKey == "LMS-24002") {
				setElementValue("moedaFoiSelecionada", "true");
				var tabGroup = getTabGroup(this.document);	
				tabGroup.setDisabledTab('cheq',false);
				tabGroup.selectTab('cheq',{name:'tab_click'});
			}
			
		}	
}	



function desabilitaAbaValores(){
	var tabGroup = getTabGroup(this.document);	
	tabGroup.setDisabledTab('cheq',true);
	
}

//funcao botao Excluir
function desabilitaAbaValores_cb(data,errorMsg)
{
	if(errorMsg!=null)
	{
		alert(errorMsg);
		return;
	}
	newButtonScript();
	habilitaDesabilitaCampos();
}

function habilitaDesabilitaCampos(){
	if(getElementValue("idReferenciaFreteCarreteiro") == ''){
		setDisabled("unidadeFederativaByIdUnidadeFederativaOrigem.idUnidadeFederativa", false);
		setDisabled("unidadeFederativaByIdUnidadeDestino.idUnidadeFederativa", false);
		setDisabled("filialByIdFilialOrigem.idFilial", false);
		setDisabled("filialByIdFilialDestino.idFilial", false);
		setDisabled("moedaPais.idMoedaPais", false);
		setDisabled("dtVigenciaInicial", false);
		setDisabled("dtVigenciaFinal", false);
		setFocus(document.getElementById("unidadeFederativaByIdUnidadeFederativaOrigem.sgUnidadeFederativa"));
	}else{
		setDisabled("unidadeFederativaByIdUnidadeFederativaOrigem.idUnidadeFederativa", true);
		setDisabled("unidadeFederativaByIdUnidadeDestino.idUnidadeFederativa", true);
		setDisabled("filialByIdFilialOrigem.idFilial", true);
		setDisabled("filialByIdFilialDestino.idFilial", true);
		setDisabled("moedaPais.idMoedaPais", true);
	}
}	

//******************funcoes referentes a combo moeda Pais****************************	

//funcao chamada no onShow da tab cad
function populaComboMoedaPais(){
			var remoteCall = {serviceDataObjects:new Array()};
			remoteCall.serviceDataObjects.push(createServiceDataObject("lms.fretecarreteiroviagem.manterTabelasReferenciasFreteKmAction.findMoedaByPais", "verificaMoedaPais", new Array()));
			xmit(remoteCall);
		
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
				setElementValue("moedaPais.moeda.siglaSimbolo",getNestedBeanPropertyValue(dados[i], "moeda.siglaSimbolo"));
				return;
			}
		}
}

</script> 