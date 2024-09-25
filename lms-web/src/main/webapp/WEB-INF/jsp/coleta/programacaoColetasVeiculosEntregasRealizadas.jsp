<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.coleta.programacaoColetasVeiculosAction" >
	<adsm:form action="/coleta/programacaoColetasVeiculos">

		<adsm:hidden property="idControleCarga" serializable="false" />

		<adsm:textbox label="numeroColeta" property="manifestoColeta.pedidoColeta.filialByIdFilialResponsavel.sgFilial" dataType="text" 
			  		  size="3" width="35%" disabled="true" serializable="false" >
			<adsm:textbox property="manifestoColeta.pedidoColeta.nrColeta" dataType="text" size="10" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textbox label="meioTransporte" property="meioTransporte.nrFrota" dataType="text" width="35%" size="6" serializable="false" disabled="true" >
			<adsm:textbox dataType="text" property="meioTransporte.nrIdentificador" size="19" serializable="false" disabled="true" />
		</adsm:textbox>
	</adsm:form>

	<adsm:grid property="coletasVeiculosEntregasRealizar" idProperty="idPedidoColeta" 
			   selectionMode="none" autoSearch="false"
			   gridHeight="290" rows="14" scrollBars="horizontal" unique="true" 
			   service="lms.coleta.programacaoColetasVeiculosAction.findPaginatedColetasVeiculosEntregasRealizadas"
			   rowCountService="lms.coleta.programacaoColetasVeiculosAction.getRowCountColetasVeiculosEntregasRealizadas"
			   onRowClick="coletasVeiculosEntregasRealizar_OnClick" >
		<adsm:gridColumn title="documentoServico" property="tpDocumentoServico" width="45" /> 
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">	
			<adsm:gridColumn title="" 				property="sgFilialOrigem" width="35" />
			<adsm:gridColumn title=""				property="nrDoctoServico" width="60" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="dpe" 			property="dtPrevEntrega" dataType="JTDate" width="80" align="center" />
		<adsm:gridColumn title="cliente" 		property="nrIdentificacaoFormatado" width="120" align="right" />
		<adsm:gridColumn title="" 				property="nmPessoaDestinatario" width="250" />
		<adsm:gridColumn title="endereco" 		property="dsEnderecoEntregaReal" width="280" />
		<adsm:gridColumn title="volumes" 		property="qtVolumes" width="80" align="right"/>
		<adsm:gridColumn title="peso" 			property="psDoctoServico" width="100" align="right" unit="kg" dataType="decimal" mask="###,###,###,##0.000" />
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="valor" 		property="sgMoeda" width="30" />
			<adsm:gridColumn title="" 			property="dsSimbolo" width="30" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="" 				property="vlTotalDocServico" dataType="currency" width="100" align="right" />
		<adsm:gridColumn title="situacao" 		property="situacaoDoctoServico" width="100" />
		<adsm:gridColumn title="veiculo" 		property="dadosVeiculo" image="/images/popup.gif" openPopup="true" link="/coleta/programacaoColetasVeiculosDadosVeiculo.do?cmd=main" popupDimension="790,260" width="100" align="center" linkIdProperty="idControleCarga"/>
		<adsm:gridColumn title="equipe" 		property="dadosEquipe" image="/images/popup.gif" openPopup="true" link="/coleta/programacaoColetasVeiculosDadosEquipe.do?cmd=main" popupDimension="790,520" width="100" align="center" linkIdProperty="idControleCarga"/>
		<adsm:gridColumn title="baixa" 			property="dhEvento" dataType="JTDateTimeZone" width="130" align="center" />
		<adsm:buttonBar /> 
	</adsm:grid>
</adsm:window>

<script>
function initWindow(eventObj) {
	if (eventObj.name == "tab_click") {
		povoaDadosMaster();
		var idControleCarga = getElementValue('idControleCarga');
		if (idControleCarga != undefined && idControleCarga != "") {
			povoaGrid(idControleCarga);
		}
	}
}

function povoaDadosMaster() {
	var tabGroup = getTabGroup(this.document);
    var tabDetPesq = tabGroup.getTab("veiculo");
    var filialColeta = tabDetPesq.getFormProperty("filialByIdFilialResponsavel.sgFilial");
    var numeroColeta = tabDetPesq.getFormProperty("nrColeta");

    var tabDetCad = tabGroup.getTab("cad");
    var nrFrota = tabDetCad.getFormProperty("meioTransporteByIdTransportado.nrFrota");
    var nrIdentificador = tabDetCad.getFormProperty("meioTransporteByIdTransportado.nrIdentificador");
    var idControleCarga = tabDetCad.getFormProperty("idControleCarga");
    
    setElementValue("manifestoColeta.pedidoColeta.filialByIdFilialResponsavel.sgFilial" , filialColeta);
    setElementValue("manifestoColeta.pedidoColeta.nrColeta" , numeroColeta);
    setElementValue("meioTransporte.nrFrota" , nrFrota);
    setElementValue("meioTransporte.nrIdentificador" , nrIdentificador);
    setElementValue("idControleCarga" , idControleCarga);
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
</script>