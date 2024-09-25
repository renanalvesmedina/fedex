<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.municipioFilialSegmentoService">
	<adsm:form action="/municipios/manterSegmentosMercadoAtendidos" idProperty="idMunicipioFilialSegmento" onDataLoadCallBack="pageLoad" service="lms.municipios.municipioFilialSegmentoService.findByIdDetalhamento">
		<adsm:hidden property="municipioFilial.idMunicipioFilial"/>
		<adsm:hidden property="municipioFilial.municipio.idMunicipio"/>
		
		<adsm:textbox dataType="text" property="municipioFilial.filial.sgFilial"
				label="filial" labelWidth="19%" width="81%" size="3" disabled="true" serializable="false" >
			<adsm:textbox dataType="text" property="municipioFilial.filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>
		</adsm:textbox>
		<adsm:textbox dataType="text" property="municipioFilial.municipio.nmMunicipio" label="municipio" labelWidth="19%" width="81%" size="30" disabled="true" serializable="false" />
		<adsm:textbox property="municipioFilial.municipio.unidadeFederativa.sgUnidadeFederativa" dataType="text" label="uf" labelWidth="19%" width="31%" disabled="true" size="2" serializable="false">
			<adsm:textbox dataType="text" property="municipioFilial.municipio.unidadeFederativa.nmUnidadeFederativa" size="30" disabled="true" serializable="false" />
		</adsm:textbox>
		<adsm:textbox dataType="text" property="municipioFilial.municipio.unidadeFederativa.pais.nmPais" label="pais" labelWidth="18%" width="32%" size="30" disabled="true" serializable="false"/>
		<adsm:checkbox property="municipioFilial.municipio.blDistrito" label="indDistrito" labelWidth="19%" width="31%" disabled="true" serializable="false"/>
		<adsm:textbox dataType="text" property="municipioFilial.municipio.municipioDistrito.nmMunicipio" label="municDistrito" labelWidth="18%" width="32%" size="30" disabled="true" serializable="false"/>
		
		<adsm:combobox property="segmentoMercado.idSegmentoMercado" optionLabelProperty="dsSegmentoMercado" optionProperty="idSegmentoMercado"
		required="true" label="segmentoMercado" service="lms.vendas.segmentoMercadoService.find"
		labelWidth="19%" width="31%" boxWidth="172" onlyActiveValues="true"/>
		
		<adsm:range label="vigencia" labelWidth="18%" width="32%">
			<adsm:textbox dataType="JTDate" required="true" property="dtVigenciaInicial" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>

		<adsm:buttonBar>
			<adsm:storeButton callbackProperty="storeSegmento" service="lms.municipios.municipioFilialSegmentoService.storeMap"/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>

	function storeSegmento_cb(data, error, key){
		store_cb(data, error, key);
		if (error == undefined) {
			comportamentVigencia(data);
			setFocusOnNewButton();
		}
	}

	function comportamentoFoco(acaoVigenciaAtual){
		if (acaoVigenciaAtual == 2)
			setFocusOnNewButton();
		else
			setFocusOnFirstFocusableField();
	}
	
	function comportamentVigencia(data){
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
		
		// 2 = VIGENCIA INICIAL <= HOJE E 
		//     VIGENCIA FINAL   < HOJE
		//     DESABILITA TUDO, EXCETO O BOTÃO NOVO
		} else if (acaoVigenciaAtual == 2) {
			setDisabled(document, true);
			setDisabled("__buttonBar:0.newButton", false);
		} 
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
		comportamentVigencia(data);
		comportamentoFoco(data.acaoVigenciaAtual);
	}
	
	
	function estadoNovo() {
		setDisabled('dtVigenciaInicial', false);
		setDisabled('dtVigenciaFinal', false);	
		setDisabled('segmentoMercado.idSegmentoMercado', false);	
		setFocusOnFirstFocusableField();
	}
	
	
	// ############################################################
	// tratamento dos eventos da initWindow para <tab_click>, 
	// <gridRow_click>, <newButton_click> e/ou <removeButton_click> 
	// ############################################################
	function initWindow(eventObj) {		
		if ((eventObj.name != "gridRow_click") && (eventObj.name != "store_click"))  {
			estadoNovo();
		}
    }
</script>