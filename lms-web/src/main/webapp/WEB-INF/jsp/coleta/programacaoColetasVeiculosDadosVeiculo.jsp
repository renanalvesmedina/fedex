<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
function carregaPagina() {
	setMasterLink(document, true);
	povoaForm();
}
</script>
<adsm:window title="programacaoColetasVeiculos" service="lms.coleta.programacaoColetasVeiculosDadosVeiculoAction" onPageLoad="carregaPagina" >
	<adsm:form action="/coleta/programacaoColetasVeiculos" idProperty="idControleCarga" onDataLoadCallBack="carregaDados_retorno"
			   service="lms.coleta.programacaoColetasVeiculosDadosVeiculoAction.findById" >
		<adsm:section caption="dadosVeiculo" />
		
		<adsm:textbox label="meioTransporte" property="meioTransporteByIdTransportado.nrFrota" dataType="text" size="6" labelWidth="17%" width="83%" disabled="true" >
			<adsm:textbox dataType="text" property="meioTransporteByIdTransportado.nrIdentificador" size="20" disabled="true" />
		</adsm:textbox>

		<adsm:textbox label="controleCargas" dataType="text" property="filialByIdFilialOrigem.sgFilial" size="3" labelWidth="17%" width="33%" disabled="true" >
			<adsm:textbox dataType="integer" property="nrControleCarga" size="10" mask="00000000" disabled="true"/>
		</adsm:textbox>
		<adsm:textbox label="tipoMeioTransporte" dataType="text" property="meioTransporteByIdTransportado.modeloMeioTransporte.tipoMeioTransporte.dsTipoMeioTransporte" 
					  size="40" labelWidth="17%" width="33%" disabled="true"/>

		<adsm:textbox label="capacidade" property="meioTransporteByIdTransportado.nrCapacidadeKg" dataType="weight" 
					  unit="kg" size="19" labelWidth="17%" width="33%" disabled="true" />
		<adsm:textbox label="capacidadeOciosa" property="pcOcupacaoInformado" dataType="decimal" unit="percent" size="7" labelWidth="17%" width="33%" disabled="true" />

		<adsm:textbox label="valorTotalFrota" property="moedaVlTotalFrota" dataType="text" size="5" labelWidth="17%" width="33%" disabled="true" >
			<adsm:textbox property="vlTotalFrota" dataType="currency" disabled="true" />
		</adsm:textbox>
		<adsm:textbox label="pesoTotalFrota" property="psTotalFrota" dataType="weight" unit="kg" size="19" labelWidth="17%" width="33%" disabled="true" />

		<adsm:textbox label="valorColetado" property="moedaVlColetado" dataType="text" size="5" labelWidth="17%" width="33%" disabled="true" >
			<adsm:textbox property="vlColetado" dataType="currency" disabled="true" />
		</adsm:textbox>
		<adsm:textbox label="valorColetar" property="moedaVlAColetar" dataType="text" size="5" labelWidth="17%" width="33%" disabled="true" >
			<adsm:textbox property="vlAColetar" dataType="currency" size="20" disabled="true" />
		</adsm:textbox>

		<adsm:textbox label="valorEntregar" property="moedaVlAEntregar" dataType="text" size="5" labelWidth="17%" width="33%" disabled="true" >
			<adsm:textbox property="vlAEntregar" dataType="currency" size="20" disabled="true" />
		</adsm:textbox>
		<adsm:textbox label="valorEntregue" property="moedaVlEntregue" dataType="text" size="5" labelWidth="17%" width="33%" disabled="true" >
			<adsm:textbox property="vlEntregue" dataType="currency" size="20" disabled="true" />
		</adsm:textbox>

		<adsm:textbox label="pesoColetado" property="psColetado" dataType="weight" unit="kg" size="19" labelWidth="17%" width="33%"  disabled="true" />
		<adsm:textbox label="pesoColetar" property="psAColetar" dataType="weight" unit="kg" size="19" labelWidth="17%" width="33%" disabled="true" />

		<adsm:textbox label="pesoEntregar" property="psAEntregar" dataType="weight" unit="kg" size="19" labelWidth="17%" width="33%" disabled="true" />
		<adsm:textbox label="pesoEntregue" property="psEntregue" dataType="weight" unit="kg" size="19" labelWidth="17%" width="33%" disabled="true" />

		<adsm:buttonBar> 
			<adsm:button caption="fechar" id="botaoFechar" onclick="javascript:window.close();" disabled="false" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>
function povoaForm() {
	onDataLoad( getElementValue('idControleCarga') );
	setDisabled(document.getElementById("botaoFechar"), false);
	setFocus(document.getElementById("botaoFechar"), true, true);
}

function carregaDados_retorno_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	onDataLoad_cb(data);
	format(document.getElementById("nrControleCarga"));
	setDisabled(document.getElementById("botaoFechar"), false);
	setFocus(document.getElementById("botaoFechar"), true, true);
}
</script>