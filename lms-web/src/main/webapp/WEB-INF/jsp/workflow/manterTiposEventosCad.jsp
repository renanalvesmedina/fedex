<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	service="lms.workflow.manterTiposEventosAction"
>
	<adsm:form 
		action="/workflow/manterTiposEventos"
		idProperty="idTipoEvento"
	>
		<adsm:textbox 
			dataType="text" 
			size="80%" 
			width="60%" 
			property="dsTipoEvento" 
			label="descricao" 
			maxLength="60" 
			required="true"
		/>

		<adsm:textbox 
			dataType="integer" 
			width="10%" 
			property="nrTipoEvento" 
			label="numero" 
			maxLength="4" 
			minValue="1"
			size="3" 
			required="true"
		/>

        <adsm:combobox 
	        property="tpSituacao" 
	        label="situacao" 
			domain="DM_STATUS"
			required="true"
        />

        <adsm:buttonBar>
        	<adsm:button caption="eventos" action="/workflow/manterEventos" cmd="main">
        		<adsm:linkProperty src="dsTipoEvento" target="tipoEvento.dsTipoEvento"/>
        		<adsm:linkProperty src="nrTipoEvento" target="tipoEvento.nrTipoEvento"/>
        		<adsm:linkProperty src="idTipoEvento" target="tipoEvento.idTipoEvento"/>
        	</adsm:button>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>