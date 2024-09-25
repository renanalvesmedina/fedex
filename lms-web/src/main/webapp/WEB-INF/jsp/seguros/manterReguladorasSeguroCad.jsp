<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguros.reguladoraSeguroService" onPageLoadCallBack="reguladora_pageLoad" >
	<adsm:form action="/seguros/manterReguladorasSeguro" idProperty="idReguladora" onDataLoadCallBack="carregaDadosPagina" 
			   service="lms.seguros.reguladoraSeguroService.findByIdDetalhamento"	>

		<adsm:hidden property="pessoa.tpPessoa" value="J" />
		<adsm:hidden property="labelPessoa" />

		<adsm:complement label="identificacao" width="85%" required="true" >
			<adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.cad" required="false"/>
			<adsm:lookup definition="IDENTIFICACAO_PESSOA" onDataLoadCallBack="validaPessoa" />
		</adsm:complement>

		<adsm:textbox dataType="text" property="pessoa.nmPessoa" label="razaoSocial" size="50" maxLength="50" width="85%" required="true" depends="pessoa.nrIdentificacao"/>
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="85%" required="true" renderOptions="true"/>
		<adsm:textbox dataType="text" property="nmServicoLiberacaoPrestado" label="servicoLiberacao" size="60" maxLength="100" width="85%" />
		<adsm:textbox dataType="email" property="pessoa.dsEmail" label="email" size="40" maxLength="60" width="85%" depends="pessoa.nrIdentificacao" />
		<adsm:textbox dataType="text" property="dsEnderecoWeb" label="homepage" size="40" maxLength="60" width="85%" />
		
		<adsm:buttonBar>
			<adsm:button caption="contatos" action="/configuracoes/manterContatos" cmd="main" >
				<adsm:linkProperty src="pessoa.idPessoa" target="pessoa.idPessoa" />
				<adsm:linkProperty src="pessoa.tpIdentificacao" target="pessoa.tpIdentificacao" />
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" />
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa" />
				<adsm:linkProperty src="labelPessoa" target="labelPessoaTemp" />
			</adsm:button>
			<adsm:button caption="enderecos" action="/configuracoes/manterEnderecoPessoa" cmd="main" >
				<adsm:linkProperty src="pessoa.idPessoa" target="pessoa.idPessoa" />
				<adsm:linkProperty src="pessoa.tpIdentificacao" target="pessoa.tpIdentificacao" />
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" />
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa" />
				<adsm:linkProperty src="labelPessoa" target="labelPessoaTemp" />
			</adsm:button>
			<adsm:button caption="seguradoras" action="/seguros/manterSeguradorasReguladora" cmd="main" >
				<adsm:linkProperty src="idReguladora" target="reguladoraSeguro.idReguladora" disabled="true"/>
				<adsm:linkProperty src="pessoa.nmPessoa" target="reguladoraSeguro.pessoa.nmPessoa" disabled="true"/>
			</adsm:button>
			<adsm:button caption="empresasEscolta" action="/seguros/manterEmpresasEscoltaReguladora" cmd="main" >
				<adsm:linkProperty src="idReguladora" target="reguladoraSeguro.idReguladora" disabled="true"/>
				<adsm:linkProperty src="pessoa.nmPessoa" target="reguladoraSeguro.pessoa.nmPessoa" disabled="true"/>				
			</adsm:button>
			<adsm:storeButton/>
			<adsm:newButton />
			<adsm:removeButton service="lms.seguros.reguladoraSeguroService.removeReguladoraSeguroById" />
		</adsm:buttonBar>

		<script>
			var labelCorretoraSeguro = "<adsm:label key="corretoraSeguro"/>";

			function lms_22001() {
				alert('<adsm:label key="LMS-22001"/>');
			}
		</script>
	</adsm:form>
</adsm:window>

<script>
	document.getElementById("pessoa.nrIdentificacao").serializable = true;
	document.getElementById("labelPessoa").masterLink = "true";
	
	function reguladora_pageLoad_cb(data, error){
		onPageLoad_cb(data, error);
		setElementValue(document.getElementById("labelPessoa"), labelCorretoraSeguro);
		changeTypePessoaWidget({tpTipoElement:document.getElementById("pessoa.tpPessoa"), tpIdentificacaoElement:document.getElementById('pessoa.tpIdentificacao'), numberElement:document.getElementById('pessoa.nrIdentificacao'), tabCmd:'cad'})
	}

	/**
	 * Quando detalhar item, desabilita campos de Identificacao da Pessoa
	 */
	function initWindow(eventObj) {
		if (eventObj.name == "storeButton"){
			desabilitaCampos(true);
		} else if (eventObj.name == "gridRow_click"){
			desabilitaCampos(true);
			setFocusOnFirstFocusableField();
		} else {
			desabilitaCampos(false);
			setFocusOnFirstFocusableField();
		}
		setDisabled('pessoa.idPessoa', true);
	}
	
	/**
	 * Habilita ou desabilita os campos pessoa.tpIdentificacao e pessoa.idPessoa
	 */
	function desabilitaCampos(valor) {
		setDisabled('pessoa.tpIdentificacao', valor);
		setDisabled('pessoa.idPessoa', valor);
	}

	/**
	 *
	 */
	function carregaDadosPagina_cb(data, error) {
		onDataLoad_cb(data, error);
		if ( getElementValue('idReguladora') != "" ) {
			desabilitaCampos(true);
		}
		else {
			desabilitaCampos(false);
		}
		setFocusOnFirstFocusableField();
		document.getElementById('pessoa.tpIdentificacao').required = true;		
		return false;
	}
	
	/**
	 * Busca Pessoa pelo Nr Identificação, Tipo de Identificação e Tipo de Pessoa.
	 */
	function validaPessoa_cb(data) {
		limpaCamposLookup();
	    pessoa_nrIdentificacao_exactMatch_cb(data);
		var sdo = createServiceDataObject("lms.seguros.reguladoraSeguroService.find", "resultadoDaPesquisa", 
			{
			pessoa:{tpPessoa:getElementValue('pessoa.tpPessoa'),
					tpIdentificacao:getElementValue('pessoa.tpIdentificacao'),
			        nrIdentificacao:getElementValue('pessoa.nrIdentificacao')}
			});
	    xmit({serviceDataObjects:[sdo]});
	    
	    if ( getElementValue('pessoa.nmPessoa') == "" ) {
	    	buscaPessoa();
	    }
	}
	
	/**
	 * CallBack da busca de pessoa (validaPessoa_cb)
	 * Se a pesquisa retornar alguma pessoa, será apresentada uma mensagem ao usuário informando que 
	 * já existe uma reguladora de seguro cadastrada para a pessoa informada. Após será carregados os dados dessa reguladora de seguro, 
	 * possibilitando a edição.
	 */
	function resultadoDaPesquisa_cb(data, error){
		if (error != undefined) {
			alert(error);
			return;
		}
		if (data != undefined && data.length > 0 && getNestedBeanPropertyValue(data,":0.pessoa.idPessoa") != undefined) {
			lms_22001();
			onDataLoad(getNestedBeanPropertyValue(data,":0.pessoa.idPessoa"));
		}
	}
	
	/**
	 *
	 */
	function buscaPessoa() {
	   	data = new Array();
	   	setNestedBeanPropertyValue(data, "tpPessoa", getElementValue('pessoa.tpPessoa'));
	   	setNestedBeanPropertyValue(data, "tpIdentificacao", getElementValue('pessoa.tpIdentificacao'));
		setNestedBeanPropertyValue(data, "nrIdentificacao", getElementValue('pessoa.nrIdentificacao'));
	
	    pessoa_nrIdentificacao_exactMatch_cb(data);
		var sdo = createServiceDataObject("lms.configuracoes.pessoaService.find", "resultadoDaPesquisaPessoa", 
			{
				tpPessoa:getElementValue('pessoa.tpPessoa'),
				tpIdentificacao:getElementValue('pessoa.tpIdentificacao'),
				nrIdentificacao:getElementValue('pessoa.nrIdentificacao')
			});
			
	    xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 *
	 */
	function resultadoDaPesquisaPessoa_cb(data, error){
		if (error != undefined) {
			alert(error);
			return;
		}
		if (data != undefined && data.length > 0 && getNestedBeanPropertyValue(data,":0.idPessoa") != undefined) {
			setElementValue('pessoa.idPessoa', getNestedBeanPropertyValue(data,":0.idPessoa"));
			setElementValue('pessoa.nmPessoa', getNestedBeanPropertyValue(data,":0.nmPessoa"));
			setElementValue('pessoa.dsEmail', getNestedBeanPropertyValue(data,":0.dsEmail"));
		}
	}
	
	/**
	 *
	 */
	function limpaCamposLookup() {
		setElementValue('pessoa.nmPessoa', "");
		setElementValue('pessoa.dsEmail', "");
		setElementValue('dsEnderecoWeb', "");
	}
</script>