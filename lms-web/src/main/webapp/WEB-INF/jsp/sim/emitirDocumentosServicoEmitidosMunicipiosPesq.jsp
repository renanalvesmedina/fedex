<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window onPageLoadCallBack="pageLoad">
	<adsm:form action="/sim/emitirDocumentosServicoEmitidosMunicipios" >
        
		<adsm:lookup action="/vendas/manterDadosIdentificacao" criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" criteriaSerializable="true"
					 dataType="text" exactMatch="true" idProperty="idCliente" label="cliente" maxLength="20" property="cliente" 
					 service="lms.sim.emitirDocumentosServicoEmitidosMunicipiosAction.findLookupCliente" size="20" 	
					 labelWidth="20%" 	width="80%" required="true">
			
			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>			
			<adsm:textbox dataType="text" disabled="true" property="cliente.pessoa.nmPessoa" serializable="true" size="30"/>
		</adsm:lookup>

		<adsm:combobox property="tpRelatorio" label="tipoRelatorio" domain="DM_TIPO_CRITERIO_CLIENTE_DOCS_EMITIDOS" 
					   labelWidth="20%" width="80%" required="true" >
			<adsm:propertyMapping relatedProperty="dsTipoRelatorio" modelProperty="description"/>
        </adsm:combobox>
	    <adsm:hidden property="dsTipoRelatorio"/>
		
		<adsm:combobox property="tpOpcao" label="opcao" labelWidth="20%" domain="DM_OPCAO_RELATORIO_DOC_SERV_MUNICIPIO" 
					   width="80%" required="true" onchange="tpOpcaoChange(this);" >	
   			<adsm:propertyMapping relatedProperty="dsOpcao" modelProperty="description"/>
        </adsm:combobox>
		<adsm:hidden property="dsOpcao"/>
				
		<adsm:lookup action="/municipios/manterMunicipios" dataType="text"
				service="lms.sim.emitirDocumentosServicoEmitidosMunicipiosAction.findLookupMunicipio"
				property="municipio" idProperty="idMunicipio" criteriaProperty="nmMunicipio" 						 
				maxLength="60" size="35" criteriaSerializable="true"
				labelWidth="20%" width="80%" exactMatch="false"
				minLengthForAutoPopUpSearch="3" label="municipio">
		</adsm:lookup>

		<adsm:lookup property="unidadeFederativa" dataType="text"
				idProperty="idUnidadeFederativa" criteriaProperty="sgUnidadeFederativa"
				service="lms.sim.emitirDocumentosServicoEmitidosMunicipiosAction.findLookupUnidadeFederativa"
				label="uf" labelWidth="20%" width="80%" criteriaSerializable="true"
				maxLength="3" size="3" minLengthForAutoPopUpSearch="2" exactMatch="true"
				action="/municipios/manterUnidadesFederativas" >
    		<adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa"/>
			<adsm:textbox dataType="text" property="unidadeFederativa.nmUnidadeFederativa" size="35" serializable="true" disabled="true"/>
		</adsm:lookup>
				
		<adsm:combobox property="moedaPais.idMoedaPais" autoLoad="true"
				optionProperty="idMoedaPais" optionLabelProperty="moeda.siglaSimbolo"
				service="lms.sim.emitirDocumentosServicoEmitidosMunicipiosAction.findComboMoedaPais"
				label="converterParaMoeda" labelWidth="20%" width="80%" required="true" >
			<adsm:propertyMapping relatedProperty="moedaPais.moeda.idMoeda" modelProperty="moeda.idMoeda" />
			<adsm:propertyMapping relatedProperty="moedaPais.pais.idPais" modelProperty="pais.idPais" />
			<adsm:propertyMapping relatedProperty="moedaPais.moeda.siglaSimbolo" modelProperty="moeda.siglaSimbolo" />
			<adsm:propertyMapping relatedProperty="moedaPais.moeda.dsSimbolo" modelProperty="moeda.dsSimbolo" />
		</adsm:combobox>
        <adsm:hidden property="moedaPais.moeda.idMoeda" />
        <adsm:hidden property="moedaPais.pais.idPais" />
        <adsm:hidden property="moedaPais.moeda.siglaSimbolo" />
        <adsm:hidden property="moedaPais.moeda.dsSimbolo" />
        
        <adsm:combobox property="tpDocumentoServico" label="tipoDocumentoServico"
		        service="lms.sim.emitirDocumentosServicoEmitidosMunicipiosAction.findTipoDocumentoServico"
				optionProperty="value" optionLabelProperty="description"
				labelWidth="20%" width="80%" cellStyle="vertical-align:bottom;" >	
			<adsm:propertyMapping relatedProperty="dsTipoDocumentoServico" modelProperty="description"/>
	   </adsm:combobox>
	   <adsm:hidden property="dsTipoDocumentoServico"/>
					   
		<adsm:range label="periodoEmissao" labelWidth="20%" width="80%" required="true" maxInterval="31">
             <adsm:textbox dataType="JTDate" property="dtEmissaoInicial" picker="true" />
             <adsm:textbox dataType="JTDate" property="dtEmissaoFinal" picker="true"/>
        </adsm:range>

		<adsm:combobox property="tpFormatoRelatorio" label="formatoRelatorio" labelWidth="20%" 
						domain="DM_FORMATO_RELATORIO" width="80%" cellStyle="vertical-align:bottom;" defaultValue="pdf" required="true"/>	
						
		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.sim.emitirDocumentosServicoEmitidosMunicipiosAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

	var idMoedaPais;


	function initWindow(evt){
		setRowVisibility("municipio.idMunicipio", false);
		setRowVisibility("unidadeFederativa.idUnidadeFederativa", false);
	
		if (evt.name == 'cleanButton_click')
			preencheDadosUsuario();
	}

	function pageLoad_cb(){
		loadDadosSessao();
	}

	function loadDadosSessao(){		
		var data = new Array();
		var sdo = createServiceDataObject("lms.sim.emitirDocumentosServicoEmitidosMunicipiosAction.findMoedaUsuario",
				"setaDadosUsuario",data);
		xmit({serviceDataObjects:[sdo]});	
	}

	function setaDadosUsuario_cb(data){
		if (data != undefined){
			idMoedaPais = data.idMoedaPais;
			preencheDadosUsuario();
		}
	}
	
	function preencheDadosUsuario(){
		setElementValue("moedaPais.idMoedaPais", idMoedaPais);
		comboboxChange({e:document.getElementById("moedaPais.idMoedaPais")});
	}
	
	function tpOpcaoChange(e) {
		setRowVisibility("municipio.idMunicipio", e.value == 'M');
		setRowVisibility("unidadeFederativa.idUnidadeFederativa", e.value == 'U');
		comboboxChange({e:e});
	}

</script>