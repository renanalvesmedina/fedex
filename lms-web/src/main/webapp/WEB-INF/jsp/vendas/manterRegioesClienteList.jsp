<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.clienteRegiaoService" onPageLoadCallBack="myPageLoad">
	<adsm:form action="/vendas/manterRegioesCliente" >
		<adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="permissao" serializable="false"/>
		<adsm:hidden property="cliente.idCliente"/>
		<adsm:textbox label="cliente" property="cliente.pessoa.nrIdentificacao" dataType="text" size="20" maxLength="20" labelWidth="15%" width="85%" disabled="true" serializable="false">
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="60" maxLength="60" disabled="true" serializable="false"/>
		</adsm:textbox>	

		<adsm:lookup 
			service="lms.municipios.municipioService.findLookup" 
			action="/municipios/manterMunicipios" 
			property="municipio" 
			idProperty="idMunicipio" 
			criteriaProperty="nmMunicipio" 
			exactMatch="false"
			minLengthForAutoPopUpSearch="3"
			label="municipio" 
			dataType="text" maxLength="60" size="30">	

			<!-- Seta o hidden para carregar a UF relacionada -->
			<adsm:propertyMapping 
				relatedProperty="municipio.unidadeFederativa.siglaDescricao" 
				modelProperty="unidadeFederativa.siglaDescricao" 
			/>								
		</adsm:lookup>
		<adsm:textbox property="municipio.unidadeFederativa.siglaDescricao" maxLength="70" size="40" label="uf" dataType="text" disabled="true" serializable="false"/>

		<adsm:combobox 
			service="lms.municipios.tipoLocalizacaoMunicipioService.find" 
			property="tipoLocalizacaoMunicipio.idTipoLocalizacaoMunicipio" 
			optionLabelProperty="dsTipoLocalizacaoMunicipio" 
			optionProperty="idTipoLocalizacaoMunicipio" 
			label="tipoLocalizacao" 
			boxWidth="230"
		/>
				
		<adsm:combobox property="tpModal" label="modal" domain="DM_MODAL"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="clienteRegiao"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idClienteRegiao" property="clienteRegiao" gridHeight="200" unique="true" onSelectAll="myOnSelectAll" onSelectRow="myOnSelectRow" rows="12">
		<adsm:gridColumn title="municipio" property="municipio.nmMunicipio" width="35%" />
		<adsm:gridColumn title="uf" property="municipio.unidadeFederativa.siglaDescricao" width="15%" />
		<adsm:gridColumn title="tipoLocalizacao" property="tipoLocalizacaoMunicipio.dsTipoLocalizacaoMunicipio" width="30%" />
		<adsm:gridColumn title="modal" property="tpModal" isDomain="true" width="20%" />
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
		var idFilial = getElementValue("idFilial");
		var data = new Array();	   
		setNestedBeanPropertyValue(data, "idFilial", idFilial);
		var sdo = createServiceDataObject("lms.vendas.clienteRegiaoService.validatePermissao", "validarPermissoes", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function validarPermissoes_cb(data, error){
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