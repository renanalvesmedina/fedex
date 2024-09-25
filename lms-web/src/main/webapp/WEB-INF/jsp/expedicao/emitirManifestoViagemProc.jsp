<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.emitirManifestoViagemAction">
	<adsm:i18nLabels>
		<adsm:include key="LMS-04100"/>
		<adsm:include key="LMS-04101"/>
		<adsm:include key="LMS-04176"/>
	</adsm:i18nLabels>

	<adsm:form action="/expedicao/emitirManifestoViagem">
		<adsm:hidden property="idFilial"/>
		<adsm:hidden property="nmFilial"/>
		<adsm:textbox
				label="preManifesto"
				property="sgFilial"
				dataType="text"
				disabled="true"
				size="5"
				maxLength="3"
		>
			<adsm:combobox
					property="manifesto.idManifesto"
					optionProperty="idManifesto"
					optionLabelProperty="nrPreManifesto"
					service="lms.expedicao.emitirManifestoViagemAction.findComboPreManifestoInit"
					onDataLoadCallBack="findComboPreManifestoInit"
					required="true">
				<adsm:propertyMapping relatedProperty="tpModal.description" modelProperty="tpModal.description"/>
				<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.idFilial" modelProperty="filialByIdFilialDestino.idFilial"/>
				<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.sgFilial" modelProperty="filialByIdFilialDestino.sgFilial"/>
				<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia" modelProperty="filialByIdFilialDestino.pessoa.nmFantasia"/>
			</adsm:combobox>
		</adsm:textbox>

		<adsm:textbox
				label="modal"
				property="tpModal.description"
				dataType="text"
				size="20"
				serializable="false"
				disabled="true"
		/>

		<adsm:hidden property="filialByIdFilialDestino.idFilial"/>
		<adsm:textbox
				label="filialDestino2"
				property="filialByIdFilialDestino.sgFilial"
				dataType="text"
				disabled="true"
				size="5" maxLength="3"
		>
			<adsm:textbox
					dataType="text"
					property="filialByIdFilialDestino.pessoa.nmFantasia"
					disabled="true"
					size="30" maxLength="25"
			/>
		</adsm:textbox>

		<adsm:textarea
				maxLength="255"
				property="observacao"
				label="observacao"
				columns="125" rows="2" width="80%"/>

		<adsm:buttonBar>
			<adsm:button
					id="updateScreen"
					caption="atualizar"
					onclick="clearScrean();"
					boxWidth="100"/>
			<adsm:button
					id="emitirControleCargaButton"
					caption="emitirControleCargas"
					onclick="exibeEmitirControleCargas();"
					boxWidth="180"/>
			<adsm:button
					id="emitirManifestoButton"
					caption="emitirManifesto2"
					service="lms.expedicao.emitirManifestoViagemAction.emitirManifestoViagemNacional"
					callbackProperty="emitirManifestoViagemNacional"
					boxWidth="150"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script type="text/javascript">
    var _reportData;
    function executeReports() {
		/* Verifica se existe Boletos Bancários para emitir */
        if (_reportData.blExisteBoleto == 'true') {
            emitirBoleto();
        }

		/* Emite os recibos retornados */
        var dadosRelatorio = getNestedBeanPropertyValue(_reportData, "dadosRelatorio");
        for(var i=0; i<dadosRelatorio.length; i++) {
            openReport(dadosRelatorio[i], "RELATORIO_FRETE_CARRETEIRO"+i, false);
        }
        _reportData = null;
    }

    function findComboPreManifestoInit() {
        var sdo = createServiceDataObject("lms.expedicao.emitirManifestoViagemAction.findComboPreManifestoInit", "findComboPreManifestoInit");
        xmit({serviceDataObjects:[sdo]});
    }
    function findComboPreManifestoInit_cb(data, error) {
        manifesto_idManifesto_cb(data);
        defaultParameters();
    }

    function defaultParameters() {
        var sdo = createServiceDataObject("lms.expedicao.emitirManifestoViagemAction.findDefaultParameters", "defaultParameters");
        xmit({serviceDataObjects:[sdo]});
    }
    function defaultParameters_cb(data, error) {
        if(error!=undefined) {
            alert(error);
            return;
        }
		/* Filial Usuario */
        setElementValue("idFilial", data.idFilial);
        setElementValue("sgFilial", data.sgFilial);
        setElementValue("nmFilial", data.nmFilial);
        setDisabled("updateScreen", false);
        setDisabled("emitirManifestoButton", false);
        setDisabled("emitirManifestoButton", false);
		/* Chama execução dos relatorios */
        if(_reportData != null) {
            executeReports();
        }
    }

    function clearFields() {
        resetValue("tpModal.description");
        resetValue("filialByIdFilialDestino.idFilial");
        resetValue("filialByIdFilialDestino.sgFilial");
        resetValue("filialByIdFilialDestino.pessoa.nmFantasia");
        resetValue("observacao");
    }

    function exibeEmitirControleCargas() {
        parent.parent.redirectPage("carregamento/emitirControleCargas.do?cmd=main");
    }

    function clearScrean() {
		/* Limpa os Campos e refaz a consulta da Combo */
        clearFields();
        findComboPreManifestoInit();
    }

    function emitirManifestoViagemNacional_cb(data, error) {
        store_cb(data, error);
        if (error != undefined) {
            return;
        }

		/* Imprime o Manifesto */
        var mvn = getNestedBeanPropertyValue(data, "mvn");
        if( mvn != null){
            emitirMVN(data.mvn);
        } else {
            openReport(data.reportFileMVNParaMDFe, null, true);
        }

        _reportData = data;

		/*
		 * TODO: Implementar Rotina de:
		 * -> Geracao SINTEGRA.
		 */
        clearScrean();
    }

    function emitirRecibosReembolso() {
        if(window.confirm(i18NLabel.getLabel("LMS-04176"))) {
            var idManifesto = _reportData.manifesto.idManifesto;
            var sdo = createServiceDataObject("lms.entrega.emitirManifestoAction.execute", "emitirRecibosReembolso", {whichReport:'emissaoReciboViagem', idManisfestoViagemNacional:idManifesto});
            xmit({serviceDataObjects:[sdo]});
        }
    }
    function emitirRecibosReembolso_cb(data, error) {
        printPdf_cb(data, error);
    }

    function emitirBoleto() {
        var params = {};
        params.idManifesto = _reportData.manifesto.idManifesto;
        params.tpManifesto = "V";
        params.blReemissaoManifesto = "N";

        var sdo = createServiceDataObject("lms.contasreceber.emitirBoletosManifestoAction.execute", "openPdf", params);
        executeReportWindowed(sdo, "pdf");
    }

	/*
	 * Envia Docto para Impressora
	 */
    function emitirMVN(data) {
        if((data == undefined) || (data == "")) {
            alertI18nMessage("LMS-04100");
            return;
        }

        var printer = window.top[0].document.getElementById("printer");
        if(printer){
            printer.print(data);
            alertI18nMessage("LMS-04101", "1", false);
        }
    }
</script>