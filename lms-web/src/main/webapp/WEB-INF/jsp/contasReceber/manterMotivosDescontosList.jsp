<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.manterMotivosDescontosAction">
	<adsm:form action="/contasReceber/manterMotivosDescontos" idProperty="idMotivoDesconto">

		<adsm:textbox 	dataType="text" 
						property="dsMotivoDesconto" 
						size="70" 
						maxLength="60" 
						width="85%" 
						label="descricao"/>
		
		<adsm:textbox 	dataType="text" 
						property="cdMotivoDesconto"  
						size="3"  
						maxLength="3" 
						labelWidth="15%" 
						width="15%" 
						label="codigo"/>

		<adsm:combobox 	property="tpMotivoDesconto" 
						label="setorCausadorAbatimento" 
						labelWidth="22%"
						width="38%"
						domain="DM_SETOR_CAUSADOR"/>
						
		<adsm:combobox 	property="tpSituacao" 
						label="situacao"
						labelWidth="15%"
						width="35%" 
						domain="DM_STATUS"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="motivos"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid idProperty="idMotivoDesconto" property="motivos" gridHeight="200" unique="true" defaultOrder="dsMotivoDesconto" rows="11">
		<adsm:gridColumn title="descricao" property="dsMotivoDesconto" width="65%" />
		<adsm:gridColumn title="codigo" property="cdMotivoDesconto" width="10%" />
		<adsm:gridColumn title="setorCausadorAbatimento" property="tpMotivoDesconto" isDomain="true" width="15%" />
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="10%" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>