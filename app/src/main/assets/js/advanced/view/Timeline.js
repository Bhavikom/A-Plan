Ext.define('APlan.view.Timeline', {
    extend   : 'Gnt.panel.Timeline',
    xtype    : 'advanced-timeline',
    requires : ['APlan.view.ControlHeader'],

    //split  : true,
    border : false,

    header   : {
        xtype : 'controlheader'
    }
});