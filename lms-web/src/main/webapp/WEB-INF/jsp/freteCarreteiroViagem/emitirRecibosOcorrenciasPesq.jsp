<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window onPageLoadCallBack="recibosPageLoad">

	<adsm:form action="/freteCarreteiroViagem/emitirRecibosOcorrencias" >
		
		<adsm:lookup property="filial" idProperty="idFilial" criteriaProperty="sgFilial" maxLength="3"
				service="lms.fretecarreteiroviagem.emitirRecibosOcorrenciasAction.findFilial" dataType="text" label="filial" size="3"
				action="/municipios/manterFiliais" labelWidth="24%" width="80%" minLengthForAutoPopUpSearch="3"
				exactMatch="false" style="width:45px" disabled="false" required="true" >
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" />
		</adsm:lookup>
		
		<adsm:combobox property="tpRecibo" label="tipoRecibo" required="true" domain="DM_TIPO_RECIBO_PAGAMENTO_FRETE_CARRETEIR" labelWidth="24%" width="60%">	
			<adsm:propertyMapping relatedProperty="dsTpRecibo"  modelProperty="description"/>
			<adsm:hidden property="dsTpRecibo"/>
		</adsm:combobox>
	
		<adsm:combobox property="tpCarreteiro" label="tipoVinculo" domain="DM_TIPO_VINCULO_CARRETEIRO" onDataLoadCallBack="tpCarreteiroDataLoad" labelWidth="24%" width="60%">
			<adsm:propertyMapping relatedProperty="dsTpCarreteiro"  modelProperty="description"/>
			<adsm:hidden property="dsTpCarreteiro"/>
		</adsm:combobox>
	
		<adsm:combobox property="tpSituacaoRecibo" label="situacaoRecibo"  domain="DM_STATUS_RECIBO_PAGAMENTO_FRETE_CARRETE" labelWidth="24%" width="60%">
			<adsm:propertyMapping relatedProperty="dsTpSituacaoRecibo"  modelProperty="description"/>
			<adsm:hidden property="dsTpSituacaoRecibo"/>
		</adsm:combobox>
	
		<adsm:combobox property="tpOcorrencia" label="tipoOcorrencia" domain="DM_TIPO_OCORRENCIA_RECIBO_CARRETEIRO" labelWidth="24%" width="60%">
			<adsm:propertyMapping relatedProperty="dsTpOcorrencia"  modelProperty="description"/>
			<adsm:hidden property="dsTpOcorrencia"/>
		</adsm:combobox>
	
		<adsm:combobox property="moedaPais.idMoedaPais" autoLoad="true" required="true"
				optionProperty="idMoedaPais" optionLabelProperty="moeda.siglaSimbolo" onDataLoadCallBack="loadMoedaPadrao"
				service="lms.fretecarreteiroviagem.emitirRecibosOcorrenciasAction.findMoedaByPais"
				label="converterParaMoeda" labelWidth="24%" width="60%" boxWidth="85"  > 
			<adsm:propertyMapping relatedProperty="moedaPais.moeda.dsSimbolo" modelProperty="moeda.dsSimbolo"/>
			<adsm:propertyMapping relatedProperty="moeda.siglaSimbolo" modelProperty="moeda.siglaSimbolo"/>
			<adsm:propertyMapping relatedProperty="moeda.idMoeda" modelProperty="moeda.idMoeda"/>
		</adsm:combobox>
		<adsm:hidden property="moedaPais.moeda.dsSimbolo"/>
		<adsm:hidden property="moeda.siglaSimbolo"/>
		<adsm:hidden property="moeda.idMoeda"/>
		<adsm:hidden property="pais.idPais"/>
		
		<adsm:combobox label="formatoRelatorio" labelWidth="24%" required="true" property="tpFormatoRelatorio" domain="DM_FORMATO_RELATORIO" defaultValue="pdf"/>
					   
		<adsm:range label="periodoEmissaoRecibo" required="true" labelWidth="24%" width="60%" maxInterval="30" >
			<adsm:textbox dataType="JTDate" property="dtPeriodoInicial"/> 
			<adsm:textbox dataType="JTDate" property="dtPeriodoFinal"/>
		</adsm:range>
	
		<adsm:buttonBar>
			<adsm:button id="visualizar" caption="visualizar" onclick="abreRelatorio()"/> 
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	    <adsm:i18nLabels>
    	     <adsm:include key="LMS-00013"/>
    	     <adsm:include key="filial"/>
    	     <adsm:include key="tipoRecibo"/>
	    </adsm:i18nLabels>
	
	</adsm:form>
	
</adsm:window>
<script type="text/javascript">
<!--
	function recibosPageLoad_cb(){
		document.getElementById("pais.idPais").masterLink = "true";
		document.getElementById("filial.sgFilial").serializable = true;
		var data = new Array();
		
		var sdo = createServiceDataObject("lms.fretecarreteiroviagem.emitirRecibosOcorrenciasAction.findIdPaisSessao",
				"setaPais",data);
		xmit({serviceDataObjects:[sdo]});
	}
		
	function setaPais_cb(data){
		if (data != undefined)
			setElementValue("pais.idPais", data._value);
	}
	
	function initWindow(evt){
		setDisabled("visualizar", false);
		if (evt.name == 'tab_load') {
			loadDadosSessao();
		} else if (evt.name == 'cleanButton_click') {
			setValuesUsuarioLogado();
		}
	}


	function tpCarreteiroDataLoad_cb(data){
		var newData = new Array();
		for(var i=0; i < data.length; i++){
			if (getNestedBeanPropertyValue(data[i], "value") != "F"){
				newData[i] = data[i];
			}
		}
		
		tpCarreteiro_cb(newData);
	}

	function loadMoedaPadrao_cb(data){
		moedaPais_idMoedaPais_cb(data);
		setElementValue("moedaPais.idMoedaPais", moedaPais_idMoedaPais);
	}


	function loadDadosSessao(){
		var data = new Array();
		var sdo = createServiceDataObject("lms.fretecarreteiroviagem.emitirRecibosOcorrenciasAction.findDadosUsuario",
				"setaDadosUsuario",data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	var moedaPais_idMoedaPais;
	
	var dataUsuario = undefined;
				
	function setaDadosUsuario_cb(data,error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		dataUsuario = data;
		setValuesUsuarioLogado();
	}
	
	function setValuesUsuarioLogado() {
		if (dataUsuario != undefined) {
			setElementValue("filial.idFilial", dataUsuario.idFilial);
			setElementValue("filial.sgFilial", dataUsuario.sgFilial);
			setElementValue("filial.pessoa.nmFantasia", dataUsuario.nmFantasia);
			setElementValue("moedaPais.idMoedaPais", dataUsuario.idMoedaPais);
			moedaPais_idMoedaPais = dataUsuario.idMoedaPais;
			setElementValue("moeda.idMoeda", dataUsuario.idMoeda);
			setElementValue("moedaPais.moeda.dsSimbolo", dataUsuario.dsSimbolo);
			setElementValue("moeda.siglaSimbolo", dataUsuario.siglaSimbolo);
		}
	}
	
	function abreRelatorio(){
		if (getElementValue("filial.idFilial") == "" &&
			getElementValue("tpRecibo") == ""){
		
			alert(i18NLabel.getLabel("LMS-00013") + " " + i18NLabel.getLabel("filial") + ", " + i18NLabel.getLabel("tipoRecibo") + ".");
			
		} else {
			reportButtonScript('lms.fretecarreteiroviagem.emitirRecibosOcorrenciasAction', 'openPdf', document.forms[0]);
		}		
	}


//-->
</script>

