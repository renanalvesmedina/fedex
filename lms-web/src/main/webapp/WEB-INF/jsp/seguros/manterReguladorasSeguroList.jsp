<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguros.reguladoraSeguroService" onPageLoadCallBack="reguladora_pageLoad">
	<adsm:form action="/seguros/manterReguladorasSeguro" idProperty="idReguladora">
		<adsm:hidden property="pessoa.tpPessoa" value="J" />

		<adsm:complement label="identificacao" width="85%">
            <adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.list"/>
            <adsm:textbox definition="IDENTIFICACAO_PESSOA"/>
		</adsm:complement>

		<adsm:textbox dataType="text" property="pessoa.nmPessoa" label="razaoSocial" size="50" maxLength="50" width="85%" />
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="85%" renderOptions="true" />

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="reguladoras" />
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid property="reguladoras" idProperty="idReguladora" 
			   rows="11" defaultOrder="pessoa_.nmPessoa:asc" selectionMode="check" gridHeight="200" unique="true">
		<adsm:gridColumn title="identificacao" 	property="pessoa.tpIdentificacao" isDomain="true" width="80" />
		<adsm:gridColumn title="" 				property="pessoa.nrIdentificacaoFormatado" align="right" width="150" />
		<adsm:gridColumn title="razaoSocial" 	property="pessoa.nmPessoa" />
		<adsm:gridColumn title="situacao" 		property="tpSituacao" isDomain="true" width="10%" />
		<adsm:buttonBar>
			<adsm:removeButton service="lms.seguros.reguladoraSeguroService.removeReguladoraSeguroByIds" />
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
	function reguladora_pageLoad_cb(data, error){
		onPageLoad_cb(data,error);

		var nrIdentificacaoBkp = getElementValue("pessoa.nrIdentificacao");
		var tpIdentificacaoBkp = getElementValue("pessoa.tpIdentificacao");
		/** Essa função sempre reseta o valor da textbox */
		changeTypePessoaWidget({tpTipoElement:document.getElementById("pessoa.tpPessoa"), tpIdentificacaoElement:document.getElementById('pessoa.tpIdentificacao'), numberElement:document.getElementById('pessoa.nrIdentificacao'), tabCmd:'list'})

		if (tpIdentificacaoBkp != "") {
		   setElementValue("pessoa.tpIdentificacao", tpIdentificacaoBkp);
		}
		if (nrIdentificacaoBkp != "") {
		   setElementValue("pessoa.nrIdentificacao", nrIdentificacaoBkp);
		}
	}
</script>