<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
<!--
	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		if (getElementValue("tipoMeioTransporte.tpMeioTransporte") != "")
			notifyElementListeners({e:document.getElementById("tipoMeioTransporte.tpMeioTransporte")});	
		
		if (getElementValue("idTipoMeioTransporteTemp") != ""){
			notifyElementListeners({e:document.getElementById("tipoMeioTransporte.tpMeioTransporte")});
			document.getElementById("tipoMeioTransporte.idTipoMeioTransporte").masterLink = "true";
			setDisabled("tipoMeioTransporte.idTipoMeioTransporte", true);
		}
		document.getElementById("nrIdentificacaoSemiReboque.nrPlaca").serializable = true;
		document.getElementById("nrIdentificacaoMeioTransp.nrPlaca").serializable = true;
 
		isLookup = window.dialogArguments && window.dialogArguments.window;
		if (!isLookup) {
			var sdo = createServiceDataObject("lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.findDataSession","dataSession",null);
			xmit({serviceDataObjects:[sdo]});
		}
	}	
	
	function setaTipoMeioTransporte_cb(data, error){
		tipoMeioTransporte_idTipoMeioTransporte_cb(data);
		if (getElementValue("idTipoMeioTransporteTemp") != "")
			setComboBoxElementValue(document.getElementById("tipoMeioTransporte.idTipoMeioTransporte"), getElementValue("idTipoMeioTransporteTemp"),
									getElementValue("idTipoMeioTransporteTemp"), getElementValue("dsTipoMeioTransporteTemp"));
	}
	
	function validateTab() {
		if (validateTabScript(document.Lazy)) {
			if (getElementValue("nrSolicitacaoContratacao") != "" ||
				(getElementValue("dtInicioContratacao") != "" && getElementValue("dtFimContratacao") != "") ||
				getElementValue("nrIdentificacaoMeioTransp.nrPlaca") != "")
					return true;
			else{
				alert(i18NLabel.getLabel("LMS-00013") +
						document.getElementById("nrSolicitacaoContratacao").label + ", " +
						document.getElementById("dtInicioContratacao").label + ", " +
						document.getElementById("nrIdentificacaoMeioTransp.nrPlaca").label + ".");
				return false;
			}
		}
		return false;
	}
	
	function validateLookupForm() {
      	return validateTab();
	}
	
	function disableTabAnexos(disabled) {
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("anexo", disabled);
	}
	
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click") {
			writeDataSession();
		}else if (eventObj.name == "tab_click") {
			var tabGroup = getTabGroup(this.document);
			tabGroup.setDisabledTab("parc",true);
			tabGroup.getTab("parc").getDocument().getElementById("ParcelaTabelaCe.dataTable").gridDefinition.resetGrid();
			tabGroup.setDisabledTab("fluxoContratacao",true);
			tabGroup.getTab("fluxoContratacao").getDocument().getElementById("FluxoContratacao.dataTable").gridDefinition.resetGrid();
			disableTabAnexos(true);
		}
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
		}
	}
	
	
	function dataLoadMeioTransporte_cb(data) {
		var flag = nrIdentificacaoMeioTransp_nrPlaca_exactMatch_cb(data);
		if (data != undefined && data.length != 1) {
			resetValue("nrIdentificacaoMeioTransp.nrFrota"); 
			setElementValue("nrIdentificacaoMeioTransp.nrPlaca",getElementValue("nrIdentificacaoMeioTransp.nrPlaca").toUpperCase());
		}
		return flag;
    }
	function dataLoadSemiReboque_cb(data) {
		var flag = nrIdentificacaoSemiReboque_nrPlaca_exactMatch_cb(data);
		if (data != undefined && data.length != 1) {
			resetValue("nrIdentificacaoSemiReboque.nrFrota");
			setElementValue("nrIdentificacaoSemiReboque.nrPlaca",getElementValue("nrIdentificacaoSemiReboque.nrPlaca").toUpperCase());
		}
		return flag;
     }
	
	
//--> 
</script>
<adsm:window title="consutarSolicitacoesContratacao" onPageLoadCallBack="pageLoad" service="lms.contratacaoveiculos.manterSolicitacoesContratacaoAction">
	<adsm:form action="/contratacaoVeiculos/manterSolicitacoesContratacao" height="102">
		<adsm:hidden property="empresa.tpEmpresa" value="M" serializable="false"/>
		<adsm:hidden property="idProcessoWorkflow" serializable="false"/>
		<adsm:i18nLabels>
			<adsm:include key="LMS-00013"/>
		</adsm:i18nLabels>
		<adsm:lookup label="filialSolicitante" labelWidth="23%" dataType="text" size="3" maxLength="3" width="77%"
				     service="lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.findLookupFilial" property="filial" idProperty="idFilial"
					 criteriaProperty="sgFilial" action="/municipios/manterFiliais" required="true">
  		            <adsm:propertyMapping criteriaProperty="empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>
					<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
					<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="35" disabled="true"/> 
		</adsm:lookup>
	
		<adsm:combobox property="tpSolicitacaoContratacao" label="tipoSolicitacao" domain="DM_TIPO_SOLICITACAO_CONTRATACAO" labelWidth="23%" width="32%" boxWidth="150"/>
		<adsm:textbox dataType="integer" property="nrSolicitacaoContratacao" label="numeroSolicitacao" maxLength="10" size="20" labelWidth="21%" width="24%" mask="0000000000"/>

		<adsm:hidden property="tipoMeioTransporte.tpMeioTransporte" value="R" />
		<adsm:combobox property="tpModal" boxWidth="150" label="modalidade" domain="DM_TIPO_MEIO_TRANSPORTE" width="77%" labelWidth="23%"/>
		<adsm:hidden property="idTipoMeioTransporteTemp" serializable="false" />
		<adsm:hidden property="dsTipoMeioTransporteTemp" serializable="false" />
		<adsm:combobox property="tipoMeioTransporte.idTipoMeioTransporte" 
				optionLabelProperty="dsTipoMeioTransporte" optionProperty="idTipoMeioTransporte" 
				service="lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.findComboTpMeioTransporte" 
				label="tipoMeioTransporte" labelWidth="23%" width="32%" boxWidth="235"
				onDataLoadCallBack="setaTipoMeioTransporte" >
			<adsm:propertyMapping criteriaProperty="tipoMeioTransporte.tpMeioTransporte" modelProperty="tpMeioTransporte"/>
		</adsm:combobox>

		<adsm:combobox property="blIndicadorRastreamento" label="meioTransporteRastreado" domain="DM_SIM_NAO" width="24%" labelWidth="21%"/>

		<adsm:range label="dataSolicitacao" labelWidth="23%" width="77%" maxInterval="30">
			<adsm:textbox dataType="JTDate" property="dtInicioContratacao"/>
			<adsm:textbox dataType="JTDate" property="dtFimContratacao"/>
		</adsm:range>
 
		<adsm:lookup property="usuario" label="solicitadoPor" idProperty="idUsuario" criteriaProperty="nrMatricula" 
					 action="/configuracoes/consultarFuncionariosView" dataType="text" size="10" maxLength="16" labelWidth="23%" width="77%"
		 			 service="lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.findLookupUsuario">
			<adsm:propertyMapping modelProperty="nmUsuario"    relatedProperty="usuario.nmUsuario"/>
			<adsm:textbox dataType="text" property="usuario.nmUsuario" size="30" disabled="true" serializable="false"/>
		</adsm:lookup> 

		<adsm:combobox property="tpSituacaoContratacao" label="situacao" domain="DM_SITUACAO_SOLICITACAO_CONTRATACAO" labelWidth="23%" width="77%" boxWidth="150"/>

		<adsm:textbox size="7" disabled="true" dataType="text" property="nrIdentificacaoMeioTransp.nrFrota" label="meioTransporte" labelWidth="23%" width="8%"/>
		
	    <adsm:lookup service="lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.findMeioTransporte"
	    	property="nrIdentificacaoMeioTransp" idProperty="anoFabricacao" action="f" picker="false" criteriaProperty="nrPlaca"
	    	dataType="text" maxLength="25" size="20"  onDataLoadCallBack="dataLoadMeioTransporte" width="19%" allowInvalidCriteriaValue="true">
	    	<adsm:propertyMapping relatedProperty="nrIdentificacaoMeioTransp.nrFrota" modelProperty="nrFrota"/>
		</adsm:lookup>   	

		<adsm:textbox size="7" disabled="true" dataType="text" property="nrIdentificacaoSemiReboque.nrFrota" label="semiReboque" labelWidth="23%" width="8%"/>

	    <adsm:lookup service="lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.findMeioTransporte"
	    	property="nrIdentificacaoSemiReboque" idProperty="anoFabricacao" action="f" picker="false" criteriaProperty="nrPlaca"
	    	dataType="text" maxLength="25" size="20" onDataLoadCallBack="dataLoadSemiReboque" width="19%" allowInvalidCriteriaValue="true">
	    	<adsm:propertyMapping relatedProperty="nrIdentificacaoSemiReboque.nrFrota" modelProperty="nrFrota"/>
		</adsm:lookup>
		
		<adsm:combobox property="tpAbrangencia" label="abrangencia" domain="DM_ABRANGENCIA_TOTAL" labelWidth="23%" width="27%" boxWidth="150"/>
		
		<adsm:combobox
			property="blQuebraMeioTransporte"
			label="quebraMeioTransporte"
			domain="DM_SIM_NAO"
			labelWidth="23%"
			width="27%"
		/>
        
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="SolicitacaoContratacao"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid 
		idProperty="idSolicitacaoContratacao" 
		property="SolicitacaoContratacao" 
		gridHeight="170" 
		rows="8"
		unique="true" 
		scrollBars="horizontal">
		
		<adsm:gridColumn title="tipoSolicitacao" property="tpSolicitacaoContratacao" isDomain="true" width="150"/>
		<adsm:gridColumn title="numeroSolicitacao" property="nrSolicitacaoContratacao" width="150" dataType="integer" mask="0000000000" />
		<adsm:gridColumn title="modalidade" property="tipoMeioTransporte.tpMeioTransporte" isDomain="true" width="120"/>
		<adsm:gridColumn title="tipoMeioTransporte" property="tipoMeioTransporte.dsTipoMeioTransporte" width="150"/>
		<adsm:gridColumn title="meioTransporte" property="nrFrota" width="80"/>
		<adsm:gridColumn title=""               property="nrIdentificacaoMeioTransp" width="80"/>
		<adsm:gridColumn title="solicitadoPor" property="usuarioSolicitador.nmUsuario" width="200"/>
		<adsm:gridColumn title="dataSolicitacao" property="dtCriacao" dataType="JTDate" width="130"/>
		<adsm:gridColumn title="situacao" property="tpSituacaoContratacao" isDomain="true" width="90" />
		<adsm:gridColumn title="abrangencia" property="tpAbrangencia" isDomain="true" width="90" />
		<adsm:gridColumn title="tipoRota" property="tpRotaSolicitacao" isDomain="true" width="90" />
		<adsm:gridColumn title="rota" property="rota.dsRota" dataType="text" width="90" />
		<adsm:gridColumn title="freteValorSugerido" property="dsSiglaSimboloVlFreteSugerido" dataType="text" width="50" />
		<adsm:gridColumn title="" property="vlFreteSugerido" dataType="currency" width="100" />
		<adsm:gridColumn title="freteValorAutorizadoAte" property="dsSiglaSimboloVlFreteMaximoAutorizado" dataType="text" width="50" />
		<adsm:gridColumn title="" property="vlFreteMaximoAutorizado" dataType="currency" width="100" />
		<adsm:gridColumn title="freteValorNegociado" property="dsSiglaSimboloVlFreteNegociado" dataType="text" width="50" />
		<adsm:gridColumn title="" property="vlFreteNegociado" dataType="currency" width="100" />
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>