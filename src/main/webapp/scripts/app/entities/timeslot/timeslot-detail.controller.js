'use strict';

angular.module('swimmingchallengeApp')
    .controller('TimeslotDetailController', function ($scope, $rootScope, $stateParams, entity, Timeslot, User) {
        $scope.timeslot = entity;
        $scope.load = function (id) {
            Timeslot.get({id: id}, function(result) {
                $scope.timeslot = result;
            });
        };

    });
