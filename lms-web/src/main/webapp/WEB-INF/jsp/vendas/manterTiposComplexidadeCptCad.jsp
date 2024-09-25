<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterTiposComplexidadeAction">
	<adsm:form action="/vendas/manterTiposComplexidadeCpt" idProperty="idCptComplexidade">
		
		<adsm:combobox 
			property="cptTipoValor.idCptTipoValor" 
			label="tipoValor" 
			onlyActiveValues="true"
			optionLabelProperty="dsTipoValor" 
			optionProperty="idCptTipoValor" 
			service="lms.vendas.manterTiposValorAction.findTiposValor" 
			width="30%" required="true" labelWidth="18%"
			boxWidth="200"/>

		<adsm:combobox property="tpComplexidade" label="tpComplexidade" domain="DM_CPT_TIPO_COMPLEXIDADE" width="15%" required="true"/>

		<adsm:combobox property="tpMedidaComplexidade" label="tpMedidaComplexidade" labelWidth="18%" width="30%" domain="DM_CPT_TIPO_MEDIDA_COMPLEXIDADE" required="true"/>

		<adsm:textbox size="12" width="20%"  maxLength="6" dataType="decimal" property="vlComplexidade" 
			label="valor" minLength="0" mask="##0.00" required="true" labelWidth="15%"/>

		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" required="true" labelWidth="18%"/>
                                      
		<adsm:buttonBar>				
			<adsm:storeButton />
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>	
<script>
	
</script>

