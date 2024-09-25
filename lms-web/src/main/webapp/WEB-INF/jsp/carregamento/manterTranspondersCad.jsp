<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.carregamento.manterTranspondersAction" >
	<adsm:form action="/carregamento/manterTransponders" idProperty="idTransponder" height="388" >

		<adsm:lookup dataType="text" 
				property="controleCarga.filialByIdFilialOrigem" idProperty="idFilial" criteriaProperty="sgFilial" 
				service="lms.portaria.manterQuilometragensSaidaChegadaAction.findLookupFilial" popupLabel="pesquisarFilial"
				label="controleCarga" size="3" maxLength="3" width="85%" labelWidth="15%" picker="false" serializable="false"
				action="/municipios/manterFiliais"  disabled="true" >
				
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
		
		<adsm:textbox label="transponder" property="nrTransponder" dataType="integer" maxLength="10" size="20" width="85%" required="true" />

		<adsm:combobox label="situacaoTransponder" property="tpSituacaoTransponder" domain="DM_SITUACAO_TRANSPONDER" width="85%" renderOptions="true" required="true" />		
		
		<adsm:lookup label="filial" property="filial" 
        	service="lms.municipios.filialService.findLookupFilial" 
        	action="/municipios/manterFiliais" 
        	idProperty="idFilial" 
        	criteriaProperty="sgFilial" dataType="text" size="5" 
        	labelWidth="15%" 
			width="85%" 
			maxLength="3" required="true" >
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
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton /> 
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
	function initWindow(eventObj){		
    	if( eventObj.name != 'storeButton' && eventObj.name != 'gridRow_click' ){
			var sdo = createServiceDataObject("lms.carregamento.manterTranspondersAction.getFilialSessao", "loadFilialSessao");
		   	xmit({serviceDataObjects:[sdo]});
		}
	}

	function loadFilialSessao_cb(data, error) {
		if (error) {
			alert(error);
			setFocusOnFirstFocusableField();
			return false;
		}
		setElementValue("filial.idFilial", data.filial.idFilial);
		setElementValue("filial.sgFilial", data.filial.sgFilial);
		setElementValue("filial.pessoa.nmFantasia", data.filial.pessoa.nmFantasia);
	}
</script>