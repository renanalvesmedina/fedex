<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.vol.manterGruposFrotasAction" >
	<adsm:form  action="/vol/manterGruposFrotas">
	    <adsm:hidden property="ids"/>
	    <adsm:lookup 
	        service="lms.vol.manterGruposFrotasAction.findLookupUsuarioFuncionario" 
	        property="usuario"
			idProperty="idUsuario"
			criteriaProperty="nrMatricula"
			label="usuario" 
			size="10" 
			maxLength="10" 
			dataType="text"
			required="true"
			action="/configuracoes/consultarFuncionariosView">

<%--
			<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial" />
			<adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial" />
			<adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" />

			<adsm:propertyMapping relatedProperty="usuario.idUsuario" modelProperty="idUsuario"/>
--%>
			<adsm:propertyMapping relatedProperty="usuario.codPessoa.nome" modelProperty="nmUsuario"/>
			<adsm:textbox dataType="text" property="usuario.codPessoa.nome" size="25" disabled="true"/>
		</adsm:lookup>
	    
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="confirmar" id="btnAssociar" onclick="retornaUsuario();" disabled="false"/>
			<adsm:button caption="fechar" id="btnFechar" disabled="false" onclick="self.close();" buttonType="closeButton" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
    function retornaUsuario() {
       if (validateTabScript(document.forms)) {
          window.returnValue = getElementValue("usuario.idUsuario");
          window.close();
       }
    }
    
    
</script>