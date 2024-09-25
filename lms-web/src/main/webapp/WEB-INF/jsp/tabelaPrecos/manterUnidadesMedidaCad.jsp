<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tabelaprecos.unidadeMedidaService">
	<adsm:form action="/tabelaPrecos/manterUnidadesMedida" idProperty="idUnidadeMedida">
		<adsm:textbox 	
						dataType		="text" 
						property		="dsUnidadeMedida" 
						label			="unidadeMedida" 
						maxLength		="60" 
						size			="60" 
						width			="50%" 
						labelWidth		="20%"
						required		="true" 
		/>
		<adsm:textbox 
						dataType		="text" 
						property		="sgUnidadeMedida" 
						label			="sigla" 
						maxLength		="3" 
						size			="5"
						width			="15%" 
		/>
		<adsm:combobox 
						property		="tpSituacao" 
						label			="situacao" 
						domain			="DM_STATUS" 
						width			="80%" 
						labelWidth		="20%" 
						required="true"
		/>

		<adsm:buttonBar>
			<adsm:storeButton />
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
