<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterTiposValorAction">
	<adsm:form action="/vendas/manterTiposValorCpt">
        				
		<adsm:textbox  property="dsTipoValor"  dataType="text" label="descricao" width="55%" size="70" labelWidth="10%" />

		<adsm:combobox property="tpGrupoValor" label="tpGrupoValorCpt" width="10%" labelWidth="6%" domain="DM_CPT_TIPO_GRUPO_VALOR" />

		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="40%" labelWidth="10%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="cptTiposValorCpt" />
			<adsm:resetButton />
		</adsm:buttonBar>

	</adsm:form>
	<adsm:grid property="cptTiposValorCpt" idProperty="idCptTipoValor" defaultOrder="dsTipoValor" rows="12">
	    
	    <adsm:gridColumn title="descricao" 	property="dsTipoValor" 	width="" />
		<adsm:gridColumn title="tpGrupoValorCpt" 	property="tpGrupoValor" width="100" isDomain="true" />
		<adsm:gridColumn title="situacao" 		property="tpSituacao" 	width="100" isDomain="true"/>		

		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>
</script>