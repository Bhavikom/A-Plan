Ext.define('APlan.locale.Nl', {
    extend    : 'Sch.locale.Locale',
    requires  : 'Gnt.locale.Nl',
    singleton : true,

    l10n : {
        'APlan.Application' : {
            error        : 'Error',
            requestError : 'Request error'
        },

        'APlan.view.ControlHeader' : {
            previousTimespan      : 'Vorige periode',
            nextTimespan          : 'Volgende periode',
            collapseAll           : 'Alles invouwen',
            expandAll             : 'Alles uitvouwen',
            zoomOut               : 'Uitzoomen',
            zoomIn                : 'Inzoomen',
            zoomToFit             : 'Zoom passend',
            undo                  : 'Ontdoen',
            redo                  : 'Opnieuw',
            viewFullScreen        : 'Volledig scherm',
            highlightCriticalPath : 'Licht kritiek pad op',
            addNewTask            : 'Voeg nieuwe taak toe',
            newTask               : 'Nieuwe taak',
            removeSelectedTasks   : 'Verwijder geselecteerde taken',
            indent                : 'Inspringen',
            outdent               : 'Uitspringen',
            manageCalendars       : 'Beheer kalenders',
            saveChanges           : 'Opslaan',
            language              : 'Taal: ',
            selectLanguage        : 'Selecteer een taal ...',
            tryMore               : 'Probeer meer features...',
            print                 : 'Print'
        },

        'APlan.plugin.TaskContextMenu' : {
            changeTaskColor : 'Wijzig taak-kleur'
        },

        'APlan.view.GanttSecondaryToolbar' : {
            toggleChildTasksGrouping      : 'Zet subtaken groepering aan/uit',
            toggleRollupTasks             : 'Zet rollup taken aan/uit',
            highlightTasksLonger8         : 'Accentueer taken langer dan 8 dagen',
            filterTasksWithProgressLess30 : 'Filter: taken met voortgang < 30%',
            clearFilter                   : 'Verwijder Filter',
            scrollToLastTask              : 'Scroll naar laatste taak'
        }
    },

    apply : function (classNames) {
        Gnt.locale.Nl.apply(classNames);
        this.callParent(arguments);
    }

});
