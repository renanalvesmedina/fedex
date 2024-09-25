<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.cancelarCRTAction">
	<adsm:i18nLabels>
		<adsm:include key="LMS-04085"/>
	</adsm:i18nLabels>

	<adsm:form action="/expedicao/cancelarCRT">
		<adsm:textbox 
			label="numeroCRT"
			dataType="text" size="1"
			maxLength="2"
			onchange="return sgPaisChange(this);"
			property="ctoInternacional.sgPais" serializable="false"
			labelWidth="16%"
			width="32%">
			<adsm:lookup dataType="integer" 
				criteriaSerializable="true"
				property="ctoInternacional" 
				idProperty="idDoctoServico"
				criteriaProperty="nrCrt"
				required="true"
				service="lms.expedicao.cancelarCRTAction.findLookup"
				action="/expedicao/manterCRT" 
				cmd="list"
				onchange="return nrCrtOnChange(this)"
				onPopupSetValue="findLookupCRT"
				size="6" mask="000000" maxLength="6" >
				<adsm:propertyMapping
					criteriaProperty="ctoInternacional.sgPais"
					modelProperty="sgPais"/>

				<adsm:propertyMapping
					relatedProperty="totalFreteDolar"
					modelProperty="vlTotalDocServico"/>
				<adsm:propertyMapping 
					relatedProperty="remetente"
					modelProperty="dsDadosRemetente"/>
				<adsm:propertyMapping 
					relatedProperty="destinatario"
					modelProperty="dsDadosDestinatario"/>
			</adsm:lookup>
		</adsm:textbox>

		<adsm:textbox property="totalFreteDolar" label="valorTotalFreteDolar" dataType="decimal" labelWidth="20%" width="20%" disabled="true"/>

		<adsm:combobox label="motivoDeCancelamento"
			optionProperty="idMotivoCancelamento"
			property="motivoCancelamento.idMotivoCancelamento" 
			optionLabelProperty="dsMotivoCancelamento" required="true"
			service="lms.indenizacoes.manterMotivosCancelamentoAction.findAllAtivo" 
			onlyActiveValues="true"
			labelWidth="16%"
			boxWidth="250"/>

		<adsm:section caption="dadosRemetente"/>
		<adsm:textarea property="remetente" label="remetente" maxLength="400" columns="100" rows="3" labelWidth="16%" width="77%" disabled="true"/>

		<adsm:section caption="dadosDestinatario"/>
		<adsm:textarea property="destinatario" label="destinatario" maxLength="400" columns="100" rows="3" labelWidth="16%" width="77%" disabled="true"/>

		<adsm:buttonBar>
			<adsm:button id="efetuarCancelamentoButton" caption="efetuarCancelamento" 
						 service="lms.expedicao.cancelarCRTAction.cancelarCRT"
						 callbackProperty="cancelarCRT"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script language="javascript" type="text/javascript" src="../lib/expedicao.js"></script>
<script language="javascript" type="text/javascript">
<!--
	function findLookupCRT(data) {
		setElementValue("ctoInternacional.sgPais", data.sgPais);
		setElementValue("ctoInternacional.nrCrt", data.nrCrt);
		return nrCrtOnChange(getElement("ctoInternacional.nrCrt"));
	}

	function nrCrtOnChange(obj) {
		var isValid = getElementValue(obj).length > 0;
		if(!isValid) {
			resetValue("motivoCancelamento.idMotivoCancelamento");
		}
		setDisabled("efetuarCancelamentoButton", !isValid);
		return lookupChange({e:document.getElementById("ctoInternacional.idDoctoServico"), forceChange:true});
	}

	function cancelarCRT_cb(data, error) {
		store_cb(data, error);
		if (error != undefined) {
			return;
		}
		alertI18nMessage("LMS-04085");
		newButtonScript(document);
	}

	function sgPaisChange(campo) {
	 	var isValid = getElementValue(campo).length > 0;
	 	if(isValid) {
	 		setElementValue(campo, getElementValue(campo).toUpperCase());
	 	}
	 	resetValue("ctoInternacional.idDoctoServico");
 		resetValue("totalFreteDolar");
 		resetValue("remetente");
 		resetValue("destinatario");
	 	resetValue("motivoCancelamento.idMotivoCancelamento");
	 	setDisabled("efetuarCancelamentoButton", !isValid);
	 	return true;
	}
</script>