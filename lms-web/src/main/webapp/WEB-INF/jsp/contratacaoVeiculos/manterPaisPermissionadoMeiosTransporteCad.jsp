<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
function habilitaDesabilitaCfmeVigencias_cb(data,exception){
  onDataLoad_cb(data,exception);
  
  if (data != undefined) {
	   var idFrota = getNestedBeanPropertyValue(data,"meioTransporteRodoviario.idMeioTransporte");
	   var nrFrota = getNestedBeanPropertyValue(data,"meioTransporteRodoviario.meioTransporte.nrFrota");
	 // É necessário preencher via js a segunda lookup de 'Cavalo Mecânico'.                        
	if (idFrota != undefined) {
		 setNestedBeanPropertyValue(data,"meioTransporteRodoviario2.idMeioTransporte",idFrota);
		 setElementValue("meioTransporteRodoviario2.meioTransporte.nrFrota",nrFrota);
	}
  }
  
  var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
  validaAcaoVigencia(acaoVigenciaAtual, null);
 	
}
	function validaAcaoVigencia(acaoVigenciaAtual, tipoEvento){
		  if(acaoVigenciaAtual == 0){
		     setDisabled(document, false);
		     if(tipoEvento == "" ||  tipoEvento == null)
     		   setFocusOnFirstFocusableField(document);
		     else
		       setFocusOnNewButton(document);
		  }else if(acaoVigenciaAtual == 1) {
			 setDisabled(document,true);
		     setDisabled("botaoNovo",false);
		     setDisabled("botaoSalvar",false);
		     setDisabled("dtVigenciaFinal", false);
		     if(tipoEvento == "" ||  tipoEvento == null)
     		   setFocusOnFirstFocusableField(document);
		     else
		       setFocusOnNewButton(document);
		  }else if(acaoVigenciaAtual == 2) {
		  	 setDisabled(document,true);
		     setDisabled("botaoNovo",false);
		     setFocusOnNewButton(document);
		  }
	}

	function afterStore_cb(data,exception,key) {
    	store_cb(data,exception,key);
		if (exception == undefined) {
			var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
			var store = "true";
		    validaAcaoVigencia(acaoVigenciaAtual, store);
		}
    }

//nova function do button novo
function habilitaCampos(){
	newButtonScript();
	setDisabled(document,false);
	setDisabled("botaoExcluir",true);
}

//é executada qdo clica na aba detalhamento
function initWindow(eventObj) {
    if (eventObj.name != "gridRow_click" && eventObj.name != "storeButton") {
		setDisabled(document,false);
		setDisabled("botaoExcluir",true);
		setFocusOnFirstFocusableField();
	}
}
</script>
<adsm:window service="lms.contratacaoveiculos.meioTranspRodoPermissoService">
	<adsm:form action="/contratacaoVeiculos/manterPaisPermissionadoMeiosTransporte" idProperty="idMeioTranspRodoPermisso"
		onDataLoadCallBack="habilitaDesabilitaCfmeVigencias"
		service="lms.contratacaoveiculos.meioTranspRodoPermissoService.findByIdEValidaDtVigencia">

		<adsm:hidden property="tpSituacao" serializable="false" value="A"/>

		<adsm:lookup 
		    picker="false"
		    serializable="false"
			property="meioTransporteRodoviario2" 
			criteriaProperty="meioTransporte.nrFrota" 
			idProperty="idMeioTransporte" 
			service="lms.contratacaoveiculos.meioTransporteRodoviarioService.findLookup" 
			label="meioTransporte" labelWidth="24%"
			dataType="text" size="8" maxLength="6"
			width="82%" 
			cmd="rodo" required="true"
			action="/contratacaoVeiculos/manterMeiosTransporte" >
			<adsm:propertyMapping 
			         criteriaProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador"
                     modelProperty="meioTransporte.nrIdentificador" />

            <adsm:propertyMapping 
                      relatedProperty="meioTransporteRodoviario.idMeioTransporte"
                      modelProperty="idMeioTransporte" />      
            <adsm:propertyMapping 
            		  relatedProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador"
                      modelProperty="meioTransporte.nrIdentificador" />
              
            <adsm:propertyMapping 
            		  criteriaProperty="tpSituacao"
                      modelProperty="meioTransporte.tpSituacao" />
  	
		<adsm:lookup
			picker="true"
			property="meioTransporteRodoviario" 
			criteriaProperty="meioTransporte.nrIdentificador" 
			idProperty="idMeioTransporte" 
			service="lms.contratacaoveiculos.meioTransporteRodoviarioService.findLookup" 
			dataType="text"  
			size="20" maxLength="25" 
			cmd="rodo"
			action="/contratacaoVeiculos/manterMeiosTransporte"
			exactMatch="false"
			minLengthForAutoPopUpSearch="3">
			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario2.meioTransporte.nrFrota" modelProperty="meioTransporte.nrFrota" />

			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.idMeioTransporte" modelProperty="idMeioTransporte" /> 

			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.meioTransporte.nrFrota" modelProperty="meioTransporte.nrFrota" />
			
            <adsm:propertyMapping 
            		  criteriaProperty="tpSituacao"
                      modelProperty="meioTransporte.tpSituacao" />
						
		</adsm:lookup>		
	   </adsm:lookup>

		<adsm:lookup property="pais" criteriaProperty="nmPais" idProperty="idPais" service="lms.municipios.paisService.findLookup" dataType="text" maxLength="60" label="pais" size="30" labelWidth="24%" width="76%" action="/municipios/manterPaises" required="true" exactMatch="false" minLengthForAutoPopUpSearch="3">		
            <adsm:propertyMapping 
            		  criteriaProperty="tpSituacao"
                      modelProperty="tpSituacao" />
		
        </adsm:lookup>

		<adsm:textbox dataType="integer" property="nrPermisso" label="numeroPermisso" maxLength="10" size="20" labelWidth="24%" width="60%" required="true"/>

		<adsm:range label="vigencia" width="60%" labelWidth="24%">
			<adsm:textbox dataType="JTDate" required="true" property="dtVigenciaInicial"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal" required="true"/>
		</adsm:range>

		<adsm:buttonBar>
		    <adsm:storeButton callbackProperty="afterStore" id="botaoSalvar"
		    		service="lms.contratacaoveiculos.meioTranspRodoPermissoService.storeMap" />
			<adsm:newButton id="botaoNovo" />
			<adsm:removeButton id="botaoExcluir"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>