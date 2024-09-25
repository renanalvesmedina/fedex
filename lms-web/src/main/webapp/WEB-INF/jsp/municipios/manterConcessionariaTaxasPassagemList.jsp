<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
	function pageLoadCustom_cb(data,error) {
		initPessoaWidget({tpTipoElement:document.getElementById("pessoa.tpPessoa")
   				   ,tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao")
      			   ,numberElement:document.getElementById("pessoa.nrIdentificacao")});

		changeTypePessoaWidget(
				{tpTipoElement:document.getElementById("pessoa.tpPessoa"),
				 tpIdentificacaoElement:document.getElementById('pessoa.tpIdentificacao'),
				 numberElement:document.getElementById('pessoa.nrIdentificacao'),tabCmd:'list'});

		onPageLoad_cb(data,error);
	}

	function pageLoadConcessionaria() {
   		onPageLoad();
	}
</script>
<adsm:window service="lms.municipios.manterConcessionariaTaxasPassagemAction" onPageLoadCallBack="pageLoadCustom" onPageLoad="pageLoadConcessionaria">
	<adsm:form idProperty="idConcessionaria" action="/municipios/manterConcessionariaTaxasPassagem">
		<adsm:hidden property="pessoa.tpPessoa" value="J" />

        <adsm:complement label="identificacao" labelWidth="18%" width="32%">
            <adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.list"/>
            <adsm:textbox definition="IDENTIFICACAO_PESSOA"/>
		</adsm:complement>
	    <adsm:textbox dataType="text" property="pessoa.nmPessoa" label="razaoSocial" maxLength="60" size="35" labelWidth="18%" width="32%" />	

		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" labelWidth="18%" width="82%"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="concessionaria" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idConcessionaria" property="concessionaria" rows="13" unique="true" defaultOrder="pessoa_.nmPessoa">
		<adsm:gridColumn title="identificacao" property="pessoa.tpIdentificacao" isDomain="true" width="65" />
		<adsm:gridColumn title="" property="pessoa.nrIdentificacaoFormatado" width="100" dataType="text" align="right" />		
		<adsm:gridColumn title="razaoSocial" property="pessoa.nmPessoa"/>
		<adsm:gridColumn title="homepage" property="dsHomePage" width="140" /> 
		<adsm:gridColumn title="email" property="pessoa.dsEmail" width="140" />	
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="72" />	
		<adsm:buttonBar>
			<adsm:removeButton /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window> 