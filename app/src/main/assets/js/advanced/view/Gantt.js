Ext.define("APlan.view.Gantt", {
    extend: "Gnt.panel.Gantt",
    xtype: "advanced-gantt",
    requires: ["Ext.form.field.Text", "Sch.plugin.TreeCellEditing", "Sch.plugin.Pan", "Gnt.plugin.taskeditor.TaskEditor", "Gnt.plugin.taskeditor.ProjectEditor", "Gnt.column.Sequence", "Gnt.column.Name", "Gnt.column.StartDate", "Gnt.column.EndDate", "Gnt.column.Duration", "Gnt.column.ConstraintType", "Gnt.column.ConstraintDate", "Gnt.column.PercentDone", "Gnt.column.Predecessor", "Gnt.column.ManuallyScheduled", "Gnt.column.AddNew", "Gnt.column.DeadlineDate", "Gnt.selection.SpreadsheetModel", "Gnt.plugin.Printable", "Gnt.column.ShowInTimeline", "APlan.plugin.TaskArea", "APlan.plugin.TaskContextMenu", "APlan.field.Filter"],
    showTodayLine: true,
    loadMask: true,
    enableProgressBarResize: true,
    showRollupTasks: true,
    eventBorderWidth: 0,
    rowHeight: 31,
    viewPreset: "weekAndDayLetter",
    enableDragDropColumn: true,
    projectLinesConfig: {linesFor: "start"},
    allowDeselect: true,
    selModel: {type: "gantt_spreadsheet"},
    lockedGridConfig: {width: 400},
    lockedViewConfig: {
        getRowClass: function (a) {
            return a.isRoot() ? "root-row" : ""
        }
    },
    taskBodyTemplate: '<div class="sch-gantt-progress-bar" style="width:{progressBarWidth}px;{progressBarStyle}" unselectable="on"><span class="sch-gantt-progress-bar-label">{[Math.round(values.percentDone)]}%</span></div>',
    tooltipTpl: '<strong class="tipHeader">{Name}</strong><table class="taskTip"><tr><td>Start:</td> <td align="right">{[values._record.getDisplayStartDate("y-m-d")]}</td></tr><tr><td>End:</td> <td align="right">{[values._record.getDisplayEndDate("y-m-d")]}</td></tr><tr><td>Progress:</td><td align="right">{[ Math.round(values.PercentDone)]}%</td></tr></table>',
    leftLabelField: {dataIndex: "Name", editor: {xtype: "textfield"}},
    plugins: ["advanced_taskcontextmenu", "scheduler_pan", "gantt_taskeditor", "gantt_projecteditor", "gridfilters", {
        ptype: "gantt_dependencyeditor",
        width: 320
    }, {pluginId: "taskarea", ptype: "taskarea"}, {
        ptype: "scheduler_treecellediting",
        clicksToEdit: 2,
        pluginId: "editingInterface"
    }, {ptype: "gantt_clipboard", source: ["raw", "text"]}, {
        ptype: "gantt_printable",
        exportDialogConfig: {
            form: {stateId: "gantt-example-advanced"},
            showDPIField: true,
            showColumnPicker: true,
            dateRangeRestriction: false
        }
    }, "gantt_selectionreplicator"],
    columns: [{
        header: "No",
        name: "SequenceNumber",
        type: "string",
        id: "NoId",
        tdCls: "nocell",
        sortable: false,
        width: 50,
        locked: true,
        renderer: function (b, a, c) {
            return c.getSequenceNumber()
        }
    }, {xtype: "dragdropcolumn"}, {
        xtype: "namecolumn",
        width: 200,
        items: {xtype: "gantt-filter-field"}
    }, {xtype: "startdatecolumn", width: 130, dataIndex: "StartDate", filter: {type: "date"}}, {
        xtype: "enddatecolumn",
        width: 130,
        dataIndex: "EndDate",
        filter: {type: "date"}
    }, {
        xtype: "durationcolumn",
        width: 100
    }, {xtype: "constrainttypecolumn"}, {xtype: "constraintdatecolumn"}, {
        xtype: "percentdonecolumn",
        width: 100,
        dataIndex: "PercentDone",
        filter: {type: "number"}
    }, {xtype: "predecessorcolumn", useSequenceNumber: true}, {xtype: "addnewcolumn"}],
    eventRenderer: function (c, b) {
        var f, d, e, a;
        if (c.get("Color")) {
            f = Ext.String.format("background-color: #{0};border-color:#{0}", c.get("Color"));
            if (!b.segments) {
                a = {style: f}
            } else {
                d = b.segments;
                for (e = 0; e < d.length; e++) {
                    d[e].style = f
                }
            }
        }
        return a
    }
});