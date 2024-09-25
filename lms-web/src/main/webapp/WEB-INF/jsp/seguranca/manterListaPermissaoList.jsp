<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguranca.manterListaPermissaoAction">
	<adsm:form action="/seguranca/manterListaPermissao">
		<adsm:textbox dataType="text" property="nmListaPermissao" width="55%" size="74" maxLength="60" label="descricao" />
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridPermissao" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idListaPermissao" property="gridPermissao" 
			   service="lms.seguranca.manterListaPermissaoAction.findPaginated"
			   rowCountService="lms.seguranca.manterListaPermissaoAction.getRowCount"
			   defaultOrder="nmListaPermissao"
			   rows="13">
		<adsm:gridColumn property="nmListaPermissao" title="descricao" />
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
