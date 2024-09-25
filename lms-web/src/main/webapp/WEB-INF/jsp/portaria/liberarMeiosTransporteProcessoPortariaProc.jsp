<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/portaria/liberarMeiosTransporteProcessoPortaria">
		
		<adsm:lookup labelWidth="17%"  service="" dataType="text" property="filialId" criteriaProperty="pessoa.nome" label="filial" action="/municipios/manterFiliais" size="15" maxLength="14" width="17%">
                  <adsm:propertyMapping modelProperty="nome" formProperty="nomeEmpresa" />
        </adsm:lookup>
        <adsm:textbox dataType="text" property="nomeEmpresa" size="50" disabled="true" required="true" width="66%"/>		

		<adsm:lookup service="" dataType="text" property="auditoriaCarga.id" criteriaProperty="auditoriaCarga.codigo" label="numeroRegistroAuditoria" size="30" maxLength="30" width="83%" labelWidth="17%" action="/portaria/manterRegistrosAuditoriasCargas" cmd="list" required="true" cellStyle="style:vertical-align:bottom" >
        	<adsm:propertyMapping modelProperty="descricao" formProperty="auditoriaCarga"/> 
        </adsm:lookup>

		<adsm:section caption="controleCarga" />	

		<adsm:textbox dataType="text" property="controleCarga" label="controleCarga" size="30" labelWidth="17%" width="33%" required="true" disabled="true" />
		
		<adsm:textbox dataType="text" property="destino" size="20" maxLength="20" label="filialDestino2" labelWidth="17%" width="33%" disabled="true"/>	
		
		<adsm:textbox dataType="text" property="placa" label="identificacaoMeioTransporte" size="15" labelWidth="17%" width="14%" disabled="true" cellStyle="vertical-Align:bottom" />
        <adsm:textbox dataType="text" property="frota" size="18" disabled="true" width="19%" cellStyle="vertical-Align:bottom" required="true" />

		<adsm:textbox dataType="text" property="placa" label="identificacaoDoSemiReboque" size="15" labelWidth="17%" width="14%" disabled="true" cellStyle="vertical-Align:bottom" />
        <adsm:textbox dataType="text" property="frota" size="18" disabled="true" width="19%" cellStyle="vertical-Align:bottom" />

		<adsm:textbox dataType="text" property="idmotorista" label="motorista" size="15" labelWidth="17%" width="14%" disabled="true" cellStyle="vertical-Align:bottom" />
        <adsm:textbox dataType="text" property="motorista" size="18" disabled="true" width="26%" cellStyle="vertical-Align:bottom" required="true" />
			
		<adsm:section caption="informacoesLiberacao" />	
		<adsm:textarea property="motivo" label="motivoDaLiberacao" maxLength="200" labelWidth="17%" width="83%" rows="2" columns="100" required="true" />
		<adsm:textbox dataType="dateTime" property="dataHoraLiberacao" label="dataHoraLiberacao" labelWidth="17%" width="33%" required="true" disabled="true" />
		<adsm:textbox dataType="text" property="usuarioLiberacao" label="usuarioLiberacao" size="22" maxLength="50" labelWidth="17%" width="33%" required="true" disabled="true" />
				
		<adsm:buttonBar>
			<adsm:button caption="liberar"/>
			<adsm:button caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   