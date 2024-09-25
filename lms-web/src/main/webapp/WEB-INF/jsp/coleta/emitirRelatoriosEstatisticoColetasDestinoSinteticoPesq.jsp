<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/coleta/emitirRelatoriosEstatisticoColetasDestinoSintetico">

		<adsm:range label="periodo" width="85%" >
			<adsm:textbox dataType="JTDateTimeZone" size="10" maxLength="10" property="dataInicial" picker="true" />
			<adsm:textbox dataType="JTDateTimeZone" size="10" maxLength="10" property="dataFinal" picker="true"/>
		</adsm:range>

		<adsm:buttonBar>
			<adsm:reportViewerButton service="coleta/emitirRelatoriosEstatisticoColetasDestinoSintetico.jasper"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>