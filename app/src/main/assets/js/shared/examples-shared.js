/* global ExampleDefaults:true */
ExampleDefaults = {
    width     : 800,
    height    : 400,
    idCounter : 1
};

// load before logging starts to avoid logging...
if (!Ext.ClassManager.get('Sch.examples.DetailsPanel')) {
    Ext.Loader.loadScript('../lib/DetailsPanel.js');
}

(function () {

    var oldLoadScripts = Ext.Loader.loadScripts,
        oldRequest     = Ext.Ajax.request;

    Sch.loadedFiles = [];

    // override loadScripts to log LOD files
    Ext.Loader.loadScripts = function (params) {
        Sch.loadedFiles = Ext.Array.union(Sch.loadedFiles, params.url);

        return oldLoadScripts.apply(this, arguments);
    };

    // override Ajax request to log data loaded
    Ext.Ajax.request = function (options) {
        // cleanup url and add to files
        var url = options.url.split('?')[ 0 ];
        Sch.loadedFiles.push(url);

        return oldRequest.apply(this, arguments);
    };
}());

/* For testability purposes so that Siesta can record targets more easily, provide an id to all scheduler panels */
Ext.define('TimelineGridPanelOverride', {
    override    : 'Sch.panel.TimelineGridPanel',
    constructor : function (config) {
        config = config || {};

        config.id = config.id || (this.xtype + '-' + ExampleDefaults.idCounter++);

        this.callParent([ config ]);
    }
});

Ext.define('TimelineGridPanelOverride', {
    override : 'Sch.panel.TimelineTreePanel',

    constructor : function (config) {
        config = config || {};

        config.id = config.id || (this.xtype + '-' + ExampleDefaults.idCounter++);

        this.callParent([ config ]);
    }
});
/* EOF For testability purposes so that Siesta can record targets more easily, provide an id to all scheduler panels */


Ext.onReady(function () {
    if (window.location.href.match(/^file:\/\/\//)) {
        Ext.Msg.alert('ERROR: You must use a web server', 'You must run the examples in a web server (not using the file:/// protocol)');
    }
});