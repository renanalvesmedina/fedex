<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.pendencia.manterOcorrenciasLiberacaoOcorrenciasBloqueioAction" >
	<adsm:form action="/pendencia/manterOcorrenciasLiberacaoOcorrenciasBloqueio" idProperty="idLiberacaoBloqueio" service="lms.pendencia.manterOcorrenciasLiberacaoOcorrenciasBloqueioAction.findByIdCustom">
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
					 required="true"
					 >
			<adsm:hidden property="flagBloqueio" serializable="false" value="B"/>
			<adsm:propertyMapping criteriaProperty="flagBloqueio" modelProperty="tpOcorrencia"/>
			<adsm:textbox property="ocorrenciaPendenciaByIdOcorrenciaBloqueio.dsOcorrencia"	size="40" disabled="true" dataType="text" serializable="false" />
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
					 required="true"
					 >
			<adsm:hidden property="flagLiberacao" serializable="false" value="L"/>
			<adsm:propertyMapping criteriaProperty="flagLiberacao" modelProperty="tpOcorrencia"/>
			<adsm:textbox property="ocorrenciaPendenciaByIdOcorrenciaLiberacao.dsOcorrencia" size="40" disabled="true" dataType="text" serializable="false"/>
			<adsm:propertyMapping relatedProperty="ocorrenciaPendenciaByIdOcorrenciaLiberacao.dsOcorrencia" modelProperty="dsOcorrencia"/>
		</adsm:lookup>
		<adsm:buttonBar>
			<adsm:storeButton service="lms.pendencia.manterOcorrenciasLiberacaoOcorrenciasBloqueioAction.storeLiberacaoBloqueio"/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>