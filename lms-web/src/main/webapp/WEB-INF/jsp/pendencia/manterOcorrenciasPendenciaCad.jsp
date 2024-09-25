<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.pendencia.manterOcorrenciasPendenciaAction">
	<adsm:form action="/pendencia/manterOcorrenciasPendencia" 
				idProperty="idOcorrenciaPendencia" 
				service="lms.pendencia.manterOcorrenciasPendenciaAction.findByIdCustom">

		<adsm:textbox property="cdOcorrencia" dataType="integer" size="3" maxLength="3" label="codigo" labelWidth="15%" width="85%" required="true"/>
		<adsm:textbox label="descricao" property="dsOcorrencia" dataType="text" size="60" maxLength="60" width="85%" required="true"/>
		<adsm:combobox property="tpOcorrencia" label="tipo" domain="DM_TIPO_OCORRENCIA_PENDENCIA" width="85%" required="true" renderOptions="true"/>

		<adsm:hidden property="tpSituacaoEvento" value="A" serializable="false" />
		<adsm:lookup property="evento" service="lms.pendencia.manterOcorrenciasPendenciaAction.findLookupEvento" 
		 			action="/sim/manterEventosDocumentosServico" dataType="integer" idProperty="idEvento" criteriaProperty="cdEvento" serializable="true"
					label="evento" width="85%" size="5" labelWidth="15%" required="true" maxLength="3">
				<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacaoEvento" />
				<adsm:propertyMapping relatedProperty="evento.dsEvento" modelProperty="dsEvento"/>
				<adsm:propertyMapping relatedProperty="evento.codigoEvento" modelProperty="cdEvento"/>
				<adsm:textbox dataType="text" size="80" disabled="true" serializable="false" property="evento.dsEvento"/>
				<adsm:hidden property="evento.codigoEvento"/>
		</adsm:lookup>
		
		
		
		<adsm:checkbox label="descontaDPE" property="blDescontaDPE" />
		<adsm:checkbox label="exigeRNC" property="blExigeRNC" labelWidth="23%" width="27%"/>
		<adsm:combobox label="permissaoUnidade" property="tpPermissaoUnidade" domain="DM_PERMISSAO_UNIDADE_PENDENCIA" required="true" width="85%" renderOptions="true"/>
		<adsm:checkbox label="apreensao" property="blApreensao" />
		<adsm:checkbox label="permiteOcorrenciaManifesto" property="blPermiteOcorrenciaManifesto" labelWidth="23%" width="27%"/>
		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS" required="true" renderOptions="true"/>
		<adsm:buttonBar freeLayout="false">
			<adsm:storeButton  service="lms.pendencia.manterOcorrenciasPendenciaAction.storeCustom" />
			<adsm:button caption="faseProcesso" action="/pendencia/manterFaseOcorrencia" cmd="main">
        		<adsm:linkProperty src="idOcorrenciaPendencia" target="ocorrenciaPendencia.idOcorrenciaPendencia"/>
        		<adsm:linkProperty src="dsOcorrencia" target="ocorrenciaPendencia.dsOcorrencia"/>
        	</adsm:button>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>

	</adsm:form>
</adsm:window>