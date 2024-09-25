<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
    function loadPageObjects() {
        document.getElementById("causaNaoConformidade").readonly = true;
        document.getElementById("acoesCorretivas").readOnly = true;
        document.getElementById("ocorrenciaNaoConformidade.dsOcorrenciaNc").readOnly = true;
        document.getElementById("ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial").required = "true";

        setDisabled("btnLimpar", false);
        setDisabled("btnStore", false);

        var url = new URL(parent.location.href);
        var idOcorrenciaNaoConformidadeLocMerc = url.parameters["idOcorrenciaNaoConformidadeLocMerc"];
        if (idOcorrenciaNaoConformidadeLocMerc != undefined && idOcorrenciaNaoConformidadeLocMerc != "") {
            setDisabled("btnStore", true);
            setDisabled("btnLimpar", true);
        }

        var objSemLabel = document.getElementById('idsOcorrenciaNaoConformidade');
        var objComLabel = document.getElementById('spanlbl_idsOcorrenciaNaoConformidade');
        objSemLabel.label = objComLabel.innerText;

        var data = new Array();
        var sdo = createServiceDataObject("lms.rnc.registrarDisposicaoAction.getDataUsuario", "loadDataUsuario", data);
        xmit({serviceDataObjects: [sdo]});
    }

    /**
     * Carrega um array 'dataUsuario' com os dados do usuario em sessao
     */
    var dataUsuario;
    function loadDataUsuario_cb(data, error) {
        onPageLoad();
        dataUsuario = data;
        fillDataUsuario();
    }
</script>
<adsm:window service="lms.rnc.registrarDisposicaoAction" onPageLoad="loadPageObjects">
    <adsm:form action="/rnc/registrarDisposicao" idProperty="idDisposicao" id="formDisposicao"
               onDataLoadCallBack="onDataLoadDisp">

        <adsm:hidden property="idOcorrenciaNaoConformidadeLocMerc" serializable="false"/>

        <adsm:lookup dataType="text" property="ocorrenciaNaoConformidade.naoConformidade.filial"
                     idProperty="idFilial" criteriaProperty="sgFilial"
                     service="lms.rnc.registrarDisposicaoAction.findLookupBySgFilial"
                     action="/municipios/manterFiliais"
                     onchange="return sgFilialOnChangeHandler();" onDataLoadCallBack="disableNrNaoConformidade"
                     label="naoConformidade" size="3" maxLength="3" labelWidth="20%" width="30%"
                     picker="false" popupLabel="pesquisarFilial">
            <adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado"
                                  modelProperty="flagBuscaEmpresaUsuarioLogado"/>
            <adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado"
                                  modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>

            <adsm:lookup dataType="integer" property="ocorrenciaNaoConformidade.naoConformidade"
                         idProperty="idNaoConformidade" criteriaProperty="nrNaoConformidade"
                         action="/rnc/manterNaoConformidade"
                         service="lms.rnc.registrarDisposicaoAction.findLookupNaoConformidade"
                         onchange="return nrNaoConformidadeChangeHandler()"
                         onDataLoadCallBack="loadNrNaoConformidade" onPopupSetValue="enableNrNaoConformidade"
                         disabled="true" required="true"
                         exactMatch="false" size="15" maxLength="8" mask="00000000"
                         popupLabel="pesquisarNaoConformidade">
                <adsm:propertyMapping modelProperty="filial.sgFilial"
                                      relatedProperty="ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial"
                                      blankFill="false"/>
                <adsm:propertyMapping modelProperty="filial.sgFilial"
                                      criteriaProperty="ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial"/>
                <adsm:propertyMapping modelProperty="filial.idFilial"
                                      criteriaProperty="ocorrenciaNaoConformidade.naoConformidade.filial.idFilial"/>
            </adsm:lookup>
        </adsm:lookup>

        <adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="false"/>
        <adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>

        <adsm:textbox dataType="JTDateTimeZone" property="dhDisposicao"
                      label="dataHoraDisposicao" labelWidth="17%" width="33%" disabled="true" picker="false"/>

        <adsm:hidden property="ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade"/>
        <adsm:listbox property="idsOcorrenciaNaoConformidade" optionProperty="idOcorrenciaNC"
                      optionLabelProperty="nrOcorrenciaNc"
                      onContentChange="loadFieldsFromOcorrencia" onchange="onChangeListNumOcorrencia();"
                      label="numeroOcorrencia" labelWidth="20%" width="80%" size="5" boxWidth="400" required="true">
            <adsm:combobox property="ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade"
                           optionProperty="idOcorrenciaNaoConformidade" optionLabelProperty="nrOcorrenciaNc"
                           service="lms.rnc.registrarDisposicaoAction.findOcorrenciaNaoConformidade"
                           onchange="comboBoxOcorrenciaNCChangeHandler({e:this});"
                           onDataLoadCallBack="loadComboOcorrenciasNC">
                <adsm:propertyMapping modelProperty="naoConformidade.idNaoConformidade"
                                      criteriaProperty="ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade"/>
                <adsm:propertyMapping modelProperty="dsOcorrenciaNc"
                                      relatedProperty="ocorrenciaNaoConformidade.dsOcorrenciaNc"/>
            </adsm:combobox>
        </adsm:listbox>

        <adsm:combobox property="motivoDisposicao.idMotivoDisposicao" optionLabelProperty="dsMotivo"
                       optionProperty="idMotivoDisposicao"
                       service="lms.rnc.registrarDisposicaoAction.findOrderByDsMotivoDisposicao" boxWidth="201"
                       label="motivoDisposicao" labelWidth="20%" width="30%" required="true" autoLoad="false"/>

        <adsm:hidden property="usuario.idUsuario"/>
        <adsm:textbox dataType="text" property="usuario.nmUsuario"
                      label="usuarioResponsavel" size="23" labelWidth="17%" width="33%" disabled="true"/>

        <adsm:textarea property="dsDisposicao" labelWidth="20%" width="80%" label="descricaoDisposicao" columns="108"
                       rows="4" maxLength="500" required="true"/>

        <adsm:listbox property="causaNaoConformidade" optionProperty="idCausaNaoConformidade"
                      optionLabelProperty="dsCausaNaoConformidade"
                      label="causasNaoConformidade" labelWidth="20%" width="30%" size="5" boxWidth="201"
                      serializable="false"/>

        <adsm:listbox property="acoesCorretivas" optionProperty="idAcaoCorretiva" optionLabelProperty="dsAcaoCorretiva"
                      label="acoesCorretivas" labelWidth="17%" width="33%" size="5" boxWidth="201"
                      serializable="false"/>

        <adsm:textarea property="ocorrenciaNaoConformidade.dsOcorrenciaNc"
                       label="descricaoOcorrencia" columns="108" rows="5" maxLength="255" labelWidth="20%" width="80%"
                       cellStyle="vertical-align:bottom"/>

        <adsm:buttonBar>
            <adsm:button caption="salvar" id="btnStore" buttonType="store" onclick="return validateView();"/>
            <adsm:button caption="limpar" id="btnLimpar" onclick="return clearScreen();"/>
        </adsm:buttonBar>
    </adsm:form>
</adsm:window>

<script>

    function onDataLoadDisp_cb(data, exception) {
        onDataLoad_cb(data, exception);

        if (getElementValue("idOcorrenciaNaoConformidadeLocMerc") != undefined && getElementValue("idOcorrenciaNaoConformidadeLocMerc") != "") {
            setDisabled("btnStore", true);
            setDisabled("btnLimpar", true);
        }
    }


    function initWindow(eventObj) {
        setDisabled("btnLimpar", false);

        document.getElementById("ocorrenciaNaoConformidade.naoConformidade.filial.idFilial").required = true;
        disableNrNaoConformidade(true);

        if (eventObj.name == "storeButton") {
            disableObjects(true);
            setFocusOnButton("btnLimpar");
        } else {
            loadDataFromURL();
        }
    }

    //#####################################################
    // Inicio da validacao do pce
    //#####################################################

    /**
     * Faz a validacao da tela
     */
    function validateView(form) {
        if ((validateTabScript(document.getElementById("formDisposicao"))) == false)
            return false;

        var data = new Object();
        data.idNaoConformidade = getElementValue("ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade");
        var sdo = createServiceDataObject("lms.rnc.registrarDisposicaoAction.validatePCE", "validatePCE", data);
        xmit({serviceDataObjects: [sdo]});
    }

    /**
     * Callback da chamada de validacao do PCE, chama a popUp de alert com os dados do
     * PCE caso necessario.
     *
     * @param data
     * @param error
     */
    function validatePCE_cb(data, error) {
        if (data._exception == undefined) {
            if (data.destinatario != undefined) {
                if (data.destinatario.codigo != undefined) {
                    showModalDialog('vendas/alertaPce.do?idVersaoDescritivoPce=' + data.destinatario.codigo + '&cmd=pop', window, 'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:310px;');
                }
            }

            if (data.remetente != undefined) {
                if (data.remetente.codigo != undefined) {
                    showModalDialog('vendas/alertaPce.do?idVersaoDescritivoPce=' + data.remetente.codigo + '&cmd=pop', window, 'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:310px;');
                }
            }
        } else {
            alert(error);
        }
        storeDisposicao();
    }

    /**
     * Este callback existe decorrente de uma necessidade da popUp de alert.
     */
    function alertPCE_cb() {
        //Empty...
    }

    //#####################################################
    // Fim da validacao do pce
    //#####################################################

    /**
     * Realiza a chamada para a persistencia da tela.
     */
    function storeDisposicao() {
        storeButtonScript('lms.rnc.registrarDisposicaoAction.storeDisposicao', 'storeDisposicao', document.getElementById("formDisposicao"));
    }

    /**
     * Callback do store de persistencia.
     *
     * @param
     * @param
     */
    function storeDisposicao_cb(data, error) {
        if (data._exception == undefined) {
            showSuccessMessage();
            setElementValue("dhDisposicao", setFormat(document.getElementById("dhDisposicao"), data.dhDisposicao));
            disableObjects(true);
        } else {
            alert(data._exception._message);
        }
        setFocusOnButton("btnLimpar");
    }

    /**
     * Limpa a tela do usuario...
     */
    function clearScreen() {
        resetValue(document.getElementById("formDisposicao"));
        var e = document.getElementById("motivoDisposicao.idMotivoDisposicao");
        for (var i = e.options.length - 1; i > 0; i--) {
            e.remove(e.options.length - 1);
        }

        if (!checkURLParameters()) {
            disableObjects(false);
            setFocus(document.getElementById("ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial"));
        } else {
            loadDataFromURL();
            setFocus(document.getElementById("idsOcorrenciaNaoConformidade_ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade"));
            setDisabled("idsOcorrenciaNaoConformidade", false);
            setDisabled("idsOcorrenciaNaoConformidade_ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade", false);
        }

        disableNrNaoConformidade(true);
        fillDataUsuario();
    }

    /**
     * Carrega os dados do usuario logado
     */
    function fillDataUsuario() {
        if (dataUsuario.usuario) {
            setElementValue("usuario.idUsuario", dataUsuario.usuario.idUsuario);
            setElementValue("usuario.nmUsuario", dataUsuario.usuario.nmUsuario);
        }
    }

    function disableObjects(disable) {
        setDisabled("ocorrenciaNaoConformidade.naoConformidade.filial.idFilial", disable);
        setDisabled("idsOcorrenciaNaoConformidade_ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade", disable);
        setDisabled("idsOcorrenciaNaoConformidade", disable);
        setDisabled("ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade", disable);
        setDisabled("motivoDisposicao.idMotivoDisposicao", disable);
        setDisabled("dsDisposicao", disable);
        setDisabled("ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade", disable);
        setDisabled("btnStore", disable);
    }

    /**
     * Controla o objeto 'Numero Ocorrencia'
     */
    function onChangeListNumOcorrencia() {
        idsOcorrenciaNaoConformidadeListboxDef.updateRelateds();
        comboboxChange({e: document.getElementById("idsOcorrenciaNaoConformidade_ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade")});
    }

    /**
     * Manipula o evento de insercao e remocao de dados na list de ocorrencias
     * nao conformidade.
     */
    function loadFieldsFromOcorrencia(command) {

        if ((command.name == "deleteButton_afterClick") || (command.name == "modifyButton_afterClick")) {
            var list = new Array();
            for (i = 0; i < document.getElementById("idsOcorrenciaNaoConformidade").length; i++) {
                list[i] = document.getElementById("idsOcorrenciaNaoConformidade").options[i].data.idOcorrenciaNaoConformidade;
            }

            //Caso existe elementos as serem enviados
            if (list.length > 0) {
                var sdo = createServiceDataObject("lms.rnc.registrarDisposicaoAction.findFieldsByOcorrenciaNC", "loadFieldsFromOcorrencia", {ids: list});
                xmit({serviceDataObjects: [sdo]});
            } else {
                cleanFields();
            }
        }
    }

    function cleanFields() {
        resetValue("causaNaoConformidade", document);
        resetValue("acoesCorretivas", document);
        resetValue("ocorrenciaNaoConformidade.dsOcorrenciaNc", document);
        cleanComboMotivoDisposicao();
    }

    function loadFieldsFromOcorrencia_cb(data, error) {
        cleanFields();
        if (data != null) {
            causaNaoConformidade_cb(data, error);
            acoesCorretivas_cb(data, error);
            motivoDisposicao_idMotivoDisposicao_cb(data.motivosDisposicao);
        }
    }

    /**
     * Carrega a list de ocorrencia nao conformidade caso ela tenha uma
     * negociacao vinculada.
     */
    var comboValue = null;
    function comboBoxOcorrenciaNCChangeHandler(element) {
        setElementValue("ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade", getElementValue("idsOcorrenciaNaoConformidade_ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade"));

        var beanValue = element.e.value;
        comboValue = element;

        if (beanValue !== null && beanValue !== undefined && beanValue !== "") {
            var sdo = createServiceDataObject("lms.rnc.registrarDisposicaoAction.validateOcorrenciaNaoConformidade", "loadDataListOcorrenciaNaoConformidade", {idOcorrenciaNaoConformidade: beanValue});
            xmit({serviceDataObjects: [sdo]});
        }

    }


    function loadComboOcorrenciasNC_cb(data, error) {
        idsOcorrenciaNaoConformidade_ocorrenciaNaoConformidade_idOcorrenciaNaoConformidade_cb(data);

        if (document.getElementById("ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial").isDisabled == true) {

            //busca os dados da URL
            var url = new URL(parent.location.href);
            var idOcorrenciaNaoConformidadeValue = url.parameters["ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade"];
            if (idOcorrenciaNaoConformidadeValue != undefined) {
                document.getElementById("idsOcorrenciaNaoConformidade_ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade").value = idOcorrenciaNaoConformidadeValue;
            }
        } else if (getNestedBeanPropertyValue(data, "_exception") != undefined) {
            alert(data._exception._message);
            var numeroNaoConformidade = getElementValue("ocorrenciaNaoConformidade.naoConformidade.nrNaoConformidade");
            setElementValue("ocorrenciaNaoConformidade.naoConformidade.nrNaoConformidade", "");
            cleanComboMotivoDisposicao();
            setFocus(document.getElementById("ocorrenciaNaoConformidade.naoConformidade.nrNaoConformidade"));
        }
    }


    function loadDataListOcorrenciaNaoConformidade_cb(data, error) {
        if (getNestedBeanPropertyValue(data, "ocorrenciaNaoConformidade") == "true") {
            comboboxChange(comboValue);
        } else {
            alert(data._exception._message);
            document.getElementById("idsOcorrenciaNaoConformidade_ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade").firstChild.selected = true;
        }
    }

    /**
     * Controle para o objeto de 'N?o conformidade'
     */
    function sgFilialOnChangeHandler() {
        if (getElementValue("ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial") == "") {
            disableNrNaoConformidade(true);
            resetValue(this.document);
            cleanComboMotivoDisposicao();
            fillDataUsuario();
        } else {
            disableNrNaoConformidade(false);
        }
        return lookupChange({e: document.forms[0].elements["ocorrenciaNaoConformidade.naoConformidade.filial.idFilial"]});
    }

    function disableNrNaoConformidade(disable) {
        setDisabled("ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade", false);
        setDisabled("ocorrenciaNaoConformidade.naoConformidade.nrNaoConformidade", disable);
    }

    function disableNrNaoConformidade_cb(data, error) {
        if (data.length == 0) {
            disableNrNaoConformidade(false);
            document.getElementById("ocorrenciaNaoConformidade.naoConformidade.nrNaoConformidade").value = "";
        }
        return lookupExactMatch({
            e: document.getElementById("ocorrenciaNaoConformidade.naoConformidade.filial.idFilial"),
            data: data
        });
    }

    function loadNrNaoConformidade_cb(data, error) {
        ocorrenciaNaoConformidade_naoConformidade_nrNaoConformidade_exactMatch_cb(data);
        if (data.length != 0) {
            document.getElementById("ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial").value = data[0].filial.sgFilial;
        }
    }

    function nrNaoConformidadeChangeHandler() {
        var r = lookupChange({e: document.forms[0].elements["ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade"]});
        var idFilial = getElementValue("ocorrenciaNaoConformidade.naoConformidade.filial.idFilial");
        var sgFilial = getElementValue("ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial");
        resetValue(this.document);
        setElementValue("ocorrenciaNaoConformidade.naoConformidade.filial.idFilial", idFilial);
        setElementValue("ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial", sgFilial);
        cleanComboMotivoDisposicao();
        fillDataUsuario();
        return r;
    }

    function enableNrNaoConformidade(data) {
        if (data.nrNaoConformidade != undefined) {
            disableNrNaoConformidade(false);
        } else {
            disableNrNaoConformidade(true);
        }
    }

    /**
     * Carrega o objeto a partir url
     * caso o usuario tenha vindo de uma outra tela
     */
    function loadDataFromURL() {
        var url = new URL(parent.location.href);
        var idNaoConformidade = url.parameters["ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade"];

        if (idNaoConformidade != undefined) {
            setElementValue("ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade", idNaoConformidade);

            //Carrega a combo numero ocorrencia
            notifyElementListeners({e: document.getElementById("ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade")});

            //Deixa os objetos
            setDisabled("ocorrenciaNaoConformidade.naoConformidade.filial.idFilial", true)
            setDisabled("ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade", true);

            //Forca o campo nrNaoConformidade a ficar desabilitado
            disableNrNaoConformidade(true);

            setDisabled("motivoDisposicao.idMotivoDisposicao", false);

            //aplica a mascara no campo
            format(document.getElementById("ocorrenciaNaoConformidade.naoConformidade.nrNaoConformidade"));
        }
    }

    /**
     * Verifica se existem parametros setados na URL
     */
    function checkURLParameters() {
        var url = new URL(parent.location.href);
        var idNaoConformidade = url.parameters["ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade"];

        if (idNaoConformidade != undefined)
            return true;

        return false;
    }

    /**
     * Limpa a combo de motivos de disposicao
     */
    function cleanComboMotivoDisposicao() {
        var comboData = document.getElementById("motivoDisposicao.idMotivoDisposicao").options;
        for (var i = comboData.length - 1; i > 0; i--) {
            comboData.remove(i);
        }
    }

    /**
     * Seta o foco no botao limpar
     */
    function setFocusOnButton(buttonType) {
        var doc = this.document;
        var button = findButtonByType(buttonType, doc);
        if (button == null) {
            // se n?oo achou o bot?o seta o foco no primeiro campo.
            setFocus("btnLimpar", true, true, true);
        } else if (isVisible(button)) {
            button.focus();
        } else {
            setFocusOnFirstFocusableField(doc);
        }
    }
</script>