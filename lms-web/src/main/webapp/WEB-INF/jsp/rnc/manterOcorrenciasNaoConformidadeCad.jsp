<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
    var tpStatusOcorrenciaNc;
    var tpStatusOcorrenciaNcValor;

    function carregaPagina() {
        onPageLoad();
        mostraEscondeBotaoFechar();
        loadDataUsuario();
        loadDadosTpStatusOcorrenciaNc();
    }

    function retornoPageLoad_cb(data) {
        onPageLoad_cb();
//lms.rnc.manterOcorrenciasNaoConformidadeAction.findById
        var idProcessoWorkflow = getIdProcessoWorkflow();
        //Parametro passado da tela de manterAcoes (Workflow)...
        if (idProcessoWorkflow != undefined) {
            var data = new Object();
            data.idProcessoWorkflow = idProcessoWorkflow;
            var sdo = createServiceDataObject("lms.rnc.manterOcorrenciasNaoConformidadeAction.findByIdProcessoWorkflow", "carregaDadosWorkflow", data);
            xmit({serviceDataObjects: [sdo]});
        }
    }

    function getIdProcessoWorkflow() {
        var url = new URL(parent.location.href);
        return url.parameters["idProcessoWorkflow"];
    }

    function getIsReciboIndenizacao() {
        var url = new URL(parent.location.href);
        return url.parameters["isReciboIndenizacao"];
    }

    function mostraEscondeBotaoFechar() {
        var isLookup = window.dialogArguments && window.dialogArguments.window;
        if (isLookup) {
            setDisabled('btnFechar', false);
        } else {
            document.getElementById("btnFechar").style.display = "none";
        }
    }

</script>

<adsm:window service="lms.rnc.manterOcorrenciasNaoConformidadeAction" onPageLoad="carregaPagina"
             onPageLoadCallBack="retornoPageLoad">
    <adsm:form action="/rnc/manterOcorrenciasNaoConformidade" idProperty="idOcorrenciaNaoConformidade" height="390"
               onDataLoadCallBack="carregaDados">

        <adsm:hidden property="idOcorrenciaNaoConformidadeLocMerc" serializable="false"/>

        <adsm:hidden property="naoConformidade.idNaoConformidade" serializable="true"/>
        <adsm:hidden property="naoConformidade.filial.idFilial" serializable="true"/>
        <adsm:hidden property="naoConformidade.doctoServico.idDoctoServico" serializable="true"/>
        <adsm:hidden property="naoConformidade.clienteByIdClienteRemetente.idCliente" serializable="true"/>
        <adsm:hidden property="naoConformidade.clienteByIdClienteDestinatario.idCliente" serializable="true"/>

        <adsm:textbox dataType="text" property="naoConformidade.filial.sgFilial" size="3" maxLength="3"
                      label="naoConformidade"
                      labelWidth="23%" width="27%" disabled="true" serializable="false">
            <adsm:textbox dataType="integer" property="naoConformidade.nrNaoConformidade" size="9" maxLength="8"
                          mask="00000000"
                          disabled="true" serializable="false"/>
        </adsm:textbox>

        <adsm:textbox dataType="text" property="tpStatusOcorrenciaNc.description" label="statusOcorrencia" size="25"
                      labelWidth="18%" width="32%" disabled="true" serializable="false"/>
        <adsm:hidden property="tpStatusOcorrenciaNc" serializable="false"/>

        <adsm:textbox dataType="integer" property="nrOcorrenciaNc" label="numeroOcorrencia" size="4" maxLength="2"
                      labelWidth="23%" width="27%" disabled="true" serializable="false"/>

        <adsm:combobox property="motivoAberturaNc.idMotivoAberturaNc"
                       optionProperty="idMotivoAberturaNc" optionLabelProperty="dsMotivoAbertura"
                       service="lms.rnc.manterOcorrenciasNaoConformidadeAction.findMotivoAberturaNc"
                       onchange="return motivoAberturaNc_OnChange(this)"
                       label="motivoNaoConformidade" labelWidth="18%" width="32%" required="true">
            <adsm:propertyMapping modelProperty="blExigeDocServico" relatedProperty="blExigeDocServico"/>
            <adsm:propertyMapping modelProperty="blExigeValor" relatedProperty="blExigeValor"/>
            <adsm:propertyMapping modelProperty="blExigeQtdVolumes" relatedProperty="blExigeQtdVolumes"/>
        </adsm:combobox>
        <adsm:hidden property="blExigeDocServico" serializable="false"/>
        <adsm:hidden property="blExigeValor" serializable="false"/>
        <adsm:hidden property="blExigeQtdVolumes" serializable="false"/>


        <adsm:textbox dataType="text" property="filialByIdFilialResponsavel.sgFilial" label="filialResponsavel" size="4"
                      maxLength="3"
                      labelWidth="23%" width="77%" disabled="true" serializable="false">
            <adsm:textbox dataType="text" property="filialByIdFilialResponsavel.pessoa.nmFantasia" size="50"
                          maxLength="50"
                          disabled="true" serializable="false"/>
        </adsm:textbox>
        <adsm:hidden property="filialByIdFilialResponsavel.idFilial" serializable="true"/>

        <adsm:textbox dataType="JTDateTimeZone" property="dhInclusao" label="dataHoraOcorrencia" size="16"
                      labelWidth="23%" width="77%"
                      picker="false" disabled="true" serializable="false"/>


        <adsm:textbox dataType="text" property="naoConformidade.doctoServico.tpDocumentoServico.description"
                      label="documentoServico" size="10" labelWidth="23%" width="27%" disabled="true"
                      serializable="false">
            <adsm:textbox dataType="text" property="naoConformidade.doctoServico.filialByIdFilialOrigem.sgFilial"
                          size="3" maxLength="3" serializable="false" disabled="true"/>
            <adsm:textbox dataType="integer" property="naoConformidade.doctoServico.nrDoctoServico"
                          size="11" maxLength="8" mask="00000000" serializable="false" disabled="true"/>
        </adsm:textbox>


        <adsm:hidden property="usuario.idUsuario" serializable="true"/>
        <adsm:textbox dataType="text" property="usuario.nmUsuario" label="usuario" size="35" maxLength="60"
                      labelWidth="18%" width="32%" disabled="true" serializable="false"/>


        <adsm:hidden property="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" serializable="false"/>
        <adsm:lookup dataType="text" label="controleCargas"
                     property="controleCarga.filialByIdFilialOrigem"
                     idProperty="idFilial" criteriaProperty="sgFilial"
                     service="lms.rnc.manterOcorrenciasNaoConformidadeAction.findLookupFilialByControleCarga"
                     action="/municipios/manterFiliais"
                     onchange="return controleCargaFilial_OnChange()"
                     onDataLoadCallBack="retornoControleCargaFilial"
                     popupLabel="pesquisarFilial"
                     onPopupSetValue="popupControleCargaFilial"
                     size="3" maxLength="3" labelWidth="23%" width="27%" picker="false" serializable="false">
            <adsm:propertyMapping modelProperty="pessoa.nmFantasia"
                                  relatedProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia"/>
            <adsm:lookup dataType="integer" property="controleCarga"
                         idProperty="idControleCarga" criteriaProperty="nrControleCarga"
                         service="lms.rnc.manterOcorrenciasNaoConformidadeAction.findLookupControleCarga"
                         action="/carregamento/manterControleCargas"
                         onPopupSetValue="popupControleCarga"
                         popupLabel="pesquisarControleCarga"
                         size="9" maxLength="8" mask="00000000" serializable="true">
                <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial"
                                      criteriaProperty="controleCarga.filialByIdFilialOrigem.idFilial" disable="false"/>
                <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial"
                                      criteriaProperty="controleCarga.filialByIdFilialOrigem.sgFilial" disable="false"/>
                <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia"
                                      criteriaProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia"/>
                <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial"
                                      relatedProperty="controleCarga.filialByIdFilialOrigem.idFilial"
                                      blankFill="false"/>
                <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial"
                                      relatedProperty="controleCarga.filialByIdFilialOrigem.sgFilial"
                                      blankFill="false"/>
                <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia"
                                      relatedProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia"
                                      blankFill="false"/>
                <adsm:propertyMapping modelProperty="meioTransporteByIdTransportado.nrFrota"
                                      relatedProperty="controleCarga.meioTransporteByIdTransportado.nrFrota"/>
                <adsm:propertyMapping modelProperty="meioTransporteByIdTransportado.nrIdentificador"
                                      relatedProperty="controleCarga.meioTransporteByIdTransportado.nrIdentificador"/>
                <adsm:propertyMapping modelProperty="meioTransporteByIdSemiRebocado.nrFrota"
                                      relatedProperty="controleCarga.meioTransporteByIdSemiRebocado.nrFrota"/>
                <adsm:propertyMapping modelProperty="meioTransporteByIdSemiRebocado.nrIdentificador"
                                      relatedProperty="controleCarga.meioTransporteByIdSemiRebocado.nrIdentificador"/>
            </adsm:lookup>
        </adsm:lookup>


        <adsm:textbox dataType="text" property="filialByIdFilialLegado.sgFilial" size="3" label="rncLegado"
                      labelWidth="18%" width="32%" disabled="true" serializable="false">
            <adsm:textbox dataType="integer" property="nrRncLegado" size="8" mask="00000000" disabled="true"
                          serializable="false"/>
        </adsm:textbox>


        <adsm:textbox dataType="text" property="controleCarga.meioTransporteByIdTransportado.nrFrota" label="veiculo"
                      size="6" maxLength="6" labelWidth="23%" width="27%" disabled="true" serializable="false">
            <adsm:textbox dataType="text" property="controleCarga.meioTransporteByIdTransportado.nrIdentificador"
                          size="20" maxLength="25" disabled="true" serializable="false"/>
        </adsm:textbox>

        <adsm:textbox dataType="text" property="controleCarga.meioTransporteByIdSemiRebocado.nrFrota"
                      label="semiReboque"
                      size="6" maxLength="6" labelWidth="18%" width="32%" disabled="true" serializable="false">
            <adsm:textbox dataType="text" property="controleCarga.meioTransporteByIdSemiRebocado.nrIdentificador"
                          size="20" maxLength="25" disabled="true" serializable="false"/>
        </adsm:textbox>


        <adsm:combobox property="manifesto.tpManifesto"
                       label="manifesto" labelWidth="23%" width="77%" serializable="false"
                       service="lms.rnc.manterOcorrenciasNaoConformidadeAction.findTipoManifesto"
                       optionProperty="value" optionLabelProperty="description"
                       onchange="limpaManifesto();
					   			 return changeDocumentWidgetType({
					   		documentTypeElement:this,
					   		filialElement:document.getElementById('manifesto.filialByIdFilialOrigem.idFilial'),
					   		documentNumberElement:document.getElementById('manifesto.manifestoViagemNacional.idManifestoViagemNacional'),
					   		documentGroup:'MANIFESTO',
					   		actionService:'lms.rnc.manterOcorrenciasNaoConformidadeAction'
					   		});">

            <adsm:lookup dataType="text"
                         property="manifesto.filialByIdFilialOrigem"
                         idProperty="idFilial" criteriaProperty="sgFilial"
                         service=""
                         action=""
                         size="3" maxLength="3" picker="false" disabled="true" serializable="false"
                         onDataLoadCallBack="enableManifestoManifestoViagemNacioal"
                         popupLabel="pesquisarFilial"
                         onchange="limpaManifesto();
						 		   return changeDocumentWidgetFilial({
						 	filialElement:document.getElementById('manifesto.filialByIdFilialOrigem.idFilial'),
						 	documentNumberElement:document.getElementById('manifesto.manifestoViagemNacional.idManifestoViagemNacional')
						 	});"
            />

            <adsm:lookup dataType="integer"
                         property="manifesto.manifestoViagemNacional"
                         idProperty="idManifestoViagemNacional"
                         criteriaProperty="nrManifestoOrigem"
                         service=""
                         action="" popupLabel="pesquisarManifesto"
                         onchange="return manifestoNrManifestoOrigem_OnChange();"
                         onDataLoadCallBack="retornoManifesto"
                         afterPopupSetValue="popupManifesto"
                         size="10" maxLength="8" mask="00000000" disabled="true" serializable="false"/>
        </adsm:combobox>
        <adsm:hidden property="manifesto.idManifesto" serializable="true"/>
        <adsm:hidden property="manifesto.tpStatusManifesto" serializable="false"/>
        <adsm:hidden property="manifesto.tpStatusManifestoEntrega" value="" serializable="false"/>
        <adsm:hidden property="manifesto.tpManifestoEntrega" value="EN" serializable="false"/>
        <adsm:hidden property="manifesto.filialByIdFilialOrigem.pessoa.nmFantasia" serializable="false"/>
        <adsm:hidden property="blManifestoPreenchidoManualmente" serializable="false"/>


        <adsm:pairedListbox property="notaOcorrenciaNcs"
                            size="9" boxWidth="90" label="notasFiscais" labelWidth="23%" width="32%"
                            optionProperty="idNotaOcorrenciaNc" optionLabelProperty="nrNotaFiscal"
                            sourceOptionProperty="notaFiscalConhecimento.idNotaFiscalConhecimento"/>

        <adsm:listbox property="notaOcorrenciaNcs2"
                      optionProperty="idNotaOcorrenciaNc" optionLabelProperty="nrNotaFiscal"
                      size="4" boxWidth="90" width="45%" serializable="true">
            <adsm:textbox property="nrNotaFiscal" dataType="integer" maxLength="9" size="14" serializable="true"
                          disabled="true"/>
        </adsm:listbox>

        <adsm:lookup dataType="text" property="empresa"
                     idProperty="idEmpresa" criteriaProperty="pessoa.nrIdentificacao"
                     service="lms.rnc.manterOcorrenciasNaoConformidadeAction.findLookupEmpresa"
                     action="/municipios/manterEmpresas"
                     label="companhiaAerea" size="18" maxLength="20" labelWidth="23%" width="77%"
                     disabled="true" serializable="true">
            <adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="empresa.pessoa.nmPessoa"/>
            <adsm:textbox dataType="text" property="empresa.pessoa.nmPessoa"
                          size="50" maxLength="50" disabled="true" serializable="false"/>
        </adsm:lookup>

        <adsm:complement label="descricaoNaoConformidade" width="77%" labelWidth="23%" separator="branco">
            <adsm:combobox property="descricaoPadraoNc.idDescricaoPadraoNc"
                           optionProperty="idDescricaoPadraoNc" optionLabelProperty="dsPadraoNc"
                           service="lms.rnc.manterOcorrenciasNaoConformidadeAction.findDescricaoPadraoNc"
                           boxWidth="446" onlyActiveValues="true" style="vertical-align:top" required="true"/>
            <adsm:textarea property="dsOcorrenciaNc" maxLength="1000" columns="85" rows="3" required="true"/>
        </adsm:complement>

        <adsm:textarea
                property="naoConformidade.dsMotivoAbertura"
                label="motivoAbertura"
                width="50%"
                labelWidth="23%"
                maxLength="400"
                columns="85"
                rows="3"
                disabled="true"
        />

        <adsm:checkbox property="blCaixaReaproveitada" label="caixaReaproveitada"
                       onclick="checkCaixaReaproveitada_OnClick()"
                       labelWidth="23%" width="77%"/>
        <adsm:textbox label="clienteCaixa" dataType="text" property="dsCaixaReaproveitada" size="60" maxLength="80"
                      labelWidth="23%" width="77%" serializable="true"/>

        <adsm:complement label="causaNaoConformidade" width="77%" labelWidth="23%" separator="branco">
            <adsm:combobox property="causaNaoConformidade.idCausaNaoConformidade"
                           optionProperty="idCausaNaoConformidade" optionLabelProperty="dsCausaNaoConformidade"
                           service="lms.rnc.manterOcorrenciasNaoConformidadeAction.findCausaNaoConformidade"
                           boxWidth="446" onlyActiveValues="true" style="vertical-align:top" required="true"/>
            <adsm:textarea property="dsCausaNc" maxLength="200" columns="85"/>
        </adsm:complement>

        <adsm:combobox property="moeda.idMoeda" label="valorNaoConformidade"
                       service="lms.rnc.manterOcorrenciasNaoConformidadeAction.findMoeda"
                       optionProperty="idMoeda" optionLabelProperty="siglaSimbolo"
                       onchange="return moeda_OnChange(this)"
                       labelWidth="23%" width="77%">
            <adsm:textbox dataType="currency"
                          property="vlOcorrenciaNc" mask="###,###,###,###,##0.00" minValue="0.01" size="18"
                          maxLength="18" disabled="true" required="true"/>
        </adsm:combobox>

        <adsm:textbox property="qtVolumes" label="quantidadeVolumes" dataType="integer" size="6" maxLength="6"
                      labelWidth="23%" width="77%" required="true"/>

        <adsm:buttonBar>
            <adsm:button caption="negociacoes" id="botaoNegociacoes" onclick="negociacaoClick();" disabled="false"
                         buttonType="findButton">
                <adsm:linkProperty src="naoConformidade.idNaoConformidade"
                                   target="ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade"/>
                <adsm:linkProperty src="naoConformidade.filial.sgFilial"
                                   target="ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial"/>
                <adsm:linkProperty src="naoConformidade.nrNaoConformidade"
                                   target="ocorrenciaNaoConformidade.naoConformidade.nrNaoConformidade"/>
                <adsm:linkProperty src="idOcorrenciaNaoConformidade"
                                   target="ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade"/>
                <adsm:linkProperty src="nrOcorrenciaNc" target="ocorrenciaNaoConformidade.nrOcorrenciaNc"/>
                <adsm:linkProperty src="idOcorrenciaNaoConformidadeLocMerc"
                                   target="idOcorrenciaNaoConformidadeLocMerc"/>
            </adsm:button>

            <adsm:button caption="disposicao" id="botaoDisposicao" onclick="disposicaoClick();" disabled="false"
                         buttonType="findButton">
                <adsm:linkProperty src="idOcorrenciaNaoConformidadeLocMerc"
                                   target="idOcorrenciaNaoConformidadeLocMerc"/>
                <adsm:linkProperty src="naoConformidade.idNaoConformidade"
                                   target="ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade"/>
                <adsm:linkProperty src="naoConformidade.filial.idFilial"
                                   target="ocorrenciaNaoConformidade.naoConformidade.filial.idFilial"/>
                <adsm:linkProperty src="naoConformidade.filial.sgFilial"
                                   target="ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial"/>
                <adsm:linkProperty src="naoConformidade.nrNaoConformidade"
                                   target="ocorrenciaNaoConformidade.naoConformidade.nrNaoConformidade"/>
                <adsm:linkProperty src="idOcorrenciaNaoConformidade"
                                   target="ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade"/>
                <adsm:linkProperty src="nrOcorrenciaNc" target="ocorrenciaNaoConformidade.nrOcorrenciaNc"/>

            </adsm:button>

            <adsm:button caption="salvar" id="botaoSalvar" disabled="true" onclick="salvarRnc(this.form)"/>
            <adsm:newButton id="botaoNovo"/>
            <adsm:button caption="fechar" id="btnFechar" onclick="self.close();" buttonType="closeButton"/>
        </adsm:buttonBar>

        <script>
            var lms_12004 = '<adsm:label key="LMS-12004"/>';
            var labelCausaNaoConformidade = '<adsm:label key="causaNaoConformidade"/>';
            var labelDescricaoNaoConformidade = '<adsm:label key="descricaoNaoConformidade"/>';
            var labelFilialResponsavel = '<adsm:label key="filialResponsavel" />';
            var labelManifesto = '<adsm:label key="manifesto" />';
            var labelValorNaoConformidade = '<adsm:label key="valorNaoConformidade"/>';
        </script>

    </adsm:form>
</adsm:window>

<script>
    document.getElementById("dsOcorrenciaNc").label = labelDescricaoNaoConformidade;
    document.getElementById("dsCausaNc").label = labelCausaNaoConformidade;

    /**
     * Fun??o chamada ao entrar na tela
     */
    function initWindow(eventObj) {
        getDoctoServico();
        setDisabled('botaoSalvar', true);

        var event = eventObj.name;
        if (event == "gridRow_click" || event == "storeButton") {
            setDisabled('motivoAberturaNc.idMotivoAberturaNc', true);
            desabilitaTodosCampos();
            if (event == "storeButton") {
                mostraMsgFilialResponsavel();
            }
            setFocusOnNewButton();
        }
        else {
            var idDoctoServico = getElementValue("naoConformidade.doctoServico.idDoctoServico");
            if (idDoctoServico != '') {
                buscarDadosDoctoServico(idDoctoServico);
            }
            if (event == "newButton_click" || event == "tab_click") {
                if (event == "newButton_click") {
                    if (idDoctoServico == '') {
                        parent.parent.redirectPage("rnc/abrirRNC.do?cmd=main");
                        setDisabled('botaoNegociacoes', false);
                        setDisabled('botaoDisposicao', false);
                        return;
                    }
                }
                document.getElementById("dsCaixaReaproveitada").required = "false";
                if (tpStatusOcorrenciaNc != null) {
                    setElementValue('tpStatusOcorrenciaNc', tpStatusOcorrenciaNcValor);
                    setElementValue('tpStatusOcorrenciaNc.description', tpStatusOcorrenciaNc);
                }
                desabilitaTodosCampos();
                buscarNotasFiscais();
                if (getElementValue('idOcorrenciaNaoConformidade') == '') {
                    povoaDadosUsuario();
                    buscarManifestoControleCargas(idDoctoServico);
                    desabilitaTab("caracteristicas", true);
                    desabilitaTab("fotos", true);
                    setDisabled('botaoSalvar', false);
                    setDisabled('motivoAberturaNc.idMotivoAberturaNc', false);
                }
                setFocusOnFirstFocusableField();
            }
        }

        if (getIdProcessoWorkflow() != undefined && getIdProcessoWorkflow() != "") {
            setDisabled('botaoNegociacoes', false);
            setDisabled('botaoDisposicao', true);
            setDisabled("botaoNovo", true);
            setFocus(document.getElementById("btnFechar"), true, true);
        }
    }

    function disposicaoClick() {
        if (getElementValue("tpStatusOcorrenciaNc") == "F" || getIsReciboIndenizacao() != undefined) {
            showModalDialog('rnc/exibirDisposicao.do?cmd=cad', window, 'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:285px;');
        } else {
            parent.parent.redirectPage('rnc/registrarDisposicao.do?cmd=main' + buildLinkPropertiesQueryString_disposicao());
        }
    }

    // ButtonTag.linkProperties support function for dynamic queryString build
    function buildLinkPropertiesQueryString_disposicao() {
        var qs = "";
        if (document.getElementById("naoConformidade.idNaoConformidade").type == 'checkbox') {
            qs += "&ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade=" + document.getElementById("naoConformidade.idNaoConformidade").checked;
        } else {
            qs += "&ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade=" + document.getElementById("naoConformidade.idNaoConformidade").value;
        }
        if (document.getElementById("naoConformidade.filial.sgFilial").type == 'checkbox') {
            qs += "&ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial=" + document.getElementById("naoConformidade.filial.sgFilial").checked;
        } else {
            qs += "&ocorrenciaNaoConformidade.naoConformidade.filial.idFilial=" + document.getElementById("naoConformidade.filial.idFilial").value;
            qs += "&ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial=" + document.getElementById("naoConformidade.filial.sgFilial").value;
        }
        if (document.getElementById("naoConformidade.nrNaoConformidade").type == 'checkbox') {
            qs += "&ocorrenciaNaoConformidade.naoConformidade.nrNaoConformidade=" + document.getElementById("naoConformidade.nrNaoConformidade").checked;
        } else {
            qs += "&ocorrenciaNaoConformidade.naoConformidade.nrNaoConformidade=" + document.getElementById("naoConformidade.nrNaoConformidade").value;
        }
        if (document.getElementById("idOcorrenciaNaoConformidade").type == 'checkbox') {
            qs += "&ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade=" + document.getElementById("idOcorrenciaNaoConformidade").checked;
        } else {
            qs += "&ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade=" + document.getElementById("idOcorrenciaNaoConformidade").value;
        }
        if (document.getElementById("nrOcorrenciaNc").type == 'checkbox') {
            qs += "&ocorrenciaNaoConformidade.nrOcorrenciaNc=" + document.getElementById("nrOcorrenciaNc").checked;
        } else {
            qs += "&ocorrenciaNaoConformidade.nrOcorrenciaNc=" + document.getElementById("nrOcorrenciaNc").value;
        }
        qs += "&idOcorrenciaNaoConformidadeLocMerc=" + document.getElementById("idOcorrenciaNaoConformidadeLocMerc").value;

        return qs;
    }

    function negociacaoClick() {
        if (getIdProcessoWorkflow() != undefined || getIsReciboIndenizacao() != undefined) {
            showModalDialog('rnc/registrarNegociacoes.do?cmd=main' + buildLinkPropertiesQueryString_negociacoes(), window, 'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:500px;');
        } else {
            parent.parent.redirectPage('rnc/registrarNegociacoes.do?cmd=main' + buildLinkPropertiesQueryString_negociacoes());
        }
    }

    // ButtonTag.linkProperties support function for dynamic queryString build
    function buildLinkPropertiesQueryString_negociacoes() {
        var qs = "";
        if (document.getElementById("naoConformidade.idNaoConformidade").type == 'checkbox') {
            qs += "&ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade=" + document.getElementById("naoConformidade.idNaoConformidade").checked;
        } else {
            qs += "&ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade=" + encodeString(document.getElementById("naoConformidade.idNaoConformidade").value);
        }
        if (document.getElementById("naoConformidade.filial.sgFilial").type == 'checkbox') {
            qs += "&ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial=" + document.getElementById("naoConformidade.filial.sgFilial").checked;
        } else {
            qs += "&ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial=" + encodeString(document.getElementById("naoConformidade.filial.sgFilial").value);
        }
        if (document.getElementById("naoConformidade.nrNaoConformidade").type == 'checkbox') {
            qs += "&ocorrenciaNaoConformidade.naoConformidade.nrNaoConformidade=" + document.getElementById("naoConformidade.nrNaoConformidade").checked;
        } else {
            qs += "&ocorrenciaNaoConformidade.naoConformidade.nrNaoConformidade=" + encodeString(document.getElementById("naoConformidade.nrNaoConformidade").value);
        }
        if (document.getElementById("idOcorrenciaNaoConformidade").type == 'checkbox') {
            qs += "&ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade=" + document.getElementById("idOcorrenciaNaoConformidade").checked;
        } else {
            qs += "&ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade=" + encodeString(document.getElementById("idOcorrenciaNaoConformidade").value);
        }
        if (document.getElementById("nrOcorrenciaNc").type == 'checkbox') {
            qs += "&ocorrenciaNaoConformidade.nrOcorrenciaNc=" + document.getElementById("nrOcorrenciaNc").checked;
        } else {
            qs += "&ocorrenciaNaoConformidade.nrOcorrenciaNc=" + encodeString(document.getElementById("nrOcorrenciaNc").value);
        }
        if (document.getElementById("idOcorrenciaNaoConformidadeLocMerc").type == 'checkbox') {
            qs += "&idOcorrenciaNaoConformidadeLocMerc=" + document.getElementById("idOcorrenciaNaoConformidadeLocMerc").checked;
        } else {
            qs += "&idOcorrenciaNaoConformidadeLocMerc=" + encodeString(document.getElementById("idOcorrenciaNaoConformidadeLocMerc").value);
        }
        return qs;
    }

    /**
     * Busca dados da tela de pesquisa
     */
    function getDoctoServico() {
        var tabDet = getTabGroup(this.document).getTab("pesq");
        var idDoctoServico = tabDet.getFormProperty("naoConformidade.doctoServico.idDoctoServico");
        setElementValue('naoConformidade.doctoServico.idDoctoServico', idDoctoServico);
        setElementValue('naoConformidade.idNaoConformidade', tabDet.getFormProperty("naoConformidade.idNaoConformidade"));
        if (idDoctoServico != '') {
            setElementValue('moeda.idMoeda', tabDet.getFormProperty("naoConformidade.doctoServico.moeda.idMoeda"));
        }
    }

    function mostraMsgFilialResponsavel() {
        alert(labelFilialResponsavel + ': ' + getElementValue('filialByIdFilialResponsavel.sgFilial'));
        desabilitaTab("caracteristicas", false);
        desabilitaTab("fotos", false);
    }

    /**
     * Carrega os dados do usuario (Usuario, Filial).
     */
    function loadDataUsuario() {
        var data = new Array();
        var sdo = createServiceDataObject("lms.rnc.manterOcorrenciasNaoConformidadeAction.getDataUsuario", "resultado_loadDataUsuario", data);
        xmit({serviceDataObjects: [sdo]});
    }

    var dataUsuario;
    function resultado_loadDataUsuario_cb(data, error) {
        if (error != undefined) {
            alert(error);
            return false;
        }
        dataUsuario = data;
    }

    /**
     * Carrega os campos da tela. Utilizada pelas funcoes
     * carregaDados e carregaDadosWorkflow.
     *
     * @param data
     * @param error
     */
    function carregaCampos(data, error) {
        onDataLoad_cb(data, error);
        format(document.getElementById("naoConformidade.nrNaoConformidade"));
        format(document.getElementById("naoConformidade.doctoServico.nrDoctoServico"));
        carregaManifesto(data);
        buscarNotasFiscais();

    }

    /**
     * Chamada ao retornar do carregamento dos dados
     */
    function carregaDados_cb(data, error) {
        if (error != undefined) {
            alert(error);
            return false;
        }

        // Seta na tela de pesquisa o idNaoConformidade e o idDoctoServico
        var windowList = getTabGroup(this.document).getTab("pesq").tabOwnerFrame.window;
        var idNaoConformidade = getNestedBeanPropertyValue(data, "naoConformidade.idNaoConformidade");
        if (idNaoConformidade != undefined && idNaoConformidade != "") {
            windowList.setElementValue("naoConformidade.idNaoConformidade", idNaoConformidade);
        }
        var idDoctoServico = getNestedBeanPropertyValue(data, "naoConformidade.doctoServico.idDoctoServico");
        if (idDoctoServico != undefined && idDoctoServico != "") {
            windowList.setElementValue("naoConformidade.doctoServico.idDoctoServico", idDoctoServico);
        }

        //Utilizada pela carregaDadosWorkFlow...
        carregaCampos(data, error);

        setDisabled('botaoSalvar', true);

        if (data.naoConformidade.tpStatusNaoConformidade && data.naoConformidade.tpStatusNaoConformidade == "AGP") {
            desabilitaTab("caracteristicas", true);
            desabilitaTab("fotos", true);
            setDisabled('botaoNegociacoes', true);
            setDisabled('botaoDisposicao', true);
        } else {
            desabilitaTab("caracteristicas", false);
            desabilitaTab("fotos", false);
            setDisabled('botaoNegociacoes', false);
            setDisabled('botaoDisposicao', false);
        }

        setFocusOnNewButton();

        if (getIdProcessoWorkflow() != undefined && getIdProcessoWorkflow() != "") {
            setDisabled('botaoNegociacoes', false);
            setDisabled('botaoDisposicao', true);
            setDisabled("botaoNovo", true);
            setFocusOnFirstFocusableField();
            setFocus(document.getElementById("btnFechar"), true, true);
            alert(getElementValue('naoConformidade.dsMotivoAbertura'));
        }

        // LMS-3240
        var idsMotivoAberturaNF = "30,31,32,33,34,35";

        if (idsMotivoAberturaNF.indexOf(getElementValue('motivoAberturaNc.idMotivoAberturaNc')) != -1) {
            setDisabled('botaoNegociacoes', true);
            setDisabled('botaoDisposicao', true);
            setDisabled('botaoSalvar', true);
            setDisabled('botaoNovo', true);

            desabilitaTab("item", false);
        }
        else {
            desabilitaTab("item", true);
        }

    }

    /**
     * Utilizada o carregamento da visualizacao do workflow.
     */
    function carregaDadosWorkflow_cb(data, error) {
        if (error != undefined) {
            alert(error);
            return false;
        }

        //Utilizada pela carregaDados...
        carregaCampos(data, error);
        desabilitaTodosCampos();
        setDisabled('motivoAberturaNc.idMotivoAberturaNc', true);

        //Desabilita botoes...
        setDisabled('botaoNegociacoes', false);
        setDisabled('botaoDisposicao', true);
        setDisabled('botaoSalvar', true);
        setDisabled('botaoNovo', true);
        return false;
    }

    function loadDadosTpStatusOcorrenciaNc() {
        var isLookup = window.dialogArguments && window.dialogArguments.window;
        if (isLookup) {
            var data = new Array();
            setNestedBeanPropertyValue(data, "domainName", "DM_STATUS_OCORRENCIA_NC");
            setNestedBeanPropertyValue(data, "value", "A");
            var sdo = createServiceDataObject("lms.rnc.manterOcorrenciasNaoConformidadeAction.findDomainValueDescription", "resultado_loadDadosTpStatusOcorrenciaNc", data);
            xmit({serviceDataObjects: [sdo]});
        }
    }

    function resultado_loadDadosTpStatusOcorrenciaNc_cb(data) {
        tpStatusOcorrenciaNc = getNestedBeanPropertyValue(data, "dominio");
        tpStatusOcorrenciaNcValor = "A";
    }

    function povoaDadosUsuario() {
        setElementValue("usuario.nmUsuario", getNestedBeanPropertyValue(dataUsuario, "usuario.nmUsuario"));
    }

    /**
     * Desabilita todos os campos da tela com exce??o do "Motivo n?o conformidade"
     */
    function desabilitaTodosCampos() {
        setDisabled('naoConformidade.filial.sgFilial', true);
        setDisabled('naoConformidade.nrNaoConformidade', true);
        desabilitaControleCarga(true);
        desabilitaTagManifesto(true);
        setDisabled('empresa.idEmpresa', true);
        setDisabled('descricaoPadraoNc.idDescricaoPadraoNc', true);
        setDisabled('dsOcorrenciaNc', true);
        setDisabled('blCaixaReaproveitada', true);
        setDisabled('dsCaixaReaproveitada', true);
        setDisabled('causaNaoConformidade.idCausaNaoConformidade', true);
        setDisabled('dsCausaNc', true);
        setDisabled('moeda.idMoeda', true);
        setDisabled('vlOcorrenciaNc', true);
        setDisabled('qtVolumes', true);
        setDisabled('notaOcorrenciaNcs', true);
        setDisabled('notaOcorrenciaNcs2', true);
        setDisabled('notaOcorrenciaNcs2_nrNotaFiscal', true);
    }

    /**
     * Respons?vel por habilitar/desabilitar uma determinada aba
     */
    function desabilitaTab(aba, disabled) {
        var tabGroup = getTabGroup(this.document);
        tabGroup.setDisabledTab(aba, disabled);
    }

    /**
     * Chamada quando o campo 'Caixa reaproveitada' for marcado/desmarcado. Conforme a escolha, deve habilitar/desabilitar
     * o campo Cliente (caixa).
     */
    function checkCaixaReaproveitada_OnClick() {
        if (getElementValue('blCaixaReaproveitada') == false) {
            resetValue('dsCaixaReaproveitada');
            setDisabled('dsCaixaReaproveitada', true);
            document.getElementById("dsCaixaReaproveitada").required = "false";
        }
        else {
            setDisabled('dsCaixaReaproveitada', false);
            document.getElementById("dsCaixaReaproveitada").required = "true";
        }
    }

    /**
     * Realiza a pesquisa para buscar os dados da combo "Descri??o n?o conformidade" de acordo com o "Motivo n?o conformidade"
     * selecionado.
     */
    function loadDataDescricaoPadraoNc() {
        var sdo = createServiceDataObject("lms.rnc.manterOcorrenciasNaoConformidadeAction.findDescricaoPadraoNc", "resultado_loadDataDescricaoPadraoNc",
            {
                motivoAberturaNc: {idMotivoAberturaNc: getElementValue('motivoAberturaNc.idMotivoAberturaNc')},
                tpSituacao: "A"
            });
        xmit({serviceDataObjects: [sdo]});
    }

    /**
     * Povoa a combo "Descri??o n?o conformidade".
     */
    function resultado_loadDataDescricaoPadraoNc_cb(data, error) {
        if (error != undefined) {
            alert(error);
            return false;
        }
        descricaoPadraoNc_idDescricaoPadraoNc_cb(data);
        // se for retornado 1 registro, ele deve ser selecionado
        if (document.getElementById('descricaoPadraoNc.idDescricaoPadraoNc').length == 2) {
            setElementValue('descricaoPadraoNc.idDescricaoPadraoNc', document.getElementById('descricaoPadraoNc.idDescricaoPadraoNc').options[1].value);
        }
    }

    function moeda_OnChange(combo) {
        if (getElementValue("moeda.idMoeda") == "") {
            resetValue("vlOcorrenciaNc");
            setDisabled("vlOcorrenciaNc", true);
        }
        else
            setDisabled("vlOcorrenciaNc", false);
        return comboboxChange({e: combo});
    }

    /**
     * Realiza pesquisa em NotasFiscais de acordo com o docto de servico informado (CTRC, NFT ou MDA).
     */
    function buscarNotasFiscais() {
        getDoctoServico();
        var idDoctoServico = getElementValue('naoConformidade.doctoServico.idDoctoServico');
        if (idDoctoServico == "")
            return;

        setDisabled('notaOcorrenciaNcs', true);
        var tpDocumentoServico = getElementValue('naoConformidade.doctoServico.tpDocumentoServico.description');
        if (tpDocumentoServico == 'CTRC' || tpDocumentoServico == 'NFT') {
            var sdo = createServiceDataObject("lms.rnc.manterOcorrenciasNaoConformidadeAction.findNotaFiscalConhecimento",
                "resultado_buscarNotasFiscais", {conhecimento: {idDoctoServico: idDoctoServico}});
            xmit({serviceDataObjects: [sdo]});
        }
        else if (tpDocumentoServico == 'MDA') {
            var sdo = createServiceDataObject("lms.rnc.manterOcorrenciasNaoConformidadeAction.findNotaFiscalMda",
                "resultado_buscarNotasFiscais", {mda: {idDoctoServico: idDoctoServico}});
            xmit({serviceDataObjects: [sdo]});
        }
    }

    /**
     * Retorno da pesquisa em Notas Fiscais.
     */
    function resultado_buscarNotasFiscais_cb(data, error) {
        if (error != undefined) {
            alert(error);
            return false;
        }
        limpaPaired();
        notaOcorrenciaNcs_source_cb(data, error);
    }

    function limpaPaired() {
        notaOcorrenciaNcsListboxDef.clearSourceOptions();
    }

    function buscarDadosDoctoServico(idDoctoServico) {
        var sdo = createServiceDataObject("lms.rnc.manterOcorrenciasNaoConformidadeAction.findDataDoctoServico",
            "resultado_buscarDadosDoctoServico", {idDoctoServico: idDoctoServico});
        xmit({serviceDataObjects: [sdo]});
    }

    /**
     * Povoa os campos com os dados retornados da busca em documento de servi?o
     */
    function resultado_buscarDadosDoctoServico_cb(data, error) {
        if (error != undefined) {
            alert(error);
            return false;
        }
        var awb = "";
        if (data != undefined) {
            awb = getNestedBeanPropertyValue(data, "awb");
        }
        if (awb != undefined && awb != "")
            setDisabled('empresa.idEmpresa', false);
        else
            setDisabled('empresa.idEmpresa', true);
    }


    var formRnc;
    /**
     * Fun??o chamada quando no eveto onclick do bot?o salvar.
     */
    function salvarRnc(form) {
        if (getElementValue('controleCarga.idControleCarga') != "") {
            document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem").required = "true";
            document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem").label = labelManifesto;
        }
        else {
            document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem").required = "false";
        }

        if (!validateForm(form)) {
            return false;
        }

        formRnc = form;
        validaPreCondicoes();
    }

    /**
     * Valida a quantidade e o valor que foi informado, com os respectivos valores do docto de servi?o.
     */
    function validaPreCondicoes() {
        var sdo = createServiceDataObject("lms.rnc.manterOcorrenciasNaoConformidadeAction.validatePreConditions", "resultado_validaPreCondicoes",
            {
                idNaoConformidade: getElementValue('naoConformidade.idNaoConformidade'),
                idDoctoServico: getElementValue('naoConformidade.doctoServico.idDoctoServico'),
                qtVolumes: getElementValue('qtVolumes'),
                vlOcorrenciaNc: getElementValue('vlOcorrenciaNc'),
                idManifesto: getElementValue('manifesto.idManifesto'),
                dsOcorrenciaNc: getElementValue('dsOcorrenciaNc'),
                idMotivoAberturaNc: getElementValue('motivoAberturaNc.idMotivoAberturaNc')
            });
        xmit({serviceDataObjects: [sdo]});
    }


    function resultado_validaPreCondicoes_cb(data, error) {
        if (error != undefined) {
            alert(error);
            return false;
        }
        submeterFormulario();
    }


    function submeterFormulario() {
        showModalDialog('rnc/registrarFilialResponsavelNaoConformidade.do?cmd=main', window, 'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:550px;dialogHeight:120px;');
        if (getElementValue('filialByIdFilialResponsavel.idFilial') == undefined || getElementValue('filialByIdFilialResponsavel.idFilial') == "") {
            return false;
        }
        storeButtonScript('lms.rnc.manterOcorrenciasNaoConformidadeAction.store', 'store', formRnc);
    }


    function store_cb(data, errorMsg, errorKey, showErrorAlert, eventObj) {
        // executa a onDataLoad_cb indicando que n?o deve ser executada as fun??es
        // de habilitar e desabilitar os botoes.
        if (eventObj == undefined) {
            eventObj = {name: "storeButton"};
        }
        onDataLoad_cb(data, errorMsg, errorKey, eventObj);
        if (errorMsg == null) {
            // verifica se tem a tab se existir adiciona ela como origem do evento.
            var tab = getTab(document, false);
            if (tab) {
                eventObj.src = tab.tabGroup.selectedTab;
            }

            showSuccessMessage();

            var lmsMensagemAgp = getNestedBeanPropertyValue(data, "lmsMensagemAgp");
            if (lmsMensagemAgp != null) {
                alert(lmsMensagemAgp);
            }

            // initWindowScript definido em 'elements.js'
            initWindowScript(document.parentWindow, eventObj);
        } else {
            if ((showErrorAlert == undefined) || (showErrorAlert == true)) {
                alert(errorMsg);
            }
            setFocusOnFirstFocusableField(document);
        }
    }


    /************************* INICIO - CONTROLE CARGA *************************/
    function resetaControleCarga() {
        resetValue('manifesto.idManifesto');
        resetValue('controleCarga.idControleCarga');
        resetValue('controleCarga.filialByIdFilialOrigem.idFilial');
        limparDadosFrota();
        desabilitaControleCarga(false);
    }

    /**
     * Limpa os dados informados no campo ve?culo e semi-reboque
     */
    function limparDadosFrota() {
        resetValue('controleCarga.meioTransporteByIdTransportado.nrFrota');
        resetValue('controleCarga.meioTransporteByIdTransportado.nrIdentificador');
        resetValue('controleCarga.meioTransporteByIdSemiRebocado.nrFrota');
        resetValue('controleCarga.meioTransporteByIdSemiRebocado.nrIdentificador');
    }

    function desabilitaControleCarga(valor) {
        setDisabled('controleCarga.filialByIdFilialOrigem.idFilial', valor);
        if (valor == true)
            setDisabled('controleCarga.idControleCarga', valor);
        else {
            setDisabled('controleCarga.idControleCarga', false);
            if (getElementValue('controleCarga.nrControleCarga') == "")
                setDisabled('controleCarga.nrControleCarga', true);
        }
    }

    function controleCargaFilial_OnChange() {
        if (getElementValue('controleCarga.filialByIdFilialOrigem.sgFilial') == "") {
            resetValue('controleCarga.idControleCarga');
            setDisabled('controleCarga.idControleCarga', false);
            setDisabled('controleCarga.nrControleCarga', true);
        }
        return controleCarga_filialByIdFilialOrigem_sgFilialOnChangeHandler();
    }

    function retornoControleCargaFilial_cb(data, error) {
        if (error != undefined) {
            alert(error);
            return false;
        }
        var r = controleCarga_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data)
        if (r == true) {
            setDisabled('controleCarga.idControleCarga', false);
            setFocus(document.getElementById("controleCarga.nrControleCarga"));
        }
    }

    function popupControleCargaFilial(data) {
        setDisabled('controleCarga.idControleCarga', false);
    }

    function popupControleCarga(data) {
        setDisabled('controleCarga.idControleCarga', false);
    }
    /************************* FIM - CONTROLE CARGA *************************/







    /************************* INICIO - MOTIVO ABERTURA NC *************************/
    /**
     * Resultado da pesquisa de "Motivo n?o conformidade".
     */
    function setaDadosByMotivoAberturaNC() {
        if (getElementValue('blExigeDocServico') == 'true') {
            limparCamposRelacionadosManifesto();
            buscarManifestoControleCargas(getElementValue('naoConformidade.doctoServico.idDoctoServico'));
            desabilitaControleCarga(true);
            desabilitaTagManifesto(true);
            setDisabled('notaOcorrenciaNcs2', true);
            setDisabled('notaOcorrenciaNcs2_nrNotaFiscal', true);
            resetValue(document.getElementById('notaOcorrenciaNcs2'));
            resetValue('notaOcorrenciaNcs2_nrNotaFiscal');
        }
        else {
            if (getElementValue('blManifestoPreenchidoManualmente') != 'true') {
                desabilitaControleCarga(false);
            }
            desabilitaTagManifesto(false);
            if (document.getElementById('_notaOcorrenciaNcs_source').length == 0) {
                setDisabled('notaOcorrenciaNcs2', false);
                setDisabled('notaOcorrenciaNcs2_nrNotaFiscal', false);
            }
            else {
                setDisabled('notaOcorrenciaNcs2', true);
                setDisabled('notaOcorrenciaNcs2_nrNotaFiscal', true);
            }
        }

        if (getElementValue("blExigeValor") == 'true') {
            document.getElementById("moeda.idMoeda").required = "true";
            document.getElementById("vlOcorrenciaNc").required = "true";
            document.getElementById("vlOcorrenciaNc").label = labelValorNaoConformidade;
        }
        else {
            document.getElementById("moeda.idMoeda").required = "false";
            document.getElementById("vlOcorrenciaNc").required = "false";
        }

        if (getElementValue("blExigeQtdVolumes") == 'true')
            document.getElementById("qtVolumes").required = "true";
        else
            document.getElementById("qtVolumes").required = "false";
    }

    /**
     * Chamada a cada altera??o do valor da combo "Motivo n?o conformidade". Chama a fun??o para povoar a "Descri??o n?o conformidade"
     * de acordo com o "Motivo n?o conformidade" selecionado e habilita alguns campos da tela.
     */
    function motivoAberturaNc_OnChange(combo) {
        limparCamposRelacionadosMotivoAbertura();
        var r = comboboxChange({e: combo})
        if (getElementValue('motivoAberturaNc.idMotivoAberturaNc') != "") {
            // Verifica se est? no modo de inclus?o
            if (getElementValue('idOcorrenciaNaoConformidade') == '') {
                habilitaCamposConformeMotivoAberturaNc();
                loadDataDescricaoPadraoNc();
                setaDadosByMotivoAberturaNC();
            }
        }
        else
            desabilitaTodosCampos();
        return r;
    }


    function limparCamposRelacionadosMotivoAbertura() {
        resetValue(document.getElementById("notaOcorrenciaNcs2"));
        resetValue("notaOcorrenciaNcs2_nrNotaFiscal");
        resetValue('empresa.idEmpresa');
        resetValue('descricaoPadraoNc.idDescricaoPadraoNc');
        resetValue('dsOcorrenciaNc');
        resetValue('blCaixaReaproveitada');
        resetValue('dsCaixaReaproveitada');
        resetValue('causaNaoConformidade.idCausaNaoConformidade');
        resetValue('dsCausaNc');
        resetValue('vlOcorrenciaNc');
        resetValue('qtVolumes');
        if (getElementValue('naoConformidade.doctoServico.idDoctoServico') == '')
            resetValue('moeda.idMoeda');
    }

    /**
     * Respons?vel por habilitar campos quando o 'Motivo n?o conformidade' for informado.
     */
    function habilitaCamposConformeMotivoAberturaNc() {
        setDisabled('descricaoPadraoNc.idDescricaoPadraoNc', false);
        setDisabled('dsOcorrenciaNc', false);
        setDisabled('blCaixaReaproveitada', false);
        setDisabled('dsCaixaReaproveitada', true);
        setDisabled('causaNaoConformidade.idCausaNaoConformidade', false);
        setDisabled('dsCausaNc', false);
        setDisabled('qtVolumes', false);
        setDisabled('notaOcorrenciaNcs', false);
        if (getElementValue('naoConformidade.doctoServico.idDoctoServico') == '')
            setDisabled('moeda.idMoeda', false);
        else
            setDisabled('vlOcorrenciaNc', false);
    }
    /************************* FIM - MOTIVO ABERTURA NC *************************/







    /************************* INICIO - MANIFESTO *************************/

    /**
     * Limpa os campos relacionados ao manifesto
     */
    function limparCamposRelacionadosManifesto() {
        resetValue('controleCarga.idControleCarga');
        resetValue('controleCarga.filialByIdFilialOrigem.idFilial');
        limparTagManifesto();
        limparDadosFrota();
    }

    /**
     * Limpa os dados informados na tag manifesto
     */
    function limparTagManifesto() {
        resetValue("manifesto.tpManifesto");
        resetValue("manifesto.idManifesto");
        resetValue("manifesto.filialByIdFilialOrigem.idFilial");
        resetValue("manifesto.manifestoViagemNacional.idManifestoViagemNacional");
        resetValue("manifesto.manifestoViagemNacional.nrManifestoOrigem");
    }

    function limpaManifesto() {
        resetaControleCarga();
        resetValue('manifesto.idManifesto');
        desabilitaControleCarga(false);
    }

    /**
     * Respons?vel por habilitar/desabilitar os campos da tag manifesto
     */
    function desabilitaTagManifesto(valor) {
        setDisabled('manifesto.tpManifesto', valor);
        if (valor == true) {
            setDisabled('manifesto.filialByIdFilialOrigem.idFilial', true);
            setDisabled('manifesto.manifestoViagemNacional.idManifestoViagemNacional', true);
        }
        else {
            if (getElementValue('manifesto.idManifesto') != "" || getElementValue('manifesto.tpManifesto') != "")
                setDisabled('manifesto.filialByIdFilialOrigem.idFilial', false);
            if (getElementValue('manifesto.idManifesto') != "" || getElementValue('manifesto.filialByIdFilialOrigem.idFilial') != "")
                setDisabled('manifesto.manifestoViagemNacional.idManifestoViagemNacional', false);
        }
    }

    function popupManifesto(data) {
        callBackManifesto();
    }

    /**
     * Quando o "Manifesto" for informado
     */
    function retornoManifesto_cb(data) {
        var r = manifesto_manifestoViagemNacional_nrManifestoOrigem_exactMatch_cb(data);
        if (r == true) {
            callBackManifesto();
        }
    }

    function callBackManifesto() {
        var idManifesto = getElementValue("manifesto.manifestoViagemNacional.idManifestoViagemNacional");
        setElementValue('manifesto.idManifesto', idManifesto);
        setElementValue('blManifestoPreenchidoManualmente', "true");
        buscarManifesto(idManifesto);
    }

    /**
     * Busca os dados relacionados ao manifesto.
     */
    function buscarManifesto(idManifesto) {
        var sdo = createServiceDataObject("lms.rnc.manterOcorrenciasNaoConformidadeAction.findManifestoByRNC",
            "resultado_buscarManifesto", {idManifesto: idManifesto});
        xmit({serviceDataObjects: [sdo]});
    }

    /**
     * Povoa os campos com os dados retornados da busca em manifesto
     */
    function resultado_buscarManifesto_cb(data, error) {
        if (error != undefined) {
            alert(error);
            return false;
        }
        if (data != undefined) {
            var idFilialUsuario = getNestedBeanPropertyValue(dataUsuario, "filial.idFilial");
            var idFilialOrigem = getNestedBeanPropertyValue(data, "0:filialByIdFilialDestino.idFilial");
            var idFilialDestino = getNestedBeanPropertyValue(data, "0:filialByIdFilialOrigem.idFilial");
            var tpEmpresaFilialDestino = getNestedBeanPropertyValue(data, "0:filialByIdFilialDestino.empresa.tpEmpresa.value");

            if (tpEmpresaFilialDestino == "P") {
                if (idFilialUsuario != idFilialOrigem) {
                    alert(lms_12004);
                    resetValue('manifesto.manifestoViagemNacional.idManifestoViagemNacional');
                    setFocus(document.getElementById('manifesto.manifestoViagemNacional.nrManifestoOrigem'));
                    return false;
                }
            } else {
                if (idFilialUsuario != idFilialOrigem && idFilialUsuario != idFilialDestino) {
                    alert(lms_12004);
                    resetValue('manifesto.manifestoViagemNacional.idManifestoViagemNacional');
                    setFocus(document.getElementById('manifesto.manifestoViagemNacional.nrManifestoOrigem'));
                    return false;
                }
            }

            setElementValue('controleCarga.idControleCarga', getNestedBeanPropertyValue(data, "0:controleCarga.idControleCarga"));
            setElementValue('controleCarga.nrControleCarga', getNestedBeanPropertyValue(data, "0:controleCarga.nrControleCarga"));
            setElementValue('controleCarga.filialByIdFilialOrigem.sgFilial', getNestedBeanPropertyValue(data, "0:controleCarga.filialByIdFilialOrigem.sgFilial"));

            setElementValue('controleCarga.meioTransporteByIdTransportado.nrFrota', getNestedBeanPropertyValue(data, "0:controleCarga.meioTransporteByIdTransportado.nrFrota"));
            setElementValue('controleCarga.meioTransporteByIdTransportado.nrIdentificador', getNestedBeanPropertyValue(data, "0:controleCarga.meioTransporteByIdTransportado.nrIdentificador"));

            setElementValue('controleCarga.meioTransporteByIdSemiRebocado.nrFrota', getNestedBeanPropertyValue(data, "0:controleCarga.meioTransporteByIdSemiRebocado.nrFrota"));
            setElementValue('controleCarga.meioTransporteByIdSemiRebocado.nrIdentificador', getNestedBeanPropertyValue(data, "0:controleCarga.meioTransporteByIdSemiRebocado.nrIdentificador"));

            format(document.getElementById("controleCarga.nrControleCarga"));
            desabilitaControleCarga(true);
        }
    }

    /**
     * Busca os dados relacionados ao Manifesto/Controle de cargas
     */
    function buscarManifestoControleCargas(idDoctoServico) {
        if (idDoctoServico != undefined && idDoctoServico != "") {
            var sdo = createServiceDataObject("lms.rnc.manterOcorrenciasNaoConformidadeAction.findManifestoComControleCargas",
                "resultado_buscarManifestoControleCargas", {idDoctoServico: idDoctoServico});
            xmit({serviceDataObjects: [sdo]});
        }
    }

    /**
     * Povoa os campos com os dados retornados da busca em Manifesto/Controle de cargas
     */
    function resultado_buscarManifestoControleCargas_cb(data, error) {
        if (error != undefined) {
            alert(error);
            return false;
        }
        resetValue('blManifestoPreenchidoManualmente');

        setElementValue('controleCarga.idControleCarga', getNestedBeanPropertyValue(data, "idControleCarga"));
        setElementValue('controleCarga.nrControleCarga', getNestedBeanPropertyValue(data, "nrControleCarga"));
        setElementValue('controleCarga.filialByIdFilialOrigem.sgFilial', getNestedBeanPropertyValue(data, "sgFilialControleCarga"));

        setElementValue('manifesto.idManifesto', getNestedBeanPropertyValue(data, "idManifesto"));
        setElementValue('manifesto.filialByIdFilialOrigem.sgFilial', getNestedBeanPropertyValue(data, "sgFilialManifesto"));
        setElementValue('manifesto.manifestoViagemNacional.nrManifestoOrigem', getNestedBeanPropertyValue(data, "nrManifesto"));
        setElementValue('manifesto.tpManifesto', getNestedBeanPropertyValue(data, "tpManifesto"));

        setElementValue('controleCarga.meioTransporteByIdTransportado.nrFrota', getNestedBeanPropertyValue(data, "veiculoFrota"));
        setElementValue('controleCarga.meioTransporteByIdTransportado.nrIdentificador', getNestedBeanPropertyValue(data, "veiculoPlaca"));

        setElementValue('controleCarga.meioTransporteByIdSemiRebocado.nrFrota', getNestedBeanPropertyValue(data, "semiReboqueFrota"));
        setElementValue('controleCarga.meioTransporteByIdSemiRebocado.nrIdentificador', getNestedBeanPropertyValue(data, "semiReboquePlaca"));

        if (getElementValue('manifesto.tpManifesto') != "") {
            changeDocumentWidget({
                documentDefinition: eval(getElementValue("manifesto.tpManifesto") + "_MANIFESTO_DOCUMENT_WIDGET_DEFINITION"),
                filialElement: document.getElementById("manifesto.filialByIdFilialOrigem.idFilial"),
                documentNumberElement: document.getElementById("manifesto.manifestoViagemNacional.idManifestoViagemNacional"),
                actionService: "lms.rnc.manterOcorrenciasNaoConformidadeAction"
            });
        }

        format(document.getElementById("controleCarga.nrControleCarga"));
        format(document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem"));
    }


    function manifestoNrManifestoOrigem_OnChange() {
        if (getElementValue("manifesto.manifestoViagemNacional.nrManifestoOrigem") == "") {
            resetaControleCarga();
            resetValue('manifesto.idManifesto');
        }
        return manifesto_manifestoViagemNacional_nrManifestoOrigemOnChangeHandler();
    }

    function enableManifestoManifestoViagemNacioal_cb(data) {
        var r = manifesto_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
        if (r == true) {
            setDisabled("manifesto.manifestoViagemNacional.idManifestoViagemNacional", false);
            setFocus(document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem"));
        }
    }

    function carregaManifesto(data) {
        resetValue('blManifestoPreenchidoManualmente');
        if (getNestedBeanPropertyValue(data, "manifesto.manifestoEntrega.nrManifestoEntrega") != "" && getNestedBeanPropertyValue(data, "manifesto.manifestoEntrega.nrManifestoEntrega") != undefined) {
            setElementValue('manifesto.tpManifesto', 'EN');
            setElementValue('manifesto.manifestoViagemNacional.idManifestoViagemNacional', getNestedBeanPropertyValue(data, "manifesto.idManifesto"));
            setElementValue('manifesto.manifestoViagemNacional.nrManifestoOrigem', getNestedBeanPropertyValue(data, "manifesto.manifestoEntrega.nrManifestoEntrega"));
            document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem").mask = "00000000";
            format(document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem"));
        }
        else if (getNestedBeanPropertyValue(data, "manifesto.manifestoViagemNacional.nrManifestoOrigem") != "" && getNestedBeanPropertyValue(data, "manifesto.manifestoViagemNacional.nrManifestoOrigem") != undefined) {
            setElementValue('manifesto.tpManifesto', 'VN');
            setElementValue('manifesto.manifestoViagemNacional.idManifestoViagemNacional', getNestedBeanPropertyValue(data, "manifesto.idManifesto"));
            setElementValue('manifesto.manifestoViagemNacional.nrManifestoOrigem', getNestedBeanPropertyValue(data, "manifesto.manifestoViagemNacional.nrManifestoOrigem"));
            document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem").mask = "00000000";
            format(document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem"));
        }
        else if (getNestedBeanPropertyValue(data, "manifesto.manifestoInternacional.nrManifestoInt") != "" && getNestedBeanPropertyValue(data, "manifesto.manifestoInternacional.nrManifestoInt") != undefined) {
            setElementValue('manifesto.tpManifesto', 'VI');
            setElementValue('manifesto.manifestoViagemNacional.idManifestoViagemNacional', getNestedBeanPropertyValue(data, "manifesto.idManifesto"));
            setElementValue('manifesto.manifestoViagemNacional.nrManifestoOrigem', getNestedBeanPropertyValue(data, "manifesto.manifestoInternacional.nrManifestoInt"));
            document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem").mask = "000000";
            format(document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem"));
        }
    }
    /************************* FIM - MANIFESTO *************************/
</script>