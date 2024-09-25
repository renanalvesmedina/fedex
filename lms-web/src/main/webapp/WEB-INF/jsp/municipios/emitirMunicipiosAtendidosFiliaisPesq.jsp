<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
<!--
	var idEmpresa;
	var nrIdentificacao;
	var nmPessoa;
	var idFilialSessao;
	var sgFilialSessao;
	var nmFilialSessao;	
	
	function loadCustom_cb(data,error) {
		onPageLoad_cb(data,error);
		preencheEmpresa();
	}
	
	function preencheEmpresa() {
		var sdoUsuario = createServiceDataObject("lms.municipios.emitirMunicipiosAtendidosFiliaisAction.findEmpresaLogado",
			"preencheEmpresa",undefined);
		xmit({serviceDataObjects:[sdoUsuario]});
	}
		
	//Funcao de callback do servico que retorna os dados do usuario logado. 
	function preencheEmpresa_cb(data, exception){
		if (exception == null){
			idEmpresa = getNestedBeanPropertyValue(data,"idEmpresa");
			nrIdentificacao = getNestedBeanPropertyValue(data,"pessoa.nrIdentificacaoFormatado");
			nmPessoa = getNestedBeanPropertyValue(data,"pessoa.nmPessoa");
			
			idFilialSessao = getNestedBeanPropertyValue(data,"filial.idFilial");
			sgFilialSessao = getNestedBeanPropertyValue(data,"filial.sgFilial");
			nmFilialSessao = getNestedBeanPropertyValue(data,"filial.pessoa.nmFantasia");
			
			setEmpresaValues();
		}
	}
	
	function setEmpresaValues() {
		setElementValue("empresa.idEmpresa",idEmpresa);
		setElementValue("empresa.pessoa.nrIdentificacao",nrIdentificacao);
		setElementValue("empresa.pessoa.nmPessoa",nmPessoa);
		
		setElementValue("filial.idFilial",idFilialSessao);
		setElementValue("filial.sgFilial",sgFilialSessao);
		setElementValue("filial.pessoa.nmFantasia",nmFilialSessao);		
	}
	
	function initWindow(obj) {
		setFocus("filial.sgFilial",false);
		setEmpresaValues();
	}
//-->
</script>
<adsm:window onPageLoadCallBack="loadCustom" >
	<adsm:form action="/municipios/emitirMunicipiosAtendidosFiliais" >
		<adsm:lookup label="empresa" dataType="text" size="20" maxLength="20" width="85%"
				service="lms.municipios.emitirMunicipiosAtendidosFiliaisAction.findLookupEmpresa" property="empresa" idProperty="idEmpresa"
				criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				action="/municipios/manterEmpresas" required="true" >
			
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="empresa.pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="empresa.pessoa.nmPessoa" size="50" disabled="true" />
		</adsm:lookup>
		
		<adsm:hidden property="tpAcesso" serializable="false" value="A"></adsm:hidden> 
		
		<adsm:lookup service="lms.municipios.emitirMunicipiosAtendidosFiliaisAction.findLookupFilial" dataType="text"
				property="filial" criteriaProperty="sgFilial" label="filial" size="3" maxLength="3"
				width="85%" action="/municipios/manterFiliais" idProperty="idFilial" required="true" >
        	<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
        	<adsm:propertyMapping modelProperty="empresa.idEmpresa" criteriaProperty="empresa.idEmpresa"/>
        	<adsm:propertyMapping modelProperty="empresa.pessoa.nrIdentificacao"
        			criteriaProperty="empresa.pessoa.nrIdentificacao" inlineQuery="false" />
        			
        	<adsm:propertyMapping relatedProperty="empresa.pessoa.nrIdentificacao" 
        			modelProperty="empresa.pessoa.nrIdentificacao" blankFill="false" />
			<adsm:propertyMapping relatedProperty="empresa.pessoa.nmPessoa" 
					modelProperty="empresa.pessoa.nmPessoa" blankFill="false" />
			<adsm:propertyMapping relatedProperty="empresa.idEmpresa" modelProperty="empresa.idEmpresa" blankFill="false"/>        			
        			
        	<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso" />
        	
        	<adsm:propertyMapping modelProperty="empresa.pessoa.nmPessoa" criteriaProperty="empresa.pessoa.nmPessoa" inlineQuery="false" />
        	<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" />
        </adsm:lookup>
		
		<adsm:combobox property="servico.idServico" optionLabelProperty="dsServico" optionProperty="idServico" width="75%"
				label="servico" service="lms.municipios.emitirMunicipiosAtendidosFiliaisAction.findComboServico" boxWidth="200" >
			<adsm:propertyMapping relatedProperty="servico.dsServico" modelProperty="dsServico" />
		</adsm:combobox>
		<adsm:hidden property="servico.dsServico" />
		<adsm:combobox label="formatoRelatorio" property="tpFormatoRelatorio" domain="DM_FORMATO_RELATORIO" defaultValue="pdf" required="true"/>
		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.municipios.emitirMunicipiosAtendidosFiliaisAction" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
<!--
	document.getElementById("filial.sgFilial").serializable = true;
//-->
</script>