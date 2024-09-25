<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/coleta/emitirPosicaoAnualColetas">
		<adsm:textbox property="ano" label="ano" dataType="text" width="90%" labelWidth="10%" maxLength="4" size="5" required="true"/>
		<adsm:buttonBar>
			<adsm:reportViewerButton service="coleta/emitirPosicaoAnualColetas.jasper"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>