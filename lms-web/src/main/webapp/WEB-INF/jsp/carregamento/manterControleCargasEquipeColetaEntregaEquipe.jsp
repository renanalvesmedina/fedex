<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window title="carregarVeiculo" service="lms.carregamento.manterControleCargasJanelasAction" 
			 onPageLoadCallBack="retornoCarregaPagina" >

	<adsm:form action="/carregamento/manterControleCargasEquipeColetaEntrega" idProperty="idEquipeOperacao" 
			   service="lms.carregamento.manterControleCargasJanelasAction.findByIdEquipeOperacao" onDataLoadCallBack="retornoCarregaDados" >

		<adsm:hidden property="blPermiteAlterar" serializable="false" />

		<adsm:hidden property="controleCarga.idControleCarga" />
		<adsm:hidden property="idEquipeOperacao" />

		<adsm:hidden property="filialUsuario.idFilial" serializable="false" />
		<adsm:hidden property="filialUsuario.sgFilial" serializable="false" />
		<adsm:hidden property="filialUsuario.pessoa.nmFantasia" serializable="false" />


		<adsm:hidden property="tpSituacao" value="A" serializable="false" />
		<adsm:lookup label="equipe" dataType="text" 
					 property="equipe" idProperty="idEquipe" criteriaProperty="dsEquipe"
					 action="/carregamento/manterEquipes" 
					 service="lms.carregamento.manterControleCargasJanelasAction.findLookupEquipe" 
					 width="85%" maxLength="50" size="50" required="true" 
					 minLengthForAutoPopUpSearch="3" exactMatch="false" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao"/>
		</adsm:lookup>

		<adsm:textbox label="inicioOperacao" property="dhInicioOperacao" dataType="JTDateTimeZone" disabled="true" 
					  picker="false" serializable="false" />

		<adsm:textbox label="fimOperacao" property="dhFimOperacao" dataType="JTDateTimeZone" disabled="true" 
					  picker="false" serializable="false" />

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="trocarEquipe" id="botaoTrocar" onclick="trocar_OnClick();" />
			<adsm:button caption="salvar" id="botaoSalvar" onclick="salvar_OnClick(this.form);" />
		</adsm:buttonBar>
	</adsm:form>


	<adsm:grid property="equipesOperacao" idProperty="idEquipeOperacao" 
			   service="lms.carregamento.manterControleCargasJanelasAction.findPaginatedEquipe"
			   rowCountService="lms.carregamento.manterControleCargasJanelasAction.getRowCountEquipe"
			   onRowClick="equipeOperacaoOnRowClick"
			   gridHeight="250" selectionMode="none" rows="9" autoSearch="false" >
		<adsm:gridColumn title="equipe" 		property="equipe.dsEquipe" width="60%" />
		<adsm:gridColumn title="inicioOperacao" property="dhInicioOperacao" width="20%" dataType="JTDateTimeZone" align="center"/>
		<adsm:gridColumn title="fimOperacao" 	property="dhFimOperacao" width="20%" dataType="JTDateTimeZone" align="center"/>
		<adsm:buttonBar>
			<adsm:button caption="fechar" id="botaoFechar" onclick="javascript:window.close();" disabled="false" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>
function initWindow(eventObj) {
	carregaDadosMaster();
	if (eventObj.name == "tab_load") {
		desabilitaAbaIntegrantes(true);
		setDisabled("equipe.idEquipe", true);
		setDisabled("botaoSalvar", true);
		setDisabled("botaoFechar", false);
		habilitaBotaoTrocar();
	}
	else 
	if (eventObj.name == "tab_click") {
		setDisabled("botaoSalvar", true);
		setDisabled("botaoFechar", false);
		habilitaBotaoTrocar();
	}
	else
		setFocusOnFirstFocusableField();
}


function habilitaBotaoTrocar() {
	if (getElementValue("blPermiteAlterar") == "true") {
		setDisabled("botaoTrocar", false);
		setFocus(document.getElementById("botaoTrocar"), true, true);
	}
	else {
		setDisabled("botaoTrocar", true);
		setFocus(document.getElementById("botaoFechar"), true, true);
	}
}


function retornoCarregaPagina_cb(data, error) {
	onPageLoad_cb(data, error);
	if (error == undefined) {
		povoaGrid();
	}
}

function retornoCarregaDados_cb(data, error) {
	onDataLoad_cb(data, error);
	setDisabled("equipe.idEquipe", true);
	setDisabled("botaoSalvar", true);
	setDisabled("botaoFechar", false);
	setElementValue("idEquipeOperacao", getNestedBeanPropertyValue(data, "idEquipeOperacao"));
	carregaDadosMaster();
	habilitaBotaoTrocar();
}

function trocar_OnClick() {
	desabilitaAbaIntegrantes(true);
	resetValue(this.document);
	carregaDadosMaster();
	setDisabled("equipe.idEquipe", false);
	setDisabled("botaoSalvar", false);
	setFocusOnFirstFocusableField();
}

function salvar_OnClick(form) {
	if (!validateForm(form))
		return false;
		
	var sdo = createServiceDataObject("lms.carregamento.manterControleCargasJanelasAction.generateTrocaEquipe", "resultado_generateTrocaEquipe", 
		{idControleCarga:getElementValue("controleCarga.idControleCarga"),
		idEquipe:getElementValue("equipe.idEquipe")}
	);
   	xmit({serviceDataObjects:[sdo]});
}


function resultado_generateTrocaEquipe_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	desabilitaAbaIntegrantes(false);
	setDisabled("equipe.idEquipe", true);
	setDisabled("botaoSalvar", true);
	setElementValue("idEquipeOperacao", getNestedBeanPropertyValue(data, "idEquipeOperacao"));
	setElementValue("dhInicioOperacao", setFormat(document.getElementById("dhInicioOperacao"), getNestedBeanPropertyValue(data,"dhInicioOperacao")));
	povoaGrid();
}

function carregaDadosMaster() {
	setElementValue("controleCarga.idControleCarga", dialogArguments.window.document.getElementById('idControleCarga').value);
	setElementValue("filialUsuario.idFilial", dialogArguments.window.document.getElementById('filialUsuario.idFilial').value);
	setElementValue("filialUsuario.sgFilial", dialogArguments.window.document.getElementById('filialUsuario.sgFilial').value);
	setElementValue("filialUsuario.pessoa.nmFantasia", dialogArguments.window.document.getElementById('filialUsuario.pessoa.nmFantasia').value);
	setElementValue("blPermiteAlterar", dialogArguments.window.document.getElementById('blPermiteAlterar').value);
}

function povoaGrid() {
	var filtro = new Array();
    setNestedBeanPropertyValue(filtro, "controleCarga.idControleCarga", getElementValue("controleCarga.idControleCarga"));
    equipesOperacaoGridDef.executeSearch(filtro, true);
    return false;
}

function equipeOperacaoOnRowClick(idEquipeOperacao) {
	onDataLoad(idEquipeOperacao);
	desabilitaAbaIntegrantes(false);
	return false;
}

function desabilitaAbaIntegrantes(valor) {
	getTabGroup(this.document).setDisabledTab("integrantes", valor);
}
</script>