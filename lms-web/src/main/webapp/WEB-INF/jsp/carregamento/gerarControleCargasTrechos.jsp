<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.carregamento.gerarControleCargasAction" >

	<adsm:form action="/carregamento/gerarControleCargas">
		<adsm:masterLink idProperty="idControleCarga" showSaveAll="false">
			<adsm:masterLinkItem property="controleCargaConcatenado" label="controleCargas" itemWidth="100" />
			<adsm:hidden property="_rotaIdaVolta.idRotaIdaVolta" />
			<adsm:hidden property="_rota.idRota" />
			<adsm:hidden property="_dhPrevisaoSaida" />
		</adsm:masterLink>
	</adsm:form>

	<adsm:grid property="controleTrecho" idProperty="idControleTrecho" 
			   selectionMode="none" gridHeight="315" showPagging="false" scrollBars="vertical" 
			   service="lms.carregamento.gerarControleCargasAction.findPaginatedControleTrecho"
			   rowCountService="lms.carregamento.gerarControleCargasAction.getRowCountControleTrecho"
			   onRowClick="trechos_OnClick" autoSearch="false"
			   >
		<adsm:gridColumn title="origem" 				property="filialByIdFilialOrigem.sgFilial" width="19%" />
		<adsm:gridColumn title="destino" 				property="filialByIdFilialDestino.sgFilial" width="19%" align="left" />
		<adsm:gridColumn title="dataHoraSaida" 			property="dhPrevisaoSaida" dataType="JTDateTimeZone" width="19%" align="center"/>
		<adsm:gridColumn title="distancia" 				property="nrDistancia" width="13%" align="right" unit="km" />
		<adsm:gridColumn title="tempoViagem" 			property="hrTempoViagem" width="15%" align="center"/>
		<adsm:gridColumn title="tempoOperacaoPrevisto" 	property="hrTempoOperacao" width="15%" align="center"/>
		<adsm:buttonBar/> 
	</adsm:grid>
</adsm:window>

<script>
function trechos_OnClick(id) {
	return false;
}
</script>