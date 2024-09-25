<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterTabelasRefereniasFrteKm" service="lms.fretecarreteiroviagem.manterTabelasReferenciasFreteKmAction">
	<adsm:form action="entrega/fecharManifestoEntregas" idProperty="idReferenciaFreteCarreteiro">
		
		<adsm:lookup 
		dataType="text" 
		property="unidadeFederativaByIdUnidadeFederativaOrigem" 
		idProperty="idUnidadeFederativa" 
		criteriaProperty="sgUnidadeFederativa" 
		service="lms.fretecarreteiroviagem.manterTabelasReferenciasFreteKmAction.findLookupUnidadeFederativa" 
		label="ufOrigem" width="35%" size="3" action="/municipios/manterUnidadesFederativas" maxLength="3">
		 	<adsm:propertyMapping relatedProperty="unidadeFederativaByIdUnidadeFederativaOrigem.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa"/>
		 	<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.idFilial" modelProperty=""/>
		    <adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.sgFilial" modelProperty=""/>
		    <adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty=""/>
		    
		 	<adsm:textbox dataType="text" property="unidadeFederativaByIdUnidadeFederativaOrigem.nmUnidadeFederativa" disabled="true" size="30"/>
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="unidadeFederativaByIdUnidadeDestino" idProperty="idUnidadeFederativa" criteriaProperty="sgUnidadeFederativa" service="lms.fretecarreteiroviagem.manterTabelasReferenciasFreteKmAction.findLookupUnidadeFederativa" label="ufDestino" width="35%" size="3" action="/municipios/manterUnidadesFederativas" maxLength="3">
			<adsm:propertyMapping relatedProperty="unidadeFederativaByIdUnidadeDestino.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa"/>
			<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.idFilial" modelProperty=""/>
		    <adsm:propertyMapping relatedProperty="filialByIdFilialDestino.sgFilial" modelProperty=""/>
		    <adsm:propertyMapping relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia" modelProperty=""/>
		    
		 	<adsm:textbox dataType="text" property="unidadeFederativaByIdUnidadeDestino.nmUnidadeFederativa" disabled="true" size="30"/>
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="filialByIdFilialOrigem" idProperty="idFilial" criteriaProperty="sgFilial" service="lms.fretecarreteiroviagem.manterTabelasReferenciasFreteKmAction.findLookupFilial" label="filialOrigem" width="35%" size="3" exactMatch="false" minLengthForAutoPopUpSearch="3" action="/municipios/manterFiliais" maxLength="3">
			<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
		 	<adsm:textbox dataType="text" property="filialByIdFilialOrigem.pessoa.nmFantasia" disabled="true" size="30"/>
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="filialByIdFilialDestino" idProperty="idFilial" criteriaProperty="sgFilial" service="lms.fretecarreteiroviagem.manterTabelasReferenciasFreteKmAction.findLookupFilial" label="filialDestino" width="35%" size="3" exactMatch="false" minLengthForAutoPopUpSearch="3" action="/municipios/manterFiliais" maxLength="3">
			<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
		 	<adsm:textbox dataType="text" property="filialByIdFilialDestino.pessoa.nmFantasia" disabled="true" size="30"/>
		</adsm:lookup>
	
			
		<adsm:range label="vigencia" width="85%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"/>
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>
        
        
		<adsm:buttonBar freeLayout="true">
				<adsm:findButton callbackProperty="referenciaFreteCarreteiro"/> 
				<adsm:resetButton/> 
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid rows="11" property="referenciaFreteCarreteiro" idProperty="idReferenciaFreteCarreteiro" scrollBars="horizontal" gridHeight="220" unique="true" service="lms.fretecarreteiroviagem.manterTabelasReferenciasFreteKmAction.findPaginatedReferenciaFreteCarreteiro" rowCountService="lms.fretecarreteiroviagem.manterTabelasReferenciasFreteKmAction.getRowCountReferenciaFreteCarreteiro" defaultOrder="unidadeFederativaByIdUnidadeFederativaOrigem_.sgUnidadeFederativa,filialByIdFilialOrigem_.sgFilial,unidadeFederativaByIdUnidadeDestino_.sgUnidadeFederativa,filialByIdFilialDestino_.sgFilial,dtVigenciaFinal">
		<adsm:gridColumn width="150" title="ufOrigem" property="sgUnidadeFederativaOrigem"/>
		<adsm:gridColumn width="150" title="ufDestino" property="sgUnidadeFederativaDestino"/>
		<adsm:gridColumnGroup separatorType="FILIAL">
			<adsm:gridColumn width="90" title="filialOrigem" property="sgFilialOrigem"/>		
			<adsm:gridColumn width="90" title="" property="nomeFilialOrigem"/>		
		</adsm:gridColumnGroup>		
		<adsm:gridColumnGroup separatorType="FILIAL">
			<adsm:gridColumn width="90" title="filialDestino" property="sgFilialDestino"/>
			<adsm:gridColumn width="90" title="" property="nomeFilialDestino"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn width="100" title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate"/>
		<adsm:gridColumn width="100" title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate"/>
		
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>
<script>
function initWindow(eventObj){
	if(eventObj.name=='tab_click'){
		var tabGroup = getTabGroup(this.document);	
		var tabCheq = tabGroup.getTab('cheq');
		tabCheq.tabOwnerFrame.referenciaTipoVeiculoGridDef.resetGrid();
		tabGroup.setDisabledTab('cheq',true);
	}
}
</script>
