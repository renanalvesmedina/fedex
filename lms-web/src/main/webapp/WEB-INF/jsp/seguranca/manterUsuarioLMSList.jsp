<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguranca.manterUsuarioLMSAction">
	<adsm:form action="/seguranca/manterUsuarioLMS">
		
		<adsm:lookup size="16" maxLength="60" width="45%" labelWidth="180"
					 property="usuario" 
					 idProperty="idUsuario"
					 criteriaProperty="login"
					 action="/seguranca/manterUsuarioADSM" 
					 service="lms.seguranca.manterUsuarioLMSAction.findLookupUsuarioAdsm" 
					 dataType="text"
					 exactMatch="false"
					 minLengthForAutoPopUpSearch="3"
					 label="loginUsuarioADSM"
		>
		<adsm:propertyMapping relatedProperty="usuario.nmUsuario" modelProperty="nmUsuario" />
		<adsm:textbox property="usuario.nmUsuario" size="30"  dataType="text" disabled="true" />
		</adsm:lookup>
		
		<adsm:combobox property="usuario.blAdminCliente" label="administradorDoCliente" domain="DM_SIM_NAO" labelWidth="180" width="10%"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridUsuario" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idUsuario" property="gridUsuario" 
			   rows="13"
			   service="lms.seguranca.manterUsuarioLMSAction.findPaginatedADSM"
			   rowCountService="lms.seguranca.manterUsuarioLMSAction.getRowCountADSM"
	>
		<adsm:gridColumn property="idMatricula" title="matricula" width="20%"/> 
		<adsm:gridColumn property="login" title="loginUsuarioADSM" width="20%"/> 
		<adsm:gridColumn property="nmUsuario"   title="nome" width="40%"/>
		<adsm:gridColumn property="blAdminCliente" renderMode="image-check"  title="administradorDoCliente" width="20%"/>
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>