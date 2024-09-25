<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 

<adsm:window service="lms.pendencia.registrarOcorrenciasDocumentosServicoOcorrenciaLookupAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/pendencia/registrarOcorrenciasDocumentosServico" idProperty="idOcorrenciaPendencia">
		<adsm:hidden property="tpSituacao" value="A"/>
		<adsm:hidden property="idDoctoServico"/>
		<adsm:hidden property="blMatriz"/>
		<adsm:hidden property="blPermiteOcorParaManif"/>		
		<adsm:hidden property="idOcorrenciaBloqueio"/>
		
		<adsm:textbox property="cdOcorrencia" label="codigo" dataType="integer" 
					  size="3" maxLength="3"  labelWidth="15%" width="85%"/>
		<adsm:textbox property="dsOcorrencia" label="descricao" dataType="text" 
					  size="60" maxLength="60" width="85%"/>
		<adsm:combobox property="tpOcorrencia" label="tipo" 
					   domain="DM_TIPO_OCORRENCIA_PENDENCIA" width="85%" renderOptions="true"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="ocorrenciaPendencia"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid property="ocorrenciaPendencia" idProperty="idOcorrenciaPendencia" selectionMode="check" 
			   gridHeight="190" rows="9" scrollBars="horizontal" defaultOrder="cdOcorrencia"
			   service="lms.pendencia.registrarOcorrenciasDocumentosServicoOcorrenciaLookupAction.findPaginatedOcorrencias"
			   rowCountService="lms.pendencia.registrarOcorrenciasDocumentosServicoOcorrenciaLookupAction.getRowCountOcorrencias">
		<adsm:gridColumn title="codigo"           property="cdOcorrencia"  width="60" align="right" />
		<adsm:gridColumn title="descricao"        property="dsOcorrencia"  width="230"/>		
		<adsm:gridColumn title="tipo"             property="tpOcorrencia"  width="100" isDomain="true"/>
		<adsm:gridColumn title="descontaDPE"      property="blDescontoDPE" width="80" renderMode="image-check"/>
		<adsm:gridColumn title="exigeRNC"         property="blExigeRnc"  width="80"  renderMode="image-check"/>		
		<adsm:gridColumn title="apreensao"        property="blApreensao" width="80"  renderMode="image-check"/>		
		<adsm:gridColumn title="permissaoUnidade" property="tpPermissaoUnidade" width="120" isDomain="true" />
		<adsm:gridColumn title="permiteOcorrenciaManifesto" property="blPermiteOcorrenciaManifesto"  
															renderMode="image-check" width="200"/>

		<adsm:buttonBar >
			<adsm:button caption="fechar" id="btnFechar" onclick="self.close();" buttonType="closeButton" />				
		</adsm:buttonBar>		
	</adsm:grid>
</adsm:window>
<script>
	function pageLoad_cb(data, error) {
		onPageLoad_cb(data, error);
		mostraEscondeBotaoFechar();
	}
	function mostraEscondeBotaoFechar() {
		var isLookup = window.dialogArguments && window.dialogArguments.window;
		if (isLookup) {
			setDisabled('btnFechar',false);
		} else {
			setVisibility('btnFechar', false);
		}	
	}
</script>