<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sgr.gerenciadoraRiscoService" onPageLoadCallBack="gerenciadora_pageLoad">
	<adsm:form action="/sgr/manterGerenciadorasRisco" idProperty="idGerenciadoraRisco" onDataLoadCallBack="carregaPagina"
	 		   service="lms.sgr.gerenciadoraRiscoService.findByIdDetalhamento">

		<adsm:hidden property="pessoa.tpPessoa" value="J" />
		<adsm:hidden property="labelGerenciadoraRisco" serializable="false"/>
		
		<adsm:complement label="identificacao" width="85%" required="true" >
			<adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.cad" required="false"/>
			<adsm:lookup definition="IDENTIFICACAO_PESSOA" onDataLoadCallBack="validaPessoa" />
		</adsm:complement>
		
		<adsm:textbox label="razaoSocial" property="pessoa.nmPessoa" dataType="text" size="40" width="85%" maxLength="50" required="true" depends="pessoa.nrIdentificacao" />
		<adsm:textbox label="email" property="pessoa.dsEmail" maxLength="60" dataType="email" size="40" width="35%" depends="pessoa.nrIdentificacao" />
		<adsm:textbox label="homepage" property="dsEnderecoWeb" dataType="text" size="40" maxLength="80"/>
		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS" width="85%" required="true" renderOptions="true"/>
		
		<adsm:buttonBar>
			<adsm:button caption="contatos"  action="/configuracoes/manterContatos" cmd="main" >
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" />
				<adsm:linkProperty src="labelGerenciadoraRisco" target="labelPessoaTemp" />
				<adsm:linkProperty src="pessoa.idPessoa" target="pessoa.idPessoa" disabled="true"/>
				<adsm:linkProperty src="pessoa.tpIdentificacao" target="pessoa.tpIdentificacao" disabled="true"/>
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa" disabled="true"/>
			</adsm:button>
			<adsm:button caption="enderecos" action="/configuracoes/manterEnderecoPessoa" cmd="main" >
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" />
				<adsm:linkProperty src="labelGerenciadoraRisco" target="labelPessoaTemp" />
				<adsm:linkProperty src="pessoa.idPessoa" target="pessoa.idPessoa" disabled="true"/>
				<adsm:linkProperty src="pessoa.tpIdentificacao" target="pessoa.tpIdentificacao" disabled="true"/>
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa" disabled="true"/>
			</adsm:button>			
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton service="lms.sgr.gerenciadoraRiscoService.removeGerenciadoraById"/>
		</adsm:buttonBar>

		<script>
			var labelGerenciadoraRisco = '<adsm:label key="gerenciadoraRisco"/>';
			
			function lms_11001() {
				alert('<adsm:label key="LMS-11001"/>');
			}
		</script>
		
	</adsm:form>
</adsm:window>

<script>
	document.getElementById("pessoa.nrIdentificacao").serializable = true;
	document.getElementById("labelGerenciadoraRisco").masterLink = "true";

	function gerenciadora_pageLoad_cb(data, error){
		onPageLoad_cb(data, error);
		setElementValue(document.getElementById("labelGerenciadoraRisco"), labelGerenciadoraRisco);
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
	function carregaPagina_cb(data, error) {
		onDataLoad_cb(data, error);
		if ( getElementValue('idGerenciadoraRisco') != "" ) {
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
		var sdo = createServiceDataObject("lms.sgr.gerenciadoraRiscoService.find", "resultadoDaPesquisa", 
			{
			pessoa:{tpPessoa:getElementValue('pessoa.tpPessoa'),
					tpIdentificacao:getElementValue("pessoa.tpIdentificacao"),
			        nrIdentificacao:getElementValue("pessoa.nrIdentificacao")}
			});
	    xmit({serviceDataObjects:[sdo]});
	    
	    if ( getElementValue('pessoa.nmPessoa') == "" ) {
	    	buscaPessoa();
	    }
	}
	
	/**
	 * CallBack da busca de pessoa (validaPessoa_cb)
	 * Se a pesquisa retornar alguma pessoa, será apresentada uma mensagem ao usuário informando que 
	 * já existe uma seguradora cadastrada para a pessoa informada. Após será carregados os dados dessa seguradora, possibilitando
	 * a edição.
	 */
	function resultadoDaPesquisa_cb(data, error){
		if (error != undefined) {
			alert(error);
			return false;
		}
		if (data != undefined && data.length > 0 && getNestedBeanPropertyValue(data,":0.pessoa.idPessoa") != undefined) {
			lms_11001();
			onDataLoad(getNestedBeanPropertyValue(data,":0.pessoa.idPessoa"));
		}
	}
	
	/**
	 * Função que busca os dados da pessoa a partir do nrIdentificacao
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
	 * Função que seta os valores nos campos baseado no resultado de buscaPessoa()
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
	 * Função para limpar os campos preenchidos pela busca de pessoa.
	 */
	function limpaCamposLookup() {
		setElementValue('pessoa.nmPessoa', "");
		setElementValue('pessoa.dsEmail', "");
		setElementValue('dsEnderecoWeb', "");
	}
</script>