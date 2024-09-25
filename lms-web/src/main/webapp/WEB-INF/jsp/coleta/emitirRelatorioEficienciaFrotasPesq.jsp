<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/coleta/emitirRelatorioEficienciaFrotas">

		<adsm:range label="periodo" required="true" width="85%">
			<adsm:textbox property="periodoIni" dataType="JTDate"/>
			<adsm:textbox property="periodoFim" dataType="JTDate"/>
		</adsm:range>

		<adsm:lookup property="filial.id" label="filial" action="/municipios/manterFiliais" service="" dataType="text" size="3" maxLength="3" width="85%" required="true">
			<adsm:propertyMapping modelProperty="filial.id" formProperty="nomeFilial"/>
			<adsm:textbox dataType="text" property="nomeFilialEmitente" size="50" disabled="true"/>
		</adsm:lookup>

		<adsm:textbox property="frota" label="frota" dataType="text" size="6%" width="8%" maxLength="8" />
		<adsm:lookup dataType="text" property="placa" size="10%" action="/contratacaoVeiculos/manterMeiosTransporte" service="" width="77%" />

		<adsm:buttonBar>
			<adsm:reportViewerButton service="coleta/emitirRelatorioEficienciaFrotas.jasper"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>