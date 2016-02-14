'use strict';

angular.module('swimmingchallengeApp').controller('TimeslotDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Timeslot', 'User',
        function($scope, $stateParams, $uibModalInstance, entity, Timeslot, User) {

        $scope.timeslot = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            Timeslot.get({id : id}, function(result) {
                $scope.timeslot = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('swimmingchallengeApp:timeslotUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.timeslot.id != null) {
                Timeslot.update($scope.timeslot, onSaveSuccess, onSaveError);
            } else {
                Timeslot.save($scope.timeslot, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
