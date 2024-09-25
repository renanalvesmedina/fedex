<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.configuracoes.ramoAtividadeService">
	<adsm:form action="/configuracoes/manterRamosAtividade">
		<adsm:textbox dataType="text" size="90%" width="85%" property="dsRamoAtividade" label="descricao" maxLength="60"/>
		<adsm:lookup service="lms.tributos.codigoFiscalOperacaoService.findLookup"
			action="/tributos/manterCFOP"
			width="90%" 
			dataType="integer" 
			property="codigoFiscalOperacao" 
			idProperty="idCodigoFiscalOperacao"
			criteriaProperty="cdCfop" 
			exactMatch="false"
			label="cfop" size="10" maxLength="5">
			<adsm:propertyMapping modelProperty="dsCfop" formProperty="codigoFiscalOperacao.dsCfop"/>
			<adsm:textbox dataType="text" property="codigoFiscalOperacao.dsCfop" style="width:376px" maxLength="50" disabled="true"/>
		</adsm:lookup>	
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS"/>	        
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="ramoAtividade"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="ramoAtividade" idProperty="idRamoAtividade" 
				gridHeight="200" defaultOrder="dsRamoAtividade" 
				rows="12"
				unique="true">
		<adsm:gridColumn title="descricao" property="dsRamoAtividade" width="87%" />
		<adsm:gridColumn title="cfop" property="codigoFiscalOperacao.cdCfop" width="6%" align="right"/>
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="7%" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
