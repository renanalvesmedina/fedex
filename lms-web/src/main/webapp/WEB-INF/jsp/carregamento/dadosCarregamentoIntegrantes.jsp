<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.carregamento.dadosCarregamentoAction">
	<adsm:form action="/carregamento/dadosCarregamento" idProperty="idIntegranteEqOperac" >

		<adsm:hidden property="equipeOperacao.idEquipeOperacao" />

		<adsm:textbox label="controleCargas" property="sgFilialControleCarga" dataType="text" 
					  size="3" labelWidth="18%" width="82%" disabled="true" serializable="false" >
	 		<adsm:textbox property="nrControleCarga" dataType="integer" size="9" mask="00000000" disabled="true" serializable="false" />
		</adsm:textbox>
	</adsm:form>


	<adsm:grid property="integrantesEqOperac" idProperty="idIntegranteEqOperac" scrollBars="horizontal" selectionMode="none"
			   service="lms.carregamento.dadosCarregamentoAction.findPaginatedIntegranteEqOperac" 
			   rowCountService="lms.carregamento.dadosCarregamentoAction.getRowCountIntegranteEqOperac"
			   gridHeight="290" unique="true" rows="14" autoSearch="false" onRowClick="populaForm" >
		<adsm:gridColumn title="nome" 			property="nmIntegranteEquipe" width="200" />
		<adsm:gridColumn title="contratacao" 	property="tpIntegrante" isDomain="true" width="145" />
		<adsm:gridColumn title="matricula" 		property="usuario.nrMatricula" width="75" align="right" />
		<adsm:gridColumn title="identificacao"	property="pessoa.nrIdentificacaoFormatado" width="110" align="right" />
		<adsm:gridColumn title="cargo" 			property="cargoOperacional.dsCargo" width="170" /> 
		<adsm:gridColumn title="empresa" 		property="empresa.pessoa.nmPessoa" width="170" />

		<adsm:buttonBar>
			<adsm:button caption="fechar" id="botaoFechar" onclick="javascript:window.close();" disabled="false" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>
function initWindow(eventObj) {
	if (eventObj.name == "tab_click") {
	    povoaDadosMaster();
	    povoaGrid();
	}
	setDisabled("botaoFechar", false);
	setFocus(document.getElementById("botaoFechar"), true, true);
	setFocusOnFirstFocusableField();
}

function povoaGrid() {
	var filtro = new Array();
    setNestedBeanPropertyValue(filtro, "idEquipeOperacao", getElementValue("equipeOperacao.idEquipeOperacao"));
    integrantesEqOperacGridDef.executeSearch(filtro, true);
}

function povoaDadosMaster() {
    var tabDet = getTabGroup(this.document).getTab("list");
    setElementValue("sgFilialControleCarga", tabDet.getFormProperty("sgFilialControleCarga"));
    setElementValue("nrControleCarga", tabDet.getFormProperty("nrControleCarga"));
	setElementValue("equipeOperacao.idEquipeOperacao", tabDet.getFormProperty("equipeOperacao.idEquipeOperacao"));
}

function populaForm(valor) {
	return false;
}
</script>