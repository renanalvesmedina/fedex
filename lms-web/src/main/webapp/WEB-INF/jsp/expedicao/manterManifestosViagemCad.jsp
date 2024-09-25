<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.manterManifestosViagemAction">
    <adsm:i18nLabels>
        <adsm:include key="LMS-04100"/>
        <adsm:include key="LMS-04101"/>
        <adsm:include key="LMS-04171"/>
        <adsm:include key="LMS-04176"/>
        <adsm:include key="LMS-09077"/>
    </adsm:i18nLabels>

    <adsm:form id="form1" action="/expedicao/manterManifestosViagem" idProperty="idManifestoViagemNacional"
               onDataLoadCallBack="findDadosSessao">

        <adsm:hidden property="filialPadrao.idFilial"/>
        <adsm:hidden property="manifesto.filialByIdFilialOrigem.idFilial"/>
        <adsm:hidden property="manifesto.tpStatusManifesto"/>
        <adsm:hidden property="manifesto.controleCarga.idControleCarga"/>
        <adsm:hidden property="manifesto.controleCarga.filialByIdFilialOrigem.idFilial"/>
        <adsm:hidden property="manifesto.controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia"/>

        <adsm:textbox property="manifesto.filialByIdFilialOrigem.sgFilial" label="numeroManifesto2" dataType="text"
                      maxLength="3" size="4" disabled="true" labelWidth="20%" width="35%">
            <adsm:textbox
                    property="nrManifestoOrigem"
                    dataType="integer"
                    mask="00000000"
                    size="8"
                    maxLength="8"
                    disabled="true"/>
        </adsm:textbox>

        <adsm:complement label="filialDestino" labelWidth="15%" width="25%">
            <adsm:textbox dataType="text" size="3" maxLength="3"
                          property="manifesto.filialByIdFilialDestino.sgFilial"
                          serializable="false"
                          disabled="true"/>
            <adsm:textbox dataType="text" size="25"
                          property="manifesto.filialByIdFilialDestino.pessoa.nmFantasia"
                          serializable="false"
                          disabled="true"/>
        </adsm:complement>

        <adsm:textbox property="manifesto.dhEmissaoManifesto" label="dataHoraEmissao" dataType="JTDateTimeZone"
                      disabled="true" picker="false" labelWidth="20%" width="35%"/>

        <adsm:textbox property="manifesto.tpStatusManifesto.description" label="situacao" serializable="false"
                      dataType="text" size="34" disabled="true" labelWidth="15%" width="25%"/>
        <%--<adsm:combobox property="manifesto.tpStatusManifesto" label="situacao" domain="DM_STATUS_MANIFESTO" disabled="true" labelWidth="20%" width="23%" />--%>

        <adsm:textbox property="manifesto.controleCarga.meioTransporteByIdTransportado.nrIdentificador"
                      label="meioTransporte" dataType="text" disabled="true" size="6" maxLength="25" labelWidth="20%"
                      width="35%">
            <adsm:textbox property="manifesto.controleCarga.meioTransporteByIdTransportado.nrFrota" dataType="text"
                          size="5" maxLength="6" disabled="true"/>
        </adsm:textbox>

        <adsm:textbox property="manifesto.controleCarga.meioTransporteByIdSemiRebocado.nrIdentificador"
                      label="semiReboque" dataType="text" disabled="true" size="6" maxLength="25" labelWidth="15%"
                      width="20%">
            <adsm:textbox property="manifesto.controleCarga.meioTransporteByIdSemiRebocado.nrFrota" dataType="text"
                          size="5" maxLength="6" disabled="true"/>
        </adsm:textbox>

        <adsm:textbox dataType="text" property="manifesto.controleCarga.proprietario.pessoa.nmPessoa"
                      label="proprietario" serializable="false" size="40" labelWidth="20%" width="35%" disabled="true"/>
        <%--
                <adsm:lookup property="manifesto.controleCarga.proprietario"
                    action="/contratacaoMeioTransporte/manterProprietario"
                    service="lms.vendas.emitirRelClientesDescontoTotalFreteAction.findLookupProprietario"
                    idProperty="idProprietario"
                    criteriaProperty="pessoa.nrIdentificacao"
                    relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
                    label="proprietario"
                    size="10" maxLength="10" dataType="integer"
                    disabled="true" labelWidth="20%" width="70%" >
                    <adsm:propertyMapping relatedProperty="manifesto.controleCarga.proprietario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
                    <adsm:textbox dataType="text" property="manifesto.controleCarga.proprietario.pessoa.nmPessoa" serializable="false" size="30" disabled="true"/>
                </adsm:lookup>
        --%>

        <adsm:textbox dataType="text" property="manifesto.controleCarga.motorista.pessoa.nmPessoa" label="motorista"
                      size="40" labelWidth="15%" width="25%" disabled="true"/>
        <%--
                <adsm:lookup property="manifesto.controleCarga.motorista"
                    action="/contratacaoVeiculos/manterMotoristas"
                    service="lms.portaria.manterOrdensSaidaAction.findMotorista"
                    idProperty="idMotorista"
                    criteriaProperty="pessoa.nrIdentificacao"
                    relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
                    label="motorista" dataType="text" disabled="true"
                    size="10" maxLength="10" width="80%" labelWidth="17%">
                       <adsm:propertyMapping relatedProperty="manifesto.controleCarga.motorista.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
                       <adsm:textbox dataType="text" property="manifesto.controleCarga.motorista.pessoa.nmPessoa" size="30" disabled="true"/>
                </adsm:lookup>
        --%>

        <adsm:textbox property="manifesto.controleCarga.filialByIdFilialOrigem.sgFilial" label="numeroControleCargas"
                      dataType="text" maxLength="3" size="4" disabled="true" labelWidth="20%" width="35%">
            <adsm:textbox
                    property="manifesto.controleCarga.nrControleCarga"
                    dataType="integer"
                    mask="00000000"
                    size="8"
                    maxLength="8"
                    disabled="true"/>
        </adsm:textbox>


        <adsm:textbox dataType="integer" label="qtdeCTRCs" property="qtConhecimentos" disabled="true" size="6"
                      labelWidth="15%" width="25%"/>

        <adsm:label key="espacoBranco" width="100%" style="border:none"/>
        <adsm:section caption="totais"/>

        <adsm:buttonBar>
            <adsm:button boxWidth="170" caption="controleCargas" id="controleCargas"
                         action="carregamento/consultarControleCargas" cmd="main">
                <adsm:linkProperty src="manifesto.controleCarga.idControleCarga" target="idControleCarga"
                                   disabled="true"/>
                <adsm:linkProperty src="manifesto.controleCarga.nrControleCarga" target="nrControleCarga"
                                   disabled="true"/>
                <adsm:linkProperty src="manifesto.controleCarga.filialByIdFilialOrigem.idFilial"
                                   target="filialByIdFilialOrigem.idFilial" disabled="true"/>
                <adsm:linkProperty src="manifesto.controleCarga.filialByIdFilialOrigem.sgFilial"
                                   target="filialByIdFilialOrigem.sgFilial" disabled="true"/>
                <adsm:linkProperty src="manifesto.controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia"
                                   target="filialByIdFilialOrigem.pessoa.nmFantasia" disabled="true"/>
            </adsm:button>
            <adsm:button
                    boxWidth="107"
                    caption="reemitirBoletos"
                    id="reemitirBoletosButton"
                    onclick="reemitirBoleto();"/>
            <adsm:button
                    boxWidth="184"
                    caption="reemitirReciboReembolso"
                    id="existeRecibosButton"
                    service="lms.expedicao.manterManifestosViagemAction.existeRecibosReembolso"
                    callbackProperty="existeRecibosReembolso"/>
            <adsm:button
                    boxWidth="115"
                    caption="reemitirManifesto"
                    id="reemitirManifestoButton"
                    service="lms.expedicao.emitirManifestoViagemAction.reemitirManifestoViagemNacional"
                    callbackProperty="reemitirManifestoViagemNacional"
            />
            <adsm:button
                    boxWidth="115"
                    caption="cancelarManifesto2"
                    id="cancelarManifestoButton"
                    onclick="calcelarMAV();"/>
        </adsm:buttonBar>
    </adsm:form>

    <adsm:grid idProperty="idManifestoViagemNacional" property="gridDetalhamento" selectionMode="none"
               service="lms.expedicao.manterManifestosViagemAction.findPaginatedCadManifestoViagem"
               rows="3" gridHeight="200" showPagging="false" showRowIndex="false" showTotalPageCount="false"
               onRowClick="noAcao" onDataLoadCallBack="populaQtConhecimentos">
        <adsm:gridColumn title="totais" property="totais" width="12%"/>
        <adsm:gridColumn title="qtdeCTRCs" property="qtConhecimentos" width="14%" align="right"/>
        <adsm:gridColumn title="qtdeVolumes" property="qtVolumes" width="14%" align="right"/>
        <adsm:gridColumn title="peso" unit="kg" property="psReal" dataType="decimal" mask="###,###,###,###,##0.000"
                         width="15%" align="right"/>
        <adsm:gridColumn title="pesoAforado" unit="kg" property="psAforado" dataType="decimal"
                         mask="###,###,###,###,##0.000" width="15%" align="right"/>
        <adsm:gridColumn title="valorFrete" unit="reais" property="vlLiquido" dataType="currency" width="15%"
                         align="right"/>
        <adsm:gridColumn title="valorMerc" unit="reais" property="vlMercadoria" dataType="currency" width="15%"
                         align="right"/>
    </adsm:grid>

</adsm:window>

<script language="javascript" type="text/javascript" src="../lib/expedicao.js"></script>
<script type="text/javascript">
    function initWindow(event) {
        if (event.name == "tab_click") {
            habilitaBotoes();
        }
    }
    function findDadosSessao_cb(data, erro) {
        onDataLoad_cb(data, erro);

        var sdo = createServiceDataObject("lms.expedicao.manterManifestosViagemAction.findDadosSessao", "ajustaDadosSessao");
        xmit({serviceDataObjects: [sdo]});
    }
    function ajustaDadosSessao_cb(data, errorMsg, errorKey) {
        if (errorMsg) {
            alert(errorMsg);
            return;
        }
        setElementValue("filialPadrao.idFilial", data.filial.idFilial);
        populaGrid();
    }

    function populaGrid() {
        habilitaBotoes();
        findButtonScript("gridDetalhamento", getElement("form1"));
    }

    function habilitaBotoes() {
        setDisabled("reemitirBoletosButton", true);
        setDisabled("existeRecibosButton", true);
        setDisabled("reemitirManifestoButton", true);
        setDisabled("cancelarManifestoButton", true);

        if (getElementValue("manifesto.tpStatusManifesto") == "ME") {
            if (getElementValue("filialPadrao.idFilial") == getElementValue("manifesto.filialByIdFilialOrigem.idFilial")) {
                setDisabled("reemitirBoletosButton", false);
                setDisabled("existeRecibosButton", false);
                setDisabled("reemitirManifestoButton", false);
                setDisabled("cancelarManifestoButton", false);
            }
        }
    }

    function populaQtConhecimentos_cb(data, erro) {
        setElementValue("qtConhecimentos", data[data.length - 1].qtConhecimentos);
    }

    function noAcao() {
        return false;
    }

    function reemitirManifestoViagemNacional_cb(data, error) {
        store_cb(data, error);
        if (error != undefined) {
            return;
        }

        /* Imprime o Manifesto */
        var mvn = getNestedBeanPropertyValue(data, "mvn");
        if (mvn != null) {
            reemitirMVN(data.mvn);
        } else {
            openReport(data.reportFileMVNParaMDFe, null, true);
        }
    }

    /* Verifica se Existe Conhecimentos para Reembolsar
     * que ainda nao possuam Doctos de Reembolso. */
    function existeRecibosReembolso_cb(data, error) {
        store_cb(data, error);
        if (error != undefined) {
            return;
        }
        /* Verifica se existe Recibo de Reembolso para emitir */
        if (data.blExisteReembolso == 'true') {
            reemitirRecibosReembolso();
        } else {
            alertI18nMessage("LMS-09077");
        }
    }

    /*
     * Envia Recibos Reembolso direto para a impressora do usuario.
     */
    function reemitirRecibosReembolso() {
        if (window.confirm(i18NLabel.getLabel("LMS-04176"))) {
            var idManisfesto = getElementValue("idManifestoViagemNacional");
            var sdo = createServiceDataObject("lms.entrega.emitirManifestoAction.execute", "reemitirRecibosReembolso", {
                whichReport: 'reemissaoReciboViagem',
                idManisfestoViagemNacional: idManisfesto
            });
            xmit({serviceDataObjects: [sdo]});
        }
    }
    function reemitirRecibosReembolso_cb(data, error) {
        printPdf_cb(data, error);
    }

    function reemitirBoleto() {
        var params = {};
        params.idManifesto = getElementValue("idManifestoViagemNacional");
        params.tpManifesto = "V";
        params.blReemissaoManifesto = "S";

        var sdo = createServiceDataObject("lms.contasreceber.emitirBoletosManifestoAction.execute", "reemitirBoleto", params);
        executeReportWindowed(sdo, "pdf");
    }

    /*
     * Envia Manifesto para Impressora
     */
    function reemitirMVN(data) {
        if ((data == undefined) || (data == "")) {
            alertI18nMessage("LMS-04100");
            return;
        }
        var printer = window.top[0].document.getElementById("printer");
        if (printer) {
            printer.print(data);
            alertI18nMessage("LMS-04101", "1", false);
        }
    }

    function calcelarMAV() {
        var returnValue = showModalDialog('expedicao/manterManifestosViagemCancelarManifesto.do?cmd=main&idManifestoViagemNacional=' + getElementValue("idManifestoViagemNacional"), window, 'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:300px;dialogHeight:170px;');
        if (returnValue != undefined) {
            alertI18nMessage("LMS-04171");
            /* Reaproveitamento de Dados */
            if (returnValue == 'false') {
                setDisabled("reemitirBoletosButton", true);
                setDisabled("existeRecibosButton", true);
                setDisabled("reemitirManifestoButton", true);
                setDisabled("cancelarManifestoButton", true);
            } else {
                getTabGroup(this.document).selectTab(0);
            }
        }
    }
</script>