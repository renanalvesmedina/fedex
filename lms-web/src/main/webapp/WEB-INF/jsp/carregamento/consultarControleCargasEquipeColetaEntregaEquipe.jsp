<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window title="carregarVeiculo" service="lms.carregamento.consultarControleCargasJanelasAction" 
			 onPageLoadCallBack="retornoCarregaPagina" >

	<adsm:form action="/carregamento/consultarControleCargasEquipeColetaEntrega" idProperty="idEquipeOperacao" 
			   service="lms.carregamento.consultarControleCargasJanelasAction.findByIdEquipeOperacao" onDataLoadCallBack="retornoCarregaDados" >

		<adsm:hidden property="controleCarga.idControleCarga" />
		<adsm:hidden property="idEquipeOperacao" />

		<adsm:textbox label="equipe" dataType="text" property="equipe.dsEquipe"
					  width="85%" maxLength="50" size="50" disabled="true" serializable="false" />

		<adsm:textbox label="inicioOperacao" property="dhInicioOperacao" dataType="JTDateTimeZone" disabled="true" 
					  picker="false" serializable="false" />

		<adsm:textbox label="fimOperacao" property="dhFimOperacao" dataType="JTDateTimeZone" disabled="true" 
					  picker="false" serializable="false" />

		<adsm:buttonBar freeLayout="true">
		</adsm:buttonBar>
	</adsm:form>


	<adsm:grid property="equipesOperacao" idProperty="idEquipeOperacao" 
			   service="lms.carregamento.consultarControleCargasJanelasAction.findPaginatedEquipe"
			   rowCountService="lms.carregamento.consultarControleCargasJanelasAction.getRowCountEquipe"
			   onRowClick="equipeOperacaoOnRowClick"
			   onDataLoadCallBack="retornoCarregaGrid"
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
	}
	setDisabled("botaoFechar", false);
	setFocusOnFirstFocusableField();
}

function retornoCarregaPagina_cb(data, error) {
	onPageLoad_cb(data, error);
	if (error == undefined) {
		povoaGrid();
	}
}

function retornoCarregaDados_cb(data, error) {
	onDataLoad_cb(data, error);
	setDisabled("botaoFechar", false);
}

function carregaDadosMaster() {
	setElementValue("controleCarga.idControleCarga", dialogArguments.window.document.getElementById('idControleCarga').value);
}

function povoaGrid() {
	var filtro = new Array();
    setNestedBeanPropertyValue(filtro, "controleCarga.idControleCarga", getElementValue("controleCarga.idControleCarga"));
    equipesOperacaoGridDef.executeSearch(filtro, true);
    return false;
}

function equipeOperacaoOnRowClick(idEquipeOperacao) {
	onDataLoad(idEquipeOperacao);
	setElementValue("idEquipeOperacao", idEquipeOperacao);
	desabilitaAbaIntegrantes(false);
	return false;
}

function desabilitaAbaIntegrantes(valor) {
	getTabGroup(this.document).setDisabledTab("integrantes", valor);
}

function retornoCarregaGrid_cb(data, error) {
	setFocus(document.getElementById("botaoFechar"), true, true);
}
</script>