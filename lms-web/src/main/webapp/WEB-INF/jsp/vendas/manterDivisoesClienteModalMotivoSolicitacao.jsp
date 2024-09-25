<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.vendas.manterDivisoesClienteAction">
	<adsm:form action="/vendas/manterDivisoesCliente">
		<adsm:textarea 
			property="dsMotivoSolicitacao"
			maxLength="500"
			required="true"
			columns="80"
			rows="9"
			label="motivo" 
			labelStyle="vertical-align:top"
			cellStyle="vertical-align:top"
			labelWidth="10%" 
			width="82%"/>
			
		<adsm:buttonBar>
			<adsm:button 
				buttonType="storeButton"
				caption="salvar" 
				onclick="setMotivoSolicitacao(this.form)"
				disabled="false" />
			<adsm:newButton onclick="habilitarSalvar()" id="newButton"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>
	function setMotivoSolicitacao(form) {
		if (!validateForm(form)) {
			return "false";
		}
		var dsMotivoSolicitacao = getElementValue("dsMotivoSolicitacao");
		var parentWindow = dialogArguments.window.document;
		setElementValue(parentWindow.getElementById("dsMotivoSolicitacao"), dsMotivoSolicitacao);
		self.close();
	}
</script>