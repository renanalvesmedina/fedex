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
			width="40%" 
		/>

		<adsm:range 
			label="vigencia" 
			width="35%"
		>
	        <adsm:textbox 
	        	dataType="JTDate" 
	        	property="dtVigencia"
	        />
        </adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="servicoTributo"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid idProperty="idServicoTributo" rows="13" property="servicoTributo" defaultOrder="dsServicoTributo, dtVigenciaInicial" unique="true" >
		<adsm:gridColumn title="descricao" property="dsServicoTributo" width="70%" />
        <adsm:gridColumn title="vigenciaInicial" dataType="JTDate" property="dtVigenciaInicial" width="15%" />
        <adsm:gridColumn title="vigenciaFinal" dataType="JTDate" property="dtVigenciaFinal" width="15%" />
    
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>