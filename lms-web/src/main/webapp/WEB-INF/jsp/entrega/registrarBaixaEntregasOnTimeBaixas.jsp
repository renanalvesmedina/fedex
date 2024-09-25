<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<script type="text/javascript">
<!--
	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		var findPagginated = "findPaginatedConfirmation";
		var getRowCount = "getRowCountConfirmation";
		var action = getElementValue(dialogArguments.document.getElementById("actionBaixas"));
		DoctoServicoGridDef.service = action + "." + findPagginated;
		DoctoServicoGridDef.rowCountService = action + "." + getRowCount;
		DoctoServicoGridDef.executeSearch(buildFormBeanFromForm(dialogArguments.document.forms['Lazy']));
	}

	function rowClick() {
		return false;
	}
//-->
</script>
<adsm:window service="lms.entrega.registrarBaixaEntregasOnTimeAction" onPageLoadCallBack="pageLoad"> 
	<adsm:grid
		idProperty="idDoctoServico"
		property="DoctoServico"
		selectionMode="none"
		onRowClick="rowClick"
		rows="9"
		service="lms.entrega.registrarBaixaEntregasOnTimeAction.findPaginatedConfirmation"
		rowCountService="lms.entrega.registrarBaixaEntregasOnTimeAction.getRowCountConfirmation"
		scrollBars="horizontal"
	>
		<adsm:gridColumn title="documentoServico" property="tpDocumentoServico" width="55" isDomain="true"/>
		<adsm:gridColumnGroup customSeparator=" " >
			<adsm:gridColumn title="" property="sgFilialOrigemDoctoServico" width="50" />
			<adsm:gridColumn title="" property="nrDoctoServico" dataType="integer" mask="00000000" width="100"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumnGroup customSeparator=" - ">
			<adsm:gridColumn title="ocorrencia" property="cdOcorrenciaEntrega" width="80"/>
			<adsm:gridColumn title="" dataType="text" property="dsOcorrenciaEntrega" width="70"/> 
		</adsm:gridColumnGroup>
		<%--<adsm:gridColumn width="60" title="identificacao" property="tpIdentificacaoClienteDestinatario" isDomain="true" align="left"/>
		<adsm:gridColumn width="120" title="" property="nrIdentificacaoClienteDestinatario" align="right"/>--%>
		<adsm:gridColumn width="150" title="destinatario" property="nmClienteDestinatario" align="left"/>
		<adsm:gridColumn width="90" title="dpe" property="nrDpe" align="center" />
		<adsm:gridColumn width="500" title="endereco" property="dsEnderecoEntrega" align="left"/>
		<adsm:gridColumnGroup customSeparator=" " >
			<adsm:gridColumn title="manifestoEntrega" property="sgFilialManifestoEntrega" width="80"/>
			<adsm:gridColumn title="" dataType="integer" mask="00000000" property="nrManifestoEntrega" width="70" />
		</adsm:gridColumnGroup>
		<adsm:gridColumnGroup customSeparator=" " >
			<adsm:gridColumn title="manifestoViagem" property="sgFilialManifestoViagem" width="80"/>
			<adsm:gridColumn title="" dataType="integer" mask="00000000" property="nrManifestoViagem" width="70" />
		</adsm:gridColumnGroup>
		<%--<adsm:gridColumnGroup customSeparator=" " >
			<adsm:gridColumn title="controleCarga" property="sgFilialControleCarga" width="80"/>
			<adsm:gridColumn title="" dataType="integer" mask="00000000" property="nrControleCarga" width="70" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn width="150" title="recebedor" property="nmRecebedorControleCarga" dataType="text"/>--%>
		<adsm:gridColumn width="200" title="ciaAerea" property="ciaAerea" align="left"/>
		<adsm:gridColumn width="90" title="preAwb" property="idAwb" align="right"/>		
		<adsm:buttonBar>
			<adsm:button caption="fechar" onclick="self.close();" disabled="false"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
