(function () {
    'use strict';

    angular.module('swimmingchallengeApp')
        .controller('TimeslotController', TimeslotController);

    function TimeslotController($state, Timeslot) {

        var vm = this;

        vm.clear = clear;
        vm.loadAll = loadAll;
        vm.refresh = refresh;
        vm.timeslots = [];

        activate();

        function activate() {
            loadAll();
        }

        function loadAll() {
            Timeslot.query(function (result) {
                vm.timeslots = result;
            });
        }

        function refresh() {
            loadAll();
            clear();
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
                swimmer5: null,
                line: null,
                version: null,
                id: null
            };
        }
    }
})();
