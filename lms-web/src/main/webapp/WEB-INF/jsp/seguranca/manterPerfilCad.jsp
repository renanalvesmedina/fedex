<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguranca.manterPerfilAction" >
	<adsm:form action="/seguranca/manterPerfil" idProperty="idPerfil">
		<adsm:textbox dataType="text" property="dsPerfil" label="descricaoPerfil" required="true" maxLength="60"/>
		
		<adsm:listbox 	label="perfilHerdado" 
						size="4" 
						boxWidth="170" 
						width="85%" 
						property="perfilPai" 												
						optionProperty="idPerfilHerdado"
						optionLabelProperty="dsPerfil"
						>
						
				<adsm:lookup property="perfil" 
							 idProperty="idPerfil"
							 criteriaProperty="dsPerfil"
							 action="/seguranca/manterPerfil"
							 service="lms.seguranca.manterPerfilAction.findLookup" 
							 dataType="text"
							 exactMatch="false"
							 size="30" maxLength="60">
				 </adsm:lookup>
		</adsm:listbox>		
		<adsm:buttonBar>
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>