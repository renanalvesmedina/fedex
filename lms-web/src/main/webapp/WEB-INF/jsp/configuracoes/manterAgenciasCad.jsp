<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.agenciaBancariaService">

	<adsm:form action="/configuracoes/manterAgencias" idProperty="idAgenciaBancaria">
		<adsm:hidden property="banco.tpSituacao" value="A"/>
     	<adsm:lookup service="lms.configuracoes.bancoService.findLookup" 
					 idProperty="idBanco"
					 dataType="integer" 
					 property="banco" 					 
					 criteriaProperty="nrBanco"					 
					 label="banco" 
					 size="5" 
					 maxLength="3" 
					 width="30%" required="true"
					 action="/configuracoes/manterBancos"
					 exactMatch="false" minLengthForAutoPopUpSearch="1">
			<adsm:propertyMapping modelProperty="nmBanco" relatedProperty="banco.nmBanco"/>				
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="banco.tpSituacao"/>						
			<adsm:textbox dataType="text" property="banco.nmBanco" disabled="true" />
		</adsm:lookup>		

		<adsm:complement label="numero" width="40%">
			<adsm:textbox dataType="integer" minValue="0" property="nrAgenciaBancaria" maxLength="4" size="5" width="9%" required="true"/>
			<adsm:textbox dataType="text" property="nrDigito" maxLength="2" size="2" width="9%" style="width: 18px;"/>		
		</adsm:complement>

		<adsm:textbox dataType="text" property="nmAgenciaBancaria" label="nome" maxLength="60" size="60" width="85%"  required="true"/>
		<adsm:textbox dataType="text" property="dsEndereco" label="endereco" maxLength="100" size="100"width="85%"/>
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" required="true"/>

		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>