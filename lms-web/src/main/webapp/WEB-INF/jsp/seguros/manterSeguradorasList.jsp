<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguros.seguradoraService" onPageLoadCallBack="seguradora_pageLoad">
	<adsm:form action="/seguros/manterSeguradoras" idProperty="idSeguradora" >

		<adsm:hidden property="pessoa.tpPessoa" value="J" />
		<adsm:complement label="identificacao" width="85%">
            <adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.list"/>
            <adsm:textbox definition="IDENTIFICACAO_PESSOA"/>
		</adsm:complement>

		<adsm:textbox dataType="text" property="pessoa.nmPessoa" label="razaoSocial" size="40" maxLength="50" width="85%" />
		<adsm:textbox dataType="text" property="pessoa.dsEmail" label="email" size="40" maxLength="60" width="35%" />
		<adsm:textbox dataType="text" property="dsEnderecoWeb" label="homepage" size="40" maxLength="80" width="35%" />
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="85%" renderOptions="true" />

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="seguradoras" />
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid property="seguradoras" idProperty="idSeguradora" rows="11" defaultOrder="pessoa_.nmPessoa:asc" selectionMode="check" 
			   gridHeight="200" unique="true">
		<adsm:gridColumn title="identificacao" 	property="pessoa.tpIdentificacao" isDomain="true" width="80" />
		<adsm:gridColumn title="" 				property="pessoa.nrIdentificacaoFormatado" dataType="text" width="150" align="right" />
		<adsm:gridColumn title="razaoSocial" 	property="pessoa.nmPessoa" />
		<adsm:gridColumn title="situacao" 		property="tpSituacao" isDomain="true" width="10%" />
		<adsm:buttonBar>
			<adsm:removeButton service="lms.seguros.seguradoraService.removeSeguradoraByIds" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>
	/**
	 * Fun��o para arrumar o dataType quando ocorre o click no bot�o limpar.
	 */
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click")
			document.getElementById("pessoa.nrIdentificacao").dataType = "text";
	}
	
	/**
	 * Fun��o para filtrar os tipos de identifica��o para mostrar apenas os Jur�dicos.
	 */	
	function seguradora_pageLoad_cb(data, error){
		changeTypePessoaWidget({tpTipoElement:document.getElementById("pessoa.tpPessoa"), tpIdentificacaoElement:document.getElementById('pessoa.tpIdentificacao'), numberElement:document.getElementById('pessoa.nrIdentificacao'), tabCmd:'list'})
	}
</script>