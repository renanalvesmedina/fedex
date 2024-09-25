<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	/**
	 * Como são usados divs, é necessário a função para gerar 100 colunas dentro da table no div.
	 */
	function geraColunas() {
		colunas = '<table class="Form" cellpadding="0" cellspacing="0" width="98%><tr>';
		for (i = 0 ; i < 33 ; i++) {
			colunas += '<td><img src="lms/images/spacer.gif" width="7px" height="1px"></td>';
			colunas += '<td><img src="lms/images/spacer.gif" width="8px" height="1px"></td><td><img src="lms/images/spacer.gif" width="8px" height="1px"></td>';
		}
		colunas += '<td><img src="lms/images/spacer.gif" width="7px" height="1px"></td></tr>';
		return colunas;
	}
	
	function pageLoadCustom() {
   		onPageLoad();
 		initPessoaWidget({tpTipoElement:document.getElementById("pessoa.tpPessoa")
   				   ,tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao")
      			   ,numberElement:document.getElementById("pessoa.idPessoa")});
	}
	
</script>
<adsm:window service="lms.entrega.emitirManifestoAction" onPageLoad="pageLoadCustom" >
	<adsm:form action="/entrega/emitirManifesto" idProperty="idManifesto"
			service="lms.entrega.emitirManifestoAction.findByIdCustom" onDataLoadCallBack="dataLoadCustom" >	
	
		<adsm:i18nLabels>
			<adsm:include key="LMS-09031"/>
		</adsm:i18nLabels>
			
	<td colspan="100" >

	<div id="principal" style="display:;border:none;">
	<script>
		document.write(geraColunas());
	</script>
		<adsm:hidden property="filial.idFilial" serializable="false" />
		<adsm:textbox dataType="text" property="filial.sgFilial"
				label="filial" labelWidth="18%" width="32%" size="3" disabled="true" serializable="false" >
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false" />
		</adsm:textbox>
		
		<adsm:hidden property="tpStatusManifesto.value" />
		<adsm:hidden property="tpManifestoEntrega.value" />
		<adsm:textbox dataType="text" property="tpManifestoEntrega.description"
				label="tipoPreManifesto" labelWidth="18%" width="32%" size="25" disabled="true" serializable="false" />
		
		<adsm:textbox dataType="integer" property="controleCarga.rotaColetaEntrega.nrRota"
				label="rota" labelWidth="18%" width="82%" size="3" disabled="true" serializable="false" >
			<adsm:textbox dataType="text" property="controleCarga.rotaColetaEntrega.dsRota"
					size="30" disabled="true" serializable="false" />
		</adsm:textbox>
		
		<adsm:textbox dataType="text" property="filialNrPreManifesto.sgFilial"
				label="numeroPreManifesto" size="3"
				labelWidth="18%" width="32%" disabled="true" serializable="false" >
			<adsm:textbox dataType="integer" property="nrPreManifesto"
					size="10" disabled="true" serializable="false" mask="0000000000" />
		</adsm:textbox>
		 
		<adsm:hidden property="idManifestoEntrega" />
		<adsm:textbox dataType="text" property="manifestoEntrega.filial.sgFilial"
				label="manifestoEntrega" size="3"
				labelWidth="18%" width="32%" disabled="true" serializable="false" >
			<adsm:textbox dataType="integer" property="manifestoEntrega.nrManifestoEntrega"
					size="8" disabled="true" serializable="false" mask="00000000" />
		</adsm:textbox>
		
		<adsm:textbox dataType="JTDateTimeZone" property="dhEmissao" 
				label="dataHoraDeEmissao" labelWidth="18%" width="32%" disabled="true"/>
		<adsm:textbox dataType="text" property="tpStatusManifesto.description"
				label="situacao" size="30" labelWidth="18%" width="32%" disabled="true"/>		
		
		<adsm:hidden property="solicitacaoRetirada.idSolicitacaoRetirada" />
		<adsm:hidden property="solicitacaoRetirada.filial.idFilial" />
		<adsm:hidden property="solicitacaoRetirada.filial.nmFilial" />
        <adsm:textbox dataType="text" property="solicitacaoRetirada.filial.sgFilial"
        		label="solicitacaoRetirada" size="3" cellStyle="vertical-Align:bottom"
        		labelWidth="18%" width="82%" disabled="true" serializable="false" >
        	<adsm:textbox dataType="integer" property="solicitacaoRetirada.nrSolicitacaoRetirada"
	        		size="8" cellStyle="vertical-Align:bottom"
	        		disabled="true" serializable="false" mask="00000000" />        		
        </adsm:textbox>
		
		<adsm:textarea maxLength="500" property="obManifestoEntrega"
				label="observacao" labelWidth="18%" width="82%" columns="108"/>

		<adsm:section caption="configuracoesDocumento" />
		<adsm:combobox property="filial.tpOrdemDoc" domain="DM_TP_ORDEM_DOC"
				label="ordemExibicaoDocumentos" labelWidth="18%" width="82%" renderOptions="true"/>				
	</table>
	</div>
	
	<div id="infoSolicitacao" style="display:none;border:none;">
	<script>
		document.write(geraColunas());
	</script>
		<adsm:section caption="informacoesSolicitacaoRetirada"/>
				
		<adsm:complement label="identificacao" labelWidth="18%" width="32%" required="true" >
			<adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.cad" />
			<adsm:lookup definition="IDENTIFICACAO_PESSOA" 
					service="lms.entrega.emitirManifestoAction.validateIdentificacao"
					onDataLoadCallBack="pessoaCallback" onchange="return pessoaChange(this);" />
		</adsm:complement>
		<adsm:textbox dataType="text" property="pessoa.nmPessoa" 
				label="nome" maxLength="60" size="30" 
				required="true" labelWidth="18%" width="32%" />

		<adsm:textbox dataType="text" property="nrDdd"
				label="telefone" maxLength="5" size="5"
				labelWidth="18%" width="32%" >
			<adsm:textbox dataType="text" property="nrTelefone" maxLength="10" size="10" required="true" />
		</adsm:textbox>
		
		<adsm:textbox dataType="text" property="pessoa.nrRg"
				label="rg" maxLength="12" size="20" 
				labelWidth="18%" width="32%" required="true" />
		
		<adsm:hidden property="pessoa.tpPessoa" />
		<adsm:hidden property="pessoa.dsEmail" />
		
		<adsm:lookup property="meioTransporte" idProperty="id" criteriaProperty="nrPlaca"
		    	service="lms.entrega.emitirManifestoAction.findLookupMeioTransporte"
		    	action="f" picker="false" onDataLoadCallBack="dataLoadMeioTransporte"
		    	dataType="text" maxLength="7" size="7"
		    	label="placa" labelWidth="18%" width="32%"
		    	allowInvalidCriteriaValue="true" required="true" >
		</adsm:lookup>

		<adsm:lookup property="semiReboque" idProperty="id" criteriaProperty="nrPlaca"
		    	service="lms.entrega.emitirManifestoAction.findLookupMeioTransporte"
		    	action="f" picker="false" onDataLoadCallBack="dataLoadSemiReboque"
		    	dataType="text" maxLength="7" size="7"
		    	label="semiReboque" labelWidth="18%" width="32%"
				allowInvalidCriteriaValue="true"  >
		</adsm:lookup>

	</table>
	</div>

		<adsm:hidden property="whichReport" />
		<adsm:hidden property="tpManifesto" />
		<adsm:hidden property="blReemissaoManifesto" />

		<adsm:hidden property="controleCarga.idControleCarga" />
		<adsm:hidden property="controleCarga.nrControleCarga" />
		<adsm:hidden property="tpControleCargaValor" />
		<adsm:hidden property="filialControleCarga.idFilial" />
		<adsm:hidden property="filialControleCarga.sgFilial" />
		
		<adsm:buttonBar lines="2" >
			<adsm:button caption="solicitacaoRetirada" id="btnSolicitacaoRetirada"
					action="/sim/registrarSolicitacoesRetirada" cmd="main" boxWidth="160" >
				<adsm:linkProperty src="solicitacaoRetirada.idSolicitacaoRetirada" target="idSolicitacaoRetirada" />
				<adsm:linkProperty src="solicitacaoRetirada.nrSolicitacaoRetirada" target="nrSolicitacaoRetirada" />
				<adsm:linkProperty src="solicitacaoRetirada.filial.idFilial" target="filial.idFilial" />
				<adsm:linkProperty src="solicitacaoRetirada.filial.sgFilial" target="filial.sgFilial" />
				<adsm:linkProperty src="solicitacaoRetirada.filial.nmFilial" target="filial.pessoa.nmFantasia" />
				
				<adsm:linkProperty src="idManifestoEntrega" target="manifestoEntrega.idManifestoEntrega" />
				<adsm:linkProperty src="manifestoEntrega.nrManifestoEntrega" target="manifestoEntrega.nrManifestoEntrega" />
				<adsm:linkProperty src="filial.idFilial" target="manifestoEntrega.filial.idFilial" />
				<adsm:linkProperty src="filial.sgFilial" target="manifestoEntrega.filial.sgFilial" />
				
				<adsm:linkProperty src="nrPreManifesto" target="manifesto.nrManifesto" />				
			</adsm:button>
			
			<adsm:storeButton caption="emitir" id="btnEmitir" service="lms.entrega.emitirManifestoAction.storeValidateEmissao"
					callbackProperty="emitirClick" breakBefore="true" />

			<adsm:button buttonType="storeButton" caption="reemitirCobranca" id="btnReemitirCobranca"
					service="lms.entrega.emitirManifestoAction.validateReemissaoCobranca"
					callbackProperty="reemitirCobrancaClick" boxWidth="140" />

			<adsm:button id="btnEmitirControleCarga" caption="emitirControleCargas" boxWidth="180"
					onclick="exibeEmitirControleCargas();" />
		</adsm:buttonBar>
		
	</td></tr></table>
	</adsm:form>
</adsm:window>

<script type="text/javascript">
<!--

	var blEmitirManifesto = undefined;
	var blEmitirBoleto = undefined;
	var form_aux = document.Lazy;

	function emitirClick_cb(data,error,key) {
		if (error != undefined) {
			alert(error);
			setFocusOnNewButton();
			return false;
		} else {
			onDataLoad_cb(data,error);
			
			setDisabledCustom(true);
			
			var eventObj = {name:"storeButton"};
			// verifica se tem a tab se existir adiciona ela como origem do evento.
			var tab = getTab(document, false);
			if (tab) {
				eventObj.src = tab.tabGroup.selectedTab;
			}
			// initWindowScript definido em 'elements.js'
	    	initWindowScript(document.parentWindow, eventObj);
			
			blEmitirManifesto = getNestedBeanPropertyValue(data,"blEmitirManifesto");
			blEmitirBoleto = getNestedBeanPropertyValue(data,"blEmitirBoleto");
			
			manipulaBotoes(getElementValue("tpManifestoEntrega.value"),"ME");
			setFocus("btnEmitir",false);
			
			emitirManifesto();
		}
	}

	function reemitirCobrancaClick_cb(data,error,key) {
		if (error != undefined) {
			alert(error);
			setFocusOnNewButton();
			return false;
		} else {
			blEmitirBoleto = getNestedBeanPropertyValue(data,"blEmitirBoleto");

			setElementValue("tpManifesto",'E');
			setElementValue("blReemissaoManifesto",'S');
			reemitirBoleto();
		}
	}
	
	function reemitirBoleto() {
		if (blEmitirBoleto == "true") {
			reportButtonScript('lms.contasreceber.emitirBoletosManifestoAction', 'openPdf', document.Lazy);
		}
	}
	
	//####################################################################################################################################
	// Relatórios são chamados nos callbacks para evitar problemas com sincronismo das chamadas.
	//####################################################################################################################################
	function emitirManifesto() {
		_serviceDataObjects = new Array(); 
		var data = buildFormBeanFromForm(document.Lazy);
		
		addServiceDataObject(createServiceDataObject("lms.entrega.emitirManifestoAction.executeManifesto", "emitirManifesto", data)); 
		xmit(false);
	}

	function emitirManifesto_cb(data, error) {
		if (error != null) {
			// exibe BusinessException's
			alert(error);
		} else if (data.idPce != undefined) {
			// alerta PCE
			showModalDialog('vendas/alertaPce.do?idVersaoDescritivoPce=' + data.idPce + '&cmd=pop',window,
					'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:310px;');
		} else if (data.strFile != undefined && data.strFile != "") {
			// emite relatorio e chama a emissão do boleto.
			openReport(data.strFile, ''); // define o nome da janela como vazio para permitir abrir mais de uma janela
			emitirBoleto();
		}
	}

	function emitirBoleto() {
		if (blEmitirBoleto == "true") {
			setElementValue("tpManifesto",'E');
			setElementValue("blReemissaoManifesto",'N');
			reportButtonScript('lms.contasreceber.emitirBoletosManifestoAction', 'openPdf', document.Lazy);
		}
	}

	function openPdf_cb(strFile, error) {
		openPdfCustom_cb(strFile, error);
	}

	function emitirReport(which,callback) {
		setElementValue("whichReport",which);
		if (callback == null) {
			callback = "openPdfCustom";
		}
		executeReportWithCallback('lms.entrega.emitirManifestoAction.execute', callback, document.Lazy);
	}
	
	function openPdfCustom_cb(strFile, error){
		if (error == null) {
			openReport(strFile._value, ''); // definido em reports.js, define como vazio o nome para abrir mais de uma janela
		} else {
			alert(error+'');
		}
	}
	
	//####################################################################################################################################

	function manipulaBotoes(tpManifestoEntrega,tpStatusManifesto) {
		setDisabled("btnSolicitacaoRetirada",
				(tpManifestoEntrega != "PR" && tpManifestoEntrega != "CR") ||
				getElementValue("solicitacaoRetirada.idSolicitacaoRetirada") == "");

		setDisabled("btnReemitirCobranca",tpStatusManifesto != 'ME');
		setDisabled("btnEmitirControleCarga",getElementValue("controleCarga.idControleCarga") == "");
	}

	function pessoaCallback_cb(data,error) {
		if (error != undefined) {
			alert(error);
			resetValue("pessoa.nrIdentificacao");
			resetValue("pessoa.nrRg");
			resetValue("nrTelefone");
			resetValue("nrDdd");
			setFocus(document.getElementById('pessoa.nrIdentificacao'));
			return false;
		}
		var retorno = pessoa_nrIdentificacao_exactMatch_cb(data);
		var idPessoa = getNestedBeanPropertyValue(data,':0.idPessoa');
		if (idPessoa != undefined && idPessoa != "") {
			setElementValue("pessoa.nrRg", getNestedBeanPropertyValue(data,':0.pessoa.nrRg'));
			setElementValue("nrTelefone", getNestedBeanPropertyValue(data,":0.nrTelefone"));
			setElementValue("nrDdd", getNestedBeanPropertyValue(data,":0.nrDdd"));
			setFocus("meioTransporte.nrPlaca");
		}		
		return retorno;
	}
	
	function pessoaChange(elem) {
		if (elem.value == "") {
			resetValue("pessoa.nrRg");
			resetValue("nrTelefone");
			resetValue("nrDdd");
		}
		return pessoa_nrIdentificacaoOnChangeHandler();
	}
	
	function dataLoadMeioTransporte_cb(data) {
	
		if (data[0].length=="0"){			
			data[0].nrPlaca=getElementValue("meioTransporte.nrPlaca");
		}			
	
		var flag = meioTransporte_nrPlaca_exactMatch_cb(data);

		if (getElementValue("meioTransporte.nrPlaca") == "") {
			document.getElementById("meioTransporte.id").value = "";
		} else {
			document.getElementById("meioTransporte.id").value = -1;
		}
		
		if (data != undefined && data.length != 1) {
			setElementValue("meioTransporte.nrPlaca",getElementValue("meioTransporte.nrPlaca").toUpperCase());
		}
		
		return flag;
    }

	function dataLoadSemiReboque_cb(data) {
		var flag = semiReboque_nrPlaca_exactMatch_cb(data);
		
		if (data != undefined && data.length != 1) {
			setElementValue("semiReboque.nrPlaca",getElementValue("semiReboque.nrPlaca").toUpperCase());
		}
		
		return flag;
    }

	function dataLoadCustom_cb(data,error) {
		onDataLoad_cb(data,error);
		onDataLoadCallbackPessoaWidget({tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao"), 
				numberElement:document.getElementById("pessoa.nrIdentificacao")});
		
		var tpManifestoEntrega = getNestedBeanPropertyValue(data,"tpManifestoEntrega.value");
		showInfoSolicitacao(tpManifestoEntrega == "PR" || tpManifestoEntrega == "CR");
		var tpStatusManifesto = getNestedBeanPropertyValue(data,"tpStatusManifesto.value");
		setDisabledCustom(tpStatusManifesto == "ME");
		
		// Id não existe em meio de transporte. Colocado só para 'enganar' o framework.
		setElementValue("meioTransporte.id",-1);
		
		// só habilita o botão emitir se o controle de carga tiver status 'CC'
		if(data.controleCarga != null){
			setDisabled("btnEmitir",data.controleCarga.tpStatusControleCarga != "PO");	
	}
	}
	
	function showInfoSolicitacao(valueBoolean) {
		var value = valueBoolean ? "" : "none";
		var valueSrt = valueBoolean ? "true" : "false";
		
		document.getElementById("infoSolicitacao").style.display = value;
		
		document.getElementById("pessoa.tpIdentificacao").required = valueSrt;
		document.getElementById("pessoa.nrIdentificacao").required = valueSrt;
		document.getElementById("pessoa.nmPessoa").required = valueSrt;
		document.getElementById("pessoa.nrRg").required = valueSrt;
		document.getElementById("nrTelefone").required = valueSrt;
		document.getElementById("meioTransporte.id").required = valueSrt;
		document.getElementById("meioTransporte.nrPlaca").required = valueSrt;
		
		document.getElementById("pessoa.tpIdentificacao").serializable = valueBoolean;
		document.getElementById("pessoa.nrIdentificacao").serializable = valueBoolean;
		document.getElementById("pessoa.nmPessoa").serializable = valueBoolean;
		document.getElementById("pessoa.nrRg").serializable = valueBoolean;
		document.getElementById("nrTelefone").serializable = valueBoolean;
		document.getElementById("meioTransporte.id").serializable = valueBoolean;
		document.getElementById("meioTransporte.nrPlaca").serializable = valueBoolean;
		document.getElementById("semiReboque.nrPlaca").serializable = valueBoolean;
	}

	function setDisabledCustom(valueBoolean) {
		setDisabled("obManifestoEntrega",valueBoolean);
		setDisabled("pessoa.tpIdentificacao",valueBoolean);
		setDisabled("pessoa.nrIdentificacao",valueBoolean);
		setDisabled("pessoa.nmPessoa",valueBoolean);
		setDisabled("pessoa.nrRg",valueBoolean);
		setDisabled("meioTransporte.id",valueBoolean);
		setDisabled("semiReboque.id",valueBoolean);
		setDisabled("nrDdd",valueBoolean);
		setDisabled("nrTelefone",valueBoolean);
	}

	function exibeEmitirControleCargas() {
		var parametros = 
			'&idControleCarga=' + getElementValue("controleCarga.idControleCarga") +
			'&nrControleCarga=' + getElementValue("controleCarga.nrControleCarga") +
			'&idFilialOrigem=' + getElementValue("filialControleCarga.idFilial") +
			'&sgFilialOrigem=' + getElementValue("filialControleCarga.sgFilial");
		if (getElementValue("tpControleCargaValor") == "V") {
			parent.parent.redirectPage("carregamento/emitirControleCargas.do?cmd=main" + parametros);
		} else {
			parent.parent.redirectPage("coleta/emitirControleCargasColetaEntrega.do?cmd=main" + parametros);
		}
	}

//-->
</script>