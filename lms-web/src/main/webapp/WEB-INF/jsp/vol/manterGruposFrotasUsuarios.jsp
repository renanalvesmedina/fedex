<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.vol.manterGruposFrotasAction" >
	<adsm:form  action="/vol/manterGruposFrotas" idProperty="idGruFunc" service="lms.vol.manterGruposFrotasAction.findByIdUsuario">
		<adsm:masterLink showSaveAll="true" idProperty="idGrupoFrota" >
			<adsm:masterLinkItem property="dsNome"	label="grupo" />
		</adsm:masterLink>
	
	<%-- 
		<adsm:lookup 
		     service="lms.vol.manterGruposFrotasAction.findLookupUsuarioAdsm" 
		     dataType="text" 
			 property="usuario" 
			 idProperty="idUsuario"
			 criteriaProperty="login" 
			 label="usuario" 
			 size="10" 
			 maxLength="10"
			 action="/seguranca/manterUsuarioADSM" 
			 width="100%" 
			 exactMatch="true"/>
	--%>
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
			<adsm:storeButton caption="salvarUsuario"  service="lms.vol.manterGruposFrotasAction.storeUsuarios"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>

	<adsm:grid property="usuariosVol" idProperty="idGruFunc" selectionMode="check" 
			   rows="11" gridHeight="150" onRowClick="disableClick"
			   service="lms.vol.manterGruposFrotasAction.findPaginatedGruposFrotaUsuario"
			   rowCountService="lms.vol.manterGruposFrotasAction.getRowCountGruposFrotaUsuario"
			   unique="true"
			   autoSearch="false" showGotoBox="true" showPagging="true" detailFrameName="usuarios">
<%--
		<adsm:gridColumn property="usuario.login" title="login" width="60" />
	--%>	
		<adsm:gridColumn property="usuario.nrMatricula" title="matricula" width="100" />
		<adsm:gridColumn property="usuario.nmUsuario" title="nome" width="300" />
		<adsm:gridColumn property="funcionario.dsFuncao" title="funcao"  />
			
		<adsm:buttonBar> 
			<adsm:removeButton service="lms.vol.manterGruposFrotasAction.removeByIdsGruposFrotaUsuario"/>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>

<script>
	
	function disableClick() {
		return false;
	}
</script>