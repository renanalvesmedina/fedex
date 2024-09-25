<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.rnc.acaoCorretivaService" >
	<adsm:form action="/rnc/manterAcoesCorretivas" idProperty="idAcaoCorretiva">
		<adsm:textbox dataType="text" property="dsAcaoCorretiva" label="descricao" size="60" maxLength="60" width="85%" />
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="85%" required="false" renderOptions="true"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="acoes"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="acoes" idProperty="idAcaoCorretiva" defaultOrder="dsAcaoCorretiva:asc" selectionMode="check" gridHeight="200" unique="true" rows="13">
		<adsm:gridColumn property="dsAcaoCorretiva" title="descricao" width="90%" />
		<adsm:gridColumn property="tpSituacao" title="situacao" isDomain="true" width="10%" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>