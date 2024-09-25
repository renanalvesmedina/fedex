<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window onPageLoadCallBack="myOnPageLoad">

    <adsm:form action="/contasReceber/carregarArquivoRecebidoPreFaturaPadrao">
        <adsm:hidden property="tpSituacao" value="A"/>
        <adsm:lookup label="cliente"
                     service="lms.contasreceber.carregarArquivoRecebidoPreFaturaAction.findLookupCliente"
                     dataType="text"
                     property="cliente"
                     idProperty="idCliente"
                     labelWidth="15%"
                     criteriaProperty="pessoa.nrIdentificacao"
                     relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
                     onPopupSetValue="popupCliente"
                     exactMatch="true"
                     size="20"
                     maxLength="20"
                     width="100%"
                     action="/vendas/manterDadosIdentificacao"
                     required="true"
                     onchange="return onChangeCliente()">
            <adsm:propertyMapping modelProperty="pessoa.nmPessoa" formProperty="cliente.pessoa.nmPessoa"/>
            <adsm:propertyMapping modelProperty="pessoa.nrIdentificacao" formProperty="nrIdentificacao"/>
            <adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao"/>
            <adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" disabled="true" size="61"/>
        </adsm:lookup>
        <adsm:hidden property="nrIdentificacao" serializable="true"/>

        <adsm:combobox property="divisaoCliente.idDivisaoCliente" label="divisao"
                       service="lms.contasreceber.carregarArquivoRecebidoPreFaturaAction.findComboDivisaoCliente"
                       optionLabelProperty="dsDivisaoCliente"
                       labelWidth="15%"
                       width="35%"
                       required="true"
                       boxWidth="180"
                       optionProperty="idDivisaoCliente" autoLoad="false"
                       onchange="return onChangeDivisao(this)">
        </adsm:combobox>

        <adsm:combobox
                service="lms.contasreceber.carregarArquivoRecebidoPreFaturaAction.findComboCedentes"
                optionLabelProperty="comboText"
                optionProperty="idCedente"
                property="cedente.idCedente"
                label="banco"
                boxWidth="180"
                labelWidth="15%"
                width="35%"
                required="true">
        </adsm:combobox>

        <adsm:hidden property="dataAtual"/>

        <adsm:textbox labelWidth="15%" width="35%" dataType="JTDate" label="dataEmissao" property="dtEmissao" size="8"
                      maxLength="20" required="true" onchange="calculaVencimento();"/>

        <adsm:textbox labelWidth="15%" dataType="JTDate" label="dataVencimento" property="dtVencimento" size="8"
                      maxLength="20" required="true"/>

        <adsm:checkbox property="blGerarBoleto" label="gerarBoleto" labelWidth="15%"/>

        <adsm:textbox labelWidth="15%" label="arquivo" property="arquivo" dataType="file" width="85%" size="72"
                      required="true"/>

        <adsm:combobox property="tpModal" label="modal" domain="DM_MODAL" required="true" labelWidth="15%" width="35%"/>

        <adsm:combobox property="tpAbrangencia" label="abrangencia" domain="DM_ABRANGENCIA" labelWidth="15%"
                       width="35%" required="true" defaultValue="N"/>

        <adsm:buttonBar>

            <adsm:button caption="relatorioOcorrencias" id="btnOcorrencia"
                         action="/contasReceber/emitirOcorrenciasPreFaturasImportadas.do" cmd="main" disabled="false">
                <adsm:linkProperty src="cliente.idCliente" target="cliente.idCliente"/>
                <adsm:linkProperty src="cliente.pessoa.nrIdentificacao" target="cliente.pessoa.nrIdentificacao"/>
                <adsm:linkProperty src="cliente.pessoa.nmPessoa" target="cliente.pessoa.nmPessoa"/>
                <adsm:linkProperty src="dataAtual" target="periodoInicial"/>
                <adsm:linkProperty src="dataAtual" target="periodoFinal"/>
            </adsm:button>

            <adsm:button
                    caption="importarArquivo"
                    id="btnImportar"
                    disabled="false"
                    onclick="storeButtonScript('lms.contasreceber.carregarArquivoRecebidoPreFaturaPadraoAction.executeImportacao', 'retornoImportacao', this.form);"
            />
            <adsm:button id="btnLimpar" caption="limpar" onclick="cleanButtonScript(this.document);ajustaFoco();"
                         disabled="false"/>

        </adsm:buttonBar>

    </adsm:form>

</adsm:window>

<script>
    function calculaVencimento() {

        var dtEmissao = getElement("dtEmissao");

        if (dtEmissao.value != "" && isDate(dtEmissao.value, dtEmissao.mask) &&
            getElementValue("divisaoCliente.idDivisaoCliente") != "") {

            _serviceDataObjects = new Array();

            addServiceDataObject(
                createServiceDataObject("lms.contasreceber.carregarArquivoRecebidoPreFaturaAction.findDataVencimento",
                    "retornoCalculaVencimento",
                    {
                        idDivisaoCliente: getElementValue("divisaoCliente.idDivisaoCliente"),
                        dtEmissao: getElementValue("dtEmissao")
                    }
                )
            );

            xmit(false);
        }
    }

    function retornoCalculaVencimento_cb(data, error) {
        if (error != undefined) {
            alert(error);
            setFocusOnFirstFocusableField(document);
            resetValue('dtVencimento');
            return false;
        }
        document.getElementById("dtVencimento").value = data._value;
    }

    function retornoImportacao_cb(data, error, c) {
        if (error != null) {
            alert(error);
            if (c == "LMS-36099") {
                var dtEmissao = getElement("dtEmissao");
                dtEmissao.focus();
                dtEmissao.select();
            }
        } else if (data.erroPreFatura != null) {
            alert(data.erroPreFatura);
        } else {
            if (data.idFatura != null) {
                addServiceDataObject(
                    createServiceDataObject("lms.contasreceber.carregarArquivoRecebidoPreFaturaPadraoAction.printReport",
                        "retornoImprecao", {
                            idFatura: data.idFatura
                        }
                    )
                );
                xmit(false);
            } else {
                alert(data.ex);

            }
        }
    }

    function retornoImprecao_cb(data, error, c) {
        if (error != null) {
            alert(error);
        } else if (data.erroImpressao != null) {
            alert(data.erroImpressao);
        } else {
            if (data.ex == "") {
                showSuccessMessage();
                openReportWithLocator(data, error);
            } else {
                alert(data.ex);
            }
        }
    }

    function myOnPageLoad_cb(data, erro) {
        onPageLoad_cb(data, erro);
        setDisabled('btnImportar', false);
        setDisabled('btnLimpar', false);
        resetFileElementValue(document.getElementById('arquivo'));
    }

    function setDataAtual_cb(data, error) {
        setElementValue('dataAtual', setFormat("dtEmissao", data._value));
        setElementValue('dtEmissao', setFormat("dtEmissao", data._value));
    }

    document.getElementById("cliente.idCliente").callBack = "clienteExactMatch";

    function popupCliente(data) {
        if (data == undefined) {
            return;
        }
        mountDivisaoCliente(data.idCliente);
        findCedenteByCliente(data.idCliente);
        setFocusOnFirstFocusableField(document);
    }

    function mountDivisaoCliente(varIdCliente) {
        _serviceDataObjects = new Array();
        addServiceDataObject(createServiceDataObject("lms.contasreceber.carregarArquivoRecebidoPreFaturaAction.findComboDivisaoCliente", "myOnDataLoadDivisao", {cliente: {idCliente: varIdCliente}}));
        xmit(false);
    }

    function verificaDivisaoClientePossuiAgrupamento() {
        _serviceDataObjects = new Array();
        addServiceDataObject(createServiceDataObject("lms.contasreceber.carregarArquivoRecebidoPreFaturaAction.verifDivisaoClientePossuiAgrup",
            "verifDivisaoClientePossuiAgrup",
            {idDivisaoCliente: getElementValue("divisaoCliente.idDivisaoCliente")}));
        xmit(false);
    }

    function verifDivisaoClientePossuiAgrup_cb(data) {
        if (data._value == "true") {
            setDisabled("blGerarBoleto", true);
            setElementValue("blGerarBoleto", false);
        } else {
            setDisabled("blGerarBoleto", false);
        }
    }

    function onChangeCliente() {
        var retorno = cliente_pessoa_nrIdentificacaoOnChangeHandler();
        resetDivisao();
        verificaDivisaoClientePossuiAgrupamento();
        return retorno;
    }

    function resetDivisao() {
        var cliente = getElement("cliente.pessoa.nrIdentificacao");
        if (getElementValue("cliente.pessoa.nrIdentificacao") == "" || isElementChanged(cliente)) {
            document.getElementById("divisaoCliente.idDivisaoCliente").options.length = 1;
            document.getElementById("divisaoCliente.idDivisaoCliente").selectedIndex = 0;
            resetValue('dtVencimento');
        }
    }

    function clienteExactMatch_cb(data) {
        var retorno = lookupExactMatch({e: document.getElementById("cliente.idCliente"), data: data});

        if (data != undefined && data.length > 0) {
            if (data.length == 1) {

                mountDivisaoCliente(data[0].idCliente);
                findCedenteByCliente(data[0].idCliente);
            }
        }

        return retorno;
    }

    function myOnDataLoadDivisao_cb(data) {
        var retorno = divisaoCliente_idDivisaoCliente_cb(data);
        setDivisao();
        return retorno;
    }

    function setDivisao() {
        if (document.getElementById("divisaoCliente.idDivisaoCliente").options.length == 2) {
            setElementValue("divisaoCliente.idDivisaoCliente", document.getElementById("divisaoCliente.idDivisaoCliente").options[1].value);
            calculaVencimento();
            verificaDivisaoClientePossuiAgrupamento();
        }
    }

    function findCedenteByCliente(idCliente) {
        _serviceDataObjects = new Array();
        addServiceDataObject(createServiceDataObject("lms.contasreceber.carregarArquivoRecebidoPreFaturaAction.findCedenteByCliente",
            "retornoCedenteByCliente",
            {cliente: {idCliente: idCliente}}));
        xmit(false);
    }

    function retornoCedenteByCliente_cb(data, error) {

        if (error != undefined) {
            alert(error);
            setFocusOnFirstFocusableField(document);
            return false;
        }

        if (data != undefined) {
            fillFormWithFormBeanData(0, data);
        }
    }

    function initWindow(event) {
        if (event.name == "tab_load" || event.name == "cleanButton_click") {
            if (event.name == "cleanButton_click") {
                resetDivisao();
            }
            setElementValue("blGerarBoleto", true);
            _serviceDataObjects = new Array();
            addServiceDataObject(createServiceDataObject("lms.contasreceber.carregarArquivoRecebidoPreFaturaAction.findDataAtual", "setDataAtual", new Array()));
            xmit(false);
            setDisabled('btnOcorrencia', false);
        }
    }

    function onChangeDivisao(elem) {
        var retorno = comboboxChange({e: elem});
        resetValue('dtVencimento');
        calculaVencimento();
        verificaDivisaoClientePossuiAgrupamento();
        return retorno;
    }

    function ajustaFoco() {
        setFocusOnFirstFocusableField(document);
        setDisabled("btnImportar", false);
        setDisabled("btnLimpar", false);
    }

    /**
     * Valida os campos para somente no caso de o arquivo não estiver setado
     * para enviar o foco para o botão de seleção do arquivo
     */
    function validateTab() {
        // campos obrigatórios exceto o arquivo
        var idCliente = getElementValue("cliente.idCliente");
        var emissao = getElementValue("dtEmissao");
        var vencimento = getElementValue("dtVencimento");
        var divisao = document.getElementById("divisaoCliente.idDivisaoCliente").selectedIndex;
        var banco = document.getElementById("cedente.idCedente").selectedIndex;

        if (!validateTabScript(document.forms)) {
            if (idCliente != "" && emissao != "" && vencimento != "" && divisao != 0 && banco != 0) {
                setFocus("arquivo_browse", false);
            }
            return false;
        }
        return true;
    }
</script>