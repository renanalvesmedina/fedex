<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/tabelaPrecos/relacaoClientesFretePercentual">

		<adsm:combobox property="regional" optionLabelProperty="value" optionProperty="0" service="" label="regional" width="30%" labelWidth="15%"/>

		<adsm:lookup property="filial.id" label="filial" action="/rcv/manterFiliais" service="" dataType="text" size="3" maxLength="3" width="38%" labelWidth="17%" >
			<adsm:propertyMapping modelProperty="filial.id" formProperty="nomeFilial"/>
			<adsm:textbox dataType="text" property="nomeFilial" size="35" maxLength="50" disabled="true"/>
		</adsm:lookup>

		<adsm:textbox dataType="date" property="dataReferencia" label="dataReferencia" width="30%" labelWidth="15%"/>

		<adsm:buttonBar>
			<adsm:reportViewerButton reportName="tabelaPrecos/relacaoClientesFretePercentual.jasper" />
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>