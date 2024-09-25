<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguranca.consultarUsuarioLMSAction">
	<adsm:form action="/seguranca/consultarUsuarioLMS">
		
		<adsm:textbox label="matricula" dataType="text" property="nrMatricula" maxLength="16" size="16"/>
		
		<adsm:textbox label="login" dataType="text" property="login" maxLength="60" size="16"/>
		
		<adsm:textbox label="nome" dataType="text" property="nmUsuario" maxLength="60" size="48"/>
		
		<adsm:combobox property="tpCategoriaUsuario" label="categoria"
			domain="DM_CATEGORIA_USUARIO" boxWidth="250"/>		
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridUsuario" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idUsuario" property="gridUsuario" 
			   rows="13"
			   service="lms.seguranca.consultarUsuarioLMSAction.findPaginated"
			   rowCountService="lms.seguranca.consultarUsuarioLMSAction.getRowCount"
	>
		<adsm:gridColumn property="nrMatricula" title="matricula" width="20%"/> 
		<adsm:gridColumn property="login" title="loginUsuarioADSM" width="20%"/> 
		<adsm:gridColumn property="nmUsuario"   title="nome" width="40%"/>
		<adsm:gridColumn property="blAdminCliente" renderMode="image-check"  title="administradorDoCliente" width="20%"/>
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>