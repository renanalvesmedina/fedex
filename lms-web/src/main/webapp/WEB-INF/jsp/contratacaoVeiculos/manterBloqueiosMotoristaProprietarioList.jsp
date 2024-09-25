<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarBloqueioLiberacao" service="lms.contratacaoveiculos.bloqueioMotoristaPropService">
		<adsm:form action="/contratacaoVeiculos/manterBloqueiosMotoristaProprietario" idProperty="idBloqueioMotoristaProp" >
		
        	<adsm:textbox label="meioTransporte" property="meioTransporte.nrIdentificador" size="20" labelWidth="17%" width="83%" disabled="true" dataType="text" serializable="false">
				<adsm:hidden property="meioTransporte.idMeioTransporte" />
		        <adsm:textbox dataType="text" property="meioTransporte.nrFrota" size="50" maxLength="50" disabled="true" serializable="false"/>
        	</adsm:textbox>

			<adsm:textbox label="proprietario" property="proprietario.pessoa.nrIdentificacao" size="20" disabled="true" labelWidth="17%" width="83%" dataType="text" serializable="false">
				<adsm:hidden property="proprietario.idProprietario" />
				<adsm:textbox dataType="text" property="proprietario.pessoa.nmPessoa" size="50" maxLength="50" disabled="true" serializable="false"/>	
			</adsm:textbox>
			         
			<adsm:textbox property="motorista.pessoa.nrIdentificacao" dataType="text" label="motorista" size="20" labelWidth="17%" width="83%" disabled="true" serializable="false">
				<adsm:hidden property="motorista.idMotorista" />        	        	
		        <adsm:textbox dataType="text" property="motorista.pessoa.nmPessoa" size="50" maxLength="50" disabled="true" serializable="false"/>
	        </adsm:textbox>

			<adsm:textbox dataType="text" label="descricao" property="obBloqueioMotoristaProp" size="44" maxLength="500" labelWidth="17%" width="33%" />

		<adsm:range label="periodo" labelWidth="14%" width="33%" >
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="bloqueioMotoristaProp" />
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="bloqueioMotoristaProp" idProperty="idBloqueioMotoristaProp"
			selectionMode="none" unique="true" rows="10" scrollBars="horizontal" gridHeight="203" onRowClick="rowClick();">
		<adsm:gridColumn width="200" title="motivo" property="obBloqueioMotoristaProp" />
		<adsm:gridColumn width="140" title="bloqueadoPor" property="nmUsuarioBloqueio" />
		<adsm:gridColumnGroup separatorType="FILIAL" >
			<adsm:gridColumn title="filial" property="sgFilialBloqueio" width="20" />
			<adsm:gridColumn title="" property="nmFilialBloqueio" width="220" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn width="100" title="bloqueadoEm" property="dtRegistroBloqueio" dataType="JTDate"/>
		<adsm:gridColumn width="140" title="liberadoPor" property="nmUsuarioDesbloqueio" />
		<adsm:gridColumnGroup separatorType="FILIAL" >
			<adsm:gridColumn title="filial" property="sgFilialDesbloqueio" width="20" />
			<adsm:gridColumn title="" property="nmFilialDesbloqueio" width="220" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn width="100" title="liberadoEm" property="dtRegistroDesbloqueio" dataType="JTDate"/>
		<adsm:gridColumn width="150" title="vigenciaInicial" property="dhVigenciaInicial" dataType="JTDateTimeZone"/>
		<adsm:gridColumn width="150" title="vigenciaFinal" property="dhVigenciaFinal"  dataType="JTDateTimeZone"/>

		<adsm:buttonBar>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>
function rowClick() {
	return false;
}
</script>