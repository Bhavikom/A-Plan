Ext.define('APlan.locale.En', {
    extend    : 'Sch.locale.Locale',
    requires  : 'Gnt.locale.En',
    singleton : true,

    l10n : {
        'APlan.Application' : {
            error           : 'Error',
            requestError    : 'Request error'
        },

        'APlan.view.ControlHeader' : {
            previousTimespan        : 'Previous timespan',
            nextTimespan            : 'Next timespan',
            collapseAll             : 'Collapse all',
            expandAll               : 'Expand all',
            zoomOut                 : 'Zoom out',
            zoomIn                  : 'Zoom in',
            zoomToFit               : 'Zoom to fit',
            undo                    : 'Undo',
            redo                    : 'Redo',
            viewFullScreen          : 'View full screen',
            highlightCriticalPath   : 'Highlight critical path',
            addNewTask              : 'Add new task',
            newTask                 : 'New Task',
            removeSelectedTasks     : 'Remove selected task(s)',
            indent                  : 'Indent',
            outdent                 : 'Outdent',
            manageCalendars         : 'Manage calendars',
            saveChanges             : 'Save changes',
            language                : 'Language: ',
            selectLanguage          : 'Select a language...',
            tryMore                 : 'Try more features...',
            print                   : 'Print'
        },

        'APlan.plugin.TaskContextMenu' : {
            changeTaskColor         : 'Change task color'
        },

        'APlan.view.GanttSecondaryToolbar' : {
            toggleChildTasksGrouping        : 'Toggle child tasks grouping on/off',
            toggleRollupTasks               : 'Toggle rollup tasks',
            highlightTasksLonger8           : 'Highlight tasks longer than 8 days',
            filterTasksWithProgressLess30   : 'Filter: Tasks with progress < 30%',
            clearFilter                     : 'Clear Filter',
            scrollToLastTask                : 'Scroll to last task'
        }

    },

    apply : function (classNames) {
        Gnt.locale.En.apply(classNames);
        this.callParent(arguments);
    }

});
