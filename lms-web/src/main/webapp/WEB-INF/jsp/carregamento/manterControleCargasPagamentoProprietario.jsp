<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterControleCargasPagamentoProprietario" service="lms.carregamento.manterControleCargasJanelasAction" 
			 onPageLoadCallBack="retornoCarregaPagina" >

	<adsm:form action="/carregamento/manterControleCargasVeiculos" idProperty="idPagtoProprietarioCc" onDataLoadCallBack="retorno_carregaDados"
			   service="lms.carregamento.manterControleCargasJanelasAction.findByIdPagtoProprietario" >

		<adsm:section caption="pagamentoProprietario" />
		
		<adsm:hidden property="blPermiteAlterar" serializable="false" />

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

		<adsm:combobox property="moeda.idMoeda" label="valor"
					   service="lms.carregamento.manterControleCargasJanelasAction.findMoeda" 
					   optionProperty="idMoeda" optionLabelProperty="siglaSimbolo" 
					   onchange="return moeda_OnChange(this)"
					   labelWidth="21%" width="79%" >
			<adsm:textbox dataType="currency" property="vlPagamento" 
						  minValue="0.01" size="18" maxLength="18" disabled="true" />
		</adsm:combobox>


		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="salvar" id="botaoSalvar" onclick="botaoSalvar_onClick(this.form)" disabled="false" />
		</adsm:buttonBar>
		
		<script>
				var labelValorNaoConformidade = '<adsm:label key="valorNaoConformidade"/>';
		</script>
		
	</adsm:form>

	<adsm:grid idProperty="idPagtoProprietarioCc" property="pagtoProprietarios" 
			   selectionMode="none" gridHeight="220" rows="11" unique="true" 
			   autoSearch="false" onRowClick="populaForm"
			   service="lms.carregamento.manterControleCargasJanelasAction.findPaginatedPagtoProprietario"
			   rowCountService="lms.carregamento.manterControleCargasJanelasAction.getRowCountPagtoProprietario"
			   onDataLoadCallBack="retornoGrid"
			   >
		<adsm:gridColumn title="identificacao" 			property="proprietario.pessoa.tpIdentificacao" isDomain="true" width="70" />
		<adsm:gridColumn title="" 						property="proprietario.pessoa.nrIdentificacaoFormatado" width="105" align="right" />
		<adsm:gridColumn title="nomeProprietario" 		property="proprietario.pessoa.nmPessoa" width="" />
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
	if (getElementValue("blPermiteAlterar") == "true" && getElementValue("tpVinculo") == "P")
		desabilitaVlPagamento(false);
	else
		desabilitaVlPagamento(true);

	setDisabled("botaoFechar", false);
}

function retornoCarregaPagina_cb(data, error) {
	onPageLoad_cb(data, error);
	if (error == undefined) {
		povoaDadosMaster();
		desabilitaVlPagamento(true);
		povoaGrid(getElementValue("controleCarga.idControleCarga"));
	}
}

function desabilitaVlPagamento(valor) {
	setDisabled("moeda.idMoeda", valor);
	setDisabled("vlPagamento", valor);
	setDisabled("botaoSalvar", valor);
	if (valor == false) {
		document.getElementById("moeda.idMoeda").required = "true";
		document.getElementById("vlPagamento").required = "true";
		document.getElementById("vlPagamento").label = labelValorNaoConformidade;
	}
	else {
		document.getElementById("moeda.idMoeda").required = "false";
		document.getElementById("vlPagamento").required = "false";
	}
}


function povoaDadosMaster() {
	setElementValue("controleCarga.idControleCarga", dialogArguments.window.document.getElementById('idControleCarga').value);
	setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", dialogArguments.window.document.getElementById('filialByIdFilialOrigem.sgFilial').value);
	setElementValue("controleCarga.nrControleCarga", dialogArguments.window.document.getElementById('nrControleCarga').value);
	setElementValue("blPermiteAlterar", dialogArguments.window.document.getElementById('blPermiteAlterarPgtoProprietario').value);
}

function povoaGrid(idControleCarga) {
	var filtro = new Array();
    setNestedBeanPropertyValue(filtro, "controleCarga.idControleCarga", idControleCarga);
    pagtoProprietariosGridDef.executeSearch(filtro, true);
    return false;
}

function moeda_OnChange(combo){
	if (getElementValue("moeda.idMoeda") == "" ) {
		resetValue("vlPagamento");
		setDisabled("vlPagamento", true);
	}
	else
		setDisabled("vlPagamento", false);
	return comboboxChange({e:combo});
}


function botaoSalvar_onClick(form) {
	if (!validateForm(form)) {
		return false;
	}
    var sdo = createServiceDataObject("lms.carregamento.manterControleCargasJanelasAction.storePagamentoProprietario", 
    			"store", buildFormBeanFromForm(form));
    xmit({serviceDataObjects:[sdo]});
}


function store_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
   	showSuccessMessage();
   	povoaGrid(getElementValue("controleCarga.idControleCarga"));
}


function retornoGrid_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	setFocus(document.getElementById("botaoFechar"), true, true);
}
</script>