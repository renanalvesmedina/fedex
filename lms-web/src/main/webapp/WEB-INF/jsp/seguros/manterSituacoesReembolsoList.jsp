<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguros.manterSituacoesReembolsoAction">
	<adsm:form action="/seguros/manterSituacoesReembolso" idProperty="idSituacaoReembolso">
		
		<adsm:textbox dataType="text" property="dsSituacaoReembolso" label="descricao" labelwidth="8%" size="120" maxLength="20" width="85%" required="true"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="situacoesReembolso"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid property="situacoesReembolso" idProperty="idSituacaoReembolso" 
			   rows="13" defaultOrder="dsSituacaoReembolso:asc" selectionMode="check" gridHeight="200" unique="true">
		<adsm:gridColumn title="descricao" property="dsSituacaoReembolso" width="80%"/>
		<adsm:gridColumn title="status" property="blStatus" isDomain="true" width="20%"/>
		
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	
	</adsm:grid>
</adsm:window>