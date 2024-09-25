<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.coleta.pedidoColetaService">
	<adsm:grid idProperty="" property="" selectionMode="check" rows="15" scrollBars="horizontal" gridHeight="335" showPagging="false">
		<adsm:gridColumn title="servico" property="servico.dsServico" width="220" />
		<adsm:gridColumn title="pais" property="pais.nmPais" width="145" />
		<adsm:gridColumn title="pesoMaximoPorVolume" property="psMaximoVolume" width="99" align="right" unit="kg" />
		<adsm:gridColumn title="produtoProibido" property="produtoProibido.dsProduto" width="400" />
	</adsm:grid>
	<adsm:buttonBar/>
</adsm:window>
