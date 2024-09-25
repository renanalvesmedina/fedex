<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	function myOnPageLoad(){
		onPageLoad();
		//setDisabled("pessoa.nrIdentificacao", true );
	     initPessoaWidget({ tpTipoElement:document.getElementById("pessoa.tpPessoa")
	     , tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao")
	       , numberElement:document.getElementById("pessoa.idPessoa") });		
	}
	
	
</script>
<adsm:window service="lms.vendas.despachanteService" onPageLoad="myOnPageLoad">
	<adsm:form action="/vendas/manterDespachantes">
        <adsm:combobox property="pessoa.tpPessoa" onlyActiveValues="true" labelWidth="15%" width="30%" label="tipoPessoa" domain="DM_TIPO_PESSOA" definition="TIPO_PESSOA.list" />	
		
		<adsm:complement label="identificacao" labelWidth="15%" width="40%">
			<adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.list" />
			<adsm:textbox definition="IDENTIFICACAO_PESSOA"/>
		</adsm:complement>	
		
		<adsm:textbox dataType="text" property="pessoa.nmPessoa" 
				label="nome" maxLength="50" size="50" 
				labelWidth="15%" width="40%" />
				
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" />

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="despachante" />
			<adsm:resetButton />
		</adsm:buttonBar>

	</adsm:form>
	<adsm:grid property="despachante" idProperty="idDespachante" defaultOrder="pessoa_.nmPessoa"  rows="12">
	    <adsm:gridColumn title="tipoPessoa" property="pessoa.tpPessoa" width="" isDomain="true"/>
		<adsm:gridColumn title="identificacao" property="pessoa.tpIdentificacao" isDomain="true" width="40"/>
		<adsm:gridColumn title="" width="100"  property="pessoa.nrIdentificacaoFormatado" align="right"/>
		<adsm:gridColumn title="nome" property="pessoa.nmPessoa" width="390"/>
		<adsm:gridColumn title="situacao" property="tpSituacao" width="100" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton service="lms.vendas.despachanteService.removeDespachanteByIds"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>
</script>