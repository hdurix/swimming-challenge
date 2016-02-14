'use strict';

angular.module('swimmingchallengeApp')
    .controller('TimeslotController', function ($scope, $state, Timeslot) {

        $scope.timeslots = [];
        $scope.loadAll = function() {
            Timeslot.query(function(result) {
               $scope.timeslots = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.timeslot = {
                startTime: null,
                endTime: null,
                payed: false,
                reserved: false,
                teamName: null,
                swimmer1: null,
                swimmer2: null,
                swimmer3: null,
                swimmer4: null,
                version: null,
                id: null
            };
        };
    });
