<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.manterRegioesFilialRotaAction" onPageLoadCallBack="regiaoFilialRotaPageLoad">

	<adsm:form action="/municipios/manterRegioesFilialRota" service="lms.municipios.manterRegioesFilialRotaAction.findById" onDataLoadCallBack="regiaoFilialRotaDataLoad" idProperty="idRegiaoFilialRotaColEnt">
	
			
		<adsm:lookup service="lms.municipios.manterRegioesFilialRotaAction.findFilialLookup" dataType="text" property="rotaColetaEntrega.filial" 
					 idProperty="idFilial" criteriaProperty="sgFilial" label="filialAtendida" size="3" maxLength="3" 
					 exactMatch="true" labelWidth="15%" width="50%"  action="/municipios/manterFiliais" required="true">
			<adsm:propertyMapping relatedProperty="rotaColetaEntrega.filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="rotaColetaEntrega.filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>
		</adsm:lookup>

		<adsm:lookup service="lms.municipios.manterRegioesFilialRotaAction.findRotaLookup" idProperty="idRotaColetaEntrega" 
					 dataType="integer" property="rotaColetaEntrega" criteriaProperty="nrRota" required="true"
					 label="numeroRota" size="3" maxLength="3" labelWidth="15%" width="50%" action="/municipios/manterRotaColetaEntrega">
            <adsm:propertyMapping relatedProperty="rotaColetaEntrega.dsRota" modelProperty="dsRota"/>
            <adsm:propertyMapping criteriaProperty="rotaColetaEntrega.filial.sgFilial" modelProperty="filial.sgFilial" />
            <adsm:propertyMapping criteriaProperty="rotaColetaEntrega.filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" />
            <adsm:propertyMapping criteriaProperty="rotaColetaEntrega.filial.idFilial" modelProperty="filial.idFilial" />
	        <adsm:textbox dataType="text" property="rotaColetaEntrega.dsRota" size="30" disabled="true"/>             
        </adsm:lookup>
      
		<adsm:combobox property="regiaoColetaEntregaFil.idRegiaoColetaEntregaFil" label="regiaoFilial" autoLoad="false" service="" 
					   optionLabelProperty="dsRegiaoColetaEntregaFil" optionProperty="idRegiaoColetaEntregaFil" required="true" labelWidth="15%" width="35%" boxWidth="231" >
	            <adsm:propertyMapping relatedProperty="regiao.dtVigenciaInicial" modelProperty="dtVigenciaInicial"/>
   	            <adsm:propertyMapping relatedProperty="regiao.dtVigenciaFinal" modelProperty="dtVigenciaFinal"/>
		</adsm:combobox>
		<adsm:hidden property="idRegiaoColetaEntregaFilMasterLink" serializable="false"/>
		
		<adsm:range label="vigenciaRegiao" >
             <adsm:textbox dataType="JTDate" property="regiao.dtVigenciaInicial" picker="false" disabled="true" serializable="false"/>
             <adsm:textbox dataType="JTDate" property="regiao.dtVigenciaFinal" picker="false" disabled="true" serializable="false"/>
        </adsm:range>
        
		<adsm:range label="vigencia" width="60%">
             <adsm:textbox dataType="JTDate" required="true" property="dtVigenciaInicial"/>
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>

	<adsm:buttonBar>
		<adsm:storeButton service="lms.municipios.manterRegioesFilialRotaAction.storeMap" callbackProperty="regiaoFilialRotaStore"/>
		<adsm:newButton id="limpar"/>
		<adsm:removeButton/>
	</adsm:buttonBar>

	</adsm:form>	
</adsm:window>

<script>
	
	function regiaoFilialRotaPageLoad_cb(){
		onPageLoad_cb();
		carregaRegiaoFilial();	
	}
	
	function carregaRegiaoFilial(){

		var data = new Array();

		if (document.getElementById("regiaoColetaEntregaFil.idRegiaoColetaEntregaFil").masterLink != "true")
			setNestedBeanPropertyValue(data, "filial.idFilial", getElementValue("rotaColetaEntrega.filial.idFilial"));
			
		
		var sdo = createServiceDataObject("lms.municipios.manterRegioesFilialRotaAction.findRegiaoColetaFilial", "carregaRegiaoFilial", data);
		xmit({serviceDataObjects:[sdo], onXmitDone:"setaRegiaoFilial"});
	}
	
	function carregaRegiaoFilial_cb(data, error){
	 	regiaoColetaEntregaFil_idRegiaoColetaEntregaFil_cb(data, error);
	 	
	 	if (data != undefined)
		 	setaMascaraVigenciaRegiao(data);
	 
	 }
	
	
	function setaRegiaoFilial_cb(){
		if (getElementValue("idRegiaoColetaEntregaFilMasterLink") != ""){
			setElementValue("regiaoColetaEntregaFil.idRegiaoColetaEntregaFil", getElementValue("idRegiaoColetaEntregaFilMasterLink"));
		}
	}
	
	function regiaoFilialRotaStore_cb(data, error, key){
		store_cb(data, error, key);

		if (error == undefined && data != undefined){
			var acaoVigencia = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
			var store = "true";
			validaAcaoVigencia(acaoVigencia, store);
		}
	}

	function regiaoFilialRotaDataLoad_cb(data, error){
		onDataLoad_cb(data, error);
		var acaoVigencia = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
		validaAcaoVigencia(acaoVigencia, null);
	}
	
	function validaAcaoVigencia(acaoVigencia, tipoEvento){
		if (acaoVigencia == 0){
			novo();
			setDisabled("__buttonBar:0.removeButton", false);
			if(tipoEvento == "" ||  tipoEvento == null)
     			setFocusOnFirstFocusableField(document);
		    else
		    	setFocus(document.getElementById("limpar"), false);   
		} else if (acaoVigencia == 1){
			setDisabled(document, true);					
			setDisabled("dtVigenciaFinal", false);
			setDisabled("limpar", false);
			setDisabled("__buttonBar:0.storeButton", false);
			if(tipoEvento == "" ||  tipoEvento == null)
     			setFocusOnFirstFocusableField(document);
		    else
		    	setFocus(document.getElementById("limpar"), false);	
		} else if (acaoVigencia == 2) {
			setDisabled(document, true);
			setDisabled("limpar", false);
			setFocus(document.getElementById("limpar"), false);
		}
	}
	
	function novo(){
		setDisabled("rotaColetaEntrega.filial.pessoa.nmFantasia", document.getElementById("rotaColetaEntrega.filial.pessoa.nmFantasia").masterLink == "true");
		setDisabled("rotaColetaEntrega.filial.sgFilial", document.getElementById("rotaColetaEntrega.filial.sgFilial").masterLink == "true");
		setDisabled("rotaColetaEntrega.nrRota", document.getElementById("rotaColetaEntrega.nrRota").masterLink == "true");
		setDisabled("rotaColetaEntrega.idRotaColetaEntrega", document.getElementById("rotaColetaEntrega.idRotaColetaEntrega").masterLink == "true");
		setDisabled("regiaoColetaEntregaFil.idRegiaoColetaEntregaFil", document.getElementById("regiaoColetaEntregaFil.idRegiaoColetaEntregaFil").masterLink ==  "true");
		setDisabled("dtVigenciaInicial", false);
		setDisabled("dtVigenciaFinal", false);
		setDisabled("limpar", false);
		setDisabled("__buttonBar:0.storeButton", false);
		setDisabled("__buttonBar:0.removeButton", true);
	}

	function initWindow(event){
		if (event.name != "gridRow_click" && event.name != "storeButton"){
			novo();
			setFocusOnFirstFocusableField();
		}
	}	

	function setaMascaraVigenciaRegiao(data) {
		
		var i;
		var vi = document.getElementById("regiao.dtVigenciaInicial");
		var vf = document.getElementById("regiao.dtVigenciaFinal");	
		for (i = 0; i < data.length; i++) {
			setNestedBeanPropertyValue(data, i + ":dtVigenciaInicial", setFormat(vi, getNestedBeanPropertyValue(data, i + ":dtVigenciaInicial")));
			setNestedBeanPropertyValue(data, i + ":dtVigenciaFinal",   setFormat(vf, getNestedBeanPropertyValue(data, i + ":dtVigenciaFinal")));		
		}
	}
</script>
