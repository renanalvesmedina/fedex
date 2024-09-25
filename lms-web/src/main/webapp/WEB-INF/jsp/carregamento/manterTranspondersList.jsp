<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.carregamento.manterTranspondersAction">
	<adsm:form action="/carregamento/manterTransponders" idProperty="idTransponder" >

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
		
		<adsm:textbox label="transponder" property="nrTransponder" dataType="integer" maxLength="10" size="20" width="85%"/>

		<adsm:combobox label="situacaoTransponder" property="tpSituacaoTransponder" domain="DM_SITUACAO_TRANSPONDER" width="85%" renderOptions="true"/>		
		
		<adsm:lookup label="filial" property="filial" 
        	service="lms.municipios.filialService.findLookupFilial" 
        	action="/municipios/manterFiliais" 
        	idProperty="idFilial" 
        	criteriaProperty="sgFilial" dataType="text" size="5" 
        	labelWidth="15%" 
			width="85%" 
			maxLength="3" >
			<adsm:propertyMapping
            	relatedProperty="filial.pessoa.nmFantasia"
            	modelProperty="pessoa.nmFantasia" />
			<adsm:textbox
            	dataType="text"
            	property="filial.pessoa.nmFantasia"
            	size="30"
            	disabled="true"/>
		</adsm:lookup>
		
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