

<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
function desabilitaLookupPostoPassagem() {
   onPageLoad();
   setDisabled("postoPassagem.idPostoPassagem", false);
   setDisabled("postoPassagem.tpPostoPassagem.description", true);
}
</script>
<adsm:window title="emitirValoresPostosPassagemRota" onPageLoad="desabilitaLookupPostoPassagem" onPageLoadCallBack="findFilialAcessoUsuario">
	<adsm:form action="/municipios/emitirValoresPostosPassagemRota">
		<adsm:hidden property="dsRota" serializable="false"/>
		<adsm:hidden property="tpEmpresa" value="M" serializable="false"/>
	    <adsm:listbox 
                   label="rota" 
                   size="4" 
                   property="filialRotas" 
				   optionProperty="sgFilial"
				   optionLabelProperty="dsRota"
				   labelWidth="22%"
                   width="78%" 
                   showOrderControls="false" boxWidth="180" showIndex="false" serializable="true" required="true">
                 <adsm:lookup 
	                 property="filial" 
	                 idProperty="idFilial" 
	                 criteriaProperty="sgFilial" 
	                 dataType="text" size="3" maxLength="3" 
	                 service="lms.municipios.emitirValoresPostosPassagemRotaAction.findFilialLookup" action="/municipios/manterFiliais"
	                 exactMatch="false" minLengthForAutoPopUpSearch="3">
	                 <adsm:propertyMapping relatedProperty="filialRotas_filial.nmFilial" modelProperty="pessoa.nmFantasia"/>
	                 <adsm:propertyMapping criteriaProperty="tpEmpresa" modelProperty="empresa.tpEmpresa"/>
	                 <adsm:textbox dataType="text" property="filial.nmFilial" serializable="false" disabled="true"/>
	              </adsm:lookup>   
       </adsm:listbox>
       <adsm:combobox property="tpPostoPassagem" domain="DM_POSTO_PASSAGEM" label="tipoPostoPassagem" labelWidth="22%" width="78%"/>
       		
       
		
		<adsm:lookup onPopupSetValue="popUpLookup" dataType="text" property="postoPassagem" idProperty="idPostoPassagem" criteriaProperty="tpPostoPassagem.description"  labelWidth="22%"  label="postoPassagem" size="35" maxLength="30" width="78%" action="/municipios/manterPostosPassagem" exactMatch="false" minLengthForAutoPopUpSearch="3" required="false" disabled="true">
	        <adsm:propertyMapping relatedProperty="postoPassagem.municipio.nmMunicipio" modelProperty="municipio.nmMunicipio" />
	       	<adsm:propertyMapping relatedProperty="postoPassagem.tpSentidoCobranca.description" modelProperty="tpSentidoCobranca.description" />
	       	<adsm:propertyMapping relatedProperty="postoPassagem.nrKm" modelProperty="nrKm" />
	       	<adsm:propertyMapping relatedProperty="postoPassagem.rodovia.sgRodovia" modelProperty="rodovia.sgRodovia" />
	       	<adsm:propertyMapping criteriaProperty="tpPostoPassagem" modelProperty="tpPostoPassagem"/>
	    </adsm:lookup>
		
		<adsm:textbox dataType="text" property="postoPassagem.municipio.nmMunicipio" label="localizacao" size="35" maxLength="35" labelWidth="22%" width="28%" disabled="true" serializable="false"/>
		<adsm:textbox dataType="text" property="postoPassagem.tpSentidoCobranca.description" label="sentido" size="35" maxLength="35" labelWidth="22%" width="28%" disabled="true" serializable="false"/>
		<adsm:textbox dataType="text" property="postoPassagem.rodovia.sgRodovia" label="rodovia" size="35" maxLength="35" labelWidth="22%" width="28%" disabled="true" serializable="false"/>
		<adsm:textbox dataType="integer" mask="##,###.#" property="postoPassagem.nrKm" label="km" size="10" maxLength="10" labelWidth="22%" width="28%" disabled="true" serializable="false"/>
		
		<adsm:combobox property="moedaPais.idMoedaPais" onDataLoadCallBack="loadMoedaPadrao" optionLabelProperty="moeda.siglaSimbolo" optionProperty="idMoedaPais" label="converterParaMoeda" service="lms.municipios.emitirValoresPostosPassagemRotaAction.findMoedaPais" labelWidth="22%" width="78%" onchange="onChangeMoeda(this);" required="true">
			<adsm:propertyMapping relatedProperty="descricaoMoeda" modelProperty="moeda.siglaSimbolo"/>
			<adsm:propertyMapping relatedProperty="dsSimbolo" modelProperty="moeda.dsSimbolo"/>
			<adsm:propertyMapping relatedProperty="idMoedaDestino" modelProperty="moeda.idMoeda"/>
			<adsm:propertyMapping relatedProperty="idPaisDestino" modelProperty="pais.idPais"/>
		</adsm:combobox>
		
		<adsm:hidden property="descricaoMoeda" serializable="true"/>	
		<adsm:hidden property="idPaisDestino" serializable="true"/>
		<adsm:hidden property="idMoedaDestino" serializable="true"/>	
		<adsm:hidden property="dsSimbolo" serializable="true"/>
		
		<adsm:combobox label="formatoRelatorio" labelWidth="22%" property="tpFormatoRelatorio" domain="DM_FORMATO_RELATORIO" defaultValue="pdf" required="true"/>
		
		<adsm:buttonBar>	
			<adsm:reportViewerButton service="lms.municipios.emitirValoresPostosPassagemRotaAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar>	
	</adsm:form>
</adsm:window>
<script>
document.getElementById("postoPassagem.tpPostoPassagem.description").serializable = "true";

	function findFilialAcessoUsuario_cb(data,exception){
		onPageLoad_cb(data,exception);
		findFilialAcessoUsuario();
	}
	
	function findFilialAcessoUsuario(){
		_serviceDataObjects = new Array();
	   	addServiceDataObject(createServiceDataObject("lms.municipios.emitirValoresPostosPassagemRotaAction.findFilialUsuarioLogado", "setaInformacoesFilial", new Array()));
	  	xmit();
	}
	
	function setaInformacoesFilial_cb(data, exception){
		if(data != undefined){

			filialRotasListboxDef.renderOptions({filialRotas:data});
			
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
		
		var sdo = createServiceDataObject("lms.municipios.emitirValoresPostosPassagemRotaAction.findMoedaUsuario",
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
	
	//posto de passagem
	
	//PopUp
	function popUpLookup(data) {
		if (getElementValue("postoPassagem.idPostoPassagem") != data.idPostoPassagem) {
			setElementValue("postoPassagem.idPostoPassagem",data.idPostoPassagem);
					
		}
		return true;
	}
	
	function initWindow(eventObj){
		if(eventObj.name == "cleanButton_click"){
			loadMoedaPadrao();
			findFilialAcessoUsuario();
		}	
	}

	
</script>


