<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	service="lms.vendas.gerarCotacoesParametroClienteAction"
	onPageLoadCallBack="myOnPageLoad">

	<adsm:i18nLabels>
		<adsm:include key="LMS-01040"/>
		<adsm:include key="LMS-01045"/>
		<adsm:include key="LMS-01050"/>
		<adsm:include key="LMS-01052"/>
		<adsm:include key="LMS-01060"/>
		<adsm:include key="LMS-01070"/>
		<adsm:include key="LMS-01155"/>
		<adsm:include key="pagaPesoExcedente"/>
	</adsm:i18nLabels>

	<adsm:form
		action="/vendas/gerarCotacoes"
		height="390"
		idProperty="idParametroCliente">
		
		<adsm:hidden property="blPagaCubagem" serializable="false"/>
		<adsm:hidden property="tabelaDivisaoCliente.tabelaPreco.subtipoTabelaPreco.idSubtipoTabelaPreco" serializable="false"/>
		<adsm:hidden property="tabelaDivisaoCliente.tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco" serializable="false"/>

		<adsm:textbox 
			dataType="text" 
			label="tabela" 
			labelWidth="17%" 
			property="dsTabelaPreco"
			required="true"	
			serializable="false" 
			size="60" 
			width="45%" />

		<adsm:textbox
			dataType="text"
			label="moeda"
			labelWidth="20%"
			property="dsSimbolo"
			required="true"
			serializable="false"
			size="8"
			width="18%" />

		<%-- Include do JSP que contem os campos dos parametros do cliente --%>
		<%@ include file="parametroCliente.jsp" %>

		<adsm:buttonBar>
			<adsm:storeButton
				service="lms.vendas.gerarCotacoesParametroClienteAction.storeParametroCliente"
				callbackProperty="storeParametroCliente"
				id="salvarButton"/>
			<adsm:newButton
				id="limparButton"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script language="javascript" type="text/javascript" src="../lib/parametroCliente.js"></script>
<script type="text/javascript">

	getElement("dsSimbolo").masterLink = "true";;
	getElement("dsTabelaPreco").masterLink = "true";;

	function getDsSimbolo(){
		return getElementValue("dsSimbolo");
	}

	function validateTab() {
		return (validateTabScript(document.forms) && validateParametroCliente());
	}

	function storeParametroCliente_cb(dados, erros) {
		if (erros) {
			alert(erros);
			setFocusOnFirstFocusableField(document);
			return false;
		}
		setDisabledTaxasGeneralidades(false);
		showSuccessMessage();
	}

	function setDisabledTaxasGeneralidades(dis) {
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("taxas", dis);
		tabGroup.setDisabledTab("gen", dis);
	}

	function myOnPageLoad_cb() {
		var eventObj = new Object();
		eventObj.name = "tab_click";
		initWindow(eventObj);
	}

	function initWindow(eventObj) {
		var event = eventObj.name;
		if(event == "tab_click") {
			var tabGroup = getTabGroup(this.document);
			var idTab = tabGroup.oldSelectedTab.properties.id ;
			if(idTab == "cad" || idTab == "servAd") {
				var tabCad = tabGroup.getTab("cad");
				var idCotacao = getElementValue(tabCad.getElementById("idCotacao"))
				setDisabled("dsSimbolo", true);
				setDisabled("dsTabelaPreco", true);
				if(idCotacao && idCotacao != "") {
					findParametroByCotacao(idCotacao);
				} else { 
					inicializaTela();
				}
			}
		} else if(event == "newButton_click") {
			resetParametros();
			setDisabledTaxasGeneralidades(true);
		}
	}

	function findParametroByCotacao(idCotacao){
		var sdo = createServiceDataObject("lms.vendas.gerarCotacoesParametroClienteAction.findParametroByCotacao", "findParametroByCotacao", {idCotacao:idCotacao});
		xmit({serviceDataObjects:[sdo]});
	}

	function findParametroByCotacao_cb(dados, erros) {
		onDataLoad_cb(dados, erros);
		setDisabledTaxasGeneralidades(false);
	}

	function resetParametros() {
		var sdo = createServiceDataObject("lms.vendas.gerarCotacoesParametroClienteAction.resetParametros", "resetParametros");
		xmit({serviceDataObjects:[sdo]});
	}

	function resetParametros_cb(data, erros) {
		onDataLoad_cb(data, erros);
	}

	function inicializaTela() {
		setDisabledTaxasGeneralidades(true);
		carregaParametros();
		findLimitesDescontos("lms.vendas.gerarCotacoesParametroClienteAction.findLimitesDescontos");
	}

	function carregaParametros(){
		var sdo = createServiceDataObject("lms.vendas.gerarCotacoesParametroClienteAction.findParametroCliente", "carregaParametros");
		xmit({serviceDataObjects:[sdo]});
	}

	function carregaParametros_cb(data, erros){
		if (erros) {
			alert(erros);
			setFocusOnFirstFocusableField(document);
			return false;
		}
		onDataLoad_cb(data, erros);
	}

</script>
