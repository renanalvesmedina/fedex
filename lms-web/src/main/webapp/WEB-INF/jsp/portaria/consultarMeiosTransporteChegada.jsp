<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:i18nLabels>
	<adsm:include key="LMS-00000"/>
</adsm:i18nLabels>
<adsm:window title="branco">
	<adsm:form action="/portaria/consultarMeiosTransporteChegada" idProperty="idMeioTransporte">
		<adsm:complement required="true" label="meioTransporte">
			<adsm:textbox dataType="text" style="text-transform: uppercase;" maxLength="6" property="nrFrota"
					size="15" labelWidth="22%" width="78%" cellStyle="vertical-Align:bottom" required="false"
					onchange="return onNrFrotaChange(this);" >
		        <adsm:textbox dataType="text" style="text-transform: uppercase;" maxLength="25" property="nrIdentificador"
		        		size="20" cellStyle="vertical-Align:bottom" required="false"
		        		onchange="return onNrIdentificadorChange(this);" />
			</adsm:textbox>
		</adsm:complement>
		<adsm:buttonBar>
			<adsm:button caption="consultar" onclick="consulta()" disabled="false" id="consultar"/>
			<adsm:button caption="fechar" onclick="self.close();" disabled="false" id="fechar"/>
		</adsm:buttonBar>
    </adsm:form>
</adsm:window>
<script>
	window.returnValue = window;
	
	function initWindow(evt){
		setDisabled("consultar", false);
		setDisabled("fechar", false);
	}
	
	function consulta(){
		var validou = validateTabScript(document.forms[0]);
		if (validou) {
			dialogArguments.procuraMeioTransporte(getElementValue("nrIdentificador"), getElementValue("nrFrota"));
			self.close();
		}
	}
	
	/*
	 * Método chamado no evento onChange do elemento nrFrota.
	 */
	function onNrFrotaChange(elem) {
		treatBehavior(elem,"nrIdentificador");
		return true;
	}

	/*
	 * Método chamado no evento onChange do elemento nrIdentificador.
	 */
	function onNrIdentificadorChange(elem) {
		treatBehavior(elem,"nrFrota");
		return true;
	}
	
	/*
	 * Se o valor de elemOrigem for diferente de vazio, faz com que elemDestino seja limpo.
	 */
	function treatBehavior(elemOrigem, elemDestino) {
		if (elemOrigem.value != "") {
			resetValue(elemDestino);
		}
		elemOrigem.required = true;
		document.getElementById(elemDestino).required = false;
	}

</script>