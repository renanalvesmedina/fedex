<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguranca.manterPerfilUsuarioAction">
	<adsm:form action="/seguranca/manterPerfilUsuario">	
	
		<adsm:hidden property="hidUsuarioADSM" serializable="true" />			
		
		<adsm:lookup size="16" maxLength="60" width="20%" labelWidth="100"
		 	  property="loginUsuario" 
			  idProperty="idLogin"
			  criteriaProperty="login"
			  action="/seguranca/manterUsuarioADSM" 
			  service="lms.seguranca.manterPerfilUsuarioAction.findLookupUsuarioAdsm"  			                         
			  dataType="text"
			  exactMatch="false"
			  minLengthForAutoPopUpSearch="3"
			  label="usuario"
		>				
			<adsm:propertyMapping relatedProperty="hidUsuarioADSM" modelProperty="idUsuario" />			
		</adsm:lookup>

		<adsm:lookup dataType="text" label="perfil"
			     	 property="perfil" idProperty="idPerfil" criteriaProperty="dsPerfil"
	  			 	 exactMatch="false"
	  			 	 maxLength="60"
	  			 	 minLengthForAutoPopUpSearch="3"
	  			 	 action="/seguranca/manterPerfil"	     
	 				 service="lms.seguranca.manterPerfilUsuarioAction.findLookupPerfil"	  			 	 
				     labelWidth="15%" width="20%">
	    </adsm:lookup>
	    
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="perfilUsuario" />
			<adsm:resetButton />
		</adsm:buttonBar>
		
		</adsm:form>
		
		<adsm:grid idProperty="idPerfilUsuario" property="perfilUsuario"
				   service="lms.seguranca.manterPerfilUsuarioAction.findPaginated"
				   rowCountService="lms.seguranca.manterPerfilUsuarioAction.getRowCount"
				   rows="12">
			<adsm:gridColumn property="dsPerfil"  title="perfil" width="20%"/> 
			<adsm:gridColumn property="nmUsuario" title="usuario" width="80%"/>					
			
			<adsm:buttonBar>
				<adsm:removeButton/>
			</adsm:buttonBar>
			
		</adsm:grid>
</adsm:window>