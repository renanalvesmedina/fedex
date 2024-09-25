<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/carregamento/emitirReciboAdiantamento">
		<adsm:lookup dataType="text" property="controleCargas" label="controleCargas" action="/carregamento/manterControleCargas" cmd="list" service="" size="20" maxLength="20" width="84%" labelWidth="16%"/>
		<adsm:buttonBar>
			<adsm:reportViewerButton service="carregamento/emitirReciboAdiantamento.jasper"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>