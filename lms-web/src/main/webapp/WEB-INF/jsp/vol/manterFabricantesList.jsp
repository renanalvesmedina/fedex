<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	function myOnPageLoad(){
		onPageLoad();
	     initPessoaWidget({ tpTipoElement:document.getElementById("pessoa.tpPessoa")
	     , tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao")
	     , numberElement:document.getElementById("pessoa.idPessoa") });	
	}	
	
</script>

<adsm:window service="lms.vol.manterFabricantesAction" onPageLoad="myOnPageLoad">
	<adsm:form action="/vol/manterFabricantes">
		<adsm:hidden property="pessoa.tpPessoa" value="J" />
		<adsm:complement label="identificacao" labelWidth="18%" width="82%"> 
			<adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.list" />
			<adsm:textbox definition="IDENTIFICACAO_PESSOA"/>
		</adsm:complement>


		<adsm:hidden property="idFabricante" />

		<adsm:textbox dataType="text" size="95%" labelWidth="18%" width="82%"
			property="nmPessoa" label="nome" maxLength="50" />

	 	<adsm:combobox property="tpSituacao" domain="DM_STATUS" 
			label="situacao" labelWidth="18%" width="32%" />

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="listaPessoa" />
			<adsm:resetButton />
		</adsm:buttonBar>

	</adsm:form>

	<adsm:grid property="listaPessoa" idProperty="idFabricante" unique="true" rows="11"
		 service="lms.vol.manterFabricantesAction.findPaginatedFabricante" rowCountService="lms.vol.manterFabricantesAction.getRowCountFabricante">
		<adsm:gridColumn title="identificacao" property="tpIdentificacao" isDomain="true" width="60" align="left" />
		<adsm:gridColumn title="" property="nrIdentificacao" width="120" align="right" /> 
		<adsm:gridColumn title="fabricante" property="nmPessoa"	width="250"/>
		<adsm:gridColumn title="email" property="dsEmail" width="200" />
  		<adsm:gridColumn title="situacao" property="tpSituacao" width="100" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>