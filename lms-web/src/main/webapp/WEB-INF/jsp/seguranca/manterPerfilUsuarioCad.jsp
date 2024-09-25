<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguranca.manterPerfilUsuarioAction">
	<adsm:form action="/seguranca/manterPerfilUsuario" idProperty="idPerfilUsuario" >	
							
							
		<adsm:lookup dataType="text" label="usuario"
			     	 property="usuario" idProperty="idUsuario" criteriaProperty="nmUsuario"
	  			 	 exactMatch="false"
	  			 	 required="true"
	  			 	 maxLength="60"
	  			 	 minLengthForAutoPopUpSearch="3"
	  			 	 action="/seguranca/manterUsuario"	     
	 				 service="lms.seguranca.manterPerfilUsuarioAction.findLookupUsuario"  			 	 
				     labelWidth="15%" width="100%">	    			
	    </adsm:lookup>


		<adsm:lookup dataType="text" label="perfil"
			     	 property="perfil" idProperty="idPerfil" criteriaProperty="dsPerfil"
	  			 	 exactMatch="false"
	  			 	 maxLength="60"
	  			 	 required="true"
	  			 	 minLengthForAutoPopUpSearch="3"
	  			 	 action="/seguranca/manterPerfil"	     
	 				 service="lms.seguranca.manterPerfilUsuarioAction.findLookupPerfil"	  			 	 
				     labelWidth="15%" width="20%">
	    </adsm:lookup>
	    
	    
     	<adsm:buttonBar>
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton />
			
		</adsm:buttonBar>
		
		
	    
		</adsm:form>
		
</adsm:window>


