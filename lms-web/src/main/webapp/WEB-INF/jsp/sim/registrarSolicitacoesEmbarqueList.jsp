<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<script type="text/javascript">
<!--
	
	
	function changeComboDocumentoServico(field) {
		var flag = changeDocumentWidgetType(
					{documentTypeElement:field, 
					 filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
					 documentNumberElement:document.getElementById('doctoServico.idDoctoServico'), 
					 parentQualifier:'doctoServico',
					 documentGroup:'DOCTOSERVICE',
					 actionService:'lms.sim.registrarSolicitacoesEmbarqueAction'}
				  );
        var pms = document.getElementById("doctoServico.idDoctoServico").propertyMappings;

		pms[pms.length] = { modelProperty:"clienteByIdClienteDestinatario.idCliente", criteriaProperty:"destinatario.idCliente", inlineQuery:true};
		pms[pms.length] = { modelProperty:"clienteByIdClienteRemetente.idCliente", criteriaProperty:"remetente.idCliente", inlineQuery:true };
		pms[pms.length] = { modelProperty:"clienteByIdClienteDestinatario.pessoa.nrIdentificacao", criteriaProperty:"destinatario.pessoa.nrIdentificacao", inlineQuery:false};
		pms[pms.length] = { modelProperty:"clienteByIdClienteRemetente.pessoa.nrIdentificacao", criteriaProperty:"remetente.pessoa.nrIdentificacao", inlineQuery:false };
		pms[pms.length] = { modelProperty:"clienteByIdClienteDestinatario.pessoa.nmPessoa", criteriaProperty:"destinatario.pessoa.nmPessoa", inlineQuery:false};
		pms[pms.length] = { modelProperty:"clienteByIdClienteRemetente.pessoa.nmPessoa", criteriaProperty:"remetente.pessoa.nmPessoa", inlineQuery:false };

		pms[pms.length] = { modelProperty:"idDoctoServico", relatedProperty:"idDoctoServico" };
		pms[pms.length] = { modelProperty:"filialByIdFilialOrigem.pessoa.nmFantasia", relatedProperty:"doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia", blankFill:false};

		addElementChangeListener({e:document.getElementById("destinatario.idCliente"), changeListener:document.getElementById("doctoServico.idDoctoServico")});
		addElementChangeListener({e:document.getElementById("remetente.idCliente"), changeListener:document.getElementById("doctoServico.idDoctoServico")});

		return flag;
	}
	
	
	function changeFilialDoctoServico() {
		resetValue('idDoctoServico');
		return changeDocumentWidgetFilial(
					{filialElement:document.getElementById("doctoServico.filialByIdFilialOrigem.idFilial"), 
					 documentNumberElement:document.getElementById("doctoServico.idDoctoServico")}
			   );
	}
	
	//Implementação da filial do usuário logado como padrão
	var idFilial = null;
	var sgFilial = null;
	var nmFilial = null;
	var dtRegistroInicial = null;
	var dtRegistroFinal = null;
	
	
	function dataSession_cb(data) {
		idFilial = getNestedBeanPropertyValue(data,"filial.idFilial");
		sgFilial = getNestedBeanPropertyValue(data,"filial.sgFilial");
		nmFilial = getNestedBeanPropertyValue(data,"filial.pessoa.nmFantasia");
		dtRegistroInicial = getNestedBeanPropertyValue(data,"dtRegistroInicial");
		dtRegistroFinal =  getNestedBeanPropertyValue(data,"dtRegistroFinal");
		writeDataSession();
	}
	
	function writeDataSession() {
		if (!isPopup()) {
			if (idFilial != null &&
				sgFilial != null &&
				nmFilial != null) {
				setElementValue("filial.idFilial",idFilial);
				setElementValue("filial.sgFilial",sgFilial);
				setElementValue("filial.pessoa.nmFantasia",nmFilial);
			}
			if (dtRegistroInicial != null &&
				dtRegistroFinal != null) {
				setElementValue("dtRegistroInicial",setFormat(document.getElementById("dtRegistroInicial"),dtRegistroInicial));
				setElementValue("dtRegistroFinal",setFormat(document.getElementById("dtRegistroFinal"),dtRegistroFinal));
			}
		} else {
			setDisabled("filial.idFilial",false);
			getElement("filial.sgFilial").required = false;
		}
	}
	
	function isPopup() {
		var url = new URL(parent.location.href);
		
		return url.parameters["isPopup"] == 'true';
	}
	
	function initWindow(eventObj) {
		if(eventObj.name == "tab_load"){
			setaMasterLink();
		}
		if (eventObj.name == "cleanButton_click" || eventObj.name == "tab_click") {
			writeDataSession();
			
			if (document.getElementById("doctoServico.idDoctoServico").masterLink == true || document.getElementById("doctoServico.idDoctoServico").masterLink == "true"){
				setDisabled("doctoServico.idDoctoServico", true);
				setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
			} else {
				setDisabled("doctoServico.idDoctoServico", getElementValue("doctoServico.tpDocumentoServico") == "");
				setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", getElementValue("doctoServico.tpDocumentoServico") == "");
			}
		}
	}
	
	function validateTab() {
		if (validateTabScript(document.forms)) {
			if (getElementValue("idDoctoServico") == "" &&
					(getElementValue("dtRegistroInicial") == "" || getElementValue("dtRegistroFinal") == "") &&
					(getElementValue("dhRegistroEmbarqueInicial") == "" || getElementValue("dhRegistroEmbarqueFinal") == "")) {
				alert(i18NLabel.getLabel("LMS-10055"));
				return false;
			}
			return true;
		}
		return false;
    }
	
	
	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		var sdo = createServiceDataObject("lms.sim.registrarSolicitacoesEmbarqueAction.findDataSession","dataSession",null);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function setaMasterLink(){
		var url = new URL(parent.location.href);

		if(url.parameters["idDoctoServicoLocMerc"]!= undefined){
	 		setDisabled("filial.idFilial",true);
	 	}
	}
//-->
</script>
<adsm:window service="lms.sim.registrarSolicitacoesEmbarqueAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/sim/registrarSolicitacoesEmbarque">
		<adsm:i18nLabels>
			<adsm:include key="LMS-10055"/>
		</adsm:i18nLabels>
	
		<adsm:hidden property="doctoServico.idDoctoServico"/>
		<adsm:lookup label="filialSolicitante" dataType="text" size="3" maxLength="3" width="32%" property="filial"
				     service="lms.sim.registrarSolicitacoesEmbarqueAction.findLookupFilial" action="municipios/manterFiliais"
				      idProperty="idFilial" criteriaProperty="sgFilial" required="true" labelWidth="22%" disabled="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/> 
		</adsm:lookup>
		
		
		<adsm:combobox property="doctoServico.tpDocumentoServico" label="documentoServico" labelWidth="22%" optionLabelProperty="description"
					   service="lms.sim.registrarSolicitacoesEmbarqueAction.findTipoDocumentoServico" optionProperty="value" width="35%"
					   onchange="return changeComboDocumentoServico(this);">
			<adsm:lookup dataType="text" property="doctoServico.filialByIdFilialOrigem" idProperty="idFilial" criteriaProperty="sgFilial" 
						 service="" disabled="true" action="" size="3" maxLength="3" picker="false" popupLabel="pesquisarFilial"
						 onchange="return changeFilialDoctoServico();">
						 <adsm:hidden property="doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" serializable="false"/>
			</adsm:lookup>
			
			<adsm:lookup dataType="integer" property="doctoServico" idProperty="idDoctoServico" criteriaProperty="nrDoctoServico" 
						 service="" action="" size="12" maxLength="8" mask="00000000" serializable="true" disabled="true" popupLabel="pesquisarDocumentoServico"/>
			<adsm:hidden property="idDoctoServico"/>
	
		</adsm:combobox>

       	<adsm:lookup label="notaFiscal" action="/expedicao/consultarNotaFiscalCliente" mask="000000000" maxLength="9"
       	
					 service="lms.sim.registrarSolicitacoesEmbarqueAction.findLookupNotaFiscalCliente" criteriaProperty="nrNotaFiscal" 
					 dataType="integer" property="notaFiscalConhecimento" popupLabel="pesquisarNotaFiscal" criteriaSerializable="true"
					 idProperty="idDoctoServico" labelWidth="15%" width="28%" exactMatch="false" minLengthForAutoPopUpSearch="3">

				<adsm:propertyMapping criteriaProperty="remetente.idCliente" modelProperty="remetente.idCliente" inlineQuery="true"/>
				<adsm:propertyMapping criteriaProperty="remetente.pessoa.nrIdentificacao" modelProperty="remetente.pessoa.nrIdentificacao" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="remetente.pessoa.nmPessoa" modelProperty="remetente.pessoa.nmPessoa" inlineQuery="false"/>
			
				<adsm:propertyMapping criteriaProperty="destinatario.idCliente" modelProperty="destinatario.idCliente" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="destinatario.pessoa.nrIdentificacao" modelProperty="destinatario.pessoa.nrIdentificacao" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="destinatario.pessoa.nmPessoa" modelProperty="destinatario.pessoa.nmPessoa" inlineQuery="false"/>

		</adsm:lookup>
       
       
		<adsm:lookup dataType="text" property="remetente" idProperty="idCliente" width="78%" action="/vendas/manterDadosIdentificacao"
				criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" maxLength="20" labelWidth="22%"
				service="lms.sim.registrarSolicitacoesEmbarqueAction.findLookupCliente" label="remetente" size="17" exactMatch="true">
			<adsm:propertyMapping relatedProperty="remetente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="remetente.pessoa.nmPessoa" size="30" disabled="true" serializable="false"/>
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="destinatario" idProperty="idCliente" action="/vendas/manterDadosIdentificacao" width="78%"
				criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" maxLength="20" labelWidth="22%"
				service="lms.sim.registrarSolicitacoesEmbarqueAction.findLookupCliente" label="destinatario" size="17" exactMatch="true">
			<adsm:propertyMapping relatedProperty="destinatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="destinatario.pessoa.nmPessoa" size="30" disabled="true" serializable="false"/>
		</adsm:lookup>

		<adsm:range label="periodoLimiteEmbarque" labelWidth="22%" width="35%">
			<adsm:textbox dataType="JTDate" property="dhRegistroEmbarqueInicial"/>
			<adsm:textbox dataType="JTDate" property="dhRegistroEmbarqueFinal"/>
		</adsm:range>
		
		<adsm:range label="periodoRegistro" labelWidth="15%" width="28%" maxInterval="31">
			<adsm:textbox dataType="JTDate" property="dtRegistroInicial"/>
			<adsm:textbox dataType="JTDate" property="dtRegistroFinal"/>
		</adsm:range>
		

		
		<adsm:combobox domain="DM_SIM_NAO" property="isSomenteEntregasNaoEfetuadas" defaultValue="S" labelWidth="22%" label="somenteEntregasNaoEfetuadas" width="22%"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="RegistroPriorizacaoEmbarq"/>
			<adsm:resetButton/>
		</adsm:buttonBar> 
	</adsm:form>
	<adsm:grid idProperty="idRegistroPriorizacaoEmbarq" property="RegistroPriorizacaoEmbarq" unique="true" rows="8" 
			   scrollBars="horizontal" gridHeight="160">
		<adsm:gridColumn width="40" title="identificacao" property="remetente.pessoa.tpIdentificacao" align="left" isDomain="true"/>
		<adsm:gridColumn width="110" title="" property="remetente.pessoa.nrIdentificacaoFormatado" align="right"/>
		<adsm:gridColumn title="remetente" property="remetente.pessoa.nmPessoa" width="150"/>
		<adsm:gridColumn width="40" title="identificacao" property="destinatario.pessoa.tpIdentificacao" align="left" isDomain="true"/>
		<adsm:gridColumn width="110" title="" property="destinatario.pessoa.nrIdentificacaoFormatado" align="right"/>
		<adsm:gridColumn title="destinatario" property="destinatario.pessoa.nmPessoa" width="150"/>
		<adsm:gridColumn width="150" title="dataHoraRegistro" property="dhRegistro" dataType="JTDateTimeZone"/>
		<adsm:gridColumn width="80" title="status" property="tpStatus" dataType="text"/>
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid> 
</adsm:window>
			
