<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<script type="text/javascript">
var idControleCarga;

function carregaPagina() {
	onPageLoad();

	var u = new URL(parent.location.href);
	idReciboIndenizacao = u.parameters["idReciboIndenizacao"];
	carregaGrid();
}

function recarregaPagina() {
	onPageLoad();

	var u = new URL(parent.location.href);
	idReciboIndenizacao = u.parameters["idReciboIndenizacao"];
	carregaGrid();
}  	

</script>
<adsm:window service="lms.indenizacoes.manterReciboIndenizacaoAction" onPageLoad="carregaPagina">
	<adsm:grid idProperty="idReciboIndenizacao" property="reciboIndenizacao" selectionMode="none" 
			   gridHeight="330" rows="16" onRowClick="rowClickNone" 
			   service="lms.indenizacoes.manterReciboIndenizacaoAction.findLancamentosByReciboIndenizacaoId"
			   rowCountService="lms.indenizacoes.manterReciboIndenizacaoAction.getRowCountLancamentosByReciboIndenizacaoId"
			   defaultOrder="franquia_filial_.sgFilial, dtCompetencia:asc"
	   		   >
		<adsm:gridColumn title="franquia" property="franquia.filial.sgFilial" width="100"/>
		<adsm:gridColumn title="competenciaDesconto" property="dtCompetenciaAsMesAno" width="100" align="center"/>		
		<adsm:gridColumn title="valorBaseIndenizacao" property="nrValorIndenizado" width="100" unit="reais" align="right" dataType="currency"/>
		<adsm:gridColumn title="pcIndenizacao" property="nrPercentualIndenizacao" width="100" unit="percent" align="right" />
		<adsm:gridColumn title="valorIndenizado" property="nrValorIndenizacaoReal" width="100" unit="reais" align="right" dataType="currency"/>
		<adsm:gridColumn title="parcelas" property="nrParcelas" width="100" align="right"/>
		<adsm:gridColumn title="valorPorParcela" property="nrValorPorParcela" width="100" unit="reais" align="right" dataType="currency"/>
		<adsm:gridColumn title="situacaoAprovacao" property="tpSituacaoPendencia" width="100" isDomain="true"/>
	</adsm:grid>

	<adsm:buttonBar>
		<adsm:button caption="fechar" disabled="false" onclick="window.close()"/>
	</adsm:buttonBar>
	
</adsm:window>

<script type="text/javascript">
function carregaGrid() {
	var fb = buildFormBeanFromForm(this.document.forms[0], 'LIKE_END');
	var u = new URL(parent.location.href);
	fb['idReciboIndenizacao'] = idReciboIndenizacao;
	
	reciboIndenizacao_cb(fb);
}

function rowClickNone() {
	return false;
}
</script>