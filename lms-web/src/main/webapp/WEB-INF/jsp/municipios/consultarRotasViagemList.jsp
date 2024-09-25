<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarRotas">
	<adsm:form action="/municipios/consultarRotasViagem">
		<adsm:lookup service="" dataType="integer" property="filial.id" criteriaProperty="filial.codigo" label="rota" size="5" maxLength="10" width="85%"  action="/municipios/manterFiliais" required="true">
			<adsm:propertyMapping modelProperty="nome" formProperty="nomeFilial"/>
	        <adsm:listbox property="" size="3" prototypeValue="" optionLabelProperty="2|1"  optionProperty="" service="false" useRowspan="false" boxWidth="100" />
		</adsm:lookup>
		<adsm:lookup service="" dataType="text" property="filial.id" criteriaProperty="filial.codigo" label="origemTrecho" size="6" maxLength="50" width="11%" action="/municipios/manterFiliais" cmd="list">
        	<adsm:propertyMapping modelProperty="nome" formProperty="nomeFilial" /> 
        </adsm:lookup>		
        <adsm:textbox dataType="text" property="nomeFilial" size="16" maxLength="50" disabled="true" width="24%" />
		<adsm:lookup service="" dataType="text" property="filial.id" criteriaProperty="filial.codigo" label="destinoTrecho" size="6" maxLength="50" width="11%" action="/municipios/manterFiliais" cmd="list">
        	<adsm:propertyMapping modelProperty="nome" formProperty="nomeFilial" /> 
        </adsm:lookup>		
        <adsm:textbox dataType="text" property="nomeFilial" size="16" maxLength="50" disabled="true" width="24%" />
		<adsm:range label="horarioSaida" width="35%" >
			<adsm:textbox dataType="JTTime" property="horarioInicial"/>
			<adsm:textbox dataType="JTTime" property="horarioFinal"/>
		</adsm:range>
		<adsm:textbox dataType="JTDate" property="dataVigencia" label="vigencia" width="80%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid paramId="id" paramProperty="id" selectionMode="check" gridHeight="180" unique="true" rows="8" >
		<adsm:gridColumn title="rota" property="rota" width="28%" />
		<adsm:gridColumn title="horarioSaida" property="horarioSaida" width="18%" align="center" />
		<adsm:gridColumn title="dom" property="do" width="4%"/>
		<adsm:gridColumn title="seg" property="sg" width="4%"/>
		<adsm:gridColumn title="ter" property="te" width="4%"/>
		<adsm:gridColumn title="qua" property="qa" width="4%"/>
		<adsm:gridColumn title="qui" property="qi" width="4%"/>
		<adsm:gridColumn title="sex" property="sx" width="4%"/>
		<adsm:gridColumn title="sab" property="sa" width="4%"/>
		<adsm:gridColumn title="vigenciaInicial" property="vigenciaInicial" width="13%" />
		<adsm:gridColumn title="vigenciaFinal" property="vigenciaFinal" width="13%" />
		<adsm:buttonBar>
			<adsm:button caption="excluir" /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>