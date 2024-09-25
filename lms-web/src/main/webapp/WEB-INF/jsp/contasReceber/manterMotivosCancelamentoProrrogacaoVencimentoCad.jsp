<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.contasreceber.manterMotivosCancelamentoProrrogacaoVencimentoAction">

	<adsm:form action="/contasReceber/manterMotivosCancelamentoProrrogacaoVencimento" idProperty="idMotivoOcorrencia">
	
		<adsm:textbox label="descricao" property="dsMotivoOcorrencia" dataType="text" size="60" maxLength="60" width="85%" required="true"/>
		
		<adsm:combobox onlyActiveValues="true" label="tipo" property="tpMotivoOcorrencia" domain="DM_TIPO_MOTIVO_OCORRENCIA" width="35%" required="true"/>
		
		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS" width="35%" required="true"/>
		
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
		
	</adsm:form>
	
</adsm:window>