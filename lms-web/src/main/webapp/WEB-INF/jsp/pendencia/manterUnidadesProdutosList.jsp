<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.pendencia.unidadeProdutoService" >
	<adsm:form action="/pendencia/manterUnidadesProdutos" idProperty="idUnidadeProduto" >
		<adsm:textbox dataType="text" property="dsUnidadeProduto" label="descricao" size="30" maxLength="30" width="85%" />
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="85%" renderOptions="true" />
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="unidades"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="unidades" idProperty="idUnidadeProduto" defaultOrder="dsUnidadeProduto:asc" selectionMode="check" gridHeight="200" unique="true" rows="13">
		<adsm:gridColumn property="dsUnidadeProduto" title="descricao" width="90%" />
		<adsm:gridColumn property="tpSituacao" title="situacao" isDomain="true" width="10%" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
