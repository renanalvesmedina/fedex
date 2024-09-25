<%-- @ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" import="org.apache.commons.beanutils.*" --%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.agenciaBancariaService">
	<adsm:form action="/configuracoes/manterAgencias" idProperty="idAgenciaBancaria">

     	<adsm:lookup service="lms.configuracoes.bancoService.findLookup"
					 idProperty="idBanco"
				     dataType="integer"
					 property="banco"
					 criteriaProperty="nrBanco"
					 criteriaSerializable="true"
					 label="banco"
					 size="5"
					 maxLength="3"
					 width="30%"
					 action="/configuracoes/manterBancos"
					 exactMatch="false" minLengthForAutoPopUpSearch="1">
			<adsm:propertyMapping modelProperty="nmBanco" relatedProperty="banco.nmBanco"/>
			<adsm:textbox dataType="text" property="banco.nmBanco" disabled="true"/>
		</adsm:lookup>

		<adsm:complement label="numero" width="40%">
			<adsm:textbox dataType="integer" minValue="0" property="nrAgenciaBancaria" maxLength="4" size="5" width="9%"/>
			<adsm:textbox dataType="text" property="nrDigito" maxLength="2" size="2" width="9%" style="width: 18px;"/>
		</adsm:complement>


		<adsm:textbox dataType="text" property="nmAgenciaBancaria" label="nome" maxLength="60" size="60" width="85%"/>
		<adsm:textbox dataType="text" property="dsEndereco" label="endereco" maxLength="100" size="100"width="85%"/>
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="agencia"/>
			<adsm:resetButton/>
		</adsm:buttonBar>

	</adsm:form>
	<adsm:grid idProperty="idAgenciaBancaria" property="agencia" defaultOrder="nmAgenciaBancaria:asc">
		<adsm:gridColumn width="40%" title="nome" property="nmAgenciaBancaria" />
		<adsm:gridColumn width="10%" title="numero" property="nrAgenciaBancariaDigito"/>
		<adsm:gridColumn width="40%" title="banco" property="banco.nmBancoNmPais"/>
		<adsm:gridColumn width="10%" title="situacao" property="tpSituacao.description"/>
        <adsm:buttonBar> 
		   <adsm:removeButton/>
	    </adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>
</script>
