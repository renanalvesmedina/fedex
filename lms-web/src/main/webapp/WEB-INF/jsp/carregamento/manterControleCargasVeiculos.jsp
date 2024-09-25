<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterControleCargasVeiculos" service="lms.carregamento.manterControleCargasJanelasAction" 
			 onPageLoadCallBack="retornoCarregaPagina" >

	<adsm:form action="/carregamento/manterControleCargasVeiculos">
		<adsm:section caption="veiculosControleCarga" />

		<adsm:hidden property="tpControleCarga" serializable="false" />
		<adsm:hidden property="blEntregaDireta" serializable="false" />
		<adsm:hidden property="idRotaIdaVolta" serializable="false" />
		<adsm:hidden property="blPermiteAlterar" serializable="false" />
		<adsm:hidden property="blPermiteAlterarPgtoProprietario" serializable="false" />

		<adsm:hidden property="idControleCarga"/>
		<adsm:textbox dataType="text" label="controleCargas" property="filialByIdFilialOrigem.sgFilial"
					  size="3" width="85%" disabled="true" serializable="false" >
	 		<adsm:textbox dataType="integer" property="nrControleCarga" size="9" mask="00000000" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="trocarVeiculo" id="botaoTrocarVeiculo" disabled="false" onclick="abrirTrocarMeioTransporte();" />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid idProperty="idVeiculoControleCarga" property="veiculosControleCarga" 
			   selectionMode="none" title="historicoVeiculos" autoSearch="false"
			   gridHeight="265" rows="13" unique="true" scrollBars="horizontal" 
			   service="lms.carregamento.manterControleCargasJanelasAction.findPaginatedVeiculo"
			   rowCountService="lms.carregamento.manterControleCargasJanelasAction.getRowCountVeiculo"
			   onRowClick="veiculosControleCarga_onClick"
			   onDataLoadCallBack="retornoGrid"
			   >
		<adsm:gridColumn title="meioTransporte"			property="nrFrota" width="50" />
		<adsm:gridColumn title="" 						property="nrIdentificador" width="100" />
		<adsm:gridColumn title="proprietario" 			property="nmPessoaProprietario" width="220" />
		<adsm:gridColumnGroup separatorType="CONTROLE_CARGA">
			<adsm:gridColumn title="solicitacaoContratacao"	property="sgFilialSolicitacaoContratacao" width="45" />
			<adsm:gridColumn title=""						property="nrSolicitacaoContratacao" width="110" align="right" dataType="integer" mask="0000000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="dataHoraTroca" 			property="dhTroca" dataType="JTDateTimeZone" align="center" width="130" />
		<adsm:gridColumn title="trecho" 				property="trecho" width="100" />
		<adsm:gridColumn title="rodovia" 				property="sgRodovia" width="80" />
		<adsm:gridColumn title="km" 					property="nrKmRodoviaTroca" width="50" align="right" />
		<adsm:gridColumn title="municipio" 				property="nmMunicipio" width="200" />
		<adsm:gridColumn title="uf" 					property="sgUnidadeFederativa" width="50" />
		<adsm:gridColumn title="descricaoLocal" 		property="descricaoLocal" image="/images/popup.gif" link="javascript:exibirDescricaoLocal" openPopup="true" width="120" align="center" />
		<adsm:gridColumn title="usuario"			 	property="nmUsuarioAlteraStatus" width="200" />
		<adsm:buttonBar>
			<adsm:button caption="pagamentoProprietario" id="botaoPagamento" disabled="false" 
						 onclick="showModalDialog('carregamento/manterControleCargasPagamentoProprietario.do?cmd=main', window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:520px;');" />
			<adsm:button caption="fechar" id="botaoFechar" onclick="javascript:window.close();" disabled="false" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>
function veiculosControleCarga_onClick(id) {
	return false;
}

function retornoCarregaPagina_cb(data, error) {
	onPageLoad_cb(data, error);
	if (error == undefined) {
		setElementValue("idControleCarga", dialogArguments.window.document.getElementById('idControleCarga').value);
		setElementValue("filialByIdFilialOrigem.sgFilial", dialogArguments.window.document.getElementById('filialByIdFilialOrigem.sgFilial').value);
		setElementValue("nrControleCarga", dialogArguments.window.document.getElementById('nrControleCarga').value);
		setElementValue("tpControleCarga", dialogArguments.window.document.getElementById('tpControleCargaValor').value);
		setElementValue("idRotaIdaVolta", dialogArguments.window.document.getElementById('rotaIdaVolta.idRotaIdaVolta').value);
		setElementValue("blEntregaDireta", getElementValue(dialogArguments.window.document.getElementById('blEntregaDireta')) );
		setElementValue("blPermiteAlterar", dialogArguments.window.document.getElementById('blPermiteAlterar').value);
		setElementValue("blPermiteAlterarPgtoProprietario", dialogArguments.window.document.getElementById('blPermiteAlterarPgtoProprietario').value);
		if (getElementValue("blPermiteAlterar") == "false") {
			setDisabled("botaoTrocarVeiculo", true);
		}
		povoaGrid(getElementValue("idControleCarga"));
	}
}

function povoaGrid(idControleCarga) {
	var filtro = new Array();
    setNestedBeanPropertyValue(filtro, "controleCarga.idControleCarga", idControleCarga);
    veiculosControleCargaGridDef.executeSearch(filtro, true);
    return false;
}

function exibirDescricaoLocal(id) {
	var idLocalTroca = veiculosControleCargaGridDef.findById(id).idLocalTroca;
	if (idLocalTroca != undefined && idLocalTroca != "")
		showModalDialog('carregamento/manterControleCargasLocalTroca.do?cmd=descricao&idLocalTroca=' + idLocalTroca, window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:450px;dialogHeight:200px;');
}

function abrirTrocarMeioTransporte() {
	showModalDialog('carregamento/manterControleCargasLocalTroca.do?cmd=veiculos',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:520px;');
	povoaGrid(getElementValue("idControleCarga"));
}

function retornoGrid_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	setFocus(document.getElementById("botaoFechar"), true, true);
}
</script>