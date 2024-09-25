<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sgr.escoltaService" onPageLoadCallBack="escolta_pageLoad">
	<adsm:form action="/sgr/manterEmpresasEscolta">
	
		<adsm:hidden property="pessoa.tpPessoa" value="J" />
		<adsm:complement label="identificacao" width="85%">
            <adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.list"/>
            <adsm:textbox definition="IDENTIFICACAO_PESSOA"/>
		</adsm:complement>
		
		<adsm:textbox dataType="text" property="pessoa.nmPessoa" label="razaoSocial" size="40" maxLength="50" width="85%" />
		<adsm:textbox dataType="text" property="pessoa.dsEmail" label="email" size="40" maxLength="60" width="35%" />
		<adsm:textbox dataType="text" property="dsEnderecoWeb" label="homepage" maxLength="60" size="40" />
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="85%" renderOptions="true" />
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="escoltas"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid property="escoltas" idProperty="idEscolta" 
			   rows="10" defaultOrder="pessoa_.nmPessoa" gridHeight="200" unique="true">
		<adsm:gridColumn title="identificacao" 	property="pessoa.tpIdentificacao" isDomain="true" width="80" />
		<adsm:gridColumn title="" 			   	property="pessoa.nrIdentificacaoFormatado" width="150" dataType="text" align="right" />		
		<adsm:gridColumn title="razaoSocial" 	property="pessoa.nmPessoa" />
		<adsm:buttonBar>
			<adsm:removeButton service="lms.sgr.escoltaService.removeEscoltaByIds" />
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
	function escolta_pageLoad_cb(data, error){
		changeTypePessoaWidget({tpTipoElement:document.getElementById("pessoa.tpPessoa"), tpIdentificacaoElement:document.getElementById('pessoa.tpIdentificacao'), numberElement:document.getElementById('pessoa.nrIdentificacao'), tabCmd:'list'})
	}
</script>