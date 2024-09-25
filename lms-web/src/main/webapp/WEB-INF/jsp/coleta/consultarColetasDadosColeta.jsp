<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	function carregaPagina() {
		setMasterLink(document, true);
		
		//Parametro passado da tela de manterAcoes (Workflow)...
		var idProcessoWorkflow = getIdProcessoWorkflow(); 
		if (idProcessoWorkflow==undefined) {
			povoaDados();
		} else {
			//Desabilita os botoes em caso de workflow...
			var data = new Object();
			data.idProcessoWorkflow = idProcessoWorkflow;
			var sdo = createServiceDataObject("lms.coleta.consultarColetasDadosColetaAction.findByIdProcessoWorkflow", "carregaDados_retorno_workflow", data);
			xmit({serviceDataObjects:[sdo]});
		}
	} 
	
	function getIdProcessoWorkflow() {
		var url = new URL(parent.location.href);
		return url.parameters["idProcessoWorkflow"];
	}

</script>
<adsm:window title="dadosColeta" service="lms.coleta.consultarColetasDadosColetaAction" onPageLoad="carregaPagina" >
	<adsm:form action="/coleta/consultarColetasDadosColeta" height="320" idProperty="idPedidoColeta" 
			   service="lms.coleta.consultarColetasDadosColetaAction.findById" onDataLoadCallBack="carregaDados_retorno" >

		<adsm:hidden property="idPedidoColeta" serializable="true" />
		<adsm:hidden property="manifestoColeta.controleCarga.idControleCarga" serializable="true" />

		<adsm:section caption="dadosColeta" />
		
		<adsm:textbox label="numero" property="sgFilialPedidoColeta" dataType="text" 
					  size="3" labelWidth="18%" width="37%" disabled="true" serializable="false" >
			<adsm:textbox property="nrColeta" dataType="integer" size="10" mask="00000000" maxLength="8" disabled="true" serializable="false" />
		</adsm:textbox>
		
		<adsm:textbox label="status" property="tpStatusColeta.description" dataType="text" size="20" serializable="false" width="30%" disabled="true" />
					  
		<adsm:textbox label="filialParaColeta" property="filialByIdFilialResponsavel.sgFilial" 
					  dataType="text" size="3" labelWidth="18%" width="82%" disabled="true" serializable="false" >
			<adsm:textbox property="filialByIdFilialResponsavel.pessoa.nmFantasia" dataType="text" 
						  size="50" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textbox label="filialSolicitante" property="filialByIdFilialSolicitante.sgFilial" 
					  dataType="text" size="3" labelWidth="18%" width="82%" disabled="true" serializable="false" >
			<adsm:textbox property="filialByIdFilialSolicitante.pessoa.nmFantasia" dataType="text" 
						  size="50" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textbox label="solicitacao" property="dhPedidoColeta" dataType="JTDateTimeZone" 
					  picker="false" size="20" labelWidth="18%" width="37%" disabled="true" serializable="false" />
		
		<adsm:textbox label="tipoColeta" property="tpPedidoColeta.description" dataType="text" size="20" 
					  serializable="false" width="30%" disabled="true" />

		<adsm:textbox label="registradaPor" property="nmSolicitante" dataType="text" size="50" 
					  labelWidth="18%" width="37%" disabled="true" serializable="false" />

		<adsm:textbox label="modoColeta" property="tpModoPedidoColeta.description" dataType="text" size="20" 
					  serializable="false" width="30%" disabled="true" />

		<adsm:textbox label="cliente" property="cliente.pessoa.nrIdentificacaoFormatado" dataType="text" size="20" labelWidth="18%" 
					  width="82%" disabled="true" serializable="false" >
			<adsm:textbox property="cliente.pessoa.nmPessoa" dataType="text" size="50" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textarea label="endereco" property="enderecoComComplemento" maxLength="200" columns="70" 
					   labelWidth="18%" width="82%" disabled="true" serializable="false" />

		<adsm:textbox label="municipio" dataType="text" property="municipio.nmMunicipio" 
					  size="30" labelWidth="18%" width="37%" disabled="true" serializable="false" />
		
		<adsm:textbox label="cep" property="nrCep" dataType="text" 
					  size="10" width="30%" disabled="true" serializable="false" />

		<adsm:textbox label="disponibilidadeColeta" property="dhColetaDisponivel" dataType="JTDateTimeZone" serializable="false" labelWidth="18%" width="37%" disabled="true" size="16" picker="false"/>

		<adsm:textbox label="horarioLimite" property="hrLimiteColeta" dataType="JTTime" serializable="false" width="30%" disabled="true" size="6" />

		<adsm:textbox label="dataPrevistaColeta" property="dtPrevisaoColeta" dataType="JTDate" serializable="false" labelWidth="18%" width="82%" disabled="true" size="11" picker="false"/>

		<adsm:textbox label="telefone" property="nrTelefoneCliente" dataType="text" 
					  size="15" labelWidth="18%" width="37%" disabled="true" serializable="false" />
		
		<adsm:textbox label="contato" property="nmContatoCliente" dataType="text" 
					  size="35" width="30%" disabled="true" serializable="false" />

		<adsm:textbox label="rota" property="rotaColetaEntrega.dsRota" dataType="text" 
					  size="40" labelWidth="18%" width="37%" disabled="true" serializable="false" />
		
		<adsm:textbox label="regiao" property="dsRegiaoColetaEntregaFil" 
					  dataType="text" size="35" width="30%" disabled="true" serializable="false" />

		<adsm:textbox label="totalVolumes" property="qtTotalVolumesVerificado" dataType="integer" 
					  size="10" labelWidth="18%" width="37%" disabled="true" serializable="false" />
		
		<adsm:textbox label="totalPeso" property="psTotalInformado" dataType="weight" unit="kg"
					  size="18" width="30%" disabled="true" serializable="false" />

		<adsm:textbox label="valorTotalMercadoria" property="moeda.siglaSimbolo" dataType="text" 
					  size="6" labelWidth="18%" width="37%" disabled="true" serializable="false" >
			<adsm:textbox dataType="currency" property="vlTotalVerificado" mask="###,###,###,###,##0.00" size="18" disabled="true" serializable="false" />
		</adsm:textbox>
		
		<adsm:textbox label="totalPesoAforado" property="psTotalVerificado" dataType="weight" unit="kg" 
					  size="18" width="30%" disabled="true" serializable="false" />

		<adsm:textbox label="meioTransporte" property="manifestoColeta.controleCarga.meioTransporteByIdTransportado.nrFrota" 
					  dataType="text" size="6" labelWidth="18%" width="37%" disabled="true" serializable="false" >
			<adsm:textbox property="manifestoColeta.controleCarga.meioTransporteByIdTransportado.nrIdentificador" 
						  dataType="text" size="10" disabled="true" serializable="false" />
		</adsm:textbox>
		
		<adsm:textbox label="controleCargas" property="manifestoColeta.controleCarga.filialByIdFilialOrigem.sgFilial" 
					  dataType="text" size="3" width="30%" disabled="true" serializable="false" >
			<adsm:textbox property="manifestoColeta.controleCarga.nrControleCarga" dataType="integer" size="9" maxLength="8" mask="00000000" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textbox label="manifesto" property="manifestoColeta.filial.sgFilial" labelWidth="18%"
					  dataType="text" size="3" width="37%" disabled="true" serializable="false" >
			<adsm:textbox property="manifestoColeta.nrManifesto" dataType="integer" size="9" maxLength="8" mask="00000000" disabled="true" serializable="false" />
		</adsm:textbox>	
		
		<adsm:checkbox label="produtoDiferenciado" property="blProdutoDiferenciado"  width="10%" disabled="true" serializable="false" />				  

		<adsm:textarea label="observacao" property="obPedidoColeta" maxLength="200" columns="70" 
					   labelWidth="18%" width="82%" disabled="true" serializable="false" />

		<adsm:textarea label="obsCancelamento" property="obsCancelamento" maxLength="200" columns="70" 
					   labelWidth="18%" width="82%" disabled="true" serializable="false" />

    <script type="text/javascript" language="javascript">
        var LMS_02045 = "<adsm:label key="LMS-02045" />";
    </script>
	</adsm:form>

	<adsm:grid property="detalhesColeta" idProperty="idDetalheColeta" selectionMode="none" scrollBars="horizontal" 
			   rows="2" unique="true" gridHeight="40" autoSearch="false"
			   onRowClick="populaForm" 
			   service="lms.coleta.consultarColetasDadosColetaAction.findPaginatedDetalhesColeta"
			   rowCountService="lms.coleta.consultarColetasDadosColetaAction.getRowCountDetalhesColeta" 
			   onDataLoadCallBack="gridCallback">
		<adsm:gridColumn title="destino" 			property="municipio.nmMunicipio" width="170" />
		<adsm:gridColumn title="filialDestino"		property="filial.sgFilial" width="90" />
		<adsm:gridColumn title="naturezaMercadoria" property="naturezaProduto.dsNaturezaProduto" width="170" />
		<adsm:gridColumn title="destinatario" 		property="cliente.pessoa.tpIdentificacao" isDomain="true" width="40" />
		<adsm:gridColumn title="" 					property="cliente.pessoa.nrIdentificacaoFormatado" width="100" align="right" />
		<adsm:gridColumn title="" 					property="cliente.pessoa.nmPessoa" width="160" />
		<adsm:gridColumn title="servico" 			property="servico.dsServico" width="200" />
		<adsm:gridColumn title="frete" 				property="tpFrete" isDomain="true" width="50" />
		<adsm:gridColumn title="peso" 				property="psAforado" unit="kg" dataType="decimal" mask="###,###,###,##0.000" width="80" align="right" />
		<adsm:gridColumn title="volumes" 			property="qtVolumes" width="80" align="right" />
		<adsm:gridColumn title="valor" 				property="moeda.siglaSimbolo" width="80"/>
		<adsm:gridColumn title="" 					property="vlMercadoria" dataType="currency" width="110" align="right" />
		<adsm:gridColumn title="notaFiscal" 		property="notaFiscalColetas" image="/images/popup.gif" openPopup="true"  link="/coleta/cadastrarPedidoColeta.do?cmd=listaNota" popupDimension="380,240" width="80" align="center" linkIdProperty="idDetalheColeta" />
		<adsm:gridColumn title="preAwbAwb" property="awb" width="140" align="left"/>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
        	<adsm:gridColumn title="documentoServico" property="sgFilialOrigemDocto" width="60" />
            <adsm:gridColumn title="" property="nrDoctoServico" width="70" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="entregaDireta" property="blEntregaDireta" width="110" renderMode="image-check"/>
		<adsm:gridColumnGroup separatorType="COTACAO">
			<adsm:gridColumn title="cotacao"			property="cotacao.filial.sgFilial" width="40"/>
			<adsm:gridColumn title="" 					property="cotacao.nrCotacao" width="50" dataType="integer" mask="00000000" align="right" />
		</adsm:gridColumnGroup>
		<adsm:gridColumnGroup separatorType="CRT">
			<adsm:gridColumn title="crt"				property="ctoInternacional.sgPais" width="40" />
			<adsm:gridColumn title="" 					property="ctoInternacional.nrCrt" width="50" dataType="integer" mask="00000000" align="right" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="observacao" 		property="pedidoColeta.obPedidoColeta" width="400" />

		<adsm:buttonBar freeLayout="false" >
			<adsm:button id="botaoEmitir" caption="emitirPedidoColeta" disabled="false" onclick="visualizaRelatorioPedidoColeta();" />
			<adsm:button id="botaoEventoColeta" caption="eventosColeta" disabled="false" onclick="eventosColeta_click();" />
			<adsm:button id="botaoDadosEquipe" caption="equipe" disabled="false" onclick="dadosEquipe_click();" buttonType="findButton" />
			<adsm:button caption="fechar" id="botaoFechar" onclick="javascript:window.close();" disabled="false" />
		</adsm:buttonBar>  
	</adsm:grid>
</adsm:window>

<script>

/**
 * Função para abrir o relatório de pedido coleta. Foi criado essa função pois como 
 * o botão para visualizar está dentro da grid, não era enviado nenhum parametro 
 * para o relatorio.
 * @author Rodrigo Antunes
 */
 function visualizaRelatorioPedidoColeta() {
    reportButtonScript('lms.coleta.relatorioPedidoColetaService', 'openPdf', this.document.forms[0]);    
}

function povoaDados() {
	detalhesColetaGridDef.executeSearch({pedidoColeta:{idPedidoColeta:getElementValue("idPedidoColeta")}}, true);
	onDataLoad(getElementValue("idPedidoColeta"));
}

function populaForm(valor) {
	return false;
}

function carregaDados_retorno_cb(data, error) {
	onDataLoad_cb(data, error);
	format(document.getElementById("nrColeta"));
	format(document.getElementById("manifestoColeta.controleCarga.nrControleCarga"));
		
	setDisabled("botaoEmitir", false);
	setDisabled("botaoEventoColeta", false);
	setDisabled("botaoDadosEquipe", false);
	
	setDisabled("botaoFechar", false);
	
	setFocus(document.getElementById("botaoFechar"), true, true);
}

function carregaDados_retorno_workflow_cb(data, error){
	carregaDados_retorno_cb(data, error);
	detalhesColetaGridDef.executeSearch({pedidoColeta:{idPedidoColeta:getIdProcessoWorkflow()}}, true);
}

function gridCallback_cb(data, error) {
	var idProcessoWorkflow = getIdProcessoWorkflow(); 
	if (idProcessoWorkflow){
		setDisabled("botaoEmitir", true);
		setDisabled("botaoEventoColeta", true);
		setDisabled("botaoDadosEquipe", true);
	}
	if(error){
		alert(error);
		return false;
	}
	return true;

}

function eventosColeta_click() {
	var parametros = "&idPedidoColeta=" + getElementValue("idPedidoColeta");
	//Verifica se a tela esta servindo como popUp e informa como parametro...
	if(getTabGroup(this.document) == null) {
		parametros = parametros + "&popUp=true";
	} 
	showModalDialog('coleta/consultarEventosColeta.do?cmd=pesq' + parametros,window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:470px;');
}

function dadosEquipe_click() {
    var idControleCarga = getElementValue("manifestoColeta.controleCarga.idControleCarga");
    if (idControleCarga==undefined || idControleCarga=="") {
        alert(LMS_02045);
        return false;
    }
	var parametros = "&idControleCarga=" + idControleCarga;
	showModalDialog('coleta/programacaoColetasVeiculosDadosEquipe.do?cmd=main' + parametros,window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:520px;');
}
</script>