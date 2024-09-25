<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.portaria.manterFinalidadesBoxAction" >

	<adsm:form action="/portaria/manterFinalidadesBox" idProperty="idFinalidade" service="lms.portaria.manterFinalidadesBoxAction.findById">
	
       <adsm:textbox dataType="text" property="dsFinalidade" size="60" maxLength="60" label="descricao" required="true" labelWidth="21%" width="79%"/>
	   
	   <adsm:combobox property="tpControleCarga" domain="DM_TIPO_CONTROLE_CARGAS" labelWidth="21%" label="tipoControleCarga" width="29%" />
	  
	  <adsm:checkbox property="blDescarga" labelWidth="21%" label="descarga" width="29%" />   
	   
	  <adsm:combobox property="tpSituacao" domain="DM_STATUS" labelWidth="21%" required="true" label="situacao"  width="79%" />   
             
	<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>				
	</adsm:form>	
</adsm:window>   
