<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.coleta.executarRetornarColetasEntregasAction" >

	<adsm:grid property="entregasRealizar" idProperty="idPedidoColeta" 
			   selectionMode="none" autoSearch="false"
			   onRowClick="entregasRealizar_OnClick"
			   gridHeight="210" scrollBars="horizontal" unique="true" 
			   service="lms.coleta.executarRetornarColetasEntregasAction.findPaginatedColetasVeiculosEntregasRealizar"
			   rowCountService="lms.coleta.executarRetornarColetasEntregasAction.getRowCountColetasVeiculosEntregasRealizar">
		<adsm:gridColumn title="documentoServico" 		property="doctoServico.tpDocumentoServico" isDomain="true" width="40" />
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="" 				property="doctoServico.filialByIdFilialOrigem.sgFilial" width="40" />
			<adsm:gridColumn title="" 				property="nrDoctoServico" width="70" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="cliente" 		property="doctoServico.clienteByIdClienteDestinatario.pessoa.nrIdentificacaoFormatado" width="120" align="right" />
		<adsm:gridColumn title="" 				property="doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa" width="250" />
		<adsm:gridColumn title="endereco" 		property="doctoServico.dsEnderecoEntregaReal" width="280" />
		<adsm:gridColumn title="volumes" 		property="qtVolumes" width="80" align="right"/>
		<adsm:gridColumn title="peso" 			property="psDoctoServico" width="100" align="right" unit="kg" dataType="decimal" mask="###,###,###,##0.000" />
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="valor" 		property="doctoServico.moeda.sgMoeda" width="30" />
			<adsm:gridColumn title="" 			property="doctoServico.moeda.dsSimbolo" width="30" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="" 				property="vlTotalDocServico" dataType="currency" width="90" align="right" />
		<adsm:gridColumn title="situacao" 		property="situacaoDoctoServico" width="100" />
		<adsm:gridColumn title="veiculo" 		property="dadosVeiculo" image="/images/popup.gif" openPopup="true" link="/coleta/programacaoColetasVeiculosDadosVeiculo.do?cmd=main" popupDimension="790,260" width="100" align="center" linkIdProperty="idControleCarga"/>
		<adsm:gridColumn title="equipe" 		property="dadosEquipe" image="/images/popup.gif" openPopup="true" link="/coleta/programacaoColetasVeiculosDadosEquipe.do?cmd=main" popupDimension="790,520" width="100" align="center" linkIdProperty="idControleCarga"/>

		<adsm:buttonBar> 
			<adsm:button caption="executarEntregas" id="botaoExecutarEntregas" onclick="exibeExecutarColetas();" />
		</adsm:buttonBar>
	</adsm:grid>

	<adsm:form action="/coleta/executarRetornarColetasEntregas" >
		<adsm:hidden property="filial.idFilial"/>
		<adsm:hidden property="filial.sgFilial"/>
		<adsm:hidden property="filial.pessoa.nmFantasia"/>
		<adsm:hidden property="meioTransporteByIdTransportado.idMeioTransporte"/>
		<adsm:hidden property="meioTransporteByIdTransportado.nrFrota"/>
		<adsm:hidden property="meioTransporteByIdTransportado.nrIdentificador"/>
		<adsm:hidden property="controleCarga.nrControle"/>
		<adsm:hidden property="controleCarga.sgFilial"/>
		<adsm:hidden property="idControleCarga"/>
		<adsm:hidden property="origem" value="executarRetornarColetasEntregas"/>
	</adsm:form>	

</adsm:window>

<script>
function initWindow(eventObj) {
	if (eventObj.name == "tab_click") {
	    var tabDet = getTabGroup(this.document).parentTab.tabGroup.getTab("conteudo");
	    var idControleCarga = tabDet.getFormProperty("controleCarga.idControleCarga");

	    setElementValue("filial.idFilial", tabDet.getFormProperty("filialUsuario.idFilial"));
	    setElementValue("filial.sgFilial", tabDet.getFormProperty("filialUsuario.sgFilial"));
	    setElementValue("filial.pessoa.nmFantasia", tabDet.getFormProperty("filialUsuario.pessoa.nmFantasia"));
	    setElementValue("meioTransporteByIdTransportado.idMeioTransporte", tabDet.getFormProperty("meioTransporte.idMeioTransporte"));
	    setElementValue("meioTransporteByIdTransportado.nrFrota", tabDet.getFormProperty("meioTransporte2.nrFrota"));
	    setElementValue("meioTransporteByIdTransportado.nrIdentificador", tabDet.getFormProperty("meioTransporte.nrIdentificador"));
	    
	    setElementValue("controleCarga.nrControle", tabDet.getFormProperty("controleCarga.nrControleCarga"));
	    setElementValue("controleCarga.sgFilial", tabDet.getFormProperty("controleCarga.filialByIdFilialOrigem.sgFilial"));
	    setElementValue("idControleCarga", tabDet.getFormProperty("controleCarga.idControleCarga"));

		if (idControleCarga != undefined && idControleCarga != "")
			povoaGrid(idControleCarga);
	}
}

function povoaGrid(idControleCarga) {
      var filtro = new Array();
      setNestedBeanPropertyValue(filtro, "idControleCarga", idControleCarga);
      entregasRealizarGridDef.executeSearch(filtro);
      return false;
}

function limpaGrid() {
	entregasRealizarGridDef.resetGrid();
}

function entregasRealizar_OnClick(id) {
	return false;
}

function exibeExecutarColetas() {
	var parametros = 
		'&filial.idFilial=' + getElementValue("filial.idFilial") +
		'&filial.sgFilial=' + getElementValue("filial.sgFilial") +
		'&filial.pessoa.nmFantasia=' + getElementValue("filial.pessoa.nmFantasia") +
		'&meioTransporte2.idMeioTransporte=' + getElementValue("meioTransporteByIdTransportado.idMeioTransporte") +
		'&meioTransporte2.nrFrota=' + getElementValue("meioTransporteByIdTransportado.nrFrota") +
		'&meioTransporte.idMeioTransporte=' + getElementValue("meioTransporteByIdTransportado.idMeioTransporte") +
		'&meioTransporte.nrIdentificador=' + getElementValue("meioTransporteByIdTransportado.nrIdentificador") +
		
		'&controleCarga.sgFilial=' + getElementValue("controleCarga.sgFilial") +
		'&controleCarga.nrControle=' + getElementValue("controleCarga.nrControle") +
		'&idControleCarga=' + getElementValue("idControleCarga") +
		
		'&origem=' + getElementValue("origem");

	parent.parent.parent.redirectPage("entrega/registrarBaixaEntregasOnTime.do?cmd=main" + parametros);
}
</script>