<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.workflow.manterCopiasEmailAction" onPageLoadCallBack="myOnPageLoadCallBack">
	<adsm:form action="/workflow/manterCopiasEmail" idProperty="idEmailEventoUsuario">

		<adsm:hidden property="eventoWorkflow.tpSituacao" serializable="true" value="A"/>
		
		<adsm:lookup property="eventoWorkflow"
               		 idProperty="idEventoWorkflow"
		             criteriaProperty="tipoEvento.nrTipoEvento"
		             label="evento" 
		             service="lms.workflow.manterCopiasEmailAction.findLookupEventoWorkflow"
		             dataType="integer"
		             width="85%" 
		             size="4" 
		             maxLength="4"
		             exactMatch="true"
		             required="true"					 		             
		             action="/workflow/manterEventos"
		             minLengthForAutoPopUpSearch="3"
		             labelWidth="15%" >		             
			<adsm:propertyMapping relatedProperty="eventoWorkflow.tipoEvento.dsTipoEvento" modelProperty="tipoEvento.dsTipoEvento" />
			<adsm:propertyMapping criteriaProperty="eventoWorkflow.tipoEvento.dsTipoEvento" modelProperty="tipoEvento.dsTipoEvento" />
			<adsm:propertyMapping criteriaProperty="eventoWorkflow.tpSituacao" modelProperty="tpSituacao"/>				
			<adsm:textbox dataType="text" property="eventoWorkflow.tipoEvento.dsTipoEvento" size="60" maxLength="60" disabled="true"/>
	  	</adsm:lookup>  
        
        <adsm:hidden property="usuario.tpCategoriaUsuario" value="F"/>
        <adsm:lookup label="usuario"
					 property="usuario" 
					 idProperty="idUsuario"
					 criteriaProperty="nrMatricula" 
					 dataType="text"					  
					 size="16" 
					 width="85%"
					 maxLength="16"	
					 required="true"	 					 
					 service="lms.workflow.manterIntegrantesComiteAction.findLookupUsuarioFuncionario"
					 action="/seguranca/consultarUsuarioLMS">
			<adsm:propertyMapping relatedProperty="usuario.nmUsuario" modelProperty="nmUsuario" />
			<adsm:propertyMapping criteriaProperty="usuario.tpCategoriaUsuario" modelProperty="tpCategoriaUsuario"/>
			<adsm:textbox dataType="text" property="usuario.nmUsuario" disabled="true" serializable="true" maxLength="60" size="40"/>
		</adsm:lookup>	        
                						
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" width="85%" required="true"/>
		
		<adsm:hidden property="dataAtual" serializable="false"/>
		
		<adsm:listbox label="filiais" 
					  property="filialEmailEventoUsuario" 
					  optionProperty="sgFilial"					  
					  size="05" 					  
					  boxWidth="150">		
				<adsm:lookup property="filial"
		  					 idProperty="idFilial"
		  	                 criteriaProperty="sgFilial"
		  					 action="/municipios/manterFiliais"
		  					 service="lms.workflow.manterCopiasEmailAction.findLookupBySgFilial"
		  					 dataType="text"
		  					 serializable="false"
		  					 size="3"
		  					 maxLength="3">
							<adsm:propertyMapping criteriaProperty="dataAtual" modelProperty="historicoFiliais.vigenteEm"/>
  				</adsm:lookup>	    			  	    			    								  
		</adsm:listbox>		
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>		
	</adsm:form>			
</adsm:window>
<script>
	function myOnPageLoadCallBack_cb(data, error){
	
		onPageLoad_cb(data,error);
		
		var dados = new Array();
         
        var sdo = createServiceDataObject("lms.workflow.manterCopiasEmailAction.findDataAtual",
                                          "setaDataAtual",
                                          dados);
        xmit({serviceDataObjects:[sdo]});
		
	}
	
	function setaDataAtual_cb(data,error){
	
		setElementValue("dataAtual",data._value);
		document.getElementById("dataAtual").masterLink = 'true';
	
	}
</script>
