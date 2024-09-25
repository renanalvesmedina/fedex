<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.carregamento.vincularTransponderControleCargaAction" >
	<adsm:form action="/carregamento/vincularTransponderControleCarga" idProperty="idTransponder" height="388" onDataLoadCallBack="myOnDataLoad" >

		<adsm:lookup dataType="text" 
				property="controleCarga.filialByIdFilialOrigem" idProperty="idFilial" criteriaProperty="sgFilial" 
				service="lms.portaria.manterQuilometragensSaidaChegadaAction.findLookupFilial" popupLabel="pesquisarFilial"
				label="controleCarga" size="3" maxLength="3" width="85%" labelWidth="15%" picker="false" serializable="false"
				action="/municipios/manterFiliais" onchange="return onFilialControleCargaChange(this);" required="true" >
				
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
		
		
		<adsm:hidden property="tpSituacaoTransponder" value="D" serializable="false" />
		<adsm:lookup label="transponder" property="transponder" 
        	service="lms.carregamento.manterTranspondersAction.findLookup" 
        	action="/carregamento/manterTransponders" 
        	idProperty="idTransponder" 
        	criteriaProperty="nrTransponder" dataType="text" size="10" 
        	labelWidth="15%"
			width="85%"
			maxLength="10" required="true" >
			<adsm:propertyMapping modelProperty="tpSituacaoTransponder" criteriaProperty="tpSituacaoTransponder" disable="true" />
		</adsm:lookup>
			
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton id="storeButton" />
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
function onFilialControleCargaChange(elem) {
	setDisabled("controleCarga.idControleCarga",elem.value == "");
	return controleCarga_filialByIdFilialOrigem_sgFilialOnChangeHandler();
}

function initWindow(eventObj){
	if( eventObj.name == 'storeButton' || eventObj.name == 'gridRow_click' ){
		setDisabled("storeButton",true);
	} else {
		setDisabled("storeButton",false);
	}
}

function myOnDataLoad_cb(d,e,c,x){
	onDataLoad_cb(d,e,c,x);
	if (e == undefined) {
		setDisabled("storeButton",true);
	}
}
</script>