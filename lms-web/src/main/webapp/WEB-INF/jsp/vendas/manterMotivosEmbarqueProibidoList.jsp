<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.motivoProibidoEmbarqueService">
	<adsm:form action="/vendas/manterMotivosEmbarqueProibido" idProperty="idMotivoProibidoEmbarque">
        <adsm:textbox dataType="text" property="dsMotivoProibidoEmbarque" label="descricao" maxLength="60" size="70" width="75%"/>
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" />

		<adsm:combobox property="blFinanceiro" label="motivoFinanceiro" domain="DM_SIM_NAO" />

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="motivosProibidoEmbarque"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid property="motivosProibidoEmbarque" defaultOrder="dsMotivoProibidoEmbarque" idProperty="idMotivoProibidoEmbarque" selectionMode="check" rows="13">
		<adsm:gridColumn title="descricao" property="dsMotivoProibidoEmbarque" width="60%" />
		<adsm:gridColumn title="situacao" property="tpSituacao" width="20%" isDomain="true"/>
		<adsm:gridColumn title="motivoFinanceiro" property="blFinanceiro" width="20%" renderMode="image-check"/>
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
