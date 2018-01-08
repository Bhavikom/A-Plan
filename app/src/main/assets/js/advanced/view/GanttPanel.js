Ext.Loader.setConfig({enabled: true, disableCaching: false});
Ext.Loader.setPath("Ext.ux", "js/ux");
Ext.Loader.setPath("Sch", "js/Sch");
Ext.tip.QuickTipManager.init();
Ext.define("APlan.view.GanttPanel", {
    extend: "Gnt.panel.Gantt",
    requires: ["APlan.model.Dependency", "APlan.model.Task", "APlan.store.Task", "APlan.store.Resource", "APlan.store.Dependency", "APlan.store.Assignment", "Gnt.plugin.TaskEditor", "Gnt.column.ResourceAssignment", "Sch.plugin.TreeCellEditing", "Gnt.plugin.DependencyEditor", "Ext.layout.container.*", "Ext.resizer.Splitter", "Ext.ux.statusbar.StatusBar", "Ext.ux.window.Notification", "Ext.grid.plugin.BufferedRenderer", "Ext.form.Panel", "Gnt.plugin.Printable"],
    mixins: ["Sch.mixin.FilterableTreeView"],
    initComponent: function () {
        var e = function (g, f) {
            f = Ext.getCmp(f);
            f.setStatus(g)
        };
        var a = Ext.create("Ext.toolbar.TextItem", {text: Ext.Date.format(new Date(), "g:i:s A")});
        var d = new Ext.form.ComboBox({
            store: new Ext.data.ArrayStore({
                id: 0,
                fields: ["Id", "displayText"],
                data: [[0, "Low"], [1, "Normal"], [2, "High"]]
            }), triggerAction: "all", mode: "local", valueField: "Id", displayField: "displayText"
        });
        var c = this;
        var b;
        Ext.apply(this, {
            taskBodyTemplate: '<div class="sch-gantt-progress-bar" style="width:{progressBarWidth}px;{progressBarStyle}" unselectable="on"><span class="sch-gantt-progress-bar-label">{[Math.round(values.percentDone)]}%</span></div>',
            plugins: [Ext.create("Ext.grid.plugin.BufferedRenderer", {
                pageSize: 100,
                trailingBufferZone: 50,
                leadingBufferZone: 50,
                numFromEdge: 5,
                purgePageCount: 0,
                scrollToLoadBuffer: 100,
            }), Ext.create("Gnt.plugin.TaskContextMenu"), Ext.create("Gnt.plugin.DependencyEditor", {hideOnBlur: true}), Ext.create("Gnt.plugin.TaskEditor"), {
                ptype: "scheduler_lines",
                innerTpl: '<span class="line-text">{Text}</span>',
                showHeaderElements: true,
                store: new Ext.data.JsonStore({
                    fields: ["Date", "Text", "Cls"],
                    data: [{Date: new Date(2014, 3, 30), Text: "Project kickoff", Cls: "kickoff"}]
                })
            }, new Gnt.plugin.Printable({
                printRenderer: function (h, f) {
                    if (h.isMilestone()) {
                        return
                    } else {
                        if (h.isLeaf()) {
                            var i = f.width - 4, g = Math.floor(i * h.get("PercentDone") / 100);
                            return {progressBarStyle: Ext.String.format("width:{2}px;border-left:{0}px solid #7971E2;border-right:{1}px solid #E5ECF5;", g, i - g, i)}
                        } else {
                            var i = f.width - 2, g = Math.floor(i * h.get("PercentDone") / 100);
                            return {progressBarStyle: Ext.String.format("width:{2}px;border-left:{0}px solid #FFF3A5;border-right:{1}px solid #FFBC00;", g, i - g, i)}
                        }
                    }
                }, beforePrint: function (f) {
                    var g = f.getSchedulingView();
                    this.oldRenderer = g.eventRenderer;
                    g.eventRenderer = this.printRenderer
                }, afterPrint: function (f) {
                    var g = f.getSchedulingView();
                    g.eventRenderer = this.oldRenderer
                }
            }), Ext.create("Sch.plugin.TreeCellEditing", {
                clicksToEdit: 2, listeners: {
                    edit: function () {
                        c.assignmentStore.sync()
                    },
                }
            }), this.depEditor = new Gnt.plugin.DependencyEditor({
                showLag: true,
                constrain: true,
                buttons: [{
                    text: "Ok", scope: this, handler: function () {
                        var f = c.depEditor;
                        f.getForm().updateRecord(f.dependencyRecord);
                        c.depEditor.collapse()
                    }
                }, {
                    text: "Cancel", scope: this, handler: function () {
                        c.depEditor.collapse()
                    }
                }, {
                    text: "Delete", scope: this, handler: function () {
                        var f = c.depEditor, g = c.depEditor.dependencyRecord;
                        c.dependencyStore.remove(g);
                        f.collapse()
                    }
                }]
            })],
            layout: "border",
            lockedGridConfig: {
                width: $(window).width() / 3,
                collapsible: true,
                split: true,
                region: "west",
                scroll: true,
                listeners: {
                    itemexpand: function () {
                        var g = Ext.ComponentQuery.query("#gv")[0];
                        var f = g.columns[0].id;
                        var h = g.columns[1].id;
                        var i = $("#gv-locked-body").contents("div").attr("id");
                        $(document).ready(function () {
                            var j = parseInt($("#" + i).scrollLeft());
                            $("#" + f).css({left: $(this).scrollLeft() + 0, position: "relative", zIndex: 200001});
                            $("#" + h).css({left: $(this).scrollLeft() + 30, position: "relative", zIndex: 200001});
                            $(".x-grid-cell-first").css({left: $(this).scrollLeft() + j});
                            $(".x-grid-cell-treecolumn").css({left: $(this).scrollLeft() + j})
                        })
                    }, itemcollapse: function () {
                        var g = Ext.ComponentQuery.query("#gv")[0];
                        var f = g.columns[0].id;
                        var h = g.columns[1].id;
                        var i = $("#gv-locked-body").contents("div").attr("id");
                        $(document).ready(function () {
                            var j = parseInt($("#" + i).scrollLeft());
                            $("#" + f).css({left: $(this).scrollLeft() + j, position: "relative", zIndex: 200001});
                            $("#" + h).css({left: $(this).scrollLeft() + 30 + j, position: "relative", zIndex: 200001});
                            $(".x-grid-cell-first").css({left: $(this).scrollLeft() + j});
                            $(".x-grid-cell-treecolumn").css({left: $(this).scrollLeft() + j})
                        })
                    }
                }
            },
            schedulerConfig: {region: "center", collapsible: true},
            lockedViewConfig: {plugins: {ptype: "treeviewdragdrop"}},
            tooltipTpl: new Ext.XTemplate('<strong class="tipHeader">{Name}</strong><table class="taskTip"><tr><td>Start:</td> <td align="right">{[values._record.getDisplayStartDate("y-m-d")]}</td></tr><tr><td>End:</td> <td align="right">{[values._record.getDisplayEndDate("y-m-d")]}</td></tr><tr><td>Progress:</td><td align="right">{[ Math.round(values.PercentDone)]}%</td></tr></table>'),
            eventRenderer: function (g, f) {
                if (g.get("Color")) {
                    var h = Ext.String.format("background-color: #{0};border-color:#{0}", g.get("Color"));
                    return {ctcls: f.get("Id")}
                }
            },
            columns: [{
                header: "No",
                name: "SequenceNumber",
                type: "string",
                id: "NoId",
                order: 1,
                tdCls: "nocell",
                sortable: false,
                width: 30,
                locked: true,
                renderer: function (g, f, h) {
                    return h.getSequenceNumber()
                }
            }, {
                xtype: "namecolumn",
                header: "Tasks",
                dataIndex: "Name",
                width: 150,
                id: "NameId",
                tdCls: "namecell",
                sortable: false,
                listeners: {
                    mouseover: function () {
                        e("Task Name", "basic-statusbar")
                    }
                },
                renderer: function (g, f, h) {
                    if (h.get("IsLeaf")) {
                        f.css = "task"
                    } else {
                        f.css = "parent"
                    }
                    if (h.data.Object == "0") {
                        return h.data.Name
                    }
                    return h.data.Name + " (" + h.data.Object + ")"
                }
            }, {
                header: "Status",
                sortable: false,
                id: "StatusId",
                renderer: function (s, j, g) {
                    var l = [];
                    if (g.data.isCancelled == true && g.data.Pro_ID != 0) {
                        return '<img src="Ribbon/images/Icons/12_task_canceled_16.png"> '
                    }
                    if (g.data.isPassive == true && g.data.Pro_ID != 0) {
                        return '<img src="Ribbon/images/Icons/11_task_passive_16.png"> '
                    }
                    if (g.data.Completed != null && g.data.Pro_ID != 0 && g.data.isPassive != true && g.data.isCancelled != true) {
                        l.push("Completed")
                    }
                    if (g.data.Completed == null && g.data.LateFinish != null && g.data.EndDate > g.data.LateFinish && g.data.Pro_ID != 0 && g.data.isPassive != true && g.data.isCancelled != true) {
                        l.push("LimitOverrun")
                    }
                    if (g.data.isPassive != true && g.data.isCancelled != true && g.data.Pro_ID != 0) {
                        if (g.data.Req_Effort > 0 && ((parseFloat(g.data.Work_progn) / parseFloat(g.data.Req_Effort) * 100) - 100).toFixed(1) > 10) {
                            l.push("CapacityTooHigh")
                        }
                        if (((parseFloat(g.data.Work_progn) / parseFloat(g.data.Req_Effort) * 100) - 100).toFixed(1) < -10) {
                            l.push("CapacityTooLow")
                        }
                        if (g.data.EndDate != null && g.data.End_progn > g.data.EndDate) {
                            l.push("DeadlineOverrun")
                        }
                        if (g.data.Reshow_Date != null) {
                            l.push("ReshowDate")
                        }
                    }
                    var p = new Date();
                    var t = p.getDate();
                    var k = p.getMonth() + 1;
                    var n = p.getFullYear();
                    if (t < 10) {
                        t = "0" + t
                    }
                    if (k < 10) {
                        k = "0" + k
                    }
                    p = t + "/" + k + "/" + n;
                    if (g.data.EndDate != null && g.data.StartDate != null) {
                        var h = 0;
                        var f = 0;
                        if (g.data.EndDate > new Date()) {
                            h = 1
                        } else {
                            h = 0
                        }
                        if (g.data.StartDate > new Date()) {
                            f = 1
                        } else {
                            f = 0
                        }
                        if (h == 0 && g.data.Pro_ID != 0 && g.data.isPassive != true && g.data.isCancelled != true) {
                            if (g.data.Total_Cost_Progn - g.data.Total_Cost > 0) {
                                l.push("CostsOverrun")
                            }
                        }
                        if (f == 0 && h == 0 && g.data.Completed == null && g.data.Pro_ID != 0) {
                            l.push("Overrun")
                        }
                    } else {
                        if (g.data.Total_Cost_Progn - g.data.Total_Cost > 0) {
                            l.push("CostsOverrun")
                        }
                    }
                    var q = "";
                    for (var o = 0; o < l.length; o++) {
                        if (l[o] == "Completed") {
                            q = q + '<img src="Ribbon/images/Icons/10_check_16.png">'
                        }
                        if (l[o] == "CostsOverrun") {
                            q = q + '<img src="Ribbon/images/CostsOverrun.png">'
                        }
                        if (l[o] == "DeadlineOverrun") {
                            q = q + '<img src="Ribbon/images/FinalDeadLine.png">'
                        }
                        if (l[o] == "CapacityTooLow") {
                            q = q + '<img src="Ribbon/images/CapicitytoLow.png">'
                        }
                        if (l[o] == "CapacityTooHigh") {
                            q = q + '<img src="Ribbon/images/CapicitytoHigh.png">'
                        }
                        if (l[o] == "Overrun") {
                            q = q + '<img src="Ribbon/images/Icons/04_task_overdue_16.png">'
                        }
                        if (l[o] == "ReshowDate") {
                            q = q + '<img src="Ribbon/images/Icons/19_warning_16.png">'
                        }
                        if (l[o] == "LimitOverrun") {
                            q = q + '<img src="Ribbon/images/Icons/32_Limit_16.png">'
                        }
                    }
                    return q
                },
                editor: new Ext.form.DateField({allowBlank: false, format: "m/d/y"}),
                width: 50,
                listeners: {
                    mouseover: function () {
                        e("Status", "basic-statusbar")
                    }
                }
            }, {
                header: "Traffic Light",
                id: "TrafficLightId",
                sortable: false,
                renderer: function (q, j, g) {
                    var l = [];
                    if ((g.data.isPassive == true || g.data.isCancelled == true) && g.data.Pro_ID != 0) {
                        return ""
                    } else {
                        if (g.data.isCriticalTask == true || g.data.isClarificationRequired == true) {
                            return '<img src="Ribbon/images/Icons/14_trafficlight_yellow_16_2.png">'
                        } else {
                            if (g.data.Completed != null) {
                                l.push("Completed")
                            } else {
                                if (g.data.LateFinish != null && g.data.EndDate > g.data.LateFinish) {
                                    l.push("Overrun")
                                }
                            }
                        }
                    }
                    var p = new Date();
                    var s = p.getDate();
                    var k = p.getMonth() + 1;
                    var n = p.getFullYear();
                    if (s < 10) {
                        s = "0" + s
                    }
                    if (k < 10) {
                        k = "0" + k
                    }
                    p = s + "/" + k + "/" + n;
                    if (g.data.EndDate != null && g.data.StartDate != null) {
                        var h = 0;
                        var f = 0;
                        if (g.data.EndDate > new Date()) {
                            h = 1
                        } else {
                            h = 0
                        }
                        if (g.data.StartDate > new Date()) {
                            f = 1
                        } else {
                            f = 0
                        }
                        if (h == 1 && f == 0 && g.data.Pro_ID != 0 && g.data.isPassive != true && g.data.isCancelled != true) {
                            if (g.data.Req_Effort > 0 && ((parseFloat(g.data.Work_progn) / parseFloat(g.data.Req_Effort) * 100) - 100).toFixed(1) > -30 && ((parseFloat(g.data.Work_progn) / parseFloat(g.data.Req_Effort) * 100) - 100).toFixed(1) < -10) {
                                l.push("CapacityTooHigh")
                            } else {
                                if (g.data.Req_Effort > 0 && ((parseFloat(g.data.Work_progn) / parseFloat(g.data.Req_Effort) * 100) - 100).toFixed(1) > 10) {
                                    l.push("CapacityTooHigh")
                                } else {
                                    if (g.data.Req_Effort > 0 && ((parseFloat(g.data.Work_progn) / parseFloat(g.data.Req_Effort) * 100) - 100).toFixed(1) < -30) {
                                        l.push("CapacityTooLow")
                                    } else {
                                        if (g.data.Total_Cost_Progn - g.data.Total_Cost > 0) {
                                            l.push("CostsOverrun")
                                        } else {
                                            if (g.data.EndDate != null && g.data.End_progn > g.data.EndDate) {
                                                l.push("DeadlineOverrun")
                                            } else {
                                                if (g.data.LateFinish != null && g.data.LateFinish < g.data.EndDate) {
                                                    l.push("LimitOverrun")
                                                } else {
                                                    if (g.data.isPassive != true && g.data.isCancelled != true && g.data.Pro_ID != 0 && g.data.completed == null) {
                                                        l.push("GreenStatus")
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            if (h == 0 && f == 0 && g.data.Pro_ID != 0 && g.data.isPassive != true && g.data.isCancelled != true) {
                                if (g.data.Req_Effort > 0 && g.data.Req_Effort > 0 && ((parseFloat(g.data.Work_progn) / parseFloat(g.data.Req_Effort) * 100) - 100).toFixed(1) > -30 && ((parseFloat(g.data.Work_progn) / parseFloat(g.data.Req_Effort) * 100) - 100).toFixed(1) < -10) {
                                    l.push("CapacityTooLow")
                                } else {
                                    if (g.data.Req_Effort > 0 && ((parseFloat(g.data.Work_progn) / parseFloat(g.data.Req_Effort) * 100) - 100).toFixed(1) > 10) {
                                        l.push("CapacityTooLow")
                                    } else {
                                        if (g.data.Req_Effort > 0 && ((parseFloat(g.data.Work_progn) / parseFloat(g.data.Req_Effort) * 100) - 100).toFixed(1) < -30) {
                                            l.push("CapacityTooLow")
                                        } else {
                                            if (g.data.Total_Cost_Progn - g.data.Total_Cost > 0) {
                                                l.push("CapacityTooLow")
                                            } else {
                                                if (g.data.EndDate != null && g.data.End_progn > g.data.EndDate) {
                                                    l.push("Overrun")
                                                } else {
                                                    if (g.data.LateFinish != null && g.data.LateFinish < g.data.EndDate) {
                                                        l.push("LimitOverrun")
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    for (var o = 0; o < l.length; o++) {
                        if (l[o] == "Completed") {
                            return ""
                        }
                        if (l[o] == "CostsOverrun") {
                            return '<img src="Ribbon/images/Icons/14_trafficlight_yellow_16_2.png">'
                        }
                        if (l[o] == "DeadlineOverrun") {
                            return '<img src="Ribbon/images/Icons/14_trafficlight_yellow_16_2.png">'
                        }
                        if (l[o] == "CapacityTooLow") {
                            return '<img src="Ribbon/images/Icons/13_trafficlight_red_16_2.png">'
                        }
                        if (l[o] == "CapacityTooHigh") {
                            return '<img src="Ribbon/images/Icons/14_trafficlight_yellow_16_2.png">'
                        }
                        if (l[o] == "Overrun") {
                            return '<img src="Ribbon/images/Icons/13_trafficlight_red_16_2.png">'
                        }
                        if (l[o] == "GreenStatus") {
                            return '<img src="Ribbon/images/Icons/15_trafficlight_green_16_2.png">'
                        }
                        if (l[o] == "YellowStatus") {
                            return '<img src="Ribbon/images/Icons/14_trafficlight_yellow_16_2.png">'
                        }
                        if (l[o] == "LimitOverrun") {
                            return '<img src="Ribbon/images/Icons/13_trafficlight_red_16_2.png">'
                        }
                    }
                },
                editor: new Ext.form.DateField({allowBlank: false, format: "m/d/y"}),
                width: 80,
                listeners: {
                    mouseover: function () {
                        e("Traffic Lights", "basic-statusbar")
                    }
                }
            }, {
                header: "Assigned Resources",
                id: "AssignedResourcesId",
                width: 150,
                sortable: false,
                xtype: "resourceassignmentcolumn",
                listeners: {
                    mouseover: function () {
                        e("Assigned Resources", "basic-statusbar")
                    }
                }
            }, {
                xtype: "startdatecolumn",
                header: "Start",
                id: "StartId",
                dataIndex: "StartDate",
                width: 80,
                sortable: false,
                listeners: {
                    mouseover: function () {
                        e("EndDate", "basic-statusbar")
                    }
                },
                renderer: Ext.util.Format.dateRenderer("m/d/Y")
            }, {
                xtype: "enddatecolumn",
                header: "End",
                id: "EndId",
                dataIndex: "EndDate",
                width: 80,
                sortable: false,
                listeners: {
                    mouseover: function () {
                        e("EndDate", "basic-statusbar")
                    }
                },
                renderer: Ext.util.Format.dateRenderer("m/d/Y")
            }, {
                xtype: "durationcolumn",
                id: "durationId",
                sortable: false,
                renderer: function (i, f, j) {
                    if (!Ext.isNumber(i)) {
                        return ""
                    }
                    if (!j.isEditable(this.dataIndex)) {
                        f.tdCls = (f.tdCls || "") + " sch-column-readonly"
                    }
                    var g = j.getDurationUnit();
                    var h = parseFloat((j.data.EndDate - j.data.StartDate) / (1000 * 60 * 60 * 24)).toFixed(1);
                    if (h <= 1) {
                        return h + " day "
                    }
                    return h + " days "
                },
                editor: Ext.create("Gnt.field.Duration", {
                    decimalPrecision: 2,
                    instantUpdate: true,
                    listeners: {
                        change: function (j, i, g, h) {
                            var f = j.task;
                            f.setStartDate(new Date())
                        }, focus: function (f) {
                        }
                    }
                })
            }, {xtype: "effortcolumn", id: "effortId", sortable: false, width: 70}, {
                xtype: "addnewcolumn",
                id: "addnewId",
                sortable: false
            }],
            tbar: [{
                xtype: "textfield", triggerCls: "x-form-clear-trigger1", onTriggerClick: function () {
                    this.setValue("")
                }, emptyText: "Type Id", width: 80, enableKeyEvents: true, listeners: {
                    change: function (j, i, g) {
                        if (i) {
                            var f = Ext.Array.map(i.split(/\s+/), function (k) {
                                return new RegExp(Ext.String.escapeRegex(k), "i")
                            });
                            var h = f.length;
                            c.taskStore.filterTreeBy({
                                filter: function (m) {
                                    var l = m.get("Id");
                                    for (var k = 0; k < h; k++) {
                                        if (!f[k].test(l)) {
                                            return false
                                        }
                                    }
                                    return true
                                }, checkParents: true
                            })
                        } else {
                            c.taskStore.clearTreeFilter()
                        }
                    }, specialkey: function (h, g, f) {
                        if (g.keyCode === g.ESC) {
                            h.reset()
                        }
                    }
                }
            }, {
                xtype: "textfield", triggerCls: "x-form-clear-trigger", onTriggerClick: function () {
                    this.setValue("")
                }, emptyText: "Type Task Name", width: 150, enableKeyEvents: true, listeners: {
                    change: function (j, i, g) {
                        if (i) {
                            var f = Ext.Array.map(i.split(/\s+/), function (k) {
                                return new RegExp(Ext.String.escapeRegex(k), "i")
                            });
                            var h = f.length;
                            c.taskStore.filterTreeBy({
                                filter: function (m) {
                                    var k = m.get("Name");
                                    for (var l = 0; l < h; l++) {
                                        if (!f[l].test(k)) {
                                            return false
                                        }
                                    }
                                    return true
                                }, checkParents: true
                            })
                        } else {
                            c.taskStore.clearTreeFilter()
                        }
                    }, specialkey: function (h, g, f) {
                        if (g.keyCode === g.ESC) {
                            h.reset()
                        }
                    }
                }
            }],
            dockedItems: [Ext.create("Ext.ux.StatusBar", {
                id: "basic-statusbar",
                dock: "bottom",
                defaultText: "Default status text",
                text: "Ready",
                iconCls: "x-status-valid",
                items: [{
                    id: "totalTasks",
                    dock: "bottom",
                    defaultText: "Default status text",
                    align: "left",
                    text: "TotalTasks",
                    iconCls: "x-status-valid",
                    xtype: "label",
                    width: 200
                }, {
                    xtype: "slider",
                    cls: "sliderStyle",
                    width: 100,
                    value: 0,
                    increment: 1,
                    minValue: 0,
                    maxValue: 10,
                    listeners: {
                        change: function (h, f) {
                            var g = Ext.ComponentQuery.query("#gv")[0];
                            g.zoomToLevel(f)
                        }
                    }
                }, a],
                listeners: {
                    render: {
                        fn: function () {
                            Ext.fly(a.getEl().parent()).addCls("x-status-text-panel").createChild({cls: "spacer"});
                            Ext.TaskManager.start({
                                run: function () {
                                    Ext.fly(a.getEl()).update(Ext.Date.format(new Date(), "g:i:s A"))
                                }, interval: 1000
                            })
                        }, delay: 100
                    }
                }
            })],
            listeners: {
                render: function (f) {
                    var g = f.getSchedulingView().headerCt;
                    f.tip = Ext.create("Ext.tip.ToolTip", {
                        target: g.id,
                        delegate: ".sch-simple-timeheader",
                        showDelay: 0,
                        trackMouse: true,
                        anchor: "bottom",
                        dateFormat: "Y-m-d",
                        renderTo: Ext.getBody(),
                        listeners: {
                            beforeshow: function (k) {
                                var j = Ext.get(k.triggerElement), h = j.getXY(), i = f.getSchedulingView().getDateFromXY(h);
                                k.update(Ext.Date.format(i, k.dateFormat))
                            }
                        }
                    })
                }, itemmouseenter: function () {
                    var g = Ext.ComponentQuery.query("#gv")[0];
                    var f = g.columns[0].id;
                    var h = g.columns[1].id;
                    var i = $("#gv-locked-body").contents("div").attr("id");
                    $(document).ready(function () {
                        var j = $("#" + i).scrollLeft();
                        $(".nocell").css({left: $(this).scrollLeft() + j,});
                        $(".namecell").css({left: $(this).scrollLeft() + j,});
                        $("#" + f).css({left: $(this).scrollLeft() + j, position: "relative", zIndex: 200001});
                        $("#" + h).css({left: $(this).scrollLeft() + 30 + j, position: "relative", zIndex: 200001})
                    })
                }, containermouseout: function () {
                    var g = Ext.ComponentQuery.query("#gv")[0];
                    var f = g.columns[0].id;
                    var h = g.columns[1].id;
                    var i = $("#gv-locked-body").contents("div").attr("id");
                    $(document).ready(function () {
                        var j = $("#" + i).scrollLeft();
                        $(".nocell").css({left: $(this).scrollLeft() + j,});
                        $(".namecell").css({left: $(this).scrollLeft() + j,});
                        $("#" + f).css({left: $(this).scrollLeft() + j, position: "relative", zIndex: 200001});
                        $("#" + h).css({left: $(this).scrollLeft() + 30 + j, position: "relative", zIndex: 200001})
                    })
                }, beforeitemmouseenter: function () {
                    var g = Ext.ComponentQuery.query("#gv")[0];
                    var f = g.columns[0].id;
                    var h = g.columns[1].id;
                    var i = $("#gv-locked-body").contents("div").attr("id");
                    $(document).ready(function () {
                        var j = $("#" + i).scrollLeft();
                        $(".nocell").css({left: $(this).scrollLeft() + j,});
                        $(".namecell").css({left: $(this).scrollLeft() + j,});
                        $("#" + f).css({left: $(this).scrollLeft() + j, position: "relative", zIndex: 200001});
                        $("#" + h).css({left: $(this).scrollLeft() + 30 + j, position: "relative", zIndex: 200001})
                    })
                }, itemmouseleave: function () {
                    var g = Ext.ComponentQuery.query("#gv")[0];
                    var f = g.columns[0].id;
                    var h = g.columns[1].id;
                    var i = $("#gv-locked-body").contents("div").attr("id");
                    $(document).ready(function () {
                        var j = $("#" + i).scrollLeft();
                        $(".nocell").css({left: $(this).scrollLeft() + j,});
                        $(".namecell").css({left: $(this).scrollLeft() + j,});
                        $("#" + f).css({left: $(this).scrollLeft() + j, position: "relative", zIndex: 200001});
                        $("#" + h).css({left: $(this).scrollLeft() + 30 + j, position: "relative", zIndex: 200001})
                    })
                }, containermouseover: function () {
                    var g = Ext.ComponentQuery.query("#gv")[0];
                    var f = g.columns[0].id;
                    var h = g.columns[1].id;
                    var i = $("#gv-locked-body").contents("div").attr("id");
                    $(document).ready(function () {
                        var j = $("#" + i).scrollLeft();
                        $(".nocell").css({left: $(this).scrollLeft() + j,});
                        $(".namecell").css({left: $(this).scrollLeft() + j,});
                        $("#" + f).css({left: $(this).scrollLeft() + j, position: "relative", zIndex: 200001});
                        $("#" + h).css({left: $(this).scrollLeft() + 30 + j, position: "relative", zIndex: 200001})
                    })
                }, staterestore: function (f, h, g) {
                    f.lockedGrid.setWidth(f.lockedGridConfig.width)
                }, itemclick: function (i, l, j, k) {
                    var m = document.getElementById("resourceScheduler");
                    if (m.style.display != "none") {
                        var g = Ext.ComponentQuery.query("#gv")[0];
                        var h = Ext.ComponentQuery.query("#resourceScheduler")[0];
                        var n = g.taskStore.getById(l.data.Id);
                        if (n.data.Resource_Names) {
                            var f = h.getSchedulingView();
                            f.scrollEventIntoView(n, {duration: 3000}, true)
                        }
                    }
                },
            }
        });
        c.on("zoomchange", function (f, g) {
            b.setValue(g)
        });
        this.callParent(arguments)
    },
    showFullScreen: function () {
        this.el.down(".x-panel-body").dom[this._fullScreenFn]()
    },
    applyPercentDone: function (a) {
        this.gantt.getSelectionModel().selected.each(function (b) {
            b.setPercentDone(a)
        })
    },
    eventRenderer: function (a) {
        var b;
        switch (a.get("Priority")) {
            case TaskPriority.Low:
                b = "sch-gantt-prio-low";
                break;
            case TaskPriority.Normal:
                b = "sch-gantt-prio-normal";
                break;
            case TaskPriority.High:
                b = "sch-gantt-prio-high";
                break
        }
        return {cls: b}
    },
    _fullScreenFn: (function () {
        var a = document.documentElement;
        if (a.requestFullscreen) {
            return "requestFullscreen"
        } else {
            if (a.mozRequestFullScreen) {
                return "mozRequestFullScreen"
            } else {
                if (a.webkitRequestFullScreen) {
                    return "webkitRequestFullScreen"
                }
            }
        }
    })()
});
var reshowDateWindow = Ext.create("Ext.menu.DatePicker", {
    handler: function (d, b) {
        var c = Ext.ComponentQuery.query("#gv")[0];
        var a = c.getSelectionModel().selected;
        a.items[0].data.isReshow = true;
        a.items[0].set("Reshow_Date", b)
    }
});
Ext.define("Gnt.plugin.TaskContextMenu", {
    extend: "Ext.menu.Menu",
    requires: ["Gnt.model.Dependency"],
    plain: true,
    triggerEvent: "taskcontextmenu",
    texts: {
        cut: "Cut",
        copy: "Copy",
        paste: "Paste",
        critical: "Critical",
        reshow: "Reshow",
        completed: "Completed",
        notclear: "Not Clear",
        noticearrow: "Notice arrow",
        passive: "Passive",
        cancelled: "Cancelled",
        donotfilter: "Do not filter",
        lock: "Lock",
        masterlock: "Master lock",
        pagebreak: "Page break",
        newTaskText: "Add Component",
        newMilestoneText: "New milestone",
        deleteTask: "Delete Component(s)",
        editLeftLabel: "Edit left label",
        editRightLabel: "Edit right label",
        convert: "Convert",
        converToMilestone: "Convert to Milestone",
        converToRegularTask: "Convert to Regular Task",
        add: "Add",
        deleteDependency: "Delete dependency",
        addTaskBelow: "Same Level",
        addSubtask: "on next lower level",
        OnLevel1: "OnLevel1(Folder)",
        OnLevel2: "OnLevel2(Project)",
        OnLevel3: "OnLevel3(Task)",
        addMilestone: "Milestone",
        addSuccessor: "Successor",
        addPredecessor: "Predecessor",
    },
    Id: "TaskContextMenu",
    grid: null,
    rec: null,
    lastHighlightedItem: null,
    createMenuItems: function () {
        var a = this.texts;
        return [{
            itemId: "contextmenu-Cut",
            handler: this.cut,
            requiresTask: true,
            scope: this,
            icon: "Ribbon/images/Cut.png",
            text: a.cut
        }, {
            itemId: "contextmenu-Copy",
            handler: this.copy,
            requiresTask: true,
            scope: this,
            icon: "Ribbon/images/Copy.png",
            text: a.copy,
        }, {
            itemId: "contextmenu-Paste",
            handler: this.paste,
            requiresTask: true,
            scope: this,
            icon: "Ribbon/images/icon_paste.png",
            text: a.paste
        }, {
            itemId: "contextmenu-Critical",
            handler: this.critical,
            requiresTask: true,
            scope: this,
            icon: "Ribbon/images/Icons/17_flash_16.png",
            text: a.critical
        }, {
            itemId: "contextmenu-Reshow",
            handler: this.reshow,
            requiresTask: true,
            scope: this,
            icon: "Ribbon/images/Icons/19_warning_16.png",
            text: a.reshow
        }, {
            itemId: "contextmenu-Completed",
            handler: this.completed,
            requiresTask: true,
            scope: this,
            icon: "Ribbon/images/Icons/10_check_16.png",
            text: a.completed
        }, {
            itemId: "contextmenu-NotClear",
            handler: this.notclear,
            requiresTask: true,
            scope: this,
            icon: "Ribbon/images/Icons/18_question_16.png",
            text: a.notclear
        }, {
            itemId: "contextmenu-NoticeArrow",
            handler: this.noticearrow,
            requiresTask: true,
            scope: this,
            icon: "Ribbon/images/Icons/16_arrow_16.png",
            text: a.noticearrow
        }, {
            itemId: "contextmenu-Passive",
            handler: this.passive,
            requiresTask: true,
            scope: this,
            icon: "Ribbon/images/Icons/11_task_passive_16.png",
            text: a.passive,
        }, {
            itemId: "contextmenu-Cancelled",
            handler: this.cancelled,
            requiresTask: true,
            scope: this,
            icon: "Ribbon/images/Icons/12_task_canceled_16.png",
            text: a.cancelled
        }, {
            itemId: "contextmenu-DoNotFilter",
            handler: this.donotfilter,
            requiresTask: true,
            scope: this,
            icon: "Ribbon/images/DonotFilter.png",
            text: a.donotfilter
        }, {
            itemId: "contextmenu-Lock",
            handler: this.lock,
            requiresTask: true,
            scope: this,
            icon: "Ribbon/images/Lock.png",
            text: a.lock
        }, {
            itemId: "contextmenu-MasterLock",
            handler: this.masterlock,
            requiresTask: true,
            scope: this,
            icon: "Ribbon/images/MasterLock.png",
            text: a.masterlock
        }, {
            itemId: "contextmenu-PageBreak",
            handler: this.pagebreak,
            requiresTask: true,
            scope: this,
            icon: "Ribbon/images/PageBreak.png",
            text: a.pagebreak
        }, {
            itemId: "contextmenu-DeleteTask",
            handler: this.deleteTask,
            requiresTask: true,
            scope: this,
            icon: "Ribbon/images/Icons/12_task_canceled_16.png",
            text: a.deleteTask
        }, {
            itemId: "contextmenu-EditLeftLable",
            handler: this.editLeftLabel,
            requiresTask: true,
            scope: this,
            text: a.editLeftLabel
        }, {
            itemId: "contextmenu-EditRightLable",
            handler: this.editRightLabel,
            requiresTask: true,
            scope: this,
            text: a.editRightLabel
        }, {
            text: a.convert,
            itemId: "contextmenu-Convert",
            menu: {
                plain: true,
                items: [{
                    itemId: "contextmenu-ConvertToMilestone",
                    handler: this.converToMilestone,
                    requiresTask: true,
                    scope: this,
                    text: a.converToMilestone
                }, {
                    itemId: "contextmenu-ConvertToRegularTask",
                    handler: this.converToRegularTask,
                    requiresTask: true,
                    scope: this,
                    text: a.converToRegularTask
                }]
            }
        }, {
            text: a.add,
            itemId: "contextmenu-Add",
            menu: {
                plain: true,
                items: [{
                    itemId: "contextmenu-OnLevel1",
                    handler: this.OnLevel1Action,
                    scope: this,
                    text: a.OnLevel1
                }, {
                    itemId: "contextmenu-OnLevel2",
                    handler: this.OnLevel2Action,
                    requiresTask: true,
                    scope: this,
                    text: a.OnLevel2
                }, {
                    itemId: "contextmenu-OnLevel3",
                    handler: this.OnLevel3Action,
                    requiresTask: true,
                    scope: this,
                    text: a.OnLevel3
                }, {
                    itemId: "contextmenu-AddTaskBelow",
                    handler: this.addTaskBelowAction,
                    requiresTask: true,
                    scope: this,
                    text: a.addTaskBelow
                }, {
                    itemId: "contextmenu-AddSubTask",
                    handler: this.addSubtask,
                    requiresTask: true,
                    scope: this,
                    text: a.addSubtask
                }, {
                    itemId: "contextmenu-AddMilsestone",
                    handler: this.addMilestone,
                    requiresTask: true,
                    scope: this,
                    text: a.addMilestone
                }, {
                    itemId: "contextmenu-AddSuccessor",
                    handler: this.addSuccessor,
                    requiresTask: true,
                    scope: this,
                    text: a.addSuccessor
                }, {
                    itemId: "contextmenu-AddPredecessor",
                    handler: this.addPredecessor,
                    requiresTask: true,
                    scope: this,
                    text: a.addPredecessor
                }]
            }
        }, {
            itemId: "contextmenu-deleteDependency",
            text: a.deleteDependency,
            requiresTask: true,
            menu: {
                plain: true,
                listeners: {
                    beforeshow: this.populateDependencyMenu,
                    mouseover: this.onDependencyMouseOver,
                    mouseleave: this.onDependencyMouseOut,
                    scope: this
                }
            }
        }]
    },
    listeners: {
        beforeshow: function () {
            var d = this;
            var f = this.grid.getSelectionModel().selected;
            var b = d.getComponent("contextmenu-Add");
            if (f.length > 0) {
                var c = f.items[0].data.Id;
                var e = f.items[0].data.Outline_Level;
                if (f.items[0].data.Outline_Level == 1) {
                    d.getComponent("contextmenu-Cut").show();
                    d.getComponent("contextmenu-Copy").show();
                    d.getComponent("contextmenu-Paste").show();
                    d.getComponent("contextmenu-Critical").hide();
                    d.getComponent("contextmenu-Reshow").hide();
                    d.getComponent("contextmenu-Completed").hide();
                    d.getComponent("contextmenu-NotClear").hide();
                    d.getComponent("contextmenu-NoticeArrow").hide();
                    d.getComponent("contextmenu-Passive").hide();
                    d.getComponent("contextmenu-Cancelled").show();
                    d.getComponent("contextmenu-Lock").show();
                    d.getComponent("contextmenu-MasterLock").hide();
                    d.getComponent("contextmenu-DoNotFilter").hide();
                    d.getComponent("contextmenu-DeleteTask").show();
                    d.getComponent("contextmenu-EditLeftLable").hide();
                    d.getComponent("contextmenu-EditRightLable").hide();
                    d.getComponent("contextmenu-Convert").hide();
                    if (b.menu.items.items[0].itemId == "contextmenu-OnLevel1") {
                        b.menu.items.items[0].show()
                    }
                    if (b.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                        b.menu.items.items[1].show()
                    }
                    if (b.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                        b.menu.items.items[2].hide()
                    }
                    if (b.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                        b.menu.items.items[3].show()
                    }
                    if (b.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                        b.menu.items.items[4].show()
                    }
                    if (b.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                        b.menu.items.items[5].hide()
                    }
                    if (b.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                        b.menu.items.items[6].hide()
                    }
                    if (b.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                        b.menu.items.items[7].hide()
                    }
                    d.getComponent("contextmenu-deleteDependency").hide();
                    if (f.items[0].data.Lock == 1) {
                        d.getComponent("contextmenu-Cut").hide();
                        d.getComponent("contextmenu-Copy").show();
                        d.getComponent("contextmenu-Paste").show();
                        d.getComponent("contextmenu-Critical").hide();
                        d.getComponent("contextmenu-Reshow").hide();
                        d.getComponent("contextmenu-Completed").hide();
                        d.getComponent("contextmenu-NotClear").hide();
                        d.getComponent("contextmenu-NoticeArrow").hide();
                        d.getComponent("contextmenu-Passive").hide();
                        d.getComponent("contextmenu-Cancelled").show();
                        d.getComponent("contextmenu-Lock").show();
                        d.getComponent("contextmenu-MasterLock").hide();
                        d.getComponent("contextmenu-DoNotFilter").hide();
                        d.getComponent("contextmenu-DeleteTask").hide();
                        d.getComponent("contextmenu-EditLeftLable").hide();
                        d.getComponent("contextmenu-EditRightLable").hide();
                        d.getComponent("contextmenu-Convert").hide();
                        if (b.menu.items.items[0].itemId == "contextmenu-OnLevel1") {
                            b.menu.items.items[0].show()
                        }
                        if (b.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                            b.menu.items.items[1].hide()
                        }
                        if (b.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                            b.menu.items.items[2].hide()
                        }
                        if (b.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                            b.menu.items.items[3].show()
                        }
                        if (b.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                            b.menu.items.items[4].hide()
                        }
                        if (b.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                            b.menu.items.items[5].hide()
                        }
                        if (b.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                            b.menu.items.items[6].hide()
                        }
                        if (b.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                            b.menu.items.items[7].hide()
                        }
                        d.getComponent("contextmenu-deleteDependency").hide()
                    }
                    $.ajax({
                        type: "GET",
                        url: "Tasks/CheckPermission?rawId=" + c + "&level=" + e,
                        autoSync: true,
                        autoLoad: true,
                        success: function (h) {
                            if (h == "Read" || h == "Change") {
                                d.getComponent("contextmenu-Cut").hide();
                                var g = d.getComponent("contextmenu-Add");
                                if (g.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                                    g.menu.items.items[1].hide()
                                }
                                if (g.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                                    g.menu.items.items[2].hide()
                                }
                                if (g.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                                    g.menu.items.items[3].show()
                                }
                                if (g.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                                    g.menu.items.items[4].hide()
                                }
                                d.getComponent("contextmenu-DeleteTask").hide()
                            }
                        }
                    })
                } else {
                    if (f.items[0].data.Outline_Level == 2) {
                        d.getComponent("contextmenu-Cut").show();
                        d.getComponent("contextmenu-Copy").show();
                        d.getComponent("contextmenu-Paste").show();
                        d.getComponent("contextmenu-Critical").show();
                        d.getComponent("contextmenu-Reshow").show();
                        d.getComponent("contextmenu-Completed").show();
                        d.getComponent("contextmenu-NotClear").show();
                        d.getComponent("contextmenu-NoticeArrow").show();
                        d.getComponent("contextmenu-Passive").show();
                        d.getComponent("contextmenu-Cancelled").show();
                        d.getComponent("contextmenu-DoNotFilter").show();
                        d.getComponent("contextmenu-DeleteTask").show();
                        d.getComponent("contextmenu-EditLeftLable").show();
                        d.getComponent("contextmenu-EditRightLable").show();
                        d.getComponent("contextmenu-Convert").show();
                        if (b.menu.items.items[0].itemId == "contextmenu-OnLevel1") {
                            b.menu.items.items[0].show()
                        }
                        if (b.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                            b.menu.items.items[1].show()
                        }
                        if (b.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                            b.menu.items.items[2].show()
                        }
                        if (b.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                            b.menu.items.items[3].show()
                        }
                        if (b.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                            b.menu.items.items[4].show()
                        }
                        if (b.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                            b.menu.items.items[5].show()
                        }
                        if (b.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                            b.menu.items.items[6].show()
                        }
                        if (b.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                            b.menu.items.items[7].show()
                        }
                        d.getComponent("contextmenu-deleteDependency").show();
                        if (f.items[0].data.Lock == 1) {
                            d.getComponent("contextmenu-Cut").hide();
                            d.getComponent("contextmenu-Copy").show();
                            d.getComponent("contextmenu-Paste").show();
                            d.getComponent("contextmenu-Critical").hide();
                            d.getComponent("contextmenu-Reshow").hide();
                            d.getComponent("contextmenu-Completed").hide();
                            d.getComponent("contextmenu-NotClear").hide();
                            d.getComponent("contextmenu-NoticeArrow").hide();
                            d.getComponent("contextmenu-Passive").hide();
                            d.getComponent("contextmenu-Cancelled").show();
                            d.getComponent("contextmenu-Lock").show();
                            d.getComponent("contextmenu-MasterLock").hide();
                            d.getComponent("contextmenu-DoNotFilter").hide();
                            d.getComponent("contextmenu-DeleteTask").hide();
                            d.getComponent("contextmenu-EditLeftLable").hide();
                            d.getComponent("contextmenu-EditRightLable").hide();
                            d.getComponent("contextmenu-Convert").hide();
                            if (b.menu.items.items[0].itemId == "contextmenu-OnLevel1") {
                                b.menu.items.items[0].show()
                            }
                            if (b.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                                b.menu.items.items[1].hide()
                            }
                            if (b.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                                b.menu.items.items[2].hide()
                            }
                            if (b.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                                b.menu.items.items[3].show()
                            }
                            if (b.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                                b.menu.items.items[4].hide()
                            }
                            if (b.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                                b.menu.items.items[5].hide()
                            }
                            if (b.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                                b.menu.items.items[6].hide()
                            }
                            if (b.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                                b.menu.items.items[7].hide()
                            }
                            d.getComponent("contextmenu-deleteDependency").hide()
                        }
                        $.ajax({
                            type: "GET",
                            url: "Tasks/CheckPermission?rawId=" + c + "&level=" + e,
                            autoSync: true,
                            autoLoad: true,
                            success: function (h) {
                                var g = d.getComponent("contextmenu-Add");
                                if (h == "Change") {
                                    d.getComponent("contextmenu-Cut").hide();
                                    if (g.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                                        g.menu.items.items[1].hide()
                                    }
                                    if (g.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                                        g.menu.items.items[2].hide()
                                    }
                                    if (g.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                                        g.menu.items.items[3].show()
                                    }
                                    if (g.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                                        g.menu.items.items[4].hide()
                                    }
                                    if (g.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                                        g.menu.items.items[5].hide()
                                    }
                                    if (g.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                                        g.menu.items.items[6].hide()
                                    }
                                    if (g.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                                        g.menu.items.items[7].hide()
                                    }
                                    d.getComponent("contextmenu-DeleteTask").hide()
                                } else {
                                    if (h == "Read") {
                                        d.getComponent("contextmenu-Cut").hide();
                                        d.getComponent("contextmenu-Copy").show();
                                        d.getComponent("contextmenu-Paste").show();
                                        d.getComponent("contextmenu-Critical").hide();
                                        d.getComponent("contextmenu-Reshow").hide();
                                        d.getComponent("contextmenu-Completed").hide();
                                        d.getComponent("contextmenu-NotClear").hide();
                                        d.getComponent("contextmenu-NoticeArrow").hide();
                                        d.getComponent("contextmenu-Passive").hide();
                                        d.getComponent("contextmenu-Cancelled").hide();
                                        d.getComponent("contextmenu-Lock").show();
                                        d.getComponent("contextmenu-MasterLock").show();
                                        d.getComponent("contextmenu-DoNotFilter").hide();
                                        d.getComponent("contextmenu-DeleteTask").hide();
                                        d.getComponent("contextmenu-EditLeftLable").hide();
                                        d.getComponent("contextmenu-EditRightLable").hide();
                                        d.getComponent("contextmenu-Convert").hide();
                                        if (g.menu.items.items[0].itemId == "contextmenu-OnLevel1") {
                                            g.menu.items.items[0].show()
                                        }
                                        if (g.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                                            g.menu.items.items[1].hide()
                                        }
                                        if (g.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                                            g.menu.items.items[2].hide()
                                        }
                                        if (g.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                                            g.menu.items.items[3].show()
                                        }
                                        if (g.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                                            g.menu.items.items[4].hide()
                                        }
                                        if (g.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                                            g.menu.items.items[5].hide()
                                        }
                                        if (g.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                                            g.menu.items.items[6].hide()
                                        }
                                        if (g.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                                            g.menu.items.items[7].hide()
                                        }
                                        d.getComponent("contextmenu-deleteDependency").hide()
                                    }
                                }
                            }
                        })
                    } else {
                        if (f.items[0].data.Outline_Level == 3) {
                            if (f.items[0].data.Lock == 1) {
                                d.getComponent("contextmenu-Cut").hide();
                                d.getComponent("contextmenu-Copy").show();
                                d.getComponent("contextmenu-Paste").show();
                                d.getComponent("contextmenu-Critical").hide();
                                d.getComponent("contextmenu-Reshow").hide();
                                d.getComponent("contextmenu-Completed").hide();
                                d.getComponent("contextmenu-NotClear").hide();
                                d.getComponent("contextmenu-NoticeArrow").hide();
                                d.getComponent("contextmenu-Passive").hide();
                                d.getComponent("contextmenu-Cancelled").show();
                                d.getComponent("contextmenu-Lock").show();
                                d.getComponent("contextmenu-MasterLock").hide();
                                d.getComponent("contextmenu-DoNotFilter").hide();
                                d.getComponent("contextmenu-DeleteTask").hide();
                                d.getComponent("contextmenu-EditLeftLable").hide();
                                d.getComponent("contextmenu-EditRightLable").hide();
                                d.getComponent("contextmenu-Convert").hide();
                                if (b.menu.items.items[0].itemId == "contextmenu-OnLevel1") {
                                    b.menu.items.items[0].show()
                                }
                                if (b.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                                    b.menu.items.items[1].hide()
                                }
                                if (b.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                                    b.menu.items.items[2].hide()
                                }
                                if (b.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                                    b.menu.items.items[3].show()
                                }
                                if (b.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                                    b.menu.items.items[4].hide()
                                }
                                if (b.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                                    b.menu.items.items[5].hide()
                                }
                                if (b.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                                    b.menu.items.items[6].hide()
                                }
                                if (b.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                                    b.menu.items.items[7].hide()
                                }
                                d.getComponent("contextmenu-deleteDependency").hide()
                            } else {
                                $.ajax({
                                    type: "GET",
                                    url: "Tasks/CheckPermission?rawId=" + f.items[0].data.parentId + "&level=" + e,
                                    autoSync: true,
                                    autoLoad: true,
                                    success: function (h) {
                                        var g = d.getComponent("contextmenu-Add");
                                        if (h == "Change") {
                                            d.getComponent("contextmenu-Cut").hide();
                                            d.getComponent("contextmenu-Copy").show();
                                            d.getComponent("contextmenu-Paste").show();
                                            d.getComponent("contextmenu-Critical").show();
                                            d.getComponent("contextmenu-Reshow").show();
                                            d.getComponent("contextmenu-Completed").show();
                                            d.getComponent("contextmenu-NotClear").show();
                                            d.getComponent("contextmenu-NoticeArrow").show();
                                            d.getComponent("contextmenu-Passive").show();
                                            d.getComponent("contextmenu-Cancelled").show();
                                            d.getComponent("contextmenu-DoNotFilter").show();
                                            d.getComponent("contextmenu-Lock").show();
                                            d.getComponent("contextmenu-MasterLock").show();
                                            d.getComponent("contextmenu-EditLeftLable").show();
                                            d.getComponent("contextmenu-EditRightLable").show();
                                            d.getComponent("contextmenu-Convert").show();
                                            if (g.menu.items.items[0].itemId == "contextmenu-OnLevel1") {
                                                g.menu.items.items[0].show()
                                            }
                                            if (g.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                                                g.menu.items.items[1].hide()
                                            }
                                            if (g.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                                                g.menu.items.items[2].hide()
                                            }
                                            if (g.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                                                g.menu.items.items[3].show()
                                            }
                                            if (g.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                                                g.menu.items.items[4].hide()
                                            }
                                            if (g.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                                                g.menu.items.items[5].hide()
                                            }
                                            if (g.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                                                g.menu.items.items[6].hide()
                                            }
                                            if (g.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                                                g.menu.items.items[7].hide()
                                            }
                                            d.getComponent("contextmenu-deleteDependency").hide();
                                            d.getComponent("contextmenu-DeleteTask").hide()
                                        }
                                        if (h == "Read") {
                                            d.getComponent("contextmenu-Cut").hide();
                                            d.getComponent("contextmenu-Copy").show();
                                            d.getComponent("contextmenu-Paste").show();
                                            d.getComponent("contextmenu-Critical").hide();
                                            d.getComponent("contextmenu-Reshow").hide();
                                            d.getComponent("contextmenu-Completed").hide();
                                            d.getComponent("contextmenu-NotClear").hide();
                                            d.getComponent("contextmenu-NoticeArrow").hide();
                                            d.getComponent("contextmenu-Passive").hide();
                                            d.getComponent("contextmenu-Cancelled").hide();
                                            d.getComponent("contextmenu-DoNotFilter").hide();
                                            d.getComponent("contextmenu-Lock").show();
                                            d.getComponent("contextmenu-MasterLock").show();
                                            d.getComponent("contextmenu-DeleteTask").hide();
                                            d.getComponent("contextmenu-EditLeftLable").hide();
                                            d.getComponent("contextmenu-EditRightLable").hide();
                                            d.getComponent("contextmenu-Convert").hide();
                                            if (g.menu.items.items[0].itemId == "contextmenu-OnLevel1") {
                                                g.menu.items.items[0].show()
                                            }
                                            if (g.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                                                g.menu.items.items[1].hide()
                                            }
                                            if (g.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                                                g.menu.items.items[2].hide()
                                            }
                                            if (g.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                                                g.menu.items.items[3].show()
                                            }
                                            if (g.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                                                g.menu.items.items[4].hide()
                                            }
                                            if (g.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                                                g.menu.items.items[5].hide()
                                            }
                                            if (g.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                                                g.menu.items.items[6].hide()
                                            }
                                            if (g.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                                                g.menu.items.items[7].hide()
                                            }
                                            d.getComponent("contextmenu-deleteDependency").hide()
                                        }
                                        if (h == "Create" || h == "Standard") {
                                            d.getComponent("contextmenu-Cut").show();
                                            d.getComponent("contextmenu-Copy").show();
                                            d.getComponent("contextmenu-Paste").show();
                                            d.getComponent("contextmenu-Critical").show();
                                            d.getComponent("contextmenu-Reshow").show();
                                            d.getComponent("contextmenu-Completed").show();
                                            d.getComponent("contextmenu-NotClear").show();
                                            d.getComponent("contextmenu-NoticeArrow").show();
                                            d.getComponent("contextmenu-Passive").show();
                                            d.getComponent("contextmenu-Cancelled").show();
                                            d.getComponent("contextmenu-DoNotFilter").show();
                                            d.getComponent("contextmenu-DeleteTask").show();
                                            d.getComponent("contextmenu-EditLeftLable").show();
                                            d.getComponent("contextmenu-EditRightLable").show();
                                            d.getComponent("contextmenu-Convert").show();
                                            if (g.menu.items.items[0].itemId == "contextmenu-OnLevel") {
                                                g.menu.items.items[0].show()
                                            }
                                            if (g.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                                                g.menu.items.items[1].show()
                                            }
                                            if (g.menu.items.items[2].itemId == "contextmenu-AddTaskBelow") {
                                                g.menu.items.items[2].show()
                                            }
                                            if (g.menu.items.items[3].itemId == "contextmenu-AddSubTask") {
                                                g.menu.items.items[3].show()
                                            }
                                            if (g.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                                                g.menu.items.items[4].show()
                                            }
                                            if (g.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                                                g.menu.items.items[5].show()
                                            }
                                            if (g.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                                                g.menu.items.items[6].show()
                                            }
                                            if (g.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                                                g.menu.items.items[7].show()
                                            }
                                            d.getComponent("contextmenu-deleteDependency").show()
                                        }
                                    },
                                    error: function () {
                                        alert("Request Failed")
                                    },
                                    processData: true,
                                    async: true
                                })
                            }
                        }
                    }
                }
            }
        }
    },
    buildMenuItems: function () {
        this.items = this.createMenuItems()
    },
    initComponent: function () {
        this.buildMenuItems();
        this.callParent(arguments)
    },
    init: function (b) {
        b.on("destroy", this.cleanUp, this);
        var a = b.getSchedulingView(), c = b.lockedGrid.getView();
        if (this.triggerEvent === "itemcontextmenu") {
            c.on("itemcontextmenu", this.onItemContextMenu, this);
            a.on("itemcontextmenu", this.onItemContextMenu, this)
        } else {
            a.on("taskcontextmenu", this.onTaskContextMenu, this);
            c.on("itemcontextmenu", this.onItemContextMenu, this)
        }
        a.on("containercontextmenu", this.onContainerContextMenu, this);
        c.on("containercontextmenu", this.onContainerContextMenu, this);
        this.grid = b
    },
    populateDependencyMenu: function (f) {
        var d = this.grid, b = d.getTaskStore(), e = this.rec.getAllDependencies(), a = d.dependencyStore;
        f.removeAll();
        if (e.length === 0) {
            return false
        }
        var c = this.rec.getId() || this.rec.internalId;
        Ext.each(e, function (i) {
            var h = i.get("From"), g = b.getById(h == c ? i.get("To") : h);
            if (g) {
                f.add({
                    depId: i.internalId,
                    text: Ext.util.Format.ellipsis(g.get("Name"), 30),
                    scope: this,
                    handler: function (k) {
                        var j;
                        a.each(function (l) {
                            if (l.internalId == k.depId) {
                                j = l;
                                return false
                            }
                        });
                        a.remove(j)
                    }
                })
            }
        }, this)
    },
    onDependencyMouseOver: function (d, a, b) {
        if (a) {
            var c = this.grid.getSchedulingView();
            if (this.lastHighlightedItem) {
                c.unhighlightDependency(this.lastHighlightedItem.depId)
            }
            this.lastHighlightedItem = a;
            c.highlightDependency(a.depId)
        }
    },
    onDependencyMouseOut: function (b, a) {
        if (this.lastHighlightedItem) {
            this.grid.getSchedulingView().unhighlightDependency(this.lastHighlightedItem.depId)
        }
    },
    cleanUp: function () {
        if (this.menu) {
            this.menu.destroy()
        }
    },
    onTaskContextMenu: function (b, a, c) {
        this.activateMenu(a, c)
    },
    onItemContextMenu: function (b, a, d, c, f) {
        this.activateMenu(a, f)
    },
    onContainerContextMenu: function (a, b) {
        this.activateMenu(null, b)
    },
    activateMenu: function (c, b) {
        b.stopEvent();
        this.rec = c;
        var a = this.query("[requiresTask]");
        Ext.each(a, function (d) {
            d.setDisabled(!c)
        });
        this.showAt(b.getXY())
    },
    copyTask: function (c) {
        if (c != null) {
            var b = this.grid.getTaskStore(), a = new b.model({
                PercentDone: 0,
                Name: this.texts.newTaskText,
                StartDate: c.get("StartDate"),
                EndDate: c.get("EndDate"),
                parentId: c.get("parentId") + 1,
                leaf: true,
                Outline_Level: c.get("Outline_Level")
            });
            return a
        } else {
            var b = this.grid.getTaskStore(), a = new b.model({
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
            return a
        }
    },
    addTaskAbove: function (a) {
        var c = this.rec;
        a.isCriticalTask = false;
        a.isClarificationRequired = false;
        a.isPassive = false;
        a.isCancelled = false;
        a.isNotice = false;
        a.isCompleted = false;
        a.isReshow = false;
        a.NoticeStatus = 3;
        a.LastModification = new Date();
        a.LastUserMod = new Date();
        a.Group_Name = c.parentNode.raw.Name;
        a.Created = new Date();
        if (c.data.Outline_Level == 1) {
            a.Name = "(Project desig.)"
        } else {
            if (c.data.Outline_Level >= 2) {
                a.Name = "(Task desig.)"
            }
        }
        c.parentNode.set("Object", parseInt(c.parentNode.data.Object) + 1);
        var b = Ext.getCmp("basic-statusbar");
        b.showBusy();
        Ext.defer(function () {
            b.setStatus("Ready")
        }, 1000);
        if (c) {
            c.parentNode.insertBefore(a, c)
        } else {
            this.grid.taskStore.getRootNode().appendChild(a)
        }
    },
    addTaskBelow: function (a) {
        var c = this.rec;
        var b = Ext.getCmp("basic-statusbar");
        b.showBusy();
        Ext.defer(function () {
            b.setStatus("Ready")
        }, 1000);
        if (c.data.Outline_Level == 1) {
            a.isCriticalTask = false;
            a.isClarificationRequired = false;
            a.isPassive = false;
            a.isCancelled = false;
            a.isNotice = false;
            a.isCompleted = false;
            a.isReshow = false;
            a.NoticeStatus = 3;
            a.LastModification = new Date();
            a.LastUserMod = new Date();
            a.Group_Name = c.parentNode.raw.Name;
            a.Created = new Date();
            a.set("Outline_Level", 1);
            a.set("leaf", true);
            a.set("Name", "(Folder desig.)");
            a.set("parentId", 0);
            c.parentNode.set("Object", parseInt(c.parentNode.data.Object) + 1);
            if (c) {
                c.parentNode.insertBefore(a, c.nextSibling)
            } else {
                this.grid.taskStore.getRootNode().appendChild(a)
            }
        } else {
            if (c.data.Outline_Level == 2) {
                a.isCriticalTask = false;
                a.isClarificationRequired = false;
                a.isPassive = false;
                a.isCancelled = false;
                a.isNotice = false;
                a.isCompleted = false;
                a.isReshow = false;
                a.NoticeStatus = 3;
                a.LastModification = new Date();
                a.LastUserMod = new Date();
                a.Group_Name = c.parentNode.raw.Name;
                a.Created = new Date();
                a.set("Outline_Level", 2);
                a.set("Name", "(Project desig.)");
                a.set("leaf", true);
                c.parentNode.set("Object", parseInt(c.parentNode.data.Object) + 1);
                if (c) {
                    c.parentNode.insertBefore(a, c.nextSibling)
                } else {
                    this.grid.taskStore.getRootNode().appendChild(a)
                }
            } else {
                a.isCriticalTask = false;
                a.isClarificationRequired = false;
                a.isPassive = false;
                a.isCancelled = false;
                a.isNotice = false;
                a.isCompleted = false;
                a.isReshow = false;
                a.NoticeStatus = 3;
                a.LastModification = new Date();
                a.LastUserMod = new Date();
                a.Group_Name = c.parentNode.raw.Name;
                a.Created = new Date();
                a.set("Outline_Level", c.data.Outline_Level);
                a.set("Name", "(Task desig.)");
                a.set("leaf", c.data.leaf);
                c.parentNode.set("Object", parseInt(c.parentNode.data.Object) + 1);
                if (c) {
                    c.parentNode.insertBefore(a, c.nextSibling)
                } else {
                    this.grid.taskStore.getRootNode().appendChild(a)
                }
            }
        }
    },
    critical: function () {
        var b = this.rec;
        var a = Ext.getCmp("basic-statusbar");
        a.showBusy();
        Ext.defer(function () {
            a.setStatus("Ready")
        }, 3000);
        if (b.data.isCriticalTask == true) {
            b.set("isCriticalTask", false)
        } else {
            b.set("isCriticalTask", true)
        }
    },
    reshow: function () {
        var b = this.rec;
        var a = Ext.getCmp("basic-statusbar");
        a.showBusy();
        Ext.defer(function () {
            a.setStatus("Ready")
        }, 3000);
        if (b.data.isReshow == true) {
            b.data.Reshow_Date = null;
            b.set("isReshow", false)
        } else {
            reshowDateWindow.show()
        }
    },
    completed: function () {
        var b = this.rec;
        var a = Ext.getCmp("basic-statusbar");
        a.showBusy();
        b.expand();
        Ext.defer(function () {
            a.setStatus("Ready");
            if (b.data.isCompleted != true) {
                f(b);
                b.set("PercentDone", 100);
                b.set("isCompleted", true);
                b.set("Completed", new Date())
            } else {
                c(b);
                b.set("PercentDone", 0);
                b.set("isCompleted", false);
                b.set("Completed", null)
            }
            function f(h) {
                for (var g = 0; g < h.childNodes.length; g++) {
                    h.expand();
                    var j = h.childNodes[g];
                    f(j);
                    e(j)
                }
            }

            function c(h) {
                for (var g = 0; g < h.childNodes.length; g++) {
                    h.expand();
                    var j = h.childNodes[g];
                    c(j);
                    d(j)
                }
            }

            function e(g) {
                g.set("PercentDone", 100);
                g.set("isCompleted", true);
                g.set("Completed", new Date())
            }

            function d(g) {
                g.set("PercentDone", 0);
                g.set("isCompleted", false);
                g.set("Completed", null)
            }
        }, 1000)
    },
    notclear: function () {
        var b = this.rec;
        var a = Ext.getCmp("basic-statusbar");
        a.showBusy();
        Ext.defer(function () {
            a.setStatus("Ready")
        }, 3000);
        if (b.data.isClarificationRequired == true) {
            b.set("isClarificationRequired", false)
        } else {
            b.set("isClarificationRequired", true)
        }
    },
    noticearrow: function () {
        var b = this.rec;
        var a = Ext.getCmp("basic-statusbar");
        a.showBusy();
        Ext.defer(function () {
            a.setStatus("Ready")
        }, 3000);
        if (b.data.isNotice == true) {
            b.set("isNotice", false)
        } else {
            b.set("isNotice", true)
        }
    },
    passive: function () {
        var b = this.rec;
        var a = Ext.getCmp("basic-statusbar");
        a.showBusy();
        b.expand();
        Ext.defer(function () {
            a.setStatus("Ready");
            if (b.data.isPassive != true) {
                e(b);
                b.set("isPassive", true);
                b.set("isCancelled", false)
            } else {
                c(b);
                b.set("isPassive", false)
            }
            function e(h) {
                for (var g = 0; g < h.childNodes.length; g++) {
                    h.expand();
                    var j = h.childNodes[g];
                    e(j);
                    f(j)
                }
            }

            function c(h) {
                for (var g = 0; g < h.childNodes.length; g++) {
                    h.expand();
                    var j = h.childNodes[g];
                    c(j);
                    d(j)
                }
            }

            function f(g) {
                g.set("isPassive", true);
                g.set("isCancelled", false)
            }

            function d(g) {
                g.set("isPassive", false)
            }
        }, 1000)
    },
    cancelled: function () {
        var b = this.rec;
        var a = Ext.getCmp("basic-statusbar");
        a.showBusy();
        b.expand();
        Ext.defer(function () {
            a.setStatus("Ready");
            if (b.data.isCancelled != true) {
                c(b);
                b.set("isCancelled", true);
                b.set("isPassive", false)
            } else {
                d(b);
                b.set("isCancelled", false)
            }
            function c(h) {
                for (var g = 0; g < h.childNodes.length; g++) {
                    h.expand();
                    var j = h.childNodes[g];
                    c(j);
                    f(j)
                }
            }

            function d(h) {
                for (var g = 0; g < h.childNodes.length; g++) {
                    h.expand();
                    var j = h.childNodes[g];
                    d(j);
                    e(j)
                }
            }

            function f(g) {
                g.set("isCancelled", true);
                g.set("isPassive", false)
            }

            function e(g) {
                g.set("isCancelled", false)
            }
        }, 1000)
    },
    paste: function () {
        var a = this.rec;
        console.log(this.copiedTasks);
        if (a) {
            this.addTaskBelow(this.copiedTasks)
        } else {
            this.grid.taskStore.getRootNode().appendChild(this.copiedTasks)
        }
        this.copiedTasks = this.copyTaskWithChildren(this.copiedTasks)
    },
    copy: function () {
        var a = this;
        var b = this.grid.getSelectionModel().selected;
        b.each(function (c) {
            if (!b.contains(c.parentNode)) {
                a.copiedTasks = a.copyTaskWithChildren(c)
            }
        })
    },
    cut: function () {
        var a = this;
        var b = this.grid.getSelectionModel().selected;
        b.each(function (c) {
            if (!b.contains(c.parentNode)) {
                a.copiedTasks = a.copyTaskWithChildren(c);
                a.grid.taskStore.remove(b.getRange())
            }
        })
    },
    lock: function () {
        var b = this.rec;
        var a = Ext.getCmp("basic-statusbar");
        a.showBusy();
        Ext.defer(function () {
            a.setStatus("Ready")
        }, 3000);
        if (b.data.Lock == 0) {
            b.set("Lock", 1)
        } else {
            b.set("Lock", 0)
        }
    },
    copyTaskWithChildren: function (c) {
        var d = this;
        var b = this.grid.getTaskStore().model;
        var a = new b({});
        a.set("leaf", (c && c.get("leaf")) || null);
        a.set("expanded", (c && c.get("expanded")) || null);
        a.setName((c && c.getName()) || null);
        a.set(a.startDateField, (c && c.getStartDate()) || null);
        a.set(a.endDateField, (c && c.getEndDate()) || null);
        a.set(a.percentDoneField, (c && c.getPercentDone()) || null);
        a.set(a.durationField, (c && c.getDuration()) || null);
        a.set(a.durationUnitField, (c && c.getDurationUnit()) || "d");
        a.set(a.effortField, (c && c.getEffort()) || null);
        a.set(a.effortUnitField, (c && c.getEffortUnit()) || "h");
        a.set(a.schedulingModeField, (c && c.getSchedulingMode()) || "Normal");
        a.set(a.clsField, (c && c.getCls()) || null);
        a.set("isCritical", (c && c.get("isCritical")) || null);
        a.set("isClarificationRequired", (c && c.get("isClarificationRequired")) || null);
        a.set("LimitDate", (c && c.get("LimitDate")) || null);
        a.set("isNotice", (c && c.get("isNotice")) || null);
        a.set("isReshow", (c && c.get("isReshow")) || null);
        a.set("isCompleted", (c && c.get("isCompleted")) || null);
        a.set("isPassive", (c && c.get("isPassive")) || null);
        a.set("isCancelled", (c && c.get("isCancelled")) || null);
        a.set("Act_Fixed_Cost", (c && c.get("Act_Fixed_Cost")) || null);
        a.set("Contact", (c && c.get("Contact")) || null);
        a.set("Responsible", (c && c.get("Responsible")) || null);
        a.set("Resource_Names", (c && c.get("Resource_Names")) || null);
        a.set("Created", (c && c.get("Created")) || null);
        a.set("Reshow_Date", (c && c.get("Reshow_Date")) || null);
        a.set("Capacity_usage", (c && c.get("Capacity_usage")) || null);
        a.set("Actual_Start", (c && c.get("Actual_Start")) || null);
        a.set("Object", (c && c.get("Object")) || null);
        a.set("Actual_Finish", (c && c.get("Actual_Finish")) || null);
        a.set("Rem_Time", (c && c.get("Rem_Time")) || null);
        a.set("Completed", (c && c.get("Completed")) || null);
        a.set("Actual_Duration", (c && c.get("Actual_Duration")) || null);
        a.set("Duration_Act_div_Pld", (c && c.get("Duration_Act_div_Pld")) || null);
        a.set("Duration_Act_Pld", (c && c.get("Duration_Act_Pld")) || null);
        a.set("Req_Effort", (c && c.get("Req_Effort")) || null);
        a.set("Req_Ress", (c && c.get("Req_Ress")) || null);
        a.set("Effort", (c && c.get("Effort")) || null);
        a.set("Actual_Effort", (c && c.get("Actual_Effort")) || null);
        a.set("Effort_Pld_Req", (c && c.get("Effort_Pld_Req")) || null);
        a.set("Effort_Pld_div_Req", (c && c.get("Effort_Pld_div_Req")) || null);
        a.set("Effort_Act_Pld1", (c && c.get("Effort_Act_Pld1")) || null);
        a.set("Effort_Act_Pld", (c && c.get("Effort_Act_Pld")) || null);
        a.set("Ovt_Effort", (c && c.get("Ovt_Effort")) || null);
        a.set("Actual_Ovt_Effort", (c && c.get("Actual_Ovt_Effort")) || null);
        a.set("Fixed_Cost", (c && c.get("Fixed_Cost")) || null);
        a.set("Standard_Rate", (c && c.get("Standard_Rate")) || null);
        a.set("Act_Standard_Rate", (c && c.get("Act_Standard_Rate")) || null);
        a.set("Labor_Cost", (c && c.get("Labor_Cost")) || null);
        a.set("Act_Labor_Cost", (c && c.get("Act_Labor_Cost")) || null);
        a.set("Ovt_Cost", (c && c.get("Ovt_Cost")) || null);
        a.set("Act_Ovt_Cost", (c && c.get("Act_Ovt_Cost")) || null);
        a.set("Total_Cost", (c && c.get("Total_Cost")) || null);
        a.set("Act_Total_Cost", (c && c.get("Act_Total_Cost")) || null);
        a.set("Total_Cost_Act_div_Pld", (c && c.get("Total_Cost_Act_div_Pld")) || null);
        a.set("Total_Cost_Act_Pld", (c && c.get("Total_Cost_Act_Pld")) || null);
        a.set("Cost_Calc_Meth", (c && c.get("Cost_Calc_Meth")) || null);
        a.set("Time_Unit_of_Cost", (c && c.get("Time_Unit_of_Cost")) || null);
        a.set("Time_Unit_of_Effort", (c && c.get("Time_Unit_of_Effort")) || null);
        a.set("Time_Unit_of_Duration", (c && c.get("Time_Unit_of_Duration")) || null);
        a.set("Text1", (c && c.get("Text1")) || null);
        a.set("Text2", (c && c.get("Text2")) || null);
        a.set("Text3", (c && c.get("Text3")) || null);
        a.set("Text4", (c && c.get("Text4")) || null);
        a.set("Text5", (c && c.get("Text5")) || null);
        a.set("Text6", (c && c.get("Text6")) || null);
        a.set("Text7", (c && c.get("Text7")) || null);
        a.set("Text8", (c && c.get("Text8")) || null);
        a.set("Text9", (c && c.get("Text9")) || null);
        a.set("Text10", (c && c.get("Text10")) || null);
        a.set("Number1", (c && c.get("Number1")) || null);
        a.set("Number2", (c && c.get("Number2")) || null);
        a.set("Number3", (c && c.get("Number3")) || null);
        a.set("Number4", (c && c.get("Number4")) || null);
        a.set("Number5", (c && c.get("Number5")) || null);
        a.set("Number6", (c && c.get("Number6")) || null);
        a.set("Number7", (c && c.get("Number7")) || null);
        a.set("Number8", (c && c.get("Number8")) || null);
        a.set("Number9", (c && c.get("Number9")) || null);
        a.set("Number10", (c && c.get("Number10")) || null);
        a.set("Finished_pr", (c && c.get("Finished_pr")) || null);
        a.set("End_progn", (c && c.get("End_progn")) || null);
        a.set("Dur_progn", (c && c.get("Dur_progn")) || null);
        a.set("Work_progn", (c && c.get("Work_progn")) || null);
        a.set("EMail", (c && c.get("EMail")) || null);
        a.set("Qty_Pld", (c && c.get("Qty_Pld")) || null);
        a.set("Qty_durch_Time_Pld", (c && c.get("Qty_durch_Time_Pld")) || null);
        a.set("Qty_Act", (c && c.get("Qty_Act")) || null);
        a.set("Qty_durch_Time_Act", (c && c.get("Qty_durch_Time_Act")) || null);
        a.set("Factor", (c && c.get("Factor")) || null);
        a.set("Buffer", (c && c.get("Buffer")) || null);
        a.set("Buffer_cum", (c && c.get("Buffer_cum")) || null);
        a.set("Buffer_Act", (c && c.get("Buffer_Act")) || null);
        a.set("Buffer_Act_cum", (c && c.get("Buffer_Act_cum")) || null);
        a.set("Constraint_Type", (c && c.get("Constraint_Type")) || null);
        a.set("Successors", (c && c.get("Successors")) || null);
        a.set("Group_Name", (c && c.get("Group_Name")) || null);
        a.set("Rec_Options", (c && c.get("Rec_Options")) || null);
        a.set("NoticeStatus", (c && c.get("NoticeStatus")) || null);
        a.set("Auftr_ID", (c && c.get("Auftr_ID")) || null);
        a.set("Bearb_ID", (c && c.get("Bearb_ID")) || null);
        a.set("LastUserMod", (c && c.get("LastUserMod")) || null);
        a.set("LastModification", (c && c.get("LastModification")) || null);
        a.set("LastUser", (c && c.get("LastUser")) || null);
        c.eachChild(function (e) {
            a.addSubtask(d.copyTaskWithChildren(e))
        });
        return a
    },
    deleteTask: function () {
        var b = this.grid.getSelectionModel().selected;
        var a = Ext.getCmp("basic-statusbar");
        a.showBusy();
        Ext.defer(function () {
            a.setStatus("Ready")
        }, 500);
        this.grid.taskStore.remove(b.getRange())
    },
    editLeftLabel: function () {
        this.grid.getSchedulingView().editLeftLabel(this.rec)
    },
    editRightLabel: function () {
        this.grid.getSchedulingView().editRightLabel(this.rec)
    },
    converToMilestone: function () {
        var b = this.rec;
        var a = Ext.getCmp("basic-statusbar");
        a.showBusy();
        Ext.defer(function () {
            a.setStatus("Ready")
        }, 1000);
        b.setStartDate(b.getEndDate(), false)
    },
    converToRegularTask: function () {
        var b = this.rec;
        b.set({
            StartDate: b.calculateStartDate(b.getStartDate(), 1, Sch.util.Date.DAY),
            EndDate: b.getStartDate(),
            Duration: 1,
            DurationUnit: Sch.util.Date.DAY
        });
        var a = Ext.getCmp("basic-statusbar");
        a.showBusy();
        Ext.defer(function () {
            a.setStatus("Ready")
        }, 1000)
    },
    addTaskAboveAction: function () {
        this.addTaskAbove(this.copyTask(this.rec))
    },
    addTaskBelowAction: function () {
        this.addTaskBelow(this.copyTask(this.rec))
    },
    OnLevel1Action: function (a) {
        var c = this.grid.getSelectionModel().selected;
        var b = Ext.getCmp("basic-statusbar");
        b.showBusy();
        Ext.defer(function () {
            b.setStatus("Ready")
        }, 1000);
        a.Outline_Level = 1;
        a.leaf = true;
        a.Name = "(Folder desig.)";
        a.isCriticalTask = false;
        a.isClarificationRequired = false;
        a.isPassive = false;
        a.isCancelled = false;
        a.isNotice = false;
        a.isCompleted = false;
        a.isReshow = false;
        a.NoticeStatus = 3;
        a.LastModification = new Date();
        a.LastUserMod = new Date();
        a.Group_Name = a.Name;
        a.Created = new Date();
        if (c.items.length > 0) {
            a.parentId = c.items[0].data.Id
        } else {
            a.parentId = 0
        }
        this.grid.taskStore.getRootNode().appendChild(a)
    },
    OnLevel2Action: function (a) {
        var c = this.rec;
        var b = Ext.getCmp("basic-statusbar");
        b.showBusy();
        Ext.defer(function () {
            b.setStatus("Ready")
        }, 1000);
        if (c.data.Outline_Level == 1) {
            a.Outline_Level = 2;
            a.leaf = true;
            a.Name = "(Project desig.)";
            a.isCriticalTask = false;
            a.isClarificationRequired = false;
            a.isPassive = false;
            a.isCancelled = false;
            a.isNotice = false;
            a.isCompleted = false;
            a.isReshow = false;
            a.NoticeStatus = 3;
            a.LastModification = new Date();
            a.LastUserMod = new Date();
            a.Group_Name = c.Name;
            a.Created = new Date();
            c.set("leaf", false);
            c.set("Object", parseInt(c.data.Object) + 1);
            c.appendChild(a);
            c.expand()
        } else {
            if (c.data.Outline_Level == 2) {
                a.isCriticalTask = false;
                a.isClarificationRequired = false;
                a.isPassive = false;
                a.isCancelled = false;
                a.isNotice = false;
                a.isCompleted = false;
                a.isReshow = false;
                a.NoticeStatus = 3;
                a.LastModification = new Date();
                a.LastUserMod = new Date();
                a.Group_Name = c.parentNode.raw.Name;
                a.Created = new Date();
                a.Outline_Level = 2;
                a.Name = "(Project desig.)";
                a.leaf = true;
                c.parentNode.set("Object", parseInt(c.parentNode.data.Object) + 1);
                if (c) {
                    c.parentNode.insertBefore(a, c.nextSibling)
                } else {
                    this.grid.taskStore.getRootNode().appendChild(a)
                }
            } else {
                if (c.data.Outline_Level >= 3) {
                    var e;
                    d(c);
                    function d(f) {
                        if (f.parentNode.data.Id > 0) {
                            d(f.parentNode)
                        } else {
                            e = f
                        }
                    }

                    a.isCriticalTask = false;
                    a.isClarificationRequired = false;
                    a.isPassive = false;
                    a.isCancelled = false;
                    a.isNotice = false;
                    a.isCompleted = false;
                    a.isReshow = false;
                    a.NoticeStatus = 3;
                    a.LastModification = new Date();
                    a.LastUserMod = new Date();
                    a.Group_Name = c.parentNode.raw.Name;
                    a.Created = new Date();
                    a.Outline_Level = 2;
                    a.Name = "(Project desig.)";
                    a.leaf = true;
                    e.set("Object", parseInt(e.data.Object) + 1);
                    e.appendChild(a)
                }
            }
        }
    },
    OnLevel3Action: function (a) {
        var c = this.rec;
        var b = Ext.getCmp("basic-statusbar");
        b.showBusy();
        Ext.defer(function () {
            b.setStatus("Ready")
        }, 1000);
        if (c.data.Outline_Level == 2) {
            a.Outline_Level = 3;
            a.leaf = true;
            a.Name = "(Task desig.)";
            a.isCriticalTask = false;
            a.isClarificationRequired = false;
            a.isPassive = false;
            a.isCancelled = false;
            a.isNotice = false;
            a.isCompleted = false;
            a.isReshow = false;
            a.NoticeStatus = 3;
            a.LastModification = new Date();
            a.LastUserMod = new Date();
            a.Group_Name = c.parentNode.raw.Name;
            a.Created = new Date();
            c.set("leaf", false);
            c.set("Object", parseInt(c.data.Object) + 1);
            c.appendChild(a);
            c.expand()
        } else {
            if (c.data.Outline_Level >= 3) {
                var e;
                d(c);
                function d(f) {
                    if (f.parentNode.data.Outline_Level >= 2) {
                        d(f.parentNode)
                    } else {
                        e = f
                    }
                }

                a.Outline_Level = 3;
                a.leaf = true;
                a.Name = "(Task desig.)";
                a.isCriticalTask = false;
                a.isClarificationRequired = false;
                a.isPassive = false;
                a.isCancelled = false;
                a.isNotice = false;
                a.isCompleted = false;
                a.isReshow = false;
                a.NoticeStatus = 3;
                a.LastModification = new Date();
                a.LastUserMod = new Date();
                a.Group_Name = c.parentNode.raw.Name;
                a.Created = new Date();
                e.set("leaf", false);
                e.set("Object", parseInt(e.data.Object) + 1);
                e.appendChild(a);
                e.expand()
            }
        }
    },
    addSubtask: function (a) {
        var c = this.rec;
        var b = Ext.getCmp("basic-statusbar");
        b.showBusy();
        Ext.defer(function () {
            b.setStatus("Ready")
        }, 1000);
        a.leaf = true;
        a.parentId = c.data.Id;
        a.Outline_Level = c.data.Outline_Level + 1;
        if (c.data.Outline_Level == 1) {
            a.Name = "(Project desig.)"
        } else {
            if (c.data.Outline_Level >= 2) {
                a.Name = "(Task desig.)"
            }
        }
        a.isCriticalTask = false;
        a.isClarificationRequired = false;
        a.isPassive = false;
        a.isCancelled = false;
        a.isNotice = false;
        a.isCompleted = false;
        a.isReshow = false;
        a.NoticeStatus = 3;
        a.LastModification = new Date();
        a.LastUserMod = new Date();
        a.Group_Name = c.parentNode.raw.Name;
        a.Created = new Date();
        c.set("leaf", false);
        c.set("Object", parseInt(c.data.Object) + 1);
        c.appendChild(a);
        c.expand()
    },
    addSuccessor: function () {
        var c = this.rec, d = this.grid.dependencyStore, a = this.copyTask(c);
        this.addTaskBelow(a);
        var b = Ext.getCmp("basic-statusbar");
        b.showBusy();
        Ext.defer(function () {
            b.setStatus("Ready")
        }, 1000);
        a.set("StartDate", c.getEndDate());
        a.set("NoticeStatus", "3");
        if (c.data.Outline_Level == 2) {
            a.Name = "(Project desig.)"
        } else {
            if (c.data.Outline_Level > 2) {
                a.Name = "(Task desig.)"
            }
        }
        a.setDuration(1, Sch.util.Date.DAY);
        var e = d.model;
        d.add(new e({From: c.getId() || c.internalId, To: a.getId() || a.internalId, Type: e.EndToStart}))
    },
    addPredecessor: function () {
        var c = this.rec;
        var d = this.grid.dependencyStore;
        var a = this.copyTask(c);
        this.addTaskAbove(a);
        var b = Ext.getCmp("basic-statusbar");
        b.showBusy();
        Ext.defer(function () {
            b.setStatus("Ready")
        }, 1000);
        a.set("NoticeStatus", "3");
        if (c.data.Outline_Level == 1) {
            a.set("Name", "(Folder desig.)")
        } else {
            if (c.data.Outline_Level == 2) {
                a.set("Name", "(Project desig.)")
            } else {
                if (c.data.Outline_Level > 2) {
                    a.set("Name", "(Task desig.)")
                }
            }
        }
        a.set({
            StartDate: a.calculateStartDate(c.getStartDate(), 1, Sch.util.Date.DAY),
            EndDate: c.getStartDate(),
            Duration: 1,
            DurationUnit: Sch.util.Date.DAY
        });
        var e = d.model;
        d.add(new e({From: a.getId() || a.internalId, To: c.getId() || c.internalId, Type: e.EndToStart}))
    },
    addMilestone: function () {
        var c = this.rec, a = this.copyTask(c);
        this.addTaskBelow(a);
        var b = Ext.getCmp("basic-statusbar");
        b.showBusy();
        Ext.defer(function () {
            b.setStatus("Ready")
        }, 1000);
        if (c.data.Outline_Level == 1) {
            a.Name = "(Project desig.)"
        } else {
            if (c.data.Outline_Level >= 2) {
                a.Name = "(Task desig.)"
            }
        }
        a.setStartDate(a.getEndDate(), false);
        a.set("NoticeStatus", "1")
    },
});