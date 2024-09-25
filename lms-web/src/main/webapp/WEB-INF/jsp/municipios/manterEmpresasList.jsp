<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
<!--
	function pageLoadCustomizada(data,error) {
		onPageLoad();
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
			var sdo = createServiceDataObject("adsm.configuration.domainValueService.find", "setaParamTpEmpresa", {domain:{name:"DM_TIPO_EMPRESA_FILIAL"}});
			xmit({serviceDataObjects:[sdo]});
			empresaGridDef.service = "lms.municipios.empresaService.findPaginatedFilial";
			empresaGridDef.rowCountService = "lms.municipios.empresaService.getRowCountFilial";
		}
	}
	
	function setaParamTpEmpresa_cb(){
		var tpEmpresa = document.getElementById("tpEmpresa");
		setElementValue("tpEmpresa", tpEmpresa.value);
	}
	
	function onChangeSgEmpresa(field) {
		field.value = field.value.toUpperCase();
		return validate(field);
	}
//-->
</script>
<adsm:window service="lms.municipios.empresaService" onPageLoad="pageLoadCustomizada" onPageLoadCallBack="returnOnPageLoad">
	<adsm:form idProperty="idEmpresa" action="/municipios/manterEmpresas" >
		<adsm:hidden property="flag" serializable="false" value="" />
		<adsm:hidden property="pessoa.tpPessoa" value="J" />
		
		<adsm:complement label="identificacao" labelWidth="18%" width="82%">
            <adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.list"/>
            <adsm:textbox definition="IDENTIFICACAO_PESSOA"/>
		</adsm:complement>
	    <adsm:textbox dataType="text" property="pessoa.nmPessoa" label="razaoSocial" maxLength="50" size="35" labelWidth="18%" width="32%" />
	    <adsm:textbox dataType="text" property="sgEmpresa" label="sigla" maxLength="3" size="3" labelWidth="18%" width="15%" onchange="return onChangeSgEmpresa(this);" />
		<adsm:textbox dataType="text" property="pessoa.nmFantasia" label="nomeFantasia" maxLength="60" size="35" labelWidth="18%" width="32%" />
	    
		<adsm:combobox property="tpEmpresa" label="tipoEmpresa" domain="DM_TIPO_EMPRESA" labelWidth="18%" width="32%" />
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" labelWidth="18%" width="32%"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="empresa" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idEmpresa" property="empresa" selectionMode="check" unique="true"
			rows="11" defaultOrder="tpEmpresa,pessoa_.nmPessoa">
		<adsm:gridColumn title="tipoEmpresa" property="tpEmpresa" isDomain="true" width="105" />
		<adsm:gridColumn title="identificacao" property="pessoa.tpIdentificacao" isDomain="true" width="40" align="left"/>
		<adsm:gridColumn title="" property="pessoa.nrIdentificacaoFormatado" width="110" dataType="text" align="right" />		
		<adsm:gridColumn title="razaoSocial" property="pessoa.nmPessoa"/>
		<adsm:gridColumn title="sigla" property="sgEmpresa" width="80" />
		<adsm:gridColumn title="nomeFantasia" property="pessoa.nmFantasia"/>
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="70" />
		<adsm:buttonBar>
			<adsm:removeButton /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>