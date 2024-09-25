<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.postoPassagemService">
	<adsm:form action="/municipios/manterPostosPassagem" service="lms.municipios.postoPassagemService.findByIdDetalhamento" onDataLoadCallBack="pageLoad" idProperty="idPostoPassagem">
		<adsm:hidden property="filtroRodovia" value="isNullTbm"/>
		<adsm:hidden property="tpSituacao" value="A" serializable="false"/>
		<adsm:combobox property="tpPostoPassagem" label="tipoPostoPassagem" domain="DM_POSTO_PASSAGEM" onchange="return chngTpPstPssgm(this);"  labelWidth="18%" width="32%" required="true" />
		 
		<adsm:lookup dataType="text" property="municipio" idProperty="idMunicipio" criteriaProperty="nmMunicipio"
	             action="/municipios/manterMunicipios" service="lms.municipios.municipioService.findLookup"
                 maxLength="60" size="30" minLengthForAutoPopUpSearch="3" exactMatch="false" 
                 label="localizacaoMunicipio" labelWidth="18%" width="32%" required="true">
                <adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.sgUnidadeFederativa" modelProperty="unidadeFederativa.sgUnidadeFederativa" />
                <adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.nmUnidadeFederativa" modelProperty="unidadeFederativa.nmUnidadeFederativa" />
                <adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.idUnidadeFederativa" modelProperty="unidadeFederativa.idUnidadeFederativa" />
				<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.pais.nmPais" modelProperty="unidadeFederativa.pais.nmPais" />
				<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.pais.idPais" modelProperty="unidadeFederativa.pais.idPais" />
				<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
        </adsm:lookup>

		<adsm:textbox property="municipio.unidadeFederativa.sgUnidadeFederativa" dataType="text"
				label="uf" labelWidth="18%" width="32%" disabled="true" size="3" maxLength="3" serializable="false">
			<adsm:textbox dataType="text" property="municipio.unidadeFederativa.nmUnidadeFederativa" size="30" disabled="true" serializable="false" />
		</adsm:textbox>
		<adsm:textbox label="pais" labelWidth="18%" dataType="text" property="municipio.unidadeFederativa.pais.nmPais" width="32%" size="28" serializable="false" disabled="true"/>
		
		<adsm:hidden property="municipio.unidadeFederativa.idUnidadeFederativa" serializable="true"/>
		<adsm:hidden property="municipio.unidadeFederativa.pais.idPais" serializable="false"/>
		
		<adsm:lookup label="rodovia" criteriaProperty="sgRodovia" service="lms.municipios.rodoviaService.findLookupWithUFNull" 
					 dataType="text" size="7" maxLength="10" labelWidth="18%" width="35%" action="/municipios/manterRodovias" 
					 idProperty="idRodovia" property="rodovia" onchange="return validaSigla()">
			<adsm:propertyMapping modelProperty="dsRodovia" relatedProperty="rodovia.dsRodovia"/>
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.idUnidadeFederativa" modelProperty="unidadeFederativa.idUnidadeFederativa" />
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.sgUnidadeFederativa" modelProperty="unidadeFederativa.sgUnidadeFederativa" />
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.nmUnidadeFederativa" modelProperty="unidadeFederativa.nmUnidadeFederativa" />
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.nmPais" modelProperty="pais.nmPais" />
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.idPais" modelProperty="pais.idPais" />
			<adsm:propertyMapping criteriaProperty="filtroRodovia" modelProperty="flag" />
			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
			<adsm:textbox dataType="text" property="rodovia.dsRodovia" size="25" disabled="true"/>
		</adsm:lookup>

		<adsm:textbox dataType="decimal" mask="##,###,###" property="nrKm" label="km" maxLength="8" size="10" labelWidth="18%" width="32%"/>
		<adsm:textbox dataType="currency" mask="##0.00"  property="nrLatitude" label="latitude" size="10" labelWidth="18%" width="32%"/>
		<adsm:textbox dataType="currency" mask="##0.00" property="nrLongitude" label="longitude" size="10" labelWidth="18%" width="32%"/>
		
		<adsm:combobox property="tpSentidoCobranca" label="sentidoCobranca" required="true" domain="DM_SENTIDO_COBRANCA_POSTO_PASSAGEM" labelWidth="18%" width="82%" />
		
		<adsm:lookup minLengthForAutoPopUpSearch="5" exactMatch="true" service="lms.municipios.concessionariaService.findLookup" 
				     dataType="text" property="concessionaria" criteriaProperty="pessoa.nrIdentificacao" idProperty="idConcessionaria" 
				     label="concessionaria" size="21" maxLength="20" labelWidth="18%" width="75%" required="true"
				     action="municipios/manterConcessionariaTaxasPassagem" onPopupSetValue="popUpSetC" onDataLoadCallBack="dataLoadC">
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="concessionaria.pessoa.nmPessoa"/>
			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
			<adsm:textbox dataType="text" property="concessionaria.pessoa.nmPessoa" size="25" disabled="true"/>
		</adsm:lookup>

		<adsm:range label="vigencia" labelWidth="18%" width="78%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"/>
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>
		<adsm:buttonBar>
			<adsm:button id="tiposPagamentoPostos" caption="tiposPagamentoPostos" onclick="parent.parent.redirectPage('municipios/manterTipoPagamentoPosto.do?cmd=main' + buildLinkPropertiesQueryString_tiposPagamentoPostosAndtarifasPostosPassagem());"/>
			<adsm:button id="tarifasPostosPassagem" caption="tarifasPostosPassagem" onclick="parent.parent.redirectPage('municipios/manterTarifaPostoPassagem.do?cmd=main' + buildLinkPropertiesQueryString_tiposPagamentoPostosAndtarifasPostosPassagem());"/>
			<adsm:storeButton id="storeButton" callbackProperty="afterStore" service="lms.municipios.postoPassagemService.storeMap"/>
			<adsm:newButton id="newButton"/>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<Script>
<!--
    setDisabled("storeButton",false);
	function pageLoad_cb(data,error) {
		onDataLoad_cb(data,error);
		acaoVigencia(data);
		chngTpPstPssgm();
	}
	
	function validaSigla(){
		var sigla = document.getElementById("rodovia.sgRodovia");
		sigla.value = sigla.value.toUpperCase();
		var retorno = rodovia_sgRodoviaOnChangeHandler();
		return retorno;
	}
	
	function chngTpPstPssgm(field) {
		if (field != undefined)
			comboboxChange({e:field});
			
		if (getElementValue("tpPostoPassagem") == "B"){
			if (field != undefined) {
				resetValue("rodovia.idRodovia");
				resetValue("rodovia.dsRodovia");
				resetValue("nrKm");
			}
			document.getElementById("rodovia.sgRodovia").required = "false";
			document.getElementById("nrKm").required = "false";
		}else{
			document.getElementById("rodovia.sgRodovia").required = "true";
			document.getElementById("nrKm").required = "true";
		}
	}
	
	function acaoVigencia(data) {
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
		if (acaoVigenciaAtual == 0) {
			  enabledFields();
			  setDisabled("removeButton",false);
			  setFocusOnFirstFocusableField();
		}else if (acaoVigenciaAtual == 1) {
		      setDisabled(document,true);
		      setDisabled("storeButton",false);
		      setDisabled("newButton",false);
		      setDisabled("dtVigenciaFinal");
		      setFocusOnFirstFocusableField();
		}else if (acaoVigenciaAtual == 2) {
		      setDisabled(document,true);
		      setDisabled("__buttonBar:0.newButton",false);
		      setDisabled("removeButton",true);
		      setFocusOnNewButton();
		}
		setDisabled("tarifasPostosPassagem",false);
		setDisabled("tiposPagamentoPostos",false);
	}
	
	function enabledFields() {
		setDisabled(document,false);
		setDisabled("municipio.unidadeFederativa.sgUnidadeFederativa",true);
		setDisabled("municipio.unidadeFederativa.nmUnidadeFederativa",true);
		setDisabled("municipio.unidadeFederativa.pais.nmPais",true);
		setDisabled("rodovia.dsRodovia",true);
		setDisabled("concessionaria.idConcessionaria",document.getElementById("concessionaria.idConcessionaria").masterLink == "true");
		setDisabled("concessionaria.pessoa.nmPessoa",true);
		setDisabled("removeButton",true);
		
		setDisabled("tarifasPostosPassagem",true);
		setDisabled("tiposPagamentoPostos",true);
		chngTpPstPssgm();
		setFocusOnFirstFocusableField();
	}
	
	function initWindow(eventObj) {
		if ((eventObj.name == "newButton_click") || (eventObj.name == "tab_click"))
			enabledFields();
	}
	
	function buildLinkPropertiesQueryString_tiposPagamentoPostosAndtarifasPostosPassagem() {
		var urlString = "&postoPassagem.idPostoPassagem=" + escape(getElementValue("idPostoPassagem"));
		if (document.getElementById("tpPostoPassagem").selectedIndex >= 0)
			urlString+= "&tpPosto=" + escape(document.getElementById("tpPostoPassagem")[document.getElementById("tpPostoPassagem").selectedIndex].text);

			var dsRodovia = getElementValue("rodovia.dsRodovia");
			var strRodovia = escape(getElementValue("rodovia.sgRodovia") + " - " +
					(dsRodovia != "" ? (dsRodovia + " - ") : "") +
					getElementValue("nrKm")+ " km");

			urlString+= "&localizacao=" + escape(getElementValue("municipio.nmMunicipio") + " - " + getElementValue("municipio.unidadeFederativa.sgUnidadeFederativa")
									+ " - " + getElementValue("municipio.unidadeFederativa.pais.nmPais")) +
					"&rodovia=" + strRodovia;
						
		if (document.getElementById("tpSentidoCobranca").selectedIndex >= 0)
			urlString+= "&sentido=" + escape(document.getElementById("tpSentidoCobranca")[document.getElementById("tpSentidoCobranca").selectedIndex].text);
		
		urlString+= "&pais.idPais=" + getElementValue("municipio.unidadeFederativa.pais.idPais");
		return urlString;
	}
	
	function afterStore_cb(data,exception,key) {
    	store_cb(data,exception,key);
		if (exception == undefined) {
			acaoVigencia(data);
		    setFocusOnNewButton();
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
		return concessionaria_pessoa_nrIdentificacao_likeEndMatch_cb(data);
	}
//-->
</Script>