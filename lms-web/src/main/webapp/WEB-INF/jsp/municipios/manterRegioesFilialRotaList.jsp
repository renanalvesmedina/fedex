<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarRegioesFilialRota" service="lms.municipios.manterRegioesFilialRotaAction" onPageLoadCallBack="regiaoFilialRotaPageLoad">
	<adsm:form action="/municipios/manterRegioesFilialRota" idProperty="idRegiaoFilialRotaColEnt">
	
		<adsm:lookup service="lms.municipios.manterRegioesFilialRotaAction.findFilialLookup" dataType="text" property="rotaColetaEntrega.filial" 
					 idProperty="idFilial" criteriaProperty="sgFilial" label="filialAtendida" size="3" maxLength="3" 
					 exactMatch="true" labelWidth="15%" width="50%"  action="/municipios/manterFiliais">
			<adsm:propertyMapping relatedProperty="rotaColetaEntrega.filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia"/>
			<adsm:propertyMapping />
			<adsm:textbox dataType="text" property="rotaColetaEntrega.filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>
		</adsm:lookup>
         
		<adsm:lookup service="lms.municipios.manterRegioesFilialRotaAction.findRotaLookup" idProperty="idRotaColetaEntrega" 
					 dataType="integer" property="rotaColetaEntrega" criteriaProperty="nrRota" 		
					 label="numeroRota" size="3" maxLength="3" labelWidth="15%" width="50%" action="/municipios/manterRotaColetaEntrega">
            <adsm:propertyMapping relatedProperty="rotaColetaEntrega.dsRota" modelProperty="dsRota" />
            <adsm:propertyMapping criteriaProperty="rotaColetaEntrega.filial.sgFilial" modelProperty="filial.sgFilial" />
            <adsm:propertyMapping criteriaProperty="rotaColetaEntrega.filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" />
            <adsm:propertyMapping criteriaProperty="rotaColetaEntrega.filial.idFilial" modelProperty="filial.idFilial" />
	        <adsm:textbox dataType="text" property="rotaColetaEntrega.dsRota" size="30" disabled="true"/>            
        </adsm:lookup>
        
		<adsm:combobox property="regiaoColetaEntregaFil.idRegiaoColetaEntregaFil" label="regiaoFilial" autoLoad="false"
				service="" optionLabelProperty="dsRegiaoColetaEntregaFil"
				optionProperty="idRegiaoColetaEntregaFil" boxWidth="231" >
			<adsm:propertyMapping relatedProperty="regiao.dtVigenciaInicial" modelProperty="dtVigenciaInicial"/>
	        <adsm:propertyMapping relatedProperty="regiao.dtVigenciaFinal"   modelProperty="dtVigenciaFinal"/>
      	</adsm:combobox>
		<adsm:range label="vigenciaRegiao" >
             <adsm:textbox dataType="JTDate" property="regiao.dtVigenciaInicial" picker="false" disabled="true" serializable="false"/>
             <adsm:textbox dataType="JTDate" property="regiao.dtVigenciaFinal" picker="false" disabled="true" serializable="false"/>
        </adsm:range>
		<adsm:hidden property="idRegiaoColetaEntregaFilMasterLink" serializable="false"/>
				
		<adsm:range label="vigencia" width="60%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial"/>
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>
         
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="regiaoFilialRotaColEnt"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid selectionMode="check" idProperty="idRegiaoFilialRotaColEnt" rows="11" property="regiaoFilialRotaColEnt" gridHeight="200" unique="true"
				defaultOrder="rotaColetaEntrega_filial_.sgFilial, regiaoColetaEntregaFil_.dsRegiaoColetaEntregaFil, rotaColetaEntrega_.nrRota, dtVigenciaInicial">
		<adsm:gridColumnGroup separatorType="FILIAL">
			<adsm:gridColumn title="filial" property="rotaColetaEntrega.filial.sgFilial" width="95" />
			<adsm:gridColumn title="" property="rotaColetaEntrega.filial.pessoa.nmFantasia" width="45" /> 
		</adsm:gridColumnGroup>
		 
		<adsm:gridColumnGroup customSeparator=" - ">		
				<adsm:gridColumn title="rota" property="rotaColetaEntrega.nrRota" width="105"/>
				<adsm:gridColumn title="" property="rotaColetaEntrega.dsRota" width="45"/>
		</adsm:gridColumnGroup>
	
		<adsm:gridColumn title="regiaoFilial" property="regiaoColetaEntregaFil.dsRegiaoColetaEntregaFil" width="0" />
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" width="90" dataType="JTDate"/>
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" width="80" dataType="JTDate"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>

	function regiaoFilialRotaPageLoad_cb(data){
		onPageLoad_cb(data);
		carregaRegiaoFilial();
	}
	
	function carregaRegiaoFilial(){
		var data = new Array();

		if (document.getElementById("regiaoColetaEntregaFil.idRegiaoColetaEntregaFil").masterLink != "true")
			setNestedBeanPropertyValue(data, "filial.idFilial", getElementValue("rotaColetaEntrega.filial.idFilial"));
			
		var sdo = createServiceDataObject("lms.municipios.manterRegioesFilialRotaAction.findComboRegiaoColetaList", "setaRegiaoFilial", data);
		xmit({serviceDataObjects:[sdo]});
	}

	function setaRegiaoFilial_cb(data,error){
		regiaoColetaEntregaFil_idRegiaoColetaEntregaFil_cb(data,error);
		
		setaMascaraVigenciaRegiao_cb(data,error);
		if (getElementValue("idRegiaoColetaEntregaFilMasterLink") != ""){
			setElementValue("regiaoColetaEntregaFil.idRegiaoColetaEntregaFil", getElementValue("idRegiaoColetaEntregaFilMasterLink"));
		}
	}
	
	function setaMascaraVigenciaRegiao_cb(data, error) {
		var i;
		var vi = document.getElementById("regiao.dtVigenciaInicial");
		var vf = document.getElementById("regiao.dtVigenciaFinal");	
		for (i = 0; i < data.length; i++) {
			setNestedBeanPropertyValue(data, i + ":dtVigenciaInicial", setFormat(vi, getNestedBeanPropertyValue(data, i + ":dtVigenciaInicial")));
			setNestedBeanPropertyValue(data, i + ":dtVigenciaFinal",   setFormat(vf, getNestedBeanPropertyValue(data, i + ":dtVigenciaFinal")));		
		}
	}
</script>