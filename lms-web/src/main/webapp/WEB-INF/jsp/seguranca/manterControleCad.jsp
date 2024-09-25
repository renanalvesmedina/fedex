<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguranca.manterControleAction">
	<adsm:form action="/seguranca/manterControle" idProperty="idRecurso">


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
					 maxLength="200"
					 required="true"
					 minLengthForAutoPopUpSearch="3"
					 exactMatch="false"
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

		<adsm:textbox dataType="text" property="nmRecurso" label="nome" width="80%" size="60" maxLength="200" required="true"/>
		<adsm:textbox dataType="text" property="dsRecurso" label="descricao" width="80%" size="40" maxLength="60" required="true"/> 
		<adsm:textbox dataType="text" property="cdRecurso" label="codigo" width="80%" size="40" maxLength="60" required="true"/>


		<adsm:buttonBar>
			
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton service="lms.seguranca.manterControleAction.removeByIdRecursoControle"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>