'use strict';

(function() {
    describe('app base test', function() {
        
        beforeEach(module('app'));
        
        var scope,
            $rootScope,
            $httpBackend,
            $location,
            $window;
            
        beforeEach(inject(function(_$rootScope_, _$location_, _$httpBackend_, _$window_) {
            scope = _$rootScope_.$new();
            $rootScope = _$rootScope_;
            $httpBackend = _$httpBackend_;
            $location = _$location_;
            $window = _$window_;
            
            $httpBackend.when('GET',/\/rest\/currentUser\?.*/).respond({
                "filial":"Porto Alegre",
                "usuario":"Melissa Lemos Azevedo",
                "empresa":"TNT Mercúrio Cargas e Encomendas Expressas S.A"
            });
            
        }));
        
        describe('ErrorInterceptorFactory', function() {
            
            beforeEach(inject(function($controller) {
                $httpBackend.when('GET',/\/js\/app\/common\/view\/templates\/app-template.html\?.*/).respond('<html></html>');
                $httpBackend.when('GET',/\/js\/app\/common\/view\/home.html\?.*/).respond('<html></html>');
                
                $controller('AppController', {
                    $scope: scope,
                    $rootScope: $rootScope
                });
            }));
            
            afterEach(function() {
                $httpBackend.verifyNoOutstandingExpectation();
                $httpBackend.verifyNoOutstandingRequest();
            });
            
            it('deve popular as variaveis de erro', function() {
                $httpBackend.expectGET(/\/rest\/config\/recursoMensagem\/getMensagens\?.*/).respond({
                    error: 'Erro no request',
                    key: 'LMS-0036'
                });
                
                $httpBackend.flush();
                
                expect($rootScope.alerts.length).toEqual(1);
                expect($rootScope.alerts[0].msg).toEqual('LMS-0036: Erro no request');
                expect($rootScope.alerts[0].type).toEqual('danger');
            });
        });
    });
})();