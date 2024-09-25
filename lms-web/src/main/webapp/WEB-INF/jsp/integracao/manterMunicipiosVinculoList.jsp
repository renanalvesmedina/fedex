<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.integracao.manterMunicipiosVinculoAction">
	<adsm:form action="/integracao/manterMunicipiosVinculo" idProperty="idMunicipioVinculo">

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
					 exactMatch="false"
			         action="/municipios/manterMunicipios" 
			         minLengthForAutoPopUpSearch="3"
			         onPopupSetValue="muniLmsPopUp">
			
			<adsm:propertyMapping relatedProperty="municipioLmsCep.nrCep"
        						  modelProperty="nrCep" />       	
        	<adsm:propertyMapping relatedProperty="municipioLmsCep.idMunicipio"
        						  modelProperty="idMunicipio"/>		
        		
        	<adsm:propertyMapping relatedProperty="municipioLms.unidadeFederativa.idUnidadeFederativa" 
        						  modelProperty="unidadeFederativa.idUnidadeFederativa" />			  			  					  
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
			         serializable="false"
			         onPopupSetValue="muniLmsPopUp">
			         
	 		<adsm:propertyMapping relatedProperty="municipioLms.nmMunicipio"
        						  modelProperty="nmMunicipio" /> 	        		  
			<adsm:propertyMapping relatedProperty="municipioLms.idMunicipio"
        						  modelProperty="idMunicipio"/>	
       		
        	<adsm:propertyMapping relatedProperty="municipioLms.unidadeFederativa.idUnidadeFederativa" 
        						  modelProperty="unidadeFederativa.idUnidadeFederativa" />					  
        	<adsm:propertyMapping relatedProperty="municipioLms.unidadeFederativa.sgUnidadeFederativa" 
        						  modelProperty="unidadeFederativa.sgUnidadeFederativa" />

			<adsm:propertyMapping relatedProperty="municipioLms.unidadeFederativa.pais.nmPais" 
								  modelProperty="unidadeFederativa.pais.nmPais" />
			<adsm:propertyMapping relatedProperty="municipioLms.cdIbge" 
        						  modelProperty="cdIbge" /> 
        </adsm:lookup>
					  
					  
		<adsm:lookup label="uf" 
					 property="municipioLms.unidadeFederativa" 
					 idProperty="idUnidadeFederativa"		 
					 dataType="text" 
					 criteriaProperty="sgUnidadeFederativa"
					 service="lms.integracao.manterMunicipiosVinculoAction.findLookupUnidadeFederativa" 
					 size="4" 
					 maxLength="2"
			         action="/municipios/manterUnidadesFederativas" 
			         minLengthForAutoPopUpSearch="5"
			         >
	 		<adsm:propertyMapping relatedProperty="municipioLms.nmMunicipio"
        						  modelProperty="nmMunicipio" /> 	        		  
			<adsm:propertyMapping relatedProperty="municipioLms.idMunicipio"
        						  modelProperty="idMunicipio"/>			                						  
			
			<adsm:propertyMapping relatedProperty="municipioLmsCep.nrCep"
        						  modelProperty="nrCep" />       	
        						  
        	<adsm:propertyMapping relatedProperty="municipioLmsCep.idMunicipio"
        						  modelProperty="idMunicipio"/>	
			
			<adsm:propertyMapping relatedProperty="municipioLms.unidadeFederativa.pais.nmPais" 
								  modelProperty="pais.nmPais" />
 
			<adsm:propertyMapping relatedProperty="municipioLms.cdIbge" 
        						  modelProperty="cdIbge" />  
        </adsm:lookup>
        			  
		<adsm:textbox label="pais" dataType="text" 
					  property="municipioLms.unidadeFederativa.pais.nmPais" 
					  serializable="false"
					  disabled="true"/>

		<adsm:textbox label="codIBGE" dataType="text"
					  property="municipioLms.cdIbge"
					  serializable="false"
					  size="10"
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
					  size="4" onchange="uper();"
					  maxLength="2"/>

		<adsm:textbox label="pais" dataType="text" 
					  property="municipioCorporativo.pais.nmPais" 
					  serializable="false"
					  disabled="true"/>		
		
		<adsm:textbox label="codIBGE" dataType="text"
					  property="municipioCorporativo.cdIbge" 
					  serializable="false"
					  size="10"
					  disabled="true" />
		
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="municipioVinculo"/>
			<adsm:resetButton />
		</adsm:buttonBar>
		
		
		
	</adsm:form>
	<adsm:grid property="municipioVinculo" idProperty="idMunicipioVinculo" rows="7"
			   defaultOrder="municipioLms.nmMunicipio" >
	
		<adsm:gridColumn title="municipioLms"
						 property="municipioLms.nmMunicipio" 
						 dataType="text"
						 width="35%" />
						 
		<adsm:gridColumn title="cepLms"
						 property="municipioLms.nrCep" 
						 dataType="text"
						 width="15%" />	
		
		<adsm:gridColumn title="municipioCorporativo"
						 property="municipioCorporativo.nmMunicipio" 
						 dataType="text"
						 width="35%" />
						 
		<adsm:gridColumn title="cepCorporativo"
						 property="municipioCorporativo.nrCep" 
						 dataType="text"
						 width="15%" />			

		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>
<script type="text/javascript">

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
	
	function uper(){
		setElementValue("municipioCorporativo.sgUnidadeFederativa", getElementValue("municipioCorporativo.sgUnidadeFederativa").toUpperCase());
	}

</script>