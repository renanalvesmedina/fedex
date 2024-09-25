<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tabelaprecos.unidadeMedidaService">
	<adsm:form action="/tabelaPrecos/manterUnidadesMedida" >
		<adsm:textbox 	
						dataType		="text" 
						property		="dsUnidadeMedida" 
						label			="unidadeMedida" 
						maxLength		="60" 
						size			="60" 
						width			="50%" 
						labelWidth		="20%"
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
		/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="unidadeMedida"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid selectionMode="check" idProperty="idUnidadeMedida" property="unidadeMedida" gridHeight="200" unique="true" defaultOrder="dsUnidadeMedida:asc" rows="13">
		<adsm:gridColumn title="unidadeMedida" property="dsUnidadeMedida" width="60%" />
		<adsm:gridColumn title="sigla" property="sgUnidadeMedida" width="20%" />
		<adsm:gridColumn title="situacao" property="tpSituacao" width="20%" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>