<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/coleta/emitirLiberacoesBloqueiosClientes">
		<adsm:lookup property="cliente.id" label="cliente" action="/vendas/manterDadosIdentificacao" cmd="list" service="" dataType="text" size="18" maxLength="3" width="85%" >
			<adsm:propertyMapping modelProperty="cliente.id" formProperty="nomeCliente"/>
			<adsm:textbox dataType="text" property="nomeCliente" size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>
		<adsm:range label="periodo" width="85%" >
			<adsm:textbox dataType="JTDateTimeZone" size="10" maxLength="10" property="dataInicial" picker="true" />
			<adsm:textbox dataType="JTDateTimeZone" size="10" maxLength="10" property="dataFinal" picker="true"/>
		</adsm:range>
		<adsm:buttonBar>
			<adsm:reportViewerButton service="coleta/emitirLiberacoesBloqueiosClientes.jasper"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>