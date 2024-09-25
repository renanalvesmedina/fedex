<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.configuracoes.manterAbaAction">
	<adsm:form action="/seguranca/manterAba">

		<adsm:hidden serializable="false"
					 property="criteriaLookupTela" 
					 value="tela" 
		/>			 	 
							 
		<adsm:lookup dataType="text" label="nmTela"  
		     	 	 property="tela" idProperty="idRecurso" criteriaProperty="nmRecurso" 
	 				 action="/seguranca/manterMetodoTela" 
	 				 service="lms.configuracoes.manterAbaAction.findLookupTela"
			 	     width="100%"  		
			 	     exactMatch="false"
			 	     minLengthForAutoPopUpSearch="3" 
			 	     maxLength="200"	 				 	          
	    >				
	    	<adsm:propertyMapping modelProperty="tpMetodoTela" criteriaProperty="criteriaLookupTela" />	    	
	    </adsm:lookup>				    
	    
		<adsm:textbox dataType="text" property="recurso_nmRecurso" label="nome" width="35%" size="20"     maxLength="200"/>
		<adsm:textbox dataType="text" property="recurso_dsRecurso" label="descricao" width="20%" size="20" maxLength="60"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="aba"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idAba" property="aba" 
			   gridHeight="200" rows="12"  
			   service="lms.configuracoes.manterAbaAction.findPagineted"	
			   rowCountService="lms.configuracoes.manterAbaAction.findPaginetedRowCount" 			   
	 >
		<adsm:gridColumn title="nome" 	 property="recurso_nmRecurso"  		 width="20%"/>
		<adsm:gridColumn title="descricao" property="recurso_dsRecurso" 	 	 width="20%"/>
		<adsm:gridColumn title="nmTela"  property="tela_recurso_nmRecurso"	 width="60%"/>
		
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>