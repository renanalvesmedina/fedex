<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
<!--
	function pageLoadCustomizada(data,error) {
		onPageLoad();

		addServiceDataObject(createServiceDataObject("adsm.configuration.domainValueService.find", "tpEmpresa", {domain:{name:"DM_TIPO_EMPRESA"}}));
		xmit({onXmitDone:"returnOnPageLoad"});
		
		initPessoaWidget({tpTipoElement:document.getElementById("pessoa.tpPessoa")
   				   ,tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao")
      			   ,numberElement:document.getElementById("pessoa.nrIdentificacao")});
	}
	 
	function returnOnPageLoad_cb() {
		changeTypePessoaWidget(
			{tpTipoElement:document.getElementById("pessoa.tpPessoa"),
			 tpIdentificacaoElement:document.getElementById('pessoa.tpIdentificacao'),
			 numberElement:document.getElementById('pessoa.nrIdentificacao'),tabCmd:'list'});
	
		onPageLoad_cb();
		var tpEmpresa = document.getElementById("tpEmpresa");
		if (document.getElementById("flag").value == "telaFilial") {
			for(x = 0; x < tpEmpresa.length; x++) {
				if (tpEmpresa[x].value != "" && tpEmpresa[x].value != "M" && tpEmpresa[x].value != "P")
					tpEmpresa[x] = null;
			}
			empresaGridDef.service = "lms.municipios.empresaService.findPaginatedFilial";
			empresaGridDef.rowCountService = "lms.municipios.empresaService.getRowCountFilial";
		}
	}
	
//-->
</script>
<adsm:window service="lms.seguranca.manterUsuarioLMSEmpresaAction" onPageLoad="pageLoadCustomizada">
	<adsm:form idProperty="idEmpresa" action="/seguranca/manterUsuarioLMSEmpresa" >
		<adsm:hidden property="flag" serializable="false" value="" />
		<adsm:hidden property="pessoa.tpPessoa" value="J" />
		
		<adsm:complement label="identificacao" labelWidth="18%" width="82%">
            <adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.list"/>
            <adsm:textbox definition="IDENTIFICACAO_PESSOA"/>
		</adsm:complement>
	    <adsm:textbox dataType="text" property="pessoa.nmPessoa" label="razaoSocial" maxLength="50" size="35" labelWidth="18%" width="32%" />
		<adsm:textbox dataType="text" property="pessoa.nmFantasia" label="nomeFantasia" maxLength="60" size="35" labelWidth="18%" width="32%" />
	    
		<adsm:combobox property="tpEmpresa" label="tipoEmpresa" domain="DM_TIPO_EMPRESA" labelWidth="18%" width="32%" autoLoad="false" />
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" labelWidth="18%" width="32%"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="empresa" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idEmpresa" property="empresa" selectionMode="check" unique="true"
			rows="11" defaultOrder="tpEmpresa,pessoa_.nmPessoa"
			service="lms.seguranca.manterUsuarioLMSEmpresaAction.findPaginatedEmpresa"
			rowCountService="lms.seguranca.manterUsuarioLMSEmpresaAction.getRowCountEmpresa"
			>
		<adsm:gridColumn title="tipoEmpresa" property="tpEmpresa" isDomain="true" width="105" />
		<adsm:gridColumn title="identificacao" property="pessoa.tpIdentificacao" isDomain="true" width="40" align="left"/>
		<adsm:gridColumn title="" property="pessoa.nrIdentificacaoFormatado" width="110" dataType="text" align="right" />		
		<adsm:gridColumn title="razaoSocial" property="pessoa.nmPessoa"/>
		<adsm:gridColumn title="nomeFantasia" property="pessoa.nmFantasia"/>
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="70" />
		<adsm:buttonBar>
			<adsm:removeButton /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>