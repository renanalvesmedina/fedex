<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.carregamento.manterControleCargasAction" onPageLoadCallBack="retornoCarregaPagina">
	
	<adsm:form action="/carregamento/manterControleCargas">

		<adsm:hidden property="idControleCarga" serializable="true" />
		<adsm:hidden property="blInseridoManualmente" value="true" serializable="false" />
		<adsm:hidden property="filial.idFilial" />

		<adsm:combobox label="filial" 
					   property="filialAnterior.idFilialRotaCc"
					   service="lms.carregamento.manterControleCargasAction.findFilialRotaCc"
					   optionProperty="idFilialRotaCc" optionLabelProperty="filial.sgFilialConcatenado"
					   boxWidth="230" labelWidth="18%" width="82%" required="true" serializable="false" >
			<adsm:propertyMapping modelProperty="idControleCarga" criteriaProperty="idControleCarga" />
			<adsm:propertyMapping modelProperty="blInseridoManualmente" criteriaProperty="blInseridoManualmente" />
			<adsm:propertyMapping modelProperty="filial.idFilial" relatedProperty="filial.idFilial" />
		</adsm:combobox>

		<adsm:buttonBar freeLayout="false">
			<adsm:button caption="excluir" id="botaoExcluir" onclick="excluir_onClick(this.form);" disabled="false" />
			<adsm:button caption="fechar" id="botaoFechar" onclick="javascript:window.close();" disabled="false" />
		</adsm:buttonBar>
		
		<script>
			var LMS_05162 = "<adsm:label key='LMS-05162'/>";
		</script>
	</adsm:form>

</adsm:window>

<script>

function retornoCarregaPagina_cb(data, error) {
	onPageLoad_cb(data, error);
	if (error == undefined) {
		setElementValue("idControleCarga", dialogArguments.window.document.getElementById('idControleCarga').value);
		notifyElementListeners({e:document.getElementById("idControleCarga")});
	}
}

function excluir_onClick(form) {
	if (!validateForm(form)) {
		return false;
	}
	var sdo = createServiceDataObject("lms.carregamento.manterControleCargasAction.removeFilialRota", 
		"retornoRemoveFilialRota", buildFormBeanFromForm(form));
    xmit({serviceDataObjects:[sdo]});
}


function retornoRemoveFilialRota_cb(data, error) {
	if (error != undefined) {
		alert(error);
	}
	showSuccessMessage();
	
	var tabDet = getTabGroup(dialogArguments.window.document).getTab("cad");
	tabDet.tabOwnerFrame.window.setElementValue("hrTempoViagem", getNestedBeanPropertyValue(data, "hrTempoViagem"));
	
	var blNecessitaCartaoPedagio = getNestedBeanPropertyValue(data, "blNecessitaCartaoPedagio");
	if (blNecessitaCartaoPedagio != undefined && blNecessitaCartaoPedagio == "true") {
		alert(LMS_05162);
	}
	window.close();
}
</script>