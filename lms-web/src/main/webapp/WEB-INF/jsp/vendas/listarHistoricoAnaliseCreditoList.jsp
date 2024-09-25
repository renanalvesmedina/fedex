<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.vendas.listarHistoricoAnaliseCreditoAction" onPageLoadCallBack="historico">
	<adsm:section caption="historicoAnaliseCredito"/>

	<adsm:form action="/vendas/listarHistoricoAnaliseCredito">
		<adsm:hidden property="analiseCreditoCliente.idAnaliseCreditoCliente"/>

		<adsm:label key="branco" width="2%"/>
		<!-- Lookup de cliente -->
		<adsm:lookup 
			action="/vendas/manterDadosIdentificacao" 
			dataType="text"
			criteriaProperty="pessoa.nrIdentificacao" 
			exactMatch="true" 
			idProperty="idCliente" 
			property="cliente" 
			label="cliente" 
			size="20"
			maxLength="20"
			width="91%" 
			labelWidth="7%"
			serializable="false"
			disabled="true">

			<adsm:textbox 
				dataType="text" 
				serializable="false"
				property="cliente.pessoa.nmPessoa" 
				size="30"
				disabled="true"/>
		</adsm:lookup>
	</adsm:form>

	<adsm:grid
		selectionMode="none"
		idProperty="idHistoricoAnaliseCredito"
		property="historicoAnaliseCredito"
		onDataLoadCallBack="onDataLoadCallBack"
		gridHeight="200"
		unique="true"
		rows="9"
		onRowClick="rowClick">
		<adsm:gridColumn title="descricao" property="tpEvento" width="25%" isDomain="true"/>
		<adsm:gridColumn title="usuario" property="usuario.nmUsuario" width="15%"/>
		<adsm:gridColumn title="dataHora" property="dhEvento" width="15%" dataType="JTDateTimeZone"/>
		<adsm:gridColumn title="observacao" property="obEvento" width="35%"/>
		<adsm:gridColumn
			title="pdfSerasa"
			property="serasaCliente"
			width="10%"
			align="center"
			image="/images/tratativas.gif"
			imageLabel="visualizar"
			link="javascript:openPdfSerasa(); event.cancelBubble=true;" />
		<adsm:editColumn
			property="serasaCliente.idSerasaCliente"
			field="hidden"
			dataType="text"
			title=""
			width="1%"/>
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
		findButtonScript('historicoAnaliseCredito', document.forms[0]);
	}

	function onDataLoadCallBack_cb(data, error) {
		if(data.list != undefined) {
			for (rowIndex=0; rowIndex < data.list.length; rowIndex++) {
				if(data.list[rowIndex].serasaCliente == null) {
					document.getElementById("historicoAnaliseCredito:"+rowIndex+".serasaCliente_href").style.visibility = 'hidden';
				}
		    }
		}
	}

	function openPdfSerasa(){
		var rowIndex = historicoAnaliseCreditoGridDef.navigate.currentRow;
		var idSerasaCliente = historicoAnaliseCreditoGridDef.getCellObject(rowIndex,"serasaCliente.idSerasaCliente");
		downloadFile("IM_PDF_SERASA", "SERASA_CLIENTE", "ID_SERASA_CLIENTE", idSerasaCliente.value);
	}
</script>