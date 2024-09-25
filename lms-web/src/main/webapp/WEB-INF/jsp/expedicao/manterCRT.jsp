<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterCRT" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab
			title="listagem"
			id="pesq"
			src="/expedicao/manterCRT"
			cmd="list"/>
		<adsm:tab
			title="detalhamento"
			id="cad"
			src="/expedicao/manterCRT"
			cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
<script type="text/javascript">
	var MODO_TELA = 'INCLUSAO';
</script>