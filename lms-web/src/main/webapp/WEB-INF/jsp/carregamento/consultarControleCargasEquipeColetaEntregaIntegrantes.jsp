<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.carregamento.consultarControleCargasJanelasAction">
	<adsm:form action="/carregamento/consultarControleCargasEquipeColetaEntrega" idProperty="idIntegranteEqOperac" 
			   service="lms.carregamento.consultarControleCargasJanelasAction.findByIdIntegranteEqOperac" 
			   onDataLoadCallBack="retornoCarregaDados" >

		<adsm:hidden property="equipeOperacao.idEquipeOperacao"/>

		<adsm:textbox property="tpIntegrante.description" dataType="text" label="contratacao" width="85%" 
					  disabled="true" serializable="false" />

		<adsm:textbox property="usuario.nrMatricula" label="funcionario" 
            		 dataType="integer" size="16" maxLength="16" width="85%" disabled="true" serializable="false" >
        	<adsm:textbox dataType="text" property="usuario.nmUsuario" size="30" maxLength="45" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textbox property="pessoa.nrIdentificacaoFormatado" dataType="text" 
					  label="integrante" size="18" maxLength="20" width="85%" disabled="true" serializable="false" >
			<adsm:textbox property="pessoa.nmPessoa" dataType="text" size="30" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textbox property="cargoOperacional.dsCargo" label="cargo" dataType="text" 
					  width="85%" disabled="true" serializable="false" />

		<adsm:textbox property="empresa.pessoa.nrIdentificacaoFormatado" dataType="text"
					  label="empresa" size="18" maxLength="20" width="85%" disabled="true" serializable="false"  >
			<adsm:textbox dataType="text" property="empresa.pessoa.nmPessoa" size="30" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:buttonBar freeLayout="true">
		</adsm:buttonBar>
	</adsm:form>


	<adsm:grid property="integrantesEqOperac" idProperty="idIntegranteEqOperac" scrollBars="horizontal" selectionMode="none"
			   service="lms.carregamento.consultarControleCargasJanelasAction.findPaginatedIntegranteEqOperac" 
			   rowCountService="lms.carregamento.consultarControleCargasJanelasAction.getRowCountIntegranteEqOperac"
			   gridHeight="180" unique="true" rows="9" autoSearch="false" onRowClick="populaForm" >
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
		resetaCampos();
	    carregaDadosMaster();
	    povoaGrid();
	}
	setDisabled("botaoFechar", false);
	setFocusOnFirstFocusableField();
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
}

function populaForm(valor) {
	onDataLoad(valor);
	return false;
}

function retornoCarregaDados_cb(data, error) {
	onDataLoad_cb(data, error);
	carregaDadosMaster();
	setDisabled("botaoFechar", false);
}


function resetaCampos() {
	resetValue("tpIntegrante.description");
	resetValue("usuario.nrMatricula");
	resetValue("usuario.nmUsuario");
	resetValue("pessoa.nrIdentificacaoFormatado");
	resetValue("pessoa.nmPessoa");
	resetValue("cargoOperacional.dsCargo");
	resetValue("empresa.pessoa.nrIdentificacaoFormatado");
	resetValue("empresa.pessoa.nmPessoa");
}
</script>