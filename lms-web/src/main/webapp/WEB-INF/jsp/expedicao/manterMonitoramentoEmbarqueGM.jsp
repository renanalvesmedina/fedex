<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterMonitoramentoEmbarqueGM" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/expedicao/manterMonitoramentoEmbarqueGM" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/expedicao/manterMonitoramentoEmbarqueGM" cmd="cad"/>	
	</adsm:tabGroup>
</adsm:window>
<script type="text/javascript">
</script>