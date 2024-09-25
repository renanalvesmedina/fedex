<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
<!--
	function pageLoadCustom_cb(data,error) {
		onPageLoad_cb(data,error);
		changeTypePessoaWidget(
				{tpTipoElement:document.getElementById("pessoa.tpPessoa"),
				 tpIdentificacaoElement:document.getElementById('pessoa.tpIdentificacao'),
				 numberElement:document.getElementById('pessoa.nrIdentificacao'),tabCmd:'list'});
	}
	
	function pageLoadOperadora() {
   		onPageLoad();
 		initPessoaWidget({tpTipoElement:document.getElementById("pessoa.tpPessoa")
   				   ,tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao")
      			   ,numberElement:document.getElementById("pessoa.nrIdentificacao")});
	} 
//-->
</script>
<adsm:window service="lms.contratacaoveiculos.operadoraMctService" onPageLoadCallBack="pageLoadCustom" onPageLoad="pageLoadOperadora">
	<adsm:form action="contratacaoVeiculos/manterOperadorasMCT" idProperty="idOperadoraMct" >
		<adsm:hidden property="pessoa.tpPessoa" value="J" />
		<adsm:complement label="identificacao" >
            <adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.list"/>
            <adsm:textbox definition="IDENTIFICACAO_PESSOA"/>
		</adsm:complement>
	    <adsm:textbox dataType="text" property="pessoa.nmPessoa" label="razaoSocial" labelWidth="12%" maxLength="50" size="50" />	
		
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="85%" />
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="operadoraMct" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="operadoraMct" idProperty="idOperadoraMct" rows="13" unique="true" defaultOrder="pessoa_.nmPessoa" >
		<adsm:gridColumn title="identificacao" property="pessoa.tpIdentificacao" isDomain="true" width="60" align="left"/>
		<adsm:gridColumn title="" property="pessoa.nrIdentificacaoFormatado" align="right" width="130"/>		
		<adsm:gridColumn title="razaoSocial" property="pessoa.nmPessoa"/>
		<adsm:gridColumn title="homepage" property="dsHomepage" width="140"/>
		<adsm:gridColumn title="email" property="pessoa.dsEmail" width="140"/>
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="80"/>
		<adsm:buttonBar>
			<adsm:removeButton /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>