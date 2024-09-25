<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterPrazosEntregaClienteAction">
	<adsm:form action="/vendas/manterPrazosEntregaCliente" idProperty="idPrazoEntregaCliente" onDataLoadCallBack="myDataLoadCallBack" >
	<adsm:i18nLabels>
		<adsm:include key="LMS-01149" />
		<adsm:include key="LMS-01150" />
	</adsm:i18nLabels>

		<adsm:hidden property="cliente.idCliente"/>
		<adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="tpAcessoFilial" value="A" serializable="false"/>
		
		<adsm:textbox label="cliente" property="cliente.pessoa.nrIdentificacao" dataType="text"
		 size="20" maxLength="20" 
		 labelWidth="16%" width="85%" disabled="true" 
		 serializable="false">
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="60" maxLength="60" disabled="true" serializable="false"/>
		</adsm:textbox>	
		<adsm:hidden property="origemString" serializable="false" />
		
		<adsm:section caption="origem"/>
		<adsm:hidden property="tpSituacaoAtivo" value="A" serializable="false"/>
		
		<adsm:combobox
			service="lms.vendas.manterPrazosEntregaClienteAction.findZona" 
			property="zonaByIdZonaOrigem.idZona" 
			optionLabelProperty="dsZona" 
			optionProperty="idZona" 
			label="zona" 
			onlyActiveValues="true"
			onchange="limpaZona('Origem')"
			labelWidth="16%" width="19%" boxWidth="130">
		</adsm:combobox>

		<adsm:lookup
			service="lms.vendas.manterPrazosEntregaClienteAction.findLookupPais" 
			action="/municipios/manterPaises" 
			property="paisByIdPaisOrigem" 
			idProperty="idPais" 
			criteriaProperty="nmPais" 
			label="pais"
			minLengthForAutoPopUpSearch="3" 
			exactMatch="false"
			onchange="return changePais('Origem');"
			dataType="text" labelWidth="6%" width="25%" size="25" maxLength="60">

			<adsm:propertyMapping criteriaProperty="tpSituacaoAtivo" modelProperty="tpSituacao" />
			
			<adsm:propertyMapping 
				addChangeListener="false"
				criteriaProperty="zonaByIdZonaOrigem.idZona" 
				modelProperty="zona.idZona"
			/>
			<adsm:propertyMapping 
				relatedProperty="zonaByIdZonaOrigem.idZona" 
				modelProperty="zona.idZona" 
			/> 
		</adsm:lookup>

		<adsm:hidden property="dsUFOrigem" serializable="false"/>
		<adsm:hidden property="sgUFOrigem" serializable="false"/>
		
		<adsm:combobox 
			service="lms.vendas.manterPrazosEntregaClienteAction.findUnidadeFederativaByPais" 
			property="unidadeFederativaByIdUfOrigem.idUnidadeFederativa" 
			optionLabelProperty="siglaDescricao" 
			optionProperty="idUnidadeFederativa" 
			label="uf" 
			onlyActiveValues="true"
			onDataLoadCallBack="ufOrigemOnDataLoad"
			onchange="return changeUF('Origem');"
			labelWidth="5%" width="29%" boxWidth="150">
			
			<adsm:propertyMapping 
				criteriaProperty="paisByIdPaisOrigem.idPais" 
				modelProperty="pais.idPais"

			/>
		</adsm:combobox>



        <adsm:hidden  property="dataAtual" serializable="false"/>
        
		<adsm:lookup 
			service="lms.vendas.manterPrazosEntregaClienteAction.findLookupFilial" 
			action="/municipios/manterFiliais" 
			property="filialByIdFilialOrigem" 
			idProperty="idFilial" 
			criteriaProperty="sgFilial" 
			exactMatch="false"
			allowInvalidCriteriaValue="false"
			minLengthForAutoPopUpSearch="3"	
			onDataLoadCallBack="filialOrigem"		
			label="filial" 
			onchange="return changeFilial('Origem');"
			onPopupSetValue="changeFilialOrigemPopup"
			dataType="text" size="5" maxLength="3" labelWidth="16%" width="37%">

		   <adsm:propertyMapping modelProperty="tpAcesso" 
		   		criteriaProperty="tpAcessoFilial"/>

			<adsm:propertyMapping  modelProperty="historicoFiliais.vigenteEm"
				criteriaProperty="dataAtual" />

		   <adsm:propertyMapping modelProperty="pessoa.nmFantasia" 
		   		relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia"/>
		   		
		   	<!-- Seta o Nome Pais automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="paisByIdPaisOrigem.nmPais" 
				modelProperty="endereco.municipio.unidadeFederativa.pais.nmPais" 
			/>
			<!-- Seta o ID Pais automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="paisByIdPaisOrigem.idPais" 
				modelProperty="endereco.municipio.unidadeFederativa.pais.idPais" 
			/>
			<!-- Seta a Zona automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="zonaByIdZonaOrigem.idZona" 
				modelProperty="endereco.municipio.unidadeFederativa.pais.zona.idZona" 
			/>
			
			<!-- Seta o hidden para carregar a UF relacionada -->
			<adsm:propertyMapping 
				relatedProperty="_idUfOrigem" 
				modelProperty="endereco.municipio.unidadeFederativa.idUnidadeFederativa" 
			/>
		 
			<adsm:textbox 
				dataType="text" 
				property="filialByIdFilialOrigem.pessoa.nmFantasia" 
				serializable="false"
				size="30" maxLength="30" disabled="true"
			/>
        </adsm:lookup>
        
		<adsm:hidden property="_idUfOrigem" serializable="false"/>		
		<adsm:hidden property="municipioByIdMunicipioOrigem.idMunicipio" />

		<adsm:lookup 
			service="lms.vendas.manterPrazosEntregaClienteAction.findLookupMunicipioFilial" 
			action="/municipios/manterMunicipiosAtendidos" 
			property="municipioByIdMunicipioOrigem" 
			idProperty="municipio.idMunicipio" 
			criteriaProperty="municipio.nmMunicipio" 
			exactMatch="false"
			minLengthForAutoPopUpSearch="2"
			label="municipio" 
			serializable="false"
			onchange="return municipioChange('Origem');"
			onDataLoadCallBack="municipioOrigem"
			onPopupSetValue="MunicipioOrigem_PopupSetValue"
			dataType="text" labelWidth="10%" width="37%" maxLength="60" size="30">
			
			<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.idFilial" modelProperty="filial.idFilial" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.sgFilial" modelProperty="filial.sgFilial" addChangeListener="false" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" addChangeListener="false" inlineQuery="false"/>
			
			<adsm:propertyMapping criteriaProperty="paisByIdPaisOrigem.nmPais" modelProperty="municipio.unidadeFederativa.pais.nmPais" addChangeListener="false" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="paisByIdPaisOrigem.idPais" modelProperty="municipio.unidadeFederativa.pais.idPais" addChangeListener="false"/>
			
			<adsm:propertyMapping criteriaProperty="unidadeFederativaByIdUfOrigem.idUnidadeFederativa" modelProperty="municipio.unidadeFederativa.idUnidadeFederativa" addChangeListener="false"/>			
			<adsm:propertyMapping criteriaProperty="sgUFOrigem" modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="dsUFOrigem" modelProperty="municipio.unidadeFederativa.nmUnidadeFederativa" addChangeListener="false" inlineQuery="false"/>			
			
			<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.idFilial" modelProperty="filial.idFilial"/>
			<adsm:propertyMapping relatedProperty="municipioByIdMunicipioOrigem.idMunicipio" modelProperty="municipio.idMunicipio"/>
			<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.sgFilial" modelProperty="filial.sgFilial"/>
			<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia"/>
			
			  	<!-- Seta o Nome Pais automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="paisByIdPaisOrigem.nmPais" 
				modelProperty="municipio.unidadeFederativa.pais.nmPais" 
			/>
			<!-- Seta o ID Pais automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="paisByIdPaisOrigem.idPais" 
				modelProperty="municipio.unidadeFederativa.pais.idPais" 
			/>
			<!-- Seta a Zona automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="zonaByIdZonaOrigem.idZona" 
				modelProperty="municipio.unidadeFederativa.pais.zona.idZona" 
			/>
			
			<!-- Seta o hidden para carregar a UF relacionada -->
			<adsm:propertyMapping 
				relatedProperty="_idUfOrigem" 
				modelProperty="municipio.unidadeFederativa.idUnidadeFederativa" 
			/>
		</adsm:lookup>

		<adsm:lookup 
			service="lms.vendas.manterPrazosEntregaClienteAction.findLookupAeroporto" 
			action="/municipios/manterAeroportos" 
			property="aeroportoByIdAeroportoOrigem" 
			idProperty="idAeroporto" 
			dataType="text" 
			criteriaProperty="sgAeroporto" 
			label="aeroporto" 
			onchange="return changeAeroporto('Origem');"
			onDataLoadCallBack="aeroportoOrigem"
			onPopupSetValue="changeAeroportoOrigemPopup"
			size="5" maxLength="3" width="80%" labelWidth="16%" >

			
			<adsm:propertyMapping criteriaProperty="tpSituacaoAtivo" modelProperty="tpSituacao" />

			<!-- Seta o Nome Pais automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="paisByIdPaisOrigem.nmPais" 
				modelProperty="endereco.municipio.unidadeFederativa.pais.nmPais" 
			/>
			<!-- Seta o ID Pais automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="paisByIdPaisOrigem.idPais" 
				modelProperty="endereco.municipio.unidadeFederativa.pais.idPais" 
			/>
			<!-- Seta a Zona automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="zonaByIdZonaOrigem.idZona" 
				modelProperty="endereco.municipio.unidadeFederativa.pais.zona.idZona" 
			/>
			
			<!-- Seta o hidden para carregar a UF relacionada -->
			<adsm:propertyMapping 
				relatedProperty="_idUfOrigem" 
				modelProperty="endereco.municipio.unidadeFederativa.idUnidadeFederativa" 
			/>

		   	<adsm:propertyMapping 
				relatedProperty="aeroportoByIdAeroportoOrigem.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa"
			/>
            <adsm:textbox 
				dataType="text" 
				property="aeroportoByIdAeroportoOrigem.pessoa.nmPessoa" 
				serializable="false"
				size="30" maxLength="30" disabled="true"
			/>
        </adsm:lookup>

		<adsm:combobox 
			service="lms.vendas.manterPrazosEntregaClienteAction.findTipoLocalizacao" 
			property="tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio" 
			optionLabelProperty="dsTipoLocalizacaoMunicipio" 
			optionProperty="idTipoLocalizacaoMunicipio" 
			label="tipoLocalizacao" 
			onchange="return changeTpLocalizacao('Origem');"
			labelWidth="16%" width="84%" boxWidth="200"
			onlyActiveValues="true"
		/>

		<adsm:section caption="destino" />

		<adsm:combobox 
			service="lms.vendas.manterPrazosEntregaClienteAction.findZona" 
			property="zonaByIdZonaDestino.idZona" 
			optionLabelProperty="dsZona" 
			optionProperty="idZona" 
			label="zona"
			onchange="limpaZona('Destino')"
			onlyActiveValues="true"
			labelWidth="16%" width="19%"  boxWidth="130"
		/>

		<adsm:lookup
			service="lms.vendas.manterPrazosEntregaClienteAction.findLookupPais" 
			action="/municipios/manterPaises" 
			property="paisByIdPaisDestino" 
			idProperty="idPais" 
			criteriaProperty="nmPais" 
			label="pais"
			minLengthForAutoPopUpSearch="3" 
			exactMatch="false"
			onchange="return changePais('Destino');"
			dataType="text" labelWidth="6%" width="25%" size="25" maxLength="60">

			<adsm:propertyMapping criteriaProperty="tpSituacaoAtivo" modelProperty="tpSituacao" />
			
			<adsm:propertyMapping 
				addChangeListener="false"
				criteriaProperty="zonaByIdZonaDestino.idZona" 
				modelProperty="zona.idZona"
			/>
			<adsm:propertyMapping 
				relatedProperty="zonaByIdZonaDestino.idZona" 
				modelProperty="zona.idZona" 
			/>
		</adsm:lookup>
		
		<adsm:hidden property="dsUFDestino" serializable="false"/>
		<adsm:hidden property="sgUFDestino" serializable="false"/>

		<adsm:combobox 
			service="lms.vendas.manterPrazosEntregaClienteAction.findUnidadeFederativaByPais" 
			property="unidadeFederativaByIdUfDestino.idUnidadeFederativa" 
			optionLabelProperty="siglaDescricao" 
			optionProperty="idUnidadeFederativa" 
			label="uf" 
			onlyActiveValues="true"
			onDataLoadCallBack="ufDestinoOnDataLoad"
			onchange="return changeUF('Destino');"
			labelWidth="5%" width="29%" boxWidth="150">

			<adsm:propertyMapping 
				criteriaProperty="paisByIdPaisDestino.idPais" 
				modelProperty="pais.idPais"
			/>
		</adsm:combobox>



		<adsm:lookup 
			service="lms.vendas.manterPrazosEntregaClienteAction.findLookupFilial" 
			action="/municipios/manterFiliais" 
			property="filialByIdFilialDestino" 
			idProperty="idFilial" 
			criteriaProperty="sgFilial" 
			exactMatch="false"
			minLengthForAutoPopUpSearch="3"			
			label="filial"
			onDataLoadCallBack="filialDestino"
			onchange="return changeFilial('Destino');"
			onPopupSetValue="changeFilialDestinoPopup"
			dataType="text" size="5" maxLength="3" labelWidth="16%" width="37%">
			
			<adsm:propertyMapping modelProperty="tpAcesso" 
		   		criteriaProperty="tpAcessoFilial"/>
			
			<adsm:propertyMapping modelProperty="historicoFiliais.vigenteEm" 
				criteriaProperty="dataAtual" />
			
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" 
		   		relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia"/>
		    <!-- Seta o Nome Pais automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="paisByIdPaisDestino.nmPais" 
				modelProperty="endereco.municipio.unidadeFederativa.pais.nmPais" 
			/>
			<!-- Seta o ID Pais automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="paisByIdPaisDestino.idPais" 
				modelProperty="endereco.municipio.unidadeFederativa.pais.idPais" 
			/>
			<!-- Seta a Zona automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="zonaByIdZonaDestino.idZona" 
				modelProperty="endereco.municipio.unidadeFederativa.pais.zona.idZona" 
			/>
			
			<!-- Seta o hidden para carregar a UF relacionada -->
			<adsm:propertyMapping 
				relatedProperty="_idUfDestino" 
				modelProperty="endereco.municipio.unidadeFederativa.idUnidadeFederativa" 
			/>
			
			<adsm:textbox 
				dataType="text" 
				property="filialByIdFilialDestino.pessoa.nmFantasia" 
				serializable="false"
				size="30" maxLength="30" disabled="true"
			/>
        </adsm:lookup>
        
        <adsm:hidden property="_idUfDestino" serializable="false"/>
        <adsm:hidden property="municipioByIdMunicipioDestino.idMunicipio"/>
        
		<adsm:lookup 
			service="lms.vendas.manterPrazosEntregaClienteAction.findLookupMunicipioFilial" 
			action="/municipios/manterMunicipiosAtendidos" 
			property="municipioByIdMunicipioDestino" 
			idProperty="municipio.idMunicipio" 
			criteriaProperty="municipio.nmMunicipio" 
			exactMatch="false"
			minLengthForAutoPopUpSearch="2"
			label="municipio" 
			serializable="false"
			onchange="return municipioChange('Destino');"
			onDataLoadCallBack="municipioDestino"
			onPopupSetValue="MunicipioDestino_PopupSetValue"
			dataType="text" labelWidth="10%" width="37%" maxLength="60" size="30">
			
			<adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.idFilial" modelProperty="filial.idFilial" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.sgFilial" modelProperty="filial.sgFilial" addChangeListener="false" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" addChangeListener="false" inlineQuery="false"/>
			
			<adsm:propertyMapping criteriaProperty="paisByIdPaisDestino.nmPais" modelProperty="municipio.unidadeFederativa.pais.nmPais" addChangeListener="false" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="paisByIdPaisDestino.idPais" modelProperty="municipio.unidadeFederativa.pais.idPais" addChangeListener="false"/>			
			
			<adsm:propertyMapping criteriaProperty="unidadeFederativaByIdUfDestino.idUnidadeFederativa" modelProperty="municipio.unidadeFederativa.idUnidadeFederativa" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="sgUFDestino" modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="dsUFDestino" modelProperty="municipio.unidadeFederativa.nmUnidadeFederativa" addChangeListener="false" inlineQuery="false"/>			
			
			<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.idFilial" modelProperty="filial.idFilial"/>
			<adsm:propertyMapping relatedProperty="municipioByIdMunicipioDestino.idMunicipio" modelProperty="municipio.idMunicipio"/>
			<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.sgFilial" modelProperty="filial.sgFilial"/>
			<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia"/>
			<!-- Seta o Nome Pais automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="paisByIdPaisDestino.nmPais" 
				modelProperty="municipio.unidadeFederativa.pais.nmPais" 
			/>
			<!-- Seta o ID Pais automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="paisByIdPaisDestino.idPais" 
				modelProperty="municipio.unidadeFederativa.pais.idPais" 
			/>
			<!-- Seta a Zona automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="zonaByIdZonaDestino.idZona" 
				modelProperty="municipio.unidadeFederativa.pais.zona.idZona" 
			/>
			
			<!-- Seta o hidden para carregar a UF relacionada -->
			<adsm:propertyMapping 
				relatedProperty="_idUfDestino" 
				modelProperty="municipio.unidadeFederativa.idUnidadeFederativa" 
			/>
		</adsm:lookup>



		<adsm:lookup 
			service="lms.vendas.manterPrazosEntregaClienteAction.findLookupAeroporto" 
			action="/municipios/manterAeroportos" 
			property="aeroportoByIdAeroportoDestino" 
			idProperty="idAeroporto" 
			criteriaProperty="sgAeroporto" 
			dataType="text" 
			label="aeroporto" 
			onchange="return changeAeroporto('Destino');"
			onDataLoadCallBack="aeroportoDestino"
			onPopupSetValue="changeAeroportoDestinoPopup"
			size="5" maxLength="3" width="34%" labelWidth="16%" >
			
			
			<adsm:propertyMapping criteriaProperty="tpSituacaoAtivo" modelProperty="tpSituacao" />
			
			<!-- Seta o Nome Pais automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="paisByIdPaisDestino.nmPais" 
				modelProperty="endereco.municipio.unidadeFederativa.pais.nmPais" 
			/>
			<!-- Seta o ID Pais automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="paisByIdPaisDestino.idPais" 
				modelProperty="endereco.municipio.unidadeFederativa.pais.idPais" 
			/>
			<!-- Seta a Zona automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="zonaByIdZonaDestino.idZona" 
				modelProperty="endereco.municipio.unidadeFederativa.pais.zona.idZona" 
			/>
			
			<!-- Seta o hidden para carregar a UF relacionada -->
			<adsm:propertyMapping 
				relatedProperty="_idUfDestino" 
				modelProperty="endereco.municipio.unidadeFederativa.idUnidadeFederativa" 
			/>

		   	<adsm:propertyMapping 
				relatedProperty="aeroportoByIdAeroportoDestino.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa"
			/>

            <adsm:textbox 
				dataType="text" 
				property="aeroportoByIdAeroportoDestino.pessoa.nmPessoa" 
				serializable="false"
				size="30" maxLength="30" disabled="true"
			/>
        </adsm:lookup>

		<adsm:combobox 
			service="lms.vendas.manterPrazosEntregaClienteAction.findTipoLocalizacao" 
			property="tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.idTipoLocalizacaoMunicipio" 
			optionLabelProperty="dsTipoLocalizacaoMunicipio" 
			optionProperty="idTipoLocalizacaoMunicipio" 
			label="tipoLocalizacao" 
			onchange="return changeTpLocalizacao('Destino');"
			labelWidth="16%" width="84%" boxWidth="200"
			onlyActiveValues="true"
		/>


		<adsm:section caption="prazoRota"/>
		
		<adsm:combobox property="servico.idServico" boxWidth="250" 
		label="servico" onlyActiveValues="true" optionLabelProperty="dsServico" 
		optionProperty="idServico" service="lms.configuracoes.servicoService.find" 
		required="true"
		labelWidth="16%"
		width="37%"/>

		<adsm:textbox  property="nrPrazo" label="prazoEntrega2" dataType="integer" mask="###0" size="4" 
		maxLength="4" unit="horas" required="true"
		width="20%"/>


		<adsm:section caption="responsavelFrete"/>

		<adsm:combobox property="tpResponsavelFrete"
			   label="responsavel"
			   domain="DM_TIPO_RESPONSAVEL_FRETE"/>


		<adsm:buttonBar>
			<adsm:storeButton id="storeButton"/>		
			<adsm:newButton id="btnLimpar"/>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
	
	document.getElementById("tpSituacaoAtivo").masterLink = "true";
	
	function myDataLoadCallBack_cb(data){
		onDataLoad_cb(data);
		
		if(data.municipioByIdMunicipioOrigem != null){
			setElementValue("municipioByIdMunicipioOrigem.municipio.idMunicipio",  data.municipioByIdMunicipioOrigem.idMunicipio);
			setElementValue("municipioByIdMunicipioOrigem.municipio.nmMunicipio",  data.municipioByIdMunicipioOrigem.nmMunicipio);
		}
		
		if(data.municipioByIdMunicipioDestino != null){
			setElementValue("municipioByIdMunicipioDestino.municipio.idMunicipio",  data.municipioByIdMunicipioDestino.idMunicipio);
			setElementValue("municipioByIdMunicipioDestino.municipio.nmMunicipio",  data.municipioByIdMunicipioDestino.nmMunicipio);
		}
		
		validaPermissao();
	}
	
	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function validaPermissao(){
		if (getTabGroup(document).getTab("pesq").getElementById("permissao").value!="true") {
			setDisabled("storeButton", true);
			setDisabled("btnLimpar", true);
			setDisabled("removeButton", true);
		}
	}

//	busca a data atual para as lookups de filial
	function findDataAtual(data, error){
	
		var dados = new Array();
    
        var sdo = createServiceDataObject("lms.vendas.manterPrazosEntregaClienteAction.findDataAtual",
                                          "setaDataAtual",
                                          dados);
        xmit({serviceDataObjects:[sdo]});
		
	}
	
	//seta a data atual
	function setaDataAtual_cb(data,error){

		setElementValue("dataAtual",data._value);
		setFocusOnFirstFocusableField(document);
	
	}
	
	//executado no inicio da tela
	function initWindow(eventObj){
	
		if (eventObj.name == "tab_click" || eventObj.name == "gridRow_click"){
			validaPermissao();
			setElementValue("idFilial",getTabGroup(document).getTab("pesq").getElementById("idFilial").value)
		}
		
		if( eventObj.name != 'storeButton' ){		
			findDataAtual();					
		} else {
			setFocus('btnLimpar',true,true);
		}		
			
	}
	
	function resetPais(tipo){
		setElementValue("paisByIdPais" + tipo + ".idPais","");
		setElementValue("paisByIdPais" + tipo + ".nmPais","");
	}	
	
	function resetFilial(tipo) {
		setElementValue("filialByIdFilial" + tipo + ".idFilial", ""); 
		setElementValue("filialByIdFilial" + tipo + ".pessoa.nmFantasia", ""); 
		setElementValue("filialByIdFilial" + tipo + ".sgFilial", ""); 
	}
	
	function resetMunicipio(tipo) {
		setElementValue("municipioByIdMunicipio" + tipo + ".municipio.idMunicipio", "");
		setElementValue("municipioByIdMunicipio" + tipo + ".idMunicipio", "");
		setElementValue("municipioByIdMunicipio" + tipo + ".municipio.nmMunicipio", "");			
	}
	
	function resetAeroporto(tipo) {
		setElementValue("aeroportoByIdAeroporto" + tipo + ".idAeroporto", ""); 
		setElementValue("aeroportoByIdAeroporto" + tipo + ".pessoa.nmPessoa", ""); 
		setElementValue("aeroportoByIdAeroporto" + tipo + ".sgAeroporto", ""); 
	}
	
	function limpaZona(tipo) {
		resetPais(tipo);
		setElementValue("unidadeFederativaByIdUf" + tipo + ".idUnidadeFederativa","");
		resetFilial(tipo);
		resetMunicipio(tipo);
		resetAeroporto(tipo);
		setElementValue("_idUf" + tipo + "","");
		setElementValue("dsUf" + tipo,"");
		setElementValue("sgUf" + tipo,"");		
		setElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacao" + tipo + ".idTipoLocalizacaoMunicipio","");
		notifyElementListeners({e:document.getElementById("paisByIdPais" + tipo + ".idPais")});
	}
	
	/*
	*  OnChange da lookup de Pais.
	*/
	function changePais(tipo){
		var idZona = getElementValue("zonaByIdZona" + tipo + ".idZona");
		var r = true;
		eval("r = paisByIdPais" + tipo + "_nmPaisOnChangeHandler();");
		setElementValue("unidadeFederativaByIdUf" + tipo + ".idUnidadeFederativa", "");
		setElementValue("_idUf" + tipo + "","");
		setElementValue("dsUf" + tipo,"");
		setElementValue("sgUf" + tipo,"");
		changeUF(tipo);
		setElementValue("zonaByIdZona" + tipo + ".idZona", idZona);
		return r;
	}
	
	/*
	*  OnChange da combo de UF.
	*/
	function changeUF(tipo){
		resetFilial(tipo);
		resetMunicipio(tipo);
		resetAeroporto(tipo);
		setElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacao" + tipo + ".idTipoLocalizacaoMunicipio", "");
		montaDadosUF(null,tipo);
		return true;
	}
	
	function montaDadosUF(dados, tipo){
		var uf = getElement("unidadeFederativaByIdUf" + tipo + ".idUnidadeFederativa");
		var dsUF;
		var sgUF;
		
		if( uf.selectedIndex > 0 ){		
		
			if( dados != undefined && dados.idUnidadeFederativa != undefined ){
				dsUF = dados.dsUnidadeFederativa;
				sgUF = dados.sgUnidadeFederativa;
			} else {		
				dsUF = uf.options[uf.selectedIndex].text.substr(5);
				sgUF = uf.options[uf.selectedIndex].text.substr(0,2);
			}
			
		} else {
			dsUF = '';
			sgUF = '';
		}
		
		setElementValue('dsUF' + tipo,dsUF);
		setElementValue('sgUF' + tipo,sgUF);
	}
	
	/*
	*  Callback para combo de UF origem.
	*/
	function ufOrigemOnDataLoad_cb(dados, erro){
		var retorno = unidadeFederativaByIdUfOrigem_idUnidadeFederativa_cb(dados);
		var idUf = getElementValue("_idUfOrigem");
		if (idUf != null && idUf != ""){
			setElementValue("unidadeFederativaByIdUfOrigem.idUnidadeFederativa",idUf);
		}
		
		if( dados.length == 1 ){
			dados = dados[0];
		} else {
			dados = null;
		}		
		montaDadosUF(dados, "Origem");
		return retorno;
	}
	
	/*
	*  Callback para combo de UF destino.
	*/
	function ufDestinoOnDataLoad_cb(dados, erro){
		var retorno = unidadeFederativaByIdUfDestino_idUnidadeFederativa_cb(dados);
		var idUf = getElementValue("_idUfDestino");
		if (idUf != null && idUf != ""){
			setElementValue("unidadeFederativaByIdUfDestino.idUnidadeFederativa",idUf);
		}
		
		if( dados.length == 1 ){
			dados = dados[0];
		} else {
			dados = null;
		}		
		montaDadosUF(dados, "Destino");
		return retorno;
	}
	
	/*
	*  Callback para lookup de f origem.
	*/	
	function filialOrigem_cb(data) {
		var retorno = lookupExactMatch({e:document.getElementById("filialByIdFilialOrigem.idFilial"), data:data });
		if( retorno == true ){
			notifyElementListeners({e:document.getElementById("paisByIdPaisOrigem.idPais")});
		}
		return retorno;
	}	
	
	/*
	*  Callback para lookup de f origem.
	*/	
	function filialDestino_cb(data) {		
		var retorno = lookupExactMatch({e:document.getElementById("filialByIdFilialDestino.idFilial"), data:data });
		if( retorno == true ){
			notifyElementListeners({e:document.getElementById("paisByIdPaisDestino.idPais")});
		}
		return retorno;
	}
	
	/*
	*	OnChange da Filial
	*/
	function changeFilial(tipo){
		var r = true;
		if (getElementValue("filialByIdFilial" + tipo + ".sgFilial") == "") {
			resetFilial(tipo);
		} else {
			eval("r = filialByIdFilial" + tipo + "_sgFilialOnChangeHandler()");
		}
		resetMunicipio(tipo);
		resetAeroporto(tipo);
		setElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacao" + tipo + ".idTipoLocalizacaoMunicipio", "");
		return r;
	}
	
	/*
	*	OnChange da Filial Origem
	*/
	function changeFilialOrigemPopup(data){
		changeFilialPopup(getNestedBeanPropertyValue(data,"idFilial"), "Origem");
	}
	
	/*
	*	OnChange da Filial Destino
	*/
	function changeFilialDestinoPopup(data){
		changeFilialPopup(getNestedBeanPropertyValue(data,"idFilial"), "Destino");		
	}
	
	function changeFilialPopup(idFilial, tipo) {
		if (idFilial != "")
			findEndereco(idFilial, tipo);
		resetMunicipio(tipo);
		resetAeroporto(tipo);
		setElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacao" + tipo + ".idTipoLocalizacaoMunicipio", "");
	}
	
	function findEndereco(idPessoa, tipo) {
		 var sdo = createServiceDataObject("lms.vendas.manterPrazosEntregaClienteAction.findEndereco", "endereco" + tipo, {idPessoa:idPessoa});
         xmit({serviceDataObjects:[sdo]});
	}

	function enderecoOrigem_cb(dados, erros) {
		configEndereco(dados, "Origem");
		notifyElementListeners({e:document.getElementById("paisByIdPaisOrigem.idPais")});
	}	
		
	function enderecoDestino_cb(dados, erros) {
		configEndereco(dados, "Destino");
		notifyElementListeners({e:document.getElementById("paisByIdPaisDestino.idPais")});
	}
	
	function configEndereco(dados, tipo) {
		var uf = getNestedBeanPropertyValue(dados, "municipio.unidadeFederativa.idUnidadeFederativa");
	  	var	idPais = getNestedBeanPropertyValue(dados, "municipio.unidadeFederativa.pais.idPais");
  		var nmPais = getNestedBeanPropertyValue(dados, "municipio.unidadeFederativa.pais.nmPais");
  		var	idZona = getNestedBeanPropertyValue(dados, "municipio.unidadeFederativa.pais.zona.idZona");
   		setElementValue("_idUf" + tipo, uf);
   		setElementValue("paisByIdPais" + tipo + ".idPais", idPais);
   		setElementValue("paisByIdPais" + tipo + ".nmPais", nmPais);
   		setElementValue("zonaByIdZona" + tipo + ".idZona", idZona);
	}
	
	/*
	*  OnChange da lookup de Municipio
	*/
	function municipioChange(tipo){
		var r = true;
		if (getElementValue("municipioByIdMunicipio" + tipo + ".municipio.nmMunicipio") != ""){
			eval("r = municipioByIdMunicipio"+ tipo + "_municipio_nmMunicipioOnChangeHandler()");
		} else {
			resetMunicipio(tipo);
		}
		return r;
	}
	
	function municipioOrigem_cb(data) {
		var retorno = lookupExactMatch({e:document.getElementById("municipioByIdMunicipioOrigem.municipio.idMunicipio"), data:data, callBack:'municipioOrigemLikeEndMatch'});
		if (data != undefined && data.length == 1) 	{
	    	eventMunicipio("Origem");
		}		
		return retorno;
	}
	
	function municipioDestino_cb(data) {
		var retorno = lookupExactMatch({e:document.getElementById("municipioByIdMunicipioDestino.municipio.idMunicipio"), data:data, callBack:'municipioDestinoLikeEndMatch'});
		if (data != undefined && data.length == 1) {
	    	eventMunicipio("Destino");
		}
		return retorno;
	}
	
	function municipioOrigemLikeEndMatch_cb(data){
		var retorno = lookupLikeEndMatch({e:document.getElementById("municipioByIdMunicipioOrigem.municipio.idMunicipio"), data:data});
		if (data != undefined && data.length == 1) {
		  eventMunicipio("Origem");
		}
		return retorno;
	}
	
	function municipioDestinoLikeEndMatch_cb(data){
		var retorno = lookupLikeEndMatch({e:document.getElementById("municipioByIdMunicipioDestino.municipio.idMunicipio"), data:data});
		if (data != undefined && data.length == 1) {
		   eventMunicipio("Destino");
		}
		return retorno;
	}
	
	/*
	*  OnPopupSetValue da lookup de Municipio origem.
	*/
	function MunicipioOrigem_PopupSetValue(dados){
  	  	configEndereco(dados, "Origem");
		eventMunicipio("Origem");
	}
	
	/*
	*  OnPopupSetValue da lookup de Municipio destino.
	*/
	function MunicipioDestino_PopupSetValue(dados){
  		configEndereco(dados, "Destino");
		eventMunicipio("Destino");
	}	
	
	function eventMunicipio(tipo){
		notifyElementListeners({e:document.getElementById("paisByIdPais" + tipo + ".idPais")});
		resetAeroporto(tipo);
		setElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacao" + tipo + ".idTipoLocalizacaoMunicipio", "");
	}
	
	/*
	*  OnChange da combo de Tipo de Localização.
	*/
	function changeTpLocalizacao(tipo){
		resetFilial(tipo);
		resetMunicipio(tipo);
		resetAeroporto(tipo);
		return true;
	}
	
	/*
	*  OnChange da lookup de Aeroporto.
	*/
	function changeAeroporto(tipo){
		var r = true;
		if (getElementValue("aeroportoByIdAeroporto" + tipo + ".sgAeroporto") != "") {
		    eval("r = aeroportoByIdAeroporto" + tipo + "_sgAeroportoOnChangeHandler()");
		} else {
			resetAeroporto(tipo);
		}
		return r;
	}
	
	/*
	*	OnPopupSetValue do Aeroporto origem
	*/
	function changeAeroportoOrigemPopup(data){
		return changeAeroportoPopup(getNestedBeanPropertyValue(data,"idAeroporto"), "Origem");
	}
	
	/*
	*	OnChange do Aeroporto Destino
	*/
	function changeAeroportoDestinoPopup(data){
		return changeAeroportoPopup(getNestedBeanPropertyValue(data,"idAeroporto"), "Destino");
	}
	
	function changeAeroportoPopup(idPessoa, tipo) {
		if (idPessoa != "") {
			 findEndereco(idPessoa, tipo);
		}
		resetFilial(tipo);
		resetMunicipio(tipo);
		setElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacao" + tipo + ".idTipoLocalizacaoMunicipio", "");
	}
	
	/*
	*  Callback para lookup de Aeroporto origem.
	*/	
	function aeroportoOrigem_cb(data) {
		var retorno = lookupExactMatch({e:document.getElementById("aeroportoByIdAeroportoOrigem.idAeroporto"), data:data });

		if( retorno == true ){
				
			notifyElementListeners({e:document.getElementById("paisByIdPaisOrigem.idPais")});
			resetFilial("Origem");
			resetMunicipio("Origem");
			setElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio", "");
			
		} else {		
			limpaZona("Origem");
			resetValue('zonaByIdZonaOrigem.idZona');
		}
		
		return retorno;
	}

	/*
	*  Callback para lookup de Aeroporto Destino.
	*/	
	function aeroportoDestino_cb(data) {	
		var retorno = lookupExactMatch({e:document.getElementById("aeroportoByIdAeroportoDestino.idAeroporto"), data:data });
		
		if( retorno == true ){
		
			notifyElementListeners({e:document.getElementById("paisByIdPaisDestino.idPais")});
			resetFilial("Destino");
			resetMunicipio("Destino");
			setElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.idTipoLocalizacaoMunicipio", "");
			
		} else {
			limpaZona("Destino");
			resetValue('zonaByIdZonaDestino.idZona');
		}
		
		return retorno;
	}		
	
	function validateTab() {
		if( getElementValue("zonaByIdZonaOrigem.idZona") == "" &&
			getElementValue("paisByIdPaisOrigem.idPais") == "" &&
			getElementValue("filialByIdFilialOrigem.idFilial") == "" &&
			getElementValue("unidadeFederativaByIdUfOrigem.idUnidadeFederativa") == "" &&
			getElementValue("municipioByIdMunicipioOrigem.municipio.idMunicipio") == "" &&
			getElementValue("aeroportoByIdAeroportoOrigem.idAeroporto") == "" &&
			getElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio") == "") {
			alert(i18NLabel.getLabel("LMS-01149"));
			setFocus("zonaByIdZonaOrigem.idZona");
			return false;
		}

		if( getElementValue("zonaByIdZonaDestino.idZona") == "" &&
			getElementValue("paisByIdPaisDestino.idPais") == "" &&
			getElementValue("filialByIdFilialDestino.idFilial") == "" &&
			getElementValue("unidadeFederativaByIdUfDestino.idUnidadeFederativa") == "" &&
			getElementValue("municipioByIdMunicipioDestino.municipio.idMunicipio") == "" &&
			getElementValue("aeroportoByIdAeroportoDestino.idAeroporto") == "" &&
			getElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.idTipoLocalizacaoMunicipio") == "") {
			alert(i18NLabel.getLabel("LMS-01150"));
			setFocus("zonaByIdZonaDestino.idZona");
			return false;
		}
	
		return validateTabScript(document.forms);
	}
    
</script>