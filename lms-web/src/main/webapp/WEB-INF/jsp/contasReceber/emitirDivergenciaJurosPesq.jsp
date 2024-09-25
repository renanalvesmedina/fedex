<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window onPageLoadCallBack="myPageLoadCallBack">

	<adsm:form action="/contasReceber/emitirDivergenciaJuros" >
	
		<adsm:textbox property="competencia" 
					  dataType="monthYear" 
					  label="competencia" 
					  labelWidth="15%" required="true"
					  width="25%"/>
	
		<adsm:hidden property="sgFilial" serializable="true"/>
		
		<adsm:lookup action="/municipios/manterFiliais" 
					 service="lms.contasreceber.emitirDivergenciaJurosAction.findLookupFilial" 
					 dataType="text" 
					 property="filial" 
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 labelWidth="15%"
					 label = "filialCobranca" 
					 size="3" 
					 maxLength="3" 
					 width="45%"
					 exactMatch="true">
					 
			<adsm:propertyMapping relatedProperty="nmFilial" modelProperty="pessoa.nmFantasia"/>
			
			<adsm:propertyMapping relatedProperty="sgFilial" modelProperty="sgFilial"/>
			
			<adsm:textbox dataType="text" property="nmFilial" size="50" maxLength="50" disabled="true" serializable="true"/>
			
		</adsm:lookup>
		
		<adsm:hidden property="sgDsRegional" serializable="true"/>
		
		 <adsm:combobox label="regional" 
		 				property="regional.idRegional" 
              			optionLabelProperty="siglaDescricao" optionProperty="idRegional" 
		                service="lms.municipios.regionalService.findRegionaisVigentes" 
		                labelWidth="15%" width="25%" boxWidth="170" required="false">
		                
		    <adsm:propertyMapping relatedProperty="sgDsRegional" modelProperty="siglaDescricao"/>
		               
		 </adsm:combobox>
        
        
        <adsm:combobox property="tpFormatoRelatorio" 
					   required="true"
					   label="formatoRelatorio" 
					   domain="DM_FORMATO_RELATORIO"
					   labelWidth="15%" width="45%"/>
		
		
		<adsm:checkbox property="soTotais" label="soTotais" labelWidth="15%" width="45%"/>
		
		<adsm:buttonBar>
		
			<adsm:reportViewerButton service="lms.contasreceber.emitirDivergenciaJurosAction" disabled="false"/>
			
			<adsm:button buttonType="resetButton" caption="limpar" onclick="limpar();" disabled="false"/>
			
		</adsm:buttonBar>
		
	</adsm:form>
	
</adsm:window>

<script>
function myPageLoadCallBack_cb(data, erro){
	onPageLoad_cb(data,erro);
	setElementValue("tpFormatoRelatorio", "pdf");
}

function limpar(){
	cleanButtonScript(document);
	setElementValue("tpFormatoRelatorio", "pdf");
}

</script>