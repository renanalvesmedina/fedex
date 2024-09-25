<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterTiposClassificacaoClienteAction">
	<adsm:form action="/vendas/manterTiposClassificacaoCliente" idProperty="idTipoClassificacaoCliente"
		newService="lms.vendas.manterTiposClassificacaoClienteAction.newMaster">
        <adsm:textbox dataType="text" property="dsTipoClassificacaoCliente" label="descricao" maxLength="60" size="53" required="true" width="45%"/>
		<adsm:checkbox property="blOpcional" label="opcional" width="15%"/>

		<adsm:checkbox property="blEspecial" label="clienteEspecial" width="45%"/>
		<adsm:checkbox property="blEventual" label="clienteEventual" width="15%"/>
		
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="50%" required="true"/>		
		
		<adsm:hidden property="empresa.idEmpresa"/>

		<adsm:buttonBar>
			<adsm:storeButton id="storeButton"/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>