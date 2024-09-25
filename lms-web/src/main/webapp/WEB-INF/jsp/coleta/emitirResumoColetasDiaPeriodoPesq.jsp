<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>


<adsm:window service="lms.coleta.emitirResumoColetasDiaPeriodoAction" onPageLoadCallBack="buscarDadosSessao">
	<adsm:form action="/coleta/emitirResumoColetasDiaPeriodo">
	
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="true"/>	
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="true" serializable="true"/>	
		<adsm:hidden property="tpAcesso" value="F" />
	
		<adsm:lookup property="filial" idProperty="idFilial" 
					 criteriaProperty="sgFilial" criteriaSerializable="true" 
				 	 service="lms.coleta.emitirResumoColetasDiaPeriodoAction.findLookupFilial"
				 	 action="/municipios/manterFiliais" 
					 label="filial" dataType="text" size="5" maxLength="3" 
					 onPopupSetValue="validaAcessoFilial"
					 labelWidth="15%" width="85%" required="true">
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>					 
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>					 
			<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso"/>					 
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" 
						  size="30" maxLength="50" disabled="true" serializable="false"/>
		</adsm:lookup>	
		
		<adsm:range label="periodo" width="85%" required="true" maxInterval="31">
			<adsm:textbox dataType="JTDate" property="dataInicial" picker="true"/>
			<adsm:textbox dataType="JTDate" property="dataFinal" picker="true"/>
		</adsm:range>
		
		<adsm:combobox label="servico" property="servico.idServico"
					   optionProperty="idServico" optionLabelProperty="dsServico"
					   service="lms.coleta.emitirResumoColetasDiaPeriodoAction.findServico"
					   labelWidth="15%" width="85%"/>		
		
		<adsm:combobox label="modoColeta" property="tpModoPedidoColeta" domain="DM_MODO_PEDIDO_COLETA" renderOptions="true"
					   labelWidth="15%" width="85%"/>
		
  		<adsm:combobox label="tipoColeta" property="tpPedidoColeta" domain="DM_TIPO_PEDIDO_COLETA" renderOptions="true"
  					   labelWidth="15%" width="85%"/>
		
		<adsm:buttonBar>
			<adsm:reportViewerButton caption="visualizar" service="lms.coleta.emitirResumoColetasDiaPeriodoAction"/>
			<adsm:button caption="limpar" id="botaoLimpar" onclick="limpaTela()" disabled="false"/>
		</adsm:buttonBar>
		
	</adsm:form>
</adsm:window>

<script type="text/javascript">

	/**
	 * Busca os Dados referente ao usuário da sessão.
	 */
	function buscarDadosSessao_cb() {
	    var sdo = createServiceDataObject("lms.coleta.emitirResumoColetasDiaPeriodoAction.getDadosSessao", "retornoBuscarDadosSessao");
		xmit({serviceDataObjects:[sdo]});
	}
		
	/**
	 * Retorno da pesquisa de dados do usuário da sessão em getDadosSessao().
	 */
	function retornoBuscarDadosSessao_cb(data, error) {
		setElementValue("filial.idFilial", data.idFilialSessao);
		setElementValue("filial.sgFilial", data.sgFilialSessao);
		setElementValue("filial.pessoa.nmFantasia", data.nmFilialSessao);
		setElementValue("dataInicial", setFormat(document.getElementById("dataInicial"), data.dataPrimeiroDiaMes));
		setElementValue("dataFinal", setFormat(document.getElementById("dataFinal"), data.dataAtual));
		setDisabled("botaoLimpar", false);
	}

	function limpaTela(){
		cleanButtonScript(this.document);
		buscarDadosSessao_cb();
	}
	
	/**
 * Verifica se o usuario tem acesso a filial selecionada na popup de filial.
 * Função necessária pois quando é selecionado um item na popup não é chamado
 * o serviço definido na lookup.
 */
 function validaAcessoFilial(data) {
	var criteria = new Array();
    // Monta um map
    setNestedBeanPropertyValue(criteria, "idFilial", data.idFilial);
    setNestedBeanPropertyValue(criteria, "sgFilial", data.sgFilial);
	
    var sdo = createServiceDataObject("lms.coleta.emitirColetasAbertasAction.findLookupFiliaisPorUsuario", "resultadoLookup", criteria);
    xmit({serviceDataObjects:[sdo]});
}

/**
 * Função que trata o retorno da função validaAcessoFilial.
 */
function resultadoLookup_cb(data, error) {

	if (error != undefined) {
		alert(error);
		resetValue("filial.idFilial");
	    setFocus(document.getElementById("filial.sgFilial"));
	} else {
		filial_sgFilial_exactMatch_cb(data, error);
	}
}
</script>