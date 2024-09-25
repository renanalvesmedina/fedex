<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.coleta.emitirJustificacaoColetasNaoRealizadasAction" onPageLoadCallBack="buscarDataAtual">
	<adsm:form action="/coleta/emitirJustificacaoColetasNaoRealizadas">
		<adsm:range label="periodo" width="85%" maxInterval="30" required="true">
			<adsm:textbox dataType="JTDate" size="10" maxLength="10" property="dataInicial" picker="true"/>
			<adsm:textbox dataType="JTDate" size="10" maxLength="10" property="dataFinal" picker="true"/>
		</adsm:range>
		
		<adsm:hidden property="servico.dsServico" />
		<adsm:combobox label="servico" property="servico.idServico" renderOptions="true"
					   optionProperty="idServico" optionLabelProperty="dsServico"
					   service="lms.coleta.emitirJustificacaoColetasNaoRealizadasAction.findServico" 
					   labelWidth="15%" width="85%" boxWidth="230"
					   onlyActiveValues="true" onchange="setDsServico();"/>
		<adsm:hidden property="dsTpPedidoColeta" />
		<adsm:combobox label="tipoColeta" property="tpPedidoColeta" domain="DM_TIPO_PEDIDO_COLETA" width="85%" renderOptions="true" onchange="setDsTipoColeta();"/>
		
		<adsm:checkbox label="naoProgramadas" property="naoProgramadas" />
		
		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.coleta.emitirJustificacaoColetasNaoRealizadasAction"/>
			<adsm:button caption="limpar" id="cleanButton" onclick="limpaRegistro()"/>
		</adsm:buttonBar>
	</adsm:form>	
</adsm:window>

<script type="text/javascript">

	/**
	 * Função chamada quando a tela é inicializada
	 */
	function initWindow(eventObj) {
		setDisabled("cleanButton", false);
	}
	
	/**
	 * Busca os Data Atual.
	 */
	function buscarDataAtual_cb() {
	    var sdo = createServiceDataObject("lms.coleta.emitirJustificacaoColetasNaoRealizadasAction.getDataAtual", "retornoBuscarDataAtual");
		xmit({serviceDataObjects:[sdo]});
	}	
	
			
	/**
	 * Retorno da pesquisa de dados do usuário da sessão em getDadosSessao().
	 */
	function retornoBuscarDataAtual_cb(data, error) {
		setElementValue("dataInicial", setFormat(document.getElementById("dataInicial"), data.dataPrimeiroDiaMes));
		setElementValue("dataFinal", setFormat(document.getElementById("dataFinal"), data.dataAtual));
	}
	
	/**
	 * Limpa a tela e carrega os dados necessarios novamente.
	 */
	function limpaRegistro() {
		resetValue(this.document);
		buscarDataAtual_cb();
		setFocus(document.getElementById("dataInicial"));
	}
	
	function setDsServico(){
		var comboBoxServico = document.getElementById("servico.idServico");
		setElementValue("servico.dsServico", comboBoxServico.options[comboBoxServico.selectedIndex].text);
	}
	
	function setDsTipoColeta(){
		var comboBoxServico = document.getElementById("tpPedidoColeta");
		setElementValue("dsTpPedidoColeta", comboBoxServico.options[comboBoxServico.selectedIndex].text);
	}
</script>