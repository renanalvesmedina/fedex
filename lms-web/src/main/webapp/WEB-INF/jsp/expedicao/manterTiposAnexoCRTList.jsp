<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.anexoDoctoServicoService">
	<adsm:form action="/expedicao/manterTiposAnexoCRT">
		<adsm:textbox 
						dataType	="text" 
						label		="tipoAnexo" 
						maxLength	="60" 
						property	="dsAnexoDoctoServico" 
						size		="60" 
						width		="55%"
		/>
		<adsm:combobox 	
						domain		="DM_STATUS" 
						label		="situacao" 
						property	="tpSituacao" 
						width		="15%"
		/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="tipoAnexoCRT"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idAnexoDoctoServico" property="tipoAnexoCRT" gridHeight="200" unique="true" defaultOrder="dsAnexoDoctoServico" rows="13">
		<adsm:gridColumn title="tipoAnexo" property="dsAnexoDoctoServico" width="85%" />
		<adsm:gridColumn title="situacao" property="tpSituacao" width="15%" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
