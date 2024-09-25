<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<script type="text/javascript">
<!--

	var idFilial = null;
	var sgFilial = null;
	var nmFilial = null;
	
	function filialSession_cb(data) {
		idFilial = getNestedBeanPropertyValue(data,"filial.idFilial");
		sgFilial = getNestedBeanPropertyValue(data,"filial.sgFilial");
		nmFilial = getNestedBeanPropertyValue(data,"filial.pessoa.nmFantasia");
		writeFilialSession();
	}
	function writeFilialSession() {
		if (idFilial != null &&
			sgFilial != null &&
			nmFilial != null) {
			setElementValue("filial.idFilial",idFilial);
			setElementValue("filial.sgFilial",sgFilial);
			setElementValue("notaCredito2.sgFilial",sgFilial);
			setElementValue("filial.pessoa.nmFantasia",nmFilial);
			setDisabled("notaCredito.idNotaCredito",false);
		}
	}
	
	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		var sdo = createServiceDataObject("lms.fretecarreteirocoletaentrega.emitirNotasCreditoAction.findFilialSession","filialSession",null);
		xmit({serviceDataObjects:[sdo]});
	}

	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click")
			writeFilialSession();
		setDisabled("emitir",false);
	}
	
	function popUpSetNotaCredito() {
		return true;
	}
	
	function dataLoadNotaCredito2_cb(data) {
		controleCargas_meioTransporte2_nrFrota_exactMatch_cb(data);
	}
	function dataLoadNotaCredito_cb(data) {
		controleCargas_meioTransporte_nrIdentificador_exactMatch_cb(data);
	}
	function dataLoadFilial_cb(data) {
		filial_sgFilial_exactMatch_cb(data);
		if (data != undefined && data.length == 1)
			setDisabled("notaCredito.idNotaCredito",false);
	}
	function setPopUpFilial(data) {
		setDisabled("notaCredito.idNotaCredito",false);
		return true;
	}
	function changeFilial() {
		var flag = filial_sgFilialOnChangeHandler();
		if (getElementValue("filial.idFilial") == "")
			setDisabled("notaCredito.idNotaCredito",true);
		return flag;
	}
	function emitirReport() {
		executeReportWithCallback('lms.fretecarreteirocoletaentrega.emitirNotasCreditoAction.executeReport', 'emitirReport', document.forms[0]);
	}
	function emitirReport_cb(data,error,key) {
		if (error == null) {
			if (data.workflow == "true" || getNestedBeanPropertyValue(data,"texto"))
				alert(getNestedBeanPropertyValue(data,"texto"));
			openReportWithLocator(data.path);
		}else{
			alert(error);
			if (key == "LMS-25023")
				setFocus("dhEmissaoFinal");
		}		
	}
	function dataLoadSamples_cb(data,error) {
		if (error != undefined)
			alert(error);
		else
			fillFormWithFormBeanData(document.forms[0].tabIndex, data);
	}
	
	
	function dataLoadMeioTransp_cb(data) {
		controleCargas_meioTransporte_nrIdentificador_exactMatch_cb(data);
		if (data != undefined && data.length == 1)
			loadProprietarioByMeioTransp(getNestedBeanPropertyValue(data[0],"idMeioTransporte"));
	}
	function dataLoadMeioTransp2_cb(data) {
		controleCargas_meioTransporte2_nrFrota_exactMatch_cb(data);
		if (data != undefined && data.length == 1)
			loadProprietarioByMeioTransp(getNestedBeanPropertyValue(data[0],"idMeioTransporte"));
	}
 	function setPopUpMeioTransp(data) {
		loadProprietarioByMeioTransp(getNestedBeanPropertyValue(data,"idMeioTransporte"));
		return true;
	}
	function loadProprietarioByMeioTransp(idMeioTransporte) {
		var sdo = createServiceDataObject("lms.fretecarreteirocoletaentrega.emitirNotasCreditoAction.findProprietarioByMeioTransporte","dataLoadSamples",{e:idMeioTransporte});
		xmit({serviceDataObjects:[sdo]});
	}
	
	function changeMT2() {
		var flag = controleCargas_meioTransporte2_nrFrotaOnChangeHandler();
		if (getElementValue("controleCargas.meioTransporte2.nrFrota") == "") {
			resetValue("controleCargas.proprietario.idProprietario");
			resetValue("controleCargas.proprietario.pessoa.nmPessoa");
		}
		return flag;
	}

	function changeMT() {
		var flag = controleCargas_meioTransporte_nrIdentificadorOnChangeHandler();
		if (getElementValue("controleCargas.meioTransporte.nrIdentificador") == "") {
			resetValue("controleCargas.proprietario.idProprietario");
			resetValue("controleCargas.proprietario.pessoa.nmPessoa");
		}
		return flag;
	}
//-->
</script>
<adsm:window title="emitirNotasCredito" onPageLoadCallBack="pageLoad" service="lms.fretecarreteirocoletaentrega.emitirNotasCreditoAction">
	<adsm:form action="/freteCarreteiroColetaEntrega/emitirNotasCredito">
		<adsm:hidden property="tpSituacao" value="A" serializable="false"/>
		<adsm:hidden property="isDhEmissao" value="true" serializable="false"/>
		<adsm:i18nLabels>
			<adsm:include key="LMS-00065"/>
		</adsm:i18nLabels>

		<adsm:hidden property="filial.empresa.tpEmpresa" value="M" serializable="false"/>
		<adsm:lookup label="filial" labelWidth="29%" dataType="text" size="3" maxLength="3" width="71%" action="municipios/manterFiliais" onPopupSetValue="setPopUpFilial"
				     service="lms.fretecarreteirocoletaentrega.emitirNotasCreditoAction.findLookupFilial" property="filial" onchange="return changeFilial()"
				      idProperty="idFilial" criteriaProperty="sgFilial" required="true" onDataLoadCallBack="dataLoadFilial" disabled="true">
					<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
					<adsm:propertyMapping criteriaProperty="filial.empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>
					<adsm:propertyMapping relatedProperty="notaCredito2.sgFilial" modelProperty="sgFilial"/>
					
					<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/> 
		</adsm:lookup>


		<adsm:lookup dataType="text" property="controleCargas.meioTransporte2" idProperty="idMeioTransporte" cellStyle="vertical-align:bottom;"
				service="lms.fretecarreteirocoletaentrega.emitirNotasCreditoAction.findLookupMeioTransporte" picker="false" maxLength="6"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="list" criteriaProperty="nrFrota" onchange="return changeMT2();"
				label="meioTransporte" labelWidth="29%" width="8%" size="7" serializable="false" onDataLoadCallBack="dataLoadMeioTransp">
			<adsm:propertyMapping criteriaProperty="controleCargas.meioTransporte.nrIdentificador" modelProperty="nrIdentificador" />
			<adsm:propertyMapping relatedProperty="controleCargas.meioTransporte.idMeioTransporte" modelProperty="idMeioTransporte" />		
			<adsm:propertyMapping relatedProperty="controleCargas.meioTransporte.nrIdentificador"  modelProperty="nrIdentificador" />
			<adsm:propertyMapping criteriaProperty="controleCargas.proprietario.idProprietario" modelProperty="proprietario.idProprietario"/> 
			<adsm:propertyMapping criteriaProperty="controleCargas.proprietario.pessoa.nrIdentificacao" modelProperty="proprietario.pessoa.nrIdentificacao" inlineQuery="false"/> 
			<adsm:propertyMapping criteriaProperty="controleCargas.proprietario.pessoa.nmPessoa" modelProperty="proprietario2.pessoa.nmPessoa" inlineQuery="false"/> 
			<adsm:propertyMapping relatedProperty="nrMarca" modelProperty="modeloMeioTransporte.marcaMeioTransporte.dsMarcaMeioTransporte"/>
			<adsm:propertyMapping relatedProperty="nrModelo" modelProperty="modeloMeioTransporte.dsModeloMeioTransporte"/>
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="controleCargas.meioTransporte" idProperty="idMeioTransporte" onchange="return changeMT();"
				service="lms.fretecarreteirocoletaentrega.emitirNotasCreditoAction.findLookupMeioTransporte" picker="true" maxLength="25"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="list" criteriaProperty="nrIdentificador" onDataLoadCallBack="dataLoadMeioTransp2"
				width="60%" size="20" cellStyle="vertical-align:bottom;" onPopupSetValue="setPopUpMeioTransp">
			<adsm:propertyMapping criteriaProperty="controleCargas.meioTransporte2.nrFrota" modelProperty="nrFrota" />
			<adsm:propertyMapping relatedProperty="controleCargas.meioTransporte2.idMeioTransporte" modelProperty="idMeioTransporte" />	
			<adsm:propertyMapping relatedProperty="controleCargas.meioTransporte2.nrFrota" modelProperty="nrFrota" />	
			<adsm:propertyMapping criteriaProperty="controleCargas.proprietario.idProprietario" modelProperty="proprietario.idProprietario"/> 
			<adsm:propertyMapping criteriaProperty="controleCargas.proprietario.pessoa.nrIdentificacao" modelProperty="proprietario.pessoa.nrIdentificacao" inlineQuery="false"/> 
			<adsm:propertyMapping criteriaProperty="controleCargas.proprietario.pessoa.nmPessoa" modelProperty="proprietario2.pessoa.nmPessoa" inlineQuery="false"/> 
			<adsm:propertyMapping relatedProperty="nrMarca" modelProperty="modeloMeioTransporte.marcaMeioTransporte.dsMarcaMeioTransporte"/>
			<adsm:propertyMapping relatedProperty="nrModelo" modelProperty="modeloMeioTransporte.dsModeloMeioTransporte"/>
		</adsm:lookup>

		<adsm:textbox dataType="text" property="nrMarca" size="24" maxLength="50" width="31%" labelWidth="29%" label="marcaMeioTransporte" disabled="true" />
		<adsm:textbox dataType="text" property="nrModelo" size="24" maxLength="50" width="31%" labelWidth="29%" label="modeloMeioTransporte" disabled="true" />

		<adsm:lookup dataType="text" property="controleCargas.proprietario" idProperty="idProprietario" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				service="lms.fretecarreteirocoletaentrega.emitirNotasCreditoAction.findLookupProprietario"
				action="/contratacaoVeiculos/manterProprietarios" criteriaProperty="pessoa.nrIdentificacao"
				label="proprietario" labelWidth="29%" width="71%" size="20" maxLength="20">
			<adsm:propertyMapping relatedProperty="controleCargas.proprietario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
			<adsm:textbox dataType="text" property="controleCargas.proprietario.pessoa.nmPessoa" serializable="false" size="30" disabled="true"/>
		</adsm:lookup>
		
		<adsm:section caption="criteriosParaReemissao"/>


		<adsm:lookup dataType="text" property="notaCredito2" idProperty="idFilial" cellStyle="vertical-align:bottom;"
				action="/freteCarreteiroColetaEntrega/manterNotasCredito" cmd="list" criteriaProperty="sgFilial" disabled="true"
				service="lms.fretecarreteirocoletaentrega.emitirNotasCreditoAction.findLookupFilial"
				label="numeroNota" labelWidth="29%" width="7%" size="5" picker="false" serializable="false">
		</adsm:lookup>
		
		<adsm:lookup dataType="integer" property="notaCredito" idProperty="idNotaCredito"
				service="lms.fretecarreteirocoletaentrega.emitirNotasCreditoAction.findLookupNotaCredito" maxLength="25"
				action="/freteCarreteiroColetaEntrega/manterNotasCredito" cmd="list" criteriaProperty="nrNotaCredito"
				width="60%" size="20" cellStyle="vertical-align:bottom;" mask="0000000000">
				
			<adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial"/>
			
			<adsm:propertyMapping criteriaProperty="isDhEmissao" modelProperty="isDhEmissao"/>

			<adsm:propertyMapping criteriaProperty="controleCargas.proprietario.idProprietario" modelProperty="controleCargas.proprietario.idProprietario"/> 
			<adsm:propertyMapping criteriaProperty="controleCargas.proprietario.pessoa.nrIdentificacao" modelProperty="controleCargas.proprietario.pessoa.nrIdentificacao" inlineQuery="false"/> 
			<adsm:propertyMapping criteriaProperty="controleCargas.proprietario.pessoa.nmPessoa" modelProperty="controleCargas.proprietario.pessoa.nmPessoa" inlineQuery="false"/> 

			<adsm:propertyMapping criteriaProperty="controleCargas.meioTransporte2.nrFrota" modelProperty="controleCargas.meioTransporte2.nrFrota" inlineQuery="false" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="controleCargas.meioTransporte2.idMeioTransporte" modelProperty="controleCargas.meioTransporte2.idMeioTransporte" inlineQuery="false"  addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="controleCargas.meioTransporte.idMeioTransporte" modelProperty="controleCargas.meioTransporte.idMeioTransporte" addChangeListener="false"/>	
			<adsm:propertyMapping criteriaProperty="controleCargas.meioTransporte.nrIdentificador"  modelProperty="controleCargas.meioTransporte.nrIdentificador" inlineQuery="false" addChangeListener="false"/>	
			
			<adsm:propertyMapping relatedProperty="controleCargas.proprietario.idProprietario" modelProperty="controleCargas.proprietario.idProprietario" blankFill="false"/> 
			<adsm:propertyMapping relatedProperty="controleCargas.proprietario.pessoa.nrIdentificacao" modelProperty="controleCargas.proprietario.pessoa.nrIdentificacao" inlineQuery="false" blankFill="false"/> 
			<adsm:propertyMapping relatedProperty="controleCargas.proprietario.pessoa.nmPessoa" modelProperty="controleCargas.proprietario.pessoa.nmPessoa" inlineQuery="false" blankFill="false"/> 

			<adsm:propertyMapping relatedProperty="controleCargas.meioTransporte2.nrFrota" modelProperty="controleCargas.meioTransporte2.nrFrota" inlineQuery="false" blankFill="false"/>
			<adsm:propertyMapping relatedProperty="controleCargas.meioTransporte2.idMeioTransporte" modelProperty="controleCargas.meioTransporte2.idMeioTransporte" inlineQuery="false" blankFill="false"/>
			<adsm:propertyMapping relatedProperty="controleCargas.meioTransporte.idMeioTransporte" modelProperty="controleCargas.meioTransporte.idMeioTransporte" blankFill="false"/>	
			<adsm:propertyMapping relatedProperty="controleCargas.meioTransporte.nrIdentificador"  modelProperty="controleCargas.meioTransporte.nrIdentificador" inlineQuery="false" blankFill="false"/>	
			
			<adsm:propertyMapping relatedProperty="nrModelo" modelProperty="controleCargas.meioTransporte.modeloMeioTransporte.dsModeloMeioTransporte" blankFill="false"/>
			<adsm:propertyMapping relatedProperty="nrMarca" modelProperty="controleCargas.meioTransporte.modeloMeioTransporte.marcaMeioTransporte.dsMarcaMeioTransporte" blankFill="false"/>
		</adsm:lookup>
		
		<adsm:range label="periodoEmissao" labelWidth="29%" width="71%">
             <adsm:textbox dataType="JTDate" property="dhEmissaoInicial"/>
             <adsm:textbox dataType="JTDate" property="dhEmissaoFinal"/>
        </adsm:range>
		
		<adsm:buttonBar>
			<adsm:button id="emitir" caption="emitir" onclick="emitirReport()"/>
			<adsm:resetButton/>
		</adsm:buttonBar>	
	</adsm:form>
</adsm:window>