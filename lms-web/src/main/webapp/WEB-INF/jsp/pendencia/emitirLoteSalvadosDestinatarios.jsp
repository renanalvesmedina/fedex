<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window title="destinatarios">

	<adsm:form action="/pendencia/emitirLoteSalvados" height="82">

		<adsm:section caption="destinatarios" width="90%"/>
		
		<adsm:lookup property="cliente.id" criteriaProperty="cliente" dataType="text" label="cliente" action="/vendas/manterDadosIdentificacao" cmd="list" service="" size="18" maxLength="18" labelWidth="20%" width="80%" disabled="false" required="true">
			<adsm:propertyMapping modelProperty="cliente.id" formProperty="nomeCliente"/>
			<adsm:textbox property="nomeCliente" dataType="text" size="50" disabled="false"/>
		</adsm:lookup>

		<adsm:textbox label="setor" property="setor" dataType="text" labelWidth="20%" width="80%" size="50" required="true"/>
		<adsm:textbox label="email" property="email" dataType="text" labelWidth="20%" width="80%" size="50"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="adicionar"/>
		</adsm:buttonBar>

	</adsm:form>

	<adsm:grid paramProperty="id" gridHeight="280" >
		<adsm:gridColumn property="cliente" title="cliente"/>
		<adsm:gridColumn property="setor" title="setor"/>
		<adsm:gridColumn property="email" title="email"/>

		<adsm:buttonBar>
			<adsm:button caption="enviarPorEmail"/>
			<adsm:reportViewerButton caption="emitir" service="pendencia/emitirLoteSalvados.jasper"/>
		</adsm:buttonBar>
	</adsm:grid>



</adsm:window>