Ext.Loader.setConfig({ enabled: true, disableCaching: false });
Ext.Loader.setPath('Ext.ux', 'js/ux');
Ext.Loader.setPath('Sch', 'js/Sch');
Ext.tip.QuickTipManager.init();
Ext.define("APlan.view.GanttPanel", {
    extend: "Gnt.panel.Gantt",

    requires: [
        'APlan.model.Dependency',
        'APlan.model.Task',
        'APlan.store.Task',
        'APlan.store.Resource',
        'APlan.store.Dependency',
        'APlan.store.Assignment',
        'Gnt.plugin.TaskEditor',
        'Gnt.column.ResourceAssignment',
        'Sch.plugin.TreeCellEditing',
        'Gnt.plugin.DependencyEditor',
        'Ext.layout.container.*',
        'Ext.resizer.Splitter',
        'Ext.ux.statusbar.StatusBar',
        'Ext.ux.window.Notification',
        'Ext.grid.plugin.BufferedRenderer',
        'Ext.form.Panel',
        'Gnt.plugin.Printable'
    ],
    mixins: [
        'Sch.mixin.FilterableTreeView'
    ],
    initComponent: function () {
        // status bar function for show where mouse is
        var loadFn = function (task, statusBar) {
            statusBar = Ext.getCmp(statusBar);
            statusBar.setStatus(task);
        };
        /*var assignmentEditor = Ext.create('Gnt.widget.AssignmentCellEditor', {
            assignmentStore: this.assignmentStore,
            resourceStore: this.resourceStore
        });*/
        var clock = Ext.create('Ext.toolbar.TextItem', { text: Ext.Date.format(new Date(), 'g:i:s A') });
        //Combobx for Priority
        var combo = new Ext.form.ComboBox({
            store: new Ext.data.ArrayStore({
                id: 0,
                fields: [
                    'Id',
                    'displayText'
                ],
                data: [[0, 'Low'], [1, 'Normal'], [2, 'High']]
            }),
            triggerAction: 'all',
            mode: 'local',
            valueField: 'Id',
            displayField: 'displayText'
        });

        var g = this; var slider;
        Ext.apply(this, {

            taskBodyTemplate: '<div class="sch-gantt-progress-bar" style="width:{progressBarWidth}px;{progressBarStyle}" unselectable="on">' +
            '<span class="sch-gantt-progress-bar-label">{[Math.round(values.percentDone)]}%</span>' +
            '</div>',
            plugins: [
                Ext.create("Ext.grid.plugin.BufferedRenderer", {
                    pageSize: 100,
                    trailingBufferZone: 50,
                    leadingBufferZone: 50,
                    numFromEdge: 5,
                    purgePageCount: 0,
                    scrollToLoadBuffer: 100,
                }),
                Ext.create("Gnt.plugin.TaskContextMenu"),
                Ext.create("Gnt.plugin.DependencyEditor", {
                    hideOnBlur: true
                }),
                //gnt plugin for task editor in grid
                Ext.create('Gnt.plugin.TaskEditor'),
                 {
                     ptype: 'scheduler_lines',
                     innerTpl: '<span class="line-text">{Text}</span>',
                     showHeaderElements: true,
                     store: new Ext.data.JsonStore({
                         fields: ['Date', 'Text', 'Cls'],
                         data: [
                             {
                                 Date: new Date(2014, 3, 30),
                                 Text: 'Project kickoff',
                                 Cls: 'kickoff' // A CSS class
                             }
                         ]
                     })
                 },

                new Gnt.plugin.Printable({
                    printRenderer: function (task, tplData) {
                        if (task.isMilestone()) {
                            return;
                        } else if (task.isLeaf()) {
                            var availableWidth = tplData.width - 4,
                                progressWidth = Math.floor(availableWidth * task.get('PercentDone') / 100);

                            return {
                                // Style borders to act as background/progressbar
                                progressBarStyle: Ext.String.format('width:{2}px;border-left:{0}px solid #7971E2;border-right:{1}px solid #E5ECF5;', progressWidth, availableWidth - progressWidth, availableWidth)
                            };
                        } else {
                            var availableWidth = tplData.width - 2,
                                progressWidth = Math.floor(availableWidth * task.get('PercentDone') / 100);

                            return {
                                // Style borders to act as background/progressbar
                                progressBarStyle: Ext.String.format('width:{2}px;border-left:{0}px solid #FFF3A5;border-right:{1}px solid #FFBC00;', progressWidth, availableWidth - progressWidth, availableWidth)
                            };
                        }
                    },

                    beforePrint: function (sched) {
                        var v = sched.getSchedulingView();
                        this.oldRenderer = v.eventRenderer;
                        v.eventRenderer = this.printRenderer;
                    },

                    afterPrint: function (sched) {
                        var v = sched.getSchedulingView();
                        v.eventRenderer = this.oldRenderer;
                    }
                }),

                Ext.create('Sch.plugin.TreeCellEditing', {
                    clicksToEdit: 2,
                    listeners: {
                        edit: function () {
                            g.assignmentStore.sync();
                        },
                        //beforeedit: function (e) {
                        //    var me = this;
                        //    var tasks = this.grid.getSelectionModel().selected;
                        //    if (tasks.length > 0) {
                        //        var rawId = tasks.items[0].data.Id;
                        //        $.ajax({
                        //            type: 'GET',
                        //            url: "Tasks/CheckPermission?rawId=" + rawId,
                        //            autoSync: true,
                        //            autoLoad: true,
                        //            success: function (response) {
                        //                if (response == "Change") {
                        //                }
                        //                if (response == "Read") {
                        //                    me.getEditor().cancelEdit();
                        //                    return false;
                        //                }
                        //                if (response == "Standard") {
                        //                }
                        //                if (response == "Create") {
                        //                }
                        //            }
                        //        });
                        //    }
                        //}
                    }
                }),
                this.depEditor = new Gnt.plugin.DependencyEditor({
                    showLag: true,
                    constrain: true,
                    buttons: [
                        {
                            text: 'Ok',
                            scope: this,
                            handler: function () {
                                var formPanel = g.depEditor;
                                formPanel.getForm().updateRecord(formPanel.dependencyRecord);
                                g.depEditor.collapse();
                            }
                        },
                        {
                            text: 'Cancel',
                            scope: this,
                            handler: function () {
                                g.depEditor.collapse();
                            }
                        },
                        {
                            text: 'Delete',
                            scope: this,
                            handler: function () {
                                var formPanel = g.depEditor,
                                    record = g.depEditor.dependencyRecord;
                                g.dependencyStore.remove(record);
                                formPanel.collapse();
                            }
                        }
                    ]
                })

            ],

            layout: 'border',

            //Left side grid area
            lockedGridConfig: {
                width: $(window).width() / 3,
                collapsible: true,
                split: true,
                region: 'west',
                scroll: true,
                listeners: {
                    itemexpand: function () {
                        var grid = Ext.ComponentQuery.query("#gv")[0];
                        var firstColumnHeaderId = grid.columns[0].id;
                        var secondColumnHeaderId = grid.columns[1].id;
                        var lockedDiv = $("#gv-locked-body").contents("div").attr("id");
                        $(document).ready(function () {
                            var scroll = parseInt($('#' + lockedDiv).scrollLeft());
                            $('#' + firstColumnHeaderId).css({
                                'left': $(this).scrollLeft() + 0,
                                'position': 'relative',
                                'zIndex': 200001
                            });
                            $('#' + secondColumnHeaderId).css({
                                'left': $(this).scrollLeft() + 30,
                                'position': 'relative',
                                'zIndex': 200001
                            });
                            $('.x-grid-cell-first').css({
                                'left': $(this).scrollLeft() + scroll
                            });
                            $('.x-grid-cell-treecolumn').css({
                                'left': $(this).scrollLeft() + scroll
                            });
                        });
                    },
                    itemcollapse: function () {
                        var grid = Ext.ComponentQuery.query("#gv")[0];
                        var firstColumnHeaderId = grid.columns[0].id;
                        var secondColumnHeaderId = grid.columns[1].id;
                        var lockedDiv = $("#gv-locked-body").contents("div").attr("id");
                        $(document).ready(function () {
                            var scroll = parseInt($('#' + lockedDiv).scrollLeft());
                            $('#' + firstColumnHeaderId).css({
                                'left': $(this).scrollLeft() + scroll,
                                'position': 'relative',
                                'zIndex': 200001
                            });
                            $('#' + secondColumnHeaderId).css({
                                'left': $(this).scrollLeft() + 30 + scroll,
                                'position': 'relative',
                                'zIndex': 200001
                            });
                            $('.x-grid-cell-first').css({
                                'left': $(this).scrollLeft() + scroll
                            });
                            $('.x-grid-cell-treecolumn').css({
                                'left': $(this).scrollLeft() + scroll
                            });
                        });
                    }
                }
            },

            //Right side grid area
            schedulerConfig: {
                region: 'center',
                collapsible: true
            },

            lockedViewConfig: {
                plugins: {
                    ptype: 'treeviewdragdrop'
                }
            },
            // Tooltip template for show tooltip on task with startdate, enddate, percentage Done
            tooltipTpl: new Ext.XTemplate(
               '<strong class="tipHeader">{Name}</strong>' +
                '<table class="taskTip">' +
                '<tr><td>Start:</td> <td align="right">{[values._record.getDisplayStartDate("y-m-d")]}</td></tr>' +
                '<tr><td>End:</td> <td align="right">{[values._record.getDisplayEndDate("y-m-d")]}</td></tr>' +
                '<tr><td>Progress:</td><td align="right">{[ Math.round(values.PercentDone)]}%</td></tr>' +
                '</table>'
           ),
            eventRenderer: function (task, taskRecord) {
                if (task.get('Color')) {
                    var style = Ext.String.format('background-color: #{0};border-color:#{0}', task.get('Color'));

                    return {
                        ctcls: taskRecord.get('Id')
                    };
                }
            },
            // Setup your static columns
            columns: [
                {
                    header: 'No',
                    name: 'SequenceNumber',
                    type: 'string',
                    id:  "NoId",
                    order: 1,
                    tdCls: 'nocell',
                    sortable: false,
                    width: 30,
                    locked: true,
                    renderer: function (e, t, n) {
                        return n.getSequenceNumber();
                    }

                },
                {
                    xtype: 'namecolumn',
                    header: 'Tasks',
                    dataIndex: 'Name',
                    width: 150,
                    id:  "NameId",
                    tdCls: 'namecell',
                    sortable: false,
                    listeners: {
                        mouseover: function () {
                            loadFn('Task Name', 'basic-statusbar');
                        }
                    },
                    renderer: function (v, m, r) {
                        if (r.get('IsLeaf')) {
                            m.css = 'task';
                        } else {
                            m.css = 'parent';
                        }
                        if (r.data.Object == "0") {
                            return r.data.Name;
                        }
                        return r.data.Name + ' (' + r.data.Object + ')';
                    }
                },
                {
                    header: 'Status',
                    sortable: false,
                    id:  "StatusId",
                    renderer: function (v, m, r) {
                        var returnData = [];
                        if (r.data.isCancelled == true && r.data.Pro_ID != 0) {
                            return '<img src="Ribbon/images/Icons/12_task_canceled_16.png"> ';
                        }
                        if (r.data.isPassive == true && r.data.Pro_ID != 0) {
                            return '<img src="Ribbon/images/Icons/11_task_passive_16.png"> ';
                        }
                        if (r.data.Completed != null && r.data.Pro_ID != 0 && r.data.isPassive != true && r.data.isCancelled != true) {
                            returnData.push('Completed');
                        }
                        if (r.data.Completed == null && r.data.LateFinish != null && r.data.EndDate > r.data.LateFinish && r.data.Pro_ID != 0 && r.data.isPassive != true && r.data.isCancelled != true) {
                            returnData.push('LimitOverrun');
                        }

                        if (r.data.isPassive != true && r.data.isCancelled != true && r.data.Pro_ID != 0) {
                            if (r.data.Req_Effort > 0 && ((parseFloat(r.data.Work_progn) / parseFloat(r.data.Req_Effort) * 100) - 100).toFixed(1) > 10.0) {
                                returnData.push('CapacityTooHigh');
                            } if (((parseFloat(r.data.Work_progn) / parseFloat(r.data.Req_Effort) * 100) - 100).toFixed(1) < -10.0) {
                                returnData.push('CapacityTooLow');
                            }
                            if (r.data.EndDate != null && r.data.End_progn > r.data.EndDate) {
                                returnData.push('DeadlineOverrun');
                            }
                            if (r.data.Reshow_Date != null) {
                                returnData.push('ReshowDate');
                            }
                        }

                        var today = new Date();
                        var dd = today.getDate();
                        var mm = today.getMonth() + 1;

                        var yyyy = today.getFullYear();
                        if (dd < 10) {
                            dd = '0' + dd;
                        }
                        if (mm < 10) {
                            mm = '0' + mm;
                        }
                        today = dd + '/' + mm + '/' + yyyy;
                        if (r.data.EndDate != null && r.data.StartDate != null) {
                            var flag1 = 0;
                            var flag2 = 0;

                            if (r.data.EndDate > new Date()) {
                                flag1 = 1;
                            } else {
                                flag1 = 0;
                            }

                            if (r.data.StartDate > new Date()) {
                                flag2 = 1;
                            } else {
                                flag2 = 0;
                            }
                            if (flag1 == 0 && r.data.Pro_ID != 0 && r.data.isPassive != true && r.data.isCancelled != true) {
                                if (r.data.Total_Cost_Progn - r.data.Total_Cost > 0) {
                                    returnData.push('CostsOverrun');
                                }
                            }
                            //StartDate smaller, EndDate smaller than Today's Date
                            if (flag2 == 0 && flag1 == 0 && r.data.Completed == null && r.data.Pro_ID != 0) {
                                returnData.push('Overrun');
                            }
                        }
                        else {
                            if (r.data.Total_Cost_Progn - r.data.Total_Cost > 0) {
                                returnData.push('CostsOverrun');
                            }
                        }
                        var a = '';
                        for (var i = 0; i < returnData.length; i++) {
                            if (returnData[i] == 'Completed') {
                                a = a + '<img src="Ribbon/images/Icons/10_check_16.png">';
                            }
                            if (returnData[i] == 'CostsOverrun') {
                                a = a + '<img src="Ribbon/images/CostsOverrun.png">';
                            }
                            if (returnData[i] == 'DeadlineOverrun') {
                                a = a + '<img src="Ribbon/images/FinalDeadLine.png">';
                            }
                            if (returnData[i] == 'CapacityTooLow') {
                                a = a + '<img src="Ribbon/images/CapicitytoLow.png">';
                            }
                            if (returnData[i] == 'CapacityTooHigh') {
                                a = a + '<img src="Ribbon/images/CapicitytoHigh.png">';
                            }
                            if (returnData[i] == 'Overrun') {
                                a = a + '<img src="Ribbon/images/Icons/04_task_overdue_16.png">';
                            }
                            if (returnData[i] == 'ReshowDate') {
                                a = a + '<img src="Ribbon/images/Icons/19_warning_16.png">';
                            }
                            if (returnData[i] == 'LimitOverrun') {
                                a = a + '<img src="Ribbon/images/Icons/32_Limit_16.png">';
                            }
                        }
                        return a;
                    },
                    editor: new Ext.form.DateField({
                        allowBlank: false,
                        format: 'm/d/y'
                    }),
                    width: 50,
                    listeners: {
                        mouseover: function () {
                            loadFn('Status', 'basic-statusbar');
                        }
                    }
                },
                {
                    header: 'Traffic Light',
                    id:  "TrafficLightId",
                    sortable: false,
                    renderer: function (v, m, r) {
                        var returnData = [];
                        if ((r.data.isPassive == true || r.data.isCancelled == true) && r.data.Pro_ID != 0) {
                            return '';
                        }
                        else if (r.data.isCriticalTask == true || r.data.isClarificationRequired == true) {
                            return '<img src="Ribbon/images/Icons/14_trafficlight_yellow_16_2.png">';
                        }
                        else if (r.data.Completed != null) {
                            returnData.push('Completed'); // No Status
                        }
                        else if (r.data.LateFinish != null && r.data.EndDate > r.data.LateFinish) {
                            returnData.push('Overrun'); // RedStatus
                        }

                        var today = new Date();
                        var dd = today.getDate();
                        var mm = today.getMonth() + 1;

                        var yyyy = today.getFullYear();
                        if (dd < 10) {
                            dd = '0' + dd;
                        }
                        if (mm < 10) {
                            mm = '0' + mm;
                        }
                        today = dd + '/' + mm + '/' + yyyy;
                        if (r.data.EndDate != null && r.data.StartDate != null) {
                            var flag1 = 0;
                            var flag2 = 0;

                            if (r.data.EndDate > new Date()) {
                                flag1 = 1;
                            } else {
                                flag1 = 0;
                            }

                            if (r.data.StartDate > new Date()) {
                                flag2 = 1;
                            } else {
                                flag2 = 0;
                            }
                            if (flag1 == 1 && flag2 == 0 && r.data.Pro_ID != 0 && r.data.isPassive != true && r.data.isCancelled != true) {
                                if (r.data.Req_Effort > 0 && ((parseFloat(r.data.Work_progn) / parseFloat(r.data.Req_Effort) * 100) - 100).toFixed(1) > -30.0 && ((parseFloat(r.data.Work_progn) / parseFloat(r.data.Req_Effort) * 100) - 100).toFixed(1) < -10.0) {
                                    returnData.push('CapacityTooHigh'); //capacitytoolow with YellowStatus
                                } else if (r.data.Req_Effort > 0 && ((parseFloat(r.data.Work_progn) / parseFloat(r.data.Req_Effort) * 100) - 100).toFixed(1) > 10.0) {
                                    returnData.push('CapacityTooHigh'); // with YellowStatus
                                } else if (r.data.Req_Effort > 0 && ((parseFloat(r.data.Work_progn) / parseFloat(r.data.Req_Effort) * 100) - 100).toFixed(1) < -30.0) {
                                    returnData.push('CapacityTooLow'); // with RedStatus
                                } else if (r.data.Total_Cost_Progn - r.data.Total_Cost > 0) {
                                    returnData.push('CostsOverrun'); // with YellowStatus
                                } else if (r.data.EndDate != null && r.data.End_progn > r.data.EndDate) {
                                    returnData.push('DeadlineOverrun'); // with RedStatus
                                }
                                else if (r.data.LateFinish != null && r.data.LateFinish < r.data.EndDate) {
                                    returnData.push('LimitOverrun'); // with RedStatus
                                }
                                else if (r.data.isPassive != true && r.data.isCancelled != true && r.data.Pro_ID != 0 && r.data.completed == null) {
                                    returnData.push('GreenStatus'); // with GreenStatus
                                }
                            } else if (flag1 == 0 && flag2 == 0 && r.data.Pro_ID != 0 && r.data.isPassive != true && r.data.isCancelled != true) {
                                if (r.data.Req_Effort > 0 && r.data.Req_Effort > 0 && ((parseFloat(r.data.Work_progn) / parseFloat(r.data.Req_Effort) * 100) - 100).toFixed(1) > -30.0 && ((parseFloat(r.data.Work_progn) / parseFloat(r.data.Req_Effort) * 100) - 100).toFixed(1) < -10.0) {
                                    returnData.push('CapacityTooLow'); //capacitytoolow with RedStatus
                                } else if (r.data.Req_Effort > 0 && ((parseFloat(r.data.Work_progn) / parseFloat(r.data.Req_Effort) * 100) - 100).toFixed(1) > 10.0) {
                                    returnData.push('CapacityTooLow'); // with RedStatus
                                } else if (r.data.Req_Effort > 0 && ((parseFloat(r.data.Work_progn) / parseFloat(r.data.Req_Effort) * 100) - 100).toFixed(1) < -30.0) {
                                    returnData.push('CapacityTooLow'); // with RedStatus
                                } else if (r.data.Total_Cost_Progn - r.data.Total_Cost > 0) {
                                    returnData.push('CapacityTooLow'); // with RedStatus
                                } else if (r.data.EndDate != null && r.data.End_progn > r.data.EndDate) {
                                    returnData.push('Overrun'); //with RedStatus
                                }
                                else if (r.data.LateFinish != null && r.data.LateFinish < r.data.EndDate) {
                                    returnData.push('LimitOverrun'); //with RedStatus
                                }
                            }
                        }
                        for (var i = 0; i < returnData.length; i++) {
                            if (returnData[i] == 'Completed') {
                                return '';
                            }
                            if (returnData[i] == 'CostsOverrun') {
                                return '<img src="Ribbon/images/Icons/14_trafficlight_yellow_16_2.png">';
                            }
                            if (returnData[i] == 'DeadlineOverrun') {
                                return '<img src="Ribbon/images/Icons/14_trafficlight_yellow_16_2.png">';
                            }
                            if (returnData[i] == 'CapacityTooLow') {
                                return '<img src="Ribbon/images/Icons/13_trafficlight_red_16_2.png">';
                            }
                            if (returnData[i] == 'CapacityTooHigh') {
                                return '<img src="Ribbon/images/Icons/14_trafficlight_yellow_16_2.png">';
                            }
                            if (returnData[i] == 'Overrun') {
                                return '<img src="Ribbon/images/Icons/13_trafficlight_red_16_2.png">';
                            }
                            if (returnData[i] == 'GreenStatus') {
                                return '<img src="Ribbon/images/Icons/15_trafficlight_green_16_2.png">';
                            }
                            if (returnData[i] == 'YellowStatus') {
                                return '<img src="Ribbon/images/Icons/14_trafficlight_yellow_16_2.png">';
                            }
                            if (returnData[i] == 'LimitOverrun') {
                                return '<img src="Ribbon/images/Icons/13_trafficlight_red_16_2.png">';
                            }
                        }
                    },
                    editor: new Ext.form.DateField({
                        allowBlank: false,
                        format: 'm/d/y'
                    }),
                    width: 80,
                    listeners: {
                        mouseover: function () {
                            loadFn('Traffic Lights', 'basic-statusbar');
                        }
                    }
                },
                {
                    header: 'Assigned Resources',
                    id:  "AssignedResourcesId",
                    width: 150,
                    sortable: false,
                    /*editor: assignmentEditor,*/
                    xtype: 'resourceassignmentcolumn',
                    listeners: {
                        mouseover: function () {
                            loadFn('Assigned Resources', 'basic-statusbar');
                        }
                    }
                },
                {
                    xtype: 'startdatecolumn',
                    header: 'Start',
                    id:  "StartId",
                    dataIndex: 'StartDate',
                    width: 80,
                    sortable: false,
                    listeners: {
                        mouseover: function () {
                            loadFn('EndDate', 'basic-statusbar');
                        }
                    },
                    renderer: Ext.util.Format.dateRenderer('m/d/Y')
                },
                {
                    xtype: 'enddatecolumn',
                    header: 'End',
                    id:  "EndId",
                    dataIndex: 'EndDate',
                    width: 80,
                    sortable: false,
                    listeners: {
                        mouseover: function () {
                            loadFn('EndDate', 'basic-statusbar');
                        }
                    },
                    renderer: Ext.util.Format.dateRenderer('m/d/Y')
                },
                {
                    xtype: 'durationcolumn',
                    id:  "durationId",
                    sortable: false,
                    renderer: function (e, t, n) {
                        if (!Ext.isNumber(e)) return "";
                        if (!n.isEditable(this.dataIndex)) {
                            t.tdCls = (t.tdCls || "") + " sch-column-readonly";
                        }
                        var r = n.getDurationUnit();
                        var diffDays = parseFloat((n.data.EndDate - n.data.StartDate) / (1000 * 60 * 60 * 24)).toFixed(1);
                        if (diffDays <= 1) {
                            return diffDays + ' day ';
                        }
                        return diffDays + ' days ';
                    },
                    editor: Ext.create('Gnt.field.Duration', {
                        decimalPrecision: 2,
                        instantUpdate: true,
                        listeners: {
                            change: function (field, newValue, oldValue, eOpts) {
                                var task = field.task;
                                task.setStartDate(new Date());
                            },
                            focus: function (field) { }
                        }
                    })
                },
                {
                    xtype: 'effortcolumn',
                    id:  "effortId",
                    sortable: false,
                    width: 70
                },
                {
                    xtype: 'addnewcolumn',
                    id:  "addnewId",
                    sortable: false
                }
            ],
            // tbar function for search task by task name
            tbar: [
                 {
                     xtype: 'textfield',
                     triggerCls: 'x-form-clear-trigger1',
                     onTriggerClick: function () {
                         this.setValue('');
                     },
                     emptyText: 'Type Id',
                     width: 80,
                     enableKeyEvents: true,
                     listeners: {
                         change: function (field, newValue, oldValue) {
                             if (newValue) {
                                 var regexps = Ext.Array.map(newValue.split(/\s+/), function (token) {
                                     return new RegExp(Ext.String.escapeRegex(token), 'i');
                                 });
                                 var length = regexps.length;
                                 g.taskStore.filterTreeBy({
                                     filter: function (tasks) {
                                         var id = tasks.get('Id');
                                         // blazing fast "for" loop! :)
                                         for (var i = 0; i < length; i++)
                                             if (!regexps[i].test(id)) return false;
                                         return true;
                                     }, checkParents: true
                                 });
                             } else {
                                 g.taskStore.clearTreeFilter();
                             }
                         },
                         specialkey: function (field, e, t) {
                             if (e.keyCode === e.ESC) field.reset();
                         }
                     }
                 },
                {
                    xtype: 'textfield',
                    triggerCls: 'x-form-clear-trigger',
                    onTriggerClick: function () {
                        this.setValue('');
                    },
                    emptyText: 'Type Task Name',
                    width: 150,
                    enableKeyEvents: true,
                    listeners: {
                        change: function (field, newValue, oldValue) {
                            if (newValue) {
                                var regexps = Ext.Array.map(newValue.split(/\s+/), function (token) {
                                    return new RegExp(Ext.String.escapeRegex(token), 'i');
                                });
                                var length = regexps.length;

                                g.taskStore.filterTreeBy({
                                    filter: function (tasks) {
                                        var name = tasks.get('Name');
                                        // blazing fast "for" loop! :)
                                        for (var i = 0; i < length; i++)
                                            if (!regexps[i].test(name)) return false;
                                        return true;
                                    }, checkParents: true
                                });
                            } else {
                                g.taskStore.clearTreeFilter();
                            }
                        },
                        specialkey: function (field, e, t) {
                            if (e.keyCode === e.ESC) field.reset();
                        }
                    }
                }
            ],
            //DocItes show at bottom of Gird
            dockedItems: [

                //Status bar
                Ext.create('Ext.ux.StatusBar', {
                    id: 'basic-statusbar',
                    dock: 'bottom',
                    // defaults to use when the status is cleared:
                    defaultText: 'Default status text',
                    //defaultIconCls: 'default-icon',

                    // values to set initially:
                    text: 'Ready',
                    iconCls: 'x-status-valid',

                    // any standard Toolbar items:
                    items: [
                        {
                            id: 'totalTasks',
                            dock: 'bottom',
                            // defaults to use when the status is cleared:
                            defaultText: 'Default status text',
                            //defaultIconCls: 'default-icon',
                            align: 'left',
                            // values to set initially:
                            text: 'TotalTasks',
                            iconCls: 'x-status-valid',
                            xtype: 'label',

                            width: 200
                        },
                         {
                             xtype: 'slider',
                             cls: 'sliderStyle',
                             width: 100,
                             value: 0,
                             increment: 1,
                             minValue: 0,
                             maxValue: 10,
                             listeners: {
                                 change: function (s, v) {
                                     var grid = Ext.ComponentQuery.query("#gv")[0];
                                     grid.zoomToLevel(v);
                                 }
                             }
                         },
                        clock
                    ],

                    listeners: {
                        render: {
                            fn: function () {
                                Ext.fly(clock.getEl().parent()).addCls('x-status-text-panel').createChild({ cls: 'spacer' });
                                // Kick off the clock timer that updates the clock el every second:
                                Ext.TaskManager.start({
                                    run: function () {
                                        Ext.fly(clock.getEl()).update(Ext.Date.format(new Date(), 'g:i:s A'));
                                    },
                                    interval: 1000
                                });
                            },
                            delay: 100
                        }
                    }
                })
            ],
            listeners: {
                render: function (view) {
                    var header = view.getSchedulingView().headerCt;
                    //Tooltip for Header of Task Grid
                    view.tip = Ext.create('Ext.tip.ToolTip', {
                        // The overall target element.
                        target: header.id,
                        // Each grid row causes its own separate show and hide.
                        delegate: '.sch-simple-timeheader',
                        showDelay: 0,
                        trackMouse: true,
                        anchor: 'bottom',
                        dateFormat: 'Y-m-d',
                        //dateFormat: 'Y-m-d, g:i a',
                        renderTo: Ext.getBody(),
                        listeners: {
                            // Change content dynamically depending on which element triggered the show.
                            beforeshow: function (tip) {
                                var el = Ext.get(tip.triggerElement),
                                    position = el.getXY(),
                                    date = view.getSchedulingView().getDateFromXY(position);

                                //update the tip with date
                                tip.update(Ext.Date.format(date, tip.dateFormat));
                            }
                        }
                    });
                },
                itemmouseenter: function () {
                    var grid = Ext.ComponentQuery.query("#gv")[0];
                    var firstColumnHeaderId = grid.columns[0].id;

                    var secondColumnHeaderId = grid.columns[1].id;
                    var lockedDiv = $("#gv-locked-body").contents("div").attr("id");
                    $(document).ready(function () {
                        var scroll = $('#' + lockedDiv).scrollLeft();
                        $('.nocell').css({
                            'left': $(this).scrollLeft() + scroll,
                        });
                        $('.namecell').css({
                            'left': $(this).scrollLeft() + scroll,
                        });
                        $('#' + firstColumnHeaderId).css({
                            'left': $(this).scrollLeft() + scroll,
                            'position': 'relative',
                            'zIndex': 200001
                        });
                        $('#' + secondColumnHeaderId).css({
                            'left': $(this).scrollLeft() + 30 + scroll,
                            'position': 'relative',
                            'zIndex': 200001
                        });
                    });
                },
                containermouseout: function () {
                    var grid = Ext.ComponentQuery.query("#gv")[0];
                    var firstColumnHeaderId = grid.columns[0].id;
                    var secondColumnHeaderId = grid.columns[1].id;
                    var lockedDiv = $("#gv-locked-body").contents("div").attr("id");
                    $(document).ready(function () {
                        var scroll = $('#' + lockedDiv).scrollLeft();
                        $('.nocell').css({
                            'left': $(this).scrollLeft() + scroll,
                        });
                        $('.namecell').css({
                            'left': $(this).scrollLeft() + scroll,
                        });
                        $('#' + firstColumnHeaderId).css({
                            'left': $(this).scrollLeft() + scroll,
                            'position': 'relative',
                            'zIndex': 200001
                        });
                        $('#' + secondColumnHeaderId).css({
                            'left': $(this).scrollLeft() + 30 + scroll,
                            'position': 'relative',
                            'zIndex': 200001
                        });
                    });
                },
                beforeitemmouseenter: function () {
                    var grid = Ext.ComponentQuery.query("#gv")[0];
                    var firstColumnHeaderId = grid.columns[0].id;
                    var secondColumnHeaderId = grid.columns[1].id;
                    var lockedDiv = $("#gv-locked-body").contents("div").attr("id");
                    $(document).ready(function () {
                        var scroll = $('#' + lockedDiv).scrollLeft();
                        $('.nocell').css({
                            'left': $(this).scrollLeft() + scroll,
                        });
                        $('.namecell').css({
                            'left': $(this).scrollLeft() + scroll,
                        });
                        $('#' + firstColumnHeaderId).css({
                            'left': $(this).scrollLeft() + scroll,
                            'position': 'relative',
                            'zIndex': 200001
                        });
                        $('#' + secondColumnHeaderId).css({
                            'left': $(this).scrollLeft() + 30 + scroll,
                            'position': 'relative',
                            'zIndex': 200001
                        });
                    });
                },
                itemmouseleave: function () {
                    var grid = Ext.ComponentQuery.query("#gv")[0];
                    var firstColumnHeaderId = grid.columns[0].id;
                    var secondColumnHeaderId = grid.columns[1].id;
                    var lockedDiv = $("#gv-locked-body").contents("div").attr("id");
                    $(document).ready(function () {
                        var scroll = $('#' + lockedDiv).scrollLeft();
                        $('.nocell').css({
                            'left': $(this).scrollLeft() + scroll,
                        });
                        $('.namecell').css({
                            'left': $(this).scrollLeft() + scroll,
                        });
                        $('#' + firstColumnHeaderId).css({
                            'left': $(this).scrollLeft() + scroll,
                            'position': 'relative',
                            'zIndex': 200001
                        });
                        $('#' + secondColumnHeaderId).css({
                            'left': $(this).scrollLeft() + 30 + scroll,
                            'position': 'relative',
                            'zIndex': 200001
                        });
                    });
                },
                containermouseover: function () {
                    var grid = Ext.ComponentQuery.query("#gv")[0];
                    var firstColumnHeaderId = grid.columns[0].id;
                    var secondColumnHeaderId = grid.columns[1].id;
                    var lockedDiv = $("#gv-locked-body").contents("div").attr("id");
                    $(document).ready(function () {
                        var scroll = $('#' + lockedDiv).scrollLeft();
                        $('.nocell').css({
                            'left': $(this).scrollLeft() + scroll,
                        });
                        $('.namecell').css({
                            'left': $(this).scrollLeft() + scroll,
                        });
                        $('#' + firstColumnHeaderId).css({
                            'left': $(this).scrollLeft() + scroll,
                            'position': 'relative',
                            'zIndex': 200001
                        });
                        $('#' + secondColumnHeaderId).css({
                            'left': $(this).scrollLeft() + 30 + scroll,
                            'position': 'relative',
                            'zIndex': 200001
                        });
                    });
                },
                staterestore: function (component, state, eOpts) {
                    component.lockedGrid.setWidth(component.lockedGridConfig.width);
                },
                itemclick: function (scheduler, eventRecord, e, eOpts) {
                    var resourceScheduler = document.getElementById("resourceScheduler");
                    if (resourceScheduler.style.display != "none") {
                        var grid = Ext.ComponentQuery.query("#gv")[0];
                        var resourceSchedulerGrid = Ext.ComponentQuery.query("#resourceScheduler")[0];
                        var row = grid.taskStore.getById(eventRecord.data.Id);
                        if (row.data.Resource_Names) {
                            var gv = resourceSchedulerGrid.getSchedulingView();
                            gv.scrollEventIntoView(row, { duration: 3000 }, true);
                        }
                    }
                },
            }
        });
        g.on('zoomchange', function (scheduler, zoomLevel) {
            slider.setValue(zoomLevel);
        });
        this.callParent(arguments);
    },
    showFullScreen: function () {
        this.el.down('.x-panel-body').dom[this._fullScreenFn]();
    },
    applyPercentDone: function (value) {
        this.gantt.getSelectionModel().selected.each(function (task) {
            task.setPercentDone(value);
        });
    },

    eventRenderer: function (task) {
        var prioCls;
        switch (task.get('Priority')) {
            case TaskPriority.Low:
                prioCls = 'sch-gantt-prio-low';
                break;

            case TaskPriority.Normal:
                prioCls = 'sch-gantt-prio-normal';
                break;

            case TaskPriority.High:
                prioCls = 'sch-gantt-prio-high';
                break;
        }

        return {
            cls: prioCls
        };
    },
    //Show Full screen function
    _fullScreenFn: (function () {
        var docElm = document.documentElement;

        if (docElm.requestFullscreen) {
            return "requestFullscreen";
        }
        else if (docElm.mozRequestFullScreen) {
            return "mozRequestFullScreen";
        }
        else if (docElm.webkitRequestFullScreen) {
            return "webkitRequestFullScreen";
        }
    })()
});

var reshowDateWindow = Ext.create('Ext.menu.DatePicker', {
    handler: function (dp, date) {
        var grid = Ext.ComponentQuery.query("#gv")[0];
        var task = grid.getSelectionModel().selected;
        task.items[0].data.isReshow = true;
        task.items[0].set('Reshow_Date', date);
    }
});

//Customize Task Context Menu
Ext.define("Gnt.plugin.TaskContextMenu", {
    extend: "Ext.menu.Menu",

    requires: [
        'Gnt.model.Dependency'
    ],
    plain: true,
    triggerEvent: 'taskcontextmenu',

    texts: {
        cut: 'Cut',
        copy: 'Copy',
        paste: 'Paste',
        critical: 'Critical',
        reshow: 'Reshow',
        completed: 'Completed',
        notclear: 'Not Clear',
        noticearrow: 'Notice arrow',
        passive: 'Passive',
        cancelled: 'Cancelled',
        donotfilter: 'Do not filter',
        lock: 'Lock',
        masterlock: 'Master lock',
        pagebreak: 'Page break',
        newTaskText: 'Add Component',
        newMilestoneText: 'New milestone',
        deleteTask: 'Delete Component(s)',
        editLeftLabel: 'Edit left label',
        editRightLabel: 'Edit right label',
        convert: 'Convert',
        converToMilestone: 'Convert to Milestone',
        converToRegularTask: 'Convert to Regular Task',
        add: 'Add',
        deleteDependency: 'Delete dependency',
        //addTaskAbove: 'Task above',
        addTaskBelow: 'Same Level',
        addSubtask: 'on next lower level',
        OnLevel1: 'OnLevel1(Folder)',
        OnLevel2: 'OnLevel2(Project)',
        OnLevel3: 'OnLevel3(Task)',
        addMilestone: 'Milestone',
        addSuccessor: 'Successor',
        addPredecessor: 'Predecessor',

    },

    Id: 'TaskContextMenu',
    grid: null,

    rec: null,

    lastHighlightedItem: null,

    createMenuItems: function () {
        var texts = this.texts;

        return [
            {
                itemId: 'contextmenu-Cut',
                handler: this.cut,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/Cut.png',
                text: texts.cut
            },
            {
                itemId: 'contextmenu-Copy',
                handler: this.copy,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/Copy.png',
                text: texts.copy,
            },
            {
                itemId: 'contextmenu-Paste',
                handler: this.paste,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/icon_paste.png',
                text: texts.paste
            },
            {
                itemId: 'contextmenu-Critical',
                handler: this.critical,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/Icons/17_flash_16.png',
                text: texts.critical
            },
            {
                itemId: 'contextmenu-Reshow',
                handler: this.reshow,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/Icons/19_warning_16.png',
                text: texts.reshow
            },
            {
                itemId: 'contextmenu-Completed',
                handler: this.completed,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/Icons/10_check_16.png',
                text: texts.completed
            },
            {
                itemId: 'contextmenu-NotClear',
                handler: this.notclear,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/Icons/18_question_16.png',
                text: texts.notclear
            },
            {
                itemId: 'contextmenu-NoticeArrow',
                handler: this.noticearrow,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/Icons/16_arrow_16.png',
                text: texts.noticearrow
            },
            {
                itemId: 'contextmenu-Passive',
                handler: this.passive,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/Icons/11_task_passive_16.png',
                text: texts.passive,
            },
            {
                itemId: 'contextmenu-Cancelled',
                handler: this.cancelled,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/Icons/12_task_canceled_16.png',
                text: texts.cancelled
            },
            {
                itemId: 'contextmenu-DoNotFilter',
                handler: this.donotfilter,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/DonotFilter.png',
                text: texts.donotfilter
            },
            {
                itemId: 'contextmenu-Lock',
                handler: this.lock,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/Lock.png',
                text: texts.lock
            },
            {
                itemId: 'contextmenu-MasterLock',
                handler: this.masterlock,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/MasterLock.png',
                text: texts.masterlock
            },
            {
                itemId: 'contextmenu-PageBreak',
                handler: this.pagebreak,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/PageBreak.png',
                text: texts.pagebreak
            },
            {
                itemId: 'contextmenu-DeleteTask',
                handler: this.deleteTask,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/Icons/12_task_canceled_16.png',
                text: texts.deleteTask
            },
            {
                itemId: 'contextmenu-EditLeftLable',
                handler: this.editLeftLabel,
                requiresTask: true,
                scope: this,
                text: texts.editLeftLabel
            },
            {
                itemId: 'contextmenu-EditRightLable',
                handler: this.editRightLabel,
                requiresTask: true,
                scope: this,
                text: texts.editRightLabel
            },
            {
                text: texts.convert,
                itemId: 'contextmenu-Convert',
                menu: {
                    plain: true,
                    items: [
                        {
                            itemId: 'contextmenu-ConvertToMilestone',
                            handler: this.converToMilestone,
                            requiresTask: true,
                            scope: this,
                            text: texts.converToMilestone
                        },
                        {
                            itemId: 'contextmenu-ConvertToRegularTask',
                            handler: this.converToRegularTask,
                            requiresTask: true,
                            scope: this,
                            text: texts.converToRegularTask
                        }
                    ]
                }
            },
            {
                text: texts.add,
                itemId: 'contextmenu-Add',
                menu: {
                    plain: true,
                    items: [
                        {
                            itemId: 'contextmenu-OnLevel1',
                            handler: this.OnLevel1Action,
                            scope: this,
                            text: texts.OnLevel1
                        },
                        {
                            itemId: 'contextmenu-OnLevel2',
                            handler: this.OnLevel2Action,
                            requiresTask: true,
                            scope: this,
                            text: texts.OnLevel2
                        },
                        {
                            itemId: 'contextmenu-OnLevel3',
                            handler: this.OnLevel3Action,
                            requiresTask: true,
                            scope: this,
                            text: texts.OnLevel3
                        },
                        //{
                        //    handler: this.addTaskAboveAction,
                        //    requiresTask: true,
                        //    scope: this,
                        //    text: texts.addTaskAbove
                        //},
                        {
                            itemId: 'contextmenu-AddTaskBelow',
                            handler: this.addTaskBelowAction,
                            requiresTask: true,
                            scope: this,
                            text: texts.addTaskBelow
                        },
                        {
                            itemId: 'contextmenu-AddSubTask',
                            handler: this.addSubtask,
                            requiresTask: true,
                            scope: this,
                            text: texts.addSubtask
                        },
                        {
                            itemId: 'contextmenu-AddMilsestone',
                            handler: this.addMilestone,
                            requiresTask: true,
                            scope: this,
                            text: texts.addMilestone
                        },
                        {
                            itemId: 'contextmenu-AddSuccessor',
                            handler: this.addSuccessor,
                            requiresTask: true,
                            scope: this,
                            text: texts.addSuccessor
                        },
                        {
                            itemId: 'contextmenu-AddPredecessor',
                            handler: this.addPredecessor,
                            requiresTask: true,
                            scope: this,
                            text: texts.addPredecessor
                        }
                    ]
                }
            },
            {
                itemId: 'contextmenu-deleteDependency',
                text: texts.deleteDependency,
                requiresTask: true,
                menu: {
                    plain: true,

                    listeners: {
                        beforeshow: this.populateDependencyMenu,

                        // highlight dependencies on mouseover of the menu item
                        mouseover: this.onDependencyMouseOver,

                        // unhighlight dependencies on mouseout of the menu item
                        mouseleave: this.onDependencyMouseOut,

                        scope: this
                    }
                }
            }
        ];
    },
    listeners: {
        'beforeshow': function () {
            var me = this;
            var tasks = this.grid.getSelectionModel().selected;
            var a = me.getComponent("contextmenu-Add");
            if (tasks.length > 0) {
                var rawId = tasks.items[0].data.Id;
                var level = tasks.items[0].data.Outline_Level;
                if (tasks.items[0].data.Outline_Level == 1) {
                    me.getComponent("contextmenu-Cut").show();
                    me.getComponent("contextmenu-Copy").show();
                    me.getComponent("contextmenu-Paste").show();
                    me.getComponent("contextmenu-Critical").hide();
                    me.getComponent("contextmenu-Reshow").hide();
                    me.getComponent("contextmenu-Completed").hide();

                    me.getComponent("contextmenu-NotClear").hide();
                    me.getComponent("contextmenu-NoticeArrow").hide();
                    me.getComponent("contextmenu-Passive").hide();

                    me.getComponent("contextmenu-Cancelled").show();
                    me.getComponent("contextmenu-Lock").show();
                    me.getComponent("contextmenu-MasterLock").hide();
                    me.getComponent("contextmenu-DoNotFilter").hide();

                    me.getComponent("contextmenu-DeleteTask").show();

                    me.getComponent("contextmenu-EditLeftLable").hide();
                    me.getComponent("contextmenu-EditRightLable").hide();
                    me.getComponent("contextmenu-Convert").hide();
                    if (a.menu.items.items[0].itemId == "contextmenu-OnLevel1") {
                        a.menu.items.items[0].show();
                    }
                    if (a.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                        a.menu.items.items[1].show();
                    }
                    if (a.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                        a.menu.items.items[2].hide();
                    }
                    if (a.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                        a.menu.items.items[3].show();
                    }
                    if (a.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                        a.menu.items.items[4].show();
                    }
                    if (a.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                        a.menu.items.items[5].hide();
                    }
                    if (a.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                        a.menu.items.items[6].hide();
                    }
                    if (a.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                        a.menu.items.items[7].hide();
                    }
                    me.getComponent("contextmenu-deleteDependency").hide();

                    if (tasks.items[0].data.Lock == 1) {
                        me.getComponent("contextmenu-Cut").hide();
                        me.getComponent("contextmenu-Copy").show();
                        me.getComponent("contextmenu-Paste").show();
                        me.getComponent("contextmenu-Critical").hide();
                        me.getComponent("contextmenu-Reshow").hide();
                        me.getComponent("contextmenu-Completed").hide();

                        me.getComponent("contextmenu-NotClear").hide();
                        me.getComponent("contextmenu-NoticeArrow").hide();
                        me.getComponent("contextmenu-Passive").hide();

                        me.getComponent("contextmenu-Cancelled").show();
                        me.getComponent("contextmenu-Lock").show();
                        me.getComponent("contextmenu-MasterLock").hide();
                        me.getComponent("contextmenu-DoNotFilter").hide();

                        me.getComponent("contextmenu-DeleteTask").hide();

                        me.getComponent("contextmenu-EditLeftLable").hide();
                        me.getComponent("contextmenu-EditRightLable").hide();
                        me.getComponent("contextmenu-Convert").hide();
                        if (a.menu.items.items[0].itemId == "contextmenu-OnLevel1") {
                            a.menu.items.items[0].show();
                        }
                        if (a.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                            a.menu.items.items[1].hide();
                        }
                        if (a.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                            a.menu.items.items[2].hide();
                        }
                        if (a.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                            a.menu.items.items[3].show();
                        }
                        if (a.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                            a.menu.items.items[4].hide();
                        }
                        if (a.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                            a.menu.items.items[5].hide();
                        }
                        if (a.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                            a.menu.items.items[6].hide();
                        }
                        if (a.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                            a.menu.items.items[7].hide();
                        }
                        me.getComponent("contextmenu-deleteDependency").hide();
                    }
                    $.ajax({
                        type: 'GET',
                        url: "Tasks/CheckPermission?rawId=" + rawId + '&level=' + level,
                        autoSync: true,
                        autoLoad: true,
                        success: function (response) {

                            if (response == "Read" || response == "Change") {
                                me.getComponent("contextmenu-Cut").hide();
                                var a = me.getComponent("contextmenu-Add");
                                if (a.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                                    a.menu.items.items[1].hide();
                                }
                                if (a.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                                    a.menu.items.items[2].hide();
                                }
                                if (a.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                                    a.menu.items.items[3].show();
                                }
                                if (a.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                                    a.menu.items.items[4].hide();
                                }
                                me.getComponent("contextmenu-DeleteTask").hide();
                            }
                        }
                    });
                }
                else if (tasks.items[0].data.Outline_Level == 2) {
                    me.getComponent("contextmenu-Cut").show();
                    me.getComponent("contextmenu-Copy").show();
                    me.getComponent("contextmenu-Paste").show();
                    me.getComponent("contextmenu-Critical").show();
                    me.getComponent("contextmenu-Reshow").show();
                    me.getComponent("contextmenu-Completed").show();

                    me.getComponent("contextmenu-NotClear").show();
                    me.getComponent("contextmenu-NoticeArrow").show();
                    me.getComponent("contextmenu-Passive").show();

                    me.getComponent("contextmenu-Cancelled").show();
                    me.getComponent("contextmenu-DoNotFilter").show();

                    me.getComponent("contextmenu-DeleteTask").show();

                    me.getComponent("contextmenu-EditLeftLable").show();
                    me.getComponent("contextmenu-EditRightLable").show();
                    me.getComponent("contextmenu-Convert").show();

                    if (a.menu.items.items[0].itemId == "contextmenu-OnLevel1") {
                        a.menu.items.items[0].show();
                    }
                    if (a.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                        a.menu.items.items[1].show();
                    }
                    if (a.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                        a.menu.items.items[2].show();
                    }
                    if (a.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                        a.menu.items.items[3].show();
                    }
                    if (a.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                        a.menu.items.items[4].show();
                    }
                    if (a.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                        a.menu.items.items[5].show();
                    }
                    if (a.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                        a.menu.items.items[6].show();
                    }
                    if (a.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                        a.menu.items.items[7].show();
                    }
                    me.getComponent("contextmenu-deleteDependency").show();

                    if (tasks.items[0].data.Lock == 1) {
                        me.getComponent("contextmenu-Cut").hide();
                        me.getComponent("contextmenu-Copy").show();
                        me.getComponent("contextmenu-Paste").show();
                        me.getComponent("contextmenu-Critical").hide();
                        me.getComponent("contextmenu-Reshow").hide();
                        me.getComponent("contextmenu-Completed").hide();

                        me.getComponent("contextmenu-NotClear").hide();
                        me.getComponent("contextmenu-NoticeArrow").hide();
                        me.getComponent("contextmenu-Passive").hide();

                        me.getComponent("contextmenu-Cancelled").show();
                        me.getComponent("contextmenu-Lock").show();
                        me.getComponent("contextmenu-MasterLock").hide();
                        me.getComponent("contextmenu-DoNotFilter").hide();

                        me.getComponent("contextmenu-DeleteTask").hide();

                        me.getComponent("contextmenu-EditLeftLable").hide();
                        me.getComponent("contextmenu-EditRightLable").hide();
                        me.getComponent("contextmenu-Convert").hide();
                        if (a.menu.items.items[0].itemId == "contextmenu-OnLevel1") {
                            a.menu.items.items[0].show();
                        }
                        if (a.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                            a.menu.items.items[1].hide();
                        }
                        if (a.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                            a.menu.items.items[2].hide();
                        }
                        if (a.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                            a.menu.items.items[3].show();
                        }
                        if (a.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                            a.menu.items.items[4].hide();
                        }
                        if (a.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                            a.menu.items.items[5].hide();
                        }
                        if (a.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                            a.menu.items.items[6].hide();
                        }
                        if (a.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                            a.menu.items.items[7].hide();
                        }
                        me.getComponent("contextmenu-deleteDependency").hide();
                    }
                    $.ajax({
                        type: 'GET',
                        url: "Tasks/CheckPermission?rawId=" + rawId + '&level=' + level,
                        autoSync: true,
                        autoLoad: true,
                        success: function (response) {
                            var a = me.getComponent("contextmenu-Add");
                            if (response == "Change") {
                                me.getComponent("contextmenu-Cut").hide();
                                if (a.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                                    a.menu.items.items[1].hide();
                                }
                                if (a.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                                    a.menu.items.items[2].hide();
                                }
                                if (a.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                                    a.menu.items.items[3].show();
                                }
                                if (a.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                                    a.menu.items.items[4].hide();
                                }
                                if (a.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                                    a.menu.items.items[5].hide();
                                }
                                if (a.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                                    a.menu.items.items[6].hide();
                                }
                                if (a.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                                    a.menu.items.items[7].hide();
                                }
                                me.getComponent("contextmenu-DeleteTask").hide();
                            }
                            else if (response == "Read") {
                                me.getComponent("contextmenu-Cut").hide();
                                me.getComponent("contextmenu-Copy").show();
                                me.getComponent("contextmenu-Paste").show();
                                me.getComponent("contextmenu-Critical").hide();
                                me.getComponent("contextmenu-Reshow").hide();
                                me.getComponent("contextmenu-Completed").hide();

                                me.getComponent("contextmenu-NotClear").hide();
                                me.getComponent("contextmenu-NoticeArrow").hide();
                                me.getComponent("contextmenu-Passive").hide();

                                me.getComponent("contextmenu-Cancelled").hide();
                                me.getComponent("contextmenu-Lock").show();
                                me.getComponent("contextmenu-MasterLock").show();
                                me.getComponent("contextmenu-DoNotFilter").hide();

                                me.getComponent("contextmenu-DeleteTask").hide();

                                me.getComponent("contextmenu-EditLeftLable").hide();
                                me.getComponent("contextmenu-EditRightLable").hide();
                                me.getComponent("contextmenu-Convert").hide();
                                if (a.menu.items.items[0].itemId == "contextmenu-OnLevel1") {
                                    a.menu.items.items[0].show();
                                }
                                if (a.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                                    a.menu.items.items[1].hide();
                                }
                                if (a.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                                    a.menu.items.items[2].hide();
                                }
                                if (a.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                                    a.menu.items.items[3].show();
                                }
                                if (a.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                                    a.menu.items.items[4].hide();
                                }
                                if (a.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                                    a.menu.items.items[5].hide();
                                }
                                if (a.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                                    a.menu.items.items[6].hide();
                                }
                                if (a.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                                    a.menu.items.items[7].hide();
                                }
                                me.getComponent("contextmenu-deleteDependency").hide();
                            }
                        }
                    });
                } else if (tasks.items[0].data.Outline_Level == 3) {
                    if (tasks.items[0].data.Lock == 1) {
                        me.getComponent("contextmenu-Cut").hide();
                        me.getComponent("contextmenu-Copy").show();
                        me.getComponent("contextmenu-Paste").show();
                        me.getComponent("contextmenu-Critical").hide();
                        me.getComponent("contextmenu-Reshow").hide();
                        me.getComponent("contextmenu-Completed").hide();

                        me.getComponent("contextmenu-NotClear").hide();
                        me.getComponent("contextmenu-NoticeArrow").hide();
                        me.getComponent("contextmenu-Passive").hide();

                        me.getComponent("contextmenu-Cancelled").show();
                        me.getComponent("contextmenu-Lock").show();
                        me.getComponent("contextmenu-MasterLock").hide();
                        me.getComponent("contextmenu-DoNotFilter").hide();

                        me.getComponent("contextmenu-DeleteTask").hide();

                        me.getComponent("contextmenu-EditLeftLable").hide();
                        me.getComponent("contextmenu-EditRightLable").hide();
                        me.getComponent("contextmenu-Convert").hide();
                        if (a.menu.items.items[0].itemId == "contextmenu-OnLevel1") {
                            a.menu.items.items[0].show();
                        }
                        if (a.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                            a.menu.items.items[1].hide();
                        }
                        if (a.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                            a.menu.items.items[2].hide();
                        }
                        if (a.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                            a.menu.items.items[3].show();
                        }
                        if (a.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                            a.menu.items.items[4].hide();
                        }
                        if (a.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                            a.menu.items.items[5].hide();
                        }
                        if (a.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                            a.menu.items.items[6].hide();
                        }
                        if (a.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                            a.menu.items.items[7].hide();
                        }
                        me.getComponent("contextmenu-deleteDependency").hide();
                    } else {
                        $.ajax({
                            type: 'GET',
                            url: "Tasks/CheckPermission?rawId=" + tasks.items[0].data.parentId + '&level=' + level,
                            autoSync: true,
                            autoLoad: true,
                            success: function (response) {
                                var a = me.getComponent("contextmenu-Add");
                                if (response == "Change") {
                                    me.getComponent("contextmenu-Cut").hide();
                                    me.getComponent("contextmenu-Copy").show();
                                    me.getComponent("contextmenu-Paste").show();

                                    me.getComponent("contextmenu-Critical").show();
                                    me.getComponent("contextmenu-Reshow").show();
                                    me.getComponent("contextmenu-Completed").show();

                                    me.getComponent("contextmenu-NotClear").show();
                                    me.getComponent("contextmenu-NoticeArrow").show();
                                    me.getComponent("contextmenu-Passive").show();

                                    me.getComponent("contextmenu-Cancelled").show();
                                    me.getComponent("contextmenu-DoNotFilter").show();
                                    me.getComponent("contextmenu-Lock").show();
                                    me.getComponent("contextmenu-MasterLock").show();

                                    me.getComponent("contextmenu-EditLeftLable").show();
                                    me.getComponent("contextmenu-EditRightLable").show();
                                    me.getComponent("contextmenu-Convert").show();

                                    if (a.menu.items.items[0].itemId == "contextmenu-OnLevel1") {
                                        a.menu.items.items[0].show();
                                    }
                                    if (a.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                                        a.menu.items.items[1].hide();
                                    }
                                    if (a.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                                        a.menu.items.items[2].hide();
                                    }
                                    if (a.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                                        a.menu.items.items[3].show();
                                    }
                                    if (a.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                                        a.menu.items.items[4].hide();
                                    }
                                    if (a.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                                        a.menu.items.items[5].hide();
                                    }
                                    if (a.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                                        a.menu.items.items[6].hide();
                                    }
                                    if (a.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                                        a.menu.items.items[7].hide();
                                    }
                                    me.getComponent("contextmenu-deleteDependency").hide();
                                    me.getComponent("contextmenu-DeleteTask").hide();
                                }
                                if (response == "Read") {
                                    me.getComponent("contextmenu-Cut").hide();
                                    me.getComponent("contextmenu-Copy").show();
                                    me.getComponent("contextmenu-Paste").show();
                                    me.getComponent("contextmenu-Critical").hide();
                                    me.getComponent("contextmenu-Reshow").hide();
                                    me.getComponent("contextmenu-Completed").hide();

                                    me.getComponent("contextmenu-NotClear").hide();
                                    me.getComponent("contextmenu-NoticeArrow").hide();
                                    me.getComponent("contextmenu-Passive").hide();

                                    me.getComponent("contextmenu-Cancelled").hide();
                                    me.getComponent("contextmenu-DoNotFilter").hide();
                                    me.getComponent("contextmenu-Lock").show();
                                    me.getComponent("contextmenu-MasterLock").show();

                                    me.getComponent("contextmenu-DeleteTask").hide();

                                    me.getComponent("contextmenu-EditLeftLable").hide();
                                    me.getComponent("contextmenu-EditRightLable").hide();
                                    me.getComponent("contextmenu-Convert").hide();
                                    if (a.menu.items.items[0].itemId == "contextmenu-OnLevel1") {
                                        a.menu.items.items[0].show();
                                    }
                                    if (a.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                                        a.menu.items.items[1].hide();
                                    }
                                    if (a.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                                        a.menu.items.items[2].hide();
                                    }
                                    if (a.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                                        a.menu.items.items[3].show();
                                    }
                                    if (a.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                                        a.menu.items.items[4].hide();
                                    }
                                    if (a.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                                        a.menu.items.items[5].hide();
                                    }
                                    if (a.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                                        a.menu.items.items[6].hide();
                                    }
                                    if (a.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                                        a.menu.items.items[7].hide();
                                    }
                                    me.getComponent("contextmenu-deleteDependency").hide();
                                }
                                if (response == "Create" || response == "Standard") {
                                    me.getComponent("contextmenu-Cut").show();
                                    me.getComponent("contextmenu-Copy").show();
                                    me.getComponent("contextmenu-Paste").show();
                                    me.getComponent("contextmenu-Critical").show();
                                    me.getComponent("contextmenu-Reshow").show();
                                    me.getComponent("contextmenu-Completed").show();

                                    me.getComponent("contextmenu-NotClear").show();
                                    me.getComponent("contextmenu-NoticeArrow").show();
                                    me.getComponent("contextmenu-Passive").show();

                                    me.getComponent("contextmenu-Cancelled").show();
                                    me.getComponent("contextmenu-DoNotFilter").show();

                                    me.getComponent("contextmenu-DeleteTask").show();

                                    me.getComponent("contextmenu-EditLeftLable").show();
                                    me.getComponent("contextmenu-EditRightLable").show();
                                    me.getComponent("contextmenu-Convert").show();

                                    if (a.menu.items.items[0].itemId == "contextmenu-OnLevel") {
                                        a.menu.items.items[0].show();
                                    }
                                    if (a.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                                        a.menu.items.items[1].show();
                                    }
                                    if (a.menu.items.items[2].itemId == "contextmenu-AddTaskBelow") {
                                        a.menu.items.items[2].show();
                                    }
                                    if (a.menu.items.items[3].itemId == "contextmenu-AddSubTask") {
                                        a.menu.items.items[3].show();
                                    }
                                    if (a.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                                        a.menu.items.items[4].show();
                                    }
                                    if (a.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                                        a.menu.items.items[5].show();
                                    }
                                    if (a.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                                        a.menu.items.items[6].show();
                                    }
                                    if (a.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                                        a.menu.items.items[7].show();
                                    }
                                    me.getComponent("contextmenu-deleteDependency").show();
                                }
                            },
                            error: function () {
                                alert("Request Failed");
                            },
                            processData: true,
                            async: true
                        });
                    }
                }
            }
        }
    },

    // backward compat
    buildMenuItems: function () {
        this.items = this.createMenuItems();
    },

    initComponent: function () {
        this.buildMenuItems();
        this.callParent(arguments);
    },

    init: function (grid) {
        grid.on('destroy', this.cleanUp, this);
        var scheduleView = grid.getSchedulingView(),
            lockedView = grid.lockedGrid.getView();

        //TODO::This Coment is only for readonly mode

        if (this.triggerEvent === 'itemcontextmenu') {
            lockedView.on('itemcontextmenu', this.onItemContextMenu, this);
            scheduleView.on('itemcontextmenu', this.onItemContextMenu, this);
        } else {
            //get task context menu on both left and right grid click event
            scheduleView.on('taskcontextmenu', this.onTaskContextMenu, this);
            lockedView.on('itemcontextmenu', this.onItemContextMenu, this);
        }

        // Handle case of empty schedule too
        scheduleView.on('containercontextmenu', this.onContainerContextMenu, this);
        lockedView.on('containercontextmenu', this.onContainerContextMenu, this);
        this.grid = grid;
    },

    populateDependencyMenu: function (menu) {
        var grid = this.grid,
            taskStore = grid.getTaskStore(),
            dependencies = this.rec.getAllDependencies(),
            depStore = grid.dependencyStore;

        menu.removeAll();

        if (dependencies.length === 0) {
            return false;
        }

        var taskId = this.rec.getId() || this.rec.internalId;

        Ext.each(dependencies, function (dependency) {
            var fromId = dependency.get('From'),
                task = taskStore.getById(fromId == taskId ? dependency.get('To') : fromId);

            if (task) {
                menu.add({
                    depId: dependency.internalId,
                    text: Ext.util.Format.ellipsis(task.get('Name'), 30),

                    scope: this,
                    handler: function (menuItem) {
                        // in 4.0.2 `indexOfId` returns the record by the `internalId`
                        // in 4.0.7 `indexOfId` returns the record by its "real" id
                        // so need to manually scan the store to find the record

                        var record;

                        depStore.each(function (dependency) {
                            if (dependency.internalId == menuItem.depId) { record = dependency; return false; }
                        });

                        depStore.remove(record);
                    }
                });
            }
        }, this);
    },

    onDependencyMouseOver: function (menu, item, e) {
        if (item) {
            var schedulingView = this.grid.getSchedulingView();

            if (this.lastHighlightedItem) {
                schedulingView.unhighlightDependency(this.lastHighlightedItem.depId);
            }

            this.lastHighlightedItem = item;
            schedulingView.highlightDependency(item.depId);
        }
    },

    onDependencyMouseOut: function (menu, e) {
        if (this.lastHighlightedItem) {
            this.grid.getSchedulingView().unhighlightDependency(this.lastHighlightedItem.depId);
        }
    },

    cleanUp: function () {
        if (this.menu) {
            this.menu.destroy();
        }
    },

    onTaskContextMenu: function (g, record, e) {
        this.activateMenu(record, e);
    },

    onItemContextMenu: function (view, record, item, index, e) {
        this.activateMenu(record, e);
    },

    onContainerContextMenu: function (g, e) {
        this.activateMenu(null, e);
    },

    activateMenu: function (rec, e) {
        e.stopEvent();

        this.rec = rec;
        var reqTasks = this.query('[requiresTask]');
        Ext.each(reqTasks, function (item) {
            item.setDisabled(!rec);
        });
        this.showAt(e.getXY());
    },

    copyTask: function (originalRecord) {
        if (originalRecord != null) {
            var taskStore = this.grid.getTaskStore(),

                 newTask = new taskStore.model({
                     PercentDone: 0,
                     Name: this.texts.newTaskText,
                     StartDate: originalRecord.get('StartDate'),
                     EndDate: originalRecord.get('EndDate'),
                     parentId: originalRecord.get('parentId') + 1,
                     leaf: true,
                     Outline_Level: originalRecord.get('Outline_Level')
                 });
            return newTask;
        } else {
            var taskStore = this.grid.getTaskStore(),

           newTask = new taskStore.model({
               leaf: true,
               Id: null,
               StartDate: null,
               EndDate: null,
               Priority: 1,
               parentId: null,
               isCriticalTask: false,
               isClarificationRequired: false,
               LimitDate: null,
               isNotice: false,
               isReshow: false,
               isCompleted: false,
               isPassive: false,
               isCancelled: false,
               index: 32,
               Cls: "",
               Name: "New task",
               Duration: null,
               Effort: null,
               EffortUnit: "h",
               CalendarId: "",
               Note: "",
               PercentDone: 0,
               ManuallyScheduled: false,
               SchedulingMode: "Normal",
               BaselineStartDate: null,
               BaselineEndDate: null,
               BaselinePercentDone: 0,
               PhantomId: "ext-record-363",
               PhantomParentId: "",
               DurationUnit: "d"
           });
            return newTask;
        }
    },

    addTaskAbove: function (newTask) {
        var task = this.rec;
        newTask.isCriticalTask = false;
        newTask.isClarificationRequired = false;
        newTask.isPassive = false;
        newTask.isCancelled = false;
        newTask.isNotice = false;
        newTask.isCompleted = false;
        newTask.isReshow = false;
        newTask.NoticeStatus = 3;
        newTask.LastModification = new Date();
        newTask.LastUserMod = new Date();
        newTask.Group_Name = task.parentNode.raw.Name;
        newTask.Created = new Date();
        if (task.data.Outline_Level == 1) {
            newTask.Name = '(Project desig.)';
        }
        else if (task.data.Outline_Level >= 2) {
            newTask.Name = '(Task desig.)';
        }
        task.parentNode.set('Object', parseInt(task.parentNode.data.Object) + 1);
        var statusBar = Ext.getCmp("basic-statusbar");
        statusBar.showBusy();
        Ext.defer(function () {
            statusBar.setStatus("Ready");
        }, 1000);
        if (task) {
            task.parentNode.insertBefore(newTask, task);
        } else {
            this.grid.taskStore.getRootNode().appendChild(newTask);
        }
    },

    addTaskBelow: function (newTask) {
        var task = this.rec;
        var statusBar = Ext.getCmp("basic-statusbar");
        statusBar.showBusy();
        Ext.defer(function () {
            statusBar.setStatus("Ready");
        }, 1000);
        if (task.data.Outline_Level == 1) {
            newTask.isCriticalTask = false;
            newTask.isClarificationRequired = false;
            newTask.isPassive = false;
            newTask.isCancelled = false;
            newTask.isNotice = false;
            newTask.isCompleted = false;
            newTask.isReshow = false;
            newTask.NoticeStatus = 3;
            newTask.LastModification = new Date();
            newTask.LastUserMod = new Date();
            newTask.Group_Name = task.parentNode.raw.Name;
            newTask.Created = new Date();
            newTask.set('Outline_Level', 1);
            newTask.set('leaf', true);
            newTask.set('Name', '(Folder desig.)');
            newTask.set('parentId', 0);
            task.parentNode.set('Object', parseInt(task.parentNode.data.Object) + 1);
            if (task) {
                task.parentNode.insertBefore(newTask, task.nextSibling);
            } else {
                this.grid.taskStore.getRootNode().appendChild(newTask);
            }
        }
        else if (task.data.Outline_Level == 2) {
            newTask.isCriticalTask = false;
            newTask.isClarificationRequired = false;
            newTask.isPassive = false;
            newTask.isCancelled = false;
            newTask.isNotice = false;
            newTask.isCompleted = false;
            newTask.isReshow = false;
            newTask.NoticeStatus = 3;
            newTask.LastModification = new Date();
            newTask.LastUserMod = new Date();
            newTask.Group_Name = task.parentNode.raw.Name;
            newTask.Created = new Date();
            newTask.set('Outline_Level', 2);
            newTask.set('Name', '(Project desig.)');
            newTask.set('leaf', true);
            task.parentNode.set('Object', parseInt(task.parentNode.data.Object) + 1);
            if (task) {
                task.parentNode.insertBefore(newTask, task.nextSibling);
            } else {
                this.grid.taskStore.getRootNode().appendChild(newTask);
            }
        } else {
            newTask.isCriticalTask = false;
            newTask.isClarificationRequired = false;
            newTask.isPassive = false;
            newTask.isCancelled = false;
            newTask.isNotice = false;
            newTask.isCompleted = false;
            newTask.isReshow = false;
            newTask.NoticeStatus = 3;
            newTask.LastModification = new Date();
            newTask.LastUserMod = new Date();
            newTask.Group_Name = task.parentNode.raw.Name;
            newTask.Created = new Date();
            newTask.set('Outline_Level', task.data.Outline_Level);
            newTask.set('Name', '(Task desig.)');
            newTask.set('leaf', task.data.leaf);
            task.parentNode.set('Object', parseInt(task.parentNode.data.Object) + 1);
            if (task) {
                task.parentNode.insertBefore(newTask, task.nextSibling);
            } else {
                this.grid.taskStore.getRootNode().appendChild(newTask);
            }
        }
    },

    critical: function () {
        var task = this.rec;
        var statusBar = Ext.getCmp("basic-statusbar");
        statusBar.showBusy();
        Ext.defer(function () {
            statusBar.setStatus("Ready");
        }, 3000);
        if (task.data.isCriticalTask == true) {
            task.set('isCriticalTask', false);
        } else {
            task.set('isCriticalTask', true);
        }
    },

    reshow: function () {
        var task = this.rec;
        var statusBar = Ext.getCmp("basic-statusbar");
        statusBar.showBusy();
        Ext.defer(function () {
            statusBar.setStatus("Ready");
        }, 3000);
        if (task.data.isReshow == true) {
            task.data.Reshow_Date = null;
            task.set('isReshow', false);
        } else {
            reshowDateWindow.show();
        }
    },

    completed: function () {
        var task = this.rec;
        var statusBar = Ext.getCmp("basic-statusbar");
        statusBar.showBusy();
        task.expand();
        Ext.defer(function () {
            statusBar.setStatus("Ready");
            if (task.data.isCompleted != true) {
                makeCompletedTasks(task);
                task.set('PercentDone', 100);
                task.set('isCompleted', true);
                task.set('Completed', new Date());
            } else {
                makeDefaultTasks(task);
                task.set('PercentDone', 0);
                task.set('isCompleted', false);
                task.set('Completed', null);
            }
            function makeCompletedTasks(node) {
                for (var i = 0; i < node.childNodes.length; i++) {
                    node.expand();
                    var child = node.childNodes[i];
                    makeCompletedTasks(child);
                    completedTasks(child);
                }
            }
            function makeDefaultTasks(node) {
                for (var i = 0; i < node.childNodes.length; i++) {
                    node.expand();
                    var child = node.childNodes[i];
                    makeDefaultTasks(child);
                    defaultTasks(child);
                }
            }

            function completedTasks(child) {
                child.set('PercentDone', 100);
                child.set('isCompleted', true);
                child.set('Completed', new Date());
            }
            function defaultTasks(child) {
                child.set('PercentDone', 0);
                child.set('isCompleted', false);
                child.set('Completed', null);
            }
        }, 1000);
    },

    notclear: function () {
        var task = this.rec;
        var statusBar = Ext.getCmp("basic-statusbar");
        statusBar.showBusy();
        Ext.defer(function () {
            statusBar.setStatus("Ready");
        }, 3000);
        if (task.data.isClarificationRequired == true) {
            task.set('isClarificationRequired', false);
        } else {
            task.set('isClarificationRequired', true);
        }
    },

    noticearrow: function () {
        var task = this.rec;
        var statusBar = Ext.getCmp("basic-statusbar");
        statusBar.showBusy();
        Ext.defer(function () {
            statusBar.setStatus("Ready");
        }, 3000);
        if (task.data.isNotice == true) {
            task.set('isNotice', false);
        } else {
            task.set('isNotice', true);
        }
    },

    passive: function () {
        var task = this.rec;
        var statusBar = Ext.getCmp("basic-statusbar");
        statusBar.showBusy();
        task.expand();
        Ext.defer(function () {
            statusBar.setStatus("Ready");
            if (task.data.isPassive != true) {
                makeisPassiveTasks(task);
                task.set('isPassive', true);
                task.set('isCancelled', false);
            } else {
                makeDefaultTasks(task);
                task.set('isPassive', false);
            }
            function makeisPassiveTasks(node) {
                for (var i = 0; i < node.childNodes.length; i++) {
                    node.expand();
                    var child = node.childNodes[i];
                    makeisPassiveTasks(child);
                    passiveTasks(child);
                }
            }
            function makeDefaultTasks(node) {
                for (var i = 0; i < node.childNodes.length; i++) {
                    node.expand();
                    var child = node.childNodes[i];
                    makeDefaultTasks(child);
                    defaultTasks(child);
                }
            }
            function passiveTasks(child) {
                child.set('isPassive', true);
                child.set('isCancelled', false);
            }
            function defaultTasks(child) {
                child.set('isPassive', false);
            }
        }, 1000);
    },

    cancelled: function () {
        var task = this.rec;
        var statusBar = Ext.getCmp("basic-statusbar");
        statusBar.showBusy();
        task.expand();
        Ext.defer(function () {
            statusBar.setStatus("Ready");
            if (task.data.isCancelled != true) {
                makeCancelledTasks(task);
                task.set('isCancelled', true);
                task.set('isPassive', false);
            } else {
                makeDefaultTasks(task);
                task.set('isCancelled', false);
            }
            function makeCancelledTasks(node) {
                for (var i = 0; i < node.childNodes.length; i++) {
                    node.expand();
                    var child = node.childNodes[i];
                    makeCancelledTasks(child);
                    cancelledTasks(child);
                }
            }
            function makeDefaultTasks(node) {
                for (var i = 0; i < node.childNodes.length; i++) {
                    node.expand();
                    var child = node.childNodes[i];
                    makeDefaultTasks(child);
                    defaultTasks(child);
                }
            }
            function cancelledTasks(child) {
                child.set('isCancelled', true);
                child.set('isPassive', false);
            }
            function defaultTasks(child) {
                child.set('isCancelled', false);
            }
        }, 1000);
    },

    paste: function () {
        var task = this.rec;
        console.log(this.copiedTasks);

        if (task) {
            this.addTaskBelow(this.copiedTasks);
        }
        else {
            this.grid.taskStore.getRootNode().appendChild(this.copiedTasks);
        }
        this.copiedTasks = this.copyTaskWithChildren(this.copiedTasks);
    },

    copy: function () {
        var me = this;
        var tasks = this.grid.getSelectionModel().selected;
        tasks.each(function (t) {
            if (!tasks.contains(t.parentNode)) {
                me.copiedTasks = me.copyTaskWithChildren(t);
            }
        });
    },

    cut: function () {
        var me = this;
        var tasks = this.grid.getSelectionModel().selected;
        tasks.each(function (t) {
            if (!tasks.contains(t.parentNode)) {
                me.copiedTasks = me.copyTaskWithChildren(t);
                me.grid.taskStore.remove(tasks.getRange());
            }
        });
    },

    lock: function () {
        var task = this.rec;
        var statusBar = Ext.getCmp("basic-statusbar");
        statusBar.showBusy();
        Ext.defer(function () {
            statusBar.setStatus("Ready");
        }, 3000);
        if (task.data.Lock == 0) {
            task.set('Lock', 1);
        } else {
            task.set('Lock', 0);
        }
    },

    copyTaskWithChildren: function (original) {
        var me = this;
        var model = this.grid.getTaskStore().model;

        var newTask = new model({
        });

        newTask.set('leaf', (original && original.get('leaf')) || null);
        newTask.set('expanded', (original && original.get('expanded')) || null);
        newTask.setName((original && original.getName()) || null);
        newTask.set(newTask.startDateField, (original && original.getStartDate()) || null);
        newTask.set(newTask.endDateField, (original && original.getEndDate()) || null);
        newTask.set(newTask.percentDoneField, (original && original.getPercentDone()) || null);
        newTask.set(newTask.durationField, (original && original.getDuration()) || null);
        newTask.set(newTask.durationUnitField, (original && original.getDurationUnit()) || 'd');
        newTask.set(newTask.effortField, (original && original.getEffort()) || null);
        newTask.set(newTask.effortUnitField, (original && original.getEffortUnit()) || 'h');
        newTask.set(newTask.schedulingModeField, (original && original.getSchedulingMode()) || 'Normal');
        newTask.set(newTask.clsField, (original && original.getCls()) || null);
        //newTask.set('parentId', (original && original.get('parentId')) || null);
        //newTask.set('Priority', (original && original.get('Priority')) || null);
        //newTask.set('index', (original && original.get('index')) || null);
        newTask.set('isCritical', (original && original.get('isCritical')) || null);
        newTask.set('isClarificationRequired', (original && original.get('isClarificationRequired')) || null);
        newTask.set('LimitDate', (original && original.get('LimitDate')) || null);
        newTask.set('isNotice', (original && original.get('isNotice')) || null);
        newTask.set('isReshow', (original && original.get('isReshow')) || null);
        newTask.set('isCompleted', (original && original.get('isCompleted')) || null);
        newTask.set('isPassive', (original && original.get('isPassive')) || null);
        newTask.set('isCancelled', (original && original.get('isCancelled')) || null);
        newTask.set('Act_Fixed_Cost', (original && original.get('Act_Fixed_Cost')) || null);
        newTask.set('Contact', (original && original.get('Contact')) || null);
        newTask.set('Responsible', (original && original.get('Responsible')) || null);
        newTask.set('Resource_Names', (original && original.get('Resource_Names')) || null);
        newTask.set('Created', (original && original.get('Created')) || null);
        newTask.set('Reshow_Date', (original && original.get('Reshow_Date')) || null);
        newTask.set('Capacity_usage', (original && original.get('Capacity_usage')) || null);
        newTask.set('Actual_Start', (original && original.get('Actual_Start')) || null);
        newTask.set('Object', (original && original.get('Object')) || null);
        newTask.set('Actual_Finish', (original && original.get('Actual_Finish')) || null);
        newTask.set('Rem_Time', (original && original.get('Rem_Time')) || null);
        newTask.set('Completed', (original && original.get('Completed')) || null);
        newTask.set('Actual_Duration', (original && original.get('Actual_Duration')) || null);
        newTask.set('Duration_Act_div_Pld', (original && original.get('Duration_Act_div_Pld')) || null);
        newTask.set('Duration_Act_Pld', (original && original.get('Duration_Act_Pld')) || null);
        newTask.set('Req_Effort', (original && original.get('Req_Effort')) || null);
        newTask.set('Req_Ress', (original && original.get('Req_Ress')) || null);
        newTask.set('Effort', (original && original.get('Effort')) || null);
        newTask.set('Actual_Effort', (original && original.get('Actual_Effort')) || null);
        newTask.set('Effort_Pld_Req', (original && original.get('Effort_Pld_Req')) || null);
        newTask.set('Effort_Pld_div_Req', (original && original.get('Effort_Pld_div_Req')) || null);
        newTask.set('Effort_Act_Pld1', (original && original.get('Effort_Act_Pld1')) || null);
        newTask.set('Effort_Act_Pld', (original && original.get('Effort_Act_Pld')) || null);
        newTask.set('Ovt_Effort', (original && original.get('Ovt_Effort')) || null);
        newTask.set('Actual_Ovt_Effort', (original && original.get('Actual_Ovt_Effort')) || null);
        newTask.set('Fixed_Cost', (original && original.get('Fixed_Cost')) || null);
        newTask.set('Standard_Rate', (original && original.get('Standard_Rate')) || null);
        newTask.set('Act_Standard_Rate', (original && original.get('Act_Standard_Rate')) || null);
        newTask.set('Labor_Cost', (original && original.get('Labor_Cost')) || null);
        newTask.set('Act_Labor_Cost', (original && original.get('Act_Labor_Cost')) || null);
        newTask.set('Ovt_Cost', (original && original.get('Ovt_Cost')) || null);
        newTask.set('Act_Ovt_Cost', (original && original.get('Act_Ovt_Cost')) || null);
        newTask.set('Total_Cost', (original && original.get('Total_Cost')) || null);
        newTask.set('Act_Total_Cost', (original && original.get('Act_Total_Cost')) || null);
        newTask.set('Total_Cost_Act_div_Pld', (original && original.get('Total_Cost_Act_div_Pld')) || null);
        newTask.set('Total_Cost_Act_Pld', (original && original.get('Total_Cost_Act_Pld')) || null);
        newTask.set('Cost_Calc_Meth', (original && original.get('Cost_Calc_Meth')) || null);
        newTask.set('Time_Unit_of_Cost', (original && original.get('Time_Unit_of_Cost')) || null);
        newTask.set('Time_Unit_of_Effort', (original && original.get('Time_Unit_of_Effort')) || null);
        newTask.set('Time_Unit_of_Duration', (original && original.get('Time_Unit_of_Duration')) || null);
        newTask.set('Text1', (original && original.get('Text1')) || null);
        newTask.set('Text2', (original && original.get('Text2')) || null);
        newTask.set('Text3', (original && original.get('Text3')) || null);
        newTask.set('Text4', (original && original.get('Text4')) || null);
        newTask.set('Text5', (original && original.get('Text5')) || null);
        newTask.set('Text6', (original && original.get('Text6')) || null);
        newTask.set('Text7', (original && original.get('Text7')) || null);
        newTask.set('Text8', (original && original.get('Text8')) || null);
        newTask.set('Text9', (original && original.get('Text9')) || null);
        newTask.set('Text10', (original && original.get('Text10')) || null);
        newTask.set('Number1', (original && original.get('Number1')) || null);
        newTask.set('Number2', (original && original.get('Number2')) || null);
        newTask.set('Number3', (original && original.get('Number3')) || null);
        newTask.set('Number4', (original && original.get('Number4')) || null);
        newTask.set('Number5', (original && original.get('Number5')) || null);
        newTask.set('Number6', (original && original.get('Number6')) || null);
        newTask.set('Number7', (original && original.get('Number7')) || null);
        newTask.set('Number8', (original && original.get('Number8')) || null);
        newTask.set('Number9', (original && original.get('Number9')) || null);
        newTask.set('Number10', (original && original.get('Number10')) || null);
        newTask.set('Finished_pr', (original && original.get('Finished_pr')) || null);
        newTask.set('End_progn', (original && original.get('End_progn')) || null);
        newTask.set('Dur_progn', (original && original.get('Dur_progn')) || null);
        newTask.set('Work_progn', (original && original.get('Work_progn')) || null);
        newTask.set('EMail', (original && original.get('EMail')) || null);
        newTask.set('Qty_Pld', (original && original.get('Qty_Pld')) || null);
        newTask.set('Qty_durch_Time_Pld', (original && original.get('Qty_durch_Time_Pld')) || null);
        newTask.set('Qty_Act', (original && original.get('Qty_Act')) || null);
        newTask.set('Qty_durch_Time_Act', (original && original.get('Qty_durch_Time_Act')) || null);
        newTask.set('Factor', (original && original.get('Factor')) || null);
        newTask.set('Buffer', (original && original.get('Buffer')) || null);
        newTask.set('Buffer_cum', (original && original.get('Buffer_cum')) || null);
        newTask.set('Buffer_Act', (original && original.get('Buffer_Act')) || null);
        newTask.set('Buffer_Act_cum', (original && original.get('Buffer_Act_cum')) || null);
        newTask.set('Constraint_Type', (original && original.get('Constraint_Type')) || null);
        newTask.set('Successors', (original && original.get('Successors')) || null);
        newTask.set('Group_Name', (original && original.get('Group_Name')) || null);
        //newTask.set('Ord_ID', (original && original.get('Ord_ID')) || null);
        //newTask.set('Pro_ID', (original && original.get('Pro_ID')) || null);
        //newTask.set('Vor_ID', (original && original.get('Vor_ID')) || null);
        //newTask.set('Ord_ID2', (original && original.get('Ord_ID2')) || null);
        //newTask.set('Pro_ID2', (original && original.get('Pro_ID2')) || null);
        //newTask.set('Vor_ID2', (original && original.get('Vor_ID2')) || null);
        newTask.set('Rec_Options', (original && original.get('Rec_Options')) || null);
        //newTask.set('Outline_Level', (original && original.get('Outline_Level')) || null);
        newTask.set('NoticeStatus', (original && original.get('NoticeStatus')) || null);
        newTask.set('Auftr_ID', (original && original.get('Auftr_ID')) || null);
        newTask.set('Bearb_ID', (original && original.get('Bearb_ID')) || null);
        newTask.set('LastUserMod', (original && original.get('LastUserMod')) || null);
        newTask.set('LastModification', (original && original.get('LastModification')) || null);
        newTask.set('LastUser', (original && original.get('LastUser')) || null);

        original.eachChild(function (t) {
            newTask.addSubtask(me.copyTaskWithChildren(t));
        });

        return newTask;
    },

    // actions follow below
    deleteTask: function () {
        var e = this.grid.getSelectionModel().selected;
        var statusBar = Ext.getCmp("basic-statusbar");
        statusBar.showBusy();
        Ext.defer(function () {
            statusBar.setStatus("Ready");
        }, 500);
        this.grid.taskStore.remove(e.getRange());
    },

    editLeftLabel: function () {
        this.grid.getSchedulingView().editLeftLabel(this.rec);
    },

    editRightLabel: function () {
        this.grid.getSchedulingView().editRightLabel(this.rec);
    },

    converToMilestone: function () {
        var task = this.rec;
        var statusBar = Ext.getCmp("basic-statusbar");
        statusBar.showBusy();
        Ext.defer(function () {
            statusBar.setStatus("Ready");
        }, 1000);
        task.setStartDate(task.getEndDate(), false);
    },

    converToRegularTask: function () {
        var task = this.rec;
        task.set({
            StartDate: task.calculateStartDate(task.getStartDate(), 1, Sch.util.Date.DAY),
            EndDate: task.getStartDate(),
            Duration: 1,
            DurationUnit: Sch.util.Date.DAY
        });
        //task.EndDate=task.startDate
        var statusBar = Ext.getCmp("basic-statusbar");
        statusBar.showBusy();
        Ext.defer(function () {
            statusBar.setStatus("Ready");
        }, 1000);
    },

    /**
    * Handler for the "add task above" menu item
    */
    addTaskAboveAction: function () {
        this.addTaskAbove(this.copyTask(this.rec));
    },

    /**
    * Handler for the "add task below" menu item
    */
    addTaskBelowAction: function () {
        this.addTaskBelow(this.copyTask(this.rec));
    },

    OnLevel1Action: function (newTask) {
        var tasks = this.grid.getSelectionModel().selected;
        var statusBar = Ext.getCmp("basic-statusbar");
        statusBar.showBusy();
        Ext.defer(function () {
            statusBar.setStatus("Ready");
        }, 1000);
        newTask.Outline_Level = 1;
        newTask.leaf = true;
        newTask.Name = '(Folder desig.)';
        newTask.isCriticalTask = false;
        newTask.isClarificationRequired = false;
        newTask.isPassive = false;
        newTask.isCancelled = false;
        newTask.isNotice = false;
        newTask.isCompleted = false;
        newTask.isReshow = false;
        newTask.NoticeStatus = 3;
        newTask.LastModification = new Date();
        newTask.LastUserMod = new Date();
        newTask.Group_Name = newTask.Name;
        newTask.Created = new Date();
        if (tasks.items.length > 0) {
            newTask.parentId = tasks.items[0].data.Id;
        } else {
            newTask.parentId = 0;
        }
        this.grid.taskStore.getRootNode().appendChild(newTask);
    },

    OnLevel2Action: function (newTask) {
        var task = this.rec;

        var statusBar = Ext.getCmp("basic-statusbar");
        statusBar.showBusy();
        Ext.defer(function () {
            statusBar.setStatus("Ready");
        }, 1000);
        if (task.data.Outline_Level == 1) {
            newTask.Outline_Level = 2;
            newTask.leaf = true;
            newTask.Name = '(Project desig.)';
            newTask.isCriticalTask = false;
            newTask.isClarificationRequired = false;
            newTask.isPassive = false;
            newTask.isCancelled = false;
            newTask.isNotice = false;
            newTask.isCompleted = false;
            newTask.isReshow = false;
            newTask.NoticeStatus = 3;
            newTask.LastModification = new Date();
            newTask.LastUserMod = new Date();
            newTask.Group_Name = task.Name;
            newTask.Created = new Date();
            task.set('leaf', false);
            task.set('Object', parseInt(task.data.Object) + 1);
            task.appendChild(newTask);
            task.expand();
        }
        else if (task.data.Outline_Level == 2) {
            newTask.isCriticalTask = false;
            newTask.isClarificationRequired = false;
            newTask.isPassive = false;
            newTask.isCancelled = false;
            newTask.isNotice = false;
            newTask.isCompleted = false;
            newTask.isReshow = false;
            newTask.NoticeStatus = 3;
            newTask.LastModification = new Date();
            newTask.LastUserMod = new Date();
            newTask.Group_Name = task.parentNode.raw.Name;
            newTask.Created = new Date();
            newTask.Outline_Level = 2;
            newTask.Name = '(Project desig.)';
            newTask.leaf = true;
            task.parentNode.set('Object', parseInt(task.parentNode.data.Object) + 1);
            if (task) {
                task.parentNode.insertBefore(newTask, task.nextSibling);
            } else {
                this.grid.taskStore.getRootNode().appendChild(newTask);
            }
        } else if (task.data.Outline_Level >= 3) {
            var parentTask;
            findRootParentNode(task);
            function findRootParentNode(t) {
                if (t.parentNode.data.Id > 0) {
                    findRootParentNode(t.parentNode);
                } else {
                    parentTask = t;
                }
            }
            newTask.isCriticalTask = false;
            newTask.isClarificationRequired = false;
            newTask.isPassive = false;
            newTask.isCancelled = false;
            newTask.isNotice = false;
            newTask.isCompleted = false;
            newTask.isReshow = false;
            newTask.NoticeStatus = 3;
            newTask.LastModification = new Date();
            newTask.LastUserMod = new Date();
            newTask.Group_Name = task.parentNode.raw.Name;
            newTask.Created = new Date();
            newTask.Outline_Level = 2;
            newTask.Name = '(Project desig.)';
            newTask.leaf = true;
            parentTask.set('Object', parseInt(parentTask.data.Object) + 1);
            parentTask.appendChild(newTask);
        }
    },

    OnLevel3Action: function (newTask) {
        var task = this.rec;
        var statusBar = Ext.getCmp("basic-statusbar");
        statusBar.showBusy();
        Ext.defer(function () {
            statusBar.setStatus("Ready");
        }, 1000);
        if (task.data.Outline_Level == 2) {
            newTask.Outline_Level = 3;
            newTask.leaf = true;
            newTask.Name = '(Task desig.)';
            newTask.isCriticalTask = false;
            newTask.isClarificationRequired = false;
            newTask.isPassive = false;
            newTask.isCancelled = false;
            newTask.isNotice = false;
            newTask.isCompleted = false;
            newTask.isReshow = false;
            newTask.NoticeStatus = 3;
            newTask.LastModification = new Date();
            newTask.LastUserMod = new Date();
            newTask.Group_Name = task.parentNode.raw.Name;
            newTask.Created = new Date();
            task.set('leaf', false);
            task.set('Object', parseInt(task.data.Object) + 1);
            task.appendChild(newTask);
            task.expand();
        }
        else if (task.data.Outline_Level >= 3) {
            var parentTask;
            findRootParentNode(task);
            function findRootParentNode(t) {
                if (t.parentNode.data.Outline_Level >= 2) {
                    findRootParentNode(t.parentNode);
                } else {
                    parentTask = t;
                }
            }
            newTask.Outline_Level = 3;
            newTask.leaf = true;
            newTask.Name = '(Task desig.)';
            newTask.isCriticalTask = false;
            newTask.isClarificationRequired = false;
            newTask.isPassive = false;
            newTask.isCancelled = false;
            newTask.isNotice = false;
            newTask.isCompleted = false;
            newTask.isReshow = false;
            newTask.NoticeStatus = 3;
            newTask.LastModification = new Date();
            newTask.LastUserMod = new Date();
            newTask.Group_Name = task.parentNode.raw.Name;
            newTask.Created = new Date();
            parentTask.set('leaf', false);
            parentTask.set('Object', parseInt(parentTask.data.Object) + 1);
            parentTask.appendChild(newTask);
            parentTask.expand();
        }

    },

    /**
    * Handler for the "add subtask" menu item
    */
    addSubtask: function (newTask) {
        var task = this.rec;
        var statusBar = Ext.getCmp("basic-statusbar");
        statusBar.showBusy();
        Ext.defer(function () {
            statusBar.setStatus("Ready");
        }, 1000);
        newTask.leaf = true;
        newTask.parentId = task.data.Id;
        newTask.Outline_Level = task.data.Outline_Level + 1;
        if (task.data.Outline_Level == 1) {
            newTask.Name = '(Project desig.)';
        }
        else if (task.data.Outline_Level >= 2) {
            newTask.Name = '(Task desig.)';
        }
        newTask.isCriticalTask = false;
        newTask.isClarificationRequired = false;
        newTask.isPassive = false;
        newTask.isCancelled = false;
        newTask.isNotice = false;
        newTask.isCompleted = false;
        newTask.isReshow = false;
        newTask.NoticeStatus = 3;
        newTask.LastModification = new Date();
        newTask.LastUserMod = new Date();
        newTask.Group_Name = task.parentNode.raw.Name;
        newTask.Created = new Date();
        task.set('leaf', false);
        task.set('Object', parseInt(task.data.Object) + 1);
        task.appendChild(newTask);
        task.expand();
    },

    /**
    * Handler for the "add successor" menu item
    */
    addSuccessor: function () {
        var task = this.rec,
            depStore = this.grid.dependencyStore,
            newTask = this.copyTask(task);

        this.addTaskBelow(newTask);
        var statusBar = Ext.getCmp("basic-statusbar");
        statusBar.showBusy();
        Ext.defer(function () {
            statusBar.setStatus("Ready");
        }, 1000);
        newTask.set('StartDate', task.getEndDate());
        newTask.set('NoticeStatus', '3');
        if (task.data.Outline_Level == 2) {
            newTask.Name = '(Project desig.)';
        }
        else if (task.data.Outline_Level > 2) {
            newTask.Name = '(Task desig.)';
        }
        newTask.setDuration(1, Sch.util.Date.DAY);
        //task.parentNode.set('Object', parseInt(task.parentNode.data.Object) + 1);
        var dependencyClass = depStore.model;

        depStore.add(
            new dependencyClass({
                From: task.getId() || task.internalId,
                To: newTask.getId() || newTask.internalId,
                Type: dependencyClass.EndToStart
            })
        );
    },

    /**
    * Handler for the "add predecessor" menu item
    */
    addPredecessor: function () {
        var task = this.rec;
        var depStore = this.grid.dependencyStore;
        var newTask = this.copyTask(task);

        this.addTaskAbove(newTask);
        var statusBar = Ext.getCmp("basic-statusbar");
        statusBar.showBusy();
        Ext.defer(function () {
            statusBar.setStatus("Ready");
        }, 1000);
        newTask.set('NoticeStatus', '3');
        if (task.data.Outline_Level == 1) {
            newTask.set('Name', '(Folder desig.)');
        }
        else if (task.data.Outline_Level == 2) {
            newTask.set('Name', '(Project desig.)');
        }
        else if (task.data.Outline_Level > 2) {
            newTask.set('Name', '(Task desig.)');
        }
        //task.parentNode.set('Object', parseInt(task.parentNode.data.Object) + 1);
        newTask.set({
            StartDate: newTask.calculateStartDate(task.getStartDate(), 1, Sch.util.Date.DAY),
            EndDate: task.getStartDate(),
            Duration: 1,
            DurationUnit: Sch.util.Date.DAY
        });

        var dependencyClass = depStore.model;

        depStore.add(
            new dependencyClass({
                From: newTask.getId() || newTask.internalId,
                To: task.getId() || task.internalId,
                Type: dependencyClass.EndToStart
            })
        );
    },

    /**
    * Handler for the "add milestone" menu item
    */
    addMilestone: function () {
        var task = this.rec,
            newTask = this.copyTask(task);

        this.addTaskBelow(newTask);
        var statusBar = Ext.getCmp("basic-statusbar");
        statusBar.showBusy();
        Ext.defer(function () {
            statusBar.setStatus("Ready");
        }, 1000);
        if (task.data.Outline_Level == 1) {
            newTask.Name = '(Project desig.)';
        }
        else if (task.data.Outline_Level >= 2) {
            newTask.Name = '(Task desig.)';
        }
        newTask.setStartDate(newTask.getEndDate(), false);
        newTask.set('NoticeStatus', '1');
    },
});


