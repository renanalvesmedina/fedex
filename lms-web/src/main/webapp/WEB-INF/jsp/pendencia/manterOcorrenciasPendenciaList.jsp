<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.pendencia.manterOcorrenciasPendenciaAction">
	<adsm:form action="/pendencia/manterOcorrenciasPendencia" idProperty="idOcorrenciaPendencia">
		<adsm:textbox property="cdOcorrencia" dataType="integer" size="3" maxLength="3" label="codigo" labelWidth="15%" width="85%" required="fasle"/>
		<adsm:textbox label="descricao" property="dsOcorrencia" dataType="text" size="60" maxLength="60" width="85%" required="false"/>
		<adsm:combobox property="tpOcorrencia" label="tipo" domain="DM_TIPO_OCORRENCIA_PENDENCIA" width="85%" required="false" renderOptions="true"/>
		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS" required="false" renderOptions="true"/>
		<adsm:hidden property="blDescontaDpe" />
		<adsm:hidden property="blPermiteOcorParaManif" />
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="ocorrenciaPendencia"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="ocorrenciaPendencia" 
				idProperty="idOcorrenciaPendencia" 
				selectionMode="check" 
				gridHeight="200" 
				rows="10" 
				scrollBars="horizontal"
				service="lms.pendencia.manterOcorrenciasPendenciaAction.findPaginatedCustom"
				rowCountService="lms.pendencia.manterOcorrenciasPendenciaAction.getRowCountCustom"
				defaultOrder="cdOcorrencia"
				>
		<adsm:gridColumn title="codigo"           property="cdOcorrencia"  width="60" align="right" />
		<adsm:gridColumn title="descricao"        property="dsOcorrencia"  width="230"/>		
		<adsm:gridColumn title="tipo"             property="tpOcorrencia"  width="100" isDomain="true"/>
		<adsm:gridColumn title="descontaDPE"      property="blDescontaDpe" width="100" renderMode="image-check"/>
		<adsm:gridColumn title="exigeRNC"         property="blExigeRNC"  width="80"  renderMode="image-check"/>		
		<adsm:gridColumn title="apreensao"        property="blApreensao" width="80"  renderMode="image-check"/>		
		<adsm:gridColumn title="permissaoUnidade" property="tpPermissaoUnidade" width="120" isDomain="true" />
		<adsm:gridColumn title="permiteOcorrenciaManifesto" property="blPermiteOcorrenciaManifesto"  renderMode="image-check" width="240"/>
		<adsm:gridColumn title="situacao"         property="tpSituacao"  width="110" isDomain="true"/>
		<adsm:buttonBar >
		<adsm:removeButton/>
		</adsm:buttonBar>
		
	</adsm:grid>
</adsm:window>