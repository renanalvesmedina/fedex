<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script>
function carregaDadosControleCarga() {
	onPageLoad();
	setMasterLink(this.document, true);
	var idManifesto = getElementValue("manifesto.idManifesto");
	var map = new Array();
	setNestedBeanPropertyValue(map, "idManifesto", idManifesto);
    var sdo = createServiceDataObject("lms.recepcaodescarga.consultarCargasViagemAction.findDadosPopupDetalhes", "resultadoFindDadosPopupDetalhes", map);
    xmit({serviceDataObjects:[sdo]});
}

function resultadoFindDadosPopupDetalhes_cb(data, error) {
	if(error!=undefined) {
		alert(error);
		return false;
	}
	if (data!=undefined) {
		setElementValue('controleCarga.filialByIdFilialOrigem.sgFilial', data.controleCarga.filialByIdFilialOrigem.sgFilial);
		setElementValue('controleCarga.nrControleCarga', setFormat(document.getElementById("controleCarga.nrControleCarga"), data.controleCarga.nrControleCarga));
		setElementValue('controleCarga.meioTransporteByIdTransportado.nrIdentificador', data.controleCarga.meioTransporteByIdTransportado.nrIdentificador);
		setElementValue('controleCarga.meioTransporteByIdTransportado.nrFrota', data.controleCarga.meioTransporteByIdTransportado.nrFrota);
		setElementValue('controleCarga.meioTransporteByIdTransportado.nrCapacidadeKg',setFormat(document.getElementById("controleCarga.meioTransporteByIdTransportado.nrCapacidadeKg"), data.controleCarga.meioTransporteByIdTransportado.nrCapacidadeKg));
		setElementValue('controleCarga.meioTransporteByIdSemiRebocado.nrIdentificador', data.controleCarga.meioTransporteByIdSemiRebocado.nrIdentificador);
		setElementValue('controleCarga.meioTransporteByIdSemiRebocado.nrFrota', data.controleCarga.meioTransporteByIdSemiRebocado.nrFrota);
		setElementValue('controleCarga.psTotalFrota', setFormat(document.getElementById("controleCarga.psTotalFrota"), data.controleCarga.psTotalFrota));
		setElementValue('controleCarga.pcOcupacaoCalculado', setFormat(document.getElementById("controleCarga.pcOcupacaoCalculado"), data.controleCarga.pcOcupacaoCalculado));
		setElementValue('controleCarga.pcOcupacaoAforadoCalculado', setFormat(document.getElementById("controleCarga.pcOcupacaoAforadoCalculado"), data.controleCarga.pcOcupacaoAforadoCalculado));
	    setElementValue('controleCarga.psOcupacaoVisual', setFormat(document.getElementById("controleCarga.psOcupacaoVisual"), data.controleCarga.psOcupacaoVisual));
		setElementValue('controleCarga.motorista.nrCarteiraHabilitacao', data.controleCarga.motorista.nrCarteiraHabilitacao);
		setElementValue('controleCarga.motorista.pessoa.nmPessoa', data.controleCarga.motorista.pessoa.nmPessoa);
		setElementValue('controleCarga.filialByIdFilialDestino.sgFilial', data.controleCarga.filialByIdFilialDestino.sgFilial);
		setElementValue('manifesto.tpManifesto', data.manifesto.tpManifesto);
		setElementValue('manifesto.filialByIdFilialOrigem', data.manifesto.filialByIdFilialOrigem);
		setElementValue('manifesto.nrManifesto', setFormat(document.getElementById('manifesto.nrManifesto'), data.manifesto.nrManifesto));
		setElementValue('manifesto.dhEmissaoManifesto', setFormat(document.getElementById("manifesto.dhEmissaoManifesto") ,data.manifesto.dhEmissaoManifesto ));
		setElementValue('manifesto.siglaSimboloVlTotalManifesto', data.manifesto.siglaSimboloVlTotalManifesto);
		setElementValue('manifesto.vlTotalManifesto', setFormat(document.getElementById("manifesto.vlTotalManifesto"), data.manifesto.vlTotalManifesto));
		setElementValue('chegadaProgramada', setFormat(document.getElementById("chegadaProgramada"),data.chegadaProgramada));
		setElementValue('chegadaEstimada', setFormat(document.getElementById("chegadaEstimada"),data.chegadaEstimada));
		setElementValue('atraso', setFormat(document.getElementById("atraso"), data.atraso));
		setElementValue('quantidadeDoctos', setFormat(document.getElementById("quantidadeDoctos"), data.quantidadeDoctos));
	}
	
}
</script>

<adsm:window service="lms.recepcaodescarga.consultarCargasViagemAction" onPageLoad="carregaDadosControleCarga">
	<adsm:form action="/recepcaoDescarga/consultarCargasViagemDetalhe">
		<adsm:section caption="cargasViagemDetalhe" />
		<adsm:label key="branco" width="1%"/>
		<adsm:hidden property="manifesto.idManifesto"/>
		<adsm:textbox property="controleCarga.filialByIdFilialOrigem.sgFilial" label="controleCargas" labelWidth="23%" width="76%" disabled="true" dataType="text" size="3">
			<adsm:textbox property="controleCarga.nrControleCarga" size="8" dataType="integer" mask="00000000" disabled="true" />
		</adsm:textbox>

		<adsm:label key="branco" width="1%"/>
		<adsm:textbox property="controleCarga.meioTransporteByIdTransportado.nrFrota" label="meioTransporte" labelWidth="23%" width="76%" disabled="true" dataType="text" size="6">
			<adsm:textbox property="controleCarga.meioTransporteByIdTransportado.nrIdentificador" dataType="text" size="25" disabled="true"/>
		</adsm:textbox>
		
		<adsm:label key="branco" width="1%"/>
		<adsm:textbox property="controleCarga.meioTransporteByIdSemiRebocado.nrFrota" label="semiReboque" labelWidth="23%" width="76%" disabled="true" dataType="text" size="6">
			<adsm:textbox property="controleCarga.meioTransporteByIdSemiRebocado.nrIdentificador" dataType="text" size="25" disabled="true"/>
		</adsm:textbox>
		
		<adsm:label key="branco" width="1%"/>
		<adsm:textbox property="chegadaProgramada" label="chegadaProgramada" dataType="JTDateTimeZone" labelWidth="23%" width="76%" disabled="true" picker="false"/>

		<adsm:label key="branco" width="1%"/>
		<adsm:textbox property="chegadaEstimada" label="chegadaEstimada" dataType="JTDateTimeZone" labelWidth="23%" width="76%" disabled="true" picker="false"/>

		<adsm:label key="branco" width="1%"/>
		<adsm:textbox property="atraso" label="atraso" dataType="JTDateTimeZone" labelWidth="23%" width="76%" disabled="true" picker="false" />

		<adsm:label key="branco" width="1%"/>
		<adsm:textbox property="controleCarga.meioTransporteByIdTransportado.nrCapacidadeKg" label="capacidadeVeiculo" dataType="weight" unit="kg" labelWidth="23%" width="76%" disabled="true" />

		<adsm:label key="branco" width="1%"/>
		<adsm:textbox property="controleCarga.psTotalFrota" label="pesoCarga" dataType="weight" unit="kg" labelWidth="23%" width="76%" disabled="true"  />

		<adsm:label key="branco" width="1%"/>
		<adsm:textbox property="controleCarga.pcOcupacaoCalculado" label="ocupacaoCalculada" dataType="percent" unit="percent" size="6" maxLength="6" labelWidth="23%" width="76%" disabled="true" />

		<adsm:label key="branco" width="1%"/>
		<adsm:textbox property="controleCarga.pcOcupacaoAforadoCalculado" label="ocupacaoAforadaCalculada" dataType="percent" unit="percent" size="6" maxLength="6" labelWidth="23%" width="76%" disabled="true" />

		<adsm:label key="branco" width="1%"/>
		<adsm:textbox property="controleCarga.psOcupacaoVisual" label="ocupacaoVisual" dataType="percent" unit="percent" size="6" maxLength="6" labelWidth="23%" width="76%" disabled="true" />

		<adsm:label key="branco" width="1%"/>
		<adsm:textbox property="controleCarga.motorista.nrCarteiraHabilitacao" label="motorista" labelWidth="23%" width="76%" disabled="true" dataType="text" size="15">
			<adsm:textbox property="controleCarga.motorista.pessoa.nmPessoa" size="35" dataType="text" disabled="true" />
		</adsm:textbox>
		
		<adsm:label key="branco" width="1%"/>
		<adsm:textbox property="manifesto.tpManifesto" label="manifesto" labelWidth="23%" width="76%" disabled="true" dataType="text" size="20">
			<adsm:textbox property="manifesto.filialByIdFilialOrigem" dataType="text" size="3" disabled="true"/>
			<adsm:textbox property="manifesto.nrManifesto" dataType="integer" mask="00000000" size="8" disabled="true"/>
		</adsm:textbox>

		<adsm:label key="branco" width="1%"/>
		<adsm:textbox property="manifesto.dhEmissaoManifesto" label="emissaoManifesto" dataType="JTDateTimeZone" labelWidth="23%" width="76%" disabled="true" picker="false"/>

		<adsm:label key="branco" width="1%"/>
		<adsm:textbox property="controleCarga.filialByIdFilialDestino.sgFilial" label="destino" dataType="text"  size="3"labelWidth="23%" width="76%" disabled="true" />

		<adsm:label key="branco" width="1%"/>
		<adsm:textbox property="manifesto.siglaSimboloVlTotalManifesto" label="valorManifesto" dataType="text" size="8" maxLength="8" labelWidth="23%" width="76%" disabled="true">
			<adsm:textbox property="manifesto.vlTotalManifesto" dataType="decimal" size="16" maxLength="16" disabled="true" />
		</adsm:textbox>

		<adsm:label key="branco" width="1%"/>
		<adsm:textbox property="quantidadeDoctos" label="quantidadeDocumentos" dataType="integer" size="16" labelWidth="23%" width="76%" disabled="true" />

		<adsm:buttonBar freeLayout="false">
			<adsm:button id="closeButton" caption="fechar" onclick="closeWindow()" disabled="false" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
document.getElementById("closeButton").disabled = false;

function closeWindow() {
	window.close();
}
</script>