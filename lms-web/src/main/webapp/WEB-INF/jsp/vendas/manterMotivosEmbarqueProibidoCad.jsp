<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.motivoProibidoEmbarqueService">
	<adsm:form action="/vendas/manterMotivosEmbarqueProibido" idProperty="idMotivoProibidoEmbarque">
        <adsm:textbox dataType="text" property="dsMotivoProibidoEmbarque" label="descricao" maxLength="60" size="70" width="75%" required="true"/>
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" required="true"/>

		<adsm:checkbox property="blFinanceiro" label="motivoFinanceiro"  />
        <adsm:hidden property="empresa.idEmpresa" />
         
		<adsm:buttonBar>
		    <adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>