<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window>

	<adsm:form action="/configuracoes/emitirOcorrenciaAtualizacaoAutomaticaEnderecoPadrao">
	
		<adsm:range label="dataAtualizacao" labelWidth="20%" width="30%" >
			<adsm:textbox dataType="JTDate" property="dtInicial"/>
			<adsm:textbox dataType="JTDate" property="dtFinal"/> 
		</adsm:range>
		
		<adsm:combobox property="tpOcorrencia" 
			label="tipoOcorrencia" 
			domain="DM_TIPO_OCORRENCIA_ENDERECO"
			labelWidth="20%" width="30%"/>
		
		<adsm:combobox property="tpFormatoRelatorio" 
			required="true"
			defaultValue="pdf"
			label="formatoRelatorio" 
			domain="DM_FORMATO_RELATORIO"
			labelWidth="20%" width="80%"/>

		<adsm:buttonBar>
		
			<adsm:reportViewerButton service="lms.configuracoes.emitirOcorrenciaAtualizacaoAutomaticaEnderecoPadraoAction" disabled="false"/>
			
			<adsm:resetButton/>
			
		</adsm:buttonBar>
		
	</adsm:form>
	
</adsm:window>

<script>


</script>