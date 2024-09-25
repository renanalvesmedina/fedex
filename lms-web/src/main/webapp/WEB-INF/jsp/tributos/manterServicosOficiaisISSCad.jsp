<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	service="lms.tributos.manterServicosOficiaisISSAction"
>
	<adsm:form 
		action="/tributos/manterServicosOficiaisISS"
		idProperty="idServicoOficialTributo"
	>

		<adsm:textbox 
			property="nrServicoOficialTributo" 
			label="numero" 
			dataType="integer" 
			size="10" 
			maxLength="10" 
			width="85%" 
			required="true"
		/>
		
		<adsm:textarea 
			property="dsServicoOficialTributo" 
			label="descricao" 
			width="85%" 
			columns="60" 
			rows="3" 
			maxLength="500"
			required="true" 
		/>

        <adsm:combobox 
	        property="tpLocalDevido" 
	        label="localDevido" 
	        domain="DM_LOCAL_DEVIDO_ISS"
	        required="true"
        />


		<adsm:checkbox 
	        property="blRetencaoTomadorServico" 
	        label="retencaoTomadorServico" 
	        labelWidth="30%" 
	        width="20%"
		/>

        <adsm:combobox 
	        property="tpSituacao" 
	        label="situacao" 
			domain="DM_STATUS"
			required="true" 
        />

		<adsm:textarea
			label="observacao" 
			width="85%" 
			columns="60" 
			rows="3" 
			maxLength="500" 
			property="obServicoOficialTributo" 
		/>

		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>