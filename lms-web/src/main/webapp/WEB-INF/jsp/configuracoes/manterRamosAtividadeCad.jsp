<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.configuracoes.ramoAtividadeService">
	<adsm:form action="/configuracoes/manterRamosAtividade" idProperty="idRamoAtividade">
		<adsm:hidden property="codigoFiscalOperacao.tpSituacao" value="A"/>
		<adsm:textbox dataType="text" size="90%" width="85%" property="dsRamoAtividade" label="descricao" maxLength="60" required="true" />
		<adsm:lookup service="lms.tributos.codigoFiscalOperacaoService.findLookup"
			action="/tributos/manterCFOP"
			width="90%" 
			dataType="integer" 
			property="codigoFiscalOperacao" 
			idProperty="idCodigoFiscalOperacao"
			criteriaProperty="cdCfop" 
			exactMatch="false"
			label="cfop" size="10" maxLength="5" required="true">
			<adsm:propertyMapping modelProperty="dsCfop" formProperty="codigoFiscalOperacao.dsCfop"/>
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="codigoFiscalOperacao.tpSituacao"/>			
			<adsm:textbox dataType="text" property="codigoFiscalOperacao.dsCfop" style="width:376px" maxLength="50" disabled="true"/>
		</adsm:lookup>	
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" required="true"/>		
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>