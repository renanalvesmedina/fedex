<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.integracao.manterVersaoPontoLayoutBinderAction">
	<adsm:form action="/integracao/manterVersaoPontoLayoutBinder"> 		
		
		<adsm:hidden property="parametroFilial.nmParametroFilial" value="IMP_INT_%" />		
		
		<adsm:lookup dataType="text" 
					 label="pontoLayoutBinder"  
					 serializable="true" 					
		   	 	 	 property="pontoLayoutBinder" 
		   	 	 	 idProperty="id" 
		   	 	 	 criteriaProperty="pontoBinder.nome" 
	 			 	 action="/integracao/manterPontoLayoutBinder" 
	 			 	 service="lms.integracao.manterVersaoPontoLayoutBinderAction.findLookupPontoLayoutBinder"
			         required="false"
			         exactMatch="false"
			         minLengthForAutoPopUpSearch="3" 
			         maxLength="100"	
			         width="19%"    
			         
	    >	    	
	    	<adsm:propertyMapping relatedProperty="layoutOrGroup" modelProperty="layoutOrGroup"/>
	    	<adsm:propertyMapping relatedProperty="nmLayoutFormated" modelProperty="nmLayoutFormated"/>
	    	
	    	<adsm:textbox 
 				  dataType="text" 
 				  property="layoutOrGroup" 
 				  serializable="false"	    				  
 				  disabled="true"
 				  size="40"
 				  width="20%"/>
 				  
	    	<adsm:textbox
	   			dataType="text"
	   			property="nmLayoutFormated"
	   			size="10"
	   			serializable="true"
	   			disabled="true"
	   			width="20%"/>

	    </adsm:lookup>
	      
	    
	    <adsm:lookup dataType="text" 
					 label="conteudoParametroFilialInicio"  
					 serializable="true" 					
		   	 	 	 property="parametroFilialInicio" 
		   	 	 	 idProperty="idParametroFilial" 
		   	 	 	 criteriaProperty="nmParametroFilial"
	 			 	 action="/configuracoes/manterParametrosFiliais" 
	 			 	 service="lms.integracao.manterVersaoPontoLayoutBinderAction.findLookupParametroFilial"		     	   
			         required="false"
			         exactMatch="false"
			         minLengthForAutoPopUpSearch="3" 
			         maxLength="200"
			         width="59%"
	    >
	    	<adsm:propertyMapping  modelProperty="nmParametroFilial" 
	    						   criteriaProperty="parametroFilial.nmParametroFilial" />
	    </adsm:lookup>
	    
	    <adsm:lookup dataType="text" 
					 label="conteudoParametroFilialFim"  
					 serializable="true" 					
		   	 	 	 property="parametroFilialFim" 
		   	 	 	 idProperty="idParametroFilial" 
		   	 	 	 criteriaProperty="nmParametroFilial"
	 			 	 action="/configuracoes/manterParametrosFiliais" 
	 			 	 service="lms.integracao.manterVersaoPontoLayoutBinderAction.findLookupParametroFilial"			     	 
			         required="false"
			         exactMatch="false"
			         minLengthForAutoPopUpSearch="3" 
			         maxLength="200"
			         width="59%"  
	    >
	    	<adsm:propertyMapping  modelProperty="nmParametroFilial" 
	    						   criteriaProperty="parametroFilial.nmParametroFilial" />	   
	    </adsm:lookup>	
	
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="versaoPontoLayoutBinder"/>
			<adsm:resetButton />
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid idProperty="idVersaoPontoLayoutBinder"  property="versaoPontoLayoutBinder"  
			   rows="12" selectionMode="check" gridHeight="200" unique="true"
	>
			
		<adsm:gridColumn title="nomePontoBinder"         		  property="nomePontoBinder"       align="center" />
		<adsm:gridColumn title="nomeLayoutBinder"     			  property="nomeLayoutBinder"      align="center" />
		<adsm:gridColumn title="nomeGrupoLayoutBinder"     		  property="nomeGrupoLayoutBinder" align="center" />
		<adsm:gridColumn title="nomeVersaoIni"     		  		  property="nomeVersaoIni"		   align="center" />		
		<adsm:gridColumn title="nomeVersaoFim"     		  		  property="nomeVersaoFim"		   align="center" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar> 
	</adsm:grid>
</adsm:window>