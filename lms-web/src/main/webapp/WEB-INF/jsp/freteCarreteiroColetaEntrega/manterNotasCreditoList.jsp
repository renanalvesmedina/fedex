<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
<!--  
	var tabGroup = null;
	
	function disableTabAnexos(disabled) {
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("anexo", disabled);
	}
	
	function validateFilter() {
		if (getElementValue("filial.idFilial") != "") {
			if (getElementValue("nrNotaCredito") == "" && 
				getElementValue("controleCargas.meioTransporte.idMeioTransporte") == "" && 
				getElementValue("controleCargas.proprietario.idProprietario") == "" &&
				(getElementValue("dhGeracaoInicial") == "" || getElementValue("dhGeracaoFinal") == "") &&
				(getElementValue("dhEmissaoInicial") == "" || getElementValue("dhEmissaoFinal") == "")) {
						alert(i18NLabel.getLabel("LMS-00013") +
								document.getElementById("nrNotaCredito").label + ", " + 
								document.getElementById("controleCargas.meioTransporte.idMeioTransporte").label + ", " +
								document.getElementById("controleCargas.proprietario.idProprietario").label + ", " +
								document.getElementById("dhGeracaoInicial").label + ", " +
								document.getElementById("dhEmissaoFinal").label + ".");
			}else  
				findButtonScript('NotaCredito', document.Lazy);
		}else
			findButtonScript('NotaCredito', document.Lazy);
	}
	
	function filialSession_cb(data) {
		var idFilial = getNestedBeanPropertyValue(data,"filial.idFilial");
		var sgFilial = getNestedBeanPropertyValue(data,"filial.sgFilial");
		var nmFilial = getNestedBeanPropertyValue(data,"filial.pessoa.nmFantasia");
		if (idFilial != null &&
			sgFilial != null &&
			nmFilial != null) {
			if (document.getElementById("filial.idFilial").masterLink != "true") {
				setElementValue("filial.idFilial",idFilial);
				setElementValue("filial.sgFilial",sgFilial);
				setElementValue("filial.pessoa.nmFantasia",nmFilial);
				setElementValue("reciboFreteCarreteiro2.filial.sgFilial",sgFilial);
			} else {
				setElementValue("reciboFreteCarreteiro2.filial.sgFilial",getElementValue("filial.sgFilial"));
			}
		}
		
		//LMS-3468
		if(sgFilial == "MTZ") {
			setDisabled('filial.idFilial',false);
		}
	}
	
	function myOnPageLoad() {
		var url = new URL(parent.location.href);
		if (url.parameters != undefined 
				&& url.parameters.idProcessoWorkflow != undefined 
				&& url.parameters.idProcessoWorkflow != '') {

			setDisabled("formList", true);
			getTabGroup(document).selectTab("cad", "tudoMenosNulo", true);
		} else {
			onPageLoad();
		}
	}
	
	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		
		if (getElementValue("filial.idFilial") == "") {
			loadFilialSessao();
		}
		
		setDisabled("findButton",false);
		
		tabGroup = getTabGroup(document);
	}
	
	function loadFilialSessao() {
		var sdo = createServiceDataObject("lms.fretecarreteirocoletaentrega.manterNotasCreditoAction.findFilialSession","filialSession",null);
		xmit({serviceDataObjects:[sdo]});
	}
	
	//VALIDACAO DA COMBO TABELA
	function changeTabela(element) { 
		if (element.value != "A"){
			setDisabled("tpTabela",true);
			document.getElementById("tpTabela").selectedIndex = 0;
		}else
			setDisabled("tpTabela",false);
	}
	
	function initWindow(eventObj) {
		if (tabGroup)
			tabGroup.setDisabledTab("cad",true);
		if (eventObj.name == "cleanButton_click") {
			loadFilialSessao();
		}
		setDisabled("findButton",false);
		
		if(eventObj.name == "tab_click"){
			disableTabAnexos(true);
		}
	}
	
	function rowClick() {
		if (tabGroup)
			tabGroup.setDisabledTab("cad",false);
	}
//-->
</script>
<adsm:window title="consultarNotasCredito" service="lms.fretecarreteirocoletaentrega.manterNotasCreditoAction" onPageLoadCallBack="pageLoad" onPageLoad="myOnPageLoad">
	<adsm:form id="formList" action="freteCarreteiroColetaEntrega/manterNotasCredito" height="142">
		<adsm:hidden property="isDhEmissao"/>
		<adsm:i18nLabels>
			<adsm:include key="LMS-00013"/>
			<adsm:include key="LMS-25021"/>
		</adsm:i18nLabels>
		<adsm:hidden property="idProcessoWorkflow" serializable="false"/>
		<adsm:hidden property="filial.empresa.tpEmpresa" value="M" serializable="false"/>
				
		<adsm:lookup label="filial" labelWidth="18%" dataType="text" size="3" maxLength="3" width="32%" action="municipios/manterFiliais"
				     service="lms.fretecarreteirocoletaentrega.manterNotasCreditoAction.findLookupFilial" property="filial"
				      idProperty="idFilial" criteriaProperty="sgFilial" required="true" disabled="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
			<adsm:propertyMapping modelProperty="sgFilial" relatedProperty="reciboFreteCarreteiro2.filial.sgFilial"/>
			
			<adsm:propertyMapping criteriaProperty="filial.empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>
			
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/> 
		</adsm:lookup>

		<adsm:textbox dataType="integer" mask="0000000000" property="nrNotaCredito" size="12" maxLength="50" width="32%" labelWidth="18%" label="numeroNota"/>
		
		<%--Lookup Recibo Frete Carreteiro ------------------------------------------------------------------------------------------------------%>
		<adsm:lookup dataType="text" property="reciboFreteCarreteiro2" idProperty="idReciboFreteCarreteiro" criteriaProperty="filial.sgFilial" disabled="true"
				service="lms.fretecarreteirocoletaentrega.manterNotasCreditoAction.findLookupRecibo" action="/freteCarreteiroColetaEntrega/manterRecibos"
				label="recibo" size="3" maxLength="3" labelWidth="18%" width="5%" cellStyle="vertical-align=bottom;" serializable="false" picker="false">
        </adsm:lookup>
        <adsm:lookup dataType="integer" property="reciboFreteCarreteiro"
				idProperty="idReciboFreteCarreteiro" criteriaProperty="nrReciboFreteCarreteiro"
				service="lms.fretecarreteirocoletaentrega.manterNotasCreditoAction.findLookupRecibo"
				action="/freteCarreteiroColetaEntrega/manterRecibos"  serializable="true" 
				size="10" maxLength="10" width="27%" cellStyle="vertical-align=bottom;" exactMatch="true" mask="0000000000">

        	<adsm:propertyMapping modelProperty="filial.idFilial" criteriaProperty="filial.idFilial"/>
        	<adsm:propertyMapping modelProperty="filial.sgFilial" criteriaProperty="filial.sgFilial" inlineQuery="false"/>       	        	
        	<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" criteriaProperty="filial.pessoa.nmFantasia" inlineQuery="false"/> 	

        	
        </adsm:lookup>
        
        
        <adsm:lookup dataType="text" property="controleCargas.meioTransporte2" idProperty="idMeioTransporte" cellStyle="vertical-align:bottom;"
				service="lms.fretecarreteirocoletaentrega.manterNotasCreditoAction.findLookupMeioTransporte" picker="false" maxLength="6"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="list" criteriaProperty="nrFrota"
				label="meioTransporte" labelWidth="18%" width="9%" size="8" serializable="false">
			<adsm:propertyMapping criteriaProperty="controleCargas.meioTransporte.nrIdentificador"
					modelProperty="nrIdentificador" />
			<adsm:propertyMapping relatedProperty="controleCargas.meioTransporte.idMeioTransporte"
					modelProperty="idMeioTransporte" />		
			<adsm:propertyMapping relatedProperty="controleCargas.meioTransporte.nrIdentificador"
					modelProperty="nrIdentificador" />

			<adsm:propertyMapping criteriaProperty="controleCargas.proprietario.idProprietario" modelProperty="proprietario.idProprietario"/> 
			<adsm:propertyMapping criteriaProperty="controleCargas.proprietario.pessoa.nrIdentificacao" modelProperty="proprietario.pessoa.nrIdentificacao" inlineQuery="false"/> 
			<adsm:propertyMapping criteriaProperty="controleCargas.proprietario.pessoa.nmPessoa" modelProperty="proprietario2.pessoa.nmPessoa" inlineQuery="false"/> 
			
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="controleCargas.meioTransporte" idProperty="idMeioTransporte"
				service="lms.fretecarreteirocoletaentrega.manterNotasCreditoAction.findLookupMeioTransporte" maxLength="25"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="list" criteriaProperty="nrIdentificador"
				width="21%" size="20" cellStyle="vertical-align:bottom;">
			<adsm:propertyMapping criteriaProperty="controleCargas.meioTransporte2.nrFrota"
					modelProperty="nrFrota" />
			<adsm:propertyMapping relatedProperty="controleCargas.meioTransporte2.idMeioTransporte"
					modelProperty="idMeioTransporte" />	
			<adsm:propertyMapping relatedProperty="controleCargas.meioTransporte2.nrFrota"
					modelProperty="nrFrota" />	
			<adsm:propertyMapping criteriaProperty="controleCargas.proprietario.idProprietario" modelProperty="proprietario.idProprietario"/> 
			<adsm:propertyMapping criteriaProperty="controleCargas.proprietario.pessoa.nrIdentificacao" modelProperty="proprietario.pessoa.nrIdentificacao" inlineQuery="false"/> 
			<adsm:propertyMapping criteriaProperty="controleCargas.proprietario.pessoa.nmPessoa" modelProperty="proprietario2.pessoa.nmPessoa" inlineQuery="false"/> 
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="controleCargas.proprietario" idProperty="idProprietario" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				service="lms.fretecarreteirocoletaentrega.manterNotasCreditoAction.findLookupProprietario"
				action="/contratacaoVeiculos/manterProprietarios" criteriaProperty="pessoa.nrIdentificacao"
				label="proprietario" labelWidth="18%" width="82%" size="20" maxLength="20">
			<adsm:propertyMapping relatedProperty="controleCargas.proprietario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="controleCargas.proprietario.pessoa.nmPessoa" serializable="false" size="30" disabled="true"/>
		</adsm:lookup>

		<adsm:combobox property="tpProprietario" label="tipoProprietario" domain="DM_TIPO_PROPRIETARIO" width="82%" labelWidth="18%" cellStyle="vertical-align:bottom;"/>

		<adsm:combobox property="tabela" label="tabela" onchange="comboboxChange({e:this});changeTabela(this);"  domain="DM_TIPO_TABELA_COLETA_ENTREGA" width="32%" labelWidth="18%" cellStyle="vertical-align:bottom;"/>
		
		<adsm:combobox property="tpTabela" label="tipoTabela"
					   service="lms.fretecarreteirocoletaentrega.manterTabelasFretesAgregadosAction.findComboTipoTabelaColetaEntrega" 
					   optionProperty="idTipoTabelaColetaEntrega" optionLabelProperty="dsTipoTabelaColetaEntrega" 
					    width="32%" labelWidth="18%" cellStyle="vertical-align:bottom;" boxWidth="160" disabled="true"/>

		<adsm:combobox property="tpSituacaoAprovacao" label="situacaoAprovacao" domain="DM_SITUACAO_APROVACAO_NOTA_CREDITO" width="32%" labelWidth="18%" cellStyle="vertical-align:bottom;"/>
		<adsm:combobox property="situacaoNotaCredito" label="situacao" domain="DM_SITUACAO_EMISSAO_NC" width="32%" labelWidth="18%" cellStyle="vertical-align:bottom;"/>
		
		<adsm:range label="periodoGeracao" labelWidth="18%" width="32%" maxInterval="30">
             <adsm:textbox dataType="JTDate" property="dhGeracaoInicial"/>
             <adsm:textbox dataType="JTDate" property="dhGeracaoFinal"/>
        </adsm:range>
		<adsm:range label="periodoEmissao" labelWidth="18%" width="32%" maxInterval="30">
             <adsm:textbox dataType="JTDate" property="dhEmissaoInicial"/>
             <adsm:textbox dataType="JTDate" property="dhEmissaoFinal"/>
        </adsm:range>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:button id="findButton" disabled="false" caption="consultar" onclick="validateFilter();"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form> 
	<adsm:grid property="NotaCredito" idProperty="idNotaCredito" selectionMode="none"
				gridHeight="124" rows="7" unique="true" scrollBars="horizontal" onRowClick="rowClick">
		<adsm:gridColumnGroup customSeparator=" " >
			<adsm:gridColumn title="numero" property="filial.sgFilial" width="55" />
			<adsm:gridColumn title="" property="nrNotaCredito" dataType="integer" mask="0000000000" width="30" />
		</adsm:gridColumnGroup>
		
		<adsm:gridColumnGroup customSeparator=" " >
			<adsm:gridColumn title="recibo" property="recibo.sgFilial" width="55" />
			<adsm:gridColumn title="" property="nrRecibo" dataType="integer" mask="00000000" width="30"/>
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn title="identificacao" property="controleCargas.proprietario.pessoa.tpIdentificacao" isDomain="true" width="50"/>
		<adsm:gridColumn title="" property="controleCargas.proprietario.pessoa.nrIdentificacaoFormatado" width="130" align="right"/>
		<adsm:gridColumn title="proprietario" property="controleCargas.proprietario.pessoa.nmPessoa" width="150" />
		
		<adsm:gridColumn title="meioTransporte" property="controleCargas.meioTransporte2.nrFrota" width="60" />
		<adsm:gridColumn title="" property="controleCargas.meioTransporte.nrIdentificador" width="70" />
		
		<adsm:gridColumn title="tabela" property="notasCreditoParcelas.parcelaTabelaCe.tabelaColetaEntrega.tpRegistro" isDomain="true" width="100"/>
		<adsm:gridColumn title="tipoTabela" property="notasCreditoParcelas.parcelaTabelaCe.tabelaColetaEntrega.tipoTabelaColetaEntrega.dsTipoTabelaColetaEntrega" width="100"/>
		<adsm:gridColumn title="dataGeracao" property="dhGeracao" width="150" dataType="JTDateTimeZone"/>
		<adsm:gridColumn title="dataEmissao" property="dhEmissao" width="150" dataType="JTDateTimeZone"/>
		<adsm:gridColumnGroup customSeparator=" " >
			<adsm:gridColumn title="valor" property="sgMoeda" width="25" />
			<adsm:gridColumn title="" property="dsSimbolo" dataType="text" width="25" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="" property="vlNotaCredito" width="100" align="right" dataType="currency"/>
		<adsm:gridColumn title="situacao" property="tpSituacaoAprovacao" width="100" isDomain="true"/>
		<adsm:gridColumn title="eventos" property="eventosNotaCredito" image="/images/popup.gif" openPopup="true" 
 			link="freteCarreteiroColetaEntrega/consultarEventosNotaCredito.do?cmd=main" popupDimension="790,520" width="100" align="center" linkIdProperty="idNotaCredito"/> 
		<adsm:buttonBar>
			<adsm:button caption="fechar" id="btnFechar" onclick="self.close();" buttonType="closeButton" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window> 
<script>
	isLookup = window.dialogArguments && window.dialogArguments.window;
	if (isLookup) {
		document.getElementById('btnFechar').property = ".closeButton";
		setDisabled('btnFechar',false);
	} else {
		setVisibility('btnFechar', false); 
	}	
</script>