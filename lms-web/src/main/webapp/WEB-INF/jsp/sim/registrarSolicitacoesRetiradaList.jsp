<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="registrarSolicitacoesRetirada" onPageLoadCallBack="pageLoad" service="lms.sim.registrarSolicitacoesRetiradaAction">
	<adsm:form action="/municipios/manterRegionais" idProperty="idSolicitacaoRetirada"  height="129" >
		
		<adsm:i18nLabels>
        	<adsm:include key="LMS-10048"/>
        	<adsm:include key="filialSolicitante"/>
        	<adsm:include key="filialRetirada"/>
        	<adsm:include key="destinatario"/>
        	<adsm:include key="remetente"/>
        	<adsm:include key="consignatario"/>
        	<adsm:include key="periodoSolicitacao"/>

   		</adsm:i18nLabels>
		
		
		<adsm:hidden property="idProcessoWorkflow"/>
		<adsm:hidden property="idDoctoServico" />
		
		<adsm:hidden property="idFilialLogado" />
		<adsm:hidden property="sgFilialLogado" />
		<adsm:hidden property="nmFilialLogado" />

		<adsm:hidden property="dhSolicitacaoInicialPadrao" />
		<adsm:hidden property="dhSolicitacaoFinalPadrao" />
		
		<adsm:hidden property="blBloqueado" value="N"/>
		<adsm:hidden property="doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" />
		
		<adsm:lookup property="filial" cellStyle="vertical-align: bottom;" idProperty="idFilial" criteriaProperty="sgFilial" maxLength="3"
				service="lms.sim.registrarSolicitacoesRetiradaAction.findLookupFilial" dataType="text" label="filialSolicitante" size="3"
				action="/municipios/manterFiliais" labelWidth="15%" width="35%" minLengthForAutoPopUpSearch="3"
				exactMatch="false" style="width:45px" disabled="true" >
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text" cellStyle="vertical-align: bottom;" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>			
		</adsm:lookup>
	
		<adsm:textbox dataType="integer" mask="00000000" cellStyle="vertical-align: bottom;" property="nrSolicitacaoRetirada" label="numeroSolicitacaoRetirada" maxLength="8" size="10" labelWidth="15%" width="25%"/>
					
		<adsm:combobox property="doctoServico.tpDocumentoServico" 
					   label="documentoServico" labelWidth="15%" width="35%" 
					   service="lms.sim.registrarSolicitacoesRetiradaAction.findTipoDocumentoServico" 
					   optionProperty="value" optionLabelProperty="description"
					   onchange="resetValue('doctoServico.idDoctoServico');changeDocumentWidgetType({
						   documentTypeElement:this, 
						   filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
						   documentNumberElement:document.getElementById('doctoServico.idDoctoServico'), 
						   parentQualifier:'doctoServico', 
						   documentGroup:'DOCTOSERVICE',
						   actionService:'lms.sim.registrarSolicitacoesRetiradaAction'
					   });">
			
			<adsm:lookup dataType="text"
						 property="doctoServico.filialByIdFilialOrigem"
					 	 idProperty="idFilial" criteriaProperty="sgFilial" 
						 service="" 
						 disabled="true"
						 action="" 
						 size="3" maxLength="3" picker="false" 
						  onchange="resetValue('doctoServico.idDoctoServico'); return changeDocumentWidgetFilial({
							 filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
							 documentNumberElement:document.getElementById('doctoServico.idDoctoServico')
						  }); "
						  popupLabel="pesquisarFilial"/>
			
			<adsm:lookup dataType="integer" 
						 property="doctoServico" 
						 popupLabel="pesquisarDocumentoServico"
						 idProperty="idDoctoServico" criteriaProperty="nrDoctoServico" 
						 service="" 
						 action="" 
						 onDataLoadCallBack="documentoServicoDataLoad" onPopupSetValue="documentoServicoPopup"
						 size="12" maxLength="8" mask="00000000" serializable="true" disabled="true" >
				<adsm:propertyMapping criteriaProperty="blBloqueado" modelProperty="blBloqueado"/>	
			</adsm:lookup>		 
		</adsm:combobox>
		 
		<adsm:lookup property="filialRetirada" cellStyle="vertical-align: bottom;" idProperty="idFilial" criteriaProperty="sgFilial" maxLength="3"
				service="lms.sim.registrarSolicitacoesRetiradaAction.findLookupFilial" dataType="text" label="filialRetirada" size="3"
				action="/municipios/manterFiliais" labelWidth="9%" width="41%" minLengthForAutoPopUpSearch="3"
				exactMatch="false" style="width:45px" disabled="false" >
			<adsm:propertyMapping relatedProperty="filialRetirada.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text" cellStyle="vertical-align: bottom;" property="filialRetirada.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>			
		</adsm:lookup>	

		<adsm:lookup dataType="text" property="remetente" idProperty="idCliente" 
				criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				service="lms.sim.registrarSolicitacoesRetiradaAction.findLookupCliente" 
				label="remetente" size="17" maxLength="20" labelWidth="15%" width="45%"
				action="/vendas/manterDadosIdentificacao" exactMatch="true" >
			<adsm:propertyMapping relatedProperty="remetente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />			
			<adsm:textbox dataType="text" property="remetente.pessoa.nmPessoa" size="30" disabled="true" serializable="false" />
		</adsm:lookup>	
		
		<adsm:lookup label="notaFiscal" action="/expedicao/consultarNotaFiscalCliente" 
					 service="lms.sim.registrarSolicitacoesRetiradaAction.findLookupNotaFiscal"
					 dataType="integer" property="notaFiscalCliente" popupLabel="pesquisarNotaFiscal" 
					 idProperty="idNotaFiscalConhecimento" labelWidth="15%" width="25%" criteriaProperty="nrNotaFiscal" 
					 mask="000000000" maxLength="30" exactMatch="false" minLengthForAutoPopUpSearch="3"
					 onDataLoadCallBack="notaFiscalLoad">

				<adsm:propertyMapping criteriaProperty="remetente.idCliente" modelProperty="remetente.idCliente" inlineQuery="true"/>
				<adsm:propertyMapping criteriaProperty="remetente.pessoa.nrIdentificacao" modelProperty="remetente.pessoa.nrIdentificacao" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="remetente.pessoa.nmPessoa" modelProperty="remetente.pessoa.nmPessoa" inlineQuery="false"/>
			
				<adsm:propertyMapping criteriaProperty="destinatario.idCliente" modelProperty="destinatario.idCliente" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="destinatario.pessoa.nrIdentificacao" modelProperty="destinatario.pessoa.nrIdentificacao" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="destinatario.pessoa.nmPessoa" modelProperty="destinatario.pessoa.nmPessoa" inlineQuery="false"/>
				
				<adsm:propertyMapping relatedProperty="doctoServico.tpDocumentoServico" modelProperty="tpDocumentoServico.value" blankFill="false"/>
				<adsm:propertyMapping relatedProperty="doctoServico.filialByIdFilialOrigem.sgFilial" modelProperty="sgFilialOrigem" blankFill="false" />
				<adsm:propertyMapping relatedProperty="doctoServico.filialByIdFilialOrigem.idFilial" modelProperty="idFilialOrigem" blankFill="false"/>
				<adsm:propertyMapping relatedProperty="doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="nmFantasiaOrigem" blankFill="false"/>
				<adsm:propertyMapping relatedProperty="doctoServico.idDoctoServico" modelProperty="idDoctoServico" blankFill="false"/>
				<adsm:propertyMapping relatedProperty="idDoctoServico" modelProperty="idDoctoServico" blankFill="false"/>
				<adsm:propertyMapping relatedProperty="doctoServico.nrDoctoServico" modelProperty="nrDoctoServico" blankFill="false" />
				
				<adsm:propertyMapping criteriaProperty="doctoServico.tpDocumentoServico" modelProperty="doctoServico.tpDocumentoServico" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="doctoServico.filialByIdFilialOrigem.sgFilial" modelProperty="doctoServico.filialByIdFilialOrigem.sgFilial" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="doctoServico.filialByIdFilialOrigem.idFilial" modelProperty="doctoServico.filialByIdFilialOrigem.idFilial" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="doctoServico.idDoctoServico" modelProperty="doctoServico.idDoctoServico" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="doctoServico.idDoctoServico" modelProperty="idDoctoServico" />
				<adsm:propertyMapping criteriaProperty="doctoServico.nrDoctoServico" modelProperty="doctoServico.nrDoctoServico" inlineQuery="false"/>
		</adsm:lookup>
		               
		<adsm:lookup dataType="text" property="destinatario" idProperty="idCliente" 
				criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				service="lms.sim.registrarSolicitacoesRetiradaAction.findLookupCliente" 
				label="destinatario" size="17" maxLength="20" labelWidth="15%" width="85%"
				action="/vendas/manterDadosIdentificacao" exactMatch="true" >
			<adsm:propertyMapping relatedProperty="destinatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />			
			<adsm:textbox dataType="text" property="destinatario.pessoa.nmPessoa" size="30" disabled="true" serializable="false" />
		</adsm:lookup>	       
		
		<adsm:lookup dataType="text" property="consignatario" idProperty="idCliente" 
				criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				service="lms.sim.registrarSolicitacoesRetiradaAction.findLookupCliente" 
				label="consignatario" size="17" maxLength="20" labelWidth="15%" width="85%"
				action="/vendas/manterDadosIdentificacao" exactMatch="true" >
			<adsm:propertyMapping relatedProperty="consignatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />			
			<adsm:textbox dataType="text" property="consignatario.pessoa.nmPessoa" size="30" disabled="true" serializable="false" />
		</adsm:lookup>	
        
		<adsm:lookup property="filialDestino" idProperty="idFilial" criteriaProperty="sgFilial" maxLength="3"
				service="lms.sim.registrarSolicitacoesRetiradaAction.findLookupFilial" dataType="text" label="filialDestino" size="3"
				action="/municipios/manterFiliais" labelWidth="15%" width="35%" minLengthForAutoPopUpSearch="3"
				exactMatch="false" style="width:45px" disabled="false" >
			<adsm:propertyMapping relatedProperty="filialDestino.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text" property="filialDestino.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>			
		</adsm:lookup>
			
		<adsm:range label="periodoSolicitacao" labelWidth="15%" width="35%" maxInterval="15">
			<adsm:textbox dataType="JTDate" property="dhSolicitacaoInicial"/>
			<adsm:textbox dataType="JTDate" property="dhSolicitacaoFinal"/>
		</adsm:range>
							
		<adsm:buttonBar freeLayout="true" >
			<adsm:button id="findButton" caption="consultar" onclick="consulta()" buttonType="findButton" disabled="false"/>
			<adsm:resetButton />		
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid idProperty="idSolicitacaoRetirada" rowCountService="lms.sim.registrarSolicitacoesRetiradaAction.getRowCountCustom" 
				service="lms.sim.registrarSolicitacoesRetiradaAction.findPaginatedCustom" selectionMode="none"
				property="solicitacaoRetirada" unique="true" rows="7" scrollBars="horizontal" gridHeight="150">

		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn width="75" title="numeroSolicitacao" property="sgFilial"/>
			<adsm:gridColumn width="75" title="" property="nrSolicitacaoRetirada" dataType="integer" mask="00000000"/>
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn width="60" title="situacao" property="tpSituacao" align="left" isDomain="true"/>
		<adsm:gridColumn width="60" title="identificacao" property="tpIdentificacaoRemetente" isDomain="true" align="left"/>
		<adsm:gridColumn width="125" title="" property="nrIdentificacaoRemetente" align="right"/>		
		<adsm:gridColumn width="150" title="remetente" property="nmRemetente"/>	
		
		<adsm:gridColumn width="60" title="identificacao" property="tpIdentificacaoDestinatario" isDomain="true" align="left"/>
		<adsm:gridColumn width="125" title="" property="nrIdentificacaoDestinatario" align="right"/>
		<adsm:gridColumn width="150" title="destinatario" property="nmDestinatario"/>
		
		<adsm:gridColumnGroup separatorType="FILIAL">
			<adsm:gridColumn width="75" title="filialRetirada" property="sgFilialRetirada"/>
			<adsm:gridColumn width="75" title="" property="nmFantasiaRetirada"/>
		</adsm:gridColumnGroup>		
			
		<adsm:gridColumn width="100" title="liberadoPor" property="tpRegistroLiberacao" isDomain="true"/>
		<adsm:gridColumn width="220" title="nomeResponsavelLiberacao" property="nmResponsavelAutorizacao"/>
		<adsm:gridColumn width="150" title="funcao" property="dsFuncaoResponsavelAutoriza"/>
		<adsm:gridColumn width="100" title="telefone" property="nrTelefoneAutorizador"/>
		
		<adsm:gridColumn width="60" title="identificacao" property="tpIdentificacao" isDomain="true" align="left"/>
		<adsm:gridColumn width="125" title="" property="nrCnpj" align="right"/>
		<adsm:gridColumn width="160" title="nomeRetirante" property="nmRetirante"/>		
		<adsm:gridColumn width="115" title="rg" property="nrRg" align="right"/>
		
		<adsm:gridColumn width="210" title="dataHoraPrevistaRetirada" property="dhPrevistaRetirada" dataType="JTDateTimeZone" align="center"/>
		<adsm:gridColumn width="150" title="dataHoraSolicitacao" property="dhSolicitacao" dataType="JTDateTimeZone" align="center"/>
		
		<adsm:gridColumnGroup separatorType="MANIFESTO">
			<adsm:gridColumn width="70" title="manifestoEntrega" property="sgFilialManifesto"/>
			<adsm:gridColumn width="70"  title="" property="nrManifestoEntrega" mask="00000000" dataType="integer" align="right"/>	
		</adsm:gridColumnGroup>	

		<adsm:gridColumn width="160" title="dataHoraDeEmissao" property="dhEmissao" dataType="JTDateTimeZone" align="center"/>	
		<adsm:gridColumn width="160" title="dataHoraFechamento" property="dhFechamento" dataType="JTDateTimeZone" align="center"/>				
		
		<adsm:buttonBar>
			<adsm:button caption="fechar" id="btnFechar" onclick="self.close();" buttonType="closeButton" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>
	//	var isLookup = false;
	isLookup = window.dialogArguments && window.dialogArguments.window;
	if (isLookup) {
		document.getElementById('btnFechar').property = ".closeButton";
		setDisabled('btnFechar',false);
	} else {
		setVisibility('btnFechar', false);
	}	
	
	
	document.getElementById("idFilialLogado").masterLink = "true";
	document.getElementById("sgFilialLogado").masterLink = "true";
	document.getElementById("nmFilialLogado").masterLink = "true";

	document.getElementById("dhSolicitacaoInicialPadrao").masterLink = "true";
	document.getElementById("dhSolicitacaoFinalPadrao").masterLink = "true";
		
	function consulta() {
	
		if (getElementValue("doctoServico.idDoctoServico") == '' &&
			getElementValue("nrSolicitacaoRetirada") == '' &&
			(getElementValue("dhSolicitacaoInicial") == '' || getElementValue("dhSolicitacaoFinal") == '')
			  )
			{
				var msg = getMessage(i18NLabel.getLabel("LMS-10048"));
				alert(msg);
				return;
			}				
				
		findButtonScript('solicitacaoRetirada', document.getElementById("form_idSolicitacaoRetirada"));	
	}
	
	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		if (!isPopup()) {
			getFilialUsuario();
		}
	}
	
	function isPopup() {
		var url = new URL(parent.location.href);
		
		return url.parameters["isPopup"] == 'true';
	}
	
	function initWindow(evt) {
		if(evt.name == "tab_load" || evt.name == 'cleanButton_click'){
			estadoNovo();
		}
		
		if (evt.name == "tab_click") {
			setDisabledTabCustom("doc", true);
			if (getElementValue("doctoServico.tpDocumentoServico") == "") {
				setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
				setDisabled("doctoServico.idDoctoServico", true);
			}
		}
		
		setDisabled("findButton", false);
    }    
    
    
    function setDisabledTabCustom(tabName,disabled) {
        var tabGroup = getTabGroup(this.document);
        tabGroup.setDisabledTab(tabName, disabled);
    }
    
    function estadoNovo() {
		newButtonScript();			
		// Carrega filial do usuário logado.
		// Se já foi consultado uma vez não é necessário chamar a service novamente.
		if (getElementValue("idFilialLogado") != '') {
			setaValoresFilial();
		} 
		
		setDisabled("doctoServico.idDoctoServico", true);
		setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
	}
	
	function setaValoresFilial() {
		if (!document.getElementById("filial.idFilial").masterLink) {
			setElementValue("filial.idFilial", getElementValue("idFilialLogado"));
			setElementValue("filial.sgFilial", getElementValue("sgFilialLogado"));
			setElementValue("filial.pessoa.nmFantasia", getElementValue("nmFilialLogado"));
			
			setElementValue("dhSolicitacaoInicial", getElementValue("dhSolicitacaoInicialPadrao"));
			setElementValue("dhSolicitacaoFinal", getElementValue("dhSolicitacaoFinalPadrao"));			
<%--			
			setElementValue("dhSolicitacaoInicial", setFormat(document.getElementById("dhSolicitacaoInicial"),getElementValue("dhSolicitacaoInicialPadrao")));
			setElementValue("dhSolicitacaoFinal", setFormat(document.getElementById("dhSolicitacaoFinal"),getElementValue("dhSolicitacaoFinalPadrao")));			
--%>			
		}
	}
	
	function getFilialUsuario() {
		var sdo = createServiceDataObject("lms.sim.registrarSolicitacoesRetiradaAction.findFilialUsuarioLogado","getFilialCallBack",null);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function getFilialCallBack_cb(data,error) {

		if (error != undefined) {
			alert(error);
			return false;
		}
		
		if (data != undefined) {
			setElementValue("idFilialLogado", getNestedBeanPropertyValue(data,"idFilial"));
			setElementValue("sgFilialLogado", getNestedBeanPropertyValue(data,"sgFilial"));
			setElementValue("nmFilialLogado", getNestedBeanPropertyValue(data,"pessoa.nmFantasia"));

			setElementValue("dhSolicitacaoInicialPadrao", setFormat(document.getElementById("dhSolicitacaoInicial"),getNestedBeanPropertyValue(data,"dhSolicitacaoInicial")));
			setElementValue("dhSolicitacaoFinalPadrao", setFormat(document.getElementById("dhSolicitacaoFinal"),getNestedBeanPropertyValue(data,"dhSolicitacaoFinal")));
			
			setaValoresFilial();
		}
	}

	function documentoServicoDataLoad_cb(data) {
		doctoServico_nrDoctoServico_exactMatch_cb(data);
		
		if (data != undefined && data.length > 0) {
			setElementValue("idDoctoServico", getNestedBeanPropertyValue(data,":0.idDoctoServico"));
		}
	}
	
	function documentoServicoPopup(data) {
		if (data != undefined) {
			setElementValue("idDoctoServico", getNestedBeanPropertyValue(data,"idDoctoServico"));
		}
	}
	
	function notaFiscalLoad_cb(data, exception) {
		notaFiscalCliente_nrNotaFiscal_exactMatch_cb(data);
		
		if (data != undefined) {
			setDisabled("doctoServico.idDoctoServico", false);
			setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", false);
		}
	}
</script>