<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.integracao.manterValoresDominiosIntegracaoAction">
	<adsm:form action="/integracao/manterValoresDominiosIntegracao" idProperty="idDominioVinculoIntegracao">
	
		<adsm:hidden property="dominioNomeIntegracao.idDominioNomeIntegracao"/>
		
		<adsm:textbox dataType="text" 
					  property="dominioNomeIntegracao.nmDominio" 
					  label="dominio" 
					  disabled="true" 
					  size="40" 
					  maxLength="60"/>
		
		<adsm:textbox dataType="text" 
					  property="dsValorLms" 
					  label="valorLms" 
					  size="20" 
					  maxLength="60"/>
					  
        <adsm:textbox dataType="text" 
        			  property="dsValorCorporativo" 
        			  label="valorCorporativo" 
        			  size="20" 
        			  maxLength="60"/>

        <adsm:textbox dataType="text" 
        			  property="dsValorClipper" 
        			  label="valorClipper" 
        			  size="20" 
        			  maxLength="60"/>
        			      			      	      
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="dominioVinculoIntegracao"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid property="dominioVinculoIntegracao" idProperty="idDominioVinculoIntegracao" defaultOrder="dsValorLms" rows="10">
        <adsm:gridColumn width="15%" title="valorLms" property="dsValorLms" dataType="text" />
        <adsm:gridColumn width="15%" title="valorCorporativo" property="dsValorCorporativo" dataType="text"/>
        <adsm:gridColumn width="15%" title="valorClipper" property="dsValorClipper"  dataType="text"/>
        <adsm:gridColumn width="11%" title="padraoCorporativo" property="blLmsCorporativo"  renderMode="image-check"/>
        <adsm:gridColumn width="11%" title="padraoClipper" property="blLmsClipper"  renderMode="image-check"/>
        <adsm:gridColumn width="33%" title="significado" property="dsSignificadoIntegracao"  dataType="text"/>
                
     	<adsm:buttonBar> 
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
	
</adsm:window>
