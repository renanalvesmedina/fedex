<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.entrega.emitirEficienciaEntregaClienteAction" onPageLoadCallBack="pageLoadCustom" >
	<adsm:form action="/entrega/emitirOcorrenciasCliente" idProperty="id" >
		
		<adsm:combobox property="tpCliente" domain="DM_TIPO_CLIENTE_ANALISE_EFICIENCIA"
				label="tipoCliente" labelWidth="15%" width="85%" required="true" />
				
		<adsm:lookup dataType="text" property="cliente" idProperty="idCliente"
				criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				service="lms.entrega.emitirEficienciaEntregaClienteAction.findLookupCliente" 
				action="/vendas/manterDadosIdentificacao"
				label="cliente" labelWidth="15%" width="85%"
				maxLength="20" size="20" exactMatch="true" required="true" >
			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" 
					serializable="false" disabled="true" size="30" />
		</adsm:lookup>

		<adsm:combobox property="contato.idContato" 
				optionProperty="idContato" optionLabelProperty="nmContato"
				service="lms.entrega.emitirEficienciaEntregaClienteAction.findComboContato"
				label="contato" labelWidth="15%" width="85%" boxWidth="315" >
			<adsm:propertyMapping criteriaProperty="cliente.idCliente" modelProperty="idPessoa"/>
			<adsm:propertyMapping relatedProperty="contato.nmContato" modelProperty="nmContato"/>
			<adsm:hidden property="contato.nmContato" />
		</adsm:combobox>

		<adsm:lookup property="unidadeFederativa" idProperty="idUnidadeFederativa" criteriaProperty="sgUnidadeFederativa" 
				 service="lms.entrega.emitirEficienciaEntregaClienteAction.findLookupUF" dataType="text"
				 width="35%" label="uf" size="3" maxLength="3"
				 action="/municipios/manterUnidadesFederativas" minLengthForAutoPopUpSearch="2" exactMatch="true">
    		<adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa" />
    		<adsm:textbox dataType="text" property="unidadeFederativa.nmUnidadeFederativa" size="25" serializable="false" disabled="true"/>	
		</adsm:lookup>
		
		<adsm:hidden property="tpEmpresaFilial" value="M" />
		<adsm:lookup dataType="text" property="filial" idProperty="idFilial" criteriaProperty="sgFilial"  
				service="lms.entrega.emitirEficienciaEntregaClienteAction.findLookupFilial"
				action="/municipios/manterFiliais" criteriaSerializable="true"
				label="filialDestino" labelWidth="15%" width="85%" size="3" maxLength="3" exactMatch="true" >
			<adsm:propertyMapping criteriaProperty="tpEmpresaFilial" modelProperty="empresa.tpEmpresa" />
			<adsm:propertyMapping criteriaProperty="unidadeFederativa.idUnidadeFederativa"
					modelProperty="pessoa.enderecoPessoas.municipio.unidadeFederativa.idUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" disabled="true" serializable="true" size="30" />
		</adsm:lookup>		
		
		<adsm:range label="periodoEntrega" labelWidth="15%" width="75%" required="true" maxInterval="60">
			<adsm:textbox dataType="JTDate" property="dtEventoInicial"/> 
			<adsm:textbox dataType="JTDate" property="dtEventoFinal"/>
		</adsm:range>
		
		<adsm:combobox label="formatoRelatorio" property="tpFormatoRelatorio" domain="DM_FORMATO_RELATORIO" 
        		defaultValue="pdf" required="true" labelWidth="15%" width="85%" />
        
        <adsm:buttonBar>
			<adsm:reportViewerButton service="lms.entrega.emitirEficienciaEntregaClienteAction" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>	
</adsm:window>

<script type="text/javascript">
<!--

	function pageLoadCustom_cb(data) {
		onPageLoad_cb(data);
		findInfoUsuarioLogado();
	}
	
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click") {
			populateInfoUsuarioLogado();
		}
	}

	var infoUsuario = undefined;
	
	function findInfoUsuarioLogado() {
		var sdo = createServiceDataObject("lms.entrega.emitirEficienciaEntregaClienteAction.findInfoUsuarioLogado",
				"findInfoUsuarioLogado",undefined);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function findInfoUsuarioLogado_cb(data,error) {
		infoUsuario = data;
		populateInfoUsuarioLogado();
	}

	function populateInfoUsuarioLogado() {
		fillFormWithFormBeanData(document.forms[0].tabIndex, infoUsuario);
	}

//-->
</script>