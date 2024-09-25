<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	service="lms.workflow.manterTiposEventosAction"
>
	<adsm:form 
		action="/workflow/manterTiposEventos"
		idProperty="idTipoEvento"
	>
	
		<adsm:hidden property="eventoWorkflow.tpSituacao" serializable="true"/>	
		
		<adsm:textbox 
			dataType="text" 
			size="80%" 
			width="60%" 
			property="dsTipoEvento" 
			label="descricao" 
			maxLength="60" 
		/>
		
		<adsm:textbox 
			dataType="integer" 
			width="10%" 
			property="nrTipoEvento" 
			label="numero" 
			maxLength="4" 
			size="3" 
		/>
		
        <adsm:combobox 
	        property="tpSituacao" 
	        label="situacao" 
			domain="DM_STATUS"
        />

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="tipoEvento"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid idProperty="idTipoEvento" property="tipoEvento" unique="true" defaultOrder="nrTipoEvento" rows="13">
		<adsm:gridColumn title="descricao" property="dsTipoEvento" width="70%" />
		<adsm:gridColumn title="numero" property="nrTipoEvento" width="15%" dataType="integer" />
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="15%" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
