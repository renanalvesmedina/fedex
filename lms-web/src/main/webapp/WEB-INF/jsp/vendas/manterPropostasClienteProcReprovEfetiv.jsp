<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.vendas.manterPropostasClienteFormalidadesAction">
	<adsm:form action="/vendas/manterPropostasCliente">
	
		<adsm:hidden property="idSimulacao"/>
		
		<adsm:combobox 
			property="idMotivoReprovacao" 
			label="motivo" 
			service="lms.vendas.motivoReprovacaoService.findMotivosReprovacao" 
			optionLabelProperty="dsMotivoReprovacao" 
			optionProperty="idMotivoReprovacao" 
			labelWidth="10%" 
			width="82%"
			required="true"/>
			
		<adsm:textarea 
			property="observacaoReprovacao"
			maxLength="500"
			required="true"
			columns="80"
			rows="9"
			label="observacao" 
			labelStyle="vertical-align:top"
			cellStyle="vertical-align:top"
			labelWidth="10%" 
			width="82%"/>
			
		<adsm:buttonBar>
			<adsm:button 
				caption="reprovarEfetivacao" 
				service="lms.vendas.manterPropostasClienteFormalidadesAction.executeReprovarEfetivacao" 
				callbackProperty="reprovEfetivacao"
				disabled="false" />
			<adsm:button 
				id="fechar"
				caption="fechar"
				onclick="self.close();"
				disabled="false"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>
	function reprovEfetivacao_cb() {
		self.close();
	}
	var u = new URL(parent.location.href);
	setElementValue("idSimulacao", u.parameters["idSimulacao"]);
</script>