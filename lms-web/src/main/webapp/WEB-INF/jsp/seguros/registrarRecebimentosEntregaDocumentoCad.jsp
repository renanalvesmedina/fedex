<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.seguros.action.registrarRecebimentosEntregaDocumentoAction" onPageLoadCallBack="pageLoad" >
	<adsm:form action="/seguros/registrarRecebimentosEntregaDocumento" service="lms.seguros.action.registrarRecebimentosEntregaDocumentoAction.findByIdCustom" idProperty="idDoctoProcessoSinistro" onDataLoadCallBack="dataLoad">

		<adsm:hidden property="processoSinistro.idProcessoSinistro" />
		<adsm:textbox label="numeroProcesso" property="processoSinistro.nrProcessoSinistro" dataType="text" labelWidth="15%" width="85%" disabled="true" />
		
		<adsm:combobox property="tpEntregaRecebimento" label="acao" domain="DM_ENTREGA_RECEBIMENTO" labelWidth="16%" width="84%" required="true" onchange="onTpEntregaRecebimentoChange(this);" renderOptions="true"/>

		<adsm:textbox dataType="JTDateTimeZone" property="dhCadastroDocumento" label="data" required="true" labelWidth="16%" width="24%"/>
		
		<adsm:combobox property="tipoDocumentoSeguro.idTipoDocumentoSeguro" 
					   service="lms.seguros.action.registrarRecebimentosEntregaDocumentoAction.findComboTiposDocumentoSeguro" 
					   optionProperty="idTipoDocumentoSeguro"
					   optionLabelProperty="dsTipo"
					   label="tipoDocumento" required="true" 
					   labelWidth="16%" width="44%" onlyActiveValues="true"/>
		
		<adsm:textbox dataType="text" property="nrDocumento" label="documento" maxLength="15" size="15" required="true" labelWidth="16%" width="24%"/>
		<adsm:textbox dataType="text" property="nmRecebedor" label="entregaRecebimento" maxLength="50" size="40" labelWidth="16%" width="44%" required="true"/>
		<adsm:textbox dataType="integer" property="nrProtocolo" label="protocolo" maxLength="10" size="15" labelWidth="16%" width="24%" disabled="true"/>
		<adsm:textarea property="obDocumentoProcesso" label="observacao" rows="3" maxLength="500" columns="120" labelWidth="16%" width="84%"/>

		<adsm:buttonBar>
			<adsm:storeButton service="lms.seguros.action.registrarRecebimentosEntregaDocumentoAction.storeCustom"/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

	document.getElementById("processoSinistro.idProcessoSinistro").masterLink = "true";
	document.getElementById("processoSinistro.nrProcessoSinistro").masterLink = "true";

	function initWindow(event) {
		if (event.name == 'tab_click' || event.name == 'newButton_click') {
			newPage();
		}
	}
	
	function dataLoad_cb(data, error) {
		onDataLoad_cb(data, error);

		if (error == undefined) {
			if(getNestedBeanPropertyValue(data, "tpEntregaRecebimento.value") == "R") {
				disableAcao(false);
				setDisabled("tpEntregaRecebimento", false);
			} else {
				disableAcao(true);			
				setDisabled("tpEntregaRecebimento", true);				
			}
		}
	}

	function disableAcao(value) {
		if (value == true) {
			// resetValue("nrProtocolo");
			setDisabled("nrProtocolo", true);
			document.getElementById("nrProtocolo").required = "false";			
		} else {
			setDisabled("nrProtocolo", false);
			document.getElementById("nrProtocolo").required = "true";
		}
	}
	
	function onTpEntregaRecebimentoChange(element) {
		if (element.value == 'R') {
			disableAcao(false);
			resetValue("nrProtocolo");			
		} else {
			disableAcao(true);
			resetValue("nrProtocolo");
		}
	}

	function pageLoad_cb() {
		onPageLoad_cb();
		newPage();
	}
	
	function newPage() {
		getDataAtual();
		setDisabled("nrProtocolo", true);
		setDisabled("tpEntregaRecebimento", false);		
		document.getElementById("nrProtocolo").required = "false";
		setFocusOnFirstFocusableField();			
	}
	
	function getDataAtual() {
		var data = new Array();
		var sdo = createServiceDataObject("lms.seguros.action.registrarRecebimentosEntregaDocumentoAction.getDate", "getDataAtual", data);
    	xmit({serviceDataObjects:[sdo]});
	}
	
	function getDataAtual_cb(data, error) {
		setElementValue("dhCadastroDocumento", setFormat("dhCadastroDocumento", getNestedBeanPropertyValue(data, "dtConsulta")));
	}

</script>
