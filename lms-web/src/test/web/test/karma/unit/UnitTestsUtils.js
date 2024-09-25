var UnitTestsUtils = {
		
	replaceAll: function(str, find, replace) {
		return str.replace(new RegExp(find, 'g'), replace);
	},
		
	baseInit: function(){
		beforeEach(module('app'));
        
		var state = {};
            
        beforeEach(inject(function(_$rootScope_, _$location_, _$httpBackend_, _$window_, _$compile_) {
        	state.scope = _$rootScope_.$new();
        	state.$rootScope = _$rootScope_;
        	state.$httpBackend = _$httpBackend_;
        	state.$location = _$location_;
        	state.$window = _$window_;
        	state.$compile = _$compile_;
            
        	state.$httpBackend.when('GET',/\/rest\/currentUser\?.*/).respond({
                "filial":"Porto Alegre",
                "usuario":"Melissa Lemos Azevedo",
                "empresa":"TNT Merc�rio Cargas e Encomendas Expressas S.A"
            });
            
        	state.$httpBackend.when('GET',/\/rest\/config\/recursoMensagem\/getMensagens\?.*/).respond({
                "LMS-00078":"É necessário digitar no mínimo {0} caracteres.",
                "ADSM_SESSION_EXPIRED_EXCEPTION_KEY":"Sua sessão expirou!",
                "LMS-00077":"Informe uma data válida!",
                "LMS-05330":"Valor inicial maior que valor final.",
                "grid.paginacao.nenhum-registro":"<BR>A pesquisa feita pelos critérios informados não retornou nenhum registro.",
                "LMS-30017":"Nenhum resultado encontrado para o critério utilizado!"
            });
        	
        	UnitTestsUtils.mockHtmlRequest(['/js/app/common/view/templates/app-template.html','/js/app/common/view/home.html'], state, false);
            
        }));
        
        return state;
	},
	enableVerifications: function(state){
		afterEach(function() {
			state.$httpBackend.verifyNoOutstandingExpectation();
			state.$httpBackend.verifyNoOutstandingRequest();
        });
	},
	initializeController: function(controllerName, state){
		beforeEach(inject(function($controller) {
            $controller(controllerName, {
                $scope: state.scope,
                $rootScope: state.$rootScope
            });
        }));
	},
	mockHtmlRequest: function(urls, state, wrap){
		if(typeof wrap === 'undefined'){
			wrap = true;
		}
		if(typeof urls === 'string'){
			urls = [urls];
		}
		
		var callback = function(){
			for(var i=0; i<urls.length; i++){
				var url = urls[i];
				var reg = new RegExp(UnitTestsUtils.replaceAll(url, '\/', '\\/')+'\?.*');
				state.$httpBackend.when('GET',reg).respond('<html></html>');
			}
		}
		if(wrap){
			beforeEach(function() {
				callback();
	        });
		}else{
			callback();
		}
	},
	exceptGet: function(url, toBe, state, wrap){
		if(typeof wrap === 'undefined'){
			wrap = false;
		}
		
		var callback = function(){
			url = UnitTestsUtils.replaceAll(url, '\/', '\\/');
			if(url.indexOf('?') !== -1){
				url = url.replace('?', '\\?')+'\&?';
			}else{
				url += '\?.*';
			}
			var reg = new RegExp(url);
			state.$httpBackend.expectGET(reg).respond(toBe);
		}
		if(wrap){
			beforeEach(function() {
				callback();
	        });
		}else{
			callback();
		}
	}
		
}