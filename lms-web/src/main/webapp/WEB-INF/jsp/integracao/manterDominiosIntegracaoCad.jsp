<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.integracao.manterDominiosIntegracaoAction">
	<adsm:form action="/integracao/manterDominiosIntegracao" idProperty="idDominioNomeIntegracao">
		
		<adsm:textbox dataType="text" 
					  property="nmDominio" 
					  label="nome" 
					  size="50" 
					  maxLength="60"
					  required="true"
					  width="100%"/>
					  
		<adsm:textbox dataType="text"
					  property="dsDominio" 
					  label="descricao"
					  size="80"
					  maxLength="100"
					  required="true"
					  width="100%"/>
					   
		<adsm:combobox property="tpSituacao" 
					   label="situacao" 
					   domain="DM_SITUACAO"
					   required="true"/>
		
		<adsm:buttonBar>
			<adsm:button caption="valoresDominios" action="/integracao/manterValoresDominiosIntegracao" cmd="main">
				<adsm:linkProperty src="idDominioNomeIntegracao" target="dominioNomeIntegracao.idDominioNomeIntegracao"/>
				<adsm:linkProperty src="nmDominio" target="dominioNomeIntegracao.nmDominio"/>
			</adsm:button>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
	