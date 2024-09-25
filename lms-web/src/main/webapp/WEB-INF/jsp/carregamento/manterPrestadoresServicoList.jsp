<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.carregamento.manterPrestadoresServicoAction" onPageLoadCallBack="retornoCarregaPagina" >
	<adsm:form action="/carregamento/manterPrestadoresServico" idProperty="idPrestadorServico">

		<adsm:hidden property="pessoa.tpPessoa" value="F"/>
	
		<adsm:complement label="identificacao" labelWidth="18%" width="82%">
			<adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.list" />
			<adsm:textbox definition="IDENTIFICACAO_PESSOA" />
		</adsm:complement>	

		<adsm:hidden property="pessoa.idPessoa" />
		
		<adsm:textbox dataType="text" size="95%" labelWidth="18%" width="82%"
			property="pessoa.nmPessoa" label="nome" maxLength="50"/>
		
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" renderOptions="true" 
			label="situacao" labelWidth="18%" width="32%" />

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="prestadoresServico"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>

	<adsm:grid property="prestadoresServico" idProperty="idPrestadorServico" defaultOrder="pessoa_.nmPessoa"
		rows="12" unique="true">
		<adsm:gridColumn title="identificacao" 	property="pessoa.tpIdentificacao" isDomain="true" width="70" />
		<adsm:gridColumn title="" 				property="pessoa.nrIdentificacaoFormatado" width="100" align="right" />
		<adsm:gridColumn title="nome" 			property="pessoa.nmPessoa" width="60%" />
		<adsm:gridColumn title="situacao" 		property="tpSituacao" isDomain="true" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>

	</adsm:grid>
</adsm:window>

<script>

/**
 * Função para filtrar os tipos de identificação para mostrar apenas as pessoas Físicas.
 */	
function retornoCarregaPagina_cb(data, error){
	onPageLoad_cb(data,error);
	changeTypePessoaWidget({tpTipoElement:document.getElementById("pessoa.tpPessoa"), tpIdentificacaoElement:document.getElementById('pessoa.tpIdentificacao'), numberElement:document.getElementById('pessoa.nrIdentificacao'), tabCmd:'list'})
}

</script>