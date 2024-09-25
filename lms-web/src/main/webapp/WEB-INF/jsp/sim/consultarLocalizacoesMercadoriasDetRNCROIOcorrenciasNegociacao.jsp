<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="negociacaoOcorrencia">
	<adsm:form action="/sim/consultarLocalizacoesMercadorias" height="445">
		<adsm:section caption="negociacoesOcorrencia"/>
		<adsm:textbox width="32%" labelWidth="18%" size="15" dataType="text" property="dataHora" label="dataHora" disabled="true"/>
		<adsm:textbox width="32%" labelWidth="18%" size="25" dataType="text" property="filial" label="filial" disabled="true"/>

		<adsm:textbox width="32%" labelWidth="18%" size="25" dataType="text" property="setor" label="setor" disabled="true"/>
		<adsm:textbox width="32%" labelWidth="18%" size="25" dataType="text" property="funcionario" label="funcionario" disabled="true"/>

		<adsm:textarea property="descricao" maxLength="50" label="descricao" columns="104" labelWidth="18%" width="78%" disabled="true"/>

		<adsm:grid paramId="id" paramProperty="id" showCheckbox="false" unique="false" gridHeight="295">
			<adsm:gridColumn width="20%" title="dataHora" property="data" align="center"/>
			<adsm:gridColumn width="20%" title="filial" property="filial" align="left"/>
			<adsm:gridColumn width="20%" title="setor" property="setor" align="left"/>
			<adsm:gridColumn width="20%" title="funcionario" property="funcionario" align="left"/>
			<adsm:gridColumn width="20%" title="situacao" property="situacao" align="left"/>
			
			<adsm:buttonBar>
				<adsm:button caption="fechar"/>
			</adsm:buttonBar>
		</adsm:grid>
	</adsm:form>
</adsm:window>   