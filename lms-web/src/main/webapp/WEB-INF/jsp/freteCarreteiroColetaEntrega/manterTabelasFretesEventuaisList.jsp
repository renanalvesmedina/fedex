 <%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
<!--
	var tabGroup = null;
	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		tabGroup = getTabGroup(document);
		var sdo = createServiceDataObject("lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.findDataSession","dataSession",null);
		xmit({serviceDataObjects:[sdo]});
	}
	 
	function validateTab() {
		if (validateTabScript(document.Lazy)) {
			if (getElementValue("meioTransporteRodoviario.idMeioTransporte") != "" ||
				getElementValue("proprietario.idProprietario") != "" ||
				getElementValue("solicitacaoContratacao.idSolicitacaoContratacao") != "" ||
				getElementValue("dtVigenciaInicial") != "" ||
				getElementValue("vigentes") != "")
					return true;
			else{
				alert("LMS-00013 - " + i18NLabel.getLabel("LMS-00013") +
						document.getElementById("meioTransporteRodoviario.idMeioTransporte").label + ", " +
						document.getElementById("proprietario.idProprietario").label + ", " +
						document.getElementById("solicitacaoContratacao.idSolicitacaoContratacao").label + ", " +
						i18NLabel.getLabel("vigenciaInicial") + " " +
						i18NLabel.getLabel("ou") + " " +
					    document.getElementById("vigentes").label + ".");
				return false;
			}
		}
		return false;
	}
	
	
	
	//Implementação da filial do usuário logado como padrão
	var idFilial = null;
	var sgFilial = null;
	var nmFilial = null;
	
	function dataSession_cb(data) {
		idFilial = getNestedBeanPropertyValue(data,"filial.idFilial");
		sgFilial = getNestedBeanPropertyValue(data,"filial.sgFilial");
		nmFilial = getNestedBeanPropertyValue(data,"filial.pessoa.nmFantasia");
		writeDataSession();
	}
	
	function writeDataSession() {
		if (idFilial != null &&
			sgFilial != null &&
			nmFilial != null) {
			setElementValue("filial.idFilial",idFilial);
			setElementValue("filial.sgFilial",sgFilial);
			setElementValue("filial.pessoa.nmFantasia",nmFilial);
			setElementValue("solicitacaoContratacao.filial.sgFilial",sgFilial);
		}
	}
	
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click") {
			writeDataSession();
		}
		if (eventObj.name == "tab_click") {
			tabGroup.setDisabledTab("cad",true);
		}
	}
	function gridRowClick() {
		tabGroup.setDisabledTab("cad",false);
		return true;
	}
	
//--> 
</script>
<adsm:window title="manterTabelasFretesEventuais" service="lms.fretecarreteirocoletaentrega.manterTabelasFretesEventuaisAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/freteCarreteiroColetaEntrega/manterTabelasFretesEventuais" id="Lazy">
		<adsm:hidden property="idProcessoWorkflow" serializable="false"/>
		<adsm:hidden property="tipoMeioTransporte.tpMeioTransporte" value="R" serializable="false"/>
		<adsm:hidden property="tpEmpresa" value="M" serializable="false"/>
		<adsm:hidden property="idTabelaColetaEntrega"/>
		
		<adsm:i18nLabels>
			<adsm:include key="LMS-00013"/>
			<adsm:include key="vigenciaInicial"/>
			<adsm:include key="ou"/>
		</adsm:i18nLabels>
		 
		<adsm:lookup label="filial" labelWidth="9%" dataType="text" size="3" maxLength="3" width="45%" action="municipios/manterFiliais"
				     service="lms.fretecarreteirocoletaentrega.manterTabelasFretesEventuaisAction.findLookupFilial" property="filial" idProperty="idFilial" criteriaProperty="sgFilial" required="true">
					<adsm:propertyMapping criteriaProperty="tpEmpresa" modelProperty="empresa.tpEmpresa"/>
					<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
					<adsm:propertyMapping modelProperty="sgFilial" relatedProperty="solicitacaoContratacao.filial.sgFilial"/>
					<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="35" disabled="true" serializable="false"/> 
		</adsm:lookup>
		
		<adsm:lookup picker="false" width="5%" maxLength="3" action="/contratacaoVeiculos/manterSolicitacoesContratacao"
			service="lms.fretecarreteirocoletaentrega.manterTabelasFretesEventuaisAction.findLookupSolicitacaoContratacao" 
			dataType="text" property="solicitacaoContratacao" idProperty="idSolicitacaoContratacao"
			criteriaProperty="filial.sgFilial" disabled="true" label="solicitacaoContratacao" size="3" labelWidth="18%" >
	
		</adsm:lookup>
		<adsm:lookup width="20%" maxLength="10"
		service="lms.fretecarreteirocoletaentrega.manterTabelasFretesEventuaisAction.findLookupSolicitacaoContratacao" 
		dataType="integer" property="solicitacaoContratacao2" idProperty="idSolicitacaoContratacao" mask="0000000000"
		criteriaProperty="nrSolicitacaoContratacao" size="15" action="/contratacaoVeiculos/manterSolicitacoesContratacao">
			<adsm:propertyMapping criteriaProperty="tipoMeioTransporte.tpMeioTransporte" modelProperty="tipoMeioTransporte.tpMeioTransporte"/>
			
		    <adsm:propertyMapping relatedProperty="solicitacaoContratacao.idSolicitacaoContratacao" modelProperty="idSolicitacaoContratacao"/>
		    <adsm:propertyMapping relatedProperty="solicitacaoContratacao.filial.sgFilial" modelProperty="filial.sgFilial" blankFill="false"/>
		    <adsm:propertyMapping criteriaProperty="solicitacaoContratacao.filial.sgFilial" modelProperty="filial.sgFilial" inlineQuery="false"/>
		    <adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial"/>
		    <adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" inlineQuery="false"/>
		    <adsm:propertyMapping criteriaProperty="meioTransporteRodoviario2.meioTransporte.nrFrota" modelProperty="meioTransporteRodoviario2.meioTransporte.nrFrota" inlineQuery="false"/>
		    <adsm:propertyMapping criteriaProperty="meioTransporteRodoviario2.idMeioTransporte" modelProperty="meioTransporteRodoviario2.idMeioTransporte" inlineQuery="false"/>
		    <adsm:propertyMapping criteriaProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador" modelProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador" inlineQuery="false"/>
		    <adsm:propertyMapping criteriaProperty="meioTransporteRodoviario.idMeioTransporte" modelProperty="meioTransporteRodoviario.idMeioTransporte"/>
		    
			<adsm:hidden property="solicitacaoContratacao.tpSolicitacaoContratacao" serializable="false" value="C"/>
			<adsm:hidden property="solicitacaoContratacao.tpSituacaoContratacao" serializable="false" value="AP"/>
		    
			<adsm:propertyMapping criteriaProperty="solicitacaoContratacao.tpSolicitacaoContratacao" modelProperty="tpSolicitacaoContratacao"/>
			<adsm:propertyMapping criteriaProperty="solicitacaoContratacao.tpSituacaoContratacao" modelProperty="tpSituacaoContratacao"/>
			
		</adsm:lookup>

		<adsm:hidden property="tpMeioTransporte" serializable="false" value="R"/>
		<adsm:lookup dataType="text" property="meioTransporteRodoviario2" idProperty="idMeioTransporte" cellStyle="vertical-align:bottom"
				service="lms.fretecarreteirocoletaentrega.manterTabelasFretesEventuaisAction.findLookupMeioTransporteRodoviario" picker="false"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrFrota" 
				label="meioTransporte" labelWidth="9%" width="9%" size="8" serializable="false" maxLength="6">
			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador"
					modelProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.idMeioTransporte"
					modelProperty="idMeioTransporte" />		
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador"
					modelProperty="meioTransporte.nrIdentificador" />
			
			<adsm:propertyMapping modelProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte" criteriaProperty="tpMeioTransporte"/>
		
			<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="meioTransporte.filial.idFilial"/>
			<adsm:propertyMapping criteriaProperty="proprietario.idProprietario" modelProperty="meioTransporte.meioTranspProprietarios.proprietario.idProprietario"/> 
		</adsm:lookup>
		<adsm:lookup dataType="text" property="meioTransporteRodoviario" idProperty="idMeioTransporte" cellStyle="vertical-align:bottom"
				service="lms.fretecarreteirocoletaentrega.manterTabelasFretesEventuaisAction.findLookupMeioTransporteRodoviario" 
				maxLength="25" action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo"
				criteriaProperty="meioTransporte.nrIdentificador" width="36%" size="20">
			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario2.meioTransporte.nrFrota"
					modelProperty="meioTransporte.nrFrota" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.idMeioTransporte"
					modelProperty="idMeioTransporte" />	
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.meioTransporte.nrFrota"
					modelProperty="meioTransporte.nrFrota" disable="false" />		
								<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="meioTransporte.filial.idFilial"/>
			
			<adsm:propertyMapping modelProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte" criteriaProperty="tpMeioTransporte"/>
			
			<adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="meioTransporte.filial.sgFilial" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="meioTransporte.filial.pessoa.nmFantasia" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="proprietario.idProprietario" modelProperty="meioTransporte.meioTranspProprietarios.proprietario.idProprietario"/> 
			<adsm:propertyMapping criteriaProperty="proprietario.idProprietario" modelProperty="proprietario.idProprietario" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nrIdentificacao" modelProperty="proprietario.pessoa.nrIdentificacao" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nmPessoa" modelProperty="proprietario.pessoa.nmPessoa" inlineQuery="false"/>
		</adsm:lookup>
		
		<adsm:range label="vigencia" labelWidth="18%" width="28%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>
		
		<adsm:lookup dataType="text" property="proprietario" idProperty="idProprietario" maxLength="20"
				service="lms.fretecarreteirocoletaentrega.manterTabelasFretesEventuaisAction.findLookupProprietario" exactMatch="true"
				action="/contratacaoVeiculos/manterProprietarios" criteriaProperty="pessoa.nrIdentificacao" size="17"
				label="proprietario" labelWidth="9%" width="45%" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado">
			<adsm:propertyMapping relatedProperty="proprietario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial"/>
			<adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" inlineQuery="false"/>
			<adsm:textbox dataType="text" property="proprietario.pessoa.nmPessoa" serializable="false" size="35" disabled="true"/>
		</adsm:lookup>
		
		<adsm:combobox property="vigentes" label="vigentes" domain="DM_SIM_NAO" defaultValue="S" labelWidth="18%" width="28%" />
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="TabelaColetaEntrega"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>
	<adsm:grid idProperty="idTabelaColetaEntrega" property="TabelaColetaEntrega" unique="true" rows="9" onRowClick="gridRowClick">
		<adsm:gridColumn width="45" title="meioTransporte" property="nrFrota" align="left"/>
		<adsm:gridColumn width="95" title="" property="nrIdentificador" align="left"/>
		<adsm:gridColumn width="40" title="identificacao" property="proprietarioTpIdentificacao" align="left" isDomain="true"/>
		<adsm:gridColumn width="110" title="" property="proprietarioNrIdentificacao" align="right"/>
		<adsm:gridColumn title="proprietario" property="proprietarioNome"/>	
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="solicitacaoContratacao" property="sgFilial" width="50" />		
			<adsm:gridColumn title="" property="nrContratacao" align="right" dataType="integer" mask="0000000000" width="50" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn width="90" title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate"/>
		<adsm:gridColumn width="90" title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate"/>
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid> 
</adsm:window>