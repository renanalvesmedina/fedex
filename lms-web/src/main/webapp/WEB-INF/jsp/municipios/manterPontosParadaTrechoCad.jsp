<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.manterPontosParadaTrechoAction" >
	<adsm:form action="/municipios/manterPontosParadaTrecho" idProperty="idPontoParadaTrecho" onDataLoadCallBack="trechoLoad" >
		<adsm:hidden property="trechoRotaIdaVolta.idTrechoRotaIdaVolta" value="1" />
		<adsm:hidden property="trechoRotaIdaVolta.versao" value="1" />
	
				
		<adsm:textbox dataType="integer" label="rota" property="rota.nrRota" disabled="true" mask="0000" size="5" labelWidth="18%" width="32%" serializable="false">
			<adsm:textbox dataType="text" property="rota.dsRota" disabled="true" size="30" serializable="false"/>
		</adsm:textbox>
	
				
				
		<adsm:textbox dataType="text" label="trecho" property="trechoRotaIdaVolta.dsTrechoRotaIdaVolta"
				size="10" disabled="true" labelWidth="18%" width="82%" serializable="false" />
				
        <adsm:lookup dataType="text" property="pontoParada" idProperty="idPontoParada" criteriaProperty="nmPontoParada"
	        	service="lms.municipios.manterPontosParadaTrechoAction.findLookupPontoParada" action="/municipios/manterPontosParadaRota"
    	    	label="local" labelWidth="18%" width="32%" exactMatch="false" minLengthForAutoPopUpSearch="3" required="true"
    	    	onDataLoadCallBack="paradaCallbackCustom" onPopupSetValue="paradaPopupCustom" >
        	<adsm:propertyMapping modelProperty="municipio.nmMunicipio"
        			relatedProperty="pontoParada.municipio.nmMunicipio" />
        	<adsm:propertyMapping modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa"
        			relatedProperty="pontoParada.municipio.unidadeFederativa.sgUnidadeFederativa" />
        	<adsm:propertyMapping modelProperty="municipio.unidadeFederativa.pais.nmPais"
        			relatedProperty="pontoParada.municipio.unidadeFederativa.pais.nmPais" />
        	<adsm:propertyMapping modelProperty="rodovia.sgDsRodovia"
        			relatedProperty="pontoParada.rodovia.sgDsRodovia" />
        	<adsm:propertyMapping modelProperty="nrKm"
        			relatedProperty="pontoParada.nrKm" />
        	<adsm:propertyMapping modelProperty="blAduana"
        			relatedProperty="pontoParada.blAduana" />
        	<adsm:propertyMapping modelProperty="nrLatitudePessoa"
        			relatedProperty="nrLatitude" />
        	<adsm:propertyMapping modelProperty="nrLongitudePessoa"
        			relatedProperty="nrLongitude" />
        </adsm:lookup>
        	
		<adsm:checkbox property="pontoParada.blAduana"
				label="indicadorAduana" labelWidth="18%" width="32%" disabled="true" serializable="true" />
		
		<adsm:textbox dataType="text" property="pontoParada.municipio.nmMunicipio" label="municipio"
				labelWidth="18%" width="82%" disabled="true" serializable="false" />
		<adsm:textbox dataType="text" property="pontoParada.municipio.unidadeFederativa.sgUnidadeFederativa" label="uf"
				size="5" labelWidth="18%" width="32%" disabled="true" serializable="false" />
		<adsm:textbox dataType="text" property="pontoParada.municipio.unidadeFederativa.pais.nmPais" label="pais"
				size="26" labelWidth="18%" width="32%" disabled="true" serializable="false" />
				
		<adsm:textbox dataType="text" label="rodovia" property="pontoParada.rodovia.sgDsRodovia"
				size="26" labelWidth="18%" width="32%" disabled="true" serializable="false" />
		<adsm:textbox dataType="text" property="pontoParada.nrKm" label="km"
				size="5" labelWidth="18%" width="32%" disabled="true" serializable="false" />
				
		<adsm:textbox dataType="currency" property="nrLatitude" label="latitude"
				maxLength="18" size="20" labelWidth="18%" width="32%" serializable="false" disabled="true" />
		<adsm:textbox dataType="currency" property="nrLongitude" label="longitude"
				maxLength="18" size="20" labelWidth="18%" width="32%" serializable="false" disabled="true" />
		
		<adsm:textbox dataType="JTTime" property="nrTempoParada" label="tempoParada"
				mask="hhh:mm" size="6" required="true" maxLength="6" unit="h" labelWidth="18%" width="32%" />
		<adsm:textbox dataType="integer" property="nrOrdem" label="ordem"
				maxLength="3" size="6" labelWidth="18%" width="32%" />
				 
		<adsm:range label="vigencia" labelWidth="18%" width="82%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true" />
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal" />
        </adsm:range>
        
		<adsm:buttonBar>
			<adsm:button id="motivosParadaTrecho" caption="motivosParadaTrecho" action="/municipios/manterMotivosPontoParadaTrecho" cmd="main" >
				<adsm:linkProperty src="idPontoParadaTrecho" target="pontoParadaTrecho.idPontoParadaTrecho" />
				<adsm:linkProperty src="rota.dsRota" target="rota.dsRota" />
				<adsm:linkProperty src="rota.nrRota" target="rota.nrRota" />
				<adsm:linkProperty src="trechoRotaIdaVolta.dsTrechoRotaIdaVolta" target="trechoRotaIdaVolta.dsTrechoRotaIdaVolta" />
				<adsm:linkProperty src="pontoParada.nmPontoParada" target="pontoParadaTrecho.pessoa.nmPessoa" />
			</adsm:button>
			<adsm:storeButton id="salvar" callbackProperty="afterStore" />
			<adsm:newButton id="novo" />
			<adsm:removeButton id="excluir" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
	/**
	 * Retorna estado dos campos como foram carregados na página.
	 */
	function estadoNovo() {
		setDisabled(document,false);
		setDisabled("rota.dsRota",true);
		setDisabled("trechoRotaIdaVolta.dsTrechoRotaIdaVolta",true);
		setDisabled("pontoParada.blAduana",true);
		setDisabled("pontoParada.municipio.nmMunicipio",true);
		setDisabled("pontoParada.municipio.unidadeFederativa.sgUnidadeFederativa",true);
		setDisabled("pontoParada.municipio.unidadeFederativa.pais.nmPais",true);
		setDisabled("pontoParada.rodovia.sgDsRodovia",true);
		setDisabled("pontoParada.nrKm",true);
		setDisabled("nrLatitude",true);
		setDisabled("nrLongitude",true);
		setDisabled("motivosParadaTrecho",true);
		setDisabled("excluir",true);
		setFocusOnFirstFocusableField();
	}
	
	/**
	 * Habilitar campos se o registro estiver vigente.
	 */
	function habilitarCampos() {
		setDisabled("dtVigenciaFinal",false);
		setDisabled("nrOrdem",false);
		setDisabled("novo",false);
		setDisabled("salvar",false);
		setDisabled("motivosParadaTrecho",false);
	}
	
	/**
	 * Ao carregar os dados, é tratado o retorno da validação de vigência no detalhamento:
	 */
	function trechoLoad_cb(data,exception) {
		onDataLoad_cb(data,exception);
		comportamentoDetalhe(data);
	}
	
	/**
	 * Após salvar, deve-se carregar o valor da vigência inicial detalhada.
	 */
	function afterStore_cb(data,exception,key){
		store_cb(data,exception,key);	
		if(exception == undefined) {
			comportamentoDetalhe(data);
			setFocusOnNewButton();
		}
	}
	
	function comportamentoDetalhe(data) {
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
		if (acaoVigenciaAtual == 0) {
			estadoNovo();
			setDisabled("excluir",false);
			setDisabled("motivosParadaTrecho",false);
			setFocusOnFirstFocusableField();
		} else if (acaoVigenciaAtual == 1) {
			setDisabled(document,true);
			habilitarCampos();
			setFocusOnFirstFocusableField();
		} else if (acaoVigenciaAtual == 2) {
			setDisabled(document,true);
			setDisabled("novo",false);
			setDisabled("motivosParadaTrecho",false);
			setFocusOnNewButton();
		}
	}
	
	/**
	 * Tratamento dos eventos da initWindow.
	 */
	function initWindow(eventObj) {
		if (eventObj.name != "gridRow_click" && eventObj.name != "storeButton")
			estadoNovo();
	}
	
	function paradaCallbackCustom_cb(data,exception) {
		paradaCallbackCustomDefault(data);	
		return lookupExactMatch({e:document.getElementById("pontoParada.idPontoParada"), data:data, callBack:"paradaCallbackCustomLikeEnd"});
	}
	
	function paradaCallbackCustomLikeEnd_cb(data,exception) {
		paradaCallbackCustomDefault(data);
		return pontoParada_nmPontoParada_likeEndMatch_cb(data);
	}
	
	function paradaCallbackCustomDefault(data) {
		if (data != undefined && data.length == 1) {
			var nrLatitude = getNestedBeanPropertyValue(data,":0.nrLatitudePessoa");
			var nrLongitude = getNestedBeanPropertyValue(data,":0.nrLongitudePessoa");
			//controlDisabledFields(nrLongitude,nrLatitude);
		}
	}
	
	function paradaPopupCustom(data,dialogWindow) {
		if (data != undefined) {
			var nrLatitude = getNestedBeanPropertyValue(data,":nrLatitudePessoa");
			var nrLongitude = getNestedBeanPropertyValue(data,":nrLongitudePessoa");
			//controlDisabledFields(nrLongitude,nrLatitude);
		}
		return true;
	}
	
	/*function controlDisabledFields(nrLongitude,nrLatitude) {
		setDisabled("nrLatitude",(nrLatitude != undefined && nrLatitude != ""));
		setDisabled("nrLongitude",(nrLongitude != undefined && nrLongitude != ""));
	}*/
	
</script> 