<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarFinalidadesBox" service="lms.portaria.manterFinalidadesBoxAction">

	   <adsm:form action="/portaria/manterFinalidadesBox" idProperty="idFinalidade">
	   
        <adsm:textbox dataType="text" property="dsFinalidade" size="60" maxLength="60" label="descricao" labelWidth="21%" width="79%"/>
        
	   <adsm:combobox property="tpControleCarga" domain="DM_TIPO_CONTROLE_CARGAS" labelWidth="21%" label="tipoControleCarga" width="29%" />
	 
	 	<adsm:combobox property="blDescarga" labelWidth="21%" label="descarga" width="29%" domain="DM_SIM_NAO"/>   
	   
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" labelWidth="21%" label="situacao" width="79%" />
           
     
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="finalidade"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid property="finalidade" defaultOrder="dsFinalidade" idProperty="idFinalidade" gridHeight="200" unique="true" rows="12">
		<adsm:gridColumn title="descricao" property="dsFinalidade" width="55%" />	
		<adsm:gridColumn title="tipoControleCarga" isDomain="true" property="tpControleCarga" width="25%" />		
		<adsm:gridColumn title="descarga" renderMode="image-check" property="blDescarga"  width="10%" />		
		<adsm:gridColumn title="situacao" isDomain="true" property="tpSituacao" width="10%" />	
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
	
</adsm:window>
