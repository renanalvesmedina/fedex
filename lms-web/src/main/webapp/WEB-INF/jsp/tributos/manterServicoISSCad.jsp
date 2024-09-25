<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	service="lms.tributos.manterServicoISSAction"
>
	<adsm:form 
		action="/tributos/manterServicoISS"
		idProperty="idServicoTributo"
	>
		<adsm:textbox 
			dataType="text" 
			property="dsServicoTributo" 
			label="descricao" 
			size="50" 
			maxLength="60" 
			width="85%" 
			required="true"
		/>

        <adsm:combobox 
	        property="servicoOficialTributo.idServicoOficialTributo" 
	        optionLabelProperty="nrServicoTributoDsServicoTributo" 
	        optionProperty="idServicoOficialTributo" 
	        label="servicoOficial" 
	        service="lms.tributos.manterServicoISSAction.findServicoOficialTributo" 
	        required="true" 
	        boxWidth="630" 
	        width="85%"
	        onlyActiveValues="true"
        />

		<adsm:range 
			label="vigencia" 
			width="35%"
		>
	        <adsm:textbox 
	        	dataType="JTDate" 
	        	property="dtVigenciaInicial"
	        	smallerThan="dtVigenciaFinal"
	        	required="true"
	        />
	    	<adsm:textbox 
	    		biggerThan="dtVigenciaInicial"
	    		dataType="JTDate" 
	    		property="dtVigenciaFinal"
	    	/>
        </adsm:range>


		<adsm:textarea 
			label="observacao" 
			width="85%" 
			columns="60" 
			rows="3" 
			maxLength="500" 
			property="obServicoTributo" 
		/>
		
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>