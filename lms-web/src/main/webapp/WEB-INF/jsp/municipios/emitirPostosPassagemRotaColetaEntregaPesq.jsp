<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="emitirPostosPassagemRotaColetaEntrega" onPageLoadCallBack="findFilialAcessoUsuario">
	<adsm:form action="/municipios/emitirPostosPassagemRotaViagem" >
		 <adsm:i18nLabels>
			<adsm:include key="LMS-29112"/>
		 </adsm:i18nLabels>
		 <adsm:hidden property="tpEmpresa" value="M"/>
		 		 
		 
		<adsm:lookup dataType="text" property="filial" idProperty="idFilial" criteriaProperty="sgFilial" label="filial" 
	    			size="3" maxLength="3" width="80%" action="/municipios/manterFiliais" 
	    			service="lms.municipios.emitirPostosPassagemRotaColetaEntregaAction.findLookupFilial" style="width:45px" labelWidth="20%" required="true" >
                  <adsm:propertyMapping criteriaProperty="tpEmpresa" modelProperty="empresa.tpEmpresa"/>
                  <adsm:propertyMapping relatedProperty="rotaColetaEntrega.idRotaColetaEntrega" modelProperty=""/>
                  <adsm:propertyMapping relatedProperty="rotaColetaEntrega.nrRota" modelProperty=""/>
                  <adsm:propertyMapping relatedProperty="rotaColetaEntrega.dsRota" modelProperty=""/>
                  <adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
                  <adsm:propertyMapping relatedProperty="filialSigla" modelProperty="sgFilial"/>
        		  <adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true"/>
        		  <adsm:hidden property="filialSigla"/>
        </adsm:lookup>
		
		<adsm:lookup 
	        action="/municipios/manterRotaColetaEntrega" 
	        dataType="integer" 
	        property="rotaColetaEntrega" 
	        idProperty="idRotaColetaEntrega" 
	        criteriaProperty="nrRota" 
	        service="lms.municipios.emitirPostosPassagemRotaColetaEntregaAction.findLookupRotaEntrega" 
	        label="rota" labelWidth="20%" size="5" width="80%" maxLength="3">
	        <adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial"/>
   	        <adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial"/>
   	        <adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia"/>
   	        
   	        <adsm:propertyMapping relatedProperty="filial.sgFilial" modelProperty="filial.sgFilial" blankFill="false"/>
   	        <adsm:propertyMapping relatedProperty="filial.idFilial" modelProperty="filial.idFilial" blankFill="false"/>
   	        <adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" blankFill="false"/>
   	        
	        <adsm:propertyMapping relatedProperty="rotaColetaEntrega.dsRota" modelProperty="dsRota"/>
			<adsm:textbox dataType="text" property="rotaColetaEntrega.dsRota" size="30" disabled="true"/>
        </adsm:lookup>
        
		
		<adsm:lookup service="lms.municipios.emitirPostosPassagemRotaColetaEntregaAction.findLookupMunicipio" 
		 dataType="text" disabled="false" property="municipio" criteriaProperty="nmMunicipio" idProperty="idMunicipio" label="localizacao" size="30" 
					 maxLength="30" action="/municipios/manterMunicipios" labelWidth="20%" width="80%" exactMatch="false" minLengthForAutoPopUpSearch="3">
			<adsm:propertyMapping relatedProperty="municipio.nmMunicipio" modelProperty="nmMunicipio"/>
			<adsm:propertyMapping relatedProperty="uf" modelProperty="unidadeFederativa.nmUnidadeFederativa"/>
			<adsm:propertyMapping relatedProperty="pais" modelProperty="unidadeFederativa.pais.nmPais"/>
			<adsm:textbox dataType="text" property="uf" disabled="true"/>
			<adsm:textbox dataType="text" property="pais" disabled="true"/>
		</adsm:lookup>
		
		<adsm:combobox property="moedaPais.idMoedaPais" required="true" onDataLoadCallBack="loadMoedaPadrao" optionLabelProperty="moeda.siglaSimbolo" optionProperty="idMoedaPais" label="converterParaMoeda" service="lms.fretecarreteirocoletaentrega.consultarNotasCreditoAction.findMoedaPais" labelWidth="20%" width="80%" onchange="onChangeMoeda(this);" >
			<adsm:propertyMapping relatedProperty="descricaoMoeda" modelProperty="moeda.siglaSimbolo"/>
			<adsm:propertyMapping relatedProperty="dsSimbolo" modelProperty="moeda.dsSimbolo"/>
			<adsm:propertyMapping relatedProperty="idMoedaDestino" modelProperty="moeda.idMoeda"/>
			<adsm:propertyMapping relatedProperty="idPaisDestino" modelProperty="pais.idPais"/>
		</adsm:combobox>
		
		<adsm:hidden property="descricaoMoeda" serializable="true"/>	
		<adsm:hidden property="idPaisDestino" serializable="true"/>
		<adsm:hidden property="idMoedaDestino" serializable="true"/>	
		<adsm:hidden property="dsSimbolo" serializable="true"/>
		
		<adsm:combobox label="formatoRelatorio" labelWidth="20%" property="tpFormatoRelatorio" domain="DM_FORMATO_RELATORIO" defaultValue="pdf" required="true"/>
		
		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.municipios.emitirPostosPassagemRotaColetaEntregaAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar>	
	</adsm:form>
</adsm:window>
<script>


document.getElementById("municipio.nmMunicipio").serializable="true";
document.getElementById("filial.sgFilial").serializable="true";
document.getElementById("rotaColetaEntrega.nrRota").serializable="true";



function validateTab() {
		if (validateTabScript(document.forms)) {
			if (
			     getElementValue("filial.idFilial") != "" || 
			     getElementValue("rotaColetaEntrega.idRotaColetaEntrega") != "" ||
				 getElementValue("municipio.idMunicipio") != "" || 
				 getElementValue("moedaPais.idMoedaPais") != "" ||
				 getElementValue("periodoEmissaoInicial") != "" || getElementValue("periodoEmissaoFinal")!= "") 
				return true;
			else
				alert(i18NLabel.getLabel("LMS-29112"));	
		}
		return false;			
}

function initWindow(eventObj){
	if(eventObj.name == "cleanButton_click"){
		findFilialAcessoUsuario();
		loadMoedaPadrao();
	}
}

function findFilialAcessoUsuario_cb(data,exception){
		onPageLoad_cb(data,exception);
		setMasterLink(document, true);
		if(document.getElementById("filial.idFilial").masterLink != "true")
			findFilialAcessoUsuario();
	}
	
	function findFilialAcessoUsuario(){
		_serviceDataObjects = new Array();
	   	addServiceDataObject(createServiceDataObject("lms.municipios.emitirPostosPassagemRotaColetaEntregaAction.findFilialUsuarioLogado", "setaInformacoesFilial", new Array()));
	  	xmit();
	}
	
	function setaInformacoesFilial_cb(data, exception){
		if(data != undefined){
			setElementValue("filial.sgFilial", getNestedBeanPropertyValue(data, "filial.sgFilial"));
			setElementValue("filialSigla", getNestedBeanPropertyValue(data, "filial.sgFilial"));
			setElementValue("filial.idFilial", getNestedBeanPropertyValue(data, "filial.idFilial"));
			setElementValue("filial.pessoa.nmFantasia", getNestedBeanPropertyValue(data, "filial.pessoa.nmFantasia"));
			/*
			if(getNestedBeanPropertyValue(data, "filial.sgFilial") == "MTZ")
				setDisabled("filial.idFilial", false);
			else
				setDisabled("filial.idFilial", true);	*/
			
		}
	}

function onChangeMoeda(elem) {
		comboboxChange({e:elem});
	}	
	
	function loadMoedaPadrao_cb(data){
		moedaPais_idMoedaPais_cb(data);
		loadMoedaPadrao();
	}

	function loadMoedaPadrao(){

		var data = new Array();
		
		var sdo = createServiceDataObject("lms.municipios.emitirPostosPassagemRotaColetaEntregaAction.findMoedaUsuario",
				"setaMoeda",data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function setaMoeda_cb(data){
		if (data != undefined) {
			setElementValue("moedaPais.idMoedaPais", data.idMoedaPais);
			setElementValue("idMoedaDestino", data.idMoeda);
			setElementValue("dsSimbolo", data.dsSimbolo);
			setElementValue("descricaoMoeda", data.siglaSimbolo);
		}
	}

</script>
