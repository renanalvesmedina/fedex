<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window onPageLoadCallBack="pageLoad"> 
	<adsm:form action="/municipios/emitirPostoPassagem" service="lms.municipios.emitirPostoPassagemAction">
		<adsm:combobox property="tpPostoPassagem.value" domain="DM_POSTO_PASSAGEM" label="tipoPostoPassagem" labelWidth="19%" width="83%">
							<adsm:propertyMapping relatedProperty="tpPostoPassagem.descricao" modelProperty="description"/>
		</adsm:combobox>
		<adsm:hidden property="tpPostoPassagem.descricao"/>
		<adsm:lookup action="/municipios/manterMunicipios" 					 
						 service="lms.municipios.emitirPostoPassagemAction.findLookupMunicipio"
						 dataType="text" 
						 property="municipio" 
						 idProperty="idMunicipio"						 
						 criteriaProperty="nmMunicipio" 						 
						 maxLength="60"
						 size="35"
						 width="83%"
						 labelWidth="19%"
						 onDataLoadCallBack="dataLoadM"
						 onPopupSetValue="popUpM"
						 exactMatch="false"
						 minLengthForAutoPopUpSearch="3"
						 label="localizacaoMunicipio">

						 <adsm:propertyMapping relatedProperty="pais.idPais" modelProperty="unidadeFederativa.pais.idPais" blankFill="false"/>
						 <adsm:propertyMapping relatedProperty="pais.nmPais" modelProperty="unidadeFederativa.pais.nmPais" blankFill="false"/>

						 <adsm:propertyMapping relatedProperty="unidadeFederativa.idUnidadeFederativa" modelProperty="unidadeFederativa.idUnidadeFederativa" blankFill="false"/>
						 <adsm:propertyMapping relatedProperty="unidadeFederativa.sgUnidadeFederativa" modelProperty="unidadeFederativa.sgUnidadeFederativa" blankFill="false"/>
						 <adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="unidadeFederativa.nmUnidadeFederativa" blankFill="false"/>


						 <adsm:propertyMapping criteriaProperty="unidadeFederativa.idUnidadeFederativa" modelProperty="unidadeFederativa.idUnidadeFederativa" addChangeListener="false"/>
						 <adsm:propertyMapping criteriaProperty="unidadeFederativa.sgUnidadeFederativa" modelProperty="unidadeFederativa.sgUnidadeFederativa" addChangeListener="false"/>
						 <adsm:propertyMapping criteriaProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="unidadeFederativa.nmUnidadeFederativa" addChangeListener="false"/>

						 <adsm:propertyMapping criteriaProperty="pais.idPais" modelProperty="unidadeFederativa.pais.idPais" addChangeListener="false"/>
						 <adsm:propertyMapping criteriaProperty="pais.nmPais" modelProperty="unidadeFederativa.pais.nmPais" addChangeListener="false"/>

		</adsm:lookup>

		<adsm:lookup property="unidadeFederativa"
					idProperty="idUnidadeFederativa"
					criteriaProperty="sgUnidadeFederativa"
					label="uf"
					dataType="text"
					service="lms.municipios.emitirPostoPassagemAction.findLookupUnidadeFederativa"
					width="83%"
					maxLength="3"
					size="3"				
					onDataLoadCallBack="dataLoadU"
					onPopupSetValue="popUpU"
					labelWidth="19%"
					action="/municipios/manterUnidadesFederativas"
					minLengthForAutoPopUpSearch="2"
					exactMatch="false"
					onchange="return changeUF();">
    		<adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa"/>
			<adsm:textbox dataType="text" property="unidadeFederativa.nmUnidadeFederativa" size="35" serializable="false" disabled="true"/>
			<adsm:propertyMapping relatedProperty="pais.idPais" modelProperty="pais.idPais" blankFill="false"/>
			<adsm:propertyMapping relatedProperty="pais.nmPais" modelProperty="pais.nmPais" blankFill="false"/>

			<adsm:propertyMapping relatedProperty="municipio.idMunicipio" modelProperty="" blankFill="false"/>
			<adsm:propertyMapping relatedProperty="municipio.nmMunicipio" modelProperty="" blankFill="false"/>

			<adsm:propertyMapping criteriaProperty="pais.idPais" modelProperty="pais.idPais" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="pais.nmPais" modelProperty="pais.nmPais" addChangeListener="false"/>
		</adsm:lookup>
		
		<adsm:lookup property="pais"
					idProperty="idPais"
					service="lms.municipios.emitirPostoPassagemAction.findLookupPais"
					dataType="text"
					criteriaProperty="nmPais"
					label="pais"
					size="35"
					maxLength="60"
					action="/municipios/manterPaises"
					exactMatch="false"
					minLengthForAutoPopUpSearch="3"
					onDataLoadCallBack="dataLoadP"
					onPopupSetValue="popUpP"
					labelWidth="19%"
					width="83%"
					onchange="return changeP();"
					required="true">
					<adsm:propertyMapping relatedProperty="unidadeFederativa.idUnidadeFederativa" modelProperty=""/>
					<adsm:propertyMapping relatedProperty="unidadeFederativa.sgUnidadeFederativa" modelProperty=""/>
					<adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty=""/>
										
		</adsm:lookup>

		<adsm:lookup dataType="text"
						property="rodovia"
						idProperty="idRodovia"
						criteriaProperty="sgRodovia"
						service="lms.municipios.emitirPostoPassagemAction.findLookupRodovia"
						action="/municipios/manterRodovias"
						cmd="list"
						label="rodovia"
						size="7"
						maxLength="10"
						width="73%"
						labelWidth="19%"
						onchange="return validaSigla()">
			<adsm:propertyMapping modelProperty="dsRodovia" relatedProperty="rodovia.dsRodovia"/>
			<adsm:textbox dataType="text" property="rodovia.dsRodovia" size="35" maxLength="50" disabled="true"/>
			<adsm:propertyMapping criteriaProperty="pais.idPais" modelProperty="pais.idPais"/>
			<adsm:propertyMapping criteriaProperty="pais.nmPais" modelProperty="pais.nmPais"/>
		</adsm:lookup>


		<adsm:range label="km" labelWidth="19%" width="33%">
             <adsm:textbox dataType="integer" size="6" maxLength="6" property="nrKmInicial"/>
             <adsm:textbox dataType="integer" size="6" maxLength="6" property="nrKmFinal"/>
		</adsm:range>

		<adsm:combobox property="tpSentidoCobranca.value" domain="DM_SENTIDO_COBRANCA_POSTO_PASSAGEM" label="sentidoCobranca" labelWidth="19%" width="33%">		
					<adsm:propertyMapping relatedProperty="tpSentidoCobranca.descricao" modelProperty="description"/>
		</adsm:combobox>
		<adsm:hidden property="tpSentidoCobranca.descricao"/>

		<adsm:lookup dataType="text" property="concessionaria" idProperty="idConcessionaria"
				service="lms.municipios.emitirPostoPassagemAction.findLookupConcessionaria"
				criteriaProperty="pessoa.nrIdentificacao"
				relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				label="concessionaria"
				size="20" maxLength="20"
				labelWidth="19%" width="73%" exactMatch="true"
				action="municipios/manterConcessionariaTaxasPassagem" >
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="concessionaria.pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="concessionaria.pessoa.nmPessoa" size="35" disabled="true" serializable="false"/>
		</adsm:lookup>

		<adsm:combobox property="moeda.idMoeda" service="lms.municipios.emitirPostoPassagemAction.findComboMoeda"
				 optionLabelProperty="moeda.siglaSimbolo" optionProperty="moeda.idMoeda" label="converterParaMoeda" 
				 onDataLoadCallBack="dataLoadMoeda" labelWidth="19%" width="33%" autoLoad="false" required="true" >
			<adsm:propertyMapping criteriaProperty="pais.idPais" modelProperty="pais.idPais"/>
			<adsm:propertyMapping relatedProperty="moeda.siglaSimbolo" modelProperty="moeda.siglaSimbolo"/>
			<adsm:propertyMapping relatedProperty="moeda.dsSimbolo" modelProperty="moeda.dsSimbolo"/> 
		</adsm:combobox>
		<adsm:hidden property="moeda.siglaSimbolo"/>
		<adsm:hidden property="moeda.dsSimbolo"/>
		<adsm:combobox property="apenasVigentes.value" domain="DM_SIM_NAO" label="apenasVigentes" labelWidth="19%" width="33%" defaultValue="S">		
			<adsm:propertyMapping relatedProperty="apenasVigentes.descricao" modelProperty="description"/>
		</adsm:combobox>
		<adsm:hidden property="apenasVigentes.descricao"/>

		<adsm:combobox labelWidth="19%" label="formatoRelatorio" property="tpFormatoRelatorio" domain="DM_FORMATO_RELATORIO" defaultValue="pdf" required="true"/>
		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.municipios.emitirPostoPassagemAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar> 
	</adsm:form>
</adsm:window>
<script type="text/javascript">
	document.getElementById("rodovia.sgRodovia").serializable="true";
<!--
	
	function validaSigla(){
		var sigla = document.getElementById("rodovia.sgRodovia");
		sigla.value = sigla.value.toUpperCase();
		var retorno = rodovia_sgRodoviaOnChangeHandler();
		return retorno;
	}
	
	var idPais = undefined;
	var nmPais = undefined;
	
	function pageLoad_cb(data) {
		onPageLoad_cb(data);
			    
	    /**
	     * Foi chamado desse maneira, por que a arquitetura simplesmente nao chama o onChange quando o campo é limpo, e tem que ser validado tambem!
	     */
		document.getElementById("nrKmInicial").onblur = changeRequired;
		document.getElementById("nrKmFinal").onblur = changeRequired;
		
		// carrega info do usuário logado
		findInfoUsuarioLogado();
	}
		
	function changeRequired() {
		if (getElementValue("nrKmInicial") != "" || getElementValue("nrKmFinal") != "") {
			document.getElementById("nrKmInicial").required = "true";
			document.getElementById("nrKmFinal").required = "true";
		}else{
			document.getElementById("nrKmInicial").required = "false";
			document.getElementById("nrKmFinal").required = "false";
		}
	}
	
	document.getElementById("municipio.nmMunicipio").serializable = "true";
	document.getElementById("pais.nmPais").serializable = "true";
	document.getElementById("unidadeFederativa.sgUnidadeFederativa").serializable = "true";
	document.getElementById("concessionaria.pessoa.nmPessoa").serializable = "true";
	
	function initWindow(eventObject) {
		if (eventObject.name == "cleanButton_click") {
			document.getElementById("nrKmInicial").required = "false";
			document.getElementById("nrKmFinal").required = "false";
			populateInfoUsuarioLogado();
		}
	}
	
	function dataLoadM_cb(data) {
		var flag = municipio_nmMunicipio_exactMatch_cb(data);
		if (flag)
			carregaMoedas();
		return flag;
	}
	
	function popUpM(data) {
		setElementValue("pais.idPais",getNestedBeanPropertyValue(data,"unidadeFederativa.pais.idPais"));
		carregaMoedas();
		return true;
	}
	
	function dataLoadU_cb(data) {
		var flag = unidadeFederativa_sgUnidadeFederativa_exactMatch_cb(data);
		if (flag)
			carregaMoedas();
		return flag;
	}	
	function popUpU(data) {
		setElementValue("pais.idPais",getNestedBeanPropertyValue(data,"pais.idPais"));
		carregaMoedas();
		return true;
	}
	
	function changeP() {
		var flag = pais_nmPaisOnChangeHandler();
		if (flag == true && getElementValue("pais.nmPais") == "")
		   resetValue("moeda.idMoeda");
		
		//Limpa o municício quando apaga ou muda de país   
		resetValue("municipio.idMunicipio");
		resetValue("municipio.nmMunicipio");
		return flag;
	}
	
	function changeUF() {
	   var flag = unidadeFederativa_sgUnidadeFederativaOnChangeHandler();
	   resetValue("municipio.idMunicipio");
       resetValue("municipio.nmMunicipio");
	   return flag;
	   
	}
	
	function dataLoadP_cb(data) {
		return dataLoadP_default(lookupExactMatch({e:document.getElementById("pais.idPais"), data:data, callBack:"dataLoadP_LikeEnd"}));
	}
	
	function dataLoadP_LikeEnd_cb(data,exception) {
		return dataLoadP_default(pais_nmPais_likeEndMatch_cb(data));
	}
	
	function dataLoadP_default(flag) {
		if (flag == true)
			carregaMoedas();
		return flag;
	}
	
	function popUpP(data) {
		setElementValue("pais.idPais",getNestedBeanPropertyValue(data,"idPais"));
		carregaMoedas();
		return true;
	}
	
	
	function carregaMoedas() {
	    var e = document.getElementById("moeda.idMoeda");
	    var sdo = createServiceDataObject(e.service, e.callBack, {pais:{idPais:getElementValue("pais.idPais")}});
	    xmit({serviceDataObjects:[sdo]});
	    notifyElementListeners({e:document.getElementById("pais.idPais")});
	}
	
	function findInfoUsuarioLogado() {
		var sdo = createServiceDataObject("lms.municipios.emitirPostoPassagemAction.findInfoUsuarioLogado",
				"findInfoUsuarioLogado",undefined);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function findInfoUsuarioLogado_cb(data,error) {
		idPais = getNestedBeanPropertyValue(data,"idPais");
		nmPais = getNestedBeanPropertyValue(data,"nmPais");
		populateInfoUsuarioLogado();
	}

	function populateInfoUsuarioLogado() {
		setElementValue("pais.idPais",idPais);
		setElementValue("pais.nmPais",nmPais);
		//notifyElementListeners({e:document.getElementById("pais.idPais")});
		//comboboxChange({e:document.getElementById("moedaPais.idMoedaPais")});
		var e = document.getElementById("moeda.idMoeda");
		var sdo = createServiceDataObject(e.service, e.callBack, {pais:{idPais:getElementValue("pais.idPais")}});
	    xmit({serviceDataObjects:[sdo]});
	}
	
	function dataLoadMoeda_cb(data) {
		moeda_idMoeda_cb(data);
		//setElementValue("moeda.idMoeda",idMoeda);
		for (var x = 0; x < data.length; x++) {
			if (getNestedBeanPropertyValue(data[x],"blIndicadorMaisUtilizada") == "true") {
				document.getElementById("moeda.idMoeda").selectedIndex = x + 1;
				break;
			}
		}
		comboboxChange({e:document.getElementById("moeda.idMoeda")});
	}
	
	
//-->
</script>