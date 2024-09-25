<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>					  
<adsm:window service="lms.configuracoes.manterAbaAction" >
	<adsm:form action="/seguranca/manterAba" idProperty="idAba">
	
		<adsm:hidden serializable="false"
					 property="criteriaLookupTela" 
					 value="tela" 
		/>			 
							 
		<adsm:lookup dataType="text" label="nmTela"  serializable="true" 					
		     	 	 property="tela" idProperty="idRecurso" criteriaProperty="nmRecurso" 
	 				 action="/seguranca/manterMetodoTela" 
	 				 service="lms.configuracoes.manterAbaAction.findLookupTela"
			 	     width="100%"  
			 	     required="true"
			 	     exactMatch="false"
			 	     minLengthForAutoPopUpSearch="3" 
			 	     maxLength="200"
	    >
	    	
	    <adsm:propertyMapping modelProperty="tpMetodoTela" criteriaProperty="criteriaLookupTela" />
	    </adsm:lookup>				    

		<adsm:textbox dataType="text" property="recurso.nmRecurso" label="nome"     width="35%" size="20" maxLength="200" required="true"/>
		<adsm:textbox dataType="text" property="recurso.dsRecurso" label="descricao" width="20%" size="20" maxLength="60"  required="true"/> 
		<adsm:textbox dataType="text" property="cdRecurso" label="codigo" width="80%" size="40" maxLength="60" required="true"/>

		<adsm:buttonBar>
			<adsm:button caption="controles" action="/seguranca/manterControle"  boxWidth="65" cmd="main">
				<adsm:linkProperty src="tela.idRecurso" target="tela.idRecurso"/>
				<adsm:linkProperty src="tela.nmRecurso" target="tela.nmRecurso"/>
				<adsm:linkProperty src="idAba" target="aba.idAba"/>
				<adsm:linkProperty src="recurso.nmRecurso" target="aba.recurso_nmRecurso"/>
			</adsm:button>
			
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>