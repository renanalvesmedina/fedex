<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/contratacaoVeiculos/aprovarSolicitacoesContratacao" height="390" >
		<adsm:textbox dataType="text" property="filial.id" label="filial" labelWidth="23%" width="11%" size="10" disabled="true" />
		<adsm:textbox dataType="text" property="filial.nome" width="66%" size="20" disabled="true" />

		<adsm:textbox dataType="text" property="tipoSolicitacao" label="tipoSolicitacao" maxLength="20" size="20" labelWidth="23%" width="32%" disabled="true" />
		<adsm:textbox dataType="text" property="numeroSolicitacao" label="numeroSolicitacao" maxLength="20" size="20" labelWidth="21%" width="24%" disabled="true" />

		<adsm:textbox dataType="text" property="meioTransporte" label="meioTransporte" maxLength="20" size="20" labelWidth="23%" width="32%" disabled="true" />
		<adsm:textbox dataType="text" property="tipoMeioTransporte" label="tipoMeioTransporte" maxLength="20" size="20" labelWidth="21%" width="24%" disabled="true" />

		<adsm:checkbox property="meioTransporteRastreado" label="meioTransporteRastreado" width="60%" labelWidth="23%" disabled="true"/>

		<adsm:textarea property="observacao" maxLength="50" label="observacao" columns="90" labelWidth="23%" width="87%" disabled="true"/>

		<adsm:textbox dataType="JTDate" property="dataSolicitacao" label="dataSolicitacao" maxLength="10" labelWidth="23%" width="50%" disabled="true" />

		<adsm:textbox dataType="text" property="solicitadoPor" label="solicitadoPor" size="22" disabled="true" labelWidth="23%" width="32%" />

		<adsm:textbox dataType="text" property="situacao" label="situacao" labelWidth="21%" width="24%" disabled="true" />

		<adsm:section caption="viagem"/>
        <adsm:listbox property="" size="3" label="rota" prototypeValue="POA|CXS" optionLabelProperty="2|1" optionProperty="dd12dsa" service="false" useRowspan="false" boxWidth="50" labelWidth="23%" width="77%" />

 		<adsm:textbox dataType="JTDate" property="dataViagem" label="dataViagem" maxLength="10" width="77%" labelWidth="23%" disabled="true"/>

		<adsm:textbox dataType="text" property="moeda.id" size="20" label="moeda" width="32%" labelWidth="23%" disabled="true" />

		<adsm:textbox dataType="text" property="freteSugerido" label="freteSugerido" maxLength="20" size="20" labelWidth="23%" width="32%" disabled="true" />
		<adsm:textbox dataType="currency" property="freteMaximo" label="freteMaximoAutorizado" maxLength="20" size="10" labelWidth="21%" width="24%" />

		<adsm:section caption="coletaEntrega"/>
		<adsm:multicheckbox property="dia" labelWidth="23%" width="32%" label="frequenciaValidade" texts="dom|seg|ter|qua|qui|sex|sab" align="top"/>
		<adsm:textbox cellStyle="verticalAlignment:middle"  dataType="JTTime" label="horarioInicio" labelWidth="21%" width="24%" property="horarioDiarioInicial"/>
		<adsm:range label="periodoContratacao" width="60%" labelWidth="23%" >
			<adsm:textbox dataType="JTDate" property="dataVigenciaInicial" disabled="true" />
			<adsm:textbox dataType="JTDate" property="dataVigenciaFinal" disabled="true" />
		</adsm:range>

		<!-- Foi implementado uma table manualmente! -->
		<adsm:label key="branco" width="100%" />
		<tr><td colspan="100">
		<table width="100%" class="Form"> 
			<tr>
				<td width="16%">
					<table>
						<adsm:section caption="parcela" width="40%" />
						<tr><adsm:label key="diaria" width="20%" style="height:23px" /></tr>
						<tr><adsm:label key="evento" width="20%" style="height:23px" /></tr>
						<tr><adsm:label key="fracaoPeso" width="20%" style="height:23px" /></tr>
						<tr><adsm:label key="kmExcedente" width="20%" style="height:25px" /></tr>
					</table>
				</td>
				<td width="42%">
					<table>
						<adsm:section caption="sugerido" width="65%" />
						<tr><adsm:textbox dataType="text" property="diariaSugerido" size="30" width="40%" cellStyle="text-Align:center;" disabled="true" /></tr>
						<tr><adsm:textbox dataType="text" property="eventoSugerido" size="30" width="40%" cellStyle="text-Align:center;" disabled="true" /></tr>
						<tr><adsm:textbox dataType="text" property="fracaoPesoSugerido" size="30" width="40%" cellStyle="text-Align:center;" disabled="true" /></tr>
						<tr><adsm:textbox dataType="text" property="kmExcedenteSugerido" size="30" width="40%" cellStyle="text-Align:center;" disabled="true" /></tr>
					</table>
				</td>
				<td width="42%">
					<table>
						<adsm:section caption="maximoAprovado" width="65%" />
						<tr><adsm:textbox dataType="text" property="diariaMaximo" size="30" width="40%" cellStyle="text-Align:center;" /></tr>
						<tr><adsm:textbox dataType="text" property="eventoMaximo" size="30" width="40%" cellStyle="text-Align:center;" /></tr>
						<tr><adsm:textbox dataType="text" property="fracaoPesoMaximo" size="30" width="40%" cellStyle="text-Align:center;" /></tr>
						<tr><adsm:textbox dataType="text" property="kmExcedenteMaximo" size="30" width="40%" cellStyle="text-Align:center;" /></tr>
					</table>
				</td>
			</tr>
		</table>
		</td></tr>
		<adsm:label key="branco" width="100%" />

		<adsm:section caption="aprovacao" />
		<adsm:textbox dataType="text" property="cliente.id" label="aprovadoReprovadoPor" labelWidth="23%" width="11%" size="10" disabled="true" />
		<adsm:textbox dataType="text" property="cliente.nome" width="66%" size="20" disabled="true" />
		<adsm:buttonBar>
			<adsm:button caption="aprovar"/>
			<adsm:button caption="reprovar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
for(var i = 1; i < 8; i++){
	document.getElementById("dia" + i).checked = 1;
}
</script>