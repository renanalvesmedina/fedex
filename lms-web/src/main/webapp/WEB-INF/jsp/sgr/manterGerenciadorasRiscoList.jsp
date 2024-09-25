<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sgr.gerenciadoraRiscoService" onPageLoadCallBack="gerenciadora_pageLoad">
	<adsm:form action="/sgr/manterGerenciadorasRisco">
	
		<adsm:hidden property="pessoa.tpPessoa" value="J" />
		<adsm:complement label="identificacao" width="85%">
            <adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.list"/>
            <adsm:textbox definition="IDENTIFICACAO_PESSOA"/>
		</adsm:complement>
		
		<adsm:textbox dataType="text" size="40" property="pessoa.nmPessoa" label="razaoSocial" maxLength="50" width="85%"/>
		<adsm:textbox dataType="text" size="40" property="pessoa.dsEmail" label="email" maxLength="60" width="35%"/>
		<adsm:textbox dataType="text" size="40" label="homepage" property="dsEnderecoWeb"  maxLength="80"/>
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="85%" renderOptions="true" />
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gerenciadoraRisco"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid property="gerenciadoraRisco" idProperty="idGerenciadoraRisco" 
			   rows="10" selectionMode="check" unique="true" defaultOrder="pessoa_.nmPessoa:asc">
		<adsm:gridColumn title="identificacao" 	property="pessoa.tpIdentificacao" isDomain="true" width="80" />
		<adsm:gridColumn title="" 				property="pessoa.nrIdentificacaoFormatado" width="150" align="right"/>
		<adsm:gridColumn title="razaoSocial" 	property="pessoa.nmPessoa" />
		<adsm:gridColumn title="situacao" 		property="tpSituacao" width="15%" isDomain="true" />
		<adsm:buttonBar>
			<adsm:removeButton service="lms.sgr.gerenciadoraRiscoService.removeGerenciadorasByIds"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>
	/**
	 * Função para arrumar o dataType quando ocorre o click no botão limpar.
	 */
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click")
			document.getElementById("pessoa.nrIdentificacao").dataType = "text";
	}

	/**
	 * Função para filtrar os tipos de identificação para mostrar apenas os Jurídicos.
	 */
	function gerenciadora_pageLoad_cb(data, error){
		changeTypePessoaWidget({tpTipoElement:document.getElementById("pessoa.tpPessoa"), tpIdentificacaoElement:document.getElementById('pessoa.tpIdentificacao'), numberElement:document.getElementById('pessoa.nrIdentificacao'), tabCmd:'list'})
	}
</script>