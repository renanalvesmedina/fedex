<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterPotencialComercializacaoClienteAction" >
	<adsm:form action="/vendas/manterPotencialComercializacaoCliente" idProperty="idPotencialComercialCliente" onDataLoadCallBack="myDataLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-01147" />
		<adsm:include key="LMS-01148" />
	</adsm:i18nLabels>

		<adsm:hidden property="idFilial" serializable="false"/>	

		<adsm:hidden property="cliente.idCliente"/>			
		<adsm:textbox
			dataType="text"
			property="cliente.pessoa.nrIdentificacao"
			disabled="true"
			label="cliente"
			size="20"
			maxLength="20"
			width="82%"
			labelWidth="18%">
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" disabled="true"size="60"/>
		</adsm:textbox>	

		<adsm:lookup service="lms.municipios.paisService.findLookup" 
			property="paisOrigem" 
			label="paisOrigem"
			idProperty="idPais"
			criteriaProperty="nmPais"					 
			dataType="text" 
			maxLength="60"
			action="/municipios/manterPaises"
			exactMatch="false"
			minLengthForAutoPopUpSearch="3"
			required="true"
			size="25"
			labelWidth="18%"
			width="25%"
			onPopupSetValue="setaUFOrigemOnPopup"
			onDataLoadCallBack="paisOrigem">
			<adsm:propertyMapping criteriaProperty="statusAtivo" modelProperty="tpSituacao"/>
		</adsm:lookup>						

		<adsm:combobox property="unidadeFederativaByIdUfOrigem.idUnidadeFederativa" 
			label="ufOrigem" 
			labelWidth="20%" 
			width="30%" 
			optionLabelProperty="sgUnidadeFederativa" 
			optionProperty="idUnidadeFederativa"
			service="lms.municipios.unidadeFederativaService.findComboAtivo" 
			autoLoad="false"
			onlyActiveValues="true"
			onDataLoadCallBack="ufOrigem">
			<adsm:propertyMapping criteriaProperty="paisOrigem.idPais" modelProperty="pais.idPais"/>
			<adsm:propertyMapping criteriaProperty="statusAtivo" modelProperty="tpSituacao"/>
		</adsm:combobox>	

		<adsm:combobox property="tipoLocalizacaoMunicipioByOdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio" 
			label="tipoLocalizacaoOrigem" 
			width="85%"
			labelWidth="18%" 
			boxWidth="230"
			optionLabelProperty="dsTipoLocalizacaoMunicipio" 
			optionProperty="idTipoLocalizacaoMunicipio" 
			service="lms.municipios.tipoLocalizacaoMunicipioService.find" 
			onlyActiveValues="true"/>

		<adsm:lookup service="lms.municipios.paisService.findLookup" 
			property="paisDestino" 
			label="paisDestino"
			idProperty="idPais"
			criteriaProperty="nmPais"					 
			dataType="text" 
			maxLength="60"
			action="/municipios/manterPaises"
			exactMatch="false"
			minLengthForAutoPopUpSearch="3"
			required="true"
			size="25"
			labelWidth="18%" 
			width="25%"
			onPopupSetValue="setaUFDestinoOnPopup"
			onDataLoadCallBack="paisDestino">
			<adsm:propertyMapping criteriaProperty="statusAtivo" modelProperty="tpSituacao"/>
		</adsm:lookup>

		<adsm:combobox property="unidadeFederativaByIdUfDestino.idUnidadeFederativa" 
			label="ufDestino" 
			labelWidth="20%" 
			width="30%" 
			optionLabelProperty="sgUnidadeFederativa" 
			optionProperty="idUnidadeFederativa"
			service="lms.municipios.unidadeFederativaService.findComboAtivo" 
			autoLoad="false"
			onlyActiveValues="true"
			onDataLoadCallBack="ufDestino">
			<adsm:propertyMapping criteriaProperty="paisDestino.idPais" modelProperty="pais.idPais"/>
			<adsm:propertyMapping criteriaProperty="statusAtivo" modelProperty="tpSituacao"/>
		</adsm:combobox>	

		<adsm:combobox property="tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.idTipoLocalizacaoMunicipio" 
			label="tipoLocalizacaoDestino" 
			labelWidth="18%" 
			width="85%" 
			boxWidth="230"
			optionLabelProperty="dsTipoLocalizacaoMunicipio" 
			optionProperty="idTipoLocalizacaoMunicipio" 
			service="lms.municipios.tipoLocalizacaoMunicipioService.find" 
			onlyActiveValues="true"/>

		<adsm:combobox property="tpFrete" domain="DM_TIPO_FRETE" label="tipoFrete" labelWidth="18%" width="80%"/>
		<adsm:combobox property="tpModal" domain="DM_MODAL" label="modal" labelWidth="18%" width="25%"/>
		<adsm:combobox property="tpAbrangencia" domain="DM_ABRANGENCIA" label="abrangencia" labelWidth="20%" width="36%"/>

		<adsm:textbox property="dsTransportadora" label="transportadora" dataType="text" size="53" maxLength="60" 
			labelWidth="18%" width="82%"/>

		<adsm:textbox property="pcDetencao" label="percentualDetencao" dataType="decimal" size="12" maxLength="5" mask="##0.00"
			labelWidth="18%" width="25%" />

		<adsm:textbox property="nrNotasFiscais" label="quantidadeNF" dataType="integer" size="12" maxLength="5" 
			labelWidth="20%" width="36%"/>

		<adsm:combobox property="moeda.idMoeda" 
			service="lms.vendas.manterPotencialComercializacaoClienteAction.findMoeda"
			optionProperty="idMoeda"
			optionLabelProperty="descricao" 
			label="moeda"
			boxWidth="85"
			labelWidth="18%" 
			width="82%" 
			required="true"/>

		<adsm:textbox property="vlTotalMercadoria" label="valorMercadoria" dataType="currency" size="12" maxLength="20" mask="###,###,###,###,##0.00"
			labelWidth="18%" width="25%"/>

		<adsm:textbox property="vlFaturamentoPotencial" label="valorPotencialFaturamento" dataType="currency" size="12" maxLength="20" mask="###,###,###,###,##0.00"
			labelWidth="20%" width="36%"/>

		<adsm:textbox property="nrVolumes" label="quantidadeVolumes2" dataType="integer" maxLength="6" size="12"
			labelWidth="18%" width="25%"/>

		<adsm:textbox
			property="psTotal"
			label="pesoTotal"
			dataType="weight"
			unit="kg"
			maxLength="21"
			size="12"
			labelWidth="20%"
			width="36%"/>

		<adsm:hidden property="statusAtivo" serializable="false" value="A"/>

		<adsm:buttonBar>
			<adsm:storeButton id="storeButton"/>
			<adsm:newButton caption="novo" id="newButton" disabled="false"/>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
		
	</adsm:form>
</adsm:window>
<script language="javascript" type="text/javascript">

	var _idUFOrigem, _idUFDestino;

	/**
	* Método sobrescrito para limpar a combo de UF Origem
	*/
	function paisOrigem_nmPaisOnChangeHandler() {
		var retorno = lookupChange({e:document.forms[0].elements["paisOrigem.idPais"]});
		if( getElementValue("paisOrigem.idPais") == "" ){
			document.getElementById("unidadeFederativaByIdUfOrigem.idUnidadeFederativa").options.length = 1;
			setElementValue("unidadeFederativaByIdUfOrigem.idUnidadeFederativa", "");		
		}
		return retorno;
	}	
	
	/**
	* Método sobrescrito para limpar a combo de UF Destino
	*
	*/
	function paisDestino_nmPaisOnChangeHandler() {
		var retorno = lookupChange({e:document.forms[0].elements["paisDestino.idPais"]});
		if( getElementValue("paisDestino.idPais") == "" ){
			document.getElementById("unidadeFederativaByIdUfDestino.idUnidadeFederativa").options.length = 1;
			setElementValue("unidadeFederativaByIdUfDestino.idUnidadeFederativa", "");		
		}
		return retorno;
		
	}			

	/**
	* Método de retorno da lookup de países Origem.
	* Busca as UFs referenetes ao país retorno da pesquisa Lookup
	*/
	function paisOrigem_cb(data) {
		lookupExactMatch({e:document.getElementById("paisOrigem.idPais"), data:data, callBack:'paisOrigemLikeAndMatch'});

		var id = getNestedBeanPropertyValue(( (data[0] != undefined) ? data[0] : data), "idPais");		
		callUnidadeFederativaService(id, true, "unidadeFederativaByIdUfOrigem_idUnidadeFederativa");
	}

	/**
	* Método de pesquisa não exata pelo criterio de pesquisa de país origem
	* Busca as UFs do pais encontrado
	*/
	function paisOrigemLikeAndMatch_cb(data){
		lookupLikeEndMatch({e:document.getElementById("paisOrigem.idPais"), data:data});

		var id = getNestedBeanPropertyValue(( (data[0] != undefined) ? data[0] : data), "idPais");
		callUnidadeFederativaService(id, true, "unidadeFederativaByIdUfOrigem_idUnidadeFederativa");
	}

	/**
	* Método de retorno da lookup de países Destino.
	* Busca as UFs referenetes ao país retorno da pesquisa Lookup
	*/
	function paisDestino_cb(data) {
		lookupExactMatch({e:document.getElementById("paisDestino.idPais"), data:data, callBack:'paisDestinoLikeAndMatch'});

		var id = getNestedBeanPropertyValue(( (data[0] != undefined) ? data[0] : data), "idPais");		
		callUnidadeFederativaService(id, true, "unidadeFederativaByIdUfDestino_idUnidadeFederativa");
	}

	/**
	* Método de pesquisa não exata pelo criterio de pesquisa de país destino
	* Busca as UFs do pais encontrado
	*/
	function paisDestinoLikeAndMatch_cb(data){
		lookupLikeEndMatch({e:document.getElementById("paisDestino.idPais"), data:data});

		var id = getNestedBeanPropertyValue(( (data[0] != undefined) ? data[0] : data), "idPais");
		callUnidadeFederativaService(id, true, "unidadeFederativaByIdUfDestino_idUnidadeFederativa");
	}	
	
	/**
	* Seta a UF de Origem após pesquisa pela Popup de países
	*
	*/
	function setaUFOrigemOnPopup(data){
		callUnidadeFederativaService(data.idPais, true, "ufOrigem");
	}
	
	/**
	* Seta a UF de Origem após pesquisa pela Popup de países
	*/
	function setaUFDestinoOnPopup(data){
		callUnidadeFederativaService(data.idPais, true, "ufDestino");
	}

	function callUnidadeFederativaService(idPais, onlyActives, callBack){
		var data = new Array();
		setNestedBeanPropertyValue(data, "pais.idPais", idPais);
		if(onlyActives) {
			setNestedBeanPropertyValue(data, "tpSituacao", "A");
		}

		var sdo = createServiceDataObject(
			"lms.municipios.unidadeFederativaService.findCombo",
			callBack,
			data);
		xmit({serviceDataObjects:[sdo]});
	}

	/*
	* Criada para validar acesso do usuário 
	* logado à filial do cliente
	*/
	function myDataLoad_cb(data, error) {
		onDataLoad_cb(data, error);
		initWindow();
		if(getElementValue("unidadeFederativaByIdUfOrigem.idUnidadeFederativa") != ""){
			setElementValue("unidadeFederativaByIdUfOrigem.idUnidadeFederativa", getElementValue("unidadeFederativaByIdUfOrigem.idUnidadeFederativa"));
		}
		if(getElementValue("unidadeFederativaByIdUfDestino.idUnidadeFederativa") != ""){
			setElementValue("unidadeFederativaByIdUfDestino.idUnidadeFederativa", getElementValue("unidadeFederativaByIdUfDestino.idUnidadeFederativa"));			
		}

		/** Carrega combos */
		callUnidadeFederativaService(data.paisOrigem.idPais, false, "ufOrigem");
		callUnidadeFederativaService(data.paisDestino.idPais, false, "ufDestino");
		if(data.unidadeFederativaByIdUfOrigem != undefined) {
			_idUFOrigem = data.unidadeFederativaByIdUfOrigem.idUnidadeFederativa;
		}
		if(data.unidadeFederativaByIdUfDestino != undefined) {
			_idUFDestino = data.unidadeFederativaByIdUfDestino.idUnidadeFederativa;
		}
	}
	
	function ufOrigem_cb(data, error) {
		unidadeFederativaByIdUfOrigem_idUnidadeFederativa_cb(data, error);
		if(_idUFOrigem != undefined) {
			setElementValue("unidadeFederativaByIdUfOrigem.idUnidadeFederativa", _idUFOrigem);
			_idUFOrigem = undefined;
		}
	}

	function ufDestino_cb(data, error) {
		unidadeFederativaByIdUfDestino_idUnidadeFederativa_cb(data, error);
		if(_idUFDestino != undefined) {
			setElementValue("unidadeFederativaByIdUfDestino.idUnidadeFederativa", _idUFDestino);
			_idUFDestino = undefined;
		}
	}

	/**
	* Ao iniciar verifica se veio pelo click na aba Detalhamento,
	* ou pelo botão Novo ou pelo botão Excluir
	* 
	*/
	function initWindow(eventObj) {			
		if (getTabGroup(document).getTab("pesq").getElementById("permissao").value!="true") {
			setDisabled("storeButton", true);
			setDisabled("newButton", true);
			setDisabled("removeButton", true);
		}		
	}
</script>