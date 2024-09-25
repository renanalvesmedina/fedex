<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window>
	<adsm:form action="/contasReceber/manterMovimentoChequesPreDatados">

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
		dialogArguments.window.document.getElementById("observacao").value = document.getElementById("observacao").value;
		dialogArguments.window.aplicarAcaoScript();
		window.close();
	}
</script>