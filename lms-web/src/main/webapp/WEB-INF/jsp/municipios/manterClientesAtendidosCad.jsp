<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.manterClientesAtendidosAction">
	<adsm:form action="/municipios/manterClientesAtendidos" idProperty="idMunicipioFilialCliOrigem" onDataLoadCallBack="pageLoad">
		<adsm:hidden property="municipioFilial.idMunicipioFilial"/>
		<adsm:hidden property="municipioFilial.municipio.idMunicipio"/>
		
		<adsm:textbox dataType="text" property="municipioFilial.filial.sgFilial"
				label="filial" labelWidth="17%" width="83%" size="3" disabled="true" serializable="false" >
			<adsm:textbox dataType="text" property="municipioFilial.filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>
		</adsm:textbox>
 
		<adsm:textbox dataType="text" property="municipioFilial.municipio.nmMunicipio" label="municipio" labelWidth="17%" width="83%" size="30" disabled="true" serializable="false"/>

		<adsm:textbox property="municipioFilial.municipio.unidadeFederativa.sgUnidadeFederativa" dataType="text" label="uf" labelWidth="17%" width="33%" disabled="true" size="2" serializable="false">
			<adsm:textbox dataType="text" property="municipioFilial.municipio.unidadeFederativa.nmUnidadeFederativa" size="30" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textbox dataType="text" property="municipioFilial.municipio.unidadeFederativa.pais.nmPais" label="pais" labelWidth="17%" width="33%" size="30" disabled="true" serializable="false"/>

		<adsm:checkbox property="municipioFilial.municipio.blDistrito" label="indDistrito" width="33%" labelWidth="17%" disabled="true" serializable="false"/>

		<adsm:textbox dataType="text" property="municipioFilial.municipio.municipioDistrito.nmMunicipio" label="municDistrito" labelWidth="17%" width="33%" size="30" disabled="true" serializable="false"/>

		<adsm:textbox label="tipoIdentificacao" dataType="text" serializable="false" property="cliente.pessoa.tpIdentificacao.description" labelWidth="17%" width="60%" disabled="true"/>	

		<adsm:hidden property="cliente.tpCliente" value="S"/>
		<adsm:hidden property="cliente.tpSituacao" value="A"/>
		<adsm:lookup
			label="cliente"
			property="cliente"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			service="lms.vendas.clienteService.findLookup"
			action="/vendas/manterDadosIdentificacao"
			onDataLoadCallBack="clienteAtend_dataLoad"
			onPopupSetValue="clienteAtend_onPopup"
			dataType="text"
			size="17"
			maxLength="20"
			labelWidth="17%"
			width="17%"
			exactMatch="true"
			required="true"
		>
			<adsm:propertyMapping criteriaProperty="cliente.tpCliente" modelProperty="tpCliente" />
			<adsm:propertyMapping criteriaProperty="cliente.tpSituacao" modelProperty="tpSituacao" />
			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:propertyMapping relatedProperty="cliente.pessoa.tpIdentificacao.description" modelProperty="pessoa.tpIdentificacao.description" />
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="30" disabled="true" width="50%" />
		</adsm:lookup>

		<adsm:range label="vigencia" width="33%" labelWidth="17%" >
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>

		<adsm:buttonBar>
			<adsm:storeButton callbackProperty="clienteAtend_store" service="lms.municipios.municipioFilialCliOrigemService.storeMap"/>
			<adsm:newButton/>			
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script language="javascript" type="text/javascript">
	function clienteAtend_store_cb(data, error, key) {
		store_cb(data, error, key);
		
		if (error == undefined) {
			comportamentoVigencia(data);
			setFocusOnNewButton();
		}
	}

	function clienteAtend_dataLoad_cb(data) {
		if (data != undefined && data.length >= 1) {
			var nrIdenfitificacaoFormatado = getNestedBeanPropertyValue(data, ":0.pessoa.nrIdentificacaoFormatado");
			setNestedBeanPropertyValue(data, ":0.pessoa.nrIdentificacao", nrIdenfitificacaoFormatado);
		}
		cliente_pessoa_nrIdentificacao_exactMatch_cb(data)
	}

	function clienteAtend_onPopup(data) {
		var nrIdenfitificacaoFormatado = getNestedBeanPropertyValue(data, "pessoa.nrIdentificacaoFormatado");
		setNestedBeanPropertyValue(data, "pessoa.nrIdentificacao", nrIdenfitificacaoFormatado);

		return true;
	}

	// #####################################################################
	// tratamento do comportamento padrão da vigência
	// #####################################################################
	function pageLoad_cb(data, exception,key) {
		if (exception != undefined) {
			alert(exception);
			return;
		}
		onDataLoad_cb(data, exception); 
		comportamentoVigencia(data);
		comportamentoFoco(data.acaoVigenciaAtual);
	}

	function comportamentoFoco(acaoVigenciaAtual){
		if (acaoVigenciaAtual == 2)
			setFocusOnNewButton();
		else
			setFocusOnFirstFocusableField();
	}

	function comportamentoVigencia(data){
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data,"acaoVigenciaAtual");

		// 0 = VIGENCIA INICIAL > HOJE
		// PODE TUDO
		if (acaoVigenciaAtual == 0) {
			estadoNovo();

		// 1 = VIGENCIA INICIAL <= HOJE E 
		//     VIGENCIA FINAL   >= HOJE
		//     DESABILITA VIGENCIA INICIAL		 
		} else if (acaoVigenciaAtual == 1) {
			setDisabled(document, true);
			setDisabled("__buttonBar:0.newButton", false);
			setDisabled("__buttonBar:0.storeButton", false);

			setDisabled("dtVigenciaFinal", false);
			setFocus("dtVigenciaFinal");

		// 2 = VIGENCIA INICIAL <= HOJE E 
		//     VIGENCIA FINAL   < HOJE
		//     DESABILITA TUDO, EXCETO O BOTÃO NOVO
		} else {
			setDisabled(document, true);
			setDisabled("__buttonBar:0.newButton", false);
		} 
	}

	function estadoNovo() {
		setDisabled('dtVigenciaInicial', false);
		setDisabled('dtVigenciaFinal', false);	
		setDisabled('cliente.idCliente', false);	
		setFocus('cliente.pessoa.nrIdentificacao');
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

</script>