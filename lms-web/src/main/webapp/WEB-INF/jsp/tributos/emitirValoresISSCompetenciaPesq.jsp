<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window onPageLoadCallBack="myPageLoadCallBack" > 
	<adsm:form action="/tributos/emitirValoresISSCompetencia">
		<adsm:range label="competencia" width="85%" required="true">
			<adsm:textbox dataType="monthYear" property="competenciaInicial" size="21"/>
			<adsm:textbox dataType="monthYear" property="competenciaFinal"/> 
		</adsm:range>
		<adsm:hidden property="siglaFilial" serializable="true"/>					
		<adsm:lookup property="filial"
					 idProperty="idFilial" 
					 label="filial" 
	                 criteriaProperty="sgFilial"
					 action="/municipios/manterFiliais" 
					 service="lms.tributos.emitirValoresISSCompetenciaAction.findLookupBySgFilial" 
					 dataType="text" 
					 size="3" 
					 maxLength="3" 
					 width="85%" >
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia"  formProperty="nomeFilial"/>
			<adsm:propertyMapping modelProperty="sgFilial" formProperty="siglaFilial"/>			
			<adsm:textbox dataType="text" property="nomeFilial" size="40" maxLength="50" disabled="true"/>

		</adsm:lookup>
  		<adsm:hidden property="nomeMunicipio" serializable="true"/>					
		<adsm:lookup property="municipio" 
               		 idProperty="idMunicipio"
		             criteriaProperty="nmMunicipio"
		             label="municipio" 
		             service="lms.tributos.emitirValoresISSCompetenciaAction.findLookupMunicipio"
		             dataType="text"
		             width="85%" 
		             size="35" 
		             maxLength="60"
		             action="/municipios/manterMunicipios"
		             exactMatch="false"
		             minLengthForAutoPopUpSearch="3"
		             labelWidth="15%" >
			<adsm:propertyMapping modelProperty="nmMunicipio" formProperty="nomeMunicipio"/>			               
	  	</adsm:lookup>  
		
		<adsm:checkbox property="soTotais" label="soTotais"/>
		
		<adsm:combobox property="tpFormatoRelatorio" label="formatoRelatorio" domain="DM_FORMATO_RELATORIO" width="85%" required="true"/>		
		
		<adsm:buttonBar>			 
			<adsm:reportViewerButton id="btVisualizar" caption="visualizar" service="lms.tributos.emitirValoresISSCompetenciaAction" />

				<adsm:button caption="limpar" onclick="limpar();" buttonType="resetButton" disabled="false"/>

		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
function limpar(){
	cleanButtonScript(document);
	setElementValue("tpFormatoRelatorio", "pdf");
}

function myPageLoadCallBack_cb(data, erro){
	onPageLoad_cb(data,erro);
	setElementValue("tpFormatoRelatorio", "pdf");
}


</script>