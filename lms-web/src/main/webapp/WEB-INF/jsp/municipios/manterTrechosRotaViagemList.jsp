<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarTrechosRotaViagem">
	<adsm:form action="/municipios/manterTrechosRotaViagem">
		<adsm:textbox dataType="text" label="rota" property="rotaId" size="30" labelWidth="17%" disabled="true" width="60%"/>
        <adsm:combobox property="filial.id" label="filialOrigem"  service="" optionLabelProperty="" optionProperty="filial.codigo" labelWidth="17%" width="33%" />
		<adsm:combobox property="filial.id" label="filialDestino"  service="" optionLabelProperty="" optionProperty="filial.codigo" labelWidth="17%" width="33%" />
       	<adsm:multicheckbox property="dia" label="frequencia" texts="dom|seg|ter|qua|qui|sex|sab" align="top" labelWidth="17%" width="60%" />
		<adsm:range label="vigencia" labelWidth="17%" width="83%">
             <adsm:textbox dataType="JTDate" property="dataVigenciaInicial" picker="true"/>
             <adsm:textbox dataType="JTDate" property="dataVigenciaFinal" picker="true"/>
        </adsm:range>
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid paramId="id" paramProperty="id" selectionMode="check" gridHeight="200" unique="true" scrollBars="horizontal" rows="8">
		<adsm:gridColumn title="filialOrigem" property="filialOrigemId" width="60" />
		<adsm:gridColumn title="filialDestino" property="filialDestinoId" width="60" />
		<adsm:gridColumn title="horarioSaida" property="horaSaida" width="90" align="center" />		
		<adsm:gridColumn title="distancia" property="distancia" unit="km2" width="90" />
		<adsm:gridColumn title="tempoViagem" property="tempoViagem" unit="h" align="center" width="90"/>
		<adsm:gridColumn title="tempoOperacao" property="tempoOperacao" unit="h" align="center" width="90" />
        <adsm:gridColumn title="dom" property="do" width="40"/>
		<adsm:gridColumn title="seg" property="sg" width="40"/>
        <adsm:gridColumn title="ter" property="te" width="40"/>
        <adsm:gridColumn title="qua" property="qa" width="40"/>
        <adsm:gridColumn title="qui" property="qi" width="40"/>
        <adsm:gridColumn title="sex" property="sx" width="40"/>
        <adsm:gridColumn title="sab" property="sa" width="40"/>
		<adsm:gridColumn title="vigenciaInicial" property="vigenciaInicial" width="90" dataType="JTDate"/>
		<adsm:gridColumn title="vigenciaFinal" property="vigenciaFinal" width="80" dataType="JTDate"/>
	<adsm:buttonBar>
		<adsm:button caption="excluir" /> 
	</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
