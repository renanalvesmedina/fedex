<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.coleta.programacaoColetasVeiculosAction" >

	<adsm:grid property="coletasVeiculosEntregasRealizar" idProperty="idPedidoColeta" 
			   selectionMode="none" autoSearch="false" rows="13"
			   gridHeight="260" scrollBars="horizontal" unique="true" 
			   service="lms.coleta.programacaoColetasVeiculosAction.findPaginatedColetasVeiculosEntregasParciaisRealizar"
			   rowCountService="lms.coleta.programacaoColetasVeiculosAction.getRowCountColetasVeiculosEntregasParciaisRealizar"
			   onRowClick="coletasVeiculosEntregasRealizar_OnClick" >		
		<adsm:gridColumn title="documentoServico" property="tpDocumentoServico" width="45" /> 
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">	
			<adsm:gridColumn title="" 				property="sgFilialOrigem" width="35" />
			<adsm:gridColumn title=""				property="nrDoctoServico" width="60" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="volume"         property="nrVolume" width="55" align="center"/>
		<adsm:gridColumn title="dpe" 			property="dtPrevEntrega" dataType="JTDate" width="80" align="center" />
		<adsm:gridColumn title="cliente" 		property="nrIdentificacaoFormatado" width="120" align="right" />
		<adsm:gridColumn title="" 				property="nmPessoaDestinatario" width="250" />
		<adsm:gridColumn title="endereco" 		property="dsEnderecoEntregaReal" width="280" />		
		<adsm:gridColumn title="peso" 			property="psDoctoServico" width="100" align="right" unit="kg" dataType="decimal" mask="###,###,###,##0.000" />
		<adsm:gridColumn title="valor" 			property="siglaSimbolo" width="60" />
		<adsm:gridColumn title="" 				property="vlTotalDocServico" dataType="currency" width="100" align="right" />
		<adsm:gridColumn title="situacao" 		property="situacaoDoctoServico" width="100" />
		<adsm:gridColumn title="veiculo" 		property="dadosVeiculo" image="/images/popup.gif" openPopup="true" link="/coleta/programacaoColetasVeiculosDadosVeiculo.do?cmd=main" popupDimension="790,260" width="100" align="center" linkIdProperty="idControleCarga"/>
		<adsm:gridColumn title="equipe" 		property="dadosEquipe" image="/images/popup.gif" openPopup="true" link="/coleta/programacaoColetasVeiculosDadosEquipe.do?cmd=main" popupDimension="790,520" width="100" align="center" linkIdProperty="idControleCarga"/>
		
		<adsm:buttonBar>
			<adsm:button caption="executarEntregas" id="botaoExecutarEntregas" onclick="exibeExecutarColetas();" />
		</adsm:buttonBar>
		
	</adsm:grid>
	
	<adsm:form action="/coleta/programacaoColetasVeiculosEntregasRealizar" >
		<adsm:hidden property="idControleCarga"/>
		<adsm:hidden property="filialByIdFilialOrigem.idFilial"/>
		<adsm:hidden property="filialByIdFilialOrigem.sgFilial"/>
		<adsm:hidden property="meioTransporteByIdTransportado.idMeioTransporte"/>
		<adsm:hidden property="meioTransporteByIdTransportado.nrFrota"/>
		<adsm:hidden property="meioTransporteByIdTransportado.nrIdentificador"/>
		<adsm:hidden property="origem" value="programacaoColetasVeiculos"/>
	</adsm:form>
	
</adsm:window>

<script>
function initWindow(eventObj) {
	if (eventObj.name == "tab_click") {
	    var tabDetCad = getTabGroup(this.document).parentTab.tabGroup.getTab("cad");

     	setElementValue("idControleCarga", tabDetCad.getFormProperty("idControleCarga"));
	    setElementValue("filialByIdFilialOrigem.idFilial", tabDetCad.getFormProperty("filialByIdFilialOrigem.idFilial"));
	    setElementValue("filialByIdFilialOrigem.sgFilial", tabDetCad.getFormProperty("filialByIdFilialOrigem.sgFilial"));
	    setElementValue("meioTransporteByIdTransportado.idMeioTransporte", tabDetCad.getFormProperty("meioTransporteByIdTransportado.idMeioTransporte"));
	    setElementValue("meioTransporteByIdTransportado.nrFrota", tabDetCad.getFormProperty("meioTransporteByIdTransportado.nrFrota"));
	    setElementValue("meioTransporteByIdTransportado.nrIdentificador", tabDetCad.getFormProperty("meioTransporteByIdTransportado.nrIdentificador"));

		if (getElementValue("idControleCarga") != "") {
			povoaGrid(getElementValue("idControleCarga"));
		}
	}
}

function povoaGrid(idControleCarga) {
      var filtro = new Array();
      setNestedBeanPropertyValue(filtro, "idControleCarga", idControleCarga);
      coletasVeiculosEntregasRealizarGridDef.executeSearch(filtro);
      return false;
}

function coletasVeiculosEntregasRealizar_OnClick(id) {
	return false;
}


function exibeExecutarColetas() {
	var parametros = 
		'&filial.idFilial=' + getElementValue("filialByIdFilialOrigem.idFilial") +
		'&filial.sgFilial=' + getElementValue("filialByIdFilialOrigem.sgFilial") +
		'&meioTransporte2.idMeioTransporte=' + getElementValue("meioTransporteByIdTransportado.idMeioTransporte") +
		'&meioTransporte2.nrFrota=' + getElementValue("meioTransporteByIdTransportado.nrFrota") +
		'&meioTransporte.idMeioTransporte=' + getElementValue("meioTransporteByIdTransportado.idMeioTransporte") +
		'&meioTransporte.nrIdentificador=' + getElementValue("meioTransporteByIdTransportado.nrIdentificador") +
		'&origem=' + getElementValue("origem");

	parent.parent.parent.redirectPage("entrega/registrarBaixaEntregasOnTime.do?cmd=main" + parametros);
	if (getElementValue("idControleCarga") != "") {
		povoaGrid(getElementValue("idControleCarga"));
	}
}
</script>