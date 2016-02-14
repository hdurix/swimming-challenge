'use strict';

describe('Controller Tests', function() {

    describe('Timeslot Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTimeslot, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTimeslot = jasmine.createSpy('MockTimeslot');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Timeslot': MockTimeslot,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("TimeslotDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'swimmingchallengeApp:timeslotUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
