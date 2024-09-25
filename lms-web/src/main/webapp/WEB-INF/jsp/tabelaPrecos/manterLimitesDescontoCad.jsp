<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tabelaprecos.limiteDescontoService">
	<adsm:form action="/tabelaPrecos/manterLimitesDesconto" idProperty="idLimiteDesconto"
		service="lms.tabelaprecos.limiteDescontoService.findByIdMap">
		<adsm:combobox property="divisaoGrupoClassificacao.idDivisaoGrupoClassificacao" label="divisaoGrupo"  service="lms.municipios.divisaoGrupoClassificacaoService.findByGrupoClassificacao" onlyActiveValues="true" optionLabelProperty="dsGrupoDivisao" optionProperty="idDivisaoGrupoClassificacao" boxWidth="200" labelWidth="14%" width="40%"/>
        <adsm:lookup property="filial" idProperty="idFilial" criteriaProperty="sgFilial" service="lms.municipios.filialService.findLookup" dataType="text"  label="filial" size="3" action="/municipios/manterFiliais" minLengthForAutoPopUpSearch="3" exactMatch="true" maxLength="3" labelWidth="14%" width="32%">
            <adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true"/>
        </adsm:lookup>
		
		<adsm:lookup property="funcionario" 
					 idProperty="idUsuario" 
					 criteriaProperty="nrMatricula" 
					 serializable="false" 
					 service="lms.tabelaprecos.manterLimitesDescontoAction.findLookupUsuarioFuncionario" 
					 dataType="text" 
					 label="funcionario" 
					 size="16" maxLength="16" labelWidth="14%" width="40%" 
					 action="/configuracoes/consultarFuncionariosView" 
					 exactMatch="true">
			<adsm:propertyMapping relatedProperty="funcionario.codPessoa.nome" modelProperty="nmUsuario"/>
			<adsm:propertyMapping relatedProperty="usuario.idUsuario" modelProperty="idUsuario"/>
		<adsm:textbox dataType="text" property="funcionario.codPessoa.nome" size="27" disabled="true" serializable="false"/>
		</adsm:lookup>
		<adsm:hidden property="usuario.idUsuario"/>
		

		<adsm:combobox property="parcelaPreco.idParcelaPreco" optionLabelProperty="nmParcelaPreco" optionProperty="idParcelaPreco" service="lms.tabelaprecos.parcelaPrecoService.find" onlyActiveValues="true" label="parcela" required="true" boxWidth="220" labelWidth="14%" width="32%"/>
		
		<adsm:combobox 
			property="tpTipoTabelaPreco" 
			domain="DM_TIPO_TABELA_PRECO" 
			label="tipoTabela" 
			required="true" 
			labelWidth="14%" 
			width="40%"
		/>
		
		<adsm:combobox 
			property="subtipoTabelaPreco.idSubtipoTabelaPreco" 
			optionLabelProperty="tpSubtipoTabelaPreco" 
			optionProperty="idSubtipoTabelaPreco" 
			onlyActiveValues="true" 
			service="lms.tabelaprecos.subtipoTabelaPrecoService.findByTipoSelecionadoOuTipoNuloActiveValues" 
			label="subtipoTabela" 
			required="true" 
			labelWidth="14%" 
			width="32%"
		>
		
			<adsm:propertyMapping 
				criteriaProperty="tpTipoTabelaPreco" 
				modelProperty="tpTipoTabelaPreco"
			/>
			
		</adsm:combobox>
		
		<adsm:textbox dataType="decimal" property="pcLimiteDesconto"  mask="##0.00" onchange="return onChangePcLimiteDesconto();" label="limiteDesconto" labelWidth="14%" width="40%" maxLength="5" size="6" required="true" unit="percent"/>
		<adsm:combobox property="tpIndicadorDesconto" domain="DM_INDICADOR_LIMITE_DESCONTO" label="localDesconto" labelWidth="14%" width="32%"/>
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" required="true" labelWidth="14%" width="40%"/>
		
		<adsm:lookup service="adsm.security.perfilService.findLookup" dataType="text" 
        	property="perfil" criteriaProperty="dsPerfil" idProperty="idPerfil"
        	label="perfil" minLengthForAutoPopUpSearch="1" exactMatch="false"
        	size="30" maxLength="60" labelWidth="14%" width="32%" 
        	action="/seguranca/manterPerfil">
        </adsm:lookup>		
		
		<adsm:buttonBar>
			<%--adsm:button caption="salvar" id="salvar" buttonType="storeButton" onclick="validateFormLimiteDesconto()" disabled="false"/--%>
			<adsm:storeButton disabled="false"/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
<script>
function alertLMS_30025(){alert('<adsm:label key="LMS-30025"/>');}
</script>

<script language="javascript">

function validateTab() {
	return validateFormLimiteDesconto();
}

function validateFormLimiteDesconto(){
var x = 0;
var obj3 = getElementValue("usuario.idUsuario");
var obj2 = getElementValue("divisaoGrupoClassificacao.idDivisaoGrupoClassificacao");
var obj1 = getElementValue("filial.idFilial");

	if ((obj1 == "") && (obj2 == "") && (obj3 == "")){
			x = 1;
			}
	if ((obj1 != "") && (obj2 == "") && (obj3 == "")){
			x = 1;
			}
	if ((obj1 == "") && (obj2 != "") && (obj3 == "")){
			x = 1;
			}
	if ((obj1 == "") && (obj2 == "") && (obj3 != "")){
			x = 1;
			}
    if (x == 0){
			alert('<adsm:label key="LMS-30002"/>');
			return false;
	}
	if (x == 1){
			//storeButtonScript('lms.tabelaprecos.limiteDescontoService.store', "store", document.getElementById('form_idLimiteDesconto'));
			return validateTabScript(document.forms);
	}
return false;
} 

function str2number(str)
{
    if ((str == "")||(str == null)||(str.length == 0))
    {
        return 0;
    }
    return parseFloat(str);
}

function onChangePcLimiteDesconto()
{
//alert('onChangePcLimiteDesconto');
	var value = str2number(getElementValue("pcLimiteDesconto"));
	if(value<0 || value>100)
	{
		alertLMS_30025();
		return false;
	}
	return true;
}

</script>
	</adsm:form> 
</adsm:window>
