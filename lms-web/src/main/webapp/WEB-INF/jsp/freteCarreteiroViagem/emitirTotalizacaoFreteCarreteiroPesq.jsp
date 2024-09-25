<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
<!--
	function setMoedaDefault_cb(data) {
		moedaPais_idMoedaPais_cb(data);
		if (data != undefined && data.length > 0) {
			for(var x = 0; x < data.length; x++) {
				if (getNestedBeanPropertyValue(data[x],"blIndicadorMaisUtilizada") == "true") {
					var element = document.getElementById("moedaPais.idMoedaPais");
					element.selectedIndex = x + 1;
					comboboxChange({e:element});
					break;	
				}
			}
		}
	}
	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		document.getElementById("filial.sgFilial").serializable = true;
		var sdo = createServiceDataObject("lms.fretecarreteiroviagem.emitirTotalizacaoFreteCarreteiroAction.findDataSession","dataSession",null);
		xmit({serviceDataObjects:[sdo]});
	}
	
	//Atualiza os dados da regional dependendo da filial
	function setRelatedRegional() {
		var e = document.getElementById("regional.idRegional");
		if (e.selectedIndex > 0)
			setElementValue("regional.siglaDescricao",getNestedBeanPropertyValue(e.data[e.selectedIndex - 1],"siglaDescricao"));
	}
	function dataLoadFilial_cb(data) {
		filial_sgFilial_exactMatch_cb(data);
		if (data != undefined && data.length == 1)
			setRelatedRegional();
	}
	function setValueFilial(data) {
		setElementValue("regional.idRegional",getNestedBeanPropertyValue(data,"lastRegional.idRegional"));
		setRelatedRegional();
	}
	
	
	
	//Implementação da filial e a regional do usuário logado como padrão
	var idFilial = null;
	var sgFilial = null;
	var nmFilial = null;
	var idRegional = null;
	var nmRegional = null;
	
	function dataSession_cb(data) {
		idFilial = getNestedBeanPropertyValue(data,"filial.idFilial");
		sgFilial = getNestedBeanPropertyValue(data,"filial.sgFilial");
		nmFilial = getNestedBeanPropertyValue(data,"filial.pessoa.nmFantasia");
		
		idRegional = getNestedBeanPropertyValue(data,"regional.idRegional");
		nmRegional = getNestedBeanPropertyValue(data,"regional.siglaDescricao");
		writeDataSession();
	}
	
	function writeDataSession() {
		if (idFilial != null &&
			sgFilial != null &&
			nmFilial != null) {
			setElementValue("filial.idFilial",idFilial);
			setElementValue("filial.sgFilial",sgFilial);
			setElementValue("filial.pessoa.nmFantasia",nmFilial);
		}
		if (idRegional != null &&
			nmRegional != null) {
			setElementValue("regional.idRegional",idRegional);	
			setElementValue("regional.siglaDescricao",nmRegional);
		}
	}
	
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click" || eventObj.name == "tab_click") {
			writeDataSession();
			setMoedaDefault_cb(document.getElementById("moedaPais.idMoedaPais").data);
		}
	}

	function changeCombo(field) {
		comboboxChange({e:field});
		if (field.selectedIndex > 0) {
			var data = field.data[field.selectedIndex - 1];
			setElementValue("dtVigenciaInicial",setFormat(document.getElementById("dtVigenciaInicial"),getNestedBeanPropertyValue(data,"dtVigenciaInicial")));
			setElementValue("dtVigenciaFinal",setFormat(document.getElementById("dtVigenciaFinal"),getNestedBeanPropertyValue(data,"dtVigenciaFinal")));
		}else{
			resetValue("dtVigenciaInicial");
			resetValue("dtVigenciaFinal");
		}
		
	}
//-->
</script>
<adsm:window title="emitirTotalizacaoFreteCarreteiro" service="lms.fretecarreteiroviagem.emitirTotalizacaoFreteCarreteiroAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/freteCarreteiroViagem/emitirTotalizacaoFreteCarreteiro" service="lms.fretecarreteiroviagem.emitirTotalizacaoFreteCarreteiroAction">

		<adsm:combobox label="regional" property="regional.idRegional" optionLabelProperty="siglaDescricao" labelWidth="19%" boxWidth="200"
			optionProperty="idRegional" service="lms.fretecarreteiroviagem.emitirTotalizacaoFreteCarreteiroAction.findComboRegional" width="81%" onchange="changeCombo(this);">
	         <adsm:propertyMapping relatedProperty="regional.siglaDescricao" modelProperty="siglaDescricao"/>	         
   	         <adsm:hidden property="regional.siglaDescricao" serializable="true"/>
       </adsm:combobox>
		
		<adsm:range label="vigencia" labelWidth="19%">
			<adsm:textbox size="12" property="dtVigenciaInicial" dataType="JTDate" picker="false" disabled="true"/>
			<adsm:textbox size="12" property="dtVigenciaFinal" dataType="JTDate" picker="false" disabled="true"/>
		</adsm:range>

	   <adsm:lookup property="filial" idProperty="idFilial" criteriaProperty="sgFilial" maxLength="3" dataType="text" size="3" onPopupSetValue="setValueFilial"
			service="lms.fretecarreteiroviagem.emitirTotalizacaoFreteCarreteiroAction.findLookupFilial" label="filial"  action="/municipios/manterFiliais"
			labelWidth="19%" width="35%" minLengthForAutoPopUpSearch="3" exactMatch="false" style="width:45px" onDataLoadCallBack="dataLoadFilial">
			<adsm:propertyMapping criteriaProperty="regional.idRegional" modelProperty="regionalFiliais.regional.idRegional"/>
			<adsm:propertyMapping relatedProperty="regional.idRegional" modelProperty="lastRegional.idRegional" blankFill="false"/>
	 		<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="true"/>
		</adsm:lookup>

		<adsm:combobox property="moedaPais.idMoedaPais" required="true" optionProperty="idMoedaPais" optionLabelProperty="moeda.siglaSimbolo"
				service="lms.fretecarreteiroviagem.emitirTotalizacaoFreteCarreteiroAction.findComboMoedaPais"
				label="converterParaMoeda" labelWidth="19%" width="60%" boxWidth="85" onDataLoadCallBack="setMoedaDefault"> 
				<adsm:propertyMapping relatedProperty="moedaPais.moeda.dsSimbolo" modelProperty="moeda.dsSimbolo"/>
				<adsm:propertyMapping relatedProperty="moedaPais.moeda.siglaSimbolo" modelProperty="moeda.siglaSimbolo"/>
				<adsm:hidden property="moedaPais.moeda.dsSimbolo"/>
				<adsm:hidden property="moedaPais.moeda.siglaSimbolo"/>
		</adsm:combobox>
		
		<adsm:range label="periodoEmissao" width="68%" labelWidth="19%">
			<adsm:textbox dataType="JTDate" property="dtEmissaoInicial"/>
			<adsm:textbox dataType="JTDate" property="dtEmissaoFinal"/>
		</adsm:range>
		<adsm:range label="periodoFechamento" width="68%" labelWidth="19%">
			<adsm:textbox dataType="JTDate" property="dtFechamentoInicial"/>
			<adsm:textbox dataType="JTDate" property="dtFechamentoFinal"/>
		</adsm:range>
		
		<adsm:combobox label="formatoRelatorio" labelWidth="19%" property="tpFormatoRelatorio" domain="DM_FORMATO_RELATORIO" defaultValue="pdf" required="true"/>
		
		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.fretecarreteiroviagem.emitirTotalizacaoFreteCarreteiroAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar>	
	</adsm:form>
</adsm:window>

<script type="text/javascript">
<!--
	function changeCombo(field) {
		comboboxChange({e:field});
		if (field.selectedIndex > 0) {
			var data = field.data[field.selectedIndex - 1];
			if (getNestedBeanPropertyValue(data,"dtVigenciaInicial") != undefined) {
				setElementValue("dtVigenciaInicial",setFormat(document.getElementById("dtVigenciaInicial"),getNestedBeanPropertyValue(data,"dtVigenciaInicial")));
			} else resetValue("dtVigenciaInicial");
			if (getNestedBeanPropertyValue(data,"dtVigenciaFinal") != undefined) {
				setElementValue("dtVigenciaFinal",setFormat(document.getElementById("dtVigenciaFinal"),getNestedBeanPropertyValue(data,"dtVigenciaFinal")));
			} else resetValue("dtVigenciaFinal");
		}else{
			resetValue("dtVigenciaInicial");
			resetValue("dtVigenciaFinal");
		}
		
	}
//-->
</script>
