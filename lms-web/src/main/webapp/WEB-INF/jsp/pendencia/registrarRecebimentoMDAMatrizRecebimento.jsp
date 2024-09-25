<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/pendencia/registrarRecebimentoMDAMatriz" height="170">
		
		<adsm:lookup property="mda" label="mda" dataType="text" size="20" action="/pendencia/consultarMDA" cmd="mda" service="" labelWidth="20%" width="30%" required="true"/>

		<adsm:textbox label="recebimento" property="recebimento" dataType="JTDate" disabled="true" labelWidth="15%" width="35%"/>

		<adsm:lookup property="recebedor.id" label="recebedorFuncionario" action="/vendas/manterDadosIdentificacao" cmd="list" service="" dataType="text" labelWidth="20%" width="80%" required="true">
			<adsm:propertyMapping modelProperty="recebedor.id" formProperty="nomeRecebedor"/>
			<adsm:textbox dataType="text" property="nomeRecebedor" size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>
		
		<adsm:combobox optionLabelProperty="dispositivo" label="dispositivo" optionProperty="" property="" service="" labelWidth="20%" width="80%" >
			<adsm:textbox dataType="currency" property="dispositivo" />
		</adsm:combobox>		

		<adsm:combobox label="terminal" property="terminal" optionLabelProperty="" optionProperty="" service="" labelWidth="20%" width="80%"/>

		<adsm:textbox label="enderecoModuloRuaPredioAndarApto" property="modulo" dataType="integer" size="2" maxLength="2" labelWidth="20%" width="80%">

			<adsm:textbox property="rua" dataType="integer" size="2" maxLength="2">
	
				<adsm:textbox property="predio" dataType="integer" size="2" maxLength="2">
	
					<adsm:textbox property="andar" dataType="integer" size="2" maxLength="2">
		
						<adsm:lookup property="apto" action="/" dataType="integer" service="" size="2" maxLength="2" >
							<adsm:listbox property="enderecosList" optionLabelProperty="" optionProperty="" service="" size="6" prototypeValue="M1 - R5 - P1 - A4 - AP2|M1 - R5 - P1 - A5 - AP23|M1 - R5 - P2 - A7 - AP54|M1 - R5 - P6 - A4 - AP6" boxWidth="205"/>
	
						</adsm:lookup>
		
					</adsm:textbox>
	
				</adsm:textbox>
	
			</adsm:textbox>

		</adsm:textbox>

		<adsm:lookup property="filialOrigem.id" label="filialOrigem" action="/municipios/manterFiliais" service="" dataType="text" size="3" maxLength="3" labelWidth="20%" width="80%" disabled="true">
			<adsm:propertyMapping modelProperty="filialEmitente.id" formProperty="nomeFilialEmitente"/>
			<adsm:textbox dataType="text" property="nomeFilialEmitente" size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>

		<adsm:lookup property="filialDestino.id" label="filialDestino" action="/municipios/manterFiliais" service="" dataType="text" size="3" maxLength="3" labelWidth="20%" width="80%" disabled="true">
			<adsm:propertyMapping modelProperty="filialEmitente.id" formProperty="nomeFilialEmitente"/>
			<adsm:textbox dataType="text" property="nomeFilialEmitente" size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>

		<adsm:lookup property="remetente.id" label="remetente" action="/vendas/manterDadosIdentificacao" cmd="list" service="" dataType="text" size="18" maxLength="18" width="80%" labelWidth="20%" disabled="true">
			<adsm:propertyMapping modelProperty="remetente.id" formProperty="nomeRemetente" />
			<adsm:textbox dataType="text" property="nomeRemetente" size="50" disabled="true" />
		</adsm:lookup>

		<adsm:lookup property="desrtinatario.id" label="destinatario" action="/vendas/manterDadosIdentificacao" cmd="list" service="" dataType="text" size="18" maxLength="18" width="80%" labelWidth="20%" disabled="true">
			<adsm:propertyMapping modelProperty="remetente.id" formProperty="nomeRemetente" />
			<adsm:textbox dataType="text" property="nomeRemetente" size="50" disabled="true" />
		</adsm:lookup>

		<adsm:lookup property="consignatario.id" label="consignatario" action="/vendas/manterDadosIdentificacao" cmd="list" service="" dataType="text" size="18" maxLength="18" width="80%" labelWidth="20%" disabled="true">
			<adsm:propertyMapping modelProperty="remetente.id" formProperty="nomeRemetente" />
			<adsm:textbox dataType="text" property="nomeRemetente" size="50" disabled="true" />
		</adsm:lookup>

		<adsm:textbox label="valorMercadoria" property="valorMercadoria" dataType="currency" labelWidth="20%" width="30%" disabled="true"/>

		<adsm:textbox label="valorFrete" property="valorFrete" dataType="currency" labelWidth="15%" width="35%" disabled="true"/>

		<adsm:textarea label="observacao" property="observacao" maxLength="500" columns="80" rows="3" labelWidth="20%" width="80%" disabled="true"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="registrarRecebimento"/>
			<adsm:button caption="fechar"/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid idProperty="id" property="id" selectionMode="none" scrollBars="vertical" gridHeight="122" unique="false">
		<adsm:gridColumn title="tipoDocumento" property="tipoDocumento" width="120"/>
		<adsm:gridColumn title="documentoServico" property="documentoServico" width="135"/>
		<adsm:gridColumn title="descricaoMercadoria" property="descricaoMercadoria" width="230"/>
		<adsm:gridColumn title="natureza" property="natureza" width="115"/>
		<adsm:gridColumn title="volumes" property="volumes" width="70"/>
		<adsm:gridColumn title="peso" property="peso" width="70" unit="kg" mask="###,###,##0.000"/>

		<adsm:buttonBar/>

	</adsm:grid>

</adsm:window>