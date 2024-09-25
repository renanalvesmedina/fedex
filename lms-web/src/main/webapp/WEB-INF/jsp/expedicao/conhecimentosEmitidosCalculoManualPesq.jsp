<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
	function myOnPageLoad() {
	   onPageLoad();
	   loadDataObjects();
	}

	/**
	 * Carrega dados do usuario
	 */
	function loadDataObjects() {	
        var data = new Array();	
		var sdo = createServiceDataObject("lms.expedicao.conhecimentoEmitidoCalculoManualAction.getBasicData", "dataSession", data);
    	xmit({serviceDataObjects:[sdo]});
	}
</script>
<adsm:window onPageLoad="myOnPageLoad">
	<adsm:form action="/expedicao/conhecimentosEmitidosCalculoManual">

		<adsm:combobox
			width="35%"
			label="regional"
			property="regional.idRegional"
			optionLabelProperty="siglaDescricao"
			optionProperty="idRegional" 
			service="lms.municipios.regionalService.findByUsuarioLogado">
			<adsm:propertyMapping
				relatedProperty="regional.siglaDescricao"
				modelProperty="siglaDescricao"/>
		</adsm:combobox>

		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="false"/>
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="true" serializable="false"/>
		<adsm:hidden property="empresa.tpEmpresa" value="M" serializable="false"/>
		<adsm:hidden property="tpAcesso" value="F" serializable="false"/>
		<adsm:lookup label="filial" property="filial" 
        	service="lms.municipios.filialService.findLookupFilial" 
        	action="/municipios/manterFiliais" 
        	idProperty="idFilial" 
        	criteriaProperty="sgFilial" dataType="text" size="5" 
        	labelWidth="15%" 
			width="85%" 
			maxLength="3">
			<adsm:hidden property="regional.siglaDescricao"/>
            <adsm:propertyMapping
            	criteriaProperty="empresa.tpEmpresa"
            	modelProperty="empresa.tpEmpresa"/>
            <adsm:propertyMapping
            	criteriaProperty="tpAcesso"
				modelProperty="tpAcesso"/>
			<adsm:propertyMapping
            	criteriaProperty="flagBuscaEmpresaUsuarioLogado"
				modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping
            	criteriaProperty="flagDesabilitaEmpresaUsuarioLogado"
				modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping
            	relatedProperty="filial.pessoa.nmFantasia"
            	modelProperty="pessoa.nmFantasia" />

            <adsm:textbox
            	dataType="text"
            	property="filial.pessoa.nmFantasia"
            	size="30"
            	disabled="true"/>
        </adsm:lookup>

		<adsm:combobox
			property="modal"
			label="modal"
			domain="DM_MODAL"
			serializable="false"
			labelWidth="15%"
			width="63%">
			<adsm:hidden property="modal.valor"/>
			<adsm:hidden property="modal.descricao"/>
			<adsm:propertyMapping
				relatedProperty="modal.valor"
				modelProperty="value"/>
			<adsm:propertyMapping
				relatedProperty="modal.descricao"
				modelProperty="description"/>
		</adsm:combobox>
		
		<adsm:combobox
			property="abrangencia"
			label="abrangencia"
			domain="DM_ABRANGENCIA"
			serializable="false"
			labelWidth="15%"
			width="63%">
			<adsm:hidden property="abrangencia.valor"/>
			<adsm:hidden property="abrangencia.descricao"/>
			<adsm:propertyMapping
				relatedProperty="abrangencia.valor"
				modelProperty="value"/>
			<adsm:propertyMapping
				relatedProperty="abrangencia.descricao"
				modelProperty="description"/>
		</adsm:combobox>

		<adsm:combobox
			label="servico"
		 	property="servico.idServico"
		 	optionLabelProperty="dsServico"
		 	optionProperty="idServico"
		 	service="lms.expedicao.conhecimentoEmitidoCalculoManualAction.findServico"
		 	labelWidth="15%"
		 	width="33%">
			<adsm:hidden property="servico.dsServico"/>
			<adsm:propertyMapping
				relatedProperty="servico.dsServico"
				modelProperty="dsServico"/>
		</adsm:combobox>

		<adsm:range label="periodoEmissao" width="72%" labelWidth="15%" >
			<adsm:textbox dataType="JTDate" property="dataInicial" required="true"/>
			<adsm:textbox dataType="JTDate" property="dataFinal" required="true"/>		
		</adsm:range>

		<adsm:lookup 
			action="/vendas/manterDadosIdentificacao" 
			criteriaProperty="pessoa.nrIdentificacao" 
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			dataType="text" 
			exactMatch="true" 
			idProperty="idCliente" 
			label="cliente" 
			maxLength="20" 
			property="cliente" 
			service="lms.vendas.manterClienteAction.findLookupCliente" 
			size="20" 
			labelWidth="15%" 
			width="85%">
			<adsm:propertyMapping 
				relatedProperty="cliente.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox 
				dataType="text" 
				disabled="true" 
				property="cliente.pessoa.nmPessoa" 
				serializable="false"
				size="40"/>
		</adsm:lookup>

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.expedicao.conhecimentoEmitidoCalculoManualService"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
	//Copia dados para da sessao recebidos para variáveis globais da tela
	function dataSession_cb(data) {
		writeDataSession(data);
	}

	function initWindow(event) {
		if(event.name == "cleanButton_click"){
			loadDataObjects();
		}
	}

	/**
	 * Preenche os dados basicos da tela
	 */
	function writeDataSession(data) {
		var tpEmpresa = getNestedBeanPropertyValue(data, "empresa.tpEmpresa");
		var idFilial = getNestedBeanPropertyValue(data, "filial.idFilial");
		var sgFilial = getNestedBeanPropertyValue(data, "filial.sgFilial");
		var nmFilial = getNestedBeanPropertyValue(data, "filial.pessoa.nmFantasia");
		var dtInicial = getNestedBeanPropertyValue(data, "dataInicial");
		var dtFinal = getNestedBeanPropertyValue(data, "dataFinal");

		setElementValue("empresa.tpEmpresa", tpEmpresa);
		setElementValue("filial.idFilial", idFilial);
		setElementValue("filial.sgFilial", sgFilial);
		setElementValue("filial.pessoa.nmFantasia", nmFilial);

		setElementValue("dataInicial", dtInicial);
		setElementValue("dataFinal", dtFinal);		
	}
</script>