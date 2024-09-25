<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="veiculosControleCarga" service="lms.carregamento.consultarControleCargasJanelasAction" 
			 onPageLoadCallBack="retornoCarregaPagina" >

	<adsm:form action="/carregamento/consultarControleCargasVeiculos">
		<adsm:section caption="veiculosControleCarga" />

		<adsm:hidden property="idControleCarga"/>
		<adsm:textbox dataType="text" label="controleCargas" property="filialByIdFilialOrigem.sgFilial"
					  size="3" width="85%" disabled="true" serializable="false" >
	 		<adsm:textbox dataType="integer" property="nrControleCarga" size="9" mask="00000000" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:buttonBar freeLayout="true">
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid idProperty="idVeiculoControleCarga" property="veiculosControleCarga" 
			   selectionMode="none" title="historicoVeiculos" autoSearch="false"
			   gridHeight="265" rows="13" unique="true" scrollBars="horizontal" 
			   service="lms.carregamento.consultarControleCargasJanelasAction.findPaginatedVeiculo"
			   rowCountService="lms.carregamento.consultarControleCargasJanelasAction.getRowCountVeiculo"
			   onRowClick="veiculosControleCarga_onClick"
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
						 onclick="showModalDialog('carregamento/consultarControleCargasPagamentoProprietario.do?cmd=main', window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:520px;');" />
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
		showModalDialog('carregamento/consultarControleCargasLocalTroca.do?cmd=descricao&idLocalTroca=' + idLocalTroca, window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:450px;dialogHeight:200px;');
}
</script>