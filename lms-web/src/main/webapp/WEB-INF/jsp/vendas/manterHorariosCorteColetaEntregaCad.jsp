<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterHorariosCorteColetaEntregaAction" onPageLoadCallBack="myPageLoad">
	<adsm:form action="/vendas/manterHorariosCorteColetaEntrega" idProperty="idHorarioCorteCliente" onDataLoadCallBack="myDataLoad">
		<adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="cliente.idCliente"/>
		<adsm:textbox label="cliente" property="cliente.pessoa.nrIdentificacao" dataType="text" size="20" maxLength="20" labelWidth="15%" width="85%" disabled="true" serializable="false">
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="60" maxLength="60" disabled="true" serializable="false"/>
		</adsm:textbox>	

		<adsm:combobox property="tpHorario" domain="DM_TIPO_HORARIO_CORTE" label="tipoServico" required="true" 
			onlyActiveValues="true" style="width:250px"/>
		<adsm:combobox property="servico.idServico" optionLabelProperty="dsServico" optionProperty="idServico" 
			service="lms.configuracoes.servicoService.find" label="servico" onlyActiveValues="true" style="width:260px"/>

		<adsm:hidden property="tpSituacaoAtivo" value="A" serializable="false"/>
		
		<adsm:textbox dataType="JTTime" property="hrInicial" label="horario" required="true" width="8%"/>
		<adsm:textbox dataType="JTTime" property="hrFinal" label="ate" required="true" labelWidth="3%" width="20%"/>
		<adsm:textbox dataType="integer" property="nrHorasAplicadas" label="horasAplicadas" size="4" labelWidth="12%" width="25%"/>
		
		<adsm:section caption="origem" />

		<adsm:hidden property="dataAtual" serializable="false"/>

		<adsm:combobox
			service="lms.vendas.manterHorariosCorteColetaEntregaAction.findZona" 
			property="zonaOrigem.idZona" 
			optionLabelProperty="dsZona" 
			optionProperty="idZona" 
			label="zona" 
			onlyActiveValues="true"
			onchange="limpaZona('Origem')"
			labelWidth="15%" width="19%" boxWidth="130">
		</adsm:combobox>

		<adsm:lookup
			service="lms.vendas.manterHorariosCorteColetaEntregaAction.findLookupPais" 
			action="/municipios/manterPaises" 
			property="paisOrigem" 
			idProperty="idPais" 
			criteriaProperty="nmPais" 
			label="pais"
			minLengthForAutoPopUpSearch="3" 
			exactMatch="false"
			onchange="return changePais('Origem');"
			dataType="text" labelWidth="6%" width="25%" size="25" maxLength="60">
			<adsm:propertyMapping 
				addChangeListener="false"
				criteriaProperty="zonaOrigem.idZona" 
				modelProperty="zona.idZona"/>
			<adsm:propertyMapping 
				relatedProperty="zonaOrigem.idZona" 
				modelProperty="zona.idZona" />
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacaoAtivo" />
		</adsm:lookup>
		
		<adsm:hidden property="dsUFOrigem" serializable="false"/>
		<adsm:hidden property="sgUFOrigem" serializable="false"/>

		<adsm:combobox 
			service="lms.vendas.manterHorariosCorteColetaEntregaAction.findUnidadeFederativaByPais" 
			property="unidadeFederativaOrigem.idUnidadeFederativa" 
			optionLabelProperty="siglaDescricao" 
			optionProperty="idUnidadeFederativa" 
			label="uf" onlyActiveValues="true"
			onDataLoadCallBack="ufOrigemOnDataLoad"
			onchange="return changeUF('Origem');"
			labelWidth="5%" width="29%" boxWidth="222">
			<adsm:propertyMapping 
				criteriaProperty="paisOrigem.idPais" 
				modelProperty="pais.idPais"/>
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacaoAtivo" />
		</adsm:combobox>
		
		<adsm:lookup 
			service="lms.vendas.manterHorariosCorteColetaEntregaAction.findLookupFilial" 
			action="/municipios/manterFiliais" 
			property="filialOrigem" 
			idProperty="idFilial" 
			criteriaProperty="sgFilial" 
			exactMatch="false"
			allowInvalidCriteriaValue="false"
			minLengthForAutoPopUpSearch="3"	
			onDataLoadCallBack="filialOrigem"		
			label="filial" 
			onchange="return changeFilial('Origem');"
			onPopupSetValue="changeFilialOrigemPopup"
			dataType="text" size="5" maxLength="3" width="35%">
  		    <adsm:propertyMapping modelProperty="historicoFiliais.vigenteEm" criteriaProperty="dataAtual" />	
		    <adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialOrigem.pessoa.nmFantasia"/>
			
		   	<!-- Seta o Nome Pais automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="paisOrigem.nmPais" 
				modelProperty="endereco.municipio.unidadeFederativa.pais.nmPais" />
			
			<!-- Seta o ID Pais automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="paisOrigem.idPais" 
				modelProperty="endereco.municipio.unidadeFederativa.pais.idPais" />
			
			<!-- Seta a Zona automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="zonaOrigem.idZona" 
				modelProperty="endereco.municipio.unidadeFederativa.pais.zona.idZona" />
			
			<!-- Seta o hidden para carregar a UF relacionada -->
			<adsm:propertyMapping 
				relatedProperty="_idUfOrigem" 
				modelProperty="endereco.municipio.unidadeFederativa.idUnidadeFederativa" />
		 
			<adsm:textbox 
				dataType="text" 
				property="filialOrigem.pessoa.nmFantasia" 
				serializable="false"
				size="30" maxLength="30" disabled="true"/>
        </adsm:lookup>
		
		<adsm:hidden property="_idUfOrigem" serializable="false"/>	
		<adsm:hidden property="municipioOrigem.idMunicipio"/>
		<adsm:lookup 
			service="lms.vendas.manterHorariosCorteColetaEntregaAction.findLookupMunicipioFilial" 
			action="/municipios/manterMunicipiosAtendidos" 
			property="municipioOrigem" 
			idProperty="municipio.idMunicipio" 
			criteriaProperty="municipio.nmMunicipio" 
			exactMatch="false"
			minLengthForAutoPopUpSearch="2"
			label="municipio" 
			serializable="false"
			onchange="return municipioChange('Origem');"
			onDataLoadCallBack="municipioOrigem"
			onPopupSetValue="municipioOrigemPopupSetValue"
			dataType="text" labelWidth="10%" width="37%" maxLength="60" size="30">
			<adsm:propertyMapping criteriaProperty="filialOrigem.idFilial" modelProperty="filial.idFilial" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="filialOrigem.sgFilial" modelProperty="filial.sgFilial" addChangeListener="false" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="filialOrigem.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" addChangeListener="false" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="paisOrigem.nmPais" modelProperty="municipio.unidadeFederativa.pais.nmPais" addChangeListener="false" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="paisOrigem.idPais" modelProperty="municipio.unidadeFederativa.pais.idPais" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="unidadeFederativaOrigem.idUnidadeFederativa" modelProperty="municipio.unidadeFederativa.idUnidadeFederativa" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="sgUFOrigem" modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="dsUFOrigem" modelProperty="municipio.unidadeFederativa.nmUnidadeFederativa" addChangeListener="false" inlineQuery="false"/>
			<adsm:propertyMapping relatedProperty="filialOrigem.idFilial" modelProperty="filial.idFilial"/>
			<adsm:propertyMapping relatedProperty="municipioOrigem.idMunicipio" modelProperty="municipio.idMunicipio"/>
			<adsm:propertyMapping relatedProperty="filialOrigem.sgFilial" modelProperty="filial.sgFilial"/>
			<adsm:propertyMapping relatedProperty="filialOrigem.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia"/>
			
			<!-- Seta o Nome Pais automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="paisOrigem.nmPais" 
				modelProperty="municipio.unidadeFederativa.pais.nmPais" />
			
			<!-- Seta o ID Pais automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="paisOrigem.idPais" 
				modelProperty="municipio.unidadeFederativa.pais.idPais" />
			
			<!-- Seta a Zona automaticamente --> 
			<adsm:propertyMapping 
				relatedProperty="zonaOrigem.idZona" 
				modelProperty="municipio.unidadeFederativa.pais.zona.idZona" />
			
			<!-- Seta o hidden para carregar a UF relacionada -->
			<adsm:propertyMapping 
				relatedProperty="_idUfOrigem" 
				modelProperty="municipio.unidadeFederativa.idUnidadeFederativa" />
		</adsm:lookup>
		
		<adsm:combobox property="enderecoPessoaOrigem.idEnderecoPessoa" label="endereco" width="85%" 
			service="lms.configuracoes.enderecoPessoaService.findEnderecoMunicipioUFCombo" autoLoad="false"
			optionLabelProperty="enderecoCompleto" optionProperty="idEnderecoPessoa" style="width:645px">
		</adsm:combobox>
		
		
		
		<adsm:section caption="destino" />
		
		<adsm:combobox
			service="lms.vendas.manterHorariosCorteColetaEntregaAction.findZona" 
			property="zonaDestino.idZona" 
			optionLabelProperty="dsZona" 
			optionProperty="idZona" 
			label="zona" 
			onlyActiveValues="true"
			onchange="limpaZona('Destino')"
			labelWidth="15%" width="19%" boxWidth="130">
		</adsm:combobox>

		<adsm:lookup
			service="lms.vendas.manterHorariosCorteColetaEntregaAction.findLookupPais" 
			action="/municipios/manterPaises" 
			property="paisDestino" 
			idProperty="idPais" 
			criteriaProperty="nmPais" 
			label="pais"
			minLengthForAutoPopUpSearch="3" 
			exactMatch="false"
			onchange="return changePais('Destino');"
			dataType="text" labelWidth="6%" width="25%" size="25" maxLength="60">
			<adsm:propertyMapping 
				addChangeListener="false"
				criteriaProperty="zonaDestino.idZona" 
				modelProperty="zona.idZona"/>
			<adsm:propertyMapping 
				relatedProperty="zonaDestino.idZona" 
				modelProperty="zona.idZona" />
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacaoAtivo" />
		</adsm:lookup>
		
		<adsm:hidden property="dsUFDestino" serializable="false"/>
		<adsm:hidden property="sgUFDestino" serializable="false"/>

		<adsm:combobox 
			service="lms.vendas.manterHorariosCorteColetaEntregaAction.findUnidadeFederativaByPais" 
			property="unidadeFederativaDestino.idUnidadeFederativa" 
			optionLabelProperty="siglaDescricao" 
			optionProperty="idUnidadeFederativa" 
			label="uf" onlyActiveValues="true"
			onDataLoadCallBack="ufDestinoOnDataLoad"
			onchange="return changeUF('Destino');"
			labelWidth="5%" width="29%" boxWidth="222">
			<adsm:propertyMapping 
				criteriaProperty="paisDestino.idPais" 
				modelProperty="pais.idPais"/>
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacaoAtivo" />
		</adsm:combobox>
		
		<adsm:lookup 
			service="lms.vendas.manterHorariosCorteColetaEntregaAction.findLookupFilial" 
			action="/municipios/manterFiliais" 
			property="filialDestino" 
			idProperty="idFilial" 
			criteriaProperty="sgFilial" 
			exactMatch="false"
			allowInvalidCriteriaValue="false"
			minLengthForAutoPopUpSearch="3"	
			onDataLoadCallBack="filialDestino"		
			label="filial" 
			onchange="return changeFilial('Destino');"
			onPopupSetValue="changeFilialDestinoPopup"
			dataType="text" size="5" maxLength="3" width="35%">
  		    <adsm:propertyMapping criteriaProperty="dataAtual" modelProperty="historicoFiliais.vigenteEm"/>	
		    <adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialDestino.pessoa.nmFantasia"/>
		   		
			  	<!-- Seta o Nome Pais automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="paisDestino.nmPais" 
				modelProperty="endereco.municipio.unidadeFederativa.pais.nmPais" />
			
			<!-- Seta o ID Pais automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="paisDestino.idPais" 
				modelProperty="endereco.municipio.unidadeFederativa.pais.idPais" />
			
			<!-- Seta a Zona automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="zonaDestino.idZona" 
				modelProperty="endereco.municipio.unidadeFederativa.pais.zona.idZona" />
			
			<!-- Seta o hidden para carregar a UF relacionada -->
			<adsm:propertyMapping 
				relatedProperty="_idUfDestino" 
				modelProperty="endereco.municipio.unidadeFederativa.idUnidadeFederativa" />
		 
			<adsm:textbox 
				dataType="text" 
				property="filialDestino.pessoa.nmFantasia" 
				serializable="false"
				size="30" maxLength="30" disabled="true"/>
		</adsm:lookup>

		<adsm:hidden property="_idUfDestino" serializable="false"/>	
		<adsm:hidden property="municipioDestino.idMunicipio"/>
		<adsm:lookup 
			service="lms.vendas.manterHorariosCorteColetaEntregaAction.findLookupMunicipioFilial" 
			action="/municipios/manterMunicipiosAtendidos" 
			property="municipioDestino" 
			idProperty="municipio.idMunicipio" 
			criteriaProperty="municipio.nmMunicipio" 
			exactMatch="false"
			minLengthForAutoPopUpSearch="2"
			label="municipio" 
			serializable="false"
			onchange="return municipioChange('Destino');"
			onDataLoadCallBack="municipioDestino"
			onPopupSetValue="municipioDestinoPopupSetValue"
			dataType="text" labelWidth="10%" width="37%" maxLength="60" size="30">
			<adsm:propertyMapping criteriaProperty="filialDestino.idFilial" modelProperty="filial.idFilial" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="filialDestino.sgFilial" modelProperty="filial.sgFilial" addChangeListener="false" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="filialDestino.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" addChangeListener="false" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="paisDestino.nmPais" modelProperty="municipio.unidadeFederativa.pais.nmPais" addChangeListener="false" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="paisDestino.idPais" modelProperty="municipio.unidadeFederativa.pais.idPais" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="unidadeFederativaDestino.idUnidadeFederativa" modelProperty="municipio.unidadeFederativa.idUnidadeFederativa" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="sgUFDestino" modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="dsUFDestino" modelProperty="municipio.unidadeFederativa.nmUnidadeFederativa" addChangeListener="false" inlineQuery="false"/>
			<adsm:propertyMapping relatedProperty="filialDestino.idFilial" modelProperty="filial.idFilial"/>
			<adsm:propertyMapping relatedProperty="municipioDestino.idMunicipio" modelProperty="municipio.idMunicipio"/>
			<adsm:propertyMapping relatedProperty="filialDestino.sgFilial" modelProperty="filial.sgFilial"/>
			<adsm:propertyMapping relatedProperty="filialDestino.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia"/>

			<!-- Seta o Nome Pais automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="paisDestino.nmPais" 
				modelProperty="municipio.unidadeFederativa.pais.nmPais" />

			<!-- Seta o ID Pais automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="paisDestino.idPais" 
				modelProperty="municipio.unidadeFederativa.pais.idPais" />
			
			<!-- Seta a Zona automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="zonaDestino.idZona" 
				modelProperty="municipio.unidadeFederativa.pais.zona.idZona" />
			
			<!-- Seta o hidden para carregar a UF relacionada -->
			<adsm:propertyMapping 
				relatedProperty="_idUfDestino" 
				modelProperty="municipio.unidadeFederativa.idUnidadeFederativa" />
		</adsm:lookup>
		
		<adsm:combobox property="enderecoPessoaDestino.idEnderecoPessoa" label="endereco" width="85%" 
			service="lms.configuracoes.enderecoPessoaService.findEnderecoMunicipioUFCombo" autoLoad="false"
			optionLabelProperty="enderecoCompleto" optionProperty="idEnderecoPessoa" style="width:645px">
		</adsm:combobox>

		
		<adsm:buttonBar>
			<adsm:storeButton id="storeButton"/>
			<adsm:newButton id="newButton"/>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

	//document.getElementById("enderecoPessoa.idEnderecoPessoa").masterLink = "true";
	
	function myPageLoad_cb(data,erro){
	
		onPageLoad_cb(data,erro);
		
		var dados = new Array();
		_serviceDataObjects = new Array();
            
        setNestedBeanPropertyValue(dados, "pessoa.idPessoa", getElementValue("cliente.idCliente"));
            
        addServiceDataObject(createServiceDataObject("lms.configuracoes.enderecoPessoaService.findEnderecoMunicipioUFCombo",
                                                     "enderecoPessoaOrigem_idEnderecoPessoa",
                                                     dados));
                                                     
        addServiceDataObject(createServiceDataObject("lms.configuracoes.enderecoPessoaService.findEnderecoMunicipioUFCombo",
                									 "enderecoPessoaDestino_idEnderecoPessoa",
                									 dados));
                                              
		var dados2 = new Array();                                                     
                                                     
		addServiceDataObject(createServiceDataObject("lms.configuracoes.manterDistribuicaoFreteInternacionalAction.findDataAtual",
                                          			 "setaDataAtual",
			                                         dados2));                                                     
                                                                                       
        xmit(false);
		
		//notifyElementListeners({e:document.getElementById("cliente.idCliente")});
	}
	
	function setaDataAtual_cb(data,error){
		setElementValue("dataAtual",data._value);
		document.getElementById("dataAtual").masterLink = 'true';
	}
	
	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function myDataLoad_cb(data, error) {
		onDataLoad_cb(data, error);
		
		if(data.municipioOrigem != null){
			setElementValue("municipioOrigem.municipio.idMunicipio",  data.municipioOrigem.idMunicipio);
			setElementValue("municipioOrigem.municipio.nmMunicipio",  data.municipioOrigem.nmMunicipio);
		}
		if(data.municipioDestino != null){
			setElementValue("municipioDestino.municipio.idMunicipio",  data.municipioDestino.idMunicipio);
			setElementValue("municipioDestino.municipio.nmMunicipio",  data.municipioDestino.nmMunicipio);
		}
		
		setElementValue("enderecoPessoaOrigem.idEnderecoPessoa", getNestedBeanPropertyValue(data, "enderecoPessoaOrigem.idEnderecoPessoa"));
		setElementValue("enderecoPessoaDestino.idEnderecoPessoa", getNestedBeanPropertyValue(data, "enderecoPessoaDestino.idEnderecoPessoa"));
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

	//function changeTpLocalizacao(){
	//	setElementValue("filial.idFilial", ""); 
	//	setElementValue("filial.pessoa.nmFantasia", ""); 
	//	setElementValue("filial.sgFilial", ""); 	
	//	setElementValue("municipio.idMunicipio", ""); 
	//	setElementValue("municipio.nmMunicipio", ""); 
	//	return true;
	//}	
	
	//------------------------------------
	
	function resetPais(tipo){
		setElementValue("pais" + tipo + ".idPais","");
		setElementValue("pais" + tipo + ".nmPais","");
	}
	
	function resetFilial(tipo) {
		setElementValue("filial" + tipo + ".idFilial", ""); 
		setElementValue("filial" + tipo + ".pessoa.nmFantasia", ""); 
		setElementValue("filial" + tipo + ".sgFilial", ""); 
	}
	
	function resetMunicipio(tipo) {
		setElementValue("municipio" + tipo + ".municipio.idMunicipio", "");
		setElementValue("municipio" + tipo + ".idMunicipio", "");
		setElementValue("municipio" + tipo + ".municipio.nmMunicipio", "");	
	}
	
	function limpaZona(tipo) {
		resetPais(tipo);
		setElementValue("unidadeFederativa" + tipo + ".idUnidadeFederativa","");
		resetFilial(tipo);
		resetMunicipio(tipo);
		setElementValue("_idUf" + tipo + "","");
		setElementValue("dsUF" + tipo + "","");
		setElementValue("sgUF" + tipo + "","");
		notifyElementListeners({e:document.getElementById("pais" + tipo + ".idPais")});
	}
	
	/*
	*  OnChange da lookup de Pais.
	*/
	function changePais(tipo){
		var idZona = getElementValue("zona" + tipo + ".idZona");
		var r = true;
		eval("r = pais" + tipo + "_nmPaisOnChangeHandler();");
		setElementValue("unidadeFederativa" + tipo + ".idUnidadeFederativa", "");
		changeUF(tipo);
		setElementValue("zona" + tipo + ".idZona", idZona);
		return r;
	}
	
	/*
	*  OnChange da combo de UF.
	*/
	function changeUF(tipo){
		resetFilial(tipo);
		resetMunicipio(tipo);
		montaDadosUF(tipo);
		return true;
	}	
	
	function montaDadosUF(tipo, dados){
		var uf = getElement('unidadeFederativa'+tipo+'.idUnidadeFederativa');
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
		setElementValue('dsUF'+tipo+'',dsUF);
		setElementValue('sgUF'+tipo+'',sgUF);
	}
	
	/*
	*  Callback para combo de UF
	*/
	function ufOrigemOnDataLoad_cb(dados, erro){
		var retorno = unidadeFederativaOrigem_idUnidadeFederativa_cb(dados);
		var idUf = getElementValue("_idUfOrigem");
		if (idUf != null && idUf != ""){
			setElementValue("unidadeFederativaOrigem.idUnidadeFederativa",idUf);
		}
		if( dados.length == 1 ){
			dados = dados[0];
		} else {
			dados = null;
		}		
		montaDadosUF("Origem", dados);
		return retorno;
	}
	function ufDestinoOnDataLoad_cb(dados, erro){
		var retorno = unidadeFederativaDestino_idUnidadeFederativa_cb(dados);
		var idUf = getElementValue("_idUfDestino");
		if (idUf != null && idUf != ""){
			setElementValue("unidadeFederativaDestino.idUnidadeFederativa",idUf);
		}
		if( dados.length == 1 ){
			dados = dados[0];
		} else {
			dados = null;
		}		
		montaDadosUF("Destino", dados);
		return retorno;
	}
	
	
	/*
	*  Callback para lookup de filial
	*/	
	function filialOrigem_cb(data) {	
		if (data!=undefined && data.length == 0){
			alertI18nMessage("LMS-30017");
			return;
		}						 
		lookupExactMatch({e:document.getElementById("filialOrigem.idFilial"), data:data });
		notifyElementListeners({e:document.getElementById("paisOrigem.idPais")});		
	}
	function filialDestino_cb(data) {		
		if (data!=undefined && data.length == 0){
			alertI18nMessage("LMS-30017");
			return;
		}
		lookupExactMatch({e:document.getElementById("filialDestino.idFilial"), data:data });
		notifyElementListeners({e:document.getElementById("paisDestino.idPais")});
	}
	
	/*
	*	OnChange da Filial
	*/
	function changeFilial(tipo){
		var r = true;
		if (getElementValue("filial" + tipo + ".sgFilial") == "") {
			resetFilial(tipo);
		} else {
			eval("r = filial" + tipo + "_sgFilialOnChangeHandler()");
		}
		resetMunicipio(tipo);
		return r;
	}
	
	/*
	*	OnChange da Filial
	*/
	function changeFilialOrigemPopup(data){
		changeFilialPopup(getNestedBeanPropertyValue(data,"idFilial"), "Origem");
	}
	function changeFilialDestinoPopup(data){
		changeFilialPopup(getNestedBeanPropertyValue(data,"idFilial"), "Destino");		
		}
	function changeFilialPopup(idFilial, tipo) {
		if (idFilial != "")
			findEndereco(idFilial, tipo);
		resetMunicipio(tipo);
	}
	
	function findEndereco(idPessoa, tipo) {
		var sdo = createServiceDataObject("lms.vendas.manterHorariosCorteColetaEntregaAction.findEndereco", "endereco" + tipo, {idPessoa:idPessoa});
         xmit({serviceDataObjects:[sdo]});
	}
	
	function enderecoOrigem_cb(dados, erros) {
		configEndereco(dados, "Origem");
		notifyElementListeners({e:document.getElementById("paisOrigem.idPais")});
	}
	function enderecoDestino_cb(dados, erros) {
		configEndereco(dados, "Destino");
		notifyElementListeners({e:document.getElementById("paisDestino.idPais")});
	}
	
	/*
	*  OnChange da lookup de Municipio
	*/
	function municipioChange(tipo){
		var r = true;
		if (getElementValue("municipio" + tipo + ".municipio.nmMunicipio") != ""){
			eval("r = municipio"+ tipo + "_municipio_nmMunicipioOnChangeHandler()");
		} else {
			resetMunicipio(tipo);
		}
		return r;
	}
	
	function municipioOrigem_cb(data) {
		var retorno = lookupExactMatch({e:document.getElementById("municipioOrigem.municipio.idMunicipio"), data:data, callBack:'municipioOrigemLikeAndMatch'});
		if (data != undefined && data.length == 1) 	{
	    	eventMunicipio("Origem");
		}
		return retorno;
	}
	function municipioDestino_cb(data) {
		var retorno = lookupExactMatch({e:document.getElementById("municipioDestino.municipio.idMunicipio"), data:data, callBack:'municipioDestinoLikeAndMatch'});
		if (data != undefined && data.length == 1) 	{
	    	eventMunicipio("Destino");
		}
		return retorno;
	}
	
	function municipioOrigemLikeAndMatch_cb(data){
		var retorno = lookupLikeEndMatch({e:document.getElementById("municipioOrigem.municipio.idMunicipio"), data:data});
		if (data != undefined && data.length == 1) {
			eventMunicipio("Origem");
		}
		return retorno;
	}
	function municipioDestinoLikeAndMatch_cb(data){
		var retorno = lookupLikeEndMatch({e:document.getElementById("municipioDestino.municipio.idMunicipio"), data:data});
		if (data != undefined && data.length == 1) {
			eventMunicipio("Destino");
		}
		return retorno;
	}
	
	function eventMunicipio(tipo){
		notifyElementListeners({e:document.getElementById("pais" + tipo + ".idPais")});
	}
	
	/*
	*  OnPopupSetValue da lookup de Municipio origem.
	*/
	function municipioOrigemPopupSetValue(dados){
  	  	configEndereco(dados, "Origem");
		eventMunicipio("Origem");
	}
	function municipioDestinoPopupSetValue(dados){
  	  	configEndereco(dados, "Destino");
		eventMunicipio("Destino");
	}
	
	function configEndereco(dados, tipo) {
		var uf = getNestedBeanPropertyValue(dados, "municipio.unidadeFederativa.idUnidadeFederativa");
	  	var	idPais = getNestedBeanPropertyValue(dados, "municipio.unidadeFederativa.pais.idPais");
  		var nmPais = getNestedBeanPropertyValue(dados, "municipio.unidadeFederativa.pais.nmPais");
  		var	idZona = getNestedBeanPropertyValue(dados, "municipio.unidadeFederativa.pais.zona.idZona");
		setElementValue("_idUf" + tipo, uf);
		setElementValue("pais" + tipo + ".idPais", idPais);
		setElementValue("pais" + tipo + ".nmPais", nmPais);
		setElementValue("zona" + tipo + ".idZona", idZona);
	}
	
</script>