<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.integracao.manterDominiosIntegracaoAction">
	<adsm:form action="/integracao/manterDominiosIntegracao" idProperty="idDominioNomeIntegracao">
	
		<adsm:textbox dataType="text" 
					  property="nmDominio" 
					  label="nome" 
					  size="50" 
					  maxLength="60"
					  width="100%"/>
					  
		<adsm:textbox dataType="text"
					  property="dsDominio" 
					  label="descricao"
					  size="80"
					  maxLength="100"
					  width="100%"/>
					   
		<adsm:combobox property="tpSituacao" 
					   label="situacao" 
					   domain="DM_SITUACAO"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="dominioNomeIntegracao"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid property="dominioNomeIntegracao" defaultOrder="nmDominio" rows="10" idProperty="idDominioNomeIntegracao" >
		<adsm:gridColumn width="25%" title="nome" property="nmDominio"/>
		<adsm:gridColumn width="60%" title="descricao" property="dsDominio" />
		<adsm:gridColumn width="15%" title="situacao" property="tpSituacao" isDomain="true" />
  		
  		<adsm:buttonBar> 
	   		<adsm:removeButton/>
	   	</adsm:buttonBar>	
	   	
	</adsm:grid>	
	
</adsm:window>