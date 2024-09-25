<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterTiposClassificacaoClienteAction">
	<adsm:form action="/vendas/manterTiposClassificacaoCliente">
        <adsm:textbox dataType="text" property="dsTipoClassificacaoCliente" label="descricao" maxLength="60" size="53" width="45%"/>
		<adsm:combobox property="blOpcional" label="opcional" domain="DM_SIM_NAO" width="35%"/>		
		<adsm:combobox property="blEspecial" label="clienteEspecial" domain="DM_SIM_NAO" width="35%"/>		
		<adsm:combobox property="blEventual" label="clienteEventual" domain="DM_SIM_NAO" width="35%"/>								
		
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="35%"/>		

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="tipoClassificacaoCliente"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idTipoClassificacaoCliente" rows="12" defaultOrder="dsTipoClassificacaoCliente" property="tipoClassificacaoCliente" unique="true">
		<adsm:gridColumn title="descricao" property="dsTipoClassificacaoCliente" width="50%" />
		<adsm:gridColumn title="opcional" property="blOpcional" renderMode="image-check" width="10%" />
		<adsm:gridColumn title="clienteEspecial" property="blEspecial" width="20%" renderMode="image-check" />
		<adsm:gridColumn title="clienteEventual" property="blEventual" renderMode="image-check" width="20%" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
