<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.clienteRegiaoService">
	<adsm:form action="/vendas/manterRegioesCliente" idProperty="idClienteRegiao" onDataLoadCallBack="myDataLoad">
		<adsm:hidden property="idFilial" serializable="false"/>
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
			dataType="text" maxLength="60" size="30" required="true">	

			<!-- Seta o hidden para carregar a UF relacionada -->
			<adsm:propertyMapping 
				relatedProperty="municipio.unidadeFederativa.siglaDescricao" 
				modelProperty="unidadeFederativa.siglaDescricao" 
			/>								
		</adsm:lookup>
		<adsm:textbox property="municipio.unidadeFederativa.siglaDescricao" label="uf" maxLength="70" size="40"  dataType="text" disabled="true" serializable="false" required="true"/>

		<adsm:combobox 
			service="lms.municipios.tipoLocalizacaoMunicipioService.find" 
			property="tipoLocalizacaoMunicipio.idTipoLocalizacaoMunicipio" 
			optionLabelProperty="dsTipoLocalizacaoMunicipio" 
			optionProperty="idTipoLocalizacaoMunicipio" 
			label="tipoLocalizacao"
			required="true"
			onlyActiveValues="true"
			boxWidth="230"
		/>
				
		<adsm:combobox property="tpModal" label="modal" domain="DM_MODAL"/>		
		<adsm:buttonBar>
			<adsm:storeButton id="storeButton"/>
			<adsm:newButton id="newButton"/>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>

	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function myDataLoad_cb(data, error) {
		onDataLoad_cb(data, error);
		initWindow();
	}
	
	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function initWindow(){
		if (getTabGroup(document).getTab("pesq").getElementById("permissao").value!="true") {
			setDisabled("storeButton", true);
			setDisabled("newButton", true);
			setDisabled("removeButton", true);
		}
	}

</script>