<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.anexoDoctoServicoService">
	<adsm:form action="/expedicao/manterTiposAnexoCRT" idProperty="idAnexoDoctoServico">
		<adsm:textbox 
						dataType	="text" 
						label		="tipoAnexo" 
						maxLength	="60" 
						property	="dsAnexoDoctoServico" 
						required	="true" 
						size		="60" 
						width		="55%"
		/>
		<adsm:combobox 	
						domain		="DM_STATUS" 
						label		="situacao" 
						property	="tpSituacao" 
						required	="true"
						width		="15%"
		/>
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>