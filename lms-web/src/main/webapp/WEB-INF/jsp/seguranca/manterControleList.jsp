<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguranca.manterControleAction">
	<adsm:form action="/seguranca/manterControle">
		
		<adsm:hidden value="tela" property="criteriaLookUpTela" serializable="false" />

		<adsm:lookup dataType="text" label="tela"
					 exactMatch="false"
					 maxLength="200"
					 minLengthForAutoPopUpSearch="3"
					 property="tela" idProperty="idRecurso" criteriaProperty="nmRecurso"
	  			 	 action="/seguranca/manterMetodoTela"	     
	 				 service="lms.seguranca.manterControleAction.findLookupTela"	  			 	 
				     labelWidth="15%" width="35%"
	    >
	    	<adsm:propertyMapping modelProperty="tpMetodoTela" criteriaProperty="criteriaLookUpTela" />
	    </adsm:lookup>


		<adsm:lookup dataType="text" label="aba"
					 exactMatch="false"
					 minLengthForAutoPopUpSearch="3"					 
					 maxLength="200"
					 property="aba" idProperty="idAba" criteriaProperty="recurso_nmRecurso"
					 action="/seguranca/manterAba"
	 				 service="lms.seguranca.manterControleAction.findLookupAba"
					 labelWidth="10%" width="40%" 
		>									 					 					 
  			 <adsm:propertyMapping criteriaProperty="tela.idRecurso" modelProperty="tela.idRecurso" />
 			 <adsm:propertyMapping criteriaProperty="tela.nmRecurso" modelProperty="tela.nmRecurso" />
 			 <adsm:propertyMapping relatedProperty="tela.idRecurso"  modelProperty="tela_recurso_idRecurso" blankFill="false" />
  			 <adsm:propertyMapping relatedProperty="tela.nmRecurso"  modelProperty="tela_recurso_nmRecurso" blankFill="false" />
  			 
		</adsm:lookup>

		<adsm:textbox dataType="text" property="nmRecurso" label="nome" width="80%" size="60" maxLength="200"/>
		<adsm:textbox dataType="text" property="dsRecurso" label="descricao" width="80%" size="40" maxLength="60"   />
		
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridControle" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid idProperty="idRecurso" property="gridControle" 
			   service="lms.seguranca.manterControleAction.findPaginatedRecursoControle"
			   rowCountService="lms.seguranca.manterControleAction.getRowCountRecursoControle"
			   rows="12">
			   
		<adsm:gridColumn property="nmRecurso" title="nome" />
		<adsm:gridColumn property="dsRecurso" title="descricao" />
		<adsm:gridColumn property="nmAba" title="aba" />
		<adsm:gridColumn property="nmTela" title="tela" />

		<adsm:buttonBar>
			<adsm:removeButton service="lms.seguranca.manterControleAction.removeByIdsRecursoControle"/>
		</adsm:buttonBar>
	</adsm:grid>
	
</adsm:window>

