<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.coleta.executarRetornarColetasEntregasAction" >
	<adsm:grid property="entregasParciais" idProperty="idPedidoColeta" 
			   selectionMode="none" autoSearch="false"  
			   onRowClick="cancelaClick"
			   gridHeight="210" scrollBars="horizontal" unique="true" 
			   service="lms.coleta.executarRetornarColetasEntregasAction.findPaginatedColetasVeiculosEntregasParciaisPendentes"
			   rowCountService="lms.coleta.executarRetornarColetasEntregasAction.getRowCountColetasVeiculosEntregasParciaisPendentes">
            <adsm:gridColumn title="documentoServico" property="tpDocumentoServico" width="45" /> 
            <adsm:gridColumnGroup separatorType="DOCTO_SERVICO"> 
                  <adsm:gridColumn title=""                      property="sgFilialOrigem" width="35" />
                  <adsm:gridColumn title=""                      property="nrDoctoServico" width="60" align="right" dataType="integer" mask="00000000" />
            </adsm:gridColumnGroup>
            <adsm:gridColumn title="volume"          property="nrVolume" width="55" align="center"/>
            <adsm:gridColumn title="dpe"             property="dtPrevEntrega" dataType="JTDate" width="80" align="center" />
            <adsm:gridColumn title="cliente"         property="nrIdentificacaoFormatado" width="120" align="right" />
            <adsm:gridColumn title=""                      property="nmPessoaDestinatario" width="250" />
            <adsm:gridColumn title="endereco"        property="dsEnderecoEntregaReal" width="280" />          
            <adsm:gridColumn title="peso"                  property="psDoctoServico" width="100" align="right" unit="kg" dataType="decimal" mask="###,###,###,##0.000" />
            <adsm:gridColumn title="valor"                 property="siglaSimbolo" width="60" />
            <adsm:gridColumn title=""                      property="vlTotalDocServico" dataType="currency" width="100" align="right" />
            <adsm:gridColumn title="situacao"        property="situacaoDoctoServico" width="100" />
            <adsm:gridColumn title="veiculo"         property="dadosVeiculo" image="/images/popup.gif" openPopup="true" link="/coleta/programacaoColetasVeiculosDadosVeiculo.do?cmd=main" popupDimension="790,260" width="100" align="center" linkIdProperty="idControleCarga"/>
            <adsm:gridColumn title="equipe"          property="dadosEquipe" image="/images/popup.gif" openPopup="true" link="/coleta/programacaoColetasVeiculosDadosEquipe.do?cmd=main" popupDimension="790,520" width="100" align="center" linkIdProperty="idControleCarga"/>

		<adsm:buttonBar> 
			<adsm:button caption="executarEntregas" id="botaoExecutarEntregas" onclick="exibeExecutarEntregasParciais();" />
		</adsm:buttonBar>
	</adsm:grid>

	<adsm:form action="/coleta/executarRetornarColetasEntregas" >
		<adsm:hidden property="filial.idFilial"/>
		<adsm:hidden property="filial.sgFilial"/>
		<adsm:hidden property="filial.pessoa.nmFantasia"/>
		<adsm:hidden property="meioTransporteByIdTransportado.idMeioTransporte"/>
		<adsm:hidden property="meioTransporteByIdTransportado.nrFrota"/>
		<adsm:hidden property="meioTransporteByIdTransportado.nrIdentificador"/>
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

		if (idControleCarga != undefined && idControleCarga != "")
			povoaGrid(idControleCarga);
	}
}

function povoaGrid(idControleCarga) {
      var filtro = new Array();
      setNestedBeanPropertyValue(filtro, "idControleCarga", idControleCarga);
      entregasParciaisGridDef.executeSearch(filtro);
      return false;
}

function limpaGrid() {
	entregasParciaisGridDef.resetGrid();
}

function cancelaClick(id) {
	return false;
}

function exibeExecutarEntregasParciais() {
	var parametros = 
		'&filial.idFilial=' + getElementValue("filial.idFilial") +
		'&filial.sgFilial=' + getElementValue("filial.sgFilial") +
		'&filial.pessoa.nmFantasia=' + getElementValue("filial.pessoa.nmFantasia") +
		'&meioTransporte2.idMeioTransporte=' + getElementValue("meioTransporteByIdTransportado.idMeioTransporte") +
		'&meioTransporte2.nrFrota=' + getElementValue("meioTransporteByIdTransportado.nrFrota") +
		'&meioTransporte.idMeioTransporte=' + getElementValue("meioTransporteByIdTransportado.idMeioTransporte") +
		'&meioTransporte.nrIdentificador=' + getElementValue("meioTransporteByIdTransportado.nrIdentificador") +
		'&origem=' + getElementValue("origem");

	parent.parent.parent.redirectPage("entrega/registrarBaixaEntregasOnTime.do?cmd=main" + parametros);
}
</script>