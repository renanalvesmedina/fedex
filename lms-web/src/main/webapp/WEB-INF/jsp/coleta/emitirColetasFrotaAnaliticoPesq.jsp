<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/coleta/emitirColetasFrotaAnalitico">
		<adsm:lookup property="filial.id" label="filial" action="/municipios/manterFiliais" cmd="list" service="" dataType="text" size="3" maxLength="3" width="85%" >
			<adsm:propertyMapping modelProperty="filialm.id" formProperty="nomeFilial"/>
			<adsm:textbox dataType="text" property="nomeFilial" size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>
		<adsm:textbox property="frota" label="frota" dataType="integer" size="7%" width="85%" maxLength="5" />
		<adsm:range label="periodoColetas" width="85%" >
			<adsm:textbox dataType="JTDateTimeZone" size="10" maxLength="10" property="dataInicial" picker="true" />
			<adsm:textbox dataType="JTDateTimeZone" size="10" maxLength="10" property="dataFinal" picker="true"/>
		</adsm:range>
		<adsm:buttonBar>
			<adsm:reportViewerButton service="coleta/emitirColetasFrotaAnalitico.jasper"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>