/* globals Prism */

/**
 * Prism: Lightweight, robust, elegant syntax highlighting
 * MIT license http://www.opensource.org/licenses/mit-license.php/
 * @author Lea Verou http://lea.verou.me
 */

// supply details (as html) when creating
Ext.define('Sch.examples.DetailsPanel', {
    extend        : 'Ext.panel.Panel',
    xtype         : 'details',
    width         : 400,
    title         : 'Details',
    collapsible   : true,
    collapsed     : true,
    titleCollapse : true,
    bodyBorder    : true,
    bodyPadding   : 15,
    layout        : 'card',
    region        : 'east',
    split         : true,
    border        : false,
    scrollable    : true,
    tpl           : '<pre data-src="{src}">{content}</pre>',

    // Ignore any Ext JS files loaded on demand
    filterRe      : /extjs-/,

    /**
     * Use Ext.Loader, Ext.Ajax calls & script tags to determine which files to include in the details panel.
     */
    hookLoader : true,

    /**
     * Adds an option to open a directory to view files. Specifiy path.
     */
    showSrcDir : null,

    /**
     * App.js path
     */
    appJSPath: 'app.js',

    /**
     * Custom source to show for specified urls. Stores responseText for ajax requests.
     */
    urlDataMap: {},

    initComponent : function (config) {
        var me = this;

        me.html = me.details;

        if (!me.files) me.files = [];

        if (me.showSrcDir) {
            me.files.push('Source directory');
        }

        if (me.hookLoader) {
            // always add app.js
            me.files.push(me.appJSPath);

            // remove LOD files from directories below
            var filteredFiles = Ext.Array.filter(Sch.loadedFiles, function (file) {
                return (file.indexOf('..') == -1);
            });

            // add LOD files to files
            me.files = Ext.Array.union(me.files, filteredFiles.sort());
        }

        Ext.Ajax.on('requestcomplete', me.onAjaxRequest, me);

        Ext.apply(me, {
            tbar : [
                'Select source:',
                {
                    xtype     : 'combo',
                    value     : 'Details',
                    width     : 250,
                    editable  : false,
                    store     : [ 'Details' ].concat(me.files),
                    listeners : {
                        select : this.onFileSelected,
                        scope  : this
                    },
                    tpl       : Ext.create('Ext.XTemplate',
                        '<ul class="x-list-plain"><tpl for=".">',
                        '<li role="option" class="x-boundlist-item detail-panel-file {[this.getGlyph(values)]}">{field1}</li>',
                        '</tpl></ul>',

                        {
                            getGlyph : function (values) {
                                var path = values.field1;

                                switch (true) {
                                    case /^Details$/.test(path):
                                        return 'x-fa fa-question';

                                    case /app\.js/.test(path):
                                        return 'x-fa fa-file-text-o';

                                    case /xml$/.test(path):
                                    case /data\//.test(path):
                                        return 'x-fa fa-table';

                                    case /\/controller\//.test(path):
                                        return 'x-fa fa-bolt';

                                    case /\/view\//.test(path):
                                        return 'x-fa fa-desktop';

                                    case /\/store\//.test(path):
                                    case /crud\//.test(path):
                                        return 'x-fa fa-database';

                                    case /\/model\//.test(path):
                                        return 'x-fa fa-cube';

                                    case /\/plugin\//.test(path):
                                        return 'x-fa fa-puzzle-piece';

                                    default:
                                        return 'x-fa fa-file-o';
                                }
                            }
                        }
                    )
                }
            ]
        });

        this.addCls('details-panel');

        me.callParent();

        this.store = this.down('combo').store;

        var filterRe = this.filterRe;

        this.store.filterBy(function(rec) { return !filterRe.test(rec.get('field1')); });
    },

    onAjaxRequest : function (owner, response, options) {
        var me = this,
            store = me.store,
            url   = options.url.split('?')[ 0 ];

        // store latest responseText for ajax requests (if not static xml)
        if (!/xml$/.test(url)) me.urlDataMap[url] = response.responseText;

        if (store.findExact('field1', url) < 0) {
            store.add({ field1 : url });
        }
    },

    onFileSelected : function (combo, r) {
        var me = this,
            url = r.data.field1,
            storedData = me.urlDataMap[url];
        if (url == 'Details') {
            me.update(this.initialConfig.details);
        } else if (url == 'Source directory') {
            document.location = me.showSrcDir;
        } else if (Ext.isIE) { // Prism is unstable in IE8-11
            if (storedData) {
                me.update(storedData);
            } else {
                me.loadFileContents(url);
            }
        } else {
            if (storedData) {
                me.update({ content: '<code>' + Prism.highlight(storedData, Prism.languages.json) + '</code>' });
            } else {
                me.update({ src : url });
                Prism.fileHighlight();
            }
        }
    },

    loadFileContents : function (url) {
        Ext.Ajax.request({
            url     : url,
            success : function (response) {
                // handle displaying of XML files
                var content = Ext.String.htmlEncode(response.responseText);
                
                this.update({ content : content });
            },
            scope   : this
        });
    }
}, function () {
    if (!window.Prism && !Ext.isIE) {
        Ext.Loader.loadScript('../lib/prism.js');
    }
});