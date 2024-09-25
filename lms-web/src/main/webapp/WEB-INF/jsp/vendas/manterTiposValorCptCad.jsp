<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterTiposValorAction" >

	<adsm:form action="/vendas/manterTiposValorCpt" id="formTipoValor" idProperty="idCptTipoValor">

		<adsm:textbox  
			property="dsTipoValor"  
			dataType="text"  labelWidth="10%"
			label="descricao" width="60%" size="70" required="true"/>

		<adsm:combobox 
			property="tpGrupoValor" 
			label="tpGrupoValorCpt" width="60%" labelWidth="10%"
			domain="DM_CPT_TIPO_GRUPO_VALOR" required="true"/>

		<adsm:combobox 
			property="tpSituacao" labelWidth="10%"
			label="situacao" width="60%"
			domain="DM_STATUS" required="true"/>
                                      
		<adsm:buttonBar>					
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>

	</adsm:form>
</adsm:window>

<script>

	

</script>
	

