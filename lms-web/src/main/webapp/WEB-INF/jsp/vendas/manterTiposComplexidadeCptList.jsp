<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterTiposComplexidadeAction">
	<adsm:form action="/vendas/manterTiposComplexidadeCpt">
        				
		<adsm:combobox 
			property="cptTipoValor.idCptTipoValor" 
			label="tipoValor" 
			onlyActiveValues="true"
			optionLabelProperty="dsTipoValor" 
			optionProperty="idCptTipoValor" 
			service="lms.vendas.manterTiposValorAction.findTiposValor" 
			width="35%" 
			boxWidth="200"/>

		<adsm:combobox 
			property="tpMedidaComplexidade" 
			label="tpMedidaComplexidade" 
			domain="DM_CPT_TIPO_MEDIDA_COMPLEXIDADE" labelWidth="15%"/>

		<adsm:combobox 
			property="tpComplexidade" 
			label="tpComplexidade" domain="DM_CPT_TIPO_COMPLEXIDADE" />

		<adsm:combobox 
			property="tpSituacao" 
			label="situacao" domain="DM_STATUS" />

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="cptComplexidade" />
			<adsm:resetButton />
		</adsm:buttonBar>

	</adsm:form>
	<adsm:grid property="cptComplexidade" idProperty="idCptComplexidade" rows="12" defaultOrder="cptTipoValor_.dsTipoValor,tpComplexidade">
	    
		<adsm:gridColumn title="tipoValor" property="cptTipoValor.dsTipoValor" width="35%"/>
	    <adsm:gridColumn title="tpComplexidade" property="tpComplexidade" width="20%" isDomain="true" />
		<adsm:gridColumn title="valor" property="vlComplexidade" width="10%" dataType="decimal" />
	    <adsm:gridColumn title="tpMedidaComplexidade" property="tpMedidaComplexidade" width="25%" isDomain="true"/>
		<adsm:gridColumn title="situacao" property="tpSituacao" width="10%" isDomain="true"/>		

		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>
</script>