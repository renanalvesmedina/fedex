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
		/>
		
		<adsm:textarea 
			property="dsServicoOficialTributo" 
			label="descricao" 
			width="85%" 
			columns="60" 
			rows="3" 
			maxLength="500" 
		/>

        <adsm:combobox 
	        property="tpLocalDevido" 
	        label="localDevido" 
	        domain="DM_LOCAL_DEVIDO_ISS"
        />

        <adsm:combobox 
	        property="blRetencaoTomadorServico" 
	        label="retencaoTomadorServico" 
	        domain="DM_SIM_NAO"
	        labelWidth="30%" 
	        width="20%"
		/>

        <adsm:combobox 
	        property="tpSituacao" 
	        label="situacao" 
			domain="DM_STATUS"
        />

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="servicoOficialTributo"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid selectionMode="check" idProperty="idServicoOficialTributo" property="servicoOficialTributo" gridHeight="200" unique="true" rows="9" defaultOrder="nrServicoOficialTributo">
		<adsm:gridColumn title="numero" property="nrServicoOficialTributo" dataType="integer" width="10%" />
		<adsm:gridColumn title="descricao" property="dsServicoOficialTributo" width="40%" />
		<adsm:gridColumn title="localDevido" property="tpLocalDevido" width="20%" isDomain="true"/>
		<adsm:gridColumn title="retencaoTomadorServico" property="blRetencaoTomadorServico" renderMode="image-check" width="20%" />
        <adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="10%" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
