<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.manterManifestosViagemAction" onPageLoadCallBack="myPageLoadCB">

	<adsm:i18nLabels>
		<adsm:include key="LMS-04190"/>
	</adsm:i18nLabels>

	<adsm:form id="form3" action="/expedicao/manterManifestosViagem">
		<adsm:hidden property="tpEmpresa" value="M"/>
		<adsm:hidden property="idDoctoServico"/>

		<adsm:lookup label="filialOrigem"
			property="filialOrigem"
			service="lms.expedicao.manterManifestosViagemAction.findFilial"
			action="/municipios/manterFiliais"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			dataType="text" size="3"
			minLengthForAutoPopUpSearch="3"
			exactMatch="true"
			maxLength="3" labelWidth="15%" width="50%"
			disabled="false">
			<adsm:propertyMapping criteriaProperty="tpEmpresa" modelProperty="empresa.tpEmpresa" />
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.nmFantasiaOrigem"/>		
			<adsm:textbox dataType="text" property="filial.nmFantasiaOrigem" size="30" maxLength="45" disabled="true"/>
		</adsm:lookup>

		<adsm:lookup label="filialDestino"
			property="filialDestino"
			service="lms.expedicao.manterManifestosViagemAction.findFilial"
			action="/municipios/manterFiliais"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			dataType="text" size="3"
			minLengthForAutoPopUpSearch="3"
			exactMatch="true"
			maxLength="3" labelWidth="15%" width="50%"
			disabled="false">
			<adsm:propertyMapping criteriaProperty="tpEmpresa" modelProperty="empresa.tpEmpresa" />
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.nmFantasiaDestino"/>
			<adsm:textbox dataType="text" property="filial.nmFantasiaDestino" size="30" maxLength="45"disabled="true"/>
		</adsm:lookup>

		<adsm:textbox property="nrManifesto" label="numeroManifesto2" maxLength="8" dataType="integer" width="35%"/>

		<adsm:combobox property="manifesto.tpManifestoViagem" label="tipoManifestoViagem" domain="DM_TIPO_MANIFESTO_VIAGEM"
			labelWidth="20%" width="20%"/>

		<adsm:range label="periodoEmissao" width="80%">
			<adsm:textbox dataType="JTDate" property="dtInicial"/>
			<adsm:textbox dataType="JTDate" property="dtFinal"/>
		</adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:button id="__buttonBar:0.findButton" caption="consultar" onclick="populaGrid()" disabled="false" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid
		idProperty="idManifestoViagemNacional"
		property="gridManifesto"
		service="lms.expedicao.manterManifestosViagemAction.findPaginated"
		rowCountService="lms.expedicao.manterManifestosViagemAction.getRowCount"
		rows="11"
		onRowClick="rowClick();"
	>
		<adsm:gridColumn title="numeroManifesto2" property="nmManifestoOrigem" width="25%"/>
		<adsm:gridColumn title="filialDestino" property="filialDestino" width="25%"/>
		<adsm:gridColumn title="dataHoraDeEmissao" dataType="JTDateTimeZone" property="dhEmissaoManifesto" width="25%"/>
		<adsm:gridColumn title="situacao" property="tpStatusManifesto.description" width="25%"/>
		<adsm:editColumn title="hidden" property="nrManifesto" dataType="text" field="hidden" width=""/>
		<adsm:buttonBar>
			<adsm:removeButton service="lms.expedicao.manterManifestosViagemAction.removeByIds"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script language="javascript" type="text/javascript" src="../lib/expedicao.js"></script>
<script type="text/javascript">
	function initWindow(event) {
		setDisabled("__buttonBar:0.findButton",false);
		changeTabStatus(true);
		if (event.name == "cleanButton_click"){
			findDadosSessao();
		}
	}

	function rowClick() {
		if ( getTabGroup(this.document) != null){
			// habilita as abas
			changeTabStatus(false);
		}
	}

	function changeTabStatus(status) {
		var tabGroup = getTabGroup(this.document);
		if(tabGroup){
			tabGroup.setDisabledTab("cad", status);
			tabGroup.setDisabledTab("conhecimento", status);
		}
	}

	function myPageLoadCB_cb(data, error){	
		onPageLoad_cb();	
		if(error != undefined) {
			alert(error);
		}else{
			findDadosSessao();
		}
	}

	function findDadosSessao() {
		var sdo = createServiceDataObject("lms.expedicao.manterManifestosViagemAction.findDadosSessao", "ajustaDadosSessao");
		xmit({serviceDataObjects:[sdo]});
	}

	function ajustaDadosSessao_cb(data, errorMsg, errorKey) {
		if(errorMsg) {
			alert(errorMsg);
			return;
		}

		// se não for lookup, seta a filial padrao. 
		// já se for lookup, deixa para receber a filial passada por quem chama, conforme o padrão.		
		var isLookup = window.dialogArguments && window.dialogArguments.window;
		if (!isLookup) {
			setElementValue("filialOrigem.idFilial", data.filial.idFilial);
			setElementValue("filialOrigem.sgFilial", data.filial.sgFilial);
			setElementValue("filial.nmFantasiaOrigem", data.pessoa.nmFantasia);
		}
		setElementValue("dtInicial", data.dtInicial);
		setElementValue("dtFinal", data.dtFinal);
	}

	function populaGrid(){
		if((getElementValue("filialOrigem.idFilial") != "" && getElementValue("nrManifesto") != "") ||
			(getElementValue("filialOrigem.idFilial") != "" && getElementValue("dtInicial") != "" && getElementValue("dtFinal") != "") ||
			(getElementValue("filialDestino.idFilial") != "" && getElementValue("dtInicial") != "" && getElementValue("dtFinal") != "")) {
			findButtonScript("gridManifesto", getElement("form3"));
		} else {
			alertI18nMessage("LMS-04190");
			return;
		}
	}

</script>