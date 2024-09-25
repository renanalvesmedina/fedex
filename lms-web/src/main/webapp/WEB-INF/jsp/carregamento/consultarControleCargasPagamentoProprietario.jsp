<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="pagamentoProprietario" service="lms.carregamento.consultarControleCargasJanelasAction" 
			 onPageLoadCallBack="retornoCarregaPagina" >

	<adsm:form action="/carregamento/consultarControleCargasVeiculos" idProperty="idPagtoProprietarioCc" onDataLoadCallBack="retorno_carregaDados"
			   service="lms.carregamento.consultarControleCargasJanelasAction.findByIdPagtoProprietario" >

		<adsm:section caption="pagamentoProprietario" />

		<adsm:hidden property="tpVinculo" serializable="false" />
		<adsm:hidden property="controleCarga.idControleCarga" serializable="false" />

		<adsm:textbox dataType="text" label="controleCargas" property="controleCarga.filialByIdFilialOrigem.sgFilial"
					  size="3" labelWidth="21%" width="79%" disabled="true" serializable="false" >
	 		<adsm:textbox dataType="integer" property="controleCarga.nrControleCarga" size="9" mask="00000000" disabled="true" serializable="false" />
		</adsm:textbox>


		<adsm:textbox dataType="text" property="controleCarga.moeda.siglaSimbolo" label="valorTotalFreteCarreteiro" 
					  size="9" labelWidth="21%" width="79%" disabled="true" >
			<adsm:textbox property="controleCarga.vlFreteCarreteiro" dataType="currency" disabled="true"/>
		</adsm:textbox>

		<adsm:hidden property="proprietario.idProprietario" />
		<adsm:textbox label="proprietario" property="proprietario.pessoa.nrIdentificacaoFormatado"
					 dataType="text" size="20" labelWidth="21%" width="79%" disabled="true" serializable="false" >
			<adsm:textbox dataType="text" property="proprietario.pessoa.nmPessoa" size="30" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textbox label="solicitContratacao" property="veiculoControleCarga.solicitacaoContratacao.filial.sgFilial"
					 dataType="text" size="3" labelWidth="21%" width="79%" disabled="true" serializable="false" >
			<adsm:textbox dataType="integer" property="veiculoControleCarga.solicitacaoContratacao.nrSolicitacaoContratacao" 
						  mask="0000000000" size="10" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textbox property="moeda.siglaSimbolo" dataType="text" label="valor" size="9"
					  labelWidth="21%" width="79%" disabled="true" serializable="false" >
			<adsm:textbox dataType="currency" property="vlPagamento" disabled="true" serializable="false" />
		</adsm:textbox>


		<adsm:buttonBar freeLayout="true">
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid idProperty="idPagtoProprietarioCc" property="pagtoProprietarios" 
			   selectionMode="none" gridHeight="220" rows="11" unique="true" 
			   autoSearch="false" onRowClick="populaForm"
			   service="lms.carregamento.consultarControleCargasJanelasAction.findPaginatedPagtoProprietario"
			   rowCountService="lms.carregamento.consultarControleCargasJanelasAction.getRowCountPagtoProprietario"
			   >
		<adsm:gridColumn title="identificacao" 			property="proprietario.pessoa.tpIdentificacao" isDomain="true" width="50" />
		<adsm:gridColumn title="" 						property="proprietario.pessoa.nrIdentificacaoFormatado" width="95" align="right" />
		<adsm:gridColumn title="nomeProprietario" 		property="proprietario.pessoa.nmPessoa" width="" />
		<adsm:gridColumnGroup customSeparator=" ">	   
			<adsm:gridColumn title="solicitacaoContratacao" 	property="veiculoControleCarga.solicitacaoContratacao.filial.sgFilial" width="40" />
			<adsm:gridColumn title="" 							property="veiculoControleCarga.solicitacaoContratacao.nrSolicitacaoContratacao" width="80" align="right" dataType="integer" mask="0000000000" />
		</adsm:gridColumnGroup>	
		<adsm:gridColumn title="valor" 					property="moeda.siglaSimbolo" width="75" />
		<adsm:gridColumn title="" 						property="vlPagamento" dataType="currency" align="right" width="120" />
		<adsm:buttonBar>
			<adsm:button caption="fechar" id="botaoFechar" onclick="javascript:window.close();" disabled="false" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>
function populaForm(id) {
	onDataLoad(id);
	return false;
}

function retorno_carregaDados_cb(data, error) {
	onDataLoad_cb(data, error);
	povoaDadosMaster();
	setDisabled("botaoFechar", false);
	setFocus(document.getElementById("botaoFechar"), true, true);
}

function retornoCarregaPagina_cb(data, error) {
	onPageLoad_cb(data, error);
	if (error == undefined) {
		povoaDadosMaster();
		povoaGrid(getElementValue("controleCarga.idControleCarga"));
	}
}

function povoaDadosMaster() {
	setElementValue("controleCarga.idControleCarga", dialogArguments.window.document.getElementById('idControleCarga').value);
	setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", dialogArguments.window.document.getElementById('filialByIdFilialOrigem.sgFilial').value);
	setElementValue("controleCarga.nrControleCarga", dialogArguments.window.document.getElementById('nrControleCarga').value);
}

function povoaGrid(idControleCarga) {
	var filtro = new Array();
    setNestedBeanPropertyValue(filtro, "controleCarga.idControleCarga", idControleCarga);
    pagtoProprietariosGridDef.executeSearch(filtro, true);
    return false;
}
</script>