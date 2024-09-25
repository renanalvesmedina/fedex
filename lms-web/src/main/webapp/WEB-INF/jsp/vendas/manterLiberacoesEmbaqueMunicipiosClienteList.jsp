<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.liberacaoEmbarqueService" onPageLoadCallBack="myPageLoad">
	<adsm:form action="/vendas/manterLiberacoesEmbaqueMunicipiosCliente">
		<adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="permissao" serializable="false"/>
		<adsm:hidden property="cliente.idCliente"/>
		<adsm:textbox
			label="cliente"
			property="cliente.pessoa.nrIdentificacao"
			dataType="text"
			size="20" maxLength="20"
			labelWidth="10%" width="90%"
			disabled="true"
			serializable="false">
			<adsm:textbox
				dataType="text"
				property="cliente.pessoa.nmPessoa"
				size="60" maxLength="60"
				disabled="true"
				serializable="false"/>
		</adsm:textbox>
		<adsm:lookup
			label="municipio"
			property="municipio"
			idProperty="idMunicipio" 
			dataType="text"
			criteriaProperty="nmMunicipio"
			service="lms.municipios.municipioService.findLookup"
			size="30" maxLength="30" 
			action="/municipios/manterMunicipios"
			minLengthForAutoPopUpSearch="3"
			exactMatch="false"
			labelWidth="10%"
			width="40%">
			<adsm:propertyMapping
				relatedProperty="municipio.unidadeFederativa.siglaDescricao"
				modelProperty="unidadeFederativa.siglaDescricao" />
		</adsm:lookup>
		<adsm:textbox
			label="uf"
			dataType="text"
			property="municipio.unidadeFederativa.siglaDescricao"
			size="50"
			maxLength="71"
			width="45%"
			labelWidth="5%"
			disabled="true"
			serializable="false"/>		
		<adsm:combobox
			property="tpModal"
			label="modal"
			domain="DM_MODAL"
			labelWidth="10%"
			width="90%"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="liberacaoEmbarque"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid
		property="liberacaoEmbarque"
		idProperty="idLiberacaoEmbarque"
		gridHeight="200"
		unique="true"
		onSelectAll="myOnSelectAll"
		onSelectRow="myOnSelectRow"
		rows="12">
		<adsm:gridColumn title="municipio" property="municipio.nmMunicipio" width="40%" />
		<adsm:gridColumn title="uf" property="municipio.unidadeFederativa.nmUnidadeFederativa" width="20%" />
		<adsm:gridColumn title="modal" property="tpModal" isDomain="true" width="20%"/>
		<adsm:gridColumn title="situacaoAprovacao" property="tpSituacaoAprovacao" isDomain="true" width="20%"/>
		<adsm:buttonBar>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>
	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function myPageLoad_cb(data, error) {
		onPageLoad_cb();
		
		//Se acessado pelo workflow nao chama a validação da permissao
		if (this.parent.location.search.indexOf("idProcessoWorkflow")>=0)return;

		var idFilial = getElementValue("idFilial");
		var data = new Array();
		setNestedBeanPropertyValue(data, "idFilial", idFilial);
		var sdo = createServiceDataObject("lms.vendas.liberacaoEmbarqueService.validatePermissaoUsuarioLogado", "validatePermissoes", data);
		xmit({serviceDataObjects:[sdo]});
	}

	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function validatePermissoes_cb(data, error){
		setElementValue("permissao", data._value);
		if(data._value!="true") {
			setDisabled("removeButton", true);
		}
	}

	// Seta a propriedade masterLink para true para que o campo não seja limpo com a funcionalidade "Limpar"
	document.getElementById("permissao").masterLink = "true";

	/**
	* Esta função é utilizada para verificar a permissão de acesso pelo usuário
	* em relação a filial responsável operacional pelo cliente, desabilitando
	* o botão de excluir da listagem caso não tenha permissão.
	*/
	function myOnSelectRow(){
		var permissao = document.getElementById("permissao");
		if( permissao.value != "true" ){
			setDisabled("removeButton",true);
			return false;
		}
	}

	/**
	* Esta função deve executar exatamente a mesma tarefa que a função myOnSelectRow.
	*/
	function myOnSelectAll(){
		return myOnSelectRow();
	}
</script>