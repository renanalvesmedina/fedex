<%-- @ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" import="org.apache.commons.beanutils.*" --%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.carregamento.motivoCancelamentoCcService" >
	<adsm:form action="/carregamento/manterMotivosCancelamentoControleCargas" idProperty="idMotivoCancelamentoCc" >
		<adsm:textbox dataType="text" property="dsMotivoCancelamentoCc" label="descricao" maxLength="60" size="60" width="85%" />
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="85%" renderOptions="true" />
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="motivos"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="motivos" idProperty="idMotivoCancelamentoCc" defaultOrder="dsMotivoCancelamentoCc:asc" selectionMode="check" gridHeight="200" unique="true" rows="13">
		<adsm:gridColumn property="dsMotivoCancelamentoCc" title="descricao" width="90%" />
		<adsm:gridColumn property="tpSituacao" title="situacao" isDomain="true" width="10%" />
		<adsm:buttonBar> 
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
