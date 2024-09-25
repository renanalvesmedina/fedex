<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.conteudoParametroFilialService">
	<adsm:form action="/configuracoes/manterConteudosFiliais" idProperty="idConteudoParametroFilial">
		
		<adsm:hidden property="parametroFilial.idParametroFilial"/>
		<adsm:textbox dataType="text" property="parametroFilial.nmParametroFilial" label="nome" maxLength="20" size="40" disabled="true" serializable="false"/>
		<adsm:textbox dataType="text" property="parametroFilial.dsParametroFilial" label="descricao" maxLength="50" size="40" disabled="true" serializable="false"/>
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
					 required="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" formProperty="filial.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="50" maxLength="50" disabled="true" serializable="false"/>			
		</adsm:lookup>	

		<adsm:textbox dataType="text" property="vlConteudoParametroFilial" label="valor" maxLength="100" size="100" width="85%" required="true"/>
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
	/**
	* Função criada para desabilitar o campo filial no detalhamento
	*/
	function initWindow(eventObj){
	    
		if( eventObj.name == "gridRow_click" || eventObj.name == "storeButton" ){
			setDisabled("filial.idFilial",true);
		} else {
			setDisabled("filial.idFilial",false);
		}
		// passa o foco para o botão limpar após o storeButton, senão vai para o "FirstFocusableField"
        if( eventObj.name != "storeButton" ){
		   setFocusOnFirstFocusableField(document);
		}   
	}
</script>