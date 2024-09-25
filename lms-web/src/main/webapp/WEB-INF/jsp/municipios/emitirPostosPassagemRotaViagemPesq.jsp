<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="emitirPostosPassagemRotaViagem" service="lms.municipios.emitirPostosPassagemRotaViagemAction">
	<adsm:form action="/municipios/emitirPostosPassagemRotaViagem" >

		<adsm:lookup dataType="text"  property="rota" idProperty="idRota" criteriaProperty="dsRota"  exactMatch="false"
			service="lms.municipios.emitirPostosPassagemRotaViagemAction.findLookupRota" action="/municipios/consultarRotasViagem" cmd="list"
		  	label="rotaViagem" labelWidth="19%" width="81%" size="30"/>

		<adsm:combobox property="tpPostoPassagem" domain="DM_POSTO_PASSAGEM" onlyActiveValues="true" boxWidth="120" label="tipoPostoPassagem" labelWidth="19%" width="81%"/>
		  	  
		<adsm:lookup dataType="text" property="postoPassagem" idProperty="idPostoPassagem" criteriaProperty="tpPostoPassagem.description"
        		action="/municipios/manterPostosPassagem" service=""
        		size="30" minLengthForAutoPopUpSearch="3" exactMatch="false"
        		label="postoPassagem" labelWidth="19%" width="31%">
        	<adsm:propertyMapping criteriaProperty="tpPostoPassagem" modelProperty="tpPostoPassagem"/>
        	<adsm:propertyMapping relatedProperty="postoPassagem.municipio.nmMunicipio" modelProperty="municipio.nmMunicipio" />
        	<adsm:propertyMapping relatedProperty="postoPassagem.tpSentidoCobranca.description" modelProperty="tpSentidoCobranca.description" />
        	<adsm:propertyMapping relatedProperty="postoPassagem.rodovia.sgRodovia" modelProperty="rodovia.sgRodovia" />
        	<adsm:propertyMapping relatedProperty="postoPassagem.nrKm" modelProperty="nrKm" />
	    </adsm:lookup>
	    
		<adsm:textbox dataType="text" property="postoPassagem.municipio.nmMunicipio" serializable="false" label="localizacao" size="30" maxLength="35" labelWidth="19%" width="31%" disabled="true"/>
		<adsm:textbox dataType="text" property="postoPassagem.tpSentidoCobranca.description" serializable="false" label="sentido" size="30" maxLength="35" labelWidth="19%" width="31%" disabled="true"/>
		<adsm:textbox dataType="text" property="postoPassagem.rodovia.sgRodovia" serializable="false" label="rodovia" size="15" maxLength="35" labelWidth="19%" width="31%" disabled="true"/>
		<adsm:textbox dataType="text" property="postoPassagem.nrKm" label="km" serializable="false" size="15" maxLength="35" labelWidth="19%" width="31%" disabled="true"/>

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.municipios.emitirPostosPassagemRotaViagemService"/>
			<adsm:resetButton/>
		</adsm:buttonBar>	
	</adsm:form>
</adsm:window>
<script>
<!--
	document.getElementById("postoPassagem.tpPostoPassagem.description").disabled = true;
//-->
</script>