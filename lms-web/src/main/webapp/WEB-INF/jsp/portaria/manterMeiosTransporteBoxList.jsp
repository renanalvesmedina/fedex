<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.portaria.manterMeiosTransporteBoxAction">
	<adsm:form action="/portaria/manterMeiosTransporteBox" >

		<adsm:textbox dataType="text" property="box.doca.terminal.filial.sgFilial" size="3" label="filial" labelWidth="20%" width="30%" disabled="true">
			<adsm:textbox dataType="text"property="box.doca.terminal.filial.pessoa.nmFantasia" size="27" disabled="true"/>
		</adsm:textbox>
		
		<adsm:textbox dataType="text" property="box.doca.terminal.pessoa.nmPessoa" label="terminal" labelWidth="20%" width="30%" disabled="true" />
		
		<adsm:textbox dataType="text" property="box.doca.numeroDescricaoDoca" serializable="false" label="doca" labelWidth="20%" width="30%" disabled="true" />
		
		<adsm:textbox dataType="text" property="box.nrBox" label="box" labelWidth="20%" width="30%" disabled="true" />
		<adsm:hidden property="box.idBox"/>
		
        <adsm:lookup dataType="text" property="meioTransporteRodoviario2" idProperty="idMeioTransporte" maxLength="6"
				service="lms.municipios.manterVeiculosRotaAction.findLookupMeioTransporteRodov" picker="false"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrFrota"
				label="meioTransporte" labelWidth="20%" width="8%" size="7" serializable="false">
				
			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador"
					modelProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.idMeioTransporte"
					modelProperty="idMeioTransporte" />		
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador"
					modelProperty="meioTransporte.nrIdentificador" />				
		
			<adsm:lookup dataType="text" property="meioTransporteRodoviario" idProperty="idMeioTransporte" maxLength="25"
					service="lms.municipios.manterVeiculosRotaAction.findLookupMeioTransporteRodov" picker="true"
					action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrIdentificador"
					width="22%" size="20">
				<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario2.meioTransporte.nrFrota"
						modelProperty="meioTransporte.nrFrota" />
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.idMeioTransporte"
						modelProperty="idMeioTransporte" />	
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.meioTransporte.nrFrota"
						modelProperty="meioTransporte.nrFrota" />		
			</adsm:lookup>
			
	    </adsm:lookup>
        
		<adsm:range label="vigencia" labelWidth="20%" width="68%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" picker="true" />
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal" picker="true"/>
        </adsm:range>
        
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="meioTransporteRodoBox"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid idProperty="idMeioTransporteRodoBox" defaultOrder="meioTransporteRodoviario_meioTransporte_.nrFrota,dtVigenciaInicial" 
				property="meioTransporteRodoBox" unique="true" rows="11">
		<adsm:gridColumn title="meioTransporte" property="meioTransporteRodoviario.meioTransporte.nrFrota" width="200"  />
		<adsm:gridColumn title="" property="meioTransporteRodoviario.meioTransporte.nrIdentificador" width="200" align="left"  />
		<adsm:gridColumn title="vigenciaInicial" dataType="JTDate" property="dtVigenciaInicial" width="150" />			
		<adsm:gridColumn title="vigenciaFinal" dataType="JTDate" property="dtVigenciaFinal" width="150" />
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
