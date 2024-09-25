<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.conteudoParametroFilialService">
	<adsm:form action="/configuracoes/manterConteudosFiliais" idProperty="idConteudoParametroFilial">

		<adsm:hidden property="parametroFilial.idParametroFilial"/>
		<adsm:textbox dataType="text" property="parametroFilial.nmParametroFilial" label="nome" maxLength="20" size="40" disabled="true"/>
		<adsm:textbox dataType="text" property="parametroFilial.dsParametroFilial" label="descricao" maxLength="50" size="40" disabled="true"/>
		<adsm:lookup action="/municipios/manterFiliais" 
					 service="lms.municipios.filialService.findLookup" 
					 property="filial" 
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 dataType="text" 
					 label = "filial" 
					 size="3" 
					 maxLength="3" 
					 width="85%"
					 exactMatch="true"
					 required="false">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" formProperty="filial.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="50" maxLength="50" disabled="true" serializable="false"/>			
		</adsm:lookup>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="conteudoParametroFilial"/>
			<adsm:resetButton/>
		</adsm:buttonBar>

	</adsm:form>
	<adsm:grid idProperty="idConteudoParametroFilial" property="conteudoParametroFilial" defaultOrder="filial_.sgFilial" rows="13">	

        <adsm:gridColumnGroup separatorType="FILIAL">
              <adsm:gridColumn title="filial" property="filial.sgFilial" width="50" />
              <adsm:gridColumn title="" property="filial.pessoa.nmFantasia" width="200" />
            
        </adsm:gridColumnGroup>
        <adsm:gridColumn width="50%" title="valor" property="vlConteudoParametroFilial"/>
		<adsm:gridColumn title="nome" property="parametroFilial.nmParametroFilial" width="20%" />
        
		<adsm:buttonBar> 
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>	
</adsm:window>