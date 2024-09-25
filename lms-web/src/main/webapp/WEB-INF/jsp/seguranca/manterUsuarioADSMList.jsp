<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguranca.manterUsuarioADSMAction">
	<adsm:form action="/seguranca/manterUsuarioADSM" >
									
		<adsm:hidden property="criteriahModoConsultaNoJoin" value="sim" serializable="false" />

		<adsm:textbox dataType="text" property="nrMatricula" label="matricula" width="30%" labelWidth="140" size="16" maxLength="16" />

		<adsm:combobox property="blAtivo" label="ativo" boxWidth="100" domain="DM_SIM_NAO" onlyActiveValues="true" width="35%" labelWidth="170"/>
				
		<adsm:textbox property="login" size="15" maxLength="60" label="login" dataType="text" labelWidth="140" width="30%" />		
		
		<adsm:textbox dataType="text" property="nmUsuario" label="nome" width="35%" labelWidth="170" size="40" maxLength="60" />
				
		<adsm:combobox property="tpCategoriaUsuario" label="categoria" boxWidth="200" onlyActiveValues="true" domain="DM_CATEGORIA_USUARIO" width="30%" labelWidth="140"/>
		
		<adsm:combobox property="locale" label="idioma" domain="TP_LINGUAGEM" boxWidth="250" onlyActiveValues="true"  width="35%" labelWidth="170" />
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridUsuario" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid 
				property="gridUsuario" 
				idProperty="idUsuario" 				
				rows="12" 
				service="lms.seguranca.manterUsuarioADSMAction.findPaginatedUsuarioAdsm"
				rowCountService="lms.seguranca.manterUsuarioADSMAction.getRowCountUsuarioAdsm"
  	>
		<adsm:gridColumn property="dsMatricula" title="matricula" width="20%"/> 
		<adsm:gridColumn property="login" title="login" width="20%"/> 
		<adsm:gridColumn property="nmUsuario"   title="nome"   width="60%"/>
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>