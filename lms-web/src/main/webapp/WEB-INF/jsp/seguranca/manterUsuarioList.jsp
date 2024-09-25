<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguranca.manterUsuarioAction">
	<adsm:form action="/seguranca/manterUsuario" idProperty="idUsuario">
		
		<adsm:lookup size="40" maxLength="40" width="80%" labelWidth="125"
					 idProperty="chapa"
					 property="usuario.funcionario" 
					 criteriaProperty="idChapa" 
					 action="/configuracoes/consultarFuncionarios" 
					 service="lms.configuracoes.RHFuncionarioService.findLookupRHFuncionarios" 
					 dataType="integer"
					 exactMatch="false"
					 minLengthForAutoPopUpSearch="3"
					 label="usuario" 
					 required="true">					 
		</adsm:lookup> 
		
		   
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridUsuario" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idUsuario" property="gridUsuario" 
				rows="13"
				defaultOrder="nrMatricula,nmUsuario"
			   service="lms.seguranca.manterUsuarioAction.findPaginated"
			   rowCountService="lms.seguranca.manterUsuarioAction.getRowCount">
		<adsm:gridColumn property="nrMatricula" title="matricula" width="20%"/> 
		<adsm:gridColumn property="nmUsuario" title="usuario" width="80%"/>
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>