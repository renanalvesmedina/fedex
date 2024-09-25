<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="consultarHistoricoQuestionamentoFaturas" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="consulta" id="pesq" src="/questionamentoFaturas/consultarHistoricoQuestionamentoFaturas.do" cmd="list"/>
		</adsm:tabGroup>
</adsm:window>