<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.carregamento.gerarControleCargasAction" >

	<adsm:form idProperty="idIntegranteEqOperac" action="/carregamento/gerarControleCargas" height="215"
			   service="lms.carregamento.gerarControleCargasAction.findByIdIntegranteEqOperac" onDataLoadCallBack="retornoForm" >

		<adsm:masterLink idProperty="idControleCarga" showSaveAll="true" >
			<adsm:masterLinkItem property="controleCargaConcatenado" label="controleCargas" itemWidth="100" />
			<adsm:hidden property="idEquipe" />
		</adsm:masterLink>

		<adsm:hidden property="idControleCarga" serializable="true" />
		<adsm:hidden property="filialUsuario.idFilial" serializable="false" />
		<adsm:hidden property="filialUsuario.sgFilial" serializable="false" />
		<adsm:hidden property="filialUsuario.pessoa.nmFantasia" serializable="false" />

		<adsm:section caption="controleCargas"/>

		<adsm:hidden property="tpSituacao" value="A" serializable="false" />

		<adsm:lookup property="equipe" dataType="text" 
					 idProperty="idEquipe" criteriaProperty="dsEquipe"
					 action="/carregamento/manterEquipes" 
					 service="lms.carregamento.gerarControleCargasAction.findLookupEquipe" 
					 exactMatch="false" minLengthForAutoPopUpSearch="3" 
					 onDataLoadCallBack="retornoEquipe"
					 onPopupSetValue="popupEquipe"
					 onchange="return equipe_OnChange()"
					 required="true"
					 label="equipe" size="50" maxLength="50" labelWidth="19%" width="81%" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="filial.idFilial" criteriaProperty="filialUsuario.idFilial" />
			<adsm:propertyMapping modelProperty="filial.sgFilial" criteriaProperty="filialUsuario.sgFilial" />
			<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" criteriaProperty="filialUsuario.pessoa.nmFantasia" />
		</adsm:lookup>
		
		<adsm:section caption="integrantes"/>

		<adsm:combobox property="tpIntegrante" label="contratacao" domain="DM_INTEGRANTE_EQUIPE" 
					   onchange="return tpIntegrante_OnChange(this)" onlyActiveValues="true" renderOptions="true"
					   labelWidth="19%" width="81%" required="true" />


		<adsm:hidden property="usuario.dsFuncao"/>
		<adsm:lookup property="usuario" label="funcionario" 
					 idProperty="idUsuario" criteriaProperty="nrMatricula" 
                     service="lms.carregamento.gerarControleCargasAction.findLookupUsuarioFuncionario" 
            		 action="/configuracoes/consultarFuncionariosView"
            		 dataType="text" size="16" maxLength="16" labelWidth="19%" width="81%" >
        	<adsm:propertyMapping modelProperty="nmUsuario" relatedProperty="usuario.nmUsuario" />
        	<adsm:propertyMapping modelProperty="dsFuncao" relatedProperty="usuario.dsFuncao" />
        	<adsm:propertyMapping modelProperty="nmUsuario" criteriaProperty="usuario.nmUsuario" disable="false" />
			<adsm:propertyMapping modelProperty="filial.idFilial" criteriaProperty="filialUsuario.idFilial" disable="true" />
			<adsm:propertyMapping modelProperty="filial.sgFilial" criteriaProperty="filialUsuario.sgFilial" disable="true" />
			<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" criteriaProperty="filialUsuario.pessoa.nmFantasia" disable="true" />
        	<adsm:textbox dataType="text" property="usuario.nmUsuario" size="30" maxLength="45" disabled="true" />
		</adsm:lookup>


		<adsm:hidden property="pessoa.tpPessoa" value="F" serializable="false" />
		<adsm:hidden property="pessoa.tpIdentificacao" serializable="false" />

		<adsm:lookup property="pessoa" dataType="text" 
					 idProperty="idPessoa" 
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="nrIdentificacaoFormatado"
					 action="/configuracoes/manterPessoas" 
					 service="lms.carregamento.gerarControleCargasAction.findLookupPessoa" 
					 label="integrante" size="18" maxLength="20" labelWidth="19%" width="81%"
					 serializable="true" >
			<adsm:propertyMapping modelProperty="nmPessoa" relatedProperty="pessoa.nmPessoa" />
			<adsm:propertyMapping modelProperty="tpIdentificacao.value" relatedProperty="pessoa.tpIdentificacao" />	
			<adsm:propertyMapping modelProperty="pessoa.tpPessoa" criteriaProperty="pessoa.tpPessoa" />
			<adsm:propertyMapping modelProperty="nmPessoa" criteriaProperty="pessoa.nmPessoa" disable="false"/>	
			<adsm:propertyMapping modelProperty="pessoa.tpIdentificacao" criteriaProperty="pessoa.tpIdentificacao" disable="false"/>	
			<adsm:propertyMapping modelProperty="pessoa.nrIdentificacao" criteriaProperty="pessoa.pessoa.nrIdentificacao" disable="false"/>
			<adsm:textbox property="pessoa.nmPessoa" dataType="text" size="30" disabled="true" serializable="false" />
		</adsm:lookup>


		<adsm:combobox property="cargoOperacional.idCargoOperacional" label="cargo"
					   service="lms.carregamento.gerarControleCargasAction.findCargoOperacional" renderOptions="true"
					   optionProperty="idCargoOperacional" optionLabelProperty="dsCargo" onlyActiveValues="true"
					   boxWidth="305" labelWidth="19%" width="81%" />


		<adsm:lookup property="empresa" dataType="text"
					 idProperty="idEmpresa" 
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 service="lms.carregamento.gerarControleCargasAction.findLookupEmpresa" 
					 action="/municipios/manterEmpresas" 
					 label="empresa" size="18" maxLength="20" labelWidth="19%" width="81%"
					 serializable="true" >
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="empresa.pessoa.nmPessoa" />
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" criteriaProperty="empresa.pessoa.nmPessoa" disable="false" />
			<adsm:textbox dataType="text" property="empresa.pessoa.nmPessoa" size="30" disabled="true" serializable="false" />
		</adsm:lookup>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="salvarIntegrante" id="salvarIntegranteButton" onclick="salvarIntegrante(this.form);" />
			<adsm:button caption="limpar" id="novoIntegranteButton" onclick="novoIntegranteForm();"/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid property="integranteEqOperac" idProperty="idIntegranteEqOperac" unique="true" 
			   showPagging="false" scrollBars="both" autoSearch="false" gridHeight="105"
			   service="lms.carregamento.gerarControleCargasAction.findPaginatedIntegranteEqOperac" 
			   rowCountService="" onDataLoadCallBack="retornoGrid"
			   detailFrameName="equipe" >
		<adsm:gridColumn title="nome" 			property="nmIntegranteEquipe" width="200" />
		<adsm:gridColumn title="contratacao" 	property="tpIntegrante" isDomain="true" width="125" />
		<adsm:gridColumn title="matricula" 		property="usuario.nrMatricula" width="75" align="right" />
		<adsm:gridColumn title="identificacao"	property="pessoa.nrIdentificacaoFormatado" width="110" align="right" />
		<adsm:gridColumn title="cargo" 			property="cargoOperacional.dsCargo" width="170" /> 
		<adsm:gridColumn title="empresa" 		property="empresa.pessoa.nmPessoa" width="170" />
		<adsm:buttonBar freeLayout="false">
			<adsm:button caption="excluirIntegrante" id="excluirIntegranteButton" onclick="removeIntegrante()" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script><!--
var idEquipeOld;
var dsEquipeOld;

function initWindow(eventObj) {
	if (eventObj.name == "tab_click") {
		setDadosMaster();
		setFocusOnFirstFocusableField();
	}
}

function limpaCampos() {
	resetValue('tpIntegrante');
	resetValue('usuario.idUsuario');
	resetValue('pessoa.idPessoa');
	resetValue('cargoOperacional.idCargoOperacional');
	resetValue('empresa.idEmpresa');
}

function desabilitaCampos() {
	setDisabled('usuario.idUsuario', true);
	setDisabled("usuario.nmUsuario", true);
	setDisabled('pessoa.idPessoa', true);
	setDisabled('cargoOperacional.idCargoOperacional', true);
	setDisabled('empresa.idEmpresa', true);
}

function desabilitaBotoes(valor) {
	setDisabled('novoIntegranteButton', valor);
	setDisabled('salvarIntegranteButton', valor);
	setDisabled('excluirIntegranteButton', valor);
}

function inicializarCampos(valor) {
	limpaCampos();
	desabilitaCampos();
	desabilitaBotoes(valor);
	setDisabled('tpIntegrante', valor);
}

function tpIntegrante_OnChange(combo) {
	validaCamposItens(getElementValue('tpIntegrante'));
	return comboboxChange({e:combo});
}

function setDadosMaster() {
	var tabGroup = getTabGroup(this.document);
	var tabDet = tabGroup.getTab("cad");
	setElementValue("idControleCarga", tabDet.getFormProperty("idControleCarga"));
	setElementValue("filialUsuario.idFilial", tabDet.getFormProperty("filialUsuario.idFilial"));
	setElementValue("filialUsuario.sgFilial", tabDet.getFormProperty("filialUsuario.sgFilial"));
	setElementValue("filialUsuario.pessoa.nmFantasia", tabDet.getFormProperty("filialUsuario.pessoa.nmFantasia"));
}



/************************************ INICIO - EQUIPE ************************************/
function retornoEquipe_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = lookupExactMatch({e:document.getElementById("equipe.idEquipe"), data:data, callBack:"retornoEquipe2"});
	if (r == true) {
		callbackEquipe(getNestedBeanPropertyValue(data, "0:idEquipe"), getNestedBeanPropertyValue(data, "0:dsEquipe"));
	}
	return r;
}

function retornoEquipe2_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = equipe_dsEquipe_likeEndMatch_cb(data);
	if (r == true) {
		callbackEquipe(getNestedBeanPropertyValue(data, "0:idEquipe"), getNestedBeanPropertyValue(data, "0:dsEquipe"));
	}
	return r;
}

function popupEquipe(data) {
	callbackEquipe(getNestedBeanPropertyValue(data, "idEquipe"), getNestedBeanPropertyValue(data, "dsEquipe"));
}

function callbackEquipe(valorIdEquipe, valorDsEquipe) {
	idEquipeOld = valorIdEquipe;
	dsEquipeOld = valorDsEquipe;
	inicializarCampos(false);

	var sdo = createServiceDataObject("lms.carregamento.gerarControleCargasAction.inicializaGridIntegranteEqOperac", 
		"retornoInicializaGridIntegranteEqOperac", 
		{idControleCarga:getElementValue("idControleCarga"),
		 idEquipe:idEquipeOld,
		 idEquipeOperacao:""});
   	xmit({serviceDataObjects:[sdo]});
}


function retornoInicializaGridIntegranteEqOperac_cb(data, error) {
	if (error != undefined) {
		resetaCamposEquipe();
		alert(error);
		return false;
	}
	povoaGrid(idEquipeOld);
}

function equipe_OnChange() {
	var r = equipe_dsEquipeOnChangeHandler();
	if (getElementValue('equipe.dsEquipe') == "") {
		resetaCamposEquipe();
		resetDataByEquipe();
	}
	return r;
}

function resetaCamposEquipe() {
	setaDadosEquipe("", "");
	idEquipeOld = "";
	dsEquipeOld = "";
	inicializarCampos(true);
	limpaGrid();
}

function resetDataByEquipe() {
	var sdo = createServiceDataObject("lms.carregamento.gerarControleCargasAction.resetDataByEquipe", "retornoPadrao", 
			{idControleCarga:getElementValue("idControleCarga")});
    xmit({serviceDataObjects:[sdo]});
}

function retornoPadrao_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
}

function setaDadosEquipe(valor1, valor2) {	
	setElementValue('equipe.idEquipe', valor1);
	setElementValue('idEquipe', valor1);
	setElementValue('equipe.dsEquipe', valor2);
}
/************************************ FIM - EQUIPE ************************************/





/************************************ INICIO - GRID ************************************/
function povoaGrid(idEquipe) {
	limpaGrid();
	setElementValue('idEquipe', idEquipe);
	var data = {masterId:document.getElementById('masterId')};
	refreshItemGrid(data, document);
	return false;
}

function limpaGrid() {
	integranteEqOperacGridDef.resetGrid();
}

function removeIntegrante() {
	integranteEqOperacGridDef.removeByIds("lms.carregamento.gerarControleCargasAction.removeByIdsIntegranteEquipe", 
		"retornoRemoveIntegrante");
}

function retornoRemoveIntegrante_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return;
	}
	setaDadosEquipe(idEquipeOld, dsEquipeOld);
	povoaGrid(idEquipeOld);
	setFocus(document.getElementById('tpIntegrante'));
}
/************************************ FIM - GRID ************************************/



/**
 * Deixa a tela em seu estado inicial
 */
function novoIntegranteForm(){
	newButtonScript();
	desabilitaCampos();
	setDisabled('novoIntegranteButton', false);
	setDisabled('salvarIntegranteButton', false);
	setDisabled('excluirIntegranteButton', false);
	setDadosMaster();
	setaDadosEquipe(idEquipeOld, dsEquipeOld);
	setFocus(document.getElementById('tpIntegrante'));
}

function salvarIntegrante(form) {
	if (!validateForm(form)) {
		return false;
	}
	storeButtonScript('lms.carregamento.gerarControleCargasAction.saveIntegranteEqOperac', 'retornoSalvarItem', document.forms[0]);
}

function retornoSalvarItem_cb(data, error) {
	if (error != undefined) {
		alert(error);
		setFocus(document.getElementById('tpIntegrante'));
		return;
	}
	else {
		showSuccessMessage();
		novoIntegranteForm();
		setaDadosEquipe(idEquipeOld, dsEquipeOld);
		povoaGrid(idEquipeOld);
		setFocus(document.getElementById('tpIntegrante'));
	}
}

function validaCamposItens(tpIntegrante) {
	resetValue('pessoa.idPessoa');
	resetValue('cargoOperacional.idCargoOperacional');
	resetValue('empresa.idEmpresa');
	resetValue('usuario.idUsuario');
	if (tpIntegrante == "F") {
		setDisabled('usuario.idUsuario', false);
		setDisabled('pessoa.idPessoa', true);
		setDisabled('cargoOperacional.idCargoOperacional', true);
		setDisabled('empresa.idEmpresa', true);

		document.getElementById("usuario.idUsuario").required = "true";
		document.getElementById("pessoa.idPessoa").required = "false";
		document.getElementById("empresa.idEmpresa").required = "false";
		document.getElementById("cargoOperacional.idCargoOperacional").required = "false";
	}
	else
	if (tpIntegrante == "T") {
		setDisabled('usuario.idUsuario', true);
		setDisabled('pessoa.idPessoa', false);
		setDisabled('empresa.idEmpresa', false);
		setDisabled('cargoOperacional.idCargoOperacional', false);

		document.getElementById("usuario.idUsuario").required = "false";
		document.getElementById("pessoa.idPessoa").required = "true";
		document.getElementById("empresa.idEmpresa").required = "true";
		document.getElementById("cargoOperacional.idCargoOperacional").required = "true";
	}
	else {
		desabilitaCampos();
		setDisabled('tpIntegrante', false);
	}
	
}

/**
 * callback do form 
 */
function retornoForm_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	validaCamposItens(data.tpIntegrante.value);

	if (getNestedBeanPropertyValue(data,"tpIntegrante") == "F") {
		// remove o nrIdentificacao e o cargo, para não mostrar na tela
		setNestedBeanPropertyValue(data,"pessoa.nrIdentificacao", "");
		setNestedBeanPropertyValue(data,"pessoa.nrIdentificacaoFormatado", "");
		setNestedBeanPropertyValue(data,"cargoOperacional.dsCargo", "");
	}
	onDataLoad_cb(data, error);
	setaDadosEquipe(idEquipeOld, dsEquipeOld);
	setDadosMaster();
	if (getElementValue("idControleCarga") != "" && getElementValue("idControleCarga") != -1) {
		desabilitaCampos();
		setDisabled('novoIntegranteButton', true);
		setDisabled('salvarIntegranteButton', true);
		setDisabled('excluirIntegranteButton', true);
	}
	else {
		setDisabled('excluirIntegranteButton', false);
		setFocus(document.getElementById('tpIntegrante'));
	}
}


function inicializaTela() {
	idEquipeOld = "";
	dsEquipeOld = "";
	resetDocumentValue(this.document);
	setDisabled('equipe.idEquipe', false);
	integranteEqOperacGridDef.disabled=false;
}


function desabilitaTela() {
	newButtonScript();
	integranteEqOperacGridDef.disabled=true;
	desabilitaCampos();
	setDisabled('novoIntegranteButton', true);
	setDisabled('salvarIntegranteButton', true);
	setDisabled('excluirIntegranteButton', true);
	setDisabled('equipe.idEquipe', true);
	setDisabled('tpIntegrante', true);
	setaDadosEquipe(idEquipeOld, dsEquipeOld);
}


function retornoGrid_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	if (getElementValue("idControleCarga") != "" && getElementValue("idControleCarga") != -1) {
		desabilitaTela();
	}
	else {
		setDisabled('equipe.idEquipe', false);
		if (getElementValue('equipe.dsEquipe') == "")
			inicializarCampos(true);
		else
			inicializarCampos(false);

		if (getElementValue("idEquipe") == "") {
			setaDadosEquipe(idEquipeOld, dsEquipeOld);
		}
	}
}
--></script>