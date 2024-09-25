<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window
		service="lms.vendas.manterPropostasClienteProcAction">

	<adsm:grid
			property="historicoEfetivacao"
			idProperty="idHistoricoEfetivacao"
			selectionMode="none"
			unique="true"
			rows="16"
			autoSearch="false"
			defaultOrder="dhSolicitacao"
			onRowClick="historicoEfetivacaoOnRowClick"
			service="lms.vendas.manterPropostasClienteProcAction.findHistoricoEfetivacaoList"
			rowCountService="lms.vendas.manterPropostasClienteProcAction.findHistoricoEfetivacaoRowCount">

		<adsm:gridColumn
				title="dataSolicitacao"
				property="dhSolicitacao"
				dataType="JTDateTimeZone" />
		<adsm:gridColumn
				title="usuarioSolicitante"
				property="usuarioSolicitacao_nmUsuario" />
		<adsm:gridColumn
				title="dataReprovacao"
				property="dhReprovacao"
				dataType="JTDateTimeZone" />
		<adsm:gridColumn
				title="usuarioReprovador"
				property="usuarioReprovador_nmUsuario" />
		<adsm:gridColumn
				title="motivo"
				property="motivoReprovacao_dsMotivo" />
		<adsm:gridColumn
				title="descricao"
				property="dsMotivo" />
	</adsm:grid>
	<adsm:buttonBar />

</adsm:window>
<script type="text/javascript">

	function initWindow(event) {
		updateForm();
	}

	function updateForm() {
		var tabCad = getTabGroup(document).getTab("cad");
		var idSimulacao = tabCad.getFormProperty("simulacao.idSimulacao");
		var data = new Object();
		setNestedBeanPropertyValue(data, "simulacao.idSimulacao", idSimulacao);
		historicoEfetivacaoGridDef.executeSearch(data);
	}

	function removeHistoricoEfetivacao() {
		historicoEfetivacaoGridDef.removeByIds(
				"lms.vendas.manterPropostasClienteProcAction.removeByIdHistoricoEfetivacao",
				"removeHistoricoEfetivacao");
	}


	function historicoEfetivacaoOnRowClick() {
		return false;
	}

</script>
