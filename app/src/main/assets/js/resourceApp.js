Ext.Loader.setPath('APlan', 'js/advanced');
Ext.Loader.setPath('Ext.ux', 'ux');
Ext.Loader.setPath('Sch', 'js/advanced/Sch');
Ext.Loader.setPath('Gnt', 'js/advanced/Gnt');
var Staticcolumns = [
    {
        xtype           : 'treecolumn',
        text            : 'Name',
        width           : 200,
        sortable        : true,
        dataIndex       : 'Name'
    }
];
Ext.require([
    "APlan.model.SchedulerResource",
    "APlan.model.SchedulerTask"
]);
Ext.application({
    name: 'Sch.examples.schedulerdependencies',
    launch: function () {

        var dependencyStore = Ext.create("Sch.data.DependencyStore", {
            autoSync                : true,
            autoLoad                : true,
            proxy                   : {
                type                : 'memory',
                reader              : { type: 'json' },
                data                : [{"From":"G2P1T2","Id":1,"To":"G2P1T3","Type":0},{"From":"G2P1T3","Id":2,"To":"G2P1T4","Type":2},{"From":"G2P1T4","Id":3,"To":"G2P1T6","Type":2},{"From":"G2P1T6","Id":4,"To":"G2P1T7","Type":2},{"From":"G2P1T7","Id":5,"To":"G2P1T8","Type":0},{"From":"G2P1T8","Id":6,"To":"G2P1T9","Type":2},{"From":"G2P1T9","Id":7,"To":"G2P1T10","Type":2},{"From":"G2P2T2","Id":8,"To":"G2P2T3","Type":2},{"From":"G2P2T3","Id":9,"To":"G2P2T4","Type":2},{"From":"G2P2T4","Id":10,"To":"G2P2T6","Type":2},{"From":"G2P2T6","Id":11,"To":"G2P2T7","Type":2},{"From":"G2P2T7","Id":12,"To":"G2P2T8","Type":2},{"From":"G2P2T8","Id":13,"To":"G2P2T9","Type":2},{"From":"G2P2T9","Id":14,"To":"G2P2T10","Type":2},{"From":"G2P2T10","Id":15,"To":"G2P2T11","Type":2},{"From":"G2P3T4","Id":16,"To":"G2P3T6","Type":2},{"From":"G2P3T6","Id":17,"To":"G2P3T7","Type":2},{"From":"G2P3T7","Id":18,"To":"G2P3T8","Type":2},{"From":"G2P3T8","Id":19,"To":"G2P3T9","Type":2},{"From":"G2P3T9","Id":20,"To":"G2P3T10","Type":2},{"From":"G2P4T4","Id":21,"To":"G2P4T6","Type":2},{"From":"G2P4T6","Id":22,"To":"G2P4T7","Type":2},{"From":"G2P4T7","Id":23,"To":"G2P4T8","Type":2},{"From":"G2P4T8","Id":24,"To":"G2P4T9","Type":2},{"From":"G2P4T9","Id":25,"To":"G2P4T10","Type":2},{"From":"G2P5T2","Id":26,"To":"G2P5T3","Type":2},{"From":"G2P5T3","Id":27,"To":"G2P5T4","Type":2},{"From":"G2P5T4","Id":28,"To":"G2P5T6","Type":2},{"From":"G2P5T6","Id":29,"To":"G2P5T7","Type":2},{"From":"G2P5T7","Id":30,"To":"G2P5T8","Type":2},{"From":"G2P5T8","Id":31,"To":"G2P5T9","Type":2},{"From":"G2P5T9","Id":32,"To":"G2P5T10","Type":2},{"From":"G2P5T10","Id":33,"To":"G2P5T11","Type":2},{"From":"G2P6T2","Id":34,"To":"G2P6T3","Type":2},{"From":"G2P6T3","Id":35,"To":"G2P6T4","Type":2},{"From":"G2P6T4","Id":36,"To":"G2P6T6","Type":2},{"From":"G2P6T6","Id":37,"To":"G2P6T7","Type":0},{"From":"G2P6T7","Id":38,"To":"G2P6T8","Type":2},{"From":"G2P6T8","Id":39,"To":"G2P6T9","Type":2},{"From":"G2P6T9","Id":40,"To":"G2P6T10","Type":2},{"From":"G2P6T10","Id":41,"To":"G2P6T11","Type":2},{"From":"G2P7T2","Id":42,"To":"G2P7T3","Type":2},{"From":"G2P7T3","Id":43,"To":"G2P7T4","Type":2},{"From":"G2P7T4","Id":44,"To":"G2P7T7","Type":2},{"From":"G2P7T7","Id":45,"To":"G2P7T8","Type":2},{"From":"G2P7T8","Id":46,"To":"G2P7T9","Type":2},{"From":"G2P7T9","Id":47,"To":"G2P7T10","Type":2},{"From":"G2P7T10","Id":48,"To":"G2P7T11","Type":2},{"From":"G6P1T8","Id":49,"To":"G6P1T9","Type":2},{"From":"G6P1T9","Id":50,"To":"G6P1T10","Type":2},{"From":"G6P1T12","Id":51,"To":"G6P1T13","Type":2},{"From":"G6P1T13","Id":52,"To":"G6P1T14","Type":2},{"From":"G6P1T14","Id":53,"To":"G6P1T15","Type":2},{"From":"G6P1T15","Id":54,"To":"G6P1T16","Type":2},{"From":"G6P1T16","Id":55,"To":"G6P1T17","Type":2},{"From":"G6P1T21","Id":56,"To":"G6P1T22","Type":2},{"From":"G6P1T17","Id":57,"To":"G6P1T23","Type":2},{"From":"G6P1T23","Id":58,"To":"G6P1T24","Type":2},{"From":"G6P1T24","Id":59,"To":"G6P1T25","Type":2},{"From":"G6P1T25","Id":60,"To":"G6P1T26","Type":2},{"From":"G6P1T26","Id":61,"To":"G6P1T27","Type":2},{"From":"G6P1T28","Id":62,"To":"G6P1T29","Type":2},{"From":"G6P1T10","Id":63,"To":"G6P1T37","Type":2},{"From":"G6P1T37","Id":64,"To":"G6P1T42","Type":2},{"From":"G6P2T8","Id":65,"To":"G6P2T9","Type":2},{"From":"G6P2T42","Id":66,"To":"G6P2T12","Type":2},{"From":"G6P2T12","Id":67,"To":"G6P2T13","Type":0},{"From":"G6P2T12","Id":68,"To":"G6P2T14","Type":0},{"From":"G6P2T16","Id":69,"To":"G6P2T17","Type":2},{"From":"G6P2T17","Id":70,"To":"G6P2T18","Type":2},{"From":"G6P2T9","Id":71,"To":"G6P2T37","Type":2},{"From":"G6P2T37","Id":72,"To":"G6P2T42","Type":2},{"From":"G8P1T2","Id":73,"To":"G8P1T3","Type":2},{"From":"G8P1T3","Id":74,"To":"G8P1T4","Type":2},{"From":"G8P1T4","Id":75,"To":"G8P1T6","Type":2},{"From":"G8P1T6","Id":76,"To":"G8P1T7","Type":2},{"From":"G8P1T7","Id":77,"To":"G8P1T8","Type":2},{"From":"G8P1T8","Id":78,"To":"G8P1T9","Type":2},{"From":"G8P1T9","Id":79,"To":"G8P1T10","Type":2},{"From":"G8P2T2","Id":80,"To":"G8P2T3","Type":2},{"From":"G8P2T3","Id":81,"To":"G8P2T4","Type":2},{"From":"G8P2T4","Id":82,"To":"G8P2T6","Type":2},{"From":"G8P2T6","Id":83,"To":"G8P2T7","Type":2},{"From":"G8P2T7","Id":84,"To":"G8P2T8","Type":2},{"From":"G8P2T8","Id":85,"To":"G8P2T9","Type":2},{"From":"G8P2T9","Id":86,"To":"G8P2T10","Type":2},{"From":"G8P2T10","Id":87,"To":"G8P2T11","Type":2},{"From":"G8P3T2","Id":88,"To":"G8P3T3","Type":2},{"From":"G8P3T3","Id":89,"To":"G8P3T4","Type":2},{"From":"G8P3T4","Id":90,"To":"G8P3T6","Type":2},{"From":"G8P3T6","Id":91,"To":"G8P3T7","Type":0},{"From":"G8P3T7","Id":92,"To":"G8P3T8","Type":2}]
            }
        });

        var resourceStore = Ext.create("Sch.data.ResourceTreeStore", {
            autoLoad                : true,
            model                   : "APlan.model.SchedulerResource",
            rootProperty            : { loaded : true, expanded: true },
            proxy                   : {
                type                : 'memory',
                reader              : {
                    type            : 'json',
                    //rootProperty    : 'resources'
                },
                data                :[
                        {"Id": "1", "Name": "Res1", "parentId": null, "leaf": true},
                        {"Id": "2", "Name": "Res2", "parentId": null, "leaf": true},
                        {"Id": "3", "Name": "Android", "parentId": null, "leaf": false,
                            "children": [
                                {"Id": "4", "Name": "Nileshbhai", "parentId": "3", "leaf": true}
                            ]
                        }
                    ]
            }
        });

        var assignmentStore = Ext.create("Sch.data.AssignmentStore", {
            autoSync                : true,
            autoLoad                : true,
            proxy                   : {
                type                : 'memory',
                reader              : {  type : 'json' },
                data                : [
                    {"Id": "1", "ResourceId": "1", "TaskId": "1"},
                    {"Id": "2", "ResourceId": "2", "TaskId": "1"},
                    {"Id": "3", "ResourceId": "3", "TaskId": "2"},
                    {"Id": "4", "ResourceId": "3", "TaskId": "4"},
                ]
            },
            resourceStore           : resourceStore
        });

        var eventStore = Ext.create("Sch.data.EventStore", {
            calendarManager         : new Sch.data.Calendar(),
            model                   : "APlan.model.SchedulerTask",
            id                      : 'taskStore',
            /*resourceStore           : resourceStore,
             assignmentStore         : assignmentStore,*/
            /*dependencyStore         : dependencyStore,*/
            autoSync                : false,
            pageSize                : 100,
            trailingBufferZone      : 50,
            leadingBufferZone       : 50,
            numFromEdge             : 5,
            purgePageCount          : 0,
            scrollToLoadBuffer      : 100,
            rootVisible             : false,
            sortOnLoad              : false,
            forceFit                : true,
            autoLoad                : true,
            expanded                : false,
            proxy                   : {
                type                : 'memory',
                reader              : {
                    type            : 'json'
                },
                data                : [
                    {"Id": "1", "ResourceId":"1", "Name": "Task1", "StartDate": "/Date(1475154000000+0100)/", EndDate: "/Date(1479474000000+0100)/"},
                    {"Id": "2", "ResourceId":"2", "Name": "Task1", "StartDate": "/Date(1475154000000+0100)/", EndDate: "/Date(1479474000000+0100)/"},
                    {"Id": "3", "ResourceId":"3", "Name": "Task1", "StartDate": "/Date(1475154000000+0100)/", EndDate: "/Date(1479474000000+0100)/"},
                    {"Id": "4", "ResourceId":"4", "Name": "Task1", "StartDate": "/Date(1475154000000+0100)/", EndDate: "/Date(1479474000000+0100)/"},
                ]
            }
        });

        var ganttPanel = Ext.create('Sch.panel.SchedulerTree', {
            id                      : "gv",
            model                   : "APlan.model.SchedulerTask",
            eventStore              : eventStore,
            resourceStore           : resourceStore,
            assignmentStore         : assignmentStore,
            dependencyStore         : dependencyStore,
            height                  : Ext.getBody().getViewSize().height,
            width                   : Ext.getBody().getViewSize().width,
            headerHeight            : 50,
            layout                  : 'border',
            mask                    : true,
            viewPreset              : 'weekAndDayLetter',
            startDate               : new Date(2016, 07, 06),
            endDate                 : new Date(2016, 07, 25),
            infiniteScroll          : true,
            readOnly                : true,
            animate                 : false,
            shiftIncrement          : 1,
            multiSelect             : true,
            /*highlightWeekends       : true,*/
            showTodayLine           : true,
            loadMask                : true,
            enableDependencyDragDrop: true,
            snapToIncrement         : true,
            showExactResizePosition : true,
            title                   : 'Task',
            preventHeader           : true,
            stateId                 : 'gantt',
            reserveScrollbar        : true,
            region                  : 'center',
            columns                 : Staticcolumns,
            schedulerConfig         : { region : 'center' },
            viewConfig              : {
                stripeRows          : true,
                style               : { overflow: 'auto', overflowX: 'hidden' },
                emptyText           : 'No data'
            },
            lockedGridConfig        : {
                width               : 500,
                collapsible         : true,
                header              : false,
                split               : true,
                region              : 'west',
                scroll              : true
            },
            lockedViewConfig        : {
                plugins             : {  ptype : 'treeviewdragdrop' }
            },
            calendar                : null
        });
        ganttPanel.setRowHeight(50);
        ganttPanel.render(Ext.getBody());
    }
});
