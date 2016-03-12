(function () {
    'use strict';

    angular.module('swimmingchallengeApp')
        .controller('ChallengeController', TimeslotController);

    function TimeslotController(Timeslot, User) {

        var vm = this;

        vm.clear = clear;
        vm.getClass = getClass;
        vm.getTooltip = getTooltip;
        vm.loadAll = loadAll;
        vm.refresh = refresh;
        vm.saveTimeslot = saveTimeslot;
        vm.timeslotsByTime = {};
        vm.users = User.query();

        activate();

        function activate() {
            loadAll();
        }

        function loadAll() {
            Timeslot.query(function (timeslots) {
                var timeslots18 = _.filter(timeslots, function(timeslot) { return timeslot.startTime.substring(0, 2) == "18"; });
                var timeslots19 = _.filter(timeslots, function(timeslot) { return timeslot.startTime.substring(0, 2) == "19"; });
                var byTime18 = _.groupBy(timeslots18, function(timeslot) { return timeslot.startTime + ' - ' + timeslot.endTime; });
                var byTime19 = _.groupBy(timeslots19, function(timeslot) { return timeslot.startTime + ' - ' + timeslot.endTime; });
                var byLine18 = _.groupBy(timeslots18, 'line');
                var byLine19 = _.groupBy(timeslots19, 'line');
                vm.timeslotsByTime.time18 = {byTime : byTime18, byLine: byLine18};
                vm.timeslotsByTime.time19 = {byTime : byTime19, byLine: byLine19};
            });
        }

        function refresh() {
            loadAll();
            clear();
        }

        function getClass(timeslot) {
            if (timeslot.payed) {
                return "payed";
            }
            if (timeslot.reserved) {
                return "reserved";
            }
            return "free";
        }

        function getTooltip(timeslot) {
            if (timeslot.payed) {
                return "Equipe " + timeslot.teamName + " (payé)";
            }
            if (timeslot.reserved) {
                return "Equipe " + timeslot.teamName + " (réservé)";
            }
            return "Libre";
        }


        function onSaveSuccess(result) {
            $scope.$emit('swimmingchallengeApp:timeslotUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        }

        function onSaveError(result) {
            $scope.isSaving = false;
        }

        function saveTimeslot() {
            vm.isSaving = true;
            vm.currentTimeslot.reserved = true;
            Timeslot.update(vm.currentTimeslot, onSaveSuccess, onSaveError);
        }

        function clear() {
            vm.timeslot = {
                startTime: null,
                endTime: null,
                payed: false,
                reserved: false,
                teamName: null,
                swimmer1: null,
                swimmer2: null,
                swimmer3: null,
                swimmer4: null,
                line: null,
                version: null,
                id: null
            };
        }
    }
})();
