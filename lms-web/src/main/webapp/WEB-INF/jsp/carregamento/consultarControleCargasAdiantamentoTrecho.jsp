<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.carregamento.consultarControleCargasAction" >
	<adsm:form action="/carregamento/consultarControleCargas">

		<adsm:hidden property="idControleCarga" serializable="false" />
		<adsm:hidden property="idMeioTransporte" serializable="false" />

		<adsm:textbox dataType="text" label="controleCargas" property="filialByIdFilialOrigem.sgFilial"
					  size="3" labelWidth="18%" width="82%" disabled="true" serializable="false" >
	 		<adsm:textbox dataType="integer" property="nrControleCarga" size="9" mask="00000000" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textbox dataType="text" label="proprietario" property="proprietario.pessoa.nrIdentificacaoFormatado"
					  size="20" labelWidth="18%" width="82%" disabled="true" serializable="false" >
	 		<adsm:textbox dataType="text" property="proprietario.pessoa.nmPessoa" size="40" disabled="true" serializable="false" />
		</adsm:textbox>
		
		<adsm:combobox property="beneficiario" 
			label="beneficiario"
			domain="DM_BENEFICIARIO_ADIANTAMENTO"  
			labelWidth="18%" width="82%"  boxWidth="200" disabled="true" serializable="false" />

		<adsm:textbox dataType="integer" label="banco" property="contaBancaria.agenciaBancaria.banco.nrBanco"
					  size="6" labelWidth="18%" width="82%" disabled="true" serializable="false" >
	 		<adsm:textbox dataType="text" property="contaBancaria.agenciaBancaria.banco.nmBanco" size="40" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textbox dataType="integer" label="agencia" property="contaBancaria.agenciaBancaria.nrAgenciaBancaria"
					  size="6" labelWidth="18%" width="47%" disabled="true" serializable="false" >
	 		<adsm:textbox dataType="text" property="contaBancaria.agenciaBancaria.nmAgenciaBancaria" size="40" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textbox dataType="integer" label="contaCorrente" property="contaBancaria.nrContaBancaria"
					  size="12" labelWidth="12%" width="23%" disabled="true" serializable="false" >
	 		<adsm:textbox dataType="integer" property="contaBancaria.dvContaBancaria" size="3" disabled="true" serializable="false" />
		</adsm:textbox>

	</adsm:form>

	<adsm:grid property="adiantamentoTrechos" idProperty="idAdiantamentoTrecho" 
			   selectionMode="none" gridHeight="245" showPagging="false" scrollBars="vertical" autoSearch="false"
			   service="lms.carregamento.consultarControleCargasAction.findPaginatedAdiantamentoTrecho"
			   rowCountService=""
			   onRowClick="adiantamentoTrechos_OnClick" >
		<adsm:gridColumn title="origem" 			property="sgFilialOrigem" width="22%" />
		<adsm:gridColumn title="destino" 			property="sgFilialDestino" width="22%" />
		<adsm:gridColumn title="valor" 				property="vlFrete" dataType="currency" align="right" width="20%" />
		<adsm:gridColumn title="percentual" 		property="pcFrete" dataType="currency" align="right" width="16%" unit="percent" />
		<adsm:gridColumn title="valorAdiantamento" 	property="vlAdiantamento" dataType="currency" align="right" width="20%" />
		<adsm:buttonBar> 
		</adsm:buttonBar>
	</adsm:grid>
	

</adsm:window>

<script>
function initWindow(eventObj) {
	if (eventObj.name == "tab_click") {
		var tabDet = getTabGroup(this.document).getTab("cad");
		setElementValue("filialByIdFilialOrigem.sgFilial", tabDet.getFormProperty("filialByIdFilialOrigem.sgFilial"));
		setElementValue("idControleCarga", tabDet.getFormProperty("idControleCarga"));
		setElementValue("nrControleCarga", tabDet.getFormProperty("nrControleCarga"));
		setElementValue("idMeioTransporte", tabDet.getFormProperty("meioTransporteByIdTransportado.idMeioTransporte"));
		findDadosBeneficiario();
		povoaGrid();
	}
}

function povoaGrid() {
	var filtro = new Array();
    setNestedBeanPropertyValue(filtro, "idControleCarga", getElementValue("idControleCarga"));
    adiantamentoTrechosGridDef.executeSearch(filtro);
    return false;
}

function adiantamentoTrechos_OnClick(id) {
	return false;
}



function limparDadosAbaAdiantamento() {
	resetValue("proprietario.pessoa.nrIdentificacaoFormatado");
	resetValue("proprietario.pessoa.nmPessoa");
	resetValue("contaBancaria.agenciaBancaria.banco.nrBanco");
	resetValue("contaBancaria.agenciaBancaria.banco.nmBanco");
	resetValue("contaBancaria.agenciaBancaria.nrAgenciaBancaria");
	resetValue("contaBancaria.agenciaBancaria.nmAgenciaBancaria");
	resetValue("contaBancaria.nrContaBancaria");
	resetValue("contaBancaria.dvContaBancaria");
	resetValue("beneficiario");
}


function findDadosBeneficiario() {
	limparDadosAbaAdiantamento();
	var sdo = createServiceDataObject("lms.carregamento.consultarControleCargasAction.findDadosBeneficiario", "retornoFindBeneficiario", 
		{idMeioTransporte:getElementValue("idMeioTransporte"),idControleCarga:getElementValue("idControleCarga")});
    xmit({serviceDataObjects:[sdo]});
}

function retornoFindBeneficiario_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	if (data != undefined) {
		setElementValue("proprietario.pessoa.nrIdentificacaoFormatado", getNestedBeanPropertyValue(data, "proprietario.pessoa.nrIdentificacaoFormatado"));
		setElementValue("proprietario.pessoa.nmPessoa", getNestedBeanPropertyValue(data, "proprietario.pessoa.nmPessoa"));
		setElementValue("contaBancaria.agenciaBancaria.banco.nrBanco", getNestedBeanPropertyValue(data, "contaBancaria.agenciaBancaria.banco.nrBanco"));
		setElementValue("contaBancaria.agenciaBancaria.banco.nmBanco", getNestedBeanPropertyValue(data, "contaBancaria.agenciaBancaria.banco.nmBanco"));
		setElementValue("contaBancaria.agenciaBancaria.nrAgenciaBancaria", getNestedBeanPropertyValue(data, "contaBancaria.agenciaBancaria.nrAgenciaBancaria"));
		setElementValue("contaBancaria.agenciaBancaria.nmAgenciaBancaria", getNestedBeanPropertyValue(data, "contaBancaria.agenciaBancaria.nmAgenciaBancaria"));
		setElementValue("contaBancaria.nrContaBancaria", getNestedBeanPropertyValue(data, "contaBancaria.nrContaBancaria"));
		setElementValue("contaBancaria.dvContaBancaria", getNestedBeanPropertyValue(data, "contaBancaria.dvContaBancaria"));
		setElementValue("beneficiario", getNestedBeanPropertyValue(data, "tpBeneficiario"));
	}
}
</script>