<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguranca.manterPerfilAction">
	<adsm:form action="/seguranca/manterPerfil" idProperty="idPerfil">
		<adsm:textbox dataType="text" property="dsPerfil" label="descricaoPerfil" maxLength="60"/>		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridPerfil" />
				<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idPerfil" property="gridPerfil" 
				rows="13"
				defaultOrder="dsPerfil"
			   service="lms.seguranca.manterPerfilAction.findPaginatedExplained"
			   rowCountService="lms.seguranca.manterPerfilAction.getRowCount">
		<adsm:gridColumn property="dsPerfil" title="descricaoPerfil" />
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>