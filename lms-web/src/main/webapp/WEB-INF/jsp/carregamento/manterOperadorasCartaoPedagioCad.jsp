<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.carregamento.operadoraCartaoPedagioService" onPageLoadCallBack="operadora_pageLoad">
	<adsm:form action="/carregamento/manterOperadorasCartaoPedagio" idProperty="idOperadoraCartaoPedagio" 
			   onDataLoadCallBack="carregaPagina" service="lms.carregamento.operadoraCartaoPedagioService.findByIdDetalhamento" >

		<adsm:hidden property="pessoa.tpPessoa" value="J" />
		<adsm:hidden property="labelOperadoraCartaoPedagio" serializable="false" />
		
		<adsm:complement label="identificacao" width="85%" required="true" >
			<adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.cad" required="false"/>
			<adsm:lookup definition="IDENTIFICACAO_PESSOA" onDataLoadCallBack="validaPessoa" />
		</adsm:complement>

		<adsm:textbox dataType="text" property="pessoa.nmPessoa" label="razaoSocial" size="40" maxLength="50" width="85%" required="true" depends="pessoa.nrIdentificacao" />
		<adsm:textbox dataType="email" property="pessoa.dsEmail" label="email" size="40" maxLength="60" width="35%" depends="pessoa.nrIdentificacao" />
		<adsm:textbox dataType="text" property="dsEnderecoWeb" label="homepage" size="40" maxLength="60"/>
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="85%" required="true" renderOptions="true"/>
		
		<adsm:buttonBar>
			<adsm:button caption="cartoes" action="/carregamento/manterCartoesPedagio" cmd="main" >
				<adsm:linkProperty src="idOperadoraCartaoPedagio" target="operadoraCartaoPedagio.idOperadoraCartaoPedagio" disabled="true"/>
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="operadoraCartaoPedagio.pessoa.nrIdentificacao" disabled="true"/>
				<adsm:linkProperty src="pessoa.nmPessoa" target="operadoraCartaoPedagio.pessoa.nmPessoa" disabled="true"/>
			</adsm:button>
			<adsm:button caption="contatos"  action="/configuracoes/manterContatos" cmd="main" >
				<adsm:linkProperty src="pessoa.idPessoa" target="pessoa.idPessoa" />
				<adsm:linkProperty src="pessoa.tpIdentificacao" target="pessoa.tpIdentificacao" />
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" />
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa" />
				<adsm:linkProperty src="labelOperadoraCartaoPedagio" target="labelPessoaTemp" />
			</adsm:button>
			<adsm:button caption="enderecos" action="/configuracoes/manterEnderecoPessoa" cmd="main" >
				<adsm:linkProperty src="pessoa.idPessoa" target="pessoa.idPessoa" />
				<adsm:linkProperty src="pessoa.tpIdentificacao" target="pessoa.tpIdentificacao" />
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" />
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa" />
				<adsm:linkProperty src="labelOperadoraCartaoPedagio" target="labelPessoaTemp" />
			</adsm:button>
			<adsm:storeButton/>
			<adsm:newButton/>			
			<adsm:removeButton service="lms.carregamento.operadoraCartaoPedagioService.removeOperadoraCartaoPedagioById" />
		</adsm:buttonBar>
		
		<script>
			var labelOperadoraCartaoPedagio = '<adsm:label key="operadoraCartaoPedagio"/>';
			
			function lms_05002() {
				alert('<adsm:label key="LMS-05002"/>');
			}
		</script>
	</adsm:form>
</adsm:window>

<script>
	document.getElementById("pessoa.nrIdentificacao").serializable = true;
	document.getElementById("labelOperadoraCartaoPedagio").masterLink = "true";

	function operadora_pageLoad_cb(data, error){
		onPageLoad_cb(data, error);
		setElementValue(document.getElementById("labelOperadoraCartaoPedagio"), labelOperadoraCartaoPedagio);
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
		if ( getElementValue('idOperadoraCartaoPedagio') != "" ) {
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
		var sdo = createServiceDataObject("lms.carregamento.operadoraCartaoPedagioService.find", "resultadoDaPesquisa", 
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
	 * já existe uma operadora cadastrada para a pessoa informada. Após será carregados os dados dessa operadora, possibilitando
	 * a edição.
	 */
	function resultadoDaPesquisa_cb(data, error){
		if (error != undefined) {
			alert(error);
			return;
		}
		if (data != undefined && data.length > 0 && getNestedBeanPropertyValue(data,":0.pessoa.idPessoa") != undefined) {
			lms_05002();
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