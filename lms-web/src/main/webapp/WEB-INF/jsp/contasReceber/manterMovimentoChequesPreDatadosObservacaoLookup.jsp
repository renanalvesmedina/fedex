<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window>
	<adsm:form id="observacao.form" action="/contasReceber/manterMovimentoChequesPreDatados">

		<adsm:combobox property="empresaCobranca.idEmpresaCobranca" onlyActiveValues="true" optionLabelProperty="pessoa.nmPessoa" optionProperty="idEmpresaCobranca" service="lms.contasreceber.manterMovimentoChequesPreDatadosAction.findEmpresaCobranca" label="empresaCobranca" labelWidth="15%" width="85%" boxWidth="260" required="true"/>

		<adsm:textarea label="observacao" property="observacao" width="85%" maxLength="255" 
			columns="70" rows="5"/>

		<adsm:buttonBar>
			<adsm:button caption="confirmar" onclick="setValue();" disabled="false"/>
			<adsm:button caption="cancelar" onclick="javascript:window.close();" disabled="false"/>
		</adsm:buttonBar>

	</adsm:form>

</adsm:window>

<script>
	function setValue(){
		valid = validateTabScript(document.getElementById("observacao.form"));

		// apenas prossegue se a valida??o dos dados foi realizada com sucesso.
		if (valid == false) {
			return false;
		}
			
		dialogArguments.window.document.getElementById("observacao").value = document.getElementById("observacao").value;
		dialogArguments.window.document.getElementById("empresaCobranca").value = document.getElementById("empresaCobranca.idEmpresaCobranca").value;		
		dialogArguments.window.aplicarAcaoScript();
		window.close();
	}
</script>