<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.carregamento.manterControleCargasJanelasAction">
	<adsm:form action="/carregamento/manterControleCargasEquipeColetaEntrega" idProperty="idIntegranteEqOperac" 
			   service="lms.carregamento.manterControleCargasJanelasAction.findByIdIntegranteEqOperac" 
			   onDataLoadCallBack="retornoCarregaDados" >

		<adsm:hidden property="equipeOperacao.idEquipeOperacao"/>
		<adsm:hidden property="versao"/>
		<adsm:hidden property="tpSituacao" value="A" serializable="false" />
		<adsm:hidden property="blPermiteEdicao" serializable="false" />

		<adsm:hidden property="filialUsuario.idFilial" serializable="false" />
		<adsm:hidden property="filialUsuario.sgFilial" serializable="false" />
		<adsm:hidden property="filialUsuario.pessoa.nmFantasia" serializable="false" />

		<adsm:combobox property="tpIntegrante" domain="DM_INTEGRANTE_EQUIPE" renderOptions="true"
					   onchange="return tpIntegrante_OnChange(this)" onlyActiveValues="true"
			  		   label="contratacao" width="85%" required="true"/>

		<adsm:lookup property="usuario" label="funcionario" 
					 idProperty="idUsuario" criteriaProperty="nrMatricula" 
                     service="lms.carregamento.manterControleCargasJanelasAction.findLookupUsuarioFuncionario" 
            		 action="/configuracoes/consultarFuncionariosView"
            		 dataType="integer" size="16" maxLength="16" width="85%" >
        	<adsm:propertyMapping modelProperty="nmUsuario" relatedProperty="usuario.nmUsuario" />
        	<adsm:propertyMapping modelProperty="nmUsuario" criteriaProperty="usuario.nmUsuario" disable="false" />
			<adsm:propertyMapping modelProperty="filial.idFilial" criteriaProperty="filialUsuario.idFilial" />
			<adsm:propertyMapping modelProperty="filial.sgFilial" criteriaProperty="filialUsuario.sgFilial" />
			<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" criteriaProperty="filialUsuario.pessoa.nmFantasia" />
        	<adsm:textbox dataType="text" property="usuario.nmUsuario" size="30" maxLength="45" disabled="true" />
		</adsm:lookup>


		<adsm:hidden property="pessoa.tpPessoa" value="F" serializable="false" />
		<adsm:hidden property="pessoa.tpIdentificacao" serializable="false" />

		<adsm:lookup property="pessoa" dataType="text" 
					 idProperty="idPessoa" 
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="nrIdentificacaoFormatado"
					 action="/configuracoes/manterPessoas" 
					 service="lms.carregamento.manterControleCargasJanelasAction.findLookupPessoa" 
					 label="integrante" size="18" maxLength="20" width="85%" serializable="true" >
			<adsm:propertyMapping modelProperty="nmPessoa" relatedProperty="pessoa.nmPessoa" />
			<adsm:propertyMapping modelProperty="tpIdentificacao.value" relatedProperty="pessoa.tpIdentificacao" />	
			<adsm:propertyMapping modelProperty="pessoa.tpPessoa" criteriaProperty="pessoa.tpPessoa" />
			<adsm:propertyMapping modelProperty="nmPessoa" criteriaProperty="pessoa.nmPessoa" disable="false"/>	
			<adsm:propertyMapping modelProperty="pessoa.tpIdentificacao" criteriaProperty="pessoa.tpIdentificacao" disable="false"/>	
			<adsm:propertyMapping modelProperty="pessoa.nrIdentificacao" criteriaProperty="pessoa.pessoa.nrIdentificacao" disable="false"/>
			<adsm:textbox property="pessoa.nmPessoa" dataType="text" size="30" disabled="true" serializable="false" />
		</adsm:lookup>


		<adsm:combobox property="cargoOperacional.idCargoOperacional" label="cargo"
					   service="lms.carregamento.manterControleCargasJanelasAction.findCargoOperacional" 
					   optionProperty="idCargoOperacional" optionLabelProperty="dsCargo" onlyActiveValues="true"
					   boxWidth="305" width="85%" />

		<adsm:lookup property="empresa" dataType="text"
					 idProperty="idEmpresa" 
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 service="lms.carregamento.manterControleCargasJanelasAction.findLookupEmpresa" 
					 action="/municipios/manterEmpresas" 
					 label="empresa" size="18" maxLength="20" width="85%" serializable="true" >
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="empresa.pessoa.nmPessoa" />
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" criteriaProperty="empresa.pessoa.nmPessoa" disable="false" />
			<adsm:textbox dataType="text" property="empresa.pessoa.nmPessoa" size="30" disabled="true" serializable="false" />
		</adsm:lookup>


		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvarIntegrante" id="botaoSalvarIntegrante" 
							  service="lms.carregamento.manterControleCargasJanelasAction.storeIntegranteEqOperac" />
			<adsm:button caption="limpar" id="botaoNovoIntegrante" onclick="novoIntegranteForm();" />
		</adsm:buttonBar>
	</adsm:form>


	<adsm:grid property="integrantesEqOperac" idProperty="idIntegranteEqOperac" scrollBars="horizontal"
			   service="lms.carregamento.manterControleCargasJanelasAction.findPaginatedIntegranteEqOperac" 
			   rowCountService="lms.carregamento.manterControleCargasJanelasAction.getRowCountIntegranteEqOperac"
			   gridHeight="180" unique="true" rows="9" autoSearch="false" onRowClick="populaForm" >
		<adsm:gridColumn title="nome" 			property="nmIntegranteEquipe" width="200" />
		<adsm:gridColumn title="contratacao" 	property="tpIntegrante" isDomain="true" width="145" />
		<adsm:gridColumn title="matricula" 		property="usuario.nrMatricula" width="75" align="right" />
		<adsm:gridColumn title="identificacao"	property="pessoa.nrIdentificacaoFormatado" width="110" align="right" />
		<adsm:gridColumn title="cargo" 			property="cargoOperacional.dsCargo" width="170" /> 
		<adsm:gridColumn title="empresa" 		property="empresa.pessoa.nmPessoa" width="170" />
		<adsm:buttonBar >
			<adsm:removeButton caption="excluirIntegrante" id="botaoExcluirIntegrante" 
							   service="lms.carregamento.manterControleCargasJanelasAction.removeByIdsIntegranteEqOperac" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>
function initWindow(eventObj) {
	setDisabled("botaoNovoIntegrante", false);
	if (eventObj.name == "tab_click") {
	    resetaCampos();
		resetValue("tpIntegrante");
	    desabilitaCampos();
	    carregaDadosMaster();
	    povoaGrid();
	    if (getElementValue("blPermiteEdicao") == "false") {
			setDisabled('tpIntegrante', true);
			setDisabled('botaoSalvarIntegrante', true);
			setDisabled('botaoNovoIntegrante', true);
			integrantesEqOperacGridDef.disabled = true;
		}
		else {
			setDisabled('tpIntegrante', false);
			setDisabled('botaoSalvarIntegrante', false);
			setDisabled('botaoNovoIntegrante', false);
			integrantesEqOperacGridDef.disabled = false;
		}
	}
	else
	if (eventObj.name == "storeButton") {
		povoaGrid();
		setFocus(document.getElementById("botaoNovoIntegrante"));
	}
	else
	if (eventObj.name == "removeButton_grid") {
		resetValue('tpIntegrante');
		resetaCampos();
		desabilitaCampos();
	}
}

function povoaGrid() {
	var filtro = new Array();
    setNestedBeanPropertyValue(filtro, "idEquipeOperacao", getElementValue("equipeOperacao.idEquipeOperacao"));
    integrantesEqOperacGridDef.executeSearch(filtro, true);
}

function carregaDadosMaster() {
	var tabGroup = getTabGroup(this.document);
    var abaEquipe = tabGroup.getTab("equipe");
    setElementValue("equipeOperacao.idEquipeOperacao", abaEquipe.getElementById("idEquipeOperacao").value);
    setElementValue("filialUsuario.idFilial", abaEquipe.getElementById("filialUsuario.idFilial").value);
	setElementValue("filialUsuario.sgFilial", abaEquipe.getElementById("filialUsuario.sgFilial").value);
	setElementValue("filialUsuario.pessoa.nmFantasia", abaEquipe.getElementById("filialUsuario.pessoa.nmFantasia").value);
	if (abaEquipe.getElementById("dhFimOperacao").value != "")
    	setElementValue("blPermiteEdicao", "false");
    else
    	setElementValue("blPermiteEdicao", "true");
}

function populaForm(valor) {
	onDataLoad(valor);
	return false;
}

function retornoCarregaDados_cb(data, error) {
	onDataLoad_cb(data, error);
	carregaDadosMaster();
	if (error == undefined && getElementValue("blPermiteEdicao") == "true") {
		setDisabled('tpIntegrante', false);
		validaCamposItens(getElementValue("tpIntegrante"));
	}
	else {
		setDisabled('tpIntegrante', true);
		setDisabled('botaoSalvarIntegrante', true);
		setDisabled('botaoNovoIntegrante', true);
	}
}

function tpIntegrante_OnChange(combo) {
	resetaCampos();
	validaCamposItens(getElementValue('tpIntegrante'));
	return comboboxChange({e:combo});
}

function validaCamposItens(tpIntegrante) {
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

function resetaCampos() {
	resetValue('pessoa.idPessoa');
	resetValue('cargoOperacional.idCargoOperacional');
	resetValue('empresa.idEmpresa');
	resetValue('usuario.idUsuario');
}

function desabilitaCampos() {
	setDisabled('usuario.idUsuario', true);
	setDisabled("usuario.nmUsuario", true);
	setDisabled('pessoa.idPessoa', true);
	setDisabled('cargoOperacional.idCargoOperacional', true);
	setDisabled('empresa.idEmpresa', true);
}

/**
 * Deixa a tela em seu estado inicial
 */
function novoIntegranteForm(){
	newButtonScript();
	carregaDadosMaster();
	desabilitaCampos();
	setDisabled('botaoNovoIntegrante', false);
	setDisabled('botaoSalvarIntegrante', false);
	setFocus(document.getElementById('tpIntegrante'));
}
</script>