<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
function carregaPagina() {
	setMasterLink(document, true);
	onPageLoad();
}
</script>
<adsm:window service="lms.entrega.consultarManifestosEntregaAction" onPageLoad="carregaPagina" onPageLoadCallBack="pageLoadCustom" >
	 
	<adsm:i18nLabels>
		<adsm:include key="LMS-00013"/>
		<adsm:include key="LMS-09088"/> 
	</adsm:i18nLabels>
	
	<adsm:form action="/entrega/manterMemorandosInternosResposta" height="104" >
		<adsm:hidden property="idProcessoWorkflow" serializable="false" />
		<adsm:hidden property="tpAcessoFilial" serializable="true" value="A"/>
		<adsm:lookup dataType="text" property="filial" idProperty="idFilial" criteriaProperty="sgFilial"  
				service="lms.entrega.consultarManifestosEntregaAction.findLookupFilial"
				action="/municipios/manterFiliais"
				onchange="return filialChange(this);"
				label="filial" labelWidth="18%" width="32%" size="3" maxLength="3" required="true" exactMatch="true" >
				
			<adsm:propertyMapping relatedProperty="manifesto.controleCarga.filialByIdFilialOrigem.sgFilial" modelProperty="sgFilial" />
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:propertyMapping criteriaProperty="tpAcessoFilial" modelProperty="tpAcesso" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" disabled="true" serializable="false" size="30" />
		</adsm:lookup>

		<adsm:textbox dataType="integer" property="nrManifestoEntrega"
				label="numeroManifesto" labelWidth="18%" width="32%" maxLength="8" size="8" mask="00000000" />

		<adsm:combobox property="tpSituacaoManifesto" 
					   service="lms.entrega.consultarManifestosEntregaAction.findSituacaoManifesto"
					   optionProperty="value" optionLabelProperty="description"
				       label="situacaoManifesto" labelWidth="18%" width="32%" />

		<adsm:combobox property="tpManifestoEntrega" domain="DM_TIPO_MANIFESTO_ENTREGA"
				label="tipoDeManifesto" labelWidth="18%" width="32%" renderOptions="true"/>
		
		<adsm:lookup property="equipe" dataType="text" idProperty="idEquipe" criteriaProperty="dsEquipe"
				action="/carregamento/manterEquipes" service="lms.entrega.consultarManifestosEntregaAction.findLookupEquipe"
				label="equipe" labelWidth="18%" width="32%" maxLength="50" size="38"
				minLengthForAutoPopUpSearch="3" exactMatch="false" >
			<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial" />
            <adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial" inlineQuery="false" />
            <adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia"
            		modelProperty="filial.pessoa.nmFantasia" inlineQuery="false" />
		</adsm:lookup>		
		
		<adsm:lookup dataType="integer" property="manifesto.controleCarga.rotaColetaEntrega" 
				idProperty="idRotaColetaEntrega" criteriaProperty="nrRota"
				service="lms.entrega.consultarManifestosEntregaAction.findLookupRotaColetaEntrega"
				action="/municipios/manterRotaColetaEntrega" labelWidth="18%" width="32%"
				label="rotaColetaEntrega" size="3" maxLength="3" cellStyle="vertical-Align:bottom" >
			<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial" />
            <adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial" inlineQuery="false" />
            <adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia"
            		modelProperty="filial.pessoa.nmFantasia" inlineQuery="false" />
            
            <adsm:propertyMapping relatedProperty="rotaColetaEntrega.dsRota" modelProperty="dsRota" />
	        <adsm:textbox dataType="text" property="rotaColetaEntrega.dsRota" size="30" disabled="true" serializable="false" />            
        </adsm:lookup>
		
		<adsm:combobox property="regiaoColetaEntregaFil.idRegiaoColetaEntregaFil"
				optionLabelProperty="dsRegiaoColetaEntregaFil" optionProperty="idRegiaoColetaEntregaFil"
				service="lms.entrega.consultarManifestosEntregaAction.findComboRegiao" boxWidth="232"
				label="regiaoColetaEntrega" labelWidth="18%" width="32%" >					   
			<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial" />
		</adsm:combobox>
		
		<adsm:lookup dataType="text" property="meioTransporte2" idProperty="idMeioTransporte"
				service="lms.entrega.consultarManifestosEntregaAction.findLookupMeioTransporte" picker="false"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="list" criteriaProperty="nrFrota"
				label="meioTransporte" labelWidth="18%"
				width="32%" size="8" serializable="false" maxLength="6" cellStyle="vertical-Align:bottom" >
			<adsm:propertyMapping criteriaProperty="meioTransporte.nrIdentificador"
					modelProperty="nrIdentificador" />
			<adsm:propertyMapping relatedProperty="meioTransporte.idMeioTransporte"
					modelProperty="idMeioTransporte" />
			<adsm:propertyMapping relatedProperty="meioTransporte.nrIdentificador"
					modelProperty="nrIdentificador" />
					
			<adsm:lookup dataType="text" property="meioTransporte" idProperty="idMeioTransporte"
					service="lms.entrega.consultarManifestosEntregaAction.findLookupMeioTransporte"
					action="/contratacaoVeiculos/manterMeiosTransporte" cmd="list" criteriaProperty="nrIdentificador"
					picker="true" maxLength="25" size="20" cellStyle="vertical-Align:bottom" >
				<adsm:propertyMapping criteriaProperty="meioTransporte2.nrFrota"
						modelProperty="nrFrota" />
				<adsm:propertyMapping relatedProperty="meioTransporte2.idMeioTransporte"
						modelProperty="idMeioTransporte" />
				<adsm:propertyMapping relatedProperty="meioTransporte2.nrFrota"
						modelProperty="nrFrota" />
			</adsm:lookup>
					
		</adsm:lookup>

		<adsm:hidden property="tpControleCarga" value="C" />
		<adsm:textbox dataType="text" property="manifesto.controleCarga.filialByIdFilialOrigem.sgFilial"
				label="controleCargas" labelWidth="18%" width="32%" size="3" disabled="true" serializable="false" >
			<adsm:lookup dataType="integer"
					property="manifesto.controleCarga" idProperty="idControleCarga" criteriaProperty="nrControleCarga"
					service="lms.entrega.consultarManifestosEntregaAction.findLookupControleCarga" 
					action="/carregamento/manterControleCargas" popupLabel="pesquisarControleCarga"
					maxLength="8" size="8" mask="00000000" disabled="false" >
				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" 
						criteriaProperty="filial.idFilial"/>
				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" 
						criteriaProperty="filial.sgFilial" inlineQuery="false" />
				<adsm:propertyMapping modelProperty="meioTransporteByIdTransportado.idMeioTransporte" 
						criteriaProperty="meioTransporte.idMeioTransporte"/>
				<adsm:propertyMapping modelProperty="meioTransporteByIdTransportado2.nrFrota" 
						criteriaProperty="meioTransporte2.nrFrota" inlineQuery="false" />
				<adsm:propertyMapping modelProperty="meioTransporteByIdTransportado.nrIdentificador" 
						criteriaProperty="meioTransporte.nrIdentificador" inlineQuery="false" />
				<adsm:propertyMapping modelProperty="rotaColetaEntrega.idRotaColetaEntrega" 
						criteriaProperty="manifesto.controleCarga.rotaColetaEntrega.idRotaColetaEntrega"/>
				<adsm:propertyMapping modelProperty="rotaColetaEntrega.nrRota" 
						criteriaProperty="manifesto.controleCarga.rotaColetaEntrega.nrRota" inlineQuery="false" />
				<adsm:propertyMapping modelProperty="rotaColetaEntrega.dsRota" 
						criteriaProperty="rotaColetaEntrega.dsRota" inlineQuery="false" />
				<adsm:propertyMapping modelProperty="tpControleCarga" 
						criteriaProperty="tpControleCarga"/>
			</adsm:lookup>
		</adsm:textbox>

		<adsm:hidden property="idDoctoServico" />
		<adsm:combobox property="doctoServico.tpDocumentoServico"
				label="documentoServico" labelWidth="18%" width="32%"
				service="lms.entrega.consultarManifestosEntregaAction.findTipoDocumentoServico"
				optionProperty="value" optionLabelProperty="description"
				onchange="return changeTpDoctoServico(this);" renderOptions="true" > 

			<adsm:lookup dataType="text"
					property="doctoServico.filialByIdFilialOrigem" idProperty="idFilial" criteriaProperty="sgFilial"
					service="" action="" disabled="true"
					exactMatch="true" popupLabel="pesquisarFilial"
					size="3" maxLength="3" picker="false"
					onDataLoadCallBack="filialDoctoServicoDataLoad" onPopupSetValue="filialDoctoServicoPopup"
					onchange="return changeFilialDoctoServico();" />
			
			<adsm:lookup dataType="integer"
					property="doctoServico" idProperty="idDoctoServico" criteriaProperty="nrDoctoServico"
					service="" action="" popupLabel="pesquisarDocumentoServico"
					size="10" maxLength="8" serializable="true" disabled="true" mask="00000000" />
			
			<adsm:hidden property="doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" serializable="false"/>
		</adsm:combobox>

		
		<adsm:range label="periodoEmissao" labelWidth="18%" width="32%" maxInterval="30">
			<adsm:textbox dataType="JTDate" property="dhEmisssaoManifestoInicial" />
			<adsm:textbox dataType="JTDate" property="dhEmisssaoManifestoFinal" />
		</adsm:range>
		<adsm:range label="periodoFechamento" labelWidth="18%" width="32%" maxInterval="30" >
			<adsm:textbox dataType="JTDate" property="dhFechamentoInicial"/>
			<adsm:textbox dataType="JTDate" property="dhFechamentoFinal"/>
		</adsm:range>

		<adsm:buttonBar freeLayout="true" >
			<adsm:findButton callbackProperty="manifestoEntrega" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="manifestoEntrega" idProperty="idManifestoEntrega"
			service="lms.entrega.consultarManifestosEntregaAction.findPaginatedConsultaManifesto"
			rowCountService="lms.entrega.consultarManifestosEntregaAction.getRowCountConsultaManifesto"
	 		selectionMode="none" unique="true" rows="8" gridHeight="173" scrollBars="horizontal" onRowClick="onRowClickDef" >
		
		<adsm:gridColumnGroup customSeparator=" " >
			<adsm:gridColumn title="manifestoEntrega" property="sgFilialManifesto" width="70" />
			<adsm:gridColumn title="" property="nrManifestoEntrega" dataType="integer" mask="00000000" width="70" />
		</adsm:gridColumnGroup>	
		
		<adsm:gridColumn title="tipoManifesto" property="tpManifesto" width="130" isDomain="true" />
		<adsm:gridColumn width="150" title="dataHoraDeEmissao" property="dhEmissao" dataType="JTDateTimeZone" />
		
		<adsm:gridColumnGroup separatorType="CONTROLE_CARGA" >
			<adsm:gridColumn title="controleCarga" property="sgFilialOrigem" width="60" />
			<adsm:gridColumn title="" property="nrControleCarga" width="60" dataType="integer" mask="00000000"/>
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn title="meioTransporte" property="nrFrota" width="60"  />
		<adsm:gridColumn title="" property="nrIdentificador" width="70" />
		
		<adsm:gridColumn title="semiReboque" property="nrFrotaSemi" width="60"  />
		<adsm:gridColumn title="" property="nrIdentificadorSemi" width="70" />
				
		<adsm:gridColumn width="150" title="equipe" property="dsEquipe" />
		
		<adsm:gridColumnGroup customSeparator=" - " >
			<adsm:gridColumn title="rotaColetaEntrega" property="nrRota" width="90" dataType="integer" mask="0000" align="left" />
			<adsm:gridColumn title="" property="dsRota" width="90" dataType="text" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn width="180" title="regiaoColetaEntrega" property="dsRegiaoColetaEntregaFil" />
		
		<adsm:gridColumn width="140" title="dataHoraSaida" property="dhSaidaColetaEntrega" dataType="JTDateTimeZone" />		
		<adsm:gridColumn width="140" title="dataHoraRetorno" property="dhChegadaColetaEntrega" dataType="JTDateTimeZone" />		
		<adsm:gridColumn width="160" title="dataHoraFechamento" property="dhFechamento" dataType="JTDateTimeZone" />		
		<adsm:gridColumn width="170" title="quantidadeDocumentos" property="qtDocumentos" dataType="integer" />
		<adsm:gridColumn width="150" title="situacao" property="tpStatusManifesto" isDomain="true" />	

		<adsm:buttonBar>
			<adsm:button caption="fechar" id="btnFechar" onclick="self.close();" buttonType="closeButton" />	
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script type="text/javascript">
<!--
	document.getElementById("tpControleCarga").masterLink = true;

	function onRowClickDef() {
		var tabGroup = getTabGroup(this.document);
		if (tabGroup != null) {
			tabGroup.setDisabledTab('cad', false);
		}
		return true;
	}

	function initWindow(event) {
		setVisibility('btnFechar', false);
		if (event.name == "tab_click") {
			var tabGroup = getTabGroup(this.document);
			tabGroup.setDisabledTab("cad", true);
			tabGroup.setDisabledTab('doc', true);
		} else if (event.name == "cleanButton_click") {
			populateFilial();			
			setDisabled("doctoServico.filialByIdFilialOrigem.idFilial",true);
			setDisabled("doctoServico.idDoctoServico",true);			
		}
	}


	// Change da lookup de filial do controle de carga
	function filialChange(elem) {
		setDisabled("manifesto.controleCarga.idControleCarga",elem.value == "");
		return filial_sgFilialOnChangeHandler();
	}

	// Funções relativas à lookup de Documento de Serviço	
	function filialDoctoServicoDataLoad_cb(data) {
		var blRetorno = doctoServico_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
		if (blRetorno) {
			setDisabled("doctoServico.idDoctoServico", false);
			setFocus(document.getElementById("doctoServico.nrDoctoServico"));
		}
		return blRetorno;
	}

	function filialDoctoServicoPopup(data) {
	   setDisabled("doctoServico.idDoctoServico", false);
	   setFocus(document.getElementById("doctoServico.nrDoctoServico"));
	   return true;
	}

	function changeTpDoctoServico(field) {
		resetValue('idDoctoServico');
		var flag = changeDocumentWidgetType(
					{documentTypeElement:field, 
					 filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
					 documentNumberElement:document.getElementById('doctoServico.idDoctoServico'), 
					 parentQualifier:'doctoServico',
					 documentGroup:'DOCTOSERVICE',
					 actionService:'lms.entrega.consultarManifestosEntregaAction'}
				  );
		document.getElementById("doctoServico.filialByIdFilialOrigem.idFilial").service = "lms.entrega.consultarManifestosEntregaAction.findLookupFilial";
		var pms = document.getElementById("doctoServico.idDoctoServico").propertyMappings;
		pms[pms.length] = {modelProperty:"idDoctoServico", relatedProperty:"idDoctoServico"};
		return flag;
	}

	function changeFilialDoctoServico() {
		resetValue('idDoctoServico');
		return changeDocumentWidgetFilial(
					{filialElement:document.getElementById("doctoServico.filialByIdFilialOrigem.idFilial"), 
					 documentNumberElement:document.getElementById("doctoServico.idDoctoServico")}
			   );
	}

	function validateTab() {
		if (validateTabScript(document.forms)){
			var rangeEmissao = (getElementValue("dhEmisssaoManifestoInicial") != '' && getElementValue("dhEmisssaoManifestoFinal") != '');
			var rangeFechamento = (getElementValue("dhFechamentoInicial") != '' && getElementValue("dhFechamentoFinal") != '');
			if (!rangeEmissao
					&& !rangeFechamento
					&& getElementValue("manifesto.controleCarga.idControleCarga") == ''
					&& getElementValue("nrManifestoEntrega") == ''
					&& getElementValue("idDoctoServico") == '') {
				alert(i18NLabel.getLabel("LMS-00013") + i18NLabel.getLabel("LMS-09088"));
			} else {
				return true;
			}
		}
		return false;		
	}

	function pageLoadCustom_cb(data) {
		onPageLoad_cb(data);
		if (document.getElementById("filial.idFilial").masterLink == "true") {
			document.getElementById("filial.sgFilial").masterLink = true;
			document.getElementById("filial.pessoa.nmFantasia").masterLink = true;
		}
		if(getElementValue("idProcessoWorkflow")!= ""){
			getTabGroup(this.document).selectTab("cad");
		}else{
			var isLookup = window.dialogArguments && window.dialogArguments.window;
			if (!isLookup){
				findFilialUsuarioLogado();	
			}
			
		}
		mostraEscondeBotaoFechar();
	}

	function mostraEscondeBotaoFechar(){
		var isLookup = window.dialogArguments && window.dialogArguments.window;
		if (isLookup) {
			setDisabled('btnFechar',false);
		} else {
			setVisibility('btnFechar', false);
		}
	}

	function findFilialUsuarioLogado() {
		if (getElementValue("filial.sgFilial") != "") {
			idFilial = getElementValue("filial.idFilial");
			sgFilial = getElementValue("filial.sgFilial");
			nmFilial = getElementValue("filial.pessoa.nmFantasia");
			populateFilial();
			return;
		}
		var sdo = createServiceDataObject("lms.entrega.consultarManifestosEntregaAction.findFilialUsuarioLogado",
				"findFilialUsuarioLogado",undefined);
		xmit({serviceDataObjects:[sdo]});
	}

	var idFilial = undefined;
	var sgFilial = undefined;
	var nmFilial = undefined;

	function findFilialUsuarioLogado_cb(data,error) {
		idFilial = getNestedBeanPropertyValue(data,"idFilial");
		sgFilial = getNestedBeanPropertyValue(data,"sgFilial");
		nmFilial = getNestedBeanPropertyValue(data,"pessoa.nmFantasia");
		populateFilial();
	}

	function populateFilial() {
		setElementValue("manifesto.controleCarga.filialByIdFilialOrigem.sgFilial",sgFilial);
		setDisabled("manifesto.controleCarga.idControleCarga",false);
		if (document.getElementById("filial.idFilial").masterLink != "true") {
			setElementValue("filial.idFilial",idFilial);
			setElementValue("filial.sgFilial",sgFilial);
			setElementValue("filial.pessoa.nmFantasia",nmFilial);
			notifyElementListeners({e:document.getElementById("filial.idFilial")});
		}
		setFocusOnFirstFocusableField();
	}
//-->
</script>