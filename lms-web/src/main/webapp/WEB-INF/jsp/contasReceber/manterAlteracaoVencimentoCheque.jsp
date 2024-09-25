<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window onPageLoadCallBack="myOnPageLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-36175" />
	</adsm:i18nLabels>
	<adsm:form id="observacao.form" action="/contasReceber/manterMovimentoChequesPreDatados">

		<adsm:textbox label="dataVencimentoAtual" property="dtVencimentoAtual" dataType="JTDate" width="80%" labelWidth="20%" disabled="true" picker="false"/>
		<adsm:textbox label="dataVencimentoNova" property="dtVencimentoNova" dataType="JTDate" width="80%" labelWidth="20%" required="true"/>

		<adsm:buttonBar>
			<adsm:button caption="confirmar" onclick="setValue();" disabled="false"/>
			<adsm:button caption="cancelar" onclick="javascript:window.close();" disabled="false"/>
		</adsm:buttonBar>

	</adsm:form>

</adsm:window>

<script>
	function myOnPageLoad_cb(){
		setElementValue("dtVencimentoAtual", setFormat("dtVencimentoAtual", dialogArguments.window.document.getElementById("dtVencimentoAtual").value));
	}
	
	function setValue(){
		valid = validateTabScript(document.getElementById("observacao.form"));
		
		// apenas prossegue se a validação dos dados foi realizada com sucesso.
		if (valid == false) {
			return false;
		}			

		if (compareData(getElementValue("dtVencimentoNova"), getElementValue("dtVencimentoAtual"), "JTDate", "yyyy-MM-dd", "yyyy-MM-dd")){
			alert(''+i18NLabel.getLabel("LMS-36175"));
		} else {
			dialogArguments.window.document.getElementById("dtVencimentoNova").value = getElementValue("dtVencimentoNova");
			dialogArguments.window.alterarVencimentoScript();
			window.close();
		}

	}
</script>