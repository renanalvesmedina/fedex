<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.pendencia.unidadeProdutoService" >
	<adsm:form action="/pendencia/manterUnidadesProdutos" idProperty="idUnidadeProduto" >
		<adsm:textbox dataType="text" property="dsUnidadeProduto" label="descricao" size="30" maxLength="30" width="85%" required="true" />
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="85%" required="true" renderOptions="true"/>
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>