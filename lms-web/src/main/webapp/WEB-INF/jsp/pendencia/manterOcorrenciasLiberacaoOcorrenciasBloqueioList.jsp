<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.pendencia.manterOcorrenciasLiberacaoOcorrenciasBloqueioAction">
	<adsm:form action="/pendencia/manterOcorrenciasLiberacaoOcorrenciasBloqueio" idProperty="idLiberacaoBloqueio" >
		<adsm:lookup property="ocorrenciaPendenciaByIdOcorrenciaBloqueio" 
					 idProperty="idOcorrenciaPendencia" 
					 criteriaProperty="cdOcorrencia" 
					 label="ocorrenciaBloqueio"
					 action="/pendencia/manterOcorrenciasPendencia" 
					 service="lms.pendencia.manterOcorrenciasLiberacaoOcorrenciasBloqueioAction.findLookupOcorrenciaPendencia"
					 cmd="list" 
					 size="3" 
					 maxLength="3"
					 dataType="integer"
					 labelWidth="20%"					 
					 width="80%"
					 >
			<adsm:hidden property="flagBloqueio" serializable="false" value="B"/>
			<adsm:propertyMapping criteriaProperty="flagBloqueio" modelProperty="tpOcorrencia"/>
			<adsm:textbox property="ocorrenciaPendenciaByIdOcorrenciaBloqueio.dsOcorrencia" size="40" disabled="true" dataType="text" serializable="false"/>
			<adsm:propertyMapping relatedProperty="ocorrenciaPendenciaByIdOcorrenciaBloqueio.dsOcorrencia" modelProperty="dsOcorrencia"/>
		</adsm:lookup>
		<adsm:lookup property="ocorrenciaPendenciaByIdOcorrenciaLiberacao" 
					 idProperty="idOcorrenciaPendencia" 
					 criteriaProperty="cdOcorrencia" 
					 label="ocorrenciaLiberacao"
					 action="/pendencia/manterOcorrenciasPendencia" 
					 service="lms.pendencia.manterOcorrenciasLiberacaoOcorrenciasBloqueioAction.findLookupOcorrenciaPendencia"
					 cmd="list" 
					 size="3" 
					 maxLength="3"
					 dataType="integer"
					 labelWidth="20%"					 
					 width="80%"
					 >
			<adsm:hidden property="flagLiberacao" serializable="false" value="L"/>
			<adsm:propertyMapping criteriaProperty="flagLiberacao" modelProperty="tpOcorrencia"/>
			<adsm:textbox property="ocorrenciaPendenciaByIdOcorrenciaLiberacao.dsOcorrencia" size="40" disabled="true" dataType="text" serializable="false"/>
			<adsm:propertyMapping relatedProperty="ocorrenciaPendenciaByIdOcorrenciaLiberacao.dsOcorrencia" modelProperty="dsOcorrencia"/>
		</adsm:lookup>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="liberacaoBloqueio"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="liberacaoBloqueio" 
				idProperty="idLiberacaoBloqueio" 
				selectionMode="check" 
				gridHeight="140" 
				unique="true"
				rows="13"
				service="lms.pendencia.manterOcorrenciasLiberacaoOcorrenciasBloqueioAction.findPaginatedCustom"
				rowCountService="lms.pendencia.manterOcorrenciasLiberacaoOcorrenciasBloqueioAction.getRowCount"
				>
				
				
		<adsm:gridColumnGroup customSeparator=" - " >
		<adsm:gridColumn property="ocorrenciaPendenciaByIdOcorrenciaBloqueio.cdOcorrencia" title="ocorrenciaBloqueio" width="20" />
		<adsm:gridColumn property="ocorrenciaPendenciaByIdOcorrenciaBloqueio.dsOcorrencia" title="" width="330" />
		</adsm:gridColumnGroup>


		<adsm:gridColumnGroup customSeparator=" - " >
		<adsm:gridColumn property="ocorrenciaPendenciaByIdOcorrenciaLiberacao.cdOcorrencia" title="ocorrenciaLiberacao" width="20" />
		<adsm:gridColumn property="ocorrenciaPendenciaByIdOcorrenciaLiberacao.dsOcorrencia" title="" width="330" />
		</adsm:gridColumnGroup>
				
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
