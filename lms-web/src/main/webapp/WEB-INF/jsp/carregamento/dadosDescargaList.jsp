<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
function carregaPagina() {
	onPageLoad();
	buscarDados();
}
</script>
<adsm:window service="lms.carregamento.dadosDescargaAction" onPageLoad="carregaPagina" >
	<adsm:form action="/carregamento/dadosDescarga">

		<adsm:hidden property="idControleCarga" serializable="false" />
		<adsm:hidden property="idFilial" serializable="false" />
		<adsm:hidden property="idCarregamentoDescarga" serializable="false" />
		<adsm:hidden property="equipeOperacao.idEquipeOperacao" serializable="false" />

		<adsm:textbox label="controleCargas" property="sgFilialControleCarga" dataType="text" 
					  size="3" labelWidth="18%" width="82%" disabled="true" serializable="false" >
	 		<adsm:textbox property="nrControleCarga" dataType="integer" size="9" mask="00000000" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textbox label="filial" property="filial.sgFilial" dataType="text" size="3" maxLength="3" labelWidth="18%" width="82%" disabled="true">
			<adsm:textbox property="filial.pessoa.nmFantasia" dataType="text" size="30" disabled="true"/>
		</adsm:textbox>

		<adsm:textbox property="dhEvento" label="chegadaPortaria" dataType="JTDateTimeZone" 
					  labelWidth="18%" width="22%" picker="false" disabled="true" />
		<adsm:textbox property="dsEquipe" label="equipe" dataType="text" size="40" labelWidth="18%" width="42%" disabled="true"/>

		<adsm:textbox property="dhInicioOperacao" label="inicioDescarga" dataType="JTDateTimeZone" 
					  labelWidth="18%" width="22%" picker="false" disabled="true"/>
		<adsm:textbox property="dhFimOperacao" label="fimDescarga" dataType="JTDateTimeZone" 
					  labelWidth="18%" width="42%" picker="false" disabled="true"/>

		<adsm:textarea property="obOperacao" label="observacao" maxLength="500" columns="80" rows="3" labelWidth="18%" width="82%" disabled="true"/>

		<adsm:buttonBar>
			<adsm:button caption="fechar" id="botaoFechar" onclick="javascript:window.close();" disabled="false" />
		</adsm:buttonBar>

	</adsm:form>
</adsm:window>

<script>
function initWindow(eventObj) {
	setDisabled("botaoFechar", false);
	setFocus(document.getElementById("botaoFechar"), true, true);
}

function buscarDados() {
	setDisabled('botaoFechar', false); 
	var sdo = createServiceDataObject("lms.carregamento.dadosDescargaAction.findDadosDescarga", "resultado_buscarDados", 
			{idControleCarga:getElementValue('idControleCarga'),
			 idFilial:getElementValue('idFilial')});
    xmit({serviceDataObjects:[sdo]});
}


/**
 * Povoa os campos com os dados retornados da busca em manifesto
 */
function resultado_buscarDados_cb(data, error) {
	if (error != undefined) {
		alert(error);
		setFocus(document.getElementById("botaoFechar"), true, true);
		return false;
	}
	setElementValue('idCarregamentoDescarga', getNestedBeanPropertyValue(data, "idCarregamentoDescarga"));
	setElementValue('equipeOperacao.idEquipeOperacao', getNestedBeanPropertyValue(data, "equipeOperacao.idEquipeOperacao"));
	setElementValue('filial.pessoa.nmFantasia', getNestedBeanPropertyValue(data, "filial.pessoa.nmFantasia"));
	setElementValue('filial.sgFilial', getNestedBeanPropertyValue(data,"filial.sgFilial"));
	setElementValue('dsEquipe', getNestedBeanPropertyValue(data,"dsEquipe"));
	setElementValue("dhInicioOperacao", setFormat(document.getElementById("dhInicioOperacao"), getNestedBeanPropertyValue(data,"dhInicioOperacao")) );
	setElementValue("dhFimOperacao", setFormat(document.getElementById("dhFimOperacao"), getNestedBeanPropertyValue(data,"dhFimOperacao")) );
	setElementValue("dhEvento", setFormat(document.getElementById("dhEvento"), getNestedBeanPropertyValue(data,"dhEvento")) );
	setElementValue('obOperacao', getNestedBeanPropertyValue(data,"obOperacao"));
	setFocus(document.getElementById("botaoFechar"), true, true);
}

</script>