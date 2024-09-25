<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/municipios/manterTrechosRotaViagem">
		<adsm:textbox dataType="text" label="rota" property="rotaId" size="30" labelWidth="17%" disabled="true" width="60%"/>
        <adsm:combobox property="filial.id" label="filialOrigem" service="" optionLabelProperty="" optionProperty="filial.codigo" labelWidth="17%" width="33%" required="true" />
		<adsm:combobox property="filial.id" label="filialDestino" service="" optionLabelProperty="" optionProperty="filial.codigo" labelWidth="17%" width="33%" required="true" />
        <adsm:textbox dataType="JTTime" label="horarioSaida" property="horaSaida" maxLength="4" size="6" required="true" labelWidth="17%" width="33%"/>
		<adsm:textbox dataType="text" label="distancia" property="distancia" maxLength="18" size="6" required="true" labelWidth="17%" unit="km2" width="33%"/>		
		<adsm:textbox dataType="text" label="tempoViagem" property="tempoViagem" maxLength="4" size="6" required="true" labelWidth="17%" unit="h" width="33%"/>
		<adsm:textbox dataType="text" label="tempoOperacao" property="tempoOperacao" maxLength="4" size="6" required="true" labelWidth="17%" unit="h" width="33%"/>
		<adsm:multicheckbox property="dia" label="frequencia" texts="dom|seg|ter|qua|qui|sex|sab" align="top" labelWidth="17%" width="60%" />
		<adsm:range label="vigencia" labelWidth="17%" width="45%">
             <adsm:textbox dataType="JTDate" property="dataVigenciaInicial" required="true" picker="true"/>
             <adsm:textbox dataType="JTDate" property="dataVigenciaFinal" picker="true"/>
        </adsm:range>
	<adsm:buttonBar>
			<adsm:button caption="pontosParadaTrecho" action="/municipios/manterPontosParadaTrecho" cmd="main"/>
			<adsm:button caption="novo"/>
			<adsm:button caption="salvar"/>
			<adsm:button caption="excluir"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   