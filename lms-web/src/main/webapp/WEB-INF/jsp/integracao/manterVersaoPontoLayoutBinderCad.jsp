<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.integracao.manterVersaoPontoLayoutBinderAction">
	<adsm:form action="/integracao/manterVersaoPontoLayoutBinder" idProperty="idVersaoPontoLayoutBinder" onDataLoadCallBack="renderCallBack" >		
							 
		<adsm:hidden property="parametroFilial.nmParametroFilial" value="IMP_INT_%" />
		<adsm:hidden property="grupoLayoutBinder.id" />				
		
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
	    		    
	    	<adsm:textbox dataType="text" 
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
			  
		<adsm:buttonBar freeLayout="false">			
			<adsm:storeButton  />
			<adsm:resetButton  />
			<adsm:removeButton />
		</adsm:buttonBar>		
		
	</adsm:form>	
</adsm:window>
<script type="text/javascript">

function renderCallBack_cb(data, errorMessage, errorCode, eventObj) {
	onDataLoad_cb( data, errorMessage, errorCode, eventObj);	
	setElementValue("idVersaoPontoLayoutBinder",getNestedBeanPropertyValue(data, "idVersaoPontoLayoutBinder"));	
	setElementValue("pontoLayoutBinder.id",getNestedBeanPropertyValue(data, "pontoLayoutBinder_id"));
	setElementValue("grupoLayoutBinder.id",getNestedBeanPropertyValue(data, "grupoLayoutBinder_id"));		
	setElementValue("pontoLayoutBinder.pontoBinder.nome",getNestedBeanPropertyValue(data, "pontoLayoutBinder_pontoBinder_nome"));	
	setElementValue("parametroFilialInicio.nmParametroFilial",getNestedBeanPropertyValue(data, "parametroFilialInicio_nmParametroFilial"));	
	setElementValue("parametroFilialFim.nmParametroFilial",getNestedBeanPropertyValue(data, "parametroFilialFim_nmParametroFilial"));
	setElementValue("parametroFilialInicio.idParametroFilial",getNestedBeanPropertyValue(data, "parametroFilialInicio_idParametroFilial"));	
	setElementValue("parametroFilialFim.idParametroFilial",getNestedBeanPropertyValue(data, "parametroFilialFim_idParametroFilial"));
}
</script>