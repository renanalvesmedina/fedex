<%-- @ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" import="org.apache.commons.beanutils.*" --%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.rnc.caracteristicaProdutoService">
	<adsm:form action="/rnc/manterCaracteristicasProdutoEmbalagem" idProperty="idCaracteristicaProduto">
		<adsm:textbox dataType="text" property="dsCaracteristicaProduto" label="descricao" maxLength="100" size="50" width="85%"/>
		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS" width="85%" renderOptions="true"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="caracteristicas"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="caracteristicas" idProperty="idCaracteristicaProduto" defaultOrder="dsCaracteristicaProduto:asc" rows="13">
		<adsm:gridColumn width="90%" title="descricao" property="dsCaracteristicaProduto"/>
		<adsm:gridColumn width="10%" title="situacao" property="tpSituacao" isDomain="true"/>
		<adsm:buttonBar> 
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
