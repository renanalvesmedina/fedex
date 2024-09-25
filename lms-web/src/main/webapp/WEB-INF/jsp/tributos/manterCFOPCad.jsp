<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%> 
<adsm:window service="lms.tributos.manterCFOPAction">
	
	<adsm:form action="/tributos/manterCFOP" height="360" idProperty="idCodigoFiscalOperacao">	
		
		<adsm:textbox  dataType="integer" property="cdCfop" label="codigo" size="11" maxLength="10" required="true"/>
		
		<adsm:textarea property="dsCfop" label="descricao" width="85%" maxLength="200" required="true" columns="80" rows="3"/>
				
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" required="true"/>	        		
	
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
	
</adsm:window>