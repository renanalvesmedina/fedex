<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.manterIntervalosCEPAtendidosAction" >
     
	<adsm:form action="/municipios/manterIntervalosCEPAtendidos" idProperty="idMunicipioFilialIntervCep" onDataLoadCallBack="cepLoad">
		<adsm:hidden property="municipioFilial.idMunicipioFilial"/>
		<adsm:hidden property="municipioFilial.municipio.idMunicipio"/>		
		
		<adsm:hidden property="pais.idPais" serializable="false"/>
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

		<adsm:textbox dataType="text" property="nrCepInicial"  label="cepInicial" required="true" maxLength="8" size="12" labelWidth="17%" width="33%" />
		<adsm:textbox dataType="text" property="nrCepFinal"  label="cepFinal" required="true" maxLength="8" size="12"     labelWidth="17%" width="33%" />
		
		<adsm:range label="vigencia" labelWidth="17%" width="33%">
			<adsm:textbox dataType="JTDate" required="true" property="dtVigenciaInicial"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>
		<script>
			function LMS_29013() {
				alert('<adsm:label key="LMS-29013" />');
			}
		</script>
		<adsm:buttonBar>
			<adsm:storeButton service="lms.municipios.municipioFilialIntervCepService.storeMap" callbackProperty="storeCep"/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
	
	// #####################################################################
	// tratamento do comportamento padrão da vigência
	// #####################################################################
	function cepLoad_cb(data, exception,key) {		
		onDataLoad_cb(data,exception);
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
		
		// 2 = VIGENCIA INICIAL <= HOJE E 
		//     VIGENCIA FINAL   < HOJE
		//     DESABILITA TUDO, EXCETO O BOTÃO NOVO
		} else {
			setDisabled(document, true);
			setDisabled("__buttonBar:0.newButton", false);			
		} 
	}
	
	function estadoNovo() {
		setDisabled('nrCepInicial', false);
		setDisabled('nrCepFinal', false);	
		setDisabled('dtVigenciaInicial', false);
		setDisabled('dtVigenciaFinal', false);	
		setFocusOnFirstFocusableField();
		
	}
	
	function storeCep_cb(data, error, key){
		store_cb(data, error, key);
		
		if (error == undefined) {
			comportamentoVigencia(data);
			setFocusOnNewButton();
		}
	}
	
	// ############################################################
	// tratamento dos eventos da initWindow para <tab_click>, 
	// <gridRow_click>, <newButton_click> e/ou <removeButton_click> 
	// ############################################################
	function initWindow(eventObj) {		
		if ((eventObj.name != "gridRow_click") && (eventObj.name != "storeButton"))  {
			estadoNovo();
		}
		
    }
	
		
</script>