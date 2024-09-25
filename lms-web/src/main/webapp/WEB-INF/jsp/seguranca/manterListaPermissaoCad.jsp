<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguranca.manterListaPermissaoAction" >
	<adsm:form action="/seguranca/manterListaPermissao" idProperty="idListaPermissao">
		<adsm:textbox dataType="text" property="nmListaPermissao" width="55%" size="74" maxLength="60" label="descricao" required="true"/>
		<adsm:buttonBar>
			<adsm:button caption="permissoes" action="/seguranca/manterPermissao" cmd="main">
				<adsm:linkProperty src="idListaPermissao" target="listaPermissao.idListaPermissao" />
				<adsm:linkProperty src="nmListaPermissao" target="listaPermissao.nmListaPermissao"/>
		</adsm:button>
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

