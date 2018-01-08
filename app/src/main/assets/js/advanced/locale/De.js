Ext.define('APlan.locale.De', {
    extend    : 'Sch.locale.Locale',
    requires  : 'Gnt.locale.De',
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
            zoomOut                 : 'Verkleinern',
            zoomIn                  : 'Vergrößern',
            zoomToFit               : 'Zoom to fit',
            undo                    : 'Rückgängig',
            redo                    : 'Wiederholen',
            viewFullScreen          : 'Vollbildmodus',
            highlightCriticalPath   : 'Highlight critical path',
            addNewTask              : 'Add new task',
            newTask                 : 'New Task',
            removeSelectedTasks     : 'Remove selected task(s)',
            indent                  : 'Herunterstufen',
            outdent                 : 'Heraufstufen',
            manageCalendars         : 'Manage calendars',
            saveChanges             : 'Save changes',
            language                : 'Sprache: ',
            selectLanguage          : 'Sprache wählen...',
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
        Gnt.locale.De.apply(classNames);
        this.callParent(arguments);
    }

});
