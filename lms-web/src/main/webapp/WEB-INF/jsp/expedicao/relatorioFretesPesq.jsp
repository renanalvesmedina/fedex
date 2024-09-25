<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
	
	/**
	 * Carrega dados do usuario
	 */
	function loadDataObjects_cb(data, error) {	
	    onPageLoad_cb(data, error); 
        var data = new Array();	
		var sdo = createServiceDataObject("lms.expedicao.relatorioFretesAction.getBasicData", "dataSession", data);
    	xmit({serviceDataObjects:[sdo]});
	}
	
</script>
<adsm:window onPageLoadCallBack="loadDataObjects">
	<adsm:form action="/expedicao/relatorioFretes">

	   <adsm:hidden property="tpEmp" value="M" serializable="false" />
	   <adsm:lookup
			action="/municipios/manterFiliais"
			service="lms.expedicao.relatorioFretesAction.findFilial"
			dataType="text"
			property="filialByIdFilialOrigem"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			label="filialOrigem"
			size="3"
			maxLength="3"
			labelWidth="16%"
			width="84%">
		   	<adsm:propertyMapping
		   		modelProperty="pessoa.nmFantasia"
		   		relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia"/>
		   	<adsm:propertyMapping modelProperty="empresa.tpEmpresa" criteriaProperty="tpEmp" />
            <adsm:textbox
            	dataType="text"
            	property="filialByIdFilialOrigem.pessoa.nmFantasia"
            	serializable="false"
            	size="30"
            	maxLength="60"
            	disabled="true"/>
        </adsm:lookup>

		<adsm:lookup
			action="/municipios/manterFiliais"
			service="lms.expedicao.relatorioFretesAction.findFilial"
			dataType="text"
			property="filialByIdFilialDestino"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			label="filialDestino"
			size="3"
			maxLength="3"
			labelWidth="16%"
			width="84%">
		   	<adsm:propertyMapping
		   		modelProperty="pessoa.nmFantasia"
		   		formProperty="filialByIdFilialDestino.pessoa.nmFantasia"/>
		   	<adsm:propertyMapping modelProperty="empresa.tpEmpresa" criteriaProperty="tpEmp" />
            <adsm:textbox
            	dataType="text"
            	property="filialByIdFilialDestino.pessoa.nmFantasia"
            	size="30"
            	serializable="false"
            	maxLength="45"
            	disabled="true"/>
        </adsm:lookup>

        <adsm:lookup
			label="digitador"
			property="funcionario"
			idProperty="idUsuario"
			criteriaProperty="nrMatricula" 
			serializable="false"
			service="lms.expedicao.relatorioFretesAction.findLookupUsuarioFuncionario"
			dataType="text"
			size="17"
			maxLength="15"
			labelWidth="16%"
			width="84%"
			action="/configuracoes/consultarFuncionariosView" 
			exactMatch="true"
			disabled="false"
		>
			<adsm:propertyMapping relatedProperty="usuario.idUsuario" modelProperty="idUsuario"/>
			<adsm:propertyMapping relatedProperty="funcionario.codPessoa.nome" modelProperty="nmUsuario"/>
			<adsm:textbox dataType="text" property="funcionario.codPessoa.nome" size="50" disabled="true" serializable="false"/>
			<adsm:hidden property="usuario.idUsuario"/>
		</adsm:lookup>
		
		<adsm:lookup idProperty="idCliente"  
					 criteriaProperty="pessoa.nrIdentificacao"
					 property="cliente"					 
					 service="lms.expedicao.relatorioFretesAction.findLookupCliente" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 action="/vendas/manterDadosIdentificacao"  dataType="text" 
					 label="cliente"  size="17" maxLength="20" width="84%" labelWidth="16%"  
		>	
			<adsm:propertyMapping relatedProperty="nome" modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="nome" size="50" maxLength="50" disabled="true" />								  
		</adsm:lookup>
				
		<adsm:range label="periodoEmissao" width="84%" labelWidth="16%" required="true">
			<adsm:textbox dataType="JTDate" property="dtInicial" required="false"/>
			<adsm:textbox dataType="JTDate" property="dtFinal"   required="false"/>
		</adsm:range>		
		
		<adsm:combobox property="tpFrete" 
					   label="tipoFrete" 
					   domain="DM_TIPO_FRETE"
					   labelWidth="16%" 
					   width="84%"
					   boxWidth="120"
		/>
		 
		<adsm:combobox property="tpConhecimento" 
					   label="tipoConhecimento" 
					   domain="DM_TIPO_CONHECIMENTO"
					   labelWidth="16%" 
					   width="84%"
					   boxWidth="120"					   
		/>
		
		<adsm:combobox property="tpIndicador" 
					   label="tpIndicadorEDI" 
					   domain="DM_TP_INDICADOR_EDI"
					   labelWidth="16%" 
					   width="84%"
   					   boxWidth="120"					   
		/>
		
		<adsm:combobox property="modal" label="modal" 
					   domain="DM_MODAL" serializable="true" 
					   labelWidth="16%"  width="84%" 
					   boxWidth="120"					   					   
		/>
			
		<adsm:combobox property="tpAbrangencia" 
					   label="abrangencia" 
					   domain="DM_ABRANGENCIA"
					   labelWidth="16%" 
					   width="84%"
   					   boxWidth="120"					   
		/>
		
		<adsm:combobox property="tpAgrupamento" 
					   label="tipoAgrupamento" 
					   domain="DM_TP_AGRUPAMENTO"
					   labelWidth="16%" 
					   width="84%"
					   required="true"
					   boxWidth="120"					   					   
		/>
		
		<adsm:buttonBar>			
			<adsm:reportViewerButton caption="visualizar" service="lms.expedicao.relatorioFretesAction"/>		
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">

	var idFilial = null;
	var sgFilial = null;
	var nmFilial = null;
	
	var idUsuario = null;
	var nrMatricula = null;
	var nmUsuario = null;
	
	var dtInicial 	= null;
	var dtFinal 	= null;
	
	
// Copia dados para da sessao recebidos para variáveis globais da tela
	function dataSession_cb(data) {
		idFilial = getNestedBeanPropertyValue(data, "filial.idFilial");
		sgFilial = getNestedBeanPropertyValue(data, "filial.sgFilial");
		nmFilial = getNestedBeanPropertyValue(data, "filial.pessoa.nmFantasia");
		
		idUsuario   = getNestedBeanPropertyValue(data, "usuarioLogado.idUsuario");
		nrMatricula = getNestedBeanPropertyValue(data, "usuarioLogado.nrMatricula");
		nmUsuario   = getNestedBeanPropertyValue(data, "usuarioLogado.nmUsuario");
		
		dtInicial = getNestedBeanPropertyValue(data, "dataInicial");
		dtFinal = getNestedBeanPropertyValue(data, "dataFinal");
		
		writeDataSession();
	}
	
	function initWindow(event) {
		if(event.name == "cleanButton_click"){
			writeDataSession();
		}
	}

	/**
	 * Preenche os dados basicos da tela
	 */
	function writeDataSession() {
		setElementValue("filialByIdFilialOrigem.idFilial", idFilial);
		setElementValue("filialByIdFilialOrigem.sgFilial", sgFilial);
		setElementValue("filialByIdFilialOrigem.pessoa.nmFantasia", nmFilial);

		setElementValue("usuario.idUsuario", idUsuario);
		setElementValue("funcionario.nrMatricula", nrMatricula);
		setElementValue("funcionario.codPessoa.nome", nmUsuario);

		setElementValue("dtInicial", dtInicial);
		setElementValue("dtFinal", dtFinal);
	}
</script>