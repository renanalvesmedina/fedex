<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%> 
<adsm:window service="lms.tributos.manterCFOPAction">

	<adsm:form action="/tributos/manterCFOP">

		<adsm:textbox dataType="integer" property="cdCfop" label="codigo" size="11" maxLength="10"/>

		<adsm:textarea property="dsCfop" label="descricao" width="85%" maxLength="200" columns="80" rows="3" />

		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS"/>	        		

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="codigoFiscalOperacao"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid idProperty="idCodigoFiscalOperacao" property="codigoFiscalOperacao" gridHeight="200" unique="true" defaultOrder="cdCfop">
		<adsm:gridColumn title="codigo" property="cdCfop" dataType="integer" width="12%"/>
		<adsm:gridColumn title="descricao" property="dsCfop" width="73%"/>
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="15%"/>
		<adsm:buttonBar >
			<adsm:removeButton/>
		</adsm:buttonBar>		
	</adsm:grid>
	
</adsm:window>