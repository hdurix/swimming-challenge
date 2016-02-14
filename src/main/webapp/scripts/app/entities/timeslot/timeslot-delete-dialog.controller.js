'use strict';

angular.module('swimmingchallengeApp')
	.controller('TimeslotDeleteController', function($scope, $uibModalInstance, entity, Timeslot) {

        $scope.timeslot = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Timeslot.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
