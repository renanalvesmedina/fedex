<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.workflow.manterCopiasEmailAction">

	<adsm:form action="/workflow/manterCopiasEmail"> 						
					
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
		             required="false"					 		             
		             action="/workflow/manterEventos"
		             minLengthForAutoPopUpSearch="3"
		             labelWidth="15%" >		             
			<adsm:propertyMapping relatedProperty="tipoEvento.dsTipoEvento" modelProperty="tipoEvento.dsTipoEvento" />
			<adsm:propertyMapping criteriaProperty="tipoEvento.dsTipoEvento" modelProperty="tipoEvento.dsTipoEvento" />
			<adsm:textbox dataType="text" property="tipoEvento.dsTipoEvento" size="60" maxLength="60" disabled="true"/>
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
					 service="lms.workflow.manterIntegrantesComiteAction.findLookupUsuarioFuncionario"
					 action="/seguranca/consultarUsuarioLMS">
			<adsm:propertyMapping relatedProperty="usuario.nmUsuario" modelProperty="nmUsuario" />
			<adsm:propertyMapping criteriaProperty="usuario.tpCategoriaUsuario" modelProperty="tpCategoriaUsuario"/>
			<adsm:textbox dataType="text" property="usuario.nmUsuario" disabled="true" serializable="true" maxLength="60" size="40"/>
		</adsm:lookup>	        
                						
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" width="35%"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="emailEventoUsuario"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid 	property="emailEventoUsuario" 
				idProperty="idEmailEventoUsuario" 
				unique="true" rows="12">
		<adsm:gridColumn title="evento" property="codigoDescricao" width="50%" />
		<adsm:gridColumn title="usuario" property="codigoDescricaoUsuario" width="40%" />		
		<adsm:gridColumn title="situacao" property="tpSituacao" width="10%" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>