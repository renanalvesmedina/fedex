<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.integracao.manterMunicipiosVinculoAction">
	<adsm:form action="/integracao/manterMunicipiosVinculo" idProperty="idMunicipioVinculo"
				onDataLoadCallBack="loadData">

		<%-------- LMS --------%>
		<adsm:section caption="municipioLms" />
		<adsm:lookup label="municipio" 
					 popupLabel="municipioLms"
					 property="municipioLms" 
					 idProperty="idMunicipio"
					 dataType="text"
					 criteriaProperty="nmMunicipio"
					 service="lms.integracao.manterMunicipiosVinculoAction.findLookupMunicipio" 
					 size="35" 
					 maxLength="60" 
					 required="true"
					 exactMatch="false"
			         action="/municipios/manterMunicipios" 
			         minLengthForAutoPopUpSearch="3"
			         onPopupSetValue="muniLmsPopUp">
			
			<adsm:propertyMapping relatedProperty="municipioLmsCep.nrCep"
        						  modelProperty="nrCep" />       	
        	<adsm:propertyMapping relatedProperty="municipioLmsCep.idMunicipio"
        						  modelProperty="idMunicipio"/>		
        					  			  					  
        	<adsm:propertyMapping relatedProperty="municipioLms.unidadeFederativa.sgUnidadeFederativa" 
        						  modelProperty="unidadeFederativa.sgUnidadeFederativa" />
        						  
			<adsm:propertyMapping relatedProperty="municipioLms.unidadeFederativa.pais.nmPais" 
								  modelProperty="unidadeFederativa.pais.nmPais" />
			<adsm:propertyMapping relatedProperty="municipioLms.cdIbge"
        						  modelProperty="cdIbge" /> 
        </adsm:lookup>
				
		<adsm:lookup label="cep" 
					 popupLabel="municipioLms"
					 property="municipioLmsCep" 
					 idProperty="idMunicipio"		 
					 dataType="text" 
					 criteriaProperty="nrCep"
					 service="lms.integracao.manterMunicipiosVinculoAction.findLookupMunicipio" 
					 size="10" 
					 maxLength="9" 
			         action="/municipios/manterMunicipios" 
			         minLengthForAutoPopUpSearch="5"
			         required="true"
			         serializable="false"
			         onPopupSetValue="muniLmsPopUp">
			         
	 		<adsm:propertyMapping relatedProperty="municipioLms.nmMunicipio"
        						  modelProperty="nmMunicipio" /> 	        		  
			<adsm:propertyMapping relatedProperty="municipioLms.idMunicipio"
        						  modelProperty="idMunicipio"/>	
       						  
        	<adsm:propertyMapping relatedProperty="municipioLms.unidadeFederativa.sgUnidadeFederativa" 
        						  modelProperty="unidadeFederativa.sgUnidadeFederativa" />

			<adsm:propertyMapping relatedProperty="municipioLms.unidadeFederativa.pais.nmPais" 
								  modelProperty="unidadeFederativa.pais.nmPais" />
			<adsm:propertyMapping relatedProperty="municipioLms.cdIbge" 
        						  modelProperty="cdIbge" /> 
        </adsm:lookup>
        			  
		<adsm:textbox label="uf" dataType="text" 
					  property="municipioLms.unidadeFederativa.sgUnidadeFederativa" 
					  size="4"
					  maxLength="2"
					  serializable="false"
					  disabled="true"/>			  
    
		<adsm:textbox label="pais" dataType="text" 
					  property="municipioLms.unidadeFederativa.pais.nmPais" 
					  serializable="false"
					  disabled="true"/>

		<adsm:textbox label="codIBGE" dataType="text"
					  property="municipioLms.cdIbge" 
					  size="10"
					  serializable="false"
					  disabled="true" 
					  />
					  
		<%-------- CORPORATIVO --------%>
		<adsm:section caption="municipioCorporativo" />
		
		<adsm:lookup label="municipio"
					 popupLabel="municipioCorporativo"
					 property="municipioCorporativo" 
					 idProperty="idMunicipio" 
					 dataType="text" 
					 criteriaProperty="nmMunicipio"
					 service="lms.integracao.manterMunicipiosVinculoAction.findLookupMunicipioCorporativo" 
					 size="35" 
					 maxLength="60"
					 exactMatch="false"
					 required="true"
			         action="/integracao/manterMunicipiosCorporativo" 
			         minLengthForAutoPopUpSearch="3"
			         >
			<adsm:propertyMapping relatedProperty="municipioCorporativoCep.nrCep"
        						  modelProperty="nrCep" />       	
        	<adsm:propertyMapping relatedProperty="municipioCorporativoCep.idMunicipio"
        						  modelProperty="idMunicipio"/>		
        						  
			<adsm:propertyMapping relatedProperty="municipioCorporativo.sgUnidadeFederativa" 
								  modelProperty="sgUnidadeFederativa" />	
			<adsm:propertyMapping relatedProperty="municipioCorporativo.pais.nmPais" 
								  modelProperty="pais.nmPais" />
			<adsm:propertyMapping relatedProperty="municipioCorporativo.cdIbge" 
								  modelProperty="cdIbge" />
		</adsm:lookup>
		
		<adsm:lookup label="cep"
					 popupLabel="municipioCorporativo"
					 property="municipioCorporativoCep" 
					 idProperty="idMunicipio" 
					 dataType="text" 
					 criteriaProperty="nrCep"
					 service="lms.integracao.manterMunicipiosVinculoAction.findLookupMunicipioCorporativo" 
					 size="10" 
					 maxLength="9" 
					 required="true"
			         action="/integracao/manterMunicipiosCorporativo" 
					 serializable="false"
			         minLengthForAutoPopUpSearch="3">
			         
	 		<adsm:propertyMapping relatedProperty="municipioCorporativo.nmMunicipio"
        						  modelProperty="nmMunicipio" /> 	        		  
			<adsm:propertyMapping relatedProperty="municipioCorporativo.idMunicipio"
        						  modelProperty="idMunicipio"/>	
        						  
			<adsm:propertyMapping relatedProperty="municipioCorporativo.sgUnidadeFederativa" 
								  modelProperty="sgUnidadeFederativa" />	
			<adsm:propertyMapping relatedProperty="municipioCorporativo.pais.nmPais" 
								  modelProperty="pais.nmPais" />
			<adsm:propertyMapping relatedProperty="municipioCorporativo.cdIbge" 
								  modelProperty="cdIbge" />
		</adsm:lookup>	
				
	
		<adsm:textbox label="uf" dataType="text" 
					  property="municipioCorporativo.sgUnidadeFederativa"
					  serializable="false"
					  size="4"
					  maxLength="2"
					  disabled="true"/>
		
		<adsm:textbox label="pais" dataType="text" 
					  property="municipioCorporativo.pais.nmPais" disabled="true"
					  serializable="false"/>		
		
		<adsm:textbox label="codIBGE" dataType="text"
					  property="municipioCorporativo.cdIbge" disabled="true" 
					  size="10"
					  serializable="false"/>
		
		<adsm:buttonBar>
			<adsm:newButton/>
			<adsm:storeButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
	function loadData_cb(data, error){
		var eventObj = new Array();
		eventObj.name = "onDataLoad";
		onDataLoad_cb(data, error, error, eventObj);

		setElementValue("municipioLmsCep.nrCep", data.municipioLms.nrCep);
		setElementValue("municipioCorporativoCep.nrCep", data.municipioCorporativo.nrCep);
	}
	
	
	function muniLmsPopUp(data, error){
		if(data != undefined) {
		
			var dataLms = new Array();
			dataLms.idMunicipio = data.idMunicipio;
			var sdo = createServiceDataObject("lms.integracao.manterMunicipiosVinculoAction.findMunicipio", "loadMunicipioLms", dataLms);
    		xmit({serviceDataObjects:[sdo]});
    		return;
		}
	}

	function loadMunicipioLms_cb(data){
		if(data != undefined) {
			setElementValue("municipioLms.cdIbge", data.cdIbge);
		}
		return;
	}

</script>