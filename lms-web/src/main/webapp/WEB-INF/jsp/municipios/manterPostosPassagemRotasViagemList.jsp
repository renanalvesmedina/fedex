<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarPostosPassagemRota" service="lms.municipios.manterRotaAction">
	<adsm:form action="/municipios/manterPostosPassagemRota" idProperty="idRota">
	    
	    <adsm:textbox dataType="text" property="dsRota" maxLength="150" size="50" label="descricaoRota" labelWidth="22%" width="78%"/>
	    
	    <adsm:listbox 
                   label="rota" 
                   size="4" 
                   property="filialRotas" 
				   optionProperty="sgFilial"
				   optionLabelProperty="dsRota"
				   labelWidth="22%"
                   width="78%" 
                   showOrderControls="false" boxWidth="180" showIndex="false" serializable="true">
                 <adsm:lookup 
	                 property="filial" 
	                 idProperty="idFilial" 
	                 criteriaProperty="sgFilial" 
	                 dataType="text" size="3" maxLength="3" 
	                 exactMatch="false" minLengthForAutoPopUpSearch="3"
	                 service="lms.municipios.manterRotaAction.findFilialLookup" action="/municipios/manterFiliais">
	                 <adsm:propertyMapping relatedProperty="filialRotas_nmFilial" modelProperty="pessoa.nmFantasia"/>
	                 <adsm:textbox dataType="text" property="nmFilial" serializable="false" maxLength="60" disabled="true"/>  
	            </adsm:lookup> 
	   </adsm:listbox>
		        
		<adsm:lookup 
			label="filialOrigem" 
			service="lms.municipios.manterRotaAction.findFilialLookup" 
			dataType="text" 
			property="filialOrigem" 
			idProperty="idFilial" 
			criteriaProperty="sgFilial" 
			size="3" maxLength="3" labelWidth="22%" width="28%" action="/municipios/manterFiliais" exactMatch="false" minLengthForAutoPopUpSearch="3">
        	<adsm:propertyMapping relatedProperty="nmFilialOrigem" modelProperty="pessoa.nmFantasia"/>
        	<adsm:textbox dataType="text" property="nmFilialOrigem" size="20" maxLength="60" disabled="true" serializable="false"/>
        </adsm:lookup>
        
        
        <adsm:lookup 
	        label="filialDestino" 
	        service="lms.municipios.manterRotaAction.findFilialLookup" 
	        dataType="text" 
	        property="filialDestino" 
	        idProperty="idFilial" 
	        criteriaProperty="sgFilial" 
	        size="3" maxLength="3" labelWidth="22%" width="28%" action="/municipios/manterFiliais" exactMatch="false" minLengthForAutoPopUpSearch="3">
        	<adsm:propertyMapping relatedProperty="nmFilialDestino" modelProperty="pessoa.nmFantasia"/>
        	<adsm:textbox dataType="text" property="nmFilialDestino" size="20" disabled="true" serializable="false"/>
        </adsm:lookup>
        
        
        <adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="rota"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	 
	<adsm:grid property="rota" idProperty="idRota"  gridHeight="180" rows="9" unique="true" defaultOrder="dsRota"
		service="lms.municipios.manterRotaAction.findPaginatedRotaViagemEventual"
		rowCountService="lms.municipios.manterRotaAction.getRowCountRotaViagemEventual">
		<adsm:gridColumn title="rota" property="dsRota" width="100%" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>
document.getElementById("filialOrigem.sgFilial").serializable = true;
document.getElementById("filialDestino.sgFilial").serializable = true;
</script>