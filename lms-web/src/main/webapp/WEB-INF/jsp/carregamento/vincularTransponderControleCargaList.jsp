<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.carregamento.vincularTransponderControleCargaAction" >
	<adsm:form action="/carregamento/vincularTransponderControleCarga"  idProperty="idTransponder" >


		<adsm:lookup dataType="text" 
				property="controleCarga.filialByIdFilialOrigem" idProperty="idFilial" criteriaProperty="sgFilial" 
				service="lms.portaria.manterQuilometragensSaidaChegadaAction.findLookupFilial" popupLabel="pesquisarFilial"
				label="controleCarga" size="3" maxLength="3" width="85%" labelWidth="15%" picker="false" serializable="false"
				action="/municipios/manterFiliais" onchange="return onFilialControleCargaChange(this);" >
				
			<adsm:lookup dataType="integer"
					property="controleCarga" idProperty="idControleCarga" criteriaProperty="nrControleCarga"
					service="lms.portaria.manterQuilometragensSaidaChegadaAction.findLookupControleCarga"
					action="carregamento/manterControleCargas" size="8" mask="00000000" disabled="true" popupLabel="pesquisarControleCarga" >
				<adsm:propertyMapping criteriaProperty="controleCarga.filialByIdFilialOrigem.idFilial" 
						modelProperty="filialByIdFilialOrigem.idFilial" />
				<adsm:propertyMapping criteriaProperty="controleCarga.filialByIdFilialOrigem.sgFilial" 
						modelProperty="filialByIdFilialOrigem.sgFilial" />
	        </adsm:lookup>
	     </adsm:lookup>
		
		
		<adsm:lookup label="transponder" property="transponder" 
        	service="lms.carregamento.manterTranspondersAction.findLookup" 
        	action="/carregamento/manterTransponders" 
        	idProperty="idTransponder" 
        	criteriaProperty="nrTransponder" dataType="text" size="10" 
        	labelWidth="15%"
			width="85%"
			maxLength="10" />
			
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="transponderGrid"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid property="transponderGrid" 
				idProperty="idTransponder"  
				gridHeight="200" 
				unique="true" 
				rows="11" >
				
				
		<adsm:gridColumn property="nrTransponder" dataType="text" title="transponder" width="20%" />
		<adsm:gridColumnGroup separatorType="FILIAL">
			<adsm:gridColumn property="filial.sgFilial" dataType="text" width="50" title="filial"/>
			<adsm:gridColumn property="filial.pessoa.nmFantasia" dataType="text" width="100" title=""/>
		</adsm:gridColumnGroup>

	    <adsm:gridColumnGroup separatorType="CONTROLE_CARGA">
			<adsm:gridColumn property="controleCarga.filialByIdFilialOrigem.sgFilial" title="controleCargas" width="50" />
		    <adsm:gridColumn property="controleCarga.nrControleCarga" title="" width="100"  dataType="integer" mask="00000000"/>
		</adsm:gridColumnGroup>		
		
		<adsm:gridColumn property="tpSituacaoTransponder" isDomain="true" title="situacaoTransponder"  />

		<adsm:buttonBar>
			<adsm:removeButton /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>
function onFilialControleCargaChange(elem) {
	setDisabled("controleCarga.idControleCarga",elem.value == "");
	return controleCarga_filialByIdFilialOrigem_sgFilialOnChangeHandler();
}
</script>