<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.vendas.manterHistoricoReajusteClienteAction" onPageLoadCallBack="historico">
	<adsm:grid
		selectionMode="none"
		title="historicoReajuste"
		idProperty="idHistoricoReajusteCliente"
		property="historicoReajusteCliente"
		gridHeight="280"
		unique="true"
		rows="9"
		onRowClick="rowClick">
		<adsm:gridColumn
			title="dataReajuste"
			property="dtReajuste"
			dataType="JTDate"
			width="20%"/>
		<adsm:gridColumn
			title="tabelaAntiga"
			property="tabelaPrecoAnteriorString"
			align="center"
			width="20%"/>
		<adsm:gridColumn
			title="tabelaNova"
			property="tabelaPrecoNovaString"
			align="center"
			width="20%"/>
		<adsm:gridColumn
			title="pcReajuste"
			property="pcReajuste"
			dataType="percent"
			align="center"
			width="20%"/>
		<adsm:gridColumn
			title="formaReajuste"
			property="tpFormaReajuste"
			isDomain="true"
			align="center"
			width="20%"/>
	</adsm:grid>
	<adsm:buttonBar>
		<adsm:button caption="fechar" onclick="javascript:window.close();" disabled="false"/>
	</adsm:buttonBar>
</adsm:window>
<script>
	function rowClick(){
		return false;
	}

	function historico_cb(){
		onPageLoad_cb();
		findHistoricos();
	}

	function findHistoricos(){
		var url = new URL(parent.location.href);
		if (url.parameters != undefined 
				&& url.parameters.idTabelaDivisaoCliente != undefined 
				&& url.parameters.idTabelaDivisaoCliente != '') {   
			historicoReajusteClienteGridDef.executeSearch({idTabelaDivisaoCliente: url.parameters.idTabelaDivisaoCliente});		
		}
	}
</script>