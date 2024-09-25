<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
function carregaPagina() {
	setMasterLink(document, true);
	povoaForm();
}
</script>
<adsm:window title="programacaoColetasVeiculos" service="lms.coleta.programacaoColetasVeiculosDadosEquipeAction" onPageLoad="carregaPagina" >
	<adsm:form action="/coleta/programacaoColetasVeiculos" idProperty="idControleCarga" onDataLoadCallBack="carregaDados_retorno"
			   service="lms.coleta.programacaoColetasVeiculosDadosEquipeAction.findById" >
		<adsm:section caption="dadosEquipe" />

		<adsm:textbox label="meioTransporte" property="meioTransporteByIdTransportado.nrFrota" dataType="text" size="6" width="85%" disabled="true" >
			<adsm:textbox dataType="text" property="meioTransporteByIdTransportado.nrIdentificador" size="20" disabled="true" />
		</adsm:textbox>

		<adsm:textbox label="controleCargas" dataType="text" property="filialByIdFilialOrigem.sgFilial" size="3" width="85%" disabled="true" >
			<adsm:textbox dataType="integer" property="nrControleCarga" size="10" mask="00000000" disabled="true"/>
		</adsm:textbox>
	</adsm:form>


	<adsm:grid property="equipes" idProperty="idEquipeOperacao" 
			   selectionMode="none" showPagging="false" gridHeight="60" rows="3" scrollBars="vertical" unique="false" 
			   service="lms.coleta.programacaoColetasVeiculosDadosEquipeAction.findPaginatedDadosEquipe" 
			   onRowClick="povoaGridIntegrantes" onDataLoadCallBack="retornoGridEquipe" >
		<adsm:gridColumn title="equipe" 		property="equipe.dsEquipe" width="60%" align="left" />
		<adsm:gridColumn title="inicioOperacao" property="dhInicioOperacao" width="20%" align="center" dataType="JTDateTimeZone" />
		<adsm:gridColumn title="fimOperacao" 	property="dhFimOperacao" width="20%" align="center" dataType="JTDateTimeZone" />
	</adsm:grid>


	<adsm:grid property="integrantes" idProperty="idIntegranteEqOperac" 
			   selectionMode="none" showPagging="false" gridHeight="200" scrollBars="vertical" unique="false" 
			   onRowClick="integrantes_OnClick"
			   service="lms.coleta.programacaoColetasVeiculosDadosEquipeAction.findPaginatedIntegrantes" 
			   title="integrantes" >
		<adsm:gridColumn title="cargo" 		 	property="cargoOperacional.dsCargo" width="17%"  />
		<adsm:gridColumn title="contratacao" 	property="tpIntegrante" isDomain="true" width="13%" />
		<adsm:gridColumn title="nome" 		 	property="pessoa.nmPessoa" width="25%" />
		<adsm:gridColumn title="matricula"		property="usuario.nrMatricula" width="15%" align="right" />
		<adsm:gridColumn title="cpf" 			property="pessoa.nrIdentificacaoFormatado" width="15%" align="right" />
		<adsm:gridColumn title="cooperativa" 	property="empresa.pessoa.nmPessoa" width="15%" />
	</adsm:grid>
	<%-- Esse form foi declarado para fazer o highlight em verde no botão fechar --%>
	<form>
		<adsm:buttonBar> 
			<adsm:button caption="fechar" id="botaoFechar" onclick="javascript:window.close();" disabled="false" />
		</adsm:buttonBar>
	</form>
</adsm:window>

<script>
function povoaForm() {
	var idControleCarga = getElementValue('idControleCarga');
	onDataLoad(idControleCarga);
	povoaGridEquipes(idControleCarga);
	setarFocoBotaoFechar();
}

function povoaGridEquipes(idControleCarga) {
      var filtro = new Array();
      setNestedBeanPropertyValue(filtro, "controleCarga.idControleCarga", idControleCarga);
      equipesGridDef.executeSearch(filtro);
      return false;
}

function povoaGridIntegrantes(idEquipeOperacao) {
      var filtro = new Array();
      setNestedBeanPropertyValue(filtro, "equipeOperacao.idEquipeOperacao", idEquipeOperacao);
      integrantesGridDef.executeSearch(filtro);
      return false;
}

function integrantes_OnClick(idIntegranteEqOperac) {
	return false;
}

function carregaDados_retorno_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	onDataLoad_cb(data);
	format(document.getElementById("nrControleCarga"));
}

function setarFocoBotaoFechar() {
	setDisabled('botaoFechar', false);
	setFocus(document.getElementById("botaoFechar"), true, true);
}


function retornoGridEquipe_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return;
	}
	if (data != undefined) {
		var idEquipeOperacao = getNestedBeanPropertyValue(data, "0:idEquipeOperacao");
		if (idEquipeOperacao != undefined) {
			povoaGridIntegrantes(idEquipeOperacao);
		}
	}
}
</script>