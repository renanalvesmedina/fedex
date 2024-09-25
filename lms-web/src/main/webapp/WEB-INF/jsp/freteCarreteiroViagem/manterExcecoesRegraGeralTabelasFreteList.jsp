<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterExcecoesRegraGeral">
	<adsm:form action="/freteCarreteiroViagem/manterExcecoesRegraGeralTabelasFrete" >
		<adsm:combobox property="empresa.id" label="empresa" service="" optionLabelProperty="" optionProperty="" labelWidth="20%" width="30%"/>
		<adsm:combobox property="regional.id" label="regional" service="" optionLabelProperty="" optionProperty="" labelWidth="20%" width="30%" />
		<adsm:combobox property="filial.id" label="filial" service="" optionLabelProperty="" optionProperty="" labelWidth="20%" width="30%" />
		<adsm:combobox property="tipoRota" label="tipoRota" prototypeValue="Expressa|Eventual" service="" optionLabelProperty="" optionProperty="" labelWidth="20%" width="30%"/>
		<adsm:combobox property="tipoMeioTransporte.id" label="tipoMeioTransporte" service="" optionLabelProperty="" optionProperty="" labelWidth="20%" width="30%" />
		<adsm:lookup service="" dataType="text" property="meioTransporte.id" criteriaProperty="meioTransporte.codigo" label="meioTransporte" size="5" maxLength="10" labelWidth="20%" width="11%" action="/contratacaoVeiculos/manterMeiosTransporte" cmd="list" >
        	<adsm:propertyMapping modelProperty="meioTransporte" formProperty="meioTransporte"/> 
        </adsm:lookup>		
        <adsm:textbox dataType="text" property="meioTransporte" size="15" maxLength="50" disabled="true" width="19%" />		
		<adsm:lookup service="" dataType="text" property="proprietario.id" criteriaProperty="proprietario.codigo" label="proprietario" size="5" maxLength="10" labelWidth="20%" width="11%" action="/contratacaoVeiculos/manterProprietarios" cmd="list" >
        	<adsm:propertyMapping modelProperty="proprietario" formProperty="proprietario"/> 
        </adsm:lookup>
		<adsm:textbox dataType="text" property="proprietario" size="20" maxLength="50" disabled="true" width="69%" />
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid paramId="id" paramProperty="id" showCheckbox="true" unique="true" rows="9" >
		<adsm:gridColumn width="22%" title="empresa" property="empresa" />
		<adsm:gridColumn width="16%" title="regional" property="regional" />
		<adsm:gridColumn width="12%" title="filial" property="filial" />
		<adsm:gridColumn width="14%" title="tipoRota" property="tipoRota" />
		<adsm:gridColumn width="18%" title="rota" property="rota" />
		<adsm:gridColumn width="18%" title="meioTransporte" property="meioTransporte" />		
		<adsm:buttonBar>
			<adsm:button caption="excluir" /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
