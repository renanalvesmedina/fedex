<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.contratacaoveiculos.bloqueioMotoristaPropService" >
	<adsm:form action="/contratacaoVeiculos/manterBloqueiosMotoristaProprietario" idProperty="idBloqueioMotoristaProp">

		<adsm:lookup service="" dataType="text" property="meioTransporte" idProperty="idMeioTransporte"  criteriaProperty="nrIdentificador" label="meioTransporte" size="5" maxLength="10" labelWidth="17%" width="11%" action="/contratacaoVeiculos/manterMeiosTransporte" cmd="list" >
        	<adsm:propertyMapping relatedProperty="meioTransporte.nrFrota" modelProperty="nrFrota" /> 
        </adsm:lookup>		
        <adsm:textbox dataType="text" property="meioTransporte.nrFrota" size="23" maxLength="50" disabled="true" width="72%" required="true" />

		<adsm:lookup service="" dataType="text" property="proprietario" idProperty="idProprietario" criteriaProperty="pessoa.nrIdentificacao" label="proprietario" size="5" maxLength="10" labelWidth="17%" width="11%" action="/contratacaoVeiculos/manterProprietarios" cmd="list" >
        	<adsm:propertyMapping relatedProperty="proprietario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" /> 
        </adsm:lookup>
		<adsm:textbox dataType="text" property="proprietario.pessoa.nmPessoa" size="23" maxLength="50" disabled="true" width="22%" required="true" />

		<adsm:lookup service="" dataType="text" property="motorista" idProperty="idMotorista" criteriaProperty="pessoa.nrIdentificacao" label="motorista" size="5" maxLength="10" labelWidth="17%" width="11%" action="/contratacaoVeiculos/manterMotoristas" cmd="list" >
        	<adsm:propertyMapping relatedProperty="motorista.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" /> 
        </adsm:lookup>		
        <adsm:textbox dataType="text" property="motorista.pessoa.nmPessoa" size="23" maxLength="50" disabled="true" width="22%" required="true" />

		<adsm:textbox dataType="text" label="bloqueadoPor" property="usuarioByIdFuncionarioBloqueio" size="35" maxLength="50" disabled="true" labelWidth="17%" width="33%" />
		<adsm:textbox dataType="text" label="desbloqueadoPor" property="usuarioByIdFuncionarioDesbloqueio" size="35" maxLength="50" disabled="true" labelWidth="17%" width="33%" />
		<adsm:textbox dataType="JTDate" label="registroBloqueio" property="dtRegistroBloqueio" size="35" labelWidth="17%" width="33%" disabled="true" />
		<adsm:textbox dataType="JTDate" label="registroDesbloqueio" property="dtRegistroDesbloqueio" size="35" labelWidth="17%" width="33%" disabled="true" />
		<adsm:textbox dataType="text" label="descricao" property="obBloqueioMotoristaProp" size="35" maxLength="50" required="true" labelWidth="17%" width="83%" />

		<adsm:range label="vigencia" labelWidth="17%" >
			<adsm:textbox dataType="JTDateTimeZone"  required="true" property="dhVigenciaInicial"/>
			<adsm:textbox dataType="JTDateTimeZone" property="dhVigenciaFinal"/>
		</adsm:range>
		<adsm:buttonBar>
			<adsm:button caption="bloquear"/>
			<adsm:button caption="desbloquear"/>
			<adsm:button caption="novo"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   