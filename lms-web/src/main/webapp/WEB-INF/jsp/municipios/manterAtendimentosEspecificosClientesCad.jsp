<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarAtendimentosEspecificosClientes" service="lms.municipios.atendimentoClienteService">
	<adsm:form action="/municipios/manterAtendimentosEspecificosClientes" idProperty="idAtendimentoCliente" service="lms.municipios.atendimentoClienteService.findByIdDetalhado" onDataLoadCallBack="pageLoad">
	
		<adsm:hidden property="operacaoServicoLocaliza.idOperacaoServicoLocaliza" serializable="true"/>
		<adsm:hidden property="tpSituacao" value="A" serializable="false" />
	
		<adsm:textbox dataType="text" disabled="true" property="operacaoServicoLocaliza.municipioFilial.municipio.nmMunicipio" label="municipio" maxLength="30" size="30" labelWidth="16%" width="84%" serializable="false"/>
		<adsm:textbox dataType="text" disabled="true" property="operacaoServicoLocaliza.municipioFilial.municipio.unidadeFederativa.siglaDescricao" label="uf" maxLength="30" size="30" labelWidth="16%" width="34%"  serializable="false"/>
		<adsm:textbox dataType="text" disabled="true" property="operacaoServicoLocaliza.municipioFilial.municipio.unidadeFederativa.pais.nmPais" label="pais" maxLength="30" size="30" labelWidth="16%" width="34%"  serializable="false"/>
		<adsm:checkbox disabled="true" property="operacaoServicoLocaliza.municipioFilial.municipio.blDistrito" label="indDistrito" labelWidth="16%" width="34%"  serializable="false"/>
		<adsm:textbox dataType="text" disabled="true" property="operacaoServicoLocaliza.municipioFilial.municipio.municipioDistrito.nmMunicipio" label="municDistrito" maxLength="30" size="30" labelWidth="16%" width="34%"  serializable="false"/>
		<adsm:textbox dataType="text" disabled="true" property="operacaoServicoLocaliza.municipioFilial.filial.siglaNomeFilial" label="filial" maxLength="30" size="30" labelWidth="16%" width="84%"  serializable="false"/>
		
		<adsm:hidden property="operacaoServicoLocaliza.municipioFilial.idMunicipioFilial" serializable="true"/>
		<adsm:hidden property="operacaoServicoLocaliza.municipioFilial.municipio.idMunicipio" serializable="true"/>
		
		<adsm:hidden property="operacaoServicoLocaliza.blDomingo" serializable="true"/>
		<adsm:hidden property="operacaoServicoLocaliza.blSegunda" serializable="true"/>
		<adsm:hidden property="operacaoServicoLocaliza.blTerca" serializable="true"/>
		<adsm:hidden property="operacaoServicoLocaliza.blQuarta" serializable="true"/>
		<adsm:hidden property="operacaoServicoLocaliza.blQuinta" serializable="true"/>
		<adsm:hidden property="operacaoServicoLocaliza.blSexta" serializable="true"/>
		<adsm:hidden property="operacaoServicoLocaliza.blSabado" serializable="true"/>
						

		<adsm:textbox dataType="text" disabled="true" property="operacaoServicoLocaliza.tpOperacao" label="tipoOperacao" maxLength="30" size="30" labelWidth="16%" width="34%"  serializable="false"/>
		<adsm:textbox dataType="text" disabled="true" property="operacaoServicoLocaliza.servico.dsServico" label="servico" maxLength="30" size="30" labelWidth="16%" width="34%"  serializable="false"/>

		<adsm:textbox dataType="text" disabled="true" property="operacaoServicoLocaliza.tipoLocalizacaoMunicipio.dsTipoLocalizacaoMunicipio" label="tipoLocalizacao" maxLength="30" size="30" labelWidth="16%" width="84%"  serializable="false"/>

		<adsm:lookup 
			property="cliente" 
			criteriaProperty="pessoa.nrIdentificacao" 
			idProperty="idCliente" 
			service="lms.vendas.clienteService.findLookup" 
			dataType="text"  
			label="cliente" 
			size="20" 
			maxLength="20" 
			labelWidth="16%" 
			width="84%" 
			action="/vendas/manterDadosIdentificacao" 
			exactMatch="false" 
			required="true" 
			onDataLoadCallBack="dataLoadC" 
			onPopupSetValue="popUpSetC">
			
			<adsm:propertyMapping
				criteriaProperty="tpSituacao"
				modelProperty="tpSituacao" />
			<adsm:propertyMapping 
				relatedProperty="cliente.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa" />
							
			<adsm:textbox 
				dataType="text" 
				property="cliente.pessoa.nmPessoa" 
				size="30" 
				disabled="true" 
				serializable="false"/>
		</adsm:lookup>

		<adsm:range label="vigencia" labelWidth="16%" width="84%">
			<adsm:textbox dataType="JTDate" required="true" property="dtVigenciaInicial" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>

		<adsm:buttonBar>
			<adsm:storeButton callbackProperty="afterStore" service="lms.municipios.atendimentoClienteService.storeMap" />
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script> 

	// #####################################################################
	// tratamento do comportamento padrão da vigência
	// #####################################################################
	function pageLoad_cb(data, exception,key) {
		if (exception != undefined) {
			alert(exception);
			return;
		}
		onDataLoad_cb(data, exception); 
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data,"acaoVigenciaAtual");		
		comportamentoDetalhe(acaoVigenciaAtual, null);
	}
	
	function estadoNovo() {
		setDisabled("cliente.idCliente", false);
		setDisabled("dtVigenciaInicial", false);
		setDisabled("dtVigenciaFinal", false);
		setFocusOnFirstFocusableField(document);
	}

	function comportamentoDetalhe(acaoVigenciaAtual, tipoEvento){
		if (acaoVigenciaAtual == 0) {
			estadoNovo();
			if(tipoEvento == "" ||  tipoEvento == null)
     			setFocusOnFirstFocusableField(document);
		    else
		       setFocus(document.getElementById("__buttonBar:0.newButton"), false);
		} else if (acaoVigenciaAtual == 1) {
			setDisabled(document, true);
			setDisabled("__buttonBar:0.newButton", false);
			setDisabled("__buttonBar:0.storeButton", false);
			setDisabled("__buttonBar:0.removeButton", true);
			setDisabled("dtVigenciaFinal", false);
			if(tipoEvento == "" ||  tipoEvento == null)
     			setFocusOnFirstFocusableField(document);
		    else
		       setFocus(document.getElementById("__buttonBar:0.newButton"), false);
		} else if (acaoVigenciaAtual == 2) {
			setDisabled(document, true);
			setDisabled("__buttonBar:0.newButton", false);
			setFocus(document.getElementById("__buttonBar:0.newButton"), false);
		}
	}

	function afterStore_cb(data,exception,key) {
		store_cb(data,exception,key);
		
		if (exception == undefined) {
			var acaoVigenciaAtual = getNestedBeanPropertyValue(data,"acaoVigenciaAtual");
			var store = "true";
			comportamentoDetalhe(acaoVigenciaAtual, store);
		}
	}
	
	// ############################################################
	// tratamento dos eventos da initWindow para <tab_click>, 
	// <gridRow_click>, <newButton_click> e/ou <removeButton_click> 
	// ############################################################
	function initWindow(eventObj) {		
		if ((eventObj.name == "newButton_click") || (eventObj.name == "tab_click")) { 
			estadoNovo();			
		}		
    }
    
    function popUpSetC(data) {
		var nrFormatado = getNestedBeanPropertyValue(data, ":pessoa.nrIdentificacaoFormatado");
		setNestedBeanPropertyValue(data, ":pessoa.nrIdentificacao", nrFormatado);
		return true;	
	}

	function dataLoadC_cb(data,exception) {
		if (data != undefined && data.length == 1) {
			var nrFormatado = getNestedBeanPropertyValue(data, ":0.pessoa.nrIdentificacaoFormatado");
			setNestedBeanPropertyValue(data, ":0.pessoa.nrIdentificacao", nrFormatado);
		}
		return cliente_pessoa_nrIdentificacao_likeEndMatch_cb(data);
	}

</script>