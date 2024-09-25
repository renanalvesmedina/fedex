<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window onPageLoadCallBack="totalFretePageLoad">
	<adsm:form action="/freteCarreteiroViagem/emitirTotalFretesPagos" >
		<adsm:i18nLabels>
			<adsm:include key="requiredField"/>
			<adsm:include key="filial"/>
		</adsm:i18nLabels>
		<adsm:lookup property="filial" idProperty="idFilial" criteriaProperty="sgFilial" maxLength="3"
				service="lms.fretecarreteiroviagem.emitirRecibosOcorrenciasAction.findFilial" dataType="text" label="filial" size="3"
				action="/municipios/manterFiliais" labelWidth="24%" width="80%" minLengthForAutoPopUpSearch="3"
				exactMatch="false" style="width:45px" disabled="false">
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" />
		</adsm:lookup>

		<adsm:combobox property="tpCarreteiro" label="tipoProprietario" domain="DM_TIPO_PROPRIETARIO" labelWidth="24%" width="60%">
			<adsm:propertyMapping relatedProperty="dsTpCarreteiro" modelProperty="description"/>
			<adsm:hidden property="dsTpCarreteiro"/>
		</adsm:combobox>

		<adsm:combobox property="tpRecibo" label="tipoRecibo" domain="DM_TIPO_RECIBO_PAGAMENTO_FRETE_CARRETEIR" labelWidth="24%" width="60%" required="true">	
			<adsm:propertyMapping relatedProperty="dsTpRecibo" modelProperty="description"/>
			<adsm:hidden property="dsTpRecibo"/>
		</adsm:combobox>

		<adsm:combobox property="moedaPais.idMoedaPais" autoLoad="true" required="true" onDataLoadCallBack="loadMoedaPadrao"
				optionProperty="idMoedaPais" optionLabelProperty="moeda.siglaSimbolo" 
				service="lms.fretecarreteiroviagem.emitirRecibosOcorrenciasAction.findMoedaByPais"
				label="converterParaMoeda" labelWidth="24%" width="60%" boxWidth="85"> 
			<adsm:propertyMapping relatedProperty="moedaPais.moeda.dsSimbolo" modelProperty="moeda.dsSimbolo"/>
			<adsm:propertyMapping relatedProperty="moeda.siglaSimbolo" modelProperty="moeda.siglaSimbolo"/>
			<adsm:propertyMapping relatedProperty="moeda.idMoeda" modelProperty="moeda.idMoeda"/>
		</adsm:combobox>
		<adsm:hidden property="moedaPais.moeda.dsSimbolo"/>
		<adsm:hidden property="moeda.siglaSimbolo"/>
		<adsm:hidden property="moeda.idMoeda"/>
		<adsm:hidden property="pais.idPais"/>

		<adsm:combobox label="formatoRelatorio" labelWidth="24%" required="true" property="tpFormatoRelatorio" domain="DM_FORMATO_RELATORIO" defaultValue="pdf"/>

		<adsm:range label="periodoDePagamento" required="true" labelWidth="24%" width="76%" >
			<adsm:textbox dataType="JTDate" property="dtPeriodoInicial"/> 
			<adsm:textbox dataType="JTDate" property="dtPeriodoFinal"/>
		</adsm:range>

		<adsm:buttonBar>
			<adsm:button id="visualizar" caption="visualizar" onclick="openReport()"/>
			<adsm:resetButton/>
		</adsm:buttonBar>

	</adsm:form>
</adsm:window>
<script language="javascript" type="text/javascript">

	function totalFretePageLoad_cb(){
		document.getElementById("pais.idPais").masterLink = "true";
		document.getElementById("filial.sgFilial").serializable = true;
		var data = new Array();
		var sdo = createServiceDataObject("lms.fretecarreteiroviagem.emitirTotalFretesPagosAction.findIdPaisSessao", "setaPais", data);
		xmit({serviceDataObjects:[sdo]});
	}

	function setaPais_cb(data){
		if (data != undefined)
			setElementValue("pais.idPais", data._value);
	}

	function initWindow(evt) {
		setDisabled("visualizar", false);
		if (evt.name == 'cleanButton_click' || evt.name == 'tab_load')
			loadDadosSessao();
	}

	function loadDadosSessao(){
		var data = new Array();
		var sdo = createServiceDataObject("lms.fretecarreteiroviagem.emitirTotalFretesPagosAction.findDadosUsuario", "setaDadosUsuario", data);
		xmit({serviceDataObjects:[sdo]});
	}

	function setaDadosUsuario_cb(data){
		if (data != undefined){
			setElementValue("filial.idFilial", data.idFilial);
			setElementValue("filial.sgFilial", data.sgFilial);
			setElementValue("filial.pessoa.nmFantasia", data.nmFantasia);
			setaMoeda_cb(data);
		}
	}

	function loadMoedaPadrao_cb(data){
		moedaPais_idMoedaPais_cb(data);
		loadMoedaPadrao();
	}

	function loadMoedaPadrao(){
		var data = new Array();
		var sdo = createServiceDataObject("lms.fretecarreteiroviagem.emitirTotalFretesPagosAction.findMoedaUsuario", "setaMoeda", data);
		xmit({serviceDataObjects:[sdo]});
	}

	function setaMoeda_cb(data){
		if (data != undefined) {
			setElementValue("moedaPais.idMoedaPais", data.idMoedaPais);
			setElementValue("moeda.idMoeda", data.idMoeda);
			setElementValue("moedaPais.moeda.dsSimbolo", data.dsSimbolo);
			setElementValue("moeda.siglaSimbolo", data.siglaSimbolo);
		}
	}

	function openReport() {
		if (getElementValue("tpRecibo") == "C" && getElementValue("filial.idFilial") == "") {
			alertRequiredField("filial.idFilial");
			document.getElementById("filial.sgFilial").focus();
		} else
			reportButtonScript('lms.fretecarreteiroviagem.emitirTotalFretesPagosAction', 'openPdf', document.forms[0]);
	}

</script>