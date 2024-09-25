<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.contasreceber.manterMotivosCancelamentoProrrogacaoVencimentoAction">

	<adsm:form action="/contasReceber/manterMotivosCancelamentoProrrogacaoVencimento">
	
		<adsm:textbox label="descricao" property="dsMotivoOcorrencia" dataType="text" size="60" maxLength="60" width="85%"/>
		
		<adsm:combobox label="tipo" property="tpMotivoOcorrencia" domain="DM_TIPO_MOTIVO_OCORRENCIA" width="35%"/>
		
		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS" width="35%"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="motivoOcorrencia"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid idProperty="idMotivoOcorrencia" property="motivoOcorrencia" gridHeight="200" rows="13">
		<adsm:gridColumn width="300" title="descricao" property="dsMotivoOcorrencia"/>
		<adsm:gridColumn width="200" title="tipo" property="tpMotivoOcorrencia" isDomain="true"/>
		<adsm:gridColumn width="200" title="situacao" property="tpSituacao" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>	
	
</adsm:window>
